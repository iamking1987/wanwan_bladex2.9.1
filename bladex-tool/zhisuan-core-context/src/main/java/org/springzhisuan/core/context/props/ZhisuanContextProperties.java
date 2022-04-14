/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package org.springzhisuan.core.context.props;

import lombok.Getter;
import lombok.Setter;
import org.springzhisuan.core.launch.constant.TokenConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Headers 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@ConfigurationProperties(ZhisuanContextProperties.PREFIX)
public class ZhisuanContextProperties {
	/**
	 * 配置前缀
	 */
	public static final String PREFIX = "zhisuan.context";
	/**
	 * 上下文传递的 headers 信息
	 */
	private Headers headers = new Headers();

	@Getter
	@Setter
	public static class Headers {
		/**
		 * 请求id，默认：Zhisuan-RequestId
		 */
		private String requestId = "Zhisuan-RequestId";
		/**
		 * 用于 聚合层 向调用层传递用户信息 的请求头，默认：Zhisuan-AccountId
		 */
		private String accountId = "Zhisuan-AccountId";
		/**
		 * 用于 聚合层 向调用层传递租户id 的请求头，默认：Zhisuan-TenantId
		 */
		private String tenantId = "Zhisuan-TenantId";
		/**
		 * 自定义 RestTemplate 和 Feign 透传到下层的 Headers 名称列表
		 */
		private List<String> allowed = Arrays.asList("X-Real-IP", "x-forwarded-for", "authorization", "Authorization", TokenConstant.HEADER.toLowerCase(), TokenConstant.HEADER);
	}

	/**
	 * 获取跨服务的请求头
	 *
	 * @return 请求头列表
	 */
	public List<String> getCrossHeaders() {
		List<String> headerList = new ArrayList<>();
		headerList.add(headers.getRequestId());
		headerList.add(headers.getAccountId());
		headerList.add(headers.getTenantId());
		headerList.addAll(headers.getAllowed());
		return headerList;
	}

}
