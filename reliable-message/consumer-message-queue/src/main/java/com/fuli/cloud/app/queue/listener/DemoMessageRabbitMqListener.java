package com.fuli.cloud.app.queue.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fuli.cloud.app.queue.service.biz.DemoQueueBiz;
import com.fuli.cloud.message.provider.annotation.MessageConsumerAroundHandler;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.util.MessageBizResultUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 添加监听队列的Mq队列消息的处理逻辑 业务处理的逻辑可以封装到单独的com.fuli.cloud.app.queue.service.biz包中
 * 
 * @Date: 2019/4/18 15:18
 * @Author: chenyi
 */
@Component
@Slf4j
public class DemoMessageRabbitMqListener {

	/**
	 * 使用注解@MessageConsumerAroundHandler，方法第一个参数必须为FuliMqMessage,
	 * 必须在设置业务的执行结果:MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS)
	 * 否则会注解切面会保存
	 */
	// RabbitMqQueue.DEMO_QUEUE
	@MessageConsumerAroundHandler
	@RabbitListener(queues = "DEMO_QUEUE")
	public void handlerPojoQueueDemo(FuliMqMessage message) {
		log.debug("《====== DEMO_QUEUE 的监听器收到了消息，消息id为[{}] ，消息体为[{}]", message.getId(), message.getMessageBody());
		// 处理demo业务
		new DemoQueueBiz().hanldeDemoBiz(message);
		try {
			log.debug(" ===模拟消费端业务延时50毫秒===");
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 强制必填 ，业务处理成功
		MessageBizResultUtils.setConsumeResult(ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS);
	}
}
