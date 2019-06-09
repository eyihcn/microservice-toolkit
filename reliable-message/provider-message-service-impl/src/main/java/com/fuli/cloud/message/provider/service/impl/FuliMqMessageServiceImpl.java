package com.fuli.cloud.message.provider.service.impl;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuli.cloud.message.provider.exceptions.FuliMqMessageBizException;
import com.fuli.cloud.message.provider.mapper.FuliMqMessageMapper;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.MessageStatusEnum;
import com.fuli.cloud.message.provider.model.enums.PublicEnum;
import com.fuli.cloud.message.provider.service.FuliMqMessageService;

import eyihcn.common.core.constant.CommonConstant;
import eyihcn.common.core.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 15:44
 */
@Service
@Slf4j
public class FuliMqMessageServiceImpl extends ServiceImpl<FuliMqMessageMapper, FuliMqMessage>
		implements FuliMqMessageService {

	@Resource
	private FuliMqMessageMapper fuliMqMessageMapper;

	@Resource
	private AmqpTemplate rabbitTemplate;

	private void checkMessage(FuliMqMessage mqMessageData) {
		if (null == mqMessageData) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050007);
		}
		String consumerQueue = mqMessageData.getConsumerQueue();
		String messageBody = mqMessageData.getMessageBody();
		String id = mqMessageData.getId();
//        String producerGroup = mqMessageData.getProducerGroup();
		if (null == id) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050009);
		}
		if (StringUtils.isEmpty(consumerQueue)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050001);
		}
		if (StringUtils.isEmpty(messageBody)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050008, id);
		}
//        if (StringUtils.isEmpty(producerGroup)) {
//            throw new MessageSdkBizException(ErrorCodeEnum.MESSAGE_SDK10050004, mqMessageData.getMessageId());
//        }
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveMessageWaitingConfirm(final FuliMqMessage fuliMqMessage) throws FuliMqMessageBizException {
		// 1. 参数检查
		checkMessage(fuliMqMessage);
		// 2. 设置相关初始值
		fuliMqMessage.setCreatedTime(new Date());
		fuliMqMessage.setMessageStatus(MessageStatusEnum.WAITING_CONFIRM.getCode());
		fuliMqMessage.setResendTimes(0);
		fuliMqMessage.setDead(PublicEnum.NO.getCode());

		if (!save(fuliMqMessage)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050002, fuliMqMessage.getId());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage confirmAndSendMessage(String id) throws FuliMqMessageBizException {

		if (StringUtils.isEmpty(id)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050009);
		}
		final FuliMqMessage message = getById(id);
		checkMessage(message);
		// 设置为消息的状态为“发送中”
		if (!fuliMqMessageMapper.updateMessageStatusById(id, MessageStatusEnum.SENDING.getCode(), new Date())) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050002, message.getId());
		}
		return message;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage saveAndSendMessage(final FuliMqMessage fuliMqMessage) throws FuliMqMessageBizException {
		checkMessage(fuliMqMessage);

		fuliMqMessage.setCreatedTime(new Date());
		fuliMqMessage.setMessageStatus(MessageStatusEnum.SENDING.getCode());
		fuliMqMessage.setResendTimes(0);
		if (!save(fuliMqMessage)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050002, fuliMqMessage.getId());
		}
		return fuliMqMessage;
	}

	@Override
	public void directSendMessage(final FuliMqMessage message) throws FuliMqMessageBizException {
		checkMessage(message);
		try {
			rabbitTemplate.convertAndSend(message.getConsumerQueue(), message);
		} catch (Exception e) {
			log.error("直连MQ出现异常,消息ID={}，消息队列={},异常信息={}", message.getId(), message.getConsumerQueue(), e);
			// 事务需要回滚
			throw new RuntimeException(e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage resendMessage(final FuliMqMessage message) throws FuliMqMessageBizException {
		return resendMessage(message, false);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage resendMessage(final FuliMqMessage message, boolean ignoreResendTimes)
			throws FuliMqMessageBizException {

		checkMessage(message);

		if (!ignoreResendTimes) {
			// 检查是否已经超过最多重复次数
			if (message.getResendTimes() >= CommonConstant.MASSGE_MAX_RESEND_TIMES) {
				setMessageToAlreadyDead(message);
				return message;
			}
		}

		message.addResendTimes();
		message.setUpdateTime(new Date());
		if (!updateById(message)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050002, message.getId());
		}
		try {
			rabbitTemplate.convertAndSend(message.getConsumerQueue(), message);
		} catch (Exception e) {
			log.error("根据消息ID重发消息，DB操作成功，单个重发消息到MQ出现异常,消息ID={}，消息队列={},异常信息={}", message.getId(),
					message.getConsumerQueue(), e);
			// 事务需要回滚
			throw new RuntimeException(e);
		}
		return message;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage resendMessageById(final String id) throws FuliMqMessageBizException {

		return resendMessage(this.getById(id));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage setMessageToAlreadyDead(final FuliMqMessage message) throws FuliMqMessageBizException {

		checkMessage(message);
		// 1 : 死亡
		message.setDead(PublicEnum.YES.getCode());
		message.setUpdateTime(new Date());
		if (!updateById(message)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050002, message.getId());
		}
		return message;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public FuliMqMessage setMessageToAlreadyDead(final String id) throws FuliMqMessageBizException {

		return setMessageToAlreadyDead(getById(id));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
}
