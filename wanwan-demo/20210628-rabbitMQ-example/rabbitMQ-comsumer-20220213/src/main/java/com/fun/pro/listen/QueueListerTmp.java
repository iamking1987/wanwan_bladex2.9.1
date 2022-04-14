package com.fun.pro.listen;

import com.alibaba.fastjson.JSONObject;
import com.fun.pro.constant.RabbitMqKey;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author wanwan 2022/2/18
 */
@Component
@Slf4j
public class QueueListerTmp {

	/**
	 * 队列不存在则自动创建
	 */
	/*@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "canal_001_queue", durable = "true", autoDelete = "false"),
		exchange = @Exchange(value = "canal_001_exchange", type = ExchangeTypes.TOPIC),
		key = "canal_001_routingKey"
	))*/
	@RabbitListener(queues = "canal_001_queue")
	public void process1(Message message, Channel channel) throws IOException {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		String messageId = message.getMessageProperties().getMessageId();
		log.info("RECEIVE --- messageId:{},queue:{}", messageId, RabbitMqKey.TRADE_DIRECT_QUEUE);
		JSONObject jsonObject = null;
		try {
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			jsonObject = JSONObject.parseObject(msg);
			// 成功确认，二参为是否批量处理消息
			channel.basicAck(deliveryTag,false);
			log.info("ACK SUCCESS --- messageId:{}, message{}", messageId, jsonObject);
		} catch (Exception e) {
			//重试的时候记住设置为false，才会被丢弃发到死信队列
			channel.basicReject(deliveryTag,false);
			log.error("ACK FAIL --- messageId:{}, message:{}", messageId, jsonObject);
			//注意这里如果throw异常会进入重试
			e.printStackTrace();
		}
	}
}
