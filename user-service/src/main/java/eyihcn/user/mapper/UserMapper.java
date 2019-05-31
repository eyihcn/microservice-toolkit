package eyihcn.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import eyihcn.user.model.User;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenyi
 * @since 2019-05-31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
