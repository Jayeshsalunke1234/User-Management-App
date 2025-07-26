package com.user.portal.controller;

import java.awt.MenuComponent;
import java.security.Identity;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.user.portal.dto.QuoteApiResponseDto;
import com.user.portal.dto.ResetPwdDto;
import com.user.portal.dto.UserDto;
import com.user.portal.helper.AppConstant;
import com.user.portal.repo.UserRepo;
import com.user.portal.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String index(Model model) {
		UserDto userDto=new UserDto();
		model.addAttribute("user",userDto);
		return AppConstant.INDEX_PAGE;
	}
	
	@PostMapping("/login")
	public String handleLogin(@ModelAttribute("user") UserDto user,Model model) {
		
		UserDto userDto = userService.login(user.getEmail(), user.getPwd());
		
		if(userDto==null) {
			model.addAttribute("emsg","Invalid Credential");
			return AppConstant.INDEX_PAGE;
		}
        
		String pwdUpdated = userDto.getPwdUpdated();
		
		if(pwdUpdated.equals("NO")) {
			  ResetPwdDto resetPwdDto=new ResetPwdDto();
			  resetPwdDto.setEmail(userDto.getEmail());
			  model.addAttribute("resetPwd",resetPwdDto);
			return "resetPwd";
			
		}else {
			
			QuoteApiResponseDto dashboardDto = userService.buildDashboard();
			model.addAttribute("quote",dashboardDto);	
			
			return "dashboard";
		}
	}
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		UserDto userDto=new UserDto();
		model.addAttribute("user", userDto);
		
		model.addAttribute("country",userService.getCountry());
		return "register";
	}
	
	@GetMapping("/states")
	@ResponseBody
	public Map<Integer,String> getStates(@RequestParam("countryId") Integer countryId){
		return userService.getState(countryId);
	}
	
	@GetMapping("/cities")
	@ResponseBody
	public Map<Integer, String> getCities(@RequestParam("stateId") Integer stateId){
		return userService.getCities(stateId);
	}
	
	@PostMapping("/handleRegister")
	public String handleRegister(@ModelAttribute("user") UserDto user, Model model) {
		boolean emailUnique = userService.isEmailUnique(user.getEmail());
		
		if(emailUnique == false) {
			model.addAttribute("emsg", "Email is Duplicate");
		}else {
		
		boolean registeUser = userService.registeUser(user);
		if (registeUser) {
			model.addAttribute("smsg","Registration success,Check your email for login details");
		}else {
			model.addAttribute("emsg", "Registration Failed");
		}
		}
		
		return "register";
	}
	
	@PostMapping("/resetPwd")
	public String resetPwd(@ModelAttribute("resetPwd") ResetPwdDto resetPwdDto,Model model) {
		
		   UserDto user = userService.getUserByEmail(resetPwdDto.getEmail());
		   
             if(user !=null && !user.getPwd().equals(resetPwdDto.getOldPwd()))	{
            	 model.addAttribute("emsg","Old Pwd is incorrect");
             }
             if(!resetPwdDto.getNewPwd().equals(resetPwdDto.getConfirmPwd())) {
            	 model.addAttribute("emsg","New Pwd & Confirm Pwd is not same");

             }
             
             boolean resetPwd = userService.resetPwd(resetPwdDto);
             if(resetPwd) {
            	 model.addAttribute("smsg","Pwd updated");

             }
		
		return "resetPwd";
	}
	
	
	@GetMapping("/quote")
	public String getQuote(Model model) {
		QuoteApiResponseDto dashboard = userService.buildDashboard();
   	    model.addAttribute("quote",dashboard);
   	    return"dashboard";

	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		UserDto userDto=new UserDto();
   	 model.addAttribute("user",userDto);
     return  AppConstant.INDEX_PAGE;
	}
	
	

}
