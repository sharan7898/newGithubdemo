package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.RefereesPanels;

@Repository
public interface RefereesPanelsRepository extends CrudRepository<RefereesPanels, Integer> {
	
	public Long countById(Integer id);

	public Optional<RefereesPanels> findById(Long id);

	public List<RefereesPanels> findByChampionshipRefereePanels(ChampionshipRefereePanels eventRefereePanel);

	public RefereesPanels findByJudgeUser(Judge judgeUser);
	
	@Query(value = "SELECT * FROM referees_panels WHERE judge_user_referee_id = :judgeUser ", nativeQuery = true)
	public List<RefereesPanels> findByJudge(Judge judgeUser);
	
}
