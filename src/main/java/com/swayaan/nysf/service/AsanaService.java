package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.repository.AsanaRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class AsanaService {

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;

	@Autowired
	AsanaRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(AsanaService.class);


	public static final int RECORDS_PER_PAGE = 10;

	public List<Asana> listAllAsanas() {
		LOGGER.info("Entered listAllAsanas method -AsanaService");
		LOGGER.info("Exit listAllAsanas method -AsanaService");
      return repo.findAllByOrderByNameAsc();
	}

	public Asana save(Asana asana) {
		LOGGER.info("Entered save method -AsanaService");

		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingAsana = (asana.getId() != null);
		if (isUpdatingAsana) {
			Asana existingAsana = repo.findById(asana.getId()).get();

			asana.setLastModifiedBy(currentUser);
			asana.setLastModifiedTime(new Date());
			asana.setCreatedBy(existingAsana.getCreatedBy());
			asana.setCreatedTime(existingAsana.getCreatedTime());

		} else {
			asana.setCreatedBy(currentUser);
			asana.setCreatedTime(new Date());
		}
		LOGGER.info("Exit save method -AsanaService");
     return repo.save(asana);
	}

	public Asana get(Integer id) throws AsanaNotFoundException {
		LOGGER.info("Entered get method -AsanaService");
         try {
			LOGGER.info("Exit get method -AsanaService");
           return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new AsanaNotFoundException("Could not find any asana with ID " + id);
		}
	}

	public void delete(Integer id) throws AsanaNotFoundException {
		LOGGER.info("Entered delete method -AsanaService");
           Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new AsanaNotFoundException("Could not find any asana with ID " + id);
		}
		LOGGER.info("Exit delete method -AsanaService");
        repo.deleteById(id);
	}

	public List<Asana> listAllOptionalAsanasForRound(ParticipantTeam participantTeam, String roundNumber) {
		LOGGER.info("Entered listAllOptionalAsanasForRound method -AsanaService");
         Integer asanaCategory_id = participantTeam.getAsanaCategory().getId();
		String category = participantTeam.getCategory().toString();
		String gender = participantTeam.getGender().toString();
		boolean compulsory = true;
		LOGGER.info("Exit listAllOptionalAsanasForRound method -AsanaService");
      return repo.findOptionalAsanasForRound(asanaCategory_id, category, gender, compulsory, roundNumber);
	}

	public List<Asana> listAllOptionalAsanas() {
		LOGGER.info("Entered listAllOptionalAsanas method -AsanaService");
		LOGGER.info("Exit listAllOptionalAsanas method -AsanaService");
        return repo.findOptionalAsanas();
	}

	public List<Asana> listAllNonSelectedOptionalAsanasForRound(ParticipantTeam participantTeam, Integer roundNumber) {
		LOGGER.info("Entered listAllNonSelectedOptionalAsanasForRound method -AsanaService");
          Integer asanaCategory_id = participantTeam.getAsanaCategory().getId();
		Integer category = participantTeam.getCategory().getId();
		String gender = participantTeam.getGender().toString();
		Integer participantteam_id = participantTeam.getId();
		boolean compulsory = true;
		// return
		// repo.findNonSelectedOptionalAsanasForRound(roundNumber,participantteam_id);
		List<Asana> listAsanas = new ArrayList<>();
		if (asanaCategory_id == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
			listAsanas = repo.findNonSelectedOptionalAsanasForRound(asanaCategory_id, category, gender, compulsory,
					participantteam_id, "COMP");
		} else {
			listAsanas = repo.findNonSelectedOptionalAsanasForRound(asanaCategory_id, category, gender, compulsory,
					participantteam_id, roundNumber, "COMP");
		}
		LOGGER.info("Exit listAllNonSelectedOptionalAsanasForRound method -AsanaService");
         return listAsanas;
	}

	public List<Asana> listAllNonSelectedOptionalAsanasForRoundAndParticipant(ParticipantTeam participantTeam,
			Integer roundNumber, Integer participantTeamParticipant_id) {
		LOGGER.info("Entered listAllNonSelectedOptionalAsanasForRoundAndParticipant method -AsanaService");
		LOGGER.info("Exit listAllNonSelectedOptionalAsanasForRoundAndParticipant method -AsanaService");
            Integer asanaCategory_id = participantTeam.getAsanaCategory().getId();
		Integer category = participantTeam.getCategory().getId();
		String gender = participantTeam.getGender().toString();
		Integer participantteam_id = participantTeam.getId();
		boolean compulsory = true;

		List<Asana> listAsanas = new ArrayList<>();
		if (asanaCategory_id == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
			return repo.findNonSelectedOptionalAsanasForRoundAndParticipant(asanaCategory_id, category, gender,
					compulsory, participantteam_id, "COMP", participantTeamParticipant_id);
		} else {
			return repo.findNonSelectedOptionalAsanasForRoundAndParticipant(asanaCategory_id, category, gender,
					compulsory, participantteam_id, roundNumber, "COMP", participantTeamParticipant_id);
		}

	}

	public List<Asana> listAllNonSelectedOptionalAsanasForRoundAndParticipantWithCode(ParticipantTeam participantTeam,
			Integer roundNumber, Integer participantTeamParticipant_id) {
		LOGGER.info("Entered listAllNonSelectedOptionalAsanasForRoundAndParticipantWithCode method -AsanaService");
		Integer asanaCategory_id = participantTeam.getAsanaCategory().getId();
		Integer category = participantTeam.getCategory().getId();
		String gender = participantTeam.getGender().toString();
		Integer participantteam_id = participantTeam.getId();
		boolean compulsory = true;
		LOGGER.info("Exit listAllNonSelectedOptionalAsanasForRoundAndParticipantWithCode method -AsanaService");
       return repo.findNonSelectedOptionalAsanasForRoundAndParticipantWithCode(asanaCategory_id, category, gender,
				compulsory, participantteam_id, "COMP", participantTeamParticipant_id);
	}

	public List<Asana> getOptionalAsanas() {
		LOGGER.info("Entered getOptionalAsanas method -AsanaService");

		List<Asana> listOptionalAsanas = new ArrayList<>();
		List<Integer> listAsanaIds = Arrays.asList(284, 285, 286, 287);
		for (Integer asanaId : listAsanaIds) {
			Asana asana = repo.findById(asanaId).get();
			listOptionalAsanas.add(asana);
		}
		LOGGER.info("Exit getOptionalAsanas method -AsanaService");
       return listOptionalAsanas;
	}

	public Page<Asana> listByPage(int pageNum, String sortField, String sortDir, String name) {
		LOGGER.info("Entered listByPage method -AsanaService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != null && !name.isEmpty()) {
			return repo.findAllByNameContaining(name, pageable);
		}
		LOGGER.info("Exit listByPage method -AsanaService");
    	return repo.findAll(pageable);
	}
}
