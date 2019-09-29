package com.app.oauth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.app.oauth.services.IUserService;
import com.app.users.commons.models.entity.User;

@Component
public class AdditionalTokenInfo implements TokenEnhancer {

	private IUserService userService;
	
	@Autowired
	public AdditionalTokenInfo(IUserService userService) {
		this.userService = userService;
	}
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> additionalInfo = new HashMap<String, Object>();
		
		User user = userService.findByUsername(authentication.getName());
		
		additionalInfo.put("first_name", user.getFirsName());
		additionalInfo.put("last_name", user.getLastName());
		additionalInfo.put("email", user.getEmail());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		
		return accessToken;
	}

}
