package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;

@Repository
public interface ParticipantTeamAsanasRepository extends CrudRepository<ParticipantTeamAsanas, Integer> {
	
	public Long countById(Integer id);

	public Optional<ParticipantTeamAsanas> findById(Long id);

	public List<ParticipantTeamAsanas> findByAsana(Asana asana);

	public ParticipantTeamAsanas findByAsanaAndParticipantTeam(Asana asana, ParticipantTeam participantTeam);
	
	public List<ParticipantTeamAsanas> findAllByAsanaAndParticipantTeam(Asana asana, ParticipantTeam participantTeam);
	
	public List<ParticipantTeamAsanas> findAllByAsanaAndParticipantTeamAndRoundNumber(Asana asana, ParticipantTeam participantTeam,Integer roundNumber);

		public List<ParticipantTeamAsanas> findByParticipantTeamAndRoundNumber(ParticipantTeam participantTeam,
			Integer roundNumber);
	
	@Modifying
	@Query("DELETE ParticipantTeamAsanas a WHERE a.id = ?1")
	public void deleteById(Integer id);

	public List<ParticipantTeamAsanas> findByParticipantTeam(ParticipantTeam participantTeam);

	public List<ParticipantTeamAsanas> findByParticipantTeamAndRoundNumberOrderBySequenceNumberAsc(
			ParticipantTeam participantTeam, Integer roundNumber);

	public List<ParticipantTeamAsanas> findByParticipantTeamAndAndParticipantTeamParticipantsAndRoundNumberOrderBySequenceNumberAsc(
			ParticipantTeam participantTeam, ParticipantTeamParticipants participantTeamParticipants, Integer round);
	
	
	@Query(value="SELECT * FROM participantteam_asanas WHERE participantteam_id = :participantTeamId and round_number = :roundNumber GROUP BY asana_id ORDER BY sequence_number ASC" , nativeQuery=true)
	public List<ParticipantTeamAsanas> findDistinctByAsanaAndParticipantTeamAndRoundNumberOrderBySequenceNumberAsc(
			Integer participantTeamId, Integer roundNumber);
	
	
	@Query(value="SELECT * FROM participantteam_asanas WHERE participantteam_id = :participantTeamId GROUP BY asana_id ORDER BY sequence_number ASC" , nativeQuery=true)
	public List<ParticipantTeamAsanas> findDistinctByAsanaAndParticipantTeamOrderBySequenceNumberAsc(
			Integer participantTeamId);
	
	public List<ParticipantTeamAsanas> findByParticipantTeamAndParticipantTeamParticipantsAndRoundNumberOrderBySequenceNumberAsc(ParticipantTeam participantTeam, ParticipantTeamParticipants participantTeamParticipants,Integer roundNumber);

	@Query(value="SELECT * FROM participantteam_asanas WHERE participantteam_id = :participantTeam_id and participant_team_participants_id = :participantTeamParticipants_id and compulsory = :compulsory and round_number = :round ORDER BY id ASC", nativeQuery=true)
	public List<ParticipantTeamAsanas> findByParticipantTeamAndParticipantTeamParticipantsAndRound(Integer participantTeam_id, Integer participantTeamParticipants_id, boolean compulsory, Integer round);
	
	
	@Query(value="SELECT * FROM participantteam_asanas WHERE participantteam_id = :participantTeam_id and participant_team_participants_id = :participantTeamParticipants_id", nativeQuery=true)
	public List<ParticipantTeamAsanas> findByParticipantTeamAndParticipantTeamParticipants(Integer participantTeam_id, Integer participantTeamParticipants_id);
	
	
	@Transactional
	@Query("UPDATE ParticipantTeamAsanas p SET p.sequenceNumber = :seqNum WHERE p.id = :id")
	@Modifying
	public void updateSequenceNumber(Integer id, Integer seqNum);

	public List<ParticipantTeamAsanas> findAllByParticipantTeamParticipantsAndRoundNumberAndCompulsoryAsanaId(
			ParticipantTeamParticipants participantTeamParticipants, Integer roundNumber, Integer compulsoryId);

	public Boolean existsBycompulsoryAsanaIdAndParticipantTeam(Integer compulsoryId,ParticipantTeam participantTeam);

	public List<ParticipantTeamAsanas> findByCompulsoryAsanaId(Integer compulsoryAsanaId);

	public List<ParticipantTeamAsanas> findAllByParticipantTeamAndRoundNumberAndCompulsory(
			ParticipantTeam participantTeam, Integer round, boolean isCompulsory);

	public boolean existsByparticipantTeam(ParticipantTeam participantTeams);
}
