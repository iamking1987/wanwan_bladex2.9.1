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
package org.springzhisuan.core.context;

import lombok.RequiredArgsConstructor;
import org.springzhisuan.core.context.props.ZhisuanContextProperties;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springzhisuan.core.tool.utils.ThreadLocalUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.util.function.Function;

import static org.springzhisuan.core.tool.constant.ZhisuanConstant.CONTEXT_KEY;

/**
 * zhisuan servlet 上下文，跨线程失效
 *
 * @author L.cm
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ZhisuanServletContext implements ZhisuanContext {
	private final ZhisuanContextProperties contextProperties;
	private final ZhisuanHttpHeadersGetter httpHeadersGetter;

	@Nullable
	@Override
	public String getRequestId() {
		return get(contextProperties.getHeaders().getRequestId());
	}

	@Nullable
	@Override
	public String getAccountId() {
		return get(contextProperties.getHeaders().getAccountId());
	}

	@Nullable
	@Override
	public String getTenantId() {
		return get(contextProperties.getHeaders().getTenantId());
	}

	@Nullable
	@Override
	public String get(String ctxKey) {
		HttpHeaders headers = ThreadLocalUtil.getIfAbsent(CONTEXT_KEY, httpHeadersGetter::get);
		if (headers == null || headers.isEmpty()) {
			return null;
		}
		return headers.getFirst(ctxKey);
	}

	@Nullable
	@Override
	public <T> T get(String ctxKey, Function<String, T> function) {
		String ctxValue = get(ctxKey);
		if (StringUtil.isBlank(ctxValue)) {
			return null;
		}
		return function.apply(ctxKey);
	}

}
