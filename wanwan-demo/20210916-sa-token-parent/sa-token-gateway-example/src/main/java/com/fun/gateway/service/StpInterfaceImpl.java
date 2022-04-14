package com.fun.gateway.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.sa.common.domain.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口扩展
 * @author zz
 * @date 2021/9/16
 */
@Component
public class StpInterfaceImpl implements StpInterface {

	@Override
	public List<String> getPermissionList(Object loginId, String loginType) {
		// 返回此 loginId 拥有的权限码列表
		UserDTO userDTO = (UserDTO) StpUtil.getSession().get("userInfo");
		return userDTO.getPermissionList();
	}

	/**
	 * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
	 */
	@Override
	public List<String> getRoleList(Object loginId, String loginType) {
		// 返回此 loginId 拥有的权限码列表
		UserDTO userDTO = (UserDTO) StpUtil.getSession().get("userInfo");
		return userDTO.getRoleList();
	}

}
