package com.swayaan.nysf.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeamTotalScoring;
import com.swayaan.nysf.entity.User;

@Repository
public interface ParticipantTeamTotalScoringRepository extends CrudRepository<ParticipantTeamTotalScoring, Integer>{

	ParticipantTeamTotalScoring findByRoundAndChampionshipParticipantTeamAndJudgeUser(Integer round,
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser);

	boolean existsByRoundAndChampionshipParticipantTeamAndJudgeUser(Integer round, ChampionshipParticipantTeams team,
			Judge judgeUser);



}
