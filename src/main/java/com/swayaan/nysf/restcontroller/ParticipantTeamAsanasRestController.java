package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.API.entity.livescores.AsanasDTO;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamParticipantAsanasStatusService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ParticipantTeamAsanasRestController {

	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	UserService userService;

	@Autowired
	AsanaService asanaService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ParticipantTeamParticipantAsanasStatusService participantTeamParticipantAsanasStatusService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;

	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipRoundsService ChampionshipRoundsService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamAsanasRestController.class);

	@PostMapping("/teamAsanas/updateSequenceNumber/{id}/round/{roundNumber}/ptpId/{ptpId}")
	public String saveSequenceNumber(@Param("seqNum") Integer seqNum, @PathVariable(name = "id") Integer id,
			@PathVariable(name = "roundNumber") Integer roundNumber,@PathVariable(name = "ptpId") Integer ptpId, Model model, RedirectAttributes redirectAttributes)
			throws ParticipantTeamNotFoundException, NoSuchElementException {
		LOGGER.info("Entered saveSequenceNumber ParticipantTeamAsanasRestController");
		LOGGER.info("seqNum" + seqNum);
		// function exceutes when different asanas are assigned to all participants
		try {
			ParticipantTeamAsanas participantTeamAsanas = participantTeamAsanasService.get(id);

			// get the count of asanas for one participant and apply same for all
			ParticipantTeam participantTeam = participantTeamAsanas.getParticipantTeam();
//			ParticipantTeamParticipants participantTeamParticipant = participantTeamAsanas
//					.getParticipantTeamParticipants();
			ParticipantTeamParticipants participantTeamParticipant = participantTeamParticipantsService.get(ptpId);

			// Integer roundNumber = participantTeamAsanas.getRoundNumber();
			List<ParticipantTeamAsanas> listAsanasForParticipant = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
							roundNumber);
			Integer asanaCount = listAsanasForParticipant.size();

			// Check for duplicate sequence number
			List<Integer> listSequenceNumbers = new ArrayList<>();
			for (ParticipantTeamAsanas teamAsana : listAsanasForParticipant) {
				listSequenceNumbers.add(teamAsana.getSequenceNumber());
			}
			if (seqNum == -1) {
				participantTeamAsanasService.updateSequenceNumber(id, null);
				return "ok";
			}

			if (listSequenceNumbers.contains(seqNum)) { // if Duplicate found
				return "duplicateFound";
			} else {
				if (asanaCount < seqNum) {
					return "sequenceError";
				} else {
					participantTeamAsanasService.updateSequenceNumber(id, seqNum);
					return "ok";
				}
			}

		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/teamAsanas/updateSequenceNumber/allParticipants/{id}/round/{roundNumber}")
	public String saveSequenceNumberForAllParticipants(@Param("seqNum") Integer seqNum,
			@PathVariable(name = "id") Integer id, @PathVariable(name = "roundNumber") Integer roundNumber, Model model,
			RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException, NoSuchElementException {
		LOGGER.info("Entered saveSequenceNumber ParticipantTeamAsanasRestController");
		LOGGER.info("seqNum" + seqNum);
		// function executes when common asanas are assigned to all participants
		try {
			ParticipantTeamAsanas participantTeamAsanas = participantTeamAsanasService.get(id);

			// get the count of asanas for one participant and apply same for all
			ParticipantTeam participantTeam = participantTeamAsanas.getParticipantTeam();
			ParticipantTeamParticipants participantTeamParticipant = participantTeamAsanas
					.getParticipantTeamParticipants();
			// Integer roundNumber = participantTeamAsanas.getRoundNumber();
			List<ParticipantTeamAsanas> listAsanasForParticipant = participantTeamAsanasService
					.getByTeamAndParticipantAndRoundOrderBySequenceNum(participantTeam, participantTeamParticipant,
							roundNumber);
			Integer asanaCount = listAsanasForParticipant.size();

			// Check for duplicate sequence number
			List<Integer> listSequenceNumbers = new ArrayList<>();
			for (ParticipantTeamAsanas teamAsana : listAsanasForParticipant) {
				listSequenceNumbers.add(teamAsana.getSequenceNumber());
			}

			if (seqNum == -1) {
				if (participantTeam.isDifferentAsanasForParticipants() == false) { // common asanas for all
					// participants
					List<ParticipantTeamAsanas> listCommonAssignedAsanasForParticpants = participantTeamAsanasService
							.getAllByAsanaAndTeamAndRoundNumber(participantTeamAsanas.getAsana(), participantTeam,
									roundNumber);
					for (ParticipantTeamAsanas teamAsanas : listCommonAssignedAsanasForParticpants) {
						participantTeamAsanasService.updateSequenceNumber(teamAsanas.getId(), null);
					}
				}

				return "ok";
			}

			if (listSequenceNumbers.contains(seqNum)) { // if Duplicate found
				return "duplicateFound";
			} else {
				if (asanaCount < seqNum) {
					return "sequenceError";
				} else {

					if (participantTeam.isDifferentAsanasForParticipants() == false) { // common asanas for all
																						// participants
						List<ParticipantTeamAsanas> listCommonAssignedAsanasForParticpants = participantTeamAsanasService
								.getAllByAsanaAndTeamAndRoundNumber(participantTeamAsanas.getAsana(), participantTeam,
										roundNumber);
						for (ParticipantTeamAsanas teamAsanas : listCommonAssignedAsanasForParticpants) {
							participantTeamAsanasService.updateSequenceNumber(teamAsanas.getId(), seqNum);
						}
					}

					return "ok";
				}
			}

		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping(path = "/select-participant/team-asanas", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AsanasDTO> fetchAsanas() {

		List<AsanasDTO> listAllAsanaDTO = new ArrayList<AsanasDTO>();
		List<Asana> listAsanas = asanaService.listAllAsanas();
		for (Asana asana : listAsanas) {
			// listAllAsanaDTO.add(asana.getId(),asana.getName());
		}
		return listAllAsanaDTO;
	}

	@PostMapping("/team/saveAsanasStatus")
	public String saveRoundAsanaStatusForTeam(@RequestParam("participantTeam") Integer participantTeamId,
			@RequestParam("round") Integer round, @RequestParam("status") String status, HttpServletRequest request) {
		LOGGER.info("Entered saveRoundAsanaStatusForTeam ");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ParticipantTeam participantTeam;
		User user = CommonUtil.getCurrentUser();
		try {
			participantTeam = participantTeamService.get(participantTeamId);
			ChampionshipCategory championshipCategory = championshipCategoryService
					.getChampionshipCategoryForAllConditions(participantTeam.getChampionship().getId(),
							participantTeam.getAsanaCategory().getId(), participantTeam.getCategory().getId(),
							participantTeam.getGender().toString());
			ChampionshipRounds championshipRound = ChampionshipRoundsService
					.findByChampionshipAndChampionshipCatgeoryAndRound(participantTeam.getChampionship(),
							championshipCategory, round);
			ChampionshipParticipantTeams championshipParticipantTeam = championshipParticipantTeamsService
					.findByParticipantTeamAndChampionshipRounds(participantTeam, championshipRound);
			if (championshipParticipantTeam == null
					|| championshipParticipantTeam.getStatus().equals(StatusEnum.SCHEDULED)) {

				ParticipantTeamParticipantAsanasStatus asanaStatusRecord = participantTeamParticipantAsanasStatusService
						.findByParticipantTeamAndRound(participantTeam, round);
				AsanaStatusEnum asanaStatus = AsanaStatusEnum.valueOf(status);
				if (asanaStatusRecord == null) {
					ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = new ParticipantTeamParticipantAsanasStatus();
					participantTeamParticipantAsanasStatus.setAsanaStatus(asanaStatus);
					participantTeamParticipantAsanasStatus.setCreatedBy(user);
					participantTeamParticipantAsanasStatus.setCreatedTime(new Date());
					participantTeamParticipantAsanasStatus.setParticipantTeam(participantTeam);
					participantTeamParticipantAsanasStatus.setRound(round);
					participantTeamParticipantAsanasStatusService.save(participantTeamParticipantAsanasStatus);
				} else {
					asanaStatusRecord.setAsanaStatus(asanaStatus);
					asanaStatusRecord.setLastModifiedBy(user);
					asanaStatusRecord.setLastModifiedTime(new Date());
					participantTeamParticipantAsanasStatusService.save(asanaStatusRecord);
				}
				return "OK";
			} else {
				return "UnableToChangeStatus";
			}
		} catch (ParticipantTeamNotFoundException e) {
			e.printStackTrace();
			return "FAIL";
		}

	}

	@PostMapping("/select-team-lead/team/{teamId}/lead")
	public String saveTeamLead(Model model, @PathVariable("teamId") Integer teamId,
			@RequestParam("teamLead") Integer participantTeamParticipantId,
			@RequestParam("status") Boolean teamLeadStatus, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		LOGGER.info("Entered saveTeamLead - ParticipantTeamAsanasRestController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = null;
		try {
			ParticipantTeam participantTeam = participantTeamService.get(teamId);
			championship = participantTeam.getChampionship();

			List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeamParticipantsService
					.getTeamParticipantsForParticipantTeam(participantTeam);
			List<ParticipantTeamParticipantAsanasStatus> listParticipantTeamAsanaStatus = participantTeamParticipantAsanasStatusService
					.findAllByParticipantTeam(participantTeam);

			for (ParticipantTeamParticipants participantTeamParticipant : listParticipantTeamParticipants) {
				Boolean status = false;
				participantTeamParticipant.setIsTeamLead(status);
				Integer parId = participantTeamParticipant.getId();
				if (parId.equals(participantTeamParticipantId)) {
					participantTeamParticipant.setIsTeamLead(teamLeadStatus);
				}
				participantTeamParticipantsService.save(participantTeamParticipant);
			}
			if (!listParticipantTeamAsanaStatus.isEmpty()) {
				for (ParticipantTeamParticipantAsanasStatus participantTeamAsanasStatus : listParticipantTeamAsanaStatus) {
					if (participantTeamAsanasStatus.getCreatedBy().getRoleName().equalsIgnoreCase("PARTICIPANT")) {
						participantTeamAsanasStatus.setCreatedBy(currentUser);
					}
					if (participantTeamAsanasStatus.getLastModifiedBy() == null) {
						participantTeamAsanasStatus.setLastModifiedBy(currentUser);
					} else if (participantTeamAsanasStatus.getLastModifiedBy().getRoleName()
							.equalsIgnoreCase("PARTICIPANT")) {
						participantTeamAsanasStatus.setLastModifiedBy(currentUser);
					}
					participantTeamParticipantAsanasStatusService.save(participantTeamAsanasStatus);
				}
			}

			return "OK";
		} catch (ParticipantTeamNotFoundException e) {
			LOGGER.info(e.getMessage());
			return "FAIL";
		}
	}
}
