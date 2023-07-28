package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.EventCategory;

@Repository
public interface EventCategoryRepository extends CrudRepository<EventCategory, Integer> {

	List<EventCategory> findByAgeCategory(AgeCategory ageCategory);

	List<EventCategory> findByAsanaCategory(AsanaCategory asanaCategory);

}
