package com.fun.httpclient.config;

import com.fun.httpclient.consts.HttpConstants;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * HttpClient连接工厂
 * @author zz
 * @date 2021/7/29
 */
public class HttpClientConnectFactory {

	private static PoolingHttpClientConnectionManager cm = null;

	/*
	 * 初始化连接池
	 */
	static {
		SSLContext sslcontext;
		try {
			sslcontext = createIgnoreVerifySSL();
			ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
			// 为所支持的自定义连接套接字工厂创建一个注册
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register(HttpConstants.HTTP_PROTOCOL, plainsf)
				.register(HttpConstants.HTTPS_PROTOCOL,getSSLConnectionSocketFactory(sslcontext))
				.build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(HttpConstants.MAX_TOTAL_POOL); //客户端总并行链接最大数
			cm.setDefaultMaxPerRoute(HttpConstants.MAX_CONPERROUTE); //每个主机的最大并行链接数
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	/**
	 * 获取httpclient连接
	 */
	public static CloseableHttpClient getHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
			// 配置请求的超时设置
			.setConnectionRequestTimeout(HttpConstants.CONNECTION_REQUEST_TIMEOUT)
			.setConnectTimeout(HttpConstants.CONNECT_TIMEOUT)
			.setSocketTimeout(HttpConstants.SOCKET_TIMEOUT)
			.build();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
			.setDefaultRequestConfig(requestConfig)
			.setRetryHandler(new MyHttpRequestRetryHandler())
			.setConnectionManagerShared(true)
			.build();
		return httpClient;
	}

	/**
	 * 创建SSLContext
	 */
	private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance(HttpConstants.SSL_CONTEXT);
		X509TrustManager trustManager = new X509TrustManager(){
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

			}
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

			}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}

	/**
	 * getSSLConnectionSocketFactory
	 */
	private static ConnectionSocketFactory getSSLConnectionSocketFactory(SSLContext sslcontext) {
		return new SSLConnectionSocketFactory(sslcontext,NoopHostnameVerifier.INSTANCE);
	}
}
