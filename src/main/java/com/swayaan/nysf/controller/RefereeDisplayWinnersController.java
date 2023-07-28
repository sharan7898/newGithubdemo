package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/referee")
public class RefereeDisplayWinnersController {

	@Autowired
	UserService userService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefereeDisplayWinnersController.class);

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/display-winners")
	public String getWinnersHomePage(Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		LOGGER.info("Entered getWinnersHomePage method -RefereeDisplayWinnersController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Display Winners");
		LOGGER.info("Exit getWinnersHomePage method -RefereeDisplayWinnersController");

		return "referee/display_winners_home";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/display-winners/proceed")
	public String proceedToGetCategories(@RequestParam(name = "championship") Integer id, Model model,
			RedirectAttributes redirectAttributes,HttpServletRequest request) {
		LOGGER.info("Entered proceedToGetCategories method -RefereeDisplayWinnersController");

		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		Championship championship = championshipService.findById(id);
		List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
		if (listChampionshipCategory.size() != 0) {
			return "redirect:/referee/display-winners/categories/" + id;
		} else {
			model.addAttribute("errorMessage", "No Categories found for the Chosen Championship!");
			LOGGER.info("Exit proceedToGetCategories method -RefereeDisplayWinnersController");

			return "redirect:/referee/display-winners";
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/display-winners/categories/{championshipId}")
	public String getCategoriesPage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered getCategoriesPage method -RefereeDisplayWinnersController");

		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		Championship championship = championshipService.findById(championshipId);

		List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
		if (listChampionshipCategory.size() != 0) {
			model.addAttribute("championshipId", championshipId);
			model.addAttribute("championshipName", championship.getName());
			model.addAttribute("listChampionshipCategory", listChampionshipCategory);
			return "referee/display_winners_categories";
		} else {
			model.addAttribute("errorMessage", "No Categories found for the Chosen Championship!");
			LOGGER.info("Exit getCategoriesPage method -RefereeDisplayWinnersController");

			return "redirect:/referee/display-winners";
		}
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/display-winners/championship/{championshipId}/category/{championshipCategoryId}/round/{round}")
	public String getCategoriesWinners(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,HttpServletRequest request,
			@PathVariable(name = "round") Integer round, Model model) throws EntityNotFoundException {
		LOGGER.info("Entered getCategoriesWinners method -RefereeDisplayWinnersController");

		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
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
			return "referee/display_selected_category_winners";
		}
		model.addAttribute("pageTitle", "Winners Championship : " +championship.getName()+" | " + championshipCategory.getCategory().getTitle() +" | "+championshipCategory.getAsanaCategory().getName() + " | " + championshipCategory.getGender());
		model.addAttribute("listWinners", listWinners);
		LOGGER.info("Exit getCategoriesWinners method -RefereeDisplayWinnersController");

		return "referee/display_selected_category_winners";
	}

	private User getCurrentUser() {
		String username;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		System.out.println(username);
		User user = userService.getByEmail(username);
		return user;
	}

}
