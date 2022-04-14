package com.fun.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wanwan 2022/2/25
 */
@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService{

	@Async
	@Override
	public void executeAsync(Integer i) {
		log.info("{}--start executeAsync", i);
	}
}
