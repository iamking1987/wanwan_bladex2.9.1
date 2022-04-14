package com.fun.httpclient.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简单httpClient工具类
 *
 * @author wanwan 2022/2/8
 */
public class HttpClientUtil {

	private HttpClientUtil(){}

	/**
	 * 连接超时时间
	 */
	public static final int CONNECTION_TIMEOUT_MS = 5000;

	/**
	 * 读取数据超时时间
	 */
	public static final int SO_TIMEOUT_MS = 5000;

	public static final String utf8 = "UTF-8";
	public static final String GBK = "GBK";

	public static final String application_json = "application/json";


	/**
	 * GET FORM(utf-8)
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> header)
		throws IOException {
		return get(url, params, utf8, header);
	}

	/**
	 * GET FORM
	 */
	public static String get(String url, Map<String, String> params, String charset, Map<String, String> header)
		throws IOException {
		HttpClient client = buildHttpClient(true);
		HttpGet get = buildHttpGet(url, params, charset);
		setHeader(get, header);
		HttpResponse response = client.execute(get);
		assertStatus(response);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return EntityUtils.toString(entity, charset);
		}
		return null;
	}

	/**
	 * POST FORM(utf-8)
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> header)
		throws IOException {
		return post(url, params, utf8, header);
	}

	/**
	 * POST JSON(utf-8)
	 */
	public static String postJSON(String url, Map<String, String> params, Map<String, String> header) throws IOException {
		return postJSON(url, params, utf8, header);
	}

	/**
	 * POST FORM
	 */
	public static String post(String url, Map<String, String> params, String charset, Map<String, String> header)
		throws IOException {
		HttpClient client = buildHttpClient(true);
		HttpPost postMethod = buildHttpPost(url, params, charset);
		setHeader(postMethod, header);
		HttpResponse response = client.execute(postMethod);
		assertStatus(response);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return EntityUtils.toString(entity, charset);
		}
		return null;
	}

	/**
	 * POST JSON
	 */
	public static String postJSON(String url, Map params, String charset, Map<String, String> header)
		throws IOException {
		HttpClient client = buildHttpClient(true);
		HttpPost postMethod = buildHttpJSONPost(url, params, charset);
		setHeader(postMethod, header);
		HttpResponse response = client.execute(postMethod);
		assertStatus(response);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return EntityUtils.toString(entity, charset);
		}
		return null;
	}

	/**
	 * 创建HttpClient
	 *
	 * @param isMultiThread 是否多线程
	 */
	public static HttpClient buildHttpClient(boolean isMultiThread) {
		CloseableHttpClient client;
		if (isMultiThread)
			client = HttpClientBuilder.create()
				//构建超时时间等配置
				.setDefaultRequestConfig(buildRequestConfig())
				//设置重试次数，默认三次且关闭
				.setRetryHandler(new DefaultHttpRequestRetryHandler())
				//.setRetryHandler(new DefaultHttpRequestRetryHandler(3,true))
				//总连接
				.setConnectionManager(
					new PoolingHttpClientConnectionManager()).build();
		else
			client = HttpClientBuilder.create().build();
		return client;
	}

	/**
	 * 构建httpPost对象
	 */
	public static HttpPost buildHttpPost(String url, Map<String, String> params, String charset)
		throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		if (params != null) {
			List<NameValuePair> formParams = new ArrayList<>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			HttpEntity he = new UrlEncodedFormEntity(formParams, charset);
			post.setEntity(he);
		}
		return post;
	}

	public static HttpPost buildHttpJSONPost(String url, Map<String, String> params, String charset) {
		HttpPost post = new HttpPost(url);
		setJSONHttpMethod(post);
		if (params != null) {
			String json = JSON.toJSONString(params);
			System.out.println(json);
			StringEntity stringEntity = new StringEntity(json, charset);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, application_json));
			post.setEntity(stringEntity);
		}
		return post;
	}


	/**
	 * 构建httpGet对象
	 */
	public static HttpGet buildHttpGet(String url, Map<String, String> params, String charset) {
		Assert.notNull(url, "构建HttpGet时,url不能为null");
		return new HttpGet(buildGetUrl(url, params, charset));
	}

	/**
	 * build getUrl str
	 */
	private static String buildGetUrl(String url, Map<String, String> params, String charset) {
		StringBuilder uriStr = new StringBuilder(url);
		if (params != null) {
			List<NameValuePair> ps = new ArrayList<>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				ps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			uriStr.append("?");
			uriStr.append(URLEncodedUtils.format(ps, charset));
		}
		return uriStr.toString();
	}

	/**
	 * 设置HttpMethod通用配置
	 *
	 * @param httpMethod 设置编码
	 */
	public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, utf8);
	}

	/* 设置HttpMethod通用配置
	 *
	 * @param httpMethod
	 */
	public static void setJSONHttpMethod(HttpRequestBase httpMethod) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, utf8);
		httpMethod.setHeader(HTTP.CONTENT_TYPE, application_json);
	}

	/**
	 * 给请求添加头部
	 */
	public static void setHeader(HttpRequestBase httpMethod, Map<String, String> header) {
		if (! ObjectUtils.isEmpty(header)) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 设置成消息体的长度 setting MessageBody length
	 */
	public static void setContentLength(HttpRequestBase httpMethod, HttpEntity he) {
		if (he == null) {
			return;
		}
		httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
	}

	/**
	 * 构建公用RequestConfig
	 */
	public static RequestConfig buildRequestConfig() {
		// 设置请求和传输超时时间
		return RequestConfig.custom()
			.setSocketTimeout(SO_TIMEOUT_MS)
			.setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
	}

	/**
	 * 强验证必须是200状态否则报异常
	 */
	static void assertStatus(HttpResponse res) throws IOException {
		Assert.notNull(res, "http响应对象为null");
		Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
		if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new IOException("服务器响应状态异常,失败.");
		}
	}
}
