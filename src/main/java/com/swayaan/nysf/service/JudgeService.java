package com.swayaan.nysf.service;

import java.util.ArrayList;

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

import com.swayaan.nysf.controller.ManageJudgeController;
import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.repository.JudgeRepository;
import com.swayaan.nysf.repository.RoleRepository;
import com.swayaan.nysf.repository.UserRepository;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class JudgeService {

	public static final int RECORDS_PER_PAGE = 10;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	JudgeRepository repo;

	@Autowired
	private PasswordEncodeUtil passwordEncodeUtil;

	private static final Integer JUDGE_ROLE_ID = 2;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudgeService.class);

	@Value("${site.admin-mail}")
	private String siteAdminMail;

	public List<Judge> listAll() {
		LOGGER.info("Entered listAll method -JudgeService");
		LOGGER.info("Exit listAll method -JudgeService");
		return (List<Judge>) repo.findAll();
	}

	public List<Role> listRoles() {
		LOGGER.info("Entered listRoles method -JudgeService");
		LOGGER.info("Exit listRoles method -JudgeService");
		return (List<Role>) roleRepo.findAll();
	}

	/*
	 * public List<Country> listAllCountries(){ return (List<Country>)
	 * countryRepo.findAllByOrderByNameAsc(); }
	 */

	public Judge getJudgeById(Integer id) throws JudgeNotFoundException {
		LOGGER.info("Entered getJudgeById method -JudgeService");
		Optional<Judge> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new JudgeNotFoundException(" Judge with id " + id + " does not exist");
		}
		Judge existingJudge = byId.get();
		LOGGER.info("Exit getJudgeById method -JudgeService");
		return existingJudge;
	}

	public Judge save(Judge judge) throws JudgeNotFoundException {
		LOGGER.info("Entered save method -JudgeService");
		Optional<Role> role = roleRepo.findById(JUDGE_ROLE_ID);
		if (role.isPresent()) {
			judge.addRole(role.get());
		}
		boolean isUpdating = (judge.getId() != null);
		if (isUpdating) {
			Judge existingJudge = getJudgeById(judge.getId());
			judge.setUserName(existingJudge.getUserName());
			judge.setPassword(existingJudge.getPassword());
			judge.setRoles(existingJudge.getRoles());
			
		}
		
		LOGGER.info("Exit save method -JudgeService");
		return repo.save(judge);
	}

//	public Judge save(Judge judge) {
//		Optional<Role> role = roleRepo.findById(JUDGE_ROLE_ID);
//		if (role.isPresent()) {
//			judge.addRole(role.get());
//		}
//
//		User currentUser = CommonUtil.getCurrentUser();
//		boolean isUpdatingJudge = (judge.getId() != null);
//
//		if (isUpdatingJudge) {
//			Judge existingJudge = repo.findById(judge.getId()).get();
//
//			judge.setPassword(existingJudge.getPassword());
//
//			String judgeEmail = judge.getEmail();
//			if (judgeEmail.equals(siteAdminMail)) {
//				// do not change role or status
//				judge.setRoles(existingJudge.getRoles());
//				judge.setEnabled(true);
//			}
//			judge.setUserName(existingJudge.getUserName());
//			// judge.setUserName(judgeEmail);
//		
//			judge.setLastModifiedBy(currentUser);
//			judge.setLastModifiedTime(new Date());
//			judge.setCreatedBy(currentUser.getCreatedBy());
//			judge.setCreatedTime(currentUser.getCreatedTime());
//
//		} else {
//
//			judge.setCreatedBy(currentUser);
//			judge.setCreatedTime(new Date());
//		}
//		return repo.save(judge);
//
//	}

