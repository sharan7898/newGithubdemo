package com.swayaan.nysf.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ExecutionCategory;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ExecutionCategoryRepository;

@Service
public class ExecutionCategoryService {

	@Autowired
	ExecutionCategoryRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionCategoryService.class);

	public List<ExecutionCategory> listAllExecutionCategories() {
		LOGGER.info("Entered listAllExecutionCategories method -ExecutionCategoryService");
		LOGGER.info("Exit listAllExecutionCategories method -ExecutionCategoryService");
		return (List<ExecutionCategory>) repo.findAllByOrderByIdAsc();
	}

	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -ExecutionCategoryService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new EntityNotFoundException("Could not find any Execution Category with ID " + id);
		}
		LOGGER.info("Exit delete method -ExecutionCategoryService");
		repo.deleteById(id);
	}

	public ExecutionCategory getAsanaCategoryById(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered getAsanaCategoryById method -ExecutionCategoryService");

		Optional<ExecutionCategory> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new EntityNotFoundException("Execution Category with id " + id + " does not exist");
		}
		ExecutionCategory existingCategory = byId.get();
		LOGGER.info("Exit getAsanaCategoryById method -ExecutionCategoryService");
		return existingCategory;
	}

	public ExecutionCategory save(ExecutionCategory executionCategory) {
		LOGGER.info("Entered save method -ExecutionCategoryService");
		LOGGER.info("Exit save method -ExecutionCategoryService");
		return repo.save(executionCategory);
	}

}
