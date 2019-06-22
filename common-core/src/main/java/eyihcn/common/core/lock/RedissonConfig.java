package eyihcn.common.core.lock;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.redis.redisson.config")
public class RedissonConfig {

	@Bean
	IDistributedLockTemplate distributedLockTemplate(@Autowired RedissonClient redissonClient) {
		return new RedissonDistributedLockTemplate(redissonClient);
	}

	@Bean
	DistributedLockAspect distributedLockAspect(@Autowired IDistributedLockTemplate iDistributedLockTemplate) {
		return new DistributedLockAspect(iDistributedLockTemplate);
	}

	@Bean
	DistributedLockManager distributedLockManager(@Autowired IDistributedLockTemplate iDistributedLockTemplate) {
		return new DistributedLockManager();
	}
}
