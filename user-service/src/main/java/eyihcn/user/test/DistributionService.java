package eyihcn.user.test;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eyihcn.common.core.lock.DistributedLockAnotation;
import eyihcn.user.model.User;

@Service
public class DistributionService {

	@Autowired
	private RedissonClient redissonClient;

	@DistributedLockAnotation(param = "id", lockNamePost = ".lock")
	public Integer aspect(User person) {
		RMap<String, Integer> map = redissonClient.getMap("distributionTest");

		Integer count = map.get("count");

		if (count > 0) {
			count = count - 1;
			map.put("count", count);
		}

		return count;
	}

	@DistributedLockAnotation(argNum = 1, lockNamePost = ".lock")
	public Integer aspect(String i) {
		RMap<String, Integer> map = redissonClient.getMap("distributionTest");

		Integer count = map.get("count");

		if (count > 0) {
			count = count - 1;
			map.put("count", count);
		}

		return count;
	}

//	@DistributedLock(lockName = "lock", lockNamePost = ".lock")
//	public int aspect(Action<Integer> action) {
//		return action.action();
//	}
}