package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.swayaan.nysf.entity.ArtisticJudgeScore;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
import com.swayaan.nysf.entity.AsanaPerformEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.EvaluatorJudgeScore;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantScoringJudge;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.QuestionTypeEnum;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.SequenceEnum;
import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.entity.TraditionalJudgeScore;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.ArtisticJudgeScoreService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaEvaluationQuestionsService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.EvaluatorJudgeScoreService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.ParticipantScoringJudgeService;
import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamParticipantTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.ParticipantTeamTotalScoringService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.TimeKeeperJudgeScoreService;
import com.swayaan.nysf.service.TraditionalJudgeScoreService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.QuestionTypeUtil;

@Controller
@RequestMapping("/judge")
public class JudgeTeamScoringController {

	private static final float NOT_PERFORMED_IN_SEQUENCE_PENALTY_VALUE = 1.0f;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int JUDGETYPE_CHIEF_JUDGE = 1;
	private static final int JUDGETYPE_D_JUDGE = 2;
	private static final int JUDGETYPE_ARTISTIC_JUDGE = 3;
	private static final int JUDGETYPE_TIMEKEEPER_JUDGE = 4;
	private static final int JUDGETYPE_EVALUATOR_JUDGE = 5;
	private static final int JUDGETYPE_SCORER_JUDGE = 6;
	private static final int JUDGETYPE_STAGE_MANAGER = 7;

	@Autowired
	UserService userService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	ParticipantTeamAsanasScoringService participantTeamAsanasScoringService;
	@Autowired
	AsanaService asanaService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ParticipantTeamTotalScoringService participantTeamTotalScoringService;
	@Autowired
	ParticipantTeamParticipantTotalScoringService participantTeamParticipantTotalScoringService;
	@Autowired
	AsanaEvaluationQuestionsService asanaEvaluationQuestionsService;
	@Autowired
	ArtisticJudgeScoreService artisticJudgeScoreService;
	@Autowired
	TraditionalJudgeScoreService traditionalJudgeScoreService;
	@Autowired
	TimeKeeperJudgeScoreService timeKeeperJudgeScoreService;

	@Autowired
	EvaluatorJudgeScoreService evaluatorJudgeScoreService;

	@Autowired
	private RefereesPanelsService refereesPanelsService;

	@Autowired
	QuestionTypeUtil questionTypeUtil;
	@Autowired
	ParticipantScoringJudgeService participantScoringJudgeService;
	@Autowired
	JudgeService judgeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudgeTeamScoringController.class);

