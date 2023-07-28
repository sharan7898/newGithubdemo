package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeamAsanaJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.Role;

@Repository
public interface ParticipantTeamAsanaJudgeTotalScoreRepository
		extends CrudRepository<ParticipantTeamAsanaJudgeTotalScore, Integer> {

	public Long countById(Integer id);

	public Optional<ParticipantTeamAsanaJudgeTotalScore> findById(Long id);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantTeamAsana,
			ParticipantTeamParticipants participantTeamParticipant, Integer round);

	@Query(value = "Select SUM(total_score) FROM participant_team_asana_judge_total_scores where "
			+ "championship_participant_teams_id = :championshipParticipantTeamsId and "
			+ "participantteam_participants_id = :participantTeamParticipantId and "
			+ "round =:round", nativeQuery = true)
	public Float findSumByTeamAndParticipantAndRound(Integer championshipParticipantTeamsId,
			Integer participantTeamParticipantId, Integer round);

	@Query(value = "Select SUM(total_score) FROM participant_team_asana_judge_total_scores where "
			+ "championship_participant_teams_id = :championshipParticipantTeamsId and "
			+ "participantteam_participants_id = :participantTeamParticipantId and " + "round =:round and "
			+ "judge_type_id =:judgeTypeId", nativeQuery = true)
	public Float findSumByTeamAndParticipantAndRoundAndType(Integer championshipParticipantTeamsId,
			Integer participantTeamParticipantId, Integer round, Integer judgeTypeId);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantTeamAsana,
			ParticipantTeamParticipants participantTeamParticipant, Integer round, JudgeType judgeType);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, JudgeType judgeType);

	public List<ParticipantTeamAsanaJudgeTotalScore> findAllByChampionshipParticipantTeamsAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, JudgeType judgeType);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeamAsanas participantTeamAsana,
			Integer round, JudgeType judgeType);

	@Query(value = "Select SUM(total_score) FROM participant_team_asana_judge_total_scores where "
			+ "championship_participant_teams_id = :championshipParticipantTeamsId and " + "round =:round and "
			+ "judge_type_id =:judgeTypeId", nativeQuery = true)
	public Float findSumByTeamAndRoundAndType(Integer championshipParticipantTeamsId, Integer round,
			Integer judgeTypeId);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndSequenceNumberAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer i, Integer round, JudgeType judgeType);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndSequenceNumberAndParticipantTeamParticipantsAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams, Integer asanaSeqNum,
			ParticipantTeamParticipants tradTeamParticipant, Integer round, JudgeType judgeType);

	public ParticipantTeamAsanaJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, ParticipantTeamAsanas asana, JudgeType judgeType);

	public List<ParticipantTeamAsanaJudgeTotalScore> findAllByChampionshipParticipantTeamsAndParticipantTeamAsanasCompulsoryTrue(
			ChampionshipParticipantTeams championshipParticipantTeams);
}
