package com.swayaan.nysf.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.District;
import com.swayaan.nysf.entity.State;

@Repository
public interface DistrictRepository extends CrudRepository<District, Integer> {
	
    public List<District> findAllByOrderByNameAsc();
    	
	public Long countById(Integer id);

	public List<District> findByStateOrderByNameAsc(@Param("state") State state);

	public boolean existsByName(String districtName);

	public District findByStateAndName(State state, String district);
	
	

}
