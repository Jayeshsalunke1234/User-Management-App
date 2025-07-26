package com.user.portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.portal.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity,Integer> {
	
	public Optional<UserEntity> findByEmail(String email);
	
	public UserEntity findByEmailAndPwd(String email,String pwd);

}
