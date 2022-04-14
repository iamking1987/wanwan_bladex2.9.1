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
	 * 通用-交换器
	 */
	public static final String FANOUT_EXCHANGE = "fanout_exchange";

	/**
	 * 通用routingKey
	 */
	public static final String GENERAL_ROUTING_KEY = "general_098";


	/*=================DIRECT MODE================*/

	/**
	 * 路由（direct）测试队列
	 */
	public static final String TRADE_DIRECT_QUEUE = "trade-direct-queue";

	/**
	 * 路由（direct）测试交换机
	 */
	public static final String TRADE_DIRECT_EXCHANGE = "trade-direct-exchange";

	/**
	 * 路由-routingKey
	 */
	public static final String DIRECT_ROUTING_KEY = "direct_098";


	/*=================TOPIC MODE================*/

	/**
	 * topic-queue
	 */
	public static final String TRADE_TOPIC_QUEUE = "trade-topic-queue";

	/**
	 * topic-exchange
	 */
	public static final String TRADE_TOPIC_EXCHANGE = "trade-topic-exchange";

	/**
	 * topic-routingKey
	 */
	public static final String TOPIC_ROUTING_KEY = "topic_098";


	/*===============DEAD LETTER QUEUE===============*/

	/**
	 * delay-queue 接受延时消息的队列
	 */
	public static final String TRADE_DELAY_QUEUE = "trade-delay-queue";

	/**
	 * delay-exchange 接收延时消息的交换机
	 */
	public static final String TRADE_DELAY_EXCHANGE = "trade-delay-exchange";

	/**
	 * delay-routingKey
	 */
	public static final String DELAY_ROUTING_KEY = "delay-098";

	/**
	 * dead-letter-queue 死信队列
	 */
	public static final String TRADE_DEAD_LETTER_QUEUE = "trade-dead-letter-queue";

	/**
	 * dead-letter-exchange 死信队列交换机
	 */
	public static final String TRADE_DEAD_LETTER_EXCHANGE = "trade-dead-letter-exchange";

	/**
	 * dead-letter-routingKey
	 */
	public static final String DEAD_LETTER_ROUTING_KEY = "dead-098";

}
