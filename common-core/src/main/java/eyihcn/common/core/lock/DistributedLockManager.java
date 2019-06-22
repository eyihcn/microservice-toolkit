package eyihcn.common.core.lock;

import java.util.function.Supplier;

/**
 * 
 * <p>
 * Description: 分布式锁管理器
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月22日下午3:40:37
 */
public class DistributedLockManager {

	@DistributedLockAnotation(argNum = 1, waitTime = 1, leaseTime = 5, tryLock = true)
	public <T> boolean aspectTryLock(String lockName, Supplier<T> supplier) {
		// 切面执行时，偷换了 supplier.get()的返回值
		return (boolean) supplier.get();
	}

	@DistributedLockAnotation(argNum = 1, waitTime = 1, leaseTime = 5, tryLock = true)
	public void aspectTryLock(String lockName, Action action) {
		action.action();
	}
}
