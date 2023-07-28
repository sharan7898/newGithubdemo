package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;


@Repository
public interface UserAuditLogRepository extends PagingAndSortingRepository<UserAuditLog, Integer> {
 //public interface UserAuditLogRepository extends CrudRepository<UserAuditLog, Integer> {

	public UserAuditLog findByLoggedUserAndSessionId(@Param("user") User user, @Param("sessionId") String sessionId);

	public Page<UserAuditLog> findAllByLoggedUserFirstNameContaining(@Param("user") String user, Pageable pageable);

	public Page<UserAuditLog> findAllByOrderByLoginTimeDesc(Pageable pageable);

	public Page<UserAuditLog> findAll(Pageable pageable);

	public List<UserAuditLog> findAllByLoggedUserFirstNameContaining( String user);

	public List<UserAuditLog> findByLoggedUser(@Param("user") User user);

	public Long countById(@Param("id") Integer id);
	
	public UserAuditLog findBySessionId(@Param("sessionId") String sessionId);

	public List<UserAuditLog> findAllByOrderByLoginTimeDesc();
	

	// Admin filters starts here
	
	@Query(value="select u from UserAuditLog u where concat(u.loggedUser.firstName,' ' ,u.loggedUser.lastName) LIKE %?1% AND ipAddress LIKE %?2% ")

	public Page<UserAuditLog> findAllByLoggedUserContainingAndIpAddressContaining(String loggedUser, String ipAddress,
			Pageable pageable);

	public Page<UserAuditLog>findAllByIpAddressContaining(String ipAddress, Pageable pageable);
	
	@Query(value="select u from UserAuditLog u where concat(u.loggedUser.firstName,' ' ,u.loggedUser.lastName) LIKE %?1%  ")
	public Page<UserAuditLog> findAllByLoggedUserContaining(String loggedUser, Pageable pageable);

	// Admin filters ends  here

	
}
