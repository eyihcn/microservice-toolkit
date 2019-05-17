package eyihcn.user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eyihcn.user.service.entity.Account;

public interface AccountDAO extends JpaRepository<Account, Long> {

	Account findByUserId(String userId);

}
