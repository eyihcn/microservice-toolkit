package eyihcn.common.core.model;

import eyihcn.common.core.enums.ErrorCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author chenyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Response<T> {

	@ApiModelProperty(name = "data", value = "响应数据")
	private T data;

	@ApiModelProperty(name = "code", value = "响应编码")
	private Long code;

	@ApiModelProperty(name = "msg", value = "响应消息")
	private String msg;

	private static final Long OK_CODE = 0L;

	public boolean checkIsOk() {
		return this.code == null ? false : this.code.longValue() == OK_CODE.longValue();
	}

	/**
	 * 操作成功，返回默认响应 code 0
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> Response<T> ok() {
		return new Response(null, OK_CODE, "操作成功");
	}

	public static <T> Response<T> ok(T data) {
		return new Response(data, OK_CODE, "操作成功");
	}

	public static <T> Response<T> okWithMsg(String msg) {
		return new Response(null, OK_CODE, msg);
	}

	public static <T> Response<T> ok(T data, String msg) {
		return new Response(data, OK_CODE, msg);
	}

	/**
	 * 操作失败，返回默认响应 code -1
	 *
	 * @param <T>
	 * @return
	 */
	public static <T> Response<T> failed() {
		return new Response(null, ErrorCodeEnum.ERROR.code(), ErrorCodeEnum.ERROR.msg());
	}

	public static <T> Response<T> failed(Long code, String msg) {
		return new Response(null, code, msg);
	}

	public static <T> Response<T> failed(T Data, Long code, String msg) {
		return new Response(Data, code, msg);
	}

	public static <T> Response<T> failed(ErrorCodeEnum errorCodeEnum) {
		return new Response(null, errorCodeEnum.code(), errorCodeEnum.msg());
	}

	public static <T> Response<T> failed(T Data, ErrorCodeEnum errorCodeEnum) {
		return new Response(Data, errorCodeEnum.code(), errorCodeEnum.msg());
	}

	public static <T> Response<T> failed(T Data) {
		return new Response(Data, ErrorCodeEnum.ERROR.code(), ErrorCodeEnum.ERROR.msg());
	}

	public static <T> Response<T> failedWithMsg(String msg) {
		return new Response(null, ErrorCodeEnum.ERROR.code(), msg);
	}
}
