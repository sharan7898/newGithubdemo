package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.QuestionTypeEnum;
import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.repository.TimeKeeperJudgeScoreRepository;

@Service
public class TimeKeeperJudgeScoreService {

	@Autowired
	TimeKeeperJudgeScoreRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeKeeperJudgeScoreService.class);


	public List<TimeKeeperJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamReferees participantTeamReferees) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc method -TimeKeeperJudgeScoreService");
	return repo.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
				championshipParticipantTeams, participantTeamReferees);
	}

	public boolean isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions, ParticipantTeamParticipants participantTeamParticipants,
			ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info("Entered isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas method -TimeKeeperJudgeScoreService");
     	return repo
				.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipantsAndParticipantTeamAsanas(
						championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
						participantTeamParticipants, participantTeamAsanas);
	}

	public TimeKeeperJudgeScore save(TimeKeeperJudgeScore timeKeeperJudgeScore) {
		LOGGER.info("Entered save method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit save method -TimeKeeperJudgeScoreService");
	return repo.save(timeKeeperJudgeScore);

	}

	public TimeKeeperJudgeScore findById(Integer id) {
		LOGGER.info("Entered findById method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findById method -TimeKeeperJudgeScoreService");
	return repo.findById(id).get();
	}

//	public List<TimeKeeperJudgeScore> getAllByTeamAndRoundAndRole(ChampionshipParticipantTeams championshipParticipantTeams, Integer round,
//			Role tJudgeRole) {
//		// TODO Auto-generated method stub
//		return repo.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesRole(championshipParticipantTeams,round,tJudgeRole);
//		
//	}

	public List<TimeKeeperJudgeScore> getAllByTeamAndRoundAndReferee(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round,
			ParticipantTeamReferees tJudgeTeamReferee) {
		LOGGER.info("Entered getAllByTeamAndRoundAndReferee method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit getAllByTeamAndRoundAndReferee method -TimeKeeperJudgeScoreService");
	return repo.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamReferees(
				championshipParticipantTeams, round, tJudgeTeamReferee);

	}

	public TimeKeeperJudgeScore findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions) {
		LOGGER.info("Entered findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion method -TimeKeeperJudgeScoreService");
     return repo.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
				championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions);
	}

	public List<TimeKeeperJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			ParticipantTeamParticipants participantTeamParticipants, ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc method -TimeKeeperJudgeScoreService");
	return repo
				.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
						championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
						participantTeamAsanas);
	}

	public boolean isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestion, Integer sequenceNumber) {
		LOGGER.info("Entered isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber method -TimeKeeperJudgeScoreService");
	return repo
				.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
						championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion, sequenceNumber);
	}

	public boolean isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestion) {
		LOGGER.info("Entered isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions method -TimeKeeperJudgeScoreService");
	return repo.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
				championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);
	}

	public List<TimeKeeperJudgeScore> findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions, Integer sequenceNumber) {
		LOGGER.info("Entered findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber method -TimeKeeperJudgeScoreService");
	return repo
				.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
						championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
						sequenceNumber);
	}

	public List<TimeKeeperJudgeScore> findAllByChampionshipParticipantTeamsAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamsAndRound method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamsAndRound method -TimeKeeperJudgeScoreService");
	return repo.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
	}

	public List<TimeKeeperJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamReferees(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamReferees participantTeamReferees) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamsAndParticipantTeamReferees method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamsAndParticipantTeamReferees method -TimeKeeperJudgeScoreService");
     	return repo.findAllByChampionshipParticipantTeamsAndParticipantTeamReferees(championshipParticipantTeams,participantTeamReferees);
	}

	public Float getSumByTeamAndRoundAndReferee(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, ParticipantTeamReferees tJudgeTeamReferee) {
		LOGGER.info("Entered getSumByTeamAndRoundAndReferee method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit getSumByTeamAndRoundAndReferee method -TimeKeeperJudgeScoreService");
	return repo.findSumByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound(championshipParticipantTeams.getId(),tJudgeTeamReferee.getId(),round);
	}

	public TimeKeeperJudgeScore getBySequenceNumberAndTeamAndRound(Integer asanaSeqNum,
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round) {
		LOGGER.info("Entered getBySequenceNumberAndTeamAndRound method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit getBySequenceNumberAndTeamAndRound method -TimeKeeperJudgeScoreService");
    return repo.findBySequenceNumberAndChampionshipParticipantTeamsAndRound(asanaSeqNum,championshipParticipantTeams, round);
	}


	public TimeKeeperJudgeScore findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions, ParticipantTeamParticipants participantTeamParticipant) {
		LOGGER.info("Entered findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants method -TimeKeeperJudgeScoreService");
	return repo.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants(championshipParticipantTeams,participantTeamReferees
				,asanaEvaluationQuestions,participantTeamParticipant);
	}

	public TimeKeeperJudgeScore getByTeamAndRoundAndRefereeAndQuestionType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round,
			ParticipantTeamReferees tJudgeTeamReferee, QuestionTypeEnum commonteamquestion) {
		LOGGER.info("Entered getByTeamAndRoundAndRefereeAndQuestionType method -TimeKeeperJudgeScoreService");
		LOGGER.info("Exit getByTeamAndRoundAndRefereeAndQuestionType method -TimeKeeperJudgeScoreService");
	return repo.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndQuestionType(championshipParticipantTeams, tJudgeTeamReferee, commonteamquestion);

	}

}
