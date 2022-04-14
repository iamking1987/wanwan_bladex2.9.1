package com.auth.server.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * jwt内容增强器
 *
 * @date 2021/9/24
 */
public class CustomTokenEnhancer implements TokenEnhancer {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Authentication userAuthentication = authentication.getUserAuthentication();
		if (userAuthentication != null) {
			Object principal = authentication.getUserAuthentication().getPrincipal();
			//把用户标识嵌入 JWT Token 中去(Key 是 userDetails)
			Map<String, Object> additionalInfo = new HashMap<>();
			additionalInfo.put("userDetails", principal);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		}
		return accessToken;
	}
}
