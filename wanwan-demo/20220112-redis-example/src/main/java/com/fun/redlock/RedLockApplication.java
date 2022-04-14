package com.fun.redlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author wanwan 2022/1/12
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class RedLockApplication {
	public static void main(String[] args) {
		SpringApplication.run(RedLockApplication.class, args);
	}
}