//	@PreAuthorize("hasAuthority('Judge')")
//	@GetMapping("/team-scoring")
//	public String listChampionships(Model model) {
//		LOGGER.info("Entered listChampionships JudgeTeamScoringController");
//		User user = CommonUtil.getCurrentUser();
//		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
//		model.addAttribute("listChampionships", listChampionships);
//		model.addAttribute("pageTitle", "Scheduled Championships");
//		return "judge/team_scoring";
//	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/championship/{championshipId}/getChampionshipCategories")
	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered listChampionshipCategories method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship;
		try {
			championship = championshipService.getChampionshipById(championshipId);
		} catch (ChampionshipNotFoundException e) {
			model.addAttribute("errorMessage", "Championship Not Found.");
			LOGGER.info("championship not found in teams" +e.getMessage());
			return "judge/team_scoring";
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
		LOGGER.info("Exit listChampionshipCategories method -JudgeTeamScoringController");

		return "judge/championship_category_form";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}")
	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws AsanaCategoryNotFoundException, EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered listAllTeamsForChampionhsip method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		return listAllChampionshipTeamsByPage(id, asanaCategoryId, gender, ageCategoryId, round, 1, model,
				"chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}/page/{pageNum}")
	public String listAllChampionshipTeamsByPage(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) {
		LOGGER.info("Entered listAllChampionshipTeamsByPage method -JudgeTeamScoringController");
		RoundStatusEnum status = RoundStatusEnum.ONGOING;
		// StatusEnum participantTeamStatus = StatusEnum.SCHEDULED;
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
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
					.getByRoundAndChampionshipAndChampionshipCategoryAndStatus(round, championship,
							championshipCategory, status);
			LOGGER.info("championshipRounds" + championshipRounds);
			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
			Page<ChampionshipParticipantTeams> page;
			if (championshipRounds != null) {

				page = championshipParticipantTeamsService.listByPage(pageNum, sortField, sortDir, chestNumber,
						championshipRounds);
				listChampionshipParticipantTeams = page.getContent();
			} else {
				LOGGER.error("Championship Round is null");
				ra.addFlashAttribute("errorMessage", "Unable to get teams. Make sure teams are moved to ongoing. ");
				return "redirect:/judge/common-team-scoring";
			}
			long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
			long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

			model.addAttribute("moduleURL", "/judge/championship/" + id + "/category/" + asanaCategoryId + "/"
					+ ageCategoryId + "/" + gender + "/" + round);

			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", reverseSortDir);
			model.addAttribute("keyword1", chestNumber);
			model.addAttribute("asanaCategoryId", asanaCategoryId);
			model.addAttribute("ageCategoryId", ageCategoryId);
			model.addAttribute("gender", gender);
			model.addAttribute("round", round);
			model.addAttribute("championshipId", id);
			model.addAttribute("listChampionshipParticipantTeams", listChampionshipParticipantTeams);
			model.addAttribute("pageTitle", championship.getName() + " Teams");

		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AsanaCategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Exit listAllChampionshipTeamsByPage method -JudgeTeamScoringController");

		return "judge/participant_team_form";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/championship/{championshipTeamId}/giveScores")
	public String getTeamWithAsanasForScoring(@PathVariable(name = "championshipTeamId") Integer championshipTeamId,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered getTeamWithAsanasForScoring method -JudgeTeamScoringController");
		User user = CommonUtil.getCurrentUser();
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		CommonUtil.setNavigationHistory(mappedUrl, user);
		Judge judgeUser = CommonUtil.getCurrentJudge();
		ChampionshipParticipantTeams championshipParticipantTeams;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipTeamId);
			ChampionshipRounds championshipRounds = championshipParticipantTeams.getChampionshipRounds();
			ChampionshipCategory championshipCategory = championshipRounds.getChampionshipCategory();
			Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
			ParticipantTeamReferees participantTeamReferees = participantTeamRefereesService
					.getByParticipantTeamAndRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(
							championshipParticipantTeams.getParticipantTeam(), championshipRounds.getRound(),
							championship, championshipRounds.getChampionshipCategory(), judgeUser);
			ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
			if (participantTeamReferees == null) {

				redirectAttributes.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to Give Scores for this Category.");
				return "redirect:/judge/championship/" + championship.getId() + "/getChampionshipCategories";
			} else {

				AsanaCategory asanaCategory = participantTeamReferees.getChampionshipCategory().getAsanaCategory();
				JudgeType judgeType = participantTeamReferees.getType();
				model.addAttribute("participantTeamReferees", participantTeamReferees);
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
						"Give Scores:" + " Round : " + championshipParticipantTeams.getChampionshipRounds().getRound());
				model.addAttribute("teamId", participantTeam.getId());
				model.addAttribute("asanaCategory", championshipCategory.getAsanaCategory());
				if (judgeType.getId() == JUDGETYPE_D_JUDGE) {
// D Judge
					if (asanaCategory.getId() == 1) {
						// Traditional Category
						// create records for scoring
						createScoreRecordsForTraditionalJudge(championshipParticipantTeams, championshipRounds,
								championshipCategory, championship, participantTeamReferees, participantTeam);
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
						return "judge/djudge/traditional_d_judge_scoring_form";
					} else if (asanaCategory.getId() == 2) {
						// Artistic Single
						// create records for scoring
						createScoreRecordsForTraditionalJudge(championshipParticipantTeams, championshipRounds,
								championshipCategory, championship, participantTeamReferees, participantTeam);
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);

						return "judge/djudge/artistic_single_d_judge_scoring_form";
					} else if (asanaCategory.getId() == 3) {
						// Artistic Pair
						// create records for scoring
						createScoreRecordsForTraditionalJudgeForPair(championshipParticipantTeams, championshipRounds,
								championshipCategory, championship, participantTeamReferees, participantTeam);
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> commonQuestionForAsanaMap = getListOfParticipantsCommonAsanaQuestionsWithAsanaScoreList(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
						model.addAttribute("commonQuestionForAsanaMap", commonQuestionForAsanaMap);
						return "judge/djudge/artistic_pair_d_judge_scoring_form";
						// return "judge/djudge/artistic_pair_d_judge_scoring_form";
					} else if (asanaCategory.getId() == 4) {
						// Rhythmic Pair
						createScoreRecordsForTraditionalJudge(championshipParticipantTeams, championshipRounds,
								championshipCategory, championship, participantTeamReferees, participantTeam);
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, TraditionalJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForRhythmicPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);

						return "judge/djudge/rhythmic_pair_d_judge_scoring_form";
					} else if (asanaCategory.getId() == 5) {
						// Artistic Group
						createScoreRecordsForTraditionalJudgeArtisticGroup(championshipParticipantTeams,
								championshipRounds, championshipCategory, championship, participantTeamReferees,
								participantTeam);
						LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = getMapOfAsanasWithAsanaScoreListForArtisticGroup(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
								.findAllByTypeAndAsanaCategory(judgeType, asanaCategory);
						model.addAttribute("asanaScoreMap", asanaScoreMap);
						model.addAttribute("listAsanaEvaluationQuestions", listAsanaEvaluationQuestions);
						return "judge/djudge/artistic_group_d_judge_scoring_form";
					}
				} else if (judgeType.getId() == JUDGETYPE_TIMEKEEPER_JUDGE) {

// T Judge
					if (asanaCategory.getId() == 1) {
						// Traditional
						createScoreRecordsForTimekeeperJudge(championshipParticipantTeams, championshipRounds,
								championshipCategory, championship, participantTeamReferees, participantTeam);
						List<TimeKeeperJudgeScore> listTimekeeperScoring = timeKeeperJudgeScoreService
								.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
										championshipParticipantTeams, participantTeamReferees);
						boolean isFinal = false;
						Integer championshipTotalRounds = championshipCategory.getNoOfRounds();
						Integer roundNumber = championshipParticipantTeams.getChampionshipRounds().getRound();
						System.out.println(championshipTotalRounds + "-" + roundNumber);
						if (championshipTotalRounds == roundNumber) {
							isFinal = true;
						}
						LOGGER.info("isFinal");
						model.addAttribute("isFinal", isFinal);

						model.addAttribute("listTimekeeperScoring", listTimekeeperScoring);
						return "judge/tjudge/traditional_t_judge_scoring_form";
					} else if (asanaCategory.getId() == 2 || asanaCategory.getId() == 4 || asanaCategory.getId() == 5) {
						// Common
						createScoreRecordsForTimeKeeperJudgeForOtherGroups(championshipParticipantTeams,
								championshipRounds, championshipCategory, championship, participantTeamReferees,
								participantTeam);
						LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						// get common question records
						TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("asanaCategory", championshipCategory.getAsanaCategory());
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
						model.addAttribute("commonQuestionScore", commonQuestionScore);

						LOGGER.info("common question" + commonQuestionScore.toString());
						return "judge/tjudge/common_t_judge_scoring_form";

					} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
						// Common
						ParticipantTeamParticipants participantTeamParticipant = createScoreRecordsForTimeKeeperJudgeForArtisticPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeperForArtisticPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						// get common question records
						TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeperForArtisticPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("asanaCategory", championshipCategory.getAsanaCategory());
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
						model.addAttribute("participantTeamParticipant", participantTeamParticipant);
						model.addAttribute("commonQuestionScore", commonQuestionScore);

						LOGGER.info("common question" + commonQuestionScore.toString());
						
						return "judge/tjudge/common_t_judge_scoring_form";
					}

				} else if (judgeType.getId() == JUDGETYPE_EVALUATOR_JUDGE) {
// E Judge
					if (asanaCategory.getId() == 1) {
						// Traditional

						// create records
						createScoreRecordsForEvaluatorJudgeForTraditional(championshipParticipantTeams,
								championshipRounds, championshipCategory, championship, participantTeamReferees,
								participantTeam);
						// retrieve records & add to modal
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluator(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);

						return "judge/ejudge/traditional_e_judge_scoring_form";

					} else if (asanaCategory.getId() == 2 || asanaCategory.getId() == 4 || asanaCategory.getId() == 5) {
						// Common
						createScoreRecordsForEvaluatorJudgeForOtherGroups(championshipParticipantTeams,
								championshipRounds, championshipCategory, championship, participantTeamReferees,
								participantTeam);
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluator(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						// get common question records
						EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
								championshipRounds, championshipCategory, championship, participantTeamReferees,
								participantTeam);

						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
						model.addAttribute("commonQuestion", commonQuestion);
						return "judge/ejudge/common_e_judge_scoring_form";
					} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
						// Artistic Pair
						ParticipantTeamParticipants participantTeamParticipant = createScoreRecordsForEvaluatorJudgeForArtisticPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluatorForArtisticPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						// get common question records
						EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecordsForArtisticPair(
								championshipParticipantTeams, championshipRounds, championshipCategory, championship,
								participantTeamReferees, participantTeam);
						model.addAttribute("participantTeamParticipant", participantTeamParticipant);

						model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
						model.addAttribute("commonQuestion", commonQuestion);
						return "judge/ejudge/common_e_judge_scoring_form";
					}

				} else if (judgeType.getId() == JUDGETYPE_ARTISTIC_JUDGE) {
					LOGGER.info("Role Artistic Judge");
// A Judge
					List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
							.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
									championshipCategory.getAsanaCategory());
					LOGGER.info("listAsanaEvaluationQuestions" + listAsanaEvaluationQuestions.size());
					for (AsanaEvaluationQuestions asanaEvaluationQuestions : listAsanaEvaluationQuestions) {
						// check if already records exists for the championhsipParticipantTeam,
						// participantTeamReferee,asanaEvaluationQuestions
						boolean recordStatus = artisticJudgeScoreService
								.isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
										championshipParticipantTeams, participantTeamReferees,
										asanaEvaluationQuestions);
						if (recordStatus == false) {
							ArtisticJudgeScore artisticJudgeScore = new ArtisticJudgeScore();
							artisticJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestions);
							artisticJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
							artisticJudgeScore.setParticipantTeamReferees(participantTeamReferees);
							artisticJudgeScore.setRound(championshipRounds.getRound());

							artisticJudgeScore
									.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestions));
							artisticJudgeScoreService.save(artisticJudgeScore);

						}

					}
					List<ArtisticJudgeScore> listScoring = artisticJudgeScoreService
							.findAllByChampionshipParticipantTeamAndParticipantTeamReferees(
									championshipParticipantTeams, participantTeamReferees);
					model.addAttribute("listScoring", listScoring);

					LOGGER.info("Returning judge/ajudge/common_a_judge_scoring_form");
					return "judge/ajudge/common_a_judge_scoring_form";

				} else if (judgeType.getId() == 1 || judgeType.getId() == 6) {
// Chief Judge /Scorer
					return "referee/score_report_form";
				} else {
// Stage Manager/ Co-ordinator
				}

			}

		} catch (

		EntityNotFoundException e) {
			e.printStackTrace();
		}
		LOGGER.info("Exit getTeamWithAsanasForScoring method -JudgeTeamScoringController");
		return "judge/team_scoring_form1";
	}

