package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ParticipantTeamAsanasNotFoundException;
import com.swayaan.nysf.repository.ParticipantTeamAsanasRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ParticipantTeamAsanasService {

	@Autowired
	ParticipantTeamAsanasRepository repo;

	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;
	
	@Autowired
	ParticipantTeamService participantTeamService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamAsanasService.class);

	private static final String COMPULSORY_ASANA_CODE = "C";

	private static final float COMPULSORY_ASANA_BASE_VALUE = 1;

	public ParticipantTeamAsanas save(ParticipantTeamAsanas participantTeamAsanas) {
		LOGGER.info("Entered save method -ParticipantTeamAsanasService");
	participantTeamParticipantAsanasStatusService.saveDetailsToParticipantTeamParticipantAsanasStatus(participantTeamAsanas);
	LOGGER.info("Exit save method -ParticipantTeamAsanasService");
     return repo.save(participantTeamAsanas);
	}

	public ParticipantTeamAsanas get(Integer id) throws NoSuchElementException {
		LOGGER.info("Entered get method -ParticipantTeamAsanasService");
         try {
     		LOGGER.info("Exit get method -ParticipantTeamAsanasService");
      return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw ex;
		}
	}

	public List<ParticipantTeamAsanas> getAsanasAssignedForTeam(Asana asana) {
		LOGGER.info("Entered getAsanasAssignedForTeam method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getAsanasAssignedForTeam method -ParticipantTeamAsanasService");
	return repo.findByAsana(asana);
	}

	public ParticipantTeamAsanas getByAsanaAndTeam(Asana asana, ParticipantTeam participantTeam) {
		LOGGER.info("Entered getByAsanaAndTeam method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByAsanaAndTeam method -ParticipantTeamAsanasService");
    return repo.findByAsanaAndParticipantTeam(asana, participantTeam);
	}

	public List<ParticipantTeamAsanas> getAllByAsanaAndTeam(Asana asana, ParticipantTeam participantTeam) {
		LOGGER.info("Entered getAllByAsanaAndTeam method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getAllByAsanaAndTeam method -ParticipantTeamAsanasService");
	return repo.findAllByAsanaAndParticipantTeam(asana, participantTeam);
	}

	public List<ParticipantTeamAsanas> getAllByAsanaAndTeamAndRoundNumber(Asana asana, ParticipantTeam participantTeam,Integer roundNumber) {
		LOGGER.info("Entered getAllByAsanaAndTeamAndRoundNumber method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getAllByAsanaAndTeamAndRoundNumber method -ParticipantTeamAsanasService");
	return repo.findAllByAsanaAndParticipantTeamAndRoundNumber(asana, participantTeam,roundNumber);
	}
	
	public List<ParticipantTeamAsanas> getByTeamAndRound(ParticipantTeam participantTeam, Integer roundNumber) {
		LOGGER.info("Entered getByTeamAndRound method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByTeamAndRound method -ParticipantTeamAsanasService");
		return repo.findByParticipantTeamAndRoundNumber(participantTeam, roundNumber);
	}

	public List<ParticipantTeamAsanas> getByTeamAndRoundOrderBySequenceNum(ParticipantTeam participantTeam,
			Integer roundNumber) {
		LOGGER.info("Entered getByTeamAndRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByTeamAndRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
    return repo.findByParticipantTeamAndRoundNumberOrderBySequenceNumberAsc(participantTeam, roundNumber);
	}

	public List<ParticipantTeamAsanas> getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(
			ParticipantTeam participantTeam, Integer roundNumber) {
		LOGGER.info("Entered getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
	return repo.findDistinctByAsanaAndParticipantTeamAndRoundNumberOrderBySequenceNumberAsc(participantTeam.getId(),
				roundNumber);
	}

	public List<ParticipantTeamAsanas> getDistinctByAsanaAndTeamOrderBySequenceNum(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getDistinctByAsanaAndTeamOrderBySequenceNum method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getDistinctByAsanaAndTeamOrderBySequenceNum method -ParticipantTeamAsanasService");
	return repo.findDistinctByAsanaAndParticipantTeamOrderBySequenceNumberAsc(participantTeam.getId());
	}

	public List<ParticipantTeamAsanas> getByTeamAndParticipantAndRoundOrderBySequenceNum(
			ParticipantTeam participantTeam, ParticipantTeamParticipants participantTeamParticipant,
			Integer roundNumber) {
		LOGGER.info("Entered getByTeamAndParticipantAndRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByTeamAndParticipantAndRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
     return repo.findByParticipantTeamAndParticipantTeamParticipantsAndRoundNumberOrderBySequenceNumberAsc(
				participantTeam, participantTeamParticipant, roundNumber);
	}

	public void deleteById(Integer id) throws ParticipantTeamAsanasNotFoundException {
		LOGGER.info("Entered deleteById method -ParticipantTeamAsanasService");
     	Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ParticipantTeamAsanasNotFoundException("Could not find any record with ID " + id);
		}
		LOGGER.info("Exit deleteById method -ParticipantTeamAsanasService");
    	repo.deleteById(id);
	}

	public List<ParticipantTeamAsanas> getTeamAsanasForParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered getTeamAsanasForParticipantTeam method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getTeamAsanasForParticipantTeam method -ParticipantTeamAsanasService");
	return repo.findByParticipantTeam(participantTeam);
	}

	public List<ParticipantTeamAsanas> getAssignedAsanasForParticipant(ParticipantTeam participantTeam,
			ParticipantTeamParticipants participantTeamParticipant, Integer round) {
		LOGGER.info("Entered getAssignedAsanasForParticipant method -ParticipantTeamAsanasService");
		boolean compulsory = false;
		LOGGER.info("Exit getAssignedAsanasForParticipant method -ParticipantTeamAsanasService");
  	return repo.findByParticipantTeamAndParticipantTeamParticipantsAndRound(participantTeam.getId(),
				participantTeamParticipant.getId(), compulsory, round);
	}

	public List<ParticipantTeamAsanas> getByTeamAndParticipantRoundOrderBySequenceNum(ParticipantTeam participantTeam,
			ParticipantTeamParticipants participantTeamParticipants, Integer round) {
		LOGGER.info("Entered getByTeamAndParticipantRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByTeamAndParticipantRoundOrderBySequenceNum method -ParticipantTeamAsanasService");
    return repo.findByParticipantTeamAndAndParticipantTeamParticipantsAndRoundNumberOrderBySequenceNumberAsc(
				participantTeam, participantTeamParticipants, round);

	}

	public List<ParticipantTeamAsanas> getByTeamAndParticipantTeamParticipants(ParticipantTeam participantTeam,
			ParticipantTeamParticipants participantTeamParticipants) {
		LOGGER.info("Entered getByTeamAndParticipantTeamParticipants method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByTeamAndParticipantTeamParticipants method -ParticipantTeamAsanasService");
	return repo.findByParticipantTeamAndParticipantTeamParticipants(participantTeam.getId(),
				participantTeamParticipants.getId());
	}

	public void updateSequenceNumber(Integer id, Integer seqNum) {
		LOGGER.info("Entered updateSequenceNumber method -ParticipantTeamAsanasService");
		LOGGER.info("Exit updateSequenceNumber method -ParticipantTeamAsanasService");
    repo.updateSequenceNumber(id, seqNum);
	}


	public List<ParticipantTeamAsanas> getByParticipantTeamParticipantAndRoundAndCompulsoryAsanaId(
			ParticipantTeamParticipants participantTeamParticipants, Integer roundNumber, Integer compulsoryId) {
		LOGGER.info("Entered getByParticipantTeamParticipantAndRoundAndCompulsoryAsanaId method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByParticipantTeamParticipantAndRoundAndCompulsoryAsanaId method -ParticipantTeamAsanasService");
	return repo.findAllByParticipantTeamParticipantsAndRoundNumberAndCompulsoryAsanaId(participantTeamParticipants,roundNumber,compulsoryId);
	}

	public Boolean existByCompulsoryAsana(Integer compulsoryId, ParticipantTeam participantTeam) {
		LOGGER.info("Entered existByCompulsoryAsana method -ParticipantTeamAsanasService");
		LOGGER.info("Exit existByCompulsoryAsana method -ParticipantTeamAsanasService");
	return repo.existsBycompulsoryAsanaIdAndParticipantTeam(compulsoryId,participantTeam);
	}

	public void setCompulsoryAsanasForParticipantTeams(CompulsoryRoundAsanas compulsoryRoundAsana,
			List<CompulsoryRoundAsanas> listSavedCompulsoryAsanas) {
		LOGGER.info("Entered setCompulsoryAsanasForParticipantTeams method -ParticipantTeamAsanasService");
		LOGGER.info("Exit setCompulsoryAsanasForParticipantTeams method -ParticipantTeamAsanasService");

		List<ParticipantTeam> listParticipantTeam = participantTeamService
				.listAllParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategory(
						compulsoryRoundAsana.getChampionship().getId(),
						compulsoryRoundAsana.getAsanaCategory().getId(),
						compulsoryRoundAsana.getGender().toString(),
						compulsoryRoundAsana.getCategory().getId());
		for (ParticipantTeam team : listParticipantTeam) {
			for (CompulsoryRoundAsanas compRoundAsanas : listSavedCompulsoryAsanas) {
				Boolean ispresent = repo.existsBycompulsoryAsanaIdAndParticipantTeam(compRoundAsanas.getId(),
						team);

				if (ispresent == false && !team.getParticipantTeamParticipants().isEmpty()) {
					ParticipantTeamAsanas participantTeamAsanas = new ParticipantTeamAsanas();
					participantTeamAsanas.setAsana(compRoundAsanas.getAsana());
					participantTeamAsanas.setAsanaCode(COMPULSORY_ASANA_CODE);
					participantTeamAsanas.setBaseValue(COMPULSORY_ASANA_BASE_VALUE);
					participantTeamAsanas.setCompulsory(true);
					participantTeamAsanas.setParticipantTeam(team);
					participantTeamAsanas.setCompulsoryAsanaId(compRoundAsanas.getId());
					participantTeamAsanas
							.setParticipantTeamParticipants(team.getParticipantTeamParticipants().get(0));
					participantTeamAsanas.setRoundNumber(compRoundAsanas.getRoundNumber());
					participantTeamAsanas.setSequenceNumber(compRoundAsanas.getSequenceNumber());
					repo.save(participantTeamAsanas);
				} else {
					
				}

			}

		}
	}

	public List<ParticipantTeamAsanas> getByCompulsoryAsanaId(Integer compulsoryAsanaId) {
		LOGGER.info("Entered getByCompulsoryAsanaId method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getByCompulsoryAsanaId method -ParticipantTeamAsanasService");
	return repo.findByCompulsoryAsanaId(compulsoryAsanaId);
	}

	public List<ParticipantTeamAsanas> getCompulsoryAsanasByTeamAndRound(ParticipantTeam participantTeam,
			Integer round) {
		LOGGER.info("Entered getCompulsoryAsanasByTeamAndRound method -ParticipantTeamAsanasService");
		LOGGER.info("Exit getCompulsoryAsanasByTeamAndRound method -ParticipantTeamAsanasService");
	return repo.findAllByParticipantTeamAndRoundNumberAndCompulsory(participantTeam,round,true);
	}

	public boolean existsByTeamAsana(ParticipantTeam participantTeams) {
		LOGGER.info("Entered existsByTeamAsana method -ParticipantTeamAsanasService");
		LOGGER.info("Exit existsByTeamAsana method -ParticipantTeamAsanasService");
      return repo.existsByparticipantTeam(participantTeams);
	}

	}
