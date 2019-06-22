package eyihcn.common.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Sets;

import eyihcn.common.core.dto.UserInfo;
import eyihcn.common.core.page.IPageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 
 * <p>
 * Description: 基类（id,创建人，创建时间，更新人，更新时间）。 转json时，过滤为null的字段
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月3日下午5:31:15
 * @param <PK> 主键类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@FieldNameConstants
public abstract class BaseEntity<PK extends Serializable> implements Serializable, IPageQuery {

	private static final long serialVersionUID = 6200553196589222335L;

	public abstract PK getId();

	public abstract void setId(PK id);

	/**
	 * 创建者
	 */
	public static final String CREATE_USER = "create_user";
	@ApiModelProperty(value = "创建人")
	private Long createUser;
	/**
	 * 创建时间
	 */
	public static final String CREATE_TIME = "create_time";
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 更新人
	 */
	public static final String UPDATE_USER = "update_user";
	@ApiModelProperty(value = "更新人")
	private Long updateUser;
	/**
	 * 更新时间
	 */
	public static final String UPDATE_TIME = "update_time";
	@ApiModelProperty(value = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * Is new boolean.
	 *
	 * @return the boolean
	 */
	@JsonIgnore
	public boolean isNew() {
		return this.getId() == null;
	}

	/**
	 * Sets update info.
	 *
	 * @param user the user
	 */
	@JsonIgnore
	public void setUpdateInfo(UserInfo user) {

		if (isNew()) {
			this.createUser = user.getId();
			this.createTime = new Date();
		} else {
			this.updateUser = user.getId();
			this.updateTime = new Date();
		}
	}

	@ApiModelProperty(value = "页码")
	@TableField(exist = false)
	private Long pageNo;

	@ApiModelProperty(value = "页大小")
	@TableField(exist = false)
	private Long pageSize;

	@ApiModelProperty(value = "按字段升序")
	@TableField(exist = false)
	private String orderByAsc;

	@ApiModelProperty(value = "按字段降序")
	@TableField(exist = false)
	private String orderByDesc;

	@Override
	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public Long getPageNo() {
		return this.pageNo;
	}

	@Override
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public Long getPageSize() {
		return this.pageSize;
	}

	/**
	 * 公共字段名
	 */
	public static final Set<String> commonFieldNames = Sets.newHashSet();

	static {
		commonFieldNames.add(BaseEntity.Fields.createTime);
		commonFieldNames.add(BaseEntity.Fields.createUser);
		commonFieldNames.add(BaseEntity.Fields.updateTime);
		commonFieldNames.add(BaseEntity.Fields.updateUser);
	}

}
