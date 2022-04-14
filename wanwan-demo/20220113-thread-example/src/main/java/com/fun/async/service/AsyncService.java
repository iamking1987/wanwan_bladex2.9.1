package com.fun.async.service;

/**
 * @author wanwan 2022/2/25
 */
public interface AsyncService {
	/**
	 * 执行异步任务
	 * 可以根据需求，自己加参数拟定，我这里就做个测试演示
	 */
	void executeAsync(Integer i);
}
