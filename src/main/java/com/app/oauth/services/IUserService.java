package com.app.oauth.services;

import com.app.users.commons.models.entity.User;

public interface IUserService {
	
	public User findByUsername(String username);

}
