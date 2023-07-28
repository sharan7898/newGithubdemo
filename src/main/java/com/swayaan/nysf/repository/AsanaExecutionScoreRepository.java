package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeam;

@Repository
public interface AsanaExecutionScoreRepository extends PagingAndSortingRepository<AsanaExecutionScore, Integer> {
	
	public Long countById(Integer id);

	public List<AsanaExecutionScore> findAllByOrderByAsanaAsc();
	
	@Query(value="SELECT * FROM asana_execution_scores WHERE asana_id = :asana_id and asana_category_id = :asana_category_id and gender = :gender and age_category_id = :age_category_id", nativeQuery=true)
	public AsanaExecutionScore getBaseValueForAsana(@Param("asana_id") Integer asana_id, 
			@Param("asana_category_id") Integer asana_category_id, 
			@Param("gender") String gender,
			@Param("age_category_id") Integer age_category_id);

	@Query(value="SELECT * FROM asana_execution_scores WHERE asana_category_id = :asana_category_id and gender = :gender and age_category_id = :age_category_id", nativeQuery=true)
	public List<AsanaExecutionScore> findAsanasForAsanaCategoryAndGender(@Param("asana_category_id") Integer asana_category_id, 
			@Param("gender") String gender,
			@Param("age_category_id") Integer age_category_id);
	
	@Query(value="SELECT * FROM asana_execution_scores WHERE asana_category_id = :asana_category_id", nativeQuery=true)
	public List<AsanaExecutionScore> findAsanasForAsanaCategory(@Param("asana_category_id") Integer asana_category_id);

	public List<AsanaExecutionScore> findByAsanaCategory(AsanaCategory asanaCategory);

	public List<AsanaExecutionScore> findByAsana(Asana asana);

	
	@Query(value="SELECT * FROM asana_execution_scores WHERE asana_id = :asana_id and "
			+ "asana_category_id = :asana_category_id and "
			+ "gender = :gender and "
			+ "age_category_id = :age_category_id and "
			+ "code = :code", nativeQuery=true)
	public AsanaExecutionScore getBaseValueForCompulsaryAsana(@Param("asana_id") Integer asana_id, 
			@Param("asana_category_id") Integer asana_category_id, 
			@Param("gender") String gender,
			@Param("age_category_id") Integer age_category_id,
			@Param("code") String code);

	@Query(value="SELECT * FROM asana_execution_scores WHERE asana_id = :asana_id and "
			+ "asana_category_id = :asana_category_id and "
			+ "gender = :gender and "
			+ "age_category_id = :age_category_id and "
			+ "code != :code", nativeQuery=true)
	public AsanaExecutionScore getBaseValueForAsanaAndNotCompulsory(@Param("asana_id") Integer asana_id, 
			@Param("asana_category_id") Integer asana_category_id, 
			@Param("gender") String gender,
			@Param("age_category_id") Integer age_category_id,
			@Param("code") String code);

	public List<AsanaExecutionScore> findByAsanaCategoryAndGender(AsanaCategory asanaCategory, GenderEnum gender);

	public List<AsanaExecutionScore> findByCategory(AgeCategory ageCategory);

	@Query(value="SELECT * FROM asana_execution_scores WHERE asana_id = :asanaId and "
			+ "asana_category_id = :asanaCategoryId and "
			+ "gender = :gender and "
			+ "age_category_id = :agecategoryId", nativeQuery=true)
	public AsanaExecutionScore findByAsanaAndAsanaCategoryAndGenderAndAgeCategory(Integer asanaId,
			Integer asanaCategoryId, String gender, Integer agecategoryId);
	
	// Filters starts here

