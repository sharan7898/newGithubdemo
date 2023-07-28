package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ArtisticJudgeScore;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamReferees;

@Repository
public interface ArtisticJudgeScoreRepository extends CrudRepository<ArtisticJudgeScore, Integer> {

	boolean existsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees,
			AsanaEvaluationQuestions asanaEvaluationQuestions);

	List<ArtisticJudgeScore> findAllByChampionshipParticipantTeamsAndParticipantTeamReferees(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamReferees participantTeamReferees);

	@Query(value="Select SUM(total_score) FROM artistic_judge_scoring where "
			+ "championship_participant_teams_id = :championshipParticipantTeamsId and "
			+ "participantteam_referees_id = :participantTeamRefereeId and "
			+ "round =:round",nativeQuery=true)
	public Float findSumByChampionshipParticipantTeamAndReferee(Integer championshipParticipantTeamsId, 
			Integer participantTeamRefereeId,
			Integer round);

	List<ArtisticJudgeScore> findAllByChampionshipParticipantTeamsAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round);

}
