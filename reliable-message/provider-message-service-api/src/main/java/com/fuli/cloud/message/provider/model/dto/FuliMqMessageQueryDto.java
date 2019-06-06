package com.fuli.cloud.message.provider.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuliMqMessageQueryDto {

	/** 默认一页数据1000条 */
	@Builder.Default
	private int pageSize = 1000;

	/** 默认页码 */
	@Builder.Default
	private int pageNo = 1;

	// Greater and Euquel CteatedTime
	private Date geCteatedTime;
	/** 消费队列名称 */
	private String consumerQueue;
	/** 消息状态 {10:待确认, 20:发送中} */
	private Integer messageStatus;
	
	/**
     * 是否死亡 0 - 活着; 1 - 死亡
     */
	private Integer dead;
}
