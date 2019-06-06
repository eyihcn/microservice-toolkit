package com.fuli.cloud.app.message.biz.handler.impl;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fuli.cloud.app.message.biz.handler.WaitingConfirmMessageHandler;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 目前还没找到更好的办法，将查询主动方业务结果和处理待确认消息绑定，暂时先通过统一命名bean来绑定
 * 强制：统一命名为 [队列名 ] + MessageTaskHanlder 
 * @author chenyi
 */
@Component("DEMO_QUEUE_MessageTaskHanlder")
@Slf4j
public class DemoQueueMessageHanlder implements WaitingConfirmMessageHandler {

	AtomicInteger count = new AtomicInteger(1);
	
	@Resource
	private FuliMqMessageFeignApi fuliMqMessageFeignApi ;
	
	@Override
	public void handleWaitingConfirmTimeOutMessage(FuliMqMessage fuliMqMessage) {
		log.debug("查询DEMO_QUEUE主动方的业务结果，如果也务处理成功，则将消息修改为SEDNING状态。 如果业务处理失败，则删除消息");
		
		int localCount = count.getAndIncrement();
		// 默认里偶数业务成功，奇数业务失败
		if (localCount % 2 == 0) {
			log.debug("模拟主动方demo业务处理，线程休眠5秒");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug("模拟主动方demo业务成功，开始确认并发送消息，消息id={}" ,fuliMqMessage.getId());
			fuliMqMessageFeignApi.confirmAndSendMessage(fuliMqMessage.getId());
			log.debug("脚本确认并发送消息成功，消息id={}" ,fuliMqMessage.getId());
		}else {
			log.debug("模拟主动方demo业务处理，线程休眠5秒");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug("模拟主动方业务失败，开始删除消息 ，消息id={}" ,fuliMqMessage.getId());
			fuliMqMessageFeignApi.deleteMessageByMessageId(fuliMqMessage.getId());
			log.debug("脚本成功删除主动方业务失败的消息，消息id={}" ,fuliMqMessage.getId());
		}
		
	}

}
