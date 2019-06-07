package eyihcn.common.core.lock;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.redis.redisson.config")
public class RedissonConfig {

	@Bean
	IDistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient) {
		return new SingleDistributedLockTemplate(redissonClient);
	}

	@Bean
	DistributedLockAspect distributedLockAspect(IDistributedLockTemplate iDistributedLockTemplate) {
		return new DistributedLockAspect(iDistributedLockTemplate);
	}
}
