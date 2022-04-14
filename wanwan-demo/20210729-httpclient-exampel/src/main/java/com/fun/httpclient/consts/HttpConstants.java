package com.fun.httpclient.consts;

/**
 * @author zz
 * @date 2021/7/29
 */
public interface HttpConstants {

	/**
	 * 连接池最大连接数
	 */
	int MAX_TOTAL_POOL = 256;

	/**
	 * 每路连接最多连接数
	 */
	int MAX_CONPERROUTE = 32;

	/**
	 * socket超时时间
	 */
	int SOCKET_TIMEOUT = 60 * 1000;

	/**
	 * 连接请求超时时间
	 */
	int CONNECTION_REQUEST_TIMEOUT = 5 * 1000;

	/**
	 * 连接超时时间
	 */
	int CONNECT_TIMEOUT = 5 * 1000;

	/**
	 * http协议
	 */
	String HTTP_PROTOCOL = "http";

	/**
	 * https协议
	 */
	String HTTPS_PROTOCOL = "https";

	/**
	 * sslv3
	 */
	String SSL_CONTEXT = "SSLv3";

	/**
	 * utf-8编码
	 */
	String CHARSET_UTF_8 = "UTF-8";

	/**
	 * application/json
	 */
	String CONTENT_TYPE_JSON = "application/json";

	/**
	 * content-type
	 */
	String CONTENT_TYPE = "Content-Type";
}
