package com.swayaan.nysf.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/participant")
public class ParticipantChampionshipRegistration {

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	ParticipantTeamService participantTeamService;

	@Autowired
	CommonEmailUtil commonEmailUtil;

	private static final int TRADITIONAL_SINGLE_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantChampionshipRegistration.class);

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/championship-register/{championshipId}")
	public String registerChampionshipPage(@PathVariable(name = "championshipId") Integer championshipId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ParticipantNotFoundException {
		LOGGER.info("Entered registerChampionshipPage method  -ParticipantChampionshipRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		String currentUserGender = currentUser.getGender();
		Participant currentParticipant = CommonUtil.getCurrentParticipant();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Championship championship = championshipService.getChampionshipById(championshipId);
			if (championship.getStartDate().compareTo(new Date()) < 0
					|| !championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				redirectAttributes.addFlashAttribute("errorMessage", "Unable to find championship details");
				return "redirect:/participant/championships";
			}
			List<ChampionshipCategory> listAllChampionshipCategories = championshipCategoryService
					.getChampionshipCategoryByChampionship(championship);
			List<ChampionshipCategory> listChampionshipCategories = listAllChampionshipCategories.stream()
					.filter(cc -> cc.getGender().equals(GenderEnum.valueOf(currentUserGender))
							|| cc.getGender().equals(GenderEnum.Common))
					.collect(Collectors.toList());
			model.addAttribute("listChampionshipCategories", listChampionshipCategories);
			model.addAttribute("championship", championship);
			model.addAttribute("currentUser", currentParticipant);

		} catch (ChampionshipNotFoundException e) {
			LOGGER.error("Championship Not Found " + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to find championship details");
			return "redirect:/participant/championships";
		}
		model.addAttribute("pageTitle", "Register Team ");
		LOGGER.info("ExitregisterChampionshipPage method  -ParticipantChampionshipRegistrationController");

		return "participant/enroll_category_form";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@PostMapping("/enroll-to-category/{championshipId}")
	@ResponseBody
	public ResponseEntity<?> enrolToCategory(@PathVariable(name = "championshipId") Integer championshipId,
			@RequestParam(name = "championshipCategory") Integer championshipCategoryId,
			@RequestParam(name = "listParticipants[]") Integer[] listParticipants, HttpServletRequest request)
			throws ParticipantNotFoundException, EntityNotFoundException {
		LOGGER.info("Entered enrolToCategory method  -ParticipantChampionshipRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		Participant currentParticipant = CommonUtil.getCurrentParticipant();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("championshipId" + championshipId);
		LOGGER.info("championshipCategory" + championshipCategoryId);
		Championship championship = championshipService.findById(championshipId);
		String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		ChampionshipCategory championshipCategory = championshipCategoryService.get(championshipCategoryId);
		AsanaCategory asanaCategory = championshipCategory.getAsanaCategory();
		AgeCategory ageCategory = championshipCategory.getCategory();
		GenderEnum gender = championshipCategory.getGender();
		try {
			if (!(ArrayUtils.isEmpty(listParticipants))) {

				// check for duplication
				Set<Integer> listParticipantIds = new HashSet<>();
				for (int i = 0; i < listParticipants.length; i++) {

					listParticipantIds.add(listParticipants[i]);
				}
				if (listParticipantIds.size() != listParticipants.length) {
					// Duplicate found
					LOGGER.info("Duplicate Entry of the participant Ids");
					return new ResponseEntity<>("ENTRYDUPLICATE", HttpStatus.OK);
				}
			}

			ParticipantTeam existingParticipantTeam = participantTeamParticipantsService
					.findByParticipantAndChampionshipAndParticipantTeamCategories(currentParticipant, championship,
							asanaCategory, ageCategory, gender);
			if (existingParticipantTeam != null) {
				return new ResponseEntity<>("DUPLICATE", HttpStatus.OK);
			} else {
				// create team
				if (championship.getStartDate().compareTo(new Date()) < 0
						|| !championship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
					return new ResponseEntity<>("FAIL", HttpStatus.OK);
				} else {
					Integer listParticipantsSize = listParticipants.length;
					Integer participantCount = 0;
					if (asanaCategory.getId() == TRADITIONAL_SINGLE_ASANA_CATEGORY_ID
							|| asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
						participantCount = 1;

					} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
							|| asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
						participantCount = 2;

					} else if (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
						participantCount = 5;
					}

					if (listParticipantsSize == participantCount) {
						ParticipantTeam savedParticipantTeam = participantTeamService.registerTeamForCategory(
								championship, currentParticipant, asanaCategory, ageCategory, gender, listParticipants);
						commonEmailUtil.mailParticipantForSuccessfulTeamRegistration(nysfPortalLink,
								savedParticipantTeam);
						return new ResponseEntity<>("OK", HttpStatus.OK);

					} else {
						return new ResponseEntity<>("FAIL", HttpStatus.OK);
					}

				}

			}
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
		}

	}

	@PreAuthorize("hasAuthority('Participant')")
	@GetMapping("/register-team/checkStatus")
	public String getRegisteredTeamStatus(Model model, HttpServletRequest request) {
		LOGGER.info("Entered getRegisteredTeamStatus method -ManageParticipantChampionshipEnrollmentController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		List<Championship> listChampionships = championshipService.findAllChampionshipsForCurrentUser(currentUser);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("pageTitle", "Check Status");
		LOGGER.info("Exit getRegisteredTeamStatus method -ManageParticipantChampionshipEnrollmentController");

		return "participant/checkStatus";
	}

	@PreAuthorize("hasAuthority('Participant')")
	@PostMapping("/championship/getDetails")
	public String showChampionshipTeamDetails(@RequestParam("championship") Integer id, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered showChampionshipTeamDetails method -ManageParticipantChampionshipEnrollmentController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Championship championship = championshipService.findById(id);
		List<ParticipantTeam> listTeams = participantTeamService
				.listOfChampionshipParticipantTeamsForParticipant(championship, currentUser);
		if (championship == null) {
			redirectAttributes.addFlashAttribute("message", "Championship Not Found");
			LOGGER.info("Redirected to checkStatus page");
			return "redirect:/participant/register-team/checkStatus";
		} else {
			model.addAttribute("championship", championship);
			model.addAttribute("listTeams", listTeams);
			model.addAttribute("pageTitle", "Championship Details");
			LOGGER.info("Exit showChampionshipTeamDetails method -ManageParticipantChampionshipEnrollmentController");

			return "participant/team_registration_details";
		}

	}

}
