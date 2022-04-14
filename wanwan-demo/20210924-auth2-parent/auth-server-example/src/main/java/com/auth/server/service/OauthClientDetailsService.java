package com.auth.server.service;

import com.auth.server.domain.entity.OauthClientDetails;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (oauth_client_details)表服务接口
 *
 * @author zz
 * @since 2021-09-30 10:36:33
 */
public interface OauthClientDetailsService extends IService<OauthClientDetails> {

	/**
	 * 自定义分页
	 */
	IPage<OauthClientDetails> selectOauthClientDetailsPage(IPage<OauthClientDetails> page, OauthClientDetails oauthClientDetails);
}
