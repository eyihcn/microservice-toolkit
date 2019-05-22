package eyihcn.business.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eyihcn.business.service.feign.OrderFeignClient;
import eyihcn.business.service.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;

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
	@GlobalTransactional
	public void purchase(String userId, String commodityCode, Integer orderCount) {
		storageFeignClient.deduct(commodityCode, orderCount);

		orderFeignClient.create(userId, commodityCode, orderCount);
	}
}
