package com.swayaan.nysf.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.StatusEnum;

@Repository
public interface ChampionshipParticipantTeamsRepository extends PagingAndSortingRepository<ChampionshipParticipantTeams, Integer> {

	List<ChampionshipParticipantTeams> findAllByChampionshipRounds(ChampionshipRounds championshipRound);

	List<ChampionshipParticipantTeams> findByParticipantTeam(ParticipantTeam particpantTeam);

	boolean existsByChampionshipRounds(ChampionshipRounds championshipRound);

	public List<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatus(ChampionshipRounds championshipRounds,
			StatusEnum status);

	@Transactional
	@Modifying(clearAutomatically = true,flushAutomatically = true)
	@Query(value = "UPDATE championship_participant_teams cp SET cp.status = :status WHERE cp.championship_rounds_id = :championshipRoundsId and cp.participant_teams_id = :participantTeamId", nativeQuery = true)
	public void updateParticipantTeamStatus(Integer championshipRoundsId, Integer participantTeamId, String status);

	public ChampionshipParticipantTeams findByParticipantTeamAndChampionshipRounds(ParticipantTeam participantTeam,
			ChampionshipRounds nextChampionshipRounds);

//	@Query(value = "SELECT * from championship_participant_teams WHERE championship_rounds_id = :championshipRoundsId and status IN (:status , :status2)", nativeQuery = true)
//	public List<ChampionshipParticipantTeams> findByChampionshipRoundsAndTwoStatuses(Integer championshipRoundsId,
//			String status, String status2);


	List<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusNotIn(ChampionshipRounds championshipRounds,
			List<StatusEnum> listStatus);
	
//	List<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusNot(ChampionshipRounds championshipRounds,
//		StatusEnum status);

	@Transactional
	@Modifying(clearAutomatically = true,flushAutomatically = true)
	@Query(value = "UPDATE championship_participant_teams cp SET cp.status = :status WHERE cp.id = :chamParticipantTeamId",nativeQuery = true)
	void updateParticipantTeamStatus(Integer chamParticipantTeamId, String status);

	List<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusIn(ChampionshipRounds championshipRounds, List<StatusEnum> listStatus);

	
	// Filters starts here
	@Query(value="Select c  from ChampionshipParticipantTeams c where c.championshipRounds=?1 AND c.status NOT IN (?2) AND c.participantTeam.chestNumber LIKE %?3%")
	Page<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusNotInAndParticipantTeamChestNumber(
			ChampionshipRounds championshipRounds, List<StatusEnum> listStatusNotIn, String chestNumber,
			Pageable pageable);

	@Query(value="Select c from ChampionshipParticipantTeams c where c.championshipRounds=?1 AND c.status NOT IN (?2)")
	Page<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusNotIn(ChampionshipRounds championshipRounds,
			List<StatusEnum> listStatusNotIn, Pageable pageable);

		// Filters ends here 

	//Referee filters starts here 
	Page<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusInAndParticipantTeamChestNumberContaining(
			ChampionshipRounds championshipRounds, List<StatusEnum> status, String chestNumber, Pageable pageable);

	Page<ChampionshipParticipantTeams> findByChampionshipRoundsAndStatusIn(ChampionshipRounds championshipRounds,
			List<StatusEnum> status, Pageable pageable);
	
	//Referee filters ends here 
	
}
