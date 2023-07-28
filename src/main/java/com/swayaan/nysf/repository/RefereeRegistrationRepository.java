//package com.swayaan.nysf.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.swayaan.nysf.entity.RefereeRegistration;
//
//@Repository
//public interface RefereeRegistrationRepository extends CrudRepository<RefereeRegistration, Integer> {
//	
//	@Query("SELECT r FROM RefereeRegistration r WHERE r.email = :email")
//	public RefereeRegistration getRefereeRegistrationByEmail(@Param("email") String email);
//
//	public Long countById(Integer id);
//	
//	@Query("UPDATE RefereeRegistration r SET r.enabled = :enabled WHERE r.id = :id")
//	@Modifying
//	public void updateEnabledStatus(Integer id, boolean enabled);
//
//	@Query("UPDATE RefereeRegistration r SET r.approvalStatus = :approvalStatus WHERE r.id = :id")
//	@Modifying
//	public void updateApprovalStatus(Integer id, boolean approvalStatus);
//
//	public List<RefereeRegistration> findByEnabledTrue();
//}
