package com.fun.flux.controller;

import com.fun.flux.service.UserService;
import com.fun.flux.entity.UserVO;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wanwan 2022/3/6
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 查询用户列表
	 *
	 * @return 用户列表
	 */
	@GetMapping("/list")
	public Flux<UserVO> list() {
		return userService.getUserList();
	}

	/**
	 * 获得指定用户编号的用户
	 *
	 * @param id 用户编号
	 * @return 用户
	 */
	@GetMapping("/get")
	public Mono<UserVO> get(@RequestParam("id") Integer id) {
		return userService.getOne(id);
	}

	/**
	 * 添加用户
	 *
	 * @param addDTO 添加用户信息 DTO
	 * @return 添加成功的用户编号
	 */
	@PostMapping("add")
	public Mono<Integer> add(@RequestBody Publisher<UserVO> addDTO) {
		return userService.save();
	}

	/**
	 * 更新指定用户编号的用户
	 *
	 * @param updateDTO 更新用户信息 DTO
	 * @return 是否修改成功
	 */
	@PostMapping("/update")
	public Mono<Boolean> update(@RequestBody Publisher<UserVO> updateDTO) {
		return userService.update();
	}
}
