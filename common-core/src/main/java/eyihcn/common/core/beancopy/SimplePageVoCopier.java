package eyihcn.common.core.beancopy;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import eyihcn.common.core.model.BaseEntity;

/**
 * <p>
 * Description: 简单的拷贝属性
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午5:17:11
 */
@Component
public class SimplePageVoCopier implements IPageVoCopier {

	@Override
	public <T extends BaseEntity<? extends Serializable>, EntityVo> Collection<EntityVo> convert(Collection<T> col,
			Class<EntityVo> entityVoClass) {
		Collection<EntityVo> voCol = Lists.newArrayListWithCapacity(col.size());
		col.forEach(o -> {
			EntityVo vo = null;
			try {
				vo = entityVoClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				throw new RuntimeException(e1);
			}
			BeanUtils.copyProperties(o, vo);
			voCol.add(vo);
		});
		return voCol;
	}

}
