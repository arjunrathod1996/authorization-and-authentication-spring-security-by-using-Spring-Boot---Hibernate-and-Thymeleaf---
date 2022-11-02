package com.arjun.service;

import org.springframework.stereotype.Service;

import com.arjun.entity.User;

@Service
public interface UserService {
	
	public User createUser(User user);
	
	public boolean checkEmail(String email);

}
