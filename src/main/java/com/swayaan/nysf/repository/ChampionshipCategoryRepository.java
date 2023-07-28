package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;

@Repository
public interface ChampionshipCategoryRepository extends PagingAndSortingRepository<ChampionshipCategory, Integer> {

	@Query(value = "SELECT no_of_rounds FROM championship_category WHERE championship_id = :championshipId and asana_category_id = :asanaCategoryId and age_category_id=:ageCategoryId and gender = :gender  ORDER BY id ASC", nativeQuery = true)
	Integer getRoundsForChampionshipAndAsanaCategoryAndAgeCategoryAndGender(Integer championshipId,
			Integer asanaCategoryId, Integer ageCategoryId, String gender);

	@Query(value = "SELECT * FROM championship_category WHERE championship_id = :championshipId and asana_category_id = :asanaCategoryId and age_category_id=:ageCategoryId and gender = :gender  ORDER BY id ASC", nativeQuery = true)
	ChampionshipCategory getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryAndGender(
			Integer championshipId, Integer asanaCategoryId, Integer ageCategoryId, String gender);

	@Query(value = "SELECT * FROM championship_category WHERE championship_id = :championshipId and asana_category_id = :asanaCategoryId and age_category_id=:ageCategoryId and gender = :gender", nativeQuery = true)
	public ChampionshipCategory findByChampionshipAndAsanaCategoryAndGenderAndAgeCategory(Integer championshipId,
			Integer asanaCategoryId, Integer ageCategoryId, String gender);

	@Modifying
	@Query(value = "delete from championship_category where championship_id =:championshipId", nativeQuery = true)
	void deleteByChampionshipId(Integer championshipId);

	@Query(value = "SELECT distinct * FROM championship_category WHERE championship_id = :championshipId and asana_category_id = :asanaCategoryId and age_category_id=:ageCategoryId", nativeQuery = true)
	List<ChampionshipCategory> findDistinctByChampionshipAndAsanaCategoryAndAgeCategory(Integer championshipId,
			Integer asanaCategoryId, Integer ageCategoryId);

	@Query(value = "SELECT no_of_participants FROM championship_category WHERE championship_id = :championshipId and asana_category_id = :asanaCategoryId and age_category_id=:ageCategoryId and gender = :gender  ORDER BY id ASC", nativeQuery = true)
	public Integer getParticipantsForChampionshipAndAsanaCategoryAndAgeCategoryAndGender(Integer championshipId,
			Integer asanaCategoryId, Integer ageCategoryId, String gender);

	@Query(value = "SELECT * FROM championship_category WHERE championship_id = :championshipId and asana_category_id = :asanaCategoryId and age_category_id=:ageCategoryId", nativeQuery = true)
	List<ChampionshipCategory> findAllByChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc(
			Integer championshipId, Integer asanaCategoryId, Integer ageCategoryId);

	@Query(value = "select * from championship_category where championship_id =:championshipId", nativeQuery = true)
	List<ChampionshipCategory> findByChampionship(Championship championshipId);

	@Query(value = "select * from championship_category where championship_id =:championshipId", nativeQuery = true)
	List<ChampionshipCategory> findByChampionshipId(Integer championshipId);

	@Query(value = "select * from championship_category where age_category_id =:ageCategory and championship_id =:championshipId", nativeQuery = true)
	List<ChampionshipCategory> findByCategoryAndChampionship(AgeCategory ageCategory, Integer championshipId);

	List<ChampionshipCategory> findByCategory(AgeCategory ageCategory);

	@Query(value = "select * from championship_category where asana_category_id =:asanaCategory and championship_id =:championshipId", nativeQuery = true)
	List<ChampionshipCategory> findByAsanaCategoryAndChampionship(AsanaCategory asanaCategory, Integer championshipId);

	List<ChampionshipCategory> findByAsanaCategory(AsanaCategory asanaCategory);

	// fliters for championshipcategory ends here
//	@Query(value="select *,ac.name as asanaCategory,a.title as ageCategory from championship_category c  LEFT JOIN asana_category ac on c.asana_category_id=ac.id LEFT JOIN age_category a on c.age_category_id=a.id where  c.championship_id=:championship and c.asana_category_id IN (select aa.id from asana_category aa where aa.name LIKE %:asanaCate%) and c.age_category_id IN (select a.id from age_category a where a.title LIKE %:ageCate%)",nativeQuery=true)
	@Query(value = "select * from championship_category c where  c.championship_id=:championship and c.asana_category_id IN (select aa.id from asana_category aa where aa.name LIKE %:asanaCate%) and c.age_category_id IN (select a.id from age_category a where a.title LIKE %:ageCate%)", nativeQuery = true)
	Page<ChampionshipCategory> findAllByChampionshipIdAndAsanaCategoryNameContainingAndCategoryTitleContaining(
			Integer championship, String asanaCate, String ageCate, Pageable pageable);

//	@Query(value="select *,ac.name as asanaCategory,a.title as ageCategory from championship_category c  LEFT JOIN asana_category ac on c.asana_category_id=ac.id LEFT JOIN age_category a on c.age_category_id=a.id where c.championship_id=:championship and c.asana_category_id IN (select a.id from asana_category a where a.name LIKE %:asanaCate%)",nativeQuery=true)
	@Query(value = "select * from championship_category c where c.championship_id=:championship and c.asana_category_id IN (select a.id from asana_category a where a.name LIKE %:asanaCate%)", nativeQuery = true)
	Page<ChampionshipCategory> findAllByChampionshipIdAndAsanaCategoryNameContaining(Integer championship,
			String asanaCate, Pageable pageable);

//	@Query(value="select *,ac.name as asanaCategory,a.title as ageCategory from championship_category c  LEFT JOIN asana_category ac on c.asana_category_id=ac.id LEFT JOIN age_category a on c.age_category_id=a.id where c.championship_id=:championship and c.age_category_id IN (select a.id from age_category a where a.title LIKE %:ageCate%)",nativeQuery=true)
	@Query(value = "select * from championship_category c where c.championship_id=:championship and c.age_category_id IN (select a.id from age_category a where a.title LIKE %:ageCate%)", nativeQuery = true)
	Page<ChampionshipCategory> findAllByChampionshipIdAndCategoryTitleContaining(Integer championship, String ageCate,
			Pageable pageable);

	// @Query(value="select *,ac.name as asanaCategory,a.title as ageCategory from
	// championship_category c LEFT JOIN asana_category ac on
	// c.asana_category_id=ac.id LEFT JOIN age_category a on c.age_category_id=a.id
	// where c.championship_id=:championship",nativeQuery=true)
	@Query(value = "select * from championship_category c where c.championship_id=:championship", nativeQuery = true)
	Page<ChampionshipCategory> findAllByChampionshipId(Integer championship, Pageable pageable);

	// fliters for championshipcategory ends here

}
