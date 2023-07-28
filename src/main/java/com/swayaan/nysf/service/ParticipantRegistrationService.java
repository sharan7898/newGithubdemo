package com.swayaan.nysf.service;

import java.util.List;

import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
//import com.swayaan.nysf.entity.Event;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.exception.ParticipantRegistrationNotFoundException;
import com.swayaan.nysf.repository.ParticipantRegistrationRepository;
import com.swayaan.nysf.repository.ParticipantRepository;

@Service
@Transactional
public class ParticipantRegistrationService {

	@Autowired
	ParticipantRegistrationRepository repo;

	@Autowired
	ParticipantRepository partirepo;

	@Autowired
	RecaptchaService captchaService;

	public static final int RECORDS_PER_PAGE = 10;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantRegistrationService.class);

	public List<ParticipantRegistration> showParticipantForm() {
		LOGGER.info("Entered showParticipantForm method -ParticipantRegistrationService");
		LOGGER.info("Exit showParticipantForm method -ParticipantRegistrationService");
		return repo.findAll();
	}

	public List<ParticipantRegistration> listAllEnabledParticpants() {
		LOGGER.info("Entered listAllEnabledParticpants method -ParticipantRegistrationService");
		LOGGER.info("Exit listAllEnabledParticpants method -ParticipantRegistrationService");
		return (List<ParticipantRegistration>) repo.findByEnabledTrue();
	}

	public ParticipantRegistration save(ParticipantRegistration participantRegistration) {
		LOGGER.info("Entered save method -ParticipantRegistrationService");
		LOGGER.info("Exit save method -ParticipantRegistrationService");
		return repo.save(participantRegistration);
	}
//	

	public ParticipantRegistration get(Integer id) throws ParticipantRegistrationNotFoundException {
		LOGGER.info("Entered get method -ParticipantRegistrationService");
		try {
			LOGGER.info("Exit get method -ParticipantRegistrationService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ParticipantRegistrationNotFoundException("Could not find any Partcipant with ID " + id);
		}
	}

	public void updateParticipantRegistrationEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateParticipantRegistrationEnabledStatus method -ParticipantRegistrationService");
		LOGGER.info("Exit updateParticipantRegistrationEnabledStatus method -ParticipantRegistrationService");
		repo.updateEnabledStatus(id, enabled);
	}

	public void updateParticipantRegistrationApprovedStatus(Integer id, boolean approvalStatus) {
		LOGGER.info("Entered updateParticipantRegistrationApprovedStatus method -ParticipantRegistrationService");
		LOGGER.info("Exit updateParticipantRegistrationApprovedStatus method -ParticipantRegistrationService");
		repo.updateApprovalStatus(id, approvalStatus);
	}

	public void addParticipantRegistration(ParticipantRegistration participantRegistration) {
		LOGGER.info("Entered addParticipantRegistration method -ParticipantRegistrationService");
		// encodePassword(user);
		participantRegistration.setEnabled(true);
		participantRegistration.setApprovalStatus(false);
		LOGGER.info("Exit addParticipantRegistration method -ParticipantRegistrationService");
		repo.save(participantRegistration);
	}

	public void delete(Integer id) throws ParticipantRegistrationNotFoundException {
		LOGGER.info("Entered delete method -ParticipantRegistrationService");

		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ParticipantRegistrationNotFoundException("Could not find any Participant with ID " + id);
		}
		LOGGER.info("Exit delete method -ParticipantRegistrationService");
		repo.deleteById(id);
	}

//	public boolean isEmailUnique(Integer id, String email) {
//		ParticipantRegistration participantRegistrationByEmail = repo.getParticipantRegistrationByEmail(email);
//		Participant participantByEmail = partirepo.getParticipantByEmail(email);
//		if (participantRegistrationByEmail == null && participantByEmail == null) {
//			return true;
//		}
//		boolean isCreatingNew = (id == null);
//		if (isCreatingNew) {
//			if (participantRegistrationByEmail != null && participantByEmail != null) {
//				return false;
//				}
//		} else {
//			if (participantRegistrationByEmail.getId() != id) {
//				return false;
//			} else {
//				return true;
//			}
//		}
//		return false;
//	}

	public boolean isEmailUnique(String email) {
		LOGGER.info("Entered isEmailUnique method -ParticipantRegistrationService");
		ParticipantRegistration participantRegistrationByEmail = repo.getParticipantRegistrationByEmail(email);

		Participant participantByEmail = partirepo.getParticipantByEmail(email);

		if (participantRegistrationByEmail == null && participantByEmail == null) {
			return true;
		} else {
			LOGGER.info("Exit isEmailUnique method -ParticipantRegistrationService");
			return false;
		}
	}

	public List<ParticipantRegistration> listAllNonApprovedParticpants() {
		LOGGER.info("Entered listAllNonApprovedParticpants method -ParticipantRegistrationService");
		LOGGER.info("Exit listAllNonApprovedParticpants method -ParticipantRegistrationService");
		return (List<ParticipantRegistration>) repo.findByApprovalStatusFalse();
	}

//	public void updateEvent(Integer id, Event event) {
//		repo.updateEvent(id, event);
//	}

//	public List<ParticipantRegistration> listAllParticipantRegistrationByEvent(Event event) {
//		return repo.findByEvent(event);
//	}
	public Page<ParticipantRegistration> listByPage(int pageNum, String sortField, String sortDir, String name) {
		LOGGER.info("Entered listByPage method -ParticipantRegistrationService");

		Sort sort = null;
		if (sortField.equals("name")) {
			sortField = "firstName";
		}
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != null && !name.isEmpty()) {
			return repo.findAllByNameContainingAndApprovalStatusFalse(name, pageable);
		}
		LOGGER.info("Exit listByPage method -ParticipantRegistrationService");
		return repo.findByApprovalStatusFalse(pageable);
	}

}
