package com.swayaan.nysf.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.repository.ParticipantTeamParticipantsRepository;
import com.swayaan.nysf.repository.ParticipantTeamRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
@Transactional
public class ParticipantTeamService {

	@Autowired
	ParticipantTeamRepository repo;
	@Autowired
	AsanaService asanaService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	UserService userService;
	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;

	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;
	@Autowired
	ChampionshipRefereePanelsService championshipRefereePanelsService;
	@Autowired
	CommonUtil commonUtil;

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;

	@Autowired
	ParticipantTeamParticipantsRepository participantTeamParticipantsRepository;

	private static final DecimalFormat decimalFormat = new DecimalFormat("0000");
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamService.class);
	public static final int RECORDS_PER_PAGE = 10;

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	public List<ParticipantTeam> listAllParticipantTeams() {
		LOGGER.info("Entered listAllParticipantTeams method -ParticipantTeamService");
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		LOGGER.info("Exit listAllParticipantTeams method -ParticipantTeamService");
		return repo.findAllByStatusInOrderByNameAsc(participantTeamStatus);
	}

	public ParticipantTeam save(ParticipantTeam participantTeam) throws Exception {
		LOGGER.info("Entered save method -ParticipantTeamService");
		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingTeam = (participantTeam.getId() != null);
		ParticipantTeam savedParticipantTeam = null;
		if (isUpdatingTeam) {
			ParticipantTeam existingTeam = repo.findById(participantTeam.getId()).get();
			if (checkTeamUpdateAllowed(existingTeam)) {

				participantTeam.setLastModifiedBy(currentUser);
				participantTeam.setLastModifiedTime(new Date());
				participantTeam.setCreatedBy(existingTeam.getCreatedBy());
				participantTeam.setCreatedTime(existingTeam.getCreatedTime());
				// set existing ones as the below fields cant be updated once group is created.
				participantTeam.setAutogenChestNumber(existingTeam.getAutogenChestNumber());
				participantTeam.setAsanaCategory(existingTeam.getAsanaCategory());
				participantTeam.setGender(existingTeam.getGender());
				participantTeam.setCategory(existingTeam.getCategory());
				participantTeam.setSequenceNumber(existingTeam.getSequenceNumber());
				if (participantTeam.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
					participantTeam.setDifferentAsanasForParticipants(true);
				} else if (participantTeam.getAsanaCategory().getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID
						|| participantTeam.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID
						|| participantTeam.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
						|| participantTeam.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {

					participantTeam.setDifferentAsanasForParticipants(false);
				}
				savedParticipantTeam = repo.save(participantTeam);
			} else {
				throw new Exception("Unable to edit team");
			}
		} else {
			participantTeam.setCreatedBy(currentUser);
			participantTeam.setCreatedTime(new Date());

			// generate chestNumber
			String asanaCategoryCode = participantTeam.getAsanaCategory().getCode().toUpperCase();
			// String ageCategoryCode =
			// participantTeam.getCategory().getTitle().toString().substring(0,
			// 2).toUpperCase();
			String ageCategoryCode = participantTeam.getCategory().getCode().toUpperCase();
			String genderCode = participantTeam.getGender().toString().substring(0, 1).toUpperCase();

			String generatedSeqNum;
			ParticipantTeam prevParticipantTeam = repo.findByAsanaCategoryAndCategoryAndGenderOrderByIdDesc(
					participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
					participantTeam.getGender().toString());
			if (prevParticipantTeam == null) { // if no records present
				Integer newSeqNum = 1;
				generatedSeqNum = getUniqueIDText(newSeqNum);
				participantTeam.setSequenceNumber(newSeqNum);
			} else {
				Integer prevSequenceNumber = prevParticipantTeam.getSequenceNumber();
				if (prevSequenceNumber == null) {
					Integer newSeqNum = 1;
					generatedSeqNum = getUniqueIDText(newSeqNum);
					participantTeam.setSequenceNumber(newSeqNum);
				} else {
					Integer newSeqNum = prevSequenceNumber + 1;
					generatedSeqNum = getUniqueIDText(newSeqNum);
					participantTeam.setSequenceNumber(newSeqNum);
				}
			}

			String autogenChestNumber = asanaCategoryCode + ageCategoryCode + genderCode + generatedSeqNum;
			participantTeam.setAutogenChestNumber(autogenChestNumber);

			if (participantTeam.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				participantTeam.setDifferentAsanasForParticipants(true);
			} else if (participantTeam.getAsanaCategory().getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID
					|| participantTeam.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID
					|| participantTeam.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
					|| participantTeam.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {

				participantTeam.setDifferentAsanasForParticipants(false);
			}
			savedParticipantTeam = repo.save(participantTeam);

			// assign compulsory asanas only when creating new group
			// assignCompulsoryAsanasForTeam(savedParticipantTeam);

			// assign optional asanas only when creating new group
			// assignOptionalAsanasForTeam(savedParticipantTeam);

			copyToChampionshipParticipantTeams(savedParticipantTeam);
		}
		LOGGER.info("Exit save method -ParticipantTeamService");
		return savedParticipantTeam;
	}

	public void copyToChampionshipParticipantTeams(ParticipantTeam savedParticipantTeam) {
		LOGGER.info("Entered copyToChampionshipParticipantTeams method -ParticipantTeamService");

		ChampionshipParticipantTeams championshipParticipantTeams = new ChampionshipParticipantTeams();
		championshipParticipantTeams.setParticipantTeam(savedParticipantTeam);
		championshipParticipantTeams.setStatus(StatusEnum.SCHEDULED);
		ChampionshipRounds championshipRound = getChampionshipRoundId(savedParticipantTeam);
		championshipParticipantTeams.setChampionshipRounds(championshipRound);
		championshipParticipantTeamsService.save(championshipParticipantTeams);
		// update total number of participant teams in championship rounds
		updateNumberOfParticipantTeams(championshipRound);
		// set the status in ParticipantTeamParticipantAsanasStatus

	}

	public void updateNumberOfParticipantTeams(ChampionshipRounds championshipRound) {
		LOGGER.info("Entered updateNumberOfParticipantTeams");
		// if(championshipRound !=null) {
		Integer noOfParticpantTeams = championshipParticipantTeamsService
				.getTotalChampionshipParticipantTeams(championshipRound);
		System.out.println(noOfParticpantTeams);
		System.out.println(championshipRound);
		championshipRound.setNoOfParticipantTeams(noOfParticpantTeams);

		championshipRoundsService.save(championshipRound);
		// }
	}

	public ChampionshipRounds getChampionshipRoundId(ParticipantTeam savedParticipantTeam) {
		LOGGER.info("Entered getChampionshipRoundId method -ParticipantTeamService");
		String gender = savedParticipantTeam.getGender().toString();
		Integer asanaCategory = savedParticipantTeam.getAsanaCategory().getId();
		Integer ageCategory = savedParticipantTeam.getCategory().getId();

		ChampionshipRounds championshipRounds = getByRoundAndChampionshipIdAndAsanaCategoryAndAgeCategoryAndGender(
				savedParticipantTeam.getChampionship(), asanaCategory, ageCategory, gender);
		LOGGER.info("Exit getChampionshipRoundId method -ParticipantTeamService");
		return championshipRounds;

	}

	private ChampionshipRounds getByRoundAndChampionshipIdAndAsanaCategoryAndAgeCategoryAndGender(
			Championship championship, Integer asanaCategory, Integer ageCategory, String gender) {
		LOGGER.info(
				"Entered getByRoundAndChampionshipIdAndAsanaCategoryAndAgeCategoryAndGender method -ParticipantTeamService");
		ChampionshipCategory championshipCategory = championshipCategoryService
				.getChampionshipCategoryForChampionshipId(championship, asanaCategory, ageCategory, gender);
		System.out.println("championshipCategory" + championshipCategory);
		if (championshipCategory != null) {
			ChampionshipRounds championshipRounds = championshipRoundsService
					.getByRoundAndChampionshipAndChampionshipCategory(1, championship, championshipCategory);
			System.out.println(championshipRounds);
			if (championshipRounds != null) {
				return championshipRounds;
			}
		}
		LOGGER.info(
				"Exit getByRoundAndChampionshipIdAndAsanaCategoryAndAgeCategoryAndGender method -ParticipantTeamService");
		return null;
	}

	public void assignCompulsoryAsanasForTeam(ParticipantTeam participantTeam,
			ParticipantTeamParticipants participantTeamParticipants) {
		// get compulsory asanas for each round
		LOGGER.info("assignCompulsoryAsanas method  -ParticipantTeamService");
		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				participantTeam.getChampionship().getId(), participantTeam.getAsanaCategory().getId(),
				participantTeam.getCategory().getId(), participantTeam.getGender().toString());
		for (Integer round = 1; round <= championshipCategory.getNoOfRounds(); round++) {
			List<CompulsoryRoundAsanas> listCompulsoryAsanas = compulsoryRoundAsanasService
					.getCompulsoryAsanas(participantTeam, round);

			System.out.println("listCompulsoryAsanas : " + listCompulsoryAsanas.size());
			System.out.println("listCompulsoryAsanas : " + Arrays.toString(listCompulsoryAsanas.toArray()));
			if (listCompulsoryAsanas != null) {
				// List<ParticipantTeamParticipants> ListOfParticipantsForTeam =
				// participantTeam.getParticipantTeamParticipants();
				// for (ParticipantTeamParticipants participantTeamParticipant :
				// ListOfParticipantsForTeam) {
				copyCompulsoryAsanasToTeamAsanas(participantTeam, listCompulsoryAsanas, round,
						participantTeamParticipants);
				// }

			}
		}

	}

	private void assignOptionalAsanasForTeam(ParticipantTeam participantTeam) {
		// get optional asanas for each round
		List<Asana> listOptionalAsanas = asanaService.getOptionalAsanas();

		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				participantTeam.getChampionship().getId(), participantTeam.getAsanaCategory().getId(),
				participantTeam.getCategory().getId(), participantTeam.getGender().toString());
		for (Integer round = 1; round <= championshipCategory.getNoOfRounds(); round++) {

			if (listOptionalAsanas != null) {
				copyOptionalAsanasToTeamAsanas(participantTeam, listOptionalAsanas, round);
			}
		}

	}

	public void copyCompulsoryAsanasToTeamAsanas(ParticipantTeam participantTeam,
			List<CompulsoryRoundAsanas> listCompulsoryAsanas, Integer roundNumber,
			ParticipantTeamParticipants participantTeamParticipants) {
		LOGGER.info("copyCompulsoryAsanasToTeamAsanas function");
		if (!listCompulsoryAsanas.isEmpty()) {

			List<CompulsoryRoundAsanas> listSortedCompulsoryAsanas = listCompulsoryAsanas.stream()
					.sorted(Comparator.comparing(CompulsoryRoundAsanas::getSequenceNumber))
					.collect(Collectors.toList());

			for (CompulsoryRoundAsanas compulsoryRoundAsana : listSortedCompulsoryAsanas) {
				AsanaExecutionScore asanaExecutionScore = asanaExecutionScoreService.getBaseValueForCompulsaryAsana(
						compulsoryRoundAsana.getAsana().getId(), compulsoryRoundAsana.getAsanaCategory().getId(),
						compulsoryRoundAsana.getGender().name(), compulsoryRoundAsana.getCategory().getId());
				ParticipantTeamAsanas participantTeamAsanas = new ParticipantTeamAsanas();
				participantTeamAsanas.setParticipantTeam(participantTeam);
				participantTeamAsanas.setAsana(compulsoryRoundAsana.getAsana());
				participantTeamAsanas.setRoundNumber(roundNumber);
				participantTeamAsanas.setSequenceNumber(compulsoryRoundAsana.getSequenceNumber());
				participantTeamAsanas.setParticipantTeamParticipants(participantTeamParticipants);
				participantTeamAsanas.setCompulsoryAsanaId(compulsoryRoundAsana.getId());
				String asanaCode = "C ";
				participantTeamAsanas.setAsanaCode(asanaCode);
				if (asanaExecutionScore != null) {
					participantTeamAsanas.setBaseValue(asanaExecutionScore.getWeightage());
				} else {
					participantTeamAsanas.setBaseValue(1);
				}
				participantTeamAsanas.setCompulsory(true);
				LOGGER.info("participantTeamAsanas saved");
				participantTeamAsanasService.save(participantTeamAsanas);
			}
			participantTeamParticipantAsanasStatusService.saveDetailsByParticipantTeam(participantTeam, roundNumber);
		}

	}

	private void copyOptionalAsanasToTeamAsanas(ParticipantTeam participantTeam, List<Asana> listOptionalAsanas,
			Integer roundNumber) {
		if (listOptionalAsanas != null) {
			for (Asana optionalAsana : listOptionalAsanas) {
				ParticipantTeamAsanas participantTeamAsanas = new ParticipantTeamAsanas();
				participantTeamAsanas.setParticipantTeam(participantTeam);
				participantTeamAsanas.setAsana(optionalAsana);
				participantTeamAsanas.setRoundNumber(roundNumber);
				participantTeamAsanas.setBaseValue(1);
				participantTeamAsanas.setCompulsory(false);
				participantTeamAsanasService.save(participantTeamAsanas);
			}
		}
	}

	public String getUniqueIDText(Integer sequenceNumber) {
		LOGGER.info("Entered getUniqueIDText method -ParticipantTeamService");
		LOGGER.info("Exit getUniqueIDText method -ParticipantTeamService");
		return decimalFormat.format(sequenceNumber);
	}

	public ParticipantTeam get(Integer id) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered get method -ParticipantTeamService");
		try {
			LOGGER.info("Exit get method -ParticipantTeamService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ParticipantTeamNotFoundException("Could not find any group with ID " + id);
		}
	}

	public void delete(Integer id) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered delete method -ParticipantTeamService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ParticipantTeamNotFoundException("Could not find any group with ID " + id);
		}
		// delete from championship participants- automatic by cascade
		// update noOfParticipantTeams in championship rounds
		ParticipantTeam participantTeam = findById(id);
		// delete Participant team
		// repo.deleteById(id);
		LOGGER.info("Exit delete method -ParticipantTeamService");
		repo.deleteByTeamId(id);

	}

	public ParticipantTeam findById(Integer participantTeam_id) {
		LOGGER.info("Entered findById method -ParticipantTeamService");
		LOGGER.info("Exit findById method -ParticipantTeamService");
		return repo.findById(participantTeam_id).get();
	}

	public ParticipantTeam assignAsanaForParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered assignAsanaForParticipantTeam method -ParticipantTeamService");
		LOGGER.info("Exit assignAsanaForParticipantTeam method -ParticipantTeamService");
		return repo.save(participantTeam);
	}

	public ParticipantTeam assignParticipantForParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered assignParticipantForParticipantTeam method -ParticipantTeamService");
		LOGGER.info("Exit assignParticipantForParticipantTeam method -ParticipantTeamService");
		return repo.save(participantTeam);
	}

	/*
	 * public void deleteAssignedParticipantForParticipantTeam(Integer
	 * participantTeamId, Integer participantId) { Participant participant = null;
	 * try { participant = participantService.getParticipantById(participantId);
	 * 
	 * } catch (ParticipantNotFoundException e) { e.printStackTrace();
	 * //LOGGER.error("Could not find any Participant with ID " + asanaId); }
	 * ParticipantTeam participantTeam = findById(participantTeamId);
	 * participantTeam.getParticipants().remove(participant);
	 * repo.save(participantTeam);
	 * 
	 * }
	 */

	public List<ParticipantTeam> listAllParticipantTeamsForChampionship(Championship championship,
			RegistrationStatusEnum status) {
		LOGGER.info("Entered listAllParticipantTeamsForChampionship method -ParticipantTeamService");
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		LOGGER.info("Exit listAllParticipantTeamsForChampionship method -ParticipantTeamService");
		return repo.findByChampionshipAndStatusIn(championship, participantTeamStatus);
	}

	public List<ParticipantTeam> listAllParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategory(
			Integer championshipId, Integer asanaCategoryId, String gender, Integer ageCategory) {
		LOGGER.info(
				"Entered listAllParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategory method -ParticipantTeamService");

		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		List<ParticipantTeam> listParticipantTeam = repo
				.findAllByParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategoryAndStatus(
						championshipId, asanaCategoryId, GenderEnum.valueOf(gender), ageCategory,
						participantTeamStatus);
		LOGGER.info(
				"Exit listAllParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategory method -ParticipantTeamService");
		return listParticipantTeam;
	}

