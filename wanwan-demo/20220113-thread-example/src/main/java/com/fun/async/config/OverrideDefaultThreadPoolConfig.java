package com.fun.async.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 重写默认线程池配置
 * @author wanwan 2021/12/25
 */
@Slf4j
@EnableAsync
//@Configuration
public class OverrideDefaultThreadPoolConfig implements AsyncConfigurer {

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
	public Executor messageExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setThreadNamePrefix(threadNamePrefix);
		//设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
		executor.setWaitForTasksToCompleteOnShutdown(true);
		//设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
		executor.setAwaitTerminationSeconds(60);
		//CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> {
			log.error("==========================" + ex.getMessage() + "=======================", ex);
			log.error("exception method:" + method.getName());
		};
	}
}
