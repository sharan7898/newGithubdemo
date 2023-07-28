package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.repository.ParticipantTeamRefereesRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ParticipantTeamRefereesService {

	@Autowired
	ParticipantTeamRefereesRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamRefereesService.class);

	public ParticipantTeamReferees save(ParticipantTeamReferees participantTeamReferees) {
		LOGGER.info("participantTeamReferees" + participantTeamReferees);
		LOGGER.info("Entered save method -ParticipantTeamRefereesService");
		LOGGER.info("Exit save method -ParticipantTeamRefereesService");
    return repo.save(participantTeamReferees);
	}

	public ParticipantTeamReferees get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamRefereesService");
      try {
  		LOGGER.info("Exit get method -ParticipantTeamRefereesService");
       return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public float getTotalScore(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getTotalScore method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTotalScore method -ParticipantTeamRefereesService");
     return repo.getTeamTotalScore(participantTeam.getId());
	}

	public List<ParticipantTeamReferees> getTeamsAssignedForReferees(Judge judge_refereeUser) {
		LOGGER.info("Entered getTeamsAssignedForReferees method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTeamsAssignedForReferees method -ParticipantTeamRefereesService");
	return repo.findByJudgeUser(judge_refereeUser);
	}

	public ParticipantTeamReferees getByUserAndTeam(Judge judge_refereeUser, ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByUserAndTeam method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getByUserAndTeam method -ParticipantTeamRefereesService");
     return repo.findByJudgeUserAndParticipantTeam(judge_refereeUser, participantTeam);
	}

//	public List<ParticipantTeamReferees> getRefereesForTeamAndRefereeType(ParticipantTeam participantTeam, String refereeType) {
//		return repo.findByParticipantTeamAndRefereeType(participantTeam.getId(), refereeType);
//	}

	public List<ParticipantTeamReferees> getTeamReferresForParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getTeamReferresForParticipantTeam method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTeamReferresForParticipantTeam method -ParticipantTeamRefereesService");
   return repo.findByParticipantTeam(participantTeam);
	}

	public List<ParticipantTeamReferees> getTeamReferresForParticipantTeamAndUser(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getTeamReferresForParticipantTeamAndUser method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTeamReferresForParticipantTeamAndUser method -ParticipantTeamRefereesService");
    return repo.findByParticipantTeam(participantTeam);
	}

	public void deleteTeamReferresForParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered deleteTeamReferresForParticipantTeam method -ParticipantTeamRefereesService");
    List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeam.getParticipantTeamReferees();
		LOGGER.info("participantTeam.getId()");
		LOGGER.info("listParticipantTeamReferees.size()");
		if (listParticipantTeamReferees.size() != 0) {
			for (ParticipantTeamReferees participantTeamReferees : listParticipantTeamReferees) {
				LOGGER.info("Exit deleteTeamReferresForParticipantTeam method -ParticipantTeamRefereesService");
       	repo.deleteById(participantTeamReferees.getId());
			}
			LOGGER.info("Deleted successfully");
		}

	}

	public ParticipantTeamReferees getTeamReferresForParticipantTeamAndJudgeUser(ParticipantTeam participantTeam,
			Judge user) {
		LOGGER.info("Entered getTeamReferresForParticipantTeamAndJudgeUser method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTeamReferresForParticipantTeamAndJudgeUser method -ParticipantTeamRefereesService");
	return repo.findByParticipantTeamAndJudgeUser(participantTeam, user);
	}

	public void deleteTeamReferresForParticipantTeamAndRoundNumber(ParticipantTeam participantTeam, Integer round) {
		LOGGER.info("Entered deleteTeamReferresForParticipantTeamAndRoundNumber method -ParticipantTeamRefereesService");

		List<ParticipantTeamReferees> listParticipantTeamReferees = repo
				.findByParticipantTeamAndRoundNumber(participantTeam, round);

		if (listParticipantTeamReferees.size() != 0) {
			for (ParticipantTeamReferees participantTeamReferees : listParticipantTeamReferees) {
				LOGGER.info("Exit deleteTeamReferresForParticipantTeamAndRoundNumber method -ParticipantTeamRefereesService");
        	repo.deleteById(participantTeamReferees.getId());
			}
			LOGGER.info("Deleted successfully");
		}

	}

	public List<ParticipantTeamReferees> getByTeamAndRound(ParticipantTeam participantTeam, Integer round) {
		LOGGER.info("Entered getByTeamAndRound method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getByTeamAndRound method -ParticipantTeamRefereesService");
     return repo.findByParticipantTeamAndRoundNumber(participantTeam, round);
	}

	public List<ParticipantTeamReferees> getByRoundAndChampionshipAndChampionshipCategory(Integer round,
			Championship championship, ChampionshipCategory championshipCategory) {
		LOGGER.info("Entered getByRoundAndChampionshipAndChampionshipCategory method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getByRoundAndChampionshipAndChampionshipCategory method -ParticipantTeamRefereesService");
	return repo.findDistinctByRoundNumberAndChampionshipAndChampionshipCategory(round, championship, championshipCategory);
	}
	
	public List<ParticipantTeamReferees> getByParticipantTeamWithRoundAndChampionshipAndChampionshipCategory(Integer round,
			Championship championship, ChampionshipCategory championshipCategory,ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByParticipantTeamWithRoundAndChampionshipAndChampionshipCategory method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getByParticipantTeamWithRoundAndChampionshipAndChampionshipCategory method -ParticipantTeamRefereesService");
	return repo.findDistinctByRoundNumberAndChampionshipAndChampionshipCategoryAndParticipantTeam(round, championship, championshipCategory,participantTeam);
	}

	public ParticipantTeamReferees getTeamReferresForParticipantTeamAndJudgeUserAndRound(ParticipantTeam participantTeam,
			Judge judgeUser, Integer round) {
		LOGGER.info("Entered getTeamReferresForParticipantTeamAndJudgeUserAndRound method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTeamReferresForParticipantTeamAndJudgeUserAndRound method -ParticipantTeamRefereesService");
    return repo.findByParticipantTeamAndJudgeUserAndRoundNumber(participantTeam, judgeUser, round);
	}

	public List<ParticipantTeamReferees> getByRoundAndChampionshipAndChampionshipCategoryAndParticipantTeam(
			Integer round, Championship championship, ChampionshipCategory championshipCategory,
			ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByRoundAndChampionshipAndChampionshipCategoryAndParticipantTeam method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getByRoundAndChampionshipAndChampionshipCategoryAndParticipantTeam method -ParticipantTeamRefereesService");
	return repo.findByRoundNumberAndChampionshipAndChampionshipCategoryAndParticipantTeam(round, championship,
				championshipCategory, participantTeam);
	}

	public List<ParticipantTeamReferees> getTeamReferresForChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber(
			Championship championship, ChampionshipCategory championshipCategory, ParticipantTeam participantTeam,
			Integer round) {
		LOGGER.info("Entered getTeamReferresForChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getTeamReferresForChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber method -ParticipantTeamRefereesService");
    return repo.findAllByChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber(championship,
				championshipCategory, participantTeam, round);
	}

	public ParticipantTeamReferees getByParticipantTeamAndRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(
			ParticipantTeam participantTeam, Integer round, Championship championship,
			ChampionshipCategory championshipCategory, Judge user) {
		LOGGER.info("Entered getByParticipantTeamAndRoundAndChampionshipAndChampionshipCategoryAndJudgeUser method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getByParticipantTeamAndRoundAndChampionshipAndChampionshipCategoryAndJudgeUser method -ParticipantTeamRefereesService");
   return repo.findByParticipantTeamAndRoundNumberAndChampionshipAndChampionshipCategoryAndJudgeUser(participantTeam, round,championship,championshipCategory,user);
	}

	public List<ParticipantTeamReferees> getAllJudgesofTypeAndParticipantTeamAndRound(ParticipantTeam participantTeam,
			JudgeType judgeType, Integer round_number) {
		LOGGER.info("Entered getAllJudgesofTypeAndParticipantTeamAndRound method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getAllJudgesofTypeAndParticipantTeamAndRound method -ParticipantTeamRefereesService");
	return repo.findAllByParticipantTeamAndTypeAndRoundNumber(participantTeam,judgeType,round_number);
	}

	public ParticipantTeamReferees getJudgeByTypeAndTeamAndRound(ParticipantTeam participantTeam, JudgeType tJudgeType,
			Integer round) {
		LOGGER.info("Entered getJudgeByTypeAndTeamAndRound method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getJudgeByTypeAndTeamAndRound method -ParticipantTeamRefereesService");
    return repo.findByParticipantTeamAndTypeAndRoundNumber(participantTeam, tJudgeType, round);
	}

	public List<ParticipantTeamReferees> getAllJudgeByTypeAndTeamAndRound(ParticipantTeam participantTeam,
			JudgeType judgeType, Integer round) {
		LOGGER.info("Entered getAllJudgeByTypeAndTeamAndRound method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getAllJudgeByTypeAndTeamAndRound method -ParticipantTeamRefereesService");
    return repo.findAllByParticipantTeamAndTypeAndRoundNumber(participantTeam, judgeType, round);
	}

	public List<ParticipantTeamReferees> getAllByTeamAndRoundAndType(ParticipantTeam participantTeam, Integer round, JudgeType judgeType) {
		LOGGER.info("Entered getAllByTeamAndRoundAndType method -ParticipantTeamRefereesService");
		LOGGER.info("Exit getAllByTeamAndRoundAndType method -ParticipantTeamRefereesService");
	return repo.findAllByParticipantTeamAndTypeAndRoundNumber(participantTeam, judgeType, round);
		
	}

