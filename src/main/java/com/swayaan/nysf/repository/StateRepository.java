package com.swayaan.nysf.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.State;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {
	
    public List<State> findAllByOrderByNameAsc();
	
	public Long countById(Integer id);

	public boolean existsByName(String stateName);

	public State findByName(String state);

}
