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
package org.springzhisuan.core.datascope.config;

import lombok.AllArgsConstructor;
import org.springzhisuan.core.datascope.interceptor.DataScopeInterceptor;
import org.springzhisuan.core.datascope.props.DataScopeProperties;
import org.springzhisuan.core.datascope.handler.ZhisuanDataScopeHandler;
import org.springzhisuan.core.datascope.handler.ZhisuanScopeModelHandler;
import org.springzhisuan.core.datascope.handler.DataScopeHandler;
import org.springzhisuan.core.datascope.handler.ScopeModelHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据权限配置类
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(DataScopeProperties.class)
public class DataScopeConfiguration {

	private final JdbcTemplate jdbcTemplate;

	@Bean
	@ConditionalOnMissingBean(ScopeModelHandler.class)
	public ScopeModelHandler scopeModelHandler() {
		return new ZhisuanScopeModelHandler(jdbcTemplate);
	}

	@Bean
	@ConditionalOnBean(ScopeModelHandler.class)
	@ConditionalOnMissingBean(DataScopeHandler.class)
	public DataScopeHandler dataScopeHandler(ScopeModelHandler scopeModelHandler) {
		return new ZhisuanDataScopeHandler(scopeModelHandler);
	}

	@Bean
	@ConditionalOnBean(DataScopeHandler.class)
	@ConditionalOnMissingBean(DataScopeInterceptor.class)
	public DataScopeInterceptor interceptor(DataScopeHandler dataScopeHandler, DataScopeProperties dataScopeProperties) {
		return new DataScopeInterceptor(dataScopeHandler, dataScopeProperties);
	}

}
