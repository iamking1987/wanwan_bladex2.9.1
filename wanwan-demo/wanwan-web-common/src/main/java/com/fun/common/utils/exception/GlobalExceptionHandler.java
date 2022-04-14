//package com.fun.common.utils.exception;
//
//import com.fun.common.utils.result.ResultResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author zz
// * @date 2021/8/18
// */
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//	/**
//	 * 处理自定义的业务异常
//	 */
//	@ExceptionHandler(value = BizException.class)
//	@ResponseBody
//	public ResultResponse bizExceptionHandler(HttpServletRequest req, BizException e){
//		logger.error("发生业务异常！原因是：{}",e.getErrorMsg());
//		return ResultResponse.error(e.getErrorCode(),e.getErrorMsg());
//	}
//
//	/**
//	 * 处理空指针的异常
//	 */
//	@ExceptionHandler(value =NullPointerException.class)
//	@ResponseBody
//	public ResultResponse exceptionHandler(HttpServletRequest req, NullPointerException e){
//		logger.error("发生空指针异常！原因是:",e);
//		return ResultResponse.error(ExceptionEnum.BODY_NOT_MATCH);
//	}
//
//	/**
//	 * 处理其他异常
//	 */
//	@ExceptionHandler(value =Exception.class)
//	@ResponseBody
//	public ResultResponse exceptionHandler(HttpServletRequest req, Exception e){
//		logger.error("未知异常！原因是:",e);
//		return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
//	}
//
//
//
//}
