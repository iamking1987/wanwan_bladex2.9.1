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
package org.springzhisuan.auth.endpoint;

import com.wf.captcha.SpecCaptcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springzhisuan.common.cache.CacheNames;
import org.springzhisuan.core.cache.utils.CacheUtil;
import org.springzhisuan.core.jwt.JwtUtil;
import org.springzhisuan.core.jwt.props.JwtProperties;
import org.springzhisuan.core.launch.constant.TokenConstant;
import org.springzhisuan.core.redis.cache.ZhisuanRedis;
import org.springzhisuan.core.secure.ZhisuanUser;
import org.springzhisuan.core.secure.utils.AuthUtil;
import org.springzhisuan.core.tenant.annotation.NonDS;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.support.Kv;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springzhisuan.core.tool.utils.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static org.springzhisuan.core.cache.constant.CacheConstant.*;

/**
 * ZhisuanEndPoint
 *
 * @author Chill
 */
@NonDS
@Slf4j
@RestController
@AllArgsConstructor
public class ZhisuanTokenEndPoint {

	private final ZhisuanRedis zhisuanRedis;
	private final JwtProperties jwtProperties;

	@GetMapping("/oauth/user-info")
	public R<Authentication> currentUser(Authentication authentication) {
		return R.data(authentication);
	}

	@GetMapping("/oauth/captcha")
	public Kv captcha() {
		SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
		String verCode = specCaptcha.text().toLowerCase();
		String key = StringUtil.randomUUID();
		// 存入redis并设置过期时间为30分钟
		zhisuanRedis.setEx(CacheNames.CAPTCHA_KEY + key, verCode, Duration.ofMinutes(30));
		// 将key和base64返回给前端
		return Kv.create().set("key", key).set("image", specCaptcha.toBase64());
	}

	@GetMapping("/oauth/logout")
	public Kv logout() {
		ZhisuanUser user = AuthUtil.getUser();
		if (user != null && jwtProperties.getState()) {
			String token = JwtUtil.getToken(WebUtil.getRequest().getHeader(TokenConstant.HEADER));
			JwtUtil.removeAccessToken(user.getTenantId(), String.valueOf(user.getUserId()), token);
		}
		return Kv.create().set("success", "true").set("msg", "success");
	}

	@GetMapping("/oauth/clear-cache")
	public Kv clearCache() {
		CacheUtil.clear(BIZ_CACHE);
		CacheUtil.clear(USER_CACHE);
		CacheUtil.clear(DICT_CACHE);
		CacheUtil.clear(FLOW_CACHE);
		CacheUtil.clear(SYS_CACHE);
		CacheUtil.clear(PARAM_CACHE);
		CacheUtil.clear(RESOURCE_CACHE);
		CacheUtil.clear(MENU_CACHE);
		CacheUtil.clear(DICT_CACHE, Boolean.FALSE);
		CacheUtil.clear(MENU_CACHE, Boolean.FALSE);
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		CacheUtil.clear(PARAM_CACHE, Boolean.FALSE);
		return Kv.create().set("success", "true").set("msg", "success");
	}

}
