package com.swayaan.nysf.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.EventManagerDTO;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.CompulsoryRoundAsanasNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaEvaluationQuestionsService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipLevelsService;
import com.swayaan.nysf.service.ChampionshipLinkService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.EventManagerService;
import com.swayaan.nysf.service.ParticipantTeamParticipantAsanasStatusService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.RefereePanelsService;
import com.swayaan.nysf.service.ScheduleDaysService;
import com.swayaan.nysf.service.ScheduleFopService;
import com.swayaan.nysf.service.ScheduleService;
import com.swayaan.nysf.service.ScheduleTimeSlotsService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageChampionshipController {

	@Autowired
	ChampionshipService service;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ChampionshipLevelsService championshipLevelsService;

	@Autowired
	ChampionshipRoundsService championshipRoundsService;

	@Autowired
	ScheduleService scheduleService;

	@Autowired
	ScheduleFopService scheduleFopService;

	@Autowired
	ScheduleTimeSlotsService scheduleTimeSlotsService;

	@Autowired
	ScheduleDaysService scheduleDaysService;

	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;

	@Autowired
	ParticipantTeamService participantTeamService;

	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;

	@Autowired
	ChampionshipRefereePanelsService championshipRefereePanelsService;

	@Autowired
	RefereePanelsService refereePanelsService;

	@Autowired
	ChampionshipLinkService championshipLinkSerivce;
	@Autowired
	AsanaEvaluationQuestionsService asanaEvaluationQuestionsService;
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;

	@Autowired
	EventManagerService eventManagerService;
	@Autowired
	UserService userService;
	@Autowired
	CommonEmailUtil emailUtil;

	@Autowired
	SettingService settingService;

	@Value("${no.traditional.category.participants}")
	private Integer noTraditionalCategoryParticipants;
	@Value("${no.artistic.single.category.participants}")
	private Integer noArtisticSingleCategoryParticipants;
	@Value("${no.artistic.pair.category.participants}")
	private Integer noArtisticPairCategoryParticipants;
	@Value("${no.rhythmic.pair.category.participants}")
	private Integer noRhythmicPairCategoryParticipants;
	@Value("${no.artistic.group.category.participants}")
	private Integer noArtisticGroupCategoryParticipants;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageChampionshipController.class);

	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;
	
	private static final int ADMIN_USER_ID = 1;

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship")
	public String listFirstPageChampionships(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageChampionships method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageChampionships method -ManageChampionshipController");

		return listAllChampionshipsByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/page/{pageNum}")
	public String listAllChampionshipsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location, HttpServletRequest request) {
		LOGGER.info("Entered listAllChampionshipsByPage method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		// List<Championship> listChampionships =
		// service.listAllChampionshipsByNotDeleted();
		Page<Championship> page = service.listByPage(pageNum, sortField, sortDir, name, location);
		List<Championship> listChampionship = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
	//	List<Integer> listUsers = userService.getAllEventManagers();
		List<User> listNonAssignedUsers = new ArrayList<>();
		for (Integer user_id : listUsers) {
			User user = null;
			try {
				user = userService.get(user_id);
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listNonAssignedUsers.add(user);
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-championship");
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
		model.addAttribute("listNonAssignedEventManager", listNonAssignedUsers);
		model.addAttribute("listChampionships", listChampionship);
		model.addAttribute("pageTitle", "Manage Championships");
		// Championship championship = new Championship();
		// model.addAttribute("Championship", championship);
		LOGGER.info("Exit listAllChampionshipsByPage method -ManageChampionshipController");

		return "administration/manage_championship";
	}

//	@GetMapping("/manage-championship")
//	public String listAllChampionship(Model model, HttpServletRequest request) {
//		LOGGER.info("Entered listAll Championship ManageChampionshipController");
//		// For Navigation history
//
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		List<Championship> listChampionships = service.listAllChampionshipsByNotDeleted();
//		List<EventManager> listNonAssignedEventManager = eventManagerService.listAll();
//		model.addAttribute("listChampionships", listChampionships);
//		model.addAttribute("listNonAssignedEventManager", listNonAssignedEventManager);
//		model.addAttribute("pageTitle", "Manage Championships");
//
//		return "administration/manage_championship";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/new")
	public String newChampionship(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newChampionship method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = new Championship();
		ChampionshipCategory championshipCategory = new ChampionshipCategory();
		List<ChampionshipLevels> listChampionshipLevels = championshipLevelsService.listAllChampionshipLevels();
		model.addAttribute("listChampionshipLevels", listChampionshipLevels);
		model.addAttribute("championshipCategory", championshipCategory);
		model.addAttribute("championship", championship);
		model.addAttribute("pageTitle", "Add Championship");
		LOGGER.info("Exit newChampionship method -ManageChampionshipController");

		return "administration/add_championship";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/edit/{id}")
	public String editChampionship(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editChampionship method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Championship championship = service.getChampionshipById(id);

			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			List<ChampionshipLevels> listChampionshipLevels = championshipLevelsService.listAllChampionshipLevels();
			ChampionshipCategory championshipCategory = new ChampionshipCategory();
			model.addAttribute("listChampionshipLevels", listChampionshipLevels);
			model.addAttribute("championshipCategory", championshipCategory);
			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("championship", championship);
			model.addAttribute("pageTitle", "Edit Championship");
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			LOGGER.info("Exit editChampionship method -ManageChampionshipController");
			return "administration/add_championship";
		} catch (ChampionshipNotFoundException ex) {
			LOGGER.error("Championship not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/admin/manage-championship";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-championship/save")
	public String saveChampionship(@ModelAttribute("championship") Championship championship, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered saveChampionship method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		String ctxPath = request.getContextPath();
		url = url.replaceFirst(uri, "");
		url = url + ctxPath;
		Boolean newChampionship = false;
		if (championship.getId() == null) {
			newChampionship = true;
		} else {
			newChampionship = false;
		}

		Boolean ChampionshipName = service.checkChampionshipName(championship.getName());

		try {

			if (ChampionshipName) {
				if (championship.getId() == null) {
					redirectAttributes.addFlashAttribute("errorMessage",
							championship.getName() + " is already present");
					return "redirect:/admin/manage-championship";
				} else {
					service.save(championship);
					redirectAttributes.addFlashAttribute("message",
							"Championship   " + championship.getName() + " has been updated successfully");
					return "redirect:/admin/manage-championship";
				}

			} else {
				Championship savedChampionship = service.save(championship);
				if (newChampionship == true) {
					service.saveChampionshipLink(savedChampionship, url);
				}
				redirectAttributes.addFlashAttribute("message",
						"Championship " + championship.getName() + " has been added/updated successfully");
			}

			return "redirect:/admin/manage-championship/edit/" + championship.getId();

		} catch (Exception ex) {
			LOGGER.error("Championship not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			LOGGER.info("Exit saveChampionship method -ManageChampionshipController");

			return "redirect:/admin/manage-championship";
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/delete/{id}")
	public String deleteChampionship(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ChampionshipNotFoundException,
			ParticipantTeamNotFoundException, CompulsoryRoundAsanasNotFoundException,
			ChampionshipRefereePanelsNotFoundException, EntityNotFoundException {
		LOGGER.info("Entered deleteChampionship method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship =null;
		try {
			 championship = service.get(id);

			if (championship.getStatus().equals(ChampionshipStatusEnum.ONGOING)
					|| championship.getStatus().equals(ChampionshipStatusEnum.COMPLETED)) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Ongoing/Completed championships cannot be deleted ");
				return "redirect:/admin/manage-championship";
			}
			championship.setStatus(ChampionshipStatusEnum.DELETED);
			service.updateStatus(championship);

//			for (ChampionshipRounds championshipRounds : listChampionshipRounds) {
//				RoundStatusEnum status = championshipRounds.getStatus();
//				if (status.equals(RoundStatusEnum.ONGOING) || status.equals(RoundStatusEnum.COMPLETED)) {
//					redirectAttributes.addFlashAttribute("errorMessage", "The Championship ID " + id
//							+ " cannot be deleted because championship rounds are ongoing/completed ");
//					return "redirect:/admin/manage-championship";
//				}
//			}

			// delete if event is schedule

			// delete from schedule

//			List<Schedule> listSchedule = scheduleService.getScheduleByChampionship(championship);
//			if (listSchedule != null) {
//				for (Schedule schedule : listSchedule) {
//					try {
//						//delete from scheduleFop
//						List<ScheduleFop> listScheduleFop = scheduleFopService.getScheduleFopForSchedule(schedule);
//								if (listScheduleFop != null) {
//									for (ScheduleFop scheduleFop : listScheduleFop) {
//										try {
//											scheduleFopService.delete(scheduleFop.getId());
//										} catch (EntityNotFoundException e) {
//
//											e.printStackTrace();
//										}
//									}
//								}
//							//delete from scheduleTimeSlots
//							List<ScheduleTimeSlots> listScheduleTimeSlots = scheduleTimeSlotsService.getTimeSlotsForSchedule(schedule);
//										if (listScheduleTimeSlots != null) {
//											for (ScheduleTimeSlots scheduleTimeSlot : listScheduleTimeSlots) {
//												try {
//													scheduleTimeSlotsService.delete(scheduleTimeSlot.getId());
//												} catch (EntityNotFoundException e) {
//
//													e.printStackTrace();
//												}
//											}
//										}
//						
//						
//						scheduleService.delete(schedule.getId());
//					} catch (EntityNotFoundException e) {
//
//						e.printStackTrace();
//					}
//				}
//			}
//			
//			// delete from Participant Team Round Total Scoring
//						List<ParticipantTeamRoundTotalScoring> listParticipantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//								.getParticipantTeamRoundTotalScoreForChampionship(championship);
//						if (listParticipantTeamRoundTotalScoring != null) {
//							for (ParticipantTeamRoundTotalScoring ParticipantTeamRoundTotalScoring : listParticipantTeamRoundTotalScoring) {
//								participantTeamRoundTotalScoringService.delete(ParticipantTeamRoundTotalScoring.getId());
//							}
//						}
//
//			// delete from ParticipantTeamReferees
//			List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeamRefereesService
//					.getParticipantTeamRefereesByChampionship(championship);
//			if (listParticipantTeamReferees != null) {
//				for (ParticipantTeamReferees participantTeamReferees : listParticipantTeamReferees) {
//
//					participantTeamRefereesService.delete(participantTeamReferees.getId());
//
//				}
//			}
//			
//			// delete from ChampionshipRefereePanels
//						List<ChampionshipRefereePanels> listChampionshipRefereePanel = championshipRefereePanelsService
//								.listAllChampionshipRefereePanel(championship);
//
//						if (listChampionshipRefereePanel != null) {
//							for (ChampionshipRefereePanels championshipRefereePanels : listChampionshipRefereePanel) {
//
//								championshipRefereePanelsService.delete(championshipRefereePanels.getId());
//
//							}
//						}
//
//						// delete from RefereePanels
//						List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
//								.listAllChampionshipRefereePanel(championship);
//
//						if (listChampionshipRefereePanels != null) {
//							for (ChampionshipRefereePanels championshipRefereePanels : listChampionshipRefereePanels) {
//								List<RefereesPanels> listRefereePanels = refereePanelsService
//										.listAllRefereePanels(championshipRefereePanels);
//								for (RefereesPanels refereePanels : listRefereePanels) {
//									refereePanelsService.delete(refereePanels.getId());
//								}
//
//							}
//						}
//
//			// delete from ParticipantTeam
//			List<ParticipantTeam> listParticipantTeam = participantTeamService
//					.listAllChampionshipParticipantTeams(championship);
//			if (!listParticipantTeam.isEmpty()) {
//				for (ParticipantTeam participantTeam : listParticipantTeam) {
//					List<ParticipantTeamParticipantAsanasStatus> listParticipantTeamParticipantAsanasStatus = participantTeamParticipantAsanasStatusService
//							.findAllByParticipantTeam(participantTeam);
//					
//					if (!listParticipantTeamParticipantAsanasStatus.isEmpty()) {
//						for (ParticipantTeamParticipantAsanasStatus ParticipantTeamParticipantAsanasStatus : listParticipantTeamParticipantAsanasStatus) {
//
//							participantTeamParticipantAsanasStatusService.delete(ParticipantTeamParticipantAsanasStatus.getId());
//
//						}
//					}
//
//					participantTeamService.delete(participantTeam.getId());
//
//				}
//			}
//
//			// delete from CompulsoryRoundAsanas
//			List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas = compulsoryRoundAsanasService
//					.getCompulsoryAsanasByChampionship(championship);
//			if (listCompulsoryRoundAsanas != null) {
//				for (CompulsoryRoundAsanas compulsoryRoundAsanas : listCompulsoryRoundAsanas) {
//
//					compulsoryRoundAsanasService.delete(compulsoryRoundAsanas.getId());
//
//				}
//			}
//
//			
//
//			// delete from RefereePanels
//
//			if (listChampionshipRefereePanels != null) {
//				for (ChampionshipRefereePanels championshipRefereePanels : listChampionshipRefereePanels) {
//
//					championshipRefereePanelsService.delete(championshipRefereePanels.getId());
//
//				}
//			}
//			List<ChampionshipRounds> listChampionshipRounds = championshipRoundsService
//					.findAllByChampionship(championship);
//
//			// delete from championship rounds
//			if (listChampionshipRounds != null) {
//				for (ChampionshipRounds championshipRounds : listChampionshipRounds) {
//					championshipRoundsService.delete(championshipRounds.getId());
//				}
//
//			}
//
//			// delete from AsanaEvaluationQuestions for championship
////						List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
////								.listAllAsanaEvaluationQuestionsByChampionship(championship);
////						if (listAsanaEvaluationQuestions != null) {
////							for (AsanaEvaluationQuestions asanaEvaluationQuestions : listAsanaEvaluationQuestions) {
////								
////								asanaEvaluationQuestionsService.delete(asanaEvaluationQuestions.getId());
////								
////							}
////						}
////						
//			// delete from championship category
//			List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
//					.getChampionshipCategoryByChampionship(championship);
//			if (listChampionshipCategory != null) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					championshipCategoryService.delete(championshipCategory.getId());
//				}
//			}
//
////			// delete from championship link
//			List<ChampionshipLink> listChampionshipLink = championshipLinkSerivce
//					.getChampionshipLinkByChampionship(championship);
//			if (listChampionshipLink != null) {
//				for (ChampionshipLink championshipLink : listChampionshipLink) {
//					championshipLinkSerivce.delete(championshipLink.getId());
//				}
//			}

			// delete championship
			// service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Championship " + championship.getName() + " has been deleted successfully");

		} catch (ChampionshipNotFoundException ex) {

			LOGGER.error("Championship not found!" +ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete championship");
		} catch (Exception e) {
			LOGGER.error("Unable to edit championship!" + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete championship");

		}
		LOGGER.info("Exit deleteChampionship method -ManageChampionshipController");

		return "redirect:/admin/manage-championship";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship-category/{championshipId}/new")
	public String newChampionshipCategory(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered newChampionshipCategory method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipCategory championshipCategory = new ChampionshipCategory();
		Championship championship = service.get(championshipId);
		List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		model.addAttribute("championshipCategory", championshipCategory);
		model.addAttribute("listAsanaCategory", listAsanaCategory);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("championship", championship);
		model.addAttribute("pageTitle", "Add Championship Category");
		LOGGER.info("Exit newChampionshipCategory method -ManageChampionshipController");

		return "administration/add_championship_category";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-championship/championship-category/assign/{id}")
	public String saveChampionshipCategories(@PathVariable(name = "id") Integer id,
			ChampionshipCategory championshipCategory, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered saveChampionshipCategories method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<ChampionshipCategory> listChampionshipCategory = new ArrayList<>();
		Championship championship = null;
		try {
			championship = service.findById(id);

			if (championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				listChampionshipCategory = championship.getChampionshipCategory();
				if (championshipCategory.getId() == null) {
					if (!listChampionshipCategory.isEmpty()) {
						for (ChampionshipCategory c : listChampionshipCategory) {
							if (c.getAsanaCategory().equals(championshipCategory.getAsanaCategory())
									&& c.getCategory().equals(championshipCategory.getCategory())
									&& c.getGender().equals(championshipCategory.getGender())) {
								redirectAttributes.addFlashAttribute("errorMessage",
										"The Championship already have this category");
								return "redirect:/admin/manage-championship/edit/" + championship.getId();
							}
						}

					}

					ChampionshipCategory championshipCategoryNew = new ChampionshipCategory();
					championshipCategoryNew.setAsanaCategory(championshipCategory.getAsanaCategory());
					championshipCategoryNew.setCategory(championshipCategory.getCategory());
					championshipCategoryNew.setGender(championshipCategory.getGender());
					championshipCategoryNew.setNoOfRounds(championshipCategory.getNoOfRounds());
					if (championshipCategory.getAsanaCategory().getId() == TRADITIONAL_ASANA_CATEGORY_ID) {
						championshipCategoryNew.setNoOfParticipants(noTraditionalCategoryParticipants);
					} else if (championshipCategory.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
						championshipCategoryNew.setNoOfParticipants(noArtisticSingleCategoryParticipants);
					} else if (championshipCategory.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
						championshipCategoryNew.setNoOfParticipants(noArtisticPairCategoryParticipants);
					} else if (championshipCategory.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
						championshipCategoryNew.setNoOfParticipants(noRhythmicPairCategoryParticipants);
					} else if (championshipCategory.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
						championshipCategoryNew.setNoOfParticipants(noArtisticGroupCategoryParticipants);
					}

					listChampionshipCategory.add(championshipCategoryNew);
					championship.setChampionshipCategory(listChampionshipCategory);
					Championship championship1 = service.save(championship);
					championshipRoundsService.saveChampionshipRoundsForChampionshipCategory(championship1);
					redirectAttributes.addFlashAttribute("message",
							"The Championship category has been added successfully.");
				} else {
					if (!listChampionshipCategory.isEmpty()) {
						for (ChampionshipCategory c : listChampionshipCategory) {
							if (c.getAsanaCategory().equals(championshipCategory.getAsanaCategory())
									&& c.getCategory().equals(championshipCategory.getCategory())
									&& c.getGender().equals(championshipCategory.getGender())
									&& !c.getId().equals(championshipCategory.getId())) {
								redirectAttributes.addFlashAttribute("errorMessage",
										"The Championship already have this category");
								return "redirect:/admin/manage-championship/edit/" + championship.getId();
							}
						}

					}
					championshipCategoryService.save(championshipCategory, championship);
					redirectAttributes.addFlashAttribute("message",
							"The Championship category has been updated successfully.");
				}

				return "redirect:/admin/manage-championship/edit/" + championship.getId();
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "Unable to add championship category");
				return "redirect:/admin/manage-championship/edit/" + championship.getId();
			}

		} catch (Exception e) {
			LOGGER.error("Error creating championship category!" + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to add championship category");
			LOGGER.info("Exit saveChampionshipCategories method -ManageChampionshipController");

			return "redirect:/admin/manage-championship/edit/" + championship.getId();
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/{championshipId}/championship-category/edit/{championshipCategoryId}")
	public String editAssignedCategories(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "championshipCategoryId") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EntityNotFoundException, ChampionshipNotFoundException {
		LOGGER.info("Entered editAssignedCategories method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Championship championship = service.get(championshipId);
			ChampionshipCategory championshipCategory = championshipCategoryService.get(id);
			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			model.addAttribute("championship", championship);
			model.addAttribute("championshipCategory", championshipCategory);
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			model.addAttribute("listAgeCategory", listAgeCategory);
			LOGGER.info("Exit editAssignedCategories method -ManageChampionshipController");
			return "administration/add_championship_category";
		} catch (ChampionshipNotFoundException ex) {
			LOGGER.error("Championship not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/admin/manage-championship";
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/{championshipId}/championship-category/delete/{championshipCategoryId}")
	public String deleteAssignedCategories(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "championshipCategoryId") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered deleteAssignedCategories method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = service.findById(championshipId);
		if (championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
			try {
				// delete from championship rounds
				championshipRoundsService.delete(championshipId, id);
			} catch (Exception e) {
				LOGGER.error("championship category Not Found delete" +e.getMessage());

				redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete championship category.");
				return "redirect:/admin/manage-championship/edit/" + championshipId;
			}

			championshipCategoryService.deleteById(id);

			redirectAttributes.addFlashAttribute("message", "Championship category removed Successfully");
			LOGGER.info("Exit deleteAssignedCategories method -ManageChampionshipController");
			return "redirect:/admin/manage-championship/edit/" + championshipId;
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete championship category.");
			return "redirect:/admin/manage-championship/edit/" + championshipId;
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-championship/{championshipId}/link")
	public String generateLink(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ChampionshipNotFoundException {
		Championship championship = service.get(championshipId);
		ChampionshipLink championshiplink = championshipLinkSerivce.getByChampionship(championship);

		model.addAttribute("championship", championship);
		model.addAttribute("championshiplink", championshiplink);

		return "administration/generate_link_modal";
	}

//	// Add eventManager to team
//	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
//	@PostMapping("/manage-championship/event-manager/assign")
//	public String assignTeamParticipants(@RequestParam(value = "eventManager", required = false) Integer[] eventManager,
//			@RequestParam(name = "championshipid") Integer championship_id, RedirectAttributes redirectAttributes,
//			HttpServletRequest request) throws EventManagerNotFoundException, UserNotFoundException {
//		LOGGER.info("Entered assignTeamParticipants method -ManageChampionshipController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		Championship championship = service.findById(championship_id);
//		User userObj = null;
//		Integer eventManagerLimit = 1;
//		String errorMessage = null;
//		String message = null;
//
//		if (!championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
//			errorMessage = "Unable to add event manager for this championship because the championship is ongoing/completed";
//			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//		}
//
//		if (eventManager == null) {
//			errorMessage = "Select atleast one event manager for the championship";
//			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//			return "redirect:/admin/manage-championship";
//		} else if (eventManager.length > eventManagerLimit) {
//			errorMessage = "Only " + eventManagerLimit + " event manager can be added for this championship";
//			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//			return "redirect:/admin/manage-championship";
//		}
//
//		for (Integer manager : eventManager) {
//			userObj = userService.get(manager);
//		}
//		User eventManagerUser = userService.get(userObj.getId());
//
//		if (eventManager.length == eventManagerLimit) {
//			championship.setCreatedBy(eventManagerUser);
//			try {
//				service.save(championship);
//			} catch (Exception e) {
//
//				redirectAttributes.addFlashAttribute("errorMessage", "Unable to assign event manager. Championship is already started.");
//				LOGGER.error("Unable to assign event manager. Championship is not in the scheduled status" + e.getMessage());
//
//				return "redirect:/admin/manage-championship";
//			}
//			message = "Event Manager has been assigned to the championship successfully";
//		}
//
//		redirectAttributes.addFlashAttribute("message", message);
//		LOGGER.info("Exit assignTeamParticipants method -ManageChampionshipController");
//		return "redirect:/admin/manage-championship";
//
//	}
	
	// Add eventManager to team
		@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
		@GetMapping("/manage-championship/{championshipId}/event-manager/assign")
		public String assignEventmanager(@RequestParam(value = "eventManager", required = false) Integer[] eventManager,
				@PathVariable(name = "championshipId") Integer championship_id, Model model, RedirectAttributes redirectAttributes,
				HttpServletRequest request) throws EventManagerNotFoundException, UserNotFoundException {
			LOGGER.info("Entered assignTeamParticipants method -ManageChampionshipController");
			// For Navigation history
			String mappedUrl = request.getRequestURI();
			User currentUser = CommonUtil.getCurrentUser();
			CommonUtil.setNavigationHistory(mappedUrl, currentUser);
			Championship championship = service.findById(championship_id);

			List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
			//	List<Integer> listUsers = userService.getAllEventManagers();
				List<EventManagerDTO> listNonAssignedUsers = new ArrayList<>();
				for (Integer user_id : listUsers) {
					User user = null;
					try {
						user = userService.get(user_id);
					} catch (UserNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listNonAssignedUsers.add(new EventManagerDTO(user.getId(),user.getFullName(),user.getUserName()));
				}
			model.addAttribute("listNonAssignedUsers", listNonAssignedUsers);
			model.addAttribute("currentChampionship", championship);
			//model.addAttribute("currentChampionshipConducted", championship.getCreatedBy().getFirstName());
			LOGGER.info("Exit assignEventmanager get method -ManageChampionshipController");
			return "administration/assign_eventmanager_modal";

		}
		
		// Add eventManager to team
		@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
		@PostMapping("/manage-championship/{championshipId}/event-manager/assign")
		public String saveEventManager(@RequestParam(value = "eventManager", required = false) Integer[] eventManager,
				@PathVariable(name = "championshipId") Integer championship_id, RedirectAttributes redirectAttributes,
				HttpServletRequest request) throws Exception {
			LOGGER.info("Entered assignTeamParticipants method -ManageChampionshipController");
			// For Navigation history
			String mappedUrl = request.getRequestURI();
			User currentUser = CommonUtil.getCurrentUser();
			CommonUtil.setNavigationHistory(mappedUrl, currentUser);
			Championship championship = service.findById(championship_id);
			User userObj = null;
			Integer eventManagerLimit = 1;
			String errorMessage = null;
			String message = null;
	
			if (!championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				errorMessage = "Unable to add event manager for this championship because the championship is ongoing/completed";
				redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
				return "redirect:/admin/manage-championship";
			}
	
			if (eventManager == null) {
				User adminUser = userService.get(ADMIN_USER_ID);
				championship.setCreatedBy(adminUser);
				service.save(championship);
				message = "Admin has been assigned to the championship successfully";
				redirectAttributes.addFlashAttribute("message", message);
				return "redirect:/admin/manage-championship";
			} else if (eventManager.length > eventManagerLimit) {
				errorMessage = "Only " + eventManagerLimit + " event manager can be added for this championship";
				redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
				return "redirect:/admin/manage-championship";
			}
	
			for (Integer manager : eventManager) {
				userObj = userService.get(manager);
			}
			User eventManagerUser = userService.get(userObj.getId());
	
			if (eventManager.length == eventManagerLimit) {
				championship.setCreatedBy(eventManagerUser);
				try {
					service.save(championship);
				} catch (Exception e) {
	
					redirectAttributes.addFlashAttribute("errorMessage", "Unable to assign event manager. Championship is already started.");
					LOGGER.error("Unable to assign event manager. Championship is not in the scheduled status" + e.getMessage());
	
					return "redirect:/admin/manage-championship";
				}
				message = "Event Manager has been assigned to the championship successfully";
			}
	
			redirectAttributes.addFlashAttribute("message", message);
			LOGGER.info("Exit assignTeamParticipants method -ManageChampionshipController");
			return "redirect:/admin/manage-championship";
	
		}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/send-email/championship/{id}")
	public String sendEmailToEventManager(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		LOGGER.info("Entered sendEmailToEventManager method -ManageChampionshipController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Championship championship = service.get(id);
			Setting CHAMPIONSHIP_SUBJECT = settingService
					.getMailTemplateValueForSubjectAndContent("CHAMPIONSHIP_SUBJECT");
			Setting CHAMPIONSHIP_CONTENT = settingService
					.getMailTemplateValueForSubjectAndContent("CHAMPIONSHIP_CONTENT");

			if (championship.getCreatedBy().getRoleId() == 4) {
				String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
				emailUtil.sendEmailForSuccessfulEventRegistration(request, championship, CHAMPIONSHIP_SUBJECT,
						CHAMPIONSHIP_CONTENT,nysfPortalLink);
				redirectAttributes.addFlashAttribute("message", "The Email has been sent to the Event Manager"
						+ championship.getCreatedBy().getFullName() + " successfully");
				return "redirect:/admin/manage-championship";
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Event manager is not assigned to this championship. Assign the event manager to send the email ");
				return "redirect:/admin/manage-championship";
			}

		} catch (ChampionshipNotFoundException ex) {
			LOGGER.error("EventManager not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit sendEmailToEventManager method -ManageChampionshipController");

		return "redirect:/admin/manage-championship";

	}

}
