package com.swayaan.nysf.restcontroller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanaJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamJudgeTotalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipantFinalScore;
import com.swayaan.nysf.entity.ParticipantTeamParticipantTotalScoring;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.DTO.RoundTotalScoringDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamAsanaJudgeTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamJudgeTotalScoreService;
import com.swayaan.nysf.service.ParticipantTeamParticipantFinalScoreService;
import com.swayaan.nysf.service.ParticipantTeamParticipantTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.ParticipantTeamTotalScoringService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class WinnersRestController {

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	private ParticipantTeamTotalScoringService participantTeamTotalScoringService;
	@Autowired
	private ParticipantTeamParticipantTotalScoringService participantTeamParticipantTotalScoringService;
	@Autowired
	private ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;

	@Autowired
	ParticipantTeamParticipantFinalScoreService participantTeamParticipantFinalScoreService;
	@Autowired
	ParticipantTeamAsanaJudgeTotalScoreService participantTeamAsanaJudgeTotalScoreService;
	@Autowired
	ParticipantTeamJudgeTotalScoreService participantTeamJudgeTotalScoreService;

	DecimalFormat scoreFormat = new DecimalFormat("0.00");

	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final Integer JUDGETYPE_ARTISTIC_JUDGE = 3;

	private static final Logger LOGGER = LoggerFactory.getLogger(WinnersRestController.class);

	// @SuppressWarnings("unused")

	@PostMapping("/winners/calculate")
	public ResponseEntity<?> getListOfWinners(@RequestParam("championship") Integer championshipId,
			@RequestParam("asana-category") Integer asanaCategoryId, @RequestParam("gender") String gender,
			@RequestParam("category") Integer ageCategoryId, @RequestParam("round") Integer round)
			throws AsanaCategoryNotFoundException {

		LOGGER.info("Entered getListOfWinners WinnersRestController");
		LOGGER.info(championshipId + "  " + asanaCategoryId + "  " + ageCategoryId + " " + gender + "  " + round);
		List<RoundTotalScoringDTO> result = new ArrayList<>();
		List<ParticipantTeamRoundTotalScoring> listWinnerTeams = new ArrayList<ParticipantTeamRoundTotalScoring>();
		try {
			Championship championship = championshipService.findById(championshipId);
			System.out.println("championship" + championship);
			ChampionshipCategory championshipCategory = championshipCategoryService
					.getChampionshipCategoryForAllConditions(championshipId, asanaCategoryId, ageCategoryId, gender);
			System.out.println("championshipCategory" + championshipCategory);
			ChampionshipRounds championshipRounds = championshipRoundsService
					.getByRoundAndChampionshipAndChampionshipCategory(round, championship, championshipCategory);
			System.out.println("championshipRounds" + championshipRounds);
//			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//					.getTeamsByChampionshipRounds(championshipRounds);

			// get Total - excluding disqualified, absent & rejected teams
			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
					.getTeamsByChampionshipRoundsAndStatus(championshipRounds);

			System.out.println("listChampionshipParticipantTeams" + listChampionshipParticipantTeams);
			List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
					.getByRoundAndChampionshipAndChampionshipCategory(round, championship, championshipCategory);
			// check if already winners are announced
			RoundStatusEnum status = championshipRounds.getStatus();
			if (status.equals(RoundStatusEnum.COMPLETED)) {
				return new ResponseEntity<String>("COMPLETED", HttpStatus.OK);
			}

			System.out.println("listChampionshipParticipantTeamReferees" + listChampionshipParticipantsTeamReferees);
			try {
				boolean isScoreGivenForAllTeams = checkIfAllScoresGivenForTeams(listChampionshipParticipantTeams, round,
						championshipRounds);
				System.out.println("isScoreGivenByAllReferees" + isScoreGivenForAllTeams);
				if (isScoreGivenForAllTeams) {
					// total the score of all the referees for each team
					List<ParticipantTeamRoundTotalScoring> listWinnerGroups = new ArrayList<ParticipantTeamRoundTotalScoring>();

					LOGGER.debug("Total Teams for selected championship : " + listChampionshipParticipantTeams.size());

					listWinnerGroups = participantTeamRoundTotalScoringService.getTeamScoresForOptions(championship,
							championshipRounds);
					System.out.println("1. listwinnerGroups : " + Arrays.toString(listWinnerGroups.toArray()));
					calculateRankForWinners(listWinnerGroups);
					// Apply tie breaking logic if any Scores are equal
					checkForTieAndModifyRanks(listWinnerGroups, championship, asanaCategoryId);
					boolean isFinalRound = true;
					if (championshipCategory.getNoOfRounds() == round) {
						isFinalRound = true;
					}

					for (ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring : listWinnerGroups) {
						System.out.println("Score :" + participantTeamRoundTotalScoring.getTotalScore() + " rank :"
								+ participantTeamRoundTotalScoring.getRanking());
						result.add(new RoundTotalScoringDTO(championship.getId(), championshipRounds.getId(),
								participantTeamRoundTotalScoring.getChampionshipParticipantTeams().getParticipantTeam()
										.getId(),
								participantTeamRoundTotalScoring.getChampionshipParticipantTeams().getParticipantTeam()
										.getChestNumber(),
								participantTeamRoundTotalScoring.getChampionshipParticipantTeams().getParticipantTeam()
										.getName(),
								scoreFormat.format(participantTeamRoundTotalScoring.getTotalScore()),
								participantTeamRoundTotalScoring.getRanking(), isFinalRound,
								participantTeamRoundTotalScoring.isWinner(), participantTeamRoundTotalScoring
										.getChampionshipParticipantTeams().getStatus().toString(),
						participantTeamRoundTotalScoring.getTieScore() == null ? "": 
							scoreFormat.format(participantTeamRoundTotalScoring.getTieScore())));

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(e.getMessage());
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		Collections.sort(result, Comparator.comparingInt(RoundTotalScoringDTO::getRanking));
		return new ResponseEntity<List<RoundTotalScoringDTO>>(result, HttpStatus.OK);
		// return new ResponseEntity<String>("Unable to calculate winners",
		// HttpStatus.BAD_REQUEST);
	}

	private void checkForTieAndModifyRanks(List<ParticipantTeamRoundTotalScoring> listWinnerGroups,
			Championship championship, Integer asanaCategoryId) {
		LOGGER.info("Entered checkForTieAndModifyRanks - WinnersRestController ");

		List<ParticipantTeamRoundTotalScoring> listDistinctScoreRecords = listWinnerGroups.stream()
				.filter(distinctByKey(s -> s.getTotalScore())).collect(Collectors.toList());
		if (listWinnerGroups.size() > listDistinctScoreRecords.size()) {

			for (ParticipantTeamRoundTotalScoring eachScore : listDistinctScoreRecords) {

				List<ParticipantTeamRoundTotalScoring> listTieScoresTeams = listWinnerGroups.stream()
						.filter(team -> Float.compare(team.getTotalScore(), eachScore.getTotalScore()) == 0)
						.collect(Collectors.toList());
				Integer noOfTeamsWithTieScores = listTieScoresTeams.size();
				List<ParticipantTeamAsanaJudgeTotalScore> listCompulsoryAsanaScores = new ArrayList<>();
				if (listTieScoresTeams.size() > 1) {
					Collections.sort(listTieScoresTeams,
							Comparator.comparingInt(ParticipantTeamRoundTotalScoring::getRanking));
					Integer startRank = listTieScoresTeams.get(0).getRanking();
					// There is a tie between scores
					if (asanaCategoryId == TRADITIONAL_ASANA_CATEGORY_ID) {
						for (ParticipantTeamRoundTotalScoring team : listTieScoresTeams) {
							team.setTie(true);
							// participant_team_asana_judge_total_scores
							listCompulsoryAsanaScores = participantTeamAsanaJudgeTotalScoreService
									.findByChampionshipParticipantTeamsAndAsanasCompulsory(
											team.getChampionshipParticipantTeams());

							if (!listCompulsoryAsanaScores.isEmpty()) {
								Float tieScore = 0.0f;
								for (ParticipantTeamAsanaJudgeTotalScore score : listCompulsoryAsanaScores) {
									tieScore = tieScore + score.getTotalScore();
								}
								team.setTieScore(tieScore);
							}
						}
						if (!listCompulsoryAsanaScores.isEmpty()) {
							// sort the tie score teams with tie score descending order
							Collections.sort(listTieScoresTeams, new Comparator<ParticipantTeamRoundTotalScoring>() {
								@Override
								public int compare(ParticipantTeamRoundTotalScoring p,
										ParticipantTeamRoundTotalScoring p1) {

									return Float.compare(p1.getTieScore(), p.getTieScore());
								}
							});
							for (ParticipantTeamRoundTotalScoring partTeam : listTieScoresTeams) {
								partTeam.setRanking(startRank);
								participantTeamRoundTotalScoringService.save(partTeam);
								startRank++;
							}
						}

					} else if (asanaCategoryId == ARTISTIC_SINGLE_ASANA_CATEGORY_ID
							|| asanaCategoryId == ARTISTIC_PAIR_ASANA_CATEGORY_ID
							|| asanaCategoryId == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
							|| asanaCategoryId == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {

						for (ParticipantTeamRoundTotalScoring team : listTieScoresTeams) {
							team.setTie(true);
							// participant_team_asana_judge_total_scores
							ParticipantTeamJudgeTotalScore participantTeamArtisticJudgeTotalScore = participantTeamJudgeTotalScoreService
									.findByChampionshipParticipantTeamsAndJudgeType(
											team.getChampionshipParticipantTeams(), JUDGETYPE_ARTISTIC_JUDGE);
							if (participantTeamArtisticJudgeTotalScore != null) {
								team.setTieScore(participantTeamArtisticJudgeTotalScore.getTotalScore());
							}
						}

						// sort the tie score teams with tie score descending order
						Collections.sort(listTieScoresTeams, new Comparator<ParticipantTeamRoundTotalScoring>() {
							@Override
							public int compare(ParticipantTeamRoundTotalScoring p,
									ParticipantTeamRoundTotalScoring p1) {

								return Float.compare(p1.getTieScore(), p.getTieScore());
							}
						});
						for (ParticipantTeamRoundTotalScoring team : listTieScoresTeams) {
							team.setRanking(startRank);
							participantTeamRoundTotalScoringService.save(team);
							startRank++;
						}

					}
//					// sort the tie score teams with tie score descending order
//					Collections.sort(listTieScoresTeams, new Comparator<ParticipantTeamRoundTotalScoring>() {
//						@Override
//						public int compare(ParticipantTeamRoundTotalScoring p, ParticipantTeamRoundTotalScoring p1) {
//
//							return Float.compare(p1.getTieScore(), p.getTieScore());
//						}
//					});
//					for (ParticipantTeamRoundTotalScoring team : listTieScoresTeams) {
//						team.setRanking(startRank);
//						participantTeamRoundTotalScoringService.save(team);
//						startRank++;
//					}
				}
			}
		}
	}

	private boolean checkIfAllScoresGivenForTeams(List<ChampionshipParticipantTeams> listChampionshipParticipantTeams,
			Integer round, ChampionshipRounds championshipRounds) throws Exception {
		// check if all referees(Djudges & chiefJudge given scores for participant teams
		// for that round - if
		// yes, update status in championship_participant table to completed

		for (ChampionshipParticipantTeams team : listChampionshipParticipantTeams) {
			boolean status = false;

			if (participantTeamRoundTotalScoringService.isExistsByChampionshipParticipantTeams(team)) {

				status = true;
			} else {
				status = false;
				// throw error
				throw new Exception("Total Score for the team " + team.getParticipantTeam().getChestNumber()
						+ " is not calculated. Please contact the Chief Judge/Scorer.");
			}

		}

		// if all the championship participant teams status is completed for that
		// championship round - then all judges are scored to all the teams
		List<ChampionshipParticipantTeams> listCompletedChampionshipParticipantTeams = listChampionshipParticipantTeams
				.stream().filter(team -> team.getStatus().equals(StatusEnum.COMPLETED)).collect(Collectors.toList());
		if (listChampionshipParticipantTeams.size() == listCompletedChampionshipParticipantTeams.size()) {
			// championshipRounds.setStatus(RoundStatusEnum.COMPLETED);
			// championshipRoundsService.save(championshipRounds);
			return true;
		} else {
			throw new Exception("Total Team Scores are not calculated for some teams. Please check!");
		}
	}

//	@PostMapping("/winners/calculate")
//	public ResponseEntity<?> getListOfWinners(@RequestParam("championship") Integer championshipId,
//			@RequestParam("asana-category") Integer asanaCategoryId, @RequestParam("gender") String gender,
//			@RequestParam("category") Integer ageCategoryId, @RequestParam("round") Integer round)
//			throws AsanaCategoryNotFoundException {
//
//		LOGGER.info("Entered getListOfWinners WinnersRestController");
//		LOGGER.info(championshipId + "  " + asanaCategoryId + "  " + ageCategoryId + " " + gender + "  " + round);
//		List<RoundTotalScoringDTO> result = new ArrayList<>();
//		List<ParticipantTeamRoundTotalScoring> listWinnerTeams = new ArrayList<ParticipantTeamRoundTotalScoring>();
//		try {
//			Championship championship = championshipService.findById(championshipId);
//			System.out.println("championship" + championship);
//			ChampionshipCategory championshipCategory = championshipCategoryService
//					.getChampionshipCategoryForAllConditions(championshipId, asanaCategoryId, ageCategoryId, gender);
//			System.out.println("championshipCategory" + championshipCategory);
//			ChampionshipRounds championshipRounds = championshipRoundsService
//					.getByRoundAndChampionshipAndChampionshipCategory(round, championship, championshipCategory);
//			System.out.println("championshipRounds" + championshipRounds);
//			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//					.getTeamsByChampionshipRounds(championshipRounds);
//			System.out.println("listChampionshipParticipantTeams" + listChampionshipParticipantTeams);
//			List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
//					.getByRoundAndChampionshipAndChampionshipCategory(round, championship, championshipCategory);
//			// check if already winners are announced
//			RoundStatusEnum status = championshipRounds.getStatus();
//			if (status.equals(RoundStatusEnum.COMPLETED)) {
//
//				return new ResponseEntity<String>("COMPLETED", HttpStatus.OK);
//			}
//
//			System.out.println("listChampionshipParticipantTeamReferees" + listChampionshipParticipantsTeamReferees);
//			try {
//				boolean isScoreGivenByAllReferees = checkIfAllRefereesGivenScoresForTeams(
//						listChampionshipParticipantTeams, listChampionshipParticipantsTeamReferees, round,
//						championshipRounds);
//				System.out.println("isScoreGivenByAllReferees" + isScoreGivenByAllReferees);
//				if (isScoreGivenByAllReferees) {
//					// total the score of all the referees for each team excluding highest & lowest
//					// score
//
//					List<ParticipantTeamRoundTotalScoring> listWinnerGroups = new ArrayList<ParticipantTeamRoundTotalScoring>();
//
//					LOGGER.debug("Total Teams for selected championship : " + listChampionshipParticipantTeams.size());
//					if (listChampionshipParticipantTeams.size() != 0 || listChampionshipParticipantTeams != null) {
//						// calculate total score of each team considering 7 judges
//						for (ChampionshipParticipantTeams championshipParticipantTeam : listChampionshipParticipantTeams) {
//
//							List<Float> listAllJudgesTotalScores = new ArrayList<Float>();
//
//							List<Float> listTradAndChiefJudgesTotalScores = new ArrayList<Float>();
//
//							// get the panel judges for the team
//							List<ParticipantTeamReferees> listChampionshipParticipantTeamReferees = participantTeamRefereesService
//									.getByParticipantTeamWithRoundAndChampionshipAndChampionshipCategory(round,
//											championship, championshipCategory,
//											championshipParticipantTeam.getParticipantTeam());
//
//							for (ParticipantTeamReferees teamJudge : listChampionshipParticipantTeamReferees) {
//								if (teamJudge.getUser().getRoleId() == 2) {
//									System.out.println("Chief Judge");
//									// pass team, user and round to participant_team_total_scores and get the score
//									ParticipantTeamTotalScoring participantTeamTotalScore = participantTeamTotalScoringService
//											.findByChampionshipParticpantTeamAndRoundNumAndUser(
//													championshipParticipantTeam, round, teamJudge.getUser());
//									Float chiefJudgeScore = (float) 0;
//									if (participantTeamTotalScore != null) {
//										chiefJudgeScore = participantTeamTotalScore.getTotalScore();
//										listTradAndChiefJudgesTotalScores.add(chiefJudgeScore);
//										System.out.println("chiefJudgeScore : " + chiefJudgeScore);
//									} else {
//										listTradAndChiefJudgesTotalScores.add(chiefJudgeScore);
//									}
//
//								} else if (teamJudge.getUser().getRoleId() == 3) {
//									System.out.println("D Judge");
//									// pass team, user and round to participant_team_total_scores and get the score
//									ParticipantTeamTotalScoring participantTeamTotalScore = participantTeamTotalScoringService
//											.findByChampionshipParticpantTeamAndRoundNumAndUser(
//													championshipParticipantTeam, round, teamJudge.getUser());
//									Float tradJudgeScore = (float) 0;
//									if (participantTeamTotalScore != null) {
//										tradJudgeScore = participantTeamTotalScore.getTotalScore();
//										listTradAndChiefJudgesTotalScores.add(tradJudgeScore);
//										System.out.println("tradJudgeScore : " + tradJudgeScore);
//									} else {
//										listTradAndChiefJudgesTotalScores.add(tradJudgeScore);
//									}
//								}
//							}
//							// remove highest & lowest scores from listTradAndChiefJudgesTotalScores -
//							// calculate avg
//							// get the highest and lowest marks from trad & chief judge scores
//							Float minTradAndChiefJudgeScore = Collections.min(listTradAndChiefJudgesTotalScores);
//							Float maxTradAndChiefJudgeScore = Collections.max(listTradAndChiefJudgesTotalScores);
//							LOGGER.debug("Lowest Score given by judge : " + minTradAndChiefJudgeScore);
//							LOGGER.debug("Highest Score given by judge : " + maxTradAndChiefJudgeScore);
//
//							System.out.println("minTradAndChiefJudgeScore : " + minTradAndChiefJudgeScore);
//							System.out.println("maxTradAndChiefJudgeScore : " + maxTradAndChiefJudgeScore);
//
//							listTradAndChiefJudgesTotalScores.remove(Float.valueOf(minTradAndChiefJudgeScore));
//							listTradAndChiefJudgesTotalScores.remove(Float.valueOf(maxTradAndChiefJudgeScore));
//							System.out.println("listTradAndChiefJudgesTotalScores : "
//									+ Arrays.toString(listTradAndChiefJudgesTotalScores.toArray()));
//							Float totalTradAndChiefScoreSum = (float) 0.0;
//							for (Float num : listTradAndChiefJudgesTotalScores)
//								totalTradAndChiefScoreSum += num;
//							float totalTradAndChiefScoreAverage = (totalTradAndChiefScoreSum
//									/ listTradAndChiefJudgesTotalScores.size());
//
//							LOGGER.debug("totalTradAndChiefScoreAverage : " + totalTradAndChiefScoreAverage);
//							System.out.println("totalTradAndChiefScoreAverage : " + totalTradAndChiefScoreAverage);
//							listAllJudgesTotalScores.add(totalTradAndChiefScoreAverage);
//							LOGGER.info("Traditional & Chief Judge Total Scores Computation ends");
//
//							Float totalJudgesScoresSum = (float) listAllJudgesTotalScores.stream()
//									.mapToDouble(Float::doubleValue).sum();
//							System.out.println("TotalJudgesScoresSum : " + totalJudgesScoresSum);
//
//							ParticipantTeamRoundTotalScoring existingTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//									.findByChampionshipParticipantTeamAndChampionshipRounds(championshipParticipantTeam,
//											championshipRounds);
//
//							if (existingTeamRoundTotalScoring == null) {
//								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = new ParticipantTeamRoundTotalScoring();
//								participantTeamRoundTotalScoring
//										.setChampionshipParticipantTeams(championshipParticipantTeam);
//								participantTeamRoundTotalScoring.setTotalScore(totalJudgesScoresSum);
//								participantTeamRoundTotalScoring.setChampionshipRounds(championshipRounds);
//								participantTeamRoundTotalScoring.setChampionship(championship);
//
//								participantTeamRoundTotalScoringService.save(participantTeamRoundTotalScoring);
//							} else {
//								existingTeamRoundTotalScoring.setTotalScore(totalJudgesScoresSum);
//								participantTeamRoundTotalScoringService.save(existingTeamRoundTotalScoring);
//							}
//
//						}
//					}
//
//					listWinnerGroups = participantTeamRoundTotalScoringService.getTeamScoresForOptions(championship,
//							championshipRounds);
//					System.out.println("1. listwinnerGroups : " + Arrays.toString(listWinnerGroups.toArray()));
//					calculateRankForWinners(listWinnerGroups);
//					boolean isFinalRound = true;
//					if (championshipCategory.getNoOfRounds() == round) {
//						isFinalRound = true;
//					}
//
//					for (ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring : listWinnerGroups) {
//						System.out.println("Score :" + participantTeamRoundTotalScoring.getTotalScore() + " rank :"
//								+ participantTeamRoundTotalScoring.getRanking());
//						result.add(new RoundTotalScoringDTO(championship.getId(), championshipRounds.getId(),
//								participantTeamRoundTotalScoring.getChampionshipParticipantTeams().getParticipantTeam()
//										.getId(),
//								participantTeamRoundTotalScoring.getChampionshipParticipantTeams().getParticipantTeam()
//										.getChestNumber(),
//								participantTeamRoundTotalScoring.getChampionshipParticipantTeams().getParticipantTeam()
//										.getName(),
//								scoreFormat.format(participantTeamRoundTotalScoring.getTotalScore()),
//								participantTeamRoundTotalScoring.getRanking(), isFinalRound));
//					}
//
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				LOGGER.info(e.getMessage());
//				return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOGGER.info(e.getMessage());
//			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//		return new ResponseEntity<List<RoundTotalScoringDTO>>(result, HttpStatus.OK);
//		// return new ResponseEntity<String>("Unable to calculate winners",
//		// HttpStatus.BAD_REQUEST);
//	}

	private boolean checkIfAllRefereesGivenScoresForTeams(
			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams,
			List<ParticipantTeamReferees> listChampionshipParticipantTeamReferees, Integer round,
			ChampionshipRounds championshipRounds) throws Exception {
		// check if all referees(Djudges & chiefJudge given scores for participant teams
		// for that round - if
		// yes, update status in championship_participant table to completed
		System.out.println(listChampionshipParticipantTeamReferees);
		List<ParticipantTeamReferees> listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees = listChampionshipParticipantTeamReferees
				.stream().filter(referee -> referee.getType().getId() == 1 || referee.getType().getId() == 2)
				.collect(Collectors.toList());
		System.out.println("listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees"
				+ listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees);
		for (ChampionshipParticipantTeams team : listChampionshipParticipantTeams) {
			boolean status = false;
			List<ParticipantTeamReferees> listParticipantTeamDJudgesAndChiefJudgeReferees = listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees
					.stream().filter(referee -> referee.getParticipantTeam().equals(team.getParticipantTeam()))
					.collect(Collectors.toList());
			System.out.println("listParticipantTeamDJudgesAndChiefJudgeReferees"
					+ listParticipantTeamDJudgesAndChiefJudgeReferees);
			for (ParticipantTeamReferees referee : listParticipantTeamDJudgesAndChiefJudgeReferees) {
				if (participantTeamTotalScoringService.isExistsByRoundAndChampionshipParticipantTeamAndJudgeUser(round,
						team, referee)) {

					status = true;
				} else {
					status = false;
					// throw error - some referee is still not given scores to participant teams
					throw new Exception("Some Judges has not given scores to team "
							+ team.getParticipantTeam().getChestNumber() + ". Please check..");
				}
			}
			if (status == true) {
				// Compute participant total scores for round
				calculateParticipantFinalScoreForRound(team, listParticipantTeamDJudgesAndChiefJudgeReferees, round);

				// change the status of that championship_participant_team to completed
				team.setStatus(StatusEnum.COMPLETED);
				championshipParticipantTeamsService.save(team);
			}

		}

		// if all the championship participant teams status is completed for that
		// championship round - then all judges are scored to all the teams
		List<ChampionshipParticipantTeams> listCompletedChampionshipParticipantTeams = listChampionshipParticipantTeams
				.stream().filter(team -> team.getStatus().equals(StatusEnum.COMPLETED)).collect(Collectors.toList());
		if (listChampionshipParticipantTeams.size() == listCompletedChampionshipParticipantTeams.size()) {
			championshipRounds.setStatus(RoundStatusEnum.COMPLETED);
			championshipRoundsService.save(championshipRounds);
			return true;
		} else {
			throw new Exception("Scores are not given to some teams. Please check");
		}
	}

	private void calculateParticipantFinalScoreForRound(ChampionshipParticipantTeams team,
			List<ParticipantTeamReferees> listParticipantTeamDJudgesAndChiefJudgeReferees, Integer round) {

		List<ParticipantTeamParticipants> listParticipants = team.getParticipantTeam().getParticipantTeamParticipants();

		for (ParticipantTeamParticipants participant : listParticipants) {
			Float finalScoreForParticipant = (float) 0;
			List<ParticipantTeamParticipantTotalScoring> listTotalScoresOfAllJudges = participantTeamParticipantTotalScoringService
					.findByRoundAndParticipantTeamParticipantsAndChampionshipParticipantTeams(round, participant, team);
			LOGGER.info("Participant score by all judges" + listTotalScoresOfAllJudges.toString());
			if (!listTotalScoresOfAllJudges.isEmpty()) {
				for (ParticipantTeamParticipantTotalScoring score : listTotalScoresOfAllJudges) {
					finalScoreForParticipant = finalScoreForParticipant + score.getTotalScore();
				}

				// check if the record already exists
				ParticipantTeamParticipantFinalScore existingParticipantFinalScore = participantTeamParticipantFinalScoreService
						.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(team, participant,
								round);
				if (existingParticipantFinalScore != null) {
					System.out.println("Already exists - updating score");
					existingParticipantFinalScore.setTotalScore(finalScoreForParticipant);
					participantTeamParticipantFinalScoreService.save(existingParticipantFinalScore);
				} else {
					System.out.println("Record doesnot exist -Creating new entry");
					ParticipantTeamParticipantFinalScore participantFinalScore = new ParticipantTeamParticipantFinalScore();
					participantFinalScore.setChampionshipParticipantTeams(team);
					participantFinalScore.setParticipantTeamParticipant(participant);
					participantFinalScore.setRound(round);
					participantFinalScore.setTotalScore(finalScoreForParticipant);
					participantTeamParticipantFinalScoreService.save(participantFinalScore);
				}
			}
		}

	}

	public void calculateRankForWinners(List<ParticipantTeamRoundTotalScoring> listWinnerGroups) {

		Collections.sort(listWinnerGroups, new Comparator<ParticipantTeamRoundTotalScoring>() {
			@Override
			public int compare(ParticipantTeamRoundTotalScoring p, ParticipantTeamRoundTotalScoring p1) {

				return Float.compare(p1.getTotalScore(), p.getTotalScore());
			}
		});
//		List<Float> listWithRank = listWinnerGroups.stream().map(p -> p.getTotalScore()).distinct()
//				.collect(Collectors.toList());
//		listWithRank.forEach(p -> System.out.println(listWithRank));
//		listWinnerGroups.forEach(p -> {
//			p.setRanking(listWithRank.indexOf(p.getTotalScore()) + 1);
//			participantTeamRoundTotalScoringService.save(p);
//		});
		for (Integer rank = 1; rank <= listWinnerGroups.size(); rank++) {
			ParticipantTeamRoundTotalScoring p = listWinnerGroups.get(rank - 1);
			p.setRanking(rank);
			p.setTie(false);
			p.setTieScore(null);
			participantTeamRoundTotalScoringService.save(p);
		}
	}

	@GetMapping("/admin/manage-winners/select/championship/{championshipId}/round/{championshipRoundsId}/team/{participantTeamId}/qualify")
	public ResponseEntity<?> MoveToNextRound(@PathVariable("championshipId") Integer championshipId,
			@PathVariable("championshipRoundsId") Integer championshipRoundsId,
			@PathVariable("participantTeamId") Integer participantTeamId) throws Exception {
		LOGGER.info("Entered MoveToNextRound WinnersRestController");
		try {
			ParticipantTeam participantTeam = participantTeamService.get(participantTeamId);
			Championship championship = championshipService.get(championshipId);
			ChampionshipRounds championshipRounds = championshipRoundsService.get(championshipRoundsId);
			// find tota number of rounds fr this category
			ChampionshipCategory championshipCategory = championshipRounds.getChampionshipCategory();
			Integer noOfRounds = championshipCategory.getNoOfRounds();

			// set the winner column to true
			ChampionshipParticipantTeams currentChampionshipParticipantTeams = championshipParticipantTeamsService
					.findByParticipantTeamAndChampionshipRounds(participantTeam, championshipRounds);
			ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
					.findByChampionshipParticipantTeamAndChampionshipRounds(currentChampionshipParticipantTeams,
							championshipRounds);
			participantTeamRoundTotalScoring.setWinner(true);
			participantTeamRoundTotalScoringService.save(participantTeamRoundTotalScoring);
			return new ResponseEntity<>("success", HttpStatus.OK);
//			if (championshipRounds.getRound() < noOfRounds) {
//				// get championshipRounds for next round
//				Integer round = championshipRounds.getRound() + 1;
//				ChampionshipRounds nextChampionshipRounds = championshipRoundsService
//						.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
//
//				// add participant teams to championshipParticipant teams with new
//				// championshipRoundsNumber
//				ChampionshipParticipantTeams championshipParticipantTeams = championshipParticipantTeamsService
//						.findByParticipantTeamAndChampionshipRounds(participantTeam, nextChampionshipRounds);
//				if (championshipParticipantTeams == null) {
//					ChampionshipParticipantTeams newChampionshipParticipantTeams = new ChampionshipParticipantTeams();
//					newChampionshipParticipantTeams.setChampionshipRounds(nextChampionshipRounds);
//					newChampionshipParticipantTeams.setParticipantTeam(participantTeam);
//					newChampionshipParticipantTeams.setStatus(StatusEnum.SCHEDULED);
//					championshipParticipantTeamsService.save(newChampionshipParticipantTeams);
//					return new ResponseEntity<>("success", HttpStatus.OK);
//
//				} else {
//					// throw new Exception("Participant is already qualified to next round");
//					return new ResponseEntity<>("Participant is already qualified to next round",
//							HttpStatus.BAD_REQUEST);
//				}
//			} else {
//				return new ResponseEntity<>("NOROUNDS", HttpStatus.OK);
//			}
		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("ParticipantTeamNotFound", HttpStatus.NOT_FOUND);

		} catch (ChampionshipNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("ChampionshipNotFound", HttpStatus.NOT_FOUND);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("EntityNotFound", HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/admin/manage-winners/select/championship/{championshipId}/round/{championshipRoundsId}/team/{participantTeamId}/disqualify")
	public ResponseEntity<?> DisQualifyTeam(@PathVariable("championshipId") Integer championshipId,
			@PathVariable("championshipRoundsId") Integer championshipRoundsId,
			@PathVariable("participantTeamId") Integer participantTeamId) throws Exception {
		LOGGER.info("Entered DisQualifyTeam WinnersRestController");
		try {
			ParticipantTeam participantTeam = participantTeamService.get(participantTeamId);
			Championship championship = championshipService.get(championshipId);
			ChampionshipRounds championshipRounds = championshipRoundsService.get(championshipRoundsId);
			// find tota number of rounds fr this category
			ChampionshipCategory championshipCategory = championshipRounds.getChampionshipCategory();
			Integer noOfRounds = championshipCategory.getNoOfRounds();

			ChampionshipParticipantTeams currentChampionshipParticipantTeams = championshipParticipantTeamsService
					.findByParticipantTeamAndChampionshipRounds(participantTeam, championshipRounds);

			// set the winner column to false
			ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
					.findByChampionshipParticipantTeamAndChampionshipRounds(currentChampionshipParticipantTeams,
							championshipRounds);
			participantTeamRoundTotalScoring.setWinner(false);
			participantTeamRoundTotalScoringService.save(participantTeamRoundTotalScoring);
			return new ResponseEntity<>("success", HttpStatus.OK);

//			if (championshipRounds.getRound() < noOfRounds) {
//				// get championshipRounds for next round
//				Integer round = championshipRounds.getRound() + 1;
//				ChampionshipRounds nextChampionshipRounds = championshipRoundsService
//						.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
//
//				// add participant teams to championshipParticipant teams with new
//				// championshipRoundsNumber
//				ChampionshipParticipantTeams championshipParticipantTeams = championshipParticipantTeamsService
//						.findByParticipantTeamAndChampionshipRounds(participantTeam, nextChampionshipRounds);
//				if (championshipParticipantTeams != null) {
//					championshipParticipantTeamsService.deleteById(championshipParticipantTeams.getId());

//
//				} else {
//					// throw new Exception("Participant is already qualified to next round");
//					return new ResponseEntity<>("Failed", HttpStatus.OK);
//				}
//			} else {
//				return new ResponseEntity<>("NOROUNDS", HttpStatus.OK);
//			}

		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("ParticipantTeamNotFound", HttpStatus.NOT_FOUND);

		} catch (ChampionshipNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("ChampionshipNotFound", HttpStatus.NOT_FOUND);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>("EntityNotFound", HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/admin/manage-winners/freeze/championship/{championshipId}/round/{championshipRoundsId}")
	public ResponseEntity<?> FreezeChampionshipRound(Model model,
			@PathVariable("championshipId") Integer championshipId,
			@PathVariable("championshipRoundsId") Integer championshipRoundsId) throws Exception {
		LOGGER.info("Entered FreezeChampionshipRound WinnersController");
		try {
			Championship championship = championshipService.get(championshipId);
			ChampionshipRounds championshipRounds = championshipRoundsService.get(championshipRoundsId);
			// find tota number of rounds fr this category
			ChampionshipCategory championshipCategory = championshipRounds.getChampionshipCategory();
			Integer noOfRounds = championshipCategory.getNoOfRounds();
			if (championshipRounds.getRound() < noOfRounds) {
				// get championshipRounds for next round
				Integer round = championshipRounds.getRound() + 1;
				ChampionshipRounds nextChampionshipRounds = championshipRoundsService
						.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory, round);
				boolean winner = true;
				// add participant teams to championshipParticipant teams with new
				// championshipRoundsNumber

				List<ParticipantTeamRoundTotalScoring> listWinnersParticipantTeams = participantTeamRoundTotalScoringService
						.findByChampionshipAndChampionshipRoundsAndWinner(championship, championshipRounds, winner);
				for (ParticipantTeamRoundTotalScoring winnerTeam : listWinnersParticipantTeams) {
					ParticipantTeam participantTeam = winnerTeam.getChampionshipParticipantTeams().getParticipantTeam();
					// check if record already exists
					ChampionshipParticipantTeams championshipParticipantTeams = championshipParticipantTeamsService
							.findByParticipantTeamAndChampionshipRounds(participantTeam, nextChampionshipRounds);
					if (championshipParticipantTeams == null) {
						ChampionshipParticipantTeams newChampionshipParticipantTeams = new ChampionshipParticipantTeams();
						newChampionshipParticipantTeams.setChampionshipRounds(nextChampionshipRounds);
						newChampionshipParticipantTeams.setParticipantTeam(participantTeam);
						newChampionshipParticipantTeams.setStatus(StatusEnum.SCHEDULED);
						championshipParticipantTeamsService.save(newChampionshipParticipantTeams);

					}
				}

			}
//			else {
//				championship.setStatus(ChampionshipStatusEnum.COMPLETED);
//				championshipService.save(championship);
//			}

			// Set round status to completed
			championshipRounds.setStatus(RoundStatusEnum.COMPLETED);
			championshipRoundsService.save(championshipRounds);

			// Set championship status - completed
			List<ChampionshipRounds> listChampionshipRounds = championshipRoundsService
					.findAllByChampionship(championship);
			List<ChampionshipRounds> listCompletedChampionshipRounds = listChampionshipRounds.stream()
					.filter(cr -> cr.getStatus().equals(RoundStatusEnum.COMPLETED)).collect(Collectors.toList());
			if (listChampionshipRounds.size() == listCompletedChampionshipRounds.size()) {
				championship.setStatus(ChampionshipStatusEnum.COMPLETED);
				championshipService.updateStatus(championship);
				// reject still pending registered teams
				List<RegistrationStatusEnum> pendingStatus=new ArrayList<>(Arrays.asList (RegistrationStatusEnum.PENDING));
				List<ParticipantTeam> listPendingEnrollmentTeams= participantTeamService.listAllParticipantTeamsForChampionshipAndStatus(championship, pendingStatus);
				for(ParticipantTeam pendingTeam:listPendingEnrollmentTeams) {
					participantTeamService.updateRegisteredTeamStatus(pendingTeam,RegistrationStatusEnum.REJECTED);
				}
			
			}
			return new ResponseEntity<>("FROZEN", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("FAILED", HttpStatus.OK);
		}
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
