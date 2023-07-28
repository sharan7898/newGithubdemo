package com.swayaan.nysf.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;

@Repository
public interface NavigationHistoryRepository extends PagingAndSortingRepository<NavigationHistory, Integer> {
	
	public List<NavigationHistory> findAllByOrderByCreatedTimeDesc();
	
	@Query(value="SELECT * FROM navigation_history where not created_time > now() - INTERVAL :days day;",nativeQuery = true)
	public List<NavigationHistory> findNavigationHistory(Integer days);
	
	public List<NavigationHistory> findAllByUser(User user);
	
	public List<NavigationHistory> findAllByUserFirstNameContaining( String user);
	
	//Admin filters starts here
	public Page<NavigationHistory> findAllByUserNameContainingAndRoleNameContainingAndUrlContainingAndCreatedTimeContaining(
			String userName, String roleName, String url, String createdTime, Pageable pageable);

//	@Query(value= "select j from ParticipantTeam j where j.name LIKE %?1% and j.chestNumber LIKE %?2% and j.championship LIKE %?3% ")
//	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndChampionshipContaining(
//			String name, String chestNumber, String asanaCategory, Pageable pageable);



	public Page<NavigationHistory> findAllByUserNameContainingAndRoleNameContainingAndUrlContaining(
			String userName, String roleName, String url, Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContainingAndRoleNameContainingAndCreatedTimeContaining(
			String userName, String roleName, String createdTime, Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContainingAndRoleNameContaining(String userName, String roleName,
			Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContainingAndUrlContainingAndCreatedTimeContaining(
			String userName, String roleName, String url, Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContainingAndUrlContaining(String userName,
			String url, Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContainingAndCreatedTimeContaining(String userName,
			String createdTime, Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContaining(String userName, Pageable pageable);

	public Page<NavigationHistory> findAllByRoleNameContainingAndUrlContainingAndCreatedTimeContaining(
			String roleName, String url, String createdTime, Pageable pageable);

	public Page<NavigationHistory> findAllByRoleNameContainingAndUrlContaining(String roleName,
			String url, Pageable pageable);

	public Page<NavigationHistory> findAllByRoleNameContainingAndCreatedTimeContaining(String roleName,
			String createdTime, Pageable pageable);

	public Page<NavigationHistory> findAllByRoleNameContaining(String roleName, Pageable pageable);

	public Page<NavigationHistory> findAllByUrlContainingAndCreatedTimeContaining(
			String url, String createdTime, Pageable pageable);

	public Page<NavigationHistory> findAllByUrlContaining(String url, Pageable pageable);

	public Page<NavigationHistory> findAllByCreatedTimeContaining(String createdTime, Pageable pageable);
	
	public Page<NavigationHistory> findAll(Pageable pageable);


	//Admin filters ends here
}

	
	   
/*	//Admin filters starts here
	public Page<NavigationHistory> findAllByUserNameContainingAndRoleNameContaining(String userName, String roleName,
			Pageable pageable);

	public Page<NavigationHistory>findAllByRoleNameContaining(String roleName, Pageable pageable);

	public Page<NavigationHistory> findAllByUserNameContaining(String userName, Pageable pageable);
	
	public Page<NavigationHistory> findAll(Pageable pageable);

	// Admin filters ends  here */

	

	
