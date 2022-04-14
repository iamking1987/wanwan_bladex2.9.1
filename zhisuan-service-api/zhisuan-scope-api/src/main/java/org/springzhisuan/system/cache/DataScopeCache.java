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
package org.springzhisuan.system.cache;

import org.springzhisuan.core.cache.utils.CacheUtil;
import org.springzhisuan.core.datascope.model.DataScopeModel;
import org.springzhisuan.core.tool.utils.CollectionUtil;
import org.springzhisuan.core.tool.utils.SpringUtil;
import org.springzhisuan.core.tool.utils.StringPool;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springzhisuan.system.feign.IDataScopeClient;

import java.util.List;

import static org.springzhisuan.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 数据权限缓存
 *
 * @author Chill
 */
public class DataScopeCache {

	private static final String SCOPE_CACHE_CODE = "dataScope:code:";
	private static final String SCOPE_CACHE_CLASS = "dataScope:class:";
	private static final String DEPT_CACHE_ANCESTORS = "dept:ancestors:";

	private static IDataScopeClient dataScopeClient;

	private static IDataScopeClient getDataScopeClient() {
		if (dataScopeClient == null) {
			dataScopeClient = SpringUtil.getBean(IDataScopeClient.class);
		}
		return dataScopeClient;
	}

	/**
	 * 获取数据权限
	 *
	 * @param mapperId 数据权限mapperId
	 * @param roleId   用户角色集合
	 * @return DataScopeModel
	 */
	public static DataScopeModel getDataScopeByMapper(String mapperId, String roleId) {
		DataScopeModel dataScope = CacheUtil.get(SYS_CACHE, SCOPE_CACHE_CLASS, mapperId + StringPool.COLON + roleId, DataScopeModel.class, Boolean.FALSE);
		if (dataScope == null || !dataScope.getSearched()) {
			dataScope = getDataScopeClient().getDataScopeByMapper(mapperId, roleId);
			CacheUtil.put(SYS_CACHE, SCOPE_CACHE_CLASS, mapperId + StringPool.COLON + roleId, dataScope);
		}
		return StringUtil.isNotBlank(dataScope.getResourceCode()) ? dataScope : null;
	}

	/**
	 * 获取数据权限
	 *
	 * @param code 数据权限资源编号
	 * @return DataScopeModel
	 */
	public static DataScopeModel getDataScopeByCode(String code) {
		DataScopeModel dataScope = CacheUtil.get(SYS_CACHE, SCOPE_CACHE_CODE, code, DataScopeModel.class, Boolean.FALSE);
		if (dataScope == null || !dataScope.getSearched()) {
			dataScope = getDataScopeClient().getDataScopeByCode(code);
			CacheUtil.put(SYS_CACHE, SCOPE_CACHE_CODE, code, dataScope);
		}
		return StringUtil.isNotBlank(dataScope.getResourceCode()) ? dataScope : null;
	}

	/**
	 * 获取部门子级
	 *
	 * @param deptId 部门id
	 * @return deptIds
	 */
	public static List<Long> getDeptAncestors(Long deptId) {
		List ancestors = CacheUtil.get(SYS_CACHE, DEPT_CACHE_ANCESTORS, deptId, List.class);
		if (CollectionUtil.isEmpty(ancestors)) {
			ancestors = getDataScopeClient().getDeptAncestors(deptId);
			CacheUtil.put(SYS_CACHE, DEPT_CACHE_ANCESTORS, deptId, ancestors);
		}
		return ancestors;
	}
}
