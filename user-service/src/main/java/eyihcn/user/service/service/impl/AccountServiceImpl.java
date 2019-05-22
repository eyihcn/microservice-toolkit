package eyihcn.user.service.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.user.service.entity.Account;
import eyihcn.user.service.repository.AccountMapper;
import eyihcn.user.service.service.IAccountService;

/**
 * @author chenyi
 * @since 2019-05-20
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

	private static final String ERROR_USER_ID = "1002";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void debit(String userId, BigDecimal num) {

		Account account = findByUserId(userId);
		account.setMoney(account.getMoney().subtract(num));
		save(account);

		if (ERROR_USER_ID.equals(userId)) {
			throw new RuntimeException("account branch exception");
		}
	}

	@Override
	public Account findByUserId(String userId) {
		QueryWrapper<Account> qWrapper = new QueryWrapper<Account>();
		qWrapper.eq(Account.USER_ID, userId);
		return getOne(qWrapper);
	}
}