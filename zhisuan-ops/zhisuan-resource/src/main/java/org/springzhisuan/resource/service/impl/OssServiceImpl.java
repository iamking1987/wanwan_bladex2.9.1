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
package org.springzhisuan.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springzhisuan.core.log.exception.ServiceException;
import org.springzhisuan.core.mp.base.BaseServiceImpl;
import org.springzhisuan.core.secure.utils.AuthUtil;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.resource.entity.Oss;
import org.springzhisuan.resource.vo.OssVO;
import org.springzhisuan.resource.mapper.OssMapper;
import org.springzhisuan.resource.service.IOssService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现类
 *
 * @author ZhisuanX
 */
@Service
public class OssServiceImpl extends BaseServiceImpl<OssMapper, Oss> implements IOssService {

	@Override
	public IPage<OssVO> selectOssPage(IPage<OssVO> page, OssVO oss) {
		return page.setRecords(baseMapper.selectOssPage(page, oss));
	}

	@Override
	public boolean submit(Oss oss) {
		LambdaQueryWrapper<Oss> lqw = Wrappers.<Oss>query().lambda()
			.eq(Oss::getOssCode, oss.getOssCode()).eq(Oss::getTenantId, AuthUtil.getTenantId());
		Long cnt = baseMapper.selectCount(Func.isEmpty(oss.getId()) ? lqw : lqw.notIn(Oss::getId, oss.getId()));
		if (cnt > 0L) {
			throw new ServiceException("当前资源编号已存在!");
		}
		return this.saveOrUpdate(oss);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean enable(Long id) {
		// 先禁用
		boolean temp1 = this.update(Wrappers.<Oss>update().lambda().set(Oss::getStatus, 1));
		// 在启用
		boolean temp2 = this.update(Wrappers.<Oss>update().lambda().set(Oss::getStatus, 2).eq(Oss::getId, id));
		return temp1 && temp2;
	}

}
