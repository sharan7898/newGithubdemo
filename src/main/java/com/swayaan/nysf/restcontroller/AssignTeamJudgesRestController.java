package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.DTO.ChampionshipRefereePanelDTO;
import com.swayaan.nysf.entity.DTO.PanelParticipantTeamDTO;
import com.swayaan.nysf.entity.DTO.ParticipantTeamPanelRoundDTO;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaEvaluationQuestionsService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamService;

@RestController
public class AssignTeamJudgesRestController {
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ParticipantTeamService participantTeamService;
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
	AsanaEvaluationQuestionsService service;
	@Autowired
	private ChampionshipRefereePanelsService championshipRefereePanelsService;

	@GetMapping("/championship/{championshipId}/championshipCategories/{championship-category}/getRoundPanelTeams")
	public ParticipantTeamPanelRoundDTO getRoundForChampionshipAsanaCategoryGenderAgeCategory(
			@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "championship-category") Integer championshipcategoryId, Model model,
			RedirectAttributes redirectAttributes) throws AsanaCategoryNotFoundException, EntityNotFoundException {

		Championship championship;
		ParticipantTeamPanelRoundDTO participantTeamPanelRoundDTO = new ParticipantTeamPanelRoundDTO();

		Set<Integer> listRounds = new HashSet<Integer>();
		try {
			championship = championshipService.get(id);

			ChampionshipCategory championshipCateogry = championshipCategoryService.get(championshipcategoryId);

			Integer noOfRounds = championshipCateogry.getNoOfRounds();
			for (int i = 1; i <= noOfRounds; i++) {
				listRounds.add(i);
			}

			AsanaCategory asanaCategory = championshipCateogry.getAsanaCategory();
//			List<ChampionshipRefereePanelDTO> listChampionshipRefereePanelDTO = new ArrayList<ChampionshipRefereePanelDTO>();
//			List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
//					.getRefereePanelsForChampionship(championship, asanaCategory);
//			if (!listChampionshipRefereePanels.isEmpty()) {
//				for (ChampionshipRefereePanels panel : listChampionshipRefereePanels) {
//					listChampionshipRefereePanelDTO
//							.add(new ChampionshipRefereePanelDTO(panel.getId(), panel.getName()));
//
//				}
//			}
			List<PanelParticipantTeamDTO> listParticipantTeamsDTO = new ArrayList<PanelParticipantTeamDTO>();
			List<ParticipantTeam> listParticipantTeam = participantTeamService
					.listAllParticipantTeamsForChampionshipAndAsanaCategoryAndGenderAndAgeCategory(championship.getId(),
							asanaCategory.getId(), championshipCateogry.getGender().toString(),
							championshipCateogry.getCategory().getId());
			if (!listParticipantTeam.isEmpty()) {
				for (ParticipantTeam team : listParticipantTeam) {
					listParticipantTeamsDTO.add(new PanelParticipantTeamDTO(team.getId(), team.getName()));
				}
			}

	//		participantTeamPanelRoundDTO.setListChampionshipRefereePanels(listChampionshipRefereePanelDTO);
			participantTeamPanelRoundDTO.setListParticipantTeam(listParticipantTeamsDTO);
			participantTeamPanelRoundDTO.setListRoundNumber(listRounds);
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return participantTeamPanelRoundDTO;
	}
	
	@GetMapping("/championship/{championshipId}/championshipCategories/{championship-category}/round/{roundNumber}/getRoundPanel")
	public ParticipantTeamPanelRoundDTO getPanelForRound(
			@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "championship-category") Integer championshipcategoryId, @PathVariable(name = "roundNumber") Integer round,Model model,
			RedirectAttributes redirectAttributes) throws AsanaCategoryNotFoundException, EntityNotFoundException {

		Championship championship;
		ParticipantTeamPanelRoundDTO participantTeamPanelRoundDTO = new ParticipantTeamPanelRoundDTO();
		try {
			championship = championshipService.get(id);

			ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipcategoryId);

			// get list of championship referee panel

			AsanaCategory asanaCategory = championshipCategory.getAsanaCategory();
			List<ChampionshipRefereePanelDTO> listChampionshipRefereePanelDTO = new ArrayList<ChampionshipRefereePanelDTO>();
			List<ChampionshipRefereePanels> listChampionshipRefereePanels = championshipRefereePanelsService
					.getRefereePanelsForChampionshipAndAsanaCategoryAndAgeAndGenderAndRound(championship, asanaCategory,championshipCategory.getCategory(),championshipCategory.getGender(),
							round);
			if (!listChampionshipRefereePanels.isEmpty()) {
				for (ChampionshipRefereePanels panel : listChampionshipRefereePanels) {
					listChampionshipRefereePanelDTO
							.add(new ChampionshipRefereePanelDTO(panel.getId(), panel.getName()));

				}
			}

			participantTeamPanelRoundDTO.setListChampionshipRefereePanels(listChampionshipRefereePanelDTO);
		} catch (ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return participantTeamPanelRoundDTO;
	}

}
