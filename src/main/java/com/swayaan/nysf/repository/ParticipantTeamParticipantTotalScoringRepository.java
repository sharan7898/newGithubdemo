package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeamParticipantTotalScoring;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;

@Repository
public interface ParticipantTeamParticipantTotalScoringRepository
		extends CrudRepository<ParticipantTeamParticipantTotalScoring, Integer> {

	ParticipantTeamParticipantTotalScoring findByRoundAndJudgeUserAndParticipantTeamParticipantAndChampionshipParticipantTeams(
			Integer round, Judge judgeUser, ParticipantTeamParticipants participantTeamParticipant,
			ChampionshipParticipantTeams championshipParticipant);

	List<ParticipantTeamParticipantTotalScoring> findAllByRoundAndParticipantTeamParticipantAndChampionshipParticipantTeams(
			Integer round, ParticipantTeamParticipants participantTeamParticipant,
			ChampionshipParticipantTeams championshipParticipant);

}
