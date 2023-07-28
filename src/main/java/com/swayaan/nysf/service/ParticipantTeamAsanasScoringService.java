package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanaScoring;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RoundEnum;
import com.swayaan.nysf.repository.ParticipantTeamAsanasScoringRepository;

@Service
public class ParticipantTeamAsanasScoringService {

	@Autowired
	ParticipantTeamAsanasScoringRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamAsanasScoringService.class);

	public ParticipantTeamAsanaScoring save(ParticipantTeamAsanaScoring participantTeamAsanaScoring) {
		LOGGER.info("Entered save ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit save ParticipantTeamAsanasScoringService");

		return repo.save(participantTeamAsanaScoring);
	}

	public List<ParticipantTeamAsanaScoring> save(List<ParticipantTeamAsanaScoring> listParticipantTeamAsanaScoring) {
		LOGGER.info("Entered save ParticipantTeamAsanasScoringService");
	List<ParticipantTeamAsanaScoring> participantTeamAsanaScoringList = new ArrayList<ParticipantTeamAsanaScoring>();
		ParticipantTeamAsanaScoring participantTeam = new ParticipantTeamAsanaScoring();
		for (ParticipantTeamAsanaScoring participantTeamAsanaScoring : listParticipantTeamAsanaScoring) {
			participantTeam = repo.save(participantTeamAsanaScoring);
			participantTeamAsanaScoringList.add(participantTeam);
		}
		LOGGER.info("Exit save ParticipantTeamAsanasScoringService");
      return participantTeamAsanaScoringList;
	}

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered findAllByParticipantTeam ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit findAllByParticipantTeam ParticipantTeamAsanasScoringService");
	return repo.findAllByParticipantTeamAsanasParticipantTeam(participantTeam);
	}

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeamAndByRoundAndByJudgeUser(
			ParticipantTeam participantTeam, RoundEnum roundNumber, Judge judgeUser) {
		LOGGER.info("Entered findAllByParticipantTeamAndByRoundAndByJudgeUser ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit findAllByParticipantTeamAndByRoundAndByJudgeUser ParticipantTeamAsanasScoringService");
     	return repo.findAllByParticipantTeamAsanasParticipantTeamAndParticipantTeamAsanasRoundNumberAndJudgeUser(
				participantTeam, roundNumber.toString(), judgeUser);
	}

	public ParticipantTeamAsanaScoring getById(Integer id) {
		LOGGER.info("Entered ParticipantTeamAsanaScoring ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit ParticipantTeamAsanaScoring ParticipantTeamAsanasScoringService");
    return repo.findById(id).get();
	}

	public boolean existsByChampionshipParticipantTeamsAndJudgeUser(
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser) {
		LOGGER.info("Entered existsByChampionshipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit existsByChampionshipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
	return repo.existsByChampionshipParticipantTeamsAndJudgeUser(championshipParticipantTeams, judgeUser);
	}

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser(ParticipantTeamParticipants participant, 
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser) {
		LOGGER.info("Entered findAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit findAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
    return repo.findAllByParticipantTeamParticipantsAndChampionshipParticipantTeamsAndJudgeUser(participant,championshipParticipantTeams,judgeUser);
	}

		public List<ParticipantTeamAsanaScoring> findAllByChampionhsipParticipantTeamsAndParticipantTeamParticipantAndByParticipantTeamAsanas(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, ParticipantTeamAsanas asana) {
			LOGGER.info("Entered findAllByChampionhsipParticipantTeamsAndParticipantTeamParticipantAndByParticipantTeamAsanas ParticipantTeamAsanasScoringService");
			LOGGER.info("Exit findAllByChampionhsipParticipantTeamsAndParticipantTeamParticipantAndByParticipantTeamAsanas ParticipantTeamAsanasScoringService");
      return repo.findAllByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanas(championshipParticipantTeams,participantTeamParticipant,asana);
	}

	public Integer findAllByChampionhsipParticipantTeamsAndJudgeUser(
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser) {
		LOGGER.info("Entered findAllByChampionhsipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit findAllByChampionhsipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
	return repo.findAllByChampionshipParticipantTeamsAndJudgeUser(championshipParticipantTeams.getId(), judgeUser.getId());
	}
	
	public Float getAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser(ParticipantTeamParticipants participant, 
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser) {
		LOGGER.info("Entered getAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit getAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
	return repo.getAllAsanaScoresByParticipantTeamParticipantsAndChampionshipParticipantTeamsAndJudgeUser(participant.getId(),championshipParticipantTeams.getId(),judgeUser.getId());
	}

	public Integer findAllByChampionhsipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		LOGGER.info("Entered findAllByChampionhsipParticipantTeams ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit findAllByChampionhsipParticipantTeams ParticipantTeamAsanasScoringService");
	return repo.findAllByChampionshipParticipantTeams(championshipParticipantTeams.getId());
	}

	public Integer findAllByChampionhsipParticipantTeamsAndJudgeUser(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees referee) {
		LOGGER.info("Entered findAllByChampionhsipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
		LOGGER.info("Exit findAllByChampionhsipParticipantTeamsAndJudgeUser ParticipantTeamAsanasScoringService");
		return repo.findAllByChampionshipParticipantTeamsAndJudgeUser(championshipParticipantTeams.getId(),referee.getId());
	}
	
}
