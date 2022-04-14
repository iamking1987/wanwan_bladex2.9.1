package com.fun.redisson.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wanwan 2022/1/19
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class StockExampleService {

	private final RedissonClient redissonClient;

	/**
	 * lock-模拟这个是商品库存
	 * 		from:https://www.cnblogs.com/qdhxhz/p/11059200.html
	 */
	public static volatile Integer TOTAL = 10;

	public String lockDecreaseStock() throws InterruptedException {
		RLock redissonLock = redissonClient.getLock("lock-" + Thread.currentThread().getId());
		redissonLock.lock(10, TimeUnit.SECONDS);
		if (TOTAL > 0) {
			TOTAL--;
		}
		Thread.sleep(100);
		log.info("======减完库存后,当前库存===" + TOTAL);
		//如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
		if (redissonLock.isHeldByCurrentThread()) {
			redissonLock.unlock();
		}
		return "=================================";
	}

	/**
	 * tryLock-模拟这个是商品库存
	 * 		from:https://www.cnblogs.com/qdhxhz/p/11059200.html
	 */
	public String trylockDecreaseStock() throws InterruptedException {
		RLock redissonLock = redissonClient.getLock("lock-" + Thread.currentThread().getId());
		if (redissonLock.tryLock(10,30,TimeUnit.SECONDS)) {
			if (TOTAL > 0) {
				TOTAL--;
			}
			Thread.sleep(50);
			redissonLock.unlock();
			log.info("====tryLock===减完库存后,当前库存===" + TOTAL);
		} else {
			log.info("[ExecutorRedisson]获取锁失败");
		}
		return "===================================";
	}
}
