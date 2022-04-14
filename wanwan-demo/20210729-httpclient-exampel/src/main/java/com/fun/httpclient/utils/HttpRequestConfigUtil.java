package com.fun.httpclient.utils;

import com.fun.httpclient.consts.HttpConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.util.Map;

/**
 * @author zz
 * @date 2021/8/1
 */
public class HttpRequestConfigUtil {

	/**
	 * 返回默认的类容类型【application/json】
	 */
	protected static String getDefaultContentType() {
		return HttpConstants.CONTENT_TYPE_JSON;
	}

	/**
	 * 设置默认的content-type: application/json
	 */
	protected static void setContentTypeApplicationJson(HttpRequestBase httpBase) {
		httpBase.setHeader(HttpConstants.CONTENT_TYPE, HttpConstants.CONTENT_TYPE_JSON);
	}

	/**
	 * 设置content-Type
	 */
	protected static void setContentType(HttpRequestBase httpBase, String contentType) {
		httpBase.setHeader(HttpConstants.CONTENT_TYPE, contentType);
	}

	/**
	 * 设置请求体
	 */
	protected static void setHttpBody(HttpEntityEnclosingRequestBase httpRequest, String body) {
		if(StringUtils.isBlank(body)) {
			return;
		}
		StringEntity entity = new StringEntity(body, HttpConstants.CHARSET_UTF_8);
		entity.setContentEncoding(HttpConstants.CHARSET_UTF_8);
		entity.setContentType(HttpConstants.CHARSET_UTF_8);
		httpRequest.setEntity(entity);
	}

	/**
	 * 设置头部参数
	 */
	protected static void setHeader(HttpRequestBase httpBase, Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return;
		}
		for (String item : map.keySet()) {
			httpBase.setHeader(item, map.get(item));
		}
	}
}
