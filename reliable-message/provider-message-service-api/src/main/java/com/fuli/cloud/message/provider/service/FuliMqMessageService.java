package com.fuli.cloud.message.provider.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.message.provider.exceptions.FuliMqMessageBizException;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;

/**
 * @Description: 描述
 * @Author: chenyi
 * @CreateDate: 2019/4/16 15:46
 */
public interface FuliMqMessageService extends IService<FuliMqMessage> {
    /**
	 * 预存储消息.
	 *
	 * @param fuliMqMessage 消息实体
	 * @throws FuliMqMessageBizException 消息业务异常
	 */
	void saveMessageWaitingConfirm(FuliMqMessage fuliMqMessage) throws FuliMqMessageBizException;


    /**
     * 确认并发送消息.
     *
     * @param id 消息Id
     * @throws FuliMqMessageBizException 消息业务异常
     */
	FuliMqMessage confirmAndSendMessage(String id) throws FuliMqMessageBizException;


    /**
	 * 存储并发送消息.
	 *
	 * @param fuliMqMessage 消息实体
     * @return 
	 * @throws FuliMqMessageBizException 消息业务异常
	 */
	FuliMqMessage saveAndSendMessage(FuliMqMessage fuliMqMessage) throws FuliMqMessageBizException;


    /**
	 * 直接发送消息.
	 *
	 * @param fuliMqMessage 消息实体
	 * @throws FuliMqMessageBizException 消息业务异常
	 */
	void directSendMessage(FuliMqMessage fuliMqMessage) throws FuliMqMessageBizException;


    /**
     * 重发消息.且检查当前重发次数是否已经超过最大重发次数，若超过，则将消息标记为死亡消息
     *
     * @param id 消息Id
     * @return 
     * @throws FuliMqMessageBizException 消息业务异常
     */
    FuliMqMessage resendMessageById(String id) throws FuliMqMessageBizException;

    /**
	 * 重发消息.重复之前，检查是否大于等于最大重发次数 若大于等于最大重发次数，则不重发消息
	 *
	 * @param fuliMqMessage the message entity
     * @return 
	 * @throws FuliMqMessageBizException 消息业务异常
	 */
	FuliMqMessage resendMessage(FuliMqMessage fuliMqMessage) throws FuliMqMessageBizException;

    /**
     * @param message           the message entity
     * @param ignoreResendTimes 是否检查超过最大重发次数
     * @return 
     * @throws FuliMqMessageBizException 消息业务异常
     */
    FuliMqMessage resendMessage(final FuliMqMessage message, boolean ignoreResendTimes) throws FuliMqMessageBizException;

    /**
     * 重发某个消息队列中的全部已死亡的消息.
     *
     * @param queueName 队列名
     * @param batchSize 一次批量多少数据
     * @throws FuliMqMessageBizException 消息业务异常
     */
//    void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws FuliMqMessageBizException;

    /**
     * 将消息标记为死亡消息.
     *
     * @param message 消息实体
     * @return 
     * @throws FuliMqMessageBizException 消息业务异常
     */
    FuliMqMessage setMessageToAlreadyDead(final FuliMqMessage message) throws FuliMqMessageBizException;

    /**
     * 将消息标记为死亡消息.
     *
     * @param id 消息Id
     * @return 
     * @throws FuliMqMessageBizException 消息业务异常
     */
    FuliMqMessage setMessageToAlreadyDead(String id) throws FuliMqMessageBizException;

}
