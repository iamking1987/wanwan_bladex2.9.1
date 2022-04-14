package com.fun.httpclient.utils;

import com.alibaba.fastjson.JSON;
import com.fun.httpclient.config.HttpClientConnectFactory;
import com.fun.httpclient.consts.CustomHttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author zz
 * @date 2021/8/1
 */
public class BaseHttpUtil extends HttpRequestConfigUtil {

	/**
	 * 创建请求对象 httpRequestBase
	 */
	protected static HttpRequestBase createHttpRequestBase(CustomHttpMethod Method, String url) {
		switch (Method) {
			case POST:
				return new HttpPost(url);
			case PUT:
				return new HttpPut(url);
			case DELETE:
				return new HttpDelete(url);
			default:
				return new HttpGet(url);
		}
	}

	/**
	 * send请求
	 */
	protected static String exec(String url, CustomHttpMethod method, Map<String, String> header, String contentType, String body) {
		CloseableHttpClient httpClient = HttpClientConnectFactory.getHttpClient();
		HttpRequestBase httpBase = createHttpRequestBase(method, url);
		setHeader(httpBase, header);
		setContentType(httpBase, contentType);
		if (httpBase instanceof HttpEntityEnclosingRequestBase) {
			setHttpBody((HttpEntityEnclosingRequestBase) httpBase, body);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpBase);
			response.getStatusLine().getStatusCode();
			HttpEntity httpEntity = response.getEntity();
			return EntityUtils.toString(httpEntity,"utf-8");
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("httpGetUtil error : {}" + e.getMessage());
		}
		return JSON.toJSONString(response);
	}


	/**
	 * 处理响应值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T handleResponse(String apiResult, T defaultResult, Type type) {
		if(StringUtils.isBlank(apiResult)) {
			return defaultResult;
		}
		try {
			return (T)JSON.parseObject(apiResult, type);
		} catch (Exception e) {
			return defaultResult;
		}
	}
}
