package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamPanel;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.ParticipantTeamPanelService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.RefereePanelsService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageRefereeController {

	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	JudgeService judgeService;
	@Autowired
	UserService service;
	@Autowired
	RefereePanelsService refereePanelService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ChampionshipRefereePanelsService championshipRefereePanelsService;
	@Autowired
	RoleService roleService;
	@Autowired
	JudgeTypeService judgeTypeService;
	@Autowired
	ParticipantTeamPanelService participantTeamPanelService;

	@Autowired
	ChampionshipService championshipService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageRefereeController.class);

	@GetMapping("/manage-referee")
	public String listAllReferees(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listAllReferees method -ManageRefereeController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<Judge> listReferees = judgeService.listAllReferees();
		model.addAttribute("listReferees", listReferees);
		model.addAttribute("pageTitle", "Manage Judges");
		LOGGER.info("Exit listAllReferees method -ManageRefereeController");

		return "administration/manage_referee";
	}

	/*
	 * @GetMapping("/manage-referee/new") public String newReferee(Model model) {
	 * 
	 * User user = new User();
	 * 
	 * model.addAttribute("user", user); model.addAttribute("pageTitle",
	 * "Add Refe"); model.addAttribute("listCategories", listCategories); return
	 * "administration/asana_form"; }
	 */

//	@PostMapping("/manage-referee/save")
//	public String saveReferee(Judge judge, RedirectAttributes redirectAttributes) throws IOException {
//		service.save(judge);
//		redirectAttributes.addFlashAttribute("message", "The Referee has been saved successfully.");
//		return "redirect:/admin/manage-referee";
//	}

//	@GetMapping("/manage-referee/edit/{id}")
//	public String editReferee(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes)
//			throws UserNotFoundException {
//		LOGGER.info("Entered editReferee controller");
//		try {
//			User user = service.get(id);
//			// List<AsanaCategory> listCategories =
//			// asanaCategoryService.listAllAsanaCategories();
//			List<Role> listRoles = service.listRoles();
//
//			model.addAttribute("user", user);
//			model.addAttribute("pageTitle", "Edit Judge");
//			model.addAttribute("listRoles", listRoles);
//			LOGGER.info("Redirected to administration/referee_form");
//			return "administration/referee_form";
//		} catch (UserNotFoundException ex) {
//			LOGGER.error("User not found!");
//			redirectAttributes.addFlashAttribute("message", "Referee not found.");
//			return "redirect:/admin/manage-referee";
//		}
//	}

