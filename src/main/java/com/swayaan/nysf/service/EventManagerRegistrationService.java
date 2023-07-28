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

import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.repository.EventManagerRegistrationRepository;
import com.swayaan.nysf.repository.EventManagerRepository;
import com.swayaan.nysf.repository.ParticipantRepository;

@Service
@Transactional
public class EventManagerRegistrationService {
	
	@Autowired EventManagerRegistrationRepository repo; 
	
	@Autowired
	EventManagerRepository eventmanagerRepo;
	


	
	public static final int RECORDS_PER_PAGE = 10;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerRegistrationService.class);


	public List<EventManagerRegistration> showEventManagerForm() {
		LOGGER.info("Entered showEventManagerForm method -EventManagerRegistrationService");
		LOGGER.info("Exit showEventManagerForm method -EventManagerRegistrationService");
		return repo.findAll();
	}

	public EventManagerRegistration save(EventManagerRegistration eventManagerRegistration) {
		LOGGER.info("Entered save method -EventManagerRegistrationService");
		LOGGER.info("Exit save method -EventManagerRegistrationService");
    return repo.save(eventManagerRegistration);
	}

	public List<EventManagerRegistration> listAllNonApprovedEventManagers() {
		LOGGER.info("Entered listAllNonApprovedEventManagers method -EventManagerRegistrationService");
		LOGGER.info("Exit listAllNonApprovedEventManagers method -EventManagerRegistrationService");
     return (List<EventManagerRegistration>) repo.findByApprovalStatusFalse();
	}

	public void updateEventManagerRegistrationEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateEventManagerRegistrationEnabledStatus method -EventManagerRegistrationService");
		LOGGER.info("Exit updateEventManagerRegistrationEnabledStatus method -EventManagerRegistrationService");
    repo.updateEnabledStatus(id, enabled);
		
	}

	public EventManagerRegistration get(Integer id) throws EventManagerNotFoundException {
		LOGGER.info("Entered get method -EventManagerRegistrationService");
     	try {
    		LOGGER.info("Exit get method -EventManagerRegistrationService");
      	return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EventManagerNotFoundException("Could not find any Event Manager with ID " + id);
		}
	}

	
	public boolean isEmailUnique(String email) {
		LOGGER.info("Entered isEmailUnique method -EventManagerRegistrationService");

		EventManagerRegistration eventmanagerRegistrationByEmail = repo.geteventmanagerRegistrationByEmail(email);
		
		EventManager eventmanagerByEmail = eventmanagerRepo.getEventManagerByEmail(email);
		
		if (eventmanagerRegistrationByEmail == null && eventmanagerByEmail == null) {
			return true;
		} else {
			LOGGER.info("Exit isEmailUnique method -EventManagerRegistrationService");
       	return false;
		}
	}

	

	public void updateEventManagerRegistrationApprovedStatus(Integer id, boolean approved) {
		LOGGER.info("Entered updateEventManagerRegistrationApprovedStatus method -EventManagerRegistrationService");
		LOGGER.info("Exit updateEventManagerRegistrationApprovedStatus method -EventManagerRegistrationService");
     repo.updateApprovalStatus(id, approved);
		
	}

	public void delete(Integer id) throws EventManagerNotFoundException {
		LOGGER.info("Entered delete method -EventManagerRegistrationService");
     Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new EventManagerNotFoundException("Could not find any Event Manager with ID " + id);
		}
		LOGGER.info("Exit delete method -EventManagerRegistrationService");
       repo.deleteById(id);
		
	}
	public Page<EventManagerRegistration> listByPage(int pageNum, String sortField, String sortDir, String name, String district) {
		LOGGER.info("Entered listByPage method -EventManagerRegistrationService");

		Sort sort = null;

		if(sortField.equals("name")) {
			sortField="firstName";
		}
		sort = Sort.by(sortField);
		

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != "" && district != "") {
			LOGGER.info("findAllByNameContainingAndJrnNumberContaining method called EventManagerRegistrationService");
			return repo.findAllByNameContainingAndDistrictContainingAndApprovalStatusFalse(name, district, pageable);
		} else if (name == "" && district != "") {
			LOGGER.info("findAllByJrnNumberContaining method called EventManagerRegistrationService");
			return repo.findAllByDistrictContainingAndApprovalStatusFalse(district, pageable);
		} else if (name != "" && district == "") {
			LOGGER.info("findAllByNameContaining method called EventManagerRegistrationService");
			return repo.findAllByNameContainingAndApprovalStatusFalse(name, pageable);
		} else {
			LOGGER.info("findAll method called -EventManagerRegistrationService");
			LOGGER.info("Entered listByPage method -EventManagerRegistrationService");
        	return repo.findByApprovalStatusFalse(pageable);
		}

	}

}

	

