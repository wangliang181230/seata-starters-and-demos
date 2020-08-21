package com.easy.java.demo1.app2;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringCloudApplication
@EnableFeignClients
@EnableSwagger2
public class Demo1Application2 {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application2.class, args);
	}

}
