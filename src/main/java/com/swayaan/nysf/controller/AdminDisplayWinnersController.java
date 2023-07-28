package com.swayaan.nysf.controller;

import java.util.ArrayList;
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

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class AdminDisplayWinnersController {

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminDisplayWinnersController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/display-winners")
	public String getWinnersHomePage(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered getWinnersHomePage method   AdminDisplayWinnersController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Display Winners");
		LOGGER.info("Exit getWinnersHomePage method   AdminDisplayWinnersController");
	return "administration/display_winners_home";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/display-winners/proceed")
	public String proceedToGetCategories(@RequestParam(name = "championship") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered proceedToGetCategories method   AdminDisplayWinnersController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(id);
		List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
		if (listChampionshipCategory.size() != 0) {
			return "redirect:/admin/display-winners/categories/" + id;
		} else {
			model.addAttribute("errorMessage", "No Categories found for the Chosen Championship!");
			LOGGER.info("Exit proceedToGetCategories method  -AdminDisplayWinnersController");
               return "redirect:/admin/display-winners";
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/display-winners/categories/{championshipId}")
	public String getCategoriesPage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered getCategoriesPage method -AdminDisplayWinnersController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		return getCategoriesByPage(championshipId, 1, model, "asanaCategory", "asc", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/display-winners/categories/{championshipId}/page/{pageNum}")
	public String getCategoriesByPage(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String asanaCategory,
			@RequestParam(name = "keyword2", required = false) String ageCategory, HttpServletRequest request) {
		LOGGER.info("Entered getCategoriesByPage method -AdminDisplayWinnersController");
		Championship championship = championshipService.findById(championshipId);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Page<ChampionshipCategory> page;
//		if (championship.getCreatedBy().equals(currentUser)) {
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
		model.addAttribute("moduleURL", "/admin/display-winners/categories/" + championshipId);
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
		LOGGER.info("Exit getCategoriesByPage method -AdminDisplayWinnersController");
      	return "administration/display_winners_categories";

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/display-winners/championship/{championshipId}/category/{championshipCategoryId}/round/{round}")
	public String getCategoriesWinners(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, Model model, HttpServletRequest request)
			throws EntityNotFoundException {
		LOGGER.info("Entered getCategoriesWinners method   -AdminDisplayWinnersController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(championshipId);
		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
		List<ParticipantTeamRoundTotalScoring> listWinners = new ArrayList<>();
		if (championshipRounds.getStatus().equals(RoundStatusEnum.COMPLETED)) {
			listWinners = participantTeamRoundTotalScoringService
					.findByChampionshipAndChampionshipRoundsAndWinner(championship, championshipRounds, true);

		} else {
			model.addAttribute("errorMessage", "The Championship is still not started or Ongoing..!");
			return "administration/display_selected_category_winners";
		}
		model.addAttribute("pageTitle",
				"Winners Championship : " + championship.getName() + " | "
						+ championshipCategory.getCategory().getTitle() + " | "
						+ championshipCategory.getAsanaCategory().getName() + " | " + championshipCategory.getGender());
		model.addAttribute("listWinners", listWinners);
		LOGGER.info("Exit getCategoriesWinners method   -AdminDisplayWinnersController");

		return "administration/display_selected_category_winners";
	}

}
