//package com.swayaan.nysf.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.repository.query.Param;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
//import com.swayaan.nysf.entity.ParticipantTeamTotalScoring;
//import com.swayaan.nysf.entity.User;
//import com.swayaan.nysf.exception.EntityNotFoundException;
//import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
//import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
//import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
//import com.swayaan.nysf.service.ParticipantTeamTotalScoringService;
//import com.swayaan.nysf.service.UserService;
//
//@Controller
//public class ParticipantTeamTotalScoringController {
//	@Autowired
//	ParticipantTeamTotalScoringService participantTeamTotalScoringService;
//
//	@Autowired
//	ParticipantTeamAsanasScoringService participantTeamAsanasScoringService;
//	@Autowired
//	UserService userService;
//	@Autowired
//	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
//	
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamTotalScoringController.class);
//
//	@PostMapping("/judge/participantTeamTotalScoringForRound/{id}/round/{round}")
//	public String saveTeamTotalScore(@Param("teamTotalScore") Float teamTotalScore,
//			@PathVariable(name = "id") Integer championshipParticipantTeamsId,
//			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes)
//			throws ParticipantTeamNotFoundException {
//		LOGGER.info("Entered saveTeamTotalScore ParticipantTeamAsanaScoringRestController");
//		String errorMessage=null;
//		User currentUser = getCurrentUser();
//		ChampionshipParticipantTeams championshipParticipantTeams = null;
//		try {
//			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipParticipantTeamsId);
//		} catch (EntityNotFoundException e) {
//		 errorMessage="Championship Participant Team Not Found";
//			
//		}
//		ParticipantTeamTotalScoring participantTeamTotalScoring=participantTeamTotalScoring=participantTeamTotalScoringService.findByRoundAndChampionshipParticipantTeamAndUser(round,championshipParticipantTeams,currentUser);
//		if(participantTeamTotalScoring==null) {
//			//create new entry
//			ParticipantTeamTotalScoring participantTeamTotalScoringNew=new ParticipantTeamTotalScoring();
//			participantTeamTotalScoringNew.setChampionshipParticipantTeam(championshipParticipantTeams);
//			participantTeamTotalScoringNew.setRound(round);
//			participantTeamTotalScoringNew.setTotalScore(teamTotalScore);
//			participantTeamTotalScoringNew.setUser(currentUser);
//			participantTeamTotalScoringService.save(participantTeamTotalScoringNew);
//		}else {
//			
//			//update total team score
//			participantTeamTotalScoring.setTotalScore(teamTotalScore);
//			participantTeamTotalScoringService.save(participantTeamTotalScoring);
//		}
//		
//		
//		redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//		return "redirect:/judge/championship/"+championshipParticipantTeamsId+"/giveScores";
//		
//	}
//
//	private User getCurrentUser() {
//		String username;
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if (principal instanceof UserDetails) {
//			username = ((UserDetails) principal).getUsername();
//		} else {
//			username = principal.toString();
//		}
//		System.out.println(username);
//		User user = userService.getByEmail(username);
//		return user;
//	}
//}
