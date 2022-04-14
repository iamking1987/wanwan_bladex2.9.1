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
package org.springzhisuan.auth.service;

import lombok.Getter;
import org.springzhisuan.core.tool.support.Kv;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 用户信息拓展
 *
 * @author Chill
 */
@Getter
public class ZhisuanUserDetails extends User {

	/**
	 * 用户id
	 */
	private final Long userId;
	/**
	 * 租户ID
	 */
	private final String tenantId;
	/**
	 * 第三方认证ID
	 */
	private final String oauthId;
	/**
	 * 昵称
	 */
	private final String name;
	/**
	 * 真名
	 */
	private final String realName;
	/**
	 * 账号
	 */
	private final String account;
	/**
	 * 部门id
	 */
	private final String deptId;
	/**
	 * 岗位id
	 */
	private final String postId;
	/**
	 * 角色id
	 */
	private final String roleId;
	/**
	 * 角色名
	 */
	private final String roleName;
	/**
	 * 头像
	 */
	private final String avatar;
	/**
	 * 用户详情
	 */
	private final Kv detail;

	public ZhisuanUserDetails(Long userId, String tenantId, String oauthId, String name, String realName, String deptId, String postId, String roleId, String roleName, String avatar, String username, String password, Kv detail, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId = userId;
		this.tenantId = tenantId;
		this.oauthId = oauthId;
		this.name = name;
		this.realName = realName;
		this.account = username;
		this.deptId = deptId;
		this.postId = postId;
		this.roleId = roleId;
		this.roleName = roleName;
		this.avatar = avatar;
		this.detail = detail;
	}

}
