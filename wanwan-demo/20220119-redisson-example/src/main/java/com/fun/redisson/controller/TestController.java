package com.fun.redisson.controller;

import com.fun.redisson.service.ObjectExampleService;
import com.fun.redisson.service.RedissonExampleService;
import com.fun.redisson.service.StockExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanwan 2022/1/19
 */
@RestController
@RequiredArgsConstructor
public class TestController {

	private final ObjectExampleService exampleService;
	private final StockExampleService stockExampleService;
	private final RedissonExampleService redissonExampleService;

	@GetMapping("/test1")
	public void test1() {
		//exampleService.setRBucketObject();
//		exampleService.setRList();
		exampleService.setRMap();
	}

	@GetMapping("/test2")
	public void test2() {
		redissonExampleService.lockTest(223);
	}
}