//	public void addJudge(Judge judge) {
//		String encodedPassword = passwordEncodeUtil.encodePassword(judge.getPassword());
//		judge.setUserName(judge.getEmail());
//		judge.setPassword(encodedPassword);
//		judge.setEnabled(false);
//		judge.setCreatedTime(new Date());
//
//		String randomcode = RandomString.make(64);
////	user.setVerificationCode(randomcode);
//
//		repo.save(judge);
//	}

	public boolean isEmailUnique(Integer id, String email) {
		LOGGER.info("Entered isEmailUnique method -JudgeService");

		User judgeByEmail = userRepo.getUserByEmail(email);

		if (judgeByEmail == null) {
			return true;
		}

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (judgeByEmail != null) {
				return false;
			}
		} else {
			if (judgeByEmail.getId() != id) {
				return false;
			}
		}
		LOGGER.info("Exit isEmailUnique method -JudgeService");
		return true;
	}

	public Judge get(Integer id) throws JudgeNotFoundException {
		LOGGER.info("Entered get method -JudgeService");
		try {
			LOGGER.info("Exit get method -JudgeService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new JudgeNotFoundException("Could not find any Judge with ID " + id);
		}
	}

	public void delete(Integer id) throws JudgeNotFoundException {
		LOGGER.info("Entered delete method -JudgeService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new JudgeNotFoundException("Could not find any Judge with ID " + id);
		}
		LOGGER.info("Exit delete method -JudgeService");
		repo.deleteById(id);
	}

	public void updateJudgeEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateJudgeEnabledStatus method -JudgeService");
		LOGGER.info("Exit updateJudgeEnabledStatus method -JudgeService");
		repo.updateEnabledStatus(id, enabled);
	}

	public List<Judge> getNonSelectedEnabledReferees(Integer championshipRefereePanelId) {
		LOGGER.info("Entered getNonSelectedEnabledReferees method -JudgeService");
		boolean enabled = true;
		List<Judge> listJudges = repo
				.findAllBychampionshipRefereePanelIdAndEnabledOrderByIdAsc(championshipRefereePanelId, enabled);
		List<Judge> listJudgeTypeUsers = new ArrayList<Judge>();
		for (Judge judge : listJudges) {
//			if(user.getIsJudge()){
//				listJudgeTypeUsers.add(user);
//			}
		}
		LOGGER.info("Exit getNonSelectedEnabledReferees method -JudgeService");
		return listJudges;
	}

	public List<Judge> listAllReferees() {
		LOGGER.info("Entered listAllReferees method -JudgeService");
		List<Judge> listAllJudges = listAll();
		List<Judge> listRefereeJudges = new ArrayList<>();
		for (Judge judge : listAllJudges) {
			if (judge.hasRole("Judge")) {
				listRefereeJudges.add(judge);
			}
		}
		LOGGER.info("Exit listAllReferees method -JudgeService");
		return listRefereeJudges;
	}

	public Judge findById(Integer id) throws JudgeNotFoundException {
		LOGGER.info("Entered findById method -JudgeService");
		try {
			LOGGER.info("Exit findById method -JudgeService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new JudgeNotFoundException("Could not find any user with ID " + id);
		}
	}

	public Page<Judge> listByPage(int pageNum, String sortField, String sortDir, String name, String jrnNumber) {
		LOGGER.info("Entered listByPage method -JudgeService");

		Sort sort = null;

		if (sortField.equals("name")) {
			sortField = "firstName";
		}
		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != "" && jrnNumber != "") {
			LOGGER.info("findAllByNameContainingAndJrnNumberContaining method called -JudgeService");
			return repo.findAllByNameContainingAndJrnNumberContaining(name, jrnNumber, pageable);
		} else if (name == "" && jrnNumber != "") {
			LOGGER.info("findAllByJrnNumberContaining method called -JudgeService");
			return repo.findAllByJrnNumberContaining(jrnNumber, pageable);
		} else if (name != "" && jrnNumber == "") {
			LOGGER.info("findAllByNameContaining method called -JudgeService");
			return repo.findAllByNameContaining(name, pageable);
		} else {
			LOGGER.info("findAll method called -JudgeService");
			LOGGER.info("Exit listByPage method -JudgeService");
			return repo.findAll(pageable);
		}

	}

}
