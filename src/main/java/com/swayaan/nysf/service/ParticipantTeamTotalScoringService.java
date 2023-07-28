package com.swayaan.nysf.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.ParticipantTeamTotalScoring;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.repository.ParticipantTeamTotalScoringRepository;

@Service
public class ParticipantTeamTotalScoringService {

	@Autowired
	ParticipantTeamTotalScoringRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamTotalScoringService.class);


	public ParticipantTeamTotalScoring save(ParticipantTeamTotalScoring participantTeamTotalScoring) {
		LOGGER.info("Entered save method -ParticipantTeamTotalScoringService");
		LOGGER.info("Exit save method -ParticipantTeamTotalScoringService");
    return repo.save(participantTeamTotalScoring);
	}

	public ParticipantTeamTotalScoring get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamTotalScoringService");
     try {
 		LOGGER.info("Exit get method -ParticipantTeamTotalScoringService");
	return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public ParticipantTeamTotalScoring findByRoundAndChampionshipParticipantTeamAndJudgeUser(Integer round,
			ChampionshipParticipantTeams championshipParticipantTeams, Judge currentUser) {
		LOGGER.info("Entered findByRoundAndChampionshipParticipantTeamAndJudgeUser method -ParticipantTeamTotalScoringService");
		LOGGER.info("Exit findByRoundAndChampionshipParticipantTeamAndJudgeUser method -ParticipantTeamTotalScoringService");
	return repo.findByRoundAndChampionshipParticipantTeamAndJudgeUser(round, championshipParticipantTeams, currentUser);
	}

	public boolean isExistsByRoundAndChampionshipParticipantTeamAndJudgeUser(Integer round,
			ChampionshipParticipantTeams team, ParticipantTeamReferees referee) {
		LOGGER.info("Entered isExistsByRoundAndChampionshipParticipantTeamAndJudgeUser method -ParticipantTeamTotalScoringService");
		LOGGER.info("Exit isExistsByRoundAndChampionshipParticipantTeamAndJudgeUser method -ParticipantTeamTotalScoringService");
	return repo.existsByRoundAndChampionshipParticipantTeamAndJudgeUser(round,team,referee.getJudgeUser());
	}

	public ParticipantTeamTotalScoring findByChampionshipParticpantTeamAndRoundNumAndJudgeUser(
			ChampionshipParticipantTeams championshipParticipantTeam, Integer round, Judge user) {
		LOGGER.info("Entered findByChampionshipParticpantTeamAndRoundNumAndJudgeUser method -ParticipantTeamTotalScoringService");
		LOGGER.info("Exit findByChampionshipParticpantTeamAndRoundNumAndJudgeUser method -ParticipantTeamTotalScoringService");
	return repo.findByRoundAndChampionshipParticipantTeamAndJudgeUser(round, championshipParticipantTeam, user);
	}

//	public void delete(Integer id) throws NotFoundException {
//		Long countById = repo.countById(id);
//		if (countById == null || countById == 0) {
//			throw new NotFoundException("Could not find Participant Team total scoring with ID " + id);
//		}
//		repo.deleteById(id);
//	}
//
//
//	public float getTotalScoreForTeamAndRound(Integer participantTeamId, String roundNumber) {
//		return repo.getSumOfScoreForTeamAndRoundNumber(participantTeamId, roundNumber);
//		
//	}
//
//	public float getSumOfTotalScoresForReferee(ParticipantTeam participantTeam, User refereeUser, String round) {
//		return repo.getSumOfTotalScoresForRefereeAndRound(participantTeam.getId(),refereeUser.getId(),round);
//	}
//
//	public List<ParticipantTeamTotalScoring> getParticipantTeamTotalScoreForReferees(User user) {
//		
//		return repo.findByUser(user);
//	}
//
//	public List<ParticipantTeamTotalScoring> getParticipantTeamTotalScoreForParticpantTeam(
//			ParticipantTeam participantTeam) {
//		
//		return repo.findByParticipantTeam(participantTeam);
//	}
//
//	public List<ParticipantTeamTotalScoring> listparticipantTeamTotalScoreByAsana(Asana asana) {
//		return repo.findByAsana(asana);
//	}

//	public ParticipantTeamTotalScoring findByParticpantTeamAndRoundNumAndUser(ParticipantTeam participantTeam,
//			Integer roundNumber, User currentUser) {
//	//	return repo.findByParticipantTeamAndRoundNumberAndUser(participantTeam,roundNumber,currentUser);
//	}

}