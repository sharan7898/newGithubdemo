//package com.swayaan.nysf.controller;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.swayaan.nysf.entity.Asana;
//import com.swayaan.nysf.entity.AsanaCategory;
//import com.swayaan.nysf.entity.AsanaExecutionScore;
//import com.swayaan.nysf.entity.AsanaLimit;
//import com.swayaan.nysf.entity.Championship;
//import com.swayaan.nysf.entity.ChampionshipCategory;
//import com.swayaan.nysf.entity.ChampionshipRefereePanels;
//import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
//import com.swayaan.nysf.entity.Judge;
//import com.swayaan.nysf.entity.Participant;
//import com.swayaan.nysf.entity.ParticipantTeam;
//import com.swayaan.nysf.entity.ParticipantTeamAsanas;
//import com.swayaan.nysf.entity.ParticipantTeamParticipants;
//import com.swayaan.nysf.entity.ParticipantTeamReferees;
//import com.swayaan.nysf.entity.RefereesPanels;
//import com.swayaan.nysf.entity.RegistrationStatusEnum;
//import com.swayaan.nysf.entity.StatusEnum;
//import com.swayaan.nysf.entity.User;
//import com.swayaan.nysf.entity.DTO.AsanaCodeDTO;
//import com.swayaan.nysf.exception.AsanaNotFoundException;
//import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
//import com.swayaan.nysf.exception.EntityNotFoundException;
//import com.swayaan.nysf.exception.JudgeNotFoundException;
//import com.swayaan.nysf.exception.ParticipantNotFoundException;
//import com.swayaan.nysf.exception.ParticipantTeamAsanasNotFoundException;
//import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
//import com.swayaan.nysf.exception.ParticipantTeamParticipantsNotFoundException;
//import com.swayaan.nysf.service.AsanaCategoryService;
//import com.swayaan.nysf.service.AsanaExecutionScoreService;
//import com.swayaan.nysf.service.AsanaService;
//import com.swayaan.nysf.service.ChampionshipCategoryService;
//import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
//import com.swayaan.nysf.service.ChampionshipRoundsService;
//import com.swayaan.nysf.service.ChampionshipService;
//import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
//import com.swayaan.nysf.service.JudgeTypeService;
//import com.swayaan.nysf.service.ParticipantService;
//import com.swayaan.nysf.service.ParticipantTeamAsanasService;
//import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
//import com.swayaan.nysf.service.ParticipantTeamRefereesService;
//import com.swayaan.nysf.service.ParticipantTeamService;
//import com.swayaan.nysf.service.RefereesPanelsService;
//import com.swayaan.nysf.service.RoleService;
//import com.swayaan.nysf.service.UserService;
//import com.swayaan.nysf.utils.CommonUtil;
//
//@Controller
//@RequestMapping("/referee")
//public class ManageRefereeParticipantTeamController {
//
//	@Autowired
//	ParticipantTeamService service;
//	@Autowired
//	AsanaCategoryService asanaCategoryService;
//	@Autowired
//	ChampionshipService championshipService;
//	@Autowired
//	ChampionshipCategoryService championshipCategoryService;
//	@Autowired
//	ParticipantService participantService;
//	@Autowired
//	AsanaService asanaService;
//	@Autowired
//	UserService userService;
//	@Autowired
//	ParticipantTeamRefereesService participantTeamRefereesService;
//	@Autowired
//	ParticipantTeamAsanasService participantTeamAsanasService;
//
//	@Autowired
//	ChampionshipRefereePanelsService championshipRefereePanelsService;
//	@Autowired
//	RefereesPanelsService refereesPanelsService;
//	@Autowired
//	ParticipantTeamParticipantsService participantTeamParticipantsService;
////	@Autowired
////	ChampionshipParticipantsService championshipParticipantService;
//	@Autowired
//	CompulsoryRoundAsanasService compulsoryRoundAsanasService;
//
//	@Autowired
//	RoleService roleService;
//
//	@Autowired
//	AsanaLimit asanaLimit;
//	@Autowired
//	JudgeTypeService judgeTypeService;
//
////	@Autowired
////	ParticipantTeamAsanasService participantTeamAsanasService;
//	@Autowired
//	ChampionshipRoundsService championshipRoundsService;
//	@Autowired
//	AsanaExecutionScoreService asanaExecutionScoreService;
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ManageRefereeParticipantTeamController.class);
//
//	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
//	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
//	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
//	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
//	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;
//
//	@GetMapping("/manage-team")
//	public String listAllParticipantTeams(Model model, HttpServletRequest request) {
//		LOGGER.info("Entered listAllParticipantTeams controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		try {
//			Judge judgeUser = CommonUtil.getCurrentJudge();
//			LOGGER.info(
//					"currentUser = " + currentUser.getFullName() + " and " + "email id = " + currentUser.getEmail());
//			List<ParticipantTeam> listParticipantTeams = service.listAllParticipantTeams();
//			model.addAttribute("listParticipantTeams", listParticipantTeams);
//			model.addAttribute("pageTitle", "Manage Teams");
//			LOGGER.info("Redirected to manage_team page");
//			return "referee/manage_team";
//		} catch (JudgeNotFoundException e) {
//			LOGGER.info("Judge Not Found" + e.getMessage());
//			e.printStackTrace();
//			return "redirect:/referee/manage-team";
//
//		}
//
//	}
//
//	@GetMapping("/manage-team/new")
//	public String newParticipantTeam(Model model, HttpServletRequest request) {
//		LOGGER.info("Entered newParticipantTeam controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		ParticipantTeam participantTeam = new ParticipantTeam();
//		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
//
//		model.addAttribute("participantTeam", participantTeam);
//		model.addAttribute("pageTitle", "Add Team");
//		model.addAttribute("listChampionships", listChampionships);
//		LOGGER.info("Redirected to team_form page");
//		return "referee/team_form";
//	}
//
//	@PostMapping("/manage-team/save")
//	public String saveParticipantTeam(ParticipantTeam participantTeam, RedirectAttributes redirectAttributes,
//			HttpServletRequest request) throws IOException {
//		LOGGER.info("Entered saveParticipantTeam controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		participantTeam.setStatus(RegistrationStatusEnum.APPROVED);
//		Boolean teamChestNumber = service.checkTeamChestNumber(participantTeam.getChestNumber(),
//				participantTeam.getChampionship());
//		boolean isUpdatingTeam = (participantTeam.getId() != null);
//
//		if (participantTeam.getId() != null) {
//			ParticipantTeam existingParticipantTeam = service.findById(participantTeam.getId());
//			boolean existingStatus = existingParticipantTeam.isDifferentAsanasForParticipants();
//
//			List<ParticipantTeamParticipants> listAssignedParticipants = existingParticipantTeam
//					.getParticipantTeamParticipants();
//			if (listAssignedParticipants.size() != 0) {
//				if (participantTeam.isDifferentAsanasForParticipants() == existingStatus) {
//					participantTeam.setDifferentAsanasForParticipants(existingStatus);
//				} else {
//					participantTeam.setDifferentAsanasForParticipants(existingStatus);
//					redirectAttributes.addFlashAttribute("errorMessage",
//							"You cannot change the status of the field Different Asanas!");
//				}
//
//			}
//		}
//
//		if (isUpdatingTeam) {
//			try {
//				service.save(participantTeam);
//			} catch (Exception e) {
//				redirectAttributes.addFlashAttribute("message1", "Unable to edit team");
//				return "redirect:/referee/manage-team/edit/" + participantTeam.getId();
//			}
//			redirectAttributes.addFlashAttribute("message", "The Team has been updated successfully.");
//			return "redirect:/referee/manage-team/edit/" + participantTeam.getId();
//		} else {
//			if (teamChestNumber) {
//				redirectAttributes.addFlashAttribute("message1",
//						"The Team " + participantTeam.getName() + " is already present.");
//				return "redirect:/referee/manage-team";
//			} else {
//				try {
//					service.save(participantTeam);
//				} catch (Exception e) {
//					redirectAttributes.addFlashAttribute("message1", "Unable to edit team");
//					return "redirect:/referee/manage-team/edit/" + participantTeam.getId();
//				}
//			}
//
//		}
//		redirectAttributes.addFlashAttribute("message", "The Team has been saved successfully.");
//		LOGGER.info("Redirected to manage-team/edit/participantTeamID page");
//		return "redirect:/referee/manage-team/edit/" + participantTeam.getId();
//	}
//
//	@GetMapping("/manage-team/edit/{id}")
//	public String editParticipantTeam(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes redirectAttributes, HttpServletRequest request)
//			throws ParticipantTeamNotFoundException, JudgeNotFoundException {
//		LOGGER.info("Entered editParticipantTeam controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		try {
//			ParticipantTeam participantTeam = service.get(id);
//			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
//			List<Participant> listParticipants = participantService
//					.listAllParticipantsForGender(participantTeam.getGender().toString());
//
//			// List<ParticipantTeamParticipants> listAssignedParticipants =
//			// participantTeam.getParticipantTeamParticipants();
//			List<ParticipantTeamParticipants> listAssignedParticipants = participantTeamParticipantsService
//					.getByTeamOrderBySequenceNumberAsc(participantTeam);
//
//			LOGGER.info("listAssignedParticipants :" + listAssignedParticipants.size());
//
//			List<Participant> listNonAssignedParticipants = participantService
//					.listAllParticipantsForGender(participantTeam);
//			// LOGGER.info("listAssignedParticipants :" + listAssignedParticipants.size());
//			LOGGER.info("listNonAssignedParticipants :" + listNonAssignedParticipants.size());
//
//			List<ParticipantTeamReferees> listAssignedReferees = participantTeam.getParticipantTeamReferees();
//
//			Integer noOfRounds = championshipCategoryService.getChampionshipCategoryRoundsForChampionshipId(
//					participantTeam.getChampionship(), participantTeam.getAsanaCategory(),
//					participantTeam.getCategory(), participantTeam.getGender());
//			LOGGER.info("noOfRounds :" + noOfRounds);
//
//			Integer participantsLimit = championshipCategoryService
//					.getChampionshipCategoryParticipantsForChampionshipId(participantTeam.getChampionship(),
//							participantTeam.getAsanaCategory(), participantTeam.getCategory(),
//							participantTeam.getGender());
//			LOGGER.info("noOfParticipants :" + participantsLimit);
//
//			List<ParticipantTeamAsanas> listAsanas = new ArrayList<ParticipantTeamAsanas>();
//			List<List<ParticipantTeamAsanas>> listRoundWiseParticipantTeamAsanas = new ArrayList<>();
//			for (Integer round = 1; round <= noOfRounds; round++) {
////				if((participantTeam.getAsanaCategory().getId() == 1) || (participantTeam.getAsanaCategory().getId() == 2) ) {
////					listAsanas = participantTeamAsanasService.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
////				} else if((participantTeam.getAsanaCategory().getId() == 3) && (participantTeam.isDifferentAsanasForParticipants() == false)) {
////					listAsanas = participantTeamAsanasService.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
////				} else {
////					listAsanas = participantTeamAsanasService.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
////				}
//				listAsanas = participantTeamAsanasService
//						.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
//				listRoundWiseParticipantTeamAsanas.add(listAsanas);
//			}
//
//			// System.out.println(listRoundWiseParticipantTeamAsanas);
//			List<ParticipantTeamReferees> listReferees = new ArrayList<>();
//			List<List<ParticipantTeamReferees>> listRoundWiseParticipantTeamReferees = new ArrayList<>();
//			for (Integer round = 1; round <= noOfRounds; round++) {
//				listReferees = participantTeamRefereesService.getByTeamAndRound(participantTeam, round);
//
//				listRoundWiseParticipantTeamReferees.add(listReferees);
//			}
//
//			// Mapping of each round asanas to the participant
//			LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<Integer, List<ParticipantTeamAsanas>>> mapParticipantsWithRoundAsanas = new LinkedHashMap<>();
//			List<ParticipantTeamParticipants> listParticipantsForTeam = participantTeamParticipantsService
//					.getByTeamOrderBySequenceNumberAsc(participantTeam);
//			for (ParticipantTeamParticipants participantTeamParticipant : listParticipantsForTeam) {
//				LinkedHashMap<Integer, List<ParticipantTeamAsanas>> mapRoundAsanas = new LinkedHashMap<>();
//				for (Integer round = 1; round <= noOfRounds; round++) {
//					List<ParticipantTeamAsanas> listParticipantRoundAsanas = new ArrayList<ParticipantTeamAsanas>();
//					listParticipantRoundAsanas = participantTeamAsanasService
//							.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
//									participantTeamParticipant, round);
//					mapRoundAsanas.put(round, listParticipantRoundAsanas);
//				}
//				if (mapRoundAsanas.size() != 0) {
//					mapParticipantsWithRoundAsanas.put(participantTeamParticipant, mapRoundAsanas);
//				}
//			}
//
//			model.addAttribute("mapParticipantsWithRoundAsanas", mapParticipantsWithRoundAsanas);
//
//			List<Asana> listAllAsanas = asanaService.listAllAsanas();
//			model.addAttribute("participantTeam", participantTeam);
//			model.addAttribute("pageTitle", "Edit Team");
//			model.addAttribute("listChampionships", listChampionships);
//			model.addAttribute("listAllAsanas", listAllAsanas);
//			model.addAttribute("listAssignedParticipants", listAssignedParticipants);
//			model.addAttribute("listAssignedReferees", listAssignedReferees);
//			model.addAttribute("listNonAssignedParticipants", listNonAssignedParticipants);
//			model.addAttribute("listRoundWiseParticipantTeamAsanas", listRoundWiseParticipantTeamAsanas);
//			model.addAttribute("listRoundWiseParticipantTeamReferees", listRoundWiseParticipantTeamReferees);
//			model.addAttribute("noOfRounds", noOfRounds);
//
//			Integer limit = -1;
//			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//			if (asanaCategory.getId() == 1) {
//				limit = asanaLimit.getTraditionalSingleLimit();
//			} else if (asanaCategory.getId() == 2) {
//				limit = asanaLimit.getArtisticSingleLimit();
//			} else if (asanaCategory.getId() == 3) {
//				limit = asanaLimit.getArtisticPairLimit();
//			} else if (asanaCategory.getId() == 4) {
//				limit = asanaLimit.getRhythmicPairLimit();
//			} else if (asanaCategory.getId() == 5) {
//				limit = asanaLimit.getArtisticGroupLimit();
//			} else {
//				limit = asanaLimit.getCommonLimit();
//			}
//
//			model.addAttribute("participantsLimit", participantsLimit);
//			model.addAttribute("asanasLimit", limit);
//			LOGGER.info("Redirected to team_form page");
//			return "referee/team_form";
//		} catch (ParticipantTeamNotFoundException ex) {
//			LOGGER.error("ParticipantTeam not found!");
//			redirectAttributes.addFlashAttribute("message", "Team not found.");
//			return "redirect:/referee/manage-team";
//		}
//	}
//
//	@GetMapping("/manage-team/delete/{id}")
//	public String deleteParticipantTeam(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes redirectAttributes, HttpServletRequest request) {
//		LOGGER.info("Entered deleteParticipantTeam controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		try {
//			ParticipantTeam participantTeam = service.get(id);
//			// check for dependencies
//			/*
//			 * ParticipantTeamAsanas, ParticipantTeamParticipants,
//			 * ParticipantTeamReferees,ParticipantTeamAsanaScores
//			 * ,ParticipantTeamTotalscores,ParticipantTeamRoundTotalScore
//			 */
//
//			// List<ParticipantTeamAsanaScoring> participantTeamAsanaScore =
//			// participantTeamAsanaScoringService.getParticipantTeamAsanaScoreForParticpantTeam(participantTeam);
//			List<ParticipantTeamAsanas> participantTeamAsanas = participantTeamAsanasService
//					.getTeamAsanasForParticipantTeam(participantTeam);
//			List<ParticipantTeamParticipants> participantTeamParticipants = participantTeamParticipantsService
//					.getTeamParticipantsForParticipantTeam(participantTeam);
//			List<ParticipantTeamReferees> participantTeamReferees = participantTeamRefereesService
//					.getTeamReferresForParticipantTeam(participantTeam);
//
////			 if(!participantTeamAsanaScore.isEmpty()) {
////					System.out.println("ParticipantTeamAsanaScore Team");
////					redirectAttributes.addFlashAttribute("message1", 
////							"The Team " + participantTeam.getTeamName() + " is allocated to ParticipantTeamAsanaScore, Please remove before you delete");
////					return "redirect:/admin/manage-team";
////				}
//
//			if (!participantTeamAsanas.isEmpty()) {
//				System.out.println("participantTeamAsanas Team");
//				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
//						+ " is allocated to participant team asanas, Please remove before you delete");
//				return "redirect:/referee/manage-team";
//			}
//
//			if (!participantTeamParticipants.isEmpty()) {
//				System.out.println("participantTeamParticipants Team");
//				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
//						+ " is allocated to team participants, Please remove before you delete");
//				return "redirect:/referee/manage-team";
//			}
//
//			if (!participantTeamReferees.isEmpty()) {
//				System.out.println("participantTeamReferees Team");
//				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
//						+ " is alocated to team referees, Please remove before you delete");
//				return "redirect:/referee/manage-team";
//			}
//
//			service.delete(id);
//			service.updateNumberOfParticipantTeams(service.getChampionshipRoundId(participantTeam));
//			redirectAttributes.addFlashAttribute("message",
//					"The Team " + participantTeam.getName() + " has been deleted successfully");
//
//		} catch (ParticipantTeamNotFoundException ex) {
//			LOGGER.error("ParticipantTeam not found!");
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//		}
//		LOGGER.info("Redirected to manage-team page");
//		return "redirect:/referee/manage-team";
//	}
//
//	// Add participants to team
//	@PostMapping("/manage-team/participants/assign/{participantTeam_id}")
//	public String assignTeamParticipants(@RequestParam(value = "participants", required = false) Integer[] participants,
//			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
//			RedirectAttributes redirectAttributes, HttpServletRequest request) {
//		LOGGER.info("Entered assignTeamParticipants controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		ParticipantTeam participantTeam = service.findById(participantTeam_id);
//		AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//		int count = participantTeam.getParticipantTeamParticipants().size();
//
//		Integer participantsLimit = championshipCategoryService.getChampionshipCategoryParticipantsForChampionshipId(
//				participantTeam.getChampionship(), participantTeam.getAsanaCategory(), participantTeam.getCategory(),
//				participantTeam.getGender());
//		LOGGER.info("noOfParticipants :" + participantsLimit);
//
//		String errorMessage = null;
//		String message = null;
//		if ((count + participants.length) > participantsLimit) {
//			errorMessage = "Only " + participantsLimit + " participant can be added for this category";
//		} else if ((count + participants.length) <= participantsLimit) {
//			message = addParticipantsToTeam(participants, participantTeam);
//		}
//
//		redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//		redirectAttributes.addFlashAttribute("message", message);
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/referee/manage-team/edit/{participantTeam_id}";
//
//	}
//
//	@GetMapping("/manage-team/participants/delete/{participantTeamParticipantsId}")
//	public String deleteAssignedTeamParticipants(@PathVariable(name = "participantTeamParticipantsId") Integer id,
//			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
//			throws ParticipantTeamNotFoundException, ParticipantTeamParticipantsNotFoundException,
//			ParticipantTeamAsanasNotFoundException {
//		LOGGER.info("Entered deleteAssignedTeamParticipants controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		ParticipantTeamParticipants participantTeamParticipants = participantTeamParticipantsService.get(id);
//		ParticipantTeam participantTeam = participantTeamParticipants.getParticipantTeam();
//
//		Integer participantTeamId = participantTeam.getId();
//
//		// delete participantTeamAsanas for the given participantTeamParticipantsId
//		List<ParticipantTeamAsanas> listAssignedAsanas = participantTeamAsanasService
//				.getByTeamAndParticipantTeamParticipants(participantTeam, participantTeamParticipants);
//		for (ParticipantTeamAsanas teamAsanas : listAssignedAsanas) {
//			participantTeamAsanasService.deleteById(teamAsanas.getId());
//		}
//
//		participantTeamParticipantsService.deleteById(id);
//
//		redirectAttributes.addFlashAttribute("message", "Participant removed Successfully");
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/referee/manage-team/edit/" + participantTeamId;
//
//	}
//
//	/**
//	 * @param participantTeam_id
//	 * @param round_type
//	 * @param model
//	 * @param ra
//	 * @return
//	 */
//	@GetMapping("/manage-team/asanas/load/{participantTeam_id}/round/{round_type}")
//	public String loadTeamRoundAsanas(@PathVariable("participantTeam_id") Integer participantTeam_id,
//			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra,
//			HttpServletRequest request) {
//		LOGGER.info("Entered loadTeamRoundAsanas controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		model.addAttribute("participantTeam_id", participantTeam_id);
//
//		try {
//			ParticipantTeam participantTeam = service.get(participantTeam_id);
//			model.addAttribute("participantTeam", participantTeam);
//
//			List<Asana> listAllAsanas = asanaService.listAllAsanas();
//			model.addAttribute("listAllAsanas", listAllAsanas);
//			model.addAttribute("round", round_type);
//
//			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
//					.listExecutionScoreByAsanaCategoryAndGender(participantTeam.getAsanaCategory(),
//							participantTeam.getGender());
//
//			List<Asana> listNonSelectedOptionalAsanas = asanaService
//					.listAllNonSelectedOptionalAsanasForRound(participantTeam, round_type);
//
//			List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();
//
//			for (Asana asana : listNonSelectedOptionalAsanas) {
//				Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
//						.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
//				if (listAsanaCode.isPresent()) {
//					listAsanasWithCode.add(
//							new AsanaCodeDTO(asana, listAsanaCode.get().getCode(), listAsanaCode.get().getSubGroup()));
//				} else {
//					listAsanasWithCode.add(new AsanaCodeDTO(asana, "------", null));
//				}
//
//			}
//
//			Collections.sort(listAsanasWithCode, new Comparator<AsanaCodeDTO>() {
//				@Override
//				public int compare(AsanaCodeDTO a1, AsanaCodeDTO a2) {
//					return a1.getCode().compareTo(a2.getCode());
//				}
//			});
//
//			model.addAttribute("listOptionalAsanas", listAsanasWithCode);
//			LOGGER.info("listNonSelectedOptionalAsanas size: " + listNonSelectedOptionalAsanas.size());
//			LOGGER.info("listAsanasWithCode size: " + listAsanasWithCode.size());
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
//			return "referee/team_round_asanas_modal";
//
//		} catch (ParticipantTeamNotFoundException e) {
//			LOGGER.error("ParticipantTeam not found!");
//			ra.addFlashAttribute("message", e.getMessage());
//
//			return "redirect:/referee/manage-team/edit/" + participantTeam_id;
//		}
//
//	}
//
//	@PostMapping("/manage-team/asanas/assign/{participantTeam_id}")
//	public String assignTeamAsanas(
//			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
//			@RequestParam(value = "round", required = true) Integer round, HttpServletRequest request,
//			@PathVariable(name = "participantTeam_id") Integer participantTeam_id, RedirectAttributes ra) {
//		LOGGER.info("Entered assignTeamAsanas controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		// function when common asanas are assigned to the team participants.
//		ParticipantTeam participantTeam = service.findById(participantTeam_id);
//		AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//		String asanaCategoryName = participantTeam.getAsanaCategoryName();
//
//		String message = null;
//		// int count =
//		// participantTeamAsanasService.getByTeamAndRoundOrderBySequenceNum(participantTeam,
//		// round).size();
//		int count = participantTeamAsanasService
//				.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round).size();
//		LOGGER.info("Count of asanas :" + count);
//		LOGGER.info("Count of ParticipantTeamAsanas :" + participantTeamAsanas.length);
//		LOGGER.info("Total count :" + (count + participantTeamAsanas.length));
//
//		Integer totalAsanasLimit = -1;
//
//		if (asanaCategory.getId() == 1) {
//			totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
//		} else if (asanaCategory.getId() == 2) {
//			totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
//		} else if (asanaCategory.getId() == 3) {
//			totalAsanasLimit = asanaLimit.getArtisticPairLimit();
//		} else if (asanaCategory.getId() == 4) {
//			totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
//		} else if (asanaCategory.getId() == 5) {
//			totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
//		} else {
//			totalAsanasLimit = asanaLimit.getCommonLimit();
//		}
//
//		if ((count + participantTeamAsanas.length) > totalAsanasLimit) {
//			message = count + " asanas are already added. You can add only " + (totalAsanasLimit - count) + " asanas";
//		} else if ((count + participantTeamAsanas.length) <= totalAsanasLimit) {
//
//			List<Asana> listAssignedAsanas = new ArrayList<>();
//			if (participantTeamAsanas != null) {
//
//				List<ParticipantTeamParticipants> listAssignedParticipants = participantTeam
//						.getParticipantTeamParticipants();
//				for (ParticipantTeamParticipants participantTeamParticipant : listAssignedParticipants) {
//
//					for (Integer assignAsanaId : participantTeamAsanas) {
//						try {
//							LOGGER.info("assignAsanaId : " + assignAsanaId);
//							Asana asana = asanaService.get(assignAsanaId);
//							AsanaExecutionScore asanaExecScore = asanaExecutionScoreService
//									.getBaseValueForAsanaAndNotCompulsory(assignAsanaId,
//											participantTeam.getAsanaCategory().getId(),
//											participantTeam.getGender().toString(),
//											participantTeam.getCategory().getId());
//
//							ParticipantTeamAsanas newParticipantTeamAsanas = new ParticipantTeamAsanas();
//							newParticipantTeamAsanas.setParticipantTeam(participantTeam);
//							newParticipantTeamAsanas.setAsana(asana);
//							newParticipantTeamAsanas.setRoundNumber(round);
//							newParticipantTeamAsanas.setBaseValue(asanaExecScore.getWeightage());
//							newParticipantTeamAsanas.setParticipantTeamParticipants(participantTeamParticipant);
//							newParticipantTeamAsanas.setAsanaCode(asanaExecScore.getCode());
//							participantTeamAsanasService.save(newParticipantTeamAsanas);
//							message = "Asanas added successfully";
//
//						} catch (AsanaNotFoundException e) {
//							LOGGER.error("Asana not found!");
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//					}
//
//				}
//
//			}
//		}
//		ra.addFlashAttribute("message", message);
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/referee/manage-team/edit/{participantTeam_id}";
//	}
//
//	/**
//	 * @param participantTeam_id
//	 * @param round_type
//	 * @param model
//	 * @param ra
//	 * @return
//	 */
//	@GetMapping("/manage-team/participant-asanas/load/{participantTeam_id}/{participantTeamParticipant_id}/round/{round_type}")
//	public String loadRoundAsanasForParticipant(@PathVariable("participantTeam_id") Integer participantTeam_id,
//			@PathVariable("participantTeamParticipant_id") Integer participantTeamParticipant_id,
//			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra,
//			HttpServletRequest request) {
//		LOGGER.info("Entered loadTeamRoundAsanas controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
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
//			ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
//					.get(participantTeamParticipant_id);
//
//			List<Asana> listNonSelectedOptionalAsanas = asanaService
//					.listAllNonSelectedOptionalAsanasForRoundAndParticipant(participantTeam, round_type,
//							participantTeamParticipant_id);
//
//			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
//					.listExecutionScoreByAsanaCategoryAndGender(participantTeam.getAsanaCategory(),
//							participantTeam.getGender());
//			List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();
//
//			for (Asana asana : listNonSelectedOptionalAsanas) {
//				Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
//						.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
//				if (listAsanaCode.isPresent()) {
//					listAsanasWithCode.add(
//							new AsanaCodeDTO(asana, listAsanaCode.get().getCode(), listAsanaCode.get().getSubGroup()));
//				} else {
//					listAsanasWithCode.add(new AsanaCodeDTO(asana, "------", null));
//				}
//
//			}
//
//			Collections.sort(listAsanasWithCode, new Comparator<AsanaCodeDTO>() {
//				@Override
//				public int compare(AsanaCodeDTO a1, AsanaCodeDTO a2) {
//					return a1.getCode().compareTo(a2.getCode());
//				}
//			});
//
//			model.addAttribute("listOptionalAsanas", listAsanasWithCode);
//
//			// model.addAttribute("listOptionalAsanas", listNonSelectedOptionalAsanas);
//			LOGGER.info("listNonSelectedOptionalAsanas size: " + listNonSelectedOptionalAsanas.size());
//			LOGGER.info("listAsanasWithCode size: " + listAsanasWithCode.size());
//
//			// get the count of compulsory asanas for each round
//			List<CompulsoryRoundAsanas> listRoundCompulsoryAsanas = compulsoryRoundAsanasService
//					.getCompulsoryAsanas(participantTeam, round_type);
//			LOGGER.info("listRoundCompulsoryAsanas size: " + listRoundCompulsoryAsanas.size());
//
//			List<ParticipantTeamAsanas> listParticipantAssignedAsanas = participantTeamAsanasService
//					.getAssignedAsanasForParticipant(participantTeam, participantTeamParticipant, round_type);
//
//			Integer countOptionalAssignedAsanas = listParticipantAssignedAsanas.size();
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
//			// roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;
//			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount - countOptionalAssignedAsanas;
//			model.addAttribute("participantTeamParticipant_id", participantTeamParticipant_id);
//			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
//			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);
//
//			LOGGER.info("Redirected to team_round_asanas_modal page");
//			return "referee/team_participant_round_asanas_modal";
//
//		} catch (ParticipantTeamNotFoundException e) {
//			LOGGER.error("ParticipantTeam not found!");
//			ra.addFlashAttribute("message", e.getMessage());
//
//			return "redirect:/admin/manage-team/edit/" + participantTeam_id;
//		}
//	}
//
//	@PostMapping("/manage-team/participant-asanas/assign/{participantTeam_id}")
//	public String assignTeamParticipantRoundAsanas(
//			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
//			@RequestParam(value = "round", required = true) Integer round,
//			@RequestParam(value = "participantTeamParticipant_id", required = true) Integer participantTeamParticipant_id,
//			@PathVariable(name = "participantTeam_id") Integer participantTeam_id, RedirectAttributes ra,
//			HttpServletRequest request) {
//		LOGGER.info("Entered assignTeamAsanas controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		ParticipantTeam participantTeam = service.findById(participantTeam_id);
//		AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//		String asanaCategoryName = participantTeam.getAsanaCategoryName();
//
//		ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
//				.get(participantTeamParticipant_id);
//
//		String message = null;
//		int count = participantTeamAsanasService
//				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant, round)
//				.size();
//		LOGGER.info("Count of asanas :" + count);
//		LOGGER.info("Count of ParticipantTeamAsanas :" + participantTeamAsanas.length);
//		LOGGER.info("Total count :" + (count + participantTeamAsanas.length));
//
//		Integer totalAsanasLimit = -1;
//
//		if (asanaCategory.getId() == 1) {
//			totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
//		} else if (asanaCategory.getId() == 2) {
//			totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
//		} else if (asanaCategory.getId() == 3) {
//			totalAsanasLimit = asanaLimit.getArtisticPairLimit();
//		} else if (asanaCategory.getId() == 4) {
//			totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
//		} else if (asanaCategory.getId() == 5) {
//			totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
//		} else {
//			totalAsanasLimit = asanaLimit.getCommonLimit();
//		}
//
//		if ((count + participantTeamAsanas.length) > totalAsanasLimit) {
//			message = count + " asanas are already added. You can add only " + (totalAsanasLimit - count) + " asanas";
//		} else if ((count + participantTeamAsanas.length) <= totalAsanasLimit) {
//
//			List<Asana> listAssignedAsanas = new ArrayList<>();
//			if (participantTeamAsanas != null) {
//				for (Integer assignAsanaId : participantTeamAsanas) {
//					try {
//						LOGGER.info("assignAsanaId : " + assignAsanaId);
//						Asana asana = asanaService.get(assignAsanaId);
//						AsanaExecutionScore asanaExecScore = asanaExecutionScoreService
//								.getBaseValueForAsanaAndNotCompulsory(assignAsanaId,
//										participantTeam.getAsanaCategory().getId(),
//										participantTeam.getGender().toString(), participantTeam.getCategory().getId());
//
//						ParticipantTeamAsanas newParticipantTeamAsanas = new ParticipantTeamAsanas();
//						newParticipantTeamAsanas.setParticipantTeam(participantTeam);
//						newParticipantTeamAsanas.setAsana(asana);
//						newParticipantTeamAsanas.setRoundNumber(round);
//						newParticipantTeamAsanas.setBaseValue(asanaExecScore.getWeightage());
//						newParticipantTeamAsanas.setParticipantTeamParticipants(participantTeamParticipant);
//						newParticipantTeamAsanas.setAsanaCode(asanaExecScore.getCode());
//						participantTeamAsanasService.save(newParticipantTeamAsanas);
//						message = "Asanas added successfully";
//
//					} catch (AsanaNotFoundException e) {
//						LOGGER.error("Asana not found!");
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}
//		ra.addFlashAttribute("message", message);
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/referee/manage-team/edit/{participantTeam_id}";
//	}
//
//	@GetMapping("/manage-team/asanas/delete/{participantTeamAsanasId}")
//	public String deleteAssignedTeamAsanas(@PathVariable(name = "participantTeamAsanasId") Integer id, Model model,
//			RedirectAttributes redirectAttributes, HttpServletRequest request)
//			throws ParticipantTeamNotFoundException, ParticipantTeamAsanasNotFoundException {
//		LOGGER.info("Entered deleteAssignedTeamAsanas controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		ParticipantTeamAsanas participantTeamAsanas = participantTeamAsanasService.get(id);
//
//		// get asana_id, participantTeamAsanasId from participantTeamAsanas
//		// check if same asana_id is assigned to another participant from the team and
//		// delete the record
//		Integer asanaId = participantTeamAsanas.getAsana().getId();
//		Integer participantTeamId = participantTeamAsanas.getParticipantTeam().getId();
//
//		ParticipantTeam participantTeam = participantTeamAsanas.getParticipantTeam();
//
//		if (participantTeam.isDifferentAsanasForParticipants() == false) { // common asanas for all participants
//			List<ParticipantTeamAsanas> listCommonAssignedAsanasForParticpants = participantTeamAsanasService
//					.getAllByAsanaAndTeam(participantTeamAsanas.getAsana(), participantTeam);
//			for (ParticipantTeamAsanas teamAsanas : listCommonAssignedAsanasForParticpants) {
//				participantTeamAsanasService.deleteById(teamAsanas.getId());
//			}
//		} else if (participantTeam.isDifferentAsanasForParticipants() == true) { // if different asanas for all
//																					// participants
//			participantTeamAsanasService.deleteById(id);
//		}
//
//		redirectAttributes.addFlashAttribute("message", "Asanas removed Successfully");
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/referee/manage-team/edit/" + participantTeamId;
//	}
//
//	@GetMapping("/manage-team/referees/assign/{participantTeam_id}/round/{round}")
//	public String loadTeamRefereePanels(@PathVariable("participantTeam_id") Integer participantTeam_id,
//			@PathVariable("round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request) {
//		LOGGER.info("Entered loadTeamRoundAsanas controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		model.addAttribute("participantTeam_id", participantTeam_id);
//
//		try {
//			ParticipantTeam participantTeam = service.get(participantTeam_id);
//			List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
//					.getRefereePanelsForChampionship(participantTeam.getChampionship(),
//							participantTeam.getAsanaCategory());
//
//			model.addAttribute("participantTeam", participantTeam);
//			model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
//			model.addAttribute("round", round);
//
//			return "referee/team_round_referees_modal";
//
//		} catch (ParticipantTeamNotFoundException e) {
//			LOGGER.error("ParticipantTeam not found!");
//			ra.addFlashAttribute("message", e.getMessage());
//
//			return "redirect:/referee/manage-team/edit/" + participantTeam_id;
//		}
//
//	}
//
//	@PostMapping("/manage-team/referees/assign/{participantTeam_id}/round/{round}")
//	public String assignTeamReferees(@RequestParam(name = "panel") Integer championshipRefereePanelId,
//			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
//			@PathVariable(name = "round") Integer round, RedirectAttributes ra, HttpServletRequest request)
//			throws EntityNotFoundException {
//		LOGGER.info("Entered assignTeamReferees controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		ParticipantTeam participantTeam = service.findById(participantTeam_id);
//
//		participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam, round);
//		LOGGER.info("deleteTeamReferresForParticipantTeam Successfully deleted");
//		Championship championship = participantTeam.getChampionship();
//		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
//				championship.getId(), participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
//				participantTeam.getGender().toString());
//		List<RefereesPanels> listRefereesPanels = new ArrayList<>();
//
//		try {
//			listRefereesPanels = championshipRefereePanelsService.get(championshipRefereePanelId).getRefereesPanels();
//		} catch (ChampionshipRefereePanelsNotFoundException e) {
//
//			e.printStackTrace();
//		}
//		for (RefereesPanels referee : listRefereesPanels) {
//			ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();
//
//			participantTeamReferees.setParticipantTeam(participantTeam);
//			// if any changes needed
//			participantTeamReferees.setParticipantTeam(participantTeam);
//			participantTeamReferees.setJudgeUser(referee.getJudgeUser());
//			;
//			participantTeamReferees.setRoundNumber(round);
//			participantTeamReferees.setChampionship(championship);
//			participantTeamReferees.setChampionshipCategory(championshipCategory);
//			participantTeamReferees.setType(referee.getType());
//			LOGGER.info("assignedReferee" + referee.toString());
//			participantTeamRefereesService.save(participantTeamReferees);
//			LOGGER.info("participantTeamReferees added successfully");
//		}
//
//		// service.assignRefereesForParticipantTeam(participantTeam);
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/referee/manage-team/edit/{participantTeam_id}";
//
//	}
//
////	@PostMapping("/manage-team/referees-panel/assign/{participantTeam_id}")
////	public String assignTeamRefereesPanel(@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
////			@RequestParam(value = "panel", required = true) Integer panel_id) {
////		LOGGER.info("Entered assignTeamRefereesPanel controller");
////		ParticipantTeam participantTeam = service.findById(participantTeam_id);
////		ChampionshipRefereePanels panel;
////		List<RefereesPanels> listReferees = new ArrayList<>();
////		try {
////			panel = championshipRefereePanelsService.get(panel_id);
////			listReferees = refereesPanelsService.getByChampionshipRefereePanels(panel);
////			for (RefereesPanels refereesPanel : listReferees) {
////				ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();
////				participantTeamReferees.setParticipantTeam(participantTeam);
////				participantTeamReferees.setUser(refereesPanel.getUser());
////				participantTeamRefereesService.save(participantTeamReferees);
////			}
////
////		} catch (ChampionshipRefereePanelsNotFoundException e) {
////			LOGGER.error("ChampionshipRefereePanels not found!");
////			e.printStackTrace();
////		}
////		LOGGER.info("Redirected to manage-team/edit page");
////		return "redirect:/admin/manage-team/edit/{participantTeam_id}";
////
////	}
////
//	private String addParticipantsToTeam(Integer[] participants, ParticipantTeam participantTeam) {
//
//		List<Participant> listAssignedParticipants = new ArrayList<>();
//		if (participants != null) {
//			for (Integer assignParticipantId : participants) {
//				try {
//					Participant participant = participantService.getParticipantById(assignParticipantId);
//					LOGGER.info("assignParticipantId : " + assignParticipantId);
//					ParticipantTeamParticipants participantTeamParticipants = new ParticipantTeamParticipants();
//					participantTeamParticipants.setParticipantTeam(participantTeam);
//					participantTeamParticipants.setParticipant(participant);
//					ParticipantTeamParticipants savedParticipantTeamParticipants = participantTeamParticipantsService
//							.save(participantTeamParticipants);
//
//					// assign compulsory asanas only when creating new group
//					service.assignCompulsoryAsanasForTeam(participantTeam, savedParticipantTeamParticipants);
//					AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
//					if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
//							|| asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
//							|| asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
//						if (participantTeam.isDifferentAsanasForParticipants() == false) {
//							// if not different asanas, add optional asanas which are added to others
//							// already to the new participant.
//							service.assignOptionalAsanasForNewParticipant(participantTeam,
//									savedParticipantTeamParticipants);
//						}
//
//					}
//
//				} catch (ParticipantNotFoundException e) {
//					LOGGER.error("ParticipantTeam not found!");
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		return "Participants added successfully";
//	}
//
//}
