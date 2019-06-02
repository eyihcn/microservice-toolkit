/**
 * 
 */
package eyihcn.common.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * Description:分页参数
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月30日下午4:47:58
 */

public class PageQueryParam implements IPageQuery {

	private long pageNo;
	private long pageSize;

	@Override
	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public long getPageNo() {
		return this.pageNo;
	}

	@Override
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public long getPageSize() {
		return this.pageSize;
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
	public static <E> IPage<E> page(IPageQuery pageDto) {
		return new Page<E>(pageDto.getPageNo(), pageDto.getPageSize());
	}

}
