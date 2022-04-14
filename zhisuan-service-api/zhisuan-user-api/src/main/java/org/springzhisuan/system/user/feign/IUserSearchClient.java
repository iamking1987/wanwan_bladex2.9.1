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


import org.springzhisuan.core.launch.constant.AppConstant;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.system.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * User Search Feign接口类
 *
 * @author Chill
 */
@FeignClient(
	value = AppConstant.APPLICATION_USER_NAME
)
public interface IUserSearchClient {

	String API_PREFIX = "/client";
	String LIST_BY_USER = API_PREFIX + "/user/list-by-user";
	String LIST_BY_DEPT = API_PREFIX + "/user/list-by-dept";
	String LIST_BY_POST = API_PREFIX + "/user/list-by-post";
	String LIST_BY_ROLE = API_PREFIX + "/user/list-by-role";

	/**
	 * 根据用户ID查询用户列表
	 *
	 * @param userId 用户ID
	 * @return 用户列表
	 */
	@GetMapping(LIST_BY_USER)
	R<List<User>> listByUser(@RequestParam("userId") String userId);

	/**
	 * 根据部门ID查询用户列表
	 *
	 * @param deptId 部门ID
	 * @return 用户列表
	 */
	@GetMapping(LIST_BY_DEPT)
	R<List<User>> listByDept(@RequestParam("deptId") String deptId);

	/**
	 * 根据岗位ID查询用户列表
	 *
	 * @param postId 岗位ID
	 * @return 用户列表
	 */
	@GetMapping(LIST_BY_POST)
	R<List<User>> listByPost(@RequestParam("postId") String postId);

	/**
	 * 根据角色ID查询用户列表
	 *
	 * @param roleId 角色ID
	 * @return 用户列表
	 */
	@GetMapping(LIST_BY_ROLE)
	R<List<User>> listByRole(@RequestParam("roleId") String roleId);

}
