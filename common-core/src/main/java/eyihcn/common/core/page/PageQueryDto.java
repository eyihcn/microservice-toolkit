/**
 * 
 */
package eyihcn.common.core.page;

/**
 * <p>
 * Description:分页参数
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月30日下午4:47:58
 */

public class PageQueryDto implements PageQuery {

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

}
