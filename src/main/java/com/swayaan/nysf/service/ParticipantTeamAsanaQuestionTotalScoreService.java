package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeamAsanaQuestionTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.QuestionTypeEnum;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.repository.ParticipantTeamAsanaQuestionTotalScoreRepository;

@Service
public class ParticipantTeamAsanaQuestionTotalScoreService {

	@Autowired
	ParticipantTeamAsanaQuestionTotalScoreRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamAsanaQuestionTotalScoreService.class);

	public void save(ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore) {
		LOGGER.info("Entered save method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info("Exit save method -ParticipantTeamAsanaQuestionTotalScoreService");
		repo.save(participantTeamAsanaQuestionTotalScore);
	}

	public ParticipantTeamAsanaQuestionTotalScore findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndParticipantTeamRefereesAndQuestionTypeEnumAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantteamAsanas,
			ParticipantTeamParticipants participantTeamParticipants, ParticipantTeamReferees participantTeamReferees,
			QuestionTypeEnum questionType, Integer round) {
		LOGGER.info(
				"Entered findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndParticipantTeamRefereesAndQuestionTypeEnumAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndParticipantTeamRefereesAndQuestionTypeEnumAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo
				.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndParticipantTeamRefereesAndQuestionTypeAndRound(
						championshipParticipantTeams, participantteamAsanas, participantTeamParticipants,
						participantTeamReferees, questionType, round);
	}

	public ParticipantTeamAsanaQuestionTotalScore findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantteamAsanas,
			ParticipantTeamReferees participantTeamReferees, Integer round) {
		LOGGER.info(
				"Entered findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound(
				championshipParticipantTeams, participantteamAsanas, participantTeamReferees, round);

	}

	public ParticipantTeamAsanaQuestionTotalScore get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamAsanaQuestionTotalScoreService");
		try {
			LOGGER.info("Exit get method -ParticipantTeamAsanaQuestionTotalScoreService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public List<ParticipantTeamAsanaQuestionTotalScore> getByAsanaAndJudgeAndTeamAndRoundAndQuestionType(
			ParticipantTeamAsanas participantTeamAsana, ParticipantTeamReferees participantTeamReferee,
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, QuestionTypeEnum questionType) {
		LOGGER.info(
				"Entered getByAsanaAndJudgeAndTeamAndRoundAndQuestionType method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit getByAsanaAndJudgeAndTeamAndRoundAndQuestionType method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo
				.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRoundAndQuestionType(
						championshipParticipantTeams, participantTeamAsana, participantTeamReferee, round,
						questionType);

	}

	public List<ParticipantTeamAsanaQuestionTotalScore> getByAsanaAndTeamAndRoundAndQuestionType(
			ParticipantTeamAsanas participantTeamAsana, ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, QuestionTypeEnum questionType) {
		LOGGER.info(
				"Entered getByAsanaAndTeamAndRoundAndQuestionType method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit getByAsanaAndTeamAndRoundAndQuestionType method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndRoundAndQuestionType(
				championshipParticipantTeams, participantTeamAsana, round, questionType);
	}

	public List<ParticipantTeamAsanaQuestionTotalScore> getByAsanaAndTeamAndRound(
			ParticipantTeamAsanas participantTeamAsana, ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round) {
		LOGGER.info("Entered getByAsanaAndTeamAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info("Exit getByAsanaAndTeamAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndRound(championshipParticipantTeams,
				participantTeamAsana, round);
	}

	public List<ParticipantTeamAsanaQuestionTotalScore> getByAsanaAndTeamAndRoundAndRefereeRole(
			ParticipantTeamAsanas participantTeamAsana, ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType judgeType) {
		LOGGER.info(
				"Entered getByAsanaAndTeamAndRoundAndRefereeRole method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit getByAsanaAndTeamAndRoundAndRefereeRole method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndRoundAndParticipantTeamRefereesType(
				championshipParticipantTeams, participantTeamAsana, round, judgeType);
	}

	public ParticipantTeamAsanaQuestionTotalScore findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferee,
			Integer round) {
		LOGGER.info(
				"Entered findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound(championshipParticipantTeams,
				participantTeamReferee, round);
	}

	public List<ParticipantTeamAsanaQuestionTotalScore> findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferee,
			Integer round) {
		LOGGER.info(
				"Entered findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound(
				championshipParticipantTeams, participantTeamReferee, round);
	}

	public ParticipantTeamAsanaQuestionTotalScore getBySequenceNumberAndTeamAndRoundAndRefereeRole(Integer i,
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, JudgeType judgeType) {
		LOGGER.info(
				"Entered getBySequenceNumberAndTeamAndRoundAndRefereeRole method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit getBySequenceNumberAndTeamAndRoundAndRefereeRole method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findByChampionshipParticipantTeamsAndSequenceNumberAndRoundAndParticipantTeamRefereesType(
				championshipParticipantTeams, i, round, judgeType);
	}

	public List<ParticipantTeamAsanaQuestionTotalScore> getAllBySequenceNumberAndTeamAndRoundAndRefereeType(
			Integer asanaSeqNum, ChampionshipParticipantTeams championshipParticipantTeams, Integer round,
			JudgeType judgeType) {
		LOGGER.info(
				"Entered getAllBySequenceNumberAndTeamAndRoundAndRefereeType method -ParticipantTeamAsanaQuestionTotalScoreService");
		LOGGER.info(
				"Exit getAllBySequenceNumberAndTeamAndRoundAndRefereeType method -ParticipantTeamAsanaQuestionTotalScoreService");
		return repo.findAllByChampionshipParticipantTeamsAndSequenceNumberAndRoundAndParticipantTeamRefereesType(
				championshipParticipantTeams, asanaSeqNum, round, judgeType);

	}

}
