/**
 * 
 */
package eyihcn.common.core.web;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Description:定义查询字段
 * </p>
 * 
 * @author chenyi
 * @date 2019年5月31日上午9:24:42
 */

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface QueryKey {

	public enum Operator {
		/** = */
		EQ,
		/** != */
		NE,
		/** >= */
		GE,
		/** > */
		GT,
		/** >= */
		LE,
		/** < */
		LT,
		/** in */
		IN,
		/** not in */
		NOT_IN,
		/** like */
		LIKE,
		/** like xx% */
		LEFT_LIKE,
		/** like %xx */
		RIGHT_LIKE,
		/** not like %xx% */
		NOT_LIKE,
		/** not like xx% */
		NOT_LEFT_LIKE,
		/** not like %xx */
		NOT_RIGHT_LIKE,
		/** is null */
		NULL,
		/** is not null */
		NOT_NULL
	}

	/**
	 * 
	 * <p>
	 * Description: 数据库字段名称，为空时取java字段
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月31日上午9:35:04
	 * @return
	 */
	String name() default "";

	/**
	 * 
	 * <p>
	 * Description: where语句后面操作符
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年5月31日上午10:08:52
	 * @return
	 */
	Operator operator() default Operator.EQ;
}
