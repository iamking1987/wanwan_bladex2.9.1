package com.fun.async.controller;

import com.fun.async.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanwan 2022/2/25
 */
@RestController
@RequiredArgsConstructor
public class TestConl {

	private final AsyncService asyncService;

	@RequestMapping("/test")
	public void run() {
		for (int i = 0; i < 100; i++) {
			asyncService.executeAsync(i);
		}
	}
}
