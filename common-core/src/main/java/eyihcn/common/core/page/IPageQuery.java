/**
 * 
 */
package eyihcn.common.core.page;

/**
 * <p>
 * Description:分页入参
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月30日下午4:41:40
 */
public interface IPageQuery {

	/**
	 * 
	 * <p>
	 * Description: 设置当前页码
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月30日下午4:43:52
	 * @param pageNo
	 */
	void setPageNo(long pageNo);

	/**
	 * 
	 * <p>
	 * Description:获取当前页
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月30日下午4:45:19
	 * @return
	 */
	long getPageNo();

	/**
	 * 
	 * <p>
	 * Description: 设置页大小
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月30日下午4:45:34
	 * @param pageSize
	 */
	void setPageSize(long pageSize);

	/**
	 * 
	 * <p>
	 * Description: 获取页大小
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月30日下午4:45:58
	 * @return
	 */
	long getPageSize();
}
