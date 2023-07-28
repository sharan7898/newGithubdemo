package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Judge;

@Repository
public interface JudgeRepository extends PagingAndSortingRepository<Judge, Integer> {

	
	public Long countById(Integer id);
	
	@Query("UPDATE Judge u SET u.enabled = :enabled, u.verificationCode=null WHERE u.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query(value= "select * from judges JOIN users on judges.id=users.id where enabled= :enabled and judges.id not in (select judge_user_referee_id FROM referees_panels where championship_referee_panels_id=:championshipRefereePanelId) ORDER BY judges.id ASC",nativeQuery=true)
	public List<Judge> findAllBychampionshipRefereePanelIdAndEnabledOrderByIdAsc(@Param("championshipRefereePanelId") Integer championshipRefereePanelId,boolean enabled);

	@Query(value= "select j from Judge j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% and j.jrnNumber LIKE %?2%  ")
	public Page<Judge> findAllByNameContainingAndJrnNumberContaining(String name, String jrnnumber,
			Pageable pageable);

	public Page<Judge> findAllByJrnNumberContaining(String jrnNumber, Pageable pageable);

	@Query(value= "select j from Judge j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% ")
	public Page<Judge> findAllByNameContaining(String name, Pageable pageable);

	public Page<Judge> findAll(Pageable pageable);


}
