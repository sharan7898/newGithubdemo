package com.swayaan.nysf.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.service.TimeKeeperJudgeScoreService;

@RestController
public class TimekeeperScoreRestController {

	@Autowired
	TimeKeeperJudgeScoreService timeKeeperJudgeScoreService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TimekeeperScoreRestController.class);

	/*
	 * @PostMapping("/judge/timekeeperJudge/{id}/save") public String
	 * saveScore(@PathVariable(name = "id") Integer id,
	 * 
	 * @RequestParam(name = "attempt1", required = false) Integer attempt1,
	 * 
	 * @RequestParam(name = "attempt2", required = false) Integer attempt2,
	 * 
	 * @RequestParam(name = "penalty", required = false) Boolean penaltyScore,
	 * 
	 * @RequestParam("Float") Float totalScore, Model model) {
	 * LOGGER.info("Entered TimekeeperScoreController - saveScore method");
	 * 
	 * LOGGER.info("id " + id + " attempt1 " + attempt1 + " attempt2 " + attempt2 +
	 * " penalty " + penaltyScore + " totalScore " + totalScore); if (totalScore !=
	 * null) { TimeKeeperJudgeScore timeKeeperJudgeScore =
	 * timeKeeperJudgeScoreService.findById(id); if (timeKeeperJudgeScore != null) {
	 * timeKeeperJudgeScore.setAttempt1(attempt1);
	 * timeKeeperJudgeScore.setAttempt2(attempt2);
	 * timeKeeperJudgeScore.setTotalScore(totalScore); if (penaltyScore) {
	 * timeKeeperJudgeScore.setPenaltyScore(penaltyScore); }
	 * timeKeeperJudgeScore.setStatus(true);
	 * timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);
	 * 
	 * } }
	 * 
	 * return "success"; }
	 */

	@PostMapping("/judge/timekeeperJudgeScoreForOtherGroups/{id}/save")
	public String saveScoreForOtherGroups(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "timeInSeconds", required = false) Integer timeInSeconds,
			@RequestParam(name = "score") Float score, Model model) {
		LOGGER.info("Entered TimekeeperScoreController - saveScore method");

		LOGGER.info("id " + id + " score " + score + " timeInSeconds " + timeInSeconds);
		if (score != null) {
			TimeKeeperJudgeScore timeKeeperJudgeScore = timeKeeperJudgeScoreService.findById(id);
			if (timeKeeperJudgeScore != null) {
				timeKeeperJudgeScore.setTimeInSeconds(timeInSeconds);
				timeKeeperJudgeScore.setScore(score);
				timeKeeperJudgeScore.setTotalScore(score);
				timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);

			}
		}

		return "success";
	}

	@PostMapping("/judge/timekeeperJudgeScoreForOtherGroups/{id}/saveWholeTime")
	public String saveScoreForOtherGroups(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "wholeTimeInMin") Integer wholeTimeInMin,
			@RequestParam(name = "wholeTimeInSec") Integer wholeTimeInSec, Model model) {
		LOGGER.info("Entered TimekeeperScoreController - saveScore method");

		LOGGER.info("id " + id + " wholeTimeInMin " + wholeTimeInMin + " wholeTimeInSec " + wholeTimeInSec);
		if (wholeTimeInSec != null) {
			TimeKeeperJudgeScore timeKeeperJudgeScore = timeKeeperJudgeScoreService.findById(id);
			if (timeKeeperJudgeScore != null) {
				timeKeeperJudgeScore.setTimeInSeconds(wholeTimeInSec);
				timeKeeperJudgeScore.setTimeInMinutes(wholeTimeInMin);

				timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);

			}
		}

		return "success";
	}

	@PostMapping("/judge/timekeeperJudge/{id}/saveAttemptTime")
	public String saveAttempScore(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "attempt1", required = false) Integer attempt1,
			@RequestParam(name = "attempt2", required = false) Integer attempt2,
			@RequestParam(name = "attempt1Status", required = false) Boolean attempt1Status,
			@RequestParam(name = "attempt2Status", required = false) Boolean attempt2Status,
			@RequestParam(name = "penalty", required = false) Boolean penaltyScore,
			@RequestParam(name = "totalScore", required = false) Float totalScore, 
			@RequestParam(name = "status", required = false) Boolean status,Model model) {
		LOGGER.info("Entered TimekeeperScoreController - saveScore method");

		
		LOGGER.info("attempt1" + attempt1 + " attempt2 " + attempt2 + " attempt1Status " + attempt1Status
				+ " attempt2Status " + attempt2Status + " penaltyScore " + penaltyScore + " totalScore " + totalScore +" status "+status);
		TimeKeeperJudgeScore timeKeeperJudgeScore = timeKeeperJudgeScoreService.findById(id);
		if (timeKeeperJudgeScore != null) {

			timeKeeperJudgeScore.setAttempt1(attempt1);
			if(attempt1Status!=null) {
			timeKeeperJudgeScore.setAttempt1Status(attempt1Status);
			}
			timeKeeperJudgeScore.setAttempt2(attempt2);
			if(attempt2Status!=null) {
			timeKeeperJudgeScore.setAttempt2Status(attempt2Status);
			}
			timeKeeperJudgeScore.setPenaltyScore(penaltyScore);
			timeKeeperJudgeScore.setTotalScore(totalScore);
			timeKeeperJudgeScore.setStatus(status);
			timeKeeperJudgeScoreService.save(timeKeeperJudgeScore);

		}

		return "success";
	}

}
