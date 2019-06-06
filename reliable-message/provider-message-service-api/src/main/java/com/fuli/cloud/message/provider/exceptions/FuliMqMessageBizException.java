package com.fuli.cloud.message.provider.exceptions;

import eyihcn.common.core.enums.ErrorCodeEnum;
import eyihcn.common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 消息业务异常
 * @Author: chenyi
 * @CreateDate: 2019/4/17 12:32
 */
@Slf4j
public class FuliMqMessageBizException extends BusinessException {

	private static final long serialVersionUID = -6552248511084911254L;

	public FuliMqMessageBizException(long code, String msgFormat, Object... args) {
		super(code, msgFormat, args);
		log.info("<== FuliMqMessageBizException, code:{}, message:{}", this.code, super.getMessage());
	}

	public FuliMqMessageBizException(long code, String msg) {
		super(code, msg);
		log.info("<== FuliMqMessageBizException, code:{}, message:{}", this.code, super.getMessage());
	}

	public FuliMqMessageBizException(ErrorCodeEnum codeEnum) {
		super(codeEnum.code(), codeEnum.msg());
		log.info("<== FuliMqMessageBizException, code:{}, message:{}", this.code, super.getMessage());
	}

	public FuliMqMessageBizException(ErrorCodeEnum codeEnum, Object... args) {
		super(codeEnum, args);
		log.info("<== FuliMqMessageBizException, code:{}, message:{}", this.code, super.getMessage());
	}
}
