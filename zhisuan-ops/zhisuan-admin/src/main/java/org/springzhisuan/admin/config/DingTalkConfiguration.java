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
package org.springzhisuan.admin.config;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springzhisuan.admin.dingtalk.DingTalkNotifier;
import org.springzhisuan.admin.dingtalk.DingTalkService;
import org.springzhisuan.admin.dingtalk.MonitorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 钉钉自动配置
 *
 * @author L.cm
 */
@Configuration
@ConditionalOnProperty(value = "monitor.ding-talk.enabled", havingValue = "true")
public class DingTalkConfiguration {

	@Bean
	public DingTalkService dingTalkService(MonitorProperties properties,
										   WebClient.Builder builder) {
		return new DingTalkService(properties, builder.build());
	}

	@Bean
	public DingTalkNotifier dingTalkNotifier(MonitorProperties properties,
											 DingTalkService dingTalkService,
											 InstanceRepository repository,
											 Environment environment) {
		return new DingTalkNotifier(dingTalkService, properties, environment, repository);
	}

}
