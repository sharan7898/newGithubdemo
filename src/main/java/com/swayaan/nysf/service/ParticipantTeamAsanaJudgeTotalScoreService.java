package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeamAsanaJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.repository.ParticipantTeamAsanaJudgeTotalScoreRepository;

@Service
public class ParticipantTeamAsanaJudgeTotalScoreService {

	@Autowired
	ParticipantTeamAsanaJudgeTotalScoreRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamAsanaJudgeTotalScoreService.class);


	public ParticipantTeamAsanaJudgeTotalScore save(
			ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore) {
		LOGGER.info("Entered save method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit save method -ParticipantTeamAsanaJudgeTotalScoreService");
   	return repo.save(participantTeamAsanaJudgeTotalScore);
	}

	public ParticipantTeamAsanaJudgeTotalScore get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamAsanaJudgeTotalScoreService");
      try {
			LOGGER.info("Exit get method -ParticipantTeamAsanaJudgeTotalScoreService");
     	return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndAsanaAndParticipantAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantTeamAsana,
			ParticipantTeamParticipants participantTeamParticipant, Integer round) {
		LOGGER.info("Entered getByTeamAndAsanaAndParticipantAndRound method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndAsanaAndParticipantAndRound method -ParticipantTeamAsanaJudgeTotalScoreService");
        return repo.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndRound(
				championshipParticipantTeams, participantTeamAsana, participantTeamParticipant, round);

	}

	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndAsanaAndParticipantAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantTeamAsana,
			ParticipantTeamParticipants participantTeamParticipant, Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getByTeamAndAsanaAndParticipantAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndAsanaAndParticipantAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
    return repo
				.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndRoundAndType(
						championshipParticipantTeams, participantTeamAsana, participantTeamParticipant, round, judgeType);

	}

	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndAsanaAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantTeamAsana,
			Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getByTeamAndAsanaAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndAsanaAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
	return repo.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndRoundAndType(
				championshipParticipantTeams, participantTeamAsana, round, judgeType);

	}

	public Float getSumByTeamAndParticipantAndRound(ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, Integer round) {
		LOGGER.info("Entered getSumByTeamAndParticipantAndRound method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getSumByTeamAndParticipantAndRound method -ParticipantTeamAsanaJudgeTotalScoreService");
     return repo.findSumByTeamAndParticipantAndRound(championshipParticipantTeams.getId(),
				participantTeamParticipant.getId(), round);
	}

	public Float getSumByTeamAndParticipantAndRoundAndType(ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, Integer round, JudgeType dJudgeType) {
		LOGGER.info("Entered getSumByTeamAndParticipantAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getSumByTeamAndParticipantAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
	return repo.findSumByTeamAndParticipantAndRoundAndType(championshipParticipantTeams.getId(),
				participantTeamParticipant.getId(), round, dJudgeType.getId());
	}

	public Float getSumByTeamAndRoundAndType(ChampionshipParticipantTeams championshipParticipantTeams, Integer round,
			JudgeType judgeType) {
		LOGGER.info("Entered getSumByTeamAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit  getSumByTeamAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
    return repo.findSumByTeamAndRoundAndType(championshipParticipantTeams.getId(), round, judgeType.getId());
	}

	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndRoundAndRole(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, JudgeType aJudgeType) {
		LOGGER.info("Entered getByTeamAndRoundAndRole method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndRoundAndRole method -ParticipantTeamAsanaJudgeTotalScoreService");
   return repo.findByChampionshipParticipantTeamsAndRoundAndType(championshipParticipantTeams, round, aJudgeType);
	}

	public List<ParticipantTeamAsanaJudgeTotalScore> getAllByTeamAndRoundAndRole(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, JudgeType aJudgeType) {
		LOGGER.info("Entered getAllByTeamAndRoundAndRole method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getAllByTeamAndRoundAndRole method -ParticipantTeamAsanaJudgeTotalScoreService");
	return repo.findAllByChampionshipParticipantTeamsAndRoundAndType(championshipParticipantTeams, round,
				aJudgeType);
	}

	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndSequenceNumberAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer i, Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getByTeamAndSequenceNumberAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndSequenceNumberAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
	return repo.findByChampionshipParticipantTeamsAndSequenceNumberAndRoundAndType(championshipParticipantTeams, i,
				round, judgeType);
	}

	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer asanaSeqNum,
			ParticipantTeamParticipants tradTeamParticipant, Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
	return repo.findByChampionshipParticipantTeamsAndSequenceNumberAndParticipantTeamParticipantsAndRoundAndType(
				championshipParticipantTeams, asanaSeqNum, tradTeamParticipant, round, judgeType);
	}

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, ParticipantTeamAsanas asana, JudgeType judgeType) {
		LOGGER.info("Entered findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType method -ParticipantTeamAsanaJudgeTotalScoreService");
     return repo.findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType(

				championshipParticipantTeams, participantTeamParticipant, asana, judgeType);
	}

	public List<ParticipantTeamAsanaJudgeTotalScore> findByChampionshipParticipantTeamsAndAsanasCompulsory(
			ChampionshipParticipantTeams championshipParticipantTeams) {
		LOGGER.info("Entered findByChampionshipParticipantTeamsAndAsanasCompulsory method -ParticipantTeamAsanaJudgeTotalScoreService");
		LOGGER.info("Exit findByChampionshipParticipantTeamsAndAsanasCompulsory method -ParticipantTeamAsanaJudgeTotalScoreService");
	return repo.findAllByChampionshipParticipantTeamsAndParticipantTeamAsanasCompulsoryTrue(championshipParticipantTeams);
	}

//	public ParticipantTeamAsanaJudgeTotalScore getByTeamAndRound(
//			ChampionshipParticipantTeams championshipParticipantTeams, Integer round) {
//		// TODO Auto-generated method stub
//		return repo.;
//	}

}
