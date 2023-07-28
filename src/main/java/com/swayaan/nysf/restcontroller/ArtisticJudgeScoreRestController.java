package com.swayaan.nysf.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.ArtisticJudgeScore;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.ArtisticJudgeScoreService;

@RestController
public class ArtisticJudgeScoreRestController {
	@Autowired
	ArtisticJudgeScoreService artisticJudgeScoreService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ArtisticJudgeScoreRestController.class);

	@PostMapping("/judge/artisticJudgeScore/{id}/saveScore")
	public String saveScore(@PathVariable("id") Integer id, @RequestParam("score") Float score,  @RequestParam(name="penalty",required=false) Float penalty,Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScore ArtisticJudgeScoreRestController");
		LOGGER.info("score "+score+"  penalty "+penalty);
		ArtisticJudgeScore artisticJudgeScore = artisticJudgeScoreService.findById(id);
		if (artisticJudgeScore != null) {
			artisticJudgeScore.setScore(score);
			if(penalty!=null) {
				artisticJudgeScore.setPenaltyScore(penalty);
			}
			artisticJudgeScoreService.save(artisticJudgeScore);
		}

		return "success";
	}
	
	
//	@PostMapping("/judge/artisticJudgePenaltyScore/{id}/saveScore")
//	public String savePenalty(@PathVariable("id") Integer id, @RequestParam("penalty") Float penalty, Model model,
//			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
//		LOGGER.info("Entered saveTotalScore ArtisticJudgeScoreRestController");
//
//		ArtisticJudgeScore artisticJudgeScore = artisticJudgeScoreService.findById(id);
//		if (artisticJudgeScore != null) {
//			artisticJudgeScore.setPenaltyScore(penalty);
//			artisticJudgeScoreService.save(artisticJudgeScore);
//		}
//
//		return "success";
//	}
//	
	

	@PostMapping("/judge/artisticJudgeScore/{id}/saveTotalScore")
	public String saveTotalScore(@PathVariable("id") Integer id, @RequestParam("score") Float score,
			@RequestParam(name = "penalty",defaultValue="0.0",required = false) Float penalty, Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScore ArtisticJudgeScoreRestController");
		LOGGER.info("score:" + score + " " + "penaltyScore  " + penalty);
		ArtisticJudgeScore artisticJudgeScore = artisticJudgeScoreService.findById(id);
		if (artisticJudgeScore != null) {
			artisticJudgeScore.setScore(score);
			artisticJudgeScore.setPenaltyScore(penalty);

			float totalScore = (float) (Math.round((score - penalty) * 100.0) / 100.0);
			artisticJudgeScore.setTotalScore(totalScore);
			artisticJudgeScore.setStatus(true);
			artisticJudgeScoreService.save(artisticJudgeScore);
		}

		return "success";
	}

}
