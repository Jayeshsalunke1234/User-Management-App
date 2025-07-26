package com.user.portal.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.portal.dto.QuoteApiResponseDto;
import com.user.portal.dto.ResetPwdDto;
import com.user.portal.dto.UserDto;
import com.user.portal.entity.CityEntity;
import com.user.portal.entity.CountryEntity;
import com.user.portal.entity.StateEntity;
import com.user.portal.entity.UserEntity;
import com.user.portal.repo.CityRepo;
import com.user.portal.repo.CountryRepo;
import com.user.portal.repo.StateRepo;
import com.user.portal.repo.UserRepo;
import com.user.portal.service.UserService;

@Service
public class UserServiceImpl  implements UserService{
	@Autowired
	private CountryRepo countryRepo;
	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private StateRepo stateRepo;
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailService emailService;
	
	private Random random=new Random();


	@Override
	public Map<Integer, String> getCountry() {
	                     List<CountryEntity> conutryList = countryRepo.findAll();
	                     Map<Integer, String>countryMap=new HashMap<>();
	                     conutryList.forEach(s-> countryMap.put(s.getCountryId(),s.getCountryName()));
	                     

		return countryMap;
	}

	@Override
	public Map<Integer, String> getState(Integer countryId) {
		       List<StateEntity> stateList = stateRepo.findByCountryCountryId(countryId);
		       Map<Integer,String>stateMap=new HashMap<>();
		       stateList.forEach(s->stateMap.put(s.getStateId(), s.getStateName()));
		       
		       
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		    List<CityEntity> statelList = cityRepo.findByStateStateId(stateId);	
		    Map<Integer,String>cityMap=new HashMap<>();
		    statelList.forEach(s->cityMap.put(s.getCityId(), s.getCityName()));
		    
		    
		return cityMap;
	}

	@Override
	public boolean isEmailUnique(String email) {
		Optional<UserEntity>  byEmail = userRepo.findByEmail(email);
		   if(byEmail.isPresent()) {
			   return false;
		   }
		return true;
	}

	@Override
	public boolean registeUser(UserDto user) {
		 UserEntity entity=new UserEntity();
		 
		 BeanUtils.copyProperties(user, entity);
		 
		  Optional<CountryEntity> countryOptional = countryRepo.findById(user.getCountryId());
		 Optional<StateEntity> stateOptional = stateRepo.findById(user.getStateId());
		 Optional<CityEntity> cityOptional = cityRepo.findById(user.getCityId());
		 if(countryOptional.isPresent()) {
			 entity.setCountry(countryOptional.get());
		 }
		 if(stateOptional.isPresent()) {
			 entity.setState(stateOptional.get());

		 }
		 if(cityOptional.isPresent()) {
			 entity.setCity(cityOptional.get());

		 }
		 
		   String pwd = generatePwd();
		 entity.setPwd(pwd);
		 entity.setPwdUpdated("NO");
		 
		 UserEntity save = userRepo.save(entity);
		  Integer userId = save.getUserId();
		  
		  if(userId !=null) {
			  
			  String subject="Registration successfully to UserMangement app";
			  String body=" This is temporary pwd "+ pwd;
			  
			  return  emailService.sendMail(user.getEmail(), subject, body);
		  }
		  return false;
	}

	@Override
	public UserDto login(String email, String pwd) {
		UserEntity entity = userRepo.findByEmailAndPwd(email, pwd);
		if(entity != null) {
			UserDto userDto=new UserDto();
			BeanUtils.copyProperties(entity, userDto);
			return userDto;
		}
		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdDto resetPwd) {
		      UserEntity byEmail = userRepo.findByEmail(resetPwd.getEmail()).orElseThrow();
		      byEmail.setPwd(resetPwd.getNewPwd());
		      byEmail.setPwdUpdated("YES");
		      
		      userRepo.save(byEmail);
		return false;
	}

	@Override
	public QuoteApiResponseDto buildDashboard() {
		String apiUrl="https://dummyjson.com/quotes/random";
		
		RestTemplate rt=new RestTemplate();
		ResponseEntity<QuoteApiResponseDto> forEntity = rt.getForEntity(apiUrl, QuoteApiResponseDto.class);
		  QuoteApiResponseDto body = forEntity.getBody();
		
		return body;
	}
	
	public String generatePwd() {
		
		int pwdlength=5;
		StringBuffer buffer=new StringBuffer();
		
		String charPool= "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		
		
		for(int i=0; i<pwdlength;i++) {
			int index = random.nextInt(charPool.length());
			char charAt = charPool.charAt(index);
			
			buffer.append(charAt);
		}
		
		return buffer.toString();
	}

	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity byEmail = userRepo.findByEmail(email).orElseThrow();
		
        UserDto userDto=new UserDto();
		BeanUtils.copyProperties(byEmail, userDto);
		
		return userDto;
	}
	
	

}
