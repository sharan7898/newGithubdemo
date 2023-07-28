package com.swayaan.nysf.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.User;

@Repository
public interface ChampionshipRepository extends PagingAndSortingRepository<Championship, Integer> {

	// public List<Championship> findAllByOrderByNameAsc();

	public Long countById(Integer id);

	public Championship findByName(String name);

	public Boolean existsByName(String name);

//	@Query(value="SELECT * FROM Championship WHERE startDate () ORDER BY name ASC", nativeQuery=true)
	public List<Championship> findByStatusInAndStartDateGreaterThanEqual(List<ChampionshipStatusEnum> status,
			Date startDate);

	// public List<Championship> findAllByCreatedBy(User currentUser);
	public List<Championship> findAllByCreatedByAndStatusIn(User currentUser, List<ChampionshipStatusEnum> status);

	public List<Championship> findAllByCreatedByAndStatusNot(User currentUser, ChampionshipStatusEnum status);

	public List<Championship> findByStatusIn(List<ChampionshipStatusEnum> listStatusIn);

	public List<Championship> findByLevel(ChampionshipLevels championshipLevels);

	public List<Championship> findByStatusNot(ChampionshipStatusEnum status);

	public List<Championship> findByStatusNotIn(List<ChampionshipStatusEnum> status);

	@Query(value = "SELECT * FROM championship WHERE created_by = :eventManagerId ", nativeQuery = true)
	public List<Championship> findByEventManager(Integer eventManagerId);

	public Championship findByNameAndLevelAndStartDateAndEndDateAndLocationAndCreatedBy(String name,
			ChampionshipLevels level, Date startDate, Date endDate, String location, User createdBy);

