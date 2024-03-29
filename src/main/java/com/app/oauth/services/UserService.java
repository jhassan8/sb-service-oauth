package com.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.oauth.clients.UserFeignClient;

import brave.Tracer;
import feign.FeignException;

@Service
public class UserService implements IUserService, UserDetailsService {
	
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	private UserFeignClient userFeignClient;
	
	private Tracer tracer;
	
	@Autowired
	public UserService(UserFeignClient userFeignClient, Tracer tracer) {
		this.userFeignClient = userFeignClient;
		this.tracer = tracer;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		try {
			com.app.users.commons.models.entity.User user = userFeignClient.findByUsername(username);
			
			List<GrantedAuthority> authorities = user.getRoles()
					.stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.peek(authority -> log.info("role: " + authority.getAuthority()))
					.collect(Collectors.toList());
			
			log.info("user authenticated: " + username);
			
			return new User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
		} catch (FeignException e) {
			log.error("wrong authentication data.");
			tracer.currentSpan().tag("error.message", "wrong authentication data : " + e.getMessage());
			throw new UsernameNotFoundException("wrong authentication data.");
		}
	}

	@Override
	public com.app.users.commons.models.entity.User findByUsername(String username) {
		return userFeignClient.findByUsername(username);
	}

	@Override
	public com.app.users.commons.models.entity.User update(com.app.users.commons.models.entity.User user, Long id) {
		return userFeignClient.update(user, id);
	}

}
