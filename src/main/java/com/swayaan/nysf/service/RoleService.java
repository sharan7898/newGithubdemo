package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.repository.RoleRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class RoleService {

	public static final int RECORDS_PER_PAGE = 10;

	@Autowired
	RoleRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

	public List<Role> listAllRoles() {
		LOGGER.info("Entered listAllRoles method -RoleService");
		LOGGER.info("Exit listAllRoles method -RoleService");
    return repo.findAllByOrderByNameAsc();
	}

	public Role save(Role role) {
		LOGGER.info("Entered save method -RoleService");

		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingRole = (role.getId() != null);
		if (isUpdatingRole) {
			Role existingRole = repo.findById(role.getId()).get();

			role.setLastModifiedBy(currentUser);
			role.setLastModifiedTime(new Date());
			role.setCreatedBy(existingRole.getCreatedBy());
			role.setCreatedTime(existingRole.getCreatedTime());

		} else {
			role.setCreatedBy(currentUser);
			role.setCreatedTime(new Date());
		}
		// set status to true for Administrator
		if (role.getName().equals("Administrator")) {
			role.setEnabled(true);
		}
		LOGGER.info("Exit save method -RoleService");
	return repo.save(role);
	}

	public Role get(Integer id) throws RoleNotFoundException {
		LOGGER.info("Entered get method -RoleService");
	try {
		LOGGER.info("Exit get method -RoleService");
     return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any role with ID " + id);
			throw new RoleNotFoundException("Could not find any role with ID " + id);
		}
	}

	public void delete(Integer id) throws RoleNotFoundException {
		LOGGER.info("Entered delete method -RoleService");
     	Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any role with ID " + id);
			throw new RoleNotFoundException("Could not find any role with ID " + id);
		}
		LOGGER.info("Exit delete method -RoleService");
      repo.deleteById(id);
	}

	public void updateRoleEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateRoleEnabledStatus method -RoleService");
		LOGGER.info("Exit updateRoleEnabledStatus method -RoleService");
     repo.updateEnabledStatus(id, enabled);
	}

	public Role getRoleByName(String name) {
		LOGGER.info("Entered getRoleByName method -RoleService");
		LOGGER.info("Exit getRoleByName method -RoleService");
	return repo.findByName(name);
	}

	public Set<Role> getRoleById(Integer id) {
		LOGGER.info("Entered getRoleById method -RoleService");
		LOGGER.info("Exit getRoleById method -RoleService");
    return repo.findAllById(id);
	}

	public List<Role> getAllJudgeRoles() {
		LOGGER.info("Entered getAllJudgeRoles method -RoleService");
		LOGGER.info("Exit getAllJudgeRoles method -RoleService");
	return repo.findByJudge(true);
	}

	public Boolean checkRoleName(String name) {
		LOGGER.info("Entered checkRoleName method -RoleService");
		LOGGER.info("Exit checkRoleName method -RoleService");
		return repo.existsByName(name);
	}

	public Page<Role> listByPage(int pageNum, String sortField, String sortDir, String name) {
		LOGGER.info("Entered listByPage method -RoleService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != null && !name.isEmpty()) {
			return repo.findAllByNameContaining(name, pageable);
		}
		LOGGER.info("Exit listByPage method -RoleService");
         return repo.findAll(pageable);
	}

}
