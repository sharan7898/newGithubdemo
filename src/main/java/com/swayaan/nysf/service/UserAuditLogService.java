package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.repository.UserAuditLogRepository;


@Service
public class UserAuditLogService {
	
	@Autowired
	UserAuditLogRepository repo;
	
	@Autowired
	UserService userService;
	
	public static final int RECORDS_PER_PAGE = 20;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuditLogService.class);


	public UserAuditLog saveLoginTime(UserAuditLog userAuditLog) {
		LOGGER.info("Entered saveLoginTime method -UserAuditLogService");
		LOGGER.info("Exit saveLoginTime method -UserAuditLogService");
	return repo.save(userAuditLog);
	}
	
	public UserAuditLog saveLogoutTime(UserAuditLog userAuditLog) {
		LOGGER.info("Entered saveLogoutTime method -UserAuditLogService");
		LOGGER.info("Exit saveLogoutTime method -UserAuditLogService");
    return repo.save(userAuditLog);
	}

	public UserAuditLog getByUserAndSessionId(User user, String sessionId) {
		LOGGER.info("Entered getByUserAndSessionId method -UserAuditLogService");
		LOGGER.info("Exit getByUserAndSessionId method -UserAuditLogService");
	return repo.findByLoggedUserAndSessionId(user,sessionId);
	}

	public List<UserAuditLog> listAll() {
		LOGGER.info("Entered listAll method -UserAuditLogService");
		LOGGER.info("Exit listAll method -UserAuditLogService");
	return repo.findAllByOrderByLoginTimeDesc();
	}
	
	public List<UserAuditLog> listByUser(String user) {
		LOGGER.info("Entered listByUser method -UserAuditLogService");
	if (user != null || user != "") {
			return repo.findAllByLoggedUserFirstNameContaining(user);
		}  else {
			LOGGER.info("Exit listByUser method -UserAuditLogService");
	return (List<UserAuditLog>) repo.findAll();
		}
	}

	public List<UserAuditLog> listUserAuditLogForUser(Integer id) {
		LOGGER.info("Entered listUserAuditLogForUser method -UserAuditLogService");
      	User user;
		try {
			user = userService.get(id);
			LOGGER.info("Exit listUserAuditLogForUser method -UserAuditLogService");
       	return (List<UserAuditLog>) repo.findByLoggedUser(user);
		} catch (UserNotFoundException e) {
			
		}
		return null;
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		LOGGER.info("Entered delete method -UserAuditLogService");
      Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user audit log with ID " + id);
		}
		LOGGER.info("Exit delete method -UserAuditLogService");
     	repo.deleteById(id);
	}

	public UserAuditLog getBySessionId(String sessionId) {
		LOGGER.info("Entered getBySessionId method -UserAuditLogService");
		LOGGER.info("Exit getBySessionId method -UserAuditLogService");
	return repo.findBySessionId(sessionId);
	}
	
public Page<UserAuditLog> listByPage(int pageNum, String sortField, String sortDir, String loggedUser, String ipAddress) {
	LOGGER.info("Entered listByPage method -UserAuditLogService");

	Sort sort = null;
	
	sort = Sort.by(sortField);
	

	sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

	Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
	if (loggedUser != "" && ipAddress != "") {
		LOGGER.info("findAllByLoggedUserContainingAndIpAddressContaining method called -UserAuditLogService");
		return repo.findAllByLoggedUserContainingAndIpAddressContaining(loggedUser, ipAddress, pageable);
	} else if (loggedUser == "" && ipAddress != "") {
		LOGGER.info("findAllByIpAddressContaining method called -UserAuditLogService");
		return repo.findAllByIpAddressContaining(ipAddress, pageable);
	} else if (loggedUser != "" && ipAddress == "") {
		LOGGER.info("findAllByLoggedUserContaining method called -UserAuditLogService");
		return repo.findAllByLoggedUserContaining(loggedUser, pageable);
	} else {
		LOGGER.info("findAll method called -UserAuditLogService");
		LOGGER.info("Exit listByPage method -UserAuditLogService");
     	return repo.findAll(pageable);
	}

}

}

