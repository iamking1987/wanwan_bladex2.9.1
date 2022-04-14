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


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 监控配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("monitor")
public class MonitorProperties {
	private DingTalk dingTalk = new DingTalk();

	@Getter
	@Setter
	public static class DingTalk {
		/**
		 * 启用钉钉告警，默认为 true
		 */
		private boolean enabled = false;
		/**
		 * 钉钉机器人 token
		 */
		private String accessToken;
		/**
		 * 签名：如果有 secret 则进行签名，兼容老接口
		 */
		private String secret;
		/**
		 * 地址配置
		 */
		private String link;
		private Service service = new Service();
	}

	@Getter
	@Setter
	public static class Service {
		/**
		 * 服务 状态 title
		 */
		private String title = "服务状态通知";
	}
}
