package com.fuli.cloud.message.provider.model.enums;

/**
 * @Description: 公共枚举类
 * @Author: chenyi
 * @CreateDate: 2019/4/16 20:53
 */
public enum PublicEnum {
    /**
     * {yes:1}
     */
    YES(1),
    /**
     * {no:0}
     */
    NO(0);


    private int code;

    PublicEnum() {
    }

    PublicEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
