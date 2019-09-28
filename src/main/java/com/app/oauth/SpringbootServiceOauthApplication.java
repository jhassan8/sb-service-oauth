package com.app.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpringbootServiceOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceOauthApplication.class, args);
	}

}
