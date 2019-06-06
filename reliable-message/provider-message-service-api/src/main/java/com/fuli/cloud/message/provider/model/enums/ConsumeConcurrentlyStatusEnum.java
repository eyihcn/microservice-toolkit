package com.fuli.cloud.message.provider.model.enums;
/**
 * @author chenyi
 */
public enum ConsumeConcurrentlyStatusEnum {
	/**
	 * 业务被动方成功消费
	 */
	CONSUME_SUCCESS,
	/**
	 * 业务被动方消费失败，之后脚本再次尝试消费
	 */
	RECONSUME_LATER;
}

