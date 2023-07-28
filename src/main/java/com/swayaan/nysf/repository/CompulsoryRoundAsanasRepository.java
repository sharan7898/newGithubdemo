package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeam;

@Repository
public interface CompulsoryRoundAsanasRepository extends PagingAndSortingRepository<CompulsoryRoundAsanas, Integer> {

	public Long countById(Integer id);

	public List<CompulsoryRoundAsanas> findAll();

	@Query(value = "SELECT * FROM compulsory_round_asanas WHERE asana_category_id = :asanaCategory_id and age_category_id= :categoryId and gender = :gender and compulsory = :compulsory and round_number = :round_number and championship_id =:championshipId ORDER BY sequence_number ASC", nativeQuery = true)
	public List<CompulsoryRoundAsanas> getCompulsoryAsanasForGivenParams(Integer asanaCategory_id, Integer categoryId,
			String gender, boolean compulsory, Integer round_number,Integer championshipId);

	public List<CompulsoryRoundAsanas> findByAsanaCategory(AsanaCategory asanaCategory);

	public List<CompulsoryRoundAsanas> findByAsana(Asana asana);

	public List<CompulsoryRoundAsanas> findAllByChampionship(Championship championship);

	public List<CompulsoryRoundAsanas> findByCategory(AgeCategory ageCategory);

	@Query(value = "SELECT count(*) FROM compulsory_round_asanas WHERE championship_id =:championship and asana_category_id = :asanaCategory and age_category_id= :category and gender = :gender and round_number = :roundNumber", nativeQuery = true)
	public int getTotalCountForGivenParams(Championship championship, AsanaCategory asanaCategory, AgeCategory category,
			String gender, Integer roundNumber);

	public List<CompulsoryRoundAsanas> findAllByChampionshipAndAsanaAndAsanaCategoryAndGenderAndCategoryAndRoundNumber(
			Championship championship, Asana asana, AsanaCategory asanaCategory, GenderEnum gender, AgeCategory category,
			Integer roundNumber);

	
	// Admin Filters starts here
	
	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND p.category.title LIKE %?2% AND p.championship.name LIKE %?3% AND p.championship.status NOT IN (?4) ")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndCategoryContainingAndChampionshipNameContainingAndStatusNotIn(
			String asanaCategory, String category, String event, List<ChampionshipStatusEnum> status, Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND p.category.title LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndCategoryContainingAndStatusNotIn(
			String asanaCategory, String category, List<ChampionshipStatusEnum> status, Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND  p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndStatusNotIn(
			String asanaCategory, String event, List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND p.championship.status NOT IN (?2) ")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndStatusNotIn(String asanaCategory,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.category.title LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<CompulsoryRoundAsanas> findAllByCategoryContainingAndChampionshipNameContainingAndStatusNotIn(
			String category, String event, List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.category.title LIKE %?1% AND   p.championship.status NOT IN (?2) ")
	public Page<CompulsoryRoundAsanas> findAllByCategoryContainingAndStatusNotIn(String category,
			List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.championship.name LIKE %?1% AND p.championship.status NOT IN (?2) ")
	public Page<CompulsoryRoundAsanas> findAllByChampionshipNameContainingAndStatusNotIn(String event,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	public Page<CompulsoryRoundAsanas> findAllByChampionshipStatusNotIn(List<ChampionshipStatusEnum> status,
			Pageable pageable);
//Admin filter ends here
	
	//event manager filter starts here
	
	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND p.category.title LIKE %?2% AND p.championship.name LIKE %?3% AND p.championship.status NOT IN (?4) AND p.championship.id =?5 ")	
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndCategoryContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId(
			String asanaCategory, String category, String championshipName, List<ChampionshipStatusEnum> status,
			Integer championshipId, Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND p.category.title LIKE %?2% AND p.championship.status NOT IN (?3) AND p.championship.id =?4 ")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndCategoryContainingAndStatusNotInAndChampionshipId(
			String asanaCategory, String category, List<ChampionshipStatusEnum> status, Integer championshipId,
			Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND  p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.championship.id =?4")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId(
			String asanaCategory, String championshipName, List<ChampionshipStatusEnum> status, Integer championshipId,
			Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where p.asanaCategory.name LIKE %?1% AND p.championship.status NOT IN (?2) AND p.championship.id =?3")
	public Page<CompulsoryRoundAsanas> findAllByAsanaCategoryNameContainingAndStatusNotInAndChampionshipId(
			String asanaCategory, List<ChampionshipStatusEnum> status, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.category.title LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) AND p.championship.id =?4")
	public Page<CompulsoryRoundAsanas> findAllByCategoryContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId(
			String category, String championshipName, List<ChampionshipStatusEnum> status, Integer championshipId,
			Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.category.title LIKE %?1% AND   p.championship.status NOT IN (?2) AND p.championship.id =?3")
	public Page<CompulsoryRoundAsanas> findAllByCategoryContainingAndStatusNotInAndChampionshipId(String category,
			List<ChampionshipStatusEnum> status, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.championship.name LIKE %?1% AND p.championship.status NOT IN (?2) AND p.championship.id =?3")
	public Page<CompulsoryRoundAsanas> findAllByChampionshipNameContainingAndStatusNotInAndChampionshipId(
			String championshipName, List<ChampionshipStatusEnum> status, Integer championshipId, Pageable pageable);
	
	@Query(value = "Select p from CompulsoryRoundAsanas p where  p.championship.id =?1")
	public Page<CompulsoryRoundAsanas> findAllByChampionshipStatusNotInAndChampionshipId(
			List<ChampionshipStatusEnum> status, Integer championshipId, Pageable pageable);
		
// event manager filters ends here

}
