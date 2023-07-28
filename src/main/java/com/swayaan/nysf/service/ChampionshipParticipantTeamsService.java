package com.swayaan.nysf.service;

import java.util.ArrayList;
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

import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.repository.ChampionshipParticipantTeamsRepository;

@Service
public class ChampionshipParticipantTeamsService {

	@Autowired
	ChampionshipParticipantTeamsRepository repo;

	@Autowired
	ChampionshipRoundsService championshipRoundsService;

	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	@Autowired
	ParticipantTeamService participantTeamService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipParticipantTeamsService.class);

	public static final int RECORDS_PER_PAGE = 10;

	public ChampionshipParticipantTeams save(ChampionshipParticipantTeams championshipParticipantTeams) {
		LOGGER.info("Entered ChampionshipParticipantTeams method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit ChampionshipParticipantTeams method -ChampionshipParticipantTeamsService");
		return repo.save(championshipParticipantTeams);
	}

	public Integer getTotalChampionshipParticipantTeams(ChampionshipRounds championshipRound) {
		LOGGER.info("Entered getTotalChampionshipParticipantTeams method -ChampionshipParticipantTeamsService");
		LOGGER.info("Entered getTotalChampionshipParticipantTeams");
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = repo
				.findAllByChampionshipRounds(championshipRound);
		if (!listChampionshipParticipantTeams.isEmpty()) {
			LOGGER.info("Exit getTotalChampionshipParticipantTeams method -ChampionshipParticipantTeamsService");
			return listChampionshipParticipantTeams.size();
		}
		return 0;
	}

	public List<ChampionshipParticipantTeams> findByParticipantTeamId(Integer particpantTeamId) {
		LOGGER.info("Entered findByParticipantTeamId method -ChampionshipParticipantTeamsService");

		ParticipantTeam participantTeam = new ParticipantTeam();
		try {
			participantTeam = participantTeamService.get(particpantTeamId);
			LOGGER.info("Exit findByParticipantTeamId method -ChampionshipParticipantTeamsService");
			return repo.findByParticipantTeam(participantTeam);
		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteById(Integer championshipParticipantTeamsId) {
		LOGGER.info("Entered deleteById method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit deleteById method -ChampionshipParticipantTeamsService");
		repo.deleteById(championshipParticipantTeamsId);

	}

	public boolean findAllByChampionshipRounds(ChampionshipRounds championshipRound) {
		LOGGER.info("Entered findAllByChampionshipRounds method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit findAllByChampionshipRounds method -ChampionshipParticipantTeamsService");
		return repo.existsByChampionshipRounds(championshipRound);
	}

	public ChampionshipParticipantTeams get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ChampionshipParticipantTeamsService");
		try {
			LOGGER.info("Exit get method -ChampionshipParticipantTeamsService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EntityNotFoundException("Could not find any Championship Participant Teams with ID " + id);
		}
	}

//	public List<ChampionshipParticipantTeams> getTeamsByChampionshipRoundsAndStatusNot(ChampionshipRounds championshipRounds, StatusEnum status) {
//		return repo.findByChampionshipRoundsAndStatusNot(championshipRounds, status);
//	}

//	public List<ChampionshipParticipantTeams> getTeamsByChampionshipRoundsAndStatusNot(
//			ChampionshipRounds championshipRounds) {
//		List<StatusEnum> listStatusNotIn = new ArrayList<StatusEnum>();
//		listStatusNotIn.add(StatusEnum.SCHEDULED);
//		listStatusNotIn.add(StatusEnum.ABSENT);
//		// listStatusNotIn.add(StatusEnum.DISQUALIFIED);
//		listStatusNotIn.add(StatusEnum.REJECTED);
//		return repo.findByChampionshipRoundsAndStatusNotIn(championshipRounds, listStatusNotIn);
//	}

	public Page<ChampionshipParticipantTeams> listByPage(int pageNum, String sortField, String sortDir,
			String chestNumber, ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered listByPage method -ChampionshipParticipantTeamsService");

		List<StatusEnum> listStatusNotIn = new ArrayList<StatusEnum>();
		listStatusNotIn.add(StatusEnum.SCHEDULED);
		listStatusNotIn.add(StatusEnum.ABSENT);
		// listStatusNotIn.add(StatusEnum.DISQUALIFIED);
		listStatusNotIn.add(StatusEnum.REJECTED);

		Sort sort = null;
		sort = Sort.by(sortField);
		if (sortField.equals("chestNumber")) {
			sort = Sort.by("participantTeam.chestNumber");
		} else if (sortField.equals("name")) {
			sort = Sort.by("participantTeam.name");
		}
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (chestNumber != null && !chestNumber.isEmpty()) {
			return repo.findByChampionshipRoundsAndStatusNotInAndParticipantTeamChestNumber(championshipRounds,
					listStatusNotIn, chestNumber, pageable);
		}
		LOGGER.info("Exit listByPage method -ChampionshipParticipantTeamsService");

		return repo.findByChampionshipRoundsAndStatusNotIn(championshipRounds, listStatusNotIn, pageable);
	}

//	public List<ChampionshipParticipantTeams> getTeamsByChampionshipRoundsAndStatus(
//			ChampionshipRounds championshipRounds, StatusEnum status) {
//		return repo.findByChampionshipRoundsAndStatus(championshipRounds, status);
//	}

	public List<ChampionshipParticipantTeams> getTeamsByChampionshipRoundsAndStatusIn(
			ChampionshipRounds championshipRounds, List<StatusEnum> listStatus) {
		LOGGER.info("Entered getTeamsByChampionshipRoundsAndStatusIn method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit getTeamsByChampionshipRoundsAndStatusIn method -ChampionshipParticipantTeamsService");
		return repo.findByChampionshipRoundsAndStatusIn(championshipRounds, listStatus);
	}

	public void updateParticipantTeamStatus(Integer championshipRoundsId, Integer participantTeamId, String status) {
		LOGGER.info("Entered updateParticipantTeamStatus method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit updateParticipantTeamStatus method -ChampionshipParticipantTeamsService");
		repo.updateParticipantTeamStatus(championshipRoundsId, participantTeamId, status);
	}

//	public List<ParticipantTeam> listAllChampionshipParticipantTeamsWithStatus(Championship championship,
//			StatusEnum status) {
//		// list all championshiprounds with status (Ongoing)
//		// List<ChampionshipRounds> ChampionshipRounds=
//		// findAllByChampionshipAndStatus(championship,status);
//		return null;
//	}

	public List<ChampionshipParticipantTeams> getTeamsByChampionshipRounds(ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered getTeamsByChampionshipRounds method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit getTeamsByChampionshipRounds method -ChampionshipParticipantTeamsService");
		return repo.findAllByChampionshipRounds(championshipRounds);
	}

	public ChampionshipParticipantTeams findByParticipantTeamAndChampionshipRounds(ParticipantTeam participantTeam,
			ChampionshipRounds nextChampionshipRounds) {
		LOGGER.info("Entered findByParticipantTeamAndChampionshipRounds method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit findByParticipantTeamAndChampionshipRounds method -ChampionshipParticipantTeamsService");
		return repo.findByParticipantTeamAndChampionshipRounds(participantTeam, nextChampionshipRounds);
	}

	public void updateParticipantTeamStatus(Integer chamParticipantTeamId, String status) {
		LOGGER.info("Entered updateParticipantTeamStatus method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit updateParticipantTeamStatus method -ChampionshipParticipantTeamsService");
		repo.updateParticipantTeamStatus(chamParticipantTeamId, status);

	}

	public List<ChampionshipParticipantTeams> getAllByChampionshipRounds(ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered getAllByChampionshipRounds method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit getAllByChampionshipRounds method -ChampionshipParticipantTeamsService");
		return repo.findAllByChampionshipRounds(championshipRounds);
	}

	public List<ChampionshipParticipantTeams> getTeamsByChampionshipRoundsAndStatus(
			ChampionshipRounds championshipRounds) {
		LOGGER.info("Entered getTeamsByChampionshipRoundsAndStatus method -ChampionshipParticipantTeamsService");
		List<StatusEnum> listStatusNotIn = new ArrayList<StatusEnum>();

		listStatusNotIn.add(StatusEnum.ABSENT);
		listStatusNotIn.add(StatusEnum.DISQUALIFIED);
		listStatusNotIn.add(StatusEnum.REJECTED);
		LOGGER.info("Exit getTeamsByChampionshipRoundsAndStatus method -ChampionshipParticipantTeamsService");
		return repo.findByChampionshipRoundsAndStatusNotIn(championshipRounds, listStatusNotIn);
	}

	public List<ChampionshipParticipantTeams> findByParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered findByParticipantTeam method -ChampionshipParticipantTeamsService");
		LOGGER.info("Exit findByParticipantTeam method -ChampionshipParticipantTeamsService");
		return repo.findByParticipantTeam(participantTeam);
	}

	public ChampionshipParticipantTeams findByParticipantTeamAndRound(ParticipantTeam participantTeam, Integer round) {
		LOGGER.info("Entered findByParticipantTeamAndRound method -ChampionshipParticipantTeamsService");

		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				participantTeam.getChampionship().getId(), participantTeam.getAsanaCategory().getId(),
				participantTeam.getCategory().getId(), participantTeam.getGender().toString());
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipCategoryAndRound(championshipCategory, round);
		LOGGER.info("Exit findByParticipantTeamAndRound method -ChampionshipParticipantTeamsService");
		return repo.findByParticipantTeamAndChampionshipRounds(participantTeam, championshipRounds);
	}

	public Page<ChampionshipParticipantTeams> listByStatusAndPage(int pageNum, String sortField, String sortDir,
			String chestNumber, ChampionshipRounds championshipRounds, List<StatusEnum> status) {
		LOGGER.info("Entered listByStatusAndPage method -ChampionshipParticipantTeamsService");

		Sort sort = null;
		sort = Sort.by(sortField);
		if (sortField.equals("chestNumber")) {
			sort = Sort.by("participantTeam.chestNumber");
		} else if (sortField.equals("team")) {
			sort = Sort.by("participantTeam.name");
		}
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (chestNumber != null && !chestNumber.isEmpty()) {
			return repo.findByChampionshipRoundsAndStatusInAndParticipantTeamChestNumberContaining(championshipRounds,
					status, chestNumber, pageable);
		}
		LOGGER.info("Exit listByStatusAndPage method -ChampionshipParticipantTeamsService");
		return repo.findByChampionshipRoundsAndStatusIn(championshipRounds, status, pageable);
	}

	public Boolean checkifChampionshipParticipantTeamExists(ParticipantTeam participantTeam) {
		LOGGER.info("Entered checkifChampionshipParticipantTeamExists method -ChampionshipParticipantTeamsService");

		Integer round = 1;
		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				participantTeam.getChampionship().getId(), participantTeam.getAsanaCategory().getId(),
				participantTeam.getCategory().getId(), participantTeam.getGender().toString());
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipAndChampionshipCatgeoryAndRound(participantTeam.getChampionship(),
						championshipCategory, round);
		ChampionshipParticipantTeams championshipParticipantTeams = findByParticipantTeamAndChampionshipRounds(
				participantTeam, championshipRounds);
		if (championshipParticipantTeams != null) {
			return true;
		} else {
			LOGGER.info("Exit checkifChampionshipParticipantTeamExists method -ChampionshipParticipantTeamsService");
			return false;
		}
	}

}
