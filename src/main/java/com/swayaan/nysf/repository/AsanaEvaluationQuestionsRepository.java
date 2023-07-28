package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.Role;

@Repository
public interface AsanaEvaluationQuestionsRepository extends PagingAndSortingRepository<AsanaEvaluationQuestions, Integer> {
 //public interface AsanaEvaluationQuestionsRepository extends CrudRepository<AsanaEvaluationQuestions, Integer> {
	
	public Long countById(Integer id);

	public List<AsanaEvaluationQuestions> findAllByOrderByIdAsc();

//	public List<AsanaEvaluationQuestions> findAllByTypeAndChampionshipAndChampionshipCategory(JudgeType judgeType,
//			Championship championship, ChampionshipCategory championshipCategory);
	
	public List<AsanaEvaluationQuestions> findAllByTypeAndAsanaCategory(JudgeType judgeType,
			AsanaCategory asanaCategory);

	public List<AsanaEvaluationQuestions> findAllByTypeAndAsanaCategoryAndForEachAsanaAndCommonForEachParticipant(
			JudgeType judgeType, AsanaCategory asanaCategory, boolean forEachAsana,
			boolean commonForEachParticpant);

//	public List<AsanaEvaluationQuestions> findByChampionship(Championship championship);

//	@Query(value="SELECT * FROM asana_evaluation_questions WHERE referee_type = :refereeType and asana_category_id = :asanaCategoryId and for_each_asana = :forEachAsana", nativeQuery=true)
//	public List<AsanaEvaluationQuestions> getQuestionsForRefereeTypeAndGroupCategory(String refereeType, Integer asanaCategoryId, boolean forEachAsana);
//
//	@Query(value="SELECT * FROM asana_evaluation_questions WHERE referee_type = :refereeType and asana_category_id = :asanaCategoryId and for_each_asana = :forEachAsana", nativeQuery=true)
//	public List<AsanaEvaluationQuestions> getQuestionsForRefereeTypeAndGroupCategoryAndFullMatch(String refereeType,
//			Integer asanaCategoryId, boolean forEachAsana);

	
//	@Query(value="SELECT aeq.id, aeq.question, aeq.reference_marks,"
//			+ " pgas.score,"
//			+ " aes.weightage"
//			+ " FROM asana_evaluation_questions aeq"
//			+ " JOIN participant_group_asana_scores pgas ON aeq.id = pgas.asana_evaluation_questions_id"
//			+ " JOIN asana_execution_scores aes ON aes.asana_id = pgas.asana_id "
//			+ " JOIN participant_groups pg ON pg.id = pgas.participant_group_id WHERE aeq.referee_type = :refereeType and aeq.asana_category_id = :asanaCategoryId and aeq.for_each_asana = :forEachAsana", nativeQuery=true)
//	
//	@Query(value="SELECT aeq.id,aeq.question,aeq.reference_marks,"
//			+ " pgas.score, pgas.round_number, pgas.asana_id,"
//			+ " aes.weightage"
//			+ " FROM asana_evaluation_questions aeq"
//			+ " LEFT JOIN participant_group_asana_scores pgas ON aeq.id = pgas.asana_evaluation_questions_id AND pgas.participant_group_id = :participantGroupId AND pgas.round_number = :roundNumber AND pgas.asana_id = :asanaId"
//			+ " LEFT JOIN asana_execution_scores aes ON aeq.asana_category_id = aes.asana_category_id AND aes.asana_id = :asanaId AND aes.gender = :gender AND aes.asana_category_id = :asanaCategoryId AND aes.category = :ageCategory"
//			+ " WHERE aeq.for_each_asana = :forEachAsana AND aeq.asana_category_id = :asanaCategoryId AND aeq.referee_type = :refereeType", nativeQuery=true)
//	public List<AsanaEvaluationQuestions> getQuestionsForRefereeType(String refereeType, Integer asanaCategoryId, boolean forEachAsana, 
//			Integer participantGroupId, String roundNumber, Integer asanaId, String gender, String ageCategory);

	
//	@Query(value="SELECT aeq.id, aeq.question, aeq.reference_marks,"
//			+ " pgas.score, pgas.round_number,"
//			+ " aes.weightage"
//			+ " FROM asana_evaluation_questions aeq"
//			+ " LEFT JOIN participant_group_asana_scores pgas ON aeq.id = pgas.asana_evaluation_questions_id"
//			+ " LEFT JOIN asana_execution_scores aes ON aeq.asana_category_id = aes.asana_category_id AND aes.gender = :gender AND aes.asana_category_id = :asanaCategoryId AND aes.age_category_id = :ageCategoryId"
//			+ " WHERE aeq.for_each_asana = :forEachAsana AND aeq.asana_category_id = :asanaCategoryId AND aeq.referee_type = :refereeType", nativeQuery=true)
//	public List<AsanaEvaluationQuestions> getQuestionsForRefereeType(String refereeType, Integer asanaCategoryId, boolean forEachAsana, String gender, Integer ageCategoryId);

	//public List<AsanaEvaluationQuestions> findByAsanaCategory(AsanaCategory asanaCategory);
	
	
//	@Query(value= "select j from Judge j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% and j.jrnNumber LIKE %?2%  ")
//	public Page<AsanaEvaluationQuestions> findAllByTypeTypeContainingAndAsanaCategoryNameContaining( String asanaCategory,String type,
//			Pageable pageable);
	
	public Page<AsanaEvaluationQuestions> findAllByAsanaCategoryNameContainingAndTypeTypeContaining( String asanaCategory,String type,
			Pageable pageable);

	public Page<AsanaEvaluationQuestions> findAllByAsanaCategoryNameContaining(String asanaCategory, Pageable pageable);

//	@Query(value= "select j from Judge j where CONCAT(j.email, ' ', j.firstName, ' ',j.lastName) LIKE %?1% ")
	public Page<AsanaEvaluationQuestions> findAllByTypeTypeContaining(String type, Pageable pageable);

	public Page<AsanaEvaluationQuestions> findAll(Pageable pageable);


	
}
