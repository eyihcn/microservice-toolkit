package eyihcn.user.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eyihcn.common.core.lock.DistributedLockManager;
import eyihcn.common.core.lock.IDistributedLockCallback;
import eyihcn.common.core.lock.IDistributedLockTemplate;

@RestController
@RequestMapping("/distributedLockTest")
public class DistributedLockTestController {



	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private DistributedLockManager distributedLockManager;

	@Autowired
	private IDistributedLockTemplate iDistributedLockTemplate;

	@GetMapping("/testLock3")
	public String test3() throws Exception {

		String tryLock = iDistributedLockTemplate.lock(new IDistributedLockCallback<String>() {

			@Override
			public String process() {
				System.out.println(Thread.currentThread().getName());
				return Thread.currentThread().getName();
			}

			@Override
			public String getLockName() {
				return "testLock";
			}
		}, 50L, TimeUnit.SECONDS, true);

		System.out.println(tryLock);
		return "fish";
	}

	@GetMapping("/test2")
	public String test2() throws Exception {
		String tryLock = iDistributedLockTemplate.tryLock(new IDistributedLockCallback<String>() {

			@Override
			public String process() {
				System.out.println(Thread.currentThread().getName());
				return Thread.currentThread().getName();
			}

			@Override
			public String getLockName() {
				return "testLock";
			}
		}, 0, 5, TimeUnit.SECONDS, true);

		System.out.println(tryLock);
		return "fish";
	}

	public static AtomicInteger trySuccessCount;

	@GetMapping("/testTryLock")
	public String testTryLock() throws Exception {
		trySuccessCount = new AtomicInteger(0);
		RMap<String, Integer> map = redissonClient.getMap("distributionTest");
		map.put("count", 10000);
		int count = 1000;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(count);

		for (int i = 0; i < count; ++i) { // create and start threads
			new Thread(new TryLockTest(startSignal, doneSignal, distributedLockManager, redissonClient)).start();
		}

		startSignal.countDown(); // let all threads proceed
		doneSignal.await();
		System.out.println("trySuccessCount : " + trySuccessCount.get());
		System.out.println("All processors done. Shutdown connection");

		return "finish";
	}

	/**
	 * 通过测试
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/test")
	public String distributedLockTest() throws Exception {

		RMap<String, Integer> map = redissonClient.getMap("distributionTest");
		map.put("count", 10000);
		int count = 1000;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(count);

		for (int i = 0; i < count; ++i) { // create and start threads
			new Thread(new LockTest1(startSignal, doneSignal, distributedLockManager, redissonClient)).start();
		}

		startSignal.countDown(); // let all threads proceed
		doneSignal.await();
		System.out.println("All processors done. Shutdown connection");

		return "finish";
	}

}
