package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ParticipantTeamPanel;
import com.swayaan.nysf.repository.ParticipantTeamPanelRepository;

@Service
public class ParticipantTeamPanelService {

	@Autowired
	ParticipantTeamPanelRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamPanelService.class);


	public ParticipantTeamPanel findByParticipantTeamAndRound(Integer participantTeam_id, Integer round) {
		LOGGER.info("Entered findByParticipantTeamAndRound method -ParticipantTeamPanelService");
		LOGGER.info("Exit findByParticipantTeamAndRound method -ParticipantTeamPanelService");
    return repo.findByParticipantTeamIdAndRoundNumber(participantTeam_id, round);
	}

	public void save(ParticipantTeamPanel participantTeamPanel) {
		LOGGER.info("Entered save method -ParticipantTeamPanelService");
		LOGGER.info("Exit save method -ParticipantTeamPanelService");
    repo.save(participantTeamPanel);
}

	public List<ParticipantTeamPanel> findBychampionshipRefereePanelsId(Integer id) {
		LOGGER.info("Entered findBychampionshipRefereePanelsId method -ParticipantTeamPanelService");
		LOGGER.info("Exit findBychampionshipRefereePanelsId method -ParticipantTeamPanelService");
	return repo.findAllBychampionshipRefereePanelsId(id);
	}

}
