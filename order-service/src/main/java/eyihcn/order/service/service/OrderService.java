package eyihcn.order.service.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.order.service.entity.Order;
import eyihcn.order.service.feign.UserFeignClient;
import eyihcn.order.service.repository.OrderMapper;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

	@Autowired
	private UserFeignClient userFeignClient;

	@Transactional
	public void create(String userId, String commodityCode, Integer count) {

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
