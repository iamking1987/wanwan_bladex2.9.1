package org.springblade.cache;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Random;

/**
 * @author wanwan 2021/12/26
 */

@SpringBootApplication
@EnableCaching
public class CacheApplication {

	public static void main(String[] args) {
		long value = new Random().nextLong();
		System.out.println(value);
	}
}
