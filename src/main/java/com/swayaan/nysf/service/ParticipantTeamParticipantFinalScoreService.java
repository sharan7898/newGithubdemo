package com.swayaan.nysf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamParticipantFinalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.repository.ParticipantTeamParticipantFinalScoreRepository;

@Service
public class ParticipantTeamParticipantFinalScoreService {

	@Autowired
	ParticipantTeamParticipantFinalScoreRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamParticipantFinalScoreService.class);

	public ParticipantTeamParticipantFinalScore save(
			ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore) {
		LOGGER.info("Entered save method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit save method -ParticipantTeamParticipantFinalScoreService");
		return repo.save(participantTeamParticipantFinalScore);
	}

	public ParticipantTeamParticipantFinalScore findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
			ChampionshipParticipantTeams championshipParticipantTeam,
			ParticipantTeamParticipants participantTeamParticipant, Integer round) {
		LOGGER.info(
				"Entered findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info(
				"Exit findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound method -ParticipantTeamParticipantFinalScoreService");
		return repo.findFirstByChampionshipParticipantTeamsAndParticipantTeamParticipantAndRound(
				championshipParticipantTeam, participantTeamParticipant, round);
	}
}
