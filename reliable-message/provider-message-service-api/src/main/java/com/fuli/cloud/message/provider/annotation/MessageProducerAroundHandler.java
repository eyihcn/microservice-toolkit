package com.fuli.cloud.message.provider.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fuli.cloud.message.provider.model.enums.DelayLevelEnum;
import com.fuli.cloud.message.provider.model.enums.ProduceTypeEnum;


/**
 * 主动方生产消息.方法的第一个参数必须为FuliMqMessage类型
 * @author chenyi
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MessageProducerAroundHandler {
	
	ProduceTypeEnum produceType() default ProduceTypeEnum.WAITING_AND_CONFIRM;

    DelayLevelEnum delayLevel() default DelayLevelEnum.ZERO;
}
