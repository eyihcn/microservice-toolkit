package com.fuli.cloud.message.provider.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;

import com.fuli.cloud.message.provider.exceptions.FuliMqMessageBizException;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ConsumeConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;
import com.fuli.cloud.message.provider.util.MessageBizResultUtils;

import eyihcn.common.core.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 消息
 * @Author: chenyi
 * @CreateDate: 2019/4/16 16:28
 */
@Slf4j
@Aspect
@Order(20)
public class ConsumerAroundHandlerAspect {

	@Resource
	private FuliMqMessageFeignApi fuliMqMessageFeignApi;

	@Resource
	private TaskExecutor taskExecutor;

	/**
	 * Add exe time annotation pointcut.
	 */
	@Pointcut("@annotation(com.fuli.cloud.message.provider.annotation.MessageConsumerAroundHandler)")
	public void mqConsumerStoreAnnotationPointcut() {

	}

	/**
	 * Add exe time method object.
	 *
	 * @param joinPoint the join point
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around(value = "mqConsumerStoreAnnotationPointcut()")
	public void processMqConsumerStoreJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

		long startTime = System.currentTimeMillis();
		log.debug("processMqConsumerStoreJoinPoint - 线程id={}", Thread.currentThread().getId());
		Object[] args = joinPoint.getArgs();
//		FuliMqMessageConsumerAroundHandler annotation = getAnnotation(joinPoint);
		String methodName = joinPoint.getSignature().getName();
		if (args == null || args.length == 0) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050005);
		}
		if (!(args[0] instanceof FuliMqMessage)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050011, methodName);
		}
		FuliMqMessage fuliMqMessage = null;
		try {
			fuliMqMessage = (FuliMqMessage) args[0];
		} catch (Exception e) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050011);
		}
		final String messageId = fuliMqMessage.getId();
		// 业务执行结果
		joinPoint.proceed();
		ConsumeConcurrentlyStatusEnum consumeResult = MessageBizResultUtils.getConsumeResult();
		log.info("消费者，目标方法={}, 业务执行结果, result={}", methodName, consumeResult);
		try {
			if (ConsumeConcurrentlyStatusEnum.CONSUME_SUCCESS == consumeResult) {
				// 消息成功消费，删除消息
				// TODO 逻辑删除
				log.info("被动方业务执行成功，消费者切面开始异步删除消息，消息id为[{}]", messageId);
				taskExecutor.execute(() -> fuliMqMessageFeignApi.deleteMessageByMessageId(messageId));
			}
		} catch (Exception e) {
			log.error("消费可靠消息出现异常, 目标方法[{}], 异常={},总耗时={}ms", methodName, e.getMessage(),
					System.currentTimeMillis() - startTime);
			throw e;
		} finally {
			MessageBizResultUtils.clearConsumeResult();
			log.info("消费可靠消息 目标方法[{}], 总耗时={}ms", methodName, System.currentTimeMillis() - startTime);
		}
	}

}
