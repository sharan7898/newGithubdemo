package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.Role;

@Repository
public interface AsanaRepository extends PagingAndSortingRepository<Asana, Integer> {
//public interface AsanaRepository extends CrudRepository<Asana, Integer> {
	
	public List<Asana> findAllByOrderByNameAsc();

	public Long countById(Integer id);

	@Query(value="SELECT * FROM asana WHERE id NOT IN (SELECT asana_id FROM compulsory_round_asanas WHERE asana_category_id = :asanaCategory_id and category = :category and gender = :gender and compulsory = :compulsory and round_number = :round_number ORDER BY asana_id ASC) ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findOptionalAsanasForRound(Integer asanaCategory_id, String category, String gender, boolean compulsory, String round_number);

	@Query(value="SELECT * FROM asana WHERE id NOT IN (SELECT asana_id FROM compulsory_round_asanas) ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findOptionalAsanas();
	
	
	@Query(value="SELECT * FROM asana "
			+ "LEFT JOIN asana_execution_scores ON asana.id = asana_execution_scores.asana_id "
			+ " WHERE asana_id NOT IN "
			+ "((SELECT asana_id FROM compulsory_round_asanas WHERE "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "compulsory = :compulsory) UNION "
			+ "(Select asana_id from participantteam_asanas where participantteam_id=:participantteam_id)) and "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "code != :code "
			+ "ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findNonSelectedOptionalAsanasForRound(Integer asanaCategory_id, Integer ageCategory_id, String gender,
			boolean compulsory, Integer participantteam_id, String code);
	
	// For round wise optional asanas
	@Query(value="SELECT * FROM asana "
			+ "LEFT JOIN asana_execution_scores ON asana.id = asana_execution_scores.asana_id "
			+ " WHERE asana_id NOT IN "
			+ "((SELECT asana_id FROM compulsory_round_asanas WHERE "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "compulsory = :compulsory) UNION "
			+ "(Select asana_id from participantteam_asanas where participantteam_id=:participantteam_id and round_number=:roundNumber)) and "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "code != :code "
			+ "ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findNonSelectedOptionalAsanasForRound(Integer asanaCategory_id, Integer ageCategory_id, String gender,
			boolean compulsory, Integer participantteam_id,Integer roundNumber, String code);
	
		
	
	@Query(value="SELECT * FROM asana "
			+ "LEFT JOIN asana_execution_scores ON asana.id = asana_execution_scores.asana_id "
			+ " WHERE asana_id NOT IN "
			+ "((SELECT asana_id FROM compulsory_round_asanas WHERE "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "compulsory = :compulsory) UNION "
			+ "(Select asana_id from participantteam_asanas where participantteam_id=:participantteam_id and participant_team_participants_id =:participantTeamParticipant_id)) and "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "code != :code "
			+ "ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findNonSelectedOptionalAsanasForRoundAndParticipant(Integer asanaCategory_id, Integer ageCategory_id, String gender,
			boolean compulsory, Integer participantteam_id, String code, Integer participantTeamParticipant_id);
	
	
	// For round wise optional asanas
	
	@Query(value="SELECT * FROM asana "
			+ "LEFT JOIN asana_execution_scores ON asana.id = asana_execution_scores.asana_id "
			+ " WHERE asana_id NOT IN "
			+ "((SELECT asana_id FROM compulsory_round_asanas WHERE "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "compulsory = :compulsory) UNION "
			+ "(Select asana_id from participantteam_asanas where participantteam_id=:participantteam_id and participant_team_participants_id =:participantTeamParticipant_id and round_number=:roundNumber)) and "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "code != :code "
			+ "ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findNonSelectedOptionalAsanasForRoundAndParticipant(Integer asanaCategory_id, Integer ageCategory_id, String gender,
			boolean compulsory, Integer participantteam_id, Integer roundNumber,String code, Integer participantTeamParticipant_id);
		
	
	@Query(value="select a.id, a.created_time, a.image,a.last_modified_time, CONCAT(ae.code, '-',a.name) as name, a.created_by,a.last_modified_by from asana a "
			+ "LEFT JOIN asana_execution_scores ae ON a.id = ae.asana_id "
			+ " WHERE ae.asana_id NOT IN "
			+ "((SELECT asana_id FROM compulsory_round_asanas WHERE "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "compulsory = :compulsory) UNION "
			+ "(Select asana_id from participantteam_asanas where participantteam_id=:participantteam_id and participant_team_participants_id =:participantTeamParticipant_id)) and "
			+ "asana_category_id = :asanaCategory_id and "
			+ "age_category_id = :ageCategory_id and "
			+ "gender = :gender and "
			+ "code != :code "
			+ "ORDER BY name ASC", nativeQuery=true)
	public List<Asana> findNonSelectedOptionalAsanasForRoundAndParticipantWithCode(Integer asanaCategory_id, Integer ageCategory_id, String gender,
			boolean compulsory, Integer participantteam_id, String code, Integer participantTeamParticipant_id);
	
// Old one
//	SELECT * FROM db_nysfkhelo_youth.asana
//    LEFT JOIN  db_nysfkhelo_youth.asana_execution_scores ON asana.id = asana_execution_scores.asana_id
//	WHERE asana_id NOT IN
//	((SELECT asana_id FROM compulsory_round_asanas WHERE 
//	asana_category_id = 1 and 
//	age_category_id = 1 and 
//	gender = 'Male' and
//	compulsory = true) UNION 
//	(Select asana_id from participantteam_asanas where participantteam_id=1))
//	AND asana_category_id = 1 AND
//	age_category_id = 1 AND
//	gender = 'Male' AND
//    code != 'COMP';
	

	
	
//	@Query(value="SELECT * FROM asana WHERE id NOT IN "
//			+ "((SELECT asana_id FROM compulsory_round_asanas WHERE "
//			+ "asana_category_id = :asanaCategory_id and "
//			+ "age_category_id = :ageCategory_id and "
//			+ "gender = :gender and "
//			+ "compulsory = :compulsory) UNION "
//			+ "(Select asana_id from participantteam_asanas where participantteam_id=:participantteam_id and round_number=:roundNumber)) "
//			+ "ORDER BY name ASC", nativeQuery=true)
//	public List<Asana> findNonSelectedOptionalAsanasForRound(Integer asanaCategory_id, Integer ageCategory_id, String gender,
//			boolean compulsory, Integer roundNumber, Integer participantteam_id);
//	
	
		public Page<Asana> findAllByNameContaining(String name, Pageable pageable);

}
