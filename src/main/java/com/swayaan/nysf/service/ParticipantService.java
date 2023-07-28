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

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.repository.ParticipantRepository;
import com.swayaan.nysf.repository.ParticipantTeamParticipantsRepository;
import com.swayaan.nysf.repository.RoleRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
@Transactional
public class ParticipantService {

	private static final Integer PARTICIPANT_ROLE_ID = 3;
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantService.class);

	public static final int RECORDS_PER_PAGE = 10;

	@Autowired
	ParticipantRepository repo;
	@Autowired
	ParticipantTeamParticipantsRepository participantTeamParticipantsRepositoryRepo;

	@Autowired
	RoleRepository roleRepo;

	public List<Participant> listAll() {
		LOGGER.info("Entered listAll method -ParticipantService");
		LOGGER.info("Exit listAll method -ParticipantService");
		return (List<Participant>) repo.findAll();
	}

//	public List<Participant> listAllParticipants() {
//		List<Participant> listAllParticipants = listAll();
//		List<Participant> listRefereeUsers = new ArrayList<>();
//		for(User user : listAllUsers) {
//			if(user.hasRole("Referee")) {
//				listRefereeUsers.add(user);
//			}
//		}
//		return listRefereeUsers;
//	}
//	
	public void delete(Integer id) throws ParticipantNotFoundException {
		LOGGER.info("Entered delete method -ParticipantService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ParticipantNotFoundException("Could not find any Participant with ID " + id);
		}
		LOGGER.info("Exit delete method -ParticipantService");
		repo.deleteById(id);
	}

	public Participant getParticipantById(Integer id) throws ParticipantNotFoundException {
		LOGGER.info("Entered getParticipantById method -ParticipantService");
		Optional<Participant> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new ParticipantNotFoundException("Participant with id " + id + " does not exist");
		}
		Participant existingParticipant = byId.get();
		LOGGER.info("Exit getParticipantById method -ParticipantService");
		return existingParticipant;
	}

	public Participant save(Participant participant) throws ParticipantNotFoundException {
		LOGGER.info("Entered save method -ParticipantService");
		Optional<Role> role = roleRepo.findById(PARTICIPANT_ROLE_ID);
		if (role.isPresent()) {
			participant.addRole(role.get());
		}
		boolean isUpdating = (participant.getId() != null);
		if (isUpdating) {
			Participant existingParticipant = getParticipantById(participant.getId());
			participant.setUserName(existingParticipant.getUserName());
			participant.setPassword(existingParticipant.getPassword());
			participant.setRoles(existingParticipant.getRoles());
			participant.setResetpasswordToken(existingParticipant.getResetpasswordToken());
			participant.setUserPrnNumber(participant.getUserPrnNumber());
			
		}
		LOGGER.info("Exit save method -ParticipantService");
		
		return repo.save(participant);
	}

