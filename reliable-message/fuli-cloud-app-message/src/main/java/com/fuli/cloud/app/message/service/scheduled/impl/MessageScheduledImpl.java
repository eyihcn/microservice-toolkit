package com.fuli.cloud.app.message.service.scheduled.impl;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fuli.cloud.app.message.biz.handler.WaitingConfirmMessageHandler;
import com.fuli.cloud.app.message.service.scheduled.MessageScheduled;
import com.fuli.cloud.app.message.service.utli.SpringContext;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.dto.FuliMqMessageQueryDto;
import com.fuli.cloud.message.provider.model.dto.FuliMqMessageQueryDto.FuliMqMessageQueryDtoBuilder;
import com.fuli.cloud.message.provider.model.enums.MessageStatusEnum;
import com.fuli.cloud.message.provider.model.enums.PublicEnum;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;
import com.google.common.base.Preconditions;

import eyihcn.common.core.constants.CommonConstant;
import eyihcn.common.core.model.Response;
import eyihcn.common.core.page.PageBean;
import eyihcn.common.core.utils.MyBeanUtil;
import eyihcn.common.core.utils.PublicUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 消息定时器接口实现
 * @Author: chenyi
 * @CreateDate: 2019/4/19 17:43
 */
@Slf4j
@Component
public class MessageScheduledImpl implements MessageScheduled {

	private static final String Message_Task_Hanlder = "MessageTaskHanlder";

	public static final Map<Integer, Integer> resendTimsToDurationMap;
	static {
		// key：次数 ，value：对应时间间隔（min）
		resendTimsToDurationMap = new HashMap<Integer, Integer>();
		// 第一次重发与上次重发的间隔时间（min）
		// TODO 确定重发间隔时间
		resendTimsToDurationMap.put(1, 1);
		resendTimsToDurationMap.put(2, 4);
		resendTimsToDurationMap.put(3, 8);
		resendTimsToDurationMap.put(4, 16);
		resendTimsToDurationMap.put(5, 32);
	}

	@Resource
	private FuliMqMessageFeignApi rpMqMessageFeignApi;

	@Override
	public void handleWaitingConfirmTimeOutMessages() {
		handleByMessageStatus(MessageStatusEnum.WAITING_CONFIRM);
	}

	@Override
	public void handleSendingTimeOutMessage() {
		handleByMessageStatus(MessageStatusEnum.SENDING);
	}

	@SuppressWarnings("unchecked")
	private void handleByMessageStatus(MessageStatusEnum messageStatusEnum) {

		Preconditions.checkNotNull(messageStatusEnum);
		// 获取配置的开始处理的时间
		// TODO 可配置
		Date date = getCreateTimeBefore();
		// 查询某个时间范围内待确认的消息
		FuliMqMessageQueryDtoBuilder builder = FuliMqMessageQueryDto.builder().geCteatedTime(date)
				.messageStatus(messageStatusEnum.getCode()).dead(PublicEnum.NO.getCode()).pageNo(1)
				.pageSize(CommonConstant.BATCH_HANDLE_PAGE_SIZE);
		FuliMqMessageQueryDto rpMqMessageQueryDto = builder.build();
		Response<?> Response = rpMqMessageFeignApi.getMessagePage(rpMqMessageQueryDto);

		if (!PublicUtil.isFeignApiSuccess(Response)) {
			return;
		}
//		JacksonUtil.dumnToPrettyJson(Response);
		// ========200 请求成功==============
		Map<String, Object> pageBeanMap = (Map<String, Object>) Response.getData();
		long totalPageSize = (Long.valueOf(pageBeanMap.get(PageBean.Fields.totalPage).toString()));
		List<FuliMqMessage> msgList = null;
		for (int pageCount = 1; pageCount <= totalPageSize;) {

			List<Map<String, Object>> propertiesList = ((List<Map<String, Object>>) pageBeanMap
					.get(PageBean.Fields.records));
			if (PublicUtil.isEmpty(propertiesList)) {
				log.info("records is empty");
				continue;
			}
			msgList = MyBeanUtil.mapToEntity(FuliMqMessage.class, propertiesList);
			if (messageStatusEnum == MessageStatusEnum.WAITING_CONFIRM) {
				handleWaitingConfirmList(msgList);
			} else {
				handleSendingList(msgList);
			}
			// 查询下一页
			pageCount++;
			FuliMqMessageQueryDto localDto = builder.pageNo(pageCount).pageSize(CommonConstant.BATCH_HANDLE_PAGE_SIZE)
					.build();
			Response<?> localResponse = null;
			try {
				localResponse = rpMqMessageFeignApi.getMessagePage(localDto);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("分页查询消息接口出现异常，查询参数: " + localDto + ", 异常信息 ：" + e.getMessage());
				// TODO 是否考虑记录异常操作到DB
				continue;
			}
			if (null != localResponse) {
				pageBeanMap = (Map<String, Object>) localResponse.getData();
			}
		}
	}

