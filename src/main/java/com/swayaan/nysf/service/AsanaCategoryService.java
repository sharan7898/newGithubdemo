package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.controller.ManageAsanaCategoryController;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.repository.AsanaCategoryRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class AsanaCategoryService {

	@Autowired
	AsanaCategoryRepository repo;

	public static final int RECORDS_PER_PAGE = 10;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AsanaCategoryService.class);
	
	public List<AsanaCategory> listAllAsanaCategories() {
		LOGGER.info("Entered listAllAsanaCategories method -AsanaCategoryService");
		LOGGER.info("Exit listAllAsanaCategories method -AsanaCategoryService");

		return (List<AsanaCategory>) repo.findAllByOrderByIdAsc();
	}

	public void delete(Integer id) throws AsanaCategoryNotFoundException {
		LOGGER.info("Entered delete method -AsanaCategoryService");

		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new AsanaCategoryNotFoundException("Could not find any Asana Category with ID " + id);
		}
		LOGGER.info("Exit delete method -AsanaCategoryService");
        repo.deleteById(id);
	}

	public AsanaCategory getAsanaCategoryById(Integer id) throws AsanaCategoryNotFoundException {
		LOGGER.info("Entered getAsanaCategoryById method -AsanaCategoryService");

		Optional<AsanaCategory> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new AsanaCategoryNotFoundException("Asana Category with id " + id + " does not exist");
		}
		AsanaCategory existingCategory = byId.get();
		LOGGER.info("Exit getAsanaCategoryById method -AsanaCategoryService");

		return existingCategory;
	}

	public AsanaCategory save(AsanaCategory newAsanaCategory) {
		LOGGER.info("Entered save method -AsanaCategoryService");

		User currentUser = CommonUtil.getCurrentUser();		
		boolean isUpdatingAsanaCategory = (newAsanaCategory.getId() != null);
		if (isUpdatingAsanaCategory) {
			AsanaCategory existingAsanaCategory = repo.findById(newAsanaCategory.getId()).get();

			newAsanaCategory.setLastModifiedBy(currentUser);
			newAsanaCategory.setLastModifiedTime(new Date());
			newAsanaCategory.setCreatedBy(existingAsanaCategory.getCreatedBy());
			newAsanaCategory.setCreatedTime(existingAsanaCategory.getCreatedTime());
			
		} else {		
			newAsanaCategory.setCreatedBy(currentUser);
			newAsanaCategory.setCreatedTime(new Date());
		}
		LOGGER.info("Exit save method -AsanaCategoryService");

		return repo.save(newAsanaCategory);
	}

public Page<AsanaCategory> listByPage(int pageNum, String sortField, String sortDir, String name, String code) {
	LOGGER.info("Entered listByPage method -AsanaCategoryService");

	Sort sort = null;

/*	if(sortField.equals("name")) {
		sortField="firstName";
	}*/
	sort = Sort.by(sortField);
	

	sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

	Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
	if (name != "" && code != "") {
		// name code 
		LOGGER.info("findAllByNameContainingAndCodeContaining method called -AsanaCategoryService");
		return repo.findAllByNameContainingAndCodeContaining(name, code, pageable);
	} else if (name == "" && code != "") {
		// code
		LOGGER.info("findAllByCodeContaining method called -AsanaCategoryService");
		return repo.findAllByCodeContaining(code, pageable);
	} else if (name != "" && code == "") {
		// name
	 LOGGER.info("findAllByNameContaining method called -AsanaCategoryService");
		return repo.findAllByNameContaining(name, pageable);
	} else {
		LOGGER.info("findAll method called -AsanaCategoryService");
		LOGGER.info("Exit listByPage method -AsanaCategoryService");

		return repo.findAll(pageable);
	}

}

}
