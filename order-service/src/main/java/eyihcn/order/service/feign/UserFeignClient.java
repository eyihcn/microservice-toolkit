package eyihcn.order.service.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service")
public interface UserFeignClient {

	@GetMapping("/debit")
	Boolean debit(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money);
}
