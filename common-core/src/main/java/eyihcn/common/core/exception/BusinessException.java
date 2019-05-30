package eyihcn.common.core.exception;

import eyihcn.common.core.enums.ErrorCodeEnum;

/**
 * 业务异常.
 */
public class BusinessException extends RuntimeException {

	/**
	 * 异常码
	 */
	protected long code;

	private static final long serialVersionUID = 3160241586346324994L;

	public BusinessException() {
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
	}

	public BusinessException(ErrorCodeEnum codeEnum, Object... args) {
		super(String.format(codeEnum.msg(), args));
		this.code = codeEnum.code();
	}

	public long getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
