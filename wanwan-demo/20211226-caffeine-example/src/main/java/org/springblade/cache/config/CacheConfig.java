package org.springblade.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author wanwan 2021/12/26
 */
@Configuration
public class CacheConfig {

	/*@Bean
	public Cache<String, Object> caffeineCache() {
		return Caffeine.newBuilder()
			// 设置最后一次写入或访问后经过固定时间过期
			.expireAfterWrite(60, TimeUnit.SECONDS)
			// 初始的缓存空间大小
			.initialCapacity(100)
			// 缓存的最大条数
			.maximumSize(1000)
			.build();
	}*/

	@Bean(name = "oneMinuteCaffeineCache")
	public CacheManager oneMinuteCaffeineCache(){
		Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
			.initialCapacity(10) //初始大小
			.maximumSize(20)  //最大大小
			.expireAfterWrite(1, TimeUnit.MINUTES); //写入/更新之后1分钟过期

		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
		caffeineCacheManager.setAllowNullValues(true);
		caffeineCacheManager.setCaffeine(caffeine);
		return caffeineCacheManager;
	}
}
