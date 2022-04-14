package com.fun.pro.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanwan 2022/2/18
 */
@Configuration
public class ExchangeQueueConfigTmp {

	@Bean
	public Queue topicQueue() {
		Map<String, Object> params = new HashMap<>(3);
		// x-dead-letter-exchange 声明了当前队列绑定的死信交换机
		params.put("x-dead-letter-exchange", "dl-canal-exchange");
		// x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
		params.put("x-dead-letter-routing-key", "dl-canal-routing-key");
		return new Queue("canal_001_queue", true, false, false, params);
	}

	@Bean
	public TopicExchange topicExchange() {
		//创建交换机时，可选持久化和是否自动删除，默认为false即可
		return new TopicExchange("canal_001_exchange");
	}

	@Bean
	Binding topicBinding(@Qualifier("topicQueue") Queue queue,
						 @Qualifier("topicExchange") TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("canal_001_routingKey");
	}

	/**
	 * 死信队列
	 */
	@Bean
	public Queue dlCanalQueue() {
		return new Queue("dl-canal-queue", true);
	}

	/**
	 * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。
	 * 符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”。
	 **/
	@Bean
	public TopicExchange dlCanalExchange() {
		//创建交换机时，可选持久化和是否自动删除，默认为false即可
		return new TopicExchange("dl-canal-exchange");
	}

	@Bean
	Binding dlBinding(@Qualifier("dlCanalQueue") Queue dlCanalQueue,
							  @Qualifier("dlCanalExchange") TopicExchange dlCanalExchange) {
		return BindingBuilder.bind(dlCanalQueue).to(dlCanalExchange).with("dl-canal-routing-key");
	}
}