	//0	@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %?1% AND a.code LIKE %?2% AND a.asanaCategory.name LIKE %?3% AND a.gender LIKE %?4% ")
	     @Query(value= "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) Like %:code% AND a.asana.name LIKE %:asana% AND a.asanaCategory.name LIKE %:asanaCategory% AND a.category.title LIKE %:category% AND a.gender in (:genderEnum) ")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(
				String asana, String code, String asanaCategory,String category, GenderEnum genderEnum, Pageable pageable);

////1	@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %?1% AND a.code LIKE %?2% AND a.asanaCategory.name LIKE %?3% ")
	     @Query(value= "Select a from AsanaExecutionScore a where CONCAT(a.code , a.subGroup) Like %:code% AND a.asana.name LIKE %:asana% AND a.asanaCategory.name LIKE %:asanaCategory%  AND a.category.title LIKE %:category% ")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContaining(
			String asana, String code, String asanaCategory,String category, Pageable pageable);
//
	@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code , a.subGroup) LIKE %:code% AND a.asana.name LIKE %:asana% AND a.asanaCategory.name LIKE %:asanaCategory% AND a.gender in (:genderEnum) ")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContainingAndGenderContaining(
			String asana,String code, String asanaCategory, GenderEnum genderEnum, Pageable pageable);

//	//3	@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %?1% AND a.code LIKE %?2% AND a.gender LIKE %?3% ")
    @Query(value= "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) Like %:code% AND a.asana.name LIKE %:asana% AND a.category.title LIKE %:category% AND a.gender in (:genderEnum) ")
    public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContaining(String asana, String code, GenderEnum  genderEnum,
				Pageable pageable);
		
////	4	@Query(value = "Select a from AsanaExecutionScore a where a.code LIKE %?1% AND a.asanaCategory.name LIKE %?2% AND a.gender LIKE %?3% ")
    @Query(value= "Select a from AsanaExecutionScore a where CONCAT(a.code,  a.subGroup) LIKE %:code%  AND a.asana.name LIKE %:asana% AND a.category.title LIKE %:category% AND a.gender in (:genderEnum) ")
	public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndCategoryTitleContainingAndGenderContaining(
			String asana,String code, String category, GenderEnum genderEnum, Pageable pageable);

////	5	@Query(value = "Select a from AsanaExecutionScore a where a.code LIKE %?1% AND a.gender LIKE %?2% ")
      @Query(value= "Select a from AsanaExecutionScore a where CONCAT(a.code,  a.subGroup) LIKE %:code% AND a.asana.name Like %:asana% AND a.category.title Like %:category%")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndCategoryTitleContaining(String asana,String code,
			String category, Pageable pageable);
//
//	//6 @Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %?1% AND a CONCAT(a.code, a.subgroup) LIKE %?2% ")
    @Query(value= "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.asana.name LIKE %:asana% AND a.gender in (:genderEnum)")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContainingAndGenderContaining(String asana,
				String code,GenderEnum genderEnum, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code%  AND a.asana.name LIKE %:asana% ")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCodeContaining(
				 String asana, String  code, Pageable pageable);
//
		@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana% AND a.asanaCategory.name LIKE %:asanaCategory% AND a.category.title LIKE %:category%  AND a.gender in (:genderEnum)")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(String asana,
			String asanaCategory,String category,GenderEnum genderEnum, Pageable pageable);
//
	@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana% AND a.asanaCategory.name LIKE %:asanaCategory% AND a.category.title Like %:category% ")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndAsanaCategoryNameContainingAndCategoryTitleContaining(String asana,
			String asanaCategory,String category, Pageable pageable);

//	// 10	@Query(value = "Select a from AsanaExecutionScore a where a.code LIKE %?1% AND a.asanaCategory.name LIKE %?2% ")
    @Query(value= "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana%  AND a.asanaCategory.name LIKE %:asanaCategory% AND a.gender in (:genderEnum)")
     public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndAsanaCategoryNameContainingAndGenderContaining(String asana,
				String asanaCategory,GenderEnum genderEnum, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana% AND a.asanaCategory.name LIKE %:asanaCategory% ")
       public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndAsanaCategoryNameConatining(String asana,String asanaCategory, Pageable pageable);
//
////	12	@Query(value = "Select a from AsanaExecutionScore a where a.code LIKE %?1% ")
	     @Query(value= "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana% AND a.category.title Like %:category% AND a.gender in (:genderEnum)")
         public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCategoryTitleContainingAndGenderContaining(String asana,String category,GenderEnum genderEnum, Pageable pageable);
////----13--
		@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana%  AND a.category.title Like %:category%")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndCategoryTitleContaining(String asana ,String category, Pageable pageable);
////--14
		@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %:asana% AND a.gender in (:genderEnum)")
		public Page<AsanaExecutionScore> findAllByAsanaNameContainingAndGenderContaining(String asana,GenderEnum genderEnum, Pageable pageable);

////--15		//public Page<AsanaExecutionScore> findAll(Pageable pageable);
	@Query(value = "Select a from AsanaExecutionScore a where a.asana.name LIKE %?1% ")
       public Page<AsanaExecutionScore> findAllByAsanaNameContaining(String asana, Pageable pageable);
////-16		
		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.asanaCategory.name LIKE %:asanaCategory%  AND a.category.title Like %:category% AND a.gender in (:genderEnum)")
		public Page<AsanaExecutionScore> findAllByCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(
			String code, String asanaCategory, String category ,GenderEnum genderEnum, Pageable pageable);
////-17		
	@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) Like %:code% AND a.asanaCategory.name LIKE %:asanaCategory%  AND a.category.title Like %:category% ")
         public Page<AsanaExecutionScore> findAllByCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContaining(
			String code, String asanaCategory, String category, Pageable pageable);
