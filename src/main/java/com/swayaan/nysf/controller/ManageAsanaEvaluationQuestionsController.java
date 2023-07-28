package com.swayaan.nysf.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaEvaluationQuestionsService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ExecutionCategoryService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.RoleService;
//import com.swayaan.nysf.service.ParticipantTeamAsanaScoringService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageAsanaEvaluationQuestionsController {

	@Autowired
	AsanaEvaluationQuestionsService service;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ExecutionCategoryService executionCategoryService;
	@Autowired
	AsanaService asanaService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	JudgeTypeService judgeTypeService;
	@Autowired
	RoleService roleService;
	// @Autowired ParticipantTeamAsanaScoringService
	// participantTeamAsanaScoringService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageAsanaEvaluationQuestionsController.class);
	private static final Integer ROLE_ADMINISTRATOR = 1;

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-eval-questions")
	public String listFirstPageJudges(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageJudges method -ManageAsanaEvaluationQuestionsController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		LOGGER.info("Exit listFirstPageJudges method -ManageAsanaEvaluationQuestionsController");

		return listAllAsanaEvaluationQuestionsByPage(1, model, "type", "asc", "", "", request);
		
	}
	
	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-eval-questions/page/{pageNum}")
	public String listAllAsanaEvaluationQuestionsByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String asanaCategory ,
			@RequestParam(name = "keyword2", required = false) String type,HttpServletRequest request) {
		LOGGER.info("Entered listAllAsanaEvaluationQuestionsByPage method -ManageAsanaEvaluationQuestionsController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		Page<AsanaEvaluationQuestions> page=service.listByPage(pageNum, sortField, sortDir, asanaCategory, type);
		List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-eval-questions");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1",asanaCategory );
		model.addAttribute("keyword2", type);
		AsanaEvaluationQuestions asanaEvaluationQuestions = new AsanaEvaluationQuestions();
		model.addAttribute("listAsanaEvaluationQuestions", listAsanaEvaluationQuestions);
		model.addAttribute("pageTitle", "Manage Asana Evaluation Questions");
		model.addAttribute("asanaEvaluationQuestions", asanaEvaluationQuestions);
		LOGGER.info("Exit listAllAsanaEvaluationQuestionsByPage method -ManageAsanaEvaluationQuestionsController");

		return "administration/manage_eval_questions";
	}
	

	 /*public String listAllAsanaEvaluationQuestions(Model model, HttpServletRequest request, Role role) {
		LOGGER.info("Entered listAllAsanaEvaluationQuestions ManageAsanaEvaluationQuestionsController");
		// For Navigation history

		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = service.listAllAsanaEvaluationQuestions();

			model.addAttribute("listAsanaEvaluationQuestions", listAsanaEvaluationQuestions);
			model.addAttribute("pageTitle", "Manage Asana Evaluation Questions");

			return "administration/manage_eval_questions";
		}
	} */

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-eval-questions/new")
	public String newAsanaEvaluationQuestions(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			AsanaEvaluationQuestions asanaEvaluationQuestions = new AsanaEvaluationQuestions();
			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
			// List<Championship> listChampionships =
			// championshipService.listAllChampionships();
			List<JudgeType> listJudgeType = judgeTypeService.listAllJudgeType();
			model.addAttribute("asanaEvaluationQuestions", asanaEvaluationQuestions);
			model.addAttribute("pageTitle", "Add Asana Evaluation Questions");
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			// model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("listjudgeType", listJudgeType);
			LOGGER.info("Exit newAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");

			return "administration/asana_evaluation_questions_form";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-eval-questions/save")
	public String saveAsanaEvaluationQuestions(AsanaEvaluationQuestions asanaEvaluationQuestions,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {

		LOGGER.info("Entered saveAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			service.save(asanaEvaluationQuestions);
			redirectAttributes.addFlashAttribute("message",
					"The Asana Evaluation Questions has been saved successfully.");
			LOGGER.info("Exit saveAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");

			return "redirect:/admin/manage-eval-questions";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-eval-questions/edit/{id}")
	public String editAsanaEvaluationQuestions(@PathVariable(name = "id") Integer id, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws EntityNotFoundException {

		LOGGER.info("Entered editAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				AsanaEvaluationQuestions asanaEvaluationQuestions = service.get(id);
				model.addAttribute("asanaEvaluationQuestions", asanaEvaluationQuestions);
				model.addAttribute("pageTitle", "Edit Asana Evaluation Questions");

				// List<Championship> listChampionships =
				// championshipService.listAllChampionships();
				List<JudgeType> listJudgeType = judgeTypeService.listAllJudgeType();
				List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();

				model.addAttribute("listAsanaCategory", listAsanaCategory);
				model.addAttribute("listjudgeType", listJudgeType);
				// model.addAttribute("listChampionshipCategory", listChampionshipCategory);
				LOGGER.info("Exit editAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");

				return "administration/asana_evaluation_questions_form";
			} catch (EntityNotFoundException ex) {
				LOGGER.error("AsanaEvaluationQuestions not found!" + ex.getMessage());
				redirectAttributes.addFlashAttribute("message", "Asana Evaluation Question not found.");
				return "redirect:/admin/manage-eval-questions";
			}
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-eval-questions/delete/{id}")
	public String deleteAsanaEvaluationQuestions(@PathVariable(name = "id") Integer id, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered deleteAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				AsanaEvaluationQuestions asanaEvaluationQuestions = service.get(id);
				// List<ParticipantGroupAsanaScoring> participantGroupAsanaScoring =
				// participantGroupAsanaScoringService.listGroupAsanaByQuestions(asanaEvaluationQuestions);

//			if(!participantGroupAsanaScoring.isEmpty()) {
//				System.out.println("asanaExecutionScore");
//				redirectAttributes.addFlashAttribute("message1", 
//						"The Asana Evaluation Question " + asanaEvaluationQuestions.getId() + " is allocated to participant group asana scoring, Please remove before you delete");
//				return "redirect:/admin/manage-asana-category";
//			}

				service.delete(id);
				redirectAttributes.addFlashAttribute("message", "The Asana Execution Score "
						+ asanaEvaluationQuestions.getId() + " has been deleted successfully");

			} catch (EntityNotFoundException ex) {
				LOGGER.error("AsanaEvaluationQuestions not found!" + ex.getMessage());
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
			LOGGER.info("Exit deleteAsanaEvaluationQuestions method -ManageAsanaEvaluationQuestionsController");

			return "redirect:/admin/manage-eval-questions";
		}
	}
}
