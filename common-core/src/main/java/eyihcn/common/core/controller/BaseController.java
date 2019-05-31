package eyihcn.common.core.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import eyihcn.common.core.constant.GlobalConstant;
import eyihcn.common.core.enums.ErrorCodeEnum;
import eyihcn.common.core.exception.BusinessException;
import eyihcn.common.core.model.BaseEntity;
import eyihcn.common.core.model.LoginAuthDto;
import eyihcn.common.core.model.Response;
import eyihcn.common.core.page.PageBeanUtils;
import eyihcn.common.core.page.PageQuery;
import eyihcn.common.core.utils.QueryWrapperUtils;
import eyihcn.common.core.utils.ThreadLocalMap;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * <p>
 * Description: 基础类，简化编码
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月30日下午3:46:48
 * @param <S>       是Service实现
 * @param <T>       实体类型
 * @param <PageDto> 分页dto类型
 */
@Validated
public class BaseController<S extends IService<T>, T extends BaseEntity<? extends Serializable>, PageDto extends PageQuery> {

	@Autowired
	private S baseService;

	/**
	 * Gets login auth dto.
	 *
	 * @return the login auth dto
	 */
	protected LoginAuthDto getLoginAuthDto() {
		LoginAuthDto loginAuthDto = (LoginAuthDto) ThreadLocalMap.get(GlobalConstant.Sys.TOKEN_AUTH_DTO);
		if (null == loginAuthDto) {
			throw new BusinessException(ErrorCodeEnum.USER_10011041);
		}
		return loginAuthDto;
	}

	@ApiOperation(value = "根据id查询")
	@PostMapping("/getById")
	public Response<?> getById(@RequestParam @NotNull(message = "id不能为空") Serializable id) {
		T e = baseService.getById(id);
		if (e == null) {
			return Response.failed("查询失败，该数据已被删除或无该数据！");
		}
		return Response.ok(e);
	}

	@ApiOperation(value = "分页查询")
	@PostMapping("/getPage")
	public Response<?> getPage(@RequestBody @Validated PageDto pageDto) {

		IPage<T> page = PageBeanUtils.page(pageDto);

		baseService.page(page, QueryWrapperUtils.getQueryWrapper(pageDto));

		return Response.ok(PageBeanUtils.newPageBean(page));
	}

	@ApiOperation(value = "有id参数更新，无id新增")
	@PostMapping("/save")
	public Response<?> save(@RequestBody @Validated T e) {
		Serializable id = e.getId();
		if (null == id) {

		}
		T old = baseService.getById(id);
		if (old == null) {
			e.setCreateTime(new Date());
			// TODO
			e.setCreateUser(0L);
		} else {
			e.setUpdateTime(new Date());
			e.setUpdateUser(0L);
		}
		if (baseService.saveOrUpdate(e)) {
			return Response.ok();
		} else {
			return Response.failed();
		}
	}

	@ApiOperation(value = "批量删除")
	@PostMapping("/deleteByIds")
	public Response<?> deleteByIds(@RequestBody Set<Long> ids) {

		if (CollectionUtils.isEmpty(ids)) {
			Response.failed("请传入有效参数");
		}
		if (baseService.removeByIds(ids)) {
			return Response.ok();
		} else {
			return Response.failed();
		}
	}

}
