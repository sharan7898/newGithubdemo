package com.swayaan.nysf.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeamParticipantTotalScoring;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
import com.swayaan.nysf.service.ParticipantTeamParticipantTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ParticipantTeamParticipantTotalScoringRestController {
	@Autowired
	ParticipantTeamParticipantTotalScoringService participantTeamParticipantTotalScoringService;

	@Autowired
	ParticipantTeamAsanasScoringService participantTeamAsanasScoringService;
	@Autowired
	UserService userService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParticipantTeamParticipantTotalScoringRestController.class);

	@PostMapping("/judge/championshipParticipantTeam/{championshipParticipantId}/participantTeam/{participantTeamParticipantId}/round/{round}")
	public String saveTotalScore(@Param("totalScore") Float totalScore,
			@PathVariable("championshipParticipantId") Integer championshipParticipantId,
			@PathVariable("participantTeamParticipantId") Integer participantTeamParticipantId,
			@PathVariable("round") Integer round, Model model, RedirectAttributes redirectAttributes)
			throws ParticipantTeamNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered saveTotalScore ParticipantTeamParticipantTotalScoringRestController");
		User currentUser = CommonUtil.getCurrentUser();
		Judge judgeUser=CommonUtil.getCurrentJudge();
		LOGGER.info("totalScore:" + totalScore + " " + championshipParticipantId + "  " + participantTeamParticipantId
				+ " " + round);
		ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService
				.get(participantTeamParticipantId);
		ChampionshipParticipantTeams championshipParticipant = null;
		try {
			championshipParticipant = championshipParticipantTeamsService.get(championshipParticipantId);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParticipantTeamParticipantTotalScoring participantTeamParticipantTotalScoring = participantTeamParticipantTotalScoringService
				.findByRoundAndJudgeUserAndParticipantTeamParticipantsAndChampionshipParticipantTeams(round, judgeUser,
						participantTeamParticipant, championshipParticipant);
		if (participantTeamParticipantTotalScoring != null) {
			participantTeamParticipantTotalScoring.setTotalScore(totalScore);
			participantTeamParticipantTotalScoringService.save(participantTeamParticipantTotalScoring);
		} else {
			ParticipantTeamParticipantTotalScoring participantTeamParticipantTotalScoringNew = new ParticipantTeamParticipantTotalScoring();
			participantTeamParticipantTotalScoringNew.setChampionshipParticipantTeams(championshipParticipant);
			participantTeamParticipantTotalScoringNew.setParticipantTeamParticipant(participantTeamParticipant);
			participantTeamParticipantTotalScoringNew.setRound(round);
			participantTeamParticipantTotalScoringNew.setTotalScore(totalScore);
			participantTeamParticipantTotalScoringNew.setJudgeUser(judgeUser);
			participantTeamParticipantTotalScoringService.save(participantTeamParticipantTotalScoringNew);
		}

		return "success";
	}

	
}
