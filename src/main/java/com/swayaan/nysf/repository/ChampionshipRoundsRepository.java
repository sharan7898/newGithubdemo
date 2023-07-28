package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;

@Repository
public interface ChampionshipRoundsRepository extends CrudRepository<ChampionshipRounds, Integer> {

	@Query(value = "SELECT * FROM championship_rounds where round =:round and status=:status and championship_id=:championshipId and championship_category_id=:championshipCategoryId ORDER BY id ASC", nativeQuery = true)
	ChampionshipRounds findAllByRoundAndStatusAndChampionshipIdAndChapionshipCategoryId(Integer round, String status,
			Integer championshipId, Integer championshipCategoryId);

	@Query(value = "SELECT * FROM championship_rounds where round =:round and championship_id=:championshipId and championship_category_id=:championshipCategoryId ORDER BY id ASC", nativeQuery = true)
	ChampionshipRounds findAllByRoundAndChampionshipIdAndChapionshipCategoryId(Integer round, Integer championshipId,
			Integer championshipCategoryId);

	public Long countById(Integer id);

	@Modifying
	@Query("DELETE ChampionshipRounds r WHERE r.id = ?1")
	public void deleteById(Integer id);

	public ChampionshipRounds findByChampionshipCategoryAndRound(ChampionshipCategory championshipCategory,
			Integer round);

	@Query(value = "SELECT * FROM championship_rounds where round =:round and championship_id=:championshipId and championship_category_id=:championshipCategoryId and status=:status ORDER BY id ASC", nativeQuery = true)
	ChampionshipRounds findAllByRoundAndChampionshipIdAndChapionshipCategoryId(Integer round, Integer championshipId,
			Integer championshipCategoryId, String status);

	ChampionshipRounds findByChampionshipAndChampionshipCategoryAndRound(Championship championship,
			ChampionshipCategory championshipCategory, Integer round);

	@Query(value = "SELECT * FROM championship_rounds where championship_id=:championshipId and championship_category_id=:championshipCategoryId ORDER BY id ASC", nativeQuery = true)
	List<ChampionshipRounds> findAllByChampionshipIdAndChapionshipCategoryId(Integer championshipId,
			Integer championshipCategoryId);

	@Modifying
	@Query("delete from ChampionshipRounds c where c.championship = ?1")
	void deleteByChampionship(Championship championship);

	List<ChampionshipRounds> findAllByChampionship(Championship championship);

	List<ChampionshipRounds> findAllByChampionshipAndChampionshipCategory(Championship championship,
			ChampionshipCategory championshipCategory);

	

	

}
