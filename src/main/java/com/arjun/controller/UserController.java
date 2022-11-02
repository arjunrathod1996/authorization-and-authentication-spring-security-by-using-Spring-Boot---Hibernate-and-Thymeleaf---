package com.arjun.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.arjun.entity.User;
import com.arjun.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email =  p.getName();
		User user =  userRepository.findByEmail(email);
		
		m.addAttribute("user", user);
	}
	
	@GetMapping("/")
	public String home() {
		return "user/home";
	}
	
	@GetMapping("/changPass")
	public String loadChangePassword() {
		return "user/change_password";
	}
	
	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass, 
					@RequestParam("newPass") String newPass, HttpSession session) {
			
		String email =  p.getName();
		
		User loginUser = userRepository.findByEmail(email);
		
		boolean f =  passwordEncoder.matches(oldPass, loginUser.getPassword());
		
		if(f) {
			
			
			System.out.println("Old PassWord is Correct");
			
			loginUser.setPassword(passwordEncoder.encode(newPass));
			
			User updatePasswordUser = userRepository.save(loginUser);
			
			if(updatePasswordUser !=null) {
				
				session.setAttribute("msg", "Password updated Sucessfully");
				
			}else {
				
				session.setAttribute("msg", "Something went wrong on server");
				
			}
			
			
		}else {
			System.out.println("Old PassWord is Not Correct");
			
			session.setAttribute("msg", "Old PassWord Incorrect");
		}
		
		return "redirect:/user/changPass";
		
	}
	
}
