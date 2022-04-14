package org.springblade.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springblade.cache.entity.UserInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @author wanwan 2021/12/26
 */
@Service
@Slf4j
//@CacheConfig(cacheNames = "default") 在注解Cacheable中使用value作用相同
public class UserInfoServiceImpl2 implements UserInfoService {

	/**
	 * 模拟数据库存储数据
	 */
	private static HashMap<Integer, UserInfo> userInfoMap = new HashMap<>();

	static {
		userInfoMap.put(1, new UserInfo(1,"getPersonById","male",26));
	}

//	@Cacheable(cacheManager = "oneMinuteCaffeineCache", value = "user", key = "#id")
	//@Cacheable(cacheManager = "oneMinuteCaffeineCache", key = "#id")
	//@Cacheable("user")
	public UserInfo getPersonById(Integer id,String name){
		log.info("get");
		return userInfoMap.get(id);
	}

	@Override
	//@CachePut(key = "#userInfo.id")
	public void addUserInfo(UserInfo userInfo) {
		log.info("create");
		userInfoMap.put(userInfo.getId(), userInfo);
	}

	@Override
	@Cacheable(key = "#id")
	public UserInfo getByName(Integer id) {
		log.info("get");
		return userInfoMap.get(id);
	}

	@Override
	@CachePut(key = "#userInfo.id")
	public UserInfo updateUserInfo(UserInfo userInfo) {
		log.info("update");
		if (!userInfoMap.containsKey(userInfo.getId())) {
			return null;
		}
		// 取旧的值
		UserInfo oldUserInfo = userInfoMap.get(userInfo.getId());
		// 替换内容
		if (!StringUtils.isEmpty(oldUserInfo.getAge())) {
			oldUserInfo.setAge(userInfo.getAge());
		}
		if (!StringUtils.isEmpty(oldUserInfo.getName())) {
			oldUserInfo.setName(userInfo.getName());
		}
		if (!StringUtils.isEmpty(oldUserInfo.getSex())) {
			oldUserInfo.setSex(userInfo.getSex());
		}
		// 将新的对象存储，更新旧对象信息
		userInfoMap.put(oldUserInfo.getId(), oldUserInfo);
		// 返回新对象信息
		return oldUserInfo;
	}

	@Override
	@CacheEvict(key = "#id")
	public void deleteById(Integer id) {
		log.info("delete");
		userInfoMap.remove(id);
	}
}
