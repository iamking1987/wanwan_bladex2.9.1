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
package org.springzhisuan.admin.dingtalk;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 服务上下线告警
 *
 * <p>
 * 注意：AbstractStatusChangeNotifier 这个事件有毛病
 * </p>
 *
 * @author L.cm
 */
@Slf4j
public class DingTalkNotifier extends AbstractEventNotifier {
	private final DingTalkService dingTalkService;
	private final MonitorProperties properties;
	private final Environment environment;
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public DingTalkNotifier(DingTalkService dingTalkService, MonitorProperties properties,
							Environment environment, InstanceRepository repository) {
		super(repository);
		this.dingTalkService = dingTalkService;
		this.properties = properties;
		this.environment = environment;
	}

	@NonNull
	@Override
	protected Mono<Void> doNotify(@NonNull InstanceEvent event, @NonNull Instance instance) {
		if (event instanceof InstanceStatusChangedEvent) {
			// 构造请求结构
			return createAndPushMsg(event, instance);
		}
		return Mono.empty();
	}

	private Mono<Void> createAndPushMsg(InstanceEvent event, Instance instance) {
		Registration registration = instance.getRegistration();
		// 服务名
		String appName = registration.getName();
		// 服务地址
		String serviceUrl = registration.getServiceUrl();
		StatusInfo status = instance.getStatusInfo();
		// 时间
		LocalDateTime localDateTime = LocalDateTime.ofInstant(event.getTimestamp(), ZoneId.systemDefault());
		MonitorProperties.DingTalk dingTalk = properties.getDingTalk();
		String title = dingTalk.getService().getTitle();

		String message = "## **" + title + "**\n" +
			"#### **【服务】** " + appName + "\n" +
			"#### **【环境】** " + environment.getActiveProfiles()[0] + "\n" +
			"#### **【地址】** " + serviceUrl + "\n" +
			"#### **【状态】** " + statusCn(status) + "\n" +
			"#### **【时间】** " + DATETIME_FORMATTER.format(localDateTime) + "\n" +
			"#### **【详情】** " + dingTalk.getLink() + "\n";

		return dingTalkService.pushMsg(title, message);
	}

	private String statusCn(StatusInfo status) {
		if (status.isUp()) {
			return "应用上线（IS UP）";
		} else if (status.isDown()) {
			return "应用宕机（IS DOWN）";
		} else if (status.isOffline()) {
			return "应用掉线（IS OFFLINE）";
		} else if (status.isUnknown()) {
			return "未知状态（UNKNOWN）";
		} else {
			return "异常状态";
		}
	}
}
