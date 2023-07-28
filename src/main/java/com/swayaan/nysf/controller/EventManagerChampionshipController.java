package com.swayaan.nysf.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.API.entity.livescores.CategoriesDTO1;
import com.swayaan.nysf.API.entity.livescores.LiveScoresDTO;
import com.swayaan.nysf.API.entity.livescores.RoundsDTO1;
import com.swayaan.nysf.API.entity.livescores.TeamsDTO1;
import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.ArtisticJudgeScore;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.AsanaLimit;
import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipRefereePanelsEnum;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.EvaluatorJudgeScore;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgePanelLimit;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantScoringJudge;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamPanel;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.entity.TraditionalJudgeScore;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.AsanaCodeDTO;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO1;
import com.swayaan.nysf.entity.DTO.RoundAsanaStatusDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.CompulsoryRoundAsanasNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamAsanasNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamParticipantsNotFoundException;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.ArtisticJudgeScoreService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaEvaluationQuestionsService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipLinkService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.EvaluatorJudgeScoreService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.ParticipantScoringJudgeService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamPanelService;
import com.swayaan.nysf.service.ParticipantTeamParticipantAsanasStatusService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.RefereePanelsService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.TimeKeeperJudgeScoreService;
import com.swayaan.nysf.service.TraditionalJudgeScoreService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
@RequestMapping("/eventmanager")
public class EventManagerChampionshipController {

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipLinkService championshipLinkService;
	@Autowired
	StateService stateService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	AgeCategoryService ageCategoryService;

	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	CommonEmailUtil emailUtil;

	@Autowired
	CommonUtil commonUtil;

	@Autowired
	SettingService settingService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	AsanaService asanaService;
	@Autowired
	UserService userService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;

	@Autowired
	ChampionshipRefereePanelsService championshipRefereePanelsService;
	@Autowired
	RefereesPanelsService refereesPanelsService;

	@Autowired
	RefereePanelsService refereePanelService;

	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;

	@Autowired
	RoleService roleService;

	@Autowired
	AsanaLimit asanaLimit;

	@Autowired
	JudgePanelLimit judgePanelLimit;

	@Autowired
	JudgeService judgeService;

