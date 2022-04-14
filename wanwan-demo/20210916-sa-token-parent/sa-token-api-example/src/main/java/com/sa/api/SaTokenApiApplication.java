package com.sa.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zz
 * @date 2021/9/21
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SaTokenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaTokenApiApplication.class, args);
	}
}
