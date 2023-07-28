package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.repository.RefereesPanelsRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class RefereesPanelsService {
	
	@Autowired RefereesPanelsRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefereesPanelsService.class);

	
	public List<RefereesPanels> getByChampionshipRefereePanels(ChampionshipRefereePanels eventRefereePanel) {
		LOGGER.info("Entered getByChampionshipRefereePanels method -RefereesPanelsService");
		LOGGER.info("Exit getByChampionshipRefereePanels method -RefereesPanelsService");
	return repo.findByChampionshipRefereePanels(eventRefereePanel);
	}

	public RefereesPanels getByJudgeUser(User currentUser) throws JudgeNotFoundException {
		LOGGER.info("Entered getByJudgeUser method -RefereesPanelsService");
     Judge currentJudge=CommonUtil.getCurrentJudge();
		LOGGER.info("Exit getByJudgeUser method -RefereesPanelsService");
     return repo.findByJudgeUser(currentJudge);
	}

	public List<RefereesPanels> getByJudge(Judge judge) {
		LOGGER.info("Entered getByJudge method -RefereesPanelsService");
		LOGGER.info("Exit getByJudge method -RefereesPanelsService");
    return repo.findByJudge(judge);
	}


}
