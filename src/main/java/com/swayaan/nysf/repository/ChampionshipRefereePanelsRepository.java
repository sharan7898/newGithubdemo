package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.ParticipantTeam;

@Repository
public interface ChampionshipRefereePanelsRepository extends PagingAndSortingRepository<ChampionshipRefereePanels, Integer> {
	
    public List<ChampionshipRefereePanels> findAllByOrderByChampionshipDesc();
	
	public Long countById(Integer id);

	public List<ChampionshipRefereePanels> findByChampionshipAndAsanaCategory(Championship championship, AsanaCategory asanaCategory);

	public List<ChampionshipRefereePanels> findByChampionship(Championship championship);

	public List<ChampionshipRefereePanels> findByAsanaCategory(AsanaCategory asanaCategory);

	public List<ChampionshipRefereePanels> findAllByChampionshipStatusNot(ChampionshipStatusEnum status);
	public boolean existsByNameAndChampionship(String name, Championship championship);

	public boolean existsByNameAndChampionshipAndIdNot(String name, Championship championship, Integer id);
	
	public List<ChampionshipRefereePanels> findByChampionshipAndAsanaCategoryAndCategoryAndGenderAndRoundNumber(Championship championship,
			AsanaCategory asanaCategory, AgeCategory ageCategory, GenderEnum genderEnum, Integer round);
	
//filters starts from here
	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1% AND p.championship.name LIKE %?2% AND p.asanaCategory.name LIKE %?3% AND p.championship.status NOT IN (?4) ")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndStatusNotIn(
			String name, String event, String asanaCategory, List<ChampionshipStatusEnum> status, Pageable pageable);

	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1% AND p.championship.name LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndChampionshipNameContainingAndStatusNotIn(
			String name, String event, List<ChampionshipStatusEnum> status, Pageable pageable);

	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1% AND  p.asanaCategory.name LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndAsanaCategoryNameContainingAndStatusNotIn(
			String name, String asanaCategory, List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1% AND p.championship.status NOT IN (?2) ")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndStatusNotIn(String name,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	@Query(value = "Select p from ChampionshipRefereePanels p where  p.championship.name LIKE %?1% AND p.asanaCategory.name LIKE %?2% AND p.championship.status NOT IN (?3) ")
	public Page<ChampionshipRefereePanels> findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndStatusNotIn(
			String event, String asanaCategory, List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from ChampionshipRefereePanels p where  p.championship.name LIKE %?1% AND   p.championship.status NOT IN (?2) ")
	public Page<ChampionshipRefereePanels> findAllByChampionshipNameContainingAndAndStatusNotIn(String event,
			List<ChampionshipStatusEnum> status, Pageable pageable);
	
	@Query(value = "Select p from ChampionshipRefereePanels p where  p.asanaCategory.name LIKE %?1% AND p.championship.status NOT IN (?2) ")
	public Page<ChampionshipRefereePanels> findAllByAsanaCategoryNameContainingAndStatusNotIn(String asanaCategory,
			List<ChampionshipStatusEnum> status, Pageable pageable);

	public Page<ChampionshipRefereePanels> findAllByChampionshipStatusNotIn(List<ChampionshipStatusEnum> status,
			Pageable pageable);
//event manager championship judge panel filters 
	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1%  AND p.asanaCategory.name LIKE %?2% AND p.championship.name LIKE %?3% ")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameContaining(
			String name, String asanaCategory, String championshipName, Pageable pageable);
	
	@Query(value = "Select p from ChampionshipRefereePanels p where  p.asanaCategory.name LIKE %?1% AND p.championship.name LIKE %?2% ")
	public Page<ChampionshipRefereePanels> findAllByAsanaCategoryNameContainingAndChampionshipNameContaining(
			String asanaCategory, String championshipName, Pageable pageable);
	
	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1%  AND  p.championship.name LIKE %?2% ")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndChampionshipNameContaining(String name,
			String championshipName, Pageable pageable);
	
	@Query(value = "Select p from ChampionshipRefereePanels p where  p.championship.name LIKE %?1% ")
	public Page<ChampionshipRefereePanels> findAllByChampionshipNameContaining(String championshipName,Pageable pageable);

	public boolean existsByChampionshipAndAsanaCategoryAndCategoryAndGenderAndRoundNumber(Championship championship,
			AsanaCategory asanaCategory, AgeCategory ageCategory, GenderEnum genderEnum, Integer roundNumber);
/////////////////////////////////
	
	
	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1%  AND p.asanaCategory.name LIKE %?2% AND p.championship.name LIKE %?3%  AND p.championship.id =?4")	
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameContainingAndChampionshipId(
			String name, String asanaCategory, String championshipName, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ChampionshipRefereePanels p where  p.asanaCategory.name LIKE %?1% AND p.championship.name LIKE %?2%  AND p.championship.id =?3")
	public Page<ChampionshipRefereePanels> findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndChampionshipId(
			String asanaCategory, String championshipName, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ChampionshipRefereePanels p where p.name LIKE %?1%  AND  p.championship.name LIKE %?2% AND p.championship.id =?3")
	public Page<ChampionshipRefereePanels> findAllByNameContainingAndChampionshipNameContainingAndChampionshipId(
			String name, String championshipName, Integer championshipId, Pageable pageable);

	@Query(value = "Select p from ChampionshipRefereePanels p where  p.championship.name LIKE %?1% AND p.championship.id =?2")
	public Page<ChampionshipRefereePanels> findAllByChampionshipNameContainingAndChampionshipId(String championshipName,
			Integer championshipId, Pageable pageable);





		
}
