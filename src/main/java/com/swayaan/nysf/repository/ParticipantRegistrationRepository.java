package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Judge;
//import com.swayaan.nysf.entity.Event;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.Role;

@Repository
public interface ParticipantRegistrationRepository extends PagingAndSortingRepository<ParticipantRegistration,Integer> {

	public List<ParticipantRegistration> findAll();
	
	public List<ParticipantRegistration> findByEnabledTrue();
	
	@Query(value="SELECT * FROM participants_registration WHERE email = :email",nativeQuery=true)
	public ParticipantRegistration getParticipantRegistrationByEmail(@Param("email") String email);
	
	@Query("UPDATE ParticipantRegistration r SET r.enabled = :enabled WHERE r.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	@Query("UPDATE ParticipantRegistration r SET r.approvalStatus = :approvalStatus WHERE r.id = :id")
	@Modifying
	public void updateApprovalStatus(Integer id, boolean approvalStatus);
	
	public Long countById(Integer id);

	public List<ParticipantRegistration> findByApprovalStatusFalse();
	
//	@Query("UPDATE ParticipantRegistration r SET r.event = :event where r.id = :id")
//	@Modifying
//	public void updateEvent(@Param(value = "id") Integer id, Event event);

	//public List<ParticipantRegistration> findByEvent(Event event);
	
	// Admin filters starts here
	@Query(value= "select p from ParticipantRegistration p where CONCAT(p.email, ' ',p.firstName, ' ',p.lastName) LIKE %?1% AND p.approvalStatus is false")
    public Page<ParticipantRegistration> findAllByNameContainingAndApprovalStatusFalse(String name, Pageable pageable);
	
	public Page<ParticipantRegistration> findByApprovalStatusFalse(Pageable pageable);

	// Admin filters ends here

}
