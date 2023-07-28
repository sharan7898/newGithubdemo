package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.controller.ManageParticipantModuleController;
import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamParticipantsNotFoundException;
import com.swayaan.nysf.repository.ParticipantTeamParticipantsRepository;
import com.swayaan.nysf.repository.ParticipantTeamRepository;

@Service
public class ParticipantTeamParticipantsService {

	@Autowired
	ParticipantTeamParticipantsRepository repo;
	@Autowired
	ParticipantTeamRepository participantTeamRepo;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamParticipantsService.class);

	public ParticipantTeamParticipants save(ParticipantTeamParticipants participantTeamParticipants) {
		LOGGER.info("Entered save method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit save method -ParticipantTeamParticipantFinalScoreService");
		return repo.save(participantTeamParticipants);
	}

	public ParticipantTeamParticipants get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamParticipantFinalScoreService");
		try {
			LOGGER.info("Exit get method -ParticipantTeamParticipantFinalScoreService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public List<ParticipantTeamParticipants> getTeamsAssignedForParticipants(Participant participant) {
		LOGGER.info("Entered getTeamsAssignedForParticipants method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getTeamsAssignedForParticipants method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipant(participant);
	}

	public ParticipantTeamParticipants getByParticipantAndTeam(Participant participant,
			ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByParticipantAndTeam method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getByParticipantAndTeam method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipantAndParticipantTeam(participant, participantTeam);
	}

	public List<ParticipantTeamParticipants> getAllByTeamAndParticipant(Participant participant,
			ParticipantTeam participantTeam) {
		LOGGER.info("Entered getAllByTeamAndParticipant method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getAllByTeamAndParticipant method -ParticipantTeamParticipantFinalScoreService");
		return repo.findAllByParticipantAndParticipantTeam(participant, participantTeam);
	}

	public void deleteById(Integer id) throws ParticipantTeamParticipantsNotFoundException {
		LOGGER.info("Entered deleteById method -ParticipantTeamParticipantFinalScoreService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ParticipantTeamParticipantsNotFoundException("Could not find any record with ID " + id);
		}
		LOGGER.info("Exit deleteById method -ParticipantTeamParticipantFinalScoreService");
		repo.deleteById(id);
	}

	public Integer getCountOfTeamParticipants(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getCountOfTeamParticipants method -ParticipantTeamParticipantFinalScoreService");
		List<ParticipantTeamParticipants> listParticipantTeams = repo.findByParticipantTeam(participantTeam);
		Integer count = listParticipantTeams.size();
		LOGGER.info("Exit getCountOfTeamParticipants method -ParticipantTeamParticipantFinalScoreService");
		return count;
	}

	public List<ParticipantTeamParticipants> getTeamParticipantsForParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info(
				"Entered getTeamParticipantsForParticipantTeam method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getTeamParticipantsForParticipantTeam method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipantTeam(participantTeam);
	}

	public List<ParticipantTeamParticipants> getParticipantTeamParticipants(Participant participant) {
		LOGGER.info("Entered getParticipantTeamParticipants method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getParticipantTeamParticipants method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipant(participant);
	}

	public void updateSequenceNumber(Integer id, Integer seqNum) {
		LOGGER.info("Entered updateSequenceNumber method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit updateSequenceNumber method -ParticipantTeamParticipantFinalScoreService");
		repo.updateSequenceNumber(id, seqNum);
	}

	public List<ParticipantTeamParticipants> getByTeamOrderBySequenceNumberAsc(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByTeamOrderBySequenceNumberAsc method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getByTeamOrderBySequenceNumberAsc method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipantTeamOrderBySequenceNumberAsc(participantTeam);
	}

	public ParticipantTeamParticipants getByTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByTeam method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit getByTeam method -ParticipantTeamParticipantFinalScoreService");
		return repo.findOneByParticipantTeam(participantTeam.getId());
	}

	public List<ParticipantTeamParticipants> findByParticipant(Participant participant) {
		LOGGER.info("Entered findByParticipant method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit findByParticipant method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipant(participant);
	}

	public ParticipantTeamParticipants findByParticipantTeamAndTeamLead(ParticipantTeam participantTeam,
			Boolean teamLead) {
		LOGGER.info("Entered findByParticipantTeamAndTeamLead method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit findByParticipantTeamAndTeamLead method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipantTeamAndIsTeamLead(participantTeam, teamLead);
	}

	@Modifying
	@Transactional
	public Iterable<ParticipantTeamParticipants> saveAll(
			List<ParticipantTeamParticipants> listParticipantTeamParticipants) {
		LOGGER.info("Entered saveAll method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info("Exit saveAll method -ParticipantTeamParticipantFinalScoreService");
		return repo.saveAll(listParticipantTeamParticipants);

	}

	public void setOtherParticipantsTeamLeadStatusToFalse(ParticipantTeam participantTeam,
			ParticipantTeamParticipants teamLead) {
		LOGGER.info(
				"Entered setOtherParticipantsTeamLeadStatusToFalse method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info(
				"Exit setOtherParticipantsTeamLeadStatusToFalse method -ParticipantTeamParticipantFinalScoreService");
		repo.updateOtherParticipantsTeamLeadStatusToFalse(participantTeam.getId(), teamLead.getId());

	}

	public List<Participant> findAllByChampionship(Championship championship) {
		LOGGER.info("Entered findAllByChampionship method -ParticipantTeamParticipantFinalScoreService");

		List<ParticipantTeamParticipants> listPartcipantTeamParticipants = repo
				.findByParticipantTeamChampionship(championship);
		Set<Participant> setParticipants = new HashSet<>();
		for (ParticipantTeamParticipants participant : listPartcipantTeamParticipants) {
			setParticipants.add(participant.getParticipant());
		}
		List<Participant> listParticipants = new ArrayList<>(setParticipants);
		LOGGER.info("Exit findAllByChampionship method -ParticipantTeamParticipantFinalScoreService");
		return listParticipants;
	}

	public boolean isCurrentUserChampionshipParticipant(Participant participant, Championship championship) {
		LOGGER.info("Entered isCurrentUserChampionshipParticipant method -ParticipantTeamParticipantFinalScoreService");
		Boolean isChampionshipParticipant = repo.existsByParticipantTeamChampionshipAndParticipant(championship,
				participant);
		LOGGER.info("Exit isCurrentUserChampionshipParticipant method -ParticipantTeamParticipantFinalScoreService");
		return isChampionshipParticipant;
	}

	public List<ParticipantTeam> listParticipantTeamsByParticipantAndChampionship(Participant currentParticipant,
			Championship championship) {
		LOGGER.info(
				"Entered listParticipantTeamsByParticipantAndChampionship method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info(
				"Exit listParticipantTeamsByParticipantAndChampionship method -ParticipantTeamParticipantFinalScoreService");
		return repo.findAllParticipantTeamByParticipantAndChampionship(currentParticipant, championship);
	}

	public ParticipantTeam findByParticipantAndChampionshipAndParticipantTeamCategories(Participant currentParticipant,
			Championship championship, AsanaCategory asanaCategory, AgeCategory ageCategory, GenderEnum gender)
			throws EntityNotFoundException {
		LOGGER.info(
				"Entered findByParticipantAndChampionshipAndParticipantTeamCategories method -ParticipantTeamParticipantFinalScoreService");
		LOGGER.info(
				"Exit findByParticipantAndChampionshipAndParticipantTeamCategories method -ParticipantTeamParticipantFinalScoreService");
		return repo.findByParticipantAndChampionshipAndParticipantTeamCategories(currentParticipant, championship,
				asanaCategory, ageCategory, gender);
	}

	public Boolean checkIfCurrentUserTeamLead(ParticipantTeam participantTeam, User currentUser) {
		LOGGER.info("Entered checkIfCurrentUserTeamLead method -ParticipantTeamParticipantFinalScoreService");

		LOGGER.info("Entered isCurrentUserTeamLead - ");
		Boolean isCurrentUserTeamLead;
		ParticipantTeamParticipants teamLead = findByParticipantTeamAndTeamLead(participantTeam, true);
		if (teamLead == null) {
			isCurrentUserTeamLead = false;
		} else if (teamLead.getParticipant().getId() == currentUser.getId()) {
			isCurrentUserTeamLead = true;
		} else {
			isCurrentUserTeamLead = false;
		}
		LOGGER.info("Exit checkIfCurrentUserTeamLead method -ParticipantTeamParticipantFinalScoreService");
		return isCurrentUserTeamLead;
	}

}
