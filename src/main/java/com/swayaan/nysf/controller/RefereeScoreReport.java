package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanaScoring;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/referee")
public class RefereeScoreReport {

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
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	ParticipantTeamAsanasScoringService participantTeamAsanasScoringService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefereeScoreReport.class);

	@GetMapping("score-report/select-championship")
	public String selectChampionships(Model model,HttpServletRequest request) {
		LOGGER.info("Entered selectChampionships method - RefereeScoreReportController ");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Manage Championship");
		LOGGER.info("Exit selectChampionships method - RefereeScoreReportController ");

		return "referee/list_championships";

	}

	@GetMapping("score-report/championship/{championshipId}/getChampionshipCategories")
	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId,
			Model model,HttpServletRequest request) {
		LOGGER.info("Entered listChampionshipCategories method - RefereeScoreReportController ");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		Championship championship;
		try {
			championship = championshipService.getChampionshipById(championshipId);
		} catch (ChampionshipNotFoundException e) {
			model.addAttribute("errorMessage", "Championship Not Found.");
			return "referee/list_championships";
		}
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
		LOGGER.info("Exit listChampionshipCategories method - RefereeScoreReportController ");

		return "referee/championship_category_form1";
	}

	@GetMapping("score-report/championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}")
	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,HttpServletRequest request,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException {
		LOGGER.info("Entered listAllTeamsForChampionhsip method - RefereeScoreReportController ");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		RoundStatusEnum status = RoundStatusEnum.ONGOING;
		StatusEnum participantTeamStatus = StatusEnum.SCHEDULED;
		Championship championship;
		model.addAttribute("pageTitle", "Manage Championship");
		try {
			championship = championshipService.get(id);
			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
			AgeCategory ageCategory = ageCategoryService.get(ageCategoryId);
			ChampionshipCategory championshipCategory = championshipCategoryService
					.getChampionshipCategoryForAllConditions(id, asanaCategoryId, ageCategoryId, gender);
			Integer championshipCategoryId = championshipCategory.getId();
			ChampionshipRounds championshipRounds = championshipRoundsService
					.getByRoundAndChampionshipAndChampionshipCategory(round, championship, championshipCategory);
			LOGGER.info("championshipRounds" + championshipRounds);
			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
			if (championshipRounds != null) {
				listChampionshipParticipantTeams = championshipParticipantTeamsService
						.getTeamsByChampionshipRounds(championshipRounds);
			}
			LOGGER.info("listChampionshipParticipantTeams" + listChampionshipParticipantTeams);
			model.addAttribute("championshipId", id);
			model.addAttribute("listChampionshipParticipantTeams", listChampionshipParticipantTeams);
			model.addAttribute("pageTitle", championship.getName() + " Teams");

		} catch (ChampionshipNotFoundException e) {
			e.printStackTrace();
		}
		LOGGER.info("Exit listAllTeamsForChampionhsip method - RefereeScoreReportController ");

		return "referee/list_participant_teams_form";
	}

	@GetMapping("score-report/championship/{championshipTeamId}/viewScores")
	public String getTeamWithAsanasForScoring(@PathVariable(name = "championshipTeamId") Integer championshipTeamId,
			Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered getTeamWithAsanasForScoring method - RefereeScoreReportController ");
		ChampionshipParticipantTeams championshipParticipantTeams;
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipTeamId);
			Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();

			ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
			List<ParticipantTeamParticipants> listParticipants = participantTeam.getParticipantTeamParticipants()
					.stream().sorted(Comparator.comparing(ParticipantTeamParticipants::getSequenceNumber))
					.collect(Collectors.toList());
			List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeamRefereesService
					.getTeamReferresForChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber(championship,
							championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory(),
							participantTeam, championshipParticipantTeams.getChampionshipRounds().getRound());
			if (listParticipantTeamReferees.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "No Referee Panels in this team");
				return "redirect:/referee/score-report/championship/" + championship.getId()
						+ "/getChampionshipCategories";
			}
			List<ParticipantTeamReferees> listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees = listParticipantTeamReferees
					.stream()
					.filter(referee -> referee.getType().getId() == 1 || referee.getType().getId()== 2)
					.collect(Collectors.toList());
			if (listParticipants.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "No Participants in this team");
				return "redirect:/referee/score-report/championship/" + championship.getId()
						+ "/getChampionshipCategories";
			}
			if (listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "No Judges in this team");
				return "redirect:/referee/score-report/championship/" + championship.getId()
						+ "/getChampionshipCategories";
			}

			LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamReferees, List<ParticipantTeamAsanaScoring>>> participantScoring = new LinkedHashMap<>();
			for (ParticipantTeamParticipants participant : listParticipants) {
				List<ParticipantTeamAsanaScoring> listParticipantTeamAsanaScoring = new ArrayList<ParticipantTeamAsanaScoring>();
				LinkedHashMap<ParticipantTeamReferees, List<ParticipantTeamAsanaScoring>> userScoring = new LinkedHashMap<>();
				for (ParticipantTeamReferees participantTeamReferee : listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees) {
					listParticipantTeamAsanaScoring = participantTeamAsanasScoringService
							.findAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndJudgeUser(participant,
									championshipParticipantTeams, participantTeamReferee.getJudgeUser());
					userScoring.put(participantTeamReferee, listParticipantTeamAsanaScoring);
					System.out.println("listParticipantTeamAsanaScoring" + listParticipantTeamAsanaScoring.size());
				}
				participantScoring.put(participant, userScoring);
			}
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService.getByTeamAndRoundOrderBySequenceNum(
					participantTeam, championshipParticipantTeams.getChampionshipRounds().getRound());
			model.addAttribute("listParticipantTeamAsanas", listParticipantTeamAsanas);
			model.addAttribute("participantScoring", participantScoring);
			model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);
			model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
			model.addAttribute("championshipId", championship.getId());
			model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
					.getChampionshipCategory().getAsanaCategory().getId());
			model.addAttribute("ageCategoryId", championshipParticipantTeams.getChampionshipRounds()
					.getChampionshipCategory().getCategory().getId());
			model.addAttribute("gender",
					championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
			model.addAttribute("pageTitle",
					"View Scores: " + participantTeam.getName() + " | " + participantTeam.getChestNumber() + " | "
							+ championshipParticipantTeams.getChampionshipRounds().getRound());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		LOGGER.info("Exit getTeamWithAsanasForScoring method - RefereeScoreReportController ");
		return "referee/scores_report";
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
