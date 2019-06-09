package eyihcn.common.core.beancopy;

import java.io.Serializable;

import eyihcn.common.core.model.BaseEntity;

/**
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午5:16:49
 */
public interface ISaveDtoCopier {

	<T extends BaseEntity<? extends Serializable>> T copyProperties(Object e, Class<T> entityClass);
}
