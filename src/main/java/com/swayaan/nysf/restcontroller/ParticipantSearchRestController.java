package com.swayaan.nysf.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.DTO.ParticipantDTO;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.utils.CommonUtil;

@RestController
public class ParticipantSearchRestController {

	@Autowired
	ParticipantService participantService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	ChampionshipService championshipService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantSearchRestController.class);

	@GetMapping("/getParticipantById/{prnNumber}")
	public ResponseEntity<?> getParticipantByPRNNumber(@PathVariable("prnNumber") String prnNumber,
			@RequestParam("championshipId") Integer championshipId,
			@RequestParam("championshipCategoryId") Integer championshipCategoryId) {
		LOGGER.info("Entered getParticipantByPRNNumber ParticipantSearchRestController");
		try {
			Participant currentParticipant = CommonUtil.getCurrentParticipant();
			Participant participant = participantService.getParticipantByPrnNumber(prnNumber);

			if (participant == null) {
				return new ResponseEntity<>("NOTFOUND", HttpStatus.OK);
			}
			Championship championship = championshipService.findById(championshipId);
			ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
			ParticipantTeam participantTeam = participantTeamParticipantsService
					.findByParticipantAndChampionshipAndParticipantTeamCategories(participant, championship,
							championshipCategory.getAsanaCategory(), championshipCategory.getCategory(),
							championshipCategory.getGender());

			if (!championshipCategory.getGender().equals(GenderEnum.Common)) {
				if (!GenderEnum.valueOf(participant.getGender()).equals(championshipCategory.getGender())) {
					return new ResponseEntity<>("GENDERMISMATCH", HttpStatus.OK);

				}
			}
			if (participantTeam != null) {
				return new ResponseEntity<>("DUPLICATE", HttpStatus.OK);

			} else if (participant.getPrnNumber() == currentParticipant.getPrnNumber()) {
				return new ResponseEntity<>("CURRENTUSER", HttpStatus.OK);
			} else {
				ParticipantDTO participantDTO = new ParticipantDTO();
				participantDTO.setFirstName(participant.getFirstName());
				participantDTO.setLastName(participant.getLastName());
				participantDTO.setId(participant.getId());
				participantDTO.setPrnNumber(participant.getPrnNumber());
				participantDTO.setUserPrnNumber(participant.getUserPrnNumber());
				return new ResponseEntity<>(participantDTO, HttpStatus.OK);
			}

		} catch (Exception e) {
			LOGGER.info("Error occured" + e.getMessage());
			return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
		}
	}
}
