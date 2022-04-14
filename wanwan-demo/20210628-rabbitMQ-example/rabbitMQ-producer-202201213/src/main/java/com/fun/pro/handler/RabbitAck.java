package com.fun.pro.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 配置回调（监听消息是否被正确路由）
 * @author wanwan 2022/2/13
 */
@Slf4j
public class RabbitAck implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback{

	/**
	 * 消息是否成功发送到 Exchange
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		log.info("ACK --- MQ message id: {}" + correlationData.getId());
		if (ack) {
			log.info("ACK --- Message send to exchange success！");
		} else {
			log.info("ACK --- Message send to exchange failed, reason for failure:" + cause);
		}
	}

	/**
	 * 消息是否正确被路由到queue
	 */
	@Override
	public void returnedMessage(ReturnedMessage returnedMessage) {
		log.warn("ACK --- Message sent to queue fail！--returned：{}", returnedMessage);
	}
}
