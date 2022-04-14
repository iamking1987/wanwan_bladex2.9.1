package com.fun.httpclient.config;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * 重试处理器
 * @author zz
 * @date 2021/8/1
 */
public class MyHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {

	private  int retry_times = 3;

	public MyHttpRequestRetryHandler() {
		super();
	}

	/**
	 * 覆盖默认的重试次数及重试标志
	 */
	public MyHttpRequestRetryHandler(int retry_times) {
		super();
		this.retry_times = retry_times;
	}

	/**
	 * 检查重试次数 检查连接异常原因
	 */
	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		if (executionCount >= retry_times) {// 如果已经重试了3次，就放弃
			return false;
		}
		if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
			return true;
		}
		if (exception instanceof InterruptedIOException) {// 超时
			return true;
		}
		if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
			return false;
		}
		if (exception instanceof UnknownHostException) {// 目标服务器不可达
			return false;
		}
		if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
			return false;
		}
		if (exception instanceof SSLException) {// ssl握手异常
			return false;
		}
		HttpClientContext clientContext = HttpClientContext.adapt(context);
		HttpRequest request = clientContext.getRequest();
		// 如果请求是幂等的，就再次尝试
		if (!(request instanceof HttpEntityEnclosingRequest)) {
			return true;
		}
		return false;
	}
}
