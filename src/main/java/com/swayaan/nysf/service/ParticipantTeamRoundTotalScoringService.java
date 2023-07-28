package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.repository.ParticipantTeamRoundTotalScoringRepository;

@Service
public class ParticipantTeamRoundTotalScoringService {

	@Autowired
	ParticipantTeamRoundTotalScoringRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamRoundTotalScoringService.class);

	public ParticipantTeamRoundTotalScoring save(ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring) {
		LOGGER.info("Entered save method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit save method -ParticipantTeamRoundTotalScoringService");
	return repo.save(participantTeamRoundTotalScoring);
	}

	public ParticipantTeamRoundTotalScoring get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamRoundTotalScoringService");
      try {
  		LOGGER.info("Exit get method -ParticipantTeamRoundTotalScoringService");
     	return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

//	public List<ParticipantTeamRoundTotalScoring> getTeamScoresForOptions(Integer championshipId,
//			Integer asanaCategoryId, String gender, String ageCategory, String roundNumber) {
//		return repo.findWinnersForGivenCategories(championshipId, asanaCategoryId, gender, ageCategory, roundNumber);
//	}

	

	public List<ParticipantTeamRoundTotalScoring> getTeamScoresForOptions(Championship championship,
			ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered getTeamScoresForOptions method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit getTeamScoresForOptions method -ParticipantTeamRoundTotalScoringService");
   return repo.findAllByChampionshipAndChampionshipRounds(championship,championshipRounds);
	}
// get the total and rank for a given team and round
	public ParticipantTeamRoundTotalScoring findByChampionshipParticipantTeamAndChampionshipRounds(
			ChampionshipParticipantTeams championshipParticipantTeam, ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered findByChampionshipParticipantTeamAndChampionshipRounds method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit findByChampionshipParticipantTeamAndChampionshipRounds method -ParticipantTeamRoundTotalScoringService");
    return repo.findByChampionshipParticipantTeamsAndChampionshipRounds(championshipParticipantTeam, championshipRounds);
	}

	public List<ParticipantTeamRoundTotalScoring> findByChampionshipAndChampionshipRoundsAndWinner(
			Championship championship, ChampionshipRounds championshipRounds, boolean winner) {
		LOGGER.info("Entered findByChampionshipAndChampionshipRoundsAndWinner method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit findByChampionshipAndChampionshipRoundsAndWinner method -ParticipantTeamRoundTotalScoringService");
	return repo.findByChampionshipAndChampionshipRoundsAndWinnerOrderByRankingAsc(championship, championshipRounds,winner);
	}

	public List<ParticipantTeamRoundTotalScoring> findByChampionshipAndChampionshipRounds(Championship championship,
			ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered findByChampionshipAndChampionshipRounds method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit findByChampionshipAndChampionshipRounds method -ParticipantTeamRoundTotalScoringService");
   return repo.findByChampionshipAndChampionshipRounds(championship, championshipRounds);
	}

	public boolean isExistsByChampionshipParticipantTeams(ChampionshipParticipantTeams team) {
		LOGGER.info("Entered isExistsByChampionshipParticipantTeams method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit isExistsByChampionshipParticipantTeams method -ParticipantTeamRoundTotalScoringService");
	return repo.existsByChampionshipParticipantTeams(team);
	}

//	public ParticipantTeamRoundTotalScoring findByChampionshipAndChampionshipParticipantTeamAndChampionshipRounds(
//			Championship championship, ChampionshipParticipantTeams championshipParticipantTeams,
//			ChampionshipRounds championshipRounds) {
//		// TODO Auto-generated method stub
//		return repo.findByChampionshipAndChampionshipParticipantTeamAndChampionshipRounds(championship,championshipParticipantTeams, championshipRounds);
//	}


//	public void delete(Integer id) throws NotFoundException {
//		Long countById = repo.countById(id);
//		if (countById == null || countById == 0) {
//			throw new NotFoundException("Could not find Participant Team Round total scoring with ID " + id);
//		}
//		repo.deleteById(id);
//	}


//	public Float getFinalScoreForTeam(ParticipantTeam participantTeam) {
//		// TODO Auto-generated method stub
//		Float finalScore = repo.getSumOfTotalScoresForParticipantTeam(participantTeam.getId());
//		return finalScore;
//	}

//	public ParticipantTeamRoundTotalScoring findByParticipantTeam(ParticipantTeam participantTeam) {
//		return repo.findByParticipantTeam(participantTeam);
//	}
//
//	public ParticipantTeamRoundTotalScoring findByParticipantTeamAndRoundNumber(ParticipantTeam participantTeam,
//			String roundNumber) {
//		return repo.findByParticipantTeamAndRoundNumber(participantTeam, roundNumber);
//	}
//
////	public List<ParticipantTeamRoundTotalScoring> getParticipantTeamRoundTotalScoreForParticpantTeam(
////			ParticipantTeam participantTeam) {
////		List<ParticipantTeamRoundTotalScoring> list = new ArrayList<>();
////		list.add(repo.findByParticipantTeam(participantTeam));
////		return list;
////	}
//
	public List<ParticipantTeamRoundTotalScoring> getParticipantTeamRoundTotalScoreForChampionship(Championship championship) {
		LOGGER.info("Entered getParticipantTeamRoundTotalScoreForChampionship method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit getParticipantTeamRoundTotalScoreForChampionship method -ParticipantTeamRoundTotalScoringService");
	return repo.findByChampionship(championship);
	}
//
//	public List<ParticipantTeamRoundTotalScoring> getParticipantTeamRoundTotalScoreForAsanaCategory(
//			AsanaCategory asanaCategory) {
//		return repo.findByAsanaCategory(asanaCategory);
//	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -ParticipantTeamRoundTotalScoringService");
		LOGGER.info("Exit delete method -ParticipantTeamRoundTotalScoringService");
	repo.deleteById(id);
	}

}