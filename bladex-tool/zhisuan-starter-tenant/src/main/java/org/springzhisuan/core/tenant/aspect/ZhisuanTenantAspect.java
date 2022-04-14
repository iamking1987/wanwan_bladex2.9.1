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

package org.springzhisuan.core.tenant.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springzhisuan.core.tenant.ZhisuanTenantHolder;
import org.springzhisuan.core.tenant.annotation.TenantIgnore;

/**
 * 自定义租户切面
 *
 * @author Chill
 */
@Slf4j
@Aspect
public class ZhisuanTenantAspect {

	@Around("@annotation(tenantIgnore)")
	public Object around(ProceedingJoinPoint point, TenantIgnore tenantIgnore) throws Throwable {
		try {
			//开启忽略
			ZhisuanTenantHolder.setIgnore(Boolean.TRUE);
			//执行方法
			return point.proceed();
		} finally {
			//关闭忽略
			ZhisuanTenantHolder.clear();
		}
	}

}