	@Autowired
	JudgeTypeService judgeTypeService;

	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;
	@Autowired
	AsanaCategoryService asanaCategoryService;

	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;

	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	@Autowired
	EvaluatorJudgeScoreService evaluatorJudgeScoreService;
	@Autowired
	TraditionalJudgeScoreService traditionalJudgeScoreService;
	@Autowired
	TimeKeeperJudgeScoreService timeKeeperJudgeScoreService;
	@Autowired
	ArtisticJudgeScoreService artisticJudgeScoreService;
	@Autowired
	AsanaEvaluationQuestionsService asanaEvaluationQuestionsService;
	@Autowired
	ParticipantScoringJudgeService participantScoringJudgeService;

	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;
	@Autowired
	ParticipantTeamPanelService participantTeamPanelService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerChampionshipController.class);

	private static final Integer ROLE_PARTICIPANT = 3;
	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int D_JUDGE_TYPE_ID = 2;
	private static final int A_JUDGE_TYPE_ID = 3;
	private static final int T_JUDGE_TYPE_ID = 4;
	private static final int E_JUDGE_TYPE_ID = 5;

	private static final int RHYTHMIC_PAIR_PARTICIPANT_COUNT = 2;
	private static final int ARTISTIC_GROUP_PARTICIPANT_COUNT = 5;
	
	private static final int TRADITIONAL_CATEGORY_JUDGE_COUNT = 9;
	private static final int ARTISTIC_SINGLE_CATEGORY_JUDGE_COUNT = 11;
	private static final int ARTISTIC_PAIR_CATEGORY_JUDGE_COUNT = 12;
	private static final int RHYTHMIC_PAIR_CATEGORY_JUDGE_COUNT = 11;
	private static final int ARTISTIC_GROUP_CATEGORY_JUDGE_COUNT = 11;

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship")
	public String listFirstPageChampionships(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageChampionships method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageChampionships method -EventManagerChampionshipController");

		return listAllChampionshipsByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/page/{pageNum}")
	public String listAllChampionshipsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location, HttpServletRequest request) {
		LOGGER.info("Entered listAllChampionshipsByPage method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		// List<Championship> listChampionships =
		// service.listAllChampionshipsByNotDeleted();
		Page<Championship> page = championshipService.listByEventPage(pageNum, sortField, sortDir, name, location,
				currentUser);

		List<Championship> listChampionship = championshipService.listAllChampionshipsForCurrentUser(currentUser);
		listChampionship = page.getContent();

		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

//		List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
//		List<User> listNonAssignedUsers = new ArrayList<>();
//		for (Integer user_id : listUsers) {
//			User user=null;
//			try {
//				user = userService.get(user_id);
//			} catch (UserNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			listNonAssignedUsers.add(user);
//		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/championship");
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
		// model.addAttribute("listNonAssignedEventManager", listNonAssignedUsers);
		model.addAttribute("listChampionships", listChampionship);
		model.addAttribute("pageTitle", "Manage Championships");
		// Championship championship = new Championship();
		// model.addAttribute("Championship", championship);
		LOGGER.info("Exit listAllChampionshipsByPage method -EventManagerChampionshipController");

		return "eventmanager/list_championships";
	}

//	@GetMapping("/championship")
//	public String listChampionship(Model model) {
//		LOGGER.info("Entered listChampionship EventManagerChampionshipController");
//		User currentUser = CommonUtil.getCurrentUser();
//		List<Championship> listChampionships = championshipService.listAllChampionshipsForCurrentUser(currentUser);
//		model.addAttribute("listChampionships", listChampionships);
//		model.addAttribute("pageTitle", "Select Championship");
//		return "eventmanager/list_championships";
//
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/selected-championship/{championshipId}")
	public String selectedChampionship(Model model, @PathVariable("championshipId") Integer championshipId,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered selectedChampionship method -EventManagerChampionshipController");
		Championship championship = championshipService.findById(championshipId);
		User currentUser = CommonUtil.getCurrentUser();
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {
			ChampionshipLink championshipLink = championshipLinkService.getByChampionship(championship);

			model.addAttribute("pageTitle", "Championship");
			model.addAttribute("championshipLink", championshipLink);
			LOGGER.info("Exit selectedChampionship method -EventManagerChampionshipController");
			return "eventmanager/championship_details";
		} else {
			return "error/403";
		}
	}

	// *************** EventManager Participant Menu Controller Starts
	// ***************

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/new/participant")
	public String showAddParticipantPage(Model model, @PathVariable("championshipId") Integer championshipId,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered showAddParticipantPage method -EventManagerChampionshipController");
		List<State> listStates = stateService.listAllStates();
		Participant participant = new Participant();
		Championship championship = championshipService.findById(championshipId);
		User currentUser = CommonUtil.getCurrentUser();
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {

			model.addAttribute("participant", participant);
			model.addAttribute("listStates", listStates);
			model.addAttribute("championship", championship);
			model.addAttribute("pageTitle", "Add Participant");
			LOGGER.info("Exit showAddParticipantPage method -EventManagerChampionshipController");
			return "eventmanager/add_participant";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/championship-participant")
	public String listFirstPageChampionshipParticipants(Model model,
			@PathVariable("championshipId") Integer championshipId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listFirstPageChampionshipParticipants method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		Championship championship = championshipService.findById(championshipId);

		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {

			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			LOGGER.info("Exit listFirstPageChampionshipParticipants method -EventManagerChampionshipController");
			return "redirect:/eventmanager/championship";

		}
		if (championship.getCreatedBy().equals(currentUser)) {

			return listAllEventManagerChampionshipParticipantsByPage(1, championshipId, model, "name", "asc", "", "",
					request, redirectAttributes);
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/championship-participant/page/{pageNum}")
	public String listAllEventManagerChampionshipParticipantsByPage(@PathVariable(name = "pageNum") int pageNum,
			@PathVariable("championshipId") Integer championshipId, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String prnNumber, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info(
				"Entered listAllEventManagerChampionshipParticipantsByPage methods -EventManagerParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		// List<State> listStates = stateService.listAllStates();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		Page<Participant> page = participantService.listByChampionshipParticipantsPage(pageNum, sortField, sortDir,
				name, prnNumber, championship);
		// List<ParticipantTeamParticipants> listParticipants
		// =participantTeamParticipantsService.findAllByChampionship(championship);
		List<Participant> listParticipants = page.getContent();

		long startCount = (pageNum - 1) * participantService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + participantService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/championship-participant");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", prnNumber);
		Participant participant = new Participant();
		model.addAttribute("listParticipants", listParticipants);
		model.addAttribute("pageTitle", "Championship Participants");
		model.addAttribute("championship", championship);
		model.addAttribute("participant", participant);
		LOGGER.info(
				"Exit listAllEventManagerChampionshipParticipantsByPage methods -EventManagerParticipantController");
		return "eventmanager/list_championship_participant";
	}

//	@GetMapping("/championship/{championshipId}/championship-participant")
//	public String showChampionshipParticipants(Model model, @PathVariable("championshipId") Integer championshipId) {
//		LOGGER.info("Entered showAddParticipantPage EventManagerChampionshipController");
//		List<State> listStates = stateService.listAllStates();
//		Championship championship = championshipService.findById(championshipId);
//		User currentUser = CommonUtil.getCurrentUser();
//		if (championship.getCreatedBy().equals(currentUser)) {
//			List<Participant> listParticipants = participantTeamParticipantsService.findAllByChampionship(championship);
//			model.addAttribute("listParticipants", listParticipants);
//			model.addAttribute("championship", championship);
//			model.addAttribute("pageTitle", "Championship Participants");
//
//			return "eventmanager/list_championship_participant";
//		} else {
//			return "error/403";
//		}
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/all-participant")
	public String listFirstPageParticipants(Model model, @PathVariable("championshipId") Integer championshipId,

			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listFirstPageParticipants methods -EventManagerParticipantController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		Championship championship = championshipService.findById(championshipId);

		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {

			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			LOGGER.info("Exit listFirstPageParticipants methods -EventManagerParticipantController");
			return "redirect:/eventmanager/championship";

		}
		if (championship.getCreatedBy().equals(currentUser)) {

			return listAllEventManagerParticipantsByPage(1, championshipId, model, "name", "asc", "", "", request,
					redirectAttributes);
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/all-participant/page/{pageNum}")
	public String listAllEventManagerParticipantsByPage(@PathVariable(name = "pageNum") int pageNum,
			@PathVariable("championshipId") Integer championshipId, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String prnNumber, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listAllEventManagerParticipantsByPage methods -EventManagerParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		// List<State> listStates = stateService.listAllStates();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		Page<Participant> page = participantService.listByPage(pageNum, sortField, sortDir, name, prnNumber);
		List<Participant> listParticipants = page.getContent();

		long startCount = (pageNum - 1) * participantService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + participantService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/all-participant");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", prnNumber);
		Participant participant = new Participant();
		model.addAttribute("listParticipants", listParticipants);
		model.addAttribute("pageTitle", "All Participants");
		model.addAttribute("championship", championship);
		model.addAttribute("participant", participant);
		LOGGER.info("Exit listAllEventManagerParticipantsByPage methods -EventManagerParticipantController");
		return "eventmanager/list_all_participant";
	}

//	@GetMapping("/championship/{championshipId}/all-participant")
//	public String showAllChampionshipParticipants(Model model, @PathVariable("championshipId") Integer championshipId) {
//		LOGGER.info("Entered showAllChampionshipParticipants EventManagerChampionshipController");
//		List<State> listStates = stateService.listAllStates();
//		Championship championship = championshipService.findById(championshipId);
//		User currentUser = CommonUtil.getCurrentUser();
//		if (championship.getCreatedBy().equals(currentUser)) {
//			List<Participant> listParticipants = participantService.listAll();
//			model.addAttribute("listParticipants", listParticipants);
//			model.addAttribute("championship", championship);
//			model.addAttribute("pageTitle", "All Participants");
//
//			return "eventmanager/list_all_participant";
//		} else {
//			return "error/403";
//		}
//	}
	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/new/participant/save")
	public String saveParticipant(@ModelAttribute("participant") @Valid Participant participant, Model model,
			@RequestParam(value = "participantImage", required = false) MultipartFile participantImage,
//				@RequestParam(value = "birthCertificateFile", required = false) MultipartFile birthCertificateFile,
//				@RequestParam(value = "medicalCertificateFile", required = false) MultipartFile medicalCertificateFile,
//				@RequestParam(value = "paymentReceiptFile", required = false) MultipartFile paymentReceiptFile,
			@PathVariable("championshipId") Integer championshipId, RedirectAttributes re, HttpServletRequest request)
			throws IOException, ParticipantNotFoundException, MessagingException, UserNotFoundException {

		LOGGER.info("Entered saveParticipant methods -EventManagerParticipantController");
		LOGGER.info("participantImage" + participantImage);
		LOGGER.info("participant Password" + participant.getPassword());
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			re.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {

			return "error/403";
		}

		if (!this.checkIfEditFlow(participant, participantImage)) {
			String participantImageFileName = null;
			String birthCertificateFileName = null;
			String medicalCertificateFileName = null;
			String paymentReceiptFileName = null;

			if (!participantImage.isEmpty()) {
				participantImageFileName = StringUtils.cleanPath(participantImage.getOriginalFilename());
				participant.setImage(participantImageFileName);

			} else {
				if (participant.getImage().isEmpty()) {
					participant.setImage(null);
				}
			}

//				if (!birthCertificateFile.isEmpty()) {
//					birthCertificateFileName = StringUtils.cleanPath(birthCertificateFile.getOriginalFilename());
//					participant.setBirthCertificate(birthCertificateFileName);
//				} else {
//					if (participant.getBirthCertificate().isEmpty()) {
//						participant.setBirthCertificate(null);
//					}
//				}
			//
//				if (!medicalCertificateFile.isEmpty()) {
//					medicalCertificateFileName = StringUtils.cleanPath(medicalCertificateFile.getOriginalFilename());
//					participant.setMedicalCertificate(medicalCertificateFileName);
//				} else {
//					if (participant.getMedicalCertificate().isEmpty()) {
//						participant.setMedicalCertificate(null);
//					}
//				}
			//
//				if (!paymentReceiptFile.isEmpty()) {
//					paymentReceiptFileName = StringUtils.cleanPath(paymentReceiptFile.getOriginalFilename());
//					participant.setPaymentReceipt(paymentReceiptFileName);
//				} else {
//					if (participant.getPaymentReceipt().isEmpty()) {
//						participant.setPaymentReceipt(null);
//					}
//				}
			participant.setEnabled(true);
			participant.setApprovalStatus(true);
			String number = commonUtil.getSystemGeneratedNumber();
			String participantPassword = "Prn$" + number;
			String encryptedPassword = passwordEncoder.encode(participantPassword);
			String prnNumberString = number;
			participant.setPrnNumber(number);
			participant.setPassword(encryptedPassword);
			participant.setUserName(prnNumberString);
			String participantUsername=participant.getUserName();
			String participantEmailId = participant.getEmail();
			String participantFullName = participant.getFullName();
			Setting PARTICIPANT_PRN_SUBJECT = settingService
					.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_SUBJECT");
			Setting PARTICIPANT_PRN_Content = settingService
					.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_CONTENT");
			Participant savedParticipant = participantService.save(participant);

			// send email
//			String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":"
//					+ request.getServerPort() + request.getContextPath();
			String token = userService.updateResetPasswordToken(participantUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, participantFullName, participantEmailId, participantPassword, prnNumberString,
					ROLE_PARTICIPANT, PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content, passwordLink);

			String uploadDir = "participant-reg-uploads/" + savedParticipant.getEmail();
			if (!participantImage.isEmpty()) {
				FileUploadUtil.saveFile(uploadDir, participantImageFileName, participantImage);
			}
//				if (!birthCertificateFile.isEmpty()) {
//					FileUploadUtil.saveFile(uploadDir, birthCertificateFileName, birthCertificateFile);
//				}
//				if (!medicalCertificateFile.isEmpty()) {
//					FileUploadUtil.saveFile(uploadDir, medicalCertificateFileName, medicalCertificateFile);
//				}
//				if (!paymentReceiptFile.isEmpty()) {
//					FileUploadUtil.saveFile(uploadDir, paymentReceiptFileName, paymentReceiptFile);
//				}

			re.addFlashAttribute("message",
					" The participant " + participant.getFullName() + " has been added successfully.");

			return "redirect:/eventmanager/championship/" + championshipId + "/all-participant";
		} else {

			participantService.save(participant);
			re.addFlashAttribute("message",
					" The participant " + participant.getFullName() + " been updated successfully.");
			LOGGER.info("Exit saveParticipant methods -EventManagerParticipantController");
			return "redirect:/eventmanager/championship/" + championshipId + "/all-participant";
		}

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/edit-participant/{id}")
	public String editParticipant(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered editParticipant methods -EventManagerParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Participant participant = participantService.getParticipantById(id);
			LOGGER.info(participant.toString());
			Championship championship = championshipService.findById(championshipId);
			if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
					|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
					|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
				return "redirect:/eventmanager/championship";
			}
			if (championship.getCreatedBy().equals(currentUser)) {

				List<State> listStates = stateService.listAllStates();
				List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
				model.addAttribute("participant", participant);
				model.addAttribute("listAgeCategory", listAgeCategory);
				model.addAttribute("pageTitle", "Edit Participant");
				model.addAttribute("championship", championship);
				model.addAttribute("listStates", listStates);
				participantService.save(participant);
				LOGGER.info("Entered editParticipant methods -EventManagerParticipantController");
				return "eventmanager/add_participant";
			} else {
				return "error/403";
			}
		} catch (ParticipantNotFoundException ex) {
			LOGGER.error("Participant not found!");
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/eventmanager/allparticipant";
		}
	}

	private boolean checkIfEditFlow(Participant participant, MultipartFile participantImage) {
//		if (participantImage == null || birthCertificateFile == null || medicalCertificateFile == null
//				|| paymentReceiptFile == null) {
		if (participant.getId() != null) {
			return true;
		}
		return false;
	}

	// *************** EventManager Participant Menu Controller Ends ***************

	// *************** EventManager Team Menu Controller Starts ***************

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/new/team")
	public String showAddTeamPage(Model model, @PathVariable("championshipId") Integer championshipId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered showAddTeamPage method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam = new ParticipantTeam();
		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		if (championship.getCreatedBy().equals(currentUser)) {

			model.addAttribute("participantTeam", participantTeam);
			model.addAttribute("pageTitle", "Add Team");
			LOGGER.info("Exit showAddTeamPage method -EventManagerChampionshipController");

			return "eventmanager/team_form";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/new/team/save")
	public String saveParticipantTeam(Model model, ParticipantTeam participantTeam,
			@PathVariable("championshipId") Integer championshipId, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException {
		LOGGER.info("Entered saveParticipantTeam method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {

			ChampionshipCategory championshipCategory = championshipCategoryService
					.getChampionshipCategoryForAllConditions(participantTeam.getChampionship().getId(),
							participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
							participantTeam.getGender().toString());

			List<ChampionshipRounds> listChampionshipRounds = championshipRoundsService
					.findByChampionshipAndChampionshipCategory(participantTeam.getChampionship(), championshipCategory);

			List<ChampionshipRounds> listFilteredChampionshipRoundsWithCompletedStatus = listChampionshipRounds.stream()
					.filter(championshipRounds -> championshipRounds.getStatus().equals(RoundStatusEnum.COMPLETED))
					.collect(Collectors.toList());
			if (!listFilteredChampionshipRoundsWithCompletedStatus.isEmpty()) {
				redirectAttributes.addFlashAttribute("message1",
						"Unable to create Teams. Championship Round is completed.");
				return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";

			}
			model.addAttribute("championship", championship);
			participantTeam.setStatus(RegistrationStatusEnum.APPROVED);
			Boolean teamChestNumber = participantTeamService.checkTeamChestNumber(participantTeam.getChestNumber(),
					participantTeam.getChampionship());
			boolean isUpdatingTeam = (participantTeam.getId() != null);

			if (participantTeam.getId() != null) {
				ParticipantTeam existingParticipantTeam = participantTeamService.findById(participantTeam.getId());
				Boolean teamEditChestNumber = participantTeamService.checkTeamChestNumberForEdit(
						participantTeam.getChestNumber(), participantTeam.getChampionship(), participantTeam.getId());
				if (teamEditChestNumber) {
					redirectAttributes.addFlashAttribute("message1", "The Team with the chest number "
							+ participantTeam.getChestNumber() + " is already present.");
					return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
				}

				boolean existingStatus = existingParticipantTeam.isDifferentAsanasForParticipants();

				List<ParticipantTeamParticipants> listAssignedParticipants = existingParticipantTeam
						.getParticipantTeamParticipants();
				if (listAssignedParticipants.size() != 0) {
					if (participantTeam.isDifferentAsanasForParticipants() == existingStatus) {
						participantTeam.setDifferentAsanasForParticipants(existingStatus);
					}
//					else {
//						participantTeam.setDifferentAsanasForParticipants(existingStatus);
//						redirectAttributes.addFlashAttribute("errorMessage",
//								"You cannot change the status of the field Different Asanas!");
//					}

				}
			}

			if (isUpdatingTeam) {
				try {
					participantTeamService.save(participantTeam);
				} catch (Exception e) {
					redirectAttributes.addFlashAttribute("message1", "Unable to edit team.");
					return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/"
							+ +participantTeam.getId();
				}
				redirectAttributes.addFlashAttribute("message",
						"The Team " + participantTeam.getTeamName() + " has been updated successfully.");
				return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/"
						+ +participantTeam.getId();
			} else {
				if (teamChestNumber) {
					redirectAttributes.addFlashAttribute("message1",
							"The Team " + participantTeam.getName() + " is already present.");
					return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
				} else {
					try {
						participantTeamService.save(participantTeam);
					} catch (Exception e) {
						redirectAttributes.addFlashAttribute("message1", "Unable to edit team.");
						return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/"
								+ +participantTeam.getId();
					}
				}

			}

			// participantTeamService.save(participantTeam);
			redirectAttributes.addFlashAttribute("message",
					"The Team  " + participantTeam.getTeamName() + " has been saved successfully.");
			LOGGER.info("Redirected to manage-team/edit/participantTeamID page");
			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + +participantTeam.getId();

		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/all-teams")
	public String listFirstPageJudges(Model model, HttpServletRequest request,

			@PathVariable("championshipId") Integer championshipId, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listFirstPageJudges method -EventManagerChampionshipController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		Championship championship = championshipService.findById(championshipId);
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {
			return listAllTeamsByPage(1, championshipId, model, "name", "asc", "", "", "", request, redirectAttributes);
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/all-teams/page/{pageNum}")
	public String listAllTeamsByPage(@PathVariable(name = "pageNum") int pageNum,
			@PathVariable("championshipId") Integer championshipId, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String chestNumber,

			@RequestParam(name = "keyword3", required = false) String asanaCategory, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listAllTeamsByPage method -EventManagerChampionshipController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		Page<ParticipantTeam> page = participantTeamService.listByEventManagerPage(championshipId, pageNum, sortField,
				sortDir, name, chestNumber, asanaCategory, championship.getName());
		List<ParticipantTeam> listParticipantTeam = new ArrayList<>();
		listParticipantTeam = page.getContent();

		long startCount = (pageNum - 1) * participantTeamService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + participantTeamService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/all-teams");
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
		model.addAttribute("keyword3", asanaCategory);
		model.addAttribute("championship", championship);
		ParticipantTeam participantTeam = new ParticipantTeam();
		model.addAttribute("listParticipantTeam", listParticipantTeam);
		model.addAttribute("pageTitle", "Manage Teams");
		model.addAttribute("participantTeam", participantTeam);
		LOGGER.info("Exit listAllTeamsByPage method -EventManagerChampionshipController");

		return "eventmanager/list_championship_team";
	}

//	@GetMapping("/championship/{championshipId}/all-teams")
//	public String listAllParticipantTeams(Model model, @PathVariable("championshipId") Integer championshipId,
//			HttpServletRequest request) {
//		LOGGER.info("Entered listAllParticipantTeams controller");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.findById(championshipId);
//		if (championship.getCreatedBy().equals(currentUser)) {
//
//			List<ParticipantTeam> listParticipantTeams = participantTeamService
//					.listAllParticipantTeamsForChampionship(championship);
//			model.addAttribute("listParticipantTeams", listParticipantTeams);
//			model.addAttribute("championship", championship);
//			model.addAttribute("pageTitle", "Manage Teams");
//			LOGGER.info("Redirected to manage_team page");
//			return "eventmanager/list_championship_team";
//		} else {
//			return "error/403";
//		}
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/edit-team/{id}")
	public String editParticipantTeam(@PathVariable(name = "id") Integer id,
			@PathVariable("championshipId") Integer championshipId, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered editParticipantTeam method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			ParticipantTeam participantTeam = participantTeamService.get(id);
			Championship championship = championshipService.findById(championshipId);
			if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
					|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
					|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
				return "redirect:/eventmanager/championship";
			}
			if (championship.getCreatedBy().equals(currentUser)) {

				List<Participant> listParticipants = participantService
						.listAllParticipantsForGender(participantTeam.getGender().toString());

				List<ParticipantTeamParticipants> listAssignedParticipants = participantTeamParticipantsService
						.getByTeamOrderBySequenceNumberAsc(participantTeam);
				LOGGER.info("listAssignedParticipants :" + listAssignedParticipants.size());

				List<Participant> listNonAssignedParticipants = participantService
						.listAllParticipantsForGender(participantTeam);

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
				model.addAttribute("championship", championship);
				model.addAttribute("listAllAsanas", listAllAsanas);
				model.addAttribute("listAssignedParticipants", listAssignedParticipants);
				model.addAttribute("listAssignedReferees", listAssignedReferees);
				model.addAttribute("listNonAssignedParticipants", listNonAssignedParticipants);
				model.addAttribute("listRoundWiseParticipantTeamAsanas", listRoundWiseParticipantTeamAsanas);
				model.addAttribute("listRoundWiseParticipantTeamReferees", listRoundWiseParticipantTeamReferees);
				model.addAttribute("noOfRounds", noOfRounds);

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
				model.addAttribute("championship", championship);
				model.addAttribute("participantsLimit", participantsLimit);
				model.addAttribute("asanasLimit", limit);
				LOGGER.info("Redirected to team_form page");
				LOGGER.info("Exit editParticipantTeam method -EventManagerChampionshipController");

				return "eventmanager/team_form";
			} else {
				return "error/403";
			}
		} catch (ParticipantTeamNotFoundException ex) {
			LOGGER.error("ParticipantTeam not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", "Team not found.");
			return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
		}
	}

	// Add participants to team
	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-team/participants/assign/{teamId}")
	public String assignTeamParticipants(@RequestParam(value = "participants", required = false) Integer[] participants,
			@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "teamId") Integer teamId, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered assignTeamParticipants method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {

			return "error/403";
		}
		ParticipantTeam participantTeam = participantTeamService.findById(teamId);
		AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
		int count = participantTeam.getParticipantTeamParticipants().size();

		Integer participantsLimit = championshipCategoryService.getChampionshipCategoryParticipantsForChampionshipId(
				participantTeam.getChampionship(), participantTeam.getAsanaCategory(), participantTeam.getCategory(),
				participantTeam.getGender());
		LOGGER.info("noOfParticipants :" + participantsLimit);

		String errorMessage = null;
		String message = null;
		if ((count + participants.length) > participantsLimit) {
			errorMessage = "Only " + participantsLimit + " participant can be added for this category";
		} else if ((count + participants.length) <= participantsLimit) {
			message = addParticipantsToTeam(participants, participantTeam);
		}

		redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Redirected to manage-team/edit page");
		LOGGER.info("Exit assignTeamParticipants method -EventManagerChampionshipController");
		return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + teamId;

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-team/participants/delete/{participantTeamParticipantsId}")
	public String deleteAssignedTeamParticipants(@PathVariable(name = "participantTeamParticipantsId") Integer id,
			@PathVariable("championshipId") Integer championshipId, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ParticipantTeamNotFoundException,
			ParticipantTeamParticipantsNotFoundException, ParticipantTeamAsanasNotFoundException {
		LOGGER.info("Entered deleteAssignedTeamParticipants method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		ParticipantTeamParticipants participantTeamParticipants = participantTeamParticipantsService.get(id);
		ParticipantTeam participantTeam = participantTeamParticipants.getParticipantTeam();

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

		if (!listParticipantTeamParticipantAsanasStatus.isEmpty()) {
			for (ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus : listParticipantTeamParticipantAsanasStatus) {
				participantTeamParticipantAsanasStatusService.delete(participantTeamParticipantAsanasStatus.getId());
			}
		}
		participantTeamParticipantsService.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "Participant removed Successfully");
		LOGGER.info("Redirected to manage-team/edit page");
		return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeamId;

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-team/asanas/load/{participantTeam_id}/round/{round_type}")
	public String loadTeamRoundAsanas(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("round_type") Integer round_type, @PathVariable("championshipId") Integer championshipId,
			Model model, RedirectAttributes ra, HttpServletRequest request) {
		LOGGER.info("Entered loadTeamRoundAsanas method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {

			return "error/403";
		}
		model.addAttribute("participantTeam_id", participantTeam_id);
		model.addAttribute("championship", championship);

		try {
			ParticipantTeam participantTeam = participantTeamService.get(participantTeam_id);
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
			return "eventmanager/team_round_asanas_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!");
			ra.addFlashAttribute("message", e.getMessage());
			LOGGER.info("Exit loadTeamRoundAsanas method -EventManagerChampionshipController");
			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-team/asanas/assign/{participantTeam_id}")
	public String assignTeamAsanas(
			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
			@RequestParam(value = "round", required = true) Integer round,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
			@PathVariable("championshipId") Integer championshipId, RedirectAttributes ra, HttpServletRequest request) {
		LOGGER.info("Entered assignTeamAsanas method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}

		// function when common asanas are assigned to the team participants.
		ParticipantTeam participantTeam = participantTeamService.findById(participantTeam_id);

		// Check if asana round status is frozen
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = participantTeamParticipantAsanasStatusService
				.findByParticipantTeamAndRound(participantTeam, round);
		if (existingTeamAsanasStatus != null
				&& existingTeamAsanasStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE)) {
			LOGGER.info("Asanas are already forzen");
			LOGGER.info("Redirected to manage-team/edit page");
			LOGGER.info("Exit assignTeamAsanas method -EventManagerChampionshipController");
			ra.addFlashAttribute("message", "Unable to add asanas. Asanas are frozen.");
			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
		}
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
				if (listAssignedParticipants.size() == 0) {
					ra.addFlashAttribute("errorMessage", "Add participant to the team before adding asanas");
					LOGGER.info("Redirected to manage-team/edit page");
					return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/"
							+ participantTeam.getId();
				}
				if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
					if (listAssignedParticipants.size() != RHYTHMIC_PAIR_PARTICIPANT_COUNT) {
						ra.addFlashAttribute("errorMessage", "Add 2 participants to the team before adding asanas");
						LOGGER.info("Redirected to manage-team/edit page");
						return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/"
								+ participantTeam.getId();
					}
				}

				if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
					if (listAssignedParticipants.size() != ARTISTIC_GROUP_PARTICIPANT_COUNT) {
						ra.addFlashAttribute("errorMessage", "Add 5 participants to the team before adding asanas");
						LOGGER.info("Redirected to manage-team/edit page");
						return "redirect:/eventmanager/championship/+championshipId+/edit-team/"
								+ participantTeam.getId();
					}
				}

				for (ParticipantTeamParticipants participantTeamParticipant : listAssignedParticipants) {
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
		LOGGER.info("Exit assignTeamAsanas method -EventManagerChampionshipController");

		return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-team/participant-asanas/load/{participantTeam_id}/{participantTeamParticipant_id}/round/{round_type}")
	public String loadRoundAsanasForParticipant(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("participantTeamParticipant_id") Integer participantTeamParticipant_id,
			@PathVariable("round_type") Integer round_type, @PathVariable("championshipId") Integer championshipId,
			Model model, RedirectAttributes ra, HttpServletRequest request) {
		LOGGER.info("Entered loadRoundAsanasForParticipant method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();

		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);
		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		try {
			ParticipantTeam participantTeam = participantTeamService.get(participantTeam_id);
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

			// roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount;
			roundOptionalAsanasLimit = totalAsanasLimit - roundCompulsoryAsanasCount - countOptionalAssignedAsanas;
			model.addAttribute("participantTeamParticipant_id", participantTeamParticipant_id);
			model.addAttribute("totalAsanasLimit", totalAsanasLimit);
			model.addAttribute("roundOptionalAsanasLimit", roundOptionalAsanasLimit);

			LOGGER.info("Exit loadRoundAsanasForParticipant method -EventManagerChampionshipController");
			return "eventmanager/team_participant_round_asanas_modal";

		} catch (ParticipantTeamNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-team/participant-asanas/assign/{participantTeam_id}")
	public String assignTeamParticipantRoundAsanas(
			@RequestParam(value = "participantTeamAsanas", required = false) Integer[] participantTeamAsanas,
			@RequestParam(value = "round", required = true) Integer round,
			@RequestParam(value = "participantTeamParticipant_id", required = true) Integer participantTeamParticipant_id,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
			@PathVariable("championshipId") Integer championshipId, RedirectAttributes ra, HttpServletRequest request) {
		LOGGER.info("Entered assignTeamParticipantRoundAsanas method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		ParticipantTeam participantTeam = participantTeamService.findById(participantTeam_id);
		// Check if asana round status is frozen
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = participantTeamParticipantAsanasStatusService
				.findByParticipantTeamAndRound(participantTeam, round);
		if (existingTeamAsanasStatus != null
				&& existingTeamAsanasStatus.getAsanaStatus().equals(AsanaStatusEnum.FREEZE)) {
			LOGGER.info("Asanas are already forzen");
			ra.addFlashAttribute("message", "Unable to add asanas. Asanas are frozen.");
			LOGGER.info("Exit assignTeamParticipantRoundAsanas method -EventManagerChampionshipController");
			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
		}

		AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
		String asanaCategoryName = participantTeam.getAsanaCategoryName();

		ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
				.get(participantTeamParticipant_id);

		if (participantTeamParticipant == null) {
			ra.addFlashAttribute("errorMessage", "Add participant to the team before adding asanas");
			LOGGER.info("Redirected to manage-team/edit page");
			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
		}

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
		LOGGER.info("Exit assignTeamParticipantRoundAsanas method -EventManagerChampionshipController");
		return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-team/asanas/delete/{participantTeamAsanasId}")
	public String deleteAssignedTeamAsanas(@PathVariable(name = "participantTeamAsanasId") Integer id,
			@PathVariable("championshipId") Integer championshipId, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamAsanasNotFoundException {
		LOGGER.info("Entered deleteAssignedTeamAsanas method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeamAsanas participantTeamAsanas = participantTeamAsanasService.get(id);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
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
			redirectAttributes.addFlashAttribute("message", "Unable to delete asanas. Asanas are frozen.");
			LOGGER.info("Exit assignTeamParticipantRoundAsanas method -EventManagerChampionshipController");
			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeamId;
		}

		if (participantTeam.isDifferentAsanasForParticipants() == false) { // common asanas for all participants
			List<ParticipantTeamAsanas> listCommonAssignedAsanasForParticpants = participantTeamAsanasService
					.getAllByAsanaAndTeamAndRoundNumber(participantTeamAsanas.getAsana(), participantTeam, participantTeamAsanas.getRoundNumber());
			for (ParticipantTeamAsanas teamAsanas : listCommonAssignedAsanasForParticpants) {
				participantTeamAsanasService.deleteById(teamAsanas.getId());
			}
		} else if (participantTeam.isDifferentAsanasForParticipants() == true) { // if different asanas for all
																					// participants
			participantTeamAsanasService.deleteById(id);
		}

		redirectAttributes.addFlashAttribute("message", "Asanas removed Successfully");
		LOGGER.info("Exit deleteAssignedTeamAsanas method -EventManagerChampionshipController");
		return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeamId;
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-team/referees/assign/{participantTeam_id}/round/{round}")
	public String loadTeamRefereePanels(@PathVariable("participantTeam_id") Integer participantTeam_id,
			@PathVariable("round") Integer round, @PathVariable("championshipId") Integer championshipId, Model model,
			RedirectAttributes ra, HttpServletRequest request) {
		LOGGER.info("Entered loadTeamRefereePanels method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("participantTeam_id", participantTeam_id);
		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		try {
			ParticipantTeam participantTeam = participantTeamService.get(participantTeam_id);
			List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
					.getRefereePanelsForChampionship(participantTeam.getChampionship(),
							participantTeam.getAsanaCategory());
			model.addAttribute("participantTeam", participantTeam);
			model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
			model.addAttribute("round", round);
			LOGGER.info("Exit loadTeamRefereePanels method -EventManagerChampionshipController");

			return "eventmanager/team_round_referees_modal";

		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.error("ParticipantTeam not found!");
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
		}

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-team/referees/assign/{participantTeam_id}/round/{round}")
	public String assignTeamReferees(@RequestParam(name = "panel") Integer championshipRefereePanelId,
			@PathVariable(name = "participantTeam_id") Integer participantTeam_id,
			@PathVariable(name = "round") Integer round, @PathVariable("championshipId") Integer championshipId,
			RedirectAttributes ra, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered assignTeamReferees method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		ParticipantTeam participantTeam = participantTeamService.findById(participantTeam_id);
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
					return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;
				}
			}

			participantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
			participantTeamPanelService.save(participantTeamPanel);

		} else {
			ParticipantTeamPanel newParticipantTeamPanel = new ParticipantTeamPanel();
			newParticipantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
			newParticipantTeamPanel.setParticipantTeamId(participantTeam_id);
			newParticipantTeamPanel.setRoundNumber(round);
			participantTeamPanelService.save(newParticipantTeamPanel);
		}

		participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam, round);
		LOGGER.info("deleteTeamReferresForParticipantTeam Successfully deleted");
		ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				championship.getId(), participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
				participantTeam.getGender().toString());
		List<RefereesPanels> listRefereesPanels = new ArrayList<>();
		try {
			listRefereesPanels = championshipRefereePanelsService.get(championshipRefereePanelId).getRefereesPanels();
		} catch (ChampionshipRefereePanelsNotFoundException e) {

			e.printStackTrace();
		}
//		List<JudgeType> listAssignedUserReferees = new ArrayList<JudgeType>();
//			for (RefereesPanels referee : listRefereesPanels) {
//				listAssignedUserReferees.add(referee.getType());
//			}
//			LOGGER.info("listAssignedUserReferees" + listAssignedUserReferees.toArray());
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
		LOGGER.info("Redirected to manage-team/edit page");
		return "redirect:/eventmanager/championship/" + championshipId + "/edit-team/" + participantTeam_id;

	}

	private String addParticipantsToTeam(Integer[] participants, ParticipantTeam participantTeam) {

		List<Participant> listAssignedParticipants = new ArrayList<>();
		if (participants != null) {
			for (Integer assignParticipantId : participants) {
				try {
					Participant participant = participantService.getParticipantById(assignParticipantId);
					LOGGER.info("assignParticipantId : " + assignParticipantId);
					ParticipantTeamParticipants participantTeamParticipants = new ParticipantTeamParticipants();
					participantTeamParticipants.setParticipantTeam(participantTeam);
					participantTeamParticipants.setParticipant(participant);
					// participantTeamParticipants.setIsTeamLead(false);
					ParticipantTeamParticipants savedParticipantTeamParticipants = participantTeamParticipantsService
							.save(participantTeamParticipants);

					// assign compulsory asanas only when creating new group
					participantTeamService.assignCompulsoryAsanasForTeam(participantTeam,
							savedParticipantTeamParticipants);
					AsanaCategory asanaCategory = participantTeam.getAsanaCategory();
					if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
							|| asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
							|| asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
						if (participantTeam.isDifferentAsanasForParticipants() == false) {
							// if not different asanas, add optional asanas which are added to others
							// already to the new participant.
							participantTeamService.assignOptionalAsanasForNewParticipant(participantTeam,
									savedParticipantTeamParticipants);
						}
					}

				} catch (ParticipantNotFoundException e) {
					LOGGER.error("ParticipantTeam not found!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		LOGGER.info("Exit assignTeamReferees method -EventManagerChampionshipController");
		return "Participants added successfully";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-team/delete/{id}")
	public String deleteParticipantTeam(@PathVariable(name = "id") Integer id,
			@PathVariable("championshipId") Integer championshipId, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered deleteParticipantTeam method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		try {
			ParticipantTeam participantTeam = participantTeamService.get(id);
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
				return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
			}

			if (!participantTeamParticipants.isEmpty()) {
				System.out.println("participantTeamParticipants Team");
				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
						+ " is allocated to team participants, Please remove before you delete");
				return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
			}

			if (!participantTeamReferees.isEmpty()) {
				System.out.println("participantTeamReferees Team");
				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
						+ " is alocated to team referees, Please remove before you delete");
				return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
			}

			if (!participantTeamReferees.isEmpty()) {
				System.out.println("participantTeamReferees Team");
				redirectAttributes.addFlashAttribute("message1", "The Team " + participantTeam.getTeamName()
						+ " is alocated to team referees, Please remove before you delete");
				return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
			}

			participantTeamService.delete(id);
			participantTeamService
					.updateNumberOfParticipantTeams(participantTeamService.getChampionshipRoundId(participantTeam));
			redirectAttributes.addFlashAttribute("message",
					"The Team " + participantTeam.getName() + " has been deleted successfully");

		} catch (ParticipantTeamNotFoundException ex) {
			LOGGER.error("ParticipantTeam not found!");
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteParticipantTeam method -EventManagerChampionshipController");
		return "redirect:/eventmanager/championship/" + championshipId + "/all-teams";
	}

	// **************** EventManager Team Menu Controller Ends ***************

	// **************** EventManager Age Category Controller Starts ***************

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/manage-agecategory")
	public String listFirstPageAsanaCategory(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageAsanaCategory method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("currentUser", currentUser);
		LOGGER.info("Exit listFirstPageAsanaCategory method -EventManagerChampionshipController");
		return listAllAgeCategoriesByPage(1, model, "title", "asc", "", "", request);
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/manage-agecategory/page/{pageNum}")
	public String listAllAgeCategoriesByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String title,
			@RequestParam(name = "keyword2", required = false) String code, HttpServletRequest request) {
		LOGGER.info("Entered listAllAgeCategoriesByPage method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		model.addAttribute("currentUser", currentUser);

		Page<AgeCategory> page = ageCategoryService.listByPage(pageNum, sortField, sortDir, title, code);
		List<AgeCategory> listAgeCategories = page.getContent();

		long startCount = (pageNum - 1) * ageCategoryService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + ageCategoryService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/manage-agecategory");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", title);
		model.addAttribute("keyword2", code);
		AgeCategory ageCategory = new AgeCategory();
		model.addAttribute("listAgeCategories", listAgeCategories);
		model.addAttribute("pageTitle", "Manage Age Categories");
		model.addAttribute("ageCategory", ageCategory);
		LOGGER.info("Exit listAllAgeCategoriesByPage method -EventManagerChampionshipController");
		return "eventmanager/manage_age_category";
	}

	/*
	 * public String listAllAgeCategories(Model model, HttpServletRequest request) {
	 * 
	 * LOGGER.info("Entered listAllAgeCategories ManageAgeCategoryController"); //
	 * For Navigation history String mappedUrl = request.getRequestURI(); User
	 * currentUser = CommonUtil.getCurrentUser();
	 * CommonUtil.setNavigationHistory(mappedUrl, currentUser); List<AgeCategory>
	 * listAgeCategories = ageCategoryService.listAllAgeCategories();
	 * model.addAttribute("listAgeCategories", listAgeCategories);
	 * model.addAttribute("pageTitle", "Manage Age Categories");
	 * model.addAttribute("currentUser", currentUser); return
	 * "eventmanager/manage_age_category"; }
	 */

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("manage-agecategory/new")
	public String newAgeCategory(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newAgeCategory method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		AgeCategory ageCategory = new AgeCategory();
		ageCategory.setEnabled(true);
		model.addAttribute("ageCategory", ageCategory);
		model.addAttribute("pageTitle", "Add Age Category");
		LOGGER.info("Exit newAgeCategory method -EventManagerChampionshipController");
		return "eventmanager/age_category_form";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/manage-agecategory/save")
	public String saveAgeCategory(AgeCategory ageCategory, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException {
		LOGGER.info("Entered saveAgeCategory method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		AgeCategory checkAgeCategory = ageCategoryService.getAgeCategoryByTitle(ageCategory.getTitle());
		String message = "";
		String errorMessage = "";

		if (ageCategory.getId() == null) { // if saving new record
			if (checkAgeCategory == null) {
				message = "The Age Category " + ageCategory.getTitle() + "  has been saved successfully!";
				ageCategoryService.save(ageCategory);
				ageCategoryService.saveExecutionScoresForAgeCategory(ageCategory);
				redirectAttributes.addFlashAttribute("message", message);
			} else {
				errorMessage = "The Age Category already exists!";
				redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			}

		} else {

			try {

				AgeCategory existingAgeCategory = ageCategoryService.get(ageCategory.getId());
				if (checkAgeCategory != null) {
					if (checkAgeCategory.getTitle().equals(ageCategory.getTitle())
							&& checkAgeCategory.getId().equals(ageCategory.getId())) {
						existingAgeCategory.setCode(ageCategory.getCode());
						existingAgeCategory.setAgeAbove(ageCategory.getAgeAbove());
						existingAgeCategory.setAgeBelow(ageCategory.getAgeBelow());
						existingAgeCategory.setEnabled(ageCategory.isEnabled());
						ageCategoryService.save(existingAgeCategory);
						message = "The Age category has been updated successfully!";
						redirectAttributes.addFlashAttribute("message", message);

					} else if (!checkAgeCategory.getTitle().equals(ageCategory.getTitle())) {
						existingAgeCategory.setTitle(ageCategory.getTitle());
						existingAgeCategory.setCode(ageCategory.getCode());
						existingAgeCategory.setAgeAbove(ageCategory.getAgeAbove());
						existingAgeCategory.setAgeBelow(ageCategory.getAgeBelow());
						existingAgeCategory.setEnabled(ageCategory.isEnabled());
						message = "The Age category has been updated successfully!";
						ageCategoryService.save(existingAgeCategory);
						redirectAttributes.addFlashAttribute("message", message);

					} else {
						errorMessage = "The age category already exists!";
						redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
					}
				} else {
					existingAgeCategory.setTitle(ageCategory.getTitle());
					existingAgeCategory.setCode(ageCategory.getCode());
					existingAgeCategory.setAgeAbove(ageCategory.getAgeAbove());
					existingAgeCategory.setAgeBelow(ageCategory.getAgeBelow());
					existingAgeCategory.setEnabled(ageCategory.isEnabled());
					message = "The Age category " + ageCategory.getTitle() + " has been updated successfully!";
					ageCategoryService.save(existingAgeCategory);
					redirectAttributes.addFlashAttribute("message", message);
				}

			} catch (EntityNotFoundException e) {
				LOGGER.error("No age category found for the id " + ageCategory.getId());
			}
		}
		LOGGER.info("Exit saveAgeCategory method -EventManagerChampionshipController");
		return "redirect:/eventmanager/manage-agecategory";

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/manage-agecategory/edit/{id}")
	public String editAgeCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered editAgeCategory method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			AgeCategory ageCategory = ageCategoryService.get(id);
			model.addAttribute("ageCategory", ageCategory);
			model.addAttribute("pageTitle", "Edit Age Category");
			return "eventmanager/age_category_form";
		} catch (EntityNotFoundException ex) {
			LOGGER.error("Age category with id" + id + "not found");
			redirectAttributes.addFlashAttribute("errorMessage", "Age category with id" + id + "not found");
			LOGGER.info("Exit editAgeCategory method -EventManagerChampionshipController");
			return "redirect:/eventmanager/manage-agecategory";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/manage-agecategory/{id}/enabled/{status}")
	public String updateAgeCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EntityNotFoundException {
		LOGGER.info("Entered updateAgeCategoryEnabledStatus method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		AgeCategory ageCategory = ageCategoryService.get(id);

		ageCategoryService.updateAgeCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Age category " + ageCategory.getTitle() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateAgeCategoryEnabledStatus method -EventManagerChampionshipController");
		return "redirect:/eventmanager/manage-agecategory";
	}

	// **************** EventManager Age Category Controller Ends ***************

	// **************** EventManager Compulsory Asanas Controller Starts
	// ***************

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-round-asanas")
	public String listFirstPageCompulsoryRoundAsanas(Model model, HttpServletRequest request,

			@PathVariable("championshipId") Integer championshipId, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listFirstPageCompulsoryRoundAsanas method -EventManagerChampionshipController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		Championship championship = championshipService.findById(championshipId);
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		if (championship.getCreatedBy().equals(currentUser)) {
			return listAllCompulsoryRoundAsanasByPage(1, championshipId, model, "asana", "asc", "", "", "", request,
					redirectAttributes);
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-round-asanas/page/{pageNum}")
	public String listAllCompulsoryRoundAsanasByPage(@PathVariable(name = "pageNum") int pageNum,
			@PathVariable("championshipId") Integer championshipId, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String asanaCategory,
			@RequestParam(name = "keyword2", required = false) String category,
			@RequestParam(name = "keyword3", required = false) String championshipName, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listAllCompulsoryRoundAsanasByPage method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		Championship championship = championshipService.findById(championshipId);

		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {

			// List<Championship> listChampionships =
			// service.listAllChampionshipsByNotDeleted();
			Page<CompulsoryRoundAsanas> page = compulsoryRoundAsanasService.listByPage(championshipId, pageNum,
					sortField, sortDir, asanaCategory, category, championship.getName());
			List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas = page.getContent();

			long startCount = (pageNum - 1) * compulsoryRoundAsanasService.RECORDS_PER_PAGE + 1;
			long endCount = startCount + compulsoryRoundAsanasService.RECORDS_PER_PAGE - 1;
			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}
			/*
			 * List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
			 * List<User> listNonAssignedUsers = new ArrayList<>(); for (Integer user_id :
			 * listUsers) { User user=null; try { user = userService.get(user_id); } catch
			 * (UserNotFoundException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } listNonAssignedUsers.add(user); }
			 */

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
			model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/manage-round-asanas");
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", reverseSortDir);
			model.addAttribute("keyword1", asanaCategory);
			model.addAttribute("keyword2", category);
			model.addAttribute("keyword3", championshipName);
			// model.addAttribute("keyword2", roundNumber);
			// model.addAttribute("listNonAssignedEventManager", listNonAssignedUsers);
			model.addAttribute("championship", championship);

			model.addAttribute("listCompulsoryRoundAsanas", listCompulsoryRoundAsanas);
			model.addAttribute("pageTitle", "Manage Compulsory Round Asanas");
			CompulsoryRoundAsanas compulsoryRoundAsana = new CompulsoryRoundAsanas();
			model.addAttribute("compulsoryRoundAsana", compulsoryRoundAsana);
			LOGGER.info("Exit listAllCompulsoryRoundAsanasByPage method -EventManagerChampionshipController");

			return "eventmanager/manage_round_asanas";
		} else {
			return "error/403";
		}
	}
	/*
	 * public String listAllCompulsoryRoundAsanas(Model
	 * model, @PathVariable("championshipId") Integer championshipId,
	 * HttpServletRequest request) {
	 * 
	 * LOGGER.
	 * info("Entered listAllCompulsoryRoundAsanas ManageCompulsoryRoundAsanasController"
	 * ); // For Navigation history String mappedUrl = request.getRequestURI(); User
	 * currentUser = CommonUtil.getCurrentUser();
	 * CommonUtil.setNavigationHistory(mappedUrl, currentUser); Championship
	 * championship = championshipService.findById(championshipId); if
	 * (championship.getCreatedBy().equals(currentUser)) {
	 * 
	 * List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas =
	 * compulsoryRoundAsanasService
	 * .getCompulsoryAsanasByChampionship(championship);
	 * model.addAttribute("listCompulsoryRoundAsanas", listCompulsoryRoundAsanas);
	 * model.addAttribute("championship", championship);
	 * model.addAttribute("pageTitle", "Manage Compulsory Round Asanas"); return
	 * "eventmanager/manage_round_asanas"; } else { return "error/403"; } }
	 */

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-round-asanas/new")
	public String newRoundAsana(Model model, @PathVariable("championshipId") Integer championshipId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered newRoundAsana method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {

			CompulsoryRoundAsanas compulsoryRoundAsana = new CompulsoryRoundAsanas();
			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
			List<Asana> listAsanas = asanaService.listAllAsanas();
			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
					.listAllAsanaExecutionScores();

			List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();
			for (Asana asana : listAsanas) {
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

			compulsoryRoundAsana.setCompulsory(true);

			model.addAttribute("compulsoryRoundAsana", compulsoryRoundAsana);
			model.addAttribute("pageTitle", "Add Compulsory Round Asana");
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("championship", championship);
			model.addAttribute("listAsanas", listAsanasWithCode);
			LOGGER.info("Exit newRoundAsana method -EventManagerChampionshipController");

			return "eventmanager/compulsory_round_asana_form";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-round-asanas/save")
	public String saveRoundAsana(CompulsoryRoundAsanas compulsoryRoundAsana,
			@PathVariable("championshipId") Integer championshipId, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException, NumberFormatException, AsanaNotFoundException {

		LOGGER.info("Entered saveRoundAsana method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		ChampionshipCategory championshiCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				compulsoryRoundAsana.getChampionship().getId(), compulsoryRoundAsana.getAsanaCategory().getId(),
				compulsoryRoundAsana.getCategory().getId(), compulsoryRoundAsana.getGender().toString());
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipAndChampionshipCatgeoryAndRound(compulsoryRoundAsana.getChampionship(),
						championshiCategory, compulsoryRoundAsana.getRoundNumber());
		if (championshipRounds.getStatus().equals(RoundStatusEnum.SCHEDULED)) {
			if (compulsoryRoundAsana.getId() == null) {
				List<Asana> asanaList = compulsoryRoundAsana.getAsanaList();
				int count = compulsoryRoundAsanasService.getCount(compulsoryRoundAsana.getChampionship(),
						compulsoryRoundAsana.getAsanaCategory(), compulsoryRoundAsana.getCategory(),
						compulsoryRoundAsana.getGender().toString(), compulsoryRoundAsana.getRoundNumber());
				int seqNumber = 0;
				if (count == 0) {
					seqNumber = 1;
				} else {
					seqNumber = count + 1;
				}
				List<CompulsoryRoundAsanas> listSavedCompulsoryAsanas = new ArrayList<>();
				for (Asana stringAsana : asanaList) {
					CompulsoryRoundAsanas newCompulsoryRoundAsanas = new CompulsoryRoundAsanas();

					Asana asana = asanaService.get(stringAsana.getId());
					newCompulsoryRoundAsanas.setAsanaCategory(compulsoryRoundAsana.getAsanaCategory());
					newCompulsoryRoundAsanas.setCategory(compulsoryRoundAsana.getCategory());
					newCompulsoryRoundAsanas.setRoundNumber(compulsoryRoundAsana.getRoundNumber());
					newCompulsoryRoundAsanas.setCompulsory(compulsoryRoundAsana.isCompulsory());
					newCompulsoryRoundAsanas.setCreatedTime(compulsoryRoundAsana.getCreatedTime());
					newCompulsoryRoundAsanas.setGender(compulsoryRoundAsana.getGender());
					newCompulsoryRoundAsanas.setChampionship(compulsoryRoundAsana.getChampionship());
					newCompulsoryRoundAsanas.setCreatedBy(compulsoryRoundAsana.getCreatedBy());
					newCompulsoryRoundAsanas.setAsana(asana);
					newCompulsoryRoundAsanas.setSequenceNumber(seqNumber);
					CompulsoryRoundAsanas savedCompulsoryAsanas = compulsoryRoundAsanasService
							.save(newCompulsoryRoundAsanas);
					seqNumber++;
					listSavedCompulsoryAsanas.add(savedCompulsoryAsanas);

				}
				participantTeamAsanasService.setCompulsoryAsanasForParticipantTeams(compulsoryRoundAsana,
						listSavedCompulsoryAsanas);

			} else {
				compulsoryRoundAsanasService.save(compulsoryRoundAsana);
			}
		}

		redirectAttributes.addFlashAttribute("message", "The Record has been saved successfully.");
		LOGGER.info("Exit saveRoundAsana method -EventManagerChampionshipController");

		return "redirect:/eventmanager/championship/" + championshipId + "/manage-round-asanas";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-round-asanas/edit/{id}")
	public String editRoundAsana(@PathVariable("championshipId") Integer championshipId,
			@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws CompulsoryRoundAsanasNotFoundException {

		LOGGER.info("Entered editRoundAsana method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		try {
			CompulsoryRoundAsanas compulsoryRoundAsana = compulsoryRoundAsanasService.get(id);
			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
//			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService.getListOfAsanas(
//					compulsoryRoundAsana.getAsanaCategory().getId(),
//					compulsoryRoundAsana.getGender().toString(),
//					compulsoryRoundAsana.getCategory().toString());

			if (championship.getCreatedBy().equals(currentUser)) {

				List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();

				List<Asana> listAsanas = asanaService.listAllAsanas();
				List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
				List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService
						.listAllAsanaExecutionScores();

				List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();
				for (Asana asana : listAsanas) {
					Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
							.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
					if (listAsanaCode.isPresent()) {
						listAsanasWithCode.add(new AsanaCodeDTO(asana, listAsanaCode.get().getCode(),
								listAsanaCode.get().getSubGroup()));
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

				model.addAttribute("listAgeCategory", listAgeCategory);
				model.addAttribute("compulsoryRoundAsana", compulsoryRoundAsana);
				model.addAttribute("pageTitle", "Edit Round Asana Details");
				model.addAttribute("listAsanaCategory", listAsanaCategory);
				// model.addAttribute("listAsanaExecutionScores", listAsanaExecutionScores);
				model.addAttribute("listChampionships", listChampionships);
				model.addAttribute("listAsanas", listAsanasWithCode);
				model.addAttribute("championship", championship);
				LOGGER.info("Exit editRoundAsana method -EventManagerChampionshipController");

				return "eventmanager/compulsory_round_asana_form";
			} else {
				return "error/403";
			}
		} catch (CompulsoryRoundAsanasNotFoundException ex) {
			LOGGER.error("CompulsoryRoundAsanas not found!");
			redirectAttributes.addFlashAttribute("message", "Record not found.");
			return "redirect:/eventmanager/championship/" + championshipId + "/manage-round-asanas";
		}
	}

	// **************** EventManager Compulsory Asanas Controller Ends
	// ***************

	// **************** EventManager Evaluate Winners and Display Controller Starts
	// ***************
	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-winners")
	public String listWinners(Model model, @PathVariable("championshipId") Integer championshipId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		LOGGER.info("Entered listWinners method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {
			Championship listChampionships = null;
			try {
				listChampionships = championshipService.getChampionshipById(championshipId);
			} catch (ChampionshipNotFoundException e) {
				LOGGER.error("Championship not found!");
				redirectAttributes.addFlashAttribute("errorMessage", "Championship not found");
				return "redirect:/eventmanager/championship";
			}

			// List<Championship> listChampionships =
			// championshipService.listAllChampionshipsByNotDeleted();
			model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("championship", championship);
			model.addAttribute("pageTitle", "Evaluate Winners");
			LOGGER.info("Exit listWinners method -EventManagerChampionshipController");

			return "eventmanager/manage_winners";
		} else {
			return "error/403";
		}
	}

//	@PreAuthorize("hasAuthority('EventManager')")
//	@GetMapping("/championship/{championshipId}/display-winners")
//	public String getWinnersHomePage(Model model, @PathVariable("championshipId") Integer championshipId,
//			RedirectAttributes redirectAttributes, HttpServletRequest request) {
//		LOGGER.info("Entered getWinnersHomePage");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.findById(championshipId);
//
//		if (championship.getCreatedBy().equals(currentUser)) {
//			Championship listChampionships = null;
//			try {
//				listChampionships = championshipService.getChampionshipById(championshipId);
//			} catch (ChampionshipNotFoundException e) {
//				LOGGER.error("Championship not found!");
//				redirectAttributes.addFlashAttribute("errorMessage", "Championship not found");
//				return "redirect:/eventmanager/championship";
//
//			}
//
//			// List<Championship> listChampionships =
//			// championshipService.listAllChampionshipsByNotDeleted();
//			model.addAttribute("listChampionships", listChampionships);
//			model.addAttribute("championship", championship);
//			model.addAttribute("pageTitle", "Display Winners");
//			return "eventmanager/display_winners_home";
//		} else {
//			return "error/403";
//		}
//	}

//	@PreAuthorize("hasAuthority('EventManager')")
//	@PostMapping("/championship/{championshipId}/display-winners/proceed")
//	public String proceedToGetCategories(@RequestParam(name = "championship") Integer id, Model model,
//			@PathVariable("championshipId") Integer championshipId, RedirectAttributes redirectAttributes,
//			HttpServletRequest request) {
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.findById(id);
//		List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//		if (listChampionshipCategory.size() != 0) {
//			return "redirect:/eventmanager/championship/" + id + "/display-winners/categories";
//		} else {
//			model.addAttribute("errorMessage", "No Categories found for the Chosen Championship!");
//			return "redirect:/eventmanager/championship/" + id + "/display-winners";
//		}
//
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/display-winners/categories")
	public String getCategoriesPage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered getCategoriesPage method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		LOGGER.info("Exit getCategoriesPage method -EventManagerChampionshipController");
		return getCategoriesByPage(championshipId, 1, model, "asanaCategory", "asc", "", "", request,
				redirectAttributes);

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/display-winners/categories/page/{pageNum}")
	public String getCategoriesByPage(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String asanaCategory,
			@RequestParam(name = "keyword2", required = false) String ageCategory, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered getCategoriesByPage method - EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		Page<ChampionshipCategory> page;
		if (championship.getCreatedBy().equals(currentUser)) {
			model.addAttribute("championship", championship);
			page = championshipCategoryService.listPageByChampionship(championshipId, pageNum, sortField, sortDir,
					asanaCategory, ageCategory);
			List<ChampionshipCategory> listChampionshipCategory = page.getContent();
			long startCount = (pageNum - 1) * championshipCategoryService.RECORDS_PER_PAGE + 1;
			long endCount = startCount + championshipCategoryService.RECORDS_PER_PAGE - 1;
			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
			model.addAttribute("moduleURL",
					"/eventmanager/championship/" + championshipId + "/display-winners/categories");
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", reverseSortDir);
			model.addAttribute("keyword1", asanaCategory);
			model.addAttribute("keyword2", ageCategory);
			model.addAttribute("championshipId", championshipId);
			model.addAttribute("championshipName", championship.getName());
			model.addAttribute("listChampionshipCategory", listChampionshipCategory);
			model.addAttribute("pageTitle", "Winners");
			return "eventmanager/display_winners_categories";

		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/display-winners/{championshipId}/category/{championshipCategoryId}/round/{round}")
	public String getCategoriesWinners(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId, HttpServletRequest request,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes)
			throws EntityNotFoundException {
		LOGGER.info("Entered getCategoriesWinners method -EventManagerChampionshipController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
		List<ParticipantTeamRoundTotalScoring> listWinners = new ArrayList<>();
		if (championshipRounds.getStatus().equals(RoundStatusEnum.COMPLETED)) {
			listWinners = participantTeamRoundTotalScoringService
					.findByChampionshipAndChampionshipRoundsAndWinner(championship, championshipRounds, true);

		} else {
			model.addAttribute("errorMessage", "The Championship is still not started or Ongoing..!");
			return "eventmanager/display_selected_category_winners";
		}
		model.addAttribute("pageTitle",
				"Winners Championship : " + championship.getName() + " | "
						+ championshipCategory.getCategory().getTitle() + " | "
						+ championshipCategory.getAsanaCategory().getName() + " | " + championshipCategory.getGender());
		model.addAttribute("listWinners", listWinners);
		LOGGER.info("Exit getCategoriesWinners method -EventManagerChampionshipController");

		return "eventmanager/display_selected_category_winners";
	}
	// **************** EventManager Evaluate Winners and Display Controller Ends
	// ***************

	// **************** EventManager View Team Live Scores Controller Starts
	// ***************

//	@PreAuthorize("hasAuthority('EventManager')")
//	@GetMapping("/championship/{championshipId}/view-scores")
//	public String getJudgeScores(Model model, @PathVariable(name = "championshipId") Integer championshipId,
//			HttpServletRequest request) throws ChampionshipNotFoundException {
//		LOGGER.info("Entered listChampionships EventManagerChampionshipController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.findById(championshipId);
//		if (championship.getCreatedBy().equals(currentUser)) {
//			Championship listChampionships = championshipService.getChampionshipById(championshipId);
//			model.addAttribute("listChampionships", listChampionships);
//			model.addAttribute("championship", championship);
//			model.addAttribute("pageTitle", "View Judge Scores -" + championship.getName());
//			return "eventmanager/livescores/view_championships";
//		} else {
//			return "error/403";
//		}
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/view-scores/{championshipId}/getChampionshipCategories")
	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId, Model model,

			HttpServletRequest request, RedirectAttributes redirectAttributes) throws ChampionshipNotFoundException {
		LOGGER.info("Entered listChampionshipCategories method -EventManagerChampionshipController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship;

		championship = championshipService.getChampionshipById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {
			model.addAttribute("championship", championship);

			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
			List<ChampionshipCategoryDTO> listChampionshipCategoryDTO = new ArrayList<>();
			if (!listChampionshipCategory.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					for (int round = 1; round <= championshipCategory.getNoOfRounds(); round++) {
						listChampionshipCategoryDTO
								.add(new ChampionshipCategoryDTO(championship, championshipCategory, round));
					}

				}
			}

			model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
			model.addAttribute("pageTitle", "Championship Categories");
			LOGGER.info("Exit listChampionshipCategories method -EventManagerChampionshipController");

			return "eventmanager/livescores/view_championship_category_form";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/view-scores/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}")
	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws AsanaCategoryNotFoundException, EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered listAllTeamsForChampionhsip RefereeCalculateTeamScoresController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(id);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			ra.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		return listAllChampionshipTeamsByPage(id, asanaCategoryId, gender, ageCategoryId, round, 1, model,
				"chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/view-scores/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}/page/{pageNum}")
	public String listAllChampionshipTeamsByPage(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) {
		LOGGER.info("Entered listAllChampionshipTeamsByPage EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		RoundStatusEnum status = RoundStatusEnum.ONGOING;
		StatusEnum participantTeamStatus = StatusEnum.SCHEDULED;
		Championship championship = new Championship();
		ChampionshipRounds championshipRounds = new ChampionshipRounds();
		try {
			championship = championshipService.get(id);
			model.addAttribute("pageTitle", "Manage Championship");
			model.addAttribute("championship", championship);
			if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
					|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
					|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
				ra.addFlashAttribute("errorMessage",
						"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
				return "redirect:/eventmanager/championship";
			}

			User user = CommonUtil.getCurrentUser();
			// Judge judge=CommonUtil.getCurrentJudge();
			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
			AgeCategory ageCategory = ageCategoryService.get(ageCategoryId);
			ChampionshipCategory championshipCategory = championshipCategoryService
					.getChampionshipCategoryForAllConditions(id, asanaCategoryId, ageCategoryId, gender);
			Integer championshipCategoryId = championshipCategory.getId();
			championshipRounds = championshipRoundsService
					.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
			LOGGER.info("championshipRounds" + championshipRounds);
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AsanaCategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (championship.getCreatedBy().equals(currentUser)) {
			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
			Page<ChampionshipParticipantTeams> page = null;
			if (championshipRounds != null) {
//				if(championshipRounds.getStatus().equals(RoundStatusEnum.SCHEDULED)) {
//					LOGGER.error("Championship Round is null");
//					ra.addFlashAttribute("errorMessage", "Unable to get teams");
//					return "redirect:/eventmanager/championship/view-scores/"+id+"/getChampionshipCategories";
//				}
				page = championshipParticipantTeamsService.listByPage(pageNum, sortField, sortDir, chestNumber,
						championshipRounds);
				listChampionshipParticipantTeams = page.getContent();
			} else {
				LOGGER.error("Championship Round is null");
				ra.addFlashAttribute("errorMessage", "Unable to get teams");
				return "redirect:/eventmanager/championship/view-scores/" + id + "/getChampionshipCategories";
			}
			long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
			long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

			model.addAttribute("moduleURL", "/eventmanager/championship/view-scores/" + id + "/category/"
					+ asanaCategoryId + "/" + ageCategoryId + "/" + gender + "/" + round);

			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", reverseSortDir);
			model.addAttribute("keyword1", chestNumber);
			model.addAttribute("asanaCategoryId", asanaCategoryId);
			model.addAttribute("ageCategoryId", ageCategoryId);
			model.addAttribute("gender", gender);
			model.addAttribute("round", round);
			model.addAttribute("championshipId", id);
			model.addAttribute("listChampionshipParticipantTeams", listChampionshipParticipantTeams);
			model.addAttribute("pageTitle", championship.getName() + " Teams");

			return "eventmanager/livescores/view_championship_team_form";
		} else {
			return "error/403";
		}

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/view-scores/championshipTeam/{championshipId}/{championshipTeamId}/calculateScores")
	public String getTeamAndCalculateScores(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "championshipTeamId") Integer championshipTeamId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, RoleNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered getTeamAndCalculateScores method -EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		User user = CommonUtil.getCurrentUser();
		Championship championship1 = championshipService.findById(championshipId);
		if (!(championship1.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship1.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship1.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship1.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (!championship1.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}
		if (currentUser.getRoleName().equals("Administrator") || currentUser.getRoleName().equals("SubAdministrator")) {
			model.addAttribute("viewScores", "adminViewScores");
		} else if (currentUser.getRoleName().equals("Judge")) {
			model.addAttribute("viewScores", "refereeViewScores");
		} else if (currentUser.getRoleName().equals("EventManager")) {
			model.addAttribute("viewScores", "eventManagerViewScores");
		}

		ChampionshipParticipantTeams championshipParticipantTeams;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipTeamId);
			ChampionshipRounds championshipRounds = championshipParticipantTeams.getChampionshipRounds();
			ChampionshipCategory championshipCategory = championshipRounds.getChampionshipCategory();
			Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
			ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();

			Integer round = championshipRounds.getRound();

			boolean allJudgeScoresGivenStatus = checkAllJudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			// boolean allJudgeScoresGivenStatus = true;
			model.addAttribute("championshipRound", championshipRounds.getStatus().toString());
			model.addAttribute("allJudgeScoresGivenStatus", allJudgeScoresGivenStatus);
			model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
			model.addAttribute("championship", championship);
			model.addAttribute("championshipId", championship.getId());
			model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
					.getChampionshipCategory().getAsanaCategory().getId());
			model.addAttribute("ageCategoryId", championshipParticipantTeams.getChampionshipRounds()
					.getChampionshipCategory().getCategory().getId());
			model.addAttribute("gender",
					championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
			model.addAttribute("pageTitle", asanaCategory.getName() + " - " + participantTeam.getChestNumber());
			model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);

			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {

				// ========== get Evaluator Judge Scores =================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ========== get D Judge Scores =================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);

				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>>> refereeParticipantAsanaDJudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees dJudgeTeamReferee : listDjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);
					refereeParticipantAsanaDJudgeScoreMap.put(dJudgeTeamReferee, participantAsanaScoreMap);
				}

				model.addAttribute("refereeParticipantAsanaDJudgeScoreMap", refereeParticipantAsanaDJudgeScoreMap);

				// ========== get T Judge Scores =================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, List<TimeKeeperJudgeScore>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					List<TimeKeeperJudgeScore> listTimekeeperScoring = timeKeeperJudgeScoreService
							.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
									championshipParticipantTeams, tJudgeTeamReferee);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, listTimekeeperScoring);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				LOGGER.info("Returning team_score_calculation_form ");
				return "eventmanager/viewscores/traditional_single_judge_scores";

			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {

				// ========== get Evaluator Judge Scores =================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
							championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
							participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					if (commonQuestion != null) {
						refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
					}
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);
				List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
						.getParticipantTeamParticipants();
				LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaDjudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
					List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
							.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
									participantTeamParticipants, championshipRounds.getRound());
					LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
					for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
						List<TraditionalJudgeScore> listTraditionalJudgeScore = new ArrayList<>();
						for (ParticipantTeamReferees dJudgeParticipantTeamReferee : listDjudgeParticipantTeamReferees) {
							TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService
									.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
											championshipParticipantTeams, dJudgeParticipantTeamReferee,
											participantTeamParticipants, participantTeamAsanas);
							listTraditionalJudgeScore.add(traditionalJudgeScore);

							asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
						}
					}
					participantAsanaDjudgeScoreMap.put(participantTeamParticipants, asanaScoreMap);
				}
				model.addAttribute("participantAsanaDjudgeScoreMap", participantAsanaDjudgeScoreMap);

				// ================= get A Judge Scores ====================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// ================ get T Judge Scores ====================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);

					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					if (commonQuestionScore != null) {
						refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
					}
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning artistic_single_judge_scores page");
				return "eventmanager/viewscores/artistic_single_judge_scores";
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {

				// ============= get Evaluator Judge Scores =====================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
//						EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
//						championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
//						participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					// refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee,
					// commonQuestion);

					LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluatorForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							eJudgeParticipantTeamReferee, participantTeam);
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecordsForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							eJudgeParticipantTeamReferee, participantTeam);
					refereeParticipantAsanaScoreMap.put(eJudgeParticipantTeamReferee, participantAsanaScoreMap);
					if (commonQuestion != null) {
						refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
					}
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);
				List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
						.getParticipantTeamParticipants();

				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>>> refereeParticipantAsanaDJudgeScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>>> refereeDJudgeCommonQuestionAsanaScoreMap = new LinkedHashMap<>();

				for (ParticipantTeamReferees dJudgeTeamReferee : listDjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);
					LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> commonQuestionForAsanaMap = getListOfParticipantsCommonAsanaQuestionsWithAsanaScoreList(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);
					refereeParticipantAsanaDJudgeScoreMap.put(dJudgeTeamReferee, participantAsanaScoreMap);
					refereeDJudgeCommonQuestionAsanaScoreMap.put(dJudgeTeamReferee, commonQuestionForAsanaMap);
				}
				model.addAttribute("refereeParticipantAsanaDJudgeScoreMap", refereeParticipantAsanaDJudgeScoreMap);
				model.addAttribute("refereeDJudgeCommonQuestionAsanaScoreMap",
						refereeDJudgeCommonQuestionAsanaScoreMap);

				// ==================== get A Judge Scores =========================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// =================== get T Judge Scores =========================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
//					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
//							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
//							tJudgeTeamReferee, participantTeam);
//					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);
//					
//					// get common question records
//					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
//							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
//							tJudgeTeamReferee, participantTeam);
//					
//					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);

					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeperForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);
					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeperForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning team_score_calculation_form ");
				return "eventmanager/viewscores/artistic_pair_judge_scores";
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {

				// ================= get Evaluator Scores =======================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
							championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
							participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);
				List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
						.getParticipantTeamParticipants();
				LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaDjudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
					List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
							.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
									participantTeamParticipants, championshipRounds.getRound());
					LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
					for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
						List<TraditionalJudgeScore> listTraditionalJudgeScore = new ArrayList<>();
						for (ParticipantTeamReferees dJudgeParticipantTeamReferee : listDjudgeParticipantTeamReferees) {
							TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService
									.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
											championshipParticipantTeams, dJudgeParticipantTeamReferee,
											participantTeamParticipants, participantTeamAsanas);
							listTraditionalJudgeScore.add(traditionalJudgeScore);
							// System.out.println("traditionalJudgeScore : " +
							// traditionalJudgeScore.toString());
							asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
						}
					}
					participantAsanaDjudgeScoreMap.put(participantTeamParticipants, asanaScoreMap);
				}
				model.addAttribute("participantAsanaDjudgeScoreMap", participantAsanaDjudgeScoreMap);

				// ================= get A Judge Scores =======================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// ================== get T Judge Scores =======================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);

					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);

					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning team_score_calculation_form ");
				return "eventmanager/viewscores/rhythmic_pair_judge_scores";

			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {

				// ================= get Evaluator Scores =======================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
							championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
							participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					if (commonQuestion != null) {
						refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
					}
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				// model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> refereeAsanaDJudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees dJudgeTeamReferee : listDjudgeParticipantTeamReferees) {

					LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = getMapOfAsanasWithAsanaScoreListForArtisticGroup(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);

					refereeAsanaDJudgeScoreMap.put(dJudgeTeamReferee, asanaScoreMap);
				}
				model.addAttribute("refereeAsanaDJudgeScoreMap", refereeAsanaDJudgeScoreMap);

				// ================= get A Judge Scores =======================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// ================== get T Judge Scores =======================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);

					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);

					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Exit getTeamAndCalculateScores method -EventManagerChampionshipController");
				return "eventmanager/viewscores/artistic_group_judge_scores";

			}

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Returning team_score_calculation_form ");
			return "eventmanager/view_judge_scoring";
		}
		// return "referee/view_judge_scoring";
		return null;

	}

	private LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> getListOfParticipantsCommonAsanaQuestionsWithAsanaScoreList(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		JudgeType judgeType = participantTeamReferees.getType();
		List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
						championshipCategory.getAsanaCategory());
		LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> commonQuestionForAsanaMap = new LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>>();
		if (!listAsanaEvaluationQuestions.isEmpty()) {
			for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {
				List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);
				commonQuestionForAsanaMap.put(asanaEvaluationQuestion, listTraditionalJudgeScore);
			}

		}
		return commonQuestionForAsanaMap;
	}

	private LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> getMapOfAsanasWithAsanaScoreListForArtisticGroup(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
						listParticipantTeamParticipants.get(0), championshipRounds.getRound());
		LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

			List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamAsanasOrderBySequenceNumberAsc(
							championshipParticipantTeams, participantTeamReferees, participantTeamAsanas);
			asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
		}

		return asanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> getMapOfAsanasWithAsanaScoreList(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
//				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
//						.findAllByRoleAndChampionshipAndChampionshipCategory(role, championship, championshipCategory);
//				for(AsanaEvaluationQuestions asanaEvaluationQuestion:listAsanaEvaluationQuestions) {
//					
//				}
				List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
								participantTeamAsanas);
				asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
			}

			participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
		}
		return participantAsanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> getMapAsanaWithEJudgeScores(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ParticipantTeam participantTeam, List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees) {

		LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
			List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
					.getParticipantTeamParticipants();
			LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();
			for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
				List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
						.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
								championshipRounds.getRound());
				LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

					List<EvaluatorJudgeScore> listEvaluatorJudgeScore = evaluatorJudgeScoreService
							.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
									championshipParticipantTeams, eJudgeParticipantTeamReferee,
									participantTeamParticipants, participantTeamAsanas);
					asanaScoreMap.put(participantTeamAsanas, listEvaluatorJudgeScore);
				}
				participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
			}
			refereeParticipantAsanaScoreMap.put(eJudgeParticipantTeamReferee, participantAsanaScoreMap);
		}

		return refereeParticipantAsanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> getCommonAJudgeScoresForTeam(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeam participantTeam, Integer round)
			throws RoleNotFoundException, EntityNotFoundException {
		JudgeType aJudgeType = judgeTypeService.get(A_JUDGE_TYPE_ID);
		List<ParticipantTeamReferees> listAjudgeParticipantTeamReferees = participantTeamRefereesService
				.getAllByTeamAndRoundAndType(participantTeam, round, aJudgeType);
		LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamReferees aJudgeTeamReferee : listAjudgeParticipantTeamReferees) {
			List<ArtisticJudgeScore> listScoring = artisticJudgeScoreService
					.findAllByChampionshipParticipantTeamAndParticipantTeamReferees(championshipParticipantTeams,
							aJudgeTeamReferee);

			refereeAJudgeTeamScoreMap.put(aJudgeTeamReferee, listScoring);
		}
		return refereeAJudgeTeamScoreMap;
	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> getMapOfAsanasWithAsanaScoreListForEvaluator(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<EvaluatorJudgeScore> listEvaluatorJudgeScore = evaluatorJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
								participantTeamAsanas);
				asanaScoreMap.put(participantTeamAsanas, listEvaluatorJudgeScore);
			}

			participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
		}
		return participantAsanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> getMapOfAsanasWithAsanaScoreListForTimeKeeper(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		ParticipantTeamParticipants participantTeamParticipants = participantTeam.getParticipantTeamParticipants()
				.get(0);

		LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = new LinkedHashMap<>();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				List<TimeKeeperJudgeScore> listTimeKeeperJudgeScores = new ArrayList<>();
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					listTimeKeeperJudgeScores = timeKeeperJudgeScoreService
							.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
									championshipParticipantTeams, participantTeamReferees,
									listAsanaEvaluationQuestions.get(0), participantTeamAsanas.getSequenceNumber());

				}
				participantAsanaScoreMap.put(participantTeamAsanas, listTimeKeeperJudgeScores);
			}

		}
		return participantAsanaScoreMap;
	}

	private TimeKeeperJudgeScore getAllCommonQuestionRecordsForTimeKeeper(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		TimeKeeperJudgeScore listTimeKeeperJudgeCommonQuestionScores = new TimeKeeperJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listTimeKeeperJudgeCommonQuestionScores = timeKeeperJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0));
		}
		return listTimeKeeperJudgeCommonQuestionScores;
	}

	private boolean checkAllJudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
		if (participantTeam.getAsanaCategory().getId() == 1) { // Traditional single

			boolean eJudgeScoreGivenStatus = checkEjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean tJudgeScoreGivenStatus = checkTjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean dJudgeScoreGivenStatus = checkDjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);

			if (eJudgeScoreGivenStatus && tJudgeScoreGivenStatus && dJudgeScoreGivenStatus) {
				return true;
			} else {
				return false;
			}

		} else { // All other categories

			boolean eJudgeScoreGivenStatus = checkEjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean tJudgeScoreGivenStatus = checkTjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean dJudgeScoreGivenStatus = checkDjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean aJudgeScoreGivenStatus = checkAjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			if (eJudgeScoreGivenStatus && tJudgeScoreGivenStatus && dJudgeScoreGivenStatus && aJudgeScoreGivenStatus) {
				return true;
			} else {
				return false;
			}

		}
	}

	private boolean checkAjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<ArtisticJudgeScore> listArtisticJudgeScores = artisticJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listArtisticJudgeScores.size() != 0) {
			for (ArtisticJudgeScore artisticJudgeScore : listArtisticJudgeScores) {
				listStatusForRecords.add(artisticJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Ajudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Ajudge");
				return false;
			} else {
				LOGGER.info("Returning false - Ajudge");
				return true;
			}
		}
	}

	private boolean checkDjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<TraditionalJudgeScore> listTraditionalJudgeScores = traditionalJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listTraditionalJudgeScores.size() != 0) {
			for (TraditionalJudgeScore traditionalJudgeScore : listTraditionalJudgeScores) {
				listStatusForRecords.add(traditionalJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Djudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Djudge");
				return false;
			} else {
				LOGGER.info("Returning false - Djudge");
				return true;
			}
		}
	}

	private boolean checkTjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<TimeKeeperJudgeScore> listTimeKeeperJudgeScores = timeKeeperJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listTimeKeeperJudgeScores.size() != 0) {
			for (TimeKeeperJudgeScore timeKeeperJudgeScore : listTimeKeeperJudgeScores) {
				listStatusForRecords.add(timeKeeperJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Tjudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Tjudge");
				return false;
			} else {
				LOGGER.info("Returning false - Tjudge");
				return true;
			}
		}
	}

	private boolean checkEjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<EvaluatorJudgeScore> listEvaluatorJudgeScores = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listEvaluatorJudgeScores.size() != 0) {
			for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScores) {
				listStatusForRecords.add(evaluatorJudgeScore.getStatus());
			}
		}
		System.out.println("listStatusForRecords Ejudge : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Ejudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Ejudge");
				return false;
			} else {
				LOGGER.info("Returning false - Ejudge");
				return true;
			}
		}
	}

	private EvaluatorJudgeScore getAllCommonQuestionRecords(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds, ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LinkedHashMap<AsanaEvaluationQuestions, List<EvaluatorJudgeScore>> commonQuestionRecordsMap = new LinkedHashMap<>();
		JudgeType judgeType = participantTeamReferees.getType();
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		EvaluatorJudgeScore listEvaluatorJudgeCommonQuestionScores = new EvaluatorJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listEvaluatorJudgeCommonQuestionScores = evaluatorJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0));
		}
		return listEvaluatorJudgeCommonQuestionScores;

	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> getMapOfAsanasWithAsanaScoreListForEvaluatorForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
						championshipRounds.getRound());
		LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

			List<EvaluatorJudgeScore> listEvaluatorJudgeScore = evaluatorJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
							championshipParticipantTeams, participantTeamReferees, participantTeamParticipant,
							participantTeamAsanas);
			asanaScoreMap.put(participantTeamAsanas, listEvaluatorJudgeScore);
		}

		participantAsanaScoreMap.put(participantTeamParticipant, asanaScoreMap);

		return participantAsanaScoreMap;
	}

	private EvaluatorJudgeScore getAllCommonQuestionRecordsForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LinkedHashMap<AsanaEvaluationQuestions, List<EvaluatorJudgeScore>> commonQuestionRecordsMap = new LinkedHashMap<>();
		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		EvaluatorJudgeScore listEvaluatorJudgeCommonQuestionScores = new EvaluatorJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listEvaluatorJudgeCommonQuestionScores = evaluatorJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0),
							participantTeamParticipant);
		}
		return listEvaluatorJudgeCommonQuestionScores;

	}

	private LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> getMapOfAsanasWithAsanaScoreListForTimeKeeperForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = new LinkedHashMap<>();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
				System.out.println(participantTeamAsanas.getAsana().getName());
				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				List<TimeKeeperJudgeScore> listTimeKeeperJudgeScores = new ArrayList<>();
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					listTimeKeeperJudgeScores = timeKeeperJudgeScoreService
							.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
									championshipParticipantTeams, participantTeamReferees,
									listAsanaEvaluationQuestions.get(0), participantTeamAsanas.getSequenceNumber());

				}
				participantAsanaScoreMap.put(participantTeamAsanas, listTimeKeeperJudgeScores);
			}

		}
		return participantAsanaScoreMap;
	}

	private TimeKeeperJudgeScore getAllCommonQuestionRecordsForTimeKeeperForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		TimeKeeperJudgeScore listTimeKeeperJudgeCommonQuestionScores = new TimeKeeperJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listTimeKeeperJudgeCommonQuestionScores = timeKeeperJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0),
							participantTeamParticipant);
		}
		return listTimeKeeperJudgeCommonQuestionScores;
	}
	// **************** EventManager View Team Live Scores Controller Ends
	// ***************

	// **************** EventManager View Team Scores Controller Starts
	// ***************

