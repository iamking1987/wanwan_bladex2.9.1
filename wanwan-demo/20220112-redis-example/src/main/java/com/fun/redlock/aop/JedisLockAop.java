package com.fun.redlock.aop;

import com.fun.redlock.annotation.JLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 来自：https://www.cnblogs.com/2YSP/p/11563448.html
 */
@Component
@Aspect
@Slf4j
public class JedisLockAop {

//	@Autowired
//	private IRedisDistributedLock redisDistributedLock;

	@Pointcut(value = "@annotation(jlock)")
	public void pt1(JLock jlock){}

	@Pointcut("execution(* *..*.test(..))")
	public void pt2(){}

	@Around(value = "@annotation(jlock)")
	public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, JLock jlock) {
		//获取注解内容
//		String key = jlock.key();
//		long expire = jlock.expire();
		// 加锁
		String key = getKey(proceedingJoinPoint, jlock);
		Boolean success = null;
		System.out.println("hello");
		try {
//			success = redisDistributedLock
//				.lock(key, lock.timeout(), lock.expire(), lock.retryTimes());
//			if (success) {
//				log.info(Thread.currentThread().getName() + " 加锁成功");
//				return proceedingJoinPoint.proceed();
//			}
//			log.info(Thread.currentThread().getName() + " 加锁失败");
			return null;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return null;
		} finally {
//			if (success){
//				boolean result = redisDistributedLock.release(key);
//				log.info(Thread.currentThread().getName() + " 释放锁结果:{}",result);
//			}
		}
	}

	@Around("pt1(jlock) && pt2()")
	public void test(ProceedingJoinPoint proceedingJoinPoint, JLock jlock) {
		try {
			proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private String getKey(JoinPoint joinPoint, JLock lock) {
		if (!StringUtils.isBlank(lock.key())) {
			return lock.key();
		}
		return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature()
			.getName();
	}
}
