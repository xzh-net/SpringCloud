package net.xzh.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 配置中心客户端
 * @author CR7
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClientApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}

}
