package eyihcn.order.service.service;

import com.baomidou.mybatisplus.extension.service.IService;

import eyihcn.order.service.entity.Order;

public interface IOrderService extends IService<Order> {

	void create(String userId, String commodityCode, Integer count);

}
