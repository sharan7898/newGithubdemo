package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO;
import com.swayaan.nysf.entity.DTO.ChampionshipParticipantTeamsDTO;
import com.swayaan.nysf.entity.DTO.ParticipantTeamStatusDTO;
import com.swayaan.nysf.entity.DTO.TimekeeperParticipantTeamDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamParticipantAsanasStatusService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/referee")
public class RefereeTeamMonitoringController {
	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int TRADITIONAL_ASANA_COUNT = 7;
	private static final int ARTISTIC_SINGLE_ASANA_COUNT = 10;
	private static final int ARTISTIC_PAIR_ASANA_COUNT = 20;
	private static final int RHYTHMIC_PAIR_ASANA_COUNT = 20;
	private static final int ARTISTIC_GROUP_ASANA_COUNT = 50;

	private static final int CHIEF_JUDGE_TYPE_ID = 1;
	private static final int D_JUDGE_TYPE_ID = 2;
	private static final int A_JUDGE_TYPE_ID = 3;
	private static final int T_JUDGE_TYPE_ID = 4;
	private static final int E_JUDGE_TYPE_ID = 5;
	private static final int SCORER_TYPE_ID = 6;

	private static final int TRAD_CHIEF_JUDGE_COUNT = 1;
	private static final int TRAD_D_JUDGE_COUNT = 5;
	private static final int TRAD_A_JUDGE_COUNT = 0;
	private static final int TRAD_T_JUDGE_COUNT = 1;
	private static final int TRAD_E_JUDGE_COUNT = 1;
	private static final int TRAD_SCORER_JUDGE_COUNT = 1;
	
	private static final int ARTISTIC_CHIEF_JUDGE_COUNT = 1;
	private static final int ARTISTIC_D_JUDGE_COUNT = 4;
	private static final int ARTISTIC_A_JUDGE_COUNT = 2;
	private static final int ARTISTIC_T_JUDGE_COUNT = 2;
	private static final int ARTISTIC_E_JUDGE_COUNT = 1;
	private static final int ARTISTIC_SCORER_JUDGE_COUNT = 1;
	
	private static final int APAIR_CHIEF_JUDGE_COUNT = 1;
	private static final int APAIR_D_JUDGE_COUNT = 4;
	private static final int APAIR_A_JUDGE_COUNT = 2;
	private static final int APAIR_T_JUDGE_COUNT = 2;
	private static final int APAIR_E_JUDGE_COUNT = 2;
	private static final int APAIR_SCORER_JUDGE_COUNT = 1;

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
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	RefereesPanelsService refereesPanelsService;
	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;
	@Autowired
	CompulsoryRoundAsanasService compulsoryRoundAsanasService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RefereeTeamMonitoringController.class);

