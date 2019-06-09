package eyihcn.common.core.beancopy;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import eyihcn.common.core.model.BaseEntity;

/**
 * 
 * <p>
 * Description: 简单拷贝属性
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午5:17:54
 */
@Component
public class SimpleSaveDtoCopier implements ISaveDtoCopier {

	@Override
	public <T extends BaseEntity<? extends Serializable>> T copyProperties(Object e, Class<T> entityClass) {

		T entity = null;
		try {
			entity = entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new RuntimeException(e1);
		}
		BeanUtils.copyProperties(e, entity);
		return entity;
	}

}
