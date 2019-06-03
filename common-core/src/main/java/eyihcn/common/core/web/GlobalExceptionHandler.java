package eyihcn.common.core.web;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.common.collect.Maps;

import eyihcn.common.core.enums.ErrorCodeEnum;
import eyihcn.common.core.exception.BusinessException;
import eyihcn.common.core.model.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <p>
 * Description: 全局异常处理器
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月3日下午2:34:31
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<?> illegalArgumentException(IllegalArgumentException e) {
		log.error("参数非法异常={}", e.getMessage(), e);
		return Response.failed(ErrorCodeEnum.GLOBAL_6000);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<?> methodArgumentNotValidHandler(MethodArgumentNotValidException e) throws Exception {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		Map<String, String> fieldToErrorMsg = Maps.newHashMap();
		for (FieldError error : fieldErrors) {
			fieldToErrorMsg.put(error.getField(), error.getDefaultMessage());
			log.debug(error.getField() + ":" + error.getDefaultMessage());
		}
		log.debug("校验未通过: {}", fieldToErrorMsg);
		return Response.failed(fieldToErrorMsg, ErrorCodeEnum.GLOBAL_6000);
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<?> constraintViolationExceptionHandler(ConstraintViolationException e) throws Exception {
		log.debug("校验未通过: {}", e.getMessage());
		return Response.failed(e.getMessage(), ErrorCodeEnum.GLOBAL_6000);
	}

	@ExceptionHandler(value = UnexpectedTypeException.class)
	public Response<?> MethodArgumentNotValidHandler(UnexpectedTypeException exception) throws Exception {
		return Response.failed(null, 500L, exception.getMessage());
	}

	@ExceptionHandler({ HttpMessageConversionException.class })
	public Response<?> parameterTypeException(HttpMessageConversionException exception) {
		log.error("类型转换异常={}" + exception.getMessage());
		return Response.failed();

	}

	@ExceptionHandler(BindException.class)
	public Response<?> parameterTypeException(BindException exception) {
		log.error("类型转换异常={}" + exception.getMessage());
		return Response.failed();

	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Response<?> parameterTypeException(MethodArgumentTypeMismatchException exception) {
		log.error("类型转换异常={}" + exception.getMessage());
		return Response.failed();

	}

	/**
	 * 参数类型转换错误 （使用单个字段接收的get请求）
	 *
	 */
	@ExceptionHandler(NumberFormatException.class)
	public Response<?> parameterTypeException(NumberFormatException exception) {
		log.error("类型转换异常={}" + exception.getMessage());
		return Response.failed(ErrorCodeEnum.GLOBAL_6004);
	}

	/**
	 * 缺少参数异常
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Response<?> parameterTypeException(MissingServletRequestParameterException exception) {
		log.error("缺少参数异常={}" + exception.getMessage());
		return Response.failed(ErrorCodeEnum.GLOBAL_6003);
	}

	/**
	 * 请求方法异常
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Response<?> parameterTypeException(HttpRequestMethodNotSupportedException exception) {
		log.error("请求方法异常=" + exception.getMessage());
		return Response.failed(null, ErrorCodeEnum.GLOBAL_6006.code(),
				ErrorCodeEnum.GLOBAL_6006.msg() + " : " + exception.getMethod());
	}

	/**
	 * SQL异常
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(SQLSyntaxErrorException.class)
	public Response<?> parameterTypeException(SQLSyntaxErrorException e) {
		log.error("SQL异常={}" + e.getMessage());
		return Response.failed();
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<?> businessException(BusinessException e) {
		log.error("业务异常={}", e.getMessage());
		return Response.failed(-1L, e.getMessage());
	}

	/**
	 * 全局异常
	 *
	 * @param e the e
	 * @return the Response
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public Response<?> exception(Exception e) {
		log.info("保存全局异常信息 ex={}", e.getMessage(), e);
		return Response.failed();
	}
}
