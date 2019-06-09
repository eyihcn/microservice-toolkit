package eyihcn.common.core.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <p>
 * Description: 分布式锁模板接口的简单实现（基于redisson）
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午7:00:05
 */
@Slf4j
public class RedissonDistributedLockTemplate implements IDistributedLockTemplate {

	private RedissonClient redisson;

	public RedissonDistributedLockTemplate() {
	}

	public RedissonDistributedLockTemplate(RedissonClient redisson) {
		this.redisson = redisson;
	}

	@Override
	public <T> T lock(IDistributedLockCallback<T> callback, boolean fairLock) {
		return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
	}

	@Override
	public <T> T lock(IDistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
		RLock lock = getLock(callback.getLockName(), fairLock);
		try {
			lock.lock(leaseTime, timeUnit);
			if (lock.isHeldByCurrentThread()) {
				log.debug(Thread.currentThread().getName() + " gets lock. lockName[" + callback.getLockName() + "]");
				T t = callback.process();
				return t;
			} else {
				log.debug(Thread.currentThread().getName() + " gets lock fialed. lockName[" + callback.getLockName()
						+ "]");
				return null;
			}
		} finally {
			if (lock != null && lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.debug(
						Thread.currentThread().getName() + " releaseed lock. lockName[" + callback.getLockName() + "]");
			}
		}
	}

	@Override
	public <T> T tryLock(IDistributedLockCallback<T> callback, boolean fairLock) {
		return tryLock(callback, DEFAULT_WAIT_TIME, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
	}

	@Override
	public <T> T tryLock(IDistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit,
			boolean fairLock) {
		RLock lock = getLock(callback.getLockName(), fairLock);
		boolean trySuccess = false;
		String currThreadName = Thread.currentThread().getName();
		try {
			trySuccess = lock.tryLock(waitTime, leaseTime, timeUnit);
			log.debug(currThreadName + "===>  tryLock function returns  [" + trySuccess + "]");
			if (trySuccess) {
				if (lock.isHeldByCurrentThread()) {
					log.debug(currThreadName + " try to  get lock ok. lockName [" + callback.getLockName() + "]");
					T t = callback.process();
					return t;
				} else {
					log.warn(currThreadName + " trySuccess[" + trySuccess + "] ,but does not get lock. lockName [ "
							+ callback.getLockName() + "]");
				}
			}
			return null;
		} catch (InterruptedException e) {
			log.warn(currThreadName + " catch InterruptedException ");
			e.printStackTrace();
			Thread.interrupted();
			return null;
		} finally {
			if (trySuccess) { // 当前tryLock成功了，才能释放自己tryLock到的锁
				if (lock != null && lock.isHeldByCurrentThread()) {
					System.out.println(currThreadName + " unlock " + callback.getLockName());
					lock.unlock();
				}
			}
		}

	}

	private RLock getLock(String lockName, boolean fairLock) {
		RLock lock;
		if (fairLock) {
			lock = redisson.getFairLock(lockName);
		} else {
			lock = redisson.getLock(lockName);
		}
		return lock;
	}

	public void setRedisson(RedissonClient redisson) {
		this.redisson = redisson;
	}
}