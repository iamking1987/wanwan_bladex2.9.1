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
package org.springzhisuan.flow.engine.utils;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.springzhisuan.core.cache.utils.CacheUtil;
import org.springzhisuan.core.tool.utils.BeanUtil;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.SpringUtil;
import org.springzhisuan.core.tool.utils.StringPool;
import org.springzhisuan.flow.engine.entity.FlowProcess;
import org.springzhisuan.system.cache.DictCache;

import static org.springzhisuan.core.cache.constant.CacheConstant.FLOW_CACHE;

/**
 * 流程缓存
 *
 * @author Chill
 */
public class FlowCache {

	private static final String FLOW_DEFINITION_ID = "definition:id:";
	private static RepositoryService repositoryService;

	private static RepositoryService getRepositoryService() {
		if (repositoryService == null) {
			repositoryService = SpringUtil.getBean(RepositoryService.class);
		}
		return repositoryService;
	}

	/**
	 * 获得流程定义对象
	 *
	 * @param processDefinitionId 流程对象id
	 * @return
	 */
	public static FlowProcess getProcessDefinition(String processDefinitionId) {
		return CacheUtil.get(FLOW_CACHE, FLOW_DEFINITION_ID, processDefinitionId, () -> {
			ProcessDefinition processDefinition = getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
			ProcessDefinitionEntityImpl processDefinitionEntity = BeanUtil.copy(processDefinition, ProcessDefinitionEntityImpl.class);
			return new FlowProcess(processDefinitionEntity);
		});
	}

	/**
	 * 获取流程类型名
	 *
	 * @param category 流程类型
	 * @return
	 */
	public static String getCategoryName(String category) {
		String[] categoryArr = category.split(StringPool.UNDERSCORE);
		if (categoryArr.length <= 1) {
			return StringPool.EMPTY;
		} else {
			return DictCache.getValue(category.split(StringPool.UNDERSCORE)[0], Func.toInt(category.split(StringPool.UNDERSCORE)[1]));
		}
	}

}
