package org.springzhisuan.job.executor.controller;

import org.springzhisuan.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试定时请求
 *
 * @author job
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("testRequest")
	public R testRequest(String name) {
		return R.data("我是测试请求" + name);
	}


}
