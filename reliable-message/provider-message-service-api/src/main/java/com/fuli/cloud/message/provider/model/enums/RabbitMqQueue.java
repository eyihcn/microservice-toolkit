package com.fuli.cloud.message.provider.model.enums;

/**
 * @author chenyi
 */
public enum RabbitMqQueue {

	DEMO_QUEUE("DEMO_QUEUE"),
	//友盟广播推送队列名称
	UPSH_BROADCAST_MSSAGE_QUEUE("UPUSH_BROADCAST_MSSAGE_QUEUE"),
	//友盟自定义播推送队列名称
	UPSH_CUSTOMIZEDCAST_MSSAGE_QUEUE("UPUSH_CUSTOMIZEDCAST_MSSAGE_QUEUE"), 
	/**saas公告队列*/
	SEND_SAAS_ANNOUNCEMENT_QUEUE("SEND_SAAS_ANNOUNCEMENT_QUEUE");

	private String queueName ;

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	private RabbitMqQueue(String queueName) {
		this.queueName = queueName;
	}
}
