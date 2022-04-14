package com.auth.server.service;


import com.auth.server.domain.entity.UserDefault;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (users)表服务接口
 *
 * @author zz
 * @since 2021-09-30 10:36:12
 */
public interface OauthUserService extends IService<UserDefault> {

	/**
	 * 自定义分页
	 */
	IPage<UserDefault> selectUsersPage(IPage<UserDefault> page, UserDefault users);
}
