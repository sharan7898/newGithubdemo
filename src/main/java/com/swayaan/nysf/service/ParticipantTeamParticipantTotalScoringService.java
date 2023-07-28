package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeamParticipantTotalScoring;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.repository.ParticipantTeamParticipantTotalScoringRepository;

@Service
public class ParticipantTeamParticipantTotalScoringService {
	@Autowired
	ParticipantTeamParticipantTotalScoringRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamParticipantTotalScoringService.class);

	public ParticipantTeamParticipantTotalScoring findByRoundAndJudgeUserAndParticipantTeamParticipantsAndChampionshipParticipantTeams(
			Integer round, Judge judgeUser, ParticipantTeamParticipants participantTeamParticipant,
			ChampionshipParticipantTeams championshipParticipant) {
		LOGGER.info(
				"Entered findByRoundAndJudgeUserAndParticipantTeamParticipantsAndChampionshipParticipantTeams method -ParticipantTeamParticipantTotalScoringService");
		LOGGER.info(
				"Exit findByRoundAndJudgeUserAndParticipantTeamParticipantsAndChampionshipParticipantTeams method -ParticipantTeamParticipantTotalScoringService");
		return repo.findByRoundAndJudgeUserAndParticipantTeamParticipantAndChampionshipParticipantTeams(round,
				judgeUser, participantTeamParticipant, championshipParticipant);
	}

	public List<ParticipantTeamParticipantTotalScoring> findByRoundAndParticipantTeamParticipantsAndChampionshipParticipantTeams(
			Integer round, ParticipantTeamParticipants participantTeamParticipant,
			ChampionshipParticipantTeams championshipParticipant) {
		LOGGER.info(
				"Entered findByRoundAndParticipantTeamParticipantsAndChampionshipParticipantTeams method -ParticipantTeamParticipantTotalScoringService");
		LOGGER.info(
				"Exit findByRoundAndParticipantTeamParticipantsAndChampionshipParticipantTeams method -ParticipantTeamParticipantTotalScoringService");
		return repo.findAllByRoundAndParticipantTeamParticipantAndChampionshipParticipantTeams(round,
				participantTeamParticipant, championshipParticipant);
	}

	public ParticipantTeamParticipantTotalScoring save(
			ParticipantTeamParticipantTotalScoring participantTeamParticipantTotalScoring) {
		LOGGER.info("Entered save method -ParticipantTeamParticipantTotalScoringService");
		LOGGER.info("Exit save method -ParticipantTeamParticipantTotalScoringService");
		return repo.save(participantTeamParticipantTotalScoring);

	}

}
