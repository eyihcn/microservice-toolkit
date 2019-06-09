package eyihcn.common.core.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;

import eyihcn.common.core.constant.CommonConstant;
import eyihcn.common.core.dto.UserInfo;
import eyihcn.common.core.utils.ThreadLocalMap;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <p>
 * Description: 拦截请求，获取登录人信息，放入ThreadLocal中
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月4日上午10:29:40
 */
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {
	/**
	 * Pre handle boolean.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param handler  the handler
	 *
	 * @return the boolean
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String uri = request.getRequestURI();
		String authorization = request.getHeader(CommonConstant.HEAD_USER_INFO_KEY);
		if (StringUtils.isBlank(authorization)) {
			log.debug("当前无登录用户信息");
			return true;
		}
		log.debug("<== preHandle - LoginUserInfoInterceptor拦截器.  token={}", authorization);
		try {
			UserInfo userInfo = new UserInfo();
			Map<String, Object> map = JSONObject.parseObject(authorization);
			if (!map.isEmpty()) {
				Long userId = Long.valueOf(map.get("id").toString());
				// 用户名
				String username = map.get("user_name") == null ? "" : map.get("user_name").toString();
				// 真实姓名
				String realName = map.get("realName") == null ? "" : map.get("realName").toString();
				// 手机号
				String phone = map.get("phone").toString() == null ? "" : map.get("phone").toString();
				userInfo.setId(userId);
				userInfo.setUsername(username);
				userInfo.setRealName(realName);
				userInfo.setPhone(phone);
				log.debug("<== preHandle - 权限拦截器.  userInfo={}", userInfo);
				ThreadLocalMap.put(CommonConstant.LOGIN_USER_INFO, userInfo);
				log.debug("<== preHandle - 权限拦截器.  url={}, userInfo={}", uri, userInfo);
			}
		} catch (Exception e) {
			log.info("获取当前用户信息异常：" + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 清理自定义的 ThreadLocal变量,
		// 线程池场景下，线程经常会被复用，如果不清理自定义的 ThreadLocal变量，可能会影响后续业务逻辑和造成内存泄露等问题
		ThreadLocalMap.remove();
	}
}
