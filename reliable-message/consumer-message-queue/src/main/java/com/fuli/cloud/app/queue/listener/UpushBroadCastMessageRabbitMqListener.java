package com.fuli.cloud.app.queue.listener;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fuli.cloud.app.queue.service.biz.UpushMsgQueueBiz;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.message.provider.annotation.MessageConsumerAroundHandler;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;
import com.fuli.cloud.message.provider.util.MessageBizResultUtils;
import com.fuli.cloud.model.upush.PushVo;
import com.fuli.cloud.utils.JacksonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 添加监听友盟广播推送队列的Mq队列消息的处理逻辑
 *
 * @author: 易煌
 */
@Component
@Slf4j
public class UpushBroadCastMessageRabbitMqListener {

	@Autowired
	private UpushMsgQueueBiz upushMsgQueueBiz;

	@Resource
	private FuliMqMessageFeignApi fuliMqMessageFeignApi;

	@MessageConsumerAroundHandler
	@RabbitListener(queues = "UPUSH_BROADCAST_MSSAGE_QUEUE")
	public void handlerUpushBroadcastMssageQueue(FuliMqMessage message) {

		log.debug("《====== 友盟广播推送队列的Mq队列 的监听器收到了消息，消息id为[{}] ，消息体为[{}]", message.getId(), message.getMessageBody());
		PushVo pushVo = null;
		try {
			pushVo = JSONObject.parseObject(message.getMessageBody(), PushVo.class);
			Result<?> result = upushMsgQueueBiz.sendMsgBroadCast(pushVo);
			log.debug("===========友盟广播推送响应结果=====");
			JacksonUtil.dumnToPrettyJson(result);
			log.debug("===========友盟广播推送响应结果=====");
			MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.RECONSUME_LATER);
		}
	}

}
