package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.exception.ParticipantTeamNotFoundException;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.UserService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ParticipantTeamParticipantsRestController {
	
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamParticipantsRestController.class);

	@PostMapping("/teamParticipants/updateSequenceNumber/{id}")
	public String saveSequenceNumber(@Param("seqNum") Integer seqNum, @PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes redirectAttributes) throws ParticipantTeamNotFoundException, NoSuchElementException {
		LOGGER.info("Entered saveSequenceNumber ParticipantTeamParticipantsRestController");
		LOGGER.info("seqNum"+seqNum);
		try {
			ParticipantTeamParticipants participantTeamParticipants = participantTeamParticipantsService.get(id);
			
			// get the count of asanas for one participant and apply same for all
			ParticipantTeam participantTeam = participantTeamParticipants.getParticipantTeam();
			//List<ParticipantTeamParticipants> listParticipants = participantTeamParticipantsService.getAllByTeamAndParticipant(participantTeamParticipants.getParticipant(),participantTeam);
			List<ParticipantTeamParticipants> listParticipants = participantTeamParticipantsService.getTeamParticipantsForParticipantTeam(participantTeam);
			Integer participantsCount = listParticipants.size();
			// Check for duplicate sequence number
			List<Integer> listSequenceNumbers = new ArrayList<>();
			for(ParticipantTeamParticipants teamParticipant : listParticipants) {
				listSequenceNumbers.add(teamParticipant.getSequenceNumber());
			}
			if (seqNum == -1) {
				participantTeamParticipantsService.updateSequenceNumber(id, null);
				return "ok";
			} 
			
			if(listSequenceNumbers.contains(seqNum)) { // if Duplicate found
				return "duplicateFound";
			} else {
				if(participantsCount < seqNum) {
					return "sequenceError";
				} else {
					participantTeamParticipantsService.updateSequenceNumber(id, seqNum);
					return "ok";
				}
			}
				
			
	
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
	}

}
