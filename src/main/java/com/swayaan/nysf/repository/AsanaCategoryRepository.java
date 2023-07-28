package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.AsanaCategory;
 import com.swayaan.nysf.entity.Judge;

@Repository
public interface AsanaCategoryRepository extends PagingAndSortingRepository<AsanaCategory, Integer> {
// public interface AsanaCategoryRepository extends CrudRepository<AsanaCategory, Integer> {
	
	public List<AsanaCategory> findAllByOrderByIdAsc();
	
	public Long countById(Integer id);
	
	
	 // Admin filters starts here
	public Page<AsanaCategory> findAllByNameContainingAndCodeContaining(String name, String code,
			Pageable pageable);

	public Page<AsanaCategory> findAllByCodeContaining(String code, Pageable pageable);

	public Page<AsanaCategory> findAllByNameContaining(String name, Pageable pageable);

	public Page<AsanaCategory> findAll(Pageable pageable);
	 // Admin filters ends here


}

