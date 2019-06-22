package eyihcn.user.test.service.impl;

import org.springframework.stereotype.Service;
import eyihcn.user.test.entity.User;
import eyihcn.user.test.mapper.UserMapper;
import eyihcn.user.test.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenyi
 * @since 2019-06-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}