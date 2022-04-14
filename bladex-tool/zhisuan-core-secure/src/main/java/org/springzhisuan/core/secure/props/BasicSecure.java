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
package org.springzhisuan.core.secure.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springzhisuan.core.secure.provider.HttpMethod;

/**
 * 基础授权规则
 *
 * @author Chill
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicSecure {

	/**
	 * 请求方法
	 */
	private HttpMethod method;
	/**
	 * 请求路径
	 */
	private String pattern;
	/**
	 * 客户端id
	 */
	private String username;
	/**
	 * 客户端密钥
	 */
	private String password;

}
