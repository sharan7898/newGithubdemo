package com.swayaan.nysf.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;

@RestController
public class ParticipantChampionshipRegistrationsRestController {

	@Autowired
	ParticipantTeamService participantTeamService;

	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	
	@Autowired ChampionshipService championshipService;
	
	@Autowired ChampionshipCategoryService championshipCategoryService;
	
	@Autowired CommonEmailUtil commonEmailUtil;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParticipantChampionshipRegistrationsRestController.class);

	private static final int ADMINISTRATOR_ROLE_ID = 1;

	@PostMapping("/admin/manage-team-registrations")
	public ResponseEntity<?> updateRegisteredParticipantTeamStatus(HttpServletRequest request,
			@RequestParam("status") String status, @RequestParam("participantTeamId") Integer participantTeamId) {
		LOGGER.info(
				"Entered updateRegisteredParticipantTeamStatus - ParticipantChampionshipRegistrationsRestController");

		LOGGER.info("participantTeamId :" + participantTeamId + " status :" + status);

		User currentUser = CommonUtil.getCurrentUser();

		try {
			String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort() + request.getContextPath();

			ParticipantTeam participantTeam = participantTeamService.get(participantTeamId);
			Integer championshipId=participantTeam.getChampionship().getId();
			Championship championship=championshipService.get(championshipId);
			if (participantTeam != null) {
				if (currentUser.getRoleId() != ADMINISTRATOR_ROLE_ID) {
					if (!championship.getCreatedBy().equals(currentUser)) {
						return new ResponseEntity<>("unabletochangestatus", HttpStatus.OK);
					}
				}
			}

			
			// check if the championship round is already completed. Then Dont allow to approve.
			ChampionshipCategory championshipCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
					participantTeam.getChampionship().getId(), participantTeam.getAsanaCategory().getId(),
					participantTeam.getCategory().getId(), participantTeam.getGender().toString());

			List<ChampionshipRounds> listChampionshipRounds = championshipRoundsService
					.findByChampionshipAndChampionshipCategory(participantTeam.getChampionship(), championshipCategory);

			List<ChampionshipRounds> listFilteredChampionshipRoundsWithCompletedStatus = listChampionshipRounds.stream()
					.filter(championshipRounds -> championshipRounds.getStatus().equals(RoundStatusEnum.COMPLETED))
					.collect(Collectors.toList());
			if (!listFilteredChampionshipRoundsWithCompletedStatus.isEmpty()) {
				return new ResponseEntity<>("roundcompleted", HttpStatus.OK);
			}
			
			RegistrationStatusEnum teamStatus=RegistrationStatusEnum.PENDING;
			if (participantTeam != null) {
				if (participantTeam.getStatus().equals(RegistrationStatusEnum.PENDING)) {
					if (status.equals("APPROVE")) {
						teamStatus=RegistrationStatusEnum.APPROVED;
						participantTeamService.updateRegisteredTeamStatus(participantTeam,teamStatus);
						Boolean isExists = championshipParticipantTeamsService
								.checkifChampionshipParticipantTeamExists(participantTeam);
						if (!isExists) {
							participantTeamService.copyToChampionshipParticipantTeams(participantTeam);
							// Send email notification of approval
							commonEmailUtil.mailParticipantForRegistrationAprovalOrRejection(nysfPortalLink,
									participantTeam);
						}

					} else if (status.equals("REJECT")) {
						teamStatus=RegistrationStatusEnum.REJECTED;
						participantTeamService.updateRegisteredTeamStatus(participantTeam,teamStatus);
						// Send email notification of Rejection
						commonEmailUtil.mailParticipantForRegistrationAprovalOrRejection(nysfPortalLink,
								participantTeam);
					}

				} else {
					return new ResponseEntity<>("unabletochangestatus", HttpStatus.OK);
				}
			}

			return new ResponseEntity<>("success", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("unabletochangestatus", HttpStatus.OK);
		}

	}

}
