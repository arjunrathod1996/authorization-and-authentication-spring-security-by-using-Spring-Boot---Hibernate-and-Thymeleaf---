package com.arjun.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arjun.entity.User;
import com.arjun.repository.UserRepository;
import com.arjun.service.UserService;

@Controller
public class HomeContoller {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		
		if(p != null) {
			String email =  p.getName();
			User user =  userRepository.findByEmail(email);
			
			m.addAttribute("user", user);
		}
		
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute User user, HttpSession session) {
		
		boolean f = userService.checkEmail(user.getEmail());
		
		if(f) {
			
			System.out.println("Already Exist");
			
			session.setAttribute("msg", "Email already exists");
			
		}else {
			User user1 = userService.createUser(user);
			
			if(user1 != null) {
				System.out.println("Register Successfully...");
				session.setAttribute("msg", "Register Successfully...");
			}else {
				System.out.println("Something went wrong...");
				session.setAttribute("msg", "Something went wrong on server");
			}
		}
			
		return "redirect:/register";
	}
	
	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword() {
		
		
		
		return "forgot_password";
		
	}
	
	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable int id, Model m) { 
		
		System.out.println(id);
		
		m.addAttribute("id",id);
		
		return "reset_passsword";
		
	}
	
	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email, @RequestParam String mobileNum, HttpSession session) {
		
		
		User user =  userRepository.findByEmailAndMobileNumber(email, mobileNum);
		
		if(user != null) {
			
//			return "reset_passsword";
			
			return "redirect:/loadResetPassword/" + user.getId();
			
		}else {
			session.setAttribute("msg", "Inavlid email & phone number");
			return "forgot_password";
			
		}
		
	}
	
	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam Integer id, HttpSession session) {
		
		 User user =  userRepository.findById(id).get();
		 
		 String encryptPsw = passwordEncoder.encode(psw);
		 
		 user.setPassword(encryptPsw);
		 
		 User updateUser=  userRepository.save(user);
		 
		 if(updateUser != null) {
			 
			 session.setAttribute("msg", "Password Updated Successfully");
			 
			 
			 
		 }
		
		return "redirect:/loadForgotPassword";
	}
	
}
