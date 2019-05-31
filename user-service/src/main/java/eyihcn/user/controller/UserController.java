package eyihcn.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eyihcn.common.core.controller.BaseController;
import eyihcn.user.dto.UserPageDto;
import eyihcn.user.model.User;
import eyihcn.user.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;

/**
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月31日下午3:10:29
 */
@RestController
@Validated
@RequestMapping("/user")
@Api(value = "管理", tags = "管理")
public class UserController extends BaseController<UserServiceImpl, User, UserPageDto> {

}