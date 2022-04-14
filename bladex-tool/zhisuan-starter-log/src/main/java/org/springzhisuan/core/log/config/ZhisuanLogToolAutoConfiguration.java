/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
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
 *  Author: Chill 庄骞 (smallchill@163.com)
 */

package org.springzhisuan.core.log.config;

import org.springzhisuan.core.launch.props.ZhisuanProperties;
import org.springzhisuan.core.launch.props.ZhisuanPropertySource;
import org.springzhisuan.core.launch.server.ServerInfo;
import org.springzhisuan.core.log.aspect.ApiLogAspect;
import org.springzhisuan.core.log.aspect.LogTraceAspect;
import org.springzhisuan.core.log.event.ApiLogListener;
import org.springzhisuan.core.log.event.ErrorLogListener;
import org.springzhisuan.core.log.event.UsualLogListener;
import org.springzhisuan.core.log.feign.ILogClient;
import org.springzhisuan.core.log.filter.LogTraceFilter;
import org.springzhisuan.core.log.logger.ZhisuanLogger;
import org.springzhisuan.core.log.props.ZhisuanRequestLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

/**
 * 日志工具自动配置
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@EnableConfigurationProperties(ZhisuanRequestLogProperties.class)
@ZhisuanPropertySource(value = "classpath:/zhisuan-log.yml")
public class ZhisuanLogToolAutoConfiguration {

	@Bean
	public ApiLogAspect apiLogAspect() {
		return new ApiLogAspect();
	}

	@Bean
	public LogTraceAspect logTraceAspect() {
		return new LogTraceAspect();
	}

	@Bean
	public ZhisuanLogger zhisuanLogger() {
		return new ZhisuanLogger();
	}

	@Bean
	public FilterRegistrationBean<LogTraceFilter> logTraceFilterRegistration() {
		FilterRegistrationBean<LogTraceFilter> registration = new FilterRegistrationBean<>();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new LogTraceFilter());
		registration.addUrlPatterns("/*");
		registration.setName("LogTraceFilter");
		registration.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registration;
	}

	@Bean
	@ConditionalOnMissingBean(name = "apiLogListener")
	public ApiLogListener apiLogListener(ILogClient logService, ServerInfo serverInfo, ZhisuanProperties zhisuanProperties) {
		return new ApiLogListener(logService, serverInfo, zhisuanProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "errorEventListener")
	public ErrorLogListener errorEventListener(ILogClient logService, ServerInfo serverInfo, ZhisuanProperties zhisuanProperties) {
		return new ErrorLogListener(logService, serverInfo, zhisuanProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "usualEventListener")
	public UsualLogListener usualEventListener(ILogClient logService, ServerInfo serverInfo, ZhisuanProperties zhisuanProperties) {
		return new UsualLogListener(logService, serverInfo, zhisuanProperties);
	}

}
