package com.swayaan.nysf.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamParticipantFinalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;

@Repository
public interface ParticipantTeamParticipantFinalScoreRepository extends CrudRepository<ParticipantTeamParticipantFinalScore, Integer> {

	ParticipantTeamParticipantFinalScore findFirstByChampionshipParticipantTeamsAndParticipantTeamParticipantAndRound(
			ChampionshipParticipantTeams championshipParticipantTeam,
			ParticipantTeamParticipants participantTeamParticipant, Integer round);

}
