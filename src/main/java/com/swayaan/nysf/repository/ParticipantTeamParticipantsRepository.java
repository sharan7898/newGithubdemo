package com.swayaan.nysf.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;

@Repository
public interface ParticipantTeamParticipantsRepository extends CrudRepository<ParticipantTeamParticipants, Integer> {

	public Long countById(Integer id);

	public Optional<ParticipantTeamParticipants> findById(Long id);

	public List<ParticipantTeamParticipants> findByParticipant(Participant participant);

	public ParticipantTeamParticipants findByParticipantAndParticipantTeam(Participant participant,
			ParticipantTeam participantTeam);

	@Modifying
	@Query("DELETE ParticipantTeamParticipants p WHERE p.id = ?1")
	public void deleteById(Integer id);

	public List<ParticipantTeamParticipants> findByParticipantTeam(ParticipantTeam participantTeam);

	@Transactional
	@Query("UPDATE ParticipantTeamParticipants p SET p.sequenceNumber = :seqNum WHERE p.id = :id")
	@Modifying
	public void updateSequenceNumber(Integer id, Integer seqNum);

	public List<ParticipantTeamParticipants> findByParticipantTeamOrderBySequenceNumberAsc(
			ParticipantTeam participantTeam);

	public List<ParticipantTeamParticipants> findAllByParticipantAndParticipantTeam(Participant participant,
			ParticipantTeam participantTeam);

	@Query("SELECT p FROM ParticipantTeamParticipants p WHERE p.participantTeam.id = :participantTeamId")
	public ParticipantTeamParticipants findOneByParticipantTeam(Integer participantTeamId);

	public ParticipantTeamParticipants findByParticipantTeamAndIsTeamLead(ParticipantTeam participantTeam,
			Boolean teamLead);

	@Transactional
	@Query("UPDATE ParticipantTeamParticipants p SET p.isTeamLead=false WHERE p.participantTeam.id = :participantTeam and p.id!=:id")
	@Modifying
	public void updateOtherParticipantsTeamLeadStatusToFalse(Integer participantTeam, Integer id);
	
	@Transactional
	@Query("UPDATE ParticipantTeamParticipants p SET p.isTeamLead=true WHERE p.participantTeam.id = :participantTeam and p.participant.id=:id")
	@Modifying
	public void updateParticipantsTeamLeadStatus(Integer participantTeam, Integer id);

	public List<ParticipantTeamParticipants> findByParticipantTeamChampionship(Championship championship);

	public Boolean existsByParticipantTeamChampionshipAndParticipant(Championship championship,
			Participant participant);

	@Query("select p.participantTeam from ParticipantTeamParticipants p where p.participant=:currentParticipant and p.participantTeam.championship=:championship ")
	public List<ParticipantTeam> findAllParticipantTeamByParticipantAndChampionship(Participant currentParticipant,Championship championship);

	@Query("select p.participantTeam from ParticipantTeamParticipants p where p.participant=:currentParticipant and p.participantTeam IN (select pt from ParticipantTeam pt where pt.asanaCategory=:asanaCategory AND pt.category=:ageCategory AND pt.gender=:gender AND pt.championship=:championship) ")
	public ParticipantTeam findByParticipantAndChampionshipAndParticipantTeamCategories(Participant currentParticipant,
			Championship championship, AsanaCategory asanaCategory, AgeCategory ageCategory, GenderEnum gender);


}
