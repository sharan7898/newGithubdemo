package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeamAsanaJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.Role;


@Repository
public interface ParticipantTeamJudgeTotalScoreRepository extends CrudRepository<ParticipantTeamJudgeTotalScore, Integer> {
	
	public Long countById(Integer id);

	public Optional<ParticipantTeamJudgeTotalScore> findById(Long id);

	public ParticipantTeamJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndRound(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, Integer round);

	@Query(value="Select SUM(total_score) FROM participant_team_judge_total_scores where "
			+ "championship_participant_teams_id = :championshipParticipantTeamsId and "
			+ "round =:round",nativeQuery=true)
	public ParticipantTeamJudgeTotalScore findByChampionshipParticipantTeamsAndRoundAggregate(Integer championshipParticipantTeamsId, Integer round);

	public List<ParticipantTeamJudgeTotalScore> findByChampionshipParticipantTeamsAndRound(ChampionshipParticipantTeams championshipParticipantTeams, Integer round);

	public ParticipantTeamJudgeTotalScore findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndRoundAndType(
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipant, Integer round, JudgeType judgeType);

	public ParticipantTeamJudgeTotalScore findByChampionshipParticipantTeamsAndRoundAndType(ChampionshipParticipantTeams championshipParticipantTeams, Integer round,
			JudgeType judgeType);

	public ParticipantTeamJudgeTotalScore findByChampionshipParticipantTeamsAndType(
			ChampionshipParticipantTeams team, JudgeType judgeType);

	
}
