package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.exception.JudgeRegistrationNotFoundException;
import com.swayaan.nysf.exception.ParticipantRegistrationNotFoundException;
import com.swayaan.nysf.repository.JudgeRegistrationRepository;

@Service
@Transactional
public class JudgeRegistrationService {
	
	@Autowired JudgeRegistrationRepository repo;
	
	public static final int RECORDS_PER_PAGE = 10;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudgeRegistrationService.class);

	
	public List<JudgeRegistration> listAllNonApprovedJudges() {
		LOGGER.info("Entered listAllNonApprovedJudges method -JudgeRegistrationService");
		LOGGER.info("Exit listAllNonApprovedJudges method -JudgeRegistrationService");
     return (List<JudgeRegistration>) repo.findByApprovalStatusFalse();
	}
	
	public JudgeRegistration save(JudgeRegistration judgeRegistration) {
		LOGGER.info("Entered save method -JudgeRegistrationService");
		LOGGER.info("Exit save method -JudgeRegistrationService");
     return repo.save(judgeRegistration);
	}

	public void updateJudgeRegistrationEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateJudgeRegistrationEnabledStatus method -JudgeRegistrationService");
		LOGGER.info("Exit updateJudgeRegistrationEnabledStatus method -JudgeRegistrationService");
        repo.updateEnabledStatus(id, enabled);
			}
	
	public void updateJudgeRegistrationApprovedStatus(Integer id, boolean approvalStatus) {
		LOGGER.info("Entered updateJudgeRegistrationApprovedStatus method -JudgeRegistrationService");
		LOGGER.info("Exit updateJudgeRegistrationApprovedStatus method -JudgeRegistrationService");
    repo.updateApprovalStatus(id, approvalStatus);
	}


	public JudgeRegistration get(Integer id) throws JudgeRegistrationNotFoundException{
		LOGGER.info("Entered get method -JudgeRegistrationService");
		try {
			LOGGER.info("Exit get method -JudgeRegistrationService");
    	return repo.findById(id).get();
	}
		catch(NoSuchElementException ex) {
		throw new JudgeRegistrationNotFoundException("Could not find any Judge with ID \" + id");
		}
	}

	
	public void delete(Integer id) throws JudgeRegistrationNotFoundException {
		LOGGER.info("Entered delete method -JudgeRegistrationService");
       Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new JudgeRegistrationNotFoundException("Could not find any Judge with ID " + id);
		}
		LOGGER.info("Exit delete method -JudgeRegistrationService");
		repo.deleteById(id);
	}
	public Page<JudgeRegistration> listByPage(int pageNum, String sortField, String sortDir, String name) {
		LOGGER.info("Entered listByPage method -JudgeRegistrationService");

		Sort sort = null;
		if(sortField.equals("name")) {
			sortField="firstName";
		}
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != null && !name.isEmpty()) {
			return repo.findAllByNameContainingAndApprovalStatusFalse(name, pageable);
		}
		LOGGER.info("Exit listByPage method -JudgeRegistrationService");
       return repo.findByApprovalStatusFalse(pageable);
	}

}