//	@PreAuthorize("hasAuthority('EventManager')")
//	@GetMapping("/championship/{championshipId}/choose-championship")
//	public String getChampionships(Model model, @PathVariable("championshipId") Integer championshipId,
//			HttpServletRequest request) throws ChampionshipNotFoundException {
//		LOGGER.info("Entered getChampionships - EventManagerChampionshipController ");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.findById(championshipId);
//		if (championship.getCreatedBy().equals(currentUser)) {
//			Championship listChampionships = championshipService.getChampionshipById(championshipId);
//			model.addAttribute("listChampionships", listChampionships);
//			model.addAttribute("championship", championship);
//			model.addAttribute("pageTitle", "View Team Score");
//			return "eventmanager/list_all_championships";
//
//		} else {
//			return "error/403";
//		}
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/getTeamsForCategories")
	public String listChampionshipTeamCategories(@PathVariable(name = "championshipId") Integer championshipId,
			Model model, RedirectAttributes redirectAttributes)
			throws EntityNotFoundException, ChampionshipNotFoundException {
		LOGGER.info("Entered listChampionshipTeamCategories method -EventManagerChampionshipController");

		User refereeUser = CommonUtil.getCurrentUser();
		User currentUser = CommonUtil.getCurrentUser();

		Championship championship = championshipService.getChampionshipById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {

			List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
			List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
					.getChampionshipCategoryByChampionshipId(championshipId);
			// List of all age category for championship
			Set<AgeCategory> listAgeCategories = new HashSet<>();

			for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
				listAgeCategories.add(championshipCategory.getCategory());
			}

			List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
			for (AgeCategory ageCategory : listAgeCategories) {
				for (AsanaCategory asanaCategory : listAsanaCategories) {

					List<ChampionshipCategory> listChampionhsipCategoryForAsanaCategory = new ArrayList<>();
					for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
						if (championshipCategory.getAsanaCategory().equals(asanaCategory)
								&& championshipCategory.getCategory().equals(ageCategory)) {

							listChampionhsipCategoryForAsanaCategory.add(championshipCategory);
						}
					}

					if (!listChampionhsipCategoryForAsanaCategory.isEmpty()) {
						if (listChampionhsipCategoryForAsanaCategory.size() > 1) {
							Collections.sort(listChampionhsipCategoryForAsanaCategory,
									new Comparator<ChampionshipCategory>() {
										@Override
										public int compare(ChampionshipCategory c1, ChampionshipCategory c2) {
											return c2.getNoOfRounds().compareTo(c1.getNoOfRounds());
										}
									});
						}
						Integer noOfRounds = listChampionhsipCategoryForAsanaCategory.get(0).getNoOfRounds();
						for (int round = 1; round <= noOfRounds; round++) {
							listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship, asanaCategory,
									listChampionhsipCategoryForAsanaCategory.get(0).getCategory(), round));
						}

					}
				}
			}

			Collections.sort(listChampionshipCategoryDTO, new Comparator<ChampionshipCategoryDTO1>() {

				@Override
				public int compare(ChampionshipCategoryDTO1 c1, ChampionshipCategoryDTO1 c2) {
					return c1.getAsanaCategory().getId().compareTo(c2.getAsanaCategory().getId());
				}

			});

			model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
			model.addAttribute("pageTitle", "Championship Categories");
			LOGGER.info("Exit listChampionshipTeamCategories method -EventManagerChampionshipController");

			return "eventmanager/championship_category_form1";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/yogasana/{championshipId}/getTeamsForCategroies/asanaCategory/{asanaCategoryId}/ageCategory/{ageCategoryId}/round/{round}")
	public String scoreDetailsForCategoryAndRound(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "ageCategoryId") Integer ageCategoryId, @PathVariable(name = "round") Integer round,
			Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException, ChampionshipNotFoundException {
		LOGGER.info("Entered scoreDetailsForCategoryAndRound EventManagerChampionshipController");

		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
		List<CategoriesDTO1> listCategories = new ArrayList<CategoriesDTO1>();

		Championship championship = championshipService.get(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		User currentUser = CommonUtil.getCurrentUser();
		if (championship.getCreatedBy().equals(currentUser)) {

			model.addAttribute("championship", championship);

			List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
					.getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc(
							championshipId, asanaCategoryId, ageCategoryId);

			System.out.println("listChampionshipCategory " + listChampionshipCategory.toString());

			if (!listChampionshipCategory.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					String categoryName = championshipCategory.getCategory().getTitle() + " - "
							+ championshipCategory.getGender().toString();

					String asanaCategoryName = championshipCategory.getAsanaCategory().getName();

					Integer categoryId = null;
					Integer eventId = null;

					String eventDescription = "Yogasana Event";
					String gender = championshipCategory.getGender().toString();
					Integer noOfRounds = championshipCategory.getNoOfRounds();
					List<RoundsDTO1> listRounds = new ArrayList<>();

					String roundName = "Round " + round;

					ChampionshipRounds championshipRounds = championshipRoundsService
							.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
									round);
					if (championshipRounds != null) {
						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
								.getAllByChampionshipRounds(championshipRounds);
						List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
								.getByRoundAndChampionshipAndChampionshipCategory(round, championship,
										championshipCategory);

						List<TeamsDTO1> listTeams = new ArrayList<>();
						if (!listChampionshipParticipantTeams.isEmpty()) {

							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
								String autogenChestNumber = championshipParticipantTeams.getParticipantTeam()
										.getAutogenChestNumber();
								String teamName = championshipParticipantTeams.getParticipantTeam().getName();
								List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeamParticipantsService
										.getTeamParticipantsForParticipantTeam(
												championshipParticipantTeams.getParticipantTeam());
								List<String> listParticipantName = null;
								List<List<String>> participantLists = new ArrayList<>();
								for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {

									participantLists.add(participantService.getParticipantByParticipantTeamParticipants(
											participantTeamParticipants.getParticipant().getId()));
								}

								listParticipantName = participantLists.stream().flatMap(List::stream)
										.collect(Collectors.toList());
								System.out.println("listParticipantName" + listParticipantName);
								String status = "SCHEDULED";
								Float totalScore = 0.0f;
								status = championshipParticipantTeams.getStatus().toString();
								// String rank = "-";
								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
										.findByChampionshipParticipantTeamAndChampionshipRounds(
												championshipParticipantTeams, championshipRounds);
								Float tieScore = null;
								if (participantTeamRoundTotalScoring != null) {
									totalScore = participantTeamRoundTotalScoring.getTotalScore();
									tieScore = participantTeamRoundTotalScoring.getTieScore();
									LOGGER.info(
											"participantTeam: "
													+ participantTeamRoundTotalScoring.getChampionshipParticipantTeams()
															.getParticipantTeam().getName()
													+ "   totalScore " + totalScore);
									// rank = participantTeamRoundTotalScoring.getRanking().toString();
								}

								listTeams.add(new TeamsDTO1(chestId, autogenChestNumber, status, totalScore,
										listParticipantName, teamName, tieScore));
							}

						}

						// sort the list by score desc
						List<TeamsDTO1> listSortedTeams = listTeams.stream()
								.sorted(Comparator.comparing(TeamsDTO1::getTotal_score).reversed())
								.collect(Collectors.toList());
						listRounds.add(new RoundsDTO1(roundName, listSortedTeams));

						listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription,
								gender, listRounds, asanaCategoryName));

					}
				}
			}

			for (CategoriesDTO1 category : listCategories) {
				System.out.println(category.toString());
			}

			model.addAttribute("pageTitle", "View Team Scores");
			model.addAttribute("listCategories", listCategories);

			String imagePath1 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
					+ System.getProperty("file.separator") + "image1";
			String imagePath2 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
					+ System.getProperty("file.separator") + "image2";
			String imagePath3 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
					+ System.getProperty("file.separator") + "image3";
			String imagePath4 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
					+ System.getProperty("file.separator") + "image4";
			String fileName1 = "";
			File directory1 = new File(imagePath1);
			if (directory1.exists()) {
				File[] files = directory1.listFiles();
				fileName1 = files[0].getName();
				// System.out.println(fileName1 + "image1");
				if (fileName1.equals("")) {
					// System.out.println(fileName1 + "image1null");

					model.addAttribute("image1", "");
				} else {
					// System.out.println(fileName1 + "image1notnull");

					model.addAttribute("image1", fileName1);
				}
			}
			String fileName2 = "";
			File directory2 = new File(imagePath2);
			if (directory2.exists()) {
				File[] files = directory2.listFiles();
				fileName2 = files[0].getName();
				// System.out.println(fileName1 + "image2");
				if (fileName2.equals("")) {
					model.addAttribute("image2", "");
				} else {
					model.addAttribute("image2", fileName2);
				}
			}

			String fileName3 = "";
			File directory3 = new File(imagePath3);
			if (directory3.exists()) {
				File[] files = directory3.listFiles();
				fileName3 = files[0].getName();
				// System.out.println(fileName3 + "image3");
				if (fileName3.equals("")) {
					model.addAttribute("image3", "");
				} else {
					model.addAttribute("image3", fileName3);
				}
			}

			String fileName4 = "";
			File directory4 = new File(imagePath4);
			if (directory4.exists()) {
				File[] files = directory4.listFiles();
				fileName4 = files[0].getName();
				// System.out.println(fileName4 + "image4");
				if (fileName4.equals("")) {
					model.addAttribute("image4", "");
				} else {
					model.addAttribute("image4", fileName4);
				}
			}

			return "eventmanager/view_team_total_scores";
			// return new ResponseEntity<List<CategoriesDTO1>>(listCategories,
			// HttpStatus.OK);

		} else {
			return "error/403";
		}
	}

