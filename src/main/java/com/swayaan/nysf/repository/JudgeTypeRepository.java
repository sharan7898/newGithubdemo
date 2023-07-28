package com.swayaan.nysf.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.JudgeType;

@Repository
public interface JudgeTypeRepository extends CrudRepository<JudgeType, Integer> {
	
	public List<JudgeType> findAll();
	
	public Long countById(Integer id);
	

}
