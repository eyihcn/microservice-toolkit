package eyihcn.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_tbl")
public class Account implements Serializable {

	private static final long serialVersionUID = -6702376057861569975L;
	public static final String ID = "id";
	@TableId(type = IdType.AUTO)
	private Long id;

	public static final String USER_ID = "user_id";
	private String userId;

	public static final String MONEY = "money";
	private BigDecimal money;

}
