package com.fun.common.utils.exception;

/**
 * @author zz
 * @date 2021/8/18
 */
public interface BaseErrorInfoInterface {

	/**
	 *  错误码
	 */
	String getResultCode();

	/**
	 * 错误描述
	 */
	String getResultMsg();

}
