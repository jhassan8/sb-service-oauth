package com.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.oauth.services.IUserService;
import com.app.users.commons.models.entity.User;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	private IUserService iUserService;
	
	@Autowired
	public AuthenticationSuccessErrorHandler(IUserService iUserService) {
		this.iUserService = iUserService;
	}
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		User user = iUserService.findByUsername(authentication.getName());
		
		if(user.getTries() > 0) {
			user.setTries(0);
			iUserService.update(user, user.getId());
		} 
		
		log.info("Success Login: " + userDetails.getUsername());
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		
		try {
			User user = iUserService.findByUsername(authentication.getName());
			user.setTries(user.getTries() + 1);
			
			if(user.getTries() > 2) {
				log.error("user %s disabled for maximum failed attempts.", authentication.getName());
				user.setEnabled(false);
			}
			
			iUserService.update(user, user.getId());
			
		} catch (FeignException e) {
			log.error(String.format("user %s dont exist.", authentication.getName()));
		}
		
		log.info("Fail Login: " + exception.getMessage());
	}
	
}
