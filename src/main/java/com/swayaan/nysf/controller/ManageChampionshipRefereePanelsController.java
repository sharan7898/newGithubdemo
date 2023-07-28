package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipRefereePanelsEnum;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgePanelLimit;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamParticipantsNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.RefereePanelsService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageChampionshipRefereePanelsController {

	@Autowired
	ChampionshipRefereePanelsService service;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	UserService userService;
	@Autowired
	RefereePanelsService refereePanelService;
	@Autowired
	JudgePanelLimit judgePanelLimit;
	@Autowired
	JudgeTypeService judgeTypeService;
	@Autowired
	JudgeService judgeService;
	@Autowired
	ChampionshipRefereePanelsService championshipRefereePanelsService;
	@Autowired
	AgeCategoryService ageCategoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageChampionshipRefereePanelsController.class);
	
	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;
	
	private static final int TRADITIONAL_CATEGORY_JUDGE_COUNT = 9;
	private static final int ARTISTIC_SINGLE_CATEGORY_JUDGE_COUNT = 11;
	private static final int ARTISTIC_PAIR_CATEGORY_JUDGE_COUNT = 12;
	private static final int RHYTHMIC_PAIR_CATEGORY_JUDGE_COUNT = 11;
	private static final int ARTISTIC_GROUP_CATEGORY_JUDGE_COUNT = 11;
	

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-referee-panels")
	public String listFirstRefreePanels(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstRefreePanels method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstRefreePanels method -ManageRefereeChampionshipRefereePanelsController");

		return listAllPanelsByPage(1, model, "name", "asc", "", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-referee-panels/page/{pageNum}")
	public String listAllPanelsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String championshipName,
			@RequestParam(name = "keyword3", required = false) String asanaCategory, HttpServletRequest request) {
		LOGGER.info("Entered listAllPanelsByPage method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Page<ChampionshipRefereePanels> page = service.listByPage(pageNum, sortField, sortDir, name, 
				championshipName, asanaCategory);
		List<ChampionshipRefereePanels> listChampionshipRefereePanels = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-referee-panels");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", championshipName);
		model.addAttribute("keyword3", asanaCategory);
		ChampionshipRefereePanels championshipRefereePanels = new ChampionshipRefereePanels();
		model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
		model.addAttribute("pageTitle", "Manage Championship Judge Panels");
		model.addAttribute("championshipRefereePanels", championshipRefereePanels);
		LOGGER.info("Exit listAllPanelsByPage method -ManageRefereeChampionshipRefereePanelsController");

		return "administration/manage_referee_panels";
	}

