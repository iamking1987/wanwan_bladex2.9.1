package com.fun.gateway;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zz
 * @date 2021/9/16
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SaGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaGatewayApplication.class, args);
		System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
	}
}
