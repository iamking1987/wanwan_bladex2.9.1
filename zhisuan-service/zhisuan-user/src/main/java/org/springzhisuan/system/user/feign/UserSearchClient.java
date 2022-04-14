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
package org.springzhisuan.system.user.feign;

import lombok.AllArgsConstructor;
import org.springzhisuan.core.tenant.annotation.NonDS;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.system.user.entity.User;
import org.springzhisuan.system.user.service.IUserSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户查询服务Feign实现类
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
public class UserSearchClient implements IUserSearchClient {

	private final IUserSearchService service;

	@Override
	@GetMapping(LIST_BY_USER)
	public R<List<User>> listByUser(String userId) {
		return R.data(service.listByUser(Func.toLongList(userId)));
	}

	@Override
	@GetMapping(LIST_BY_DEPT)
	public R<List<User>> listByDept(String deptId) {
		return R.data(service.listByDept(Func.toLongList(deptId)));
	}

	@Override
	@GetMapping(LIST_BY_POST)
	public R<List<User>> listByPost(String postId) {
		return R.data(service.listByPost(Func.toLongList(postId)));
	}

	@Override
	@GetMapping(LIST_BY_ROLE)
	public R<List<User>> listByRole(String roleId) {
		return R.data(service.listByRole(Func.toLongList(roleId)));
	}
}
