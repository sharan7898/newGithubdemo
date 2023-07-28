package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.AsanaLimit;
import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.AsanaCodeDTO;
import com.swayaan.nysf.entity.DTO.CategorySelectionDTO;
import com.swayaan.nysf.entity.DTO.RoundAsanaStatusDTO;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamAsanasNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipLevelsService;
import com.swayaan.nysf.service.ChampionshipLinkService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamParticipantAsanasStatusService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.ScheduleService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/participant")

public class ManageParticipantModuleController {
	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ChampionshipLevelsService championshipLevelsService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;

	@Autowired
	ChampionshipLinkService championshipLinkService;
	@Autowired
	ScheduleService scheduleService;

	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;

	@Autowired
	ParticipantTeamService participantTeamService;

	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;
	@Autowired
	AsanaService asanaService;
	@Autowired
	AsanaLimit asanaLimit;
	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;
	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;
	@Autowired
	CommonUtil commonUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageParticipantModuleController.class);

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/dashboard")
	public String displayDashboard(Model model, HttpServletRequest request) {
		LOGGER.info("Entered displayDashboard method -ManageParticipantModuleController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		model.addAttribute("pageTitle", "Dashboard");
		LOGGER.info("Exit displayDashboard method -ManageParticipantModuleController");

		return "participant/participant_dashboard";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championships")
	public String getUpcomingChamponship(Model model, HttpServletRequest request) {
		LOGGER.info("Entered getUpcomingChamponship method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit getUpcomingChamponship method -ManageParticipantModuleController");

		return listAllChampionshipsByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Participant')")
	@GetMapping("/championships/page/{pageNum}")
	public String listAllChampionshipsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location, HttpServletRequest request) {
		LOGGER.info("Entered listAllChampionshipsByPage method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		// List<Championship> listChampionships =
		// service.listAllChampionshipsByNotDeleted();
		// List<Championship> listChampionships =
		// championshipService.findByStatusInAndStartDateGreaterThanEqual();

		Page<Championship> page = championshipService.listByParticipantPage(pageNum, sortField, sortDir, name,
				location);
		List<Championship> listChampionships = page.getContent();

		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/participant/championships");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", location);
		// System.out.println("listChampionships"+listChampionships);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Upcoming Championships");
		// Championship championship = new Championship();
		// model.addAttribute("Championship", championship);
		LOGGER.info("Exit listAllChampionshipsByPage method -ManageParticipantModuleController");

		return "participant/list_upcoming_championship";
	}

	/*
	 * @GetMapping("/championships") public String getUpcomingChamponship(Model
	 * model) { List<Championship> listChampionships =
	 * championshipService.findByStatusInAndStartDateGreaterThanEqual();
	 * model.addAttribute("listChampionships", listChampionships);
	 * model.addAttribute("pageTitle", "Upcoming Championships");
	 * 
	 * return "participant/list_upcoming_championship"; }
	 */

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championship/details/{id}")
	public String getChampionshipDetails(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes,HttpServletRequest request) {
		LOGGER.info("Entered getChampionshipDetails method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Championship championship = championshipService.getChampionshipById(id);
			if (championship.getStartDate().compareTo(new Date()) < 0
					|| !championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				redirectAttributes.addFlashAttribute("errorMessage", "Unable to find championship details");
				return "redirect:/participant/championships";
			}

			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			List<ChampionshipLevels> listChampionshipLevels = championshipLevelsService.listAllChampionshipLevels();
			ChampionshipCategory championshipCategory = new ChampionshipCategory();
			model.addAttribute("listChampionshipLevels", listChampionshipLevels);

			model.addAttribute("championshipCategory", championshipCategory);
			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("championship", championship);
			model.addAttribute("pageTitle", "Championship Details");
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			LOGGER.info("Entered getChampionshipDetails method -ManageParticipantModuleController");
			return "participant/championship_details";
			// return "administration/add_championship";
		} catch (ChampionshipNotFoundException ex) {
			LOGGER.error("Championship not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/participant/championships";
		}
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/enrolled-championships")
	public String getEnrolledChamponship(Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered getEnrolledChamponship method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit getEnrolledChamponship method -ManageParticipantModuleController");

		return listAllEnrolledChampionshipsByPage(1, model, "startDate", "asc", "", "", redirectAttributes, request);

	}

	@PreAuthorize("hasAnyAuthority('Participant')")
	@GetMapping("/enrolled-championships/page/{pageNum}")
	public String listAllEnrolledChampionshipsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered listAllEnrolledChampionshipsByPage method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Participant participant = new Participant();
		try {
			participant = CommonUtil.getCurrentParticipant();
		} catch (ParticipantNotFoundException e) {
			LOGGER.info("Unable to identify participant" + e.getMessage());
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to identify participant");
			return "redirect:/participant/participant_dashboard";
		}
		// List<Championship> listEnrolledChampionships = championshipService
		// .listAllParticipantEnrolledChampionships(participant);

		// List<Championship> listChampionships =
		// service.listAllChampionshipsByNotDeleted();
		// List<Championship> listChampionships =
		// championshipService.findByStatusInAndStartDateGreaterThanEqual();

		Page<Championship> page = championshipService.listByParticipantEnrolledPage(pageNum, sortField, sortDir, name,
				location, currentUser.getId());
		List<Championship> listEnrolledChampionships = page.getContent();

		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		/*
		 * List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
		 * List<User> listNonAssignedUsers = new ArrayList<>(); for (Integer user_id :
		 * listUsers) { User user = null; try { user = userService.get(user_id); } catch
		 * (UserNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } listNonAssignedUsers.add(user); }
		 */

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/participant/enrolled-championships");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", location);
		// model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("listEnrolledChampionships", listEnrolledChampionships);
		model.addAttribute("pageTitle", "Enrolled Championships");
		// Championship championship = new Championship();
		// model.addAttribute("Championship", championship);
		LOGGER.info("Exit listAllEnrolledChampionshipsByPage method -ManageParticipantModuleController");

		return "participant/enrolled_championships";
	}

	/*
	 * @PreAuthorize("hasAuthority('Participant')")
	 * 
	 * @GetMapping("/enrolled-championships") public String
	 * listAllEnrolledChampionships(Model model, RedirectAttributes
	 * redirectAttributes, HttpServletRequest request) { LOGGER.
	 * info("Entered listAllEnrolledChampionships ManageParticipantModuleController"
	 * ); // For Navigation history String mappedUrl = request.getRequestURI(); User
	 * currentUser = CommonUtil.getCurrentUser();
	 * CommonUtil.setNavigationHistory(mappedUrl, currentUser);
	 * 
	 * Participant participant = new Participant(); try { participant =
	 * CommonUtil.getCurrentParticipant(); } catch (ParticipantNotFoundException e)
	 * { LOGGER.info("Unable to identify participant"); e.printStackTrace();
	 * redirectAttributes.addFlashAttribute("errorMessage",
	 * "Unable to identify participant"); return
	 * "redirect:/participant/participant_dashboard"; } List<Championship>
	 * listEnrolledChampionships = championshipService
	 * .listAllParticipantEnrolledChampionships(participant);
	 * 
	 * model.addAttribute("listEnrolledChampionships", listEnrolledChampionships);
	 * model.addAttribute("pageTitle", "Enrolled Championships"); return
	 * "participant/enrolled_championships"; }
	 */

	// Generate link should generate the below url
	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championship/{championshipId}/getDetails")
	public String listAllEnrolledChampionships(Model model, @PathVariable("championshipId") Integer championshipId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered listAllEnrolledChampionships method -ManageParticipantModuleController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.getChampionshipById(championshipId);
		if (!championshipService.isNotRejectedAndDeletedChampionship(championshipId)) {
			return "error/403";
		}

		try {
			Participant participant = CommonUtil.getCurrentParticipant();
			if (!participantTeamParticipantsService.isCurrentUserChampionshipParticipant(participant, championship)) {
				return "error/403";
			}

			ChampionshipCategory championshipCategory = new ChampionshipCategory();
			List<CategorySelectionDTO> listEnrolledChampionshipCategory = championshipCategoryService
					.getEnrolledCatgeoryForChampionshipAndParticipant(championship, participant);
			model.addAttribute("championship", championship);
			model.addAttribute("championshipCategory", championshipCategory);
			model.addAttribute("listEnrolledChampionshipCategory", listEnrolledChampionshipCategory);
		} catch (ParticipantNotFoundException e) {
			LOGGER.error("Participant not found!");
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/participant/enrolled-championships";
		}

		model.addAttribute("pageTitle", "Championship");
		LOGGER.info("Exit listAllEnrolledChampionships method -ManageParticipantModuleController");

		return "participant/enrolled_championship_details";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championship/{championshipId}/team/{teamId}/add-details")
	public String teamDetails(Model model, @PathVariable("championshipId") Integer championshipId,
			@PathVariable("teamId") Integer teamId, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ChampionshipNotFoundException {
		LOGGER.info("Entered teamDetails method -ManageParticipantModuleController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.getChampionshipById(championshipId);
		ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
		model.addAttribute("championshipId", championshipId);

		if (championshipLink.getStatus() == true) {

			try {
				ParticipantTeam participantTeam = participantTeamService.get(teamId);

//				ParticipantTeamParticipants teamLeadParticipant = participantTeamParticipantsService
//						.findByParticipantTeamAndTeamLead(participantTeam, true);
//				Boolean isCurrentUserTeamLead = false;
//				if (teamLeadParticipant != null) {
//					if (teamLeadParticipant.getParticipant().getId() == currentUser.getId()) {
//						isCurrentUserTeamLead = true;
//					}
//				}
//				LOGGER.info("isCurrentUserTeamLead" + isCurrentUserTeamLead);
//				model.addAttribute("isCurrentUserTeamLead", isCurrentUserTeamLead);				
				List<ParticipantTeamParticipants> listParticipants = participantTeam.getParticipantTeamParticipants();

				Boolean isTeamLead = false;
				for (ParticipantTeamParticipants pp : listParticipants) {
					if (pp.getIsTeamLead() == true) {
						isTeamLead = true;
						break;
					}
				}

				LOGGER.info("isTeamLead" + isTeamLead);
				model.addAttribute("isTeamLead", isTeamLead);
				model.addAttribute("participantTeam", participantTeam);
				// check if asanas are already added & Frozen
				ChampionshipCategory championshipCategory = championshipCategoryService
						.getChampionshipCategoryForAllConditions(championshipId,
								participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
								participantTeam.getGender().toString());
				List<ParticipantTeamParticipantAsanasStatus> listAsanaStatus = participantTeamParticipantAsanasStatusService
						.findAllByParticipantTeam(participantTeam);
				Boolean isAsanasFrozen = false;
				if (!listAsanaStatus.isEmpty()) {
					List<ParticipantTeamParticipantAsanasStatus> roundAsanaStatusFrozen = listAsanaStatus.stream()
							.filter(asanaStatus -> asanaStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE))
							.collect(Collectors.toList());
					if (roundAsanaStatusFrozen.size() == championshipCategory.getNoOfRounds()) {
						// Asanas are frozen
						isAsanasFrozen = true;
					}
//					if(isAsanasFrozen) {
//						//redirect to asanas are already added. show the page - view details
//						return "redirect:/participant/championship/"+championshipId+"/team/"+teamId+"/showTeamDetailsWithAsanas";
//					}

				}
				model.addAttribute("isAsanasFrozen", isAsanasFrozen);

			} catch (ParticipantTeamNotFoundException e) {
				LOGGER.error("Participant Team not found!");
				model.addAttribute("pageTitle", "Championship");
				redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:/participant/championship/" + championshipId + "/getDetails";
			}

			model.addAttribute("pageTitle", "Team");
			return "participant/team_details";
		}

		else {

			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship link is deactivated,Please contact the Event Manager/Admin.");
			LOGGER.info("Exit teamDetails method -ManageParticipantModuleController");

			return "redirect:/participant/enrolled-championships";
		}
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championship/{championshipId}/team/{teamId}/showTeamDetailsWithAsanas")
	public String showTeamDetails(Model model, @PathVariable("championshipId") Integer championshipId,
			@PathVariable("teamId") Integer teamId, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws ChampionshipNotFoundException {
		LOGGER.info("Entered showTeamDetails method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Championship championship = championshipService.getChampionshipById(championshipId);
		ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
		model.addAttribute("championshipId", championshipId);

		if (championshipLink.getStatus() == true) {

			try {
				ParticipantTeam participantTeam = participantTeamService.get(teamId);
				ParticipantTeamParticipants teamLead = participantTeamParticipantsService
						.findByParticipantTeamAndTeamLead(participantTeam, true);

				Boolean isCurrentUserTeamLead = participantTeamParticipantsService
						.checkIfCurrentUserTeamLead(participantTeam, currentUser);

				Integer noOfRounds = championshipCategoryService.getChampionshipCategoryRoundsForChampionshipId(
						participantTeam.getChampionship(), participantTeam.getAsanaCategory(),
						participantTeam.getCategory(), participantTeam.getGender());
				LOGGER.info("noOfRounds :" + noOfRounds);

				List<ParticipantTeamAsanas> listAsanas = new ArrayList<ParticipantTeamAsanas>();
				List<List<ParticipantTeamAsanas>> listRoundWiseParticipantTeamAsanas = new ArrayList<>();
				for (Integer round = 1; round <= noOfRounds; round++) {
					listAsanas = participantTeamAsanasService
							.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
					listRoundWiseParticipantTeamAsanas.add(listAsanas);
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

				model.addAttribute("mapParticipantsWithRoundAsanas", mapParticipantsWithRoundAsanas);

				List<Asana> listAllAsanas = asanaService.listAllAsanas();
				model.addAttribute("participantTeam", participantTeam);
				model.addAttribute("pageTitle", "Add Asanas");
				model.addAttribute("currentUser", currentUser);
				model.addAttribute("championshipId", championshipId);
				model.addAttribute("isCurrentUserTeamLead", isCurrentUserTeamLead);
				model.addAttribute("listAllAsanas", listAllAsanas);
				model.addAttribute("teamLead", teamLead);
				model.addAttribute("listRoundWiseParticipantTeamAsanas", listRoundWiseParticipantTeamAsanas);
				model.addAttribute("noOfRounds", noOfRounds);
				model.addAttribute("listRoundAsanaStatus", listRoundAsanaStatus);

				Integer limit = -1;
				AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
				if (asanaCategory.getId() == 1) {
					limit = asanaLimit.getTraditionalSingleLimit();
				} else if (asanaCategory.getId() == 2) {
					limit = asanaLimit.getArtisticSingleLimit();
				} else if (asanaCategory.getId() == 3) {
					limit = asanaLimit.getArtisticPairLimit();
				} else if (asanaCategory.getId() == 4) {
					limit = asanaLimit.getRhythmicPairLimit();
				} else if (asanaCategory.getId() == 5) {
					limit = asanaLimit.getArtisticGroupLimit();
				} else {
					limit = asanaLimit.getCommonLimit();
				}
				model.addAttribute("asanasLimit", limit);
				LOGGER.info("Redirected to team_form page");
				return "participant/show_full_team_details";
			} catch (ParticipantTeamNotFoundException ex) {
				LOGGER.error("ParticipantTeam not found!");
				redirectAttributes.addFlashAttribute("message", "Team not found.");
				return "redirect:/participant/championship/" + championshipId + "/getDetails";
			}
		} else {

			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship link is deactivated,Please contact the Event Manager/Admin.");
			LOGGER.info("Exit showTeamDetails method -ManageParticipantModuleController");

			return "redirect:/participant/enrolled-championships";
		}
	}

	@PreAuthorize("hasAuthority('Participant')")
	@PostMapping("/team/{teamId}/lead/{participantTeamParticipantId}")
	public String saveTeamLead(Model model, @PathVariable("teamId") Integer teamId,
			@PathVariable("participantTeamParticipantId") Integer participantTeamParticipantId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered saveTeamLead method -ManageParticipantModuleController");
		LOGGER.info("participantTeamParticipantId" + participantTeamParticipantId);

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = null;

		try {
			ParticipantTeam participantTeam = participantTeamService.get(teamId);
			championship = participantTeam.getChampionship();
//			ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
//			if (championshipLink.getStatus() == false) {
//				redirectAttributes.addFlashAttribute("errorMessage",
//						"The Link is not active yet to add details. Please contact Administrator");
//				return "redirect:/participant/enrolled-championships";
//			}

			List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
					.getParticipantTeamParticipants();
			List<ParticipantTeamParticipantAsanasStatus> listParticipantTeamAsanaStatus = participantTeamParticipantAsanasStatusService
					.findAllByParticipantTeam(participantTeam);

			ParticipantTeamParticipants existingTeamLeadParticipant = participantTeamParticipantsService
					.findByParticipantTeamAndTeamLead(participantTeam, true);
			ParticipantTeamParticipants newTeamLeadParticipant = participantTeamParticipantsService
					.get(participantTeamParticipantId);
			// if team lead is already present (since this can't be checked - added by admin
			// / event manager - check & return the view
			if (existingTeamLeadParticipant != null) {
				redirectAttributes.addFlashAttribute("message1",
						"Note: Team Lead : " + existingTeamLeadParticipant.getParticipantFullName());
				return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
						+ "/add-team-asanas";
			}

			if (existingTeamLeadParticipant == null) {
				newTeamLeadParticipant.setIsTeamLead(true);
				participantTeamParticipantsService.save(newTeamLeadParticipant);
				return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
						+ "/add-team-asanas";
			} else if (existingTeamLeadParticipant.equals(newTeamLeadParticipant)) {
				return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
						+ "/add-team-asanas";
			} else {
				if (listParticipantTeamAsanaStatus.isEmpty()) {

					// Asanas not yet added - make team lead
					setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam,
							newTeamLeadParticipant);
				} else {
					List<User> lastModifiedUsers = new ArrayList<>();
					List<User> createdUsers = new ArrayList<>();
					for (ParticipantTeamParticipantAsanasStatus participantTeamAsanaStatus : listParticipantTeamAsanaStatus) {
						lastModifiedUsers.add(participantTeamAsanaStatus.getLastModifiedBy());
						createdUsers.add(participantTeamAsanaStatus.getCreatedBy());
					}
					if (lastModifiedUsers.isEmpty()) {
						List<User> participantRoleUser = createdUsers.stream()
								.filter(user -> user.hasRole("Participant")).collect(Collectors.toList());
						if (participantRoleUser.isEmpty()) {
							// Asanas not added by participant
							setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam,
									newTeamLeadParticipant);
						} else {
							if (participantRoleUser.get(0).getId() == newTeamLeadParticipant.getParticipant().getId()) {
								setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam,
										newTeamLeadParticipant);
							} else {
								// Asanas added by participant
								redirectAttributes.addFlashAttribute("errorMessage",
										"Asanas added by another team Member. You cannot change the team lead");

								return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
										+ "/add-details";
							}

						}

					} else {
						lastModifiedUsers.addAll(createdUsers);
						List<User> participantRoleUser = lastModifiedUsers.stream()
								.filter(user -> user.hasRole("Participant")).collect(Collectors.toList());

						if (participantRoleUser.isEmpty()) {
							// Asanas not added by participant
							setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam,
									newTeamLeadParticipant);
						} else {
							if (participantRoleUser.get(0).getId() == newTeamLeadParticipant.getParticipant().getId()) {
								setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam,
										newTeamLeadParticipant);
							} else {
								// Asanas added by participant
								redirectAttributes.addFlashAttribute("errorMessage",
										"Asanas added by another team Member. You cannot change the team lead");

								return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
										+ "/add-details";
							}

						}
					}
				}
			}
			return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
					+ "/add-team-asanas";
		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error(e.getMessage());
			LOGGER.info("Exit saveTeamLead method -ManageParticipantModuleController");

			return "redirect:/participant/championship/" + championship.getId() + "/getDetails";

		}
	}

//	@PostMapping("/team/{teamId}/lead/{participantTeamParticipantId}")
//	public String saveTeamLead(Model model, @PathVariable("teamId") Integer teamId,
//			@PathVariable("participantTeamParticipantId") Integer participantTeamParticipantId,
//			HttpServletRequest request, RedirectAttributes redirectAttributes) {
//
//		LOGGER.error("Entered saveTeamLead - ManageParticipantModuleController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = null;
//		try {
//			ParticipantTeam participantTeam = participantTeamService.get(teamId);
//			championship = participantTeam.getChampionship();
//			List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
//					.getParticipantTeamParticipants();
//			List<ParticipantTeamParticipantAsanasStatus> listParticipantTeamAsanaStatus = participantTeamParticipantAsanasStatusService
//					.findAllByParticipantTeam(participantTeam);
//			ParticipantTeamParticipants teamLead = null;
//			if (participantTeamParticipantId == null) {
//				teamLead = participantTeamParticipantsService.findByParticipantTeamAndTeamLead(participantTeam, true);
//			} else {
//				teamLead = participantTeamParticipantsService.get(participantTeamParticipantId);
//			}
//			
//				
//			if (listParticipantTeamAsanaStatus.isEmpty()) {
//				// Asanas not yet added - make team lead
//				setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam, teamLead);
//
//			} else {
//				for (ParticipantTeamParticipantAsanasStatus participantTeamAsanaStatus : listParticipantTeamAsanaStatus) {
//
//					if (participantTeamAsanaStatus.getLastModifiedBy() == null) {
//
//						if ((participantTeamAsanaStatus.getCreatedBy().hasRole("Administrator")
//								|| participantTeamAsanaStatus.getCreatedBy().hasRole("EventManager"))) {
//							// make team lead
//							setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam, teamLead);
//						} else {
//							redirectAttributes.addFlashAttribute("errorMessage",
//									"Asanas added by another team Member. You cannot change the team lead");
//							
//							
//							return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
//									+ "/add-details";
//						}
//					} else {
//
//						if ((participantTeamAsanaStatus.getLastModifiedBy().hasRole("Administrator")
//								|| participantTeamAsanaStatus.getLastModifiedBy().hasRole("EventManager"))) {
//							// make team lead
//							setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam, teamLead);
//						} else if (participantTeamAsanaStatus.getLastModifiedBy().equals(currentUser)) {
//
//							// make him team lead
//							setParticipantTeamLeadStatus(listParticipantTeamParticipants, participantTeam, teamLead);
//						} else {
//							// asanas added by another team member. You cannot change the team lead
//							redirectAttributes.addFlashAttribute("errorMessage",
//									"Asanas added by another team Member. You cannot change the team lead");
//							System.out.println("--------------------------++++++++++++++++++++-----------------");
//							return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
//									+ "/add-details";
//
//						}
//
//					}
//
//				}
//			}
//
//			return "redirect:/participant/championship/" + championship.getId() + "/team/" + teamId
//					+ "/add-team-asanas";
//		} catch (ParticipantTeamNotFoundException e) {
//			LOGGER.error(e.getMessage());
//
//			return "redirect:/participant/championship/" + championship.getId() + "/getDetails";
//
//		}
//	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championship/{championshipId}/team/{teamId}/add-team-asanas")
	public String addAsanasForParticipant(Model model, @PathVariable("championshipId") Integer championshipId,
			@PathVariable("teamId") Integer teamId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered addAsanasForParticipant method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Boolean isCurrentUserTeamLead = false;
		try {
			ParticipantTeam participantTeam = participantTeamService.get(teamId);
			Championship championship = null;
			try {
				championship = championshipService.getChampionshipById(participantTeam.getChampionship().getId());
			} catch (ChampionshipNotFoundException e1) {

				LOGGER.error("Championship not found!");
				redirectAttributes.addFlashAttribute("message", "Championship not found");
			}
			ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
			if (championshipLink.getStatus() == false) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"The Link is not active yet to add details. Please contact Administrator");
				return "redirect:/participant/enrolled-championships";
			}
			model.addAttribute("championship", championship);

			ParticipantTeamParticipants teamLead = participantTeamParticipantsService
					.findByParticipantTeamAndTeamLead(participantTeam, true);
			if (teamLead == null) {
				isCurrentUserTeamLead = false;
			} else if (teamLead.getParticipant().getId() == currentUser.getId()) {
				isCurrentUserTeamLead = true;
			} else {
				isCurrentUserTeamLead = false;
			}
			Integer noOfRounds = championshipCategoryService.getChampionshipCategoryRoundsForChampionshipId(
					participantTeam.getChampionship(), participantTeam.getAsanaCategory(),
					participantTeam.getCategory(), participantTeam.getGender());
			LOGGER.info("noOfRounds :" + noOfRounds);

			List<ParticipantTeamAsanas> listAsanas = new ArrayList<ParticipantTeamAsanas>();
			List<List<ParticipantTeamAsanas>> listRoundWiseParticipantTeamAsanas = new ArrayList<>();
			for (Integer round = 1; round <= noOfRounds; round++) {
				listAsanas = participantTeamAsanasService
						.getDistinctByAsanaAndTeamAndRoundOrderBySequenceNum(participantTeam, round);
				listRoundWiseParticipantTeamAsanas.add(listAsanas);
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

			model.addAttribute("mapParticipantsWithRoundAsanas", mapParticipantsWithRoundAsanas);

			List<Asana> listAllAsanas = asanaService.listAllAsanas();
			model.addAttribute("participantTeam", participantTeam);
			model.addAttribute("pageTitle", "Add Asanas");
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("championshipId", championshipId);
			model.addAttribute("isCurrentUserTeamLead", isCurrentUserTeamLead);
			model.addAttribute("listAllAsanas", listAllAsanas);
			model.addAttribute("teamLead", teamLead.getId());
			model.addAttribute("listRoundWiseParticipantTeamAsanas", listRoundWiseParticipantTeamAsanas);
			model.addAttribute("noOfRounds", noOfRounds);
			model.addAttribute("listRoundAsanaStatus", listRoundAsanaStatus);

			Integer limit = -1;
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
			if (asanaCategory.getId() == 1) {
				limit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == 2) {
				limit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == 3) {
				limit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == 4) {
				limit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == 5) {
				limit = asanaLimit.getArtisticGroupLimit();
			} else {
				limit = asanaLimit.getCommonLimit();
			}
			model.addAttribute("asanasLimit", limit);
			LOGGER.info("Redirected to team_form page");
			return "participant/add_asanas";
		} catch (ParticipantTeamNotFoundException ex) {
			LOGGER.error("ParticipantTeam not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", "Team not found.");
			LOGGER.info("Entered addAsanasForParticipant method -ManageParticipantModuleController");

			return "redirect:/participant/championship/" + championshipId + "/team/" + teamId + "/add-details";
		}
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/manage-team/participant-asanas/load/{participantTeam_id}/{participantTeamParticipant_id}/round/{round_type}")
	public String loadRoundAsanasForParticipant(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("participantTeamParticipant_id") Integer participantTeamParticipant_id,
			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		LOGGER.info("Entered loadRoundAsanasForParticipant method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);
		ParticipantTeam participantTeam = null;
		try {
			participantTeam = participantTeamService.get(participantTeam_id);
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

			if (asanaCategory.getId() == 1) {
				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == 2) {
				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == 3) {
				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == 4) {
				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == 5) {
				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
			} else {
				totalAsanasLimit = asanaLimit.getCommonLimit();
			}

			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount - countOptionalAssignedAsanas;
			model.addAttribute("participantTeamParticipant_id", participantTeamParticipant_id);
			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);

			LOGGER.info("Exit loadRoundAsanasForParticipant method -ManageParticipantModuleController");
			return "participant/team_participant_round_asanas_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!");
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
					+ participantTeam_id + "/add-team-asanas";
		}
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/manage-team/asanas/load/{participantTeam_id}/round/{round_type}")
	public String loadTeamRoundAsanas(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("round_type") Integer round_type, Model model, RedirectAttributes ra,
			HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered loadTeamRoundAsanas method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);
		ParticipantTeam participantTeam = null;

		try {
			participantTeam = participantTeamService.get(participantTeam_id);
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

			if (asanaCategory.getId() == 1) {
				totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
			} else if (asanaCategory.getId() == 2) {
				totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
			} else if (asanaCategory.getId() == 3) {
				totalAsanasLimit = asanaLimit.getArtisticPairLimit();
			} else if (asanaCategory.getId() == 4) {
				totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
			} else if (asanaCategory.getId() == 5) {
				totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
			} else {
				totalAsanasLimit = asanaLimit.getCommonLimit();
			}

			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;

			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);
			LOGGER.info("Redirected to team_round_asanas_modal page");
			return "participant/team_round_asanas_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!" + e.getMessage());
			ra.addFlashAttribute("message", e.getMessage());
			LOGGER.info("Exit loadTeamRoundAsanas method -ManageParticipantModuleController");

			return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
					+ participantTeam_id + "/add-team-asanas";
		}
	}

	@PreAuthorize("hasAuthority('Participant')")
	@PostMapping("/manage-team/asanas/assign/{participantTeam_id}")
	public String assignTeamAsanas(
			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
			@RequestParam(value = "round", required = true) Integer round,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id, RedirectAttributes ra, Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered assignTeamAsanas method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		// function when common asanas are assigned to the team participants.
		ParticipantTeam participantTeam = participantTeamService.findById(participantTeam_id);
		Championship championship = null;
		try {
			championship = championshipService.getChampionshipById(participantTeam.getChampionship().getId());
		} catch (ChampionshipNotFoundException e1) {

			LOGGER.error("Championship not found!" + e1.getMessage());
			ra.addFlashAttribute("message", "Championship not found");
		}

		// check if current user team lead
		Boolean isCurrentUserTeamLead = participantTeamParticipantsService.checkIfCurrentUserTeamLead(participantTeam,
				currentUser);
		if (!isCurrentUserTeamLead) {

			ra.addFlashAttribute("errorMessage",
					"You are not team lead to add asanas. Please contact admin for more details.");
			return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
					+ participantTeam_id + "/add-team-asanas";
		}

		ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
		if (championshipLink.getStatus() == false) {
			ra.addFlashAttribute("errorMessage",
					"The Link is not active yet to add details. Please contact Administrator");
			return "redirect:/participant/enrolled-championships";
		}
		model.addAttribute("championship", championship);

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

		if (asanaCategory.getId() == 1) {
			totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
		} else if (asanaCategory.getId() == 2) {
			totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
		} else if (asanaCategory.getId() == 3) {
			totalAsanasLimit = asanaLimit.getArtisticPairLimit();
		} else if (asanaCategory.getId() == 4) {
			totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
		} else if (asanaCategory.getId() == 5) {
			totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
		} else {
			totalAsanasLimit = asanaLimit.getCommonLimit();
		}

		if ((count + participantTeamAsanas.length) > totalAsanasLimit) {
			message = count + " asanas are already added. You can add only " + (totalAsanasLimit - count) + " asanas";
		} else if ((count + participantTeamAsanas.length) <= totalAsanasLimit) {

			List<Asana> listAssignedAsanas = new ArrayList<>();
			if (participantTeamAsanas != null) {

				List<ParticipantTeamParticipants> listAssignedParticipants = participantTeamParticipantsService
						.getTeamParticipantsForParticipantTeam(participantTeam);

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
		LOGGER.info("Exit assignTeamAsanas method -ManageParticipantModuleController");
		return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
				+ participantTeam_id + "/add-team-asanas";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@PostMapping("/manage-team/participant-asanas/assign/{participantTeam_id}")
	public String assignTeamParticipantRoundAsanas(
			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
			@RequestParam(value = "round", required = true) Integer round,
			@RequestParam(value = "participantTeamParticipant_id", required = true) Integer participantTeamParticipant_id,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id, Model model, RedirectAttributes ra,
			HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered assignTeamParticipantRoundAsanas method -ManageParticipantModuleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.getChampionshipById(championshipId);
//		ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
//		
//		
//		if (championshipLink.getStatus() == false) {
//			ra.addFlashAttribute("errorMessage",
//					"The Link is not active yet to add details. Please contact Administrator");
//			return "redirect:";
//		}
		ParticipantTeam participantTeam = participantTeamService.findById(participantTeam_id);
		Championship championship = null;
		try {
			championship = championshipService.getChampionshipById(participantTeam.getChampionship().getId());
		} catch (ChampionshipNotFoundException e1) {

			LOGGER.error("Championship not found!");
			ra.addFlashAttribute("message", "Championship not found");
		}

		// check if current user team lead
		Boolean isCurrentUserTeamLead = participantTeamParticipantsService.checkIfCurrentUserTeamLead(participantTeam,
				currentUser);
		if (!isCurrentUserTeamLead) {

			ra.addFlashAttribute("errorMessage",
					"You are not team lead to add asanas. Please contact admin for more details.");
			return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
					+ participantTeam_id + "/add-team-asanas";
		}

		ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);
		if (championshipLink.getStatus() == false) {
			ra.addFlashAttribute("errorMessage",
					"The Link is not active yet to add details. Please contact Administrator");
			return "redirect:/participant/enrolled-championships";
		}
		model.addAttribute("championship", championship);

		AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
		String asanaCategoryName = participantTeam.getAsanaCategoryName();

		ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
				.get(participantTeamParticipant_id);

		String message = null;
		int count = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant, round)
				.size();
		LOGGER.info("Count of asanas :" + count);
		LOGGER.info("Count of ParticipantTeamAsanas :" + participantTeamAsanas.length);
		LOGGER.info("Total count :" + (count + participantTeamAsanas.length));

		Integer totalAsanasLimit = -1;

		if (asanaCategory.getId() == 1) {
			totalAsanasLimit = asanaLimit.getTraditionalSingleLimit();
		} else if (asanaCategory.getId() == 2) {
			totalAsanasLimit = asanaLimit.getArtisticSingleLimit();
		} else if (asanaCategory.getId() == 3) {
			totalAsanasLimit = asanaLimit.getArtisticPairLimit();
		} else if (asanaCategory.getId() == 4) {
			totalAsanasLimit = asanaLimit.getRhythmicPairLimit();
		} else if (asanaCategory.getId() == 5) {
			totalAsanasLimit = asanaLimit.getArtisticGroupLimit();
		} else {
			totalAsanasLimit = asanaLimit.getCommonLimit();
		}

		if ((count + participantTeamAsanas.length) > totalAsanasLimit) {
			message = count + " asanas are already added. You can add only " + (totalAsanasLimit - count) + " asanas";
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
										participantTeam.getGender().toString(), participantTeam.getCategory().getId());

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
		ra.addFlashAttribute("message", message);
		LOGGER.info("Exit assignTeamParticipantRoundAsanas method -ManageParticipantModuleController");
		return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
				+ participantTeam_id + "/add-team-asanas";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/manage-team/asanas/delete/{participantTeamAsanasId}")
	public String deleteAssignedTeamAsanas(@PathVariable(name = "participantTeamAsanasId") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamAsanasNotFoundException {
		LOGGER.info("Entered deleteAssignedTeamAsanas method -ManageParticipantModuleController");
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

		// check if current user team lead
		Boolean isCurrentUserTeamLead = participantTeamParticipantsService.checkIfCurrentUserTeamLead(participantTeam,
				currentUser);
		if (!isCurrentUserTeamLead) {

			redirectAttributes.addFlashAttribute("errorMessage",
					"You are not team lead to delete asanas. Please contact admin for more details.");
			return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
					+ participantTeamId + "/add-team-asanas";
		}

		if (participantTeam.isDifferentAsanasForParticipants() == false) { // common asanas for all participants
			List<ParticipantTeamAsanas> listCommonAssignedAsanasForParticpants = participantTeamAsanasService
					.getAllByAsanaAndTeam(participantTeamAsanas.getAsana(), participantTeam);
			for (ParticipantTeamAsanas teamAsanas : listCommonAssignedAsanasForParticpants) {
				participantTeamAsanasService.deleteById(teamAsanas.getId());
			}
		} else if (participantTeam.isDifferentAsanasForParticipants() == true) { // if different asanas for all
																					// participants
			participantTeamAsanasService.deleteById(id);
		}

		redirectAttributes.addFlashAttribute("message", "Asanas removed Successfully");
		LOGGER.info("Redirected to manage-team/edit page");
		return "redirect:/participant/championship/" + participantTeam.getChampionship().getId() + "/team/"
				+ participantTeamId + "/add-team-asanas";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@PostMapping("/saveAsanasStatus")
	@ResponseBody
	public String saveRoundAsanaStatusForTeam(@RequestParam("teamLead") Integer teamLeadId,
			@RequestParam("participantTeam") Integer participantTeamId, @RequestParam("round") Integer round,
			@RequestParam("status") String status, HttpServletRequest request) {
		LOGGER.info("Entered saveRoundAsanaStatusForTeam ");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam;
		User user = CommonUtil.getCurrentUser();
		try {
			participantTeam = participantTeamService.get(participantTeamId);

			// check if current user team lead
			Boolean isCurrentUserTeamLead = participantTeamParticipantsService
					.checkIfCurrentUserTeamLead(participantTeam, currentUser);
			if (!isCurrentUserTeamLead) {
				return "FAIL";
			}
			ParticipantTeamParticipantAsanasStatus asanaStatusRecord = participantTeamParticipantAsanasStatusService
					.findByParticipantTeamAndRound(participantTeam, round);
			AsanaStatusEnum asanaStatus = AsanaStatusEnum.valueOf(status);
			if (asanaStatusRecord == null) {
				ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = new ParticipantTeamParticipantAsanasStatus();
				participantTeamParticipantAsanasStatus.setAsanaStatus(asanaStatus);
				participantTeamParticipantAsanasStatus.setCreatedBy(user);
				participantTeamParticipantAsanasStatus.setCreatedTime(new Date());
				participantTeamParticipantAsanasStatus.setParticipantTeam(participantTeam);
				participantTeamParticipantAsanasStatus.setRound(round);
				participantTeamParticipantAsanasStatusService.save(participantTeamParticipantAsanasStatus);
			} else {
				asanaStatusRecord.setAsanaStatus(asanaStatus);
				asanaStatusRecord.setLastModifiedBy(user);
				asanaStatusRecord.setLastModifiedTime(new Date());
				participantTeamParticipantAsanasStatusService.save(asanaStatusRecord);
			}
			return "OK";

		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Exit deleteAssignedTeamAsanas method -ManageParticipantModuleController");

			return "FAIL";
		}

	}

	private void setParticipantTeamLeadStatus(List<ParticipantTeamParticipants> listParticipantTeamParticipants,
			ParticipantTeam participantTeam, ParticipantTeamParticipants teamLead) {
		LOGGER.info("Entered setParticipantTeamLeadStatus method");

		// set the team Lead participant
		if (teamLead != null) {
			teamLead.setIsTeamLead(true);
			participantTeamParticipantsService.save(teamLead);
		}
		// update status of all participants to false
		participantTeamParticipantsService.setOtherParticipantsTeamLeadStatusToFalse(participantTeam, teamLead);
		// set the team Lead participant
		if (teamLead != null) {
			teamLead.setIsTeamLead(true);
			participantTeamParticipantsService.save(teamLead);
		}

	}
}
