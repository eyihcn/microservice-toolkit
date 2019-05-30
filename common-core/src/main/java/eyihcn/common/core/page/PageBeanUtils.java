package eyihcn.common.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class PageBeanUtils {

	public static <E> PageBean<E> newPageBean(IPage<E> page) {
		PageBean<E> pageBean = new PageBean<E>();
		pageBean.setCurrentPage(page.getCurrent());
		pageBean.setTotalPage(page.getPages());
		pageBean.setPageSize(page.getSize());
		pageBean.setTotal(page.getTotal());
		pageBean.setRecords(page.getRecords());
		return pageBean;
	}

	/**
	 * <p>
	 * Description:创建mybatis-plus分页参数实例
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月30日下午4:59:21
	 * @param pageDto
	 * @return
	 */
	public static <E> IPage<E> page(PageQuery pageDto) {
		return new Page<E>(pageDto.getPageNo(), pageDto.getPageSize());
	}
}
