package eyihcn.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import eyihcn.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity<Long> {

	private static final long serialVersionUID = -5129231609959684274L;

	@TableId(type = IdType.AUTO)
	private Long id;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 姓名
	 */
	public static final String NAME = "name";
	private String name;

	/**
	 * 年龄
	 */
	public static final String AGE = "age";
	private Integer age;
	/**
	 * 邮箱
	 */
	public static final String EMAIL = "email";
	private String email;

}
