package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.controller.ManageEventRegistrationController;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.NotFoundException;
import com.swayaan.nysf.repository.EventRegistrationRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
@Transactional
public class EventRegistrationService {

	@Autowired
	EventRegistrationRepository repo;
	
	public static final int RECORDS_PER_PAGE = 10;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationService.class);



	public Boolean checkEventName(String name) {
		LOGGER.info("Entered checkEventName method -EventRegistrationService");
		LOGGER.info("Exit checkEventName method -EventRegistrationService");
	return repo.existsByName(name);
	}

	public EventRegistration save(EventRegistration eventRegistration) throws Exception {
		LOGGER.info("Entered save method -EventRegistrationService");
	User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingEventRegistration = (eventRegistration.getId() != null);
		if (isUpdatingEventRegistration) {
			EventRegistration existingEventRegistration = repo.findById(eventRegistration.getId()).get();
			if(!existingEventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING))
			{
				throw new Exception("Unable to edit.Please check your championship status");
			}
			eventRegistration.setEventCategory(existingEventRegistration.getEventCategory());
			eventRegistration.setModifiedBy(currentUser);
			eventRegistration.setLastModifiedTime(new Date());
			eventRegistration.setCreatedBy(existingEventRegistration.getCreatedBy());
			eventRegistration.setCreatedTime(existingEventRegistration.getCreatedTime());
			eventRegistration.setApprovalStatus(existingEventRegistration.getApprovalStatus());

		} else {
			eventRegistration.setApprovalStatus(RegistrationStatusEnum.PENDING);
			eventRegistration.setCreatedBy(currentUser);
			eventRegistration.setCreatedTime(new Date());

		}
		EventRegistration savedEventRegistration = repo.save(eventRegistration);
		LOGGER.info("Exit save method -EventRegistrationService");
     	return savedEventRegistration;

	}

	public EventRegistration findById(Integer id) {
		LOGGER.info("Entered findById method -EventRegistrationService");
	Optional<EventRegistration> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new NotFoundException("EventRegistration with id " + id + " does not exist");
		}
		EventRegistration eventRegistration = byId.get();
		LOGGER.info("Exit findById method -EventRegistrationService");
      return eventRegistration;
	}

	public List<EventRegistration> findAll() {
		LOGGER.info("Entered findAll method -EventRegistrationService");
		LOGGER.info("Exit findAll method -EventRegistrationService");
	return (List<EventRegistration>) repo.findAll();
	}

	public List<EventRegistration> findAllForCurrentUser(User currentUser) {
		LOGGER.info("Entered findAllForCurrentUser method -EventRegistrationService");
		LOGGER.info("Exit findAllForCurrentUser method -EventRegistrationService");
	return repo.findAllByCreatedBy(currentUser);
	}

	public EventRegistration findByIdAndCurrentUser(Integer id, User currentUser) {
		LOGGER.info("Entered findByIdAndCurrentUser method -EventRegistrationService");
		LOGGER.info("Exit findByIdAndCurrentUser method -EventRegistrationService");
	return repo.findAllByIdAndCreatedBy(id,currentUser);
	}

	
	// For Admin
	
	public List<EventRegistration> listAllNonApprovedEvents() {
		LOGGER.info("Entered listAllNonApprovedEvents method -EventRegistrationService");
    List<RegistrationStatusEnum> approvalStatus=new ArrayList<>();
		approvalStatus.add(RegistrationStatusEnum.APPROVED);
		approvalStatus.add(RegistrationStatusEnum.REJECTED);
		approvalStatus.add(RegistrationStatusEnum.PENDING);
		LOGGER.info("Exit listAllNonApprovedEvents method -EventRegistrationService");
	return repo.findAllByApprovalStatusIn(approvalStatus);
	}
	public List<EventRegistration> getAllNonApprovedEvents() {
		LOGGER.info("Entered getAllNonApprovedEvents method -EventRegistrationService");
   List<RegistrationStatusEnum> approvalStatus=new ArrayList<>();	
		approvalStatus.add(RegistrationStatusEnum.PENDING);
		LOGGER.info("Exit getAllNonApprovedEvents method -EventRegistrationService");
   return repo.findAllByApprovalStatusIn(approvalStatus);
	}
	

	public void updateEventRegistrationApprovedStatus(EventRegistration eventRegistration, RegistrationStatusEnum status) {
		//eventRegistration.setApprovalStatus(status);
		LOGGER.info("Entered updateEventRegistrationApprovedStatus method -EventRegistrationService");
		LOGGER.info("Exit updateEventRegistrationApprovedStatus method -EventRegistrationService");
     repo.updateApprovalStatus(eventRegistration.getId(),status);
		
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -EventRegistrationService");
     Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new NotFoundException("Could not find any Championship  with ID " + id);
		}
		LOGGER.info("Exit delete method -EventRegistrationService");
     repo.deleteById(id);
		
	}
	
	public Boolean checkEventNameForEditFlow(String name,Integer id) {
		LOGGER.info("Entered checkEventNameForEditFlow method -EventRegistrationService");
		LOGGER.info("Exit checkEventNameForEditFlow method -EventRegistrationService");
	return repo.existsByNameAndIdNot(name,id);
	}

	public List<EventRegistration> getEventRegistrationByCreatedBy(Integer eventManagerId) {
		LOGGER.info("Entered getEventRegistrationByCreatedBy method -EventRegistrationService");
		LOGGER.info("Exit getEventRegistrationByCreatedBy method -EventRegistrationService");
	return repo.findByCreatedBy(eventManagerId);
	}

	public Page<EventRegistration> listByPage(int pageNum, String sortField, String sortDir, String name, String location) {
		LOGGER.info("Entered listByPage method -EventRegistrationService");

		Sort sort = null;

		sort = Sort.by(sortField);
		

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != "" && location != "") {
			LOGGER.info("findAllByNameContainingAndJrnNumberContaining method called -EventRegistrationService");
			return repo.findAllByNameContainingAndLocationContaining(name, location, pageable);
		} else if (name == "" && location != "") {
			LOGGER.info("findAllByJrnNumberContaining method called -EventRegistrationService");
			return repo.findAllByLocationContaining(location, pageable);
		} else if (name != "" && location == "") {
			LOGGER.info("findAllByNameContaining method called -EventRegistrationService");
			return repo.findAllByNameContaining(name, pageable);
		} else {
			LOGGER.info("findAll method called  -EventRegistrationService");
			LOGGER.info("Exit listByPage method -EventRegistrationService");

			return repo.findAll(pageable);
		}

	}

//	public List<EventRegistration> getEventRegistrationByStatusPending(boolean status) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	public List<EventRegistration> getEventRegistrationByStatusPending() {
//		// TODO Auto-generated method stub
//		List<RegistrationStatusEnum> approvalStatus=new ArrayList<RegistrationStatusEnum>();
//		approvalStatus.add(RegistrationStatusEnum.PENDING);
//		return repo.findAllByApprovalStatusIn(approvalStatus);
//	}


	}

	
	
	
