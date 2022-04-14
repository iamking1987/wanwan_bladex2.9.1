package com.auth.server.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (oauth_client_details)表实体类
 *
 * @author zz
 * @since 2021-09-30 10:07:03
 */
@Data
@TableName("oauth_client_details")
@EqualsAndHashCode
@ApiModel(value = "OauthClientDetails对象", description = "对象")
public class OauthClientDetails {

	private static final Long serialVersionId = 1L;

	private String clientId;

	private String resourceIds;

	private Boolean secretRequire;

	private Boolean scopeRequire;

	private String clientSecret;

	private String scope;

	private String authorizedGrantTypes;

	private String webServerRedirectUri;

	private String authorities;

	private Integer accessTokenValidity;

	private Integer refreshTokenValidity;

	private String additionalInformation;

	private String autoapprove;
}
