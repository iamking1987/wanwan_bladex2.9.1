/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
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
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */

package org.springzhisuan.core.mp.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springzhisuan.core.mp.base.BaseEntity;
import org.springzhisuan.core.mp.base.BaseServiceImpl;
import org.springzhisuan.core.mp.injector.ZhisuanSqlMethod;
import org.springzhisuan.core.mp.mapper.ZhisuanMapper;
import org.springzhisuan.core.mp.service.ZhisuanService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;

/**
 * ZhisuanService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 *
 * @author L.cm, chill
 */
@Validated
public class ZhisuanServiceImpl<M extends ZhisuanMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements ZhisuanService<T> {

	@Override
	public boolean saveIgnore(T entity) {
		return SqlHelper.retBool(baseMapper.insertIgnore(entity));
	}

	@Override
	public boolean saveReplace(T entity) {
		return SqlHelper.retBool(baseMapper.replace(entity));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveIgnoreBatch(Collection<T> entityList, int batchSize) {
		return saveBatch(entityList, batchSize, ZhisuanSqlMethod.INSERT_IGNORE_ONE);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveReplaceBatch(Collection<T> entityList, int batchSize) {
		return saveBatch(entityList, batchSize, ZhisuanSqlMethod.REPLACE_ONE);
	}

	private boolean saveBatch(Collection<T> entityList, int batchSize, ZhisuanSqlMethod sqlMethod) {
		String sqlStatement = zhisuanSqlStatement(sqlMethod);
		executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
		return true;
	}

	/**
	 * 获取 zhisuanSqlStatement
	 *
	 * @param sqlMethod ignore
	 * @return sql
	 */
	protected String zhisuanSqlStatement(ZhisuanSqlMethod sqlMethod) {
		return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
	}
}
