package com.auth.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.auth.server.constant.MessageConstant;
import com.auth.server.domain.entity.UserDefault;
import com.auth.server.domain.principal.UserPrincipal;
import com.auth.server.mapper.UsersMapper;
import com.auth.server.service.OauthUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanwan 2021/9/29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper, UserDefault> implements UserDetailsService, OauthUserService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		QueryWrapper<UserDefault> userWrapper = new QueryWrapper<>();
		userWrapper.eq("username", username);
		List<UserDefault> findUserList = baseMapper.selectList(userWrapper);

		if (CollUtil.isEmpty(findUserList)) {
			throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
		}
		UserPrincipal userPrincipal = new UserPrincipal(findUserList.get(0));
		if (!userPrincipal.isEnabled()) {
			throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
		} else if (!userPrincipal.isAccountNonLocked()) {
			throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
		} else if (!userPrincipal.isAccountNonExpired()) {
			throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
		} else if (!userPrincipal.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
		}
		return userPrincipal;
	}

	@Override
	public IPage<UserDefault> selectUsersPage(IPage<UserDefault> page, UserDefault users) {
		return null;
	}
}

