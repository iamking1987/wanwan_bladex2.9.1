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
package org.springzhisuan.resource.feign;

import org.springzhisuan.core.tool.api.R;
import org.springframework.stereotype.Component;

/**
 * 流程远程调用失败处理类
 *
 * @author Chill
 */
@Component
public class ISmsClientFallback implements ISmsClient {
	@Override
	public R sendMessage(String code, String params, String phones) {
		return R.fail("远程调用失败");
	}

	@Override
	public R sendValidate(String code, String phone) {
		return R.fail("远程调用失败");
	}

	@Override
	public R validateMessage(String code, String id, String value, String phone) {
		return R.fail("远程调用失败");
	}

}
