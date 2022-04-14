package com.fun.httpclient.utils;

import com.fun.httpclient.consts.CustomHttpMethod;

import java.util.Map;

/**
 * httpClient工具类
 * @author zz
 * @date 2021/8/1
 */
public class MultiHttpUtil extends HttpRequestConfigUtil {

	/**
	 * exec请求，通过以下参数获取数据
	 * @param url   请求地址
	 */
	public static String exec(CustomHttpMethod customHttpMethod, String url) {
		return exec(customHttpMethod, url, null, getDefaultContentType(), null);
	}

	/**
	 * exec请求，通过以下参数获取数据
	 * @param url   请求地址
	 * @param header  请求头部参数
	 */
	public static String exec(CustomHttpMethod customHttpMethod, String url, Map<String, String> header) {
		return exec(customHttpMethod, url, header, getDefaultContentType(), null);
	}


	/**
	 * exec请求，通过以下参数获取数据 content-Type 默认 application/json
	 * @param url   请求地址
	 * @param body  请求内容
	 */
	public static String exec(CustomHttpMethod customHttpMethod, String url, String body) {
		return exec(customHttpMethod, url, null, getDefaultContentType(), body);
	}

	/**
	 * exec请求，通过以下参数获取数据
	 * @param url   请求地址
	 * @param contentType  请求内容体类型
	 * @param body  请求内容
	 */
	public static String exec(CustomHttpMethod customHttpMethod, String url, String contentType, String body) {
		return exec(customHttpMethod, url, null, contentType, body);
	}

	/**
	 * exec请求，通过以下参数获取数据 content-Type 默认 application/json
	 * @param url   请求地址
	 * @param header  请求头部参数
	 * @param body  请求内容
	 */
	public static String exec(CustomHttpMethod customHttpMethod, String url, Map<String, String> header, String body) {
		return exec(customHttpMethod, url, header, getDefaultContentType(), body);
	}

	/**
	 * exec请求，通过以下参数获取数据
	 * @param url   请求地址
	 * @param header  请求头部参数
	 * @param contentType  请求内容体类型
	 * @param body  请求内容
	 */
	public static String exec(CustomHttpMethod customHttpMethod, String url, Map<String, String> header, String contentType, String body) {
		return BaseHttpUtil.exec(url, customHttpMethod, header, contentType, body);
	}
}
