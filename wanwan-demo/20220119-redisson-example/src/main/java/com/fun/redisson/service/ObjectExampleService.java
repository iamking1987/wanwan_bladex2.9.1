package com.fun.redisson.service;

import com.fun.redisson.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wanwan 2022/1/19
 * 参考：
 * 	https://github.com/redisson/redisson/wiki/
 * 	https://chenyangjie.com.cn/articles/2020/03/28/1585403311935.html
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ObjectExampleService {

	private final RedissonClient redissonClient;

	/**
	 * object bucket
	 * 		Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象。
	 * 		除了同步接口外，还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
	 */
	public void setRBucketObject() {
		Student student = new Student("001", "Jimmy", "F");

		RBucket<Student> rBucket = redissonClient.getBucket("cache:student:" + student.getNumber());
		rBucket.trySet(student,30, TimeUnit.SECONDS);
//		rBucket.set(student, 30000L, TimeUnit.MILLISECONDS);
//		rBucket.compareAndSet(new Student("002","tt","f"), new Student("002","tt","m"));

//		Map<String, Object> map = new HashMap<>();
//		map.put("myBucket1", new Student());
//		map.put("myBucket2", new Student());
//		rBucket.trySet(map,30, TimeUnit.SECONDS);

		System.out.println(rBucket.get());
	}

	public void setRList() {
		List<Student> students = new ArrayList<>();
		students.add(new Student("001", "Jimmy", "F"));
		students.add(new Student("002", "Jack", "M"));
		students.add(new Student("003", "Julia", "F"));

		RList<Student> rList = redissonClient.getList("cache:list");
		//rList.clear();
		rList.addAll(students);
		rList.add(new Student("004", "Jane", "F"));
		rList.addAfter(new Student("001", "Jimmy", "F"), new Student("005", "Neko", "M"));
		System.out.println("index 1 : " + rList.get(1));

		// 获取下标为 0 和 3 的两个元素组成的列表
		// get(int...indexes)
		rList.get(0, 3).forEach(System.out::println);
	}

	public void setRMap() {
		RMap<String, Student> rMap = redissonClient.getMap("cache:map");
		rMap.clear();

		rMap.put("001", new Student("001", "Jimmy", "F"));
		rMap.put("002", new Student("002", "Jack", "M"));
		rMap.put("003", new Student("003", "Julia", "F"));
		rMap.put("004", new Student("004", "Jane", "F"));

		log.info("rMap contains key '005' : {}", rMap.containsKey("005"));
		log.info("rMap size : {}", rMap.size());
		log.info("rMap get value by '002' : {}", rMap.get("002"));
		log.info("rMap put value in existed key '001' : {}", rMap.putIfAbsent("001", new Student()));

		Student removeStudent = rMap.remove("001");
		rMap.fastPut("001",new Student());
		rMap.fastRemove("001");
	}

	/**
	 * 元素淘汰的RMap
	 * 	基于RMap的前提下实现了针对单个元素的淘汰机制
	 */
	public void setRMapCache() {
		RMapCache<Object, Student> map = redissonClient.getMapCache("cache:map:cache");
		map.put("key1", new Student(), 10, TimeUnit.MINUTES);
		map.put("key1", new Student(), 10, TimeUnit.MINUTES, 10, TimeUnit.SECONDS);
		map.putIfAbsent("key2", new Student(), 3, TimeUnit.SECONDS);
		map.putIfAbsent("key2", new Student(), 40, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);
	}

	public void setRAtomic() {
		RAtomicLong rAtomicLong = redissonClient.getAtomicLong("cache:atomic:long");

		log.info("Init value : {}", rAtomicLong.get());
		log.info("After increment value: {}", rAtomicLong.incrementAndGet());
		log.info("After add value : {}", rAtomicLong.addAndGet(100L));
		RAtomicDouble rAtomicDouble = redissonClient.getAtomicDouble("cache:atomic:double");
		rAtomicDouble.set(10.0D);
		log.info("Set value : {}", rAtomicDouble.get());
		log.info("After increment value :{}", rAtomicDouble.incrementAndGet());
		log.info("After decrement value :{}", rAtomicDouble.decrementAndGet());
		rAtomicDouble.compareAndSet(10.0D, 13.3D);
		log.info("After compare and set value :{}", rAtomicDouble.get());
	}
}
