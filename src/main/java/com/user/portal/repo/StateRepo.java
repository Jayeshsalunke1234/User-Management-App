package com.user.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.portal.entity.StateEntity;

public interface StateRepo extends JpaRepository<StateEntity, Integer> {
	
	public List<StateEntity> findByCountryCountryId(Integer countryId);

}
