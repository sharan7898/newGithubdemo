package com.swayaan.nysf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.Participant;

@Repository
public interface EventManagerRepository extends PagingAndSortingRepository<EventManager, Integer>{
	
	public EventManager getEventManagerByEmail(@Param("email") String email);


	public Long countById(Integer id);
	
	@Query("UPDATE EventManager u SET u.enabled = :enabled, u.verificationCode=null WHERE u.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
//filter starts from here
	
	@Query(value= "select j from EventManager j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% and j.ernNumber LIKE %?2%  ")	
	public Page<EventManager> findAllByNameContainingAndErnNumberContaining(String name, String ernNumber,
			Pageable pageable);

	public Page<EventManager> findAllByErnNumberContaining(String ernNumber, Pageable pageable);

	@Query(value= "select j from EventManager j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% ")
	public Page<EventManager> findAllByNameContaining(String name, Pageable pageable);

	public Page<EventManager> findAll(Pageable pageable);
		
}
