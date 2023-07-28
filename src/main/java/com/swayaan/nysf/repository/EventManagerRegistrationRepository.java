package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantRegistration;

@Repository
public interface EventManagerRegistrationRepository extends CrudRepository<EventManagerRegistration, Integer>{

	public List<EventManagerRegistration> findAll();
	
	
	@Query(value="SELECT * FROM eventmanager_registration WHERE email = :email",nativeQuery=true)
	public EventManagerRegistration geteventmanagerRegistrationByEmail(@Param("email") String email);

	public List<EventManagerRegistration> findByApprovalStatusFalse();

	@Query("UPDATE EventManagerRegistration r SET r.enabled = :enabled WHERE r.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Modifying
	@Query("UPDATE EventManagerRegistration r SET r.approvalStatus = :approvalStatus WHERE r.id = :id")
	public void updateApprovalStatus(Integer id, boolean approvalStatus);

	public Long countById(Integer id);
	
	
	//Admin filters start here
	
	
	@Query(value= "select e from EventManagerRegistration e where CONCAT(e.email, ' ', e.firstName, ' ',e.lastName) LIKE %?1% and e.district LIKE %?2% And e.approvalStatus is false ")
	public Page<EventManagerRegistration> findAllByNameContainingAndDistrictContainingAndApprovalStatusFalse(String name, String district,
			Pageable pageable);
     @Query(value= "select e from EventManagerRegistration e where(e.district) LIKE %?1% AND e.approvalStatus is false ")
	public Page<EventManagerRegistration> findAllByDistrictContainingAndApprovalStatusFalse(String district, Pageable pageable);

	@Query(value= "select e from EventManagerRegistration e where CONCAT(e.email, ' ', e.firstName, ' ',e.lastName) LIKE %?1% AND e.approvalStatus is false ")
	public Page<EventManagerRegistration> findAllByNameContainingAndApprovalStatusFalse(String name, Pageable pageable);

	public Page<EventManagerRegistration> findByApprovalStatusFalse(Pageable pageable);


}
