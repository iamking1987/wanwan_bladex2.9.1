package com.fun.flux.config;

import com.fun.flux.entity.UserVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author wanwan 2022/3/9
 */
@Configuration
public class RedisConfig {

	@Bean
	public ReactiveRedisTemplate<String, Object> commonRedisTemplate(ReactiveRedisConnectionFactory factory) {
		RedisSerializationContext<String, Object> serializationContext =
			RedisSerializationContext.<String, Object>newSerializationContext(RedisSerializer.string())
				.key(StringRedisSerializer.UTF_8)
				.value(RedisSerializer.json())
				.hashKey(StringRedisSerializer.UTF_8)
				.hashValue(StringRedisSerializer.UTF_8)
				.build();
		return new ReactiveRedisTemplate<>(factory, serializationContext);
	}

	@Bean
	public ReactiveRedisTemplate<String, UserVO> userRedisTemplate(ReactiveRedisConnectionFactory factory) {
		RedisSerializationContext<String, UserVO> serializationContext =
			RedisSerializationContext.<String, UserVO>newSerializationContext(RedisSerializer.string())
				.key(StringRedisSerializer.UTF_8)
				.value(new Jackson2JsonRedisSerializer<>(UserVO.class))
				.build();
		return new ReactiveRedisTemplate<>(factory, serializationContext);
	}
}
