package eyihcn.user.test.mapper;

import org.apache.ibatis.annotations.Mapper;

import eyihcn.user.test.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenyi
 * @since 2019-06-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
