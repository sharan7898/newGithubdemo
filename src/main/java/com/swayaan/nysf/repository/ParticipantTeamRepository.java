package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;

@Repository
public interface ParticipantTeamRepository extends PagingAndSortingRepository<ParticipantTeam, Integer> {

	public List<ParticipantTeam> findAllByOrderByNameAsc();

	public Long countById(Integer id);

	@Query(value = "SELECT * FROM participant_teams WHERE asana_category_id = :asana_category_id and age_category_id = :age_category_id and gender = :gender ORDER BY id DESC LIMIT 1", nativeQuery = true)
	public ParticipantTeam findByAsanaCategoryAndCategoryAndGenderOrderByIdDesc(
			@Param("asana_category_id") Integer asana_category_id, @Param("age_category_id") Integer age_category_id,
			@Param("gender") String gender);

//	@Query(value="SELECT * FROM participant_teams WHERE championship_id = :championship_id ORDER BY id ASC", nativeQuery=true)
//	public List<ParticipantTeam> findByChampionship(Championship championship);

	@Query(value = "SELECT p FROM ParticipantTeam p WHERE p.championship.id = :championshipId and p.asanaCategory.id = :asanaCategoryId and p.gender = :gender and p.category.id = :ageCategoryId and p.status IN (:status) ORDER BY id ASC")
	public List<ParticipantTeam> findAllByParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategoryAndStatus(
			Integer championshipId, Integer asanaCategoryId, GenderEnum gender, Integer ageCategoryId,
			List<RegistrationStatusEnum> status);

	public List<ParticipantTeam> findByChampionship(Championship championship);

	public List<ParticipantTeam> findByAsanaCategoryAndStatusIn(AsanaCategory asanaCategory,
			List<RegistrationStatusEnum> status);

	public List<ParticipantTeam> findAllByStatus(StatusEnum status);

	public Boolean existsByChestNumberAndChampionshipAndStatusIn(String chestNumber, Championship championship,
			List<RegistrationStatusEnum> status);

	public List<ParticipantTeam> findByCategoryAndStatusIn(AgeCategory ageCategory,
			List<RegistrationStatusEnum> status);

	public Boolean existsByChestNumberAndChampionshipAndIdNot(String chestNumber, Championship championship,
			Integer id);

