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
package org.springzhisuan.admin.dingtalk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 钉钉 服务
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class DingTalkService {
	private static final String DING_TALK_ROBOT_URL = "https://oapi.dingtalk.com/robot/send?access_token=";
	private final MonitorProperties properties;
	private final WebClient webClient;

	/**
	 * 发送消息
	 *
	 * @param title title
	 * @param text  消息
	 */
	public Mono<Void> pushMsg(String title, String text) {
		log.info("钉钉消息：[创建消息体]title:{}, text:{}", title, text);

		HashMap<String, String> params = new HashMap<>(2);
		params.put("title", title);
		params.put("text", text);

		Map<String, Object> body = new HashMap<>(2);
		body.put("msgtype", "markdown");
		body.put("markdown", params);
		log.info("创建消息体 json：{}", body);

		MonitorProperties.DingTalk dingTalk = properties.getDingTalk();
		String accessToken = dingTalk.getAccessToken();
		if (StringUtils.isEmpty(accessToken)) {
			log.error("DingTalk alert config accessToken ${monitor.ding-talk.access-token} is blank.");
			return Mono.empty();
		}

		String urlString = DING_TALK_ROBOT_URL + dingTalk.getAccessToken();
		// 有私钥要签名
		String secret = dingTalk.getSecret();
		if (StringUtils.hasText(secret)) {
			long timestamp = System.currentTimeMillis();
			urlString += String.format("&timestamp=%s&sign=%s", timestamp, getSign(secret, timestamp));
		}
		return webClient.post()
			.uri(URI.create(urlString))
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(body))
			.retrieve()
			.bodyToMono(String.class)
			.doOnSuccess((result) -> log.info("钉钉消息：[消息返回]result:{}", result))
			.then();
	}

	private static String getSign(String secret, long timestamp) {
		String stringToSign = timestamp + "\n" + secret;
		byte[] hmacSha256Bytes = digestHmac(stringToSign, secret);
		return UriUtils.encode(Base64Utils.encodeToString(hmacSha256Bytes), StandardCharsets.UTF_8);
	}

	public static byte[] digestHmac(String data, String key) {
		SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		try {
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