//	@GetMapping("/championship/{championshipTeamId}/giveScores")
//	public String getTeamWithAsanasForScoring(@PathVariable(name = "championshipTeamId") Integer championshipTeamId,
//			Model model, RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
//		LOGGER.info("Entered getTeamWithAsanasForScoring JudgeTeamScoringController");
//		User user = getCurrentUser();
//		ChampionshipParticipantTeams championshipParticipantTeams;
//		try {
//			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipTeamId);
//			Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
//
//			ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
//			List<ParticipantTeamParticipants> listParticipants = participantTeam.getParticipantTeamParticipants()
//					.stream().sorted(Comparator.comparing(ParticipantTeamParticipants::getSequenceNumber))
//					.collect(Collectors.toList());
//			List<ParticipantTeamAsanas> listRoundParticipantTeamAsanas = participantTeamAsanasService.getByTeamAndRoundOrderBySequenceNum(
//					participantTeam, championshipParticipantTeams.getChampionshipRounds().getRound());
//
//			List<ParticipantTeamAsanaScoring> listParticipantTeamAsanaScoring = new ArrayList<ParticipantTeamAsanaScoring>();
//
//			for (ParticipantTeamParticipants participant : listParticipants) {
//				for (ParticipantTeamAsanas participantTeamAsanas : listRoundParticipantTeamAsanas) {
//					listParticipantTeamAsanaScoring.add(new ParticipantTeamAsanaScoring(participantTeamAsanas,
//							participant, championshipParticipantTeams, user));
//				}
//
//			}
//			ParticipantTeamReferees participantTeamReferees = participantTeamRefereesService
//					.getTeamReferresForParticipantTeamAndUserAndRound(participantTeam, user,
//							championshipParticipantTeams.getChampionshipRounds().getRound());
//			List<ParticipantTeamAsanaScoring> roundParticipantTeamAsanaScoringList = new ArrayList<ParticipantTeamAsanaScoring>();
//			if (participantTeamReferees != null) {
//				if (!participantTeamAsanasScoringService
//						.existsByChampionshipParticipantTeamsAndUser(championshipParticipantTeams, user)) {
//					roundParticipantTeamAsanaScoringList = participantTeamAsanasScoringService
//							.save(listParticipantTeamAsanaScoring);
//				}
//
//			} else {
//				redirectAttributes.addFlashAttribute("errorMessage",
//						"You are not a member of the Panel. You are not eligible to Give Scores for this Category.");
//				return "redirect:/judge/championship/" + championship.getId() + "/getChampionshipCategories";
//			}
//			List<ParticipantTeamAsanaScoring> listByParticipant = new ArrayList<>();
//			LinkedHashMap<ParticipantTeamParticipants, List<ParticipantTeamAsanaScoring>> mapScoring = new LinkedHashMap<>();
//			for (ParticipantTeamParticipants participant : listParticipants) {
//				listByParticipant = participantTeamAsanasScoringService
//						.findAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndUser(participant,
//								championshipParticipantTeams, user);
//				mapScoring.put(participant, listByParticipant);
//
//			}
//			model.addAttribute("mapScoring", mapScoring);
//			model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);
//			model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
//			model.addAttribute("championshipId", championship.getId());
//			model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
//					.getChampionshipCategory().getAsanaCategory().getId());
//			model.addAttribute("ageCategoryId", championshipParticipantTeams.getChampionshipRounds()
//					.getChampionshipCategory().getCategory().getId());
//			model.addAttribute("gender",
//					championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
//			model.addAttribute("roundParticipantTeamAsanaScoringList", roundParticipantTeamAsanaScoringList);
//			model.addAttribute("pageTitle",
//					"Give Scores: " + participantTeam.getName() + " | " + participantTeam.getChestNumber() + " | "
//							+ championshipParticipantTeams.getChampionshipRounds().getRound());
//			model.addAttribute("teamId", championshipParticipantTeams.getParticipantTeam().getId());
//		} catch (
//
//		EntityNotFoundException e) {
//			e.printStackTrace();
//		}
//		LOGGER.info("Returning team_scoring_form ");
//		return "judge/team_scoring_form1";
//	}
//
//	@PostMapping("/participantTeamTotalScoringForRound/{id}/round/{round}")
//	public String saveTeamTotalScore(@Param("teamTotalScore") Float teamTotalScore,
//			@PathVariable(name = "id") Integer championshipParticipantTeamsId,
//			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes)
//			throws ParticipantTeamNotFoundException {
//		LOGGER.info("Entered saveTeamTotalScore ParticipantTeamAsanaScoringRestController");
//		String errorMessage = null;
//		User currentUser = getCurrentUser();
//		ChampionshipParticipantTeams championshipParticipantTeams = null;
//		try {
//			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipParticipantTeamsId);
//			Integer noOfNullCells = participantTeamAsanasScoringService
//					.findAllByChampionhsipParticipantTeamsAndUser(championshipParticipantTeams, currentUser);
//			if (noOfNullCells != 0) {
//				redirectAttributes.addFlashAttribute("errorMessage",
//						"You have not given all the scores for the team, Consult Time Keeper or Administrator.");
//				return "redirect:/judge/championship/" + championshipParticipantTeams.getId() + "/giveScores";
//
//			}
//			// if score is not saved in each cell total save it here
//			List<ParticipantTeamParticipants> listParticipants = championshipParticipantTeams.getParticipantTeam()
//					.getParticipantTeamParticipants();
//			for (ParticipantTeamParticipants participant : listParticipants) {
//				Float totalAsanaScores = participantTeamAsanasScoringService
//						.getAllByParticipantTeamParticipantsAndchampionshipParticipantTeamsAndUser(participant,
//								championshipParticipantTeams, currentUser);
//				ParticipantTeamParticipantTotalScoring participantTotalScore = participantTeamParticipantTotalScoringService
//						.findByRoundAndUserAndParticipantTeamParticipantsAndChampionshipParticipantTeams(round,
//								currentUser, participant, championshipParticipantTeams);
//				if (participantTotalScore != null) {
//					participantTotalScore.setTotalScore(totalAsanaScores);
//				} else {
//					ParticipantTeamParticipantTotalScoring participantTeamTotalScore = new ParticipantTeamParticipantTotalScoring();
//					participantTeamTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
//					participantTeamTotalScore.setParticipantTeamParticipant(participant);
//					participantTeamTotalScore.setRound(round);
//					participantTeamTotalScore.setTotalScore(totalAsanaScores);
//					participantTeamTotalScore.setUser(currentUser);
//					participantTeamParticipantTotalScoringService.save(participantTeamTotalScore);
//				}
//			}
//
//			ParticipantTeamTotalScoring participantTeamTotalScoring = participantTeamTotalScoringService
//					.findByRoundAndChampionshipParticipantTeamAndUser(round, championshipParticipantTeams, currentUser);
//			if (participantTeamTotalScoring == null) {
//				// create new entry
//				ParticipantTeamTotalScoring participantTeamTotalScoringNew = new ParticipantTeamTotalScoring();
//				participantTeamTotalScoringNew.setChampionshipParticipantTeam(championshipParticipantTeams);
//				participantTeamTotalScoringNew.setRound(round);
//				participantTeamTotalScoringNew.setTotalScore(teamTotalScore);
//				participantTeamTotalScoringNew.setUser(currentUser);
//				participantTeamTotalScoringService.save(participantTeamTotalScoringNew);
//			} else {
//
//				// update total team score
//				participantTeamTotalScoring.setTotalScore(teamTotalScore);
//				participantTeamTotalScoringService.save(participantTeamTotalScoring);
//			}
//		} catch (EntityNotFoundException e) {
//			errorMessage = "Championship Participant Team Not Found";
//			redirectAttributes.addFlashAttribute("message", errorMessage);
//			return "redirect:/judge/championship/" + championshipParticipantTeamsId + "/giveScores";
//		}
//
//		redirectAttributes.addFlashAttribute("message", "Team Total Score Saved Successfully");
//		return "redirect:/judge/championship/" + championshipParticipantTeamsId + "/giveScores";
//
//	}

	// evaluator save for traditional
	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/evaluator/{participantTeamRefereesId}/team/{championshipParticipantTeamsId}/save")
	public String setStatusOfAllEvaluatorRecords(
			@PathVariable(name = "participantTeamRefereesId") Integer participantTeamRefereesId,
			@PathVariable(name = "championshipParticipantTeamsId") Integer championshipParticipantTeamsId, Model model,
			HttpServletRequest request) {

		LOGGER.info("Entered setStatusOfAllEvaluatorRecords method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipParticipantTeams championshipParticipantTeams = null;
		ParticipantTeamReferees participantTeamReferees = null;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipParticipantTeamsId);
			participantTeamReferees = participantTeamRefereesService.get(participantTeamRefereesId);
			List<EvaluatorJudgeScore> listEvaluatorJudgeScore = evaluatorJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
							championshipParticipantTeams, participantTeamReferees);
			if (!listEvaluatorJudgeScore.isEmpty()) {
				for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScore) {
					if (evaluatorJudgeScore.getAsPerSequence() == null) {
						evaluatorJudgeScore.setAsPerSequence(SequenceEnum.DIDNOTPERFORMINSEQUENCE);
					}
					if (evaluatorJudgeScore.getIsPerformed() == null) {
						if (evaluatorJudgeScore.getAsPerSequence() == SequenceEnum.DIDNOTPERFORMINSEQUENCE) {
							evaluatorJudgeScore.setIsPerformed(AsanaPerformEnum.NOTPERFORMED);
						} else {
							evaluatorJudgeScore.setIsPerformed(AsanaPerformEnum.PERFORMED);
						}
					}
					evaluatorJudgeScore.setStatus(true);
					evaluatorJudgeScoreService.save(evaluatorJudgeScore);
				}
			}

			for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScore) {
				Float penaltyScore = (float) 0.0;
				if (evaluatorJudgeScore.getIsPerformed() == AsanaPerformEnum.PERFORMED) {
					if (evaluatorJudgeScore.getAsPerSequence() == SequenceEnum.PERFORMEDINSEQUENCE) {
						evaluatorJudgeScore.setPenaltyScore(penaltyScore);
					} else {
						evaluatorJudgeScore.setPenaltyScore(NOT_PERFORMED_IN_SEQUENCE_PENALTY_VALUE);
					}
				}
				evaluatorJudgeScoreService.save(evaluatorJudgeScore);
			}
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		// give the view

		ChampionshipRounds championshipRounds = championshipParticipantTeams.getChampionshipRounds();
		ChampionshipCategory championshipCategory = championshipParticipantTeams.getChampionshipRounds()
				.getChampionshipCategory();
		Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluator(
				championshipParticipantTeams, championshipRounds, championshipCategory, championship,
				participantTeamReferees, participantTeam);

		model.addAttribute("participantTeamReferees", participantTeamReferees);
		model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);
		model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
		model.addAttribute("championshipId", championship.getId());
		model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
				.getChampionshipCategory().getAsanaCategory().getId());
		model.addAttribute("ageCategoryId",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getCategory().getId());
		model.addAttribute("gender",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
		model.addAttribute("pageTitle", "Give Scores:  Chest Number : " + participantTeam.getChestNumber()
				+ " |  Round : " + championshipParticipantTeams.getChampionshipRounds().getRound());
		model.addAttribute("teamId", participantTeam.getId());
		model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
		LOGGER.info("Exit setStatusOfAllEvaluatorRecords method -JudgeTeamScoringController");

		return "judge/ejudge/traditional_e_judge_scoring_form";

	}

	// Timekeeper save for all other catgeories

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/timekeeper/{participantTeamRefereesId}/team/{championshipParticipantTeamsId}/saveTeamScores")
	public String setStatusOfAllTimeKeeperRecordsForOtherGroups(
			@PathVariable(name = "participantTeamRefereesId") Integer participantTeamRefereesId,
			@PathVariable(name = "championshipParticipantTeamsId") Integer championshipParticipantTeamsId, Model model,
			HttpServletRequest request) {

		LOGGER.info("Entered setStatusOfAllTimeKeeperRecordsForOtherGroups method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipParticipantTeams championshipParticipantTeams = null;
		ParticipantTeamReferees participantTeamReferees = null;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipParticipantTeamsId);
			participantTeamReferees = participantTeamRefereesService.get(participantTeamRefereesId);
			List<TimeKeeperJudgeScore> listTimeKeeperJudgeScore = timeKeeperJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamReferees(championshipParticipantTeams,
							participantTeamReferees);

			if (!listTimeKeeperJudgeScore.isEmpty()) {
				for (TimeKeeperJudgeScore timeKeeperJudgeScore : listTimeKeeperJudgeScore) {

					timeKeeperJudgeScore.setStatus(true);
					timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);
				}

			}
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}

		// give the view

		ChampionshipRounds championshipRounds = championshipParticipantTeams.getChampionshipRounds();
		ChampionshipCategory championshipCategory = championshipParticipantTeams.getChampionshipRounds()
				.getChampionshipCategory();
		AsanaCategory asanaCategory = championshipCategory.getAsanaCategory();
		Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
		LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = new LinkedHashMap<>();
		TimeKeeperJudgeScore commonQuestionScore = new TimeKeeperJudgeScore();
		if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
			participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeperForArtisticPair(
					championshipParticipantTeams, championshipRounds, championshipCategory, championship,
					participantTeamReferees, participantTeam);
			commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeperForArtisticPair(championshipParticipantTeams,
					championshipRounds, championshipCategory, championship, participantTeamReferees, participantTeam);
			model.addAttribute("participantTeamParticipant", commonQuestionScore.getParticipantTeamParticipants());
		} else {

			participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(championshipParticipantTeams,
					championshipRounds, championshipCategory, championship, participantTeamReferees, participantTeam);
			commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(championshipParticipantTeams,
					championshipRounds, championshipCategory, championship, participantTeamReferees, participantTeam);
		}
		// get common question records

		// calculate total score for the team

		Float twTotalScore = (float) 5.0;
		Float penalty = (float) 0.0;
		if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID
				|| asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
				|| asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
			Integer minutes = commonQuestionScore.getTimeInMinutes();
			Integer seconds = commonQuestionScore.getTimeInSeconds();
			Integer totalTimeTaken = 0;
			if (minutes != null && seconds != null) {
				totalTimeTaken = (minutes * 60) + seconds;

				if (totalTimeTaken < 150) {
					twTotalScore = (float) 0.0;
				} else if (totalTimeTaken > 190) {
					twTotalScore = (float) 0.0;
				} else if (totalTimeTaken >= 150 && totalTimeTaken <= 180) {
					twTotalScore = (float) 5.0;
				} else if (totalTimeTaken > 180 && totalTimeTaken <= 190) {
					penalty = (float) ((float) (totalTimeTaken - 180) * 0.5);
					twTotalScore = (float) twTotalScore - penalty;

				}
			}

		} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {

			Integer minutes = commonQuestionScore.getTimeInMinutes();
			Integer seconds = commonQuestionScore.getTimeInSeconds();
			Integer totalTimeTaken = 0;
			if (minutes != null && seconds != null) {
				totalTimeTaken = (minutes * 60) + seconds;

				if (totalTimeTaken < 210) {
					twTotalScore = (float) 0.0;
				} else if (totalTimeTaken > 250) {
					twTotalScore = (float) 0.0;
				} else if (totalTimeTaken >= 210 && totalTimeTaken <= 240) {
					twTotalScore = (float) 5.0;
				} else if (totalTimeTaken > 240 && totalTimeTaken <= 250) {
					penalty = (float) ((float) (totalTimeTaken - 240) * 0.5);
					twTotalScore = (float) twTotalScore - penalty;

				}
			}

		}
		// SET TW SCORE

		commonQuestionScore.setTotalScore(twTotalScore);
		if (penalty > 0) {
			commonQuestionScore.setPenaltyScore(true);

		} else {
			commonQuestionScore.setPenaltyScore(false);
		}
		timeKeeperJudgeScoreService.save(commonQuestionScore);

		model.addAttribute("asanaCategory", championshipCategory.getAsanaCategory());
		model.addAttribute("commonQuestionScore", commonQuestionScore);
		model.addAttribute("participantTeamReferees", participantTeamReferees);
		model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);
		model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
		model.addAttribute("championshipId", championship.getId());
		model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
				.getChampionshipCategory().getAsanaCategory().getId());
		model.addAttribute("ageCategoryId",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getCategory().getId());
		model.addAttribute("gender",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
		model.addAttribute("pageTitle", "Give Scores:  Chest Number : " + participantTeam.getChestNumber()
				+ " |  Round : " + championshipParticipantTeams.getChampionshipRounds().getRound());
		model.addAttribute("teamId", participantTeam.getId());
		model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
		model.addAttribute("message" , "Scores saved Successfully");
		LOGGER.info("Exit setStatusOfAllTimeKeeperRecordsForOtherGroups method -JudgeTeamScoringController");

		return "judge/tjudge/common_t_judge_scoring_form";

	}

	// evaluator save for all other catgeories
	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/evaluator/{participantTeamRefereesId}/team/{championshipParticipantTeamsId}/saveTeamScores")
	public String setStatusOfAllEvaluatorRecordsForOtherGroups(
			@PathVariable(name = "participantTeamRefereesId") Integer participantTeamRefereesId,
			@PathVariable(name = "championshipParticipantTeamsId") Integer championshipParticipantTeamsId, Model model,
			HttpServletRequest request) {

		LOGGER.info("Entered setStatusOfAllEvaluatorRecordsForOtherGroups method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipParticipantTeams championshipParticipantTeams = null;
		ParticipantTeamReferees participantTeamReferees = null;
		List<EvaluatorJudgeScore> listEvaluatorJudgeScore = new ArrayList<>();
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipParticipantTeamsId);
			participantTeamReferees = participantTeamRefereesService.get(participantTeamRefereesId);
			listEvaluatorJudgeScore = evaluatorJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
							championshipParticipantTeams, participantTeamReferees);
			if (!listEvaluatorJudgeScore.isEmpty()) {
				for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScore) {
					if (evaluatorJudgeScore.getAsPerSequence() == null) {
						evaluatorJudgeScore.setAsPerSequence(SequenceEnum.DIDNOTPERFORMINSEQUENCE);
					}
					if (evaluatorJudgeScore.getIsPerformed() == null) {
						if (evaluatorJudgeScore.getAsPerSequence() == SequenceEnum.DIDNOTPERFORMINSEQUENCE) {
							evaluatorJudgeScore.setIsPerformed(AsanaPerformEnum.NOTPERFORMED);
						} else {
							evaluatorJudgeScore.setIsPerformed(AsanaPerformEnum.PERFORMED);
						}
					}
					if (evaluatorJudgeScore.getQuestionType().equals(QuestionTypeEnum.COMMONTEAMQUESTION)
							&& evaluatorJudgeScore.getPenaltyScore() == null) {
						evaluatorJudgeScore.setPenaltyScore((float) 0.0);
					}
					evaluatorJudgeScore.setStatus(true);
					evaluatorJudgeScoreService.save(evaluatorJudgeScore);
				}
			}
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		// calculate the totals considering penalties & save to db
		LOGGER.info("------------------------------------------------------------");

		for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScore) {
			Float penaltyScore = (float) 0.0;
			if (evaluatorJudgeScore.getIsPerformed() == AsanaPerformEnum.PERFORMED) {
				if (evaluatorJudgeScore.getAsPerSequence() == SequenceEnum.PERFORMEDINSEQUENCE) {
					evaluatorJudgeScore.setPenaltyScore(penaltyScore);
				} else {
					evaluatorJudgeScore.setPenaltyScore(NOT_PERFORMED_IN_SEQUENCE_PENALTY_VALUE);
				}
			}
			evaluatorJudgeScoreService.save(evaluatorJudgeScore);
		}

		// give the view

		ChampionshipRounds championshipRounds = championshipParticipantTeams.getChampionshipRounds();
		ChampionshipCategory championshipCategory = championshipParticipantTeams.getChampionshipRounds()
				.getChampionshipCategory();
		AsanaCategory asanaCategory = championshipCategory.getAsanaCategory();
		Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		EvaluatorJudgeScore commonQuestion = new EvaluatorJudgeScore();
		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();
		if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
			participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluatorForArtisticPair(
					championshipParticipantTeams, championshipRounds, championshipCategory, championship,
					participantTeamReferees, participantTeam);
			commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams, championshipRounds,
					championshipCategory, championship, participantTeamReferees, participantTeam);
			model.addAttribute("participantTeamParticipant", commonQuestion.getParticipantTeamParticipants());
		} else {
			participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluator(championshipParticipantTeams,
					championshipRounds, championshipCategory, championship, participantTeamReferees, participantTeam);
			commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams, championshipRounds,
					championshipCategory, championship, participantTeamReferees, participantTeam);
		}

		model.addAttribute("commonQuestion", commonQuestion);
		model.addAttribute("asanaCategory",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getAsanaCategory());
		model.addAttribute("participantTeamReferees", participantTeamReferees);
		model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);
		model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
		model.addAttribute("championshipId", championship.getId());
		model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
				.getChampionshipCategory().getAsanaCategory().getId());
		model.addAttribute("ageCategoryId",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getCategory().getId());
		model.addAttribute("gender",
				championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
		model.addAttribute("pageTitle", "Give Scores:  Chest Number : " + participantTeam.getChestNumber()
				+ " |  Round : " + championshipParticipantTeams.getChampionshipRounds().getRound());
		model.addAttribute("teamId", participantTeam.getId());
		model.addAttribute("participantAsanaScoreMap", participantAsanaScoreMap);
		LOGGER.info("Exit setStatusOfAllEvaluatorRecordsForOtherGroups method -JudgeTeamScoringController");

		return "judge/ejudge/common_e_judge_scoring_form";

	}

	/* ********************* Traditional Judge ********************************* */

	private void createScoreRecordsForTraditionalJudge(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds, ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		LOGGER.info("listParticipantTeamParticipants " + listParticipantTeamParticipants.size());
		LOGGER.info("listParticipantTeamParticipants " + listParticipantTeamParticipants.toString());

		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {

			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			LOGGER.info("ParticipanTeam Asanas :" + listParticipantTeamAsanas.size());
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypePARTICIPANTSPECIFICASANAQUESTION(judgeType,
								championshipCategory.getAsanaCategory());
				LOGGER.info("Participant Evaluation Questions size:" + listAsanaEvaluationQuestions.size());
				LOGGER.info("Participant Evaluation Questions :" + listAsanaEvaluationQuestions.toString());
				for (AsanaEvaluationQuestions asanaEvaluationQuestions : listAsanaEvaluationQuestions) {
					// check if already records exists for the championhsipParticipantTeam,
					// participantTeamReferee,asanaEvaluationQuestions
					boolean recordStatus = traditionalJudgeScoreService
							.isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
									championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
									participantTeamParticipants, participantTeamAsanas);
					if (recordStatus == false) {
						LOGGER.info("Creating records");
						TraditionalJudgeScore traditionalJudgeScore = new TraditionalJudgeScore();
						traditionalJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
						traditionalJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestions);
						traditionalJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
						traditionalJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
						traditionalJudgeScore.setParticipantTeamParticipants(participantTeamParticipants);
						traditionalJudgeScore.setParticipantTeamReferees(participantTeamReferees);
						traditionalJudgeScore.setRound(championshipRounds.getRound());
						traditionalJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
						traditionalJudgeScore
								.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestions));
						traditionalJudgeScoreService.save(traditionalJudgeScore);

					}

				}

			}

		}

	}

	private void createScoreRecordsForTraditionalJudgeForPair(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds, ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {

			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypePARTICIPANTSPECIFICASANAQUESTION(judgeType,
								championshipCategory.getAsanaCategory());
				for (AsanaEvaluationQuestions asanaEvaluationQuestions : listAsanaEvaluationQuestions) {
					// check if already records exists for the championhsipParticipantTeam,
					// participantTeamReferee,asanaEvaluationQuestions
					boolean recordStatus = traditionalJudgeScoreService
							.isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
									championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
									participantTeamParticipants, participantTeamAsanas);
					if (recordStatus == false) {
						TraditionalJudgeScore traditionalJudgeScore = new TraditionalJudgeScore();
						traditionalJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
						traditionalJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestions);
						traditionalJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
						traditionalJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
						traditionalJudgeScore.setParticipantTeamParticipants(participantTeamParticipants);
						traditionalJudgeScore.setParticipantTeamReferees(participantTeamReferees);
						traditionalJudgeScore.setRound(championshipRounds.getRound());
						traditionalJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
						traditionalJudgeScore
								.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestions));
						traditionalJudgeScoreService.save(traditionalJudgeScore);

					}

				}

			}

		}

		// Save records for COMMONQUESTIONFORASANA

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
						listParticipantTeamParticipants.get(0), championshipRounds.getRound());

		List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
						championshipCategory.getAsanaCategory());
		if (!listAsanaEvaluationQuestions.isEmpty()) {
			for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {
				boolean recordStatus = traditionalJudgeScoreService
						.isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);
				if (recordStatus == false) {
					for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
						TraditionalJudgeScore traditionalJudgeScore = new TraditionalJudgeScore();
						traditionalJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
						traditionalJudgeScore.setParticipantTeamReferees(participantTeamReferees);
						traditionalJudgeScore.setQuestionType(QuestionTypeEnum.COMMONQUESTIONFORASANA);
						traditionalJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
						traditionalJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
						traditionalJudgeScoreService.save(traditionalJudgeScore);
					}
				}
			}
		}
	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> getMapOfAsanasWithAsanaScoreList(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
//				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
//						.findAllByRoleAndChampionshipAndChampionshipCategory(role, championship, championshipCategory);
//				for(AsanaEvaluationQuestions asanaEvaluationQuestion:listAsanaEvaluationQuestions) {
//					
//				}
				List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
								participantTeamAsanas);
				asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
			}

			participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
		}
		return participantAsanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, TraditionalJudgeScore>> getMapOfAsanasWithAsanaScoreListForRhythmicPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, TraditionalJudgeScore>> participantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			LinkedHashMap<ParticipantTeamAsanas, TraditionalJudgeScore> asanaScoreMap = new LinkedHashMap<>();
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
//				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
//						.findAllByRoleAndChampionshipAndChampionshipCategory(role, championship, championshipCategory);
//				for(AsanaEvaluationQuestions asanaEvaluationQuestion:listAsanaEvaluationQuestions) {
//					
//				}
				List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
								participantTeamAsanas);
				asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore.get(0));
			}

			participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
		}
		return participantAsanaScoreMap;
	}

	private LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> getListOfParticipantsCommonAsanaQuestionsWithAsanaScoreList(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		JudgeType judgeType = participantTeamReferees.getType();
		List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
						championshipCategory.getAsanaCategory());
		LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> commonQuestionForAsanaMap = new LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>>();
		if (!listAsanaEvaluationQuestions.isEmpty()) {
			for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {
				List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);
				commonQuestionForAsanaMap.put(asanaEvaluationQuestion, listTraditionalJudgeScore);
			}

		}
		return commonQuestionForAsanaMap;
	}

	private LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> getMapOfAsanasWithAsanaScoreListForArtisticGroup(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
						listParticipantTeamParticipants.get(0), championshipRounds.getRound());
		LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

			List<TraditionalJudgeScore> listTraditionalJudgeScore = traditionalJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamAsanasOrderBySequenceNumberAsc(
							championshipParticipantTeams, participantTeamReferees, participantTeamAsanas);
			asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
		}

		return asanaScoreMap;
	}

	private void createScoreRecordsForTraditionalJudgeArtisticGroup(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
						listParticipantTeamParticipants.get(0), championshipRounds.getRound());
		for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
			List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
					.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
							championshipCategory.getAsanaCategory());
			for (AsanaEvaluationQuestions asanaEvaluationQuestions : listAsanaEvaluationQuestions) {
				// check if already records exists for the championhsipParticipantTeam,
				// participantTeamReferee,asanaEvaluationQuestions
				boolean recordStatus = traditionalJudgeScoreService
						.isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamAsanas(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
								participantTeamAsanas);
				if (recordStatus == false) {
					TraditionalJudgeScore traditionalJudgeScore = new TraditionalJudgeScore();
					traditionalJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
					traditionalJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestions);
					traditionalJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					traditionalJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
					traditionalJudgeScore.setParticipantTeamReferees(participantTeamReferees);
					traditionalJudgeScore.setRound(championshipRounds.getRound());
					traditionalJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
					traditionalJudgeScore.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestions));
					traditionalJudgeScoreService.save(traditionalJudgeScore);

				}

			}

		}

	}

	/* ********************* Timekeeper Judge ********************************* */

	private void createScoreRecordsForTimekeeperJudge(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds, ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {

			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				for (AsanaEvaluationQuestions asanaEvaluationQuestions : listAsanaEvaluationQuestions) {
					// check if already records exists for the championhsipParticipantTeam,
					// participantTeamReferee,asanaEvaluationQuestions
					boolean recordStatus = timeKeeperJudgeScoreService
							.isExistsByChampionshipParticipantTeamAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
									championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions,
									participantTeamParticipants, participantTeamAsanas);
					if (recordStatus == false) {
						TimeKeeperJudgeScore timeKeeperJudgeScore = new TimeKeeperJudgeScore();
						timeKeeperJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
						timeKeeperJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestions);
						timeKeeperJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
						timeKeeperJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
						timeKeeperJudgeScore.setParticipantTeamParticipants(participantTeamParticipants);
						timeKeeperJudgeScore.setParticipantTeamReferees(participantTeamReferees);
						timeKeeperJudgeScore.setRound(championshipRounds.getRound());
						timeKeeperJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
						timeKeeperJudgeScore
								.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestions));
						timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);

					}

				}

			}

		}

	}

	private TimeKeeperJudgeScore getAllCommonQuestionRecordsForTimeKeeper(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		TimeKeeperJudgeScore listTimeKeeperJudgeCommonQuestionScores = new TimeKeeperJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listTimeKeeperJudgeCommonQuestionScores = timeKeeperJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0));
		}
		return listTimeKeeperJudgeCommonQuestionScores;
	}

	private TimeKeeperJudgeScore getAllCommonQuestionRecordsForTimeKeeperForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		TimeKeeperJudgeScore listTimeKeeperJudgeCommonQuestionScores = new TimeKeeperJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listTimeKeeperJudgeCommonQuestionScores = timeKeeperJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0),
							participantTeamParticipant);
		}
		return listTimeKeeperJudgeCommonQuestionScores;
	}

	private LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> getMapOfAsanasWithAsanaScoreListForTimeKeeper(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		ParticipantTeamParticipants participantTeamParticipants = participantTeam.getParticipantTeamParticipants()
				.get(0);

		LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = new LinkedHashMap<>();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				List<TimeKeeperJudgeScore> listTimeKeeperJudgeScores = new ArrayList<>();
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					listTimeKeeperJudgeScores = timeKeeperJudgeScoreService
							.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
									championshipParticipantTeams, participantTeamReferees,
									listAsanaEvaluationQuestions.get(0), participantTeamAsanas.getSequenceNumber());

				}
				participantAsanaScoreMap.put(participantTeamAsanas, listTimeKeeperJudgeScores);
			}

		}
		return participantAsanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> getMapOfAsanasWithAsanaScoreListForTimeKeeperForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = new LinkedHashMap<>();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
				System.out.println(participantTeamAsanas.getAsana().getName());
				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				List<TimeKeeperJudgeScore> listTimeKeeperJudgeScores = new ArrayList<>();
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					listTimeKeeperJudgeScores = timeKeeperJudgeScoreService
							.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndSequenceNumber(
									championshipParticipantTeams, participantTeamReferees,
									listAsanaEvaluationQuestions.get(0), participantTeamAsanas.getSequenceNumber());

				}
				participantAsanaScoreMap.put(participantTeamAsanas, listTimeKeeperJudgeScores);
			}

		}
		return participantAsanaScoreMap;
	}

	private void createScoreRecordsForTimeKeeperJudgeForOtherGroups(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LOGGER.info("Entered createScoreRecordsForTimeKeeperJudgeForOtherGroups method  -JudgeTeamScoringController");
		JudgeType judgeType = participantTeamReferees.getType();
		ParticipantTeamParticipants participantTeamParticipants = participantTeam.getParticipantTeamParticipants()
				.get(0);

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {

						boolean recordStatus = timeKeeperJudgeScoreService
								.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber(
										championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion,
										participantTeamAsanas.getSequenceNumber());

						if (recordStatus == false) {
							TimeKeeperJudgeScore timeKeeperJudgeScore = new TimeKeeperJudgeScore();

							timeKeeperJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
							timeKeeperJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
							timeKeeperJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
							timeKeeperJudgeScore.setParticipantTeamReferees(participantTeamReferees);
							timeKeeperJudgeScore.setRound(championshipRounds.getRound());
							timeKeeperJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
							timeKeeperJudgeScore
									.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
							timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);

						}

					}
				}
			}
		}

		// Common questions records
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		if (!asanaEvaluationQuestions.isEmpty()) {
			// for (AsanaEvaluationQuestions asanaEvaluationQuestion :
			// asanaEvaluationQuestions) {

			AsanaEvaluationQuestions asanaEvaluationQuestion = asanaEvaluationQuestions.get(0);
			if (asanaEvaluationQuestion != null) {
				boolean recordStatus = timeKeeperJudgeScoreService
						.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);

				if (recordStatus == false) {
					TimeKeeperJudgeScore timeKeeperJudgeScore = new TimeKeeperJudgeScore();
					timeKeeperJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
					timeKeeperJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					timeKeeperJudgeScore.setParticipantTeamReferees(participantTeamReferees);
					timeKeeperJudgeScore.setRound(championshipRounds.getRound());
					timeKeeperJudgeScore.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
					timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);
				}
			}

			// }
		}

	}

	// create records for Artistic Pair - TimeKeeper

	private ParticipantTeamParticipants createScoreRecordsForTimeKeeperJudgeForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LOGGER.info("Entered createScoreRecordsForTimeKeeperJudgeForArtisticPair method   JudgeTeamScoringController");
		JudgeType judgeType = participantTeamReferees.getType();
		// List<ParticipantTeamParticipants> listParticipantTeamParticipants =
		// participantTeam.getParticipantTeamParticipants();
		List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeamRefereesService
				.getAllByTeamAndRoundAndType(participantTeam, championshipRounds.getRound(), judgeType);
		int refereeCount = 0;
		for (int referee = 0; referee < listParticipantTeamReferees.size(); referee++) {
			if (listParticipantTeamReferees.get(referee) == participantTeamReferees) {
				refereeCount = referee;
				break;
			}

		}
		LOGGER.info("Referee count" + refereeCount);
		ParticipantTeamParticipants participantTeamParticipant = participantTeam.getParticipantTeamParticipants()
				.get(refereeCount);

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());
		if (participantScoringJudge == null) {
			// create mapping record & create scoring records
			ParticipantScoringJudge participantScoringJudgeNew = new ParticipantScoringJudge();
			participantScoringJudgeNew.setChampionship(championship);
			participantScoringJudgeNew.setChampionshipCategory(championshipCategory);
			participantScoringJudgeNew.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantScoringJudgeNew.setParticipantTeamReferees(participantTeamReferees);
			participantScoringJudgeNew.setParticipantTeamParticipants(participantTeamParticipant);
			participantScoringJudgeNew.setType(judgeType);
			participantScoringJudgeNew.setRound(championshipRounds.getRound());

			participantScoringJudgeService.save(participantScoringJudgeNew);

		}

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
				System.out.println(participantTeamAsanas.getAsana().getName());
				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {

						boolean recordStatus = timeKeeperJudgeScoreService
								.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber(
										championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion,
										participantTeamAsanas.getSequenceNumber());

						if (recordStatus == false) {
							TimeKeeperJudgeScore timeKeeperJudgeScore = new TimeKeeperJudgeScore();

							timeKeeperJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
							timeKeeperJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
							timeKeeperJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
							timeKeeperJudgeScore.setParticipantTeamReferees(participantTeamReferees);
							timeKeeperJudgeScore.setRound(championshipRounds.getRound());
							timeKeeperJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
							timeKeeperJudgeScore.setParticipantTeamParticipants(participantTeamParticipant);
							timeKeeperJudgeScore
									.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
							timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);

						}

					}
				}
			}

		}

		// Common questions records
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		if (!asanaEvaluationQuestions.isEmpty()) {
			// for (AsanaEvaluationQuestions asanaEvaluationQuestion :
			// asanaEvaluationQuestions) {

			AsanaEvaluationQuestions asanaEvaluationQuestion = asanaEvaluationQuestions.get(0);
			if (asanaEvaluationQuestion != null) {
				boolean recordStatus = timeKeeperJudgeScoreService
						.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);

				if (recordStatus == false) {
					TimeKeeperJudgeScore timeKeeperJudgeScore = new TimeKeeperJudgeScore();
					timeKeeperJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
					timeKeeperJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					timeKeeperJudgeScore.setParticipantTeamReferees(participantTeamReferees);
					timeKeeperJudgeScore.setRound(championshipRounds.getRound());
					timeKeeperJudgeScore.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
					timeKeeperJudgeScore.setParticipantTeamParticipants(participantTeamParticipant);
					timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);
				}
			}

			// }
		}
		return participantTeamParticipant;

	}

	/* ********************* Evaluator Judge ********************************* */

	private void createScoreRecordsForEvaluatorJudgeForTraditional(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LOGGER.info("Entered createScoreRecordsForEvaluatorJudgeForTraditional method  JudgeTeamScoringController");
		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {

						boolean recordStatus = evaluatorJudgeScoreService
								.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndParticipantTeamParticipantsAndParticipantTeamAsanas(
										championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion,
										participantTeamParticipants, participantTeamAsanas);

						if (recordStatus == false) {
							EvaluatorJudgeScore evaluatorJudgeScore = new EvaluatorJudgeScore();
							evaluatorJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
							evaluatorJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
							evaluatorJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
							evaluatorJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
							evaluatorJudgeScore.setParticipantTeamParticipants(participantTeamParticipants);
							evaluatorJudgeScore.setParticipantTeamReferees(participantTeamReferees);
							evaluatorJudgeScore.setRound(championshipRounds.getRound());
							evaluatorJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
							// evaluatorJudgeScore.setIsPerformed(AsanaPerformEnum.NOTPERFORMED);
							// evaluatorJudgeScore.setAsPerSequence(SequenceEnum.DIDNOTPERFORMINSEQUENCE);
							evaluatorJudgeScore
									.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
							evaluatorJudgeScoreService.save(evaluatorJudgeScore);

						}

					}
				}
			}

		}

	}

	private void createScoreRecordsForEvaluatorJudgeForOtherGroups(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LOGGER.info("Entered createScoreRecordsForEvaluatorJudgeForOtherGroups method  JudgeTeamScoringController");
		JudgeType judgeType = participantTeamReferees.getType();
		ParticipantTeamParticipants participantTeamParticipants = participantTeam.getParticipantTeamParticipants()
				.get(0);

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {

						boolean recordStatus = evaluatorJudgeScoreService
								.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber(
										championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion,
										participantTeamAsanas.getSequenceNumber());

						if (recordStatus == false) {
							EvaluatorJudgeScore evaluatorJudgeScore = new EvaluatorJudgeScore();
							evaluatorJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
							evaluatorJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
							evaluatorJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
							evaluatorJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
							evaluatorJudgeScore.setParticipantTeamParticipants(participantTeamParticipants);
							evaluatorJudgeScore.setParticipantTeamReferees(participantTeamReferees);
							evaluatorJudgeScore.setRound(championshipRounds.getRound());
							evaluatorJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
							evaluatorJudgeScore
									.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
							evaluatorJudgeScoreService.save(evaluatorJudgeScore);

						}

					}
				}
			}
		}

		// Common questions records
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		if (!asanaEvaluationQuestions.isEmpty()) {
			// for (AsanaEvaluationQuestions asanaEvaluationQuestion :
			// asanaEvaluationQuestions) {

			AsanaEvaluationQuestions asanaEvaluationQuestion = asanaEvaluationQuestions.get(0);
			if (asanaEvaluationQuestion != null) {
				boolean recordStatus = evaluatorJudgeScoreService
						.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);

				if (recordStatus == false) {
					EvaluatorJudgeScore evaluatorJudgeScore = new EvaluatorJudgeScore();
					evaluatorJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
					evaluatorJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					evaluatorJudgeScore.setParticipantTeamParticipants(participantTeamParticipants);
					evaluatorJudgeScore.setParticipantTeamReferees(participantTeamReferees);
					evaluatorJudgeScore.setRound(championshipRounds.getRound());
					evaluatorJudgeScore.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
					evaluatorJudgeScoreService.save(evaluatorJudgeScore);
				}
			}

			// }
		}
	}

	private EvaluatorJudgeScore getAllCommonQuestionRecords(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds, ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LinkedHashMap<AsanaEvaluationQuestions, List<EvaluatorJudgeScore>> commonQuestionRecordsMap = new LinkedHashMap<>();
		JudgeType judgeType = participantTeamReferees.getType();
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		EvaluatorJudgeScore listEvaluatorJudgeCommonQuestionScores = new EvaluatorJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listEvaluatorJudgeCommonQuestionScores = evaluatorJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestion(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0));
		}
		return listEvaluatorJudgeCommonQuestionScores;

	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> getMapOfAsanasWithAsanaScoreListForEvaluator(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
				.getParticipantTeamParticipants();
		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
			List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipants,
							championshipRounds.getRound());
			LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<EvaluatorJudgeScore> listEvaluatorJudgeScore = evaluatorJudgeScoreService
						.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
								championshipParticipantTeams, participantTeamReferees, participantTeamParticipants,
								participantTeamAsanas);
				asanaScoreMap.put(participantTeamAsanas, listEvaluatorJudgeScore);
			}

			participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
		}
		return participantAsanaScoreMap;
	}

	// Artistic Pair Evaluator

	private ParticipantTeamParticipants createScoreRecordsForEvaluatorJudgeForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LOGGER.info("Entered createScoreRecordsForEvaluatorJudgeForArtisticPair method -JudgeTeamScoringController");
		JudgeType judgeType = participantTeamReferees.getType();
		List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeamRefereesService
				.getAllByTeamAndRoundAndType(participantTeam, championshipRounds.getRound(), judgeType);

		int refereeCount = 0;
		for (int referee = 0; referee < listParticipantTeamReferees.size(); referee++) {
			if (listParticipantTeamReferees.get(referee) == participantTeamReferees) {
				refereeCount = referee;
				break;
			}

		}
		System.out.println("Referee count" + refereeCount);
		ParticipantTeamParticipants participantTeamParticipant = participantTeam.getParticipantTeamParticipants()
				.get(refereeCount);

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());
		if (participantScoringJudge == null) {
			// create mapping record & create scoring records
			ParticipantScoringJudge participantScoringJudgeNew = new ParticipantScoringJudge();
			participantScoringJudgeNew.setChampionship(championship);
			participantScoringJudgeNew.setChampionshipCategory(championshipCategory);
			participantScoringJudgeNew.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantScoringJudgeNew.setParticipantTeamReferees(participantTeamReferees);
			participantScoringJudgeNew.setParticipantTeamParticipants(participantTeamParticipant);
			participantScoringJudgeNew.setType(judgeType);
			participantScoringJudgeNew.setRound(championshipRounds.getRound());

			participantScoringJudgeService.save(participantScoringJudgeNew);

		}

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
						championshipRounds.getRound());
		if (!listParticipantTeamAsanas.isEmpty()) {
			for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

				List<AsanaEvaluationQuestions> listAsanaEvaluationQuestions = asanaEvaluationQuestionsService
						.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONQUESTIONFORASANA(judgeType,
								championshipCategory.getAsanaCategory());
				if (!listAsanaEvaluationQuestions.isEmpty()) {
					for (AsanaEvaluationQuestions asanaEvaluationQuestion : listAsanaEvaluationQuestions) {

						boolean recordStatus = evaluatorJudgeScoreService
								.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionsAndSequenceNumber(
										championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion,
										participantTeamAsanas.getSequenceNumber());

						if (recordStatus == false) {
							EvaluatorJudgeScore evaluatorJudgeScore = new EvaluatorJudgeScore();
							evaluatorJudgeScore.setAsanaBaseValue(participantTeamAsanas.getBaseValue());
							evaluatorJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
							evaluatorJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
							evaluatorJudgeScore.setParticipantTeamAsanas(participantTeamAsanas);
							evaluatorJudgeScore.setParticipantTeamParticipants(participantTeamParticipant);
							evaluatorJudgeScore.setParticipantTeamReferees(participantTeamReferees);
							evaluatorJudgeScore.setRound(championshipRounds.getRound());
							evaluatorJudgeScore.setSequenceNumber(participantTeamAsanas.getSequenceNumber());
							evaluatorJudgeScore
									.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
							evaluatorJudgeScoreService.save(evaluatorJudgeScore);

						}

					}
				}
			}

		}

		// Common questions records
		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		if (!asanaEvaluationQuestions.isEmpty()) {
			// for (AsanaEvaluationQuestions asanaEvaluationQuestion :
			// asanaEvaluationQuestions) {

			AsanaEvaluationQuestions asanaEvaluationQuestion = asanaEvaluationQuestions.get(0);
			if (asanaEvaluationQuestion != null) {
				boolean recordStatus = evaluatorJudgeScoreService
						.isExistsByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestions(
								championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestion);

				if (recordStatus == false) {
					EvaluatorJudgeScore evaluatorJudgeScore = new EvaluatorJudgeScore();
					evaluatorJudgeScore.setAsanaEvaluationQuestion(asanaEvaluationQuestion);
					evaluatorJudgeScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					evaluatorJudgeScore.setParticipantTeamParticipants(participantTeamParticipant);
					evaluatorJudgeScore.setParticipantTeamReferees(participantTeamReferees);
					evaluatorJudgeScore.setRound(championshipRounds.getRound());
					evaluatorJudgeScore.setQuestionType(questionTypeUtil.getQuestionType(asanaEvaluationQuestion));
					evaluatorJudgeScoreService.save(evaluatorJudgeScore);
				}
			}

			// }
		}
		return participantTeamParticipant;
	}

	private LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> getMapOfAsanasWithAsanaScoreListForEvaluatorForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {

		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = new LinkedHashMap<>();

		List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
				.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
						championshipRounds.getRound());
		LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {

			List<EvaluatorJudgeScore> listEvaluatorJudgeScore = evaluatorJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
							championshipParticipantTeams, participantTeamReferees, participantTeamParticipant,
							participantTeamAsanas);
			asanaScoreMap.put(participantTeamAsanas, listEvaluatorJudgeScore);
		}

		participantAsanaScoreMap.put(participantTeamParticipant, asanaScoreMap);

		return participantAsanaScoreMap;
	}

	private EvaluatorJudgeScore getAllCommonQuestionRecordsForArtisticPair(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ChampionshipCategory championshipCategory, Championship championship,
			ParticipantTeamReferees participantTeamReferees, ParticipantTeam participantTeam) {
		LinkedHashMap<AsanaEvaluationQuestions, List<EvaluatorJudgeScore>> commonQuestionRecordsMap = new LinkedHashMap<>();
		JudgeType judgeType = participantTeamReferees.getType();

		ParticipantScoringJudge participantScoringJudge = participantScoringJudgeService
				.findByParticipantTeamRefereesAndChampionshipParticipantTeamsAndChampionshipCategoryAndChampionshipAndTypeAndRound(
						participantTeamReferees, championshipParticipantTeams, championshipCategory, championship,
						judgeType, championshipRounds.getRound());

		ParticipantTeamParticipants participantTeamParticipant = null;
		if (participantScoringJudge != null) {
			participantTeamParticipant = participantScoringJudge.getParticipantTeamParticipants();
		}

		List<AsanaEvaluationQuestions> asanaEvaluationQuestions = asanaEvaluationQuestionsService
				.findAllByTypeAndAsanaCategoryWithQuestionTypeCOMMONTEAMQUESTION(judgeType,
						championshipCategory.getAsanaCategory());
		EvaluatorJudgeScore listEvaluatorJudgeCommonQuestionScores = new EvaluatorJudgeScore();
		if (!asanaEvaluationQuestions.isEmpty()) {
			listEvaluatorJudgeCommonQuestionScores = evaluatorJudgeScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndAsanaEvaluationQuestionAndParticipantTeamParticipants(
							championshipParticipantTeams, participantTeamReferees, asanaEvaluationQuestions.get(0),
							participantTeamParticipant);
		}
		return listEvaluatorJudgeCommonQuestionScores;

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/common-team-scoring")
	public String commonJudgeLoginForm(Model model, HttpServletRequest request) throws JudgeNotFoundException {

		LOGGER.info("Entered commonJudgeLoginForm method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		List<Championship> listChampionships = championshipService
				.listAllChampionshipsByStatusCompletedAndByCurrentUser(currentUser);

		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Select Categories");
		LOGGER.info("Exit commonJudgeLoginForm method -JudgeTeamScoringController");

		return "judge/common_judge_login_form";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/common-team-scoring")
	public String getTeamsForJudge(Model model, @RequestParam("championship") Integer championshipId,
			@RequestParam("asana-category") Integer asanaCategoryId, @RequestParam("gender") String gender,
			@RequestParam("category") Integer ageCategoryId, @RequestParam("round") Integer round,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EntityNotFoundException, ChampionshipNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered getTeamsForJudge method -JudgeTeamScoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		HttpSession session = request.getSession();

		Championship championship = championshipService.get(championshipId);
		ChampionshipCategory championshipCategory = championshipCategoryService
				.getChampionshipCategoryForAllConditions(championshipId, asanaCategoryId, ageCategoryId, gender);
		Integer champCategoryId = championshipCategoryService.get(championshipCategory.getId()).getId();
		List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeamRefereesService
				.getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(round, championship, championshipCategory,
						currentUser);
		ChampionshipRounds championshipRound = championshipRoundsService
				.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
		if (championshipRound != null) {
			if (championshipRound.getStatus().equals(RoundStatusEnum.COMPLETED)) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"The event is already completed, please contact admin for more details.");
				return "redirect:/judge/common-team-scoring";
			}

		} else {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Unable to load the teams, please try again after sometime");
			return "redirect:/judge/common-team-scoring";
		}
		if (listParticipantTeamReferees.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"You are not a member of the Panel. You are not eligible to see the teams.");
			return "redirect:/judge/common-team-scoring";

		} else {
			ParticipantTeamReferees participantTeamReferee = listParticipantTeamReferees.get(0);
			// model.addAttribute("judgeType", participantTeamReferee.getType().getType());
			session.setAttribute("judgeTypeName", participantTeamReferee.getType().getType());

			if (participantTeamReferee.getType().getId() == 2) {

				return "redirect:/judge/championship/" + championshipId + "/category/" + asanaCategoryId + "/"
						+ ageCategoryId + "/" + gender + "/" + round;
			}

			if (participantTeamReferee.getType().getId() == 4) {

				return "redirect:/judge/championship/" + championshipId + "/category/" + asanaCategoryId + "/"
						+ ageCategoryId + "/" + gender + "/" + round;
			}

			if (participantTeamReferee.getType().getId() == 1) {

				return "redirect:/referee/view-scores/championship/" + championshipId + "/category/" + asanaCategoryId
						+ "/" + ageCategoryId + "/" + gender + "/" + round;
			}

			if (participantTeamReferee.getType().getId() == 6) {

				return "redirect:/referee/view-scores/championship/" + championshipId + "/category/" + asanaCategoryId
						+ "/" + ageCategoryId + "/" + gender + "/" + round;
			}

			return "redirect:/judge/championship/" + championshipId + "/category/" + asanaCategoryId + "/"
					+ ageCategoryId + "/" + gender + "/" + round;
		}
	}
}
