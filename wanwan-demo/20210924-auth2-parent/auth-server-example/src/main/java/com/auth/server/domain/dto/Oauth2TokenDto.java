package com.auth.server.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zz
 * @date 2021/9/29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2TokenDto {
	/**
	 * 访问令牌
	 */
	private String token;
	/**
	 * 刷新令牌
	 */
	private String refreshToken;
	/**
	 * 访问令牌头前缀
	 */
	private String tokenHead;
	/**
	 * 有效时间（秒）
	 */
	private int expiresIn;
}
