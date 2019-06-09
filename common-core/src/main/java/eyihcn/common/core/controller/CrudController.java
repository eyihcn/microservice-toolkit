package eyihcn.common.core.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import eyihcn.common.core.beancopy.IPageVoCopier;
import eyihcn.common.core.beancopy.ISaveDtoCopier;
import eyihcn.common.core.beancopy.SimplePageVoCopier;
import eyihcn.common.core.beancopy.SimpleSaveDtoCopier;
import eyihcn.common.core.constant.CommonConstant;
import eyihcn.common.core.dto.UserInfo;
import eyihcn.common.core.enums.ErrorCodeEnum;
import eyihcn.common.core.exception.BusinessException;
import eyihcn.common.core.model.BaseEntity;
import eyihcn.common.core.model.Response;
import eyihcn.common.core.page.IPageQuery;
import eyihcn.common.core.page.PageBean;
import eyihcn.common.core.page.PageQueryParam;
import eyihcn.common.core.utils.MyBeanUtil;
import eyihcn.common.core.utils.ThreadLocalMap;
import eyihcn.common.core.web.QueryWrapperUtils;
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
public class CrudController<S extends IService<T>, T extends BaseEntity<PK>, PK extends Serializable, SaveDto, PageQueryDto extends IPageQuery, EntityVo> {

	@Autowired
	private ApplicationContext applicationContext;

	private Class<T> entityClass;
//	private Class<PK> pkClass;
	private Class<EntityVo> entityVoClass;
	private Class<SaveDto> saveDtoClass;

	@Autowired
	private S baseService;

	private IPageVoCopier pageVoCopier;
	private static final String Page_Vo_Copier = "PageVoCopier";

	public CrudController() {
		this.entityClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 1);
//		Field declaredField = null;
//		try {
//			declaredField = entityClass.getDeclaredField("id");
//		} catch (NoSuchFieldException | SecurityException e) {
//			throw new RuntimeException(e);
//		}
//		this.pkClass = (Class<PK>) declaredField.getType();
//		this.pkClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 2);
		this.saveDtoClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 3);
		this.entityVoClass = MyBeanUtil.getSuperClassGenericType(this.getClass(), 5);
	}

	public IPageVoCopier getPageVoCopier() {

		if (this.pageVoCopier != null) {
			return this.pageVoCopier;
		}
		String defaultName = StringUtils.uncapitalize(entityClass.getSimpleName() + Page_Vo_Copier);
		if (applicationContext.containsBean(defaultName)) {
			pageVoCopier = (IPageVoCopier) applicationContext.getBean(defaultName);
		}
		return pageVoCopier;
	}

	/**
	 * 
	 * <p>
	 * Description: 获取登录人的信息
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年6月4日上午9:58:24
	 * @return
	 */
	protected UserInfo getLoginUserInfo() {
		UserInfo userInfo = (UserInfo) ThreadLocalMap.get(CommonConstant.LOGIN_USER_INFO);
		return userInfo;
	}

	/**
	 * 
	 * <p>
	 * Description: 获取登录人ID
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年6月4日上午9:59:25
	 * @return 无登录用户，返回null
	 */
	protected Long getLoginUserId() {
		UserInfo userInfo = (UserInfo) ThreadLocalMap.get(CommonConstant.LOGIN_USER_INFO);
		if (null == userInfo) {
			return null;
		}
		return userInfo.getId();
	}

	@ApiOperation(value = "根据id集合查询(参数请放在form表单中)")
	@PostMapping("/getByIds")
	public Response<?> getByIds(
			@RequestParam("ids") @NotNull(message = "id集合不能为空") @NotEmpty(message = "id集合不能为空") Set<PK> ids) {

		checkNumberIds(ids);

		Collection<T> listByIds = baseService.listByIds(ids);
		if (CollectionUtils.isEmpty(listByIds)) {
			return Response.failedWithMsg("查询失败，该数据已被删除或无该数据！");
		}
		return Response.ok(listByIds);
	}

	@ApiOperation(value = "根据id查询")
	@PostMapping("/getById")
	public Response<?> getById(@RequestParam @NotNull(message = "id不能为空") PK id) {
		T e = baseService.getById(id);
		if (e == null) {
			return Response.failedWithMsg("查询失败，该数据已被删除或无该数据！");
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
		if (this.getPageVoCopier() == null) {
			SimplePageVoCopier simplePageVoCopier = this.applicationContext.getBean(SimplePageVoCopier.class);
			voList = simplePageVoCopier.convert(records, entityVoClass);
		} else {
			voList = this.getPageVoCopier().convert(records, this.entityVoClass);
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
				entity.setUpdateUser(getLoginUserId());
				return baseService.saveOrUpdate((T) entity) ? Response.ok() : Response.failed();
			} else {
				return Response.failedWithMsg("该记录不存在，更新失败！");
			}
		}
		entity.setCreateTime(new Date());
		entity.setCreateUser(getLoginUserId());
		return baseService.saveOrUpdate((T) entity) ? Response.ok() : Response.failed();
	}

	@ApiOperation(value = "批量删除(参数请放在form表单中)")
	@PostMapping("/deleteByIds")
	public Response<?> deleteByIds(
			@RequestParam("ids") @NotNull(message = "id集合不能为空") @NotEmpty(message = "id集合不能为空") Set<PK> ids) {

		checkNumberIds(ids);

		if (baseService.removeByIds(ids)) {
			return Response.ok();
		} else {
			return Response.failedWithMsg("删除失败，该数据已被删除或无该数据！");
		}
	}

	/**
	 * 
	 * <p>
	 * Description: 数字类型id ，不能为负数
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年6月3日下午3:12:35
	 */
	public static void checkNumberIds(Collection<?> col) {

		if (CollectionUtils.isEmpty(col)) {
			// "接口参数不能为空"
			throw new BusinessException(ErrorCodeEnum.GLOBAL_6003);
		}

		col.forEach(o -> {
			if (o instanceof Number) {
				// 包含负数，抛异常
				if (((Number) o).longValue() < 0) {
					throw new BusinessException(ErrorCodeEnum.GLOBAL_6005);
				}
			}
		});

	}

}
