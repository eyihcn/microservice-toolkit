package eyihcn.common.core.lock;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * <p>
 * Description: 分布式锁注解拦截切面
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月9日下午6:49:24
 */
@SuppressWarnings("rawtypes")
@Aspect
public class DistributedLockAspect {

	private IDistributedLockTemplate lockTemplate;

	public DistributedLockAspect() {
	}

	public DistributedLockAspect(IDistributedLockTemplate lockTemplate ) {
		this.lockTemplate = lockTemplate;
	}

	@Pointcut("@annotation(eyihcn.common.core.lock.DistributedLockAnotation)")
	public void distributedLockAspect() {
	}

	@Around(value = "distributedLockAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

		// 切点所在的类
		Class<? extends Object> targetClass = pjp.getTarget().getClass();
		// 使用了注解的方法
		String methodName = pjp.getSignature().getName();

		Class[] parameterTypes = ((MethodSignature) pjp.getSignature()).getMethod().getParameterTypes();

		Method method = targetClass.getMethod(methodName, parameterTypes);

		Object[] arguments = pjp.getArgs();

		final String lockName = getLockName(method, arguments);

		return lock(pjp, method, lockName);
	}

	@AfterThrowing(value = "distributedLockAspect()", throwing = "ex")
	public void afterThrowing(Throwable ex) {
		throw new RuntimeException(ex);
	}

	public String getLockName(Method method, Object[] args) {
		Objects.requireNonNull(method);
		DistributedLockAnotation annotation = method.getAnnotation(DistributedLockAnotation.class);

		String lockName = annotation.lockName(), param = annotation.param();

		if (isEmpty(lockName)) {
			if (args.length > 0) {
				if (isNotEmpty(param)) {
					Object arg;
					if (annotation.argNum() > 0) {
						arg = args[annotation.argNum() - 1];
					} else {
						arg = args[0];
					}
					lockName = String.valueOf(getParam(arg, param));
				} else if (annotation.argNum() > 0) {
					lockName = args[annotation.argNum() - 1].toString();
				}
			}
		}

		if (isNotEmpty(lockName)) {
			String preLockName = annotation.lockNamePre(), postLockName = annotation.lockNamePost(),
					separator = annotation.separator();

			StringBuilder lName = new StringBuilder();
			if (isNotEmpty(preLockName)) {
				lName.append(preLockName).append(separator);
			}
			lName.append(lockName);
			if (isNotEmpty(postLockName)) {
				lName.append(separator).append(postLockName);
			}

			lockName = lName.toString();

			return lockName;
		}

		throw new IllegalArgumentException("Can't get or generate lockName accurately!");
	}

	/**
	 * 从方法参数获取数据
	 *
	 * @param param
	 * @param arg   方法的参数数组
	 * @return
	 */
	public Object getParam(Object arg, String param) {
		if (isNotEmpty(param) && arg != null) {
			try {
				Object result = PropertyUtils.getProperty(arg, param);
				return result;
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(arg + "没有属性" + param + "或未实现get方法。", e);
			} catch (Exception e) {
				throw new RuntimeException("", e);
			}
		}
		return null;
	}

	public Object lock(ProceedingJoinPoint pjp, Method method, final String lockName) {

		DistributedLockAnotation annotation = method.getAnnotation(DistributedLockAnotation.class);

		boolean fairLock = annotation.fairLock();

		boolean tryLock = annotation.tryLock();

		if (tryLock) {
			return tryLock(pjp, annotation, lockName, fairLock);
		} else {
			return lock(pjp, lockName, fairLock);
		}
	}

	public Object lock(ProceedingJoinPoint pjp, final String lockName, boolean fairLock) {
		return lockTemplate.lock(new IDistributedLockCallback<Object>() {
			@Override
			public Object process() {
				return proceed(pjp);
			}

			@Override
			public String getLockName() {
				return lockName;
			}
		}, fairLock);
	}

	public Object tryLock(ProceedingJoinPoint pjp, DistributedLockAnotation annotation, final String lockName,
			boolean fairLock) {

		long waitTime = annotation.waitTime();
		long leaseTime = annotation.leaseTime();
		TimeUnit timeUnit = annotation.timeUnit();

		return lockTemplate.tryLock(new IDistributedLockCallback<Object>() {
			@Override
			public Object process() {
				return proceed(pjp);
			}

			@Override
			public String getLockName() {
				return lockName;
			}
		}, waitTime, leaseTime, timeUnit, fairLock);
	}

	public Object proceed(ProceedingJoinPoint pjp) {
		try {
			return pjp.proceed();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	private boolean isEmpty(Object str) {
		return str == null || "".equals(str);
	}

	private boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}
}
