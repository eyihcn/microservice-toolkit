package com.fuli.cloud.app.queue.service.biz;

import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoQueueBiz {

	public void hanldeDemoBiz(FuliMqMessage fuliMqMessage) {
		log.debug("被动方，处理DemoBiz业务的逻辑 。" );
	}
}
