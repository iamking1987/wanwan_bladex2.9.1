package com.fun.pro.controller;

import com.fun.pro.send.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanwan 2022/2/18
 */
@RestController
@RequestMapping("/tmp")
@RequiredArgsConstructor
public class TmpController {

	private final RabbitTemplate rabbitTemplate;
	private final Sender sender;

	@PostMapping("/send-topic")
	public void test1() {
		sender.baseSend("canal_001_exchange","canal_001_routingKey","123", null, 3600*100L);
	}
}
