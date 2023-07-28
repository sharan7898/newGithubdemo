package com.swayaan.nysf.restcontroller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.NotificationDTO;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.EventManagerRegistrationService;
import com.swayaan.nysf.service.EventRegistrationService;
import com.swayaan.nysf.service.JudgeRegistrationService;
import com.swayaan.nysf.service.ParticipantRegistrationService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonUtil;

@RestController
public class NotificationRestController {
	@Autowired
	EventManagerRegistrationService eventManagerRegistrationService;

	@Autowired
	ParticipantRegistrationService service;
	@Autowired
	JudgeRegistrationService judgeRegistrationService;
	@Autowired
	EventRegistrationService eventRegistrationService;
	
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


	@GetMapping("/admin/notification")
	public NotificationDTO Notification() {
		Integer notificationCount = 0;
		List<EventRegistration> listEventRegistrations = eventRegistrationService.getAllNonApprovedEvents();
		List<EventManagerRegistration> listEventManagerRegistrations = eventManagerRegistrationService
				.listAllNonApprovedEventManagers();
		List<ParticipantRegistration> listParticipantRegistrations = service.listAllNonApprovedParticpants();
		List<JudgeRegistration> listJudgeRegistrations = judgeRegistrationService.listAllNonApprovedJudges();
		notificationCount = listEventRegistrations.size() + listEventManagerRegistrations.size()
				+ listParticipantRegistrations.size() + listJudgeRegistrations.size();

		NotificationDTO result = new NotificationDTO();

		result.setNotificationCount(notificationCount);

		return result;
	}
	

	@GetMapping("/event-manager/notification")
	public NotificationDTO NotificationOfEventManager() {
		// For Navigation history
		User currentUser = CommonUtil.getCurrentUser();

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
		}
		Integer notificationCount = 0;
		notificationCount =listTotalParticipantTeams.size()+listNoNAssignedJudgePanel.size()+listNoNAssignedAssanas.size();

		NotificationDTO result = new NotificationDTO();

		result.setNotificationCount(notificationCount);
		

		return result;
		
	}

}
