package com.app.oauth.services;

import com.app.users.commons.models.entity.User;

public interface IUserService {
	
	public User findByUsername(String username);
	
	public User update(User user, Long id);

}
