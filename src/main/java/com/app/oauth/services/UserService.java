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

@Service
public class UserService implements IUserService, UserDetailsService {
	
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	private UserFeignClient userFeignClient;
	
	@Autowired
	public UserService(UserFeignClient userFeignClient) {
		this.userFeignClient = userFeignClient;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.app.users.commons.models.entity.User user = userFeignClient.findByUsername(username);
		
		if(user == null) {
			log.error("wrong authentication data.");
			throw new UsernameNotFoundException("wrong authentication data.");
		}
		
		List<GrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> log.info("role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("user authenticated: " + username);
		
		return new User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
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
