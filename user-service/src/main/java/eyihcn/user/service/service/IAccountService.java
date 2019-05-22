package eyihcn.user.service.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;

import eyihcn.user.service.entity.Account;

public interface IAccountService extends IService<Account> {

	Account findByUserId(String userId);

	void debit(String userId, BigDecimal money);
}