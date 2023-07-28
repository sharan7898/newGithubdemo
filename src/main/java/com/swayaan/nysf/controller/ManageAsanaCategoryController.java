package com.swayaan.nysf.controller;

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

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.EventCategory;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.CompulsoryRoundAsanasNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.EventCategoryService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageAsanaCategoryController {

	@Autowired
	AsanaCategoryService service;

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;

	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;

	@Autowired
	EventCategoryService eventCategoryServiceService;

	@Autowired
	ParticipantTeamService participantTeamService;

	// @Autowired CompulsoryRoundAsanasService compulsoryRoundAsanasService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageAsanaCategoryController.class);

	private static final Integer ROLE_ADMINISTRATOR = 1;

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana-category")
	public String listFirstPageAsanaCategory(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageAsanaCategory method -ManageAsanaCategoryController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			LOGGER.info("Exit listFirstPageAsanaCategory method -ManageAsanaCategoryController");
        return listAllAsanaCategoriesByPage(1, model, "name", "asc", "", "", request);
		
	}
	}
	
	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana-category/page/{pageNum}")
	public String listAllAsanaCategoriesByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String code,HttpServletRequest request) {
		LOGGER.info("Entered listAllAsanaCategoriesByPage method -ManageAsanaCategoryController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

		Page<AsanaCategory> page=service.listByPage(pageNum, sortField, sortDir, name, code);
		List<AsanaCategory> listAsanaCategory = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-asana-category");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", code);
		AsanaCategory asanaCategory = new AsanaCategory();
		model.addAttribute("listCategories", listAsanaCategory);
		model.addAttribute("pageTitle", "Manage Asana Categories");
		model.addAttribute("asanaCategory", asanaCategory);
		LOGGER.info("Exit listAllAsanaCategoriesByPage method -ManageAsanaCategoryController");
         return "administration/manage_asana_category";
	}
	}
	
