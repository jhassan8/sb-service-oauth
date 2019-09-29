package com.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.oauth.services.UserService;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private UserService userService;
	private AuthenticationEventPublisher authenticationEventPublisher;
	
	@Autowired
	public SpringSecurityConfig(UserService userService, AuthenticationEventPublisher authenticationEventPublisher) {
		this.userService = userService;
		this.authenticationEventPublisher = authenticationEventPublisher;
	}

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userService).passwordEncoder(passwordEncoder())
			.and().authenticationEventPublisher(authenticationEventPublisher);
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
}
