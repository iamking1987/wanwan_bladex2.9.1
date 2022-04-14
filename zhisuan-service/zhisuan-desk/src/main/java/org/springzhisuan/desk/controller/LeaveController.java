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
package org.springzhisuan.desk.controller;

import lombok.AllArgsConstructor;
import org.springzhisuan.common.cache.CacheNames;
import org.springzhisuan.core.boot.ctrl.ZhisuanController;
import org.springzhisuan.core.tenant.annotation.NonDS;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.desk.entity.ProcessLeave;
import org.springzhisuan.desk.service.ILeaveService;
import org.springzhisuan.system.user.cache.UserCache;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 控制器
 *
 * @author Chill
 */
@NonDS
@ApiIgnore
@RestController
@RequestMapping("/process/leave")
@AllArgsConstructor
public class LeaveController extends ZhisuanController implements CacheNames {

	private final ILeaveService leaveService;

	/**
	 * 详情
	 *
	 * @param businessId 主键
	 */
	@GetMapping("detail")
	public R<ProcessLeave> detail(Long businessId) {
		ProcessLeave detail = leaveService.getById(businessId);
		detail.getFlow().setAssigneeName(UserCache.getUser(detail.getCreateUser()).getName());
		return R.data(detail);
	}

	/**
	 * 新增或修改
	 *
	 * @param leave 请假信息
	 */
	@PostMapping("start-process")
	public R startProcess(@RequestBody ProcessLeave leave) {
		return R.status(leaveService.startProcess(leave));
	}

}