public List<ParticipantTeamReferees> getParticipantTeamRefereesByChampionship(Championship championship) {
	LOGGER.info("Entered getParticipantTeamRefereesByChampionship method -ParticipantTeamRefereesService");
	LOGGER.info("Exit getParticipantTeamRefereesByChampionship method -ParticipantTeamRefereesService");
	return repo.findAllByChampionship(championship);
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -ParticipantTeamRefereesService");
		LOGGER.info("Exit delete method -ParticipantTeamRefereesService");
   repo.deleteById(id);
	}

	public List<ParticipantTeamReferees> getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(Integer round,
			Championship championship, ChampionshipCategory championshipCategory, User user) throws JudgeNotFoundException {
		LOGGER.info("Entered getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser method -ParticipantTeamRefereesService");
		Judge judgeUser=CommonUtil.getCurrentJudge();
		LOGGER.info("Exit getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser method -ParticipantTeamRefereesService");
	return repo.findByRoundNumberAndChampionshipAndChampionshipCategoryAndJudgeUser(round,championship,championshipCategory,judgeUser);
	}

	public boolean existsByTeam(ParticipantTeam participantTeams) {
		LOGGER.info("Entered existsByTeam method -ParticipantTeamRefereesService");
		LOGGER.info("Exit existsByTeam method -ParticipantTeamRefereesService");
	return repo.existsByparticipantTeam(participantTeams);
	}

}
