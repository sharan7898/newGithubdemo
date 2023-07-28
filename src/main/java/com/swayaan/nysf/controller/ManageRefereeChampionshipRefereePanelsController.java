package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgePanelLimit;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamParticipantsNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.RefereePanelsService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/referee")
public class ManageRefereeChampionshipRefereePanelsController {

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
	RefereesPanelsService refereesPanelsService;
	@Autowired
	JudgeService judgeService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ManageRefereeChampionshipRefereePanelsController.class);
	
//	@GetMapping("/manage-referee-panels")
//	public String listFirstPageJudges(Model model, HttpServletRequest request) {
//		LOGGER.info("Entered listAllRefereePanels ManageRefereeChampionshipRefereePanelsController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		return listAllPanelsByPage(1, model, "name", "asc", "", "", "", request);
//
//	}
//
//	@GetMapping("/manage-referee-panels/page/{pageNum}")
//	public String listAllPanelsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
//			@RequestParam(name = "sortField", required = false) String sortField,
//			@RequestParam(name = "sortDir", required = false) String sortDir,
//			@RequestParam(name = "keyword1", required = false) String name,
//			@RequestParam(name = "keyword2", required = false) String championshipName,
//			@RequestParam(name = "keyword3", required = false) String asanaCategory, HttpServletRequest request) {
//		LOGGER.info("Entered listAllRefereePanels ManageRefereeChampionshipRefereePanelsController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		Page<ChampionshipRefereePanels> page = service.listByPage(pageNum, sortField, sortDir, name, 
//				championshipName, asanaCategory);
//		List<ChampionshipRefereePanels> listChampionshipRefereePanels = page.getContent();
//
//		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
//		long endCount = startCount + service.RECORDS_PER_PAGE - 1;
//
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//		model.addAttribute("moduleURL", "/referee/manage-referee-panels");
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword1", name);
//		model.addAttribute("keyword2", championshipName);
//		model.addAttribute("keyword3", asanaCategory);
//		ChampionshipRefereePanels championshipRefereePanels = new ChampionshipRefereePanels();
//		model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
//		model.addAttribute("pageTitle", "Manage Championship Judge Panels");
//		model.addAttribute("championshipRefereePanels", championshipRefereePanels);
//		return "referee/manage_referee_panels";
//	}


	@GetMapping("/manage-referee-panels")
	public String listAllChampionshipRefereePanels(Model model, HttpServletRequest request)
			throws JudgeNotFoundException {

		LOGGER.info("Entered assignChampionshipParticipants method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		LOGGER.info("currentUser = " + currentUser.getFullName() + " and " + "email id = " + currentUser.getEmail());
		List<ChampionshipRefereePanels> listChampionshipRefereePanels = service.listAllChampionshipRefereePanels();
		model.addAttribute("listChampionshipRefereePanels", listChampionshipRefereePanels);
		model.addAttribute("pageTitle", "Manage Championship Judge Panels");
		LOGGER.info("Exit assignChampionshipParticipants method -ManageChampionshipRefereePanelsController");

		return "referee/manage_referee_panels";
	}

	@GetMapping("/manage-referee-panels/new")
	public String newChampionshipRefereePanels(Model model, HttpServletRequest request) throws JudgeNotFoundException {
		LOGGER.info("Entered newChampionshipRefereePanels method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipRefereePanels championshipRefereePanel = new ChampionshipRefereePanels();

		List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();

		model.addAttribute("championshipRefereePanel", championshipRefereePanel);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Add Championship Judge Panels");
		LOGGER.info("Exit newChampionshipRefereePanels method -ManageChampionshipRefereePanelsController");

		return "referee/referee_panels_form";
	}

	@GetMapping("/manage-referee-panels/edit/{id}")
	public String editChampionshipRefereePanel(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws JudgeNotFoundException {
		LOGGER.info("Entered editChampionshipRefereePanel method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			ChampionshipRefereePanels championshipRefereePanel = service.get(id);

			List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();

			List<Judge> listNonSelectedReferrees = getNonSelectedReferees(championshipRefereePanel.getId());

			List<RefereesPanels> listAssignedReferees = refereePanelService
					.listAllRefereePanels(championshipRefereePanel);

			model.addAttribute("championshipRefereePanel", championshipRefereePanel);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("listNonSelectedReferrees", listNonSelectedReferrees);
			model.addAttribute("listAssignedReferees", listAssignedReferees);
			model.addAttribute("pageTitle", "Edit Championship Judge Panel");
			LOGGER.info("Exit editChampionshipRefereePanel method -ManageChampionshipRefereePanelsController");
			return "referee/referee_panels_form";
		} catch (ChampionshipRefereePanelsNotFoundException ex) {
			LOGGER.error("ChampionshipRefereePanels not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/referee/manage-referee-panels";
		}
	}

	@PostMapping("/manage-referee-panels/save")
	public String saveChampionshipRefereePanels(
			@ModelAttribute("championshipRefereePanel") ChampionshipRefereePanels championshipRefereePanel, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered saveChampionshipRefereePanels method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			service.save(championshipRefereePanel);
			return "redirect:/referee/manage-referee-panels/edit/" + championshipRefereePanel.getId();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute("message1", e.getMessage());
			LOGGER.info("Exit saveChampionshipRefereePanels method -ManageChampionshipRefereePanelsController");

			return "redirect:/referee/manage-referee-panels";
		}

	}

	@GetMapping("/manage-referee-panels/delete/{id}")
	public String deleteChampionshipRefereePanels(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ChampionshipRefereePanelsNotFoundException {
		LOGGER.info("Entered deleteChampionshipRefereePanels method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			ChampionshipRefereePanels championshipRefereePanels = service.get(id);

			List<RefereesPanels> refereesPanels = refereePanelService.listAllRefereePanels(championshipRefereePanels);

			if (!refereesPanels.isEmpty()) {
				System.out.println("refereesPanels");
				redirectAttributes.addFlashAttribute("message1",
						"Referees are added into the panel. Please remove before you Delete");
				return "redirect:/referee/manage-referee-panels";
			}

			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The Panel ID " + id + " has been deleted successfully");
		} catch (ChampionshipRefereePanelsNotFoundException ex) {
			LOGGER.error("ChampionshipRefereePanels not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteChampionshipRefereePanels method -ManageChampionshipRefereePanelsController");

		return "redirect:/referee/manage-referee-panels";
	}

	@PostMapping("/manage-referee-panels/referees/assign/{championshipRefereePanelId}")
	public String assigntradtionalTeamReferees(
			@RequestParam(value = "listReferees", required = false) Integer[] listReferees,
			@PathVariable(name = "championshipRefereePanelId") Integer championshipRefereePanelId,
			@RequestParam(value = "type", required = false) Integer judgeType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered assigntradtionalTeamReferees method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String message = null;
		ChampionshipRefereePanels championshipRefereePanels = service.findById(championshipRefereePanelId);

		List<User> listAssignedReferees = new ArrayList<>();
		Integer judgeCount = refereePanelService.listAllRefereePanels(championshipRefereePanels).size();
		Integer limit = -1;
		JudgeType judgeTypeObject = judgeTypeService.get(judgeType);
		AsanaCategory asanaCategory = championshipRefereePanels.getAsanaCategory();
		if (asanaCategory.getId() == 1) {
			limit = judgePanelLimit.getTraditionalSingleLimit();
		} else if (asanaCategory.getId() == 2) {
			limit = judgePanelLimit.getArtisticSingleLimit();
		} else if (asanaCategory.getId() == 3) {
			limit = judgePanelLimit.getArtisticPairLimit();
		} else if (asanaCategory.getId() == 4) {
			limit = judgePanelLimit.getRhythmicPairLimit();
		} else if (asanaCategory.getId() == 5) {
			limit = judgePanelLimit.getArtisticGroupLimit();
		} else {
			limit = judgePanelLimit.getCommonLimit();
		}

		if (listReferees != null) {
			if ((judgeCount + listReferees.length) > limit) {
				message = "Only " + limit + " Judges can be added for this category";
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
						LOGGER.error("Judge not found!" + e.getMessage());
						e.printStackTrace();
					}

				}
			}
		}
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit assigntradtionalTeamReferees method -ManageChampionshipRefereePanelsController");

		return "redirect:/referee/manage-referee-panels/edit/{championshipRefereePanelId}";
	}

	@GetMapping("/manage-referee-panels/referees/delete/{referees_panels_id}")
	public String deleteAssignedReferee(@PathVariable(name = "referees_panels_id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, ParticipantTeamParticipantsNotFoundException {
		LOGGER.info("Entered deleteAssignedReferee method -ManageChampionshipRefereePanelsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Integer refereePanelId = -1;
		try {

			RefereesPanels refereesPanels = refereePanelService.get(id);
			refereePanelId = refereesPanels.getChampionshipRefereePanels().getId();

			refereePanelService.delete(id);
		} catch (ChampionshipRefereePanelsNotFoundException e) {

			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("message", "Referee removed Successfully");
		LOGGER.info("Exit deleteAssignedReferee method -ManageChampionshipRefereePanelsController");
		return "redirect:/referee/manage-referee-panels/edit/" + refereePanelId;

	}

	private List<Judge> getNonSelectedReferees(Integer championshipRefereePanelId) {
		return judgeService.getNonSelectedEnabledReferees(championshipRefereePanelId);
	}

}
