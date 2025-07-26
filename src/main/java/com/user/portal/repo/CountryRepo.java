package com.user.portal.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.user.portal.entity.CountryEntity;

public interface CountryRepo extends JpaRepository<CountryEntity,Integer> {

}
