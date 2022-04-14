package com.fun.redlock.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wanwan 2022/2/15
 */
@Service
@RequiredArgsConstructor
public class RedisTemplateService {

	private final RedisTemplate<String,String> redisTemplate;
	private final StringRedisTemplate stringRedisTemplate;

	// ===================通用操作===================

	/**
	 * 删除多个key
	 */
	public void deleteKey (String ...keys){
		redisTemplate.delete(Lists.newArrayList(keys));
	}

	/**
	 * 指定key的失效时间
	 */
	public void expire(String key,long time){
		redisTemplate.expire(key,time,TimeUnit.MINUTES);
	}

	/**
	 * 根据key获取过期时间
	 */
	public long getExpire(String key){
		return redisTemplate.getExpire(key);
	}

	/**
	 * 判断key是否存在
	 */
	public boolean hasKey(String key){
		return redisTemplate.hasKey(key);
	}

	// ================ValueOperations：简单K-V操作===============

	/**
	 * RedisTemplate中，key 默认的序列化方案是 JdkSerializationRedisSerializer
	 * 		JdkSerializationRedisSerializer会导致key带前缀
	 */
	@Test
	public void test1() {
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		ValueOperations<String,String> ops = redisTemplate.opsForValue();
		ops.set("k1", "v1");
		Object k1 = ops.get("k1");
		System.out.println(k1);
	}

	/**
	 * StringRedisTemplate 中，key 默认的序列化方案是 StringRedisSerializer
	 * 		StringRedisSerializer不会有前缀
	 */
	@Test
	public void test2() {
		ValueOperations<String,String> ops = stringRedisTemplate.opsForValue();
		ops.set("k2", "v2");
		Object k1 = ops.get("k2");
		System.out.println(k1);
	}

	// ===================boundValueOps 操作===================

	/*
	 * opsForXXX和boundXXXOps的区别？
	 * 前者获取一个operator，但是没有指定操作的对象（key），可以在一个连接（事务）内操作多个key以及对应的value；
	 * 后者获取了一个指定操作对象（key）的operator，在一个连接（事务）内只能操作这个key对应的value。
	 */

	/**
	 * 操作value/String
	 */
	@Test
	public void test3(){
		redisTemplate.boundValueOps("testValue1").set("value1",60, TimeUnit.SECONDS); //设置过期时间为60秒，60秒后自动删除
		redisTemplate.boundValueOps("testValue2").set("value2");
	}

	@Test
	public void test4(){
		Object o1 = redisTemplate.boundValueOps("testValue1").get();
		Object o2 = redisTemplate.boundValueOps("testValue2").get();
		System.out.println(o1 + " "  + o2);

	}

	/**
	 * 操作list
	 */
	@Test
	public void test5(){
		redisTemplate.boundListOps("list").leftPush("aaa");
		redisTemplate.boundListOps("list").leftPush("aaa");
		redisTemplate.boundListOps("list").leftPush("aaa");
		redisTemplate.boundListOps("list").leftPush("bbb");
		redisTemplate.boundListOps("list").rightPush("ccc");
		redisTemplate.boundListOps("list").rightPush("ddd");

		//放入一个list
		ArrayList<String> list = new ArrayList<>();
		redisTemplate.boundListOps("listKey").rightPushAll(list.stream().toArray(String[]::new));
		redisTemplate.boundListOps("listKey").leftPushAll(list.stream().toArray(String[]::new));
	}

	@Test
	public void test6(){
		List<?> list = redisTemplate.boundListOps("list").range(0,10); //查询，range(0,10)会查询出0-10的元素
		System.out.println(list);
		System.out.println(redisTemplate.boundValueOps("list").getKey()); //获取key值
		redisTemplate.boundListOps("list").remove(2,"aaa"); //删除两个个aaa

		List<?> list1 = redisTemplate.boundListOps("list").range(0,10); //查询，range(0,10)会查询出0-10的元素
		System.out.println(list1);
		redisTemplate.boundListOps("list").expire(60,TimeUnit.SECONDS); //设置60秒后过期

		System.out.println(redisTemplate.boundListOps("list").index(1)); //根据索引获取值
		System.out.println(redisTemplate.boundListOps("list").leftPop()); //bbb,打印左边起第一个元素值
	}

	/**
	 * 操作hash
	 */
	@Test
	public void test7(){
		redisTemplate.boundHashOps("hash").put("1", "a");
		redisTemplate.boundHashOps("hash").put("2", "b");
		redisTemplate.boundHashOps("hash").put("3", "c");
		redisTemplate.boundHashOps("hash").put("4", "d");
		//添加一个map集合
		HashMap<String, String> hashMap = new HashMap<>();
		redisTemplate.boundHashOps("HashKey").putAll(hashMap );
	}


	@Test
	public void test8(){
		List hash = redisTemplate.boundHashOps("hash").values();
		System.out.println(hash);

		Set set = redisTemplate.boundHashOps("hash").keys();
		System.out.println(set);

		Object o = redisTemplate.boundHashOps("hash").get("1");
		System.out.println(o);

	}



}
