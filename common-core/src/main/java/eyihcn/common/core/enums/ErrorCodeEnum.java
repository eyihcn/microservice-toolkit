package eyihcn.common.core.enums;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 12:52
 */
public enum ErrorCodeEnum {

	ERROR(-1L, "操作失败"),

	/** 页面已过期,请重新登录 */
	USER_10011041(10011041L, "页面已过期,请重新登录");

	private Long code;
	private String msg;

	/**
	 * Msg string.
	 *
	 * @return the string
	 */
	public String msg() {
		return msg;
	}

	/**
	 * Code int.
	 *
	 * @return the int
	 */
	public Long code() {
		return code;
	}

	ErrorCodeEnum(Long code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * Gets enum.
	 *
	 * @param code the code
	 * @return the enum
	 */
	public static ErrorCodeEnum getEnum(Long code) {
		for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
			if (ele.code().longValue() == code.longValue()) {
				return ele;
			}
		}
		return null;
	}
}
