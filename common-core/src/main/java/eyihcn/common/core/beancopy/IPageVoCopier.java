package eyihcn.common.core.beancopy;

import java.io.Serializable;
import java.util.Collection;

import eyihcn.common.core.model.BaseEntity;

/**
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午5:15:40
 */
public interface IPageVoCopier {

	<T extends BaseEntity<? extends Serializable>, EntityVo> Collection<EntityVo> convert(Collection<T> col,
			Class<EntityVo> entityVoClass);
}