//	@GetMapping("/manage-referee-panels")
//	public String listAllChampionshipRefereePanels(Model model, HttpServletRequest request) {
//
//		LOGGER.info("Entered assignChampionshipParticipants ManageChampionshipRefereePanelsController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		List<ChampionshipRefereePanels> listChampionshipRefereePanels = service
//				.listAllChampionshipRefereePanelsByNotDeleted();
//		model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
//		model.addAttribute("pageTitle", "Manage Championship Judge Panels");
//
//		return "administration/manage_referee_panels";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-referee-panels/new")
	public String newChampionshipRefereePanels(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newChampionshipRefereePanels method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipRefereePanels championshipRefereePanel = new ChampionshipRefereePanels();

		List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();

		model.addAttribute("championshipRefereePanel", championshipRefereePanel);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("listAsanaCategory", listAsanaCategory);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("pageTitle", "Add Championship Judge Panels");
		LOGGER.info("Exit newChampionshipRefereePanels method -ManageRefereeChampionshipRefereePanelsController");

		return "administration/referee_panels_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-referee-panels/edit/{id}")
	public String editChampionshipRefereePanel(@PathVariable(name = "id") Integer id, Model model,
			@RequestParam(value = "type", required = false) Integer judgeType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered editChampionshipRefereePanel method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			ChampionshipRefereePanels championshipRefereePanel = service.get(id);

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
			LOGGER.info("Exit editChampionshipRefereePanel method -ManageRefereeChampionshipRefereePanelsController");
			return "administration/referee_panels_form";
		} catch (ChampionshipRefereePanelsNotFoundException ex) {
			LOGGER.error("ChampionshipRefereePanels not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/admin/manage-referee-panels";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-referee-panels/save")
	public String saveChampionshipRefereePanels(
			@ModelAttribute("championshipRefereePanel") ChampionshipRefereePanels championshipRefereePanel, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered saveChampionshipRefereePanels method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			service.save(championshipRefereePanel);
			redirectAttributes.addFlashAttribute("message", "The panels " + championshipRefereePanel.getName() +" created successfully");

			return "redirect:/admin/manage-referee-panels/edit/" + championshipRefereePanel.getId();
		} catch (Exception e) {
			LOGGER.error("RefreePanels Not FoundExceptio"+e.getMessage());
			redirectAttributes.addFlashAttribute("message1", e.getMessage());
			LOGGER.info("Exit saveChampionshipRefereePanels method -ManageRefereeChampionshipRefereePanelsController");

			return "redirect:/admin/manage-referee-panels";
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-referee-panels/delete/{id}")
	public String deleteChampionshipRefereePanels(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ChampionshipRefereePanelsNotFoundException {
		LOGGER.info("Entered deleteChampionshipRefereePanels method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			ChampionshipRefereePanels championshipRefereePanels = service.get(id);
			
			if(!service.checkIfEditAllowed(championshipRefereePanels)){
				redirectAttributes.addFlashAttribute("message1",
						"Unable to delete the panel "+championshipRefereePanels.getName());
				return "redirect:/admin/manage-referee-panels";
				
			}

			List<RefereesPanels> refereesPanels = refereePanelService.listAllRefereePanels(championshipRefereePanels);

			if (!refereesPanels.isEmpty()) {
				redirectAttributes.addFlashAttribute("message1",
						"Referees are added into the panel. Please remove before you Delete");
				return "redirect:/admin/manage-referee-panels";
			}

			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The Panel  " + championshipRefereePanels.getName() + " has been deleted successfully");
		} catch (ChampionshipRefereePanelsNotFoundException ex) {
			LOGGER.error("ChampionshipRefereePanels not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteChampionshipRefereePanels method -ManageRefereeChampionshipRefereePanelsController");

		return "redirect:/admin/manage-referee-panels";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-referee-panels/referees/assign/{championshipRefereePanelId}")
	public String assignTeamReferees(@RequestParam(value = "listReferees", required = false) Integer[] listReferees,
			@PathVariable(name = "championshipRefereePanelId") Integer championshipRefereePanelId,
			@RequestParam(value = "type", required = false) Integer judgeType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered assignTeamReferees method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String message = null;
		String message1 = null;
		ChampionshipRefereePanels championshipRefereePanels = service.findById(championshipRefereePanelId);

		if (listReferees == null) {
			redirectAttributes.addFlashAttribute("message1", "Select atleast one judge");
			return "redirect:/admin/manage-referee-panels/edit/{championshipRefereePanelId}";
		} else {
			List<Integer> integerListReferees = new ArrayList<Integer>();
			for (Integer referee : listReferees) {
				boolean isPresent = refereePanelService.existByUser(championshipRefereePanels, referee);
				if (isPresent) {
					integerListReferees.add(referee);
				}

			}
			if(integerListReferees.size() > 0) {
				redirectAttributes.addFlashAttribute("message1", "Judge is already exist in the panel");
				return "redirect:/admin/manage-referee-panels/edit/{championshipRefereePanelId}";
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
							LOGGER.error("Judge not found!" + e.getMessage());
							e.printStackTrace();
						}

					}
					// Change the panel in the teams
					championshipRefereePanelsService.modifyParticipantTeamPanels(championshipRefereePanels);
				}
				championshipRefereePanelsService.validateJudgePanel(championshipRefereePanels);
				

			}
			if (message != null) {
				redirectAttributes.addFlashAttribute("message", message);
			}
			if (message1 != null) {
				redirectAttributes.addFlashAttribute("message1", message1);
			}

			return "redirect:/admin/manage-referee-panels/edit/{championshipRefereePanelId}";
		} else {
			message1 = "Unable to add new Referees. The Championship Round is already started.";
			redirectAttributes.addFlashAttribute("message1", message1);
			LOGGER.info("Exit assignTeamReferees method -ManageRefereeChampionshipRefereePanelsController");

			return "redirect:/admin/manage-referee-panels/edit/{championshipRefereePanelId}";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-referee-panels/referees/delete/{referees_panels_id}")
	public String deleteAssignedReferee(@PathVariable(name = "referees_panels_id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamParticipantsNotFoundException {
		LOGGER.info("Entered deleteAssignedReferee method -ManageRefereeChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
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
				redirectAttributes.addFlashAttribute("message1", "Unable to remove Referee. The Championship Round is already started.");
				LOGGER.info("Redirected to manage-referee-panels/edit page - ManageRefereeChampionshipRefereePanelsController");
				return "redirect:/admin/manage-referee-panels/edit/" + championshipRefereePanel.getId();
			}

		} catch (ChampionshipRefereePanelsNotFoundException e) {
			redirectAttributes.addFlashAttribute("message1", "Championship RefereePanel not found");
			return "redirect:/admin/manage-referee-panels";
		}

		redirectAttributes.addFlashAttribute("message", "Referee removed Successfully");
		LOGGER.info("Redirected to manage-referee-panels/edit page");
		LOGGER.info("Exit deleteAssignedReferee method -ManageRefereeChampionshipRefereePanelsController");

		return "redirect:/admin/manage-referee-panels/edit/" + championshipRefereePanel.getId();

	}

	private List<Judge> getNonSelectedReferees(Integer championshipRefereePanelId) {
		System.out.println(championshipRefereePanelId);
		return judgeService.getNonSelectedEnabledReferees(championshipRefereePanelId);
	}

}
