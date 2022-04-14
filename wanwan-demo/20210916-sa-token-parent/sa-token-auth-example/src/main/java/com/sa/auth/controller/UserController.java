package com.sa.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.fun.common.utils.result.Result;
import com.fun.common.utils.result.ResultUtil;
import com.sa.auth.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userService;

	@PostMapping("/login")
	public Result<?> login(@RequestParam String username, @RequestParam String password) {
		SaTokenInfo saTokenInfo = userService.login(username, password);
		if (saTokenInfo == null) {
			return ResultUtil.sendErrorMessage("用户名或密码错误");
		}
		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("token", saTokenInfo.getTokenValue());
		tokenMap.put("tokenHead", saTokenInfo.getTokenName());
		return ResultUtil.success(tokenMap);
	}
}
