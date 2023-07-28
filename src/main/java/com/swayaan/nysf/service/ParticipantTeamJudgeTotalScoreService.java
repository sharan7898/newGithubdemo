package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeamJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ParticipantTeamJudgeTotalScoreRepository;

@Service
public class ParticipantTeamJudgeTotalScoreService {

	@Autowired
	ParticipantTeamJudgeTotalScoreRepository repo;
	@Autowired
	JudgeTypeService judgeTypeService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamJudgeTotalScoreService.class);


	public ParticipantTeamJudgeTotalScore save(ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore) {
		LOGGER.info("Entered save method -ParticipantTeamJudgeTotalScoreService");
		LOGGER.info("Exit save method -ParticipantTeamJudgeTotalScoreService");
     return repo.save(participantTeamJudgeTotalScore);
	}

	public ParticipantTeamJudgeTotalScore get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamJudgeTotalScoreService");
        try {
    		LOGGER.info("Exit get method -ParticipantTeamJudgeTotalScoreService");
      return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public ParticipantTeamJudgeTotalScore getByTeamAndParticipantAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, Integer round) {
		LOGGER.info("Entered getByTeamAndParticipantAndRound method -ParticipantTeamJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndParticipantAndRound method -ParticipantTeamJudgeTotalScoreService");
    return repo.findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndRound(
				championshipParticipantTeams, participantTeamParticipant, round);
	}

	// traditional single and artistic single
	public ParticipantTeamJudgeTotalScore getByTeamAndRound(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round) {
		LOGGER.info("Entered getByTeamAndRound method -ParticipantTeamJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndRound method -ParticipantTeamJudgeTotalScoreService");
     return repo.findByChampionshipParticipantTeamsAndRoundAggregate(championshipParticipantTeams.getId(), round);
	}

	public List<ParticipantTeamJudgeTotalScore> getByTeamAndRoundAsList(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round) {
		LOGGER.info("Entered getByTeamAndRoundAsList method -ParticipantTeamJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndRoundAsList method -ParticipantTeamJudgeTotalScoreService");
	return repo.findByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
	}

	public ParticipantTeamJudgeTotalScore getByTeamAndParticipantAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getByTeamAndParticipantAndRoundAndType method -ParticipantTeamJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndParticipantAndRoundAndType method -ParticipantTeamJudgeTotalScoreService");
	return repo.findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndRoundAndType(
				championshipParticipantTeams, participantTeamParticipant, round, judgeType);
	}

	// teams get data
	public ParticipantTeamJudgeTotalScore getByTeamAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getByTeamAndRoundAndType method -ParticipantTeamJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndRoundAndType method -ParticipantTeamJudgeTotalScoreService");
    return repo.findByChampionshipParticipantTeamsAndRoundAndType(championshipParticipantTeams, round, judgeType);
	}

	public ParticipantTeamJudgeTotalScore findByChampionshipParticipantTeamsAndJudgeType(
			ChampionshipParticipantTeams team, Integer judgetypeArtisticJudge) {
		LOGGER.info("Entered findByChampionshipParticipantTeamsAndJudgeType method -ParticipantTeamJudgeTotalScoreService");
		JudgeType judgeType=null;
		try {
			judgeType = judgeTypeService.get(judgetypeArtisticJudge);
		} catch (EntityNotFoundException e) {

			e.printStackTrace();
		}
		LOGGER.info("Exit findByChampionshipParticipantTeamsAndJudgeType method -ParticipantTeamJudgeTotalScoreService");
     	return repo.findByChampionshipParticipantTeamsAndType(team, judgeType);
	}

}
