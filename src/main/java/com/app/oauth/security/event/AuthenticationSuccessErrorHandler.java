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

import brave.Tracer;
import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	private IUserService iUserService;
	private Tracer tracer;
	
	@Autowired
	public AuthenticationSuccessErrorHandler(IUserService iUserService, Tracer tracer) {
		this.iUserService = iUserService;
		this.tracer = tracer;
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
			StringBuilder errors = new StringBuilder();
			
			User user = iUserService.findByUsername(authentication.getName());
			user.setTries(user.getTries() + 1);
			
			errors.append("actualn tries: " + user.getTries());
			
			if(user.getTries() > 2) {
				log.error("user %s disabled for maximum failed attempts.", authentication.getName());
				errors.append(" - user " + authentication.getName() + " disabled for maximum failed attempts.");
				user.setEnabled(false);
			}
			
			iUserService.update(user, user.getId());
			
			tracer.currentSpan().tag("error.message", errors.toString());
			
		} catch (FeignException e) {
			log.error(String.format("user %s dont exist.", authentication.getName()));
		}
		
		log.info("Fail Login: " + exception.getMessage());
	}
	
}
