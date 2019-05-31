package eyihcn.common.core.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public abstract class BaseEntity<PK extends Serializable> implements Serializable {

	private static final long serialVersionUID = 6200553196589222335L;

	public abstract PK getId();

	public abstract void setId(PK id);

	/**
	 * 创建者
	 */
	public static final String CREATE_USER = "create_user";
	private Long createUser;
	/**
	 * 创建时间
	 */
	public static final String CREATE_TIME = "create_time";
	private Date createTime;
	/**
	 * 更新人
	 */
	public static final String UPDATE_USER = "update_user";
	private Long updateUser;
	/**
	 * 更新时间
	 */
	public static final String UPDATE_TIME = "update_time";
	private Date updateTime;
}