	// Filters starts here

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.chestNumber LIKE %?2% AND p.championship.name LIKE %?3% AND p.asanaCategory.name LIKE %?4% AND p.championship.status NOT IN (?5) AND p.status IN (?6)")
	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String name, String chestNumber, String championship, String asanaCategory,
			List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.chestNumber LIKE %?2% AND p.championship.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.status IN (?5)")
	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
			String name, String chestNumber, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.chestNumber LIKE %?2% AND p.asanaCategory.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.status IN (?5)")
	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String name, String chestNumber, String asanaCategory, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.chestNumber LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4) ")
	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndChampionshipStatusNotInAndStatus(
			String name, String chestNumber, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.championship.name LIKE %?2% AND p.asanaCategory.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.status IN (?5)")
	public Page<ParticipantTeam> findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String name, String championshipName, String asanaCategory, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4) ")
	public Page<ParticipantTeam> findAllByNameContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
			String name, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.asanaCategory.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4)")
	public Page<ParticipantTeam> findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String name, String asanaCategory, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.chestNumber LIKE %?1% AND p.championship.name LIKE %?2% AND p.asanaCategory.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.status IN (?5) ")
	public Page<ParticipantTeam> findAllByChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String chestNumber, String championshipName, String asanaCategory, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.chestNumber LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4)")
	public Page<ParticipantTeam> findAllByChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
			String chestNumber, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.chestNumber LIKE %?1% AND p.asanaCategory.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4)")
	public Page<ParticipantTeam> findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String chestNumber, String asanaCategory, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.championship.name LIKE %?1% AND p.asanaCategory.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4)")
	public Page<ParticipantTeam> findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String championshipName, String asanaCategory, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1%  AND p.championship.status NOT IN (?2) AND p.status IN (?3)")
	public Page<ParticipantTeam> findAllByNameContainingAndChampionshipStatusNotInAndStatus(String name,
			List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.chestNumber LIKE %?1% AND p.championship.status NOT IN (?2) AND p.status IN (?3)")
	public Page<ParticipantTeam> findAllByChestNumberContainingAndChampionshipStatusNotInAndStatus(String chestNumber,
			List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.championship.name LIKE %?1% AND p.championship.status NOT IN (?2) AND p.status IN (?3)")
	public Page<ParticipantTeam> findAllByChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
			String championshipName, List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus,
			Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.asanaCategory.name LIKE %?1% AND p.championship.status NOT IN (?2) AND p.status IN (?3) ")
	public Page<ParticipantTeam> findAllByAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
			String asanaCategory, List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus,
			Pageable pageable);

	public Page<ParticipantTeam> findAllByChampionshipStatusNotInAndStatusIn(List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Pageable pageable);

//event manager all teams filters
	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.chestNumber LIKE %?2% AND  p.asanaCategory.name LIKE %?3% AND p.championship.name LIKE %?4% AND p.championship.status NOT IN (?5) AND p.status IN (?6) AND p.championship.id =?7 ")
	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String name, String chestNumber, String asanaCategory, String championshipName,
			List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus, Integer championshipId,
			Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1% AND p.chestNumber LIKE %?2%  AND p.championship.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.status IN (?5) AND p.championship.id =?6")
	public Page<ParticipantTeam> findAllByNameContainingAndChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String name, String chestNumber, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1%  AND  p.asanaCategory.name LIKE %?2% AND p.championship.name LIKE %?3% AND p.championship.status NOT IN (?4)  AND p.status IN (?5) AND p.championship.id =?6 ")
	public Page<ParticipantTeam> findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String name, String asanaCategory, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where p.name LIKE %?1%  AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4)")
	public Page<ParticipantTeam> findAllByNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String name, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where  p.chestNumber LIKE %?1% AND  p.asanaCategory.name LIKE %?2% AND p.championship.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.status IN (?5) AND p.championship.id =?6 ")
	public Page<ParticipantTeam> findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String chestNumber, String asanaCategory, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where  p.chestNumber LIKE %?1%  AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4) AND p.championship.id =?5")
	public Page<ParticipantTeam> findAllByChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String chestNumber, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where   p.asanaCategory.name LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.status IN (?4) AND p.championship.id =?5")
	public Page<ParticipantTeam> findAllByAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String asanaCategory, String championshipName, List<ChampionshipStatusEnum> status,
			List<RegistrationStatusEnum> teamStatus, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where  p.championship.name LIKE %?1% AND p.championship.status NOT IN (?2) AND p.status IN (?3) AND p.championship.id =?4")
	public Page<ParticipantTeam> findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String championshipName, List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus,
			Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ParticipantTeam p where  p.championship.name LIKE %?1% AND p.championship.status NOT IN (?2) AND p.status IN (?3) AND p.championship.id =?4")
	public List<ParticipantTeam> findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId(
			String championshipName, List<ChampionshipStatusEnum> status, List<RegistrationStatusEnum> teamStatus,
			Integer championshipId);

	@Modifying
	@Query(value = "DELETE FROM participant_teams WHERE id= :id", nativeQuery = true)
	public void deleteByTeamId(Integer id);

	public List<ParticipantTeam> findAllByStatusInOrderByNameAsc(List<RegistrationStatusEnum> approved);

	public List<ParticipantTeam> findByChampionshipAndStatusIn(Championship championship,
			List<RegistrationStatusEnum> participantTeamStatus);

	@Query(value = "SELECT pt from ParticipantTeam pt where pt.championship =:championship AND pt IN (SELECT ptp.participantTeam from ParticipantTeamParticipants ptp where ptp.participant.id =:id)")
	public List<ParticipantTeam> findAllParticipantTeamsByChampionshipAndCurrentUser(Championship championship,
			Integer id);

	// List<ParticipantTeam> findAllByApprovalStatusIn(List<RegistrationStatusEnum>
	// participantTeamStatus);

	// Filters ends here

}
