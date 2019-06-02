package eyihcn.order.service.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.order.service.entity.Order;
import eyihcn.order.service.feign.UserFeignClient;
import eyihcn.order.service.repository.OrderMapper;
import io.seata.core.context.RootContext;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

	@Autowired
	private UserFeignClient userFeignClient;

	@Transactional(rollbackFor = Throwable.class)
	public void create(String userId, String commodityCode, Integer count) {

		System.out.println("全局事务id ：" + RootContext.getXID());

		BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));

		Order order = new Order();
		order.setUserId(userId);
		order.setCommodityCode(commodityCode);
		order.setCount(count);
		order.setMoney(orderMoney);

		save(order);

		log.debug("开始扣款、、、、、");

		userFeignClient.debit(userId, orderMoney);

	}
}
