package eyihcn.common.core.enums;

/**
 * @Author: chenyi
 * @CreateDate: 2019/4/16 12:52
 */
public enum ErrorCodeEnum {

	ERROR(-1L, "操作失败"),

	/** 页面已过期,请重新登录 */
	USER_10011041(10011041L, "页面已过期,请重新登录"),

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
	SYSTEM_SERVER_20200060002(20200060002L, "%s不存在,删除失败"),

	/**
	 * 消息的消费队列不能为空
	 */
	MESSAGE_SDK10050001(10050001L, "消息的消费队列不能为空"),

	/**
	 * 数据库中消息数据保存失败
	 */
	MESSAGE_SDK10050002(10050002L, "数据库中消息数据保存失败,messageId=%s"),

	/**
	 * 数据库中消息数据删除失败
	 */
	MESSAGE_SDK10050003(10050003L, "数据库中消息数据删除失败,messageId=%s"),

	/**
	 * 目标接口参数不能为空
	 */
	MESSAGE_SDK10050005(10050005L, "目标接口参数不能为空"),
	/**
	 * 根据消息id查找的消息为空
	 */
	MESSAGE_SDK10050006(10050006L, "根据消息id查找的消息为空,messageId=%s"),

	/**
	 * 消息数据不能为空
	 */
	MESSAGE_SDK10050007(10050007L, "消息数据不能为空"),

	/**
	 * 消息体不能为空
	 */
	MESSAGE_SDK10050008(10050008L, "消息体不能为空,messageId=%s"),

	/**
	 * 消息ID不能为空
	 */
	MESSAGE_SDK10050009(10050009L, "消息ID不能为空"),

	/**
	 * 参数异常
	 */
	MESSAGE_SDK10050010(10050010L, "参数异常"),

	/** 注解使用错误，方法的第一个参数必须为FuLiMqMessage类型 */
	MESSAGE_SDK10050011(10050011L, "注解使用错误，方法[%s]的第一个参数必须为FuLiMqMessage类型"),

	/**
	 * 消息中心接口异常
	 */
	MESSAGE_SDK10050004(10050004L, "消息中心接口异常,message=%s, messageId=%s"),

	/**
	 * 微服务不在线,或者网络超时
	 */
	MESSAGE_SDK99990002(99990002L, "微服务不在线,或者网络超时"),

	;

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
