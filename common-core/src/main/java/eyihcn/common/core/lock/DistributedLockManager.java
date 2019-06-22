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
	public <T> T aspectTryLock(String lockName, Supplier<T> supplier) {
		return supplier.get();
	}

	@DistributedLockAnotation(argNum = 1, leaseTime = 5)
	public <T> T lock(String lockName, Supplier<T> supplier) {
		return supplier.get();
	}
}
