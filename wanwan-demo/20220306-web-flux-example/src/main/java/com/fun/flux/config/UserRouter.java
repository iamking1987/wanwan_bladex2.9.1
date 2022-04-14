package com.fun.flux.config;

import com.fun.flux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 定义路由和处理器
 * @author wanwan 2022/3/9
 */
//@Configuration
@RequiredArgsConstructor
public class UserRouter {

	private final UserService userService;

	@Bean
	public RouterFunction<ServerResponse> userListRouterFunction() {
		return RouterFunctions.route(RequestPredicates.GET("/users/list"),
			request -> {
				// 返回列表
				return ServerResponse.ok().bodyValue(userService.getUserList());
			});
	}

	@Bean
	public RouterFunction<ServerResponse> userGetRouterFunction() {
		return RouterFunctions.route(RequestPredicates.GET("/users2/get"),
			request -> {
				// 获得入参
				Integer id = request.queryParam("id")
					.map(s -> StringUtils.isEmpty(s) ? null : Integer.valueOf(s)).get();
				// 返回user
				return ServerResponse.ok().bodyValue(userService.getOne(id));
			});
	}
}
