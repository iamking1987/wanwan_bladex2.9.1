package com.fun.pro.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wanwan 2022/2/13
 */
@Slf4j
@Configuration
public class RabbitmqConfig {

	/**
	 * 配置回调（监听消息是否被正确路由）
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		//mandatory 为 true 时，当消息无法路由到队列时，会将消息返还给消费者客户端（通过Basic.Return）
		//可以添加回调，接收并处理返回的消息，默认false
		rabbitTemplate.setMandatory(true);
		//回调--消息是否正确被路由到queue
		//当exchange不存在时，不会触发。2. 当exchange存在时，routingKey不匹配时，会触发。3. 当发送到队列时，不会触发
		rabbitTemplate.setReturnsCallback(returned -> log.info("ACK --- Message sent to queue fail！--returned：{}" + returned.toString()));
		/*
		 * 回调--消息是否成功发送到exchange
		 * exchange不存在时，触发ConfirmCallback（）
		 * 		exchange存在时，routingKey不匹配时，先触发ReturnCallback（），再触发ConfirmCallback（）
		 * 		当exchange存在，routingKey也匹配时，只会触发ConfirmCallback（）
		 */
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			log.info("ACK --- MQ message id: {}", correlationData.getId());
			if (ack) {
				log.info("ACK --- Message send to exchange success！");
			} else {
				log.error("ACK --- Message send to exchange failed, reason for failure:" + cause);
			}
		});
		return rabbitTemplate;
	}
}
