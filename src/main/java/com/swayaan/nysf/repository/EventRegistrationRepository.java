package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.User;

@Repository
public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Integer>{

	Boolean existsByName(String name);

	List<EventRegistration> findAllByCreatedBy(User currentUser);

	EventRegistration findAllByIdAndCreatedBy(Integer id, User currentUser);

	List<EventRegistration> findAllByApprovalStatusIn(List<RegistrationStatusEnum> approvalStatus);

	Long countById(Integer id);

	@Modifying
	@Query("UPDATE EventRegistration r SET r.approvalStatus = :approvalStatus WHERE r.id = :id")
	void updateApprovalStatus(Integer id, RegistrationStatusEnum approvalStatus);

	@Query(value = "SELECT * FROM event_registration WHERE created_by = :eventManagerId ", nativeQuery = true)
	List<EventRegistration> findByCreatedBy(Integer eventManagerId);
	
	Boolean existsByNameAndIdNot(String name, Integer id);
	
	
   //Admin filters start here
//	@Query(value= "select j from Judge j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% and j.jrnNumber LIKE %?2%  ")
	public Page<EventRegistration> findAllByNameContainingAndLocationContaining(String name, String location,
			Pageable pageable);

	public Page<EventRegistration> findAllByLocationContaining(String location, Pageable pageable);

//	@Query(value= "select j from Judge j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% ")
	public Page<EventRegistration> findAllByNameContaining(String name, Pageable pageable);

	public Page<EventRegistration> findAll(Pageable pageable);
	//Admin filters ends here 

}
