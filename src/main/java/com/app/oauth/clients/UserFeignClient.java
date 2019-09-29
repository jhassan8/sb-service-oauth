package com.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.users.commons.models.entity.User;

@FeignClient(name = "service-users")
public interface UserFeignClient {

	@GetMapping("users/search/byUsername")
	public User findByUsername(@RequestParam String username);
	
	@PutMapping("users/{id}")
	public User update(@RequestBody User user, @PathVariable Long id);
	
}
