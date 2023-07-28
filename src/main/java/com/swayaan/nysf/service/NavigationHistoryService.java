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

import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.repository.NavigationHistoryRepository;

@Service
public class NavigationHistoryService {
	
	@Autowired NavigationHistoryRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(NavigationHistoryService.class);
	
	public static final int RECORDS_PER_PAGE = 20;


	public List<NavigationHistory> listAllNavigationHistory(){
		LOGGER.info("Entered listAllNavigationHistory method -NavigationHistoryService");
		LOGGER.info("Exit listAllNavigationHistory method -NavigationHistoryService");
	return repo.findAllByOrderByCreatedTimeDesc();
	}
	
	public NavigationHistory save(NavigationHistory navigationHistory) {
		LOGGER.info("Entered save method -NavigationHistoryService");
		LOGGER.info("Exit save method -NavigationHistoryService");
	return repo.save(navigationHistory);
	}
	
	public List<NavigationHistory> getNavigationHistoryInDays(Integer days) {
		LOGGER.info("Entered getNavigationHistoryInDays method -NavigationHistoryService");
		LOGGER.info("Exit getNavigationHistoryInDays method -NavigationHistoryService");
		List<NavigationHistory> navigationHistory = repo.findNavigationHistory(days);
		return navigationHistory;
	}
	
	public void delete(Integer id) {
		LOGGER.info("Entered delete method -NavigationHistoryService");
		LOGGER.info("Exit delete method -NavigationHistoryService");
	repo.deleteById(id);
	}
	
	public List<NavigationHistory> listByUser(String user) {
		LOGGER.info("Entered listByUser method -NavigationHistoryService");

		if (user != null || user != "") {
			return repo.findAllByUserFirstNameContaining(user);
		}  else {
			LOGGER.info("Exit listByUser method -NavigationHistoryService");
		return (List<NavigationHistory>) repo.findAll();
		}
	}
	public Page<NavigationHistory> listByPage(int pageNum, String sortField, String sortDir, String userName,
			String roleName, String url, String createdTime) {
		LOGGER.info("Entered listByPage method -NavigationHistoryService");

		Sort sort = null;

		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (userName != "" && roleName != "" && url != "" && createdTime != "") {
//0000 userName roleName url createdTime
			return repo	.findAllByUserNameContainingAndRoleNameContainingAndUrlContainingAndCreatedTimeContaining(userName, roleName, url, createdTime, pageable);

		} else if (userName != "" && roleName != "" && url != "" && createdTime == "") {
			// userName roleName url 0001
			return repo.findAllByUserNameContainingAndRoleNameContainingAndUrlContaining(userName, roleName,
					url, pageable);
		} else if (userName != "" && roleName != "" && url == "" && createdTime != "") {
			//userName roleName  createdTime 0010
			return repo.findAllByUserNameContainingAndRoleNameContainingAndCreatedTimeContaining(userName, roleName,
					createdTime, pageable);
		} else if (userName != "" && roleName != "" && url == "" && createdTime == "") {
			// userName roleName 0011
			return repo.findAllByUserNameContainingAndRoleNameContaining(userName, roleName, pageable);
		} else if (userName != "" && roleName == "" && url != "" && createdTime != "") {
			// userName  url createdTime 0100
			return repo.findAllByUserNameContainingAndUrlContainingAndCreatedTimeContaining(userName,
					roleName, url, pageable);
		} else if (userName != "" && roleName == "" && url != "" && createdTime == "") {
			// userName url  0101
			return repo.findAllByUserNameContainingAndUrlContaining(userName, url, pageable);
		} else if (userName != "" && roleName == "" && url == "" && createdTime != "") {
			// userName  createdTime 0110
			return repo.findAllByUserNameContainingAndCreatedTimeContaining(userName, createdTime, pageable);
		}

		else if (userName != "" && roleName == "" && url == "" && createdTime == "") {
			// userName  0111
			return repo.findAllByUserNameContaining(userName, pageable);
		} else if (userName == "" && roleName != "" && url != "" && createdTime != "") {
			//  roleName url createdTime 1000
			return repo.findAllByRoleNameContainingAndUrlContainingAndCreatedTimeContaining(
					roleName, url, createdTime, pageable);
		} else if (userName == "" && roleName != "" && url != "" && createdTime == "") {
			//  roleName url  1001
			return repo.findAllByRoleNameContainingAndUrlContaining(roleName, url,
					pageable);
		}

		else if (userName == "" && roleName != "" && url == "" && createdTime != "") {
			//  roleName  createdTime 1010
			return repo.findAllByRoleNameContainingAndCreatedTimeContaining(roleName, createdTime,
					pageable);
		}

		else if (userName == "" && roleName != "" && url == "" && createdTime == "") {
			//  roleName  1011
			return repo.findAllByRoleNameContaining(roleName, pageable);
		}

		else if (userName == "" && roleName == "" && url != "" && createdTime != "") {
			// url createdTime 1100
			return repo.findAllByUrlContainingAndCreatedTimeContaining(url,
					createdTime, pageable);
		}

		else if (userName == "" && roleName == "" && url != "" && createdTime == "") {
			//  url  1101
			return repo.findAllByUrlContaining(url, pageable);
		}

		else if (userName == "" && roleName == "" && url == "" && createdTime != "") {
			//  createdTime 1110
			return repo.findAllByCreatedTimeContaining(createdTime, pageable);
		}

		else {
			LOGGER.info("findAll method called -NavigationHistoryService");
			LOGGER.info("Entered listByPage method -NavigationHistoryService");
		return repo.findAll(pageable);
		}
	}

	public List<NavigationHistory> getNavigationHistoryByUserId(User user) {
		return repo.findAllByUser(user);
	}
}

/*public Page<NavigationHistory> listByPage(int pageNum, String sortField, String sortDir, String userName, String roleName) {
	Sort sort = null;
	
	sort = Sort.by(sortField);
	

	sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

	Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
	if (userName != "" && roleName != "") {
		LOGGER.info("findAllByUserNameContainingAndRoleNameContaining method called");
		return repo.findAllByUserNameContainingAndRoleNameContaining(userName, roleName, pageable);
	} else if (userName == "" && roleName != "") {
		LOGGER.info("findAllByRoleNameContaining method called");
		return repo.findAllByRoleNameContaining(roleName, pageable);
	} else if (userName != "" && roleName == "") {
		LOGGER.info("findAllByUserNameContaining method called");
		return repo.findAllByUserNameContaining(userName, pageable);
	} else {
		LOGGER.info("findAll method called");
		return repo.findAll(pageable);
	}

}

} */

