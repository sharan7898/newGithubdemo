package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.AsanaLimit;
import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamPanel;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.AsanaCodeDTO;
import com.swayaan.nysf.entity.DTO.RoundAsanaStatusDTO;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamAsanasNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamParticipantsNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamPanelService;
import com.swayaan.nysf.service.ParticipantTeamParticipantAsanasStatusService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageParticipantTeamController {

	@Autowired
	ParticipantTeamService service;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	AsanaService asanaService;
	@Autowired
	UserService userService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;

	@Autowired
	ChampionshipRefereePanelsService championshipRefereePanelsService;
	@Autowired
	RefereesPanelsService refereesPanelsService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
//	@Autowired
//	ChampionshipParticipantsService championshipParticipantService;
	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	@Autowired
	RoleService roleService;

	@Autowired
	AsanaLimit asanaLimit;

	@Autowired
	JudgeTypeService judgeTypeService;

	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;
	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;

	@Autowired
	ParticipantTeamPanelService participantTeamPanelService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageParticipantTeamController.class);

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int RHYTHMIC_PAIR_PARTICIPANT_COUNT = 2;
	private static final int ARTISTIC_GROUP_PARTICIPANT_COUNT = 5;

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team")
	public String listFirstPageTeams(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageTeams method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageTeams method -ManageParticipantTeamController");

		return listAllTeamsByPage(1, model, "name", "asc", "", "", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/page/{pageNum}")
	public String listAllTeamsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String chestNumber,
			@RequestParam(name = "keyword3", required = false) String championshipName,
			@RequestParam(name = "keyword4", required = false) String asanaCategory, HttpServletRequest request) {
		LOGGER.info("Entered listAllTeamsByPage method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Page<ParticipantTeam> page = service.listByPage(pageNum, sortField, sortDir, name, chestNumber,
				championshipName, asanaCategory);
		List<ParticipantTeam> listParticipantTeam = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-team");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", chestNumber);
		model.addAttribute("keyword3", championshipName);
		model.addAttribute("keyword4", asanaCategory);
		ParticipantTeam participantTeam = new ParticipantTeam();
		model.addAttribute("listParticipantTeam", listParticipantTeam);
		model.addAttribute("pageTitle", "Manage Teams");
		model.addAttribute("participantTeam", participantTeam);
		LOGGER.info("Exit listAllTeamsByPage method -ManageParticipantTeamController");

		return "administration/manage_team";
	}

