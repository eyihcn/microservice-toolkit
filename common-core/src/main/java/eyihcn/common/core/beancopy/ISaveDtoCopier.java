package eyihcn.common.core.beancopy;

import java.io.Serializable;

import eyihcn.common.core.model.BaseEntity;

public interface ISaveDtoCopier {

	<T extends BaseEntity<? extends Serializable>> T copyProperties(Object e, Class<T> entityClass);
}
