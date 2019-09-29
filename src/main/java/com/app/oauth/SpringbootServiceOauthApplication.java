package com.app.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SpringbootServiceOauthApplication/* implements CommandLineRunner*/ {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceOauthApplication.class, args);
	}

	
	/**
	 * this comment block and the implements is used for 
	 * create a random encrypted password for testing
	 */
	
	/*
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 4; i++) {
			System.out.println(bCryptPasswordEncoder.encode("12345"));
		}
	}
	
	*/

}
