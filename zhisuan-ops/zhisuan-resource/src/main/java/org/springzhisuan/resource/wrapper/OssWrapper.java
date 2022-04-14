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
package org.springzhisuan.resource.wrapper;

import org.springzhisuan.core.mp.support.BaseEntityWrapper;
import org.springzhisuan.core.tool.utils.BeanUtil;
import org.springzhisuan.resource.entity.Oss;
import org.springzhisuan.resource.vo.OssVO;
import org.springzhisuan.system.cache.DictCache;
import org.springzhisuan.system.enums.DictEnum;

import java.util.Objects;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author ZhisuanX
 */
public class OssWrapper extends BaseEntityWrapper<Oss, OssVO> {

	public static OssWrapper build() {
		return new OssWrapper();
	}

	@Override
	public OssVO entityVO(Oss oss) {
		OssVO ossVO = Objects.requireNonNull(BeanUtil.copy(oss, OssVO.class));
		String categoryName = DictCache.getValue(DictEnum.OSS, oss.getCategory());
		String statusName = DictCache.getValue(DictEnum.YES_NO, oss.getStatus());
		ossVO.setCategoryName(categoryName);
		ossVO.setStatusName(statusName);
		return ossVO;
	}

}
