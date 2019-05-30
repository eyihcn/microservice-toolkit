package eyihcn.common.core.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public abstract class BaseEntity<PK extends Serializable> implements Serializable {

	private static final long serialVersionUID = 6200553196589222335L;

	public abstract PK getId();

	public abstract void setId(PK id);

	private Long createUser;
	private Date createTime;
	private Long updateUser;
	private Date updateTime;
}
