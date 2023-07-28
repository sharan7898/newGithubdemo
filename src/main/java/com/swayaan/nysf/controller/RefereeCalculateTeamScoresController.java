package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.ArtisticJudgeScore;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaEvaluationQuestions;
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
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.entity.TraditionalJudgeScore;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.ArtisticJudgeScoreService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaEvaluationQuestionsService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.EvaluatorJudgeScoreService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.ParticipantScoringJudgeService;
import com.swayaan.nysf.service.ParticipantTeamAsanaJudgeTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamAsanaQuestionTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamJudgeTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.TimeKeeperJudgeScoreService;
import com.swayaan.nysf.service.TraditionalJudgeScoreService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/referee")
public class RefereeCalculateTeamScoresController {

	@Autowired
	ParticipantTeamAsanaJudgeTotalScoreService participantTeamAsanaJudgeTotalScoreService;
	@Autowired
	ParticipantTeamAsanaQuestionTotalScoreService participantTeamAsanaQuestionTotalScoreService;
	@Autowired
	ParticipantTeamJudgeTotalScoreService participantTeamJudgeTotalScoreService;
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	UserService userService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	EvaluatorJudgeScoreService evaluatorJudgeScoreService;
	@Autowired
	TraditionalJudgeScoreService traditionalJudgeScoreService;
	@Autowired
	TimeKeeperJudgeScoreService timeKeeperJudgeScoreService;
	@Autowired
	ArtisticJudgeScoreService artisticJudgeScoreService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	RoleService roleService;
	@Autowired
	JudgeTypeService judgeTypeService;
	@Autowired
	RefereesPanelsService refereesPanelsService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	AsanaEvaluationQuestionsService asanaEvaluationQuestionsService;
	@Autowired
	ParticipantScoringJudgeService participantScoringJudgeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefereeCalculateTeamScoresController.class);
	private static final boolean EvaluatorJudgeScore = false;

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int D_JUDGE_TYPE_ID = 2;
	private static final int A_JUDGE_TYPE_ID = 3;
	private static final int T_JUDGE_TYPE_ID = 4;
	private static final int E_JUDGE_TYPE_ID = 5;

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/view-scores")
	public String getJudgeScores(Model model, HttpServletRequest request) {
		LOGGER.info("Entered getJudgeScores method RefereeCalculateTeamScoresController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Championship - View Judge Scores");
		LOGGER.info("Exit getJudgeScores method RefereeCalculateTeamScoresController");

		return "referee/view_championships";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/view-scores/championship/{championshipId}/getChampionshipCategories")
	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered listChampionshipCategories method RefereeCalculateTeamScoresController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship;
		try {
			championship = championshipService.getChampionshipById(championshipId);
		} catch (ChampionshipNotFoundException e) {
			model.addAttribute("errorMessage", "Championship Not Found.");
			return "referee/view_championships";
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
		LOGGER.info("Exit listChampionshipCategories method RefereeCalculateTeamScoresController");

		return "referee/view_championship_category_form";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/view-scores/championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}")
	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws AsanaCategoryNotFoundException, EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered listAllTeamsForChampionhsip method RefereeCalculateTeamScoresController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listAllTeamsForChampionhsip method RefereeCalculateTeamScoresController");

		return listAllChampionshipTeamsByPage(id, asanaCategoryId, gender, ageCategoryId, round, 1, model,
				"chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/view-scores/championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}/page/{pageNum}")
	public String listAllChampionshipTeamsByPage(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) {
		LOGGER.info("Entered listAllChampionshipTeamsByPage method RefereeCalculateTeamScoresController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		RoundStatusEnum status = RoundStatusEnum.ONGOING;
		StatusEnum participantTeamStatus = StatusEnum.SCHEDULED;
		Championship championship;

		model.addAttribute("pageTitle", "Manage Championship");
		try {
			championship = championshipService.get(id);
			User user = CommonUtil.getCurrentUser();
			Judge judge = CommonUtil.getCurrentJudge();
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

			List<ParticipantTeamReferees> listChiefJudgeRefereeTeams = participantTeamRefereesService
					.getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(round, championship,
							championshipCategory, user);
			System.out.println("listChiefJudgeRefereeTeams" + listChiefJudgeRefereeTeams);
			if (listChiefJudgeRefereeTeams.isEmpty() && user.getRoleId() == 2) {

				ra.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to view teams.");
				return "redirect:/judge/common-team-scoring";
			}

			long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
			long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

			if (endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}

			String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

			model.addAttribute("moduleURL", "/referee/view-scores/championship/" + id + "/category/" + asanaCategoryId
					+ "/" + ageCategoryId + "/" + gender + "/" + round);

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
		} catch (JudgeNotFoundException e) {
			e.printStackTrace();
		} catch (AsanaCategoryNotFoundException e) {
			e.printStackTrace();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		LOGGER.info("Exit listAllChampionshipTeamsByPage method RefereeCalculateTeamScoresController");

		return "referee/view_championship_team_form";
	}

//	@PreAuthorize("hasAuthority('Judge')")
//	@GetMapping("/view-scores/championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}")
//	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
//			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
//			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
//			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request)
//			throws AsanaCategoryNotFoundException, EntityNotFoundException, JudgeNotFoundException {
//		LOGGER.info("Entered listAllTeamsForChampionhsip RefereeCalculateTeamScoresController");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		RoundStatusEnum status = RoundStatusEnum.ONGOING;
//		StatusEnum participantTeamStatus = StatusEnum.SCHEDULED;
//		Championship championship;
//		
//		model.addAttribute("pageTitle", "Manage Championship");
//		try {
//			championship = championshipService.get(id);
//			User user = CommonUtil.getCurrentUser();
//			Judge judge=CommonUtil.getCurrentJudge();
//			AsanaCategory asanaCategory = asanaCategoryService.getAsanaCategoryById(asanaCategoryId);
//			AgeCategory ageCategory = ageCategoryService.get(ageCategoryId);
//			ChampionshipCategory championshipCategory = championshipCategoryService
//					.getChampionshipCategoryForAllConditions(id, asanaCategoryId, ageCategoryId, gender);
//			Integer championshipCategoryId = championshipCategory.getId();
//			ChampionshipRounds championshipRounds = championshipRoundsService
//					.getByRoundAndChampionshipAndChampionshipCategoryAndStatus(round, championship,
//							championshipCategory, status);
//			LOGGER.info("championshipRounds" + championshipRounds);
//			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
//			if (championshipRounds != null) {
////				listChampionshipParticipantTeams = championshipParticipantTeamsService
////						.getTeamsByChampionshipRoundsAndStatusNot(championshipRounds, participantTeamStatus);
//				listChampionshipParticipantTeams = championshipParticipantTeamsService
//						.getTeamsByChampionshipRoundsAndStatusNot(championshipRounds);
//			}
//			List<ParticipantTeamReferees> listChiefJudgeRefereeTeams = participantTeamRefereesService
//					.getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(round, championship, championshipCategory,
//							user);
//			System.out.println("listChiefJudgeRefereeTeams"+listChiefJudgeRefereeTeams);
//			if (listChiefJudgeRefereeTeams.isEmpty() && user.getRoleId() == 2) {
//				
//				redirectAttributes.addFlashAttribute("errorMessage",
//						"You are not a member of the Panel. You are not eligible to view teams.");
//				return "redirect:/judge/common-team-scoring";
//			}
//			LOGGER.info("listChampionshipParticipantTeams" + listChampionshipParticipantTeams);
//			model.addAttribute("asanaCategoryId", asanaCategoryId);
//			model.addAttribute("ageCategoryId", ageCategoryId);
//			model.addAttribute("gender", gender);
//			model.addAttribute("round", round);
//			model.addAttribute("championshipId", id);
//			model.addAttribute("listChampionshipParticipantTeams", listChampionshipParticipantTeams);
//			model.addAttribute("pageTitle", championship.getName() + " Teams");
//
//		} catch (ChampionshipNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JudgeNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return "referee/view_championship_team_form";
//	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/view-scores/championshipTeam/{championshipTeamId}/calculateScores")
	public String getTeamAndCalculateScores(@PathVariable(name = "championshipTeamId") Integer championshipTeamId,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantTeamNotFoundException, RoleNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered getTeamAndCalculateScores method RefereeCalculateTeamScoresController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		User user = CommonUtil.getCurrentUser();
		ChampionshipParticipantTeams championshipParticipantTeams;
		if (currentUser.getRoleName().equals("Administrator") || currentUser.getRoleName().equals("SubAdministrator")) {
			model.addAttribute("viewScores", "adminViewScores");
		} else if (currentUser.getRoleName().equals("Judge")) {
			model.addAttribute("viewScores", "refereeViewScores");
		} else if (currentUser.getRoleName().equals("EventManager")) {
			model.addAttribute("viewScores", "eventManagerViewScores");
		}
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipTeamId);
			ChampionshipRounds championshipRounds = championshipParticipantTeams.getChampionshipRounds();
			ChampionshipCategory championshipCategory = championshipRounds.getChampionshipCategory();
			Championship championship = championshipParticipantTeams.getChampionshipRounds().getChampionship();
			ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
			AsanaCategory asanaCategory = participantTeam.getAsanaCategory();

			Integer round = championshipRounds.getRound();

			boolean allJudgeScoresGivenStatus = checkAllJudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			// boolean allJudgeScoresGivenStatus = true;
			model.addAttribute("allJudgeScoresGivenStatus", allJudgeScoresGivenStatus);
			model.addAttribute("round", championshipParticipantTeams.getChampionshipRounds().getRound());
			model.addAttribute("championshipId", championship.getId());
			model.addAttribute("asanaCategoryId", championshipParticipantTeams.getChampionshipRounds()
					.getChampionshipCategory().getAsanaCategory().getId());
			model.addAttribute("ageCategoryId", championshipParticipantTeams.getChampionshipRounds()
					.getChampionshipCategory().getCategory().getId());
			model.addAttribute("gender",
					championshipParticipantTeams.getChampionshipRounds().getChampionshipCategory().getGender());
			model.addAttribute("pageTitle", asanaCategory.getName() + " - " + participantTeam.getChestNumber());
			model.addAttribute("championshipParticipantTeams", championshipParticipantTeams);

			if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID) {

				// ========== get Evaluator Judge Scores =================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ========== get D Judge Scores =================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);

				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>>> refereeParticipantAsanaDJudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees dJudgeTeamReferee : listDjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);
					refereeParticipantAsanaDJudgeScoreMap.put(dJudgeTeamReferee, participantAsanaScoreMap);
				}

				model.addAttribute("refereeParticipantAsanaDJudgeScoreMap", refereeParticipantAsanaDJudgeScoreMap);

				// ========== get T Judge Scores =================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, List<TimeKeeperJudgeScore>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					List<TimeKeeperJudgeScore> listTimekeeperScoring = timeKeeperJudgeScoreService
							.findAllByChampionshipParticipantTeamsAndParticipantTeamRefereesOrderBySequenceNumberAsc(
									championshipParticipantTeams, tJudgeTeamReferee);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, listTimekeeperScoring);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				LOGGER.info("Returning team_score_calculation_form ");
				return "referee/viewscores/traditional_single_judge_scores";

			} else if (asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {

				// ========== get Evaluator Judge Scores =================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
							championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
							participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					if (commonQuestion != null) {
						refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
					}
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);
				List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
						.getParticipantTeamParticipants();
				LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaDjudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
					List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
							.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
									participantTeamParticipants, championshipRounds.getRound());
					LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
					for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
						List<TraditionalJudgeScore> listTraditionalJudgeScore = new ArrayList<>();
						for (ParticipantTeamReferees dJudgeParticipantTeamReferee : listDjudgeParticipantTeamReferees) {
							TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService
									.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
											championshipParticipantTeams, dJudgeParticipantTeamReferee,
											participantTeamParticipants, participantTeamAsanas);
							listTraditionalJudgeScore.add(traditionalJudgeScore);

							asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
						}
					}
					participantAsanaDjudgeScoreMap.put(participantTeamParticipants, asanaScoreMap);
				}
				model.addAttribute("participantAsanaDjudgeScoreMap", participantAsanaDjudgeScoreMap);

				// ================= get A Judge Scores ====================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// ================ get T Judge Scores ====================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);

					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					if (commonQuestionScore != null) {
						refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
					}
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning artistic_single_judge_scores page");
				return "referee/viewscores/artistic_single_judge_scores";
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {

				// ============= get Evaluator Judge Scores =====================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
//						EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
//						championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
//						participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					// refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee,
					// commonQuestion);

					LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForEvaluatorForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							eJudgeParticipantTeamReferee, participantTeam);
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecordsForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							eJudgeParticipantTeamReferee, participantTeam);
					refereeParticipantAsanaScoreMap.put(eJudgeParticipantTeamReferee, participantAsanaScoreMap);
					if (commonQuestion != null) {
						refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
					}
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);
				List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
						.getParticipantTeamParticipants();

				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>>> refereeParticipantAsanaDJudgeScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>>> refereeDJudgeCommonQuestionAsanaScoreMap = new LinkedHashMap<>();

				for (ParticipantTeamReferees dJudgeTeamReferee : listDjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreList(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);
					LinkedHashMap<AsanaEvaluationQuestions, List<TraditionalJudgeScore>> commonQuestionForAsanaMap = getListOfParticipantsCommonAsanaQuestionsWithAsanaScoreList(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);
					refereeParticipantAsanaDJudgeScoreMap.put(dJudgeTeamReferee, participantAsanaScoreMap);
					refereeDJudgeCommonQuestionAsanaScoreMap.put(dJudgeTeamReferee, commonQuestionForAsanaMap);
				}
				model.addAttribute("refereeParticipantAsanaDJudgeScoreMap", refereeParticipantAsanaDJudgeScoreMap);
				model.addAttribute("refereeDJudgeCommonQuestionAsanaScoreMap",
						refereeDJudgeCommonQuestionAsanaScoreMap);

				// ==================== get A Judge Scores =========================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// =================== get T Judge Scores =========================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
