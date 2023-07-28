package com.swayaan.nysf.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipLink;

@Repository
public interface ChampionshipLinkRepository extends  CrudRepository<ChampionshipLink, Integer>{

public	ChampionshipLink findByChampionship(Championship championship);


@Query(value="select * from championship_link where championship_id =:championshipId",nativeQuery=true)
List<ChampionshipLink> findByChampionshipId(Championship championshipId);


@Modifying
@Query(value="UPDATE ChampionshipLink c set c.status=:status where c.championship=:championship")
public void updateStatusForChampionship(Championship championship, Boolean status);




	
}
