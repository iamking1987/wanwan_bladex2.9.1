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
package org.springzhisuan.flow.business.feign;

import lombok.AllArgsConstructor;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springzhisuan.core.tenant.annotation.NonDS;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.support.Kv;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springzhisuan.flow.core.entity.ZhisuanFlow;
import org.springzhisuan.flow.core.feign.IFlowClient;
import org.springzhisuan.flow.core.utils.TaskUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 流程远程调用实现类
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
public class FlowClient implements IFlowClient {

	private final RuntimeService runtimeService;
	private final IdentityService identityService;
	private final TaskService taskService;

	@Override
	@PostMapping(START_PROCESS_INSTANCE_BY_ID)
	public R<ZhisuanFlow> startProcessInstanceById(String processDefinitionId, String businessKey, @RequestBody Map<String, Object> variables) {
		// 设置流程启动用户
		identityService.setAuthenticatedUserId(TaskUtil.getTaskUser());
		// 开启流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variables);
		// 组装流程通用类
		ZhisuanFlow flow = new ZhisuanFlow();
		flow.setProcessInstanceId(processInstance.getId());
		return R.data(flow);
	}

	@Override
	@PostMapping(START_PROCESS_INSTANCE_BY_KEY)
	public R<ZhisuanFlow> startProcessInstanceByKey(String processDefinitionKey, String businessKey, @RequestBody Map<String, Object> variables) {
		// 设置流程启动用户
		identityService.setAuthenticatedUserId(TaskUtil.getTaskUser());
		// 开启流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
		// 组装流程通用类
		ZhisuanFlow flow = new ZhisuanFlow();
		flow.setProcessInstanceId(processInstance.getId());
		return R.data(flow);
	}

	@Override
	@PostMapping(COMPLETE_TASK)
	public R completeTask(String taskId, String processInstanceId, String comment, @RequestBody Map<String, Object> variables) {
		// 增加评论
		if (StringUtil.isNoneBlank(processInstanceId, comment)) {
			taskService.addComment(taskId, processInstanceId, comment);
		}
		// 非空判断
		if (Func.isEmpty(variables)) {
			variables = Kv.create();
		}
		// 完成任务
		taskService.complete(taskId, variables);
		return R.success("流程提交成功");
	}

	@Override
	@GetMapping(TASK_VARIABLE)
	public R<Object> taskVariable(String taskId, String variableName) {
		return R.data(taskService.getVariable(taskId, variableName));
	}

	@Override
	@GetMapping(TASK_VARIABLES)
	public R<Map<String, Object>> taskVariables(String taskId) {
		return R.data(taskService.getVariables(taskId));
	}

}
