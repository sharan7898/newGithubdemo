package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.repository.ChampionshipRepository;
import com.swayaan.nysf.repository.ChampionshipRoundsRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ChampionshipService {

	public static final int RECORDS_PER_PAGE = 10;
	@Autowired
	ChampionshipRepository repo;

	@Autowired
	ChampionshipRoundsRepository championshipRoundsRepository;

	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;

	@Autowired
	ChampionshipLinkService championshipLinkSerivce;

	@Autowired
	RefereesPanelsService refereePanelsService;

	@Autowired
	JudgeService judgeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipService.class);

//	public List<Championship> listAllChampionships() {
//		return repo.findAllByOrderByNameAsc();
//	}

	public Championship save(Championship championship) throws Exception {
		LOGGER.info("Entered save method -ChampionshipService");
		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingChampionship = (championship.getId() != null);
		if (isUpdatingChampionship) {
			Championship existingChampionship = repo.findById(championship.getId()).get();
			if (!existingChampionship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				throw new Exception("Unable to edit championship");

			}
			championship.setStatus(existingChampionship.getStatus());
			championship.setChampionshipCategory(existingChampionship.getChampionshipCategory());
			championship.setLastModifiedBy(currentUser);
			championship.setLastModifiedTime(new Date());
			championship.setCreatedBy(existingChampionship.getCreatedBy());
			championship.setCreatedTime(existingChampionship.getCreatedTime());

		} else {
			championship.setStatus(ChampionshipStatusEnum.SCHEDULED);
			championship.setCreatedBy(currentUser);
			championship.setCreatedTime(new Date());

		}
		Championship savedChampionship = repo.save(championship);
		LOGGER.info("Exit save method -ChampionshipService");
		return savedChampionship;
	}

	public void saveChampionshipLink(Championship championship, String url) {
		LOGGER.info("Entered saveChampionshipLink method -ChampionshipService");
		ChampionshipLink championshiplink = new ChampionshipLink();

		String setUrl = url + "/participant/championship/" + championship.getId() + "/getDetails";

		championshiplink.setLink(setUrl);
		championshiplink.setChampionship(championship);
		championshiplink.setCreatedBy(championship.getCreatedBy());
		championshiplink.setCreatedTime(new Date());
		championshiplink.setStatus(false);
		LOGGER.info("Exit saveChampionshipLink method -ChampionshipService");
		championshipLinkSerivce.saveChampionshipLink(championshiplink);

	}

	public Championship findById(Integer championshipParticipantGroup_id) {
		LOGGER.info("Entered findById method -ChampionshipService");
		LOGGER.info("Exit findById method -ChampionshipService");
		return repo.findById(championshipParticipantGroup_id).get();
	}

	public Championship get(Integer id) throws ChampionshipNotFoundException {
		LOGGER.info("Entered get method -ChampionshipService");
		try {
			LOGGER.info("Exit get method -ChampionshipService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ChampionshipNotFoundException("Could not find any Championship with ID " + id);
		}
	}

//	public void delete(Integer id) throws ChampionshipNotFoundException {
//		Long countById = repo.countById(id);
//		if (countById == null || countById == 0) {
//			throw new ChampionshipNotFoundException("Could not find any Championship with ID " + id);
//		}
//		repo.deleteById(id);
//	}

	public Championship getChampionshipById(Integer id) throws ChampionshipNotFoundException {
		LOGGER.info("Entered getChampionshipById method -ChampionshipService");
		Optional<Championship> byId = repo.findById(id);
		if (!byId.isPresent()) {
			throw new ChampionshipNotFoundException("Championship does not exist");
		}
		Championship existingChampionship = byId.get();
		LOGGER.info("Exit getChampionshipById method -ChampionshipService");
		return existingChampionship;
	}

	public Championship getChampionshipByName(String name) {
		LOGGER.info("Entered getChampionshipByName method -ChampionshipService");
		LOGGER.info("Exit getChampionshipByName method -ChampionshipService");
		return repo.findByName(name);
	}

	public Boolean checkChampionshipName(String name) {
		LOGGER.info("Entered checkChampionshipName method -ChampionshipService");
		LOGGER.info("Exit checkChampionshipName method -ChampionshipService");
		return repo.existsByName(name);
	}

	public List<Championship> findByStatusInAndStartDateGreaterThanEqual() {
		LOGGER.info("Entered findByStatusInAndStartDateGreaterThanEqual method -ChampionshipService");
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.SCHEDULED);
		Date startDate = new Date();
		LOGGER.info("Exit findByStatusInAndStartDateGreaterThanEqual method -ChampionshipService");
		return repo.findByStatusInAndStartDateGreaterThanEqual(status, startDate);
	}

	/*
	 * public List<Championship> listAllParticipantEnrolledChampionships(Participant
	 * participant) { List<ParticipantTeamParticipants>
	 * listParticipantTeamParticipants = participantTeamParticipantsService
	 * .findByParticipant(participant); Set<Championship> setEnrolledChampionships =
	 * new HashSet<Championship>(); for (ParticipantTeamParticipants
	 * enrolledParticipant : listParticipantTeamParticipants) {
	 * setEnrolledChampionships.add(enrolledParticipant.getParticipantTeam().
	 * getChampionship()); } List<Championship> listEnrolledChampionships = new
	 * ArrayList<Championship>(setEnrolledChampionships);
	 * Collections.sort(listEnrolledChampionships,
	 * Comparator.comparing(Championship::getStartDate)); return
	 * listEnrolledChampionships; }
	 */

	public Championship saveEventToChampionship(Championship championship) {
		LOGGER.info("Entered saveEventToChampionship method -ChampionshipService");
		LOGGER.info("Exit saveEventToChampionship method -ChampionshipService");
		return repo.save(championship);
	}

	public List<Championship> listAllChampionshipsForCurrentUser(User currentUser) {
		LOGGER.info("Entered listAllChampionshipsForCurrentUser method -ChampionshipService");
		List<ChampionshipStatusEnum> listStatusIn = new ArrayList<ChampionshipStatusEnum>();
		listStatusIn.add(ChampionshipStatusEnum.SCHEDULED);
		listStatusIn.add(ChampionshipStatusEnum.STARTED);
		listStatusIn.add(ChampionshipStatusEnum.ONGOING);
		listStatusIn.add(ChampionshipStatusEnum.COMPLETED);
		LOGGER.info("Exit listAllChampionshipsForCurrentUser method -ChampionshipService");
		return repo.findAllByCreatedByAndStatusIn(currentUser, listStatusIn);
	}

	public List<Championship> listAllChampionshipsForCurrentUserDashboard(User currentUser) {
		LOGGER.info("Entered listAllChampionshipsForCurrentUserDashboard method -ChampionshipService");
		List<ChampionshipStatusEnum> listStatusIn = new ArrayList<ChampionshipStatusEnum>();
		listStatusIn.add(ChampionshipStatusEnum.SCHEDULED);
		listStatusIn.add(ChampionshipStatusEnum.STARTED);
		listStatusIn.add(ChampionshipStatusEnum.ONGOING);
		listStatusIn.add(ChampionshipStatusEnum.COMPLETED);
		listStatusIn.add(ChampionshipStatusEnum.REJECTED);
		LOGGER.info("Exit listAllChampionshipsForCurrentUserDashboard method -ChampionshipService");
		return repo.findAllByCreatedByAndStatusIn(currentUser, listStatusIn);
	}

	public List<Championship> listAllChampionshipsForCurrentUserAndStatusNotCompleted(User currentUser) {
		LOGGER.info("Entered listAllChampionshipsForCurrentUserAndStatusNotCompleted method -ChampionshipService");
		ChampionshipStatusEnum status = ChampionshipStatusEnum.COMPLETED;
		LOGGER.info("Exit listAllChampionshipsForCurrentUserAndStatusNotCompleted method -ChampionshipService");
		return repo.findAllByCreatedByAndStatusNot(currentUser, status);
	}

	public List<Championship> getChampionshipByOngoingOrCompletedOrDeleted() {
		LOGGER.info("Entered getChampionshipByOngoingOrCompletedOrDeleted method -ChampionshipService");
		List<ChampionshipStatusEnum> listStatusIn = new ArrayList<ChampionshipStatusEnum>();
		listStatusIn.add(ChampionshipStatusEnum.ONGOING);
		listStatusIn.add(ChampionshipStatusEnum.COMPLETED);
		listStatusIn.add(ChampionshipStatusEnum.DELETED);
		listStatusIn.add(ChampionshipStatusEnum.REJECTED);
		LOGGER.info("Exit getChampionshipByOngoingOrCompletedOrDeleted method -ChampionshipService");
		return repo.findByStatusIn(listStatusIn);
	}

	public List<Championship> getByChampionshipLevel(ChampionshipLevels championshipLevels) {
		LOGGER.info("Entered getByChampionshipLevel method -ChampionshipService");
		LOGGER.info("Exit getByChampionshipLevel method -ChampionshipService");
		return repo.findByLevel(championshipLevels);
	}

	public List<Championship> listAllChampionshipsByNotDeleted() {
		LOGGER.info("Entered listAllChampionshipsByNotDeleted method -ChampionshipService");
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		LOGGER.info("Exit listAllChampionshipsByNotDeleted method -ChampionshipService");
		return repo.findByStatusNotIn(status);
	}

	public List<Championship> listAllChampionshipsByStatusCompleted() {
		LOGGER.info("Entered listAllChampionshipsByStatusCompleted method -ChampionshipService");
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		status.add(ChampionshipStatusEnum.COMPLETED);
		LOGGER.info("Exit listAllChampionshipsByStatusCompleted method -ChampionshipService");
		return repo.findByStatusNotIn(status);
	}

	public List<Championship> listAllChampionshipsByStatusCompletedAndByCurrentUser(User currentUser)
			throws JudgeNotFoundException {
		LOGGER.info("Entered listAllChampionshipsByStatusCompletedAndByCurrentUser method -ChampionshipService");
		Judge judgeUser = judgeService.findById(currentUser.getId());
		List<RefereesPanels> listRefereePanels = refereePanelsService.getByJudge(judgeUser);
		Set<Championship> setChampionship = new HashSet<>();
		for (RefereesPanels refereePanel : listRefereePanels) {
			setChampionship.add(refereePanel.getChampionshipRefereePanels().getChampionship());
		}
		setChampionship.stream()
				.filter(championship -> !(championship.getStatus() == ChampionshipStatusEnum.REJECTED
						&& championship.getStatus() == ChampionshipStatusEnum.DELETED
						&& championship.getStatus() == ChampionshipStatusEnum.COMPLETED));
		List<Championship> listChampionship = new ArrayList<>(setChampionship);
		LOGGER.info("Exit listAllChampionshipsByStatusCompletedAndByCurrentUser method -ChampionshipService");
		return listChampionship;
	}

	public List<Championship> getChampionshipByCreatedBy(Integer eventManagerId) {
		LOGGER.info("Entered getChampionshipByCreatedBy method -ChampionshipService");
		LOGGER.info("Exit getChampionshipByCreatedBy method -ChampionshipService");
		return repo.findByEventManager(eventManagerId);
	}

	public Championship getRegisteredEventChampionship(EventRegistration eventRegistration) {
		LOGGER.info("Entered getRegisteredEventChampionship method -ChampionshipService");
		LOGGER.info("Exit getRegisteredEventChampionship method -ChampionshipService");
		return repo.findByNameAndLevelAndStartDateAndEndDateAndLocationAndCreatedBy(eventRegistration.getName(),
				eventRegistration.getLevel(), eventRegistration.getStartDate(), eventRegistration.getEndDate(),
				eventRegistration.getLocation(), eventRegistration.getCreatedBy());
	}

	// Admin filters starts here
	public Page<Championship> listByPage(int pageNum, String sortField, String sortDir, String name, String location) {
		LOGGER.info("Entered listByPage method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		// Default filter
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		if (name != "" && location != "") {
			LOGGER.info(
					"findAllByNameContainingAndLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndLocationContainingAndStatusNotIn(name, location, status, pageable);
		} else if (name == "" && location != "") {
			LOGGER.info("findAllByLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByLocationContainingAndStatusNotIn(location, status, pageable);
		} else if (name != "" && location == "") {
			LOGGER.info("findAllByNameContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndStatusNotIn(name, status, pageable);
		} else {
			LOGGER.info("findAllByStatusNotIn method called -ChampionshipService");
			LOGGER.info("Entered listByPage method -ChampionshipService");
			return repo.findAllByStatusNotIn(status, pageable);
		}

	}

	// Admin filters ends here

//event manager filters
	public Page<Championship> listByEventPage(int pageNum, String sortField, String sortDir, String name,
			String location, User currentUser) {
		LOGGER.info("Entered listByEventPage method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		// Default filter

		List<ChampionshipStatusEnum> listStatusIn = new ArrayList<ChampionshipStatusEnum>();
		listStatusIn.add(ChampionshipStatusEnum.SCHEDULED);
		listStatusIn.add(ChampionshipStatusEnum.STARTED);
		listStatusIn.add(ChampionshipStatusEnum.ONGOING);
		listStatusIn.add(ChampionshipStatusEnum.COMPLETED);

		if (name != "" && location != "") {
			LOGGER.info(
					"findAllByNameContainingAndLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndLocationContainingAndCreatedByAndStatusIn(name, location, currentUser,
					listStatusIn, pageable);
		} else if (name == "" && location != "") {
			LOGGER.info("findAllByLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByLocationContainingAndCreatedByAndStatusIn(location, currentUser, listStatusIn,
					pageable);
		} else if (name != "" && location == "") {
			LOGGER.info("findAllByNameContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndCreatedByAndStatusIn(name, currentUser, listStatusIn, pageable);
		} else {
			LOGGER.info("findAllByStatusNotIn method called -ChampionshipService");
			LOGGER.info("Entered listByEventPage method -ChampionshipService");
			return repo.findAllByCreatedByAndStatusIn(currentUser, listStatusIn, pageable);
		}

	}

	// Participant upcoming championships filters

	public Page<Championship> listByParticipantPage(int pageNum, String sortField, String sortDir, String name,
			String location) {
		LOGGER.info("Entered listByParticipantPage method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		// Default filter
		Date startDate = new Date();

		List<ChampionshipStatusEnum> listStatusIn = new ArrayList<ChampionshipStatusEnum>();
		listStatusIn.add(ChampionshipStatusEnum.SCHEDULED);

		if (name != "" && location != "") {
			LOGGER.info(
					"findAllByNameContainingAndLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndLocationContainingAndStatusInAndStartDateGreaterThanEqual(name,
					location, listStatusIn, startDate, pageable);
		} else if (name == "" && location != "") {
			LOGGER.info("findAllByLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByLocationContainingAndStatusInAndStartDateGreaterThanEqual(location, listStatusIn,
					startDate, pageable);
		} else if (name != "" && location == "") {
			LOGGER.info("findAllByNameContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndStatusInAndStartDateGreaterThanEqual(name, listStatusIn, startDate,
					pageable);
		} else {
			LOGGER.info("findAllByStatusNotIn method called -ChampionshipService");
			LOGGER.info("Exit listByParticipantPage method -ChampionshipService");
			return repo.findByStatusInAndStartDateGreaterThanEqual(listStatusIn, startDate, pageable);
		}

	}

//participant upcoming championships filters ends here.

	// participant enrolled championships starts here.

	public Page<Championship> listByParticipantEnrolledPage(int pageNum, String sortField, String sortDir, String name,
			String location, Integer currentUser) {
		LOGGER.info("Entered listByParticipantEnrolledPage method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		// Default filter
		// Date startDate = new Date();

		// List<ChampionshipStatusEnum> listStatusIn = new
		// ArrayList<ChampionshipStatusEnum>();
		// listStatusIn.add(ChampionshipStatusEnum.SCHEDULED);
		// listStatusIn.add(ChampionshipStatusEnum.STARTED);
		// listStatusIn.add(ChampionshipStatusEnum.ONGOING);
		// listStatusIn.add(ChampionshipStatusEnum.COMPLETED);

		if (name != "" && location != "") {
			LOGGER.info("findAllByNameContainingAndLocationContainingAndStatusNotIn method called -v");
			return repo.findAllByNameContainingAndLocationContaining(name, location, currentUser, pageable);
		} else if (name == "" && location != "") {
			LOGGER.info("findAllByLocationContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByLocationContaining(location, currentUser, pageable);
		} else if (name != "" && location == "") {
			LOGGER.info("findAllByNameContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContaining(name, currentUser, pageable);
		} else {
			LOGGER.info("findAllByStatusNotIn method called -ChampionshipService");
			LOGGER.info("Entered listByParticipantEnrolledPage method -ChampionshipService");
			return repo.listAllParticipantEnrolledChampionships(currentUser, pageable);
		}
		// return null;

	}

	// Judge filters starts here
	public Page<Championship> listByJudgePage(int pageNum, String sortField, String sortDir, String name,
			String location, Integer currentUser) {
		LOGGER.info("Entered listByJudgePage method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		// Default filter
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		status.add(ChampionshipStatusEnum.COMPLETED);

		if (name != "" && location != "") {
			LOGGER.info("findAllByNameContainingAndLocationContainingAndStatusNotIn method called ChampionshipService");
			return repo.findAllByNameContainingAndLocationContainingAndStatusNotIn(status, name, location, currentUser,
					pageable);
		} else if (name == "" && location != "") {
			LOGGER.info("findAllByLocationContainingAndStatusNotIn method called ChampionshipService");
			return repo.findAllByLocationContainingAndStatusNotIn(status, location, currentUser, pageable);
		} else if (name != "" && location == "") {
			LOGGER.info("findAllByNameContainingAndStatusNotIn method called -ChampionshipService");
			return repo.findAllByNameContainingAndStatusNotIn(status, name, currentUser, pageable);
		} else {
			LOGGER.info("findAllByStatusNotIn method called -ChampionshipService");
			LOGGER.info("Exit listByJudgePage method -ChampionshipService");
			return repo.findAllByStatusNotIn(status, currentUser, pageable);
		}

	}

	// Judge filters ends here

	public void updateStatus(Championship championship) {
		LOGGER.info("Entered updateStatus method -ChampionshipService");

		User currentUser = CommonUtil.getCurrentUser();
		Championship existingChampionship = repo.findById(championship.getId()).get();
		championship.setChampionshipCategory(existingChampionship.getChampionshipCategory());
		championship.setLastModifiedBy(currentUser);
		championship.setLastModifiedTime(new Date());
		championship.setCreatedBy(existingChampionship.getCreatedBy());
		championship.setCreatedTime(existingChampionship.getCreatedTime());
		LOGGER.info("Exit updateStatus method -ChampionshipService");
		repo.save(championship);

	}

	public Championship getChampionshipByChampionshipCategory(Integer championshipCategoryId) {
		LOGGER.info("Entered getChampionshipByChampionshipCategory method -ChampionshipService");
		LOGGER.info("Exit getChampionshipByChampionshipCategory method -ChampionshipService");
		return repo.findBychampionshipCategory(championshipCategoryId);
	}

	public boolean isNotRejectedAndDeletedChampionship(Integer championshipId) {
		LOGGER.info("Entered isNotRejectedAndDeletedChampionship method -ChampionshipService");
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		LOGGER.info("Exit isNotRejectedAndDeletedChampionship method -ChampionshipService");
		return repo.existsByIdAndStatusNotIn(championshipId, status);
	}

	public Championship updateImages(Championship championship) throws Exception {
		LOGGER.info("Entered updateImages method -ChampionshipService");
		User currentUser = CommonUtil.getCurrentUser();
		championship.setLastModifiedBy(currentUser);
		championship.setLastModifiedTime(new Date());

		Championship savedChampionship = repo.save(championship);
		LOGGER.info("Exit updateImages method -ChampionshipService");
		return savedChampionship;
	}

	public List<Championship> listAllChampionshipsByNotDeletedAndImagesNotNull() {
		LOGGER.info("Entered listAllChampionshipsByNotDeletedAndImagesNotNull method -ChampionshipService");
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		LOGGER.info("Exit listAllChampionshipsByNotDeletedAndImagesNotNull method -ChampionshipService");
		return repo.findByStatusNotInAndImage1NotNullOrImage2NotNullOrImage3NotNullOrImage4NotNull(status);
	}

	public List<Championship> listAllChampionshipsByNotDeletedAndImagesNull() {
		LOGGER.info("Entered listAllChampionshipsByNotDeletedAndImagesNull method -ChampionshipService");
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		LOGGER.info("Exit listAllChampionshipsByNotDeletedAndImagesNull method -ChampionshipService");
		return repo.findByStatusNotInAndImage1NullAndImage2NullAndImage3NullAndImage4Null(status);
	}

	public void clearImages(Integer championshipId) {
		LOGGER.info("Entered clearImages method -ChampionshipService");
		Championship championship = repo.findById(championshipId).get();

		championship.setImage1(null);
		championship.setImage2(null);
		championship.setImage3(null);
		championship.setImage4(null);
		LOGGER.info("Exit clearImages method -ChampionshipService");
		repo.save(championship);

	}

	public Page<Championship> listAllScoreBoardImageChampionshipsByNotDeletedAndImagesNotNull(int pageNum,
			String sortField, String sortDir, String name) {
		LOGGER.info(
				"Entered listAllScoreBoardImageChampionshipsByNotDeletedAndImagesNotNull method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		// Default filters
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != null && !name.isEmpty()) {
			return repo.findByStatusNotInAndNameContainingAndImage1NotNullOrImage2NotNullOrImage3NotNullOrImage4NotNull(
					status, name, pageable);
		}
		LOGGER.info("Exit listAllScoreBoardImageChampionshipsByNotDeletedAndImagesNotNull method -ChampionshipService");
		return repo.findByStatusNotInAndImage1NotNullOrImage2NotNullOrImage3NotNullOrImage4NotNull(status, pageable);
	}

	public Page<Championship> listEventManagerScoreBoardImageChampionshipsByNotDeleted(Integer championshipId,
			int pageNum, String sortField, String sortDir, String name) {
		LOGGER.info("Entered listEventManagerScoreBoardImageChampionshipsByNotDeleted method -ChampionshipService");

		Sort sort = null;
		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		// Default filters
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != null && !name.isEmpty()) {
			return repo.findByStatusNotInAndNameContainingAndId(status, name, championshipId, pageable);
		}
		LOGGER.info("Exit listEventManagerScoreBoardImageChampionshipsByNotDeleted method -ChampionshipService");
		return repo.findByStatusNotInAndId(status, championshipId, pageable);
	}

	public Championship update(Championship championship) throws Exception {
		LOGGER.info("Entered update method -ChampionshipService");
		User currentUser = CommonUtil.getCurrentUser();
		Championship existingChampionship = repo.findById(championship.getId()).get();

		championship.setChampionshipCategory(existingChampionship.getChampionshipCategory());
		championship.setLastModifiedBy(currentUser);
		championship.setLastModifiedTime(new Date());

		Championship savedChampionship = repo.save(championship);
		LOGGER.info("Exit update method -ChampionshipService");
		return savedChampionship;
	}

	public List<Championship> findAllChampionshipsForCurrentUser(User currentUser) {
		List<ChampionshipStatusEnum> listStatus = new ArrayList<>(Arrays.asList(ChampionshipStatusEnum.SCHEDULED,
				ChampionshipStatusEnum.STARTED, ChampionshipStatusEnum.ONGOING));
		return repo.findAllChampionshipsForCurrentUser(currentUser.getId(), listStatus);
	}

}
