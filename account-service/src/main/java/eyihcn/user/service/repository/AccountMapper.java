package eyihcn.user.service.repository;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import eyihcn.user.service.entity.Account;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}