//	@GetMapping("/manage-team")
//	public String listAllParticipantTeams(Model model, HttpServletRequest request) {
//		LOGGER.info("Entered listAllParticipantTeams controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		List<ParticipantTeam> listParticipantTeams = service.listAllParticipantTeams();
//		model.addAttribute("listParticipantTeams", listParticipantTeams);
//		model.addAttribute("pageTitle", "Manage Teams");
//		LOGGER.info("Redirected to manage_team page");
//		return "administration/manage_team";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/new")
	public String newParticipantTeam(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newParticipantTeam method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam = new ParticipantTeam();
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("participantTeam", participantTeam);
		model.addAttribute("pageTitle", "Add Team");
		model.addAttribute("listChampionships", listChampionships);
		LOGGER.info("Redirected to team_form page");
		LOGGER.info("Exit newParticipantTeam method -ManageParticipantTeamController");

		return "administration/team_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-team/save")
	public String saveParticipantTeam(ParticipantTeam participantTeam, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException {
		LOGGER.info("Entered saveParticipantTeam method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				participantTeam.getChampionship().getId(), participantTeam.getAsanaCategory().getId(),
				participantTeam.getCategory().getId(), participantTeam.getGender().toString());

		List<ChampionshipRounds> listChampionshipRounds = championshipRoundsService
				.findByChampionshipAndChampionshipCategory(participantTeam.getChampionship(), championshipCategory);

		List<ChampionshipRounds> listFilteredChampionshipRoundsWithCompletedStatus = listChampionshipRounds.stream()
				.filter(championshipRounds -> championshipRounds.getStatus().equals(RoundStatusEnum.COMPLETED))
				.collect(Collectors.toList());
		if (!listFilteredChampionshipRoundsWithCompletedStatus.isEmpty()) {
			redirectAttributes.addFlashAttribute("message1",
					"Unable to create Teams. Championship Round is completed.");
			return "redirect:/admin/manage-team";

		}

		participantTeam.setStatus(RegistrationStatusEnum.APPROVED);
		Boolean teamChestNumber = service.checkTeamChestNumber(participantTeam.getChestNumber(),
				participantTeam.getChampionship());

		boolean isUpdatingTeam = (participantTeam.getId() != null);

		if (participantTeam.getId() != null) {
			ParticipantTeam existingParticipantTeam = service.findById(participantTeam.getId());
			Boolean teamEditChestNumber = service.checkTeamChestNumberForEdit(participantTeam.getChestNumber(),
					participantTeam.getChampionship(), participantTeam.getId());
			if (teamEditChestNumber) {
				redirectAttributes.addFlashAttribute("message1",
						"The Team with the chest number " + participantTeam.getChestNumber() + " is already present.");
				return "redirect:/admin/manage-team";
			}
			boolean existingStatus = existingParticipantTeam.isDifferentAsanasForParticipants();

			List<ParticipantTeamParticipants> listAssignedParticipants = existingParticipantTeam
					.getParticipantTeamParticipants();
			if (listAssignedParticipants.size() != 0) {
				if (participantTeam.isDifferentAsanasForParticipants() == existingStatus) {
					participantTeam.setDifferentAsanasForParticipants(existingStatus);
				}
//				else {
//					participantTeam.setDifferentAsanasForParticipants(existingStatus);
//					redirectAttributes.addFlashAttribute("errorMessage",
//							"You cannot change the status of the field Different Asanas!");
//				}

			}
		}

		if (isUpdatingTeam) {
			try {
				service.save(participantTeam);
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message1", e.getMessage());
				return "redirect:/admin/manage-team";
			}

			redirectAttributes.addFlashAttribute("message",
					"The Team " + participantTeam.getTeamName() + " has been updated successfully.");

			// redirectAttributes.addFlashAttribute("message", "The Team has been updated
			// successfully.");
			return "redirect:/admin/manage-team/edit/" + participantTeam.getId();
		} else {
			if (teamChestNumber) {
				redirectAttributes.addFlashAttribute("message1",
						"The Team with the chest number " + participantTeam.getChestNumber() + " is already present.");
				return "redirect:/admin/manage-team";
			} else {
				try {
					service.save(participantTeam);
				} catch (Exception e) {
					redirectAttributes.addFlashAttribute("message1", e.getMessage());
					return "redirect:/admin/manage-team";
				}
			}

		}

		try {
			service.save(participantTeam);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message1", e.getMessage());
			return "redirect:/admin/manage-team";
		}
		redirectAttributes.addFlashAttribute("message", "The Team " + participantTeam.getName() + " has been saved successfully.");
		LOGGER.info("Exit saveParticipantTeam method -ManageParticipantTeamController");
		return "redirect:/admin/manage-team/edit/" + participantTeam.getId();
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/edit/{id}")
	public String editParticipantTeam(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered editParticipantTeam method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			ParticipantTeam participantTeam = service.get(id);
			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
//			List<Participant> listParticipants = participantService
//					.listAllParticipantsForGender(participantTeam.getGender().toString());

			// List<ParticipantTeamParticipants> listAssignedParticipants =
			// participantTeam.getParticipantTeamParticipants();
			List<ParticipantTeamParticipants> listAssignedParticipants = participantTeamParticipantsService
					.getByTeamOrderBySequenceNumberAsc(participantTeam);
			LOGGER.info("listAssignedParticipants :" + listAssignedParticipants.size());

			List<Participant> listNonAssignedParticipants = participantService
					.listAllParticipantsForGender(participantTeam);
//
//			List<ParticipantTeam> listPartiTeam = participantTeamService
//					.listAllParticipantTeamsForChampionshipAndAsanaCategory(participantTeam.getChampionship().getId(),
//							participantTeam.getAsanaCategory().getId(), participantTeam.getGender().toString(),
//							participantTeam.getCategory().getId());
//			for (ParticipantTeam partiTeam : listPartiTeam) {
//				for (Participant paricipant : listParticipants) {
//					ParticipantTeamParticipants participantTeamParticipants = participantTeamParticipantsService
//							.getByParticipantAndTeam(paricipant, partiTeam);
//
//					if (participantTeamParticipants == null) {
//						listNonAssignedParticipants.add(paricipant);
//					}
//				}
//			}
			// LOGGER.info("listAssignedParticipants :" + listAssignedParticipants.size());
			LOGGER.info("listNonAssignedParticipants :" + listNonAssignedParticipants.size());

			List<ParticipantTeamReferees> listAssignedReferees = participantTeam.getParticipantTeamReferees();

			Integer noOfRounds = championshipCategoryService.getChampionshipCategoryRoundsForChampionshipId(
					participantTeam.getChampionship(), participantTeam.getAsanaCategory(),
					participantTeam.getCategory(), participantTeam.getGender());
			LOGGER.info("noOfRounds :" + noOfRounds);

			Integer participantsLimit = championshipCategoryService
					.getChampionshipCategoryParticipantsForChampionshipId(participantTeam.getChampionship(),
							participantTeam.getAsanaCategory(), participantTeam.getCategory(),
							participantTeam.getGender());
			LOGGER.info("noOfParticipants :" + participantsLimit);

			List<ParticipantTeamAsanas> listAsanas = new ArrayList<ParticipantTeamAsanas>();
			List<List<ParticipantTeamAsanas>> listRoundWiseParticipantTeamAsanas = new ArrayList<>();
			for (Integer round = 1; round <= noOfRounds; round++) {
//				if((participantTeam.getAsanaCategory().getId() == 1) || (participantTeam.getAsanaCategory().getId() == 2) ) {
//					listAsanas = participantTeamAsanasService.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
//				} else if((participantTeam.getAsanaCategory().getId() == 3) && (participantTeam.isDifferentAsanasForParticipants() == false)) {
//					listAsanas = participantTeamAsanasService.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
//				} else {
//					listAsanas = participantTeamAsanasService.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
//				}
				listAsanas = participantTeamAsanasService
						.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
				listRoundWiseParticipantTeamAsanas.add(listAsanas);
			}

			// System.out.println(listRoundWiseParticipantTeamAsanas);
			List<ParticipantTeamReferees> listReferees = new ArrayList<>();
			List<List<ParticipantTeamReferees>> listRoundWiseParticipantTeamReferees = new ArrayList<>();
			for (Integer round = 1; round <= noOfRounds; round++) {
				listReferees = participantTeamRefereesService.getByTeamAndRound(participantTeam, round);

				listRoundWiseParticipantTeamReferees.add(listReferees);
			}

			// Mapping of each round asanas to the participant
			LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<Integer, List<ParticipantTeamAsanas>>> mapParticipantsWithRoundAsanas = new LinkedHashMap<>();
			List<ParticipantTeamParticipants> listParticipantsForTeam = participantTeamParticipantsService
					.getByTeamOrderBySequenceNumberAsc(participantTeam);
			for (ParticipantTeamParticipants participantTeamParticipant : listParticipantsForTeam) {
				LinkedHashMap<Integer, List<ParticipantTeamAsanas>> mapRoundAsanas = new LinkedHashMap<>();
				for (Integer round = 1; round <= noOfRounds; round++) {
					List<ParticipantTeamAsanas> listParticipantRoundAsanas = new ArrayList<ParticipantTeamAsanas>();
					listParticipantRoundAsanas = participantTeamAsanasService
							.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
									participantTeamParticipant, round);
					mapRoundAsanas.put(round, listParticipantRoundAsanas);
				}
				if (mapRoundAsanas.size() != 0) {
					mapParticipantsWithRoundAsanas.put(participantTeamParticipant, mapRoundAsanas);
				}
			}

			List<RoundAsanaStatusDTO> listRoundAsanaStatus = new ArrayList<>();
			for (Integer round = 1; round <= noOfRounds; round++) {
				ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = participantTeamParticipantAsanasStatusService
						.findByParticipantTeamAndRound(participantTeam, round);
				if (participantTeamParticipantAsanasStatus == null) {
					listRoundAsanaStatus.add(new RoundAsanaStatusDTO(round, AsanaStatusEnum.UNASSIGNED.toString()));
				} else {
					listRoundAsanaStatus.add(new RoundAsanaStatusDTO(round,
							participantTeamParticipantAsanasStatus.getAsanaStatus().toString()));

				}
			}
			model.addAttribute("listRoundAsanaStatus", listRoundAsanaStatus);
			model.addAttribute("mapParticipantsWithRoundAsanas", mapParticipantsWithRoundAsanas);

			List<Asana> listAllAsanas = asanaService.listAllAsanas();
			model.addAttribute("participantTeam", participantTeam);
			model.addAttribute("pageTitle", "Edit Team");
			model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("listAllAsanas", listAllAsanas);
			model.addAttribute("listAssignedParticipants", listAssignedParticipants);
			model.addAttribute("listAssignedReferees", listAssignedReferees);
			model.addAttribute("listNonAssignedParticipants", listNonAssignedParticipants);
			model.addAttribute("listRoundWiseParticipantTeamAsanas", listRoundWiseParticipantTeamAsanas);
			model.addAttribute("listRoundWiseParticipantTeamReferees", listRoundWiseParticipantTeamReferees);
			model.addAttribute("noOfRounds", noOfRounds);

			Integer limit = -1;
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
				limit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
				limit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				limit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				limit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				limit = asanaLimit.getArtisticGroupLimit();
			} else {
				limit = asanaLimit.getCommonLimit();
			}

			model.addAttribute("participantsLimit", participantsLimit);
			model.addAttribute("asanasLimit", limit);
			LOGGER.info("Redirected to team_form page");

			return "administration/team_form";
		} catch (ParticipantTeamNotFoundException ex) {
			LOGGER.error("ParticipantTeam not found!");
			redirectAttributes.addFlashAttribute("message", "Team not found.");
			LOGGER.info("Exit editParticipantTeam method -ManageParticipantTeamController");

			return "redirect:/admin/manage-team";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/delete/{id}")
	public String deleteParticipantTeam(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered deleteParticipantTeam method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			ParticipantTeam participantTeam = service.get(id);
			if (!service.checkTeamUpdateAllowed(participantTeam)) {
				redirectAttributes.addFlashAttribute("message1",
						"The Team " + participantTeam.getTeamName() + "cannot be deleted");
				return "redirect:/admin/manage-team";
			}
			// check for dependencies
			/*
			 * ParticipantTeamAsanas, ParticipantTeamParticipants,
			 * ParticipantTeamReferees,ParticipantTeamAsanaScores
			 * ,ParticipantTeamTotalscores,ParticipantTeamRoundTotalScore
			 */

			// List<ParticipantTeamAsanaScoring> participantTeamAsanaScore =
			// participantTeamAsanaScoringService.getParticipantTeamAsanaScoreForParticpantTeam(participantTeam);
			List<ParticipantTeamAsanas> participantTeamAsanas = participantTeamAsanasService
					.getTeamAsanasForParticipantTeam(participantTeam);
			List<ParticipantTeamParticipants> participantTeamParticipants = participantTeamParticipantsService
					.getTeamParticipantsForParticipantTeam(participantTeam);
			List<ParticipantTeamReferees> participantTeamReferees = participantTeamRefereesService
					.getTeamReferresForParticipantTeam(participantTeam);

//			 if(!participantTeamAsanaScore.isEmpty()) {
//					System.out.println("ParticipantTeamAsanaScore Team");
//					redirectAttributes.addFlashAttribute("message1", 
//							"The Team " + participantTeam.getTeamName() + " is allocated to ParticipantTeamAsanaScore, Please remove before you delete");
//					return "redirect:/admin/manage-team";
//				}

			if (!participantTeamAsanas.isEmpty()) {
				System.out.println("participantTeamAsanas Team");
				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
						+ " is allocated to participant team asanas, Please remove before you delete");
				return "redirect:/admin/manage-team";
			}

			if (!participantTeamParticipants.isEmpty()) {
				System.out.println("participantTeamParticipants Team");
				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
						+ " is allocated to team participants, Please remove before you delete");
				return "redirect:/admin/manage-team";
			}

			if (!participantTeamReferees.isEmpty()) {
				System.out.println("participantTeamReferees Team");
				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
						+ " is alocated to team referees, Please remove before you delete");
				return "redirect:/admin/manage-team";
			}
			service.updateNumberOfParticipantTeams(service.getChampionshipRoundId(participantTeam));
			service.delete(id);

			redirectAttributes.addFlashAttribute("message",
					"The Team " + participantTeam.getName() + " has been deleted successfully");

		} catch (ParticipantTeamNotFoundException ex) {
			LOGGER.error("ParticipantTeam not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteParticipantTeam method -ManageParticipantTeamController");
		return "redirect:/admin/manage-team";
	}

	// Add participants to team
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-team/participants/assign/{participantTeam_id}")
	public String assignTeamParticipants(@RequestParam(value = "participants", required = false) Integer[] participants,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		LOGGER.info("Entered assignTeamParticipants method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam = service.findById(participantTeam_id);

		if (participantTeamService.checkTeamUpdateAllowed(participantTeam)) {
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			int count = participantTeam.getParticipantTeamParticipants().size();

			Integer participantsLimit = championshipCategoryService
					.getChampionshipCategoryParticipantsForChampionshipId(participantTeam.getChampionship(),
							participantTeam.getAsanaCategory(), participantTeam.getCategory(),
							participantTeam.getGender());
			LOGGER.info("noOfParticipants :" + participantsLimit);

			String errorMessage = null;
			String message = null;
			if ((count + participants.length) > participantsLimit) {
				errorMessage = "Only " + participantsLimit + " participant can be added for this category";
			} else if ((count + participants.length) <= participantsLimit) {
				message = service.addParticipantsToTeam(participants, participantTeam);
			}

			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			redirectAttributes.addFlashAttribute("message", message);
			LOGGER.info("Redirected to manage-team/edit page");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to add participant to the team");
			LOGGER.info("Exit assignTeamParticipants method -ManageParticipantTeamController");

			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/participants/delete/{participantTeamParticipantsId}")
	public String deleteAssignedTeamParticipants(@PathVariable(name = "participantTeamParticipantsId") Integer id,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamParticipantsNotFoundException,
			ParticipantTeamAsanasNotFoundException {
		LOGGER.info("Entered deleteAssignedTeamParticipants method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeamParticipants participantTeamParticipants = participantTeamParticipantsService.get(id);
		ParticipantTeam participantTeam = participantTeamParticipants.getParticipantTeam();
		if (!participantTeam.getChampionship().getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Unable to delete participant because championship is ongoing/completed");
			return "redirect:/admin/manage-team/edit/" + participantTeam.getId();
		}

		if (participantTeamService.checkTeamUpdateAllowed(participantTeam)) {
			Integer participantTeamId = participantTeam.getId();

			// make participant team asana status to pending
			List<ParticipantTeamParticipantAsanasStatus> listParticipantTeamParticipantAsanasStatus = participantTeamParticipantAsanasStatusService
					.findAllByParticipantTeam(participantTeam);
			for (ParticipantTeamParticipantAsanasStatus teamAsanasRoundStatus : listParticipantTeamParticipantAsanasStatus) {
				ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = participantTeamParticipantAsanasStatusService
						.getById(teamAsanasRoundStatus.getId());
				participantTeamParticipantAsanasStatus.setAsanaStatus(AsanaStatusEnum.DRAFTS);
				participantTeamParticipantAsanasStatusService.save(participantTeamParticipantAsanasStatus);
			}

			if (participantTeam.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				// delete participantTeamAsanas for the given participantTeam
				List<ParticipantTeamAsanas> listAssignedAsanas = participantTeamAsanasService
						.getTeamAsanasForParticipantTeam(participantTeam);
				for (ParticipantTeamAsanas teamAsanas : listAssignedAsanas) {
					participantTeamAsanasService.deleteById(teamAsanas.getId());
				}

			} else if (participantTeam.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				List<ParticipantTeamAsanas> listAssignedAsanas = participantTeamAsanasService
						.getTeamAsanasForParticipantTeam(participantTeam);
				for (ParticipantTeamAsanas teamAsanas : listAssignedAsanas) {
					participantTeamAsanasService.deleteById(teamAsanas.getId());
				}
			} else {

				// delete participantTeamAsanas for the given participantTeamParticipantsId
				List<ParticipantTeamAsanas> listAssignedAsanas = participantTeamAsanasService
						.getByTeamAndParticipantTeamParticipants(participantTeam, participantTeamParticipants);
				for (ParticipantTeamAsanas teamAsanas : listAssignedAsanas) {
					participantTeamAsanasService.deleteById(teamAsanas.getId());
				}
			}

			// delete participantTeamReferees for the given participantTeamId
			List<ParticipantTeamReferees> listAssignedParticipantTeamReferees = participantTeamRefereesService
					.getTeamReferresForParticipantTeamAndUser(participantTeam);
			for (ParticipantTeamReferees participantTeamReferees : listAssignedParticipantTeamReferees) {
				participantTeamRefereesService.delete(participantTeamReferees.getId());
			}

			if (!listParticipantTeamParticipantAsanasStatus.isEmpty()) {
				for (ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus : listParticipantTeamParticipantAsanasStatus) {
					participantTeamParticipantAsanasStatusService
							.delete(participantTeamParticipantAsanasStatus.getId());
				}
			}

			participantTeamParticipantsService.deleteById(id);

			redirectAttributes.addFlashAttribute("message", "Participant removed Successfully");
			LOGGER.info("Exit deleteAssignedTeamParticipants method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/" + participantTeamId;
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to add participant to the team");
			return "redirect:/admin/manage-team/edit/" + participantTeam.getId();
		}

	}

	/**
	 * @param participantTeam_id
	 * @param round_type
	 * @param model
	 * @param ra
	 * @return
	 */
//	@GetMapping("/manage-team/asanas/load/{participantTeam_id}/round/{round_type}")
//	public String loadTeamRoundAsanas(@PathVariable("participantTeam_id") Integer participantTeam_id,
//			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra) {
//		LOGGER.info("Entered loadTeamRoundAsanas controller");
//		model.addAttribute("participantTeam_id", participantTeam_id);
//
//		try {
//			ParticipantTeam participantTeam = service.get(participantTeam_id);
//			model.addAttribute("participantTeam", participantTeam);
//
//			List<Asana> listAllAsanas = asanaService.listAllAsanas();
//			model.addAttribute("listAllAsanas", listAllAsanas);
//
//			model.addAttribute("round", round_type);
//
//			List<Asana> listNonSelectedOptionalAsanas = asanaService
//					.listAllNonSelectedOptionalAsanasForRound(participantTeam, round_type);
//			model.addAttribute("listOptionalAsanas", listNonSelectedOptionalAsanas);
//			LOGGER.info("listNonSelectedOptionalAsanas size: " + listNonSelectedOptionalAsanas.size());
//			
//			// get the count of compulsory asanas for each round
//			List<CompulsoryRoundAsanas> listRoundCompulsoryAsanas = compulsoryRoundAsanasService
//					.getCompulsoryAsanas(participantTeam, round_type);
//			LOGGER.info("listRoundCompulsoryAsanas size: " + listRoundCompulsoryAsanas.size());
//
//			Integer roundCompulsoryAsanasCount = listRoundCompulsoryAsanas.size();
//			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//			Integer totalAsanasLimit = -1;
//			Integer roundOptionalAsanasLimit = null;
//
//			if (asanaCategory.getId() == 1) {
//				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
//			} else if (asanaCategory.getId() == 2) {
//				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
//			} else if (asanaCategory.getId() == 3) {
//				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
//			} else if (asanaCategory.getId() == 4) {
//				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
//			} else if (asanaCategory.getId() == 5) {
//				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
//			} else {
//				totalAsanasLimit = asanaLimit.getCommonLimit();
//			}
//			
//			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;
//			
//			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
//			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);
//			LOGGER.info("Redirected to team_round_asanas_modal page");
//			return "administration/team_round_asanas_modal";
//
//		} catch (ParticipantTeamNotFoundException e) {
//			LOGGER.error("ParticipantTeam not found!");
//			ra.addFlashAttribute("message", e.getMessage());
//
//			return "redirect:/admin/manage-team/edit/" + participantTeam_id;
//		}
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/asanas/load/{participantTeam_id}/round/{round_type}")
	public String loadTeamRoundAsanas(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		LOGGER.info("Entered loadTeamRoundAsanas method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);

		try {
			ParticipantTeam participantTeam = service.get(participantTeam_id);
			model.addAttribute("participantTeam", participantTeam);

			List<Asana> listAllAsanas = asanaService.listAllAsanas();
			model.addAttribute("listAllAsanas", listAllAsanas);

			model.addAttribute("round", round_type);

			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
					.listExecutionScoreByAsanaCategoryAndGender(participantTeam.getAsanaCategory(),
							participantTeam.getGender());

			List<Asana> listNonSelectedOptionalAsanas = asanaService
					.listAllNonSelectedOptionalAsanasForRound(participantTeam, round_type);

			List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();

			for (Asana asana : listNonSelectedOptionalAsanas) {
				Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
						.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
				if (listAsanaCode.isPresent()) {
					listAsanasWithCode.add(
							new AsanaCodeDTO(asana, listAsanaCode.get().getCode(), listAsanaCode.get().getSubGroup()));
				} else {
					listAsanasWithCode.add(new AsanaCodeDTO(asana, "------", null));
				}

			}

			Collections.sort(listAsanasWithCode, new Comparator<AsanaCodeDTO>() {
				@Override
				public int compare(AsanaCodeDTO a1, AsanaCodeDTO a2) {
					return a1.getCode().compareTo(a2.getCode());
				}
			});

			model.addAttribute("listOptionalAsanas", listAsanasWithCode);
			LOGGER.info("listNonSelectedOptionalAsanas size: " + listNonSelectedOptionalAsanas.size());
			LOGGER.info("listAsanasWithCode size: " + listAsanasWithCode.size());

			// get the count of compulsory asanas for each round
			List<CompulsoryRoundAsanas> listRoundCompulsoryAsanas = compulsoryRoundAsanasService
					.getCompulsoryAsanas(participantTeam, round_type);
			LOGGER.info("listRoundCompulsoryAsanas size: " + listRoundCompulsoryAsanas.size());

			Integer roundCompulsoryAsanasCount = listRoundCompulsoryAsanas.size();
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			Integer totalAsanasLimit = -1;
			Integer roundOptionalAsanasLimit = null;

			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
			} else {
				totalAsanasLimit = asanaLimit.getCommonLimit();
			}

			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;

			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);
			LOGGER.info("Exit loadTeamRoundAsanas method -ManageParticipantTeamController");
			return "administration/team_round_asanas_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!" + e.getMessage());
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/admin/manage-team/edit/" + participantTeam_id;
		}
	}

//	@GetMapping("/manage-team/asanas/load/{participantTeam_id}/round/{round_type}")
//	public String loadTeamRoundAsanas(@PathVariable("participantTeam_id") Integer participantTeam_id,
//			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra) {
//		LOGGER.info("Entered loadTeamRoundAsanas controller");
//		model.addAttribute("participantTeam_id", participantTeam_id);
//
//		try {
//			ParticipantTeam participantTeam = service.get(participantTeam_id);
//			model.addAttribute("participantTeam", participantTeam);
//			// ******* List listAllAsanas are replaced by listAsanaExecutionScores *******
//			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
//					.listExecutionScoreByAsanaCategoryAndGender(participantTeam.getAsanaCategory(),
//							participantTeam.getGender());
//			model.addAttribute("listAllAsanas", listAsanaExecutionScores);
//			model.addAttribute("round", round_type);
//
//			List<Asana> listNonSelectedOptionalAsanas = asanaService
//					.listAllNonSelectedOptionalAsanasForRound(participantTeam, round_type);
//
//			List<AsanaExecutionScore> listNonSelectedOptionalAsanaExecutionScores = new ArrayList<>();
//
//			if (!listNonSelectedOptionalAsanas.isEmpty() && !listAsanaExecutionScores.isEmpty())
//				for (AsanaExecutionScore asanaExecutionScore : listAsanaExecutionScores) {
//					if (listNonSelectedOptionalAsanas.contains(asanaExecutionScore.getAsana())) {
//						listNonSelectedOptionalAsanaExecutionScores.add(asanaExecutionScore);
//					}
//
//				}
//			Collections.sort(listNonSelectedOptionalAsanaExecutionScores, new Comparator<AsanaExecutionScore>() {
//				@Override
//				public int compare(AsanaExecutionScore a1, AsanaExecutionScore a2) {
//					return a1.getCode().compareTo(a2.getCode());
//				}
//			});
//
//			model.addAttribute("listOptionalAsanas", listNonSelectedOptionalAsanaExecutionScores);
//			// model.addAttribute("listOptionalAsanas", listNonSelectedOptionalAsanas);
//			LOGGER.info("listNonSelectedOptionalAsanas size: " + listNonSelectedOptionalAsanas.size());
//
//			// get the count of compulsory asanas for each round
//			List<CompulsoryRoundAsanas> listRoundCompulsoryAsanas = compulsoryRoundAsanasService
//					.getCompulsoryAsanas(participantTeam, round_type);
//			LOGGER.info("listRoundCompulsoryAsanas size: " + listRoundCompulsoryAsanas.size());
//
//			Integer roundCompulsoryAsanasCount = listRoundCompulsoryAsanas.size();
//			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//			Integer totalAsanasLimit = -1;
//			Integer roundOptionalAsanasLimit = null;
//
//			if (asanaCategory.getId() == 1) {
//				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
//			} else if (asanaCategory.getId() == 2) {
//				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
//			} else if (asanaCategory.getId() == 3) {
//				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
//			} else if (asanaCategory.getId() == 4) {
//				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
//			} else if (asanaCategory.getId() == 5) {
//				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
//			} else {
//				totalAsanasLimit = asanaLimit.getCommonLimit();
//			}
//
//			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;
//
//			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
//			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);
//			LOGGER.info("Redirected to team_round_asanas_modal page");
//			return "administration/team_round_asanas_modal";
//
//		} catch (ParticipantTeamNotFoundException e) {
//			LOGGER.error("ParticipantTeam not found!");
//			ra.addFlashAttribute("message", e.getMessage());
//
//			return "redirect:/admin/manage-team/edit/" + participantTeam_id;
//		}
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-team/asanas/assign/{participantTeam_id}")
	public String assignTeamAsanas(
			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
			@RequestParam(value = "round", required = true) Integer round,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id, RedirectAttributes ra,
			HttpServletRequest request) {
		LOGGER.info("Entered assignTeamAsanas method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		// function when common asanas are assigned to the team participants.
		ParticipantTeam participantTeam = service.findById(participantTeam_id);
		// Check if asana round status is frozen
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = participantTeamParticipantAsanasStatusService
				.findByParticipantTeamAndRound(participantTeam, round);
		if (existingTeamAsanasStatus != null
				&& existingTeamAsanasStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE)) {
			LOGGER.info("Asanas are already forzen");
			ra.addFlashAttribute("message", "Unable to add asanas. Asanas are frozen.");
			LOGGER.info("Exit assignTeamAsanas method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		}

		if (service.checkTeamAsanasUpdateAllowed(participantTeam, round)) {

			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			String asanaCategoryName = participantTeam.getAsanaCategoryName();

			String message = null;
			// int count =
			// participantTeamAsanasService.getByTeamAndRoundOrderBySequenceNum(participantTeam,
			// round).size();
			int count = participantTeamAsanasService
					.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round).size();
			LOGGER.info("Count of asanas :" + count);
			LOGGER.info("Count of ParticipantTeamAsanas :" + participantTeamAsanas.length);
			LOGGER.info("Total count :" + (count + participantTeamAsanas.length));

			Integer totalAsanasLimit = -1;

			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
			} else {
				totalAsanasLimit = asanaLimit.getCommonLimit();
			}

			if ((count + participantTeamAsanas.length) > totalAsanasLimit) {
				message = count + " asanas are  added. You can add only " + (totalAsanasLimit - count)
						+ " asanas";
			} else if ((count + participantTeamAsanas.length) <= totalAsanasLimit) {

				List<Asana> listAssignedAsanas = new ArrayList<>();
				if (participantTeamAsanas != null) {

					List<ParticipantTeamParticipants> listAssignedParticipants = participantTeamParticipantsService
							.getTeamParticipantsForParticipantTeam(participantTeam);
					if (listAssignedParticipants.size() == 0) {
						ra.addFlashAttribute("errorMessage", "Add participant to the team before adding asanas");
						LOGGER.info("Redirected to manage-team/edit page");
						return "redirect:/admin/manage-team/edit/" + participantTeam.getId();

					}
					if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
						if (listAssignedParticipants.size() != RHYTHMIC_PAIR_PARTICIPANT_COUNT) {
							ra.addFlashAttribute("errorMessage", "Add 2 participants to the team before adding asanas");
							LOGGER.info("Redirected to manage-team/edit page");
							return "redirect:/admin/manage-team/edit/" + participantTeam.getId();
						}
					}

					if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
						if (listAssignedParticipants.size() != ARTISTIC_GROUP_PARTICIPANT_COUNT) {
							ra.addFlashAttribute("errorMessage", "Add 5 participants to the team before adding asanas");
							LOGGER.info("Redirected to manage-team/edit page");
							return "redirect:/admin/manage-team/edit/" + participantTeam.getId();
						}

					}

					ListIterator<ParticipantTeamParticipants> listAllAssignedParticipants = listAssignedParticipants
							.listIterator();

					while (listAllAssignedParticipants.hasNext()) {
						ParticipantTeamParticipants participantTeamParticipant = listAllAssignedParticipants.next();

						// for (ParticipantTeamParticipants participantTeamParticipant :
						// listAssignedParticipants) {
						int seqNum = count;
						for (Integer assignAsanaId : participantTeamAsanas) {
							seqNum++;
							try {
								LOGGER.info("assignAsanaId : " + assignAsanaId);
								Asana asana = asanaService.get(assignAsanaId);
								AsanaExecutionScore asanaExecScore = asanaExecutionScoreService
										.getBaseValueForAsanaAndNotCompulsory(assignAsanaId,
												participantTeam.getAsanaCategory().getId(),
												participantTeam.getGender().toString(),
												participantTeam.getCategory().getId());

								ParticipantTeamAsanas newParticipantTeamAsanas = new ParticipantTeamAsanas();
								newParticipantTeamAsanas.setParticipantTeam(participantTeam);
								newParticipantTeamAsanas.setAsana(asana);
								newParticipantTeamAsanas.setRoundNumber(round);
								newParticipantTeamAsanas.setBaseValue(asanaExecScore.getWeightage());
								newParticipantTeamAsanas.setParticipantTeamParticipants(participantTeamParticipant);
								newParticipantTeamAsanas.setAsanaCode(asanaExecScore.getCode());
								newParticipantTeamAsanas.setSubGroup(asanaExecScore.getSubGroup());
								// newParticipantTeamAsanas.setSequenceNumber(seqNum);
								participantTeamAsanasService.save(newParticipantTeamAsanas);
								message = "Asanas added successfully";

							} catch (AsanaNotFoundException e) {
								LOGGER.error("Asana not found!");
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}

				}
			}
			ra.addFlashAttribute("message", message);
			LOGGER.info("Redirected to manage-team/edit page");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		} else {
			ra.addFlashAttribute("message", "Unable to add asanas to this team");
			LOGGER.info("Exit assignTeamAsanas method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		}
	}

	/**
	 * @param participantTeam_id
	 * @param round_type
	 * @param model
	 * @param ra
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/participant-asanas/load/{participantTeam_id}/{participantTeamParticipant_id}/round/{round_type}")
	public String loadRoundAsanasForParticipant(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("participantTeamParticipant_id") Integer participantTeamParticipant_id,
			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		LOGGER.info("Entered loadRoundAsanasForParticipant method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);

		try {
			ParticipantTeam participantTeam = service.get(participantTeam_id);
			model.addAttribute("participantTeam", participantTeam);

			List<Asana> listAllAsanas = asanaService.listAllAsanas();
			model.addAttribute("listAllAsanas", listAllAsanas);

			model.addAttribute("round", round_type);

			ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
					.get(participantTeamParticipant_id);

			List<Asana> listNonSelectedOptionalAsanas = asanaService
					.listAllNonSelectedOptionalAsanasForRoundAndParticipant(participantTeam, round_type,
							participantTeamParticipant_id);

			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
					.listExecutionScoreByAsanaCategoryAndGender(participantTeam.getAsanaCategory(),
							participantTeam.getGender());
			List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();

			for (Asana asana : listNonSelectedOptionalAsanas) {
				Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
						.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
				if (listAsanaCode.isPresent()) {
					listAsanasWithCode.add(
							new AsanaCodeDTO(asana, listAsanaCode.get().getCode(), listAsanaCode.get().getSubGroup()));
				} else {
					listAsanasWithCode.add(new AsanaCodeDTO(asana, "------", null));
				}

			}

			Collections.sort(listAsanasWithCode, new Comparator<AsanaCodeDTO>() {
				@Override
				public int compare(AsanaCodeDTO a1, AsanaCodeDTO a2) {
					return a1.getCode().compareTo(a2.getCode());
				}
			});

			model.addAttribute("listOptionalAsanas", listAsanasWithCode);

			// model.addAttribute("listOptionalAsanas", listNonSelectedOptionalAsanas);
			LOGGER.info("listNonSelectedOptionalAsanas size: " + listNonSelectedOptionalAsanas.size());
			LOGGER.info("listAsanasWithCode size: " + listAsanasWithCode.size());

			// get the count of compulsory asanas for each round
			List<CompulsoryRoundAsanas> listRoundCompulsoryAsanas = compulsoryRoundAsanasService
					.getCompulsoryAsanas(participantTeam, round_type);
			LOGGER.info("listRoundCompulsoryAsanas size: " + listRoundCompulsoryAsanas.size());

			List<ParticipantTeamAsanas> listParticipantAssignedAsanas = participantTeamAsanasService
					.getAssignedAsanasForParticipant(participantTeam, participantTeamParticipant, round_type);

			Integer countOptionalAssignedAsanas = listParticipantAssignedAsanas.size();

			Integer roundCompulsoryAsanasCount = listRoundCompulsoryAsanas.size();
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			Integer totalAsanasLimit = -1;
			Integer roundOptionalAsanasLimit = null;

			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
			} else {
				totalAsanasLimit = asanaLimit.getCommonLimit();
			}

			// roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;
			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount - countOptionalAssignedAsanas;
			model.addAttribute("participantTeamParticipant_id", participantTeamParticipant_id);
			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);

			LOGGER.info("Redirected to team_round_asanas_modal page");
			return "administration/team_participant_round_asanas_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!");
			ra.addFlashAttribute("message", e.getMessage());
			LOGGER.info("Exit loadRoundAsanasForParticipant method -ManageParticipantTeamController");

			return "redirect:/admin/manage-team/edit/" + participantTeam_id;
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-team/participant-asanas/assign/{participantTeam_id}")
	public String assignTeamParticipantRoundAsanas(
			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
			@RequestParam(value = "round", required = true) Integer round,
			@RequestParam(value = "participantTeamParticipant_id", required = true) Integer participantTeamParticipant_id,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id, RedirectAttributes ra,
			HttpServletRequest request) {
		LOGGER.info("Entered assignTeamParticipantRoundAsanas method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam = service.findById(participantTeam_id);

		// Check if asana round status is frozen
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = participantTeamParticipantAsanasStatusService
				.findByParticipantTeamAndRound(participantTeam, round);
		if (existingTeamAsanasStatus != null
				&& existingTeamAsanasStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE)) {
			LOGGER.info("Asanas are already forzen");
			ra.addFlashAttribute("message", "Unable to add asanas. Asanas are frozen.");
			LOGGER.info("Exit assignTeamAsanas method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		}
		if (service.checkTeamAsanasUpdateAllowed(participantTeam, round)) {
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			String asanaCategoryName = participantTeam.getAsanaCategoryName();

			ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
					.get(participantTeamParticipant_id);

			String message = null;
			int count = participantTeamAsanasService.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
					participantTeamParticipant, round).size();
			LOGGER.info("Count of asanas :" + count);
			LOGGER.info("Count of ParticipantTeamAsanas :" + participantTeamAsanas.length);
			LOGGER.info("Total count :" + (count + participantTeamAsanas.length));

			Integer totalAsanasLimit = -1;

			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
			} else {
				totalAsanasLimit = asanaLimit.getCommonLimit();
			}

			if ((count + participantTeamAsanas.length) > totalAsanasLimit) {
				message = count + " asanas are  added. You can add only " + (totalAsanasLimit - count)
						+ " asanas";
			} else if ((count + participantTeamAsanas.length) <= totalAsanasLimit) {

				List<Asana> listAssignedAsanas = new ArrayList<>();
				if (participantTeamAsanas != null) {
					int seqNum = count;
					for (Integer assignAsanaId : participantTeamAsanas) {
						seqNum++;
						try {
							LOGGER.info("assignAsanaId : " + assignAsanaId);
							Asana asana = asanaService.get(assignAsanaId);
							AsanaExecutionScore asanaExecScore = asanaExecutionScoreService
									.getBaseValueForAsanaAndNotCompulsory(assignAsanaId,
											participantTeam.getAsanaCategory().getId(),
											participantTeam.getGender().toString(),
											participantTeam.getCategory().getId());

							ParticipantTeamAsanas newParticipantTeamAsanas = new ParticipantTeamAsanas();
							newParticipantTeamAsanas.setParticipantTeam(participantTeam);
							newParticipantTeamAsanas.setAsana(asana);
							newParticipantTeamAsanas.setRoundNumber(round);
							newParticipantTeamAsanas.setBaseValue(asanaExecScore.getWeightage());
							newParticipantTeamAsanas.setParticipantTeamParticipants(participantTeamParticipant);
							newParticipantTeamAsanas.setAsanaCode(asanaExecScore.getCode());
							newParticipantTeamAsanas.setSubGroup(asanaExecScore.getSubGroup());
							// newParticipantTeamAsanas.setSequenceNumber(seqNum);
							participantTeamAsanasService.save(newParticipantTeamAsanas);
							message = "Asanas added successfully";

						} catch (AsanaNotFoundException e) {
							LOGGER.error("Asana not found!" + e.getMessage());

							e.printStackTrace();
						}

					}
				}
			}
			ra.addFlashAttribute("message", message);
			LOGGER.info("Redirected to manage-team/edit page");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		} else {
			ra.addFlashAttribute("message", "Unable to add asanas to this team");
			LOGGER.info("Exit assignTeamParticipantRoundAsanas method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/{participantTeam_id}";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/asanas/delete/{participantTeamAsanasId}")
	public String deleteAssignedTeamAsanas(@PathVariable(name = "participantTeamAsanasId") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamAsanasNotFoundException {
		LOGGER.info("Entered deleteAssignedTeamAsanas method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeamAsanas participantTeamAsanas = participantTeamAsanasService.get(id);

		// get asana_id, participantTeamAsanasId from participantTeamAsanas
		// check if same asana_id is assigned to another participant from the team and
		// delete the record
		Integer asanaId = participantTeamAsanas.getAsana().getId();
		Integer participantTeamId = participantTeamAsanas.getParticipantTeam().getId();

		ParticipantTeam participantTeam = participantTeamAsanas.getParticipantTeam();

		// Check if asana round status is frozen
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = participantTeamParticipantAsanasStatusService
				.findByParticipantTeamAndRound(participantTeam, participantTeamAsanas.getRoundNumber());
		if (existingTeamAsanasStatus != null
				&& existingTeamAsanasStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE)) {
			LOGGER.info("Asanas are already forzen");
			redirectAttributes.addFlashAttribute("message", "Unable to delete asanas. Asanas are frozen");
			LOGGER.info("Exit deleteAssignedTeamAsanas method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/" + participantTeamId;
		}
		if (service.checkTeamAsanasUpdateAllowed(participantTeam, participantTeamAsanas.getRoundNumber())) {
			if (participantTeam.isDifferentAsanasForParticipants() == false) { // common asanas for all participants
				List<ParticipantTeamAsanas> listCommonAssignedAsanasForParticpants = participantTeamAsanasService
						.getAllByAsanaAndTeamAndRoundNumber(participantTeamAsanas.getAsana(), participantTeam,participantTeamAsanas.getRoundNumber());
				for (ParticipantTeamAsanas teamAsanas : listCommonAssignedAsanasForParticpants) {
					participantTeamAsanasService.deleteById(teamAsanas.getId());
				}
			} else if (participantTeam.isDifferentAsanasForParticipants() == true) { // if different asanas for all
																						// participants
				participantTeamAsanasService.deleteById(id);
			}

			redirectAttributes.addFlashAttribute("message", "Asanas removed Successfully");
			return "redirect:/admin/manage-team/edit/" + participantTeamId;
		} else {
			redirectAttributes.addFlashAttribute("message", "Unable to delete asanas to this team");
			LOGGER.info("Exit deleteAssignedTeamAsanas method -ManageParticipantTeamController");
			return "redirect:/admin/manage-team/edit/" + participantTeamId;
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-team/referees/assign/{participantTeam_id}/round/{round}")
	public String loadTeamRefereePanels(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request) {
		LOGGER.info("Entered loadTeamRefereePanels method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);

		try {
			ParticipantTeam participantTeam = service.get(participantTeam_id);
			List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
					.getRefereePanelsForChampionshipAndAsanaCategoryAndAgeAndGenderAndRound(
							participantTeam.getChampionship(), participantTeam.getAsanaCategory(),
							participantTeam.getCategory(), participantTeam.getGender(), round);
			model.addAttribute("participantTeam", participantTeam);
			model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
			model.addAttribute("round", round);
			LOGGER.info("Exit loadTeamRefereePanels method -ManageParticipantTeamController");

			return "administration/team_round_referees_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!" + e.getMessage());
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/admin/manage-team/edit/" + participantTeam_id;
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-team/referees/assign/{participantTeam_id}/round/{round}")
	public String assignTeamReferees(@RequestParam(name = "panel") Integer championshipRefereePanelId,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
			@PathVariable(name = "round") Integer round, RedirectAttributes ra, HttpServletRequest request)
			throws EntityNotFoundException {
		LOGGER.info("Entered assignTeamReferees method -ManageParticipantTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam = service.findById(participantTeam_id);
		ParticipantTeamPanel participantTeamPanel = participantTeamPanelService
				.findByParticipantTeamAndRound(participantTeam_id, round);
		if (participantTeamPanel != null) {
			ChampionshipParticipantTeams championshipParticipantTeams = championshipParticipantTeamsService
					.findByParticipantTeamAndRound(participantTeam, round);
			if (championshipParticipantTeams != null) {
				StatusEnum championshipParticipantTeamsStatus = championshipParticipantTeams.getStatus();
				if (!championshipParticipantTeamsStatus.equals(StatusEnum.SCHEDULED)) {
					// return with error msg - cannot change the panel
					ra.addFlashAttribute("errorMessage", "Unable to change the panel. Judges are already logged in");
					LOGGER.info("Redirected to manage-team/edit page");
					return "redirect:/admin/manage-team/edit/" + participantTeam_id;
				}
			}

			participantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
			participantTeamPanelService.save(participantTeamPanel);
			ra.addFlashAttribute("message", "Judges Panels are added successfully");

		} else {
			ParticipantTeamPanel newParticipantTeamPanel = new ParticipantTeamPanel();
			newParticipantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
			newParticipantTeamPanel.setParticipantTeamId(participantTeam_id);
			newParticipantTeamPanel.setRoundNumber(round);
			participantTeamPanelService.save(newParticipantTeamPanel);
		}

		participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam, round);
		LOGGER.info("deleteTeamReferresForParticipantTeam Successfully deleted  ManageParticipantTeamController");
		Championship championship = participantTeam.getChampionship();
		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				championship.getId(), participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
				participantTeam.getGender().toString());
		List<RefereesPanels> listRefereesPanels = new ArrayList<>();
		try {
			listRefereesPanels = championshipRefereePanelsService.get(championshipRefereePanelId).getRefereesPanels();
		} catch (ChampionshipRefereePanelsNotFoundException e) {

			e.printStackTrace();
		}
//	List<JudgeType> listAssignedUserReferees = new ArrayList<JudgeType>();
//		for (RefereesPanels referee : listRefereesPanels) {
//			listAssignedUserReferees.add(referee.getType());
//		}
//		LOGGER.info("listAssignedUserReferees" + listAssignedUserReferees.toArray());
		// getRefereesForParticipantTeam(listAssignedReferees, participantTeam);
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

			LOGGER.info("participantTeamReferees added successfully");
		}
		// service.assignRefereesForParticipantTeam(participantTeam);
		LOGGER.info("Exit assignTeamReferees method -ManageParticipantTeamController");
		return "redirect:/admin/manage-team/edit/{participantTeam_id}";

	}

//	@PostMapping("/manage-team/referees-panel/assign/{participantTeam_id}")
//	public String assignTeamRefereesPanel(@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
//			@RequestParam(value = "panel", required = true) Integer panel_id) {
//		LOGGER.info("Entered assignTeamRefereesPanel controller");
//		ParticipantTeam participantTeam = service.findById(participantTeam_id);
//		ChampionshipRefereePanels panel;
//		List<RefereesPanels> listReferees = new ArrayList<>();
//		try {
//			panel = championshipRefereePanelsService.get(panel_id);
//			listReferees = refereesPanelsService.getByChampionshipRefereePanels(panel);
//			for (RefereesPanels refereesPanel : listReferees) {
//				ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();
//				participantTeamReferees.setParticipantTeam(participantTeam);
//				participantTeamReferees.setUser(refereesPanel.getUser());
//				participantTeamRefereesService.save(participantTeamReferees);
//			}
//
//		} catch (ChampionshipRefereePanelsNotFoundException e) {
//			LOGGER.error("ChampionshipRefereePanels not found!");
//			e.printStackTrace();
//		}
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/admin/manage-team/edit/{participantTeam_id}";
//
//	}
//

}
