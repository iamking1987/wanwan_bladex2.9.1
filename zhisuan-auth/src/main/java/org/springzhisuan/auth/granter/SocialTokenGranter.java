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
package org.springzhisuan.auth.granter;

import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springzhisuan.auth.constant.AuthConstant;
import org.springzhisuan.auth.service.ZhisuanUserDetails;
import org.springzhisuan.auth.utils.TokenUtil;
import org.springzhisuan.core.social.props.SocialProperties;
import org.springzhisuan.core.social.utils.SocialUtil;
import org.springzhisuan.core.tool.api.R;
import org.springzhisuan.core.tool.support.Kv;
import org.springzhisuan.core.tool.utils.BeanUtil;
import org.springzhisuan.core.tool.utils.Func;
import org.springzhisuan.core.tool.utils.WebUtil;
import org.springzhisuan.system.user.entity.User;
import org.springzhisuan.system.user.entity.UserInfo;
import org.springzhisuan.system.user.entity.UserOauth;
import org.springzhisuan.system.user.feign.IUserClient;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 第三方登录认证类
 *
 * @author Chill
 */
public class SocialTokenGranter extends AbstractTokenGranter {
	private static final String GRANT_TYPE = "social";
	private static final Integer AUTH_SUCCESS_CODE = 2000;

	private final IUserClient userClient;
	private final SocialProperties socialProperties;

	protected SocialTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, IUserClient userClient, SocialProperties socialProperties) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		this.userClient = userClient;
		this.socialProperties = socialProperties;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		// 请求头租户信息
		HttpServletRequest request = WebUtil.getRequest();
		String tenantId = Func.toStr(request.getHeader(TokenUtil.TENANT_HEADER_KEY), TokenUtil.DEFAULT_TENANT_ID);

		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
		// 开放平台来源
		String sourceParameter = parameters.get("source");
		// 匹配是否有别名定义
		String source = socialProperties.getAlias().getOrDefault(sourceParameter, sourceParameter);
		// 开放平台授权码
		String code = parameters.get("code");
		// 开放平台状态吗
		String state = parameters.get("state");

		// 获取开放平台授权数据
		AuthRequest authRequest = SocialUtil.getAuthRequest(source, socialProperties);
		AuthCallback authCallback = new AuthCallback();
		authCallback.setCode(code);
		authCallback.setState(state);
		AuthResponse authResponse = authRequest.login(authCallback);
		AuthUser authUser;
		if (authResponse.getCode() == AUTH_SUCCESS_CODE) {
			authUser = (AuthUser) authResponse.getData();
		} else {
			throw new InvalidGrantException("social grant failure, auth response is not success");
		}

		// 组装数据
		UserOauth userOauth = Objects.requireNonNull(BeanUtil.copy(authUser, UserOauth.class));
		userOauth.setSource(authUser.getSource());
		userOauth.setTenantId(tenantId);
		userOauth.setUuid(authUser.getUuid());

		// 远程调用，获取认证信息
		R<UserInfo> result = userClient.userAuthInfo(userOauth);
		ZhisuanUserDetails zhisuanUserDetails;
		if (result.isSuccess()) {
			User user = result.getData().getUser();
			Kv detail = result.getData().getDetail();
			if (user == null || user.getId() == null) {
				throw new InvalidGrantException("social grant failure, user is null");
			}
			zhisuanUserDetails = new ZhisuanUserDetails(user.getId(),
				tenantId, result.getData().getOauthId(), user.getName(), user.getRealName(), user.getDeptId(), user.getPostId(), user.getRoleId(), Func.join(result.getData().getRoles()), Func.toStr(userOauth.getAvatar(), TokenUtil.DEFAULT_AVATAR),
				userOauth.getUsername(), AuthConstant.ENCRYPT + user.getPassword(), detail, true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList(Func.join(result.getData().getRoles())));
		} else {
			throw new InvalidGrantException("social grant failure, feign client return error");
		}

		// 组装认证数据，关闭密码校验
		Authentication userAuth = new UsernamePasswordAuthenticationToken(zhisuanUserDetails, null, zhisuanUserDetails.getAuthorities());
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);
		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);

		// 返回 OAuth2Authentication
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
	}

}
