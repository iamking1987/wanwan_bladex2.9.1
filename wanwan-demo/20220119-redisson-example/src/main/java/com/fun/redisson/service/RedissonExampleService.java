package com.fun.redisson.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wanwan 2022/1/19
 */
@Service
@RequiredArgsConstructor
public class RedissonExampleService {

	private final RedissonClient redissonClient;

    /**
     * 非阻塞方式获取锁(立即返回)
	 */
	public void tryLockTest(int id) {
		RLock rLock = redissonClient.getLock("lock_prefix_" + id);
		if (rLock.tryLock()) { //如果希望阻塞方式获取分布式锁时，使用RLock#lock()来替换RLock#tryLock()
			try {
				System.out.println("------- 执行业务逻辑 --------" + Thread.currentThread());
				Thread.sleep(1000);
				System.out.println("------- 执行完毕 ----------" + Thread.currentThread());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				rLock.unlock();
			}
		} else {
			System.out.println("get lock failed for " + Thread.currentThread());
		}
	}

	/**
	 * lock 方法回阻塞线程，默认过期时间未30s
	 */
	public void lockTest(int id) {
		RLock rLock = redissonClient.getLock("lock_prefix_" + id);
//		rLock.lock();//30s
		rLock.lock(10, TimeUnit.SECONDS);
		try {
			System.out.println("------- 执行业务逻辑 --------" + Thread.currentThread());
			Thread.sleep(1000);
			System.out.println("------- 执行完毕 ----------" + Thread.currentThread());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rLock.unlock();
		}
	}
}
