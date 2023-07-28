package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;

@Repository
public interface ParticipantTeamRoundTotalScoringRepository extends CrudRepository<ParticipantTeamRoundTotalScoring, Integer> {

	
	
//	@Query(value="SELECT * FROM participant_team_round_total_scores WHERE championship_id = :championshipId AND asana_category_id = :asanaCategoryId AND gender = :gender AND category = :ageCategory AND  round_number = :roundNumber ORDER BY total_score DESC LIMIT 20", nativeQuery=true)
//	public List<ParticipantTeamRoundTotalScoring> findWinnersForGivenCategories(Integer championshipId,
//			Integer asanaCategoryId, String gender, String ageCategory, String roundNumber);

	

	public Long countById(Integer id);

	public List<ParticipantTeamRoundTotalScoring> findAllByChampionshipAndChampionshipRounds(Championship championship,
			ChampionshipRounds championshipRounds);

	public ParticipantTeamRoundTotalScoring findByChampionshipParticipantTeamsAndChampionshipRounds(
			ChampionshipParticipantTeams championshipParticipantTeam, ChampionshipRounds championshipRounds);

	public List<ParticipantTeamRoundTotalScoring> findByChampionshipAndChampionshipRoundsAndWinnerOrderByRankingAsc(
			Championship championship, ChampionshipRounds championshipRounds, boolean winner);

	public List<ParticipantTeamRoundTotalScoring> findByChampionshipAndChampionshipRounds(Championship championship,
			ChampionshipRounds championshipRounds);

	public boolean existsByChampionshipParticipantTeams(ChampionshipParticipantTeams team);

	public List<ParticipantTeamRoundTotalScoring> findByChampionship(Championship championship);

/*	public ParticipantTeamRoundTotalScoring findByChampionshipAndChampionshipParticipantTeamAndChampionshipRounds(
			Championship championship, ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds); */


}
