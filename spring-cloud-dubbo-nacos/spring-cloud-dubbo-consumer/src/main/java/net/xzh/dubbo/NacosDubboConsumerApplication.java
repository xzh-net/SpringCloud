package net.xzh.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 整合了nacos注册中心
 * 
 * @author Administrator
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosDubboConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NacosDubboConsumerApplication.class);
	}
}
