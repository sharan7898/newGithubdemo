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
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.AsanaEvaluationQuestionsRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class AsanaEvaluationQuestionsService {

	@Autowired
	AsanaEvaluationQuestionsRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(AsanaEvaluationQuestionsService.class);
	
	public static final int RECORDS_PER_PAGE = 10;

	public List<AsanaEvaluationQuestions> listAllAsanaEvaluationQuestions() {
		LOGGER.info("Entered listAllAsanaEvaluationQuestions method -AsanaEvaluationQuestionsService");
		LOGGER.info("Exit listAllAsanaEvaluationQuestions method -AsanaEvaluationQuestionsService");

		return repo.findAllByOrderByIdAsc();
	}

	public AsanaEvaluationQuestions save(AsanaEvaluationQuestions asanaEvaluationQuestions) {
		LOGGER.info("Entered save method -AsanaEvaluationQuestionsService");
      	User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingAsanaEvaluationQuestions = (asanaEvaluationQuestions.getId() != null);
		if (isUpdatingAsanaEvaluationQuestions) {
			AsanaEvaluationQuestions existingAsanaEvaluationQuestions = repo.findById(asanaEvaluationQuestions.getId())
					.get();
			asanaEvaluationQuestions.setLastModifiedBy(currentUser);
			asanaEvaluationQuestions.setLastModifiedTime(new Date());
			asanaEvaluationQuestions.setCreatedBy(existingAsanaEvaluationQuestions.getCreatedBy());
			asanaEvaluationQuestions.setCreatedTime(existingAsanaEvaluationQuestions.getCreatedTime());

		} else {
			asanaEvaluationQuestions.setCreatedBy(currentUser);
			asanaEvaluationQuestions.setCreatedTime(new Date());
		}
		LOGGER.info("Exit save method -AsanaEvaluationQuestionsService");

		return repo.save(asanaEvaluationQuestions);
	}

	public AsanaEvaluationQuestions get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -AsanaEvaluationQuestionsService");

		try {
			LOGGER.info("Exit get method -AsanaEvaluationQuestionsService");
         return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EntityNotFoundException("Could not find the question for the Asana");
		}
	}

	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -AsanaEvaluationQuestionsService");

		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new EntityNotFoundException("Could not find the question for the Asana");
		}
		LOGGER.info("Exit delete method -AsanaEvaluationQuestionsService");
       repo.deleteById(id);
	}

	public List<AsanaEvaluationQuestions> findAllByTypeAndAsanaCategoryWithQuestionTypePARTICIPANTSPECIFICASANAQUESTION(
			JudgeType judgeType, AsanaCategory asanaCategory) {
		LOGGER.info("Entered findAllByTypeAndAsanaCategoryWithQuestionTypePARTICIPANTSPECIFICASANAQUESTION method -AsanaEvaluationQuestionsService");
     	boolean forEachAsana = true;
		boolean commonForEachParticpant = false;
		LOGGER.info("Exit findAllByTypeAndAsanaCategoryWithQuestionTypePARTICIPANTSPECIFICASANAQUESTION method -AsanaEvaluationQuestionsService");
        return repo.findAllByTypeAndAsanaCategoryAndForEachAsanaAndCommonForEachParticipant(judgeType,
				asanaCategory, forEachAsana, commonForEachParticpant);
	}

	public List<AsanaEvaluationQuestions> findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(
			JudgeType judgeType, AsanaCategory asanaCategory) {
		LOGGER.info("Entered findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION method -AsanaEvaluationQuestionsService");
      	boolean forEachAsana = false;
		boolean commonForEachParticpant = false;
		LOGGER.info("Exit findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION method -AsanaEvaluationQuestionsService");
        return repo.findAllByTypeAndAsanaCategoryAndForEachAsanaAndCommonForEachParticipant(judgeType,
				asanaCategory, forEachAsana, commonForEachParticpant);
	}

	public List<AsanaEvaluationQuestions> findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(
			JudgeType judgeType, AsanaCategory asanaCategory) {
		LOGGER.info("Entered findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA method -AsanaEvaluationQuestionsService");

		boolean forEachAsana = true;
		boolean commonForEachParticpant = true;
		LOGGER.info("Exit findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA method -AsanaEvaluationQuestionsService");
        return repo.findAllByTypeAndAsanaCategoryAndForEachAsanaAndCommonForEachParticipant(judgeType,
				asanaCategory, forEachAsana, commonForEachParticpant);
	}

	public List<AsanaEvaluationQuestions> findAllByTypeAndAsanaCategory(JudgeType judgeType,
			AsanaCategory asanaCategory) {
		LOGGER.info("Entered findAllByTypeAndAsanaCategory method -AsanaEvaluationQuestionsService");
		LOGGER.info("Exit findAllByTypeAndAsanaCategory method -AsanaEvaluationQuestionsService");
     	return repo.findAllByTypeAndAsanaCategory(judgeType,asanaCategory);
	}

//	public List<AsanaEvaluationQuestions> listAllAsanaEvaluationQuestionsByChampionship(Championship championship) {
//		return repo.findByChampionship(championship);
//	}
	
	public Page<AsanaEvaluationQuestions> listByPage(int pageNum, String sortField, String sortDir, String asanaCategory , String type) {
		LOGGER.info("Entered listByPage method -AsanaEvaluationQuestionsService");

		Sort sort = null;
		sort = Sort.by(sortField);
		

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (type != "" && asanaCategory != "") {
			LOGGER.info("findAllByJudgeTypeContainingAndAsanaCategoryContaining method called -AsanaEvaluationQuestionsService");
			return repo.findAllByAsanaCategoryNameContainingAndTypeTypeContaining(asanaCategory,type, pageable);
		} else if (type == "" && asanaCategory != "") {
			LOGGER.info("findAllByAsanaCategoryContaining method called -AsanaEvaluationQuestionsService");
			return repo.findAllByAsanaCategoryNameContaining(asanaCategory, pageable);
		} else if (type != "" && asanaCategory == "") {
			LOGGER.info("findAllByjudgeTypeContaining method called AsanaEvaluationQuestionsService");
			return repo.findAllByTypeTypeContaining(type, pageable);
		} else {
			LOGGER.info("findAll method called -AsanaEvaluationQuestionsService");
			LOGGER.info("Exit listByPage method -AsanaEvaluationQuestionsService");

			return repo.findAll(pageable);
		}

	}

}



