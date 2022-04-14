package com.fun.common.utils.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

	private HttpClientUtils(){}

	public static final int CONNECTION_REQUEST_TIMEOUT = 1000;

	/**
	 * 连接超时时间
	 */
	public static final int CONNECTION_TIMEOUT_MS = 5 * 1000;

	/**
	 * 读取数据超时时间
	 */
	public static final int SO_TIMEOUT_MS = 10 * 1000;

	public static final String utf8 = "UTF-8";
	public static final String application_json = "application/json";
	public static final String gbk = "GBK";

	/**
	 * 简单get调用
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> header)
		throws IOException, URISyntaxException {
		return get(url, params, utf8, header);
	}

	/**
	 * 简单get调用
	 */
	public static String get(String url, Map<String, String> params, String charset, Map<String, String> header)
		throws IOException, URISyntaxException {
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
	 * 简单post调用
	 *
	 * @param url 请求地址
	 * @param params 表单参数
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> header)
		throws URISyntaxException, IOException {
		return post(url, params, utf8, header);
	}

	/**
	 * 简单post调用
	 */
	public static String post(String url, Map<String, String> params, String charset, Map<String, String> header)
		throws URISyntaxException, IOException {

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
	 * post调用-json参数-map参数
	 */
	public static String postJSON(String url, Map<String, String> params, Map<String, String> header) throws IOException, URISyntaxException {
		return postJSON(url, params, utf8, header);
	}

	/**
	 * post调用-json参数-String参数
	 */
	public static String postJSON(String url, String jsonString, Map<String, String> header) throws IOException, URISyntaxException {
		return postJSON(url, jsonString, utf8, header);
	}

	public static String postJSON(String url, String jsonString, String charset, Map<String, String> header)
		throws URISyntaxException, IOException {

		HttpClient client = buildHttpClient(true);
		HttpPost postMethod = buildHttpJSONPost(url, jsonString, charset);
		setHeader(postMethod, header);
		HttpResponse response = client.execute(postMethod);
		assertStatus(response);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return EntityUtils.toString(entity, charset);
		}

		return null;
	}

	public static String postJSON(String url, Map params, String charset, Map<String, String> header)
		throws URISyntaxException, IOException {

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
//				.setRetryHandler(new DefaultHttpRequestRetryHandler(3,true))
				.setRetryHandler(new DefaultHttpRequestRetryHandler())
				.setKeepAliveStrategy(getKeepAliveStrategy())
				//连接池
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
		throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		if (params != null) {
			List<NameValuePair> formparams = new ArrayList<>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			HttpEntity he = new UrlEncodedFormEntity(formparams, charset);
			post.setEntity(he);
		}
		return post;
	}

	public static HttpPost buildHttpJSONPost(String url, Map<String, String> params, String charset)
		throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setJSONHttpMethod(post);
		if (params != null) {
			String json = JSON.toJSONString(params);
			System.out.println(json);
			StringEntity stringEntity = new StringEntity(json, utf8);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, application_json));
			post.setEntity(stringEntity);
		}
		return post;
	}

	public static HttpPost buildHttpJSONPost(String url, String jsonString, String charset)
		throws UnsupportedEncodingException, URISyntaxException {
		Assert.notNull(url, "构建HttpPost时,url不能为null");
		HttpPost post = new HttpPost(url);
		setJSONHttpMethod(post);
		if (!StringUtils.isBlank(jsonString)) {
			StringEntity stringEntity = new StringEntity(jsonString, utf8);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, application_json));
			post.setEntity(stringEntity);
		}
		return post;
	}


	/**
	 * 构建httpGet对象
	 */
	public static HttpGet buildHttpGet(String url, Map<String, String> params, String charset)
		throws URISyntaxException {
		Assert.notNull(url, "构建HttpGet时,url不能为null");
		return new HttpGet(buildGetUrl(url, params, charset));
	}

	/**
	 * build getUrl str
	 */
	private static String buildGetUrl(String url, Map<String, String> params, String charset) {
		StringBuilder uriStr = new StringBuilder(url);
		if (params != null) {
			List<NameValuePair> ps = new ArrayList<NameValuePair>();

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
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, utf8);// setting
	}

	/* 设置HttpMethod通用配置
	 *
	 * @param httpMethod
	 */
	public static void setJSONHttpMethod(HttpRequestBase httpMethod) {
		httpMethod.setHeader(HTTP.CONTENT_ENCODING, utf8);// setting
		httpMethod.setHeader(HTTP.CONTENT_TYPE, application_json);// setting
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
			/*
			 * 从连接池中获取连接的超时时间，假设：连接池中已经使用的连接数等于setMaxTotal，新来的线程在等待1*1000
			 * 后超时，错误内容：org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
			 */
			.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
			/*
			 * 指的是连接上一个url，获取response的返回等待时间，假设：url程序中存在阻塞、或者response
			 * 返回的文件内容太大，在指定的时间内没有读完，则出现
			 * java.net.SocketTimeoutException: Read timed out
			 */
			.setSocketTimeout(SO_TIMEOUT_MS)
			/*
			 * 这定义了通过网络与服务器建立连接的超时时间。
			 * Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，
			 * 此处设置为2秒。假设：访问一个IP，192.168.10.100，这个IP不存在或者响应太慢，那么将会返回
			 * java.net.SocketTimeoutException: connect timed out
			 */
			.setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
	}

	/**
	 * http初始化keep-Alive配置
	 */
	public static ConnectionKeepAliveStrategy getKeepAliveStrategy(){
		return (response, context) -> {
			HeaderElementIterator it = new BasicHeaderElementIterator
				(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase
					("timeout")) {
					return Long.parseLong(value) * 1000;
				}
			}
			return 20 * 1000;//如果没有约定，则默认定义时长为20s
		};
	}

	/**
	 * 强验证必须是200状态否则报异常
	 */
	static void assertStatus(HttpResponse res) throws IOException {
		Assert.notNull(res, "http响应对象为null");
		Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
		switch (res.getStatusLine().getStatusCode()) {
			case HttpStatus.SC_OK:
				break;
			default:
				throw new IOException("服务器响应状态异常,失败.");
		}
	}

	/*public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		System.out.println(get("http://www.baidu.com", new HashMap<String, String>(), null));
	}*/
}
