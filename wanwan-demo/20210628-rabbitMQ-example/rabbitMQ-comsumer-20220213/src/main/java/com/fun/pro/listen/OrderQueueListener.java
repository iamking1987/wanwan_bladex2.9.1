package com.fun.pro.listen;

import com.alibaba.fastjson.JSONObject;
import com.fun.pro.constant.RabbitMqKey;
import com.fun.pro.utli.RedisUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 队列监听类样例
 * @author zz
 * @date 2021/6/5
 */
//@Component
public class OrderQueueListener {
	private static final Logger logger = LoggerFactory.getLogger(OrderQueueListener.class);

	private static final String HEADER_MESSAGE_ID = "spring_returned_message_correlation";
	private static final String MESSAGE_KEY_PREFIX = "rabbit:message:id:";

	@Autowired
	private RedisUtil<String> redisUtil;

	/**
	 * direct
	 * 1、manual--不会retry，异常直接加入死信队列
	 * 2、消息幂等 30天内有效
	 */
	@RabbitListener(queues = RabbitMqKey.TRADE_DIRECT_QUEUE)
	public void process2(Message message, Channel channel) throws IOException {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		String messageId = message.getMessageProperties().getMessageId();
		logger.info("RECEIVE --- messageId:{},queue:{}", messageId, RabbitMqKey.TRADE_DIRECT_QUEUE);
		JSONObject jsonObject = null;
		try {
			boolean hasKey = redisUtil.hasKey(MESSAGE_KEY_PREFIX + messageId);
			if (hasKey) {
				//1、messageId重复进来后需要进一步检查是否已对该message正确处理
				//1.1、如果正确处理则ack
				//1.2 否则再处理一次
				channel.basicAck(deliveryTag,false);
				logger.info("MESSAGE REPEAT --- messageId:{}, message{}", messageId, jsonObject);
				return;
			}
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			jsonObject = JSONObject.parseObject(msg);
			//int a = 1 / 0;
			// 成功确认，二参为是否批量处理消息
			channel.basicAck(deliveryTag,false);
			logger.info("ACK SUCCESS --- messageId:{}, message{}", messageId, jsonObject);
			redisUtil.cacheValue(MESSAGE_KEY_PREFIX+messageId, messageId, 3600 * 24 * 30);
		} catch (Exception e) {
			//重试的时候记住设置为false，才会被丢弃发到死信队列
			channel.basicReject(deliveryTag,false);
			logger.error("ACK FAIL --- messageId:{}, message:{}", messageId, jsonObject);
			//注意这里如果throw异常会进入重试
			e.printStackTrace();
		}
	}

	/**
	 * direct
	 * auto模式:
	 * 1.配置retry--会进行retry，最后一次会加入到死信队列
	 * 2.没配置retry--即使有死信也不会进，而是一直重复消费
	 */
	//@RabbitListener(queues = RabbitMqKey.TRADE_DIRECT_QUEUE)
	public void process3(Message message) throws IOException {
		String messageId = message.getMessageProperties().getHeader(HEADER_MESSAGE_ID);
		logger.info("RECEIVE --- messageId:{},queue:{}", messageId, RabbitMqKey.TRADE_DIRECT_QUEUE);
		JSONObject jsonObject = null;
		try {
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			jsonObject = JSONObject.parseObject(msg);
			int a = 1 / 0;
			logger.info("ACK SUCCESS --- messageId:{}, message{}", messageId, jsonObject);
		} catch (Exception e) {
			logger.warn("ACK FAIL --- messageId:{}, message:{}", messageId, jsonObject);
			//一定要重新抛出异常 否则重试没用！
			throw new RuntimeException(e);
		}
	}

	/**
	 * 监听delay队列消息
	 */
	@RabbitListener(queues = RabbitMqKey.TRADE_DELAY_QUEUE)
	public void process4(Message message, Channel channel) throws IOException {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		String messageId = message.getMessageProperties().getHeader(HEADER_MESSAGE_ID);
		logger.info("RECEIVE --- messageId:{},queue:{}", messageId, RabbitMqKey.TRADE_DELAY_QUEUE);
		JSONObject jsonObject = null;
		try {
			//int a = 1 / 0;
			String msg = new String(message.getBody());
			jsonObject = JSONObject.parseObject(msg);
			// 成功确认，二参为是否批量处理消息
			channel.basicAck(deliveryTag,false);
			logger.info("ACK SUCCESS --- messageId:{}, message{}", messageId, jsonObject);
		} catch (Exception e) {
			logger.warn("ACK FAIL --- messageId:{}, message:{}", messageId, jsonObject);
			//失败后消息被确认,二参为是否重新放回队列
			channel.basicReject(deliveryTag, false);
		}
	}

	/**
	 * 接收死信队列的消息
	 */
	@RabbitListener(queues = RabbitMqKey.TRADE_DEAD_LETTER_QUEUE)
	public void process5(Message message, Channel channel) throws IOException {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		String messageId = message.getMessageProperties().getHeader(HEADER_MESSAGE_ID);
		logger.info("RECEIVE --- messageId:{},queue:{}",messageId, RabbitMqKey.TRADE_DEAD_LETTER_QUEUE);
		JSONObject jsonObject = null;
		try {
			String msg = new String(message.getBody());
			jsonObject = JSONObject.parseObject(msg);
			channel.basicAck(deliveryTag, false);
			logger.info("ACK SUCCESS --- messageId:{}, message{}", messageId, jsonObject);
		} catch (Exception e) {
			logger.warn("ACK FAIL --- messageId:{}, message:{}", messageId, jsonObject);
			//失败后消息被确认,二参为是否重新放回队列
			channel.basicReject(deliveryTag, false);
		}
	}
}
