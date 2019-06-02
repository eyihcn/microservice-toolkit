package eyihcn.common.core.beancopy;

import java.io.Serializable;
import java.util.Collection;

import eyihcn.common.core.model.BaseEntity;

public interface IPageVoCopier {

	<T extends BaseEntity<? extends Serializable>, EntityVo> Collection<EntityVo> convert(Collection<T> col,
			Class<EntityVo> entityVoClass);
}
