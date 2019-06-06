package com.fuli.cloud.message.provider.model.enums;
/**
 * 生产消息类别枚举
 * @author chenyi
 */
public enum ProduceTypeEnum {

	/**业务主动方先预存储消息，消息状态为[WAITING_CONFIRM]，业务处理完成后，再修改消息状态为[SENDING]，并且将消息投递到MQ*/
	WAITING_AND_CONFIRM,
	/**业务主动方直接存储消息，消息状态为SENDING，并且将消息投递到MQ。<br/>注：无业务处理的情况可以使用该类型*/
	SAVE_AND_SEND
}
