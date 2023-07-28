package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Club;
import com.swayaan.nysf.repository.ClubRepository;

@Service
public class ClubService {
	
	@Autowired
	ClubRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubService.class);

	
	public Club save(Club newClub) {
		LOGGER.info("Entered save method -ClubService");
		LOGGER.info("Exit save method -ClubService");
	return repo.save(newClub);
	}

	public List<Club> listAllClubs() {
		LOGGER.info("Entered listAllClubs method -ClubService");
		LOGGER.info("Exit listAllClubs method -ClubService");
	return (List<Club>) repo.findAll();
	}

	public Club findById(Integer clubId) {
		LOGGER.info("Entered findById method -ClubService");
		LOGGER.info("Exit findById method -ClubService");
    return repo.findById(clubId).get();
	}

}
