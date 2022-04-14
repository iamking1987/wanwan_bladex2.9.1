package com.fun.redlock.service;

import com.fun.redlock.annotation.JLock;
import com.fun.redlock.util.JedisTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 第一种分布式锁
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class JedisLockService {
	/**
	 * 锁名称(hashKey)
	 */
	private static final String lockName = "test";

	/**
	 * 模拟业务执行
	 */
	public void buzWithJedisLuaLock() {

		String key = JedisTool.tryLock(lockName, "30"); //单秒为秒
		Optional.ofNullable(key).ifPresent(e -> log.info(Thread.currentThread().getId() +"--获取锁成功--lockName = " + lockName + "--key=" + key));
		if (!StringUtils.isEmpty(key)) {
			System.out.println(Thread.currentThread().getId() + "--开始执行业务");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getId() + "--业务执行结束");
			JedisTool.unlock(lockName, key);
		}
	}

	@JLock(key = "test")
	public void testAnnotationLock() {
		System.out.println("123");
	}

}
