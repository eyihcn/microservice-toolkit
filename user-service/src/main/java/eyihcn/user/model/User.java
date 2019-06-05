package eyihcn.user.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import eyihcn.common.core.annotation.QueryKey;
import eyihcn.common.core.annotation.QueryKey.Operator;
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
	@QueryKey(operator = Operator.LIKE)
	private String name;

	/**
	 * 年龄
	 */
	public static final String AGE = "age";
	@QueryKey(operator = Operator.GE)
	private Integer age;
	/**
	 * 邮箱
	 */
	public static final String EMAIL = "email";
	private String email;

	@QueryKey(operator = Operator.GE)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}