	// Admin filters starts here
	public Page<Championship> findAllByNameContainingAndLocationContainingAndStatusNotIn(String name, String location,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	public Page<Championship> findAllByLocationContainingAndStatusNotIn(String location,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	public Page<Championship> findAllByNameContainingAndStatusNotIn(String name, List<ChampionshipStatusEnum> status,
			Pageable pageable);

	public Page<Championship> findAllByStatusNotIn(List<ChampionshipStatusEnum> status, Pageable pageable);

//event manager filters

	public Page<Championship> findAllByNameContainingAndLocationContainingAndCreatedByAndStatusIn(String name,
			String location, User currentUser, List<ChampionshipStatusEnum> listStatusIn, Pageable pageable);

	public Page<Championship> findAllByLocationContainingAndCreatedByAndStatusIn(String location, User currentUser,
			List<ChampionshipStatusEnum> listStatusIn, Pageable pageable);

	public Page<Championship> findAllByNameContainingAndCreatedByAndStatusIn(String name, User currentUser,
			List<ChampionshipStatusEnum> listStatusIn, Pageable pageable);

	public Page<Championship> findAllByCreatedByAndStatusIn(User currentUser, List<ChampionshipStatusEnum> listStatusIn,
			Pageable pageable);

	// Admin filters ends here

	// participantUpcoming championship Filters start here

	public Page<Championship> findAllByNameContainingAndLocationContainingAndStatusInAndStartDateGreaterThanEqual(
			String name, String location, List<ChampionshipStatusEnum> listStatusIn, Date startDate, Pageable pageable);

	public Page<Championship> findAllByLocationContainingAndStatusInAndStartDateGreaterThanEqual(String location,
			List<ChampionshipStatusEnum> listStatusIn, Date startDate, Pageable pageable);

	public Page<Championship> findAllByNameContainingAndStatusInAndStartDateGreaterThanEqual(String name,
			List<ChampionshipStatusEnum> listStatusIn, Date startDate, Pageable pageable);

	public Page<Championship> findByStatusInAndStartDateGreaterThanEqual(List<ChampionshipStatusEnum> listStatusIn,
			Date startDate, Pageable pageable);

//filters participant Upcoming ends here

	// --------Participant enrolled championships filters start here.-------->

	@Query(value = "select DISTINCT c from Championship c where c.name LIKE %:name% AND c.location LIKE %:location% And c IN"
			+ " ( select p.championship from ParticipantTeam p where p IN "
			+ "(select ptp.participantTeam from ParticipantTeamParticipants ptp where ptp.participant.id =:currentUser)) ORDER BY c.startDate ")

	public Page<Championship> findAllByNameContainingAndLocationContaining(String name, String location,
			Integer currentUser, Pageable pageable);

	@Query(value = "select  DISTINCT  c from Championship c where c.location LIKE %:location% AND  c IN"
			+ " ( select p.championship from ParticipantTeam p where p IN "
			+ "(select ptp.participantTeam from ParticipantTeamParticipants ptp where ptp.participant.id =:currentUser))  ")
	public Page<Championship> findAllByLocationContaining(String location, Integer currentUser, Pageable pageable);

	@Query(value = "select  DISTINCT  c from Championship c where c.name LIKE %:name% AND c IN"
			+ " ( select p.championship from ParticipantTeam p where p IN "
			+ "(select ptp.participantTeam from ParticipantTeamParticipants ptp where ptp.participant.id =:currentUser)) ")
	public Page<Championship> findAllByNameContaining(String name, Integer currentUser, Pageable pageable);

	@Query(value = "select  DISTINCT  c from Championship c where  c IN"
			+ " ( select p.championship from ParticipantTeam p where p IN "
			+ "(select ptp.participantTeam from ParticipantTeamParticipants ptp where ptp.participant.id =:currentUser)) ")
	public Page<Championship> listAllParticipantEnrolledChampionships(Integer currentUser, Pageable pageable);

	@Query(value = "select * from championship c LEFT JOIN championship_category cc on cc.championship_id=c.id where cc.id=:championshipCategoryId", nativeQuery = true)
	public Championship findBychampionshipCategory(Integer championshipCategoryId);

	public boolean existsByIdAndStatusNotIn(Integer id, List<ChampionshipStatusEnum> status);

//filters participant enrolled championships ends here

	// filters for scorer starts here

	@Query("SELECT c from Championship c where c.status NOT IN (:status) and c.name LIKE %:name% and c.location LIKE %:location% and c IN (select crp.championship from ChampionshipRefereePanels crp where"
			+ " crp IN (select rp.championshipRefereePanels from RefereesPanels rp where judgeUser.id=:currentUserId))")
	public Page<Championship> findAllByNameContainingAndLocationContainingAndStatusNotIn(
			List<ChampionshipStatusEnum> status, String name, String location, Integer currentUserId,
			Pageable pageable);

	@Query("SELECT c from Championship c where c.status NOT IN (:status) and c.location LIKE %:location% and c IN (select crp.championship from ChampionshipRefereePanels crp where"
			+ " crp IN (select rp.championshipRefereePanels from RefereesPanels rp where judgeUser.id=:currentUserId))")
	public Page<Championship> findAllByLocationContainingAndStatusNotIn(List<ChampionshipStatusEnum> status,
			String location, Integer currentUserId, Pageable pageable);

	@Query("SELECT c from Championship c where c.status NOT IN (:status) and c.name LIKE %:name% and c IN (select crp.championship from ChampionshipRefereePanels crp where"
			+ " crp IN (select rp.championshipRefereePanels from RefereesPanels rp where judgeUser.id=:currentUserId))")
	public Page<Championship> findAllByNameContainingAndStatusNotIn(List<ChampionshipStatusEnum> status, String name,
			Integer currentUserId, Pageable pageable);

	@Query("SELECT c from Championship c where c.status NOT IN (:status) and c IN (select crp.championship from ChampionshipRefereePanels crp where"
			+ " crp IN (select rp.championshipRefereePanels from RefereesPanels rp where judgeUser.id=:currentUserId))")
	public Page<Championship> findAllByStatusNotIn(List<ChampionshipStatusEnum> status, Integer currentUserId,
			Pageable pageable);

	@Query("SELECT c from Championship c where c.status NOT IN (:status) and (c.image1 is not null or c.image2 is not null  or c.image3 is not null or c.image4 is not null )")

	public List<Championship> findByStatusNotInAndImage1NotNullOrImage2NotNullOrImage3NotNullOrImage4NotNull(
			List<ChampionshipStatusEnum> status);

	public List<Championship> findByStatusNotInAndImage1NullAndImage2NullAndImage3NullAndImage4Null(
			List<ChampionshipStatusEnum> status);

	public Page<Championship> findByStatusNotInAndImage1NotNullOrImage2NotNullOrImage3NotNullOrImage4NotNull(
			List<ChampionshipStatusEnum> status, Pageable pageable);

	public Page<Championship> findByStatusNotInAndNameContainingAndImage1NotNullOrImage2NotNullOrImage3NotNullOrImage4NotNull(
			List<ChampionshipStatusEnum> status, String name, Pageable pageable);

	public Page<Championship> findByStatusNotInAndNameContainingAndId(List<ChampionshipStatusEnum> status, String name,
			Integer championshipId, Pageable pageable);

	public Page<Championship> findByStatusNotInAndId(List<ChampionshipStatusEnum> status, Integer championshipId,
			Pageable pageable);

	// filters for scorer ends here

	@Query(value = "SELECT c from Championship c where c.status IN (:listStatus) AND c IN (SELECT pt.championship from ParticipantTeam pt where pt IN (SELECT ptp.participantTeam from ParticipantTeamParticipants ptp where ptp.participant.id =:currentUser))")
	public List<Championship> findAllChampionshipsForCurrentUser(Integer currentUser,
			List<ChampionshipStatusEnum> listStatus);

}
