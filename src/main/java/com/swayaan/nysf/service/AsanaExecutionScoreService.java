package com.swayaan.nysf.service;

import java.util.ArrayList;
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
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.AsanaExecutionScoreRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class AsanaExecutionScoreService {
	
@Autowired AsanaExecutionScoreRepository repo;
@Autowired AsanaCategoryService asanaCategoryService;

public static final int RECORDS_PER_PAGE = 10;
private static final Logger LOGGER = LoggerFactory.getLogger(AsanaExecutionScoreService.class);

	
	public List<AsanaExecutionScore> listAllAsanaExecutionScores() {
		LOGGER.info("Entered listAllAsanaExecutionScores method -AsanaExecutionScoreService");
		LOGGER.info("Exit listAllAsanaExecutionScores method -AsanaExecutionScoreService");

		return repo.findAllByOrderByAsanaAsc();
	}
	
	public AsanaExecutionScore save(AsanaExecutionScore asanaExecutionScore) {
		LOGGER.info("Entered save method -AsanaExecutionScoreService");

		User currentUser = CommonUtil.getCurrentUser();		
		boolean isUpdatingAsanaExecutionScore = (asanaExecutionScore.getId() != null);
		if (isUpdatingAsanaExecutionScore) {
			AsanaExecutionScore existingAsanaExecutionScore = repo.findById(asanaExecutionScore.getId()).get();

			asanaExecutionScore.setLastModifiedBy(currentUser);
			asanaExecutionScore.setLastModifiedTime(new Date());
			asanaExecutionScore.setCreatedBy(existingAsanaExecutionScore.getCreatedBy());
			asanaExecutionScore.setCreatedTime(existingAsanaExecutionScore.getCreatedTime());
			
		} else {		
			asanaExecutionScore.setCreatedBy(currentUser);
			asanaExecutionScore.setCreatedTime(new Date());
		}
		LOGGER.info("Exit save method -AsanaExecutionScoreService");

		return repo.save(asanaExecutionScore);
	}
	
	public AsanaExecutionScore get(Integer id) throws EntityNotFoundException {
		try {
			LOGGER.info("Entered get method -AsanaExecutionScoreService");
			LOGGER.info("Exit get method -AsanaExecutionScoreService");
          return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EntityNotFoundException("Could not find the execution score for the Asana");
		}
	}
	
	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -AsanaExecutionScoreService");

		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new EntityNotFoundException("Could not find the execution score for the Asana");
		}
		LOGGER.info("Exit delete method -AsanaExecutionScoreService");
     	repo.deleteById(id);
	}

	public AsanaExecutionScore getBaseValueForAsana(Integer asana_id, Integer asana_category_id, String gender, Integer age_category_id) {
		LOGGER.info("Entered getBaseValueForAsana method -AsanaExecutionScoreService");
		LOGGER.info("Exit getBaseValueForAsana method -AsanaExecutionScoreService");
       return repo.getBaseValueForAsana(asana_id, asana_category_id, gender, age_category_id);
	}
	

	public List<AsanaExecutionScore> getListOfAsanas(Integer asanaCategoryId, String gender, Integer ageCategoryId) {
		LOGGER.info("Entered getListOfAsanas method -AsanaExecutionScoreService");

		try {
			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
			if(asanaCategory.getName().equals("Traditional")) {
				return repo.findAsanasForAsanaCategoryAndGender(asanaCategoryId, gender,ageCategoryId);
			} else {
				return repo.findAsanasForAsanaCategory(asanaCategoryId);
			}
		} catch (AsanaCategoryNotFoundException e) {
			e.printStackTrace();
		}
		LOGGER.info("Exit getListOfAsanas method -AsanaExecutionScoreService");
        return null;
	}

	public List<AsanaExecutionScore> listExecutionScoreByAsanaCategory(AsanaCategory asanaCategory) {
		LOGGER.info("Entered listExecutionScoreByAsanaCategory method -AsanaExecutionScoreService");
		LOGGER.info("Exit listExecutionScoreByAsanaCategory method -AsanaExecutionScoreService");
       return repo.findByAsanaCategory(asanaCategory);
	}

	public List<AsanaExecutionScore> listExecutionScoreByAsana(Asana asana) {
		LOGGER.info("Entered listExecutionScoreByAsana method -AsanaExecutionScoreService");
		LOGGER.info("Exit listExecutionScoreByAsana method -AsanaExecutionScoreService");
       return repo.findByAsana(asana);
	}

	
	public AsanaExecutionScore getBaseValueForCompulsaryAsana(Integer asana_id, Integer asana_category_id, String gender, Integer age_category_id) {
		LOGGER.info("Entered getBaseValueForCompulsaryAsana method -AsanaExecutionScoreService");
		LOGGER.info("Exit getBaseValueForCompulsaryAsana method -AsanaExecutionScoreService");
      return repo.getBaseValueForCompulsaryAsana(asana_id, asana_category_id, gender, age_category_id, "COMP");
	}

	public AsanaExecutionScore getBaseValueForAsanaAndNotCompulsory(Integer asana_id, Integer asana_category_id, String gender, Integer age_category_id) {
		LOGGER.info("Entered getBaseValueForAsanaAndNotCompulsory method -AsanaExecutionScoreService");
		LOGGER.info("Exit getBaseValueForAsanaAndNotCompulsory method -AsanaExecutionScoreService");
      return repo.getBaseValueForAsanaAndNotCompulsory(asana_id, asana_category_id, gender, age_category_id, "COMP");
	}

	public List<AsanaExecutionScore> listExecutionScoreByAsanaCategoryAndGender(AsanaCategory asanaCategory,
			GenderEnum gender) {
		LOGGER.info("Entered listExecutionScoreByAsanaCategoryAndGender method -AsanaExecutionScoreService");
		LOGGER.info("Exit listExecutionScoreByAsanaCategoryAndGender method -AsanaExecutionScoreService");
      return repo.findByAsanaCategoryAndGender(asanaCategory,gender);
	}

	public List<AsanaExecutionScore> getExecutionScoresByAgeCategory(AgeCategory ageCategory) {
		LOGGER.info("Entered getExecutionScoresByAgeCategory method -AsanaExecutionScoreService");
		LOGGER.info("Exit getExecutionScoresByAgeCategory method -AsanaExecutionScoreService");
      return repo.findByCategory(ageCategory);
	}

	public List<AsanaExecutionScore> getExecutionScoresByAsanaCategory(AsanaCategory asanaCategory) {
		LOGGER.info("Entered getExecutionScoresByAsanaCategory method -AsanaExecutionScoreService");
		LOGGER.info("Exit getExecutionScoresByAsanaCategory method -AsanaExecutionScoreService");
		return repo.findByAsanaCategory(asanaCategory);
	}

	public AsanaExecutionScore getExecScoreForCompulsaryAsana(Integer asanaId, Integer asanaCategoryId, String gender, Integer agecategoryId) {
		LOGGER.info("Entered getExecScoreForCompulsaryAsana method -AsanaExecutionScoreService");
		LOGGER.info("Exit getExecScoreForCompulsaryAsana method -AsanaExecutionScoreService");
     return repo.findByAsanaAndAsanaCategoryAndGenderAndAgeCategory(asanaId,asanaCategoryId,gender,agecategoryId);
	}

	public Page<AsanaExecutionScore> listByPage(int pageNum, String sortField, String sortDir, String asana,
			String code, String asanaCategory,String category, String gender) {
		LOGGER.info("Entered listByPage method -AsanaExecutionScoreService");

		
		GenderEnum genderEnum =null; 
				
		if(gender.equalsIgnoreCase("Female")) {
		 genderEnum=GenderEnum.Female;
		}
		else if (gender.equalsIgnoreCase("Male")){
			 genderEnum=GenderEnum.Male;
		}
		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		
		
	if (asana != "" && code != "" && asanaCategory != "" && category !="" && gender != "") {
//			// 00000 asana code asanaCategory category gender ----0
			return repo
					.findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(
							asana, code, asanaCategory,category, genderEnum,pageable);

		} else if (asana != "" && code != "" && asanaCategory != "" && category !="" && gender== "") {
//			// asana code asanaCategory category 00001 ---->1
		return repo.findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContaining(asana, code,
					asanaCategory,category,pageable);
	} else if (asana != "" && code != "" && asanaCategory != "" && category =="" && gender != "") {
//			// asana code asanaCategory gender 00010 ---2----
     	return repo.findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContainingAndGenderContaining(asana,
			code, asanaCategory, genderEnum, pageable);
	} else if (asana != "" && code != "" && asanaCategory != "" && category == "" && gender == "") {
//			// asana code asanaCategory 00011---3
			return repo.findAllByAsanaNameContainingAndCodeContainingAndAsanaCategoryNameContaining(asana, code,
				genderEnum, pageable);
	} else if (asana != "" && code != "" && asanaCategory == "" && category !="" && gender != "") {
//			//asana code category gender 00100---4
		return repo.findAllByAsanaNameContainingAndCodeContainingAndCategoryTitleContainingAndGenderContaining(asana,
					code, category, genderEnum, pageable);
		} else if (asana != "" && code != "" && asanaCategory == ""&& category !="" && gender == "") {
//			//  asana code category 00101 ---5
			return repo.findAllByAsanaNameContainingAndCodeContainingAndCategoryTitleContaining(asana ,code, category,
					pageable);
		} else if (asana != "" && code != "" && asanaCategory == "" && category ==""  && gender != "") {
//			// asana code gender 00110 ----6
			return repo.findAllByAsanaNameContainingAndCodeContainingAndGenderContaining(asana, code,genderEnum, pageable);
		} else if (asana != "" && code != "" && asanaCategory == "" && category =="" && gender == "") {
//			// asana code 00111----7
			return repo.findAllByAsanaNameContainingAndCodeContaining(asana,
				code,pageable);
		} else if (asana != "" && code == "" && asanaCategory != "" && category !="" && gender != "") {
//			// asana asanaCategory category gender 01000 ----8
			return repo.findAllByAsanaNameContainingAndAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(asana, asanaCategory,category,genderEnum, pageable);
		} else if (asana != "" && code == "" && asanaCategory != "" && category !="" && gender == "") {
//			// asana asanaCategory category 01001 -----9
			return repo.findAllByAsanaNameContainingAndAsanaCategoryNameContainingAndCategoryTitleContaining(asana, asanaCategory,category, pageable);
		} else if (asana != "" && code == "" && asanaCategory != "" && category ==""  && gender != "") {
//			// asana asanaCategory gender 01010------10
			return repo.findAllByAsanaNameContainingAndAsanaCategoryNameContainingAndGenderContaining(asana, asanaCategory,genderEnum,
					pageable);
	} else if (asana != "" && code == "" && asanaCategory != "" && category ==""  && gender == "") {
//			// asana  asanaCategory 01011 --------11
			return repo.findAllByAsanaNameContainingAndAsanaCategoryNameConatining(asana, asanaCategory, pageable);
		} else if (asana != "" && code == "" && asanaCategory == ""  && category !="" && gender != "") {
//			// asana category gender 01100 ------12
			return repo.findAllByAsanaNameContainingAndCategoryTitleContainingAndGenderContaining(asana,category,genderEnum, pageable);
		} else if (asana != "" && code == "" && asanaCategory == "" && category !=""  && gender == "") {
//			// asana  category 01101 -------13
			return repo.findAllByAsanaNameContainingAndCategoryTitleContaining(asana,category, pageable);
		} else if (asana != "" && code == "" && asanaCategory == "" && category ==""  && gender != "") {
//			//asana gender 01110 -------14
			return repo.findAllByAsanaNameContainingAndGenderContaining(asana,genderEnum, pageable);
		}else if (asana != "" && code == "" && asanaCategory == "" && category =="" && gender == "") {
//			// 01111 asana  ----15
			return repo
					.findAllByAsanaNameContaining(asana, pageable);
		} else if (asana == "" && code != "" && asanaCategory != "" && category !="" && gender != "") {
//			// 10000  code asanaCategory category gender ----16
			return repo
					.findAllByCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(
							 code, asanaCategory,category, genderEnum,pageable);
		}else if (asana =="" && code != "" && asanaCategory != "" && category !="" && gender == "") {
//			// 10001  code asanaCategory category ----17
			return repo
					.findAllByCodeContainingAndAsanaCategoryNameContainingAndCategoryTitleContaining(
							 code, asanaCategory, category,pageable);
		}else if (asana == "" && code != "" && asanaCategory != "" && category =="" && gender != "") {
			// 10010  code asanaCategory  gender ----18
			return repo
					.findAllByCodeContainingAndAsanaCategoryNameContainingAndGenderContaining(
							 code, asanaCategory, genderEnum,pageable);
		}else if (asana == "" && code != "" && asanaCategory != "" && category =="" && gender == "") {
//			// 10011  code asanaCategory ----19
			return repo
					.findAllCodeContainingAndAsanaCategoryNameContaining(
							 code, asanaCategory,pageable);
		}else if (asana == "" && code != "" && asanaCategory == "" && category !="" && gender != "") {
//			// 10100 code category gender ----20
			return repo
					.findAllByCodeContainingAndCategoryTitleContainingAndGenderContaining(
						 code, category, genderEnum,pageable);
		}else if (asana == "" && code != "" && asanaCategory == "" && category !="" && gender == "") {
//			// 10101 code category ----21
			return repo
					.findAllByCodeContainingAndCategoryTitleContaining(
							 code, category,pageable);
		}else if (asana == "" && code != "" && asanaCategory == "" && category =="" && gender != "") {
//			// 10110  code  gender ----22
			return repo
					.findAllByCodeContainingAndGenderContaining(
							 code, genderEnum,pageable);
		}else if (asana == "" && code != "" && asanaCategory == "" && category =="" && gender == "") {
//			// 10111 code ----23
			return repo
					.findAllByCodeContaining(code,pageable);
    	}else if (asana == "" && code == "" && asanaCategory != "" && category !="" && gender != "") {
//			// 11000  asanaCategory category gender ----24
			return repo.findAllByAsanaCategoryNameContainingAndCategoryTitleContainingAndGenderContaining(
						 asanaCategory,category, genderEnum,pageable);
		}else if (asana =="" && code == "" && asanaCategory != "" && category !="" && gender == "") {
//			// 11001  asanaCategory category  ----25
			return repo
					.findAllByAsanaCategoryNameContainingAndCategoryTitleContaining(asanaCategory,category,pageable);
		}else if (asana == "" && code == "" && asanaCategory != "" && category =="" && gender != "") {
//			// 11010  asanaCategory gender ----26
			return repo
					.findAllByAsanaCategoryNameContainingAndAndGenderContaining(asanaCategory, genderEnum,pageable);
		}else if (asana == "" && code == "" && asanaCategory != "" && category =="" && gender == "") {
//			// 11011  asanaCategory  ----27
			return repo
					.findAllByAsanaCategoryNameContaining(
							 asanaCategory,pageable);
		}else if (asana == "" && code == "" && asanaCategory == "" && category !="" && gender != "") {
//			// 11100  category gender ----28
			return repo
					.findAllByCategoryTitleContainingAndGenderContaining(
							category, genderEnum,pageable);
	}else if (asana == "" && code == "" && asanaCategory == "" && category !="" && gender == "") {
//			// 11101 category  ----29
			return repo
					.findAllByCategoryTitleContaining(
							category,pageable);
		}else if (asana == "" && code == "" && asanaCategory == "" && category =="" && gender != "") {
//			// 11110 gender ----30
			return repo
					.findAllByGenderContaining(genderEnum,pageable);
		}	
		else {
			LOGGER.info("Exit listByPage method -AsanaExecutionScoreService");
            return repo.findAll(pageable);

		//	LOGGER.info("findAllByChampionshipStatusNotIn method called");
		//	return repo.findAllByChampionshipStatusNotIn(status,pageable);
		}
	}
}
//

