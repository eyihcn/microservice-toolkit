package eyihcn.user.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import eyihcn.user.mapper.UserMapper;
import eyihcn.user.model.User;
import eyihcn.user.service.IUserService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenyi
 * @since 2019-05-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}