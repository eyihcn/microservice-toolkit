package com.fuli.cloud.message.provider.aspect;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;

import com.fuli.cloud.message.provider.annotation.MessageProducerAroundHandler;
import com.fuli.cloud.message.provider.exceptions.FuliMqMessageBizException;
import com.fuli.cloud.message.provider.model.domain.FuliMqMessage;
import com.fuli.cloud.message.provider.model.enums.ProduceConcurrentlyStatusEnum;
import com.fuli.cloud.message.provider.model.enums.ProduceTypeEnum;
import com.fuli.cloud.message.provider.service.feign.FuliMqMessageFeignApi;

import eyihcn.common.core.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/22 10:02
 */
@Slf4j
@Aspect
@Order(20)
public class ProducerAroundHandlerAspect {

	@Resource
	private FuliMqMessageFeignApi fuliMqMessageFeignApi;

	@Resource
	private TaskExecutor taskExecutor;

	/**
	 * Add exe time annotation pointcut.
	 */
	@Pointcut("@annotation(com.fuli.cloud.message.provider.annotation.MessageProducerAroundHandler)")
	public void mqProducerStoreAnnotationPointcut() {

	}

	/**
	 * Add exe time method object.
	 *
	 * @param joinPoint the join point
	 * @return
	 * @return the object
	 */
	@Around(value = "mqProducerStoreAnnotationPointcut()")
	public Object processMqProducerStoreJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

		long startTime = System.currentTimeMillis();
		log.debug("processMqProducerStoreJoinPoint - 线程id={}", Thread.currentThread().getId());
		Object[] args = joinPoint.getArgs();

		if (args.length == 0 || args[0] == null) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050005);
		}
		String methodName = joinPoint.getSignature().getName();
		if (!(args[0] instanceof FuliMqMessage)) {
			throw new FuliMqMessageBizException(ErrorCodeEnum.MESSAGE_SDK10050011, methodName);
		}
		final FuliMqMessage domain = (FuliMqMessage) (args[0]);
		ProduceTypeEnum produceType = getAnnotation(joinPoint).produceType();
		if (ProduceTypeEnum.SAVE_AND_SEND == produceType) {
			// 直接存储消息为SENDING,且投递到MQ
			fuliMqMessageFeignApi.saveAndSendMessage(domain);
			log.info("生产者切面执行完成, 目标方法[{}],总耗时={}ms", methodName, System.currentTimeMillis() - startTime);
			return joinPoint.proceed();
		}
		// WAITING_AND_CONFIRM发送类型， 处理业务之前，保存预发送消息
		fuliMqMessageFeignApi.saveMessageWaitingConfirm(domain);
		// 处理主动发业务
		Object proceed = joinPoint.proceed();
		if (!(proceed instanceof ProduceConcurrentlyStatusEnum)) {
			throw new RuntimeException("请返回生产消息的业务处理结果枚举类型 ProduceConcurrentlyStatusEnum");
		}
		ProduceConcurrentlyStatusEnum produceResult = (ProduceConcurrentlyStatusEnum) proceed;
		log.debug("生产者，目标方法={}, 业务执行结果, result={}", methodName, produceResult);
		try {
			if (ProduceConcurrentlyStatusEnum.BUSINESS_SUCCESS == produceResult) {
				log.info("主动方业务执行成功，生产切面开始异步确认并发送消息，消息id为[{}]", domain.getId());
				taskExecutor.execute(() -> fuliMqMessageFeignApi.confirmAndSendMessage(domain.getId()));
			} else {
				// 业务处理失败，删除消息
				log.info("主动方业务执行成功，生产切面开始异步删除待确认消息，消息id为[{}]", domain.getId());
				taskExecutor.execute(() -> fuliMqMessageFeignApi.deleteMessageByMessageId(domain.getId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("生产者切面执行完成, 目标方法[{}],总耗时={}ms", methodName, System.currentTimeMillis() - startTime);
		}
		return proceed;
	}

	private static MessageProducerAroundHandler getAnnotation(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		return method.getAnnotation(MessageProducerAroundHandler.class);
	}
}
