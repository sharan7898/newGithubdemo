package com.swayaan.nysf.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Club;

@Repository
public interface ClubRepository extends CrudRepository<Club, Integer> { 
	
	

}
