package com.fun.async.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池信息打印类
 * @author wanwan 2022/2/25
 */
@Slf4j
public class VisitableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {


	private void showThreadPoolInfo(String prefix) {
		ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

		log.info("[{}], [{}],任务数:{}, 已完成任务数:{}, 线程数:{}, 队列长度:{}",
			this.getThreadNamePrefix(),
			prefix,
			threadPoolExecutor.getTaskCount(),
			threadPoolExecutor.getCompletedTaskCount(),
			threadPoolExecutor.getActiveCount(),
			threadPoolExecutor.getQueue().size());
	}

	@Override
	public void execute(Runnable task) {
		showThreadPoolInfo("do execute...");
		super.execute(task);
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		showThreadPoolInfo("do execute...");
		super.execute(task, startTimeout);
	}

	@Override
	public Future<?> submit(Runnable task) {
		showThreadPoolInfo("do submit...");
		return super.submit(task);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		showThreadPoolInfo("do submit...");
		return super.submit(task);
	}

	@Override
	public ListenableFuture<?> submitListenable(Runnable task) {
		showThreadPoolInfo("do submitListenable...");
		return super.submitListenable(task);
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		showThreadPoolInfo("do submitListenable...");
		return super.submitListenable(task);
	}
}
