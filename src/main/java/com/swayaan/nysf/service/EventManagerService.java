package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.repository.EventManagerRegistrationRepository;
import com.swayaan.nysf.repository.EventManagerRepository;
import com.swayaan.nysf.repository.RoleRepository;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

@Service
@Transactional
public class EventManagerService {

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	EventManagerRepository repo;
	
	@Autowired
	CommonUtil commonUtil;
	
//	@Autowired EventManagerRegistrationRepository eventrepo; 

	@Autowired
	PasswordEncodeUtil passwordEncodeUtil;
	@Value("${site.admin-mail}")
	private String siteAdminMail;

	private static final int ROLE_EVENT_MANAGER = 4;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerService.class);


	public static final int RECORDS_PER_PAGE = 10;

	public EventManager getEventManagerById(Integer id) throws EventManagerNotFoundException {
		LOGGER.info("Entered getEventManagerById method -EventManagerService");
 	Optional<EventManager> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new EventManagerNotFoundException("Event Manager with id " + id + " does not exist");
		}
		EventManager existingEventManager = byId.get();
		LOGGER.info("Exit getEventManagerById method -EventManagerService");
    	return existingEventManager;
	}

	public EventManager save(EventManager eventManager) throws EventManagerNotFoundException {
		LOGGER.info("Entered save method -EventManagerService");
	Optional<Role> role = roleRepo.findById(ROLE_EVENT_MANAGER);
		if(role.isPresent()) {
			eventManager.addRole(role.get());
		}
		boolean isUpdating = (eventManager.getId() != null);
		if (isUpdating) {
			EventManager existingEventManager = getEventManagerById(eventManager.getId());
			eventManager.setUserName(existingEventManager.getUserName());
			eventManager.setPassword(existingEventManager.getPassword());
			eventManager.setRoles(existingEventManager.getRoles());
			
		}
		
		LOGGER.info("Exit save method -EventManagerService");
     	return repo.save(eventManager);
	}

//	public EventManager save(EventManager eventManager) {
//		Optional<Role> role = roleRepo.findById(ROLE_EVENT_MANAGER);
//		if (role.isPresent()) {
//			eventManager.addRole(role.get());
//		}
//
//		User currentUser = CommonUtil.getCurrentUser();
//		boolean isUpdatingEventManager = (eventManager.getId() != null);
//		if (isUpdatingEventManager) {
//			EventManager existingEventManager = repo.findById(eventManager.getId()).get();
//
//				eventManager.setPassword(existingEventManager.getPassword());
//
//
//			String eventManagerEmail = eventManager.getEmail();
//			if (eventManager.equals(siteAdminMail)) {
//				// do not change role or status
//				eventManager.setRoles(existingEventManager.getRoles());
//				eventManager.setEnabled(true);
//			}
//			eventManager.setUserName(eventManagerEmail);
//			eventManager.setLastModifiedBy(currentUser);
//			eventManager.setLastModifiedTime(new Date());
//			eventManager.setCreatedBy(currentUser.getCreatedBy());
//			eventManager.setCreatedTime(currentUser.getCreatedTime());
//
//		} else {
//		
//			eventManager.setCreatedBy(currentUser);
//			eventManager.setCreatedTime(new Date());
//		}
//		return repo.save(eventManager);
//
//	}
	
	public List<EventManager> listAll() {
		LOGGER.info("Entered listAll method -EventManagerService");
		LOGGER.info("Exit listAll method -EventManagerService");
	return (List<EventManager>) repo.findAll();
	}

	public List<Role> listRoles() {
		LOGGER.info("Entered listRoles method -EventManagerService");
		LOGGER.info("Exit listRoles method -EventManagerService");
	return (List<Role>) roleRepo.findAll();
		}

	public EventManager get(Integer id) throws EventManagerNotFoundException{
		LOGGER.info("Entered get method -EventManagerService");
	try {
		LOGGER.info("Exit get method -EventManagerService");
	return repo.findById(id).get();
			} catch (NoSuchElementException ex) {
				throw new EventManagerNotFoundException("Could not find any eventmanager with ID " + id);
			}
		}

	public void delete(Integer id)throws EventManagerNotFoundException {
		LOGGER.info("Entered delete method -EventManagerService");
	Long countById = repo.countById(id);
			if (countById == null || countById == 0) {
				throw new EventManagerNotFoundException("Could not find any event manager with ID " + id);
			}
			LOGGER.info("Exit delete method -EventManagerService");
       repo.deleteById(id);
		}

	
	public boolean isEmailUnique(Integer id, String email) {
		LOGGER.info("Entered isEmailUnique method -EventManagerService");
    EventManager eventmanagerByEmail = repo.getEventManagerByEmail(email);

		if (eventmanagerByEmail == null) {
			return true;
		}

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (eventmanagerByEmail != null) {
				return false;
			}
		} else {
			if (eventmanagerByEmail.getId() != id) {
				return false;
			}
		}
		LOGGER.info("Exit isEmailUnique method -EventManagerService");
     	return true;
	}
	
		public void updateEventManagerEnabledStatus(Integer id, boolean enabled) {
			LOGGER.info("Entered updateEventManagerEnabledStatus method -EventManagerService");
			LOGGER.info("Exit updateEventManagerEnabledStatus method -EventManagerService");
        repo.updateEnabledStatus(id, enabled);
	}

		public Page<EventManager> listByPage(int pageNum, String sortField, String sortDir, String name,
				String ernNumber) {
			LOGGER.info("Entered listByPage method -EventManagerService");

			Sort sort = null;

			if(sortField.equals("name")) {
				sortField="firstName";
			}
			sort = Sort.by(sortField);
			

			sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

			Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
			if (name != "" && ernNumber != "") {
				LOGGER.info("findAllByNameContainingAndJrnNumberContaining method called -EventManagerService");
				return repo.findAllByNameContainingAndErnNumberContaining(name, ernNumber, pageable);
			} else if (name == "" && ernNumber != "") {
				LOGGER.info("findAllByJrnNumberContaining method called -EventManagerService");
				return repo.findAllByErnNumberContaining(ernNumber, pageable);
			} else if (name != "" && ernNumber == "") {
				LOGGER.info("findAllByNameContaining method called -EventManagerService");
				return repo.findAllByNameContaining(name, pageable);
			} else {
				LOGGER.info("findAll method called -EventManagerService");
				LOGGER.info("Exit listByPage method -EventManagerService");

				return repo.findAll(pageable);
			}

		}

		

		
	}
	

