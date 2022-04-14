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
package org.springzhisuan.core.log.props;

import lombok.Getter;
import lombok.Setter;
import org.springzhisuan.core.launch.log.ZhisuanLogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 日志配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(ZhisuanLogLevel.REQ_LOG_PROPS_PREFIX)
public class ZhisuanRequestLogProperties {

	/**
	 * 是否开启请求日志
	 */
	private Boolean enabled = true;

	/**
	 * 是否开启异常日志推送
	 */
	private Boolean errorLog = true;

	/**
	 * 日志级别配置，默认：BODY
	 */
	private ZhisuanLogLevel level = ZhisuanLogLevel.BODY;
}
