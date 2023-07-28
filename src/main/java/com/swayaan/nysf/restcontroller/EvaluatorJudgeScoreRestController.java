package com.swayaan.nysf.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.AsanaPerformEnum;
import com.swayaan.nysf.entity.EvaluatorJudgeScore;
import com.swayaan.nysf.entity.SequenceEnum;
import com.swayaan.nysf.service.EvaluatorJudgeScoreService;

@RestController
public class EvaluatorJudgeScoreRestController {

	@Autowired
	EvaluatorJudgeScoreService evaluatorJudgeScoreService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluatorJudgeScoreRestController.class);

	@PostMapping("judge/EvaluatorJudgeScore/{id}/saveAsPerSequence")
	public String saveEvaluatorAsPerSequence(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "asPerSequence") String asPerSequence) {

		LOGGER.info("Entered EvaluatorJudgeScoreRestController - saveEvaluatorScore method");
		LOGGER.info("asPerSequence" + asPerSequence);
		if (asPerSequence.isEmpty() || asPerSequence != null) {
			EvaluatorJudgeScore evaluatorJudgeScore = evaluatorJudgeScoreService.findById(id);
			if (evaluatorJudgeScore != null) {
				SequenceEnum sequence = SequenceEnum.valueOf(asPerSequence);
				evaluatorJudgeScore.setAsPerSequence(sequence);
				evaluatorJudgeScoreService.save(evaluatorJudgeScore);
			}
		}
		return "success";
	}

	@PostMapping("judge/EvaluatorJudgeScore/{id}/saveIsPerformed")
	public String saveEvaluatorIsPerformed(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "isPerformed") String isPerformed) {

		LOGGER.info("Entered EvaluatorJudgeScoreRestController - saveEvaluatorScore method");
		LOGGER.info("isPerformed" + isPerformed);
		if (isPerformed.isEmpty() || isPerformed != null) {
			EvaluatorJudgeScore evaluatorJudgeScore = evaluatorJudgeScoreService.findById(id);
			if (evaluatorJudgeScore != null) {
				AsanaPerformEnum asanaPerformEnum = AsanaPerformEnum.valueOf(isPerformed);
				evaluatorJudgeScore.setIsPerformed(asanaPerformEnum);
				evaluatorJudgeScoreService.save(evaluatorJudgeScore);
			}
		}
		return "success";
	}

	@PostMapping("judge/EvaluatorJudgeScore/{id}/savePenaltyScore")
	public String saveEvaluatorPenaltyScore(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "score",defaultValue="0.0") Float score) {

		LOGGER.info("Entered EvaluatorJudgeScoreRestController - saveEvaluatorIsPerformed method");
		LOGGER.info("score" + score);
		if (score != null) {
			EvaluatorJudgeScore evaluatorJudgeScore = evaluatorJudgeScoreService.findById(id);
			if (evaluatorJudgeScore != null) {
				evaluatorJudgeScore.setPenaltyScore(score);
				if(score==-1) {
					evaluatorJudgeScore.setPenaltyScore(null);
				}
				
				evaluatorJudgeScoreService.save(evaluatorJudgeScore);
			}

		}
		return "success";
	}

}
