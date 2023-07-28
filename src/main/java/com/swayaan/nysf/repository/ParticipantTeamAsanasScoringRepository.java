package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanaScoring;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;

@Repository
public interface ParticipantTeamAsanasScoringRepository extends CrudRepository<ParticipantTeamAsanaScoring, Integer> {

//	public Long countById(Integer id);
//
//	public Optional<ParticipantTeamAsanas> findById(Long id);
//
//	public List<ParticipantTeamAsanas> findByAsana(Asana asana);
//
//	public ParticipantTeamAsanas findByAsanaAndParticipantTeam(Asana asana, ParticipantTeam participantTeam);
//
//	public List<ParticipantTeamAsanas> findByParticipantTeamAndRoundNumber(ParticipantTeam participantTeam,
//			RoundEnum roundNumber);
//
//	public List<ParticipantTeamAsanas> findByParticipantTeamAndRoundNumber(ParticipantTeam participantTeam,
//			String roundNumber);
//	
//	@Modifying
//	@Query("DELETE ParticipantTeamAsanas a WHERE a.id = ?1")
//	public void deleteById(Integer id);

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeamAsanasParticipantTeam(
			ParticipantTeam participantTeam);

	public ParticipantTeamAsanaScoring save(ParticipantTeamAsanaScoring participantTeamAsanaScoring);

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeamAsanasParticipantTeamAndParticipantTeamAsanasRoundNumberAndJudgeUser(
			ParticipantTeam participantTeam, String roundNumber, Judge judgeUser);

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeamAsanasParticipantTeamAndJudgeUser(
			ParticipantTeam participantTeam, Judge judgeUser);

	public boolean existsByChampionshipParticipantTeamsAndJudgeUser(
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser);

	public List<ParticipantTeamAsanaScoring> findAllByParticipantTeamParticipantsAndChampionshipParticipantTeamsAndJudgeUser(
			ParticipantTeamParticipants participant, ChampionshipParticipantTeams championshipParticipantTeams,
			Judge judgeUser);

	public List<ParticipantTeamAsanaScoring> findAllByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, ParticipantTeamAsanas asana);

	//@Query(value = "SELECT count(*) FROM participant_team_asana_scores where  championship_participant_teams_id = :championshipParticipantTeamId and judge_user_id= :judgeUserId and score is null", nativeQuery = true)
	//public Integer findAllByChampionshipParticipantTeamsAndUser(Integer championshipParticipantTeamId,
	//		Integer judgeUserId);

	@Query(value = "SELECT SUM(score) FROM participant_team_asana_scores where participantteam_participants_id=:participantId and championship_participant_teams_id = :championshipParticipantTeamsId and judge_user_id= :judgeUserId", nativeQuery = true)
	public float getAllAsanaScoresByParticipantTeamParticipantsAndChampionshipParticipantTeamsAndJudgeUser(
			Integer participantId, Integer championshipParticipantTeamsId, Integer judgeUserId);

	@Query(value = "SELECT count(*) FROM participant_team_asana_scores where  championship_participant_teams_id = :championshipParticipantTeamsId and score is null", nativeQuery = true)
	public Integer findAllByChampionshipParticipantTeams(Integer championshipParticipantTeamsId);

	@Query(value = "SELECT count(*) FROM participant_team_asana_scores where  championship_participant_teams_id = :championshipParticipantTeamsId and judge_user_id =:judgeUserId and score is null", nativeQuery = true)
		public Integer findAllByChampionshipParticipantTeamsAndJudgeUser(Integer championshipParticipantTeamsId, Integer judgeUserId);

}
