package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.entity.TraditionalJudgeScore;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.repository.TraditionalJudgeScoreRepository;

@Service
public class TraditionalJudgeScoreService {
	@Autowired
	TraditionalJudgeScoreRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(TraditionalJudgeScoreService.class);

	public boolean isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions, ParticipantTeamParticipants participantTeamParticipants,
			ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info(
				"Entered isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas method -TraditionalJudgeScoreService");
		return repo
				.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipantsAndParticipantTeamAsanas(
						championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
						participantTeamParticipants, participantTeamAsanas);
	}

	public void save(TraditionalJudgeScore traditionalJudgeScore) {
		LOGGER.info("Entered save method -TraditionalJudgeScoreService");
		LOGGER.info("Exit save method -TraditionalJudgeScoreService");
		repo.save(traditionalJudgeScore);

	}

	public List<TraditionalJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			ParticipantTeamParticipants participantTeamParticipants, ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info(
				"Entered findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		return repo
				.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
						championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
						participantTeamAsanas);
	}

	public List<TraditionalJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestion) {
		LOGGER.info(
				"Entered findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		return repo
				.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionOrderBySequenceNumberAsc(
						championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);
	}

	public boolean isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestion) {
		LOGGER.info(
				"Entered isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions method -TraditionalJudgeScoreService");
		return repo.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
				championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);
	}

	public boolean isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamAsanas(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions, ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info(
				"Entered isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamAsanas method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamAsanas method -TraditionalJudgeScoreService");
		return repo
				.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamAsanas(
						championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
						participantTeamAsanas);
	}

	public List<TraditionalJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamAsanasOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info(
				"Entered findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		return repo
				.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamAsanasOrderBySequenceNumberAsc(
						championshipParticipantTeams, participantTeamReferees, participantTeamAsanas);
	}

	public TraditionalJudgeScore findById(Integer id) {
		LOGGER.info("Entered findById method -TraditionalJudgeScoreService");
		LOGGER.info("Exit findById method -TraditionalJudgeScoreService");
		return repo.findById(id).get();
	}

	public List<TraditionalJudgeScore> findAllByChampionshipParticipantTeamsAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamsAndRound method -TraditionalJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamsAndRound method -TraditionalJudgeScoreService");
		return repo.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
	}

	public List<TraditionalJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipants, ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info(
				"Entered findAllByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit findAllByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		return repo
				.findAllByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
						championshipParticipantTeams, participantTeamParticipants, participantTeamAsanas);
	}

	public TraditionalJudgeScore findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamReferees dJudgeParticipantTeamReferee,
			ParticipantTeamParticipants participantTeamParticipants, ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info(
				"Entered findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		LOGGER.info(
				"Exit findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TraditionalJudgeScoreService");
		return repo
				.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
						championshipParticipantTeams, dJudgeParticipantTeamReferee, participantTeamParticipants,
						participantTeamAsanas);
	}

}
