package com.swayaan.nysf.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.Role;

@Repository
public interface ChampionshipLevelsRepository extends PagingAndSortingRepository<ChampionshipLevels, Integer> {
//public interface ChampionshipLevelsRepository extends CrudRepository<ChampionshipLevels, Integer> {
	
	public List<ChampionshipLevels> findAllByOrderByTitleAsc();
	
	public Long countById(Integer id);
	
	@Modifying
	@Query("UPDATE ChampionshipLevels c SET c.enabled = ?2 WHERE c.id = ?1")
	@Transactional
	public void updateEnabledStatus(Integer id, boolean enabled);
		
	public ChampionshipLevels findByTitle(String title);
	
	
	// Admin filters starts here 
	
	public Page<ChampionshipLevels> findAllByTitleContaining(String title, Pageable pageable);
	
	// Admin filters ends here

}
