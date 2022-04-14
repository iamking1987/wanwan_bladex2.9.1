package com.fun.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author wanwan 2022/1/18
 */
@Configuration
public class RedissonConfig {

	@Autowired
	private RedisProperties redisProperties;

	/**
	 * 从application.redis配置中获取
	 */
	/*@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		String redisUrl = String.format("redis://%s:%s", redisProperties.getHost() + "", redisProperties.getPort() + "");
		config.useSingleServer().setAddress(redisUrl).setPassword(redisProperties.getPassword());
		config.useSingleServer().setDatabase(3);
		return Redisson.create(config);
	}*/

	/**
	 * 从其他文件中读取
	 */
	@Bean(destroyMethod="shutdown") // 服务停止后调用 shutdown 方法。
	public RedissonClient redissonClient() throws IOException {
		Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-single.yml"));
		return Redisson.create(config);
	}
}
