package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.RefereesPanels;

@Repository
public interface RefereePanelsRepository extends CrudRepository<RefereesPanels, Integer> {
	
    public List<RefereesPanels> findAll();
	
	public Long countById(Integer id);
	
	public List<RefereesPanels> findByChampionshipRefereePanelsAndType(ChampionshipRefereePanels eventRefereePanels,
			String type);

	public List<RefereesPanels> findByJudgeUser(Judge judgeUser);

	 @Modifying
	 @Query("DELETE RefereesPanels r WHERE r.id = ?1")
	public void deleteById(Integer id);

	public List<RefereesPanels> findByChampionshipRefereePanels(ChampionshipRefereePanels eventRefereePanels);

	public List<RefereesPanels> findByChampionshipRefereePanelsAndType(
			ChampionshipRefereePanels championshipRefereePanels, JudgeType judgeTypeObject);

	public boolean existsByChampionshipRefereePanelsAndJudgeUser(ChampionshipRefereePanels championshipRefereePanelId, Judge judgeUser);
	 
}
