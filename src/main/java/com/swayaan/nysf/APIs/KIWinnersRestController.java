//package com.swayaan.nysf.APIs;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.swayaan.nysf.API.entity.livescores.AsanasDTO;
//import com.swayaan.nysf.API.entity.livescores.AthletesDTO;
//import com.swayaan.nysf.API.entity.livescores.CategoriesDTO;
//import com.swayaan.nysf.API.entity.livescores.LiveScoresDTO;
//import com.swayaan.nysf.API.entity.livescores.RoundsDTO;
//import com.swayaan.nysf.API.entity.livescores.TeamScoresDTO;
//import com.swayaan.nysf.API.entity.livescores.TeamsDTO;
//import com.swayaan.nysf.API.entity.livescores.WinnersDTO;
//import com.swayaan.nysf.entity.Championship;
//import com.swayaan.nysf.entity.ChampionshipCategory;
//import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
//import com.swayaan.nysf.entity.ChampionshipRounds;
//import com.swayaan.nysf.entity.ParticipantTeam;
//import com.swayaan.nysf.entity.ParticipantTeamAsanaJudgeTotalScore;
//import com.swayaan.nysf.entity.ParticipantTeamAsanaScoring;
//import com.swayaan.nysf.entity.ParticipantTeamAsanas;
//import com.swayaan.nysf.entity.ParticipantTeamJudgeTotalScore;
//import com.swayaan.nysf.entity.ParticipantTeamParticipantFinalScore;
//import com.swayaan.nysf.entity.ParticipantTeamParticipants;
//import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
//import com.swayaan.nysf.entity.Schedule;
//import com.swayaan.nysf.entity.ScheduleDetails;
//import com.swayaan.nysf.entity.StatusEnum;
//import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
//import com.swayaan.nysf.exception.ChampionshipNotFoundException;
//import com.swayaan.nysf.exception.EntityNotFoundException;
//import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
//import com.swayaan.nysf.service.ChampionshipRoundsService;
//import com.swayaan.nysf.service.ChampionshipService;
//import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
//import com.swayaan.nysf.service.JudgeTypeService;
//import com.swayaan.nysf.service.ParticipantTeamAsanaJudgeTotalScoreService;
//import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
//import com.swayaan.nysf.service.ParticipantTeamAsanasService;
//import com.swayaan.nysf.service.ParticipantTeamJudgeTotalScoreService;
//import com.swayaan.nysf.service.ParticipantTeamParticipantFinalScoreService;
//import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
//import com.swayaan.nysf.service.RoleService;
//import com.swayaan.nysf.service.ScheduleService;
//
//@RestController
//@RequestMapping("/api/v1/championship/kheloindia")
//public class KIWinnersRestController {
//
//	@Autowired
//	ChampionshipService championshipService;
//	@Autowired
//	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
//	@Autowired
//	ChampionshipRoundsService championshipRoundsService;
//	@Autowired
//	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
//	@Autowired
//	ParticipantTeamParticipantFinalScoreService participantTeamParticipantFinalScoreService;
//	@Autowired
//	ParticipantTeamAsanasService participantTeamAsanasService;
//	@Autowired
//	ParticipantTeamAsanasScoringService participantTeamAsanasScoringService;
//	@Autowired
//	CompulsoryRoundAsanasService compulsoryRoundAsanasService;
//	@Autowired
//	ScheduleService scheduleService;
//	@Autowired
//	ParticipantTeamJudgeTotalScoreService participantTeamJudgeTotalScoreService;
//
//	@Autowired
//	private ScheduleDetails scheduleDetails;
//
//	@Autowired
//	ParticipantTeamAsanaJudgeTotalScoreService participantTeamAsanaJudgeTotalScoreService;
//	@Autowired
//	private RoleService roleService;
//	@Autowired
//	private JudgeTypeService judgeTypeService;
//
//	DecimalFormat scoreFormat = new DecimalFormat("0.00");
//	private static final int ASANA_CATEGORY_TRADITIONAL = 1;
//	private static final int D_Judge_Role = 2;
//	private static final int A_Judge_Role = 3;
//	private static final int T_Judge_Role = 4;
//	private static final int E_Judge_Role = 5;
//
//	// ******************************** common APIs *************************
//
//	@GetMapping(value = "/winners/boys", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getLiveScoresBoys() throws ChampionshipNotFoundException {
//		Integer scheduleId = 1;
//		Integer sportId;
//		String sportName;
//
//		try {
//			Schedule schedule = scheduleService.get(scheduleId);
//			sportId = schedule.getSport_id();
//			sportName = schedule.getSport_name();
//		} catch (EntityNotFoundException e) {
//			// throw new EntityNotFoundException("Schedule not found for the
//			// Championship!");
//			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
//		}
//		String description = scheduleDetails.getLivescoresDescription();
//
//		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
//		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
//		Integer championshipId = 1;
//		try {
//			Championship championship = championshipService.get(championshipId);
//
//			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//			if (!listChampionshipCategory.isEmpty()) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					// String categoryName = championshipCategory.getCategory().getTitle() + " - "
//					// + championshipCategory.getGender().toString();
//					System.out.println("Inside For championship id :" + championshipCategory.getId());
//					if (championshipCategory.getId() == 6 || championshipCategory.getId() == 7
//							|| championshipCategory.getId() == 8 || championshipCategory.getId() == 9
//							|| championshipCategory.getId() == 10) {
//						System.out.println("Inside If championship id :" + championshipCategory.getId());
//						continue;
//					}
//
//					String categoryName = "Under 18 Boys";
//					Integer categoryId = scheduleDetails.getFemaleCategoryId();
//					Integer eventId = scheduleDetails.getTraditionalFemaleEventId();
//					String eventDescription = championshipCategory.getAsanaCategory().getName();
//					String gender = championshipCategory.getGender().toString();
//					Integer noOfRounds = championshipCategory.getNoOfRounds();
//					Integer asanaCategoryId = championshipCategory.getAsanaCategory().getId();
//					List<RoundsDTO> listRounds = new ArrayList<>();
//					for (Integer round = 1; round <= noOfRounds; round++) {
//						String roundName = "Round " + round;
//						if (round == 1) {
//							roundName = "Round One";
//						} else if (round == 2) {
//							roundName = "Round Two";
//						}
//
//						ChampionshipRounds championshipRounds = championshipRoundsService
//								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
//										round);
//						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//								.getAllByChampionshipRounds(championshipRounds);
//
//						List<TeamsDTO> listTeams = new ArrayList<>();
//						if (asanaCategoryId == ASANA_CATEGORY_TRADITIONAL) {
//
//							if (!listChampionshipParticipantTeams.isEmpty()) {
//
//								for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//									String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//									String clubName = championshipParticipantTeams.getParticipantTeam()
//											.getParticipantTeamParticipants().get(0).getParticipant().getClub()
//											.getName();
//
//									String totalScore = "-";
//									String rank = "-";
//									ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//											.findByChampionshipParticipantTeamAndChampionshipRounds(
//													championshipParticipantTeams, championshipRounds);
//									if (participantTeamRoundTotalScoring != null) {
//										totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//										if(championshipParticipantTeams.getStatus() == StatusEnum.COMPLETED) {
//											rank = String.valueOf(participantTeamRoundTotalScoring.getRanking());		
//										}
//									}
//
//									List<AthletesDTO> listAtheletes = new ArrayList<>();
//									List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//											.getParticipantTeam().getParticipantTeamParticipants();
//									TeamScoresDTO teamScoresDTO = new TeamScoresDTO();
//									for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//										String prnNumber = Integer
//												.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//										String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//										if (kheloId == null) {
//											kheloId = "-";
//										}
//										String name = participantTeamParticipant.getParticipantFullName();
//										String image = "-";
//										if (participantTeamParticipant.getParticipant().getImage() != null) {
//											image = participantTeamParticipant.getParticipant().getImage();
//										}
//										ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//												.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//														championshipParticipantTeams, participantTeamParticipant,
//														round);
//										String playerTotalScore = "-";
//										if (participantTeamParticipantFinalScore != null) {
//											playerTotalScore = Float
//													.toString(participantTeamParticipantFinalScore.getTotalScore());
//										}
//
//										// populate asanas wise scores
//										List<AsanasDTO> listAsanas = new ArrayList<>();
//										ParticipantTeam participantTeam = championshipParticipantTeams
//												.getParticipantTeam();
//
//										List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
//												.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
//										for (ParticipantTeamAsanas asana : listParticipantTeamAsanas) {
//											String asanaId = Integer.toString(asana.getAsana().getId());
//											String asanaName = asana.getAsana().getName();
//											String displayName = asana.getAsana().getName();
//											String type = null;
//											if (asana.isCompulsory() == true) {
//												type = "compulsory";
//											} else {
//												type = "optional";
//											}
//
//											ParticipantTeamAsanaJudgeTotalScore asanaScoresForParticipants = participantTeamAsanaJudgeTotalScoreService
//													.findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType(
//															championshipParticipantTeams, participantTeamParticipant,
//															asana, judgeTypeService.get(D_Judge_Role));
//											String score = "-";
//											if (asanaScoresForParticipants != null) {
//												Float sumScores = asanaScoresForParticipants.getTotalScore();
//
//												score = scoreFormat.format(sumScores);
//											}
//											listAsanas.add(new AsanasDTO(asanaId, asanaName, displayName, type, score));
//										}
//
//										listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image,
//												playerTotalScore, listAsanas));
//									}
//
////									if (participantTeamRoundTotalScoring != null
////											&& participantTeamRoundTotalScoring.isWinner()) {
//									if (participantTeamRoundTotalScoring != null) {
//										listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes,rank));
//									}
//
//								}
//							}
//						} else {
//
//							if (!listChampionshipParticipantTeams.isEmpty()) {
//
//								for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//									String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//									String clubName = championshipParticipantTeams.getParticipantTeam()
//											.getParticipantTeamParticipants().get(0).getParticipant().getClub()
//											.getName();
//
//									String totalScore = "-";
//									String rank = "-";
//									ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//											.findByChampionshipParticipantTeamAndChampionshipRounds(
//													championshipParticipantTeams, championshipRounds);
//									if (participantTeamRoundTotalScoring != null) {
//										totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//										rank = String.valueOf(participantTeamRoundTotalScoring.getRanking());
//									}
//
//									List<AthletesDTO> listAtheletes = new ArrayList<>();
//									List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//											.getParticipantTeam().getParticipantTeamParticipants();
//									TeamScoresDTO teamScoresDTO = new TeamScoresDTO();
//									for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//										String prnNumber = Integer
//												.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//										String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//										if (kheloId == null) {
//											kheloId = "-";
//										}
//										String name = participantTeamParticipant.getParticipantFullName();
//										String image = "-";
//										if (participantTeamParticipant.getParticipant().getImage() != null) {
//											image = participantTeamParticipant.getParticipant().getImage();
//										}
//										ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//												.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//														championshipParticipantTeams, participantTeamParticipant,
//														round);
//										String playerTotalScore = "-";
//										if (participantTeamParticipantFinalScore != null) {
//											playerTotalScore = Float
//													.toString(participantTeamParticipantFinalScore.getTotalScore());
//										}
//
//										List<ParticipantTeamJudgeTotalScore> teamJudgeScores = participantTeamJudgeTotalScoreService
//												.getByTeamAndRoundAsList(championshipParticipantTeams, round);
//
//										for (ParticipantTeamJudgeTotalScore judgeScore : teamJudgeScores) {
//											if (judgeScore.getType().getId() == 2)
//												teamScoresDTO.setTotal_d_score(judgeScore.getTotalScore().toString());
//											else if (judgeScore.getType().getId() == 3)
//												teamScoresDTO.setTotal_a_score(judgeScore.getTotalScore().toString());
//											else if (judgeScore.getType().getId() == 4)
//												teamScoresDTO.setTotal_t_score(judgeScore.getTotalScore().toString());
//											else if (judgeScore.getType().getId() == 5)
//												teamScoresDTO.setTotal_e_score(judgeScore.getTotalScore().toString());
//
//										}
//										ParticipantTeamRoundTotalScoring p = participantTeamRoundTotalScoringService
//												.findByChampionshipParticipantTeamAndChampionshipRounds(
//														championshipParticipantTeams,
//														championshipParticipantTeams.getChampionshipRounds());
//										teamScoresDTO.setTotal_score(p != null ? p.getTotalScore().toString() : "-");
//										if(championshipParticipantTeams.getStatus() == StatusEnum.COMPLETED) {
//											teamScoresDTO.setRank(p != null ? String.valueOf(p.getRanking()) : "-");
//										} else {
//											teamScoresDTO.setRank("-");
//										}
//										listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image,
//												playerTotalScore, null));
//									}
//
////									if (participantTeamRoundTotalScoring != null
////											&& participantTeamRoundTotalScoring.isWinner()) {
//									if (participantTeamRoundTotalScoring != null) {
//										listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes,
//												teamScoresDTO));
//									}
//
//								}
//							}
//						}
//						
//						// sort the list by score desc
//						List<TeamsDTO> listSortedTeams = listTeams.stream().sorted(Comparator.comparing(TeamsDTO::getTotal_score, 
//								(s1, s2) -> (Float.parseFloat(s1) > Float.parseFloat(s2) ? 1 :-1)).reversed())
//								.collect(Collectors.toList());
//						
//						listRounds.add(new RoundsDTO(roundName, listSortedTeams));
//
//					}
//
//					listCategories.add(
//							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
//
//				}
//
//			}
//			listTeamLiveScores.setChampionship_name(championship.getName());
//			listTeamLiveScores.setChampionship_id(championship.getId());
//			listTeamLiveScores.setDescription(description);
//			listTeamLiveScores.setSport_id(sportId);
//			listTeamLiveScores.setSport_name(sportName);
//			listTeamLiveScores.setCategories(listCategories);
//
//		} catch (
//
//		ChampionshipNotFoundException e) {
//			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
//			// " + championshipId);
//			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
//		} catch (EntityNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return new ResponseEntity<LiveScoresDTO>(listTeamLiveScores, HttpStatus.OK);
//
//	}
//
//	// girls
//
//	@GetMapping(value = "/winners/girls", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getLiveScoresGirls() throws ChampionshipNotFoundException {
//		Integer scheduleId = 1;
//		Integer sportId;
//		String sportName;
//
//		try {
//			Schedule schedule = scheduleService.get(scheduleId);
//			sportId = schedule.getSport_id();
//			sportName = schedule.getSport_name();
//		} catch (EntityNotFoundException e) {
//			// throw new EntityNotFoundException("Schedule not found for the
//			// Championship!");
//			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
//		}
//		String description = scheduleDetails.getLivescoresDescription();
//
//		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
//		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
//		Integer championshipId = 1;
//		try {
//			Championship championship = championshipService.get(championshipId);
//
//			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//			if (!listChampionshipCategory.isEmpty()) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					// String categoryName = championshipCategory.getCategory().getTitle() + " - "
//					// + championshipCategory.getGender().toString();
//					System.out.println("Inside For championship id :" + championshipCategory.getId());
//					if (championshipCategory.getId() == 1 || championshipCategory.getId() == 2
//							|| championshipCategory.getId() == 3 || championshipCategory.getId() == 4
//							|| championshipCategory.getId() == 5) {
//						System.out.println("Inside If championship id :" + championshipCategory.getId());
//						continue;
//					}
//
//					String categoryName = "Under 18 Girls";
//					Integer categoryId = scheduleDetails.getFemaleCategoryId();
//					Integer eventId = scheduleDetails.getTraditionalFemaleEventId();
//					String eventDescription = championshipCategory.getAsanaCategory().getName();
//					String gender = championshipCategory.getGender().toString();
//					Integer noOfRounds = championshipCategory.getNoOfRounds();
//					Integer asanaCategoryId = championshipCategory.getAsanaCategory().getId();
//					List<RoundsDTO> listRounds = new ArrayList<>();
//					for (Integer round = 1; round <= noOfRounds; round++) {
//						String roundName = "Round " + round;
//						if (round == 1) {
//							roundName = "Round One";
//						} else if (round == 2) {
//							roundName = "Round Two";
//						}
//
//						ChampionshipRounds championshipRounds = championshipRoundsService
//								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
//										round);
//						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//								.getAllByChampionshipRounds(championshipRounds);
//
//						List<TeamsDTO> listTeams = new ArrayList<>();
//
//						if (asanaCategoryId == ASANA_CATEGORY_TRADITIONAL) {
//
//							if (!listChampionshipParticipantTeams.isEmpty()) {
//
//								for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//									String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//									String clubName = championshipParticipantTeams.getParticipantTeam()
//											.getParticipantTeamParticipants().get(0).getParticipant().getClub()
//											.getName();
//
//									String totalScore = "-";
//									String rank = "-";
//									ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//											.findByChampionshipParticipantTeamAndChampionshipRounds(
//													championshipParticipantTeams, championshipRounds);
//									if (participantTeamRoundTotalScoring != null) {
//										totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//										if(championshipParticipantTeams.getStatus() == StatusEnum.COMPLETED) {
//											rank = String.valueOf(participantTeamRoundTotalScoring.getRanking());	
//										}
//									}
//
//									List<AthletesDTO> listAtheletes = new ArrayList<>();
//									List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//											.getParticipantTeam().getParticipantTeamParticipants();
//									TeamScoresDTO teamScoresDTO = new TeamScoresDTO();
//									for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//										String prnNumber = Integer
//												.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//										String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//										if (kheloId == null) {
//											kheloId = "-";
//										}
//										String name = participantTeamParticipant.getParticipantFullName();
//										String image = "-";
//										if (participantTeamParticipant.getParticipant().getImage() != null) {
//											image = participantTeamParticipant.getParticipant().getImage();
//										}
//										ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//												.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//														championshipParticipantTeams, participantTeamParticipant,
//														round);
//										String playerTotalScore = "-";
//										if (participantTeamParticipantFinalScore != null) {
//											playerTotalScore = Float
//													.toString(participantTeamParticipantFinalScore.getTotalScore());
//										}
//
//										// populate asanas wise scores
//										List<AsanasDTO> listAsanas = new ArrayList<>();
//										ParticipantTeam participantTeam = championshipParticipantTeams
//												.getParticipantTeam();
//
//										List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
//												.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
//										for (ParticipantTeamAsanas asana : listParticipantTeamAsanas) {
//											String asanaId = Integer.toString(asana.getAsana().getId());
//											String asanaName = asana.getAsana().getName();
//											String displayName = asana.getAsana().getName();
//											String type = null;
//											if (asana.isCompulsory() == true) {
//												type = "compulsory";
//											} else {
//												type = "optional";
//											}
//
//											ParticipantTeamAsanaJudgeTotalScore asanaScoresForParticipants = participantTeamAsanaJudgeTotalScoreService
//													.findByChampionshipParticipantTeamsAndParticipantTeamParticipantsAndParticipantTeamAsanasAndType(
//															championshipParticipantTeams, participantTeamParticipant,
//															asana, judgeTypeService.get(D_Judge_Role));
//											String score = "-";
//											if (asanaScoresForParticipants != null) {
//												Float sumScores = asanaScoresForParticipants.getTotalScore();
//
//												score = scoreFormat.format(sumScores);
//											}
//											listAsanas.add(new AsanasDTO(asanaId, asanaName, displayName, type, score));
//										}
//
//										listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image,
//												playerTotalScore, listAsanas));
//									}
//
////									if (participantTeamRoundTotalScoring != null
////											&& participantTeamRoundTotalScoring.isWinner()) {
//									if (participantTeamRoundTotalScoring != null) {
//										listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes,rank));
//									}
//
//								}
//							}
//						} else {
//							if (!listChampionshipParticipantTeams.isEmpty()) {
//
//								for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//									String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//									String clubName = championshipParticipantTeams.getParticipantTeam()
//											.getParticipantTeamParticipants().get(0).getParticipant().getClub()
//											.getName();
//
//									String totalScore = "-";
//									String rank="-";
//									ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//											.findByChampionshipParticipantTeamAndChampionshipRounds(
//													championshipParticipantTeams, championshipRounds);
//									if (participantTeamRoundTotalScoring != null) {
//										totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//										if(championshipParticipantTeams.getStatus() == StatusEnum.COMPLETED) {
//											rank = String.valueOf(participantTeamRoundTotalScoring.getRanking());	
//										}
//									}
//
//									List<AthletesDTO> listAtheletes = new ArrayList<>();
//									List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//											.getParticipantTeam().getParticipantTeamParticipants();
//									TeamScoresDTO teamScoresDTO = new TeamScoresDTO();
//									for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//										String prnNumber = Integer
//												.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//										String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//										if (kheloId == null) {
//											kheloId = "-";
//										}
//										String name = participantTeamParticipant.getParticipantFullName();
//										String image = "-";
//										if (participantTeamParticipant.getParticipant().getImage() != null) {
//											image = participantTeamParticipant.getParticipant().getImage();
//										}
//										ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//												.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//														championshipParticipantTeams, participantTeamParticipant,
//														round);
//										String playerTotalScore = "-";
//										if (participantTeamParticipantFinalScore != null) {
//											playerTotalScore = Float
//													.toString(participantTeamParticipantFinalScore.getTotalScore());
//										}
//
//										List<ParticipantTeamJudgeTotalScore> teamJudgeScores = participantTeamJudgeTotalScoreService
//												.getByTeamAndRoundAsList(championshipParticipantTeams, round);
//
//										for (ParticipantTeamJudgeTotalScore judgeScore : teamJudgeScores) {
//											if (judgeScore.getType().getId() == 2)
//												teamScoresDTO.setTotal_d_score(judgeScore.getTotalScore().toString());
//											else if (judgeScore.getType().getId() == 3)
//												teamScoresDTO.setTotal_a_score(judgeScore.getTotalScore().toString());
//											else if (judgeScore.getType().getId() == 4)
//												teamScoresDTO.setTotal_t_score(judgeScore.getTotalScore().toString());
//											else if (judgeScore.getType().getId() == 5)
//												teamScoresDTO.setTotal_e_score(judgeScore.getTotalScore().toString());
//
//										}
//										ParticipantTeamRoundTotalScoring p = participantTeamRoundTotalScoringService
//												.findByChampionshipParticipantTeamAndChampionshipRounds(
//														championshipParticipantTeams,
//														championshipParticipantTeams.getChampionshipRounds());
//										teamScoresDTO.setTotal_score(p != null ? p.getTotalScore().toString() : "-");
//										if(championshipParticipantTeams.getStatus() == StatusEnum.COMPLETED) {
//											teamScoresDTO.setRank(p != null ? String.valueOf(p.getRanking()) : "-");
//										} else {
//											teamScoresDTO.setRank("-");
//										}
//										//teamScoresDTO.setRank(p != null ? p.getRanking().toString() : "-");
//										listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image,
//												playerTotalScore, null));
//									}
//									
//									
////									if (participantTeamRoundTotalScoring != null
////											&& participantTeamRoundTotalScoring.isWinner()) {
//									if (participantTeamRoundTotalScoring != null) {
//										listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes,
//												teamScoresDTO));
//									}
//
//								}
//							}
//						}
//						
//						
//
//						// sort the list by score desc
//						List<TeamsDTO> listSortedTeams = listTeams.stream().sorted(Comparator.comparing(TeamsDTO::getTotal_score, 
//								(s1, s2) -> (Float.parseFloat(s1) > Float.parseFloat(s2) ? 1 :-1)).reversed())
//								.collect(Collectors.toList());
//						
//						listRounds.add(new RoundsDTO(roundName, listSortedTeams));
//
//					}
//
//					listCategories.add(
//							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
//
//				}
//			}
//			listTeamLiveScores.setChampionship_name(championship.getName());
//			listTeamLiveScores.setChampionship_id(championship.getId());
//			listTeamLiveScores.setDescription(description);
//			listTeamLiveScores.setSport_id(sportId);
//			listTeamLiveScores.setSport_name(sportName);
//			listTeamLiveScores.setCategories(listCategories);
//
//		} catch (
//
//		ChampionshipNotFoundException e) {
//			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
//			// " + championshipId);
//			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
//		} catch (EntityNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return new ResponseEntity<LiveScoresDTO>(listTeamLiveScores, HttpStatus.OK);
//
//	}
//
//	// ************************************************** unused APIs
//	// *********************************************
//
//	@GetMapping(value = "/winners/Boys", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getWinnersMale() throws ChampionshipNotFoundException {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/livescores-male.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//
//	}
//
//	@GetMapping(value = "/winners/Girls", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getWinnersFemale() throws ChampionshipNotFoundException {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/livescores-female.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//
//	}
//
//	@GetMapping(value = "/winners/traditional/boys", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getWinnersTraditionalBoys()
//			throws ChampionshipNotFoundException, AsanaCategoryNotFoundException {
//		Integer scheduleId = 1;
//		Integer sportId;
//		String sportName;
//
//		try {
//			Schedule schedule = scheduleService.get(scheduleId);
//			sportId = schedule.getSport_id();
//			sportName = schedule.getSport_name();
//		} catch (EntityNotFoundException e) {
//			// throw new EntityNotFoundException("Schedule not found for the
//			// Championship!");
//			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
//		}
//		String description = scheduleDetails.getWinnersDescription();
//
//		WinnersDTO listTeamWinners = new WinnersDTO();
//		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
//		Integer championshipId = 1;
//		try {
//			Championship championship = championshipService.get(championshipId);
//
//			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//			if (!listChampionshipCategory.isEmpty()) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					// String categoryName = championshipCategory.getCategory().getTitle() + " - "
//					// + championshipCategory.getGender().toString();
//					// System.out.println("championship id :" +
//					// championshipCategory.getAsanaCategory().getName());
//					if (championshipCategory.getId() != 1)
//						continue;
//					String categoryName = "Under 18 Boys";
//					Integer categoryId = scheduleDetails.getMaleCategoryId();
//					Integer eventId = scheduleDetails.getTraditionalMaleEventId();
//					String eventDescription = championshipCategory.getAsanaCategory().getName();
//					String gender = championshipCategory.getGender().toString();
//					Integer noOfRounds = championshipCategory.getNoOfRounds();
//					List<RoundsDTO> listRounds = new ArrayList<>();
//					for (Integer round = 1; round <= noOfRounds; round++) {
//						String roundName = "Round " + round;
//						if (round == 1) {
//							roundName = "Round One";
//						} else if (round == 2) {
//							roundName = "Round Two";
//						}
//
//						ChampionshipRounds championshipRounds = championshipRoundsService
//								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
//										round);
//						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//								.getAllByChampionshipRounds(championshipRounds);
//
//						List<TeamsDTO> listTeams = new ArrayList<>();
//						if (!listChampionshipParticipantTeams.isEmpty()) {
//
//							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//								String clubName = championshipParticipantTeams.getParticipantTeam()
//										.getParticipantTeamParticipants().get(0).getParticipant().getClub().getName();
//
//								String totalScore = "-";
//								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//										.findByChampionshipParticipantTeamAndChampionshipRounds(
//												championshipParticipantTeams, championshipRounds);
//								if (participantTeamRoundTotalScoring != null) {
//									totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//								}
//
//								List<AthletesDTO> listAtheletes = new ArrayList<>();
//								List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//										.getParticipantTeam().getParticipantTeamParticipants();
//								for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//									String prnNumber = Integer
//											.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//									String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//									if (kheloId == null) {
//										kheloId = "-";
//									}
//									String name = participantTeamParticipant.getParticipantFullName();
//									String image = "-";
//									if (participantTeamParticipant.getParticipant().getImage() != null) {
//										image = participantTeamParticipant.getParticipant().getImage();
//									}
//									ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//											.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//													championshipParticipantTeams, participantTeamParticipant, round);
//									String playerTotalScore = "-";
//									if (participantTeamParticipantFinalScore != null) {
//										playerTotalScore = Float
//												.toString(participantTeamParticipantFinalScore.getTotalScore());
//									}
//
//									List<AsanasDTO> listAsanas = new ArrayList<>();
//									ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
//
//									List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
//											.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
//									for (ParticipantTeamAsanas asana : listParticipantTeamAsanas) {
//										String asanaId = Integer.toString(asana.getAsana().getId());
//										String asanaName = asana.getAsana().getName();
//										String displayName = asana.getAsana().getName();
//										String type = null;
//										if (asana.isCompulsory() == true) {
//											type = "compulsory";
//										} else {
//											type = "optional";
//										}
//
//										List<ParticipantTeamAsanaScoring> listScoresForParticipants = participantTeamAsanasScoringService
//												.findAllByChampionhsipParticipantTeamsAndParticipantTeamParticipantAndByParticipantTeamAsanas(
//														championshipParticipantTeams, participantTeamParticipant,
//														asana);
//										String score = "-";
//										if (!listScoresForParticipants.isEmpty()) {
//											Float sumScores = (float) 0.0;
//											for (ParticipantTeamAsanaScoring scores : listScoresForParticipants) {
//												if (scores.getScore() != null) {
//													sumScores = sumScores + scores.getScore();
//												}
//											}
//											score = scoreFormat.format(sumScores);
//										}
//										listAsanas.add(new AsanasDTO(asanaId, asanaName, displayName, type, score));
//									}
//
//									listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image, playerTotalScore,
//											listAsanas));
//								}
//
//								if (participantTeamRoundTotalScoring != null
//										&& participantTeamRoundTotalScoring.isWinner()) {
//
//									listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes));
//								}
//
//							}
//						}
//
//						listRounds.add(new RoundsDTO(roundName, listTeams));
//
//					}
//
//					listCategories.add(
//							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
//
//				}
//			}
//			listTeamWinners.setChampionship_name(championship.getName());
//			listTeamWinners.setChampionship_id(championship.getId());
//			listTeamWinners.setDescription(description);
//			listTeamWinners.setSport_id(sportId);
//			listTeamWinners.setSport_name(sportName);
//			listTeamWinners.setCategories(listCategories);
//
//		} catch (
//
//		ChampionshipNotFoundException e) {
//			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
//			// " + championshipId);
//			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
//		}
//
//		return new ResponseEntity<WinnersDTO>(listTeamWinners, HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/winners/traditional/girls", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getWinnersTraditionalGirls() throws ChampionshipNotFoundException {
//		Integer scheduleId = 1;
//		Integer sportId;
//		String sportName;
//
//		try {
//			Schedule schedule = scheduleService.get(scheduleId);
//			sportId = schedule.getSport_id();
//			sportName = schedule.getSport_name();
//		} catch (EntityNotFoundException e) {
//			// throw new EntityNotFoundException("Schedule not found for the
//			// Championship!");
//			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
//		}
//		String description = scheduleDetails.getLivescoresDescription();
//
//		WinnersDTO listTeamWinners = new WinnersDTO();
//		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
//		Integer championshipId = 1;
//		try {
//			Championship championship = championshipService.get(championshipId);
//
//			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//			if (!listChampionshipCategory.isEmpty()) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					// String categoryName = championshipCategory.getCategory().getTitle() + " - "
//					// + championshipCategory.getGender().toString();
//					System.out.println("championship id :" + championshipCategory.getId());
//					if (championshipCategory.getId() != 6)
//						continue;
//					String categoryName = "Under 18 Girls";
//					Integer categoryId = scheduleDetails.getFemaleCategoryId();
//					Integer eventId = scheduleDetails.getTraditionalFemaleEventId();
//					String eventDescription = championshipCategory.getAsanaCategory().getName();
//					String gender = championshipCategory.getGender().toString();
//					Integer noOfRounds = championshipCategory.getNoOfRounds();
//					List<RoundsDTO> listRounds = new ArrayList<>();
//					for (Integer round = 1; round <= noOfRounds; round++) {
//						String roundName = "Round " + round;
//						if (round == 1) {
//							roundName = "Round One";
//						} else if (round == 2) {
//							roundName = "Round Two";
//						}
//
//						ChampionshipRounds championshipRounds = championshipRoundsService
//								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
//										round);
//						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//								.getAllByChampionshipRounds(championshipRounds);
//
//						List<TeamsDTO> listTeams = new ArrayList<>();
//						if (!listChampionshipParticipantTeams.isEmpty()) {
//
//							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//								String clubName = championshipParticipantTeams.getParticipantTeam()
//										.getParticipantTeamParticipants().get(0).getParticipant().getClub().getName();
//
//								String totalScore = "-";
//								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//										.findByChampionshipParticipantTeamAndChampionshipRounds(
//												championshipParticipantTeams, championshipRounds);
//								if (participantTeamRoundTotalScoring != null) {
//									totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//								}
//
//								List<AthletesDTO> listAtheletes = new ArrayList<>();
//								List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//										.getParticipantTeam().getParticipantTeamParticipants();
//								for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//									String prnNumber = Integer
//											.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//									String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//									if (kheloId == null) {
//										kheloId = "-";
//									}
//									String name = participantTeamParticipant.getParticipantFullName();
//									String image = "-";
//									if (participantTeamParticipant.getParticipant().getImage() != null) {
//										image = participantTeamParticipant.getParticipant().getImage();
//									}
//									ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//											.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//													championshipParticipantTeams, participantTeamParticipant, round);
//									String playerTotalScore = "-";
//									if (participantTeamParticipantFinalScore != null) {
//										playerTotalScore = Float
//												.toString(participantTeamParticipantFinalScore.getTotalScore());
//									}
//
//									List<AsanasDTO> listAsanas = new ArrayList<>();
//									ParticipantTeam participantTeam = championshipParticipantTeams.getParticipantTeam();
//
//									List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
//											.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
//									for (ParticipantTeamAsanas asana : listParticipantTeamAsanas) {
//										String asanaId = Integer.toString(asana.getAsana().getId());
//										String asanaName = asana.getAsana().getName();
//										String displayName = asana.getAsana().getName();
//										String type = null;
//										if (asana.isCompulsory() == true) {
//											type = "compulsory";
//										} else {
//											type = "optional";
//										}
//
//										List<ParticipantTeamAsanaScoring> listScoresForParticipants = participantTeamAsanasScoringService
//												.findAllByChampionhsipParticipantTeamsAndParticipantTeamParticipantAndByParticipantTeamAsanas(
//														championshipParticipantTeams, participantTeamParticipant,
//														asana);
//										String score = "-";
//										if (!listScoresForParticipants.isEmpty()) {
//											Float sumScores = (float) 0.0;
//											for (ParticipantTeamAsanaScoring scores : listScoresForParticipants) {
//												if (scores.getScore() != null) {
//													sumScores = sumScores + scores.getScore();
//												}
//											}
//											score = scoreFormat.format(sumScores);
//										}
//										listAsanas.add(new AsanasDTO(asanaId, asanaName, displayName, type, score));
//									}
//
//									listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image, playerTotalScore,
//											listAsanas));
//								}
//
//								if (participantTeamRoundTotalScoring != null
//										&& participantTeamRoundTotalScoring.isWinner()) {
//									listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes));
//								}
//								// listTeams.add(new TeamsDTO(chestId, clubName, totalScore, listAtheletes));
//
//							}
//						}
//
//						listRounds.add(new RoundsDTO(roundName, listTeams));
//
//					}
//
//					listCategories.add(
//							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
//
//				}
//			}
//			listTeamWinners.setChampionship_name(championship.getName());
//			listTeamWinners.setChampionship_id(championship.getId());
//			listTeamWinners.setDescription(description);
//			listTeamWinners.setSport_id(sportId);
//			listTeamWinners.setSport_name(sportName);
//			listTeamWinners.setCategories(listCategories);
//
//		} catch (
//
//		ChampionshipNotFoundException e) {
//			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
//			// " + championshipId);
//			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
//		}
//
//		return new ResponseEntity<WinnersDTO>(listTeamWinners, HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/winners/teams/boys", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getWinnersTeamsMale() throws ChampionshipNotFoundException {
//		Integer scheduleId = 1;
//		Integer sportId;
//		String sportName;
//
//		try {
//			Schedule schedule = scheduleService.get(scheduleId);
//			sportId = schedule.getSport_id();
//			sportName = schedule.getSport_name();
//		} catch (EntityNotFoundException e) {
//			// throw new EntityNotFoundException("Schedule not found for the
//			// Championship!");
//			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
//		}
//		String description = scheduleDetails.getLivescoresDescription();
//
//		WinnersDTO listTeamWinners = new WinnersDTO();
//		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
//		Integer championshipId = 1;
//		try {
//			Championship championship = championshipService.get(championshipId);
//
//			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//			if (!listChampionshipCategory.isEmpty()) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					// String categoryName = championshipCategory.getCategory().getTitle() + " - "
//					// + championshipCategory.getGender().toString();
//					System.out.println("Inside For championship id :" + championshipCategory.getId());
//					if (championshipCategory.getId() == 1 || championshipCategory.getId() == 6
//							|| championshipCategory.getId() == 7 || championshipCategory.getId() == 8
//							|| championshipCategory.getId() == 9 || championshipCategory.getId() == 10) {
//						System.out.println("Inside If championship id :" + championshipCategory.getId());
//						continue;
//					}
//
//					String categoryName = "Under 18 Boys";
//					Integer categoryId = scheduleDetails.getFemaleCategoryId();
//					Integer eventId = scheduleDetails.getTraditionalFemaleEventId();
//					String eventDescription = championshipCategory.getAsanaCategory().getName();
//					String gender = championshipCategory.getGender().toString();
//					Integer noOfRounds = championshipCategory.getNoOfRounds();
//					List<RoundsDTO> listRounds = new ArrayList<>();
//					for (Integer round = 1; round <= noOfRounds; round++) {
//						String roundName = "Round " + round;
//						if (round == 1) {
//							roundName = "Round One";
//						} else if (round == 2) {
//							roundName = "Round Two";
//						}
//
//						ChampionshipRounds championshipRounds = championshipRoundsService
//								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
//										round);
//						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//								.getAllByChampionshipRounds(championshipRounds);
//
//						List<TeamsDTO> listTeams = new ArrayList<>();
//						if (!listChampionshipParticipantTeams.isEmpty()) {
//
//							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//								String clubName = championshipParticipantTeams.getParticipantTeam()
//										.getParticipantTeamParticipants().get(0).getParticipant().getClub().getName();
//
//								String totalScore = "-";
//								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//										.findByChampionshipParticipantTeamAndChampionshipRounds(
//												championshipParticipantTeams, championshipRounds);
//								if (participantTeamRoundTotalScoring != null) {
//									totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//								}
//
//								List<AthletesDTO> listAtheletes = new ArrayList<>();
//								List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//										.getParticipantTeam().getParticipantTeamParticipants();
//								TeamScoresDTO teamScoresDTO = new TeamScoresDTO();
//								for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//									String prnNumber = Integer
//											.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//									String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//									if (kheloId == null) {
//										kheloId = "-";
//									}
//									String name = participantTeamParticipant.getParticipantFullName();
//									String image = "-";
//									if (participantTeamParticipant.getParticipant().getImage() != null) {
//										image = participantTeamParticipant.getParticipant().getImage();
//									}
//									ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//											.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//													championshipParticipantTeams, participantTeamParticipant, round);
//									String playerTotalScore = "-";
//									if (participantTeamParticipantFinalScore != null) {
//										playerTotalScore = Float
//												.toString(participantTeamParticipantFinalScore.getTotalScore());
//									}
//
//									List<ParticipantTeamJudgeTotalScore> teamJudgeScores = participantTeamJudgeTotalScoreService
//											.getByTeamAndRoundAsList(championshipParticipantTeams, round);
//
//									for (ParticipantTeamJudgeTotalScore judgeScore : teamJudgeScores) {
//										if (judgeScore.getType().getId() == 2)
//											teamScoresDTO.setTotal_d_score(judgeScore.getTotalScore().toString());
//										else if (judgeScore.getType().getId() == 3)
//											teamScoresDTO.setTotal_a_score(judgeScore.getTotalScore().toString());
//										else if (judgeScore.getType().getId() == 4)
//											teamScoresDTO.setTotal_t_score(judgeScore.getTotalScore().toString());
//										else if (judgeScore.getType().getId() == 5)
//											teamScoresDTO.setTotal_e_score(judgeScore.getTotalScore().toString());
//
//									}
//									ParticipantTeamRoundTotalScoring p = participantTeamRoundTotalScoringService
//											.findByChampionshipParticipantTeamAndChampionshipRounds(
//													championshipParticipantTeams,
//													championshipParticipantTeams.getChampionshipRounds());
//									teamScoresDTO.setTotal_score(p != null ? p.getTotalScore().toString() : "-");
//									teamScoresDTO.setRank(p != null ? String.valueOf(p.getRanking()) : "-");
//									//teamScoresDTO.setRank(p != null ? p.getRanking().toString() : "-");
//									listAtheletes.add(
//											new AthletesDTO(prnNumber, kheloId, name, image, playerTotalScore, null));
//								}
//								if (participantTeamRoundTotalScoring != null
//										&& participantTeamRoundTotalScoring.isWinner()) {
//									listTeams.add(
//											new TeamsDTO(chestId, clubName, totalScore, listAtheletes, teamScoresDTO));
//								}
//							}
//						}
//
//						listRounds.add(new RoundsDTO(roundName, listTeams));
//
//					}
//
//					listCategories.add(
//							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
//
//				}
//			}
//			listTeamWinners.setChampionship_name(championship.getName());
//			listTeamWinners.setChampionship_id(championship.getId());
//			listTeamWinners.setDescription(description);
//			listTeamWinners.setSport_id(sportId);
//			listTeamWinners.setSport_name(sportName);
//			listTeamWinners.setCategories(listCategories);
//
//		} catch (
//
//		ChampionshipNotFoundException e) {
//			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
//			// " + championshipId);
//			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
//		}
//
//		return new ResponseEntity<WinnersDTO>(listTeamWinners, HttpStatus.OK);
//
//	}
//
//	@GetMapping(value = "/winners/teams/girls", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getWinnersTeamsFemale() throws ChampionshipNotFoundException {
//		Integer scheduleId = 1;
//		Integer sportId;
//		String sportName;
//
//		try {
//			Schedule schedule = scheduleService.get(scheduleId);
//			sportId = schedule.getSport_id();
//			sportName = schedule.getSport_name();
//		} catch (EntityNotFoundException e) {
//			// throw new EntityNotFoundException("Schedule not found for the
//			// Championship!");
//			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
//		}
//		String description = scheduleDetails.getLivescoresDescription();
//
//		WinnersDTO listTeamWinners = new WinnersDTO();
//		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
//		Integer championshipId = 1;
//		try {
//			Championship championship = championshipService.get(championshipId);
//
//			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
//			if (!listChampionshipCategory.isEmpty()) {
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					// String categoryName = championshipCategory.getCategory().getTitle() + " - "
//					// + championshipCategory.getGender().toString();
//					System.out.println("Inside For championship id :" + championshipCategory.getId());
//					if (championshipCategory.getId() == 1 || championshipCategory.getId() == 6
//							|| championshipCategory.getId() == 2 || championshipCategory.getId() == 3
//							|| championshipCategory.getId() == 4 || championshipCategory.getId() == 5) {
//						System.out.println("Inside If championship id :" + championshipCategory.getId());
//						continue;
//					}
//
//					String categoryName = "Under 18 Boys";
//					Integer categoryId = scheduleDetails.getFemaleCategoryId();
//					Integer eventId = scheduleDetails.getTraditionalFemaleEventId();
//					String eventDescription = championshipCategory.getAsanaCategory().getName();
//					String gender = championshipCategory.getGender().toString();
//					Integer noOfRounds = championshipCategory.getNoOfRounds();
//					List<RoundsDTO> listRounds = new ArrayList<>();
//					for (Integer round = 1; round <= noOfRounds; round++) {
//						String roundName = "Round " + round;
//						if (round == 1) {
//							roundName = "Round One";
//						} else if (round == 2) {
//							roundName = "Round Two";
//						}
//
//						ChampionshipRounds championshipRounds = championshipRoundsService
//								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
//										round);
//						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
//								.getAllByChampionshipRounds(championshipRounds);
//
//						List<TeamsDTO> listTeams = new ArrayList<>();
//						if (!listChampionshipParticipantTeams.isEmpty()) {
//
//							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
//								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
//								String clubName = championshipParticipantTeams.getParticipantTeam()
//										.getParticipantTeamParticipants().get(0).getParticipant().getClub().getName();
//
//								String totalScore = "-";
//								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
//										.findByChampionshipParticipantTeamAndChampionshipRounds(
//												championshipParticipantTeams, championshipRounds);
//								if (participantTeamRoundTotalScoring != null) {
//									totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
//								}
//
//								List<AthletesDTO> listAtheletes = new ArrayList<>();
//								List<ParticipantTeamParticipants> listparticipantTeamParticipants = championshipParticipantTeams
//										.getParticipantTeam().getParticipantTeamParticipants();
//								TeamScoresDTO teamScoresDTO = new TeamScoresDTO();
//								for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
//									String prnNumber = Integer
//											.toString(participantTeamParticipant.getParticipant().getPrnNumber());
//									String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
//									if (kheloId == null) {
//										kheloId = "-";
//									}
//									String name = participantTeamParticipant.getParticipantFullName();
//									String image = "-";
//									if (participantTeamParticipant.getParticipant().getImage() != null) {
//										image = participantTeamParticipant.getParticipant().getImage();
//									}
//									ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
//											.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
//													championshipParticipantTeams, participantTeamParticipant, round);
//									String playerTotalScore = "-";
//									if (participantTeamParticipantFinalScore != null) {
//										playerTotalScore = Float
//												.toString(participantTeamParticipantFinalScore.getTotalScore());
//									}
//
//									List<ParticipantTeamJudgeTotalScore> teamJudgeScores = participantTeamJudgeTotalScoreService
//											.getByTeamAndRoundAsList(championshipParticipantTeams, round);
//
//									for (ParticipantTeamJudgeTotalScore judgeScore : teamJudgeScores) {
//										if (judgeScore.getType().getId() == 2)
//											teamScoresDTO.setTotal_d_score(judgeScore.getTotalScore().toString());
//										else if (judgeScore.getType().getId() == 3)
//											teamScoresDTO.setTotal_a_score(judgeScore.getTotalScore().toString());
//										else if (judgeScore.getType().getId() == 4)
//											teamScoresDTO.setTotal_t_score(judgeScore.getTotalScore().toString());
//										else if (judgeScore.getType().getId() == 5)
//											teamScoresDTO.setTotal_e_score(judgeScore.getTotalScore().toString());
//
//									}
//									ParticipantTeamRoundTotalScoring p = participantTeamRoundTotalScoringService
//											.findByChampionshipParticipantTeamAndChampionshipRounds(
//													championshipParticipantTeams,
//													championshipParticipantTeams.getChampionshipRounds());
//									teamScoresDTO.setTotal_score(p != null ? p.getTotalScore().toString() : "-");
//									teamScoresDTO.setRank(p != null ? String.valueOf(p.getRanking()) : "-");
//									//teamScoresDTO.setRank(p != null ? p.getRanking().toString() : "-");
//									listAtheletes.add(
//											new AthletesDTO(prnNumber, kheloId, name, image, playerTotalScore, null));
//								}
//								if (participantTeamRoundTotalScoring != null
//										&& participantTeamRoundTotalScoring.isWinner()) {
//									listTeams.add(
//											new TeamsDTO(chestId, clubName, totalScore, listAtheletes, teamScoresDTO));
//								}
//							}
//						}
//
//						listRounds.add(new RoundsDTO(roundName, listTeams));
//
//					}
//
//					listCategories.add(
//							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
//
//				}
//			}
//			listTeamWinners.setChampionship_name(championship.getName());
//			listTeamWinners.setChampionship_id(championship.getId());
//			listTeamWinners.setDescription(description);
//			listTeamWinners.setSport_id(sportId);
//			listTeamWinners.setSport_name(sportName);
//			listTeamWinners.setCategories(listCategories);
//
//		} catch (
//
//		ChampionshipNotFoundException e) {
//			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
//			// " + championshipId);
//			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
//		}
//
//		return new ResponseEntity<WinnersDTO>(listTeamWinners, HttpStatus.OK);
//
//	}
//
//}
