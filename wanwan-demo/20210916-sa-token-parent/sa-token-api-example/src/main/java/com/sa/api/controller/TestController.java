package com.sa.api.controller;

import com.fun.common.utils.result.Result;
import com.fun.common.utils.result.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 * Created by macro on 2020/6/19.
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/hello")
	public Result<?> hello() {
		return ResultUtil.success("Hello World.");
	}
}
