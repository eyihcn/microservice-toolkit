package eyihcn.user.test;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import eyihcn.common.core.lock.DistributedLockAnotation;


@Component
public class DistributedLockManager {

	@DistributedLockAnotation(argNum = 1, lockNamePost = ".lock")
	public Integer aspect(String lockName, Worker1 worker1) {
		return worker1.aspectBusiness(lockName);
	}

	@DistributedLockAnotation(lockName = "lock", lockNamePost = ".lock")
	public Integer aspect(Supplier<Integer> supplier) {
		return supplier.get();
	}

	@DistributedLockAnotation(lockName = "lock", lockNamePost = ".lock", waitTime = 1, leaseTime = 5, tryLock = true)
	public Integer aspectTryLock(Supplier<Integer> supplier) {
		return supplier.get();
	}

}
