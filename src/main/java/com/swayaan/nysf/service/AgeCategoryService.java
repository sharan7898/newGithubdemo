package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.AgeCategoryRepository;
import com.swayaan.nysf.repository.AsanaExecutionScoreRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class AgeCategoryService {
	
	@Autowired AgeCategoryRepository repo;
	
	@Autowired AsanaExecutionScoreRepository asanaExecutionScoreRepo;
	
	@Autowired AsanaExecutionScoreService asanaExecutionScoreService;
	
	public static final int RECORDS_PER_PAGE = 10;

	private static final Logger LOGGER = LoggerFactory.getLogger(AgeCategoryService.class);

	public List<AgeCategory> listAllAgeCategories(){
		LOGGER.info("Entered listAllAgeCategories method -AgeCategoryService");
		LOGGER.info("Exit listAllAgeCategories method -AgeCategoryService");

		return repo.findAllByOrderByTitleAsc();
	}
	
	public List<AgeCategory> listAll(){
		LOGGER.info("Entered listAll method -AgeCategoryService");
		LOGGER.info("Exit listAll method -AgeCategoryService");

		return repo.findAllByOrderByIdAsc();
	}
	
	public AgeCategory save(AgeCategory ageCategory) {
		LOGGER.info("Entered save method -AgeCategoryService");

		User currentUser = CommonUtil.getCurrentUser();		
		boolean isUpdatingCategory = (ageCategory.getId() != null);
		if (isUpdatingCategory) {
			AgeCategory existingAgeCategory = repo.findById(ageCategory.getId()).get();

			ageCategory.setLastModifiedBy(currentUser);
			ageCategory.setLastModifiedTime(new Date());
			ageCategory.setCreatedBy(existingAgeCategory.getCreatedBy());
			ageCategory.setCreatedTime(existingAgeCategory.getCreatedTime());
			
		} else {		
			ageCategory.setCreatedBy(currentUser);
			ageCategory.setCreatedTime(new Date());
		}
		LOGGER.info("Exit save method -AgeCategoryService");

		return repo.save(ageCategory);
	}
	
	public AgeCategory get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered AgeCategory method -AgeCategoryService");

		try {
			LOGGER.info("Exit AgeCategory method -AgeCategoryService");
          	return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any age category with ID " + ex.getMessage());
			throw new EntityNotFoundException("Could not find any age category with ID " + id);
		}
	}
	
	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -AgeCategoryService");

		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any age category with ID " + id);
			throw new EntityNotFoundException("Could not find any age category with ID " + id);
		}
		LOGGER.info("Exit delete method -AgeCategoryService");

		repo.deleteById(id);
	}
	
	public void updateAgeCategoryEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateAgeCategoryEnabledStatus method -AgeCategoryService");
		LOGGER.info("Exit updateAgeCategoryEnabledStatus method -AgeCategoryService");

		repo.updateEnabledStatus(id, enabled);
	}

	public AgeCategory getAgeCategoryByTitle(String title) {
		LOGGER.info("Entered getAgeCategoryByTitle method -AgeCategoryService");
		LOGGER.info("Exit getAgeCategoryByTitle method -AgeCategoryService");


		return repo.findByTitle(title);
	}

	public void saveExecutionScoresForAgeCategory(AgeCategory ageCategory) {
		LOGGER.info("Entered saveExecutionScoresForAgeCategory method -AgeCategoryService");
		LOGGER.info("Exit saveExecutionScoresForAgeCategory method -AgeCategoryService");

		AgeCategory juniorAgeCategory = repo.findById(2).get();
		List<AsanaExecutionScore> listAsanaExecutionScore = asanaExecutionScoreService.getExecutionScoresByAgeCategory(juniorAgeCategory);
		
		for(AsanaExecutionScore asanaExecutionScore : listAsanaExecutionScore) {
			
			AsanaExecutionScore newAsanaExecutionScore = new AsanaExecutionScore();
			newAsanaExecutionScore.setAsana(asanaExecutionScore.getAsana());
			newAsanaExecutionScore.setAsanaCategory(asanaExecutionScore.getAsanaCategory());
			newAsanaExecutionScore.setCode(asanaExecutionScore.getCode());
			newAsanaExecutionScore.setCreatedTime(new Date());
			newAsanaExecutionScore.setExecutionCategory(asanaExecutionScore.getExecutionCategory());
			newAsanaExecutionScore.setGender(asanaExecutionScore.getGender());
			newAsanaExecutionScore.setSubGroup(asanaExecutionScore.getSubGroup());
			newAsanaExecutionScore.setWeightage(asanaExecutionScore.getWeightage());
			newAsanaExecutionScore.setCategory(ageCategory);
			asanaExecutionScoreRepo.save(newAsanaExecutionScore);
			
		}
		
	}

	public List<AgeCategory> getAgeCategoryByEventManager(EventManager eventManager) {
		LOGGER.info("Entered getAgeCategoryByEventManager method -AgeCategoryService");
		LOGGER.info("Exit getAgeCategoryByEventManager method -AgeCategoryService");

		return repo.findAllByEventManager(eventManager.getId());
	}

	public Page<AgeCategory> listByPage(int pageNum, String sortField, String sortDir, String title, String code) {
		LOGGER.info("Entered listByPage method -AgeCategoryService");

		Sort sort = null;

	/*	if(sortField.equals("name")) {
			sortField="firstName";
		}*/
		sort = Sort.by(sortField);
		

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (title != "" && code != "") {
			LOGGER.info("findAllByTitleContainingAndCodeContaining method called  -AgeCategoryService");
			return repo.findAllByTitleContainingAndCodeContaining(title, code, pageable);
		} else if (title == "" && code != "") {
			LOGGER.info("findAllByCodeContaining method called  -AgeCategoryService");
			return repo.findAllByCodeContaining(code, pageable);
		} else if (title != "" && code == "") {
		 LOGGER.info("findAllByTitleContaining method called  AgeCategoryService");
			return repo.findAllByTitleContaining(title, pageable);
		} else {
			LOGGER.info("findAll method called  AgeCategoryService");
			LOGGER.info("Exit listByPage method -AgeCategoryService");
           return repo.findAll(pageable);
		}

	}

	}

