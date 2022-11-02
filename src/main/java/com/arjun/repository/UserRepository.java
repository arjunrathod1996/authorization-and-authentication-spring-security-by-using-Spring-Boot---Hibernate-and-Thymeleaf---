package com.arjun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arjun.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	public boolean existsByEmail(String email);
	
	public User findByEmail(String email);

	public User findByEmailAndMobileNumber(String email, String mobileNum);
	
}
