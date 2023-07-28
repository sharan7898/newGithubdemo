package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ArtisticJudgeScore;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.repository.ArtisticJudgeScoreRepository;

@Service
public class ArtisticJudgeScoreService {

	@Autowired
	ArtisticJudgeScoreRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArtisticJudgeScoreService.class);


	public boolean isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions) {
		LOGGER.info("Entered isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions method -ArtisticJudgeScoreService");
		LOGGER.info("Exit isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions method -ArtisticJudgeScoreService");

		return repo.existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
				championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions);
	}

	public ArtisticJudgeScore save(ArtisticJudgeScore artisticJudgeScore) {
		LOGGER.info("Entered save method -ArtisticJudgeScoreService");
		LOGGER.info("Exit save method -ArtisticJudgeScoreService");
         return repo.save(artisticJudgeScore);
		}

	public List<ArtisticJudgeScore> findAllByChampionshipParticipantTeamAndParticipantTeamReferees(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamReferees participantTeamReferees) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamAndParticipantTeamReferees method -ArtisticJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamAndParticipantTeamReferees method -ArtisticJudgeScoreService");

		return repo.findAllByChampionshipParticipantTeamsAndParticipantTeamReferees(championshipParticipantTeams,participantTeamReferees);
	}

	public ArtisticJudgeScore findById(Integer id) {
		LOGGER.info("Entered findById method -ArtisticJudgeScoreService");
		LOGGER.info("Exit findById method -ArtisticJudgeScoreService");

		return repo.findById(id).get();
	}

	public  Float getSumForAllByChampionshipParticipantTeamAndParticipantTeamReferees(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferee, Integer round) {
		LOGGER.info("Entered getSumForAllByChampionshipParticipantTeamAndParticipantTeamReferees method -ArtisticJudgeScoreService");
		LOGGER.info("Exit getSumForAllByChampionshipParticipantTeamAndParticipantTeamReferees method -ArtisticJudgeScoreService");

		return repo.findSumByChampionshipParticipantTeamAndReferee(championshipParticipantTeams.getId(),participantTeamReferee.getId(), round);
	}

	public List<ArtisticJudgeScore> findAllByChampionshipParticipantTeamsAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round) {
		LOGGER.info("Entered findAllByChampionshipParticipantTeamsAndRound method -ArtisticJudgeScoreService");
		LOGGER.info("Exit findAllByChampionshipParticipantTeamsAndRound method -ArtisticJudgeScoreService");

		return repo.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams,round);
	}

}
