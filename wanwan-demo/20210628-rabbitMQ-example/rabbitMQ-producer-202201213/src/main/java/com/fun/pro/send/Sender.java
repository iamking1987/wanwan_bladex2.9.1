package com.fun.pro.send;

import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun.pro.constant.RabbitMqKey;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author zz
 * @date 2021/6/4
 */
@RequiredArgsConstructor
@Component
public class Sender {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 如果rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
	 * 需手动注入
	 */
	private final RabbitTemplate rabbitTemplate;

	/**
	 * fanout模式发送至交换机
	 */
	public String sendFanout(Object payload){
		return baseSend(RabbitMqKey.FANOUT_EXCHANGE, "", payload, null, 3600 * 10L);
	}

	/**
	 * 直接发送到队列
	 */
	public String sendSimple(Object payload){
		return baseSend("", RabbitMqKey.GENERAL_QUEUE, payload, null, 3600 * 1000L);
		//return baseSend("", RabbitMqKey.TRADE_DIRECT_QUEUE, payload, null, 3600 * 10L);
	}

	/**
	 * direct模式发送至交换机
	 */
	public String sendDirect(Object payload) {
		return baseSend(RabbitMqKey.TRADE_DIRECT_EXCHANGE, RabbitMqKey.DIRECT_ROUTING_KEY, payload, null, 3600 * 1000L);
	}

	/**
	 * topic模式发送至交换机
	 */
	public String sendTopic(Object payload) {
		return baseSend(RabbitMqKey.TRADE_TOPIC_EXCHANGE, RabbitMqKey.TOPIC_ROUTING_KEY, payload, null, 3600 * 100L);
	}

	/**
	 * 发送至延时消息队列（direct）
	 */
	public String sendDelay(Object payload){
		return baseSend(RabbitMqKey.TRADE_DELAY_EXCHANGE, RabbitMqKey.DELAY_ROUTING_KEY, payload, null, 3600 * 100L);
	}

	/**
	 * MQ 发送数据基础方法
	 *
	 * @param exchange  交换器名
	 * @param routingKey  队列名
	 * @param payload 消息信息
	 * @param uniqueMessageId  标示id，不传可自动生成
	 * @param messageExpirationTime  持久化时间
	 * @return 消息编号
	 */
	public String baseSend(String exchange, String routingKey, Object payload, String uniqueMessageId, Long messageExpirationTime) {
		//生成唯一消息id
		String finalUniqMessageId = Optional.ofNullable(uniqueMessageId).orElse(UUID.randomUUID().toString());
		// 消息属性
		MessagePostProcessor messagePostProcessor = message -> {
			// 消息属性中写入消息编号
			message.getMessageProperties().setMessageId(finalUniqMessageId);
			// 消息持久化时间
			if (!StringUtils.isEmpty(String.valueOf(messageExpirationTime))) {
				message.getMessageProperties().setExpiration(Long.toString(messageExpirationTime));
			}
			// 设置持久化模式
			message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
			return message;
		};

		// 消息
		Message message = null;
		String json = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			json = objectMapper.writeValueAsString(payload);
			// 转换数据格式
			MessageProperties messageProperties = new MessageProperties();
			//messageProperties.setContentEncoding(MessageProperties.CONTENT_TYPE_JSON);
			message = new Message(json.getBytes(), messageProperties);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// correlationData
		CorrelationData correlationData = new CorrelationData(finalUniqMessageId);

		/*
		 * convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor, CorrelationData correlationData)
		 * exchange: 路由
		 * routingKey: 绑定key
		 * message: 发送消息
		 * messagePostProcessor: 消息属性处理类
		 * correlationData: 对象内部只有一个 id 属性，用来表示当前消息唯一性
		 */
		rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData);
		logger.info("SEND --- messageId：{}, payload:{}", finalUniqMessageId, json);
		return finalUniqMessageId;
	}
}
