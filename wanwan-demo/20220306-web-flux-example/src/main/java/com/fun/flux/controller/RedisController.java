package com.fun.flux.controller;

import com.fun.flux.entity.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author wanwan 2022/3/7
 */
@RestController
@RequiredArgsConstructor
public class RedisController {

	private final ReactiveRedisTemplate<String,Object> reactiveTemplate;
	private final ReactiveRedisTemplate<String,UserVO> reactiveTemplate2;

	@GetMapping("/get")
	public Mono<UserVO> get(@RequestParam("id") Integer id) {
		String key = "user::" + id;
		return reactiveTemplate.opsForValue().get(key)
			.map(o -> (UserVO) o);
	}

	/**
	 * 设置指定用户的信息
	 *
	 * @param user 用户
	 * @return 是否成功
	 */
	@PostMapping("/set")
	public Mono<Boolean> set(UserVO user) {
		String key = "user::" + user.getId();
		return reactiveTemplate.opsForValue().set(key, user);
	}

	@GetMapping("/v2/get")
	public Mono<UserVO> getV2(@RequestParam("id") Integer id) {
		String key = "user::" + id;
		return reactiveTemplate2.opsForValue().get(key);
	}
}
