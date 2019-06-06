package com.fuli.cloud.message.provider.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 15:57
 */
@Data
@TableName("mq_message")
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class FuliMqMessage implements Serializable {
    private static final long serialVersionUID = -5951754367474682967L;
    /**
     * 消息主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 投递的队列
     */
    private String consumerQueue;

    /**
     * 生产者PID
     */
//    private String producerGroup;

    /**
     * 延时级别 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
//    private Integer delayLevel;

    /**
     * 消息状态{10:待确认, 20:发送中}
     */
    private Integer messageStatus;

    /**
     * 消息内容
     */
    private String messageBody;

    /**
     * 重发次数
     */
    private int resendTimes;

    /**
     * 是否死亡 0 - 活着; 1 - 死亡
     */
    private int dead;

    /**
     * 是否删除 0 未删除; 1 已删除
     */
    @TableLogic
	private int deleted;


    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    //===========预留字段==========

    private String field1;

    private String field2;

    private String field3;
    
    @Transient
    public void addResendTimes() {
        this.resendTimes++;
    }
}