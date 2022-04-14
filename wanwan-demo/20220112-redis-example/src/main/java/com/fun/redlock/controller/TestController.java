package com.fun.redlock.controller;

import com.fun.redlock.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanwan 2022/3/7
 */
@RestController
@RequiredArgsConstructor
public class TestController {

	private final RedisUtil<String> redisUtil;

	@GetMapping("/test")
	private void test() {
		String key = "000000:blade:token::token:state:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIwMDAwMDAiLCJsb2dpbl91dWlkIjoiM2NmNjk1NzQtMzQ3ZS00OTdiLThiOTQtZGJlZmZlOGI0NWYwIiwidXNlcl9uYW1lIjoiMTg5MDAwMDAwMDAiLCJlbnRlcnByaXNlIjowLCJyZWFsX25hbWUiOiLnrqHnkIblkZgiLCJhcmVhX2lkIjpudWxsLCJjbGllbnRfaWQiOiJzYWJlciIsInBsYXRmb3JtX3R5cGUiOiJkc3AiLCJyb2xlX2lkIjoiMSIsInNjb3BlIjpbImFsbCJdLCJvYXV0aF9pZCI6IiIsImV4cCI6MTY0NjYzODU2OSwianRpIjoiYzNjZWVmNGEtZGI3My00YmFmLWExNjAtYTc0MzEzN2QxYzdkIiwiY29tcGFueV9pZCI6MSwiYXZhdGFyIjoiaHR0cDovLzEyNC40Mi4xMDMuMjM2OjgwODAvZ3JvdXAxL00wMC8wMC8xNi9DZ3JLZTJJRWVDbUFSRHZKQUFCcU02ZkRidnMwNjQuanBnIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJyb2xlX25hbWUiOiJhZG1pbmlzdHJhdG9yIiwibGljZW5zZSI6InBvd2VyZWQgYnkgYmxhZGV4IiwidXJ0eXBlIjoyLCJwb3N0X2lkIjoiMTEyMzU5ODgxNzczODY3NTIwMSIsInVzZXJfaWQiOiI1MDAwMDAwMSIsIm5pY2tfbmFtZSI6IjPnuqciLCJkZXB0X2lkIjoiMTMwNDI0NjY1MDEwMzczODM3NCIsImFjY291bnQiOiIxODkwMDAwMDAwMCIsInBfaWQiOi0xfQ.h6th1m24w6VJ-riMsrSsmQzkWXhpx_PebPlZci3G1wc";
		//redisUtil.cacheValue(key, accessToken, 1800);
		for (int i = 0; i < 2; i++) {
			System.out.println(i+"--"+ redisUtil.hasKey(key));
			System.out.println(i+"--"+ redisUtil.hasKey(key));
			System.out.println(i+"--"+ redisUtil.hasKey(key));
		}
	}
}
