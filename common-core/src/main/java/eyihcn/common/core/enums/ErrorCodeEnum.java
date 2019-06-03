package eyihcn.common.core.enums;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 12:52
 */
public enum ErrorCodeEnum {

	ERROR(-1L, "操作失败"),

	/** 页面已过期,请重新登录 */
	USER_10011041(10011041L, "页面已过期,请重新登录"),

	// ===========================system-server 开始================================
	/** 参数输入不合法！ */
	GLOBAL_6000(6000L, "参数输入不合法！"),
	/** 系统忙，请稍后再试或联系管理员 */
	GLOBAL_6001(6001L, "系统忙，请稍后再试或联系管理员"),
	/** 缺少参数异常 */
	GLOBAL_6003(6003L, "缺少参数异常"),
	/** 参数类型转换异常 */
	GLOBAL_6004(6004L, "参数类型转换异常"),
	/** 参数Id不能包含负数 */
	GLOBAL_6005(6005L, "参数Id不能包含负数"),
	/** 请求方式不支持 */
	GLOBAL_6006(6006L, "请求方式不支持"),

	/** 内部异常 */
	SYSTEM_SERVER_10200060002(10200060002L, "系统忙，请稍后再试或联系管理员"),
	/** 删除失败，%s不存在 */
	SYSTEM_SERVER_20200060002(20200060002L, "%s不存在,删除失败"),;

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
