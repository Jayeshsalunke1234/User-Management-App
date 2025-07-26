package com.user.portal.service;

import java.util.Map;

import com.user.portal.dto.QuoteApiResponseDto;
import com.user.portal.dto.ResetPwdDto;
import com.user.portal.dto.UserDto;

public interface UserService {
	
	public Map<Integer,String> getCountry();
	public Map<Integer, String> getState(Integer countryId);
	public Map<Integer, String> getCities(Integer stateId);
	public boolean isEmailUnique(String email);
	public boolean registeUser(UserDto user);
	
	public UserDto login(String email,String pwd);
	
	public boolean resetPwd(ResetPwdDto resetPwd);
	
	public QuoteApiResponseDto buildDashboard();
	
	public UserDto getUserByEmail(String email);
	
	
	
	

}
