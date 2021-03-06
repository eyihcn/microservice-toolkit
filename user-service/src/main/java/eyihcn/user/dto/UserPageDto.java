package eyihcn.user.dto;

import eyihcn.common.core.page.PageQueryParam;
import eyihcn.common.core.web.QueryKey;
import eyihcn.common.core.web.QueryKey.Operator;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月31日下午12:50:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserPageDto extends PageQueryParam {

	private String name;

	@QueryKey(operator = Operator.RIGHT_LIKE)
	private String email;

	@QueryKey(operator = Operator.GE)
	private Integer age;
}
