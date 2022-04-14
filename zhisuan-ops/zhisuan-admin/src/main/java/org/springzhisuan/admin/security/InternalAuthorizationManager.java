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
package org.springzhisuan.admin.security;

import org.springzhisuan.core.launch.utils.INetUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * 内网认证管理，内网放行，外网认证
 *
 * @author L.cm
 */
public class InternalAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
	private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
		return Mono.just(getAuthorizationDecision(context));
	}

	private static AuthorizationDecision getAuthorizationDecision(AuthorizationContext context) {
		return new AuthorizationDecision(isInternalNet(context));
	}

	/**
	 * 判断是否内网 ip 请求
	 *
	 * @param context AuthorizationContext
	 * @return 是否内网 ip
	 */
	private static boolean isInternalNet(AuthorizationContext context) {
		ServerHttpRequest request = Optional.ofNullable(context)
			.map(AuthorizationContext::getExchange)
			.map(ServerWebExchange::getRequest)
			.orElse(null);
		if (request == null) {
			return false;
		}
		HttpHeaders headers = request.getHeaders();
		// 如果没有 X-Forwarded-For 代表为 admin 拉取
		if (!headers.containsKey(HEADER_X_FORWARDED_FOR)) {
			return true;
		}
		return Optional.of(request)
			.map(ServerHttpRequest::getRemoteAddress)
			.map(InetSocketAddress::getAddress)
			.map(INetUtil::isInternalIp)
			.orElse(false);
	}

}
