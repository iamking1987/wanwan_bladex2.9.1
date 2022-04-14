package com.fun.redlock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jedis分布式锁
 * @author wanwan 2022/1/16
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JLock {

	/**
	 * 默认包名加方法名
	 * @return
	 */
	String key() default "";

	/**
	 * 过期时间 单位：毫秒
	 * <pre>
	 *     过期时间一定是要长于业务的执行时间.
	 * </pre>
	 */
	long expire() default 30000;
}
