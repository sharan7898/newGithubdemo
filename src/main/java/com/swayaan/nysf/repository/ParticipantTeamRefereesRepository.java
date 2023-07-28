package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.User;

@Repository
public interface ParticipantTeamRefereesRepository extends CrudRepository<ParticipantTeamReferees, Integer> {

	public Long countById(Integer id);

	public Optional<ParticipantTeamReferees> findById(Long id);

	@Query(value = "SELECT ROUND(SUM(score),2) FROM participantteam_referees WHERE participantteam_id = :team_id", nativeQuery = true)
	public float getTeamTotalScore(@Param("team_id") Integer team_id);

	public List<ParticipantTeamReferees> findByJudgeUser(Judge judgeUser);

	public ParticipantTeamReferees findByJudgeUserAndParticipantTeam(Judge judgeUser, ParticipantTeam participantTeam);

	public List<ParticipantTeamReferees> findByParticipantTeam(ParticipantTeam participantTeam);

//	@Query(value = "SELECT pgr.id, pgr.participantteam_id, pgr.user_referee_id, pgr.score, u.referee_type FROM participantteam_referees pgr JOIN users u ON u.id = pgr.user_referee_id WHERE u.referee_type = :refereeType AND pgr.participantteam_id = :participantTeamId ", nativeQuery = true)
//	public List<ParticipantTeamReferees> findByParticipantTeamAndRefereeType(Integer participantTeamId,
//			String refereeType);
//	
	public void deleteByParticipantTeam(ParticipantTeam participantTeam);

	public ParticipantTeamReferees findByParticipantTeamAndJudgeUser(ParticipantTeam participantTeam, Judge judgeUser);

//	@Query(value = "DELETE p FROM participantteam_referees WHERE participantteam_id = :participantTeamId AND user_referee_id=:userRefereeId", nativeQuery = true)
//	public void deleteByParticpantTeamAndUser(Integer participantTeamId, Integer userRefereeId);

	@Query(value = "SELECT * FROM participantteam_referees WHERE participantteam_id = :participantTeamId AND judge_user_referee_id=:userRefereeId", nativeQuery = true)
	public Integer findByParticpantTeamAndJudgeUser(Integer participantTeamId, Integer userRefereeId);

	public List<ParticipantTeamReferees> findByParticipantTeamAndRoundNumber(ParticipantTeam participantTeam,
			Integer round);

	public List<ParticipantTeamReferees> findDistinctByRoundNumberAndChampionshipAndChampionshipCategory(Integer round,
			Championship championship, ChampionshipCategory championshipCategory);
	
	public List<ParticipantTeamReferees> findDistinctByRoundNumberAndChampionshipAndChampionshipCategoryAndParticipantTeam(Integer round,
			Championship championship, ChampionshipCategory championshipCategory,ParticipantTeam participantTeam);

	public ParticipantTeamReferees findByParticipantTeamAndJudgeUserAndRoundNumber(ParticipantTeam participantTeam, Judge judgeUser,
			Integer round);

	public List<ParticipantTeamReferees> findByRoundNumberAndChampionshipAndChampionshipCategoryAndParticipantTeam(
			Integer round, Championship championship, ChampionshipCategory championshipCategory,
			ParticipantTeam participantTeam);

	public List<ParticipantTeamReferees> findAllByChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber(
			Championship championship, ChampionshipCategory championshipCategory, ParticipantTeam participantTeam,
			Integer round);

	public ParticipantTeamReferees findByParticipantTeamAndRoundNumberAndChampionshipAndChampionshipCategoryAndJudgeUser(
			ParticipantTeam participantTeam, Integer round, Championship championship,
			ChampionshipCategory championshipCategory, Judge judgeUser);

	public List<ParticipantTeamReferees> findAllByParticipantTeamAndTypeAndRoundNumber(ParticipantTeam participantTeam,
			JudgeType judgeType, Integer round_number);
	

	public ParticipantTeamReferees findByParticipantTeamAndTypeAndRoundNumber(ParticipantTeam participantTeam,
			JudgeType judgeType, Integer round_number);



	public List<ParticipantTeamReferees> findAllByChampionship(Championship championship);

	public List<ParticipantTeamReferees> findByRoundNumberAndChampionshipAndChampionshipCategoryAndJudgeUser(Integer round,
			Championship championship, ChampionshipCategory championshipCategory, Judge judgeUser);

	public boolean existsByparticipantTeam(ParticipantTeam participantTeams);


	

}
