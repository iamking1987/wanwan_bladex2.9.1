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
package org.springzhisuan.core.ribbon.support;

import org.springzhisuan.core.ribbon.rule.DiscoveryEnabledRule;
import org.springzhisuan.core.ribbon.rule.MetadataAwareRule;
import org.springzhisuan.core.ribbon.rule.WeightAwareRule;
import org.springzhisuan.core.ribbon.utils.BeanUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Zhisuan ribbon rule auto configuration.
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@EnableConfigurationProperties(ZhisuanRibbonRuleProperties.class)
public class ZhisuanRibbonRuleAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = ZhisuanRibbonRuleProperties.PROPERTIES_PREFIX + ".enabled", matchIfMissing = true)
	public static class MetadataAwareRuleConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		public DiscoveryEnabledRule discoveryEnabledRule() {
			return new MetadataAwareRule();
		}
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(ZhisuanRibbonRuleProperties.PROPERTIES_PREFIX + ".weight")
	public static class WeightAwareRuleConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
		public DiscoveryEnabledRule discoveryEnabledRule() {
			return new WeightAwareRule();
		}
	}


	@Bean
	public BeanUtil beanUtil(){
		return new BeanUtil();
	}

}
