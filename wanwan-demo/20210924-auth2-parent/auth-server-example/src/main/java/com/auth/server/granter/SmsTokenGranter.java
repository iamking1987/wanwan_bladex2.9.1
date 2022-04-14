package com.auth.server.granter;

import com.auth.server.component.SmsAuthenticationToken;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 短信登录授授权器
 * 		使用当前授权器需要自己实现AuthenticationProvider 和 AbstractAuthenticationToken
 * @author wanwan 2022-01-30
 */
public class SmsTokenGranter extends AbstractTokenGranter {

	// 修改授权模式为sms_code
	private static final String GRANT_TYPE = "sms_code";
	private final AuthenticationManager authenticationManager;

	public SmsTokenGranter(AuthenticationManager authenticationManager,
						  AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {

		this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected SmsTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
							 ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {

		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {


		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
		String phone = parameters.get("phone");
		String smsCode = parameters.get("smsCode");
		// Protect from downstream leaks of password
		parameters.remove("smsCode");
		Authentication userAuth = new SmsAuthenticationToken(phone, smsCode);
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);

		try {

			userAuth = authenticationManager.authenticate(userAuth);
			if (userAuth == null) {

				throw new InternalAuthenticationServiceException("phone用户不存在，请注册");
			}
		} catch (AccountStatusException ase) {

			//covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
			throw new InvalidGrantException(ase.getMessage());
		} catch (BadCredentialsException e) {

			// If the username/password are wrong the spec says we should send 400/invalid grant
			throw new InvalidGrantException(e.getMessage());
		}
		if (userAuth == null || !userAuth.isAuthenticated()) {

			throw new InvalidGrantException("Could not authenticate user: " + phone);
		}
		// 3. 重新创建Oau已认证对象,
		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, userAuth);

	}
}
