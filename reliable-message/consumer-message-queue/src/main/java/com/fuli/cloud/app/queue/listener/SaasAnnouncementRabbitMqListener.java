package com.fuli.cloud.app.queue.listener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fuli.cloud.app.queue.service.biz.GroupPushMessageFeignClient;
import com.fuli.cloud.app.queue.service.biz.SaaSUserCenterFeignClient;
import com.fuli.cloud.app.queue.service.biz.UserGroupSentMessageFeignClient;
import com.fuli.cloud.commons.Response;
import com.fuli.cloud.enums.PushStatus;
import com.fuli.cloud.message.provider.annotation.MessageConsumerAroundHandler;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.util.MessageBizResultUtils;
import com.fuli.cloud.model.system.GroupPushMessage;
import com.fuli.cloud.utils.MyBeanUtil;
import com.fuli.cloud.utils.PublicUtil;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * 添加监听队列的Mq队列消息的处理逻辑 业务处理的逻辑可以封装到单独的com.fuli.cloud.app.queue.service.biz包中
 * 
 * @Date: 2019/4/18 15:18
 * @Author: chenyi
 */
@Component
@Slf4j
public class SaasAnnouncementRabbitMqListener {

	@Resource
	private GroupPushMessageFeignClient groupPushMessageFeignClient;
	@Resource
	private UserGroupSentMessageFeignClient userGroupSentMessageFeignClient;
	@Resource
	private SaaSUserCenterFeignClient saaSHttpClient;

	/**
	 * 使用注解@MessageConsumerAroundHandler，方法第一个参数必须为FuliMqMessage,
	 * 必须在设置业务的执行结果:MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS)
	 * 否则会注解切面会报错
	 */
	@SuppressWarnings("unchecked")
	@MessageConsumerAroundHandler
	@RabbitListener(queues = "SEND_SAAS_ANNOUNCEMENT_QUEUE")
	public void handlerSaasAnnouncementQueue(FuliMqMessage message) {
		
		try {
			log.debug("《====== SEND_SAAS_ANNOUNCEMENT_QUEUE 的监听器收到了消息，消息id为[{}] ，消息体为[{}]", message.getId(),
					message.getMessageBody());

			GroupPushMessage groupPushMessage = JSONObject.parseObject(message.getMessageBody(),
					GroupPushMessage.class);
			Response<?> result = null;
			boolean isSaveSuccess = true;
			try {
				// 1.先保存
				result = groupPushMessageFeignClient.createIfNotExsitByMessageId(groupPushMessage);
			} catch (Exception e) {
				e.printStackTrace();
				isSaveSuccess = false;
			}
			if (null == result || !isSaveSuccess) {
				groupPushMessage.setPushStatus(PushStatus.PUSH_FAIL.getCode());
				groupPushMessage.setFailMsg("后台运营SaaS服务调用失败！已重试" + message.getResendTimes() + "次 ");
				log.error("后台运营SaaS服务调用失败！已重试" + message.getResendTimes() + "次 ");
				groupPushMessageFeignClient.createOrUpdate(groupPushMessage);
				MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.RECONSUME_LATER);
				return;
			}
			if (!result.checkIsOk()) {
				// 内部接口错误,提示稍后会重试
				groupPushMessage.setPushStatus(PushStatus.PUSH_FAIL.getCode());
				groupPushMessage.setFailMsg(JSONObject.toJSONString(result));
				log.debug("其他错误，可靠消息服务会继续重试推送！,  推送返回结果result = {}", result);
				groupPushMessageFeignClient.createOrUpdate(groupPushMessage);
				MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.RECONSUME_LATER);
				return;
			}
			// 2. SaaS保存历史成功，创建SaaS消息中心的关联表
			boolean allSuccess = true;
			try {
				// TODO 查询SaaS所有用户的id
				groupPushMessage = MyBeanUtil.mapToEntity(GroupPushMessage.class,
						(Map<String, Object>) result.getData());
				log.debug("SaaS公告保存历史为推送中，创建SaaS消息中心的关联表, 公告历史id为{}，可靠消息Id为{}", groupPushMessage.getId(),
						message.getId());
				int currentPage = 1;
				int pageSize = 500;
				Response<?> userIdPage = null;
				try {
					userIdPage = saaSHttpClient.getUserIdPage(currentPage, pageSize);
				} catch (Exception e) {
					log.debug("调用SaaS用户接口，分页获取用户id，出现异常");
					e.printStackTrace();
					allSuccess = false;
				}
				if (PublicUtil.isFeignApiSuccess(userIdPage)) {

					Map<String, Object> data = (Map<String, Object>) userIdPage.getData();
					Integer totalPage = (Integer) data.get("totalPage");

					for (;;) {
						List<Map<String, Integer>> records = (List<Map<String, Integer>>) data.get("records");
						Set<Long> userIds = Sets.newHashSet();
						records.forEach(idMap -> {
							Integer id = idMap.get("id");
							userIds.add(Long.valueOf(id));
						});
						log.debug("调用SaaS用户接口，分页获取用户id，当前页码={}，当前页大小={}", currentPage, userIds.size());
						Response<?> exsitResult = userGroupSentMessageFeignClient.createIfNotExsit(userIds,
								groupPushMessage);
						if (!PublicUtil.isFeignApiSuccess(exsitResult)) {
							log.debug("\r\n调用SaaS用户接口分页获取用户id，成功返回。result:" + userIdPage);
							allSuccess = false;
						}
						currentPage++;
						if (currentPage > totalPage) {
							break;
						}
						try {
							userIdPage = saaSHttpClient.getUserIdPage(currentPage, pageSize);
						} catch (Exception e) {
							log.debug("调用SaaS用户接口，分页获取用户id，出现异常");
							e.printStackTrace();
							allSuccess = false;
						}
					}

				} else {
					allSuccess = false;
				}
			} catch (Exception e) {
				allSuccess = false;
				log.debug("SaaS系统消息服务调用出现异常");
				e.printStackTrace();
			}
			// SaaS的所有用户与公告关联成功才算成功
			if (allSuccess) {
				groupPushMessage.setPushStatus(PushStatus.PUSH_SUCCESS.getCode());
			} else {
				groupPushMessage.setPushStatus(PushStatus.PUSH_FAIL.getCode());
				groupPushMessage.setFailMsg("SaaS系统消息未推送全部推送成功！");
			}
			Response<?> uResult = groupPushMessageFeignClient.createOrUpdate(groupPushMessage);
			// 先根据可靠消息id查询，是否已经有历史记录了，若存在历史记录，则更新推送结果即可
			if (uResult.checkIsOk() && allSuccess) {
				log.info("====更新推送结果到推送历史成功====，可靠消息ID={}", message.getId());
				MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS);
			} else {
				MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.RECONSUME_LATER);
			}
		} catch (Exception e) {
			log.error("SaaS公告消息消费出现未知异常");
			e.printStackTrace();
			MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.RECONSUME_LATER);
		}
	}
}
