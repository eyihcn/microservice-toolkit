package eyihcn.user.service.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eyihcn.user.service.service.AccountService;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping("/debit")
	public Boolean debit(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money) {
		accountService.debit(userId, money);
		return true;
	}
}