//////////////////////////////////////////////////////Add panels
	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-referee-panels/new")
	public String newChampionshipRefereePanels(@PathVariable(name = "championshipId") Integer championshipId,
			Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered newChampionshipRefereePanels EventManagerChampionshipController");
// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipRefereePanels championshipRefereePanel = new ChampionshipRefereePanels();
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {
			List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();

			model.addAttribute("championship", championship);
			model.addAttribute("championshipRefereePanel", championshipRefereePanel);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Add Championship Judge Panels");
			LOGGER.info("Exit newChampionshipRefereePanels EventManagerChampionshipController");

			return "eventmanager/referee_panels_form";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-referee-panels/edit/{id}")
	public String editChampionshipRefereePanel(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "championshipId") Integer championshipId, Model model,
			@RequestParam(value = "type", required = false) Integer judgeType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered editChampionshipRefereePanel EventManagerChampionshipController");
// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		try {
			ChampionshipRefereePanels championshipRefereePanel = championshipRefereePanelsService.get(id);
			if (championship.getCreatedBy().equals(currentUser)) {
				List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
				List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();

				List<Judge> listNonSelectedReferrees = getNonSelectedReferees(championshipRefereePanel.getId());
				List<JudgeType> listJudgeType = judgeTypeService.listAllJudgeType();
				List<RefereesPanels> listAssignedReferees = refereePanelService
						.listAllRefereePanels(championshipRefereePanel);
				model.addAttribute("championshipRefereePanel", championshipRefereePanel);
				model.addAttribute("listCategories", listCategories);
				model.addAttribute("listChampionships", listChampionships);
				model.addAttribute("listNonSelectedReferrees", listNonSelectedReferrees);
				model.addAttribute("listAssignedReferees", listAssignedReferees);
				model.addAttribute("listJudgeType", listJudgeType);
				model.addAttribute("pageTitle", "Edit Championship Judge Panel");
				LOGGER.info("Exit editChampionshipRefereePanel EventManagerChampionshipController");

				return "eventmanager/referee_panels_form";
			} else {
				return "error/403";
			}
		} catch (ChampionshipRefereePanelsNotFoundException ex) {
			LOGGER.error("ChampionshipRefereePanels not found!");
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/eventmanager/manage-referee-panels";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-referee-panels/save")
	public String saveChampionshipRefereePanels(@PathVariable(name = "championshipId") Integer championshipId,
			@ModelAttribute("championshipRefereePanel") ChampionshipRefereePanels championshipRefereePanel, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered saveChampionshipRefereePanels EventManagerChampionshipController");
// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		try {
			championshipRefereePanelsService.save(championshipRefereePanel);
			return "redirect:/eventmanager/championship/" + championship.getId() + "/manage-referee-panels/edit/"
					+ championshipRefereePanel.getId();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute("message1", e.getMessage());
			LOGGER.info("Exit saveChampionshipRefereePanels EventManagerChampionshipController");

			return "redirect:/eventmanager/championship/" + championshipId + "/manage-referee-panels";
		}

	}

	private List<Judge> getNonSelectedReferees(Integer championshipRefereePanelId) {
		System.out.println(championshipRefereePanelId);
		return judgeService.getNonSelectedEnabledReferees(championshipRefereePanelId);
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-referee-panels/referees/assign/{championshipRefereePanelId}")
	public String assigntradtionalTeamReferees(
			@RequestParam(value = "listReferees", required = false) Integer[] listReferees,
			@PathVariable(name = "championshipRefereePanelId") Integer championshipRefereePanelId,
			@PathVariable(name = "championshipId") Integer championshipId,
			@RequestParam(value = "type", required = false) Integer judgeType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered assigntradtionalTeamReferees EventManagerChampionshipController");
// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String message = null;
		String message1 = null;
		ChampionshipRefereePanels championshipRefereePanels = championshipRefereePanelsService
				.findById(championshipRefereePanelId);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		if (listReferees == null) {
			redirectAttributes.addFlashAttribute("message1", "Select atleast one judge");
			return "redirect:/eventmanager/championship/{championshipId}/manage-referee-panels/edit/{championshipRefereePanelId}";
		} else {
			List<Integer> integerListReferees = new ArrayList<Integer>();
			for (Integer referee : listReferees) {
				boolean isPresent = refereePanelService.existByUser(championshipRefereePanels, referee);
				if (isPresent) {
					integerListReferees.add(referee);
				}

			}
			if (integerListReferees.size() > 0) {
				redirectAttributes.addFlashAttribute("message1", "Judge is already exist in the panel");
				return "redirect:/eventmanager/championship/{championshipId}/manage-referee-panels/edit/{championshipRefereePanelId}";
			}
		}

		if (championshipRefereePanelsService.checkIfEditRefereeAllowed(championshipRefereePanels)) {
			// if referee panel is already assigned to teams & teams are scheduled - add the
			// referees & change the panel in participantTeamParticipants

			List<User> listAssignedReferees = new ArrayList<>();
			Integer judgeCount = refereePanelService.listAllRefereePanels(championshipRefereePanels).size();
			JudgeType judgeTypeObject = judgeTypeService.get(judgeType);
			Integer judgeTypeCount = refereePanelService
					.getRefereePanelsByType(championshipRefereePanels, judgeTypeObject).size();
			Integer limit = -1;
			Integer djudgeLimit = -1;
			Integer tjudgeLimit = -1;
			Integer ejudgeLimit = -1;
			Integer ajudgeLimit = -1;
			Integer scorerLimit = -1;
			Integer chiefJudgeLimit = -1;
			Integer stageManagerLimit = -1;

			AsanaCategory asanaCategory = championshipRefereePanels.getAsanaCategory();
			if (asanaCategory.getId() == 1) {
				limit = judgePanelLimit.getTraditionalSingleLimit();
				djudgeLimit = 5;
				tjudgeLimit = 1;
				ejudgeLimit = 1;
				scorerLimit = 1;
				chiefJudgeLimit = 1;
				stageManagerLimit = 1;
			} else if (asanaCategory.getId() == 2) {
				limit = judgePanelLimit.getArtisticSingleLimit();
				djudgeLimit = 4;
				tjudgeLimit = 2;
				ejudgeLimit = 1;
				scorerLimit = 1;
				ajudgeLimit = 2;
				chiefJudgeLimit = 1;
				stageManagerLimit = 1;
			} else if (asanaCategory.getId() == 3) {
				limit = judgePanelLimit.getArtisticPairLimit();
				djudgeLimit = 4;
				tjudgeLimit = 2;
				ejudgeLimit = 2;
				scorerLimit = 1;
				ajudgeLimit = 2;
				chiefJudgeLimit = 1;
				stageManagerLimit = 1;
			} else if (asanaCategory.getId() == 4) {
				limit = judgePanelLimit.getRhythmicPairLimit();
				djudgeLimit = 4;
				tjudgeLimit = 2;
				ejudgeLimit = 1;
				scorerLimit = 1;
				ajudgeLimit = 2;
				chiefJudgeLimit = 1;
				stageManagerLimit = 1;
			} else if (asanaCategory.getId() == 5) {
				limit = judgePanelLimit.getArtisticGroupLimit();
				djudgeLimit = 4;
				tjudgeLimit = 2;
				ejudgeLimit = 1;
				scorerLimit = 1;
				ajudgeLimit = 2;
				chiefJudgeLimit = 1;
				stageManagerLimit = 1;
			} else {
				limit = judgePanelLimit.getCommonLimit();
			}

			if (listReferees != null) {
				if (((judgeTypeCount + listReferees.length) > djudgeLimit) && (judgeTypeObject.getId() == 2)) {
					message1 = "Only " + djudgeLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if (((judgeTypeCount + listReferees.length) > tjudgeLimit) && (judgeTypeObject.getId() == 4)) {
					message1 = "Only " + tjudgeLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if (((judgeTypeCount + listReferees.length) > ejudgeLimit) && (judgeTypeObject.getId() == 5)) {
					message1 = "Only " + ejudgeLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if (((judgeTypeCount + listReferees.length) > scorerLimit) && (judgeTypeObject.getId() == 6)) {
					message1 = "Only " + scorerLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if (((judgeTypeCount + listReferees.length) > chiefJudgeLimit)
						&& (judgeTypeObject.getId() == 1)) {
					message1 = "Only " + chiefJudgeLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if (((judgeTypeCount + listReferees.length) > stageManagerLimit)
						&& (judgeTypeObject.getId() == 7)) {
					message1 = "Only " + stageManagerLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if (asanaCategory.getId() == 1 && judgeTypeObject.getId() == 3) {
					message1 = "Artistic Judge cannot be added for this category";
				} else if (((judgeTypeCount + listReferees.length) > ajudgeLimit) && (asanaCategory.getId() != 1)
						&& (judgeTypeObject.getId() == 3)) {
					message1 = "Only " + ajudgeLimit + " " + judgeTypeObject.getType()
							+ " can be added for this category";
				} else if ((judgeCount + listReferees.length) <= limit) {
					for (Integer assignListId : listReferees) {
						try {
							Judge judgeUser = judgeService.findById(assignListId);
							RefereesPanels refereesPanels = new RefereesPanels();
							refereesPanels.setChampionshipRefereePanels(championshipRefereePanels);
							refereesPanels.setJudgeUser(judgeUser);
							refereesPanels.setType(judgeTypeObject);
// System.out.println(asana.getName());
							refereePanelService.save(refereesPanels);
							message = "Judges added successfully";

						} catch (JudgeNotFoundException e) {
// TODO Auto-generated catch block
							LOGGER.error("Judge not found!");
							e.printStackTrace();
						}

					}
					// Change the panel in the teams
					championshipRefereePanelsService.modifyParticipantTeamPanels(championshipRefereePanels);
				}
				Integer currentJudgeCount = refereePanelService.listAllRefereePanels(championshipRefereePanels).size();
				
				championshipRefereePanelsService.validateJudgePanel(championshipRefereePanels);
			}
			if (message != null) {
				redirectAttributes.addFlashAttribute("message", message);
			}
			if (message1 != null) {
				redirectAttributes.addFlashAttribute("message1", message1);
			}
			LOGGER.info("Exit assigntradtionalTeamReferees EventManagerChampionshipController");

			return "redirect:/eventmanager/championship/" + championshipId + "/manage-referee-panels/edit/"
					+ championshipRefereePanelId;

		} else {
			message1 = "Unable to add new Referees.";

			return "redirect:/eventmanager/championship/" + championshipId + "/manage-referee-panels/edit/"
					+ championshipRefereePanelId;
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-referee-panels")
	public String listFirstPageChampionshipRefereePanels(Model model,
			@PathVariable("championshipId") Integer championshipId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listFirstPageChampionshipRefereePanels EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (championship.getCreatedBy().equals(currentUser)) {
			LOGGER.info("Exit listFirstPageChampionshipRefereePanels EventManagerChampionshipController");

			return listAllChampionshipRefereePanelsByPage(1, championshipId, model, "name", "asc", "", "", request,
					redirectAttributes);
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-referee-panels/page/{pageNum}")
	public String listAllChampionshipRefereePanelsByPage(@PathVariable(name = "pageNum") int pageNum,
			@PathVariable("championshipId") Integer championshipId, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String asanaCategory, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered listAllChampionshipRefereePanelsByPage EventManagerChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		// List<State> listStates = stateService.listAllStates();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}

		Page<ChampionshipRefereePanels> page = championshipRefereePanelsService.listByChampionshipRefereePanelsPage(
				championshipId, pageNum, sortField, sortDir, name, asanaCategory, championship.getName());
		List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
				.listAllChampionshipRefereePanel(championship);
		listChampionshipRefereePanels = page.getContent();

		long startCount = (pageNum - 1) * championshipRefereePanelsService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipRefereePanelsService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/manage-referee-panels");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", asanaCategory);
		ChampionshipRefereePanels championshipRefereePanels = new ChampionshipRefereePanels();
		model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
		model.addAttribute("pageTitle", "Manage Championship Judge Panels");
		model.addAttribute("championship", championship);
		model.addAttribute("championshipRefereePanels", championshipRefereePanels);
		LOGGER.info("Exit listAllChampionshipRefereePanelsByPage EventManagerChampionshipController");

		return "eventmanager/manage_referee_panels";
	}

//	@GetMapping("/championship/{championshipId}/manage-referee-panels")
//	public String listAllChampionshipRefereePanels(@PathVariable(name = "championshipId") Integer championshipId,
//			Model model, HttpServletRequest request) {
//
//		LOGGER.info("Entered assignChampionshipParticipants ManageChampionshipRefereePanelsController");
//// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = championshipService.findById(championshipId);
//		if (championship.getCreatedBy().equals(currentUser)) {
////ChampionshipRefereePanels championshipRefereePanels=championshipRefereePanelsService.findById();
//			List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
//					.listAllChampionshipRefereePanel(championship);
//			model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
//			model.addAttribute("pageTitle", "Manage Championship Judge Panels");
//			model.addAttribute("championship", championship);
//
//			return "eventmanager/manage_referee_panels";
//		} else {
//			return "error/403";
//		}
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-referee-panels/referees/delete/{referees_panels_id}")
	public String deleteAssignedReferee(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "referees_panels_id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamParticipantsNotFoundException {
		LOGGER.info("Entered deleteAssignedReferee method EventManagerChampionshipController");
// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		ChampionshipRefereePanels championshipRefereePanel = null;
		try {

			RefereesPanels refereesPanels = refereePanelService.get(id);
			championshipRefereePanel = refereesPanels.getChampionshipRefereePanels();
			if (championshipRefereePanelsService.checkIfEditRefereeAllowed(championshipRefereePanel)) {
				// if referee panel is already assigned to teams & teams are scheduled - add the
				// referees & change the panel in participantTeamParticipants

				refereePanelService.delete(id);
				championshipRefereePanelsService.modifyParticipantTeamPanels(championshipRefereePanel);
				championshipRefereePanelsService.validateJudgePanel(championshipRefereePanel);
			} else {
				redirectAttributes.addFlashAttribute("message1", "Unable to remove Referee");
				LOGGER.info("Redirected to manage-referee-panels/edit page");
				return "redirect:/eventmanager/championship/" + championship.getId() + "/manage-referee-panels/edit/"
						+ championshipRefereePanel.getId();
			}
		} catch (ChampionshipRefereePanelsNotFoundException e) {

			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("message", "Referee removed Successfully");
		LOGGER.info("Exit deleteAssignedReferee method EventManagerChampionshipController");
		return "redirect:/eventmanager/championship/" + championship.getId() + "/manage-referee-panels/edit/"
				+ championshipRefereePanel.getId();

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/populate-team-judge-panel")
	public String assignRefereesToTeams(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered assignRefereesToTeams method EventManagerChampionshipController");

//ChampionshipCategory championshipCategory=ChampionshipCategoryService
//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		if (championship.getCreatedBy().equals(currentUser)) {
			List<ParticipantTeam> listParticipantTeam = participantTeamService.listAllParticipantTeams();

			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
			// List<ParticipantTeam> listParticipantTeam =
			// participantTeamService.listAllParticipantTeams();

			System.out.println(listChampionships + "this is championship");
			model.addAttribute("pageTitle", "Assign Team Panels");
			// model.addAttribute("listParticipantTeam", listParticipantTeam);
			model.addAttribute("championship", championship);

			model.addAttribute("listChampionships", listChampionships);
			LOGGER.info("Exit assignRefereesToTeams method EventManagerChampionshipController");

			return "eventmanager/team_judge_assign_form";
		} else {
			return "error/403";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/manage-team/referees/assignToCategory")
	public String saveAssignTeamJudge(@RequestParam(name = "championship") Integer championship,
			@RequestParam(name = "panel") Integer championshipRefereePanelId,
			@RequestParam(name = "championship-category") Integer championshipcategory,
			@RequestParam(name = "round") Integer round,
			@RequestParam(name = "teamList") List<Integer> participantTeam_id, RedirectAttributes redirectAttributes,
			HttpServletRequest request)
			throws IOException, ChampionshipNotFoundException, EntityNotFoundException, UserNotFoundException {
		LOGGER.info("Entered saveAssignTeamJudge method EventManagerChampionshipController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship1 = championshipService.get(championship);
		if (!(championship1.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
				|| championship1.getStatus().equals(ChampionshipStatusEnum.STARTED)
				|| championship1.getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Championship is " + championship1.getStatus().toString() + ". Unable to open championship");
			return "redirect:/eventmanager/championship";
		}
		// for each team - check if there is panel assigned for that round
		List<ParticipantTeam> listParticipantTeams = new ArrayList<>();
		List<ParticipantTeamPanel> listParticipantTeamPanel = new ArrayList<>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<>();
		for (Integer teamId : participantTeam_id) {
			ParticipantTeam participantTeam = participantTeamService.findById(teamId);
			listParticipantTeams.add(participantTeam);
			ParticipantTeamPanel participantTeamPanel = participantTeamPanelService
					.findByParticipantTeamAndRound(teamId, round);
			if (participantTeamPanel != null)
				listParticipantTeamPanel.add(participantTeamPanel);
			ChampionshipParticipantTeams championshipParticipantTeam = championshipParticipantTeamsService
					.findByParticipantTeamAndRound(participantTeam, round);
			if (championshipParticipantTeam != null)
				listChampionshipParticipantTeams.add(championshipParticipantTeam);
		}
		if (!listParticipantTeamPanel.isEmpty()) {
			if (!listChampionshipParticipantTeams.isEmpty()) {
				List<ChampionshipParticipantTeams> listFilteredChampionshipParticipantTeams = listChampionshipParticipantTeams
						.stream().filter(c -> c.getStatus().equals(StatusEnum.SCHEDULED)).collect(Collectors.toList());
				if (listChampionshipParticipantTeams.size() != listFilteredChampionshipParticipantTeams.size()) {
					String message1 = "Unable to assign Judge panels to the teams. Some of the teams are already performing.";
					redirectAttributes.addFlashAttribute("message1", message1);
					return "redirect:/eventmanager/championship/" + championship + "/populate-team-judge-panel";
				}
			}

		}

		try {
			ChampionshipRefereePanels championshipRefereePanels = championshipRefereePanelsService
					.findById(championshipRefereePanelId);
			List<RefereesPanels> listRefereesPanels = championshipRefereePanels.getRefereesPanels();
			String message = null;
			LOGGER.info("participantTeam_id" + participantTeam_id.size());
			ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipcategory);
			Championship championshipid = championshipService.get(championship);
			for (Integer participantTeams : participantTeam_id) {

				ParticipantTeam participantTeam = participantTeamService.findById(participantTeams);
				participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam,
						round);

				for (RefereesPanels referee : listRefereesPanels) {
					ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();
					participantTeamReferees.setParticipantTeam(participantTeam);
					participantTeamReferees.setType(referee.getType());
					participantTeamReferees.setRoundNumber(round);
					participantTeamReferees.setChampionship(championshipid);
					participantTeamReferees.setChampionshipCategory(championshipCategory);
					participantTeamReferees.setJudgeUser(referee.getJudgeUser());

					message = "Panels are added successfully";
					participantTeamRefereesService.save(participantTeamReferees);
				}
				// save it to participantTeamPanel
				ParticipantTeamPanel participantTeamPanel = participantTeamPanelService
						.findByParticipantTeamAndRound(participantTeam.getId(), round);
				if (participantTeamPanel != null) {

					participantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
					participantTeamPanelService.save(participantTeamPanel);
				} else {
					ParticipantTeamPanel newParticipantTeamPanel = new ParticipantTeamPanel();
					newParticipantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
					newParticipantTeamPanel.setParticipantTeamId(participantTeam.getId());
					newParticipantTeamPanel.setRoundNumber(round);
					participantTeamPanelService.save(newParticipantTeamPanel);

				}

			}
			redirectAttributes.addFlashAttribute("message", message + " " + championshipCategory.getCategoryDetail());
			LOGGER.info("Exit saveAssignTeamJudge method EventManagerChampionshipController");

			return "redirect:/eventmanager/championship/" + championship + "/populate-team-judge-panel";
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			String message1 = "Unable to assign Judge panels to the teams";

			redirectAttributes.addFlashAttribute("message1", message1);

			return "redirect:/eventmanager/championship/" + championship + "/populate-team-judge-panel";
		}

	}

	// Manager Team Registrations - Participant Enrollments

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-registered-team")
	public String listFirstPageRegisteredTeams(@PathVariable(name = "championshipId") Integer championshipId,
			Model model, HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered listFirstPageRegisteredTeams ManageTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		return listRegisteredTeamsByPage(championshipId, 1, model, "name", "asc", "", "", "PENDING", "", request);

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-registered-team/page/{pageNum}")
	public String listRegisteredTeamsByPage(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", defaultValue = "", required = false) String chestNumber,
			@RequestParam(name = "keyword3", defaultValue = "PENDING", required = false) String status,
			@RequestParam(name = "keyword4", required = false) String asanaCategory, HttpServletRequest request)
			throws ChampionshipNotFoundException {
		LOGGER.info("Entered listRegisteredTeamsByPage ManageTeamController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		System.out.println(
				"keyword1" + name + "keyword2" + chestNumber + "keyword3" + status + "keyword4" + asanaCategory);
		System.out.println("sortField" + sortField);
		// check if the current user is allowed to view the championship details
		Championship championship = championshipService.findById(championshipId);
		if (championship.getCreatedBy().equals(currentUser)) {

			List<ParticipantTeam> listParticipantTeam = new ArrayList<>();

			Page<ParticipantTeam> page = participantTeamService.eventManagerlistRegisteredTeamByPage(championshipId,
					pageNum, sortField, sortDir, name, chestNumber, status, asanaCategory);
			listParticipantTeam = page.getContent();

			long startCount = (pageNum - 1) * participantTeamService.RECORDS_PER_PAGE + 1;
			long endCount = startCount + participantTeamService.RECORDS_PER_PAGE - 1;

			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
			model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/manage-registered-team");
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
			model.addAttribute("keyword3", status);
			model.addAttribute("keyword4", asanaCategory);
			model.addAttribute("listParticipantTeam", listParticipantTeam);
			model.addAttribute("pageTitle", "Enrolled Teams");
			model.addAttribute("championship", championship);
			return "eventmanager/manage_registered_team";
		} else {
			return "error/403";
		}
	}

	// ***************** Scoreboardimages **********************

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-score-board-image")
	public String listScoreBoardImage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request, RedirectAttributes re) throws IOException {
		LOGGER.info("Entered listScoreBoardImage method -");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		return listAllScoreBoardImagesByPage(championshipId, 1, model, "name", "asc", "", request);
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/manage-score-board-image/page/{pageNum}")
	public String listAllScoreBoardImagesByPage(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name, HttpServletRequest request) {
		LOGGER.info("Entered listAllScoreBoardImagesByPage ScoreBoardImageController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		if (!championship.getCreatedBy().equals(currentUser)) {

			return "error/403";

		}
		Page<Championship> page = championshipService.listEventManagerScoreBoardImageChampionshipsByNotDeleted(
				championshipId, pageNum, sortField, sortDir, name);
		List<Championship> listChampionships = page.getContent();

		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/eventmanager/championship/" + championshipId + "/manage-score-board-image");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Manage Score Board Image");
		model.addAttribute("championship", championship);

		return "eventmanager/manage_scoreboard_image";

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/score-board-image")
	public String scoreBoardImage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered ScoreBoardImage controller");
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);

		List<Championship> listChampionships = new ArrayList<>();
		listChampionships.add(championship);
		if (!(championship.getStatus().equals(ChampionshipStatusEnum.REJECTED)
				|| championship.getStatus().equals(ChampionshipStatusEnum.DELETED)))
			if (!championship.getCreatedBy().equals(currentUser)) {

				return "error/403";

			}

		model.addAttribute("pageTitle", "Add Image");
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("championship", championship);

		LOGGER.info("Redirected to score_board page");
		return "eventmanager/score_board";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/championship/{championshipId}/score-board-image/save")
	public String saveScoreBoardImage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			@RequestParam("scoreBoardImage1") MultipartFile scoreBoardImage1,
			@RequestParam("scoreBoardImage2") MultipartFile scoreBoardImage2,
			@RequestParam("scoreBoardImage3") MultipartFile scoreBoardImage3,
			@RequestParam("scoreBoardImage4") MultipartFile scoreBoardImage4, HttpServletRequest request,
			RedirectAttributes re) throws Exception {
		LOGGER.info("Entered saveScoreBoardImageForm ScoreBoardImageController");

		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String scoreboardImageFileName = "";

		Championship championship = championshipService.get(championshipId);
		if (!championship.getCreatedBy().equals(currentUser)) {

			return "error/403";

		}

		if (scoreBoardImage1.isEmpty() && scoreBoardImage2.isEmpty() && scoreBoardImage3.isEmpty()
				&& scoreBoardImage4.isEmpty()) {
			re.addFlashAttribute("message1", "Upload atleast one image.");
			return "redirect:/eventmanager/championship/" + championshipId + "/manage-score-board-image";
		}

		if (!scoreBoardImage1.isEmpty()) {

			String uploadDir = "scoreboard-image/" + championshipId + "/image1";
			String extension = FilenameUtils.getExtension(scoreBoardImage1.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage1");
			scoreboardImageFileName = "scoreboardimage1." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage1);
			championship.setImage1(scoreboardImageFileName);

		}

		if (!scoreBoardImage2.isEmpty()) {
			String uploadDir = "scoreboard-image/" + championshipId + "/image2";
			String extension = FilenameUtils.getExtension(scoreBoardImage2.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage2");
			scoreboardImageFileName = "scoreboardimage2." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage2);
			championship.setImage2(scoreboardImageFileName);

		}

		if (!scoreBoardImage3.isEmpty()) {
			String uploadDir = "scoreboard-image/" + championshipId + "/image3";
			String extension = FilenameUtils.getExtension(scoreBoardImage3.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage3");
			scoreboardImageFileName = "scoreboardimage3." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage3);
			championship.setImage3(scoreboardImageFileName);
		}

		if (!scoreBoardImage4.isEmpty()) {
			String uploadDir = "scoreboard-image/" + championshipId + "/image4";
			String extension = FilenameUtils.getExtension(scoreBoardImage4.getOriginalFilename());
			FileUploadUtil.deleteExistingFile(uploadDir, "scoreboardimage4");
			scoreboardImageFileName = "scoreboardimage4." + extension;
			FileUploadUtil.saveFile(uploadDir, scoreboardImageFileName, scoreBoardImage4);
			championship.setImage4(scoreboardImageFileName);
		}
		championshipService.update(championship);
		re.addFlashAttribute("message", "Score board image added successfully.");
		model.addAttribute("championship", championship);
		return "redirect:/eventmanager/championship/" + championshipId + "/manage-score-board-image";

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/score-board-image/edit")
	public String editScoreBoardImage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered ScoreBoardImage ManageScoreBoardImageController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Championship championship = championshipService.get(championshipId);

			if (!championship.getCreatedBy().equals(currentUser)) {

				return "error/403";

			}
			if (!championship.getStatus().equals(ChampionshipStatusEnum.DELETED)
					|| !championship.getStatus().equals(ChampionshipStatusEnum.REJECTED)) {
				model.addAttribute("pageTitle", "Edit Score Board Image");
				model.addAttribute("championship", championship);
				return "eventmanager/score_board";

			} else {
				return "error/403";
			}
		} catch (Exception ex) {
			LOGGER.error("scoreBoardImage with given id " + championshipId + "not found!");
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/eventmanager/championship/" + championshipId + "manage-score-board-image";
		}
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/championship/{championshipId}/score-board-image/delete")
	public String deletescoreboardImage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered deleteScoreBoardImage ManageScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String uploadDir = "scoreboard-image/" + championshipId;
		Championship championship = championshipService.findById(championshipId);
		model.addAttribute("championship", championship);
		if (!championship.getCreatedBy().equals(currentUser)) {
			return "error/403";
		}

		try {
			FileUtils.deleteDirectory(new File(uploadDir));
		} catch (IOException e) {

			LOGGER.info(e.getMessage());
		}
		championshipService.clearImages(championshipId);

		redirectAttributes.addFlashAttribute("message",
				championship.getName() + "- Images has been deleted successfully");

		return "redirect:/eventmanager/championship/" + championshipId + "/manage-score-board-image";

	}

}
