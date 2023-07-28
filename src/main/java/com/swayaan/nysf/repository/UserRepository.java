package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	
	@Query("SELECT u FROM User u WHERE u.userName = :email")
	public User getUserByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getByEmail(@Param("email") String email);

	public User getUserByUserName(String username);
	
	public User findByResetpasswordToken(String token);

	public boolean existsByUserName(String userName);
	
	@Query(value="SELECT user_id FROM users_roles WHERE role_id!=2 and role_id !=3", nativeQuery=true)
	public List<Integer> findAllByNotJudgeAndParticipant();
	
	@Query(value="SELECT user_id FROM users_roles WHERE role_id=4", nativeQuery=true)
	public List<Integer> findAllEventManagers();

	public boolean existsByEmail(String email);
	
//	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
//	public User findUserByverificationCode(String code);
//
//	public Long countById(Integer id);
//	
//	@Query("UPDATE User u SET u.enabled = :enabled, u.verificationCode=null WHERE u.id = :id")
//	@Modifying
//	public void updateEnabledStatus(Integer id, boolean enabled);
//
//	public User findByResetpasswordToken(String token);
//	
//	@Query(value="SELECT * FROM users WHERE referee_type = :type ORDER BY rand() LIMIT :count", nativeQuery=true)
//	public List<User> findReferees(@Param("count") Integer count, @Param("type") String type);
//	
////	@Query(value="SELECT * FROM users WHERE referee_type = :type ORDER BY rand()", nativeQuery=true)
////	public List<User> findRefereesType(@Param("type") String type);
////
////	
////	@Query(value= "select * from users where referee_type=:type and id not in (select user_referee_id FROM referees_panels where event_referee_panels_id=:eventRefereePanelId and type=:type) ORDER BY first_name ASC",nativeQuery=true)
////	public List<User> findNonSelectedRefereesType(@Param("type") String type, @Param("eventRefereePanelId") Integer eventRefereePanelId);
//	
//	@Query(value= "select * from users where enabled= :enabled and id not in (select user_referee_id FROM referees_panels where championship_referee_panels_id=:championshipRefereePanelId) ORDER BY first_name ASC",nativeQuery=true)
//	public List<User> findAllBychampionshipRefereePanelIdAndEnabled(@Param("championshipRefereePanelId") Integer championshipRefereePanelId,boolean enabled);
//
//	@Query(value= "select * from users where enabled= :enabled and id not in (select user_referee_id FROM referees_panels where championship_referee_panels_id=:championshipRefereePanelId) ORDER BY id ASC",nativeQuery=true)
//	public List<User> findAllBychampionshipRefereePanelIdAndEnabledOrderByIdAsc(@Param("championshipRefereePanelId") Integer championshipRefereePanelId,boolean enabled);
//
}
