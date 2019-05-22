package eyihcn.storage.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("storage_tbl")
public class Storage {

	@TableId(value = "id", type = IdType.AUTO)
	public static final String ID = "id";
	private Long id;

	public static final String COMMODITY_CODE = "commodity_code";
	private String commodityCode;

	public static final String COUNT = "count";
	private Integer count;

}