//	public List<ParticipantTeam> listAllChampionshipParticipantTeamsAndStatus(Championship championship,RegistrationStatusEnum status) {
//		return repo.findByChampionshipAndStatus(championship,status);
//	}

	public List<ParticipantTeam> listAllParticipantTeamsByAsanaCategory(AsanaCategory asanaCategory) {
		LOGGER.info("Entered listAllParticipantTeamsByAsanaCategory method -ParticipantTeamService");
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		LOGGER.info("Exit listAllParticipantTeamsByAsanaCategory method -ParticipantTeamService");
		return repo.findByAsanaCategoryAndStatusIn(asanaCategory, participantTeamStatus);
	}

	public List<ParticipantTeam> listOfChampionshipParticipantTeams(Integer championshipId, String championshipName) {
		LOGGER.info("Entered listOfChampionshipParticipantTeams method -ParticipantTeamService");

		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.PENDING);
		LOGGER.info("Exit listOfChampionshipParticipantTeams method -ParticipantTeamService");
		return repo.findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId(championshipName, status,
				participantTeamStatus, championshipId);

	}

	public List<ParticipantTeam> listOfChampionshipParticipantTeamsJudgeNoNAssigned(Integer championshipId,
			String championshipName) {
		LOGGER.info("Entered listOfChampionshipParticipantTeamsJudgeNoNAssigned method -ParticipantTeamService");

		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		LOGGER.info("Exit listOfChampionshipParticipantTeamsJudgeNoNAssigned method -ParticipantTeamService");
		return repo.findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId(championshipName, status,
				participantTeamStatus, championshipId);

	}
