package com.swayaan.nysf.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.repository.ChampionshipLinkRepository;

@Service
@Transactional
public class ChampionshipLinkService {
	
	@Autowired ChampionshipLinkRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipLinkService.class);

	public ChampionshipLink getByChampionship(Championship championship) {
		LOGGER.info("Entered getByChampionship method -ChampionshipLinkService");
		LOGGER.info("Exit getByChampionship method -ChampionshipLinkService");
    return repo.findByChampionship(championship);
	}
	
	public ChampionshipLink saveChampionshipLink(ChampionshipLink championshipLink) {
		LOGGER.info("Entered saveChampionshipLink method -ChampionshipLinkService");
		LOGGER.info("Exit saveChampionshipLink method -ChampionshipLinkService");
     return repo.save(championshipLink);
		
	}
	
	public ChampionshipLink getById(Integer id) {
		LOGGER.info("Entered getById method -ChampionshipLinkService");
		LOGGER.info("Exit getById method -ChampionshipLinkService");
	return repo.findById(id).get();
	}

	public List<ChampionshipLink> getChampionshipLinkByChampionship(Championship championship) {
		LOGGER.info("Entered getChampionshipLinkByChampionship method -ChampionshipLinkService");
		LOGGER.info("Exit getChampionshipLinkByChampionship method -ChampionshipLinkService");
   return repo.findByChampionshipId(championship);
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -ChampionshipLinkService");
		LOGGER.info("Exit delete method -ChampionshipLinkService");
      repo.deleteById(id);	
	}

	public void updateStatusByChampionship(Championship championship, Boolean status) {
		LOGGER.info("Entered updateStatusByChampionship method -ChampionshipLinkService");
		LOGGER.info("Exit updateStatusByChampionship method -ChampionshipLinkService");
         repo.updateStatusForChampionship(championship,status);
		
	}
	}
