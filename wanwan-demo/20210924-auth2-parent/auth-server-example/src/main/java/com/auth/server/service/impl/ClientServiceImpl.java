package com.auth.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auth.server.constant.MessageConstant;
import com.auth.server.domain.entity.OauthClientDetails;
import com.auth.server.domain.principal.ClientPrincipal;
import com.auth.server.mapper.OauthClientDetailsMapper;
import com.auth.server.service.OauthClientDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @date 2021/9/29
 */
@Service
public class ClientServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements ClientDetailsService, OauthClientDetailsService {

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		QueryWrapper<OauthClientDetails> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("client_id", clientId);
		List<OauthClientDetails> findClientList = baseMapper.selectList(queryWrapper);

		if (CollUtil.isEmpty(findClientList)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstant.NOT_FOUND_CLIENT);
		}
		return new ClientPrincipal(findClientList.get(0));
	}

	@Override
	public IPage<OauthClientDetails> selectOauthClientDetailsPage(IPage<OauthClientDetails> page, OauthClientDetails oauthClientDetails) {
		return null;
	}
}
