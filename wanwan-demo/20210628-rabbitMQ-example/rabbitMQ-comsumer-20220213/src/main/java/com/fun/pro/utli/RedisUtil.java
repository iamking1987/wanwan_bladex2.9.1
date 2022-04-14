package com.fun.pro.utli;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wanwan 2022/1/20
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtil<T> {

	private final RedisTemplate<String, T> redisTemplate;


	/**
	 * 添加 key:string 缓存
	 */
	public boolean cacheValue(String k, T v, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(k, v, time, TimeUnit.SECONDS);
			} else {
				cacheValue(k, v);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 添加 key:string 缓存
	 */
	public boolean cacheValue(String key, T value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 当key不存在时添加 key:string 缓存(setNX)
	 * 当出现异常时会返回{@code null},可以根据实际情况改为false
	 */
	public Boolean cacheValueIfAbsent(String k, T v, long time) {
		try {
			ValueOperations<String, T> ops = redisTemplate.opsForValue();
			Boolean setFlag;
			if (time > 0) {
				setFlag = ops.setIfAbsent(k, v, time, TimeUnit.SECONDS);
			}
			else {
				setFlag = ops.setIfAbsent(k, v);
			}
			return setFlag;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 当key不存在时添加 key:string 缓存(setNX)
	 * 当出现异常时会返回{@code null},可以根据实际情况改为false
	 */
	public Boolean cacheValueIfAbsent(String key, T value) {
		return cacheValueIfAbsent(key, value, -1);
	}

	/**
	 * 查询缓存 key 是否存在
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Throwable e) {
			log.error("判断缓存存在失败key:[" + key + "],错误信息 [{}]", e);
		}
		return false;
	}

	/**
	 * 判断hash表中是否有该项的值
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey(String key, String item) {
		try {
			return redisTemplate.opsForHash().hasKey(key, item);
		} catch (Throwable e) {
			log.error("判断缓存存在失败key:[" + key + "],错误信息 [{}]", e);
		}
		return false;
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * @param key 键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Throwable e) {
			log.error("判断缓存存在失败key:[" + key + "],错误信息 [{}]", e);
		}
		return false;
	}


	/**
	 * 根据 key 获取缓存value
	 */
	public T getValue(String key) {
		try {
			ValueOperations<String, T> ops = redisTemplate.opsForValue();
			return ops.get(key);
		} catch (Exception e) {
			log.error("根据 key 获取缓存失败，当前key:[{}],失败原因:[{}]", key, e);
		}
		return null;
	}

	/**
	 * 扫描符合匹配到的key
	 */
	public Set<String> scan(String keyPattern) {
		Set<String> keys = new HashSet<>();
		redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
			//1000是步进值，过小效率会低一些，尽量与数据级匹配。
			ScanOptions so = new ScanOptions.ScanOptionsBuilder().match(keyPattern).count(1000).build();
			Cursor<byte[]> cursor = connection.scan(so);
			while (cursor.hasNext()) {
				keys.add(new String(cursor.next()));
			}
			return keys;
		});
		return keys;
	}


	/**
	 * 缓存set操作
	 */
	public boolean cacheSet(String k, T v, long time) {
		try {
			SetOperations<String, T> opsForSet = redisTemplate.opsForSet();
			opsForSet.add(k, v);
			if (time > 0) {
				redisTemplate.expire(k, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Throwable e) {
			log.error("缓存 set 失败 当前 key:[{}] 失败原因 [{}]", k, e);
		}



		return false;
	}


	/**
	 * 添加 set 缓存
	 *
	 * @param key   key
	 * @param value value
	 * @return true/false
	 */
	public boolean cacheSet(String key, T value) {
		return cacheSet(key, value, -1);
	}


	/**
	 * 添加 缓存 set
	 */
	public boolean cacheSet(String k, Set<T> v, long time) {
		try {
			SetOperations<String, T> opsForSet = redisTemplate.opsForSet();
			opsForSet.add(k, (T) v.toArray(new String[0]));
			if (time > 0) {
				redisTemplate.expire(k, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Throwable e) {
			log.error("缓存 set 失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return false;
	}


	/**
	 * 缓存 set
	 */
	public boolean cacheSet(String k, Set<T> v) {
		return cacheSet(k, v, -1);
	}


	/**
	 * 获取缓存set数据
	 */
	public Set<T> getSet(String k) {
		try {
			SetOperations<String, T> opsForSet = redisTemplate.opsForSet();
			return opsForSet.members(k);
		} catch (Throwable e) {
			log.error("获取缓存set失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return null;
	}

	/**
	 * 缓存hash操作
	 */
	public boolean cacheHash(String k, String hashKey, T v, long time) {
		try {
			HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
			opsForHash.put(k, hashKey, v);
			if (time > 0) {
				redisTemplate.expire(k, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Throwable e) {
			log.error("缓存 hash 失败 当前 key:[{}] 失败原因 [{}]", k, e);
		}
		return false;
	}


	/**
	 * 根据 key 自增hash 的 value
	 */
	public Long incrHash(String key, String hashKey, long delta) {
		return redisTemplate.opsForHash().increment(key, hashKey, delta);
	}

	/**
	 * 添加 Hash 缓存
	 */
	public boolean cacheHash(String key, String hashKey, T value) {
		return cacheHash(key, hashKey, value, -1);
	}


	/**
	 * 添加 缓存 Hash
	 */
	public boolean cacheHashAll(String k, Map<String, Object> v, long time) {
		try {
			HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
			opsForHash.putAll(k, v);
			if (time > 0) {
				redisTemplate.expire(k, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Throwable e) {
			log.error("缓存 hash 失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return false;
	}


	/**
	 * 缓存 hash all
	 */
	public boolean cacheHashAll(String k, Map<String, Object> v) {
		return cacheHashAll(k, v, -1);
	}

	/**
	 * 缓存 hash all（传入对象形式）
	 */
	public boolean cacheHashAll4object(String k, Object v) {
		return cacheHashAll(k, BeanUtil.beanToMap(v), -1);
	}

	/**
	 * 缓存 hash all（传入对象形式）
	 */
	public boolean cacheHashAll4object(String k, Object v, long time) {
		return cacheHashAll(k, BeanUtil.beanToMap(v), time);
	}

	/**
	 * 缓存 hash all（传入对象形式）
	 *
	 * @param k               key
	 * @param v               value Object
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @param time            时间/秒
	 * @return {@link boolean}
	 */
	public boolean cacheHashAll4object(String k, Object v, boolean ignoreNullValue, long time) {
		return cacheHashAll(k, BeanUtil.beanToMap(v,false,true), time);
	}


	/**
	 * 获取缓存hash数据
	 */
	public T getHash(String k, String hashKey) {
		try {
			HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
			return opsForHash.get(k, hashKey);
		} catch (Throwable e) {
			log.error("获取缓存 hash 失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return null;
	}

	/**
	 * 获取缓存 hash 所有键值对数据
	 */
	public Map<String, T> getHashEntry(String k) {
		try {
			HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
			return opsForHash.entries(k);
		} catch (Throwable e) {
			log.error("获取缓存 hash 失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return null;
	}


	/**
	 * list 缓存
	 */
	public boolean cacheList(String k, T v, long time) {
		try {
			ListOperations<String, T> opsForList = redisTemplate.opsForList();
			//此处为right push 方法/ 也可以 left push ..
			opsForList.rightPush(k, v);
			if (time > 0) {
				redisTemplate.expire(k, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Throwable e) {
			log.error("缓存list失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return false;
	}


	/**
	 * 缓存 list
	 */
	public boolean cacheList(String k, T v) {
		return cacheList(k, v, -1);
	}


	/**
	 * 缓存 list 集合
	 *
	 * @param k    key
	 * @param v    value
	 * @param time 时间/秒
	 * @return {@link boolean}
	 */
	public boolean cacheList(String k, List<T> v, long time) {
		try {
			ListOperations<String, T> opsForList = redisTemplate.opsForList();
			opsForList.rightPushAll(k, v);
			if (time > 0) {
				redisTemplate.expire(k, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Throwable e) {
			log.error("缓存list失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return false;
	}


	/**
	 * 缓存 list
	 */
	public boolean cacheList(String k, List<T> v) {
		return cacheList(k, v, -1);
	}


	/**
	 * 根据 key 获取 list 缓存
	 *
	 * @param k     key
	 * @param start 开始
	 * @param end   结束
	 * @return 获取缓存区间内 所有value
	 */
	public List<T> getList(String k, long start, long end) {
		try {
			ListOperations<String, T> opsForList = redisTemplate.opsForList();
			return opsForList.range(k, start, end);
		} catch (Throwable e) {
			log.error("获取list缓存失败 当前 key:[{}],失败原因 [{}]", k, e);
		}
		return null;
	}

	/**
	 * 根据 key 获取总条数 用于分页
	 *
	 * @param key key
	 * @return 条数
	 */
	public long getListSize(String key) {
		try {
			ListOperations<String, T> opsForList = redisTemplate.opsForList();
			return opsForList.size(key);
		} catch (Throwable e) {
			log.error("获取list长度失败key[" + key + "],[" + e + "]");
		}
		return 0;
	}


	/**
	 * 获取总条数 用于分页
	 *
	 * @param listOps =redisTemplate.opsForList();
	 * @param k       key
	 * @return size
	 */
	public long getListSize(ListOperations<String, String> listOps, String k) {
		try {
			return listOps.size(k);
		} catch (Throwable e) {
			log.error("获取list长度失败key[" + k + "],[" + e + "]");
		}
		return 0;
	}

	/**
	 * 获取set缓存的长度
	 * @param key 键
	 */
	public long getSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Throwable e) {
			log.error("获取list长度失败key[" + key + "],[" + e + "]");
		}
		return 0;
	}


	/**
	 * 根据 key 移除 list 缓存
	 */
	public boolean removeOneOfList(String k) {
		try {
			ListOperations<String, T> opsForList = redisTemplate.opsForList();
			opsForList.rightPop(k);
			return true;
		} catch (Throwable e) {
			log.error("移除list缓存失败 key[" + k + "],[" + e + "]");
		}
		return false;
	}

	/**
	 * 删除多个缓存
	 */
	public boolean del(String... key) {
		if (key != null && key.length > 0) {
			try {
				if (key.length == 1) {
					redisTemplate.delete(key[0]);
				} else {
					redisTemplate.delete(Lists.newArrayList(key));
				}
				return true;
			} catch (Throwable e) {
				log.error("移除缓存失败 key:[{}] 失败原因 [{}]", key, e);
			}
		}
		return false;
	}

	/**
	 * 删除hash表中的值
	 * @param key 键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel(String key, Object... item) {
		try {
			redisTemplate.opsForHash().delete(key, item);
		} catch (Throwable e) {
			log.error("移除缓存失败 key:[{}] 失败原因 [{}]", key, e);
		}
	}

	/**
	 * 移除值为value的
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long sdel(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Throwable e) {
			log.error("移除缓存失败 key:[{}] 失败原因 [{}]", key, e);
		}
		return 0;
	}

	/**
	 * 根据 key 自增value
	 */
	public Long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0!");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 * @param key 键
	 * @param delta 要减少几(小于)
	 */
	public long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0!");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}
}
