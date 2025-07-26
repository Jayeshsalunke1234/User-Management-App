package com.user.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.portal.entity.CityEntity;

public interface CityRepo extends JpaRepository<CityEntity, Integer> {
	
	public List<CityEntity> findByStateStateId(Integer stateId);

}
