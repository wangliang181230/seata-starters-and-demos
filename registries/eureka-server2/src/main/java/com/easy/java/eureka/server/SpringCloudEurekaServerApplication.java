package com.easy.java.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaServer
@EnableFeignClients
public class SpringCloudEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudEurekaServerApplication.class, args);
	}

}