package eyihcn.common.core.page;

import java.util.Collection;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@ApiModel("分页结果数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class PageBean<T> {

	@ApiModelProperty("总记录数")
	private long total;

	@ApiModelProperty("总页数")
	private long totalPage;

	@ApiModelProperty("当前页码")
	private long currentPage;

	@ApiModelProperty("每页显示条数")
	private long pageSize;

	@ApiModelProperty("当前页数据列表")
	private Collection<T> records;

	public static <E> PageBean<E> newPageBean(IPage<E> page) {
		PageBean<E> pageBean = new PageBean<E>();
		pageBean.setCurrentPage(page.getCurrent());
		pageBean.setTotalPage(page.getPages());
		pageBean.setPageSize(page.getSize());
		pageBean.setTotal(page.getTotal());
		pageBean.setRecords(page.getRecords());
		return pageBean;
	}

	public static <E> PageBean<E> newPageBean(IPage<?> page, Collection<E> col) {
		PageBean<E> pageBean = new PageBean<E>();
		pageBean.setCurrentPage(page.getCurrent());
		pageBean.setTotalPage(page.getPages());
		pageBean.setPageSize(page.getSize());
		pageBean.setTotal(page.getTotal());
		pageBean.setRecords(col);
		return pageBean;
	}
}