//	@PreAuthorize("hasAuthority('Judge')")
//	@GetMapping("/manage-championship")
//	public String getRefereeHomePage(Model model,HttpServletRequest request) throws JudgeNotFoundException {
//		LOGGER.info("Entered getRefereeHomePage");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
//		model.addAttribute("listChampionships", listChampionships);
//		model.addAttribute("pageTitle", "Manage Championship");
//		return "referee/team_monitoring";
//	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship")
	public String getRefereeHomePage(Model model, HttpServletRequest request) throws JudgeNotFoundException {
		LOGGER.info("Entered getRefereeHomePage method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit getRefereeHomePage method -RefereeTeamMonitoringController");

		return listAllChampionshipsByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/page/{pageNum}")
	public String listAllChampionshipsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location, HttpServletRequest request)
			throws JudgeNotFoundException {
		LOGGER.info("Entered listAllChampionshipsByPage method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<Championship> listChampionship = new ArrayList<>();
		Page<Championship> page = championshipService.listByJudgePage(pageNum, sortField, sortDir, name, location,
				currentUser.getId());
		listChampionship = page.getContent();
		System.out.println(listChampionship.toString());
		List<Championship> listChampionships = championshipService
				.listAllChampionshipsByStatusCompletedAndByCurrentUser(currentUser);
		model.addAttribute("listChampionships", listChampionships);

		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/referee/manage-championship");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", location);
		model.addAttribute("listChampionships", listChampionship);

		model.addAttribute("pageTitle", "Manage Championship");
		LOGGER.info("Exit listAllChampionshipsByPage method -RefereeTeamMonitoringController");

		return "referee/team_monitoring";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/championship/{championshipId}/getChampionshipCategories")
	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			HttpServletRequest request) throws JudgeNotFoundException {
		LOGGER.info("Entered listChampionshipCategories method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship;
		try {
			championship = championshipService.getChampionshipById(championshipId);
		} catch (ChampionshipNotFoundException e) {
			model.addAttribute("errorMessage", "Championship Not Found.");
			return "referee/manage-championship";
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
		LOGGER.info("Exit listChampionshipCategories method -RefereeTeamMonitoringController");

		return "referee/championship_category_form";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/{championshipId}/category/{asanaCategoryId}/{ageCategoryId}/{gender}/{round}")
	public String proceedToNextRound(@PathVariable(name = "championshipId") Integer id, HttpServletRequest request,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "gender") String gender, @PathVariable(name = "ageCategoryId") Integer ageCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered proceedToNextRound method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = null;
		try {
			championship = championshipService.get(id);
		} catch (ChampionshipNotFoundException e1) {
			LOGGER.info("Championship not found" + e1.getMessage());
			return "redirect:/referee/manage-championship";
		}
		model.addAttribute("pageTitle", "Manage Championship");
		// Check if he is eligible to move the teams
		User user = CommonUtil.getCurrentUser();
		Judge judgeUser = CommonUtil.getCurrentJudge();
		ChampionshipCategory championshipCategory = championshipCategoryService
				.getChampionshipCategoryForAllConditions(championship.getId(), asanaCategoryId, ageCategoryId, gender);
		Integer championshipCategoryId = championshipCategory.getId();
		List<ParticipantTeamReferees> listTimekeeperRefereeTeams = participantTeamRefereesService
				.getByRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(round, championship, championshipCategory,
						user);
		if (listTimekeeperRefereeTeams.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"You are not a member of the Panel. You are not eligible to view teams.");
			return "redirect:/referee/championship/" + id + "/getChampionshipCategories";
		} else {
			LOGGER.info("Exit proceedToNextRound method -RefereeTeamMonitoringController");

			return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/scheduled/{championshipCategoryId}/{round}")
	public String listFirstPageScheduledTeams(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, Model model, HttpServletRequest request, RedirectAttributes ra)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listFirstPageScheduledTeams method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageScheduledTeams method -RefereeTeamMonitoringController");

		return listScheduledTeamsByPage(championshipCategoryId, round, 1, model, "chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/scheduled/{championshipCategoryId}/{round}/page/{pageNum}")
	public String listScheduledTeamsByPage(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listScheduledTeamsByPage method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<StatusEnum> status = new ArrayList<>();
		status.add(StatusEnum.SCHEDULED);

		Judge judgeUser = CommonUtil.getCurrentJudge();

		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		Championship championship = championshipService.getChampionshipByChampionshipCategory(championshipCategoryId);
		List<TimekeeperParticipantTeamDTO> listParticipantTeams = new ArrayList<TimekeeperParticipantTeamDTO>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
		// get championshipRoundsId from championshipCategoryId and round
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipCategoryAndRound(championshipCategory, round);
		Page<ChampionshipParticipantTeams> page;
		if (championshipRounds != null) {
			page = championshipParticipantTeamsService.listByStatusAndPage(pageNum, sortField, sortDir, chestNumber,
					championshipRounds, status);
			listChampionshipParticipantTeams = page.getContent();

//			listChampionshipParticipantTeams = championshipParticipantTeamsService
//					.getTeamsByChampionshipRoundsAndStatus(championshipRounds, status);

			for (ChampionshipParticipantTeams cpTeams : listChampionshipParticipantTeams) {
				ParticipantTeam participantTeam = cpTeams.getParticipantTeam();

				ParticipantTeamParticipantAsanasStatus team = participantTeamParticipantAsanasStatusService
						.findByParticipantTeamAndRound(participantTeam, round);
				String asanaStatus = "UNASSIGNED";
				List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
						.getByTeamAndRound(participantTeam, round);
				List<ParticipantTeamAsanas> ListNoSequenceNumber = listParticipantTeamAsanas.stream()
						.filter(teamAsanas -> teamAsanas.getSequenceNumber() == null).collect(Collectors.toList());

				// List<CompulsoryRoundAsanas> listCompulsoryAsanas =
				// compulsoryRoundAsanasService.getCompulsoryAsanas(participantTeam, round);
				List<ParticipantTeamAsanas> listCompParticipantTeamAsanas = participantTeamAsanasService
						.getCompulsoryAsanasByTeamAndRound(participantTeam, round);
				Integer maxAsanaCount = 0;

				if (team == null) {
					asanaStatus = "UNASSIGNED";
				} else if (team.getAsanaStatus().equals(AsanaStatusEnum.FREEZE)) {
					asanaStatus = "FREEZE";

					if (participantTeam.getAsanaCategory().getId() == TRADITIONAL_ASANA_CATEGORY_ID) {
						if (listCompParticipantTeamAsanas.size() + (listParticipantTeamAsanas.size()
								- listCompParticipantTeamAsanas.size()) > TRADITIONAL_ASANA_COUNT) {
							asanaStatus = "IsCompulsory";
						}
					}
					if (participantTeam.getAsanaCategory().getId() == TRADITIONAL_ASANA_CATEGORY_ID) {
						maxAsanaCount = TRADITIONAL_ASANA_COUNT;
					} else if (participantTeam.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
						maxAsanaCount = ARTISTIC_SINGLE_ASANA_COUNT;

					} else if (participantTeam.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
						maxAsanaCount = ARTISTIC_PAIR_ASANA_COUNT;

					} else if (participantTeam.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
						maxAsanaCount = RHYTHMIC_PAIR_ASANA_COUNT;

					} else if (participantTeam.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
						maxAsanaCount = ARTISTIC_GROUP_ASANA_COUNT;

					}
					if (listParticipantTeamAsanas.size() != maxAsanaCount) {
						if (participantTeam.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
							asanaStatus = "UNASSIGNED_TOTAL_ASANA_FOR_PAIR";
						}else if (participantTeam.getAsanaCategory().getId() ==TRADITIONAL_ASANA_CATEGORY_ID) {
							asanaStatus = "IsCompulsory";
						} else {
							asanaStatus = "UNASSIGNED_TOTAL_ASANA";

						}

					}
					if (ListNoSequenceNumber.size() > 0) {
						asanaStatus = "UNASSIGNED_SEQUENCE_NUM";
					}
					if (ListNoSequenceNumber.size() == 0) {
						Boolean isValid = listParticipantTeamAsanas.stream().allMatch(i -> i.getSequenceNumber() >= 1
								&& i.getSequenceNumber() <= listParticipantTeamAsanas.size());

						if (!isValid) {
							asanaStatus = "UNASSIGNED_SEQUENCE_NUM";
						}

					}

				} else if (team.getAsanaStatus().equals(AsanaStatusEnum.INCOMPLETE)) {
					asanaStatus = "UNASSIGNED";
				} else {
					asanaStatus = "NOTFROZEN";
				}
				
				List<ParticipantTeamReferees> listParticipantTeamReferees = participantTeamRefereesService.
						getTeamReferresForChampionshipAndChampionshipCategoryAndParticipantTeamAndRoundNumber(championship, championshipCategory, participantTeam, round);
				
				if(!listParticipantTeamReferees.isEmpty()) {
					Integer ChiefJudgeCount=0;
					Integer DJudgeCount=0;
					Integer AJudgeCount=0;
					Integer TJudgeCount=0;
					Integer EJudgeCount=0;
					Integer ScorerJudgeCount=0;
					for(ParticipantTeamReferees partTeamReferee : listParticipantTeamReferees) {
							if(partTeamReferee.getType().getId()== CHIEF_JUDGE_TYPE_ID) {
								ChiefJudgeCount++;
							} 
							if(partTeamReferee.getType().getId()== D_JUDGE_TYPE_ID) {
								DJudgeCount++;
							}
							if(partTeamReferee.getType().getId()== A_JUDGE_TYPE_ID) {
								AJudgeCount++;
							}
							if(partTeamReferee.getType().getId()== T_JUDGE_TYPE_ID) {
								TJudgeCount++;
							}
							if(partTeamReferee.getType().getId()== E_JUDGE_TYPE_ID) {
								EJudgeCount++;
							}
							if(partTeamReferee.getType().getId()== SCORER_TYPE_ID) {
								ScorerJudgeCount++;
							}
						
							
					}
				//	System.out.println(ChiefJudgeCount +"-"+ DJudgeCount+ "-"+ AJudgeCount+ "-"+ TJudgeCount+ "-"+ EJudgeCount+ "-"+ ScorerJudgeCount);
					if(participantTeam.getAsanaCategory().getId() == TRADITIONAL_ASANA_CATEGORY_ID) {
						if(!(ChiefJudgeCount == TRAD_CHIEF_JUDGE_COUNT && DJudgeCount == TRAD_D_JUDGE_COUNT && AJudgeCount == TRAD_A_JUDGE_COUNT
								&& TJudgeCount == TRAD_T_JUDGE_COUNT && EJudgeCount == TRAD_E_JUDGE_COUNT && ScorerJudgeCount == TRAD_SCORER_JUDGE_COUNT)) {
							asanaStatus = "UNASSIGNED_REFEREES";
						}
					}else if(participantTeam.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
						if(!(ChiefJudgeCount == APAIR_CHIEF_JUDGE_COUNT && DJudgeCount == APAIR_D_JUDGE_COUNT && AJudgeCount == APAIR_A_JUDGE_COUNT
								&& TJudgeCount == APAIR_T_JUDGE_COUNT && EJudgeCount == APAIR_E_JUDGE_COUNT && ScorerJudgeCount == APAIR_SCORER_JUDGE_COUNT)) {
							asanaStatus = "UNASSIGNED_REFEREES";
						}
					}else if(participantTeam.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID || participantTeam.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID ||
							participantTeam.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
						if(!(ChiefJudgeCount == ARTISTIC_CHIEF_JUDGE_COUNT && DJudgeCount == ARTISTIC_D_JUDGE_COUNT && AJudgeCount == ARTISTIC_A_JUDGE_COUNT
								&& TJudgeCount == ARTISTIC_T_JUDGE_COUNT && EJudgeCount == ARTISTIC_E_JUDGE_COUNT && ScorerJudgeCount == ARTISTIC_SCORER_JUDGE_COUNT)) {
							asanaStatus = "UNASSIGNED_REFEREES";
						}
					}
				}
					
				
				ParticipantTeamReferees participantTeamReferee = participantTeamRefereesService
						.getByParticipantTeamAndRoundAndChampionshipAndChampionshipCategoryAndJudgeUser(participantTeam,
								round, championshipRounds.getChampionship(), championshipCategory, judgeUser);

				Boolean isTeamJudge = false;
				if (participantTeamReferee != null) {
					isTeamJudge = true;
				}
				
				
				listParticipantTeams.add(new TimekeeperParticipantTeamDTO(cpTeams, isTeamJudge, asanaStatus));
			}
				

		} else {
			LOGGER.error("Championship Round is null");
			ra.addFlashAttribute("errorMessage", "No Scheduled Teams");
			return "redirect:/referee/championship/" + championship.getId() + "/getChampionshipCategories";
		}

//		Collections.sort(listParticipantTeams, new Comparator<TimekeeperParticipantTeamDTO>() {
//			@Override
//			public int compare(TimekeeperParticipantTeamDTO t1, TimekeeperParticipantTeamDTO t2) {
//				return t1.getChampionshipParticipantTeams().getParticipantTeam().getChestNumber()
//						.compareTo(t2.getChampionshipParticipantTeams().getParticipantTeam().getChestNumber());
//
//			}
//		});

		long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("moduleURL",
				"/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", chestNumber);

		model.addAttribute("listParticipantTeams", listParticipantTeams);
		model.addAttribute("championshipCategoryId", championshipCategoryId);
		model.addAttribute("championshipRoundsId", championshipRounds.getId());
		model.addAttribute("round", round);
		model.addAttribute("championship", championship.getId());
		model.addAttribute("championshipId", championshipRounds.getChampionship().getId());
		model.addAttribute("pageTitle", "Schedule Teams - " + championship.getName());
		LOGGER.info("Exit listScheduledTeamsByPage method -RefereeTeamMonitoringController");

		return "referee/schedule_teams";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/manage-championship/movetoongoing/{championshipRoundsId}/{participantTeamId}")
	public String moveToOngoing(@PathVariable(name = "championshipRoundsId") Integer championshipRoundsId,
			@PathVariable(name = "participantTeamId") Integer participantTeamId,
			@RequestParam(name = "championshipCategoryId") Integer championshipCategoryId,
			@RequestParam(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws JudgeNotFoundException, ChampionshipNotFoundException {

		LOGGER.info("Entered moveToOngoing method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		StatusEnum status = StatusEnum.ONGOING;
		RoundStatusEnum roundStatus = RoundStatusEnum.ONGOING;
		Judge judgeUser = CommonUtil.getCurrentJudge();
		ParticipantTeam participantTeam;
		try {
			participantTeam = participantTeamService.get(participantTeamId);
			Championship championship = championshipService.get(participantTeam.getChampionship().getId());
			championship.setStatus(ChampionshipStatusEnum.ONGOING);
			championshipService.updateStatus(championship);
			ParticipantTeamReferees participantTeamReferees = participantTeamRefereesService
					.getTeamReferresForParticipantTeamAndJudgeUserAndRound(participantTeam, judgeUser, round);

			if (participantTeamReferees != null) {
				championshipRoundsService.updateStatus(championshipRoundsId, roundStatus);
				championshipParticipantTeamsService.updateParticipantTeamStatus(championshipRoundsId, participantTeamId,
						status.toString());
				return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;

			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to update the team status.");
				return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;
			}

		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Exit moveToOngoing method -RefereeTeamMonitoringController");

			return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/ongoing/{championshipCategoryId}/{round}")
	public String listFirstPageOngoingTeams(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listFirstPageOngoingTeams method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageOngoingTeams method -RefereeTeamMonitoringController");

		return listOngoingTeamsByPage(championshipCategoryId, round, 1, model, "chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/ongoing/{championshipCategoryId}/{round}/page/{pageNum}")
	public String listOngoingTeamsByPage(@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) throws EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered listOngoingTeamsByPage method -RefereeTeamMonitoringController");
		List<StatusEnum> listStatus = new ArrayList<>();
		listStatus.add(StatusEnum.ONGOING);
		listStatus.add(StatusEnum.PERFORMING);

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		Championship championship = championshipService.getChampionshipByChampionshipCategory(championshipCategoryId);
		List<ParticipantTeam> listParticipantTeams = new ArrayList<ParticipantTeam>();

		List<ChampionshipParticipantTeamsDTO> listChampionshipParticipantTeamsDTO = new ArrayList<ChampionshipParticipantTeamsDTO>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
		// get championshipRoundsId from championshipCategoryId and round
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipCategoryAndRound(championshipCategory, round);
		Page<ChampionshipParticipantTeams> page;
		if (championshipRounds != null) {
			page = championshipParticipantTeamsService.listByStatusAndPage(pageNum, sortField, sortDir, chestNumber,
					championshipRounds, listStatus);
			listChampionshipParticipantTeams = page.getContent();
			LOGGER.debug("listChampionshipParticipantTeams size : " + listChampionshipParticipantTeams.size());
			/*
			 * for(ChampionshipParticipantTeams cpTeams : listChampionshipParticipantTeams)
			 * { listParticipantTeams.add(cpTeams.getParticipantTeam()); }
			 */

			for (ChampionshipParticipantTeams cpTeams : listChampionshipParticipantTeams) {

				listChampionshipParticipantTeamsDTO.add(new ChampionshipParticipantTeamsDTO(cpTeams.getId(),
						cpTeams.getStatus().toString(), cpTeams.getChampionshipRounds().getId(),
						cpTeams.getChampionshipRounds().getChampionship().getName(),
						cpTeams.getParticipantTeam().getId(), cpTeams.getParticipantTeam().getName(),
						cpTeams.getParticipantTeam().getAsanaCategory(), cpTeams.getParticipantTeam().getChestNumber(),
						cpTeams.getParticipantTeam().getGender(), cpTeams.getParticipantTeam().getCategory()));
			}
		} else {
			LOGGER.error("Championship Round is null");
			ra.addFlashAttribute("errorMessage", "Unable to fetch the Teams");
			return "redirect:/referee/championship/" + championship.getId() + "/getChampionshipCategories";
		}

//		Collections.sort(listChampionshipParticipantTeamsDTO, new Comparator<ChampionshipParticipantTeamsDTO>() {
//			@Override
//			public int compare(ChampionshipParticipantTeamsDTO t1, ChampionshipParticipantTeamsDTO t2) {
//				return t1.getChestNumber().compareTo(t2.getChestNumber());
//			}
//		});

		long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", chestNumber);

		model.addAttribute("listChampionshipParticipantTeamsDTO", listChampionshipParticipantTeamsDTO);
		// model.addAttribute("listParticipantTeams", listParticipantTeams);
		model.addAttribute("championshipCategoryId", championshipCategoryId);
		model.addAttribute("championshipRoundsId", championshipRounds.getId());
		model.addAttribute("round", round);

		model.addAttribute("pageTitle", "Ongoing Teams");
		LOGGER.info("Exit listOngoingTeamsByPage method -RefereeTeamMonitoringController");

		return "referee/ongoing_teams";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/manage-championship/movetoperforming/{championshipRoundsId}/{chamParticipantTeamId}")
	public String moveToPerforming(@PathVariable(name = "championshipRoundsId") Integer championshipRoundsId,
			@PathVariable(name = "chamParticipantTeamId") Integer chamParticipantTeamId,
			@RequestParam(name = "championshipCategoryId") Integer championshipCategoryId,
			@RequestParam(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered moveToPerforming method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		StatusEnum status = StatusEnum.PERFORMING;

		// RoundStatusEnum roundStatus=RoundStatusEnum.ONGOING;
		// championshipRoundsService.updateStatus(championshipRoundsId,roundStatus);
		Judge judgeUser = CommonUtil.getCurrentJudge();

		ParticipantTeam participantTeam;
		ChampionshipParticipantTeams championshipParticipantTeams;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(chamParticipantTeamId);
			participantTeam = championshipParticipantTeams.getParticipantTeam();

			ParticipantTeamReferees participantTeamReferees = participantTeamRefereesService
					.getTeamReferresForParticipantTeamAndJudgeUserAndRound(participantTeam, judgeUser, round);

			if (participantTeamReferees != null) {
				championshipParticipantTeamsService.updateParticipantTeamStatus(chamParticipantTeamId,
						status.toString());
				return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;

			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to update the team status.");
				return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;
			}

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Exit moveToPerforming method -RefereeTeamMonitoringController");

			return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/manage-championship/movetoperformed/{championshipRoundsId}/{chamParticipantTeamId}")
	public String moveToPerformed(@PathVariable(name = "championshipRoundsId") Integer championshipRoundsId,
			@PathVariable(name = "chamParticipantTeamId") Integer chamParticipantTeamId,
			@RequestParam(name = "championshipCategoryId1") Integer championshipCategoryId,
			@RequestParam(name = "round1") Integer round, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws JudgeNotFoundException {

		LOGGER.info("Entered moveToPerformed method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		StatusEnum status = StatusEnum.PERFORMED;

		LOGGER.info("championshipRoundsId : " + championshipRoundsId);
		LOGGER.info("chamParticipantTeamId : " + chamParticipantTeamId);
		LOGGER.info("championshipCategoryId : " + championshipCategoryId);
		LOGGER.info("round : " + round);
		User user = CommonUtil.getCurrentUser();
		Judge judgeUser = CommonUtil.getCurrentJudge();

		ParticipantTeam participantTeam;
		ChampionshipParticipantTeams championshipParticipantTeams;
		ParticipantTeamReferees participantTeamReferees = null;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(chamParticipantTeamId);
			if (championshipParticipantTeams.getStatus() != StatusEnum.COMPLETED) {
				participantTeam = championshipParticipantTeams.getParticipantTeam();

				participantTeamReferees = participantTeamRefereesService
						.getTeamReferresForParticipantTeamAndJudgeUserAndRound(participantTeam, judgeUser, round);
			}

			if (participantTeamReferees != null) {
				championshipParticipantTeamsService.updateParticipantTeamStatus(chamParticipantTeamId, status.name());
				return "redirect:/referee/manage-championship/performed/" + championshipCategoryId + "/" + round;

			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to update the team status.");
				return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;
			}

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Exit moveToPerformed method -RefereeTeamMonitoringController");

			return "redirect:/referee/manage-championship/ongoing/" + championshipCategoryId + "/" + round;
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/performed/{championshipCategoryId}/{round}")
	public String listFirstPagePerformedTeams(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listFirstPagePerformedTeams method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPagePerformedTeams method -RefereeTeamMonitoringController");

		return listPerformedTeamsByPage(championshipCategoryId, round, 1, model, "chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/performed/{championshipCategoryId}/{round}/page/{pageNum}")
	public String listPerformedTeamsByPage(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) throws EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered listPerformedTeamsByPage method -RefereeTeamMonitoringController");
		List<StatusEnum> status = new ArrayList<>();
		status.add(StatusEnum.PERFORMED);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		Championship championship = championshipService.getChampionshipByChampionshipCategory(championshipCategoryId);
		List<ParticipantTeam> listParticipantTeams = new ArrayList<ParticipantTeam>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
		// get championshipRoundsId from championshipCategoryId and round
		Page<ChampionshipParticipantTeams> page = null;
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipCategoryAndRound(championshipCategory, round);
		if (championshipRounds != null) {
			page = championshipParticipantTeamsService.listByStatusAndPage(pageNum, sortField, sortDir, chestNumber,
					championshipRounds, status);
			listChampionshipParticipantTeams = page.getContent();
//			listChampionshipParticipantTeams = championshipParticipantTeamsService
//					.getTeamsByChampionshipRoundsAndStatus(championshipRounds, status);
			LOGGER.debug("listChampionshipParticipantTeams size : " + listChampionshipParticipantTeams.size());
			for (ChampionshipParticipantTeams cpTeams : listChampionshipParticipantTeams) {
				listParticipantTeams.add(cpTeams.getParticipantTeam());
			}
		} else {
			LOGGER.error("Championship Round is null");
			ra.addFlashAttribute("errorMessage", "Unable to fetch the Teams");
			return "redirect:/referee/championship/" + championship.getId() + "/getChampionshipCategories";
		}
//		Collections.sort(listChampionshipParticipantTeams, new Comparator<ChampionshipParticipantTeams>() {
//			@Override
//			public int compare(ChampionshipParticipantTeams t1, ChampionshipParticipantTeams t2) {
//				return t1.getParticipantTeam().getChestNumber().compareTo(t2.getParticipantTeam().getChestNumber());
//			}
//		});
		long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL",
				"/referee/manage-championship/performed/" + championshipCategoryId + "/" + round);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", chestNumber);

		model.addAttribute("listParticipantTeams", listChampionshipParticipantTeams);
		model.addAttribute("championshipCategoryId", championshipCategoryId);
		model.addAttribute("championshipRoundsId", championshipRounds.getId());
		model.addAttribute("round", round);
		model.addAttribute("currentUser", currentUser);

		model.addAttribute("pageTitle", "Performed Teams");
		LOGGER.info("Exit listPerformedTeamsByPage method -RefereeTeamMonitoringController");

		return "referee/performed_teams";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/manage-championship/movetocompleted/{championshipRoundsId}/{participantTeamId}")
	public String moveToCompleted(@PathVariable(name = "championshipRoundsId") Integer championshipRoundsId,
			@PathVariable(name = "participantTeamId") Integer participantTeamId,
			@RequestParam(name = "championshipCategoryId") Integer championshipCategoryId,
			@RequestParam(name = "round") Integer round, Model model, HttpServletRequest request)
			throws JudgeNotFoundException {

		LOGGER.info("Entered moveToCompleted method -RefereeTeamMonitoringController");

		StatusEnum status = StatusEnum.COMPLETED;
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		// Add all computations to check if all judges have given scores to the team
		championshipParticipantTeamsService.updateParticipantTeamStatus(championshipRoundsId, participantTeamId,
				status.toString());
		LOGGER.info("Exit moveToCompleted method -RefereeTeamMonitoringController");

		return "redirect:/referee/manage-championship/completed/" + championshipCategoryId + "/" + round;

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/completed/{championshipCategoryId}/{round}")
	public String listFirstPageCompletedTeams(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listFirstPageCompletedTeams method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageCompletedTeams method -RefereeTeamMonitoringController");

		return listCompletedTeamsByPage(championshipCategoryId, round, 1, model, "chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/completed/{championshipCategoryId}/{round}/page/{pageNum}")
	public String listCompletedTeamsByPage(
			@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) throws EntityNotFoundException, JudgeNotFoundException {

		LOGGER.info("Entered listCompletedTeamsByPage method -RefereeTeamMonitoringController");
		List<StatusEnum> status = new ArrayList<>();
		status.add(StatusEnum.COMPLETED);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		Championship championship = championshipService.getChampionshipByChampionshipCategory(championshipCategoryId);
		List<ParticipantTeam> listParticipantTeams = new ArrayList<ParticipantTeam>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
		// get championshipRoundsId from championshipCategoryId and round

		Page<ChampionshipParticipantTeams> page = null;
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipCategoryAndRound(championshipCategory, round);
		if (championshipRounds != null) {
			page = championshipParticipantTeamsService.listByStatusAndPage(pageNum, sortField, sortDir, chestNumber,
					championshipRounds, status);
			listChampionshipParticipantTeams = page.getContent();

//			listChampionshipParticipantTeams = championshipParticipantTeamsService
//					.getTeamsByChampionshipRoundsAndStatus(championshipRounds, status);
			LOGGER.debug("listChampionshipParticipantTeams size : " + listChampionshipParticipantTeams.size());
			for (ChampionshipParticipantTeams cpTeams : listChampionshipParticipantTeams) {
				listParticipantTeams.add(cpTeams.getParticipantTeam());
			}
		} else {
			LOGGER.error("Championship Round is null");
			ra.addFlashAttribute("errorMessage", "Unable to fetch the Teams");
			return "redirect:/referee/championship/" + championship.getId() + "/getChampionshipCategories";
		}

//		Collections.sort(listParticipantTeams, new Comparator<ParticipantTeam>() {
//			@Override
//			public int compare(ParticipantTeam t1, ParticipantTeam t2) {
//				return t1.getChestNumber().compareTo(t2.getChestNumber());
//			}
//		});
		long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL",
				"/referee/manage-championship/completed/" + championshipCategoryId + "/" + round);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", chestNumber);

		model.addAttribute("listParticipantTeams", listParticipantTeams);
		model.addAttribute("championshipCategoryId", championshipCategoryId);
		model.addAttribute("championshipRoundsId", championshipRounds.getId());
		model.addAttribute("round", round);
		model.addAttribute("pageTitle", "Completed Teams");
		LOGGER.info("Exit listCompletedTeamsByPage method -RefereeTeamMonitoringController");

		return "referee/completed_teams";
	}

	@PreAuthorize("hasAuthority('Judge')")
	@PostMapping("/championshipParticipantTeam/{championshipCategoryId}/{round}/executionTime")
	public String saveExecutionTime(@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, @RequestParam(name = "id") Integer id,
			@RequestParam(name = "executionTime") String executionTime, Model model, HttpServletRequest request)
			throws JudgeNotFoundException {
		LOGGER.info("Entered saveExecutionTime method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Judge judgeUser = CommonUtil.getCurrentJudge();

		try {
			ChampionshipParticipantTeams championshipParticipantTeam = championshipParticipantTeamsService.get(id);
			championshipParticipantTeam.setJudgeUser(judgeUser);
			championshipParticipantTeam.setExecutionTime(executionTime);
			championshipParticipantTeamsService.save(championshipParticipantTeam);
		} catch (EntityNotFoundException e) {
			LOGGER.info("ChampionshipParticipantTeams not found exception" + e.getMessage());
		}
		LOGGER.info("Exit saveExecutionTime method -RefereeTeamMonitoringController");

		return "redirect:/referee/manage-championship/performed/" + championshipCategoryId + "/" + round;

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/absent/{championshipRoundsId}/{participantTeamId}/{championshipParticipantTeamId}")
	public String moveToAbsent(@PathVariable(name = "championshipRoundsId") Integer championshipRoundsId,
			@PathVariable(name = "participantTeamId") Integer participantTeamId,
			@PathVariable(name = "championshipParticipantTeamId") Integer championshipParticipantTeamId, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered moveToAbsent method -RefereeTeamMonitoringController");

		StatusEnum status = StatusEnum.ABSENT;
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Judge judgeUser = CommonUtil.getCurrentJudge();
		ChampionshipRounds championshipRound = championshipRoundsService.get(championshipRoundsId);
		Integer championshipCategoryId = championshipRound.getChampionshipCategory().getId();
		Integer round = championshipRound.getRound();
		ParticipantTeam participantTeam;
		try {
			participantTeam = participantTeamService.get(participantTeamId);
			ParticipantTeamReferees participantTeamReferees = participantTeamRefereesService
					.getTeamReferresForParticipantTeamAndJudgeUserAndRound(participantTeam, judgeUser, round);
			System.out
					.println("This is for testing" + participantTeamReferees + "round" + round + "users" + currentUser);
			if (participantTeamReferees != null) {
				model.addAttribute("judgeType", participantTeamReferees.getType().getType());

				championshipParticipantTeamsService.updateParticipantTeamStatus(championshipParticipantTeamId,
						status.name());
				return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;

			} else {

				redirectAttributes.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to update the team status.");
				return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;
			}

		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Exit moveToAbsent method -RefereeTeamMonitoringController");

			return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/rejected/{championshipRoundsId}/{participantTeamId}/{championshipParticipantTeamId}")
	public String moveToReject(@PathVariable(name = "championshipRoundsId") Integer championshipRoundsId,
			@PathVariable(name = "participantTeamId") Integer participantTeamId,
			@PathVariable(name = "championshipParticipantTeamId") Integer championshipParticipantTeamId, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered moveToReject method -RefereeTeamMonitoringController");

		StatusEnum status = StatusEnum.REJECTED;
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Judge judgeUser = CommonUtil.getCurrentJudge();
		ChampionshipRounds championshipRound = championshipRoundsService.get(championshipRoundsId);
		Integer championshipCategoryId = championshipRound.getChampionshipCategory().getId();
		Integer round = championshipRound.getRound();

		ParticipantTeam participantTeam;
		try {

			participantTeam = participantTeamService.get(participantTeamId);
			ParticipantTeamReferees participantTeamReferees = participantTeamRefereesService
					.getTeamReferresForParticipantTeamAndJudgeUserAndRound(participantTeam, judgeUser, round);

			if (participantTeamReferees != null) {
				model.addAttribute("judgeType", participantTeamReferees.getType().getType());

				championshipParticipantTeamsService.updateParticipantTeamStatus(championshipParticipantTeamId,
						status.name());
				return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;

			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"You are not a member of the Panel. You are not eligible to update the team status.");
				return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;
			}

		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("Exit moveToReject method -RefereeTeamMonitoringController");

			return "redirect:/referee/manage-championship/scheduled/" + championshipCategoryId + "/" + round;
		}

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/otherteams/{championshipCategoryId}/{round}")
	public String listFirstPageOtherTeams(@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes ra, HttpServletRequest request)
			throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listFirstPageOtherTeams method -RefereeTeamMonitoringController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageOtherTeams method -RefereeTeamMonitoringController");

		return listOtherTeamsByPage(championshipCategoryId, round, 1, model, "chestNumber", "asc", "", request, ra);

	}

	@PreAuthorize("hasAuthority('Judge')")
	@GetMapping("/manage-championship/otherteams/{championshipCategoryId}/{round}/page/{pageNum}")
	public String listOtherTeamsByPage(@PathVariable(name = "championshipCategoryId") Integer championshipCategoryId,
			@PathVariable(name = "round") Integer round, @PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String chestNumber, HttpServletRequest request,
			RedirectAttributes ra) throws EntityNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered listOtherTeamsByPage method -RefereeTeamMonitoringController");
		List<StatusEnum> listStatus = new ArrayList<>();
		listStatus.add(StatusEnum.ABSENT);
		listStatus.add(StatusEnum.REJECTED);
		listStatus.add(StatusEnum.DISQUALIFIED);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		Championship championship = championshipService.getChampionshipByChampionshipCategory(championshipCategoryId);
		List<ParticipantTeamStatusDTO> listParticipantTeams = new ArrayList<ParticipantTeamStatusDTO>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<ChampionshipParticipantTeams>();
		Page<ChampionshipParticipantTeams> page = null;
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipCategoryAndRound(championshipCategory, round);
		if (championshipRounds != null) {
			page = championshipParticipantTeamsService.listByStatusAndPage(pageNum, sortField, sortDir, chestNumber,
					championshipRounds, listStatus);
			listChampionshipParticipantTeams = page.getContent();
//				listChampionshipParticipantTeams = championshipParticipantTeamsService
//					.getTeamsByChampionshipRoundsAndStatusIn(championshipRounds, listStatus);

		} else {
			LOGGER.error("Championship Round is null");
			ra.addFlashAttribute("errorMessage", "Unable to fetch the Teams");
			return "redirect:/referee/championship/" + championship.getId() + "/getChampionshipCategories";
		}
		for (ChampionshipParticipantTeams cpTeams : listChampionshipParticipantTeams) {
			listParticipantTeams.add(new ParticipantTeamStatusDTO(cpTeams.getParticipantTeam().getName(),
					cpTeams.getStatus().toString(), cpTeams.getChampionshipRounds().getChampionship().getName(),
					cpTeams.getParticipantTeam().getChestNumber()));
		}

		long startCount = (pageNum - 1) * championshipParticipantTeamsService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipParticipantTeamsService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL",
				"/referee/manage-championship/otherteams/" + championshipCategoryId + "/" + round);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", chestNumber);

		model.addAttribute("listParticipantTeams", listParticipantTeams);
		model.addAttribute("championshipCategoryId", championshipCategoryId);
		model.addAttribute("championshipRoundsId", championshipRounds.getId());
		model.addAttribute("round", round);
		LOGGER.info("Exit listOtherTeamsByPage method -RefereeTeamMonitoringController");

		return "referee/reject_and_absent";
	}

}
