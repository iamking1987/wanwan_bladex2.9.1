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
package org.springzhisuan.core.context.config;

import org.springzhisuan.core.context.ZhisuanHttpHeadersGetter;
import org.springzhisuan.core.context.listener.ZhisuanServletRequestListener;
import org.springzhisuan.core.context.props.ZhisuanContextProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet 监听器自动配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ZhisuanServletListenerConfiguration {

	@Bean
	public ServletListenerRegistrationBean<?> registerCustomListener(ZhisuanContextProperties properties,
																	 ZhisuanHttpHeadersGetter httpHeadersGetter) {
		return new ServletListenerRegistrationBean<>(new ZhisuanServletRequestListener(properties, httpHeadersGetter));
	}

}
