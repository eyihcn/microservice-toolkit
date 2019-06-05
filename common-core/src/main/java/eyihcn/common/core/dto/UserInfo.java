package eyihcn.common.core.dto;

import lombok.Data;

/**
 * 登录用户信息
 */
@Data
public class UserInfo {

	private Long id = 0L;

	/**
	 * 前台用户ID
	 */
	private long userId = 0;

	/**
	 * 后台用户ID
	 */
	private int adminId = 0;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 手机号码
	 */
	private String phone;
}
