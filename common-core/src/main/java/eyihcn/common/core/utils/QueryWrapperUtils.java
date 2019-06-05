/**
 * 
 */
package eyihcn.common.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;

import eyihcn.common.core.annotation.QueryKey;
import eyihcn.common.core.annotation.QueryKey.Operator;
import eyihcn.common.core.model.BaseEntity;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月30日下午7:52:26
 */
public class QueryWrapperUtils {

	/**
	 * 空字符
	 */
	public static final String EMPTY = "";
	/**
	 * 下划线字符
	 */
	public static final char UNDERLINE = '_';

	/**
	 * 验证字符串是否是数据库字段
	 */
	private static final Pattern P_IS_COLUMN = Pattern.compile("^\\w\\S*[\\w\\d]*$");

	public static <T> QueryWrapper<T> getQueryWrapper(Object queryObject) {

		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		Class<?> classType = queryObject.getClass();
		if (null == classType) {
			throw new IllegalArgumentException();
		}
		Field[] fs = classType.getDeclaredFields(); // 得到所有的fields

		boolean isEntityDto = queryObject instanceof BaseEntity;

		Set<String> commonFieldNames = Sets.newHashSet(BaseEntity.commonFieldNames);
		// 只支持基本类型
		for (Field f : fs) {
			if (isEntityDto) {
				// 若基类BaseEntity的字段被子类覆盖
				commonFieldNames.remove(f.getName());
			}
			convertToQueryWrapper(queryWrapper, queryObject, f);
		}
		Class<?> superclass = classType.getSuperclass();
		if (null == superclass) {
			return queryWrapper;
		}
		// 处理BaseEntity公共字段
		// 只处理单层继承
		if (superclass == BaseEntity.class && !commonFieldNames.isEmpty()) {
			commonFieldNames.forEach(name -> {
				try {
					Field field = superclass.getDeclaredField(name);
					convertToQueryWrapper(queryWrapper, queryObject, field);
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}

			});
			// 处理排序字段，排序字段在基类中
			try {
				handlerOrderby(queryObject, queryWrapper, superclass, BaseEntity.Fields.orderByAsc);
				handlerOrderby(queryObject, queryWrapper, superclass, BaseEntity.Fields.orderByDesc);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return queryWrapper;
	}

	/**
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年6月5日下午2:21:33
	 * @param <T>
	 * @param queryObject
	 * @param queryWrapper
	 * @param superclass
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */

	private static <T> void handlerOrderby(Object queryObject, QueryWrapper<T> queryWrapper, Class<?> superclass,
			String oderByFieldName) throws NoSuchFieldException, IllegalAccessException {
		Field declaredField = superclass.getDeclaredField(oderByFieldName);
		declaredField.setAccessible(true);
		Object val = declaredField.get(queryObject);
		if (null == val) {
			return;
		}
		// 获取数据库字段名称
		String fieldNameUnderline = camelToUnderline(val.toString());
		if (isNotColumnName(fieldNameUnderline)) {
			throw new IllegalArgumentException("不符合数据库字段的命名");
		}
		if (BaseEntity.Fields.orderByAsc.equals(oderByFieldName)) {
			queryWrapper.orderByAsc(fieldNameUnderline);
		} else if (BaseEntity.Fields.orderByDesc.equals(oderByFieldName)) {
			queryWrapper.orderByDesc(fieldNameUnderline);
		}
	}

	/**
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年6月5日上午11:46:14
	 * @param queryWrapper
	 * @param queryObject
	 * @param f
	 */

	private static <T> void convertToQueryWrapper(QueryWrapper<T> queryWrapper, Object queryObject, Field f) {
		// 跳过静态字段
		if (Modifier.isStatic(f.getModifiers())) {
			return;
		}
		f.setAccessible(true);

		Object val = null;
		try {
			val = f.get(queryObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// 获取数据库字段名称
		String fieldNameUnderline = getFieldNameUnderline(f);
		if (isNotColumnName(fieldNameUnderline)) {
			throw new IllegalArgumentException("不符合数据库字段的命名");
		}
		// 获取数据库字段的操作符
		QueryKey.Operator operator = getFieldOperator(f);
		if (val == null) {
			if (operator == null || operator != Operator.NULL || operator != Operator.NOT_NULL) {
				return;
			}
		} else {
			if (operator == null) {
				operator = Operator.EQ;
			}
		}
		switch (operator) {
		case EQ:
			queryWrapper.eq(fieldNameUnderline, val);
			break;
		case NE:
			queryWrapper.ne(fieldNameUnderline, val);
			break;
		case GE:
			queryWrapper.ge(fieldNameUnderline, val);
			break;
		case GT:
			queryWrapper.gt(fieldNameUnderline, val);
			break;
		case LE:
			queryWrapper.le(fieldNameUnderline, val);
			break;
		case LT:
			queryWrapper.lt(fieldNameUnderline, val);
			break;
		case LIKE:
			queryWrapper.like(fieldNameUnderline, val);
			break;
		case NOT_LIKE:
			queryWrapper.notLike(fieldNameUnderline, val);
			break;
		case RIGHT_LIKE:
			queryWrapper.likeLeft(fieldNameUnderline, val);
			break;
		case NOT_RIGHT_LIKE:
			// TODO
//						queryWrapper.apply(applySql, value)
			break;
		case LEFT_LIKE:
			queryWrapper.likeRight(fieldNameUnderline, val);
			break;
		case NOT_LEFT_LIKE:
			// TODO
			break;
		case IN:
			if (val instanceof Collection) {
				queryWrapper.in(fieldNameUnderline, (Collection<?>) val);
			} else {
				queryWrapper.in(fieldNameUnderline, val);
			}
			break;
		case NOT_IN:
			if (val instanceof Collection) {
				queryWrapper.notIn(fieldNameUnderline, (Collection<?>) val);
			} else {
				queryWrapper.notIn(fieldNameUnderline, val);
			}
			break;

		case NULL:
			queryWrapper.isNull(fieldNameUnderline);
			break;

		case NOT_NULL:
			queryWrapper.isNotNull(fieldNameUnderline);
			break;
		default:
			// TODO
			break;
		}
	}

	/**
	 * <p>
	 * Description: 获取数据库字段的操作符
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月31日上午10:35:01
	 * @param f
	 * @return
	 */

	private static Operator getFieldOperator(Field f) {
		QueryKey annotation = f.getAnnotation(QueryKey.class);
		// 如果没有注解，操作符是EQ
		if (annotation == null) {
			return QueryKey.Operator.EQ;
		}
		return annotation.operator();
	}

	/**
	 * <p>
	 * Description: 获取数据库字段名称
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月31日上午10:29:07
	 * @param f
	 * @return
	 */

	private static String getFieldNameUnderline(Field f) {

		String fieldName = f.getName();
		QueryKey annotation = f.getAnnotation(QueryKey.class);
		// 如果没有注解 或者 注解的name属性为空，查询数据库字段名默认是java字段名称驼峰转下换线，操作符是EQ
		if (annotation == null) {
			return camelToUnderline(fieldName);
		}
		String name = annotation.name();
		if (isEmpty(name)) {
			return camelToUnderline(fieldName);
		}
		return name;
	}

	/**
	 * 判断字符串是否符合数据库字段的命名
	 *
	 * @param str 字符串
	 * @return 判断结果
	 */
	public static boolean isNotColumnName(String str) {
		return !P_IS_COLUMN.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param cs 需要判断字符串
	 * @return 判断结果
	 */
	public static boolean isEmpty(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串驼峰转下划线格式
	 *
	 * @param param 需要转换的字符串
	 * @return 转换好的字符串
	 */
	public static String camelToUnderline(String param) {
		if (isEmpty(param)) {
			return EMPTY;
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append(UNDERLINE);
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}
}
