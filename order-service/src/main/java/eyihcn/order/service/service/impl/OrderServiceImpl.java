package eyihcn.order.service.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.order.service.entity.Order;
import eyihcn.order.service.feign.UserFeignClient;
import eyihcn.order.service.repository.OrderMapper;
import eyihcn.order.service.service.IOrderService;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

	@Autowired
	private UserFeignClient userFeignClient;

	@Transactional
	@Override
	public void create(String userId, String commodityCode, Integer count) {

		BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));

		Order order = new Order();
		order.setUserId(userId);
		order.setCommodityCode(commodityCode);
		order.setCount(count);
		order.setMoney(orderMoney);

		save(order);

		userFeignClient.debit(userId, orderMoney);

	}
}
