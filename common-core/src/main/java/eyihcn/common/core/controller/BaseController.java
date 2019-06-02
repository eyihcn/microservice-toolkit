package eyihcn.common.core.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import eyihcn.common.core.beancopy.IPageVoCopier;
import eyihcn.common.core.beancopy.ISaveDtoCopier;
import eyihcn.common.core.beancopy.SimplePageVoCopier;
import eyihcn.common.core.beancopy.SimpleSaveDtoCopier;
import eyihcn.common.core.constant.GlobalConstant;
import eyihcn.common.core.enums.ErrorCodeEnum;
import eyihcn.common.core.exception.BusinessException;
import eyihcn.common.core.model.BaseEntity;
import eyihcn.common.core.model.LoginAuthDto;
import eyihcn.common.core.model.Response;
import eyihcn.common.core.page.IPageQuery;
import eyihcn.common.core.page.PageBean;
import eyihcn.common.core.page.PageQueryParam;
import eyihcn.common.core.utils.MyBeanUtil;
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
 * @param <S>            是Service实现
 * @param <T>            实体类型
 * @param <PageQueryDto> 分页dto类型
 */
@SuppressWarnings("unchecked")
@Validated
public class BaseController<S extends IService<T>, T extends BaseEntity<? extends Serializable>, SaveDto, PageQueryDto extends IPageQuery, EntityVo> {

	@Autowired
	private ApplicationContext applicationContext;

	private Class<T> entityClass;
	private Class<EntityVo> entityVoClass;
	private Class<SaveDto> saveDtoClass;

	@Autowired
	private S baseService;

	private IPageVoCopier pageVoCopier;
	private static final String Page_Vo_Copier = "PageVoCopier";

	public BaseController() {
		this.entityClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 1);
		this.saveDtoClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 2);
		this.entityVoClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 4);
		this.pageVoCopier = getpageVoCopier(entityClass);
	}

	public IPageVoCopier getpageVoCopier(Class<T> entityClass2) {

		if (this.pageVoCopier != null) {
			return this.pageVoCopier;
		}
		String defaultName = StringUtils.uncapitalize(entityClass.getSimpleName() + Page_Vo_Copier);
		pageVoCopier = (IPageVoCopier) applicationContext.getBean(defaultName);
		return pageVoCopier;
	}

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
	public Response<?> getById(@RequestBody @NotNull(message = "id不能为空") Serializable id) {
		T e = baseService.getById(id);
		if (e == null) {
			return Response.failed("查询失败，该数据已被删除或无该数据！");
		}
		return Response.ok(e);
	}

	@ApiOperation(value = "分页查询")
	@PostMapping("/getPage")
	public Response<?> getPage(@RequestBody @Validated PageQueryDto pageDto) {

		IPage<T> page = PageQueryParam.page(pageDto);

		baseService.page(page, QueryWrapperUtils.getQueryWrapper(pageDto));

		if (entityClass == entityVoClass) {
			return Response.ok(PageBean.newPageBean(page));
		}
		Collection<EntityVo> voList = null;
		List<T> records = page.getRecords();
		if (this.pageVoCopier == null) {
			SimplePageVoCopier simplePageVoCopier = this.applicationContext.getBean(SimplePageVoCopier.class);
			voList = simplePageVoCopier.convert(records, entityVoClass);
		} else {
			voList = this.pageVoCopier.convert(records, this.entityVoClass);
		}
		return Response.ok(PageBean.newPageBean(page, voList));
	}

	@ApiOperation(value = "有id参数更新，无id新增")
	@PostMapping("/save")
	public Response<?> save(@RequestBody @Validated SaveDto e) {

		BaseEntity<? extends Serializable> entity = null;

		// 根据自定的复制方法，创建T的对象
		if (e instanceof ISaveDtoCopier) {
			ISaveDtoCopier saveDtoCopier = (ISaveDtoCopier) e;
			entity = saveDtoCopier.copyProperties(e, this.entityClass);
		} else {
			// 若泛型指定T与SaveDto相同
			if (this.entityClass == this.saveDtoClass) {
				entity = ((BaseEntity<? extends Serializable>) e);
			} else {
				SimpleSaveDtoCopier simpleSaveDtoCopier = this.applicationContext.getBean(SimpleSaveDtoCopier.class);
				entity = simpleSaveDtoCopier.copyProperties(e, entityClass);
			}
		}
		Serializable id = entity.getId();
		// 更新
		if (null != id) {
			T old = baseService.getById(id);
			if (old != null) {
				entity.setUpdateTime(new Date());
				// TODO
				entity.setUpdateUser(0L);
				return baseService.saveOrUpdate((T) entity) ? Response.ok() : Response.failed();
			}
		}
		entity.setCreateTime(new Date());
		// TODO
		entity.setCreateUser(0L);
		return baseService.saveOrUpdate((T) entity) ? Response.ok() : Response.failed();
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
