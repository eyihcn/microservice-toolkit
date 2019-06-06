package com.fuli.cloud.app.message.task;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fuli.cloud.app.message.service.scheduled.MessageScheduled;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/19 17:42
 */
@Slf4j
@Component
public class MessageScheduledTask {
	
	@Resource
	MessageScheduled settScheduled;
	
	@Async("messageScheduledTaskPool")
	@Scheduled(fixedDelay  = 120000)
	public void handleWaitingConfirmTimeOutMessages() {
		long start = System.currentTimeMillis();
		log.debug("===>WaitingConfirm 调度任务开始处理 超时的待确认消息 ");
		settScheduled.handleWaitingConfirmTimeOutMessages();
		log.debug("===>WaitingConfirm 调度任务完成超时的待确认消息处理 ,消耗{}ms",(System.currentTimeMillis()-start));
	}

	@Async("messageScheduledTaskPool")
	@Scheduled(fixedDelay = 120000)
	public void handleSendingTimeOutMessage() {
		long start = System.currentTimeMillis();
		log.debug("===>Sending 调度任务开始处理 超时的已确认消息");
		settScheduled.handleSendingTimeOutMessage();
		log.debug("===>Sending 调度任务完成超时的已确认消息处理 ,消耗{}ms",(System.currentTimeMillis()-start));
	}
	
}
