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
package org.springzhisuan.core.sms;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.AllArgsConstructor;
import org.springzhisuan.core.redis.cache.ZhisuanRedis;
import org.springzhisuan.core.sms.model.SmsCode;
import org.springzhisuan.core.sms.model.SmsData;
import org.springzhisuan.core.sms.model.SmsResponse;
import org.springzhisuan.core.sms.props.SmsProperties;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.StringPool;
import org.springzhisuan.core.tool.utils.StringUtil;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;

/**
 * 腾讯云短信发送类
 *
 * @author Chill
 */
@AllArgsConstructor
public class TencentSmsTemplate implements SmsTemplate {

	private static final int SUCCESS = 0;
	private static final String NATION_CODE = "86";

	private final SmsProperties smsProperties;
	private final SmsMultiSender smsSender;
	private final ZhisuanRedis zhisuanRedis;


	@Override
	public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
		try {
			Collection<String> values = smsData.getParams().values();
			String[] params = StringUtil.toStringArray(values);
			SmsMultiSenderResult senderResult = smsSender.sendWithParam(
				NATION_CODE,
				StringUtil.toStringArray(phones),
				Func.toInt(smsProperties.getTemplateId()),
				params,
				smsProperties.getSignName(),
				StringPool.EMPTY, StringPool.EMPTY
			);
			return new SmsResponse(senderResult.result == SUCCESS, senderResult.result, senderResult.toString());
		} catch (HTTPException | IOException e) {
			e.printStackTrace();
			return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
	}

	@Override
	public SmsCode sendValidate(SmsData smsData, String phone) {
		SmsCode smsCode = new SmsCode();
		boolean temp = sendSingle(smsData, phone);
		if (temp && StringUtil.isNotBlank(smsData.getKey())) {
			String id = StringUtil.randomUUID();
			String value = smsData.getParams().get(smsData.getKey());
			zhisuanRedis.setEx(cacheKey(phone, id), value, Duration.ofMinutes(30));
			smsCode.setId(id).setValue(value);
		} else {
			smsCode.setSuccess(Boolean.FALSE);
		}
		return smsCode;
	}

	@Override
	public boolean validateMessage(SmsCode smsCode) {
		String id = smsCode.getId();
		String value = smsCode.getValue();
		String phone = smsCode.getPhone();
		String cache = zhisuanRedis.get(cacheKey(phone, id));
		if (StringUtil.isNotBlank(value) && StringUtil.equalsIgnoreCase(cache, value)) {
			zhisuanRedis.del(cacheKey(phone, id));
			return true;
		}
		return false;
	}

}