/*	public String listAllAsanaCategories(Model model, HttpServletRequest request, Role role) {
		LOGGER.info("Entered listAllAsanaCategories ManageAsanaCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {
			List<AsanaCategory> listCategories = service.listAllAsanaCategories();
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Manage Asana Categories");

			return "administration/manage_asana_category";
		}
	} */

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana-category/delete/{id}")
	public String deleteAsanaCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws AsanaCategoryNotFoundException,
			ParticipantTeamNotFoundException, EntityNotFoundException, CompulsoryRoundAsanasNotFoundException {
		LOGGER.info("Entered deleteAsanaCategory method -ManageAsanaCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				AsanaCategory asanaCategory = service.getAsanaCategoryById(id);
				List<Championship> listChampionship = championshipService
						.getChampionshipByOngoingOrCompletedOrDeleted();

				for (Championship championship : listChampionship) {
					List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
							.getChampionshipCategoryByAsanaCategoryandChamionship(asanaCategory, championship.getId());

					if (!listChampionshipCategory.isEmpty()) {
						redirectAttributes.addFlashAttribute("message1", "The Asana Category " + asanaCategory.getName()
								+ " cannot be deleted because it has been already used.");
						return "redirect:/admin/manage-category";

					}

				}

				// delete from ParticipantTeam
				List<ParticipantTeam> listParticipantTeam = participantTeamService
						.listAllParticipantTeamsByAsanaCategory(asanaCategory);
				if (listParticipantTeam != null) {

					for (ParticipantTeam ParticipantTeam : listParticipantTeam) {
						participantTeamService.delete(ParticipantTeam.getId());

					}
				}

				// delete from Championship category
				List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
						.getChampionshipCategoryByAsanaCategory(asanaCategory);
				if (listChampionshipCategory != null) {

					for (ChampionshipCategory ChampionshipCategory : listChampionshipCategory) {
						championshipCategoryService.delete(ChampionshipCategory.getId());

					}
				}

				// delete from Championship category
				List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas = compulsoryRoundAsanasService
						.listCompulsoryRoundAsanasByAsanaCategory(asanaCategory);
				if (listCompulsoryRoundAsanas != null) {

					for (CompulsoryRoundAsanas CompulsoryRoundAsanas : listCompulsoryRoundAsanas) {
						compulsoryRoundAsanasService.delete(CompulsoryRoundAsanas.getId());

					}
				}
				// delete from EventCategory
				List<EventCategory> listEventCategory = eventCategoryServiceService
						.getEventCategoryByAsanaCategory(asanaCategory);
				if (listEventCategory != null) {

					for (EventCategory EventCategory : listEventCategory) {
						eventCategoryServiceService.delete(EventCategory.getId());

					}
				}

				// delete from AsanaExecutionScore
				List<AsanaExecutionScore> listAsanaExecutionScore = asanaExecutionScoreService
						.getExecutionScoresByAsanaCategory(asanaCategory);
				if (listAsanaExecutionScore != null) {

					for (AsanaExecutionScore execScore : listAsanaExecutionScore) {
						asanaExecutionScoreService.delete(execScore.getId());

					}
				}

//			if(!compulsoryRoundAsanas.isEmpty()) {
//				System.out.println("compulsoryRoundAsanas");
//				redirectAttributes.addFlashAttribute("message1", 
//						"The Asana Category " + asanaCategory.getName() + " is allocated to compulsory round asanas, Please remove before you delete");
//				return "redirect:/admin/manage-asana-category";
//			}

//			if(!participantGroup.isEmpty()) {
//				System.out.println("participantGroup");
//				redirectAttributes.addFlashAttribute("message1", 
//						"The Asana Category " + asanaCategory.getName() + " is allocated to participant group, Please remove before you delete");
//				return "redirect:/admin/manage-asana-category";
//			}

				service.delete(id);
				redirectAttributes.addFlashAttribute("message",
						"The Asana Category  " + asanaCategory.getName()+ " has been deleted successfully");
			} catch (AsanaCategoryNotFoundException ex) {
				LOGGER.error("AsanaCategoryNotFoundException not found!" + ex.getMessage());
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
			LOGGER.info("Exit deleteAsanaCategory method -ManageAsanaCategoryController");
          return "redirect:/admin/manage-asana-category";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana-category/new")
	public String newAsanaCategory(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newAsanaCategory method -ManageAsanaCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			AsanaCategory asanaCategory = new AsanaCategory();
			model.addAttribute("asanaCategory", asanaCategory);
			model.addAttribute("pageTitle", "Add Asana Category");
			LOGGER.info("Exit newAsanaCategory method -ManageAsanaCategoryController");
          return "administration/add_asana_category";
		}
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@PostMapping("/manage-asana-category/save")
	public String saveAsanaCategory(@ModelAttribute("asanaCategory") AsanaCategory asanaCategory, Model model,
			HttpServletRequest request,RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered saveAsanaCategory method -ManageAsanaCategoryController");
		try {
			// For Navigation history
			String mappedUrl = request.getRequestURI();
			User currentUser = CommonUtil.getCurrentUser();
			CommonUtil.setNavigationHistory(mappedUrl, currentUser);
			if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
				return "error/403";
			} else {

				service.save(asanaCategory);
				redirectAttributes.addFlashAttribute("message",
						"The Asana Category  " + asanaCategory.getName()+ " has been saved successfully");
				List<AsanaCategory> listCategories = service.listAllAsanaCategories();
				model.addAttribute("listCategories", listCategories);
				model.addAttribute("pageTitle", "Manage Asana Category");
				LOGGER.info("Exit saveAsanaCategory method -ManageAsanaCategoryController");
                return "redirect:/admin/manage-asana-category";

			}
		} catch (Exception e) {
			LOGGER.error("Exception found!" + e.getMessage());
			throw e;
		}

	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/manage-asana-category/edit/{id}")
	public String editAsanaCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editAsanaCategory method -ManageAsanaCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (currentUser.getRoleId() != ROLE_ADMINISTRATOR) {
			return "error/403";
		} else {

			try {
				AsanaCategory asanaCategory = service.getAsanaCategoryById(id);

				model.addAttribute("asanaCategory", asanaCategory);
				model.addAttribute("pageTitle", "Edit Asana Category");
				LOGGER.info("Exit editAsanaCategory method -ManageAsanaCategoryController");
				return "administration/add_asana_category";
			} catch (AsanaCategoryNotFoundException ex) {

				redirectAttributes.addFlashAttribute("message", ex.getMessage());
				LOGGER.error("AsanaCategory not found!" + ex.getMessage());
				return "redirect:/admin/manage-asana-category";
			}
		}
	}
}