//	@GetMapping("/manage-referee/delete/{id}")
//	public String deleteReferee(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes redirectAttributes)
//			throws UserNotFoundException, ChampionshipRefereePanelsNotFoundException {
//		LOGGER.info("Entered deleteReferee controller");
//		try {
//			User user = service.get(id);
//			Judge judgeUser=judgeService.findById(user.getId());
//			List<RefereesPanels> userRefereesPanel = refereePanelService.findByJudgeUser(judgeUser);
//			List<ParticipantTeamReferees> userParticipantTeamReferee = participantTeamRefereesService
//					.getTeamsAssignedForReferees(judgeUser);
//			System.out.println(userRefereesPanel);
//			if (!userParticipantTeamReferee.isEmpty()) {
//				System.out.println("User participant team judge");
//				redirectAttributes.addFlashAttribute("message1", "The Referee " + user.getFullName()
//						+ " is a Participant Team Judge, Please remove before you delete");
//				return "redirect:/admin/manage-referee";
//			}
//			if (!userRefereesPanel.isEmpty()) {
//				System.out.println("Panel member");
//				redirectAttributes.addFlashAttribute("message1",
//						"The Referee " + user.getFullName() + " is a Panel member, Please remove before you delete");
//				return "redirect:/admin/manage-referee";
//			}
//
//			System.out.println("Trying to delete ");
//			service.delete(id);
//			redirectAttributes.addFlashAttribute("message",
//					"The Referee " + user.getFullName() + " has been deleted successfully");
//
//		} catch (UserNotFoundException ex) {
//			LOGGER.error("User not found!");
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//		} catch (JudgeNotFoundException e) {
//			LOGGER.error("Judge not found!");
//			redirectAttributes.addFlashAttribute("message", e.getMessage());
//		}
//		return "redirect:/admin/manage-referee";
//	}

	@GetMapping("/populate-team-judge-panel")
	public String assignRefereesToTeams(Model model, HttpServletRequest request) {
		LOGGER.info("Entered assignRefereesToTeams method -ManageRefereeController");

//		ChampionshipCategory championshipCategory=ChampionshipCategoryService
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		// List<ParticipantTeam> listParticipantTeam =
		// participantTeamService.listAllParticipantTeams();
		model.addAttribute("pageTitle", "Assign Team Panels");
		// model.addAttribute("listParticipantTeam", listParticipantTeam);

		model.addAttribute("listChampionships", listChampionships);
		LOGGER.info("Exit assignRefereesToTeams method -ManageRefereeController");

		return "administration/team_judge_assign_form";
	}

	@PostMapping("/manage-team/referees/assignToCategory")
	public String saveAssignTeamJudge(@RequestParam(name = "championship") Integer championship,
			@RequestParam(name = "panel") Integer championshipRefereePanelId,
			@RequestParam(name = "championship-category") Integer championshipcategory,
			@RequestParam(name = "round") Integer round,
			@RequestParam(name = "teamList") List<Integer> participantTeam_id, RedirectAttributes redirectAttributes,
			HttpServletRequest request)
			throws IOException, ChampionshipNotFoundException, EntityNotFoundException, UserNotFoundException {
		LOGGER.info("Entered saveAssignTeamJudge method -ManageRefereeController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		// for each team - check if there is panel assigned for that round
		List<ParticipantTeam> listParticipantTeams = new ArrayList<>();
		List<ParticipantTeamPanel> listParticipantTeamPanel = new ArrayList<>();
		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<>();
		for (Integer teamId : participantTeam_id) {
			ParticipantTeam participantTeam = participantTeamService.findById(teamId);
			listParticipantTeams.add(participantTeam);
			ParticipantTeamPanel participantTeamPanel = participantTeamPanelService
					.findByParticipantTeamAndRound(teamId, round);
			if (participantTeamPanel != null) {
				listParticipantTeamPanel.add(participantTeamPanel);
			}
			ChampionshipParticipantTeams championshipParticipantTeam = championshipParticipantTeamsService
					.findByParticipantTeamAndRound(participantTeam, round);
			if(championshipParticipantTeam!=null)
			listChampionshipParticipantTeams.add(championshipParticipantTeam);
		}
		if (!listParticipantTeamPanel.isEmpty()) {
			if (!listChampionshipParticipantTeams.isEmpty()) {
				List<ChampionshipParticipantTeams> listFilteredChampionshipParticipantTeams = listChampionshipParticipantTeams
						.stream().filter(c -> c.getStatus().equals(StatusEnum.SCHEDULED)).collect(Collectors.toList());
				if (listChampionshipParticipantTeams.size() != listFilteredChampionshipParticipantTeams.size()) {
					String message1 = "Unable to assign Judge panels to the teams. Some of the teams are already performing.";
					redirectAttributes.addFlashAttribute("message1", message1);
					return "redirect:/admin/populate-team-judge-panel";
				}
			}

		}

		try {
			ChampionshipRefereePanels championshipRefereePanels = championshipRefereePanelsService
					.findById(championshipRefereePanelId);
			List<RefereesPanels> listRefereesPanels = championshipRefereePanels.getRefereesPanels();
			String message = null;
			LOGGER.info("participantTeam_id" + participantTeam_id.size());
			ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipcategory);
			Championship championshipid = championshipService.get(championship);
			for (ParticipantTeam participantTeam : listParticipantTeams) {

				participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam,
						round);

				for (RefereesPanels referee : listRefereesPanels) {
					ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();
					participantTeamReferees.setParticipantTeam(participantTeam);
					participantTeamReferees.setType(referee.getType());
					participantTeamReferees.setRoundNumber(round);
					participantTeamReferees.setChampionship(championshipid);
					participantTeamReferees.setChampionshipCategory(championshipCategory);
					participantTeamReferees.setJudgeUser(referee.getJudgeUser());

					message = "Panels are added successfully";
					participantTeamRefereesService.save(participantTeamReferees);
				}
				// save it to participantTeamPanel
				ParticipantTeamPanel participantTeamPanel = participantTeamPanelService
						.findByParticipantTeamAndRound(participantTeam.getId(), round);
				if (participantTeamPanel != null) {

					participantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
					participantTeamPanelService.save(participantTeamPanel);
				} else {
					ParticipantTeamPanel newParticipantTeamPanel = new ParticipantTeamPanel();
					newParticipantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanelId);
					newParticipantTeamPanel.setParticipantTeamId(participantTeam.getId());
					newParticipantTeamPanel.setRoundNumber(round);
					participantTeamPanelService.save(newParticipantTeamPanel);

				}

			}
				redirectAttributes.addFlashAttribute("message", "The judge panel for " + championshipCategory.getCategoryDetail() + "  is assigned successfully");

		//	redirectAttributes.addFlashAttribute("message", message + " " + championshipCategory.getCategoryDetail());

			return "redirect:/admin/populate-team-judge-panel";
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			String message1 = "Unable to assign Judge panels to the teams. Some of the teams are already performing.";

			redirectAttributes.addFlashAttribute("message1", message1);
			LOGGER.info("Exit saveAssignTeamJudge method -ManageRefereeController");

			return "redirect:/admin/populate-team-judge-panel";
		}

	}