	private void handleWaitingConfirmList(List<FuliMqMessage> records) {
		for (FuliMqMessage fuliMqMessage : records) {

			// 消息刚刚生产，还未超时（默认10s），基本没有10s的业务，如果超过绝壁有问题，规定超时时间为10s，保险
			// 避免业务还未处理完成，而脚本抢先捞取了待确认的消息，查询错误的主动方业务结果，误删了消息
			// 如果主动方业务处理成功， 在确认消息时，由于极端意外而失败，可以通过脚本查询主动方业务，确认消息
			if (!hasTimeOut(fuliMqMessage)) {
				log.debug("消息超时阈值还未达到，不执行脚本确认主动方业务结果的逻辑，避免在主动方之前执行确认消息的逻辑。消息id为[{}]", fuliMqMessage.getId());
				continue;
			}
			;
			log.debug("开始处理[WAITING_CONFIRM]消息ID为[{}]的消息", fuliMqMessage.getId());
			String consumerQueue = fuliMqMessage.getConsumerQueue();
			if (StringUtils.isBlank(consumerQueue)) {
				log.error("可靠消息的消息队列为空， 消息id为 {} ", fuliMqMessage.getId());
				continue;
			}
			WaitingConfirmMessageHandler messageHanlder = null;
			try {
				messageHanlder = SpringContext.getBean(consumerQueue + "_" + Message_Task_Hanlder);
			} catch (Exception NoSuchBeanDefinitionException) {
				log.error("从Spring容器中没有get到处理队列名[{}]的 FuliMqMessageHandler的实例 ，get参数为： {}", consumerQueue,
						(fuliMqMessage.getConsumerQueue() + "_" + Message_Task_Hanlder));
				continue;
			}
			try {
				messageHanlder.handleWaitingConfirmTimeOutMessage(fuliMqMessage);
			} catch (Exception e) {
				log.error("脚本处理待确认的消息出现异常，消息id为[{}], 消息队列名称为[{}], 异常信息： {}\r\n", fuliMqMessage.getId(), consumerQueue,
						e);
				e.printStackTrace();
			}
		}
	}

	/** 比对当前系统时间和消息出生时间，差值是否超过10s */
	private boolean hasTimeOut(FuliMqMessage fuliMqMessage) {
		Date createdTime = fuliMqMessage.getCreatedTime();
		if (null == createdTime) {
			return false;
		}
		return System.currentTimeMillis() - createdTime.getTime() > CommonConstant.MESSAGE_TIMEOUT_DURATION;
	}

	/**
	 * 检查重发次数，重发次数弱没有超过边界值，则重发到MQ<br/>
	 * 否则，标记为死亡消息
	 */
	private void handleSendingList(List<FuliMqMessage> msgList) {

		for (FuliMqMessage fuliMqMessage : msgList) {
			log.debug("开始处理, 消息状态为[SENDING]，消息ID为[{}], 已经重新发送的次数[{}]", fuliMqMessage.getId(),
					fuliMqMessage.getResendTimes());
			try {
				// 判断发送次数,如果超过最大发送次数直接退出
				if (fuliMqMessage.getResendTimes() >= CommonConstant.MASSGE_MAX_RESEND_TIMES) {
					// 标记为死亡
					this.rpMqMessageFeignApi.setMessageToAlreadyDeadById(fuliMqMessage.getId());
					log.debug("成功将消息标记为死亡消息，消息ID为[{}]，消息状态为[SENDING], 消息队列名称：[{}]", fuliMqMessage.getId(),
							fuliMqMessage.getConsumerQueue());
					continue;
				}
			} catch (Exception e) {
				log.error("脚本中将消息标记为死亡消息出现异常，消息id为[{}], 消息队列名称：[{}], 异常信息： \r\n", fuliMqMessage.getId(),
						fuliMqMessage.getConsumerQueue(), e);
			}
			// 判断是否达到发送消息的时间间隔条件
			int reSendTimes = fuliMqMessage.getResendTimes();
			int durationMin = resendTimsToDurationMap.get(reSendTimes == 0 ? 1 : reSendTimes);
			long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
			Date updateTime = fuliMqMessage.getUpdateTime();
			if (updateTime != null) {
				long editTime = updateTime.getTime();
				// 判断是否达到了可以再次发送的时间条件
				if ((durationMin * 60 * 1000 + editTime) > currentTimeInMillis) {
					log.debug("currentTime[{}],[SENDING]消息上次发送时间[{}], 必须过了[{}]分钟才可以再发送。消息ID为[{}]，消息队列名称：[{}]",
							LocalDateTime.now(), fuliMqMessage.getUpdateTime(), durationMin, fuliMqMessage.getId(),
							fuliMqMessage.getConsumerQueue());
					continue;
				}
			}
			try {
				// 重新发送消息
				this.rpMqMessageFeignApi.resendMessageById(fuliMqMessage.getId());
				log.debug("成功重发消息，消息ID为[{}],消息队列名称：[{}]，消息状态为[SENDING]", fuliMqMessage.getId(),
						fuliMqMessage.getConsumerQueue());
			} catch (Exception e) {
				log.error("脚本重发消息出现异常，消息id为[{}], 消息队列名称：[{}], 异常信息：{} \r\n", fuliMqMessage.getId(),
						fuliMqMessage.getConsumerQueue(), e);
			}
		}
	}

	/**
	 * 获取前三天的
	 * 
	 * @return
	 */
	private Date getCreateTimeBefore() {
		long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
		return new Date(currentTimeInMillis - CommonConstant.THREE_DAY_MILLIS);
	}
}
