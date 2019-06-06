package com.fuli.cloud.message.provider.service.impl;

import static org.junit.Assert.fail;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fuli.cloud.FuliCloudMessageProviderApplication;
import com.fuli.cloud.message.provider.annotation.MessageProducerAroundHandler;
import com.fuli.cloud.message.provider.aspect.ProducerAroundHandlerAspect;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.MessageStatusEnum;
import com.fuli.cloud.message.provider.model.enums.ProduceConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.model.enums.RabbitMqQueue;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;
import com.fuli.cloud.message.provider.util.MessageBizResultUtils;

import eyihcn.common.core.com.sequence.UniqueIdWorker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuliCloudMessageProviderApplication.class)
public class FuliMqMessageFeignApiTest {

	@Resource
	FuliMqMessageFeignApi feignApi;

	@Resource
	ProducerAroundHandlerAspect producerAroundHandlerAspect;

	@MessageProducerAroundHandler
	public void produceMessageByAnnotaion(FuliMqMessage fuliMqMessage) {

		System.out.println("《==================================================");
		try {
			Thread.sleep(80);
			System.out.println(" -----模拟主动方业务延时10s。。。。业务成功！");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("《==================================================");
		MessageBizResultUtils.setProduceResult(ProduceConcurrentlyStatusEnum.BUSINESS_SUCCESS);
	}

	@Test
	public void testProduceAnnotaion() {
		System.out.println(producerAroundHandlerAspect);
		int count = 1;
		while (true) {
			count++;
			produceMessageByAnnotaion(newMessage(count));
			if (count == 5) {
				break;
			}
		}
	}

	@Test
	public void testProduceMessage() {
		int count = 1;
		while (true) {
			FuliMqMessage fuliMqMessage = newMessage(count);
			feignApi.saveMessageWaitingConfirm(fuliMqMessage);
			System.out.println("《==================================================");
			try {
				Thread.sleep(80);
				System.out.println(" -----模拟主动方业务延时10s。。。。业务成功！");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("《==================================================");
			feignApi.confirmAndSendMessage(fuliMqMessage.getId());
			count++;
			System.out.println("++++++++++++++++++" + count + "++++++++++++++++");
		}
	}

	@Test
	public void testPage() {
	}

	@Test
	public void testSaveMessageWaitingConfirm() {
		int count = 1;
		while (true) {
			FuliMqMessage fuliMqMessage = newMessage(count);
			feignApi.saveMessageWaitingConfirm(fuliMqMessage);
			count++;
			System.out.println("++++++++++++++++++" + count + "++++++++++++++++");
			if (count == 4) {
				break;
			}
		}
	}

	private FuliMqMessage newMessage(int count) {
		FuliMqMessage fuliMqMessage = new FuliMqMessage();
		fuliMqMessage.setId(UniqueIdWorker.getIdStr());
		fuliMqMessage.setMessageBody("messageBody" + count);
		fuliMqMessage.setConsumerQueue(RabbitMqQueue.DEMO_QUEUE.name());
		fuliMqMessage.setMessageStatus(MessageStatusEnum.WAITING_CONFIRM.getCode());
		fuliMqMessage.setCreatedTime(new Date());
		return fuliMqMessage;
	}

	@Test
	public void testConfirmAndSendMessage() {
	}

	@Test
	public void testSaveAndSendMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testDirectSendMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testResendMessageFuliMqMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testResendMessageFuliMqMessageBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testResendMessageList() {
		fail("Not yet implemented");
	}

	@Test
	public void testResendMessageById() {
		fail("Not yet implemented");
	}

	@Test
	public void testReSendAllDeadMessageByQueueName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMessageToAlreadyDeadFuliMqMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMessageToAlreadyDeadString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpushMessage() {
	}

}
