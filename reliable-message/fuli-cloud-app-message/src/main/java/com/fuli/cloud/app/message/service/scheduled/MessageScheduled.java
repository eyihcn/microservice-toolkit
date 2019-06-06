package com.fuli.cloud.app.message.service.scheduled;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/19 17:44
 */
public interface MessageScheduled {

    /**
     * 处理状态为“待确认”但已超时的消息.业务处理超时时间阈值 CommonConstant.MESSAGE_TIMEOUT_DURATION
     */
    public void handleWaitingConfirmTimeOutMessages();

    /**
     * 处理状态为“发送中”但超时没有被成功消费确认的消息
     */
    public void handleSendingTimeOutMessage();

}