//					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
//							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
//							tJudgeTeamReferee, participantTeam);
//					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);
//					
//					// get common question records
//					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
//							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
//							tJudgeTeamReferee, participantTeam);
//					
//					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);

					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeperForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);
					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeperForArtisticPair(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning team_score_calculation_form ");
				return "referee/viewscores/artistic_pair_judge_scores";
			} else if (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {

				// ================= get Evaluator Scores =======================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
							championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
							participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);
				List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeam
						.getParticipantTeamParticipants();
				LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> participantAsanaDjudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
					List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
							.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam,
									participantTeamParticipants, championshipRounds.getRound());
					LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = new LinkedHashMap<>();
					for (ParticipantTeamAsanas participantTeamAsanas : listParticipantTeamAsanas) {
						List<TraditionalJudgeScore> listTraditionalJudgeScore = new ArrayList<>();
						for (ParticipantTeamReferees dJudgeParticipantTeamReferee : listDjudgeParticipantTeamReferees) {
							TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService
									.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndParticipantTeamParticipantsAndParticipantTeamAsanasOrderBySequenceNumberAsc(
											championshipParticipantTeams, dJudgeParticipantTeamReferee,
											participantTeamParticipants, participantTeamAsanas);
							listTraditionalJudgeScore.add(traditionalJudgeScore);
							// System.out.println("traditionalJudgeScore : " +
							// traditionalJudgeScore.toString());
							asanaScoreMap.put(participantTeamAsanas, listTraditionalJudgeScore);
						}
					}
					participantAsanaDjudgeScoreMap.put(participantTeamParticipants, asanaScoreMap);
				}
				model.addAttribute("participantAsanaDjudgeScoreMap", participantAsanaDjudgeScoreMap);

				// ================= get A Judge Scores =======================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// ================== get T Judge Scores =======================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);

					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);

					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning team_score_calculation_form ");
				return "referee/viewscores/rhythmic_pair_judge_scores";

			} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {

				// ================= get Evaluator Scores =======================
				JudgeType eJudgeType = judgeTypeService.get(E_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, eJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = getMapAsanaWithEJudgeScores(
						championshipParticipantTeams, championshipRounds, participantTeam,
						listEjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, EvaluatorJudgeScore> refereeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
					// get common question records
					EvaluatorJudgeScore commonQuestion = getAllCommonQuestionRecords(championshipParticipantTeams,
							championshipRounds, championshipCategory, championship, eJudgeParticipantTeamReferee,
							participantTeam);
					// System.out.println("commonQuestion penalty score: " +
					// commonQuestion.getPenaltyScore());
					if (commonQuestion != null) {
						refereeCommonQuestionScoreMap.put(eJudgeParticipantTeamReferee, commonQuestion);
					}
				}

				model.addAttribute("refereeCommonQuestionScoreMap", refereeCommonQuestionScoreMap);
				model.addAttribute("refereeParticipantAsanaScoreMap", refereeParticipantAsanaScoreMap);

				// ============= get D Judge Scores ======================
				JudgeType dJudgeType = judgeTypeService.get(D_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listDjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, dJudgeType);
				// model.addAttribute("listDjudgeNames", listDjudgeParticipantTeamReferees);

				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>>> refereeAsanaDJudgeScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees dJudgeTeamReferee : listDjudgeParticipantTeamReferees) {

					LinkedHashMap<ParticipantTeamAsanas, List<TraditionalJudgeScore>> asanaScoreMap = getMapOfAsanasWithAsanaScoreListForArtisticGroup(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							dJudgeTeamReferee, participantTeam);

					refereeAsanaDJudgeScoreMap.put(dJudgeTeamReferee, asanaScoreMap);
				}
				model.addAttribute("refereeAsanaDJudgeScoreMap", refereeAsanaDJudgeScoreMap);

				// ================= get A Judge Scores =======================
				LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = getCommonAJudgeScoresForTeam(
						championshipParticipantTeams, participantTeam, round);
				model.addAttribute("refereeAJudgeTeamScoreMap", refereeAJudgeTeamScoreMap);

				// ================== get T Judge Scores =======================
				JudgeType tJudgeType = judgeTypeService.get(T_JUDGE_TYPE_ID);
				List<ParticipantTeamReferees> listTjudgeParticipantTeamReferees = participantTeamRefereesService
						.getAllByTeamAndRoundAndType(participantTeam, round, tJudgeType);
				LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>>> refereeTJudgeAsanaScoreMap = new LinkedHashMap<>();
				LinkedHashMap<ParticipantTeamReferees, TimeKeeperJudgeScore> refereeTJudgeCommonQuestionScoreMap = new LinkedHashMap<>();
				for (ParticipantTeamReferees tJudgeTeamReferee : listTjudgeParticipantTeamReferees) {
					LinkedHashMap<ParticipantTeamAsanas, List<TimeKeeperJudgeScore>> participantAsanaScoreMap = getMapOfAsanasWithAsanaScoreListForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);
					refereeTJudgeAsanaScoreMap.put(tJudgeTeamReferee, participantAsanaScoreMap);

					// get common question records
					TimeKeeperJudgeScore commonQuestionScore = getAllCommonQuestionRecordsForTimeKeeper(
							championshipParticipantTeams, championshipRounds, championshipCategory, championship,
							tJudgeTeamReferee, participantTeam);

					refereeTJudgeCommonQuestionScoreMap.put(tJudgeTeamReferee, commonQuestionScore);
				}

				model.addAttribute("refereeTJudgeAsanaScoreMap", refereeTJudgeAsanaScoreMap);
				model.addAttribute("refereeTJudgeCommonQuestionScoreMap", refereeTJudgeCommonQuestionScoreMap);

				LOGGER.info("Returning team_score_calculation_form method -RefereeCalculateTeamScoresController ");
				return "referee/viewscores/artistic_group_judge_scores";

			}

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Returning team_score_calculation_form ");
			return "referee/view_judge_scoring";
		}
		// return "referee/view_judge_scoring";
		return null;

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

	private LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> getMapAsanaWithEJudgeScores(
			ChampionshipParticipantTeams championshipParticipantTeams, ChampionshipRounds championshipRounds,
			ParticipantTeam participantTeam, List<ParticipantTeamReferees> listEjudgeParticipantTeamReferees) {

		LinkedHashMap<ParticipantTeamReferees, LinkedHashMap<ParticipantTeamParticipants, LinkedHashMap<ParticipantTeamAsanas, List<EvaluatorJudgeScore>>>> refereeParticipantAsanaScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamReferees eJudgeParticipantTeamReferee : listEjudgeParticipantTeamReferees) {
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
									championshipParticipantTeams, eJudgeParticipantTeamReferee,
									participantTeamParticipants, participantTeamAsanas);
					asanaScoreMap.put(participantTeamAsanas, listEvaluatorJudgeScore);
				}
				participantAsanaScoreMap.put(participantTeamParticipants, asanaScoreMap);
			}
			refereeParticipantAsanaScoreMap.put(eJudgeParticipantTeamReferee, participantAsanaScoreMap);
		}

		return refereeParticipantAsanaScoreMap;
	}

	private LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> getCommonAJudgeScoresForTeam(
			ChampionshipParticipantTeams championshipParticipantTeams, ParticipantTeam participantTeam, Integer round)
			throws RoleNotFoundException, EntityNotFoundException {
		JudgeType aJudgeType = judgeTypeService.get(A_JUDGE_TYPE_ID);
		List<ParticipantTeamReferees> listAjudgeParticipantTeamReferees = participantTeamRefereesService
				.getAllByTeamAndRoundAndType(participantTeam, round, aJudgeType);
		LinkedHashMap<ParticipantTeamReferees, List<ArtisticJudgeScore>> refereeAJudgeTeamScoreMap = new LinkedHashMap<>();
		for (ParticipantTeamReferees aJudgeTeamReferee : listAjudgeParticipantTeamReferees) {
			List<ArtisticJudgeScore> listScoring = artisticJudgeScoreService
					.findAllByChampionshipParticipantTeamAndParticipantTeamReferees(championshipParticipantTeams,
							aJudgeTeamReferee);

			refereeAJudgeTeamScoreMap.put(aJudgeTeamReferee, listScoring);
		}
		return refereeAJudgeTeamScoreMap;
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

	private boolean checkAllJudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
		if (participantTeam.getAsanaCategory().getId() == 1) { // Traditional single

			boolean eJudgeScoreGivenStatus = checkEjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean tJudgeScoreGivenStatus = checkTjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean dJudgeScoreGivenStatus = checkDjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);

			if (eJudgeScoreGivenStatus && tJudgeScoreGivenStatus && dJudgeScoreGivenStatus) {
				return true;
			} else {
				return false;
			}

		} else { // All other categories

			boolean eJudgeScoreGivenStatus = checkEjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean tJudgeScoreGivenStatus = checkTjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean dJudgeScoreGivenStatus = checkDjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			boolean aJudgeScoreGivenStatus = checkAjudgeScoresGivenForTeam(championshipParticipantTeams,
					championshipRounds);
			if (eJudgeScoreGivenStatus && tJudgeScoreGivenStatus && dJudgeScoreGivenStatus && aJudgeScoreGivenStatus) {
				return true;
			} else {
				return false;
			}

		}
	}

	private boolean checkAjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<ArtisticJudgeScore> listArtisticJudgeScores = artisticJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listArtisticJudgeScores.size() != 0) {
			for (ArtisticJudgeScore artisticJudgeScore : listArtisticJudgeScores) {
				listStatusForRecords.add(artisticJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Ajudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Ajudge");
				return false;
			} else {
		       LOGGER.info("Returning true - Ajudge");
				return true;
			}
		}
	}

	private boolean checkDjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<TraditionalJudgeScore> listTraditionalJudgeScores = traditionalJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listTraditionalJudgeScores.size() != 0) {
			for (TraditionalJudgeScore traditionalJudgeScore : listTraditionalJudgeScores) {
				listStatusForRecords.add(traditionalJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Djudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Djudge");
				return false;
			} else {
				LOGGER.info("Returning true - Djudge");
				return true;
			}
		}
	}

	private boolean checkTjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<TimeKeeperJudgeScore> listTimeKeeperJudgeScores = timeKeeperJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listTimeKeeperJudgeScores.size() != 0) {
			for (TimeKeeperJudgeScore timeKeeperJudgeScore : listTimeKeeperJudgeScores) {
				listStatusForRecords.add(timeKeeperJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Tjudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Tjudge");
				return false;
			} else {
				LOGGER.info("Returning true - Tjudge");
				return true;
			}
		}
	}

	private boolean checkEjudgeScoresGivenForTeam(ChampionshipParticipantTeams championshipParticipantTeams,
			ChampionshipRounds championshipRounds) {
		Integer round = championshipRounds.getRound();
		List<EvaluatorJudgeScore> listEvaluatorJudgeScores = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRound(championshipParticipantTeams, round);
		List<Boolean> listStatusForRecords = new ArrayList<Boolean>();
		if (listEvaluatorJudgeScores.size() != 0) {
			for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScores) {
				listStatusForRecords.add(evaluatorJudgeScore.getStatus());
			}
		}
		LOGGER.info("listStatusForRecords Ejudge : " + Arrays.toString(listStatusForRecords.toArray()));
		if (listStatusForRecords.size() == 0) {
			LOGGER.info("Returning false - Ejudge");
			return false;
		} else {
			if (listStatusForRecords.contains(false) || listStatusForRecords.contains(null)) {
				LOGGER.info("Returning false - Ejudge");
				return false;
			} else {
				LOGGER.info("Returning true - Ejudge");
				return true;
			}
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
		LOGGER.info("Exit getTeamAndCalculateScores method RefereeCalculateTeamScoresController");

		return listTimeKeeperJudgeCommonQuestionScores;
	}

}
