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

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.ParticipantTeamAsanaScoring;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ParticipantTeamAsanaTotalScoringController {

	@Autowired
	ParticipantTeamAsanasScoringService participantTeamAsanasScoringService;
	@Autowired
	UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamAsanaTotalScoringController.class);

	@PostMapping("/judge/participantTeamAsanaScoring/{id}")
	public String saveScore(@Param("score") Float score, @PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException, JudgeNotFoundException {
		LOGGER.info("Entered saveScore ParticipantTeamAsanaScoringRestController");
		User user = CommonUtil.getCurrentUser();
		Judge judgeUser=CommonUtil.getCurrentJudge();
		LOGGER.info("Score:"+score);
		ParticipantTeamAsanaScoring participantTeamAsanaScoring = participantTeamAsanasScoringService.getById(id);
		participantTeamAsanaScoring.setScore(score);
		participantTeamAsanaScoring.setJudgeUser(judgeUser);
		participantTeamAsanasScoringService.save(participantTeamAsanaScoring);

		return "ok";
	}

	
}