//	public List<ParticipantTeam> listAllScheduledParticipantTeams(StatusEnum status) {
//
//		return repo.findAllByStatus(status);
//	}

	public void assignOptionalAsanasForNewParticipant(ParticipantTeam participantTeam,
			ParticipantTeamParticipants participantTeamParticipants) {
		LOGGER.info("Entered assignOptionalAsanasForNewParticipant method -ParticipantTeamService");

		List<ParticipantTeamAsanas> listOptionalAsanasForTeam = participantTeamAsanasService
				.getDistinctByAsanaAndTeamOrderBySequenceNum(participantTeam);
		if (listOptionalAsanasForTeam != null) {
			for (ParticipantTeamAsanas optionalParticipantTeamAsana : listOptionalAsanasForTeam) {
				ParticipantTeamAsanas participantTeamAsanas = new ParticipantTeamAsanas();
				participantTeamAsanas.setParticipantTeam(optionalParticipantTeamAsana.getParticipantTeam());
				participantTeamAsanas.setAsana(optionalParticipantTeamAsana.getAsana());
				participantTeamAsanas.setRoundNumber(optionalParticipantTeamAsana.getRoundNumber());
				participantTeamAsanas.setBaseValue(optionalParticipantTeamAsana.getBaseValue());
				participantTeamAsanas.setCompulsory(optionalParticipantTeamAsana.isCompulsory());
				participantTeamAsanas.setSequenceNumber(optionalParticipantTeamAsana.getSequenceNumber());
				participantTeamAsanas.setParticipantTeamParticipants(participantTeamParticipants);
				participantTeamAsanasService.save(participantTeamAsanas);
			}
		}
	}

	public Boolean checkTeamChestNumber(String chestNumber, Championship championship) {
		LOGGER.info("Entered checkTeamChestNumber method -ParticipantTeamService");
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		LOGGER.info("Exit checkTeamChestNumber method -ParticipantTeamService");
		return repo.existsByChestNumberAndChampionshipAndStatusIn(chestNumber, championship, participantTeamStatus);
	}

	public List<ParticipantTeam> listAllParticipantTeamsByAgeCategory(AgeCategory ageCategory) {
		LOGGER.info("Entered listAllParticipantTeamsByAgeCategory method -ParticipantTeamService");
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);
		LOGGER.info("Exit listAllParticipantTeamsByAgeCategory method -ParticipantTeamService");
		return repo.findByCategoryAndStatusIn(ageCategory, participantTeamStatus);
	}

	public void modifyRefereesForParticipantTeam(ParticipantTeam participantTeam, Integer round,
			ChampionshipRefereePanels championshipRefereePanels) {
		LOGGER.info("Entered modifyRefereesForParticipantTeam method -ParticipantTeamService");
		participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam, round);
		LOGGER.info("deleteTeamReferresForParticipantTeam Successfully deleted");
		Championship championship = participantTeam.getChampionship();
		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				championship.getId(), participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
				participantTeam.getGender().toString());
		List<RefereesPanels> listRefereesPanels = new ArrayList<>();
		try {
			listRefereesPanels = championshipRefereePanelsService.get(championshipRefereePanels.getId())
					.getRefereesPanels();
		} catch (ChampionshipRefereePanelsNotFoundException e) {

			e.printStackTrace();
		}
		for (RefereesPanels referee : listRefereesPanels) {
			ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();

			participantTeamReferees.setParticipantTeam(participantTeam);
			// if any changes needed
			participantTeamReferees.setJudgeUser(referee.getJudgeUser());
			participantTeamReferees.setRoundNumber(round);
			participantTeamReferees.setChampionship(championship);
			participantTeamReferees.setChampionshipCategory(championshipCategory);
			participantTeamReferees.setType(referee.getType());
			LOGGER.info("assignedReferee" + referee.toString());
			participantTeamRefereesService.save(participantTeamReferees);
			LOGGER.info("Exit modifyRefereesForParticipantTeam method -ParticipantTeamService");
			LOGGER.info("participantTeamReferees added successfully");
		}

	}

	public Page<ParticipantTeam> listByPage(int pageNum, String sortField, String sortDir, String name,
			String chestNumber, String championshipName, String asanaCategory) {
		LOGGER.info("Entered listByPage method -ParticipantTeamService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		// default filter for team
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);

		if (name != "" && chestNumber != "" && championshipName != "" && asanaCategory != "") {
			// 0000 name chestnumber championship asanaCategory
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");

			return repo
					.findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							name, chestNumber, championshipName, asanaCategory, status, participantTeamStatus,
							pageable);

		} else if (name != "" && chestNumber != "" && championshipName != "" && asanaCategory == "") {
			// name chestnumber championship 0001
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
							name, chestNumber, championshipName, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName != "" && asanaCategory != "") {
			// name championship asanaCategory 0001
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");

			return repo
					.findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							name, championshipName, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber != "" && championshipName == "" && asanaCategory != "") {
			// name chestnumber asanaCategory 0010
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							name, chestNumber, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber != "" && championshipName != "" && asanaCategory != "") {
			// chestnumber championship asanaCategory 1000
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							chestNumber, championshipName, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber != "" && championshipName == "" && asanaCategory != "") {
			// chestnumber asanaCategory 1010
			LOGGER.info(
					"findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
					chestNumber, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber != "" && championshipName == "" && asanaCategory == "") {
			// name chestnumber 0011
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndChestNumberContainingAndChampionshipStatusNotInAndStatus(name,
					chestNumber, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber == "" && championshipName != "" && asanaCategory != "") {
			// championship asanaCategory1100
			LOGGER.info(
					"findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							championshipName, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName != "" && asanaCategory == "") {
			// name championship 0101
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(name,
					championshipName, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName == "" && asanaCategory != "") {
			// name asanaCategory 0110
			LOGGER.info(
					"findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(name,
					asanaCategory, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber != "" && championshipName != "" && asanaCategory == "") {
			// chestnumber championship 1001
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
					chestNumber, championshipName, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName == "" && asanaCategory == "") {
			// name 0111
			LOGGER.info(
					"findAllByNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndChampionshipStatusNotInAndStatus(name, status, participantTeamStatus,
					pageable);
		} else if (name == "" && chestNumber != "" && championshipName == "" && asanaCategory == "") {
			// chestnumber 1011
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChestNumberContainingAndChampionshipStatusNotInAndStatus(chestNumber, status,
					participantTeamStatus, pageable);
		} else if (name == "" && chestNumber == "" && championshipName != "" && asanaCategory == "") {
			// championship 1101
			LOGGER.info(
					"findAllByChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChampionshipNameContainingAndChampionshipStatusNotInAndStatus(championshipName, status,
					participantTeamStatus, pageable);
		} else if (name == "" && chestNumber == "" && championshipName == "" && asanaCategory != "") {
			// asanaCategory 1110
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(asanaCategory, status,
					participantTeamStatus, pageable);
		} else {
			LOGGER.info("findAllByChampionshipStatusNotIn method called -ParticipantTeamService");
			LOGGER.info("Entered listByPage method -ParticipantTeamService");
			return repo.findAllByChampionshipStatusNotInAndStatusIn(status, participantTeamStatus, pageable);
		}
	}

	public Page<ParticipantTeam> listByEventManagerPage(Integer championshipId, int pageNum, String sortField,
			String sortDir, String name, String chestNumber, String asanaCategory, String championshipName) {
		LOGGER.info("Entered listByEventManagerPage method -ParticipantTeamService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.APPROVED);

		if (name != "" && chestNumber != "" && asanaCategory != "") {
			// name chestnumber asanaCategory 0010
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							name, chestNumber, asanaCategory, championshipName, status, participantTeamStatus,
							championshipId, pageable);
		} else if (name != "" && chestNumber != "" && asanaCategory == "") {
			// name chestNumber
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							name, chestNumber, championshipName, status, participantTeamStatus, championshipId,
							pageable);

		} else if (name != "" && chestNumber == "" && asanaCategory != "") {
			// name asanaCategory
			LOGGER.info(
					"findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							name, asanaCategory, championshipName, status, participantTeamStatus, championshipId,
							pageable);

		} else if (name != "" && chestNumber == "" && asanaCategory == "") {
			// name
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method -ParticipantTeamService");
			return repo.findAllByNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
					name, championshipName, status, participantTeamStatus, championshipId, pageable);

		} else if (name == "" && chestNumber != "" && asanaCategory != "") {
			// chestNumber asanasCategory
			LOGGER.info(
					"findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			return repo
					.findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							chestNumber, asanaCategory, championshipName, status, participantTeamStatus, championshipId,
							pageable);

		} else if (name == "" && chestNumber != "" && asanaCategory == "") {
			// chestNumber
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			return repo
					.findAllByChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							chestNumber, championshipName, status, participantTeamStatus, championshipId, pageable);

		} else if (name == "" && chestNumber == "" && asanaCategory != "") {
			// asana Category
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			return repo
					.findAllByAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							asanaCategory, championshipName, status, participantTeamStatus, championshipId, pageable);

		} else {
			LOGGER.info(
					"findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			LOGGER.info("Exit listByEventManagerPage method -ParticipantTeamService");

			return repo.findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId(championshipName,
					status, participantTeamStatus, championshipId, pageable);
		}

	}

	public Boolean checkTeamUpdateAllowed(ParticipantTeam participantTeam) {
		LOGGER.info("Entered checkTeamUpdateAllowed method -ParticipantTeamService");

		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
				.findByParticipantTeam(participantTeam);
		List<ChampionshipRounds> listChampionshipRounds = listChampionshipParticipantTeams.stream().distinct()
				.map(cpt -> cpt.getChampionshipRounds()).collect(Collectors.toList());
		List<ChampionshipRounds> listScheduledChampionshipRounds = listChampionshipRounds.stream()
				.filter(cr -> cr.getStatus().equals(RoundStatusEnum.SCHEDULED)).collect(Collectors.toList());
		if (listChampionshipRounds.size() != listScheduledChampionshipRounds.size()) {
			return false;
		} else {
			LOGGER.info("Exit checkTeamUpdateAllowed method -ParticipantTeamService");
			return true;
		}
	}

	public Boolean checkTeamAsanasUpdateAllowed(ParticipantTeam participantTeam, Integer round) {
		LOGGER.info("Entered checkTeamAsanasUpdateAllowed method -ParticipantTeamService");

		ChampionshipParticipantTeams championshipParticipantTeams = championshipParticipantTeamsService
				.findByParticipantTeamAndRound(participantTeam, round);
		ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = participantTeamParticipantAsanasStatusService
				.findByParticipantTeamAndRound(participantTeam, round);

		if ((championshipParticipantTeams == null
				|| championshipParticipantTeams.getStatus().equals(StatusEnum.SCHEDULED))
				&& (participantTeamParticipantAsanasStatus == null
						|| !participantTeamParticipantAsanasStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE))) {
			return true;
		} else {
			LOGGER.info("Exit checkTeamAsanasUpdateAllowed method -ParticipantTeamService");
			return false;
		}
	}

	public Boolean checkTeamChestNumberForEdit(String chestNumber, Championship championship, Integer id) {
		LOGGER.info("Entered checkTeamChestNumberForEdit method -ParticipantTeamService");
		LOGGER.info("Exit checkTeamChestNumberForEdit method -ParticipantTeamService");
		return repo.existsByChestNumberAndChampionshipAndIdNot(chestNumber, championship, id);
	}

	public ParticipantTeam saveTeamRegistration(ParticipantTeam participantTeam) {
		LOGGER.info("Entered saveTeamRegistration method -ParticipantTeamService");

		User currentUser = CommonUtil.getCurrentUser();
		participantTeam.setCreatedBy(currentUser);
		participantTeam.setCreatedTime(new Date());

		// generate chestNumber
		String asanaCategoryCode = participantTeam.getAsanaCategory().getCode().toUpperCase();
		// String ageCategoryCode =
		// participantTeam.getCategory().getTitle().toString().substring(0,
		// 2).toUpperCase();
		String ageCategoryCode = participantTeam.getCategory().getCode().toUpperCase();
		String genderCode = participantTeam.getGender().toString().substring(0, 1).toUpperCase();

		String generatedSeqNum;
		ParticipantTeam prevParticipantTeam = repo.findByAsanaCategoryAndCategoryAndGenderOrderByIdDesc(
				participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
				participantTeam.getGender().toString());
		if (prevParticipantTeam == null) { // if no records present
			Integer newSeqNum = 1;
			generatedSeqNum = getUniqueIDText(newSeqNum);
			participantTeam.setSequenceNumber(newSeqNum);
		} else {
			Integer prevSequenceNumber = prevParticipantTeam.getSequenceNumber();
			if (prevSequenceNumber == null) {
				Integer newSeqNum = 1;
				generatedSeqNum = getUniqueIDText(newSeqNum);
				participantTeam.setSequenceNumber(newSeqNum);
			} else {
				Integer newSeqNum = prevSequenceNumber + 1;
				generatedSeqNum = getUniqueIDText(newSeqNum);
				participantTeam.setSequenceNumber(newSeqNum);
			}
		}

		String autogenChestNumber = asanaCategoryCode + ageCategoryCode + genderCode + generatedSeqNum;
		participantTeam.setAutogenChestNumber(autogenChestNumber);
		if (participantTeam.getChestNumber() == "" || participantTeam.getChestNumber() == null) {
			participantTeam.setChestNumber(autogenChestNumber);
		}
		ParticipantTeam savedParticipantTeam = repo.save(participantTeam);
		LOGGER.info("Exit saveTeamRegistration method -ParticipantTeamService");
		return savedParticipantTeam;
	}

	public ParticipantTeam registerTeamForCategory(Championship championship, Participant currentParticipant,
			AsanaCategory asanaCategory, AgeCategory ageCategory, GenderEnum gender, Integer[] listParticipants)
			throws Exception {
		LOGGER.info("Entered registerTeamForCategory method -ParticipantTeamService");

		User currentUser = CommonUtil.getCurrentUser();
		ParticipantTeam participantTeam = new ParticipantTeam();
		participantTeam.setAsanaCategory(asanaCategory);
		participantTeam.setCategory(ageCategory);
		participantTeam.setGender(gender);
		participantTeam.setCreatedBy(currentUser);
		participantTeam.setCreatedTime(new Date());
		participantTeam.setChampionship(championship);
		// generate chestNumber
		String asanaCategoryCode = participantTeam.getAsanaCategory().getCode().toUpperCase();
		String ageCategoryCode = participantTeam.getCategory().getCode().toUpperCase();
		String genderCode = participantTeam.getGender().toString().substring(0, 1).toUpperCase();

		String generatedSeqNum;
		ParticipantTeam prevParticipantTeam = repo.findByAsanaCategoryAndCategoryAndGenderOrderByIdDesc(
				participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
				participantTeam.getGender().toString());
		if (prevParticipantTeam == null) { // if no records present
			Integer newSeqNum = 1;
			generatedSeqNum = getUniqueIDText(newSeqNum);
			participantTeam.setSequenceNumber(newSeqNum);
		} else {
			Integer prevSequenceNumber = prevParticipantTeam.getSequenceNumber();
			if (prevSequenceNumber == null) {
				Integer newSeqNum = 1;
				generatedSeqNum = getUniqueIDText(newSeqNum);
				participantTeam.setSequenceNumber(newSeqNum);
			} else {
				Integer newSeqNum = prevSequenceNumber + 1;
				generatedSeqNum = getUniqueIDText(newSeqNum);
				participantTeam.setSequenceNumber(newSeqNum);
			}
		}

		String autogenChestNumber = asanaCategoryCode + ageCategoryCode + genderCode + generatedSeqNum;
		participantTeam.setAutogenChestNumber(autogenChestNumber);
		participantTeam.setChestNumber(autogenChestNumber);
		participantTeam.setName(autogenChestNumber);
		participantTeam.setStatus(RegistrationStatusEnum.PENDING);
		if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
			participantTeam.setDifferentAsanasForParticipants(true);
		} else {
			participantTeam.setDifferentAsanasForParticipants(false);
		}
		ParticipantTeam savedParticipantTeam = repo.save(participantTeam);
		String message = addParticipantsToTeam(listParticipants, savedParticipantTeam);
		// Make Team Lead
		participantTeamParticipantsRepository.updateParticipantsTeamLeadStatus(savedParticipantTeam.getId(),
				currentParticipant.getId());
		LOGGER.info("Exit registerTeamForCategory method -ParticipantTeamService");
		return savedParticipantTeam;

	}

	public String addParticipantsToTeam(Integer[] participants, ParticipantTeam participantTeam) throws Exception {
		LOGGER.info("Entered addParticipantsToTeam method -ParticipantTeamService");

		List<Participant> listAssignedParticipants = new ArrayList<>();
		Integer count = 0;
		if (!listAssignedParticipants.isEmpty()) {

			count = participantTeam.getParticipantTeamParticipants().size();
		}
		Integer participantSeqNum = 0;
		if (count == null || count == 0) {
			participantSeqNum = count;
		}
		if (participants != null) {

			for (Integer assignParticipantId : participants) {
				try {
					participantSeqNum++;
					Participant participant = participantService.getParticipantById(assignParticipantId);
					LOGGER.info("assignParticipantId : " + assignParticipantId);
					ParticipantTeamParticipants existingParticipantTeamParticipants = participantTeamParticipantsService
							.getByParticipantAndTeam(participant, participantTeam);
					if (existingParticipantTeamParticipants != null) {
						throw new Exception("Participant already exists.Please contact Administrator/Event Manager");
					} else {
						ParticipantTeamParticipants participantTeamParticipants = new ParticipantTeamParticipants();
						participantTeamParticipants.setParticipantTeam(participantTeam);
						participantTeamParticipants.setParticipant(participant);
						participantTeamParticipants.setSequenceNumber(participantSeqNum);
						ParticipantTeamParticipants savedParticipantTeamParticipants = participantTeamParticipantsService
								.save(participantTeamParticipants);

						// assign compulsory asanas only when creating new group
						assignCompulsoryAsanasForTeam(participantTeam, savedParticipantTeamParticipants);
						AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
						if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
								|| asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
								|| asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
							if (participantTeam.isDifferentAsanasForParticipants() == false) {
								// if not different asanas, add optional asanas which are added to others
								// already to the new participant.
								assignOptionalAsanasForNewParticipant(participantTeam,
										savedParticipantTeamParticipants);
							}
						}
					}

				} catch (ParticipantNotFoundException e) {
					LOGGER.error("ParticipantTeam not found!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		LOGGER.info("Exit addParticipantsToTeam method -ParticipantTeamService");
		return "Participants added successfully";
	}

	public Page<ParticipantTeam> listRegisteredTeamByPage(int pageNum, String sortField, String sortDir, String name,
			String chestNumber, String championshipName, String asanaCategory, String teamstatus) {
		LOGGER.info("Entered listRegisteredTeamByPage method -ParticipantTeamService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		// default filter for team
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.valueOf(teamstatus));

		if (name != "" && chestNumber != "" && championshipName != "" && asanaCategory != "") {
			// 0000 name chestnumber championship asanaCategory
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");

			return repo
					.findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							name, chestNumber, championshipName, asanaCategory, status, participantTeamStatus,
							pageable);

		} else if (name != "" && chestNumber != "" && championshipName != "" && asanaCategory == "") {
			// name chestnumber championship 0001
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
							name, chestNumber, championshipName, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName != "" && asanaCategory != "") {
			// name championship asanaCategory 0001
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");

			return repo
					.findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							name, championshipName, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber != "" && championshipName == "" && asanaCategory != "") {
			// name chestnumber asanaCategory 0010
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							name, chestNumber, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber != "" && championshipName != "" && asanaCategory != "") {
			// chestnumber championship asanaCategory 1000
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByChestNumberContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							chestNumber, championshipName, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber != "" && championshipName == "" && asanaCategory != "") {
			// chestnumber asanaCategory 1010
			LOGGER.info(
					"findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
					chestNumber, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber != "" && championshipName == "" && asanaCategory == "") {
			// name chestnumber 0011
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndChestNumberContainingAndChampionshipStatusNotInAndStatus(name,
					chestNumber, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber == "" && championshipName != "" && asanaCategory != "") {
			// championship asanaCategory1100
			LOGGER.info(
					"findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo
					.findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(
							championshipName, asanaCategory, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName != "" && asanaCategory == "") {
			// name championship 0101
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(name,
					championshipName, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName == "" && asanaCategory != "") {
			// name asanaCategory 0110
			LOGGER.info(
					"findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(name,
					asanaCategory, status, participantTeamStatus, pageable);
		} else if (name == "" && chestNumber != "" && championshipName != "" && asanaCategory == "") {
			// chestnumber championship 1001
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChestNumberContainingAndChampionshipNameContainingAndChampionshipStatusNotInAndStatus(
					chestNumber, championshipName, status, participantTeamStatus, pageable);
		} else if (name != "" && chestNumber == "" && championshipName == "" && asanaCategory == "") {
			// name 0111
			LOGGER.info(
					"findAllByNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByNameContainingAndChampionshipStatusNotInAndStatus(name, status, participantTeamStatus,
					pageable);
		} else if (name == "" && chestNumber != "" && championshipName == "" && asanaCategory == "") {
			// chestnumber 1011
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChestNumberContainingAndChampionshipStatusNotInAndStatus(chestNumber, status,
					participantTeamStatus, pageable);
		} else if (name == "" && chestNumber == "" && championshipName != "" && asanaCategory == "") {
			// championship 1101
			LOGGER.info(
					"findAllByChampionshipNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByChampionshipNameContainingAndChampionshipStatusNotInAndStatus(championshipName, status,
					participantTeamStatus, pageable);
		} else if (name == "" && chestNumber == "" && championshipName == "" && asanaCategory != "") {
			// asanaCategory 1110
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus method called -ParticipantTeamService");
			return repo.findAllByAsanaCategoryNameContainingAndChampionshipStatusNotInAndStatus(asanaCategory, status,
					participantTeamStatus, pageable);
		} else {
			LOGGER.info("findAllByChampionshipStatusNotIn method called -ParticipantTeamService");
			LOGGER.info("Exit listRegisteredTeamByPage method -ParticipantTeamService");
			return repo.findAllByChampionshipStatusNotInAndStatusIn(status, participantTeamStatus, pageable);
		}
	}

	public Page<ParticipantTeam> eventManagerlistRegisteredTeamByPage(Integer championshipId, int pageNum,
			String sortField, String sortDir, String name, String chestNumber, String teamstatus, String asanaCategory)
			throws ChampionshipNotFoundException {
		LOGGER.info("Entered eventManagerlistRegisteredTeamByPage method -ParticipantTeamService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		List<RegistrationStatusEnum> participantTeamStatus = new ArrayList<>();
		participantTeamStatus.add(RegistrationStatusEnum.valueOf(teamstatus));

		Championship championship = championshipService.get(championshipId);
		if (name != "" && chestNumber != "" && asanaCategory != "") {
			// name chestnumber asanaCategory 0010
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method  -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							name, chestNumber, asanaCategory, championship.getName(), status, participantTeamStatus,
							championshipId, pageable);
		} else if (name != "" && chestNumber != "" && asanaCategory == "") {
			// name chestNumber
			LOGGER.info(
					"findAllByNameContainingAndChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method  -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							name, chestNumber, championship.getName(), status, participantTeamStatus, championshipId,
							pageable);

		} else if (name != "" && chestNumber == "" && asanaCategory != "") {
			// name asanaCategory
			LOGGER.info(
					"findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method  -ParticipantTeamService");
			return repo
					.findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							name, asanaCategory, championship.getName(), status, participantTeamStatus, championshipId,
							pageable);

		} else if (name != "" && chestNumber == "" && asanaCategory == "") {
			// name
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method -ParticipantTeamService");
			return repo.findAllByNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
					name, championship.getName(), status, participantTeamStatus, championshipId, pageable);

		} else if (name == "" && chestNumber != "" && asanaCategory != "") {
			// chestNumber asanasCategory
			LOGGER.info(
					"findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			return repo
					.findAllByChestNumberContainingAndAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							chestNumber, asanaCategory, championship.getName(), status, participantTeamStatus,
							championshipId, pageable);

		} else if (name == "" && chestNumber != "" && asanaCategory == "") {
			// chestNumber
			LOGGER.info(
					"findAllByChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			return repo
					.findAllByChestNumberContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							chestNumber, championship.getName(), status, participantTeamStatus, championshipId,
							pageable);

		} else if (name == "" && chestNumber == "" && asanaCategory != "") {
			// asana Category
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			return repo
					.findAllByAsanaCategoryNameContainingAndChampionshipNameAndChampionshipStatusNotInAndStatusAndChampionshipId(
							asanaCategory, championship.getName(), status, participantTeamStatus, championshipId,
							pageable);

		} else {
			LOGGER.info(
					"findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId method called -ParticipantTeamService");
			LOGGER.info("Exit eventManagerlistRegisteredTeamByPage method -ParticipantTeamService");
			return repo.findAllByChampionshipAndChampionshipStatusNotInAndStatusAndChampionshipId(
					championship.getName(), status, participantTeamStatus, championshipId, pageable);
		}

	}

	public void updateRegisteredTeamStatus(ParticipantTeam participantTeam, RegistrationStatusEnum teamStatus) {
		participantTeam.setStatus(teamStatus);
		repo.save(participantTeam);

	}

	public List<ParticipantTeam> listAllParticipantTeamsForChampionshipAndStatus(Championship championship,
			List<RegistrationStatusEnum> pendingStatus) {

		return repo.findByChampionshipAndStatusIn(championship, pendingStatus);
	}

	public List<ParticipantTeam> listOfChampionshipParticipantTeamsForParticipant(Championship championship,
			User currentUser) {
		// TODO Auto-generated method stub
		return repo.findAllParticipantTeamsByChampionshipAndCurrentUser(championship, currentUser.getId());
	}

}