//	@PostMapping("/manage-team/referees/assignToCategory")
//	public String assignTeamReferees(@RequestParam(name = "panel") Integer championshipRefereePanelId,
//			@RequestParam(name = "category") Integer category,
//			@RequestParam(name = "round") Integer round) throws ChampionshipNotFoundException, EntityNotFoundException {
//		LOGGER.info("Entered assignTeamReferees controller");
//		LOGGER.info(" Referee Panel Id" + championshipRefereePanelId);
//		LOGGER.info("Category id" + category);
//		LOGGER.info("round id" + round);
//		
//		/*
//		 *  1. get all the teams in round id of a given category
//		 *  
//		 *  
//		 *  
//		 *  2. iterate over the teams and do below
//		 *  3. invoke assignTeamReferees method in ManageParticipantTeamController with parameters
//		 * 
//		 */
//		ChampionshipRefereePanels championshipRefereePanels=championshipRefereePanelsService.findById(championshipRefereePanelId);
//		List<RefereesPanels> listRefereesPanels = championshipRefereePanels.getRefereesPanels();
//		
//		ChampionshipCategory championshipCategory=championshipCategoryService.get(category);
//		Championship championship=championshipService.get(1);
//		ChampionshipRounds championshipRounds=championshipRoundsService.findByChampionshipAndChampionshipCatgeoryAndRound(championship,championshipCategory,round);
//		List<ChampionshipParticipantTeams> listChampionshipParticipantTeams=championshipParticipantTeamsService.getAllByChampionshipRounds(championshipRounds);
//		for(ChampionshipParticipantTeams championshipParticipantTeam: listChampionshipParticipantTeams) {
//		ParticipantTeam participantTeam=championshipParticipantTeam.getParticipantTeam();
//		
//		participantTeamRefereesService.deleteTeamReferresForParticipantTeamAndRoundNumber(participantTeam, round);
//		
//	   List<JudgeType> listAssignedTypeReferees = new ArrayList<JudgeType>();
//		for (RefereesPanels referee : listRefereesPanels) {
//		//	listAssignedTypeReferees.add(referee.getType());
//		}
//		LOGGER.info("listAssignedTypeReferees" + listAssignedTypeReferees.toArray());
//		// getRefereesForParticipantTeam(listAssignedReferees, participantTeam);
//		for (JudgeType assignedReferee : listAssignedTypeReferees) {
//			ParticipantTeamReferees participantTeamReferees = new ParticipantTeamReferees();
//			JudgeType judgeType = null;
//		
//				judgeType = judgeTypeService.get(assignedReferee.getId());
//			
//			participantTeamReferees.setParticipantTeam(participantTeam);
//		//	participantTeamReferees.setType(assignedReferee);
//			participantTeamReferees.setRoundNumber(championshipRounds.getRound());
//			participantTeamReferees.setChampionship(championship);
//			participantTeamReferees.setChampionshipCategory(championshipCategory);
//			participantTeamReferees.setType(judgeType);
//			LOGGER.info("assignedReferee" + assignedReferee.toString());
//			participantTeamRefereesService.save(participantTeamReferees);
//			LOGGER.info("participantTeamReferees added successfully");
//		}
//
//		
//		
//		
//		}
//
//		// service.assignRefereesForParticipantTeam(participantTeam);
//		LOGGER.info("Redirected to manage-team/edit page");
//		return "redirect:/admin/populate-team-judge-panel";
//
//	}
//
//	
}
