package com.fuli.cloud.message.provider.util;

import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.model.enums.ProduceConcurrentlyStatusEnum;

import eyihcn.common.core.utils.ThreadLocalMap;

/**
 * 通过过线程内的局部变量传递消息业务执行结果
 * 
 * @author chenyi
 */
public class MessageBizResultUtils {

	private static final String CONSUMRESULT_STRING = "CONSUMRESULT";
	private static final String PRODUCERESULT_STRING = "PRODUCERESULT";

	public static ConsumeConcurrentlyStatusEnum getConsumeResult() {
		return (ConsumeConcurrentlyStatusEnum) ThreadLocalMap.get(CONSUMRESULT_STRING);
	}

	public static ProduceConcurrentlyStatusEnum getProduceResult() {
		return (ProduceConcurrentlyStatusEnum) ThreadLocalMap.get(PRODUCERESULT_STRING);
	}

	public static void setConsumeResult(ConsumeConcurrentlyStatusEnum consumeResult) {
		ThreadLocalMap.put(CONSUMRESULT_STRING, consumeResult);
	}

	public static void setProduceResult(ProduceConcurrentlyStatusEnum produceResult) {
		ThreadLocalMap.put(PRODUCERESULT_STRING, produceResult);
	}

}
