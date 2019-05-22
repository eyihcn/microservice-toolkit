package eyihcn.order.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eyihcn.order.service.service.IOrderService;

@RestController
public class OrderController {

	@Autowired
	private IOrderService orderService;

	@GetMapping("/create")
	public Boolean create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode,
			@RequestParam("count") Integer count) {

		orderService.create(userId, commodityCode, count);
		return true;
	}

}
