//package com.swayaan.nysf.APIs;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.text.DecimalFormat;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.swayaan.nysf.entity.ScheduleDetails;
//import com.swayaan.nysf.exception.ChampionshipNotFoundException;
//import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
//import com.swayaan.nysf.service.ChampionshipRoundsService;
//import com.swayaan.nysf.service.ChampionshipService;
//import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
//import com.swayaan.nysf.service.ParticipantTeamAsanasScoringService;
//import com.swayaan.nysf.service.ParticipantTeamAsanasService;
//import com.swayaan.nysf.service.ParticipantTeamParticipantFinalScoreService;
//import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
//import com.swayaan.nysf.service.ScheduleService;
//
//@RestController
//@RequestMapping("/api/v1/championship/kheloindia")
//public class KIWinnersRestController2 {
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
//	private ScheduleDetails scheduleDetails;
//	DecimalFormat scoreFormat = new DecimalFormat("0.00");
//
//	@GetMapping(value = "/winners/Boys", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getWinnersMale() throws ChampionshipNotFoundException {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/winners-male.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//	}
//
//	@GetMapping(value = "/winners/Girls", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getWinnersFemale() throws ChampionshipNotFoundException {
//		String json = "";
//		try (InputStream stream = getClass().getResourceAsStream("/winners-female.json")) {
//			json = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//		} catch (IOException ioe) {
//			System.out.println("Couldn't fetch JSON! Error: " + ioe.getMessage());
//		}
//		return json;
//	}
//
////	@GetMapping(value = "/winners", produces = MediaType.APPLICATION_JSON_VALUE)
////	public ResponseEntity<?> getWinners() throws ChampionshipNotFoundException {
////		Integer scheduleId = 1;
////		String description = scheduleDetails.getWinnersDescription();
////		Integer sportId;
////		String sportName;
////
////		try {
////			Schedule schedule = scheduleService.get(scheduleId);
////			sportId = schedule.getSport_id();
////			sportName = schedule.getSport_name();
////		} catch (EntityNotFoundException e) {
////			// throw new EntityNotFoundException("Schedule not found for the
////			// Championship!");
////			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
////		}
////
////		WinnersDTO listWinners = new WinnersDTO();
////		List<CategoriesDTO> listCategories = new ArrayList<CategoriesDTO>();
////		Integer championshipId = 1;
////		try {
////			Championship championship = championshipService.get(championshipId);
////
////			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
////			if (!listChampionshipCategory.isEmpty()) {
////				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
////					//String categoryName = championshipCategory.getCategory().getTitle() + " - "
////					//		+ championshipCategory.getGender().toString();
////					String categoryName = "-";
////					Integer categoryId = null;
////					Integer eventId = null;
////					if (championshipCategory.getGender().toString() == "Male") {
////						categoryId = scheduleDetails.getMaleCategoryId();
////						eventId = scheduleDetails.getMaleEventId();
////						categoryName = "Under 26 Boys";
////					} else {
////						categoryId = scheduleDetails.getFemaleCategoryId();
////						eventId =scheduleDetails.getFemaleEventId();
////						categoryName = "Under 26 Girls";
////					}
////
////					String eventDescription = "Yogasana Event";
////					String gender = championshipCategory.getGender().toString();
////					Integer noOfRounds = championshipCategory.getNoOfRounds();
////					List<RoundsDTO> listRounds = new ArrayList<>();
////					for (Integer round = 1; round <= noOfRounds; round++) {
////						
////						String roundName = "Round " + round;
////						if (round == 1) {
////							roundName = "Round One";
////						} else if (round == 2) {
////							roundName = "Round Two";
////						}
////						boolean winner = true;
////						ChampionshipRounds championshipRounds = championshipRoundsService
////								.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
////										round);
////						List<ParticipantTeamRoundTotalScoring> listChampionshipParticipantTeams = participantTeamRoundTotalScoringService
////								.findByChampionshipAndChampionshipRoundsAndWinner(championship, championshipRounds,
////										winner);
////
////						List<TeamsDTO> listTeams = new ArrayList<>();
////						if (!listChampionshipParticipantTeams.isEmpty()) {
////
////							for (ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring : listChampionshipParticipantTeams) {
////								String chestId = participantTeamRoundTotalScoring.getChampionshipParticipantTeams()
////										.getParticipantTeam().getChestNumber();
////								String clubName = participantTeamRoundTotalScoring.getChampionshipParticipantTeams()
////										.getParticipantTeam().getParticipantTeamParticipants().get(0).getParticipant()
////										.getClub().getName();
////								Integer rank = 0;
////								String totalScore = "-";
////								if (participantTeamRoundTotalScoring != null) {
////									rank = participantTeamRoundTotalScoring.getRanking();
////									totalScore = Float.toString(participantTeamRoundTotalScoring.getTotalScore());
////								}
////
////								List<AthletesDTO> listAtheletes = new ArrayList<>();
////								List<ParticipantTeamParticipants> listparticipantTeamParticipants = participantTeamRoundTotalScoring
////										.getChampionshipParticipantTeams().getParticipantTeam()
////										.getParticipantTeamParticipants();
////								for (ParticipantTeamParticipants participantTeamParticipant : listparticipantTeamParticipants) {
////									String prnNumber = Integer
////											.toString(participantTeamParticipant.getParticipant().getPrnNumber());
////									String kheloId = participantTeamParticipant.getParticipant().getKheloIndiaId();
////									if (kheloId == null) {
////										kheloId = "-";
////									}
////									String name = participantTeamParticipant.getParticipantFullName();
////									String image = "-";
////									if (participantTeamParticipant.getParticipant().getImage() != null) {
////										image = participantTeamParticipant.getParticipant().getImage();
////									}
////
////									ParticipantTeamParticipantFinalScore participantTeamParticipantFinalScore = participantTeamParticipantFinalScoreService
////											.findByChampionshipParticipantTeamAndParticipantTeamParticipantAndRound(
////													participantTeamRoundTotalScoring.getChampionshipParticipantTeams(),
////													participantTeamParticipant, round);
////									String playerTotalScore = "-";
////									if (participantTeamParticipantFinalScore != null) {
////										playerTotalScore = Float
////												.toString(participantTeamParticipantFinalScore.getTotalScore());
////									}
////
////									List<AsanasDTO> listAsanas = new ArrayList<>();
////									ParticipantTeam participantTeam = participantTeamRoundTotalScoring
////											.getChampionshipParticipantTeams().getParticipantTeam();
////
////									List<ParticipantTeamAsanas> listParticipantTeamAsanas = participantTeamAsanasService
////											.getByTeamAndRoundOrderBySequenceNum(participantTeam, round);
////									for (ParticipantTeamAsanas asana : listParticipantTeamAsanas) {
////										String asanaId = Integer.toString(asana.getAsana().getId());
////										String asanaName = asana.getAsana().getName();
////										String displayName = asana.getAsana().getName();
////										String type = null;
////										if (asana.isCompulsory() == true) {
////											type = "compulsory";
////										} else {
////											type = "optional";
////										}
////
////										List<ParticipantTeamAsanaScoring> listScoresForParticipants = participantTeamAsanasScoringService
////												.findAllByChampionhsipParticipantTeamsAndParticipantTeamParticipantAndByParticipantTeamAsanas(
////														participantTeamRoundTotalScoring
////																.getChampionshipParticipantTeams(),
////														participantTeamParticipant, asana);
////										String score = "-";
////										if (!listScoresForParticipants.isEmpty()) {
////											Float sumScores = (float) 0.0;
////											for (ParticipantTeamAsanaScoring scores : listScoresForParticipants) {
////												sumScores = sumScores + scores.getScore();
////											}
////											score = scoreFormat.format(sumScores);
////										}
////										listAsanas.add(new AsanasDTO(asanaId, asanaName, displayName, type, score));
////									}
////
////									listAtheletes.add(new AthletesDTO(prnNumber, kheloId, name, image, playerTotalScore,
////											listAsanas));
////								}
////								listTeams.add(new TeamsDTO(chestId, clubName, rank, totalScore, listAtheletes));
////
////							}
////						}
////
////						listRounds.add(new RoundsDTO(roundName, listTeams));
////
////					}
////
////					listCategories.add(
////							new CategoriesDTO(categoryName, categoryId, eventId, eventDescription, gender, listRounds));
////
////				}
////			}
////			listWinners.setChampionship_name(championship.getName());
////			listWinners.setChampionship_id(championship.getId());
////			listWinners.setDescription(description);
////			listWinners.setSport_id(sportId);
////			listWinners.setSport_name(sportName);
////			listWinners.setCategories(listCategories);
////
////		} catch (
////
////		ChampionshipNotFoundException e) {
////			// throw new ChampionshipNotFoundException("Championship doesnot exist with this
////			// " + championshipId);
////			return new ResponseEntity<String>("Championship doesnot exist", HttpStatus.BAD_REQUEST);
////		}
////
////		return new ResponseEntity<WinnersDTO>(listWinners, HttpStatus.OK);
////	}
//
//}
