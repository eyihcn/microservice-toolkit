package com.fuli.cloud.message.provider.service.hystrix;

import org.springframework.stereotype.Component;

import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.dto.FuliMqMessageQueryDto;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;

import eyihcn.common.core.model.Response;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FuliMqMessageFeignApiHystrix implements FallbackFactory<FuliMqMessageFeignApi> {

	@Override
	public FuliMqMessageFeignApi create(Throwable cause) {

		log.info("==== FuliMqMessageFeignApiHystrix====  ，fallback reason was: {} ", cause.getMessage());

		// TODO Auto-generated method stub
		return new FuliMqMessageFeignApi() {

			@Override
			public Response<?> setMessageToAlreadyDeadById(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> saveMessageWaitingConfirm(FuliMqMessage fuliMqMessage) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> saveAndSendMessage(FuliMqMessage fuliMqMessage) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> resendMessageById(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> reSendAllDeadMessageByQueueName(String queueName) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> getMessagePage(FuliMqMessageQueryDto fuliMqMessageQueryDto) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> getMessageByMessageId(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> deleteMessageByMessageId(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> confirmAndSendMessage(String id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Response<?> directSendMessage(FuliMqMessage fuliMqMessage) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}

}
