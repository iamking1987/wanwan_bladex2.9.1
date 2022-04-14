package com.auth.server.component;

import cn.hutool.core.util.StrUtil;
import com.auth.server.constant.AuthConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;

/**
 * @author wanwan 2022/1/30
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {


	private final UserDetailsService userDetailsServiceImpl;
	private final RedisTemplate<String, String> redisTemplate;

	public SmsAuthenticationProvider(UserDetailsService userDetailsServiceImpl, RedisTemplate<String, String> redisTemplate) {

		this.userDetailsServiceImpl = userDetailsServiceImpl;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
		Object principal = authentication.getPrincipal();// 获取凭证也就是用户的手机号
		String phone = "";
		if (principal instanceof UserDetails) {

			phone = ((UserDetails)principal).getUsername();
		} else if (principal instanceof AuthenticatedPrincipal) {

			phone = ((AuthenticatedPrincipal)principal).getName();
		} else if (principal instanceof Principal) {

			phone = ((Principal)principal).getName();
		} else {
			phone = principal == null ? "" : principal.toString();
		}
		String inputCode = (String) authentication.getCredentials(); // 获取输入的验证码
		// 1. 检验Redis手机号的验证码
		String redisCode = redisTemplate.opsForValue().get(AuthConstants.PHONE_CODE_PREFIX + phone);
		if (StrUtil.isEmpty(redisCode)) {
			throw new BadCredentialsException("验证码已经过期或尚未发送，请重新发送验证码");
		}
		if (!inputCode.equals(redisCode)) {
			throw new BadCredentialsException("输入的验证码不正确，请重新输入");
		}
		// 2. 根据手机号查询用户信息, 这里演示，直接查了user的信息
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("user");
		if (userDetails == null) {
			throw new InternalAuthenticationServiceException("phone用户不存在，请注册");
		}
		// 3. 重新创建已认证对象,
		SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(principal,inputCode, userDetails.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> aClass) {

		return SmsAuthenticationToken.class.isAssignableFrom(aClass);
	}


}
