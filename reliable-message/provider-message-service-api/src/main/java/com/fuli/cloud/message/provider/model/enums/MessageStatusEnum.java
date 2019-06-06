package com.fuli.cloud.message.provider.model.enums;

/**
 * @Description: 消息状态枚举类
 * @Author: chenyi
 * @CreateDate: 2019/4/16 18:44
 */
public enum MessageStatusEnum {

    /**
     * 状态码为10 ，待确认
     */
    WAITING_CONFIRM(10, "待确认"),
    /**
     * 状态码为20 ，发送中
     */
    SENDING(20, "发送中");

    /**
     * 描述
     */
    private String desc;
    /**
     * 消息状态码
     */
    private int code;

    MessageStatusEnum(int code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}