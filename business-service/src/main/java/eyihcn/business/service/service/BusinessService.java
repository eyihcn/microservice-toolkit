package eyihcn.business.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eyihcn.business.service.feign.OrderFeignClient;
import eyihcn.business.service.feign.StorageFeignClient;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusinessService {

	@Autowired
	private StorageFeignClient storageFeignClient;
	@Autowired
	private OrderFeignClient orderFeignClient;

	/**
	 * 减库存，下订单
	 *
	 * @param userId
	 * @param commodityCode
	 * @param orderCount
	 */
	@GlobalTransactional(timeoutMills = 300000, rollbackFor = Throwable.class)
	public void purchase(String userId, String commodityCode, Integer orderCount) {

		log.info("xid .... " + RootContext.getXID());
		storageFeignClient.deduct(commodityCode, orderCount);

		orderFeignClient.create(userId, commodityCode, orderCount);
	}
}
