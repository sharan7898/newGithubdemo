package com.swayaan.nysf.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeamAsanaQuestionTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.QuestionTypeEnum;
import com.swayaan.nysf.entity.TraditionalJudgeScore;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.ParticipantTeamAsanaQuestionTotalScoreService;
import com.swayaan.nysf.service.TraditionalJudgeScoreService;

@RestController
public class TraditionalJudgeScoreRestController {

	@Autowired
	TraditionalJudgeScoreService traditionalJudgeScoreService;

	@Autowired
	ParticipantTeamAsanaQuestionTotalScoreService participantTeamAsanaQuestionTotalScoreService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TraditionalJudgeScoreRestController.class);

	@PostMapping("/judge/traditionalJudgeScore/{id}/saveScore")
	public String saveScore(@PathVariable("id") Integer id, @RequestParam("score") Float score, Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveScore TraditionalJudgeScoreRestController");
		LOGGER.info("score:" + score);
		if (score != null) {
			TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(id);
			if (traditionalJudgeScore != null) {
				traditionalJudgeScore.setScore(score);
				// traditionalJudgeScore.setStatus(true);
				traditionalJudgeScoreService.save(traditionalJudgeScore);

			}
		}
		return "success";
	}

	@PostMapping("/judge/traditionalJudgeScore/{id}/saveScoreWithStatus")
	public String saveScoreWithStatus(@PathVariable("id") Integer id, @RequestParam(name="score") Float score, @RequestParam(name="totalScore",required=false) Float totalScore, Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveScore TraditionalJudgeScoreRestController");
		LOGGER.info("score:" + score);
		if (score != null) {
			TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(id);
			if (traditionalJudgeScore != null) {
				traditionalJudgeScore.setScore(score);
				if(totalScore!=null) {
				traditionalJudgeScore.setTotalScore(totalScore);
				}
				traditionalJudgeScore.setStatus(true);
				traditionalJudgeScoreService.save(traditionalJudgeScore);

			}
		}
		return "success";
	}

	@PostMapping("/judge/artisticSingleTraditionalJudgeTotalScoreForAsana/{id}/saveScore")
	public String saveTotalScoreForArtisticSingle(@PathVariable("id") Integer id,
			@RequestParam(name = "score",required=false) Float score, @RequestParam(name="totalScore",required=false) Float totalScore, Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScoreForArtisticSingle TraditionalJudgeScoreRestController");
		LOGGER.info("Saving score for the record with id "+id+ " score "+score+ " totalScore:" + totalScore);

		TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(id);
		if (traditionalJudgeScore != null) {
			traditionalJudgeScore.setScore(score);
			traditionalJudgeScore.setTotalScore(totalScore);
			traditionalJudgeScore.setStatus(true);
			traditionalJudgeScoreService.save(traditionalJudgeScore);
			ChampionshipParticipantTeams championshipParticipantTeams = traditionalJudgeScore
					.getChampionshipParticipantTeams();
			ParticipantTeamAsanas participantteamAsanas = traditionalJudgeScore.getParticipantTeamAsanas();
			ParticipantTeamParticipants participantTeamParticipants = traditionalJudgeScore
					.getParticipantTeamParticipants();
			ParticipantTeamReferees participantTeamReferees = traditionalJudgeScore.getParticipantTeamReferees();
			QuestionTypeEnum questionType = traditionalJudgeScore.getQuestionType();
			Integer round = traditionalJudgeScore.getRound();
			ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore = participantTeamAsanaQuestionTotalScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndParticipantTeamRefereesAndQuestionTypeEnumAndRound(
							championshipParticipantTeams, participantteamAsanas, participantTeamParticipants,
							participantTeamReferees, questionType, round);
			if (participantTeamAsanaQuestionTotalScore == null) {
				ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScoreNew = new ParticipantTeamAsanaQuestionTotalScore();
				participantTeamAsanaQuestionTotalScoreNew.setChampionshipParticipantTeams(championshipParticipantTeams);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamAsanas(participantteamAsanas);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamParticipants(participantTeamParticipants);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamReferees(participantTeamReferees);
				participantTeamAsanaQuestionTotalScoreNew.setQuestionType(questionType);
				participantTeamAsanaQuestionTotalScoreNew.setRound(round);
				participantTeamAsanaQuestionTotalScoreNew.setTotalScore(totalScore);
				participantTeamAsanaQuestionTotalScoreNew.setSequenceNumber(traditionalJudgeScore.getSequenceNumber());

				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScoreNew);
			} else {
				participantTeamAsanaQuestionTotalScore.setTotalScore(totalScore);
				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScore);
			}

		}

		return "success";
	}

	@PostMapping("/judge/traditionalJudgeTotalScoreForAsana/{id}/saveScore")
	public String saveTotalScore(@PathVariable("id") Integer id, @RequestParam("totalScore") Float totalScore,
			Model model, RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScore TraditionalJudgeScoreRestController");
		LOGGER.info("totalScore:" + totalScore);

		TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(id);
		if (traditionalJudgeScore != null) {
			// traditionalJudgeScore.setTotalScore(totalScore);
			// traditionalJudgeScoreService.save(traditionalJudgeScore);
			ChampionshipParticipantTeams championshipParticipantTeams = traditionalJudgeScore
					.getChampionshipParticipantTeams();
			ParticipantTeamAsanas participantteamAsanas = traditionalJudgeScore.getParticipantTeamAsanas();
			ParticipantTeamParticipants participantTeamParticipants = traditionalJudgeScore
					.getParticipantTeamParticipants();
			ParticipantTeamReferees participantTeamReferees = traditionalJudgeScore.getParticipantTeamReferees();
			QuestionTypeEnum questionType = traditionalJudgeScore.getQuestionType();
			Integer round = traditionalJudgeScore.getRound();
			ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore = participantTeamAsanaQuestionTotalScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamParticipantsAndParticipantTeamRefereesAndQuestionTypeEnumAndRound(
							championshipParticipantTeams, participantteamAsanas, participantTeamParticipants,
							participantTeamReferees, questionType, round);
			if (participantTeamAsanaQuestionTotalScore == null) {
				ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScoreNew = new ParticipantTeamAsanaQuestionTotalScore();
				participantTeamAsanaQuestionTotalScoreNew.setChampionshipParticipantTeams(championshipParticipantTeams);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamAsanas(participantteamAsanas);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamParticipants(participantTeamParticipants);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamReferees(participantTeamReferees);
				participantTeamAsanaQuestionTotalScoreNew.setQuestionType(questionType);
				participantTeamAsanaQuestionTotalScoreNew.setRound(round);
				participantTeamAsanaQuestionTotalScoreNew.setTotalScore(totalScore);
				participantTeamAsanaQuestionTotalScoreNew.setSequenceNumber(traditionalJudgeScore.getSequenceNumber());

				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScoreNew);
			} else {
				participantTeamAsanaQuestionTotalScore.setTotalScore(totalScore);
				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScore);
			}

		}

		return "success";
	}

	@PostMapping("/judge/ArtisticGroupTraditionalJudgeTotalScoreForAsana/saveTotalScore")
	public String saveTotalScoreForArtisticGroup(@RequestParam("questionList[]") Integer[] questionList,
			@RequestParam("totalScore") Float totalScore, Model model, RedirectAttributes redirectAttributes)
			throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScoreForArtisticGroup TraditionalJudgeScoreRestController");
		LOGGER.info("totalScore:" + totalScore);
		ChampionshipParticipantTeams championshipParticipantTeams = null;
		LOGGER.info("questionList:" + questionList.length);
		ParticipantTeamAsanas participantteamAsanas = null;
		ParticipantTeamReferees participantTeamReferees = null;
		Integer round = null;
		QuestionTypeEnum questionType = null;
		Integer sequenceNumber = traditionalJudgeScoreService.findById(questionList[0]).getSequenceNumber();
		for (Integer questionId : questionList) {
			TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(questionId);
			if (traditionalJudgeScore != null) {
				championshipParticipantTeams = traditionalJudgeScore.getChampionshipParticipantTeams();
				participantteamAsanas = traditionalJudgeScore.getParticipantTeamAsanas();
				participantTeamReferees = traditionalJudgeScore.getParticipantTeamReferees();
				round = traditionalJudgeScore.getRound();
				questionType = traditionalJudgeScore.getQuestionType();
				traditionalJudgeScore.setTotalScore(totalScore);
				traditionalJudgeScore.setStatus(true);
				traditionalJudgeScoreService.save(traditionalJudgeScore);
			}
		}
		ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore = participantTeamAsanaQuestionTotalScoreService
				.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound(
						championshipParticipantTeams, participantteamAsanas, participantTeamReferees, round);
		if (participantTeamAsanaQuestionTotalScore != null) {
			participantTeamAsanaQuestionTotalScore.setTotalScore(totalScore);
			participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScore);
		} else {
			ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScoreNew = new ParticipantTeamAsanaQuestionTotalScore();
			participantTeamAsanaQuestionTotalScoreNew.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamAsanas(participantteamAsanas);
			participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamReferees(participantTeamReferees);
			participantTeamAsanaQuestionTotalScoreNew.setQuestionType(questionType);
			participantTeamAsanaQuestionTotalScoreNew.setRound(round);
			participantTeamAsanaQuestionTotalScoreNew.setTotalScore(totalScore);
			participantTeamAsanaQuestionTotalScoreNew.setSequenceNumber(sequenceNumber);
			participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScoreNew);

		}

		return "success";
	}

	@PostMapping("/judge/ArtisticPairTraditionalJudgeTotalScoreForAsana/saveTotalScore")
	public String saveTotalScoreForArtisticPair(@RequestParam("questionIdList[]") Integer[] questionIdList,
			@RequestParam("totalScore") Float totalScore, @RequestParam("commonQuestionId") Integer commonQuestionId,
			@RequestParam("commonQuestionScore") Float commonQuestionScore, Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScoreForArtisticGroup TraditionalJudgeScoreRestController");
		LOGGER.info("totalScore:" + totalScore);
		LOGGER.info("questionList:" + questionIdList.length);
		Integer sequenceNumber = traditionalJudgeScoreService.findById(questionIdList[0]).getSequenceNumber();
		if (commonQuestionScore != null) {
			ChampionshipParticipantTeams championshipParticipantTeams = null;
			ParticipantTeamAsanas participantteamAsanas = null;
			ParticipantTeamReferees participantTeamReferees = null;
			Integer round = null;
			QuestionTypeEnum questionType = null;
			for (Integer questionId : questionIdList) {
				TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(questionId);
				if (traditionalJudgeScore != null) {
					championshipParticipantTeams = traditionalJudgeScore.getChampionshipParticipantTeams();
					participantteamAsanas = traditionalJudgeScore.getParticipantTeamAsanas();
					participantTeamReferees = traditionalJudgeScore.getParticipantTeamReferees();
					round = traditionalJudgeScore.getRound();

					traditionalJudgeScore.setTotalScore(totalScore);
					traditionalJudgeScore.setStatus(true);
					traditionalJudgeScoreService.save(traditionalJudgeScore);
				}
			}
			// save common question score
			TraditionalJudgeScore traditionalJudgeScoreCommon = traditionalJudgeScoreService.findById(commonQuestionId);
			if (traditionalJudgeScoreCommon != null) {
				traditionalJudgeScoreCommon.setScore(commonQuestionScore);
				traditionalJudgeScoreCommon.setTotalScore(totalScore);
				traditionalJudgeScoreCommon.setStatus(true);
				traditionalJudgeScoreService.save(traditionalJudgeScoreCommon);
			}

			ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore = participantTeamAsanaQuestionTotalScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound(
							championshipParticipantTeams, participantteamAsanas, participantTeamReferees, round);
			if (participantTeamAsanaQuestionTotalScore != null) {
				participantTeamAsanaQuestionTotalScore.setTotalScore(totalScore);
				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScore);
			} else {
				ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScoreNew = new ParticipantTeamAsanaQuestionTotalScore();
				participantTeamAsanaQuestionTotalScoreNew.setChampionshipParticipantTeams(championshipParticipantTeams);
				// participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamAsanas(participantteamAsanas);
				participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamReferees(participantTeamReferees);
				participantTeamAsanaQuestionTotalScoreNew.setQuestionType(questionType);
				participantTeamAsanaQuestionTotalScoreNew.setRound(round);
				participantTeamAsanaQuestionTotalScoreNew.setTotalScore(totalScore);
				participantTeamAsanaQuestionTotalScoreNew.setSequenceNumber(sequenceNumber);
				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScoreNew);

			}
		}

		return "success";
	}

	@PostMapping("/judge/RhythmicPairTraditionalJudgeTotalScoreForAsana/saveTotalScore")
	public String saveTotalScoreForRhythmicPair(@RequestParam("questionIdList[]") Integer[] questionIdList,
			@RequestParam("totalScore") Float totalScore, Model model, RedirectAttributes redirectAttributes)
			throws ParticipantTeamNotFoundException {
		LOGGER.info("Entered saveTotalScoreForArtisticGroup TraditionalJudgeScoreRestController");
		LOGGER.info("totalScore:" + totalScore);
		LOGGER.info("questionList:" + questionIdList.length);

		ChampionshipParticipantTeams championshipParticipantTeams = null;
		ParticipantTeamAsanas participantteamAsanas = null;
		ParticipantTeamReferees participantTeamReferees = null;
		Integer round = null;
		QuestionTypeEnum questionType = null;
		Integer sequenceNumber = traditionalJudgeScoreService.findById(questionIdList[0]).getSequenceNumber();

		for (Integer questionId : questionIdList) {
			TraditionalJudgeScore traditionalJudgeScore = traditionalJudgeScoreService.findById(questionId);
			if (traditionalJudgeScore != null) {
				championshipParticipantTeams = traditionalJudgeScore.getChampionshipParticipantTeams();
				participantteamAsanas = traditionalJudgeScore.getParticipantTeamAsanas();
				participantTeamReferees = traditionalJudgeScore.getParticipantTeamReferees();
				questionType = traditionalJudgeScore.getQuestionType();
				round = traditionalJudgeScore.getRound();
				traditionalJudgeScore.setTotalScore(totalScore);
				traditionalJudgeScore.setStatus(true);
				traditionalJudgeScoreService.save(traditionalJudgeScore);
			}
		}

		ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore = participantTeamAsanaQuestionTotalScoreService
				.findByChampionshipParticipantTeamsAndParticipantTeamAsanasAndParticipantTeamRefereesAndRound(
						championshipParticipantTeams, participantteamAsanas, participantTeamReferees, round);
		if (participantTeamAsanaQuestionTotalScore != null) {
			participantTeamAsanaQuestionTotalScore.setTotalScore(totalScore);
			participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScore);
		} else {
			ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScoreNew = new ParticipantTeamAsanaQuestionTotalScore();
			participantTeamAsanaQuestionTotalScoreNew.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamAsanas(participantteamAsanas);
			participantTeamAsanaQuestionTotalScoreNew.setParticipantTeamReferees(participantTeamReferees);
			participantTeamAsanaQuestionTotalScoreNew.setQuestionType(questionType);
			participantTeamAsanaQuestionTotalScoreNew.setRound(round);
			participantTeamAsanaQuestionTotalScoreNew.setTotalScore(totalScore);
			participantTeamAsanaQuestionTotalScoreNew.setSequenceNumber(sequenceNumber);
			participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScoreNew);

		}

		return "success";
	}

}
