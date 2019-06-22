package com.fuli.cloud.message.provider.util;

import java.util.function.Supplier;

import com.fuli.cloud.message.provider.annotation.MessageConsumerAroundHandler;
import com.fuli.cloud.message.provider.annotation.MessageProducerAroundHandler;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.model.enums.ProduceConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.model.enums.ProduceTypeEnum;

/**
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月22日下午6:00:42
 */
public class ReliableMessageManager {

	@MessageConsumerAroundHandler
	public ConsumeConcurrentlyStatusEnum consume(FuliMqMessage message,
			Supplier<ConsumeConcurrentlyStatusEnum> supplier) {
		return supplier.get();
	}

	@MessageProducerAroundHandler
	public ProduceConcurrentlyStatusEnum produce(FuliMqMessage message,
			Supplier<ProduceConcurrentlyStatusEnum> supplier) {
		return supplier.get();
	}

	@MessageProducerAroundHandler(produceType = ProduceTypeEnum.SAVE_AND_SEND)
	public ProduceConcurrentlyStatusEnum produce(FuliMqMessage message) {
		return ProduceConcurrentlyStatusEnum.BUSINESS_SUCCESS;
	}
}
