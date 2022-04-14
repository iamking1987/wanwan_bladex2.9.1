package com.fun.flux.service;

import com.fun.flux.entity.UserVO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanwan 2022/3/9
 */
@Component
public class UserService {

	public Flux<UserVO> getUserList() {
		// 查询列表
		List<UserVO> result = new ArrayList<>();
		result.add(new UserVO().setId(1).setUsername("yudaoyuanma"));
		result.add(new UserVO().setId(2).setUsername("woshiyutou"));
		result.add(new UserVO().setId(3).setUsername("chifanshuijiao"));
		// 返回列表
		return Flux.fromIterable(result);
	}

	public Mono<UserVO> getOne(Integer id) {
		// 查询用户
		UserVO user = new UserVO().setId(id).setUsername("username:" + id);
		// 返回
		return Mono.just(user);
	}

	public Mono<Integer> save() {
		// 插入用户记录，返回编号
		Integer returnId = 1;
		// 返回用户编号
		return Mono.just(returnId);
	}

	public Mono<Boolean> update() {
		// 更新用户记录
		Boolean success = true;
		// 返回更新是否成功
		return Mono.just(success);
	}
}
