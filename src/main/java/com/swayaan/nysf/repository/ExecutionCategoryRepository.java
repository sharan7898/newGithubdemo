package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ExecutionCategory;

@Repository
public interface ExecutionCategoryRepository extends CrudRepository<ExecutionCategory, Integer> {
	
	public List<ExecutionCategory> findAllByOrderByIdAsc();
	
	public Long countById(Integer id);
}
