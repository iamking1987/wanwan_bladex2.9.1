package com.fun.async.config;

import com.fun.async.handler.VisitableThreadPoolTaskExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池配置
 * @author wanwan 2022/2/25
 */

@Configuration
@EnableAsync
public class CustomizeThreadPoolConfig {

	@Value("${thread.pool.corePoolSize:5}")
	private int corePoolSize;

	@Value("${thread.pool.maxPoolSize:10}")
	private int maxPoolSize;

	@Value("${thread.pool.queueCapacity:200}")
	private int queueCapacity;

	@Value("${thread.pool.keepAliveSeconds:30}")
	private int keepAliveSeconds;

	@Value("${thread.pool.threadNamePrefix:ASYNC_}")
	private String threadNamePrefix;

	@Bean
	public Executor getAsyncExecutor() {
		//默认使用的Executor
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//可选--自定义能打印线程池信息的Executor
		ThreadPoolTaskExecutor executor = new VisitableThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setThreadNamePrefix(threadNamePrefix);
		//CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
