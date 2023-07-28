package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.repository.RefereePanelsRepository;

@Service
public class RefereePanelsService {

	@Autowired
	RefereePanelsRepository repo;
	
	@Autowired
	JudgeService judgeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefereePanelsService.class);

	public List<RefereesPanels> listAllChampionshipRefereePanels() {
		LOGGER.info("Entered listAllChampionshipRefereePanels method -RefereePanelsService");
		LOGGER.info("Exit listAllChampionshipRefereePanels method -RefereePanelsService");
		return repo.findAll();
	}

	public RefereesPanels save(RefereesPanels refereesPanels) {
		LOGGER.info("Entered save method -RefereePanelsService");
		LOGGER.info("Exit save method -RefereePanelsService");
		return repo.save(refereesPanels);
	}

	public List<RefereesPanels> getByRefereePanelAndType(ChampionshipRefereePanels eventRefereePanels, String type) {
		LOGGER.info("Entered getByRefereePanelAndType method -RefereePanelsService");
		LOGGER.info("Exit getByRefereePanelAndType method -RefereePanelsService");
		return repo.findByChampionshipRefereePanelsAndType(eventRefereePanels, type);
	}

	public RefereesPanels get(Integer id) throws ChampionshipRefereePanelsNotFoundException {
		LOGGER.info("Entered get method -RefereePanelsService");
		try {
			LOGGER.info("Exit get method -RefereePanelsService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ChampionshipRefereePanelsNotFoundException("Could not find any panel with ID " + id);
		}
	}

	public void delete(Integer id) throws ChampionshipRefereePanelsNotFoundException {
		LOGGER.info("Entered delete method -RefereePanelsService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ChampionshipRefereePanelsNotFoundException("Could not find any panel with ID " + id);
		}
		LOGGER.info("Exit delete method -RefereePanelsService");
		repo.deleteById(id);
	}

	public RefereesPanels findById(Integer participantGroup_id) {
		LOGGER.info("Entered findById method -RefereePanelsService");
		LOGGER.info("Exit findById method -RefereePanelsService");
		return repo.findById(participantGroup_id).get();
	}

	public List<RefereesPanels> findByJudgeUser(Judge judgeUser) {
		LOGGER.info("Entered findByJudgeUser method -RefereePanelsService");
		List<RefereesPanels> listRefereePanel = repo.findByJudgeUser(judgeUser);
		LOGGER.info("Exit findByJudgeUser method -RefereePanelsService");
		return listRefereePanel;
	}

	public List<RefereesPanels> listAllRefereePanels(ChampionshipRefereePanels eventRefereePanels) {
		LOGGER.info("Entered listAllRefereePanels method -RefereePanelsService");
		LOGGER.info("Exit listAllRefereePanels method -RefereePanelsService");
		return repo.findByChampionshipRefereePanels(eventRefereePanels);
	}

	public List<RefereesPanels> getRefereePanelsByType(ChampionshipRefereePanels championshipRefereePanels,
			JudgeType judgeTypeObject) {
		LOGGER.info("Entered getRefereePanelsByType method -RefereePanelsService");
		LOGGER.info("Exit getRefereePanelsByType method -RefereePanelsService");
		return repo.findByChampionshipRefereePanelsAndType(championshipRefereePanels, judgeTypeObject);
	}

	public boolean existByUser(ChampionshipRefereePanels championshipRefereePanel, Integer referee) throws JudgeNotFoundException {
		Judge judgeUser = judgeService.findById(referee);
		return repo.existsByChampionshipRefereePanelsAndJudgeUser(championshipRefereePanel, judgeUser);
	}

}
