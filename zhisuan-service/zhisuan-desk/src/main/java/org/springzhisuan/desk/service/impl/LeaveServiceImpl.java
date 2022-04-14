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
package org.springzhisuan.desk.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springzhisuan.core.log.exception.ServiceException;
import org.springzhisuan.core.mp.base.BaseServiceImpl;
import org.springzhisuan.core.secure.utils.AuthUtil;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.support.Kv;
import org.springzhisuan.core.tool.utils.DateUtil;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.desk.entity.ProcessLeave;
import org.springzhisuan.desk.mapper.LeaveMapper;
import org.springzhisuan.desk.service.ILeaveService;
import org.springzhisuan.flow.core.constant.ProcessConstant;
import org.springzhisuan.flow.core.entity.ZhisuanFlow;
import org.springzhisuan.flow.core.feign.IFlowClient;
import org.springzhisuan.flow.core.utils.FlowUtil;
import org.springzhisuan.flow.core.utils.TaskUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Slf4j
@Service
@AllArgsConstructor
public class LeaveServiceImpl extends BaseServiceImpl<LeaveMapper, ProcessLeave> implements ILeaveService {

	private final IFlowClient flowClient;

	@Override
	@Transactional(rollbackFor = Exception.class)
	// @GlobalTransactional
	public boolean startProcess(ProcessLeave leave) {
		String businessTable = FlowUtil.getBusinessTable(ProcessConstant.LEAVE_KEY);
		if (Func.isEmpty(leave.getId())) {
			// 保存leave
			leave.setApplyTime(DateUtil.now());
			save(leave);
			// 启动流程
			Kv variables = Kv.create()
				.set(ProcessConstant.TASK_VARIABLE_CREATE_USER, AuthUtil.getUserName())
				.set("taskUser", TaskUtil.getTaskUser(leave.getTaskUser()))
				.set("days", DateUtil.between(leave.getStartTime(), leave.getEndTime()).toDays());
			R<ZhisuanFlow> result = flowClient.startProcessInstanceById(leave.getProcessDefinitionId(), FlowUtil.getBusinessKey(businessTable, String.valueOf(leave.getId())), variables);
			if (result.isSuccess()) {
				log.debug("流程已启动,流程ID:" + result.getData().getProcessInstanceId());
				// 返回流程id写入leave
				leave.setProcessInstanceId(result.getData().getProcessInstanceId());
				updateById(leave);
			} else {
				throw new ServiceException("开启流程失败");
			}
		} else {

			updateById(leave);
		}
		return true;
	}

}
