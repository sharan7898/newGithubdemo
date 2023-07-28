package com.swayaan.nysf.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageParticipantChampionshipEnrollmentController {

	@Autowired
	ParticipantTeamService service;

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ParticipantTeamService participantTeamService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ManageParticipantChampionshipEnrollmentController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-registered-team")
	public String listFirstPageRegisteredTeams(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageRegisteredTeams method -ManageParticipantChampionshipEnrollmentController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageRegisteredTeams method -ManageParticipantChampionshipEnrollmentController");

		return listRegisteredTeamsByPage(1, model, "name", "asc", "", "", "", "", "PENDING", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-registered-team/page/{pageNum}")
	public String listRegisteredTeamsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", defaultValue = "", required = false) String chestNumber,
			@RequestParam(name = "keyword3", required = false) String championshipName,
			@RequestParam(name = "keyword4", required = false) String asanaCategory,
			@RequestParam(name = "keyword5", defaultValue = "PENDING", required = false) String status,
			HttpServletRequest request) {
		LOGGER.info("Entered listRegisteredTeamsByPage method -ManageParticipantChampionshipEnrollmentController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		System.out.println("keyword1" + name + "keyword2" + chestNumber + "keyword3" + championshipName + "keyword4"
				+ asanaCategory + "keyword5" + status);
		Page<ParticipantTeam> page = service.listRegisteredTeamByPage(pageNum, sortField, sortDir, name, chestNumber,
				championshipName, asanaCategory, status);
		List<ParticipantTeam> listParticipantTeam = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-registered-team");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", chestNumber);
		model.addAttribute("keyword3", championshipName);
		model.addAttribute("keyword4", asanaCategory);
		model.addAttribute("keyword5", status);
		model.addAttribute("listParticipantTeam", listParticipantTeam);
		model.addAttribute("pageTitle", "Enrolled Teams");
		LOGGER.info("Exit listRegisteredTeamsByPage method -ManageParticipantChampionshipEnrollmentController");

		return "administration/manage_registered_team";
	}

}