////-18		
		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.asanaCategory.name LIKE %:asanaCategory%  AND a.gender in (:genderEnum) ")
       public Page<AsanaExecutionScore> findAllByCodeContainingAndAsanaCategoryNameContainingAndGenderContaining(
				String code, String asanaCategory, GenderEnum genderEnum, Pageable pageable);
////19		
		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.asanaCategory.name LIKE %:asanaCategory% ")
		public Page<AsanaExecutionScore> findAllCodeContainingAndAsanaCategoryNameContaining(
				String code, String asanaCategory, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.category.title LIKE %:category%  AND a.gender in (:genderEnum) ")
      public Page<AsanaExecutionScore> findAllByCodeContainingAndCategoryTitleContainingAndGenderContaining(String code,
				String category, GenderEnum genderEnum, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.category.title LIKE %:category% ")
    public Page<AsanaExecutionScore> findAllByCodeContainingAndCategoryTitleContaining(String code, String category,
				Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code% AND a.gender in (:genderEnum) ")
       public Page<AsanaExecutionScore> findAllByCodeContainingAndGenderContaining(String code, GenderEnum genderEnum,
				Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where CONCAT(a.code, a.subGroup) LIKE %:code%")
		public Page<AsanaExecutionScore> findAllByCodeContaining(String code, Pageable pageable);
//
		@Query(value = "Select a from AsanaExecutionScore a where a.asanaCategory.name LIKE %:asanaCategory%  And a.category.title LIKE %:category% AND a.gender in (:genderEnum)")
      public Page<AsanaExecutionScore> findAllByAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(
				String asanaCategory, String category, GenderEnum genderEnum, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where a.asanaCategory.name LIKE %:asanaCategory%  And a.category.title LIKE %:category% ")
		public Page<AsanaExecutionScore> findAllByAsanaCategoryNameContainingAndCategoryTitleContaining(String asanaCategory,
				String category, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where a.asanaCategory.name LIKE %:asanaCategory% AND a.gender in (:genderEnum)")
		public Page<AsanaExecutionScore> findAllByAsanaCategoryNameContainingAndAndGenderContaining(
				String asanaCategory, GenderEnum genderEnum, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where a.asanaCategory.name LIKE %:asanaCategory% ")
		public Page<AsanaExecutionScore> findAllByAsanaCategoryNameContaining(String asanaCategory, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where a.category.title LIKE %:category% AND a.gender in (:genderEnum)")
       public Page<AsanaExecutionScore> findAllByCategoryTitleContainingAndGenderContaining(String category,
				GenderEnum genderEnum, Pageable pageable);

		@Query(value = "Select a from AsanaExecutionScore a where a.category.title LIKE %:category% ")
       public Page<AsanaExecutionScore> findAllByCategoryTitleContaining(String category, Pageable pageable);
		
		@Query(value = "Select a from AsanaExecutionScore a where a.gender in (:genderEnum) ")
        public Page<AsanaExecutionScore> findAllByGenderContaining(GenderEnum genderEnum, Pageable pageable);
		
		public Page<AsanaExecutionScore> findAll(Pageable pageable);


	//	public Page<ParticipantTeam> findAllByChampionshipStatusNotIn(List<ChampionshipStatusEnum> status, Pageable pageable);

		// Filters ends here

	}

