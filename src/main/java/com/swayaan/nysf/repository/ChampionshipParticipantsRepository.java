//package com.swayaan.nysf.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import com.swayaan.nysf.entity.CategoryEnum;
//import com.swayaan.nysf.entity.Championship;
//import com.swayaan.nysf.entity.ChampionshipParticipants;
//import com.swayaan.nysf.entity.GenderEnum;
//import com.swayaan.nysf.entity.Participant;
//
//@Repository
//public interface ChampionshipParticipantsRepository extends CrudRepository<ChampionshipParticipants, Integer> {
//
//	public Long countById(Integer id);
//
//	public List<ChampionshipParticipants> findByChampionship(Championship championship);
//
//	@Modifying
//	@Query("DELETE ChampionshipParticipants p WHERE p.id = ?1")
//	public void deleteById(Integer id);
//
//	
//	@Query(value= "select * from championship_participants where championship_id=:championshipId AND participant_id not in (select participant_id FROM participantteam_participants where participantteam_id=:id) ORDER BY id ASC",nativeQuery=true)
//	public List<ChampionshipParticipants> listAllNonAssignedChampionshipParticipants(Integer championshipId, Integer id);
//
//	public List<ChampionshipParticipants> findByParticipant(Participant participant);
//
//	
//}
