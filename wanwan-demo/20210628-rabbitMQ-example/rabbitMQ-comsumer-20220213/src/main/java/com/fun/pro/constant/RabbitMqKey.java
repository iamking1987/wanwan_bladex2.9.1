package com.fun.pro.constant;

/**
 * 定义队列、交换机、routingKey等常量
 * @author zz
 * @date 2021/6/4
 */
public class RabbitMqKey {

	/**
	 * 通用-队列
	 */
	public static final String GENERAL_QUEUE = "general_queue";

	/**
	 * 路由（direct）测试队列
	 */
	public static final String TRADE_DIRECT_QUEUE = "trade-direct-queue";

	/**
	 * topic-queue
	 */
	public static final String TRADE_TOPIC_QUEUE = "trade-topic-queue";

	/**
	 * delay-queue 接受延时消息的队列
	 */
	public static final String TRADE_DELAY_QUEUE = "trade-delay-queue";


	/**
	 * dead-letter-queue 死信队列
	 */
	public static final String TRADE_DEAD_LETTER_QUEUE = "trade-dead-letter-queue";

}
