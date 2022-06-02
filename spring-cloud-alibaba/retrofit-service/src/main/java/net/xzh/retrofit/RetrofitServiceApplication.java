package net.xzh.retrofit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RetrofitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetrofitServiceApplication.class, args);
    }

}
