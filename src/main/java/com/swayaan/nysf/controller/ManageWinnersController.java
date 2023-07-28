package com.swayaan.nysf.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/*import com.swayaan.nysf.entity.Event;
import com.swayaan.nysf.entity.ParticipantGroup;
import com.swayaan.nysf.entity.ParticipantGroupReferees;
import com.swayaan.nysf.entity.RefereeTotalScoreListDTO;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ParticipantGroupNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.EventService;
import com.swayaan.nysf.service.ParticipantGroupRefereesService;
import com.swayaan.nysf.service.ParticipantGroupRoundTotalScoringService;
import com.swayaan.nysf.service.ParticipantGroupService;
import com.swayaan.nysf.service.ParticipantGroupTotalScoringService;*/
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageWinnersController {

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	AsanaCategoryService asanaCategoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageWinnersController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-winners")
	public String listWinners(Model model,HttpServletRequest request) {

		LOGGER.info("Entered listWinners method -ManageWinnersController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Evaluate Winners");
		LOGGER.info("Exit listWinners method -ManageWinnersController");

		return "administration/manage_winners";
	}

	
	/* @GetMapping("/manage-winners/team/{groupId}/score/{round}") public String
	 * listTeamScores(@PathVariable(name = "groupId") Integer groupId,
	 * 
	 * @PathVariable(name = "round") String round, Model model, RedirectAttributes
	 * redirectAttributes) {
	 * 
	 * LOGGER.info("Entered listTeamScores ManageWinnersController");
	 * 
	 * ParticipantGroup participantGroup; try { participantGroup =
	 * participantGroupService.get(groupId);
	 * 
	 * // get final score of the group float finalScore =
	 * participantGroupRoundTotalScoringService.findByParticipantGroupAndRoundNumber
	 * (participantGroup,round).getTotalScore(); model.addAttribute("final_score",
	 * finalScore);
	 * 
	 * // get list of referees for the group List<ParticipantGroupReferees>
	 * listAssignedReferees = participantGroup.getParticipantGroupReferees();
	 * List<RefereeTotalScoreListDTO> result = new ArrayList<>();
	 * for(ParticipantGroupReferees participantGroupReferees : listAssignedReferees)
	 * { User refereeUser = participantGroupReferees.getUser(); float
	 * refereeTotalScore =
	 * participantGroupTotalScoringService.getSumOfTotalScoresForReferee(
	 * participantGroup, refereeUser, round); result.add(new
	 * RefereeTotalScoreListDTO(refereeUser.getFullName(),
	 * refereeUser.getRefereeType().toString(), refereeTotalScore)); }
	 * 
	 * model.addAttribute("result", result); model.addAttribute("participantGroup",
	 * participantGroup); model.addAttribute("round_number", round);
	 * model.addAttribute("listAssignedReferees", listAssignedReferees);
	 * model.addAttribute("pageTitle", "Team Score : ("+ participantGroup.getName()
	 * +")");
	 * 
	 * } catch (ParticipantGroupNotFoundException e) {
	 * LOGGER.error("Participant Group with id " + groupId + "not found!");
	 * 
	 * }
	 * 
	 * return "administration/winners_scoring_form"; }
	 */

}
