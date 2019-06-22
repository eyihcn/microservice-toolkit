package com.fuli.cloud.message.provider.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fuli.cloud.message.provider.aspect.ConsumerAroundHandlerAspect;
import com.fuli.cloud.message.provider.aspect.ProducerAroundHandlerAspect;
import com.fuli.cloud.message.provider.util.ReliableMessageManager;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
public class MessageBeanConfig {

    @Bean
    public ProducerAroundHandlerAspect mqProducerStoreAspect() {
    	log.debug("创建切面 ：ProducerAroundHandlerAspect");
        return new ProducerAroundHandlerAspect();
    }


    @Bean
    public ConsumerAroundHandlerAspect mqConsumerStoreAspect() {
    	log.debug("创建切面 ：ConsumerAroundHandlerAspect");
        return new ConsumerAroundHandlerAspect();
    }

	@Bean
	public ReliableMessageManager reliableMessageManager() {
		log.debug("创建切面 ：ReliableMessageManager");
		return new ReliableMessageManager();
	}
}
