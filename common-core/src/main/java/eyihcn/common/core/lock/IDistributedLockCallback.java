package eyihcn.common.core.lock;

/**
 * <p>
 * Description: 分布式锁回调接口
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午6:50:21
 * @param <T>
 */
public interface IDistributedLockCallback<T> {
	/**
	 * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
	 * @return
	 */
	public T process();

	/**
	 * 得到分布式锁名称
	 * @return
	 */
	public String getLockName();
}
