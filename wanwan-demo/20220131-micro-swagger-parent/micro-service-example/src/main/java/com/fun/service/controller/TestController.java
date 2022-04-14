package com.fun.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanwan 2022/2/1
 */
@RestController
@Api(tags = "生产者服务1接口")
public class TestController {

	@GetMapping("/test")
	@ApiOperation("test接口")
	public void test(@RequestParam String id) {
		System.out.println(id);
	}
}
