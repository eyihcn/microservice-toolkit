package eyihcn.order.service.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_tbl")
public class Order {

	@TableId
	public static final String ID = "id";
	private Long id;

	public static final String USER_ID = "user_id";
	private String userId;

	public static final String COMMODITY_CODE = "commodity_code";
	private String commodityCode;

	public static final String MONEY = "money";
	private BigDecimal money;

	public static final String COUNT = "count";
	private Integer count;
}
