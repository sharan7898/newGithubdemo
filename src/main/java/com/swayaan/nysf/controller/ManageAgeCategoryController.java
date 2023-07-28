package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.EventCategory;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.CompulsoryRoundAsanasNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.EventCategoryService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageAgeCategoryController {

	@Autowired
	AgeCategoryService service;

	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;

	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;

	@Autowired
	EventCategoryService eventCategoryServiceService;
	
	@Autowired
	ParticipantTeamService participantTeamService;
	
	@Autowired
	ChampionshipService championshipService;

	@Autowired
	private UserService userService;

	@Autowired
	private ChampionshipCategoryService championshipCategoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageAgeCategoryController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-agecategory")
	public String listFirstPageAgeCategory(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageAgeCategory method -ManageAgeCategoryController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		LOGGER.info("Exit listFirstPageAgeCategory method -ManageAgeCategoryController");

		return listAllAgeCategoriesByPage(1, model, "title", "asc", "", "", request);
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-agecategory/page/{pageNum}")
	public String listAllAgeCategoriesByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String title,
			@RequestParam(name = "keyword2", required = false) String code,HttpServletRequest request) {
		LOGGER.info("Entered listAllAgeCategoriesByPage method -ManageAgeCategoryController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		

		Page<AgeCategory> page=service.listByPage(pageNum, sortField, sortDir, title, code);
		List<AgeCategory> listAgeCategories = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-agecategory");
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
		LOGGER.info("Exit listAllAgeCategoriesByPage method -ManageAgeCategoryController");
        return "administration/manage_age_category";
	}
	
/*	public String listAllAgeCategories(Model model, HttpServletRequest request) {

		LOGGER.info("Entered listAllAgeCategories ManageAgeCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<AgeCategory> listAgeCategories = service.listAllAgeCategories();
		model.addAttribute("listAgeCategories", listAgeCategories);
		model.addAttribute("pageTitle", "Manage Age Categories");
		return "administration/manage_age_category";
	}*/

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-agecategory/new")
	public String newAgeCategory(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newAgeCategory method -ManageAgeCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		AgeCategory ageCategory = new AgeCategory();
		ageCategory.setEnabled(true);
		model.addAttribute("ageCategory", ageCategory);
		model.addAttribute("pageTitle", "Add Age Category");
		LOGGER.info("Exit newAgeCategory method -ManageAgeCategoryController");
      	return "administration/age_category_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-agecategory/save")
	public String saveAgeCategory(AgeCategory ageCategory, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException {
		LOGGER.info("Entered saveAgeCategory method -ManageAgeCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		AgeCategory checkAgeCategory = service.getAgeCategoryByTitle(ageCategory.getTitle());
		String message = "";		
		String errorMessage = "";

		if (ageCategory.getId() == null) { // if saving new record
			if (checkAgeCategory == null) {
				message = "The Age Category has been saved successfully!";
				
				service.save(ageCategory);

				System.out.println("ageCategory" + ageCategory);
				service.saveExecutionScoresForAgeCategory(ageCategory);
				redirectAttributes.addFlashAttribute("message", message);
				
			} else {
				errorMessage = "The Age Category already exists!";
				redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			}

		} else {

			try {

				AgeCategory existingAgeCategory = service.get(ageCategory.getId());
				if (checkAgeCategory != null) {
					if (checkAgeCategory.getTitle().equals(ageCategory.getTitle())
							&& checkAgeCategory.getId().equals(ageCategory.getId())) {
						existingAgeCategory.setCode(ageCategory.getCode());
						existingAgeCategory.setAgeAbove(ageCategory.getAgeAbove());
						existingAgeCategory.setAgeBelow(ageCategory.getAgeBelow());
						existingAgeCategory.setEnabled(ageCategory.isEnabled());
						service.save(existingAgeCategory);
						message = "The Age category has been updated successfully!";
						redirectAttributes.addFlashAttribute("message", message);

					} else if (!checkAgeCategory.getTitle().equals(ageCategory.getTitle())) {
						existingAgeCategory.setTitle(ageCategory.getTitle());
						existingAgeCategory.setCode(ageCategory.getCode());
						existingAgeCategory.setAgeAbove(ageCategory.getAgeAbove());
						existingAgeCategory.setAgeBelow(ageCategory.getAgeBelow());
						existingAgeCategory.setEnabled(ageCategory.isEnabled());
						message = "The Age category has been updated successfully!";
						service.save(existingAgeCategory);
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
					message = "The Age category has been updated successfully!";
					service.save(existingAgeCategory);
					redirectAttributes.addFlashAttribute("message", message);
				}

			} catch (EntityNotFoundException e) {
				LOGGER.error("No age category found for the id " + ageCategory.getId());
			}
		}
		LOGGER.info("Exit saveAgeCategory method -ManageAgeCategoryController");
         return "redirect:/admin/manage-agecategory";

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-agecategory/edit/{id}")
	public String editAgeCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered editAgeCategory method -ManageAgeCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			AgeCategory ageCategory = service.get(id);
			model.addAttribute("ageCategory", ageCategory);
			model.addAttribute("pageTitle", "Edit Age Category");
			LOGGER.info("Exit editAgeCategory method -ManageAgeCategoryController");
           return "administration/age_category_form";
		} catch (EntityNotFoundException ex) {
			LOGGER.error("Age category with id" + id + "not found" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Age category with id" + id + "not found");
			return "redirect:/admin/manage-agecategory";
		}
	}
	
	public static <T> Predicate<T> distinctByKey(
		    Function<? super T, ?> keyExtractor) {
		  
		    Map<Object, Boolean> seen = new ConcurrentHashMap(); 
		    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
		}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-agecategory/delete/{id}")
	public String deleteAgeCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws CompulsoryRoundAsanasNotFoundException, ParticipantTeamNotFoundException {
		LOGGER.info("Entered deleteAgeCategory method -ManageAgeCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Boolean flag = false;
		try {
			AgeCategory ageCategory = service.get(id);
			List<Championship> listChampionship = championshipService.getChampionshipByOngoingOrCompletedOrDeleted();
			
			for(Championship championship : listChampionship) {
				List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService.getChampionshipCategoryByAgeCategoryandChamionship(ageCategory,championship.getId());
				
				 if(!listChampionshipCategory.isEmpty()) {
					 redirectAttributes.addFlashAttribute("errorMessage", "The Age Category " + ageCategory.getTitle()
								+ " cannot be deleted because it has been already used.");
						return "redirect:/admin/manage-agecategory";
					 
				 }
				
			}
//			List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService.getChampionshipCategoryByAgeCategory(ageCategory);
//			
//			listChampionshipCategory =  listChampionshipCategory.stream()  
//                    .filter(distinctByKey(p -> p.))        // fetching  
//                    .collect(Collectors.toList());  
//			
//			Championship championshipObj = service.get(id);
//			
//			if (championship.getStatus().equals(ChampionshipStatusEnum.ONGOING) || championship.getStatus().equals(ChampionshipStatusEnum.COMPLETED)) {
//				redirectAttributes.addFlashAttribute("message1", "The Championship ID " + id
//						+ " cannot be deleted because championship rounds are ongoing/completed ");
//				return "redirect:/admin/manage-championship";
//			}
			
			// delete from ParticipantTeam
			List<ParticipantTeam> listParticipantTeam = participantTeamService.listAllParticipantTeamsByAgeCategory(ageCategory);
			if (listParticipantTeam != null) {

				for (ParticipantTeam ParticipantTeam : listParticipantTeam) {
					participantTeamService.delete(ParticipantTeam.getId());

				}
			}

			// delete from Championship category
			List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
					.getChampionshipCategoryByAgeCategory(ageCategory);
			if (listChampionshipCategory != null) {

				for (ChampionshipCategory ChampionshipCategory : listChampionshipCategory) {
					championshipCategoryService.delete(ChampionshipCategory.getId());

				}
			}

			// delete from Championship category
			List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas = compulsoryRoundAsanasService
					.listCompulsoryRoundAsanasByAgeCategory(ageCategory);
			if (listCompulsoryRoundAsanas != null) {

				for (CompulsoryRoundAsanas CompulsoryRoundAsanas : listCompulsoryRoundAsanas) {
					compulsoryRoundAsanasService.delete(CompulsoryRoundAsanas.getId());

				}
			}
			// delete from EventCategory
			List<EventCategory> listEventCategory = eventCategoryServiceService
					.getEventCategoryByAgeCategory(ageCategory);
			if (listEventCategory != null) {

				for (EventCategory EventCategory : listEventCategory) {
					eventCategoryServiceService.delete(EventCategory.getId());

				}
			}

			// delete from AsanaExecutionScore
			List<AsanaExecutionScore> listAsanaExecutionScore = asanaExecutionScoreService
					.getExecutionScoresByAgeCategory(ageCategory);
			if (listAsanaExecutionScore != null) {

				for (AsanaExecutionScore execScore : listAsanaExecutionScore) {
					asanaExecutionScoreService.delete(execScore.getId());

				}
			}

			/*
			 * List<User> users = userService.listAll(); for (User u : users) { if
			 * (u.hasRole(role.getName())) { flag = true; break; } }
			 */

			// if (flag == true) {
			// redirectAttributes.addFlashAttribute("message1",
			// "The role " + role.getName() + " cannot be deleted. It has been assigned to
			// the user");
			// } else {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Age category " + ageCategory.getTitle() + " has been deleted successfully");
			// }

		} catch (EntityNotFoundException ex) {
			LOGGER.error("Age category with id" + id + "not found");
			redirectAttributes.addFlashAttribute("errorMessage", "Age category with id" + id + "not found");
		}
		LOGGER.info("Exit deleteAgeCategory method -ManageAgeCategoryController");
       return "redirect:/admin/manage-agecategory";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-agecategory/{id}/enabled/{status}")
	public String updateAgeCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EntityNotFoundException {
		LOGGER.info("Entered updateAgeCategoryEnabledStatus method -ManageAgeCategoryController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		AgeCategory ageCategory = service.get(id);

		service.updateAgeCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Age category " + ageCategory.getTitle() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateAgeCategoryEnabledStatus method -ManageAgeCategoryController");
        return "redirect:/admin/manage-agecategory";
	}

}
