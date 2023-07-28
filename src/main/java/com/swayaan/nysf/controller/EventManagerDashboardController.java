package com.swayaan.nysf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.RoundEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/eventmanager")
public class EventManagerDashboardController {

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ParticipantTeamService participantTeamService;

	@Autowired
	ChampionshipRoundsService championshipRoundService;

	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerChampionshipController.class);

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/dashboard")
	public String displayDashboard(Model model,HttpServletRequest request) {
		LOGGER.info("Entered displayDashboard method EventManagerDashboardController");

		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		List<Championship> listChampionships = championshipService
				.listAllChampionshipsForCurrentUserDashboard(currentUser);

		List<ParticipantTeam> listTotalParticipantTeams = new ArrayList<>();
		List<ParticipantTeam> listNoNAssignedJudgePanel = new ArrayList<>();
		List<ParticipantTeam> listNoNAssignedAssanas = new ArrayList<>();
		

		int Championship =0;
		int TotalParticipantTeams=0;
		int NonAssignedJudgePanel=0;
		int NonAssignedAssanas=0;
				

		for (Championship champ : listChampionships) {

			List<ParticipantTeam> listParticipantTeamsPending = participantTeamService
					.listOfChampionshipParticipantTeams(champ.getId(), champ.getName());
			List<ParticipantTeam> listParticipantTeams = participantTeamService
					.listOfChampionshipParticipantTeamsJudgeNoNAssigned(champ.getId(), champ.getName());
			
			if (!listParticipantTeamsPending.isEmpty()) {
				for (ParticipantTeam participantTeams : listParticipantTeamsPending) {
					if (champ.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
						listTotalParticipantTeams.add(participantTeams);
					}
				}
			}
				if (!listParticipantTeams.isEmpty()) {

					for (ParticipantTeam participantTeams : listParticipantTeams) {
					if (champ.getStatus().equals(ChampionshipStatusEnum.ONGOING)) {
						ChampionshipCategory champCategory = championshipCategoryService
								.getChampionshipCategoryForAllConditions(participantTeams.getChampionship().getId(),
										participantTeams.getAsanaCategory().getId(),
										participantTeams.getCategory().getId(),
										participantTeams.getGender().toString());
						
						List<ChampionshipRounds> listChampionshipRounds = championshipRoundService
								.findByChampionshipAndChampionshipCategory(champ, champCategory);
						for (ChampionshipRounds champRound : listChampionshipRounds) {
							if (champRound.getStatus().equals(RoundStatusEnum.ONGOING)) {
								boolean isPresent = participantTeamRefereesService.existsByTeam(participantTeams);
								if(!isPresent) {
									listNoNAssignedJudgePanel.add(participantTeams);
								}
								boolean isAsanaPresent = participantTeamAsanasService.existsByTeamAsana(participantTeams);
								if(!isAsanaPresent) {
									listNoNAssignedAssanas.add(participantTeams);
								}
								
							}
						}
					}

				}
			}
				if (listTotalParticipantTeams != null) {
					model.addAttribute("numberOfNotifications", listTotalParticipantTeams.size());
					model.addAttribute("numberOfParticipantTeamEnrollment", listTotalParticipantTeams.size());
					if (listTotalParticipantTeams.size() > 5) {
						model.addAttribute("listParticipantTeamsPending", listTotalParticipantTeams.subList(0, 5));
					} else {
						model.addAttribute("listParticipantTeamsPending", listTotalParticipantTeams);
					}
				} else {
					model.addAttribute("numberOfNotifications", listTotalParticipantTeams.size());
					model.addAttribute("numberOfParticipantTeamEnrollment", TotalParticipantTeams);
					model.addAttribute("listParticipantTeamsPending", TotalParticipantTeams);
				}
				
				if (listNoNAssignedJudgePanel != null) {
					model.addAttribute("numberOfNotifications", listNoNAssignedJudgePanel.size());
					model.addAttribute("numberOfParticipantTeamNonAssignedPanels", listNoNAssignedJudgePanel.size());
					if (listNoNAssignedJudgePanel.size() > 5) {
						model.addAttribute("listNoNAssignedJudgePanel", listNoNAssignedJudgePanel.subList(0, 5));
					} else {
						model.addAttribute("listNoNAssignedJudgePanel", listNoNAssignedJudgePanel);
					}
				} else {
					model.addAttribute("numberOfNotifications", listNoNAssignedJudgePanel.size());
					model.addAttribute("numberOfParticipantTeamNonAssignedPanels", NonAssignedJudgePanel);
					model.addAttribute("listNoNAssignedJudgePanel", NonAssignedJudgePanel);
				}
				
				if (listNoNAssignedAssanas != null) {
					model.addAttribute("numberOfNotifications", listNoNAssignedAssanas.size());
					model.addAttribute("numberOfParticipantTeamNonAssignedAssanas", listNoNAssignedAssanas.size());
					if (listNoNAssignedAssanas.size() > 5) {
						model.addAttribute("listNoNAssignedAssanas", listNoNAssignedAssanas.subList(0, 5));
					} else {
						model.addAttribute("listNoNAssignedAssanas", listNoNAssignedAssanas);
					}
				} else {
					model.addAttribute("numberOfNotifications", listNoNAssignedAssanas.size());
					model.addAttribute("numberOfParticipantTeamNonAssignedAssanas", NonAssignedAssanas);
					model.addAttribute("listNoNAssignedAssanas", NonAssignedAssanas);
				}

		}
		if (listChampionships != null) {
			model.addAttribute("numberOfNotifications", listChampionships.size());
			model.addAttribute("numberOfChampionshipInEventManager", listChampionships.size());
			if (listChampionships.size() > 5) {
				model.addAttribute("listChampionships", listChampionships.subList(0, 5));
			} else {
				model.addAttribute("listChampionships", listChampionships);
			}
		} else {
			model.addAttribute("numberOfNotifications", listChampionships.size());
			model.addAttribute("numberOfChampionshipInEventManager", Championship);
			model.addAttribute("listChampionships", Championship);
		}

		

//		model.addAttribute("listNoNAssignedJudgePanel", listNoNAssignedJudgePanel);
//
//		model.addAttribute("listParticipantTeamsPending", listTotalParticipantTeams);
//
//		model.addAttribute("listNoNAssignedAssanas", listNoNAssignedAssanas);

	//	model.addAttribute("listChampionships", listChampionships);

		
		model.addAttribute("notificationCountForEventManager", "notificationCount");
		model.addAttribute("pageTitle", "Dashboard");
		LOGGER.info("Exit displayDashboard method EventManagerDashboardController");

		return "eventmanager/eventmanager_dashboard";
	}

}
