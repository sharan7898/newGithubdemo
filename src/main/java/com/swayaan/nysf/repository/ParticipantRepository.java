package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.RegistrationStatusEnum;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Integer> {

	public List<Participant> findAllByOrderByFirstNameAsc();

	public Long countById(Integer id);

	// public List<Participant> findByCategory(String participantCategory);

//	@Query(value="SELECT * FROM participants  WHERE email = :email ",nativeQuery=true)
//	public Participant getParticipantByEmail(@Param("email") String email);

	public Participant getParticipantByEmail(@Param("email") String email);

//	@Query(value="SELECT * FROM participants WHERE id NOT IN (SELECT participant_id FROM event_participants WHERE event_id =:event_id) ORDER BY first_name ASC", nativeQuery=true)
//	public List<Participant> findEventUnassignedParticipants(Integer event_id);

	public List<Participant> findAllByGender(String gender);

//	 @Query(value="SELECT * FROM participants LEFT JOIN users on participants.id=users.id WHERE gender=:gender and participants.id NOT IN(SELECT participant_id FROM participantteam_participants WHERE participantteam_id =:participantteamId)"
//	 		+ "and participantteams.id NOT IN(SELECT age_category_id FROM participantteam_participants WHERE asana_category_id =:asanaCategoryId and age_category_id =:ageCategoryId ) ORDER BY first_name ASC", nativeQuery=true)
//	public List<Participant> findAllParticipantsForGenderAndNotInParticipantTeam(String gender,
//			Integer participantteamId, Integer asanaCategoryId, Integer ageCategoryId);

//	@Query(value = "select * from participants left join users on participants.id=users.id where users.gender=:gender and participants.id not in (select participantteam_participants.participant_id from participantteam_participants where participant_team.status NOT IN (:status) and participantteam_id in \r\n"
//			+ "(select id from participant_teams where age_category_id=:ageCategoryId and asana_category_id=:asanaCategoryId and championship_id=:championshipId ))", nativeQuery = true)

	@Query(value = "select p from Participant p where p.gender=:gender and p not in \r\n"
			+ "(select ptp.participant from ParticipantTeamParticipants ptp where ptp.participantTeam.status NOT IN \r\n"
			+ "(:status) and ptp.participantTeam.id in (select pt.id from ParticipantTeam pt where pt.category.id=:ageCategoryId and \r\n"
			+ "pt.asanaCategory.id=:asanaCategoryId and pt.championship.id=:championshipId ))")

	public List<Participant> findAllParticipantsForGenderAndNotInParticipantTeamAndStatus(String gender,
			Integer asanaCategoryId, Integer championshipId, Integer ageCategoryId,
			List<RegistrationStatusEnum> status);

//	@Query(value = "select * from participants left join users on participants.id=users.id where users.gender=:gender and participants.id not in (select participantteam_participants.participant_id from participantteam_participants where participant_team.status NOT IN (:status) and participantteam_id in \r\n"
//			+ "(select id from participant_teams where age_category_id=:ageCategoryId and asana_category_id=:asanaCategoryId and championship_id=:championshipId ))", nativeQuery = true)
	@Query(value = "select p from Participant p where p.gender=:gender and p not in \r\n"
			+ "(select ptp.participant from ParticipantTeamParticipants ptp where ptp.participantTeam.status NOT IN \r\n"
			+ "(:status) and ptp.participantTeam.id in (select pt.id from ParticipantTeam pt where pt.category.id=:ageCategoryId and \r\n"
			+ "pt.asanaCategory.id=:asanaCategoryId and pt.championship.id=:championshipId ))")
	public List<Participant> findAllParticipantsForCommonGenderAndNotInParticipantTeamAndStatus(Integer asanaCategoryId,
			Integer championshipId, Integer ageCategoryId, List<RegistrationStatusEnum> status);

	@Query(value = "SELECT CONCAT(first_name , ' ', last_name)   FROM participants LEFT JOIN users  on participants.id=users.id WHERE participants.id= :participantId", nativeQuery = true)
	public List<String> findAllByParticipant(Integer participantId);

	public Participant findByUserName(String userName);

	// participants in admin

	@Query(value = "select j from Participant j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% and j.prnNumber LIKE %?2%  ")
	public Page<Participant> findAllByNameContainingAndPrnNumberContaining(String name, String prnNumber,
			Pageable pageable);

	public Page<Participant> findAllByPrnNumberContaining(String prnNumber, Pageable pageable);

	@Query(value = "select j from Participant j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% ")
	public Page<Participant> findAllByNameContaining(String name, Pageable pageable);

	public Page<Participant> findAll(Pageable pageable);
	// event manager championship participant fliters

	@Query(value = "SELECT p FROM Participant p where p IN (select ptp.participant from ParticipantTeamParticipants ptp where CONCAT(ptp.participant.email, ' ', ptp.participant.firstName, ' ',ptp.participant.lastName)  LIKE %:name% and ptp.participant.prnNumber LIKE %:prnNumber% and ptp.participantTeam In (select pt from ParticipantTeam pt where pt.championship=:championship))")
	public Page<Participant> findAllByNameContainingAndPrnNumberContainingAndChampionship(String name, String prnNumber,
			Championship championship, Pageable pageable);

	@Query(value = "SELECT p FROM Participant p where p IN (select ptp.participant from ParticipantTeamParticipants ptp where ptp.participant.prnNumber LIKE %:prnNumber% and ptp.participantTeam In (select pt from ParticipantTeam pt where pt.championship=:championship))")
	public Page<Participant> findAllByPrnNumberContainingAndChampionship(String prnNumber, Championship championship,
			Pageable pageable);

	@Query(value = "SELECT p FROM Participant p where p IN (select ptp.participant from ParticipantTeamParticipants ptp where CONCAT(ptp.participant.email, ' ', ptp.participant.firstName, ' ',ptp.participant.lastName)  LIKE %:name% and ptp.participantTeam In (select pt from ParticipantTeam pt where pt.championship=:championship)) ")
	public Page<Participant> findAllByNameContainingAndChampionship(String name, Championship championship,
			Pageable pageable);

	@Query(value = "SELECT p FROM Participant p where p IN (select ptp.participant from ParticipantTeamParticipants ptp where ptp.participantTeam In (select pt from ParticipantTeam pt where pt.championship=:championship)) ")
	public Page<Participant> findAllByChampionship(Championship championship, Pageable pageable);

	public boolean existsByEmail(String email);

	@Query("UPDATE Participant u SET u.enabled = :enabled, u.verificationCode=null WHERE u.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

}
