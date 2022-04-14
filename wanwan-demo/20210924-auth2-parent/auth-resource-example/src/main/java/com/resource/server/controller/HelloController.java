package com.resource.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zz
 * @date 2021/9/24
 */
@RestController
public class HelloController {
	@GetMapping("hello")
	public String hello() {
		return "Hello";
	}
}
