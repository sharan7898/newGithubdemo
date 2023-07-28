package com.swayaan.nysf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantScoringJudge;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.repository.ParticipantScoringJudgeRepository;

@Service
public class ParticipantScoringJudgeService {

	@Autowired
	ParticipantScoringJudgeRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantScoringJudgeService.class);

	public ParticipantScoringJudge findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
			ParticipantTeamReferees participantTeamReferees, ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipCategory championshipCategory, Championship championship, JudgeType judgeType, Integer round) {
		LOGGER.info(
				"Entered findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound method -ParticipantScoringJudgeService");
		LOGGER.info(
				"Exit findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound method -ParticipantScoringJudgeService");
		return repo
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, round);
	}

	public void save(ParticipantScoringJudge participantScoringJudgeNew) {
		LOGGER.info("Entered save method -ParticipantScoringJudgeService");
		LOGGER.info("Exit save method -ParticipantScoringJudgeService");
		repo.save(participantScoringJudgeNew);

	}

}
