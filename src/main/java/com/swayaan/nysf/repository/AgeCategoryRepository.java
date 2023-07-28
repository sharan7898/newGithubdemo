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
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.EventManager;

@Repository
public interface AgeCategoryRepository extends PagingAndSortingRepository<AgeCategory, Integer> {
//public interface AgeCategoryRepository extends CrudRepository<AgeCategory, Integer> {
	
	public List<AgeCategory> findAllByOrderByTitleAsc();
	
	public Long countById(Integer id);
	
	@Modifying
	@Query("UPDATE AgeCategory a SET a.enabled = ?2 WHERE a.id = ?1")
	@Transactional
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public AgeCategory findByTitle(String title);

	public List<AgeCategory> findAllByOrderByIdAsc();
	
	@Query(value = "SELECT * FROM age_category WHERE created_by = :eventManagerId ", nativeQuery = true)
	public List<AgeCategory> findAllByEventManager(Integer eventManagerId);
	
	
	
	 // Admin filters starts here
	public Page<AgeCategory> findAllByTitleContainingAndCodeContaining(String title, String code,
			Pageable pageable);

	public Page<AgeCategory> findAllByCodeContaining(String code, Pageable pageable);

	public Page<AgeCategory> findAllByTitleContaining(String title, Pageable pageable);

	public Page<AgeCategory> findAll(Pageable pageable);
	
	 // Admin filters ends here


}
