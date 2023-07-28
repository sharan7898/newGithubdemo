package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.AgeCategoryRepository;
import com.swayaan.nysf.repository.JudgeTypeRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class JudgeTypeService {

	@Autowired
	JudgeTypeRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(JudgeTypeService.class);

	public List<JudgeType> listAllJudgeType() {
		LOGGER.info("Entered listAllJudgeType method -JudgeTypeService");
		LOGGER.info("Exit listAllJudgeType method -JudgeTypeService");
		return repo.findAll();
	}

	public JudgeType get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -JudgeTypeService");
		try {
			LOGGER.info("Exit get method -JudgeTypeService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any judge type with ID " + id);
			throw new EntityNotFoundException("Could not find any judge type with ID " + id);
		}
	}

	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -JudgeTypeService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any age category with ID " + id);
			throw new EntityNotFoundException("Could not find any age category with ID " + id);
		}
		LOGGER.info("Exit delete method -JudgeTypeService");
		repo.deleteById(id);
	}

//	public AgeCategory getAgeCategoryByTitle(String title) {
//		return repo.findByTitle(title);
//	}

}
