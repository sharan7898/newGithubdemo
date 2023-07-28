package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.ExecutionCategory;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ExecutionCategoryService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageAsanaExecutionScoreController {

	@Autowired
	AsanaExecutionScoreService service;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ExecutionCategoryService executionCategoryService;
	@Autowired
	AsanaService asanaService;
	@Autowired
	private UserService userService;
	@Autowired
	RefereesPanelsService refereesPanelsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageAsanaExecutionScoreController.class);
	private static final Integer ROLE_ADMINISTRATOR = 1;

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-exec-score")
	public String listFirstPageManageExecutionScore(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageManageExecutionScore method -ManageAsanaExecutionScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageManageExecutionScore method -ManageAsanaExecutionScoreController");

		return listAllExecutionScoreByPage(1, model, "asana", "asc", "", "", "", "","", request);

	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-exec-score/page/{pageNum}")
	public String listAllExecutionScoreByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String asana,
			@RequestParam(name = "keyword2", required = false) String code,
			@RequestParam(name = "keyword3", required = false) String asanaCategory,
			@RequestParam(name = "keyword4", required = false) String category,
            @RequestParam(name = "keyword5", required = false) String gender, HttpServletRequest request) {
		LOGGER.info("Entered listAllExecutionScoreByPage method -ManageAsanaExecutionScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

		Page<AsanaExecutionScore> page = service.listByPage(pageNum, sortField, sortDir, asana, code,
				asanaCategory,category, gender);
		List<AsanaExecutionScore> listAsanaExecutionScores = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-exec-score");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", asana);
		model.addAttribute("keyword2", code);
		model.addAttribute("keyword3", asanaCategory);
		model.addAttribute("keyword4", category);

		model.addAttribute("keyword5", gender);
		AsanaExecutionScore asanaExecutionScore = new AsanaExecutionScore();
		model.addAttribute("listAsanaExecutionScores", listAsanaExecutionScores);
		model.addAttribute("pageTitle", "Manage Asana Execution Scores");
		model.addAttribute("asanaExecutionScore", asanaExecutionScore);
		LOGGER.info("Exit listAllExecutionScoreByPage method -ManageAsanaExecutionScoreController");
       return "administration/manage_exec_score";
	}
	}

/*	public String listAllAsanaExecutionScores(Model model, HttpServletRequest request, Role role) {

		LOGGER.info("Entered listAllAsanaExecutionScores ManageAsanaExecutionScoreController");
		// For Navigation history

		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			List<AsanaExecutionScore> listAsanaExecutionScores = service.listAllAsanaExecutionScores();
			model.addAttribute("listAsanaExecutionScores", listAsanaExecutionScores);
			model.addAttribute("pageTitle", "Manage Asana Execution Scores");
			return "administration/manage_exec_score";
		}
	} */

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-exec-score/new")
	public String newAsanaExecutionScore(Model model, HttpServletRequest request) {

		LOGGER.info("Entered newAsanaExecutionScore method -ManageAsanaExecutionScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			AsanaExecutionScore asanaExecutionScore = new AsanaExecutionScore();
			List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
			List<Asana> listAsanas = asanaService.listAllAsanas();
			List<ExecutionCategory> listExecCategories = executionCategoryService.listAllExecutionCategories();
			List<AgeCategory> listAgeCategories = ageCategoryService.listAll();

			model.addAttribute("asanaExecutionScore", asanaExecutionScore);
			model.addAttribute("pageTitle", "Add Asana Execution Score");
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("listAgeCategories", listAgeCategories);
			model.addAttribute("listAsanas", listAsanas);
			model.addAttribute("listExecCategories", listExecCategories);
			LOGGER.info("Exit newAsanaExecutionScore method -ManageAsanaExecutionScoreController");

			return "administration/asana_execution_score_form";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-exec-score/save")
	public String saveAsanaExecutionScore(AsanaExecutionScore asanaExecutionScore,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {

		LOGGER.info("Entered saveAsanaExecutionScore method -ManageAsanaExecutionScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			service.save(asanaExecutionScore);
			redirectAttributes.addFlashAttribute("message", "The Asana Execution Score has been saved successfully.");
			LOGGER.info("Exit saveAsanaExecutionScore method -ManageAsanaExecutionScoreController");

			return "redirect:/admin/manage-exec-score";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-exec-score/edit/{id}")
	public String editAsanaExecutionScore(@PathVariable(name = "id") Integer id, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws EntityNotFoundException {

		LOGGER.info("Entered editAsanaExecutionScore method -ManageAsanaExecutionScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				AsanaExecutionScore asanaExecutionScore = service.get(id);
				List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
				List<Asana> listAsanas = asanaService.listAllAsanas();
				List<ExecutionCategory> listExecCategories = executionCategoryService.listAllExecutionCategories();
				List<AgeCategory> listAgeCategories = ageCategoryService.listAll();

				model.addAttribute("asanaExecutionScore", asanaExecutionScore);
				model.addAttribute("pageTitle", "Edit Asana Execution Score");
				model.addAttribute("listCategories", listCategories);
				model.addAttribute("listAgeCategories", listAgeCategories);
				model.addAttribute("listAsanas", listAsanas);
				model.addAttribute("listExecCategories", listExecCategories);
				LOGGER.info("Exit editAsanaExecutionScore method -ManageAsanaExecutionScoreController");

				return "administration/asana_execution_score_form";
			} catch (EntityNotFoundException ex) {
				LOGGER.error("AsanaExecutionScore not found!" + ex.getMessage());
				redirectAttributes.addFlashAttribute("message", "Asana Execution Score not found.");
				return "redirect:/admin/manage-exec-score";
			}
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-exec-score/delete/{id}")
	public String deleteAsanaExecutionScore(@PathVariable(name = "id") Integer id, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered deleteAsanaExecutionScore method -ManageAsanaExecutionScoreController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				AsanaExecutionScore asanaExecutionScore = service.get(id);
				service.delete(id);
				redirectAttributes.addFlashAttribute("message",
						"The Asana Execution Score " + asanaExecutionScore.getId() + " has been deleted successfully");

			} catch (EntityNotFoundException ex) {
				LOGGER.error("AsanaExecutionScore not found!" + ex.getMessage());
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
			LOGGER.info("Exit deleteAsanaExecutionScore method -ManageAsanaExecutionScoreController");

			return "redirect:/admin/manage-exec-score";
		}
	}

	private User getCurrentUser() {
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		LOGGER.info(username);
		User user = userService.getByEmail(username);
		return user;

	}

}