//	public List<Participant> listAllParticipantsforCategory(String participantCategory) {
//		List<Participant> listAllParticipantsforCategory=repo.findByCategory(participantCategory);
//		return listAllParticipantsforCategory;
//	}

	public boolean isEmailUnique(Integer id, String email) {
		LOGGER.info("Entered isEmailUnique method -ParticipantService");
		Participant participantByEmail = repo.getParticipantByEmail(email);

		if (participantByEmail == null) {
			return true;
		}

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (participantByEmail != null) {
				return false;
			}
		} else {
			if (participantByEmail.getId() != id) {
				return false;
			}
		}
		LOGGER.info("Exit isEmailUnique method -ParticipantService");
		return true;
	}

	public List<Participant> listAllParticipantsForGender(String gender) {
		LOGGER.info("Entered listAllParticipantsForGender method -ParticipantService");
		LOGGER.info("Exit listAllParticipantsForGender method -ParticipantService");
		return repo.findAllByGender(gender);
	}

	public List<Participant> listAllParticipantsForGender(ParticipantTeam participantTeam) {
		LOGGER.info("Entered listAllParticipantsForGender method -ParticipantService");
		List<RegistrationStatusEnum> status = new ArrayList<>();
		status.add(RegistrationStatusEnum.REJECTED);
		if (participantTeam.getGender().toString().equals("Common")) {
			return repo.findAllParticipantsForCommonGenderAndNotInParticipantTeamAndStatus(
					participantTeam.getAsanaCategory().getId(), participantTeam.getChampionship().getId(),
					participantTeam.getCategory().getId(), status);

		} else {
			LOGGER.info("Exit listAllParticipantsForGender method -ParticipantService");
			return repo.findAllParticipantsForGenderAndNotInParticipantTeamAndStatus(
					participantTeam.getGender().toString(), participantTeam.getAsanaCategory().getId(),
					participantTeam.getChampionship().getId(), participantTeam.getCategory().getId(), status);

		}
	}

	public List<String> getParticipantByParticipantTeamParticipants(Integer participantId) {
		LOGGER.info("Entered getParticipantByParticipantTeamParticipants method -ParticipantService");
		LOGGER.info("Exit getParticipantByParticipantTeamParticipants method -ParticipantService");
		return repo.findAllByParticipant(participantId);
	}

	public Participant getParticipantByUserName(String userName) {
		LOGGER.info("Entered getParticipantByUserName method -ParticipantService");
		LOGGER.info("Exit getParticipantByUserName method -ParticipantService");
		return repo.findByUserName(userName);
	}

	public Page<Participant> listByPage(int pageNum, String sortField, String sortDir, String name, String prnNumber) {
		LOGGER.info("Entered listByPage method -ParticipantService");

		Sort sort = null;

		if (sortField.equals("name")) {
			sortField = "firstName";
		}
		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != "" && prnNumber != "") {
			LOGGER.info("findAllByNameContainingAndPrnNumberContaining method called -ParticipantService");
			return repo.findAllByNameContainingAndPrnNumberContaining(name, prnNumber, pageable);
		} else if (name == "" && prnNumber != "") {
			LOGGER.info("findAllByPrnNumberContaining method called -ParticipantService");
			return repo.findAllByPrnNumberContaining(prnNumber, pageable);
		} else if (name != "" && prnNumber == "") {
			LOGGER.info("findAllByNameContaining method called -ParticipantService");
			return repo.findAllByNameContaining(name, pageable);
		} else {
			LOGGER.info("findAll method called -ParticipantService");
			LOGGER.info("Exit listByPage method -ParticipantService");
			return repo.findAll(pageable);
		}

	}

//event manager championship-participants filter
	public Page<Participant> listByChampionshipParticipantsPage(int pageNum, String sortField, String sortDir,
			String name, String prnNumber, Championship championship) {
		LOGGER.info("Entered listByChampionshipParticipantsPage method -ParticipantService");

		Sort sort = null;

		if (sortField.equals("name")) {
			sortField = "firstName";
		}
		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		List<ParticipantTeamParticipants> listPartcipantTeamParticipants = new ArrayList<>();

		if (name != "" && prnNumber != "") {
			LOGGER.info("findAllByNameContainingAndJrnNumberContaining method called -ParticipantService");
			return repo.findAllByNameContainingAndPrnNumberContainingAndChampionship(name, prnNumber, championship,
					pageable);
		} else if (name == "" && prnNumber != "") {
			LOGGER.info("findAllByJrnNumberContaining method called -ParticipantService");
			return repo.findAllByPrnNumberContainingAndChampionship(prnNumber, championship, pageable);
		} else if (name != "" && prnNumber == "") {
			LOGGER.info("findAllByNameContaining method called -ParticipantService");
			return repo.findAllByNameContainingAndChampionship(name, championship, pageable);
		} else {
			LOGGER.info("findAll method called -ParticipantService");
			LOGGER.info("Exit listByChampionshipParticipantsPage method -ParticipantService");

			return repo.findAllByChampionship(championship, pageable);
		}

	}

	public boolean existByEmail(String email) {
		LOGGER.info("Entered existByEmail method -ParticipantService");
		LOGGER.info("Exit existByEmail method -ParticipantService");
		return repo.existsByEmail(email);
	}

	public Participant getParticipantByPrnNumber(String prnNumber) {
		LOGGER.info("Entered getParticipantByPrnNumber method -ParticipantService");
		LOGGER.info("Exit getParticipantByPrnNumber method -ParticipantService");
		return repo.findByUserName(prnNumber);
	}

	public void updateParticipantEnabledStatus(Integer id, boolean enabled) {

		LOGGER.info("Entered updateParticipantEnabledStatus method -ParticipantService");
		LOGGER.info("Exit updateParticipantEnabledStatus method -ParticipantService");
		repo.updateEnabledStatus(id, enabled);

	}

}
