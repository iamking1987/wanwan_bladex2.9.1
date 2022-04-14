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
package org.springzhisuan.core.mp.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springzhisuan.core.launch.props.ZhisuanPropertySource;
import org.springzhisuan.core.mp.injector.ZhisuanSqlInjector;
import org.springzhisuan.core.mp.intercept.QueryInterceptor;
import org.springzhisuan.core.mp.plugins.ZhisuanPaginationInterceptor;
import org.springzhisuan.core.mp.plugins.SqlLogInterceptor;
import org.springzhisuan.core.mp.props.MybatisPlusProperties;
import org.springzhisuan.core.mp.resolver.PageArgumentResolver;
import org.springzhisuan.core.secure.utils.AuthUtil;
import org.springzhisuan.core.tool.constant.ZhisuanConstant;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.ObjectUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mybatis-plus 配置
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@MapperScan("org.springzhisuan.**.mapper.**")
@EnableConfigurationProperties(MybatisPlusProperties.class)
@ZhisuanPropertySource(value = "classpath:/zhisuan-mybatis.yml")
public class MybatisPlusConfiguration implements WebMvcConfigurer {

	/**
	 * 租户拦截器
	 */
	@Bean
	@ConditionalOnMissingBean(TenantLineInnerInterceptor.class)
	public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
		return new TenantLineInnerInterceptor(new TenantLineHandler() {
			@Override
			public Expression getTenantId() {
				return new StringValue(Func.toStr(AuthUtil.getTenantId(), ZhisuanConstant.ADMIN_TENANT_ID));
			}

			@Override
			public boolean ignoreTable(String tableName) {
				return true;
			}
		});
	}

	/**
	 * mybatis-plus 拦截器集合
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<QueryInterceptor[]> queryInterceptors,
														 TenantLineInnerInterceptor tenantLineInnerInterceptor,
														 MybatisPlusProperties mybatisPlusProperties) {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 配置租户拦截器
		if (mybatisPlusProperties.getTenantMode()) {
			interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
		}
		// 配置分页拦截器
		ZhisuanPaginationInterceptor paginationInterceptor = new ZhisuanPaginationInterceptor();
		// 配置自定义查询拦截器
		QueryInterceptor[] queryInterceptorArray = queryInterceptors.getIfAvailable();
		if (ObjectUtil.isNotEmpty(queryInterceptorArray)) {
			AnnotationAwareOrderComparator.sort(queryInterceptorArray);
			paginationInterceptor.setQueryInterceptors(queryInterceptorArray);
		}
		paginationInterceptor.setMaxLimit(mybatisPlusProperties.getPageLimit());
		paginationInterceptor.setOverflow(mybatisPlusProperties.getOverflow());
		paginationInterceptor.setOptimizeJoin(mybatisPlusProperties.getOptimizeJoin());
		interceptor.addInnerInterceptor(paginationInterceptor);
		return interceptor;
	}

	/**
	 * sql 日志
	 */
	@Bean
	public SqlLogInterceptor sqlLogInterceptor(MybatisPlusProperties mybatisPlusProperties) {
		return new SqlLogInterceptor(mybatisPlusProperties);
	}

	/**
	 * sql 注入
	 */
	@Bean
	@ConditionalOnMissingBean(ISqlInjector.class)
	public ISqlInjector sqlInjector() {
		return new ZhisuanSqlInjector();
	}

	/**
	 * page 解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PageArgumentResolver());
	}

}

