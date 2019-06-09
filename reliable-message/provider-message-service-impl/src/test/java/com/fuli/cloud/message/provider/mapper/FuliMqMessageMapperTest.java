package com.fuli.cloud.message.provider.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuli.cloud.FuliCloudMessageProviderApplication;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.MessageStatusEnum;
import com.fuli.cloud.message.provider.model.enums.RabbitMqQueue;

import eyihcn.common.core.sequence.UniqueIdWorker;
import eyihcn.common.core.utils.QWrapper;

/**
 * @Date: 2019/4/17 19:06
 * @Author: chenyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuliCloudMessageProviderApplication.class)
public class FuliMqMessageMapperTest {

	@Autowired
	FuliMqMessageMapper fuliMqMessageMapper;

	@Test
	public void testDelete() {
		fuliMqMessageMapper.deleteById("1123088749670023170");
	}

	@Test
	public void testPage() {

		IPage<FuliMqMessage> page = new Page<FuliMqMessage>(1, 1);
		QWrapper<FuliMqMessage> queryWrapper = new QWrapper<FuliMqMessage>();
		queryWrapper.eq(FuliMqMessage.Fields.messageStatus, MessageStatusEnum.SENDING.getCode());
		Integer selectCount = fuliMqMessageMapper.selectCount(queryWrapper);
		System.out.println("fuliMqMessageMapper.selectCount : " + selectCount);
		IPage<FuliMqMessage> selectPage = fuliMqMessageMapper.selectPage(page, null);
		System.out.println(page == selectPage);
		System.out.println("selectPage.getTotal() : " + page.getTotal());
		System.out.println("selectPage.getPages() : " + page.getPages());
		System.out.println("selectPage.getCurrent() : " + page.getCurrent());
		System.out.println("selectPage.page.getRecords().size() : " + page.getRecords().size());
	}

	@Test
	public void testSave2() {
		FuliMqMessage fuliMqMessage = new FuliMqMessage();
		String s = UniqueIdWorker.getIdStr();
		System.out.println(s);
		fuliMqMessage.setId(s);
		fuliMqMessage.setMessageBody("messageBody");
		fuliMqMessage.setConsumerQueue(RabbitMqQueue.DEMO_QUEUE.name());
		fuliMqMessage.setMessageStatus(MessageStatusEnum.WAITING_CONFIRM.getCode());
		fuliMqMessage.setCreatedTime(new Date());
		fuliMqMessageMapper.insert(fuliMqMessage);

	}

	@Test
	public void testSave() {
		FuliMqMessage fuliMqMessage = new FuliMqMessage();
		fuliMqMessage.setMessageBody("messageBody");
		fuliMqMessage.setConsumerQueue("Queue-A");
		fuliMqMessage.setMessageStatus(MessageStatusEnum.WAITING_CONFIRM.getCode());
		fuliMqMessage.setCreatedTime(new Date());
		fuliMqMessageMapper.insert(fuliMqMessage);

	}
}
