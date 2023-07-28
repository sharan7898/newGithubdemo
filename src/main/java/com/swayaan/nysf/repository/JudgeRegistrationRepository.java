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

import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.ParticipantRegistration;

@Repository
public interface JudgeRegistrationRepository extends PagingAndSortingRepository<JudgeRegistration, Integer> {

	public List<JudgeRegistration> findByEnabledTrue();
	
	public List<JudgeRegistration> findAll();
	
	@Query(value="SELECT * FROM judges_registration WHERE email = :email",nativeQuery=true)
	public JudgeRegistration getJudgeRegistrationByEmail(@Param("email") String email);
	
	@Query("UPDATE JudgeRegistration r SET r.enabled = :enabled WHERE r.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	@Query("UPDATE JudgeRegistration r SET r.approvalStatus = :approvalStatus WHERE r.id = :id")
	@Modifying
	public void updateApprovalStatus(Integer id, boolean approvalStatus);
	
	public Long countById(Integer id);

	public List<JudgeRegistration> findByApprovalStatusFalse();
	
	// Admin filters starts here
	
		@Query(value= "select j from JudgeRegistration j where CONCAT(j.email, ' ',j.firstName, ' ',j.lastName) LIKE %?1% And j.approvalStatus is false")
	    public Page<JudgeRegistration> findAllByNameContainingAndApprovalStatusFalse(String name, Pageable pageable);
		
		public Page<JudgeRegistration> findByApprovalStatusFalse(Pageable pageable);

		// Admin filters ends here


}
