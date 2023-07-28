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

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.Schedule;

@Repository
public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Integer> {

	public Long countById(Integer id);

	public List<Schedule> findAllByOrderByChampionshipDesc();

	@Modifying
	@Query("UPDATE Schedule s SET s.enabled = ?2 WHERE s.id = ?1")
	@Transactional
	public void updateEnabledStatus(Integer id, boolean enabled);

	public List<Schedule> findAllByChampionship(Championship championship);
//filter starts from here

	@Query(value = "Select p from Schedule p where p.description LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<Schedule> findAllByDescriptionContainingAndChampionshipNameContainingAndStatusNotIn(String description,
			String championshipName, List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from Schedule p where  p.championship.name LIKE %?1% AND  p.championship.status NOT IN (?2) ")
	public Page<Schedule> findAllByChampionshipNameContainingAndStatusNotIn(String championshipName,
			List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from Schedule p where p.description LIKE %?1% AND  p.championship.status NOT IN (?2) ")
	public Page<Schedule> findAllByDescriptionContainingAndStatusNotIn(String description,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	public Page<Schedule> findAllByChampionshipStatusNotIn(List<ChampionshipStatusEnum> status, Pageable pageable);

}
