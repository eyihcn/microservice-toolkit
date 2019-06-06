package com.fuli.cloud.app.queue.autoconfigure;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fuli.cloud.message.provider.model.enums.RabbitMqQueue;


/**
 * 注册消息队列到RabbitMQ
 * @Date: 2019/4/18 11:30
 * @Author: chenyi
 */
@Configuration
public class RabbitMqQueueConfig {

	/**
	 * SaaS公告队列
	 */
    @Bean
	public Queue SEND_SAAS_ANNOUNCEMENT_QUEUE() {
		return new Queue(RabbitMqQueue.SEND_SAAS_ANNOUNCEMENT_QUEUE.name());
    }
	
    /**
	 * DEMO_QUEUE
	 */
    @Bean
	public Queue demoQueue() {
		return new Queue(RabbitMqQueue.DEMO_QUEUE.name());
    }

	/**
	 * 友盟广播推送队列
	 * @return
	 */
	@Bean
	public Queue UPSH_BROADCAST_MSSAGE_QUEUE() {
		return new Queue(RabbitMqQueue.UPSH_BROADCAST_MSSAGE_QUEUE.getQueueName());
    }
    /**
	 * 友盟自定义播推送队列
	 * @return
	 */
	@Bean
	public Queue UPSH_CUSTOMIZEDCAST_MSSAGE_QUEUE() {
		return new Queue(RabbitMqQueue.UPSH_CUSTOMIZEDCAST_MSSAGE_QUEUE.getQueueName());
    }
	
}
