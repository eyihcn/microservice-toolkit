package eyihcn.user.test.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eyihcn.common.core.controller.CrudController;
import eyihcn.user.test.dto.UserPageDto;
import eyihcn.user.test.entity.User;
import eyihcn.user.test.service.UserService;
import io.swagger.annotations.Api;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author chenyi 
 * @date 2019-06-16${time}
 * ${tags}
 */
@RestController
@Validated
@RequestMapping("/user")
@Api(value = "管理" , tags = "管理")
public class UserController extends CrudController<UserService, User, Long, User, UserPageDto, User> {

}
