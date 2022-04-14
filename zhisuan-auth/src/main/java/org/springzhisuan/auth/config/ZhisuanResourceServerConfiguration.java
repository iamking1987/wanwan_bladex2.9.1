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
package org.springzhisuan.auth.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 自定义登录成功配置
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
public class ZhisuanResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	@SneakyThrows
	public void configure(HttpSecurity http) {
		http.headers().frameOptions().disable();
		http.formLogin()
			.and()
			.authorizeRequests()
			.antMatchers(
				"/actuator/**",
				"/oauth/captcha",
				"/oauth/logout",
				"/oauth/clear-cache",
				"/oauth/render/**",
				"/oauth/callback/**",
				"/oauth/revoke/**",
				"/oauth/refresh/**",
				"/token/**",
				"/mobile/**",
				"/v2/api-docs").permitAll()
			.anyRequest().authenticated().and()
			.csrf().disable();
	}

}
