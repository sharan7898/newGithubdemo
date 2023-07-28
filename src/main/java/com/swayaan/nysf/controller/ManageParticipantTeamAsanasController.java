package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
@RequestMapping("/admin")
public class ManageParticipantTeamAsanasController {
	
	@Autowired ParticipantTeamAsanasService service;
	
	@Autowired AsanaService asanaService;
	
	@Autowired AsanaCategoryService asanaCategoryService;
	
	@Autowired ChampionshipService championshipService;
	
	@Autowired ParticipantTeamService participantTeamService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageParticipantTeamAsanasController.class);
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/select-participant-team")
	public String getParticipantTeam(Model model,HttpServletRequest request) {
		LOGGER.info("Entered getParticipantTeam method -ManageParticipantTeamAsanasController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
		List<ParticipantTeam> listParticipantTeam = participantTeamService.listAllParticipantTeams();
		ParticipantTeamAsanas participantTeamAsanas = new ParticipantTeamAsanas();
		model.addAttribute("participantTeamAsanas", participantTeamAsanas);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("listAsanaCategory", listAsanaCategory);
		model.addAttribute("listParticipantTeam", listParticipantTeam);
		model.addAttribute("pageTitle", "Manage ParticipantTeam Asanas");
		LOGGER.info("Exit getParticipantTeam method -ManageParticipantTeamAsanasController");

		return "administration/select_participant_team_asana";
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/select-participant-team")
	public String saveParticipantTeam(ParticipantTeamAsanas participantTeamAsanas,Model model,
			HttpServletRequest request) {
		LOGGER.info("Entered saveParticipantTeam method -ManageParticipantTeamAsanasController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		
		service.save(participantTeamAsanas);
		LOGGER.info("Exit saveParticipantTeam method -ManageParticipantTeamAsanasController");

		return "administration/select_participant_team_asana";
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/select-participant-team-asanas")
	public String showParticipantAsanas(Model model, HttpServletRequest request)
			 {
		LOGGER.info("Entered showParticipantAsanas method -ManageParticipantTeamAsanasController");

		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
				List<Asana> asana = asanaService.listAllAsanas();
				model.addAttribute("asana", asana);
				model.addAttribute("pageTitle", "Select Participant Team Asanas");
				LOGGER.info("Exit showParticipantAsanas method -ManageParticipantTeamAsanasController");

				return "administration/select_asanas_for_team";
	}
	
	

	
}

