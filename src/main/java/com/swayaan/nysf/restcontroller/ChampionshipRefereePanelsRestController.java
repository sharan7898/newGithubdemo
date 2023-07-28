package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.DTO.RefereesPanelsDTO;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.service.ChampionshipRefereePanelsService;
import com.swayaan.nysf.service.RefereesPanelsService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ChampionshipRefereePanelsRestController {
	
	@Autowired private ChampionshipRefereePanelsService service;
	@Autowired private RefereesPanelsService refereesPanelsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipRefereePanelsRestController.class);
	
	@GetMapping("/referees/list_by_panel/{id}")
	public List<RefereesPanelsDTO> getRefereesForPanel(@PathVariable("id") Integer id) {
		LOGGER.info("Entered getRefereesForPanel ChampionshipRefereePanelsRestController");
		
		ChampionshipRefereePanels panel;
		List<RefereesPanels> listReferees = new ArrayList<>();
		List<RefereesPanelsDTO> result = new ArrayList<>();
		try {
			panel = service.get(id);
			listReferees = refereesPanelsService.getByChampionshipRefereePanels(panel);
		
			for(RefereesPanels refereesPanel : listReferees) {
				result.add(new RefereesPanelsDTO(refereesPanel.getId(), refereesPanel.getJudgeUser().getFullName(), refereesPanel.getType().getType(), refereesPanel.getJudgeUser().getState().getName()));
			}
			System.out.println("result :" + result);
			return result;
		} catch (ChampionshipRefereePanelsNotFoundException e) {
			e.printStackTrace();
		}
		return result;	
		
	}
	

	
}
