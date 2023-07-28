package com.swayaan.nysf.controller;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.API.entity.livescores.CategoriesDTO1;
import com.swayaan.nysf.API.entity.livescores.LiveScoresDTO;
import com.swayaan.nysf.API.entity.livescores.RoundsDTO1;
import com.swayaan.nysf.API.entity.livescores.TeamsDTO1;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO1;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.ParticipantTeamTotalScoringService;
import com.swayaan.nysf.service.UserService;

@Controller
@RequestMapping("/national-sports")
public class NationalYogasanaScoreReport {

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
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	ParticipantTeamTotalScoringService participantTeamTotalScoringService;

	@Autowired
	Environment env;
	DecimalFormat scoreFormat = new DecimalFormat("0.00");

	private static final Logger LOGGER = LoggerFactory.getLogger(NationalYogasanaScoreReport.class);

	@GetMapping("/yogasana/viewscores")
	public String getChampionshipsViewScore(Model model) {
		LOGGER.info("Entered getChampionshipsViewScore method - NationalYogasanaScoreReportController");
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Manage Championship");
		LOGGER.info("Exit getChampionshipsViewScore method - NationalYogasanaScoreReportController");

		return "NationalYogasanaSports/national_yogasana_index_page";

	}

	@GetMapping("/yogasana/championship/getTeamsForCategories")
	public String listChampionshipCategories(Model model) throws EntityNotFoundException {
		LOGGER.info("Entered listChampionshipCategories method - NationalYogasanaScoreReportController");
		// User refereeUser = getCurrentUser();
		Integer championshipId = 1;
		Championship championship;
		try {
			championship = championshipService.getChampionshipById(championshipId);
		} catch (ChampionshipNotFoundException e) {
			model.addAttribute("errorMessage", "Championship Not Found.");
			return "NationalYogasanaSports/national_yogasana_index_page";
		}
		List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
		List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
				.getChampionshipCategoryByChampionshipId(championshipId);
		List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
		for (AsanaCategory asanaCategory : listAsanaCategories) {
			List<ChampionshipCategory> listChampionhsipCategoryForAsanaCategory = new ArrayList<>();
			for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
				if (championshipCategory.getAsanaCategory().equals(asanaCategory)) {

					listChampionhsipCategoryForAsanaCategory.add(championshipCategory);
				}
			}

			if (!listChampionhsipCategoryForAsanaCategory.isEmpty()) {
				if (listChampionhsipCategoryForAsanaCategory.size() > 1) {
					Collections.sort(listChampionhsipCategoryForAsanaCategory, new Comparator<ChampionshipCategory>() {
						@Override
						public int compare(ChampionshipCategory c1, ChampionshipCategory c2) {
							return c2.getNoOfRounds().compareTo(c1.getNoOfRounds());
						}
					});
				}
				Integer noOfRounds = listChampionhsipCategoryForAsanaCategory.get(0).getNoOfRounds();
				for (int round = 1; round <= noOfRounds; round++) {
					listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship, asanaCategory,
							listChampionhsipCategoryForAsanaCategory.get(0).getCategory(), round));
				}

			}
		}

		model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
		model.addAttribute("pageTitle", "Championship Categories");
		LOGGER.info("Exit listChampionshipCategories method - NationalYogasanaScoreReportController");

		return "NationalYogasanaSports/championship_category_form1";
	}

	@GetMapping("/yogasana/championship/{championshipId}/getTeamsForCategroies/{round}")
	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException {
		LOGGER.info("Entered listAllTeamsForChampionhsip method - NationalYogasanaScoreReportController");

		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
		List<CategoriesDTO1> listCategories = new ArrayList<CategoriesDTO1>();
		try {
			Championship championship = championshipService.get(id);

			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
			if (!listChampionshipCategory.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					String categoryName = championshipCategory.getCategory().getTitle() + " - "
							+ championshipCategory.getGender().toString();
					
				//	String asanaCategoryName = championshipCategory.getAsanaCategory().getName() + " - "
				//			+ championshipCategory.getGender().toString();
					
					String asanaCategoryName = championshipCategory.getAsanaCategory().getName();
					Integer categoryId = null;
					Integer eventId = null;

					String eventDescription = "Yogasana Event";
					String gender = championshipCategory.getGender().toString();
					Integer noOfRounds = championshipCategory.getNoOfRounds();
					List<RoundsDTO1> listRounds = new ArrayList<>();

					String roundName = "Round " + round;

					ChampionshipRounds championshipRounds = championshipRoundsService
							.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
									round);
					List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
							.getAllByChampionshipRounds(championshipRounds);
					List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
							.getByRoundAndChampionshipAndChampionshipCategory(round, championship,
									championshipCategory);

					List<TeamsDTO1> listTeams = new ArrayList<>();
					if (!listChampionshipParticipantTeams.isEmpty()) {

						for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
							String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
							String autogenChestNumber = championshipParticipantTeams.getParticipantTeam().getAutogenChestNumber();
							String status = "SCHEDULED";
							Float totalScore = 0.0f;
							status = championshipParticipantTeams.getStatus().toString();
							// String rank = "-";

							ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
									.findByChampionshipParticipantTeamAndChampionshipRounds(
											championshipParticipantTeams, championshipRounds);
							if (participantTeamRoundTotalScoring != null) {
								totalScore = participantTeamRoundTotalScoring.getTotalScore();
								// rank = participantTeamRoundTotalScoring.getRanking().toString();
							}

							listTeams.add(new TeamsDTO1(chestId, autogenChestNumber, status, totalScore));
						}

					}

					
				
					// sort the list by score desc
					List<TeamsDTO1> listSortedTeams = listTeams.stream()
							.sorted(Comparator.comparing(TeamsDTO1::getTotal_score).reversed())
							.collect(Collectors.toList());

					listRounds.add(new RoundsDTO1(roundName, listSortedTeams));
					
				//	listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription, gender,
					//		listRounds,asanaCategoryName));

					listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription, gender,
							listRounds,asanaCategoryName));

				}
			}

		} catch (

		ChampionshipNotFoundException e) {
			e.printStackTrace();
		}
		model.addAttribute("pageTitle", "View Team Scores");
		model.addAttribute("listCategories", listCategories);
		LOGGER.info("Exit listAllTeamsForChampionhsip method - NationalYogasanaScoreReport");

		return "NationalYogasanaSports/view_team_total_scores";
		// return new ResponseEntity<List<CategoriesDTO1>>(listCategories,
		// HttpStatus.OK);
	}

	@GetMapping("/yogasana/championship/{championshipId}/getTeamsForCategroies/asanaCategory/{asanaCategoryId}/ageCategory/{ageCategoryId}/round/{round}")
	public String scoreDetailsForCategoryAndRound(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "ageCategoryId") Integer ageCategoryId, @PathVariable(name = "round") Integer round,
			Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException, ChampionshipNotFoundException {
		LOGGER.info("Entered scoreDetailsForCategoryAndRound method - NationalYogasanaScoreReportController");

		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
		List<CategoriesDTO1> listCategories = new ArrayList<CategoriesDTO1>();
		Championship championship = championshipService.get(championshipId);

		List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
				.getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc(
						championshipId, asanaCategoryId, ageCategoryId);

		System.out.println("listChampionshipCategory " + listChampionshipCategory.toString());

		if (!listChampionshipCategory.isEmpty()) {
			for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
				String categoryName = championshipCategory.getCategory().getTitle() + " - "
						+ championshipCategory.getGender().toString();
				
		//		String asanaCategoryName = championshipCategory.getAsanaCategory().getName() + " - "
		//				+ championshipCategory.getGender().toString();
				String asanaCategoryName = championshipCategory.getAsanaCategory().getName();

				Integer categoryId = null;
				Integer eventId = null;

				String eventDescription = "Yogasana Event";
				String gender = championshipCategory.getGender().toString();
				Integer noOfRounds = championshipCategory.getNoOfRounds();
				List<RoundsDTO1> listRounds = new ArrayList<>();

				String roundName = "Round " + round;

				ChampionshipRounds championshipRounds = championshipRoundsService
						.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
				if (championshipRounds != null) {
					List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
							.getAllByChampionshipRounds(championshipRounds);
					List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
							.getByRoundAndChampionshipAndChampionshipCategory(round, championship,
									championshipCategory);

					List<TeamsDTO1> listTeams = new ArrayList<>();
					if (!listChampionshipParticipantTeams.isEmpty()) {

						

							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
								String autogenChestNumber = championshipParticipantTeams.getParticipantTeam().getAutogenChestNumber();
								String teamName = championshipParticipantTeams.getParticipantTeam().getName();
								List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeamParticipantsService
										.getTeamParticipantsForParticipantTeam(
												championshipParticipantTeams.getParticipantTeam());
								List<String> listParticipantName = null;
								List<List<String>> participantLists = new ArrayList<>();
								for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {

									participantLists.add(participantService.getParticipantByParticipantTeamParticipants(
											participantTeamParticipants.getParticipant().getId()));
								}

							

							listParticipantName = participantLists.stream().flatMap(List::stream)
									.collect(Collectors.toList());
							System.out.println("listParticipantName" + listParticipantName);
							String status = "SCHEDULED";
							Float totalScore = 0.0f;
							status = championshipParticipantTeams.getStatus().toString();
							// String rank = "-";
							ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
									.findByChampionshipParticipantTeamAndChampionshipRounds(
											championshipParticipantTeams, championshipRounds);
							Float tieScore = null;
							if (participantTeamRoundTotalScoring != null) {
								totalScore = participantTeamRoundTotalScoring.getTotalScore();
								tieScore = participantTeamRoundTotalScoring.getTieScore();
								LOGGER.info(
										"participantTeam: "
												+ participantTeamRoundTotalScoring.getChampionshipParticipantTeams()
														.getParticipantTeam().getName()
												+ "   totalScore " + totalScore);
								// rank = participantTeamRoundTotalScoring.getRanking().toString();
							}

							listTeams.add(new TeamsDTO1(chestId, autogenChestNumber, status, totalScore, listParticipantName,
									teamName,tieScore));
						}

					}
//sort the list by chest number
					
					
					Collections.sort(listTeams, new Comparator<TeamsDTO1>() {
						@Override
						public int compare(TeamsDTO1 t1, TeamsDTO1 t2) {
						return t1.getChest_id()
								.compareTo(t2.getChest_id());
			
					}
					});
			

					
					// sort the list by score desc
					List<TeamsDTO1> listSortedTeams = listTeams.stream()
							.sorted(Comparator.comparing(TeamsDTO1::getTotal_score).reversed())
							.collect(Collectors.toList());

					listRounds.add(new RoundsDTO1(roundName, listSortedTeams));
					
					listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription, gender,
											listRounds,asanaCategoryName));

			//		listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription, gender,
			//				listRounds));

				}
			}
		}
		for (CategoriesDTO1 category : listCategories) {
			System.out.println(category.toString());
		}

		model.addAttribute("pageTitle", "View Team Scores");
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("championship", championship);



		String imagePath1 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
				+ System.getProperty("file.separator") + "image1";
		String imagePath2 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
				+ System.getProperty("file.separator") + "image2";
		String imagePath3 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
				+ System.getProperty("file.separator") + "image3";
		String imagePath4 = "scoreboard-image" + System.getProperty("file.separator") + championshipId
				+ System.getProperty("file.separator") + "image4";
		String fileName1 = "";
		File directory1 = new File(imagePath1);
		if (directory1.exists()) {
			File[] files = directory1.listFiles();
			fileName1 = files[0].getName();
			if (fileName1.equals("")) {

				model.addAttribute("image1", "");
			} else {

				model.addAttribute("image1", fileName1);
			}
		}
		String fileName2 = "";
		File directory2 = new File(imagePath2);
		if (directory2.exists()) {
			File[] files = directory2.listFiles();
			fileName2 = files[0].getName();
			if (fileName2.equals("")) {
				model.addAttribute("image2", "");
			} else {
				model.addAttribute("image2", fileName2);
			}
		}
		
		String fileName3 = "";
		File directory3 = new File(imagePath3);
		if (directory3.exists()) {
			File[] files = directory3.listFiles();
			fileName3 = files[0].getName();
			if (fileName3.equals("")) {
				model.addAttribute("image3", "");
			} else {
				model.addAttribute("image3", fileName3);
			}
		}
		
		String fileName4 = "";
		File directory4 = new File(imagePath4);
		if (directory4.exists()) {
			File[] files = directory4.listFiles();
			fileName4 = files[0].getName();
			if (fileName4.equals("")) {
				model.addAttribute("image4", "");
			} else {
				model.addAttribute("image4", fileName4);
			}
		}
		LOGGER.info("Exit scoreDetailsForCategoryAndRound method - NationalYogasanaScoreReportController");

				return "administration/view_team_total_scores";
		// return new ResponseEntity<List<CategoriesDTO1>>(listCategories,
		// HttpStatus.OK);
	}

}
