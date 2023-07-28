package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.AsanaLimit;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.EvaluatorJudgeScore;
import com.swayaan.nysf.entity.JudgeType;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanaJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamAsanaQuestionTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.QuestionTypeEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.TimeKeeperJudgeScore;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.service.ArtisticJudgeScoreService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.EvaluatorJudgeScoreService;
import com.swayaan.nysf.service.JudgeTypeService;
import com.swayaan.nysf.service.ParticipantTeamAsanaJudgeTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamAsanaQuestionTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamJudgeTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.TimeKeeperJudgeScoreService;

@RestController
@RequestMapping("/referee/calculate")
public class RefereeCalculateTeamScoresRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefereeCalculateTeamScoresRestController.class);

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int D_JUDGE_ROLE_ID = 2;
	private static final int A_JUDGE_ROLE_ID = 3;
	private static final int T_JUDGE_ROLE_ID = 4;
	private static final int E_JUDGE_ROLE_ID = 5;

	private static final int AS_AP_RP_TW_VALID_MAX_LIMIT = 190;
	private static final int AP_TW_VALID_MAX_LIMIT = 250;

	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ParticipantTeamAsanaJudgeTotalScoreService participantTeamAsanaJudgeTotalScoreService;
	@Autowired
	ParticipantTeamAsanaQuestionTotalScoreService participantTeamAsanaQuestionTotalScoreService;
	@Autowired
	ParticipantTeamJudgeTotalScoreService participantTeamJudgeTotalScoreService;
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	RoleService roleService;
	@Autowired
	ArtisticJudgeScoreService artisticJudgeScoreService;
	@Autowired
	TimeKeeperJudgeScoreService timeKeeperJudgeScoreService;
	@Autowired
	AsanaLimit asanaLimit;
	@Autowired
	EvaluatorJudgeScoreService evaluatorJudgeScoreService;
	@Autowired
	JudgeTypeService judgeTypeService;

	@PostMapping("/championshipTeam/{championshipParticipantTeamsId}/asana-judge-total")
	public String calculateAsanaJudgeTotalScores(Model model) throws EntityNotFoundException {
		return "redirect:referee/view-judge-scoring";
	}

	@PostMapping("/championshipTeam/{championshipParticipantTeamsId}/judge-total")
	public String calculateJudgeTotalScores(Model model) {
		return "redirect:referee/view-judge-scoring";
	}

	@PostMapping("/championshipTeam/{championshipParticipantTeamsId}/final-scores")
	public String calculateFinalScoresForTeam(Model model,
			@PathVariable(name = "championshipParticipantTeamsId") Integer championshipParticipantTeamsId)
			throws RoleNotFoundException {

		ChampionshipParticipantTeams championshipParticipantTeams;
		try {
			championshipParticipantTeams = championshipParticipantTeamsService.get(championshipParticipantTeamsId);
			ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
			Integer round = championshipParticipantTeams.getChampionshipRounds().getRound();

			Integer dJudgeId = D_JUDGE_ROLE_ID;
			JudgeType dJudgeRole = judgeTypeService.get(dJudgeId);
			Integer aJudgeId = A_JUDGE_ROLE_ID;
			JudgeType aJudgeRole = judgeTypeService.get(aJudgeId);
			Integer tJudgeId = T_JUDGE_ROLE_ID;
			JudgeType tJudgeRole = judgeTypeService.get(tJudgeId);

			if (participantTeam.getAsanaCategory().getId() == 1) { // traditional single
				// Get All D-Judges scores for the team and calculate average -
				// E-Judge penalty & T Judge also included
				calculateDJudgeTraditionalTeamScores(championshipParticipantTeams, round);
				// calculate final score
				calculateTraditionalTeamFinalScoreForRound(championshipParticipantTeams, round);

			} else if (participantTeam.getAsanaCategory().getId() == 2) { // artistic single
				// Get All D-Judges scores for the team and calculate average - E-Judge penalty
				// included
				calculateDJudgeArtisticSingleTeamScores(championshipParticipantTeams, round, dJudgeRole);
				// Get All A-Judges score for the team & calculate average
				calculateAJudgeTeamScoresCommon(championshipParticipantTeams, round, aJudgeRole);
				// Get All T-Judges score for the team & calculate average
				calculateTJudgeTeamScoresCommon(championshipParticipantTeams, round, tJudgeRole);
				// Calculate final score
				calculateTeamFinalScoreForRound(championshipParticipantTeams, round);
			} else if (participantTeam.getAsanaCategory().getId() == 3) { // artistic pair

				// Get All D-Judges scores for the team and calculate average
				calculateDJudgeArtisticPairTeamScores(championshipParticipantTeams, round, dJudgeRole);
				// Get All A-Judges score for the team & calculate average
				calculateAJudgeTeamScoresCommon(championshipParticipantTeams, round, aJudgeRole);
				// Get All T-Judges score for the team & calculate average
				calculateTJudgeTeamScoresCommon(championshipParticipantTeams, round, tJudgeRole);
				// Calculate final score
				calculateTeamFinalScoreForRound(championshipParticipantTeams, round);

			} else if (participantTeam.getAsanaCategory().getId() == 4) { // rhythmic pair
				// Get All D-Judges scores for the team and calculate average
				calculateDJudgeRhythmicPairTeamScores(championshipParticipantTeams, round, dJudgeRole);
				// Get All A-Judges score for the team & calculate average
				calculateAJudgeTeamScoresCommon(championshipParticipantTeams, round, aJudgeRole);
				// Get All T-Judges score for the team & calculate average
				calculateTJudgeTeamScoresCommon(championshipParticipantTeams, round, tJudgeRole);
				// Calculate final score
				calculateTeamFinalScoreForRound(championshipParticipantTeams, round);

			} else if (participantTeam.getAsanaCategory().getId() == 5) { // artistic group
				// Get All D-Judges scores for the team and calculate average
				calculateDJudgeArtisticGroupTeamScores(championshipParticipantTeams, round, dJudgeRole);
				// Get All A-Judges score for the team & calculate average
				calculateAJudgeTeamScoresCommon(championshipParticipantTeams, round, aJudgeRole);
				// Get All T-Judges score for the team & calculate average
				calculateTJudgeTeamScoresCommon(championshipParticipantTeams, round, tJudgeRole);
				// Calculate final score
				calculateTeamFinalScoreForRound(championshipParticipantTeams, round);

			}

			return "success";

		} catch (EntityNotFoundException e) {
			LOGGER.error("Team not found!");
			return "failure";

		}

		// return "redirect:referee/view-judge-scoring";

	}

	private void updateChampionshipParticipantTeamStatus(ChampionshipParticipantTeams championshipParticipantTeams) {
		// TODO Auto-generated method stub
		championshipParticipantTeams.setStatus(StatusEnum.COMPLETED);
		championshipParticipantTeamsService.save(championshipParticipantTeams);
	}

	private void calculateTraditionalTeamFinalScoreForRound(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round) throws RoleNotFoundException, EntityNotFoundException {

		Integer dJudgeId = D_JUDGE_ROLE_ID;
		JudgeType dJudgeRole = judgeTypeService.get(dJudgeId);
		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeRole = judgeTypeService.get(eJudgeId);

		float teamTotalScore = (float) 0.0;
		// get d judge score
		ParticipantTeamJudgeTotalScore dJudgeTotalScore = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		ParticipantTeamJudgeTotalScore eJudgeTotalPenalty = participantTeamJudgeTotalScoreService

				.getByTeamAndRoundAndType(championshipParticipantTeams, round, eJudgeRole);

		LOGGER.info("dJudgeTotalScore" + dJudgeTotalScore);
		LOGGER.info("eJudgeTotalPenalty" + eJudgeTotalPenalty);

		if (dJudgeTotalScore != null && eJudgeTotalPenalty != null) {
			teamTotalScore = dJudgeTotalScore.getTotalScore() - eJudgeTotalPenalty.getTotalScore();
		}
		LOGGER.info("teamTotalScore" + teamTotalScore);
		// save total to participant_team_round_total_scores table
		ParticipantTeamRoundTotalScoring existingParticipantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
				.findByChampionshipParticipantTeamAndChampionshipRounds(championshipParticipantTeams,
						championshipParticipantTeams.getChampionshipRounds());
		if (existingParticipantTeamRoundTotalScoring == null) {
			ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = new ParticipantTeamRoundTotalScoring();
			participantTeamRoundTotalScoring
					.setChampionship(championshipParticipantTeams.getParticipantTeam().getChampionship());
			participantTeamRoundTotalScoring.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamRoundTotalScoring
					.setChampionshipRounds(championshipParticipantTeams.getChampionshipRounds());
			participantTeamRoundTotalScoring.setTotalScore(teamTotalScore);
			participantTeamRoundTotalScoringService.save(participantTeamRoundTotalScoring);
		} else {
			existingParticipantTeamRoundTotalScoring.setTotalScore(teamTotalScore);
			participantTeamRoundTotalScoringService.save(existingParticipantTeamRoundTotalScoring);
		}

		// update Team performance status
		if (championshipParticipantTeams.getStatus() != StatusEnum.DISQUALIFIED) {
			updateChampionshipParticipantTeamStatus(championshipParticipantTeams);
		}

	}

	private void calculateTeamFinalScoreForRound(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round) throws RoleNotFoundException, EntityNotFoundException {

		// Other categories

		Integer dJudgeId = D_JUDGE_ROLE_ID;
		JudgeType dJudgeRole = judgeTypeService.get(dJudgeId);
		Integer aJudgeId = A_JUDGE_ROLE_ID;
		JudgeType aJudgeRole = judgeTypeService.get(aJudgeId);
		Integer tJudgeId = T_JUDGE_ROLE_ID;
		JudgeType tJudgeRole = judgeTypeService.get(tJudgeId);
		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeRole = judgeTypeService.get(eJudgeId);

		float teamTotalScore = (float) 0.0;
		// get d judge score
		ParticipantTeamJudgeTotalScore dJudgeTotalScore = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		ParticipantTeamJudgeTotalScore aJudgeTotalScore = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, aJudgeRole);
		ParticipantTeamJudgeTotalScore tJudgeTotalScore = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, tJudgeRole);
		ParticipantTeamJudgeTotalScore eJudgeTotalPenalty = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, eJudgeRole);

		if (dJudgeTotalScore != null && aJudgeTotalScore != null && tJudgeTotalScore != null
				&& eJudgeTotalPenalty != null) {
			teamTotalScore = dJudgeTotalScore.getTotalScore() + aJudgeTotalScore.getTotalScore()
					+ tJudgeTotalScore.getTotalScore() + -eJudgeTotalPenalty.getTotalScore();
		}

		// save total to participant_team_round_total_scores table
		ParticipantTeamRoundTotalScoring existingParticipantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
				.findByChampionshipParticipantTeamAndChampionshipRounds(championshipParticipantTeams,
						championshipParticipantTeams.getChampionshipRounds());
		if (existingParticipantTeamRoundTotalScoring == null) {
			ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = new ParticipantTeamRoundTotalScoring();
			participantTeamRoundTotalScoring
					.setChampionship(championshipParticipantTeams.getParticipantTeam().getChampionship());
			participantTeamRoundTotalScoring.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamRoundTotalScoring
					.setChampionshipRounds(championshipParticipantTeams.getChampionshipRounds());
			participantTeamRoundTotalScoring.setTotalScore(teamTotalScore);
			participantTeamRoundTotalScoringService.save(participantTeamRoundTotalScoring);
		} else {
			existingParticipantTeamRoundTotalScoring.setTotalScore(teamTotalScore);
			participantTeamRoundTotalScoringService.save(existingParticipantTeamRoundTotalScoring);
		}

		// update Team performance status
		if (championshipParticipantTeams.getStatus() != StatusEnum.DISQUALIFIED) {
			updateChampionshipParticipantTeamStatus(championshipParticipantTeams);
		}
	}

	// D-Judge & E-judge score calculation for Artistic Group
	private void calculateDJudgeArtisticGroupTeamScores(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType dJudgeRole) throws RoleNotFoundException, EntityNotFoundException {

		LOGGER.info("Entered calculateDJudgeRhythmicPairTeamScores method");

		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeType = judgeTypeService.get(eJudgeId);

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		ParticipantTeamReferees eJudgeTeamReferee = participantTeamRefereesService
				.getJudgeByTypeAndTeamAndRound(participantTeam, eJudgeType, round);

		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonAsanaQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesAndQuestionType(
						championshipParticipantTeams, round, eJudgeTeamReferee,
						QuestionTypeEnum.COMMONQUESTIONFORASANA);
		LOGGER.debug(
				"listEvalJudgeScoresForCommonAsanaQuestion size : " + listEvalJudgeScoresForCommonAsanaQuestion.size());

		List<Float> listEvalJudgePenalty = new ArrayList<>();

		for (EvaluatorJudgeScore evalJudgeScoresForCommonAsanaQuestion : listEvalJudgeScoresForCommonAsanaQuestion) {
			Integer asanaSeqNum = evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber();
			LOGGER.debug("asanaSeqNum :" + asanaSeqNum);
			// List<Float> listDJudgeAndTjudgeScoreForAsana = new ArrayList<>();
			if (evalJudgeScoresForCommonAsanaQuestion.getIsPerformed().toString() == "PERFORMED") { // if performed

				// get the same sequence asana from ParticipantTeamAsanaQuestionTotalScore for
				// djudge
				List<ParticipantTeamAsanaQuestionTotalScore> listDJudgeScoreForAsanaSeqNum = participantTeamAsanaQuestionTotalScoreService
						.getAllBySequenceNumberAndTeamAndRoundAndRefereeType(asanaSeqNum, championshipParticipantTeams,
								round, dJudgeRole);
				LOGGER.debug(
						"listDJudgeScoreForAsanaSeqNum : " + Arrays.toString(listDJudgeScoreForAsanaSeqNum.toArray()));
				System.out.println(
						"listDJudgeScoreForAsanaSeqNum : " + Arrays.toString(listDJudgeScoreForAsanaSeqNum.toArray()));
				List<Float> listTotalScoresGiven = new ArrayList<>();
				for (ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore : listDJudgeScoreForAsanaSeqNum) {
					Float totalScoresGiven = participantTeamAsanaQuestionTotalScore.getTotalScore();
					listTotalScoresGiven.add(totalScoresGiven);
				}

				LOGGER.debug("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
				System.out.println("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
				if (listTotalScoresGiven.size() != 0) {
					List<Float> listTotalScoresGivenAfterElimination = eliminateHighAndLowJudgeScoresForEachAsana(
							listTotalScoresGiven);

					// get average
					float totalDjudgeAverageScore = getAverageForListofScores(listTotalScoresGivenAfterElimination);
					System.out.println("totalDjudgeAverageScore: " + totalDjudgeAverageScore);

					// save to ParticipantTeamAsanaJudgeTotalScore table, if record found , then
					// update else save new record
					ParticipantTeamAsanaJudgeTotalScore asanaJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
							.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams, asanaSeqNum, round,
									dJudgeRole);
					if (asanaJudgeTotalScores == null) {
						ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
						participantTeamAsanaJudgeTotalScore
								.setChampionshipParticipantTeams(championshipParticipantTeams);
						participantTeamAsanaJudgeTotalScore.setParticipantTeamAsanas(
								evalJudgeScoresForCommonAsanaQuestion.getParticipantTeamAsanas());
						participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
						participantTeamAsanaJudgeTotalScore.setRound(round);
						participantTeamAsanaJudgeTotalScore
								.setSequenceNumber(evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber());
						participantTeamAsanaJudgeTotalScore.setTotalScore(totalDjudgeAverageScore);
						participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
					} else {
						asanaJudgeTotalScores.setTotalScore(totalDjudgeAverageScore);
						participantTeamAsanaJudgeTotalScoreService.save(asanaJudgeTotalScores);
					}

				}

				if (evalJudgeScoresForCommonAsanaQuestion.getAsPerSequence().toString() == "DIDNOTPERFORMINSEQUENCE") {
					// djudge marks is saved above
					// add penalties to list
					listEvalJudgePenalty.add(evalJudgeScoresForCommonAsanaQuestion.getPenaltyScore());
					LOGGER.debug("listEvalJudgePenalty : " + Arrays.toString(listEvalJudgePenalty.toArray()));
				}
			} else { // asana not performed

				System.out.println("is performed status :false");
				// make djudge score for that asanaseqNum as zero, save it to last second table
				ParticipantTeamAsanaJudgeTotalScore asanaDJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
						.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams, asanaSeqNum, round,
								dJudgeRole);
				if (asanaDJudgeTotalScores == null) {
					ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
					participantTeamAsanaJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					participantTeamAsanaJudgeTotalScore
							.setParticipantTeamAsanas(evalJudgeScoresForCommonAsanaQuestion.getParticipantTeamAsanas());
					participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
					participantTeamAsanaJudgeTotalScore.setRound(round);
					participantTeamAsanaJudgeTotalScore
							.setSequenceNumber(evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber());
					participantTeamAsanaJudgeTotalScore.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
				} else {
					asanaDJudgeTotalScores.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(asanaDJudgeTotalScores);
				}
			}

		}

		// get the Djudge total from above table
		Float totalDjudgeScoresForTeam = participantTeamAsanaJudgeTotalScoreService
				.getSumByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		LOGGER.debug("totalDjudgeScoresForTeam : " + totalDjudgeScoresForTeam);

		ParticipantTeamJudgeTotalScore judgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		if (judgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(dJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			judgeTotalScores.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(judgeTotalScores);
		}

		// Save Penalty for not covering all categories
		Float penaltyNotCoveringAllCategories = (float) 0.0;
		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonTeamQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesAndQuestionType(
						championshipParticipantTeams, round, eJudgeTeamReferee, QuestionTypeEnum.COMMONTEAMQUESTION);
		if (listEvalJudgeScoresForCommonTeamQuestion.size() != 0) { // 1 record
			for (EvaluatorJudgeScore judgeScoresForCommonTeamQuestion : listEvalJudgeScoresForCommonTeamQuestion) {
				if (judgeScoresForCommonTeamQuestion.getPenaltyScore() != null) {
					penaltyNotCoveringAllCategories += judgeScoresForCommonTeamQuestion.getPenaltyScore();
				}
			}
		}

		// save penalty total for not performing in sequence
		Float totalEJudgePenaltyScoreSum = (float) 0.0;
		if (listEvalJudgePenalty.size() != 0) {
			for (Float num : listEvalJudgePenalty)
				totalEJudgePenaltyScoreSum += num;
		}

		if (listEvalJudgePenalty.size() > 3) { // if more than 3 penalties
			// update the team status to disqualified
			championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
			championshipParticipantTeamsService.save(championshipParticipantTeams);
		}

		Float eJudgePenaltySum = (float) 0.0;
		LOGGER.debug("totalEJudgePenaltyScoreSum : " + totalEJudgePenaltyScoreSum);
		if (penaltyNotCoveringAllCategories != null || penaltyNotCoveringAllCategories != 0) {
			eJudgePenaltySum = penaltyNotCoveringAllCategories + totalEJudgePenaltyScoreSum;
		}
		// check if record already exists
		ParticipantTeamJudgeTotalScore evaljudgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, eJudgeType);
		if (evaljudgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(eJudgeType);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(eJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			evaljudgeTotalScores.setTotalScore(eJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(evaljudgeTotalScores);
		}

	}

	// D-Judge & E-judge score calculation for Rhythmic Pair
	private void calculateDJudgeRhythmicPairTeamScores(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType dJudgeRole) throws RoleNotFoundException, EntityNotFoundException {

		LOGGER.info("Entered calculateDJudgeRhythmicPairTeamScores method");

		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeRole = judgeTypeService.get(eJudgeId);

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		ParticipantTeamReferees eJudgeTeamReferee = participantTeamRefereesService
				.getJudgeByTypeAndTeamAndRound(participantTeam, eJudgeRole, round);

		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonAsanaQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesAndQuestionType(
						championshipParticipantTeams, round, eJudgeTeamReferee,
						QuestionTypeEnum.COMMONQUESTIONFORASANA);
		LOGGER.debug(
				"listEvalJudgeScoresForCommonAsanaQuestion size : " + listEvalJudgeScoresForCommonAsanaQuestion.size());

		List<Float> listEvalJudgePenalty = new ArrayList<>();

		for (EvaluatorJudgeScore evalJudgeScoresForCommonAsanaQuestion : listEvalJudgeScoresForCommonAsanaQuestion) {
			Integer asanaSeqNum = evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber();
			LOGGER.debug("asanaSeqNum :" + asanaSeqNum);
			// List<Float> listDJudgeAndTjudgeScoreForAsana = new ArrayList<>();
			if (evalJudgeScoresForCommonAsanaQuestion.getIsPerformed().toString() == "PERFORMED") { // if performed

				// get the same sequence asana from ParticipantTeamAsanaQuestionTotalScore for
				// djudge
				List<ParticipantTeamAsanaQuestionTotalScore> listDJudgeScoreForAsanaSeqNum = participantTeamAsanaQuestionTotalScoreService
						.getAllBySequenceNumberAndTeamAndRoundAndRefereeType(asanaSeqNum, championshipParticipantTeams,
								round, dJudgeRole);
				LOGGER.debug(
						"listDJudgeScoreForAsanaSeqNum : " + Arrays.toString(listDJudgeScoreForAsanaSeqNum.toArray()));
				System.out.println(
						"listDJudgeScoreForAsanaSeqNum : " + Arrays.toString(listDJudgeScoreForAsanaSeqNum.toArray()));
				List<Float> listTotalScoresGiven = new ArrayList<>();
				for (ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore : listDJudgeScoreForAsanaSeqNum) {
					Float totalScoresGiven = participantTeamAsanaQuestionTotalScore.getTotalScore();
					listTotalScoresGiven.add(totalScoresGiven);
				}

				LOGGER.debug("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
				System.out.println("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
				if (listTotalScoresGiven.size() != 0) {
					List<Float> listTotalScoresGivenAfterElimination = eliminateHighAndLowJudgeScoresForEachAsana(
							listTotalScoresGiven);

					// get average
					float totalDjudgeAverageScore = getAverageForListofScores(listTotalScoresGivenAfterElimination);
					System.out.println("totalDjudgeAverageScore: " + totalDjudgeAverageScore);

					// save to ParticipantTeamAsanaJudgeTotalScore table, if record found , then
					// update else save new record
					ParticipantTeamAsanaJudgeTotalScore asanaJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
							.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams, asanaSeqNum, round,
									dJudgeRole);
					if (asanaJudgeTotalScores == null) {
						ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
						participantTeamAsanaJudgeTotalScore
								.setChampionshipParticipantTeams(championshipParticipantTeams);
						participantTeamAsanaJudgeTotalScore.setParticipantTeamAsanas(
								evalJudgeScoresForCommonAsanaQuestion.getParticipantTeamAsanas());
						participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
						participantTeamAsanaJudgeTotalScore.setRound(round);
						participantTeamAsanaJudgeTotalScore
								.setSequenceNumber(evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber());
						participantTeamAsanaJudgeTotalScore.setTotalScore(totalDjudgeAverageScore);
						participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
					} else {
						asanaJudgeTotalScores.setTotalScore(totalDjudgeAverageScore);
						participantTeamAsanaJudgeTotalScoreService.save(asanaJudgeTotalScores);
					}

				}

				if (evalJudgeScoresForCommonAsanaQuestion.getAsPerSequence().toString() == "DIDNOTPERFORMINSEQUENCE") {
					// djudge marks is saved above
					// add penalties to list
					listEvalJudgePenalty.add(evalJudgeScoresForCommonAsanaQuestion.getPenaltyScore());
					LOGGER.debug("listEvalJudgePenalty : " + Arrays.toString(listEvalJudgePenalty.toArray()));
				}
			} else { // asana not performed

				System.out.println("is performed status :false");
				// make djudge score for that asanaseqNum as zero, save it to last second table
				ParticipantTeamAsanaJudgeTotalScore asanaDJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
						.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams, asanaSeqNum, round,
								dJudgeRole);
				if (asanaDJudgeTotalScores == null) {
					ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
					participantTeamAsanaJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					participantTeamAsanaJudgeTotalScore
							.setParticipantTeamAsanas(evalJudgeScoresForCommonAsanaQuestion.getParticipantTeamAsanas());
					participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
					participantTeamAsanaJudgeTotalScore.setRound(round);
					participantTeamAsanaJudgeTotalScore
							.setSequenceNumber(evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber());
					participantTeamAsanaJudgeTotalScore.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
				} else {
					asanaDJudgeTotalScores.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(asanaDJudgeTotalScores);
				}
			}

		}

		// get the Djudge total from above table
		Float totalDjudgeScoresForTeam = participantTeamAsanaJudgeTotalScoreService
				.getSumByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		LOGGER.debug("totalDjudgeScoresForTeam : " + totalDjudgeScoresForTeam);

		ParticipantTeamJudgeTotalScore judgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		if (judgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(dJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			judgeTotalScores.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(judgeTotalScores);
		}

		// Save Penalty for not covering all categories
		Float penaltyNotCoveringAllCategories = (float) 0.0;
		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonTeamQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesAndQuestionType(
						championshipParticipantTeams, round, eJudgeTeamReferee, QuestionTypeEnum.COMMONTEAMQUESTION);
		if (listEvalJudgeScoresForCommonTeamQuestion.size() != 0) { // 1 record
			for (EvaluatorJudgeScore judgeScoresForCommonTeamQuestion : listEvalJudgeScoresForCommonTeamQuestion) {
				if (judgeScoresForCommonTeamQuestion.getPenaltyScore() != null) {
					penaltyNotCoveringAllCategories += judgeScoresForCommonTeamQuestion.getPenaltyScore();
				}
			}
		}

		// save penalty total for not performing in sequence
		Float totalEJudgePenaltyScoreSum = (float) 0.0;
		if (listEvalJudgePenalty.size() != 0) {
			for (Float num : listEvalJudgePenalty)
				totalEJudgePenaltyScoreSum += num;
		}

		if (listEvalJudgePenalty.size() > 3) { // if more than 3 penalties
			// update the team status to disqualified
			championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
			championshipParticipantTeamsService.save(championshipParticipantTeams);
		}

		Float eJudgePenaltySum = (float) 0.0;
		LOGGER.debug("totalEJudgePenaltyScoreSum : " + totalEJudgePenaltyScoreSum);
		if (penaltyNotCoveringAllCategories != null || penaltyNotCoveringAllCategories != 0) {
			eJudgePenaltySum = penaltyNotCoveringAllCategories + totalEJudgePenaltyScoreSum;
		} else {
			eJudgePenaltySum = totalEJudgePenaltyScoreSum;
		}
		// check if record already exists
		ParticipantTeamJudgeTotalScore evaljudgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, eJudgeRole);
		if (evaljudgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(eJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(eJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			evaljudgeTotalScores.setTotalScore(eJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(evaljudgeTotalScores);
		}

	}

	// D-Judge & E-judge score calculation for Artistic Pair
	private void calculateDJudgeArtisticPairTeamScores(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType dJudgeRole) throws RoleNotFoundException, EntityNotFoundException {

		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeRole = judgeTypeService.get(eJudgeId);

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		List<ParticipantTeamReferees> listEJudgeTeamReferees = participantTeamRefereesService
				.getAllJudgesofTypeAndParticipantTeamAndRound(participantTeam, eJudgeRole, round);

		Integer totalAsanaLimit = -1; // get Asana limit for category
		if (participantTeam.getAsanaCategory().getId() == 3) {
			totalAsanaLimit = asanaLimit.getArtisticPairLimit();
		}
		LinkedHashMap<Integer, List<Float>> asanaSeqPenaltyScoresMap = new LinkedHashMap<>();

		for (Integer i = 1; i <= totalAsanaLimit; i++) {

			List<Float> listTotalScoresGiven = new ArrayList<>();
			List<ParticipantTeamAsanaQuestionTotalScore> listAsanaQuestionScores = participantTeamAsanaQuestionTotalScoreService
					.getAllBySequenceNumberAndTeamAndRoundAndRefereeType(i, championshipParticipantTeams, round,
							dJudgeRole);

			for (ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore : listAsanaQuestionScores) {
				Float totalScoresGiven = participantTeamAsanaQuestionTotalScore.getTotalScore();
				listTotalScoresGiven.add(totalScoresGiven);
			}

			LOGGER.debug("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
			System.out.println("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));

			if (listTotalScoresGiven.size() != 0) {
				List<Float> listTotalScoresGivenAfterElimination = eliminateHighAndLowJudgeScoresForEachAsana(
						listTotalScoresGiven);
				// get average
				float totalDjudgeAverageScore = getAverageForListofScores(listTotalScoresGivenAfterElimination);
				System.out.println("totalDjudgeAverageScore: " + totalDjudgeAverageScore);

				// save to ParticipantTeamAsanaJudgeTotalScore, if record found update else save
				// new record
				ParticipantTeamAsanaJudgeTotalScore asanaJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
						.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams, i, round, dJudgeRole);
				if (asanaJudgeTotalScores == null) {
					ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
					participantTeamAsanaJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					participantTeamAsanaJudgeTotalScore.setSequenceNumber(i);
					participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
					participantTeamAsanaJudgeTotalScore.setRound(round);
					participantTeamAsanaJudgeTotalScore.setTotalScore(totalDjudgeAverageScore);
					participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
				} else {
					asanaJudgeTotalScores.setTotalScore(totalDjudgeAverageScore);
					participantTeamAsanaJudgeTotalScoreService.save(asanaJudgeTotalScores);
				}
			}

			// E judge check is_performed and as_per_sequence fields for both the evaluators
			List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonAsanaQuestion = evaluatorJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndRoundAndQuestionTypeAndSequenceNumber(
							championshipParticipantTeams, round, QuestionTypeEnum.COMMONQUESTIONFORASANA, i);
			if (listEvalJudgeScoresForCommonAsanaQuestion.size() != 0) {
				if (listEvalJudgeScoresForCommonAsanaQuestion.size() == 2) { // having 2 records

					EvaluatorJudgeScore evalObj1 = listEvalJudgeScoresForCommonAsanaQuestion.get(0);
					EvaluatorJudgeScore evalObj2 = listEvalJudgeScoresForCommonAsanaQuestion.get(1);
					if (evalObj1 != null && evalObj2 != null) {
						if ((evalObj1.getIsPerformed().toString() == "NOTPERFORMED")
								|| (evalObj2.getIsPerformed().toString() == "NOTPERFORMED")) {
							Integer nonPerformedSeqNum = evalObj1.getSequenceNumber();
							// update djudge score for that asana seq number to 0
							ParticipantTeamAsanaJudgeTotalScore asanaJudgeTotalScores1 = participantTeamAsanaJudgeTotalScoreService
									.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams,
											nonPerformedSeqNum, round, dJudgeRole);
							asanaJudgeTotalScores1.setTotalScore((float) 0.0);
							participantTeamAsanaJudgeTotalScoreService.save(asanaJudgeTotalScores1);
						}
					}
				} else if (listEvalJudgeScoresForCommonAsanaQuestion.size() == 1) {
					EvaluatorJudgeScore evalObj1 = listEvalJudgeScoresForCommonAsanaQuestion.get(0);
					if (evalObj1 != null) {
						if ((evalObj1.getIsPerformed().toString() == "NOTPERFORMED")) {
							Integer nonPerformedSeqNum = evalObj1.getSequenceNumber();
							// update djudge score for that asana seq number to 0
							ParticipantTeamAsanaJudgeTotalScore asanaJudgeTotalScores1 = participantTeamAsanaJudgeTotalScoreService
									.getByTeamAndSequenceNumberAndRoundAndType(championshipParticipantTeams,
											nonPerformedSeqNum, round, dJudgeRole);
							asanaJudgeTotalScores1.setTotalScore((float) 0.0);
							participantTeamAsanaJudgeTotalScoreService.save(asanaJudgeTotalScores1);
						}
					}
				}
			}

			// get the judge scores by asana seq num
			List<Float> listEvalJudgesPenaltyScores = new ArrayList<>();
			List<EvaluatorJudgeScore> listEvalJudgeScoresForPenalties = evaluatorJudgeScoreService
					.findAllByChampionshipParticipantTeamsAndSequenceNumberAndQuestionType(championshipParticipantTeams,
							i, QuestionTypeEnum.COMMONQUESTIONFORASANA);
			for (EvaluatorJudgeScore evaluatorJudgeScore : listEvalJudgeScoresForPenalties) {
				if (evaluatorJudgeScore.getPenaltyScore() != null) {
					listEvalJudgesPenaltyScores.add(evaluatorJudgeScore.getPenaltyScore());
				} else {
					listEvalJudgesPenaltyScores.add((float) 0);
				}

			}
			asanaSeqPenaltyScoresMap.put(i, listEvalJudgesPenaltyScores);
		} // loop ends

		List<Float> listEjudgePenalties = new ArrayList<Float>();
		// loop through hashmap to get the penalty scores of judges
		for (Entry<Integer, List<Float>> penaltyScore : asanaSeqPenaltyScoresMap.entrySet()) {
			Integer key = penaltyScore.getKey();
			List<Float> penaltyScoreList = penaltyScore.getValue();
			if (penaltyScoreList.size() == 2) {
				if ((penaltyScoreList.get(0) == (float) 1) && (penaltyScoreList.get(1) == (float) 1)) {
					listEjudgePenalties.add((float) 1);
				} else if ((penaltyScoreList.get(0) == (float) 0) && (penaltyScoreList.get(1) == (float) 1)) {
					listEjudgePenalties.add((float) 1);
				} else if ((penaltyScoreList.get(0) == (float) 1) && (penaltyScoreList.get(1) == (float) 0)) {
					listEjudgePenalties.add((float) 1);
				}
			}
		}

		// Save Penalty for not covering all categories
		Float penaltyNotCoveringAllCategories = (float) 0.0;
		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonTeamQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndQuestionType(championshipParticipantTeams, round,
						QuestionTypeEnum.COMMONTEAMQUESTION);
		if (listEvalJudgeScoresForCommonTeamQuestion.size() != 0) { // 1 record
			for (EvaluatorJudgeScore judgeScoresForCommonTeamQuestion : listEvalJudgeScoresForCommonTeamQuestion) {
				if (judgeScoresForCommonTeamQuestion.getPenaltyScore() != null) {
					penaltyNotCoveringAllCategories += judgeScoresForCommonTeamQuestion.getPenaltyScore();
				}
				System.out.println("penaltyNotCoveringAllCategories : " + penaltyNotCoveringAllCategories);
			}
		}

		Float totalEJudgeAsanaPenaltySum = (float) 0.0;
		if (listEjudgePenalties.size() != 0) {
			for (Float num : listEjudgePenalties)
				totalEJudgeAsanaPenaltySum += num;
		}

		Float totalEJudgePenaltySum = (float) 0.0;
		if (penaltyNotCoveringAllCategories != null || penaltyNotCoveringAllCategories != 0) {
			totalEJudgePenaltySum = penaltyNotCoveringAllCategories + totalEJudgeAsanaPenaltySum;
			System.out.println("penaltyNotCoveringAllCategories : " + penaltyNotCoveringAllCategories);
		}

		// check if record already exists
		ParticipantTeamJudgeTotalScore evaljudgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, eJudgeRole);
		if (evaljudgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(eJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalEJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			evaljudgeTotalScores.setTotalScore(totalEJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(evaljudgeTotalScores);
		}

		// save djudge total score for team
		Float totalDjudgeScoresForTeam = participantTeamAsanaJudgeTotalScoreService
				.getSumByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		ParticipantTeamJudgeTotalScore judgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, dJudgeRole);
		if (judgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(dJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			judgeTotalScores.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(judgeTotalScores);
		}

		if (listEjudgePenalties.size() > 3) { // if more than 3 penalties
			// update the team status to disqualified
			championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
			championshipParticipantTeamsService.save(championshipParticipantTeams);
		}

	}

	// D-Judge & E-judge score calculation for Artistic single
	private void calculateDJudgeArtisticSingleTeamScores(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType dJudgeRole) throws RoleNotFoundException, EntityNotFoundException {

		LOGGER.info("Entered calculateDJudgeArtisticSingleTeamScores method");

		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeRole = judgeTypeService.get(eJudgeId);

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		ParticipantTeamParticipants artSingleTeamParticipant = participantTeamParticipantsService
				.getByTeam(participantTeam);
		ParticipantTeamReferees eJudgeTeamReferee = participantTeamRefereesService
				.getJudgeByTypeAndTeamAndRound(participantTeam, eJudgeRole, round);

		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonAsanaQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesAndQuestionType(
						championshipParticipantTeams, round, eJudgeTeamReferee,
						QuestionTypeEnum.COMMONQUESTIONFORASANA);
		LOGGER.debug(
				"listEvalJudgeScoresForCommonAsanaQuestion size : " + listEvalJudgeScoresForCommonAsanaQuestion.size());

		List<Float> listEvalJudgePenalty = new ArrayList<>();

		for (EvaluatorJudgeScore evalJudgeScoresForCommonAsanaQuestion : listEvalJudgeScoresForCommonAsanaQuestion) {
			Integer asanaSeqNum = evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber();
			LOGGER.debug("asanaSeqNum :" + asanaSeqNum);
			// List<Float> listDJudgeAndTjudgeScoreForAsana = new ArrayList<>();
			if (evalJudgeScoresForCommonAsanaQuestion.getIsPerformed().toString() == "PERFORMED") { // if performed

				// get the same sequence asana from ParticipantTeamAsanaQuestionTotalScore for
				// djudge
				List<ParticipantTeamAsanaQuestionTotalScore> listDJudgeScoreForAsanaSeqNum = participantTeamAsanaQuestionTotalScoreService
						.getAllBySequenceNumberAndTeamAndRoundAndRefereeType(asanaSeqNum, championshipParticipantTeams,
								round, dJudgeRole);
				LOGGER.debug(
						"listDJudgeScoreForAsanaSeqNum : " + Arrays.toString(listDJudgeScoreForAsanaSeqNum.toArray()));

				List<Float> listTotalScoresGiven = new ArrayList<>();
				for (ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore : listDJudgeScoreForAsanaSeqNum) {
					Float totalScoresGiven = participantTeamAsanaQuestionTotalScore.getTotalScore();
					listTotalScoresGiven.add(totalScoresGiven);
				}

				LOGGER.debug("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
				System.out.println("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
				if (listTotalScoresGiven.size() != 0) {
					List<Float> listTotalScoresGivenAfterElimination = eliminateHighAndLowJudgeScoresForEachAsana(
							listTotalScoresGiven);

					// get average
					float totalDjudgeAverageScore = getAverageForListofScores(listTotalScoresGivenAfterElimination);
					System.out.println("totalDjudgeAverageScore: " + totalDjudgeAverageScore);

					// save asana wise Djudge total score to ParticipantTeamAsanaJudgeTotalScore.
					ParticipantTeamAsanaJudgeTotalScore asanaJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
							.getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType(championshipParticipantTeams,
									asanaSeqNum, artSingleTeamParticipant, round, dJudgeRole);
					if (asanaJudgeTotalScores == null) {
						ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
						participantTeamAsanaJudgeTotalScore
								.setChampionshipParticipantTeams(championshipParticipantTeams);
						participantTeamAsanaJudgeTotalScore.setParticipantTeamAsanas(
								evalJudgeScoresForCommonAsanaQuestion.getParticipantTeamAsanas());
						participantTeamAsanaJudgeTotalScore.setParticipantTeamParticipants(artSingleTeamParticipant);
						participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
						participantTeamAsanaJudgeTotalScore.setRound(round);
						participantTeamAsanaJudgeTotalScore
								.setSequenceNumber(evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber());
						participantTeamAsanaJudgeTotalScore.setTotalScore(totalDjudgeAverageScore);
						participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
					} else {
						asanaJudgeTotalScores.setTotalScore(totalDjudgeAverageScore);
						participantTeamAsanaJudgeTotalScoreService.save(asanaJudgeTotalScores);
					}

				}

				if (evalJudgeScoresForCommonAsanaQuestion.getAsPerSequence().toString() == "DIDNOTPERFORMINSEQUENCE") {
					// djudge marks is saved above
					// add penalties to list
					listEvalJudgePenalty.add(evalJudgeScoresForCommonAsanaQuestion.getPenaltyScore());
					System.out.println("listEvalJudgePenalty : " + Arrays.toString(listEvalJudgePenalty.toArray()));
				}
			} else { // asana not performed

				System.out.println("is performed status :false");
				// make djudge score for that asanaseqNum as zero, then save it to last second
				// table.
				ParticipantTeamAsanaJudgeTotalScore asanaDJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
						.getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType(championshipParticipantTeams, asanaSeqNum,
								artSingleTeamParticipant, round, dJudgeRole);
				if (asanaDJudgeTotalScores == null) {
					ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
					participantTeamAsanaJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					participantTeamAsanaJudgeTotalScore
							.setParticipantTeamAsanas(evalJudgeScoresForCommonAsanaQuestion.getParticipantTeamAsanas());
					participantTeamAsanaJudgeTotalScore.setParticipantTeamParticipants(artSingleTeamParticipant);
					participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
					participantTeamAsanaJudgeTotalScore.setRound(round);
					participantTeamAsanaJudgeTotalScore
							.setSequenceNumber(evalJudgeScoresForCommonAsanaQuestion.getSequenceNumber());
					participantTeamAsanaJudgeTotalScore.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
				} else {
					asanaDJudgeTotalScores.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(asanaDJudgeTotalScores);
				}
			}

		}

		// get the Djudge total from above table
		Float totalDjudgeScoresForTeam = participantTeamAsanaJudgeTotalScoreService
				.getSumByTeamAndParticipantAndRoundAndType(championshipParticipantTeams, artSingleTeamParticipant,
						round, dJudgeRole);
		LOGGER.debug("totalDjudgeScoresForTeam : " + totalDjudgeScoresForTeam);

		ParticipantTeamJudgeTotalScore judgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndParticipantAndRoundAndType(championshipParticipantTeams, artSingleTeamParticipant, round,
						dJudgeRole);
		if (judgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setParticipantTeamParticipants(artSingleTeamParticipant);
			participantTeamJudgeTotalScore.setType(dJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			judgeTotalScores.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(judgeTotalScores);
		}

		// Save Penalty for not covering all categories
		Float penaltyNotCoveringAllCategories = (float) 0.0;
		List<EvaluatorJudgeScore> listEvalJudgeScoresForCommonTeamQuestion = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamRefereesAndQuestionType(
						championshipParticipantTeams, round, eJudgeTeamReferee, QuestionTypeEnum.COMMONTEAMQUESTION);
		if (listEvalJudgeScoresForCommonTeamQuestion.size() != 0) { // 1 record
			for (EvaluatorJudgeScore judgeScoresForCommonTeamQuestion : listEvalJudgeScoresForCommonTeamQuestion) {
				if (judgeScoresForCommonTeamQuestion.getPenaltyScore() != null) {
					penaltyNotCoveringAllCategories += judgeScoresForCommonTeamQuestion.getPenaltyScore();
				}

				System.out.println("penaltyNotCoveringAllCategories : " + penaltyNotCoveringAllCategories);
			}
		}

		// save penalty total for not performing in sequence
		Float totalEJudgePenaltyScoreSum = (float) 0.0;
		if (listEvalJudgePenalty.size() != 0) {
			for (Float num : listEvalJudgePenalty) {
				totalEJudgePenaltyScoreSum += num;
			}
		}
		if (listEvalJudgePenalty.size() > 3) { // if more than 3 penalties
			// update the team status to disqualified
			championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
			championshipParticipantTeamsService.save(championshipParticipantTeams);
		}

		Float eJudgePenaltySum = (float) 0.0;
		LOGGER.debug("totalEJudgePenaltyScoreSum : " + totalEJudgePenaltyScoreSum);
		if (penaltyNotCoveringAllCategories != null || penaltyNotCoveringAllCategories != 0) {
			eJudgePenaltySum = penaltyNotCoveringAllCategories + totalEJudgePenaltyScoreSum;
			System.out.println("penaltyNotCoveringAllCategories : " + penaltyNotCoveringAllCategories);
		}
		// save ejudge total penalty to ParticipantTeamJudgeTotalScore
		ParticipantTeamJudgeTotalScore evaljudgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, eJudgeRole);
		if (evaljudgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setParticipantTeamParticipants(artSingleTeamParticipant);
			participantTeamJudgeTotalScore.setType(eJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(eJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			evaljudgeTotalScores.setTotalScore(eJudgePenaltySum);
			participantTeamJudgeTotalScoreService.save(evaljudgeTotalScores);
		}

	}

	// D-Judge & E-judge score calculation for Traditional single

	// D-Judge & E-judge score calculation for traditional single
	private void calculateDJudgeTraditionalTeamScores(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round) throws RoleNotFoundException, EntityNotFoundException {

		Integer dJudgeId = D_JUDGE_ROLE_ID;
		;
		JudgeType dJudgeRole = judgeTypeService.get(dJudgeId);
		Integer tJudgeId = T_JUDGE_ROLE_ID;
		JudgeType tJudgeRole = judgeTypeService.get(tJudgeId);
		Integer eJudgeId = E_JUDGE_ROLE_ID;
		JudgeType eJudgeRole = judgeTypeService.get(eJudgeId);

		LOGGER.info("Entered calculateDJudgeTraditionalTeamScores method");

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		ParticipantTeamParticipants tradTeamParticipant = participantTeamParticipantsService.getByTeam(participantTeam);

		ParticipantTeamReferees tJudgeTeamReferee = participantTeamRefereesService
				.getJudgeByTypeAndTeamAndRound(participantTeam, tJudgeRole, round);
		ParticipantTeamReferees eJudgeTeamReferee = participantTeamRefereesService
				.getJudgeByTypeAndTeamAndRound(participantTeam, eJudgeRole, round);

//		List<TimeKeeperJudgeScore> listTimeKeeperScores = timeKeeperJudgeScoreService
//				.getAllByTeamAndRoundAndReferee(championshipParticipantTeams, round, tJudgeTeamReferee);
//		LOGGER.debug("listTimeKeeperScores size : " + listTimeKeeperScores.size());

		List<EvaluatorJudgeScore> listEvaluatorJudgeScores = evaluatorJudgeScoreService
				.findAllByChampionshipParticipantTeamsAndRoundAndParticipantTeamReferees(championshipParticipantTeams,
						round, eJudgeTeamReferee);
		LOGGER.debug("listEvaluatorJudgeScores size : " + listEvaluatorJudgeScores.size());

		List<Float> listEvalJudgePenalty = new ArrayList<>();

		for (EvaluatorJudgeScore evaluatorJudgeScore : listEvaluatorJudgeScores) {

			Integer asanaSeqNum = evaluatorJudgeScore.getSequenceNumber();
			LOGGER.debug("asanaSeqNum :" + asanaSeqNum);
			List<Float> listDJudgeAndTjudgeScoreForAsana = new ArrayList<>();
			if (evaluatorJudgeScore.getIsPerformed().toString() == "PERFORMED") {

				TimeKeeperJudgeScore tJudgeScoreForAsanaSeqNum = timeKeeperJudgeScoreService
						.getBySequenceNumberAndTeamAndRound(asanaSeqNum, championshipParticipantTeams, round);

				// get same sequence asana from ParticipantTeamAsanaQuestionTotalScore for
				// djudge
				List<ParticipantTeamAsanaQuestionTotalScore> listDJudgeScoreForAsanaSeqNum = participantTeamAsanaQuestionTotalScoreService
						.getAllBySequenceNumberAndTeamAndRoundAndRefereeType(asanaSeqNum, championshipParticipantTeams,
								round, dJudgeRole);
				LOGGER.debug(
						"listDJudgeScoreForAsanaSeqNum : " + Arrays.toString(listDJudgeScoreForAsanaSeqNum.toArray()));

				// get d judge and t judge marks, save it to last second table
				for (ParticipantTeamAsanaQuestionTotalScore eachDJudgeScoreForAsanaSeqNum : listDJudgeScoreForAsanaSeqNum) {
					listDJudgeAndTjudgeScoreForAsana.add(
							tJudgeScoreForAsanaSeqNum.getTotalScore() + eachDJudgeScoreForAsanaSeqNum.getTotalScore());
				}

				if (listDJudgeAndTjudgeScoreForAsana.size() != 0) { // 5 records
					List<Float> listTotalScoresGivenAfterElimination = eliminateHighAndLowJudgeScoresForEachAsana(
							listDJudgeAndTjudgeScoreForAsana);

					// get average
					float totalDjudgeAverageScore = getAverageForListofScores(listTotalScoresGivenAfterElimination);
					LOGGER.debug("totalDjudgeAverageScore: " + totalDjudgeAverageScore);

					// save to ParticipantTeamAsanaJudgeTotalScore table
					ParticipantTeamAsanaJudgeTotalScore asanaDJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
							.getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType(championshipParticipantTeams,
									asanaSeqNum, tradTeamParticipant, round, dJudgeRole);
					if (asanaDJudgeTotalScores == null) {
						ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
						participantTeamAsanaJudgeTotalScore
								.setChampionshipParticipantTeams(championshipParticipantTeams);
						participantTeamAsanaJudgeTotalScore
								.setParticipantTeamAsanas(evaluatorJudgeScore.getParticipantTeamAsanas());
						participantTeamAsanaJudgeTotalScore.setParticipantTeamParticipants(tradTeamParticipant);
						participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
						participantTeamAsanaJudgeTotalScore.setRound(round);
						participantTeamAsanaJudgeTotalScore.setSequenceNumber(evaluatorJudgeScore.getSequenceNumber());
						if (tJudgeScoreForAsanaSeqNum.getPenaltyScore() == null
								|| tJudgeScoreForAsanaSeqNum.getPenaltyScore() == false) {
							participantTeamAsanaJudgeTotalScore.setTotalScore(totalDjudgeAverageScore);
						} else if (tJudgeScoreForAsanaSeqNum.getPenaltyScore() == true) {
							participantTeamAsanaJudgeTotalScore.setTotalScore((float) 0);
						}
						participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
					} else {
						if (tJudgeScoreForAsanaSeqNum.getPenaltyScore() == null
								|| tJudgeScoreForAsanaSeqNum.getPenaltyScore() == false) {
							asanaDJudgeTotalScores.setTotalScore(totalDjudgeAverageScore);
						} else if (tJudgeScoreForAsanaSeqNum.getPenaltyScore() == true) {
							asanaDJudgeTotalScores.setTotalScore((float) 0);
						}
						participantTeamAsanaJudgeTotalScoreService.save(asanaDJudgeTotalScores);
					}
				}

				if (evaluatorJudgeScore.getAsPerSequence().toString() == "DIDNOTPERFORMINSEQUENCE") {
					// not performed in sequence
					// djudge scores are added above
					// add all penalties to a list , then save it to last second table
					listEvalJudgePenalty.add(evaluatorJudgeScore.getPenaltyScore());
					LOGGER.debug("listEvalJudgePenalty : " + Arrays.toString(listEvalJudgePenalty.toArray()));
				}

			} else { // not performed
				System.out.println("is performed status :false");
				// make djudge score for that asanaseqNum as zero, then save it to last second
				// table.
				ParticipantTeamAsanaJudgeTotalScore asanaDJudgeTotalScores = participantTeamAsanaJudgeTotalScoreService
						.getByTeamAndAsanaSeqNumAndParticipantAndRoundAndType(championshipParticipantTeams, asanaSeqNum,
								tradTeamParticipant, round, dJudgeRole);
				if (asanaDJudgeTotalScores == null) {
					ParticipantTeamAsanaJudgeTotalScore participantTeamAsanaJudgeTotalScore = new ParticipantTeamAsanaJudgeTotalScore();
					participantTeamAsanaJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
					participantTeamAsanaJudgeTotalScore
							.setParticipantTeamAsanas(evaluatorJudgeScore.getParticipantTeamAsanas());
					participantTeamAsanaJudgeTotalScore.setParticipantTeamParticipants(tradTeamParticipant);
					participantTeamAsanaJudgeTotalScore.setType(dJudgeRole);
					participantTeamAsanaJudgeTotalScore.setRound(round);
					participantTeamAsanaJudgeTotalScore.setSequenceNumber(evaluatorJudgeScore.getSequenceNumber());
					participantTeamAsanaJudgeTotalScore.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(participantTeamAsanaJudgeTotalScore);
				} else {
					asanaDJudgeTotalScores.setTotalScore((float) 0.0);
					participantTeamAsanaJudgeTotalScoreService.save(asanaDJudgeTotalScores);
				}

			}
		}

		// get the Djudge total from above table
		Float totalDjudgeScoresForTeam = participantTeamAsanaJudgeTotalScoreService
				.getSumByTeamAndParticipantAndRoundAndType(championshipParticipantTeams, tradTeamParticipant, round,
						dJudgeRole);
		LOGGER.debug("totalDjudgeScoresForTeam : " + totalDjudgeScoresForTeam);
		// save to ParticipantTeamJudgeTotalScore table, if record found , then update
		// else save new record
		ParticipantTeamJudgeTotalScore judgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndParticipantAndRoundAndType(championshipParticipantTeams, tradTeamParticipant, round,
						dJudgeRole);
		if (judgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setParticipantTeamParticipants(tradTeamParticipant);
			participantTeamJudgeTotalScore.setType(dJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			judgeTotalScores.setTotalScore(totalDjudgeScoresForTeam);
			participantTeamJudgeTotalScoreService.save(judgeTotalScores);
		}

		// save ejudge penalty total
		Float totalEJudgePenaltyScoreSum = (float) 0.0;

		if (listEvalJudgePenalty.size() != 0) {
			for (Float num : listEvalJudgePenalty)
				totalEJudgePenaltyScoreSum += num;
			LOGGER.debug("totalEJudgePenaltyScoreSum : " + totalEJudgePenaltyScoreSum);
		}

		if (listEvalJudgePenalty.size() > 3) { // if more than 3 penalties
			// update the team status to disqualified
			championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
			championshipParticipantTeamsService.save(championshipParticipantTeams);
		}

		// check if record already exists
		ParticipantTeamJudgeTotalScore evaljudgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndParticipantAndRoundAndType(championshipParticipantTeams, tradTeamParticipant, round,
						eJudgeRole);
		if (evaljudgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setParticipantTeamParticipants(tradTeamParticipant);
			participantTeamJudgeTotalScore.setType(eJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalEJudgePenaltyScoreSum);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			evaljudgeTotalScores.setTotalScore(totalEJudgePenaltyScoreSum);
			participantTeamJudgeTotalScoreService.save(evaljudgeTotalScores);
		}

	}

	// Common T-Judge Score calculation method for Artistic Single, Artistic Pair,
	// Rhythmic pair, Artistic group

	private void calculateTJudgeTeamScoresCommon(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType tJudgeRole) {

		LOGGER.info("Entered calculateTJudgeArtisticSingleTeamScores method");
		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();

		List<ParticipantTeamReferees> listTJudgesAssignedForTeam = participantTeamRefereesService
				.getAllJudgesofTypeAndParticipantTeamAndRound(participantTeam, tJudgeRole, round);

		List<Float> listTotalTJudgeScoresGiven = new ArrayList<Float>();
		for (ParticipantTeamReferees tJudgeTeamReferee : listTJudgesAssignedForTeam) {
			System.out.println("tJudgeTeamReferee: " + tJudgeTeamReferee);
			Float tJudgeTeamScore = (float) timeKeeperJudgeScoreService
					.getSumByTeamAndRoundAndReferee(championshipParticipantTeams, round, tJudgeTeamReferee);
			listTotalTJudgeScoresGiven.add(tJudgeTeamScore);
			System.out.println("tJudgeTeamScore: " + tJudgeTeamScore);
		}
		// Average of both Tjudge scores
		float totalTjudgeAverageScore = getAverageForListofScores(listTotalTJudgeScoresGiven);
		System.out.println("totalTjudgeAverageScore: " + totalTjudgeAverageScore);

		// save to ParticipantTeamJudgeTotalScore table
		ParticipantTeamJudgeTotalScore tJudgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, tJudgeRole);
		if (tJudgeTotalScores == null) {
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(tJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalTjudgeAverageScore);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else {
			tJudgeTotalScores.setTotalScore(totalTjudgeAverageScore);
			participantTeamJudgeTotalScoreService.save(tJudgeTotalScores);
		}

		// check if TW score is 0 and update the team status to disqualified
		List<Boolean> wholePerformanceZeroStatus = new ArrayList<Boolean>();
		List<Integer> listWholeTimeInSeconds = new ArrayList<Integer>();
		for (ParticipantTeamReferees tJudgeTeamReferee : listTJudgesAssignedForTeam) {
			TimeKeeperJudgeScore commonTJudgeScore = timeKeeperJudgeScoreService
					.getByTeamAndRoundAndRefereeAndQuestionType(championshipParticipantTeams, round, tJudgeTeamReferee,
							QuestionTypeEnum.COMMONTEAMQUESTION);
			listWholeTimeInSeconds.add((commonTJudgeScore.getTimeInMinutes()*60)+commonTJudgeScore.getTimeInSeconds());
			if (commonTJudgeScore.getTotalScore() == (float) 0) {
				wholePerformanceZeroStatus.add(true);
			} else {
				wholePerformanceZeroStatus.add(false);
			}
		}

		if (wholePerformanceZeroStatus.contains(true)) {

			Double averageDoubleTW = listWholeTimeInSeconds.stream().mapToInt(tw -> tw).average().orElse(0.0);
			Integer averageTw = averageDoubleTW.intValue();
			// update t judge score to 0 in participantTeamJudgeTotalScore
			tJudgeTotalScores.setTotalScore((float) 0);
			participantTeamJudgeTotalScoreService.save(tJudgeTotalScores);

			// update the team status to disqualified
			if (championshipParticipantTeams.getParticipantTeam().getAsanaCategory()
					.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID
					|| championshipParticipantTeams.getParticipantTeam().getAsanaCategory()
							.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
					|| championshipParticipantTeams.getParticipantTeam().getAsanaCategory()
							.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				if (averageTw > AS_AP_RP_TW_VALID_MAX_LIMIT) {
					championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
				}
			} else if(championshipParticipantTeams.getParticipantTeam().getAsanaCategory()
					.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				if (averageTw > AP_TW_VALID_MAX_LIMIT) {
					championshipParticipantTeams.setStatus(StatusEnum.DISQUALIFIED);
				}
				
			}
			championshipParticipantTeamsService.save(championshipParticipantTeams);
		}

	}

	// Common A-Judge Score calculation method for Artistic Single, Artistic Pair,
	// Rhythmic pair, Artistic group

	private void calculateAJudgeTeamScoresCommon(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType aJudgeRole) {

		ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
		List<ParticipantTeamReferees> listAJudgesAssignedForTeam = participantTeamRefereesService
				.getAllJudgesofTypeAndParticipantTeamAndRound(participantTeam, aJudgeRole, round);

		calculateAverageForAjudgeScores(championshipParticipantTeams, round, aJudgeRole, listAJudgesAssignedForTeam);
	}

	// Avergae calculation for A judge score list

	private void calculateAverageForAjudgeScores(ChampionshipParticipantTeams championshipParticipantTeams,
			Integer round, JudgeType aJudgeRole, List<ParticipantTeamReferees> listAJudgesAssignedForTeam) {
		Float totalAJudgeScoreForTeam = (float) 0.0;

		List<Float> listTotalScoresGiven = new ArrayList<>();
		for (ParticipantTeamReferees participantTeamReferee : listAJudgesAssignedForTeam) {
			Float totalAJudgeScoreForJudge = artisticJudgeScoreService
					.getSumForAllByChampionshipParticipantTeamAndParticipantTeamReferees(championshipParticipantTeams,
							participantTeamReferee, round);

			System.out.println("totalAJudgeScoreForJudge :" + totalAJudgeScoreForJudge + "-"
					+ participantTeamReferee.getUserName());

			ParticipantTeamAsanaQuestionTotalScore asanaQuestionTotalScore = participantTeamAsanaQuestionTotalScoreService
					.findByChampionshipParticipantTeamsAndParticipantTeamRefereesAndRound(championshipParticipantTeams,
							participantTeamReferee, round);

			if (asanaQuestionTotalScore == null) { // add new
				ParticipantTeamAsanaQuestionTotalScore participantTeamAsanaQuestionTotalScore = new ParticipantTeamAsanaQuestionTotalScore();
				participantTeamAsanaQuestionTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
				participantTeamAsanaQuestionTotalScore.setParticipantTeamReferees(participantTeamReferee);
				participantTeamAsanaQuestionTotalScore.setRound(round);
				participantTeamAsanaQuestionTotalScore.setTotalScore(totalAJudgeScoreForJudge);
				participantTeamAsanaQuestionTotalScoreService.save(participantTeamAsanaQuestionTotalScore);

			} else { // update existing
				asanaQuestionTotalScore.setTotalScore(totalAJudgeScoreForJudge);
				participantTeamAsanaQuestionTotalScoreService.save(asanaQuestionTotalScore);
			}

			listTotalScoresGiven.add(totalAJudgeScoreForJudge);
		}

		System.out.println("listTotalScoresGiven : " + Arrays.toString(listTotalScoresGiven.toArray()));
		// get average
		float totalAjudgeAverageScore = getAverageForListofScores(listTotalScoresGiven);

		// check if average judge score is saved in ParticipantTeamJudgeTotalScore
		ParticipantTeamJudgeTotalScore judgeTotalScores = participantTeamJudgeTotalScoreService
				.getByTeamAndRoundAndType(championshipParticipantTeams, round, aJudgeRole);
		if (judgeTotalScores == null) { // add new
			ParticipantTeamJudgeTotalScore participantTeamJudgeTotalScore = new ParticipantTeamJudgeTotalScore();
			participantTeamJudgeTotalScore.setChampionshipParticipantTeams(championshipParticipantTeams);
			participantTeamJudgeTotalScore.setType(aJudgeRole);
			participantTeamJudgeTotalScore.setRound(round);
			participantTeamJudgeTotalScore.setTotalScore(totalAjudgeAverageScore);
			participantTeamJudgeTotalScoreService.save(participantTeamJudgeTotalScore);
		} else { // update existing
			judgeTotalScores.setTotalScore(totalAjudgeAverageScore);
			participantTeamJudgeTotalScoreService.save(judgeTotalScores);
		}
	}

	// Get the Average score for the list passed

	// get average for the list
	private float getAverageForListofScores(List<Float> listTotalScoresGiven) {
		Float totalJudgeScoreSum = (float) 0.0;
		for (Float num : listTotalScoresGiven)
			totalJudgeScoreSum += num;

		float totalJudgeScoreAverage = (totalJudgeScoreSum / listTotalScoresGiven.size());
		float totalJudgeScoreAverageRoundOff = (float) (Math.round(totalJudgeScoreAverage * 100.0) / 100.0);
		LOGGER.debug("totalJudgeScoreAverage : " + totalJudgeScoreAverageRoundOff);
		System.out.println("totalJudgeScoreAverage : " + totalJudgeScoreAverageRoundOff);

		return totalJudgeScoreAverageRoundOff;
	}

	// eliminate highest and lowest scores
	// Eliminate Highest and lowest judge score for each asana
	private List<Float> eliminateHighAndLowJudgeScoresForEachAsana(List<Float> listTotalScoresGiven) {
		Float minDJudgeScore = Collections.min(listTotalScoresGiven);
		Float maxDJudgeScore = Collections.max(listTotalScoresGiven);
		LOGGER.debug("minDJudgeScore : " + minDJudgeScore);
		LOGGER.debug("maxDJudgeScore : " + maxDJudgeScore);

		System.out.println("minDJudgeScore : " + minDJudgeScore);
		System.out.println("maxDJudgeScore : " + maxDJudgeScore);

		listTotalScoresGiven.remove(Float.valueOf(minDJudgeScore));
		listTotalScoresGiven.remove(Float.valueOf(maxDJudgeScore));
		LOGGER.debug("listTotalScoresGiven after elimination: " + Arrays.toString(listTotalScoresGiven.toArray()));

		System.out
				.println("listTotalScoresGiven after elimination: " + Arrays.toString(listTotalScoresGiven.toArray()));

		return listTotalScoresGiven;
	}

}
