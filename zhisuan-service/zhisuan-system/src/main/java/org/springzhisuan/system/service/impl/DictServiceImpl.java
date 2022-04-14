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
package org.springzhisuan.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springzhisuan.common.constant.CommonConstant;
import org.springzhisuan.core.cache.utils.CacheUtil;
import org.springzhisuan.core.log.exception.ServiceException;
import org.springzhisuan.core.mp.support.Condition;
import org.springzhisuan.core.mp.support.Query;
import org.springzhisuan.core.tool.constant.ZhisuanConstant;
import org.springzhisuan.core.tool.node.ForestNodeMerger;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.StringPool;
import org.springzhisuan.system.cache.DictCache;
import org.springzhisuan.system.entity.Dict;
import org.springzhisuan.system.mapper.DictMapper;
import org.springzhisuan.system.service.IDictService;
import org.springzhisuan.system.vo.DictVO;
import org.springzhisuan.system.wrapper.DictWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springzhisuan.core.cache.constant.CacheConstant.DICT_CACHE;


/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

	@Override
	public IPage<DictVO> selectDictPage(IPage<DictVO> page, DictVO dict) {
		return page.setRecords(baseMapper.selectDictPage(page, dict));
	}

	@Override
	public List<DictVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

	@Override
	public List<DictVO> parentTree() {
		return ForestNodeMerger.merge(baseMapper.parentTree());
	}

	@Override
	public String getValue(String code, String dictKey) {
		return Func.toStr(baseMapper.getValue(code, dictKey), StringPool.EMPTY);
	}

	@Override
	public List<Dict> getList(String code) {
		return baseMapper.getList(code);
	}

	@Override
	public boolean submit(Dict dict) {
		LambdaQueryWrapper<Dict> lqw = Wrappers.<Dict>query().lambda().eq(Dict::getCode, dict.getCode()).eq(Dict::getDictKey, dict.getDictKey());
		Long cnt = baseMapper.selectCount((Func.isEmpty(dict.getId())) ? lqw : lqw.notIn(Dict::getId, dict.getId()));
		if (cnt > 0L) {
			throw new ServiceException("当前字典键值已存在!");
		}
		// 修改顶级字典后同步更新下属字典的编号
		if (Func.isNotEmpty(dict.getId()) && dict.getParentId().longValue() == ZhisuanConstant.TOP_PARENT_ID) {
			Dict parent = DictCache.getById(dict.getId());
			this.update(Wrappers.<Dict>update().lambda().set(Dict::getCode, dict.getCode()).eq(Dict::getCode, parent.getCode()).ne(Dict::getParentId, ZhisuanConstant.TOP_PARENT_ID));
		}
		if (Func.isEmpty(dict.getParentId())) {
			dict.setParentId(ZhisuanConstant.TOP_PARENT_ID);
		}
		dict.setIsDeleted(ZhisuanConstant.DB_NOT_DELETED);
		CacheUtil.clear(DICT_CACHE, Boolean.FALSE);
		return saveOrUpdate(dict);
	}

	@Override
	public boolean removeDict(String ids) {
		Long cnt = baseMapper.selectCount(Wrappers.<Dict>query().lambda().in(Dict::getParentId, Func.toLongList(ids)));
		if (cnt > 0L) {
			throw new ServiceException("请先删除子节点!");
		}
		return removeByIds(Func.toLongList(ids));
	}

	@Override
	public IPage<DictVO> parentList(Map<String, Object> dict, Query query) {
		IPage<Dict> page = this.page(Condition.getPage(query), Condition.getQueryWrapper(dict, Dict.class).lambda().eq(Dict::getParentId, CommonConstant.TOP_PARENT_ID).orderByAsc(Dict::getSort));
		return DictWrapper.build().pageVO(page);
	}

	@Override
	public List<DictVO> childList(Map<String, Object> dict, Long parentId) {
		if (parentId < 0) {
			return new ArrayList<>();
		}
		dict.remove("parentId");
		Dict parentDict = DictCache.getById(parentId);
		List<Dict> list = this.list(Condition.getQueryWrapper(dict, Dict.class).lambda().ne(Dict::getId, parentId).eq(Dict::getCode, parentDict.getCode()).orderByAsc(Dict::getSort));
		return DictWrapper.build().listNodeVO(list);
	}
}
