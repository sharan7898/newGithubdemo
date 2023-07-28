package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.AsanaExecutionScore;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.AsanaCodeDTO;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.exception.CompulsoryRoundAsanasNotFoundException;
import com.swayaan.nysf.exception.ParticipantTeamAsanasNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.AsanaExecutionScoreService;
import com.swayaan.nysf.service.AsanaService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.CompulsoryRoundAsanasService;
import com.swayaan.nysf.service.ParticipantTeamAsanasService;
import com.swayaan.nysf.service.ParticipantTeamService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageCompulsoryRoundAsanasController {

	@Autowired
	CompulsoryRoundAsanasService service;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	AgeCategoryService ageCategoryService;
//	@Autowired ExecutionCategoryService executionCategoryService;
	@Autowired
	AsanaService asanaService;
//	@Autowired AsanaExecutionScoreService asanaExecutionScoreService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;
	@Autowired
	AsanaExecutionScoreService asanaExecutionScoreService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageCompulsoryRoundAsanasController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-round-asanas")
	public String listFirstPageCompulsoryRoundAsanas(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageCompulsoryRoundAsanas method -ManageCompulsoryRoundAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageCompulsoryRoundAsanas method -ManageCompulsoryRoundAsanasController");

		return listAllCompulsoryRoundAsanasByPage(1, model, "asana", "asc", "", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-round-asanas/page/{pageNum}")
	public String listAllCompulsoryRoundAsanasByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String asanaCategory,
			@RequestParam(name = "keyword2", required = false) String category,
			@RequestParam(name = "keyword3", required = false) String championshipName, HttpServletRequest request) {
		LOGGER.info("Entered listAllCompulsoryRoundAsanasByPage method -ManageCompulsoryRoundAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		// List<Championship> listChampionships =
		// service.listAllChampionshipsByNotDeleted();
		Page<CompulsoryRoundAsanas> page = service.listByPage(pageNum, sortField, sortDir, asanaCategory, category,
				championshipName);
		List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		/*
		 * List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
		 * List<User> listNonAssignedUsers = new ArrayList<>(); for (Integer user_id :
		 * listUsers) { User user=null; try { user = userService.get(user_id); } catch
		 * (UserNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } listNonAssignedUsers.add(user); }
		 */

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-round-asanas");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", asanaCategory);
		model.addAttribute("keyword2", category);
		model.addAttribute("keyword3", championshipName);
		// model.addAttribute("keyword2", roundNumber);
		// model.addAttribute("listNonAssignedEventManager", listNonAssignedUsers);
		model.addAttribute("listCompulsoryRoundAsanas", listCompulsoryRoundAsanas);
		model.addAttribute("pageTitle", "Manage Compulsory Round Asanas");
		CompulsoryRoundAsanas compulsoryRoundAsana = new CompulsoryRoundAsanas();
		model.addAttribute("compulsoryRoundAsana", compulsoryRoundAsana);
		LOGGER.info("Exit listAllCompulsoryRoundAsanasByPage method -ManageCompulsoryRoundAsanasController");

		return "administration/manage_round_asanas";
	}
	/*
	 * public String listAllCompulsoryRoundAsanas(Model model, HttpServletRequest
	 * request) {
	 * 
	 * LOGGER.
	 * info("Entered listAllCompulsoryRoundAsanas ManageCompulsoryRoundAsanasController"
	 * ); // For Navigation history String mappedUrl = request.getRequestURI(); User
	 * currentUser = CommonUtil.getCurrentUser();
	 * CommonUtil.setNavigationHistory(mappedUrl, currentUser);
	 * 
	 * List<CompulsoryRoundAsanas> listCompulsoryRoundAsanas =
	 * service.listAllCompulsoryRoundAsanas();
	 * model.addAttribute("listCompulsoryRoundAsanas", listCompulsoryRoundAsanas);
	 * model.addAttribute("pageTitle", "Manage Compulsory Round Asanas"); return
	 * "administration/manage_round_asanas"; }
	 */

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-round-asanas/new")
	public String newRoundAsana(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newRoundAsana method -ManageCompulsoryRoundAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		CompulsoryRoundAsanas compulsoryRoundAsana = new CompulsoryRoundAsanas();
		List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		List<Championship> listChampionships = championshipService.listAllChampionshipsByStatusCompleted();
		List<Asana> listAsanas = asanaService.listAllAsanas();

		List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService.listAllAsanaExecutionScores();
		
		List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();
		for (Asana asana : listAsanas) {
			Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
					.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
			if (listAsanaCode.isPresent()) {
				listAsanasWithCode
						.add(new AsanaCodeDTO(asana, listAsanaCode.get().getCode(), listAsanaCode.get().getSubGroup()));
			} else {
				listAsanasWithCode.add(new AsanaCodeDTO(asana, "------", null));
			}

		}

		Collections.sort(listAsanasWithCode, new Comparator<AsanaCodeDTO>() {
			@Override
			public int compare(AsanaCodeDTO a1, AsanaCodeDTO a2) {
				return a1.getCode().compareTo(a2.getCode());
			}
		});

		compulsoryRoundAsana.setCompulsory(true);
		model.addAttribute("compulsoryRoundAsana", compulsoryRoundAsana);
		model.addAttribute("pageTitle", "Add Compulsory Round Asana");
		model.addAttribute("listAsanaCategory", listAsanaCategory);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("listAsanas", listAsanasWithCode);
		LOGGER.info("Exit newRoundAsana method -ManageCompulsoryRoundAsanasController");

		return "administration/compulsory_round_asana_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-round-asanas/save")
	public String saveRoundAsana(CompulsoryRoundAsanas compulsoryRoundAsana, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws IOException, NumberFormatException, AsanaNotFoundException {

		LOGGER.info("Entered saveRoundAsana method -ManageCompulsoryRoundAsanasController");
		LOGGER.info("compulsoryRoundAsana" + compulsoryRoundAsana);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		ChampionshipCategory championshiCategory = championshipCategoryService.getChampionshipCategoryForAllConditions(
				compulsoryRoundAsana.getChampionship().getId(), compulsoryRoundAsana.getAsanaCategory().getId(),
				compulsoryRoundAsana.getCategory().getId(), compulsoryRoundAsana.getGender().toString());
		ChampionshipRounds championshipRounds = championshipRoundsService
				.findByChampionshipAndChampionshipCatgeoryAndRound(compulsoryRoundAsana.getChampionship(),
						championshiCategory, compulsoryRoundAsana.getRoundNumber());
		if (championshipRounds.getStatus().equals(RoundStatusEnum.SCHEDULED)) {
			if (compulsoryRoundAsana.getId() == null) {
				List<Asana> asanaList = compulsoryRoundAsana.getAsanaList();
				int count = service.getCount(compulsoryRoundAsana.getChampionship(),
						compulsoryRoundAsana.getAsanaCategory(), compulsoryRoundAsana.getCategory(),
						compulsoryRoundAsana.getGender().toString(), compulsoryRoundAsana.getRoundNumber());
				int seqNumber = 0;
				if (count == 0) {
					seqNumber = 1;
				} else {
					seqNumber = count + 1;
				}
				List<CompulsoryRoundAsanas> listSavedCompulsoryAsanas = new ArrayList<>();
				for (Asana stringAsana : asanaList) {
					CompulsoryRoundAsanas newCompulsoryRoundAsanas = new CompulsoryRoundAsanas();

					Asana asana = asanaService.get(stringAsana.getId());
					newCompulsoryRoundAsanas.setAsanaCategory(compulsoryRoundAsana.getAsanaCategory());
					newCompulsoryRoundAsanas.setCategory(compulsoryRoundAsana.getCategory());
					newCompulsoryRoundAsanas.setRoundNumber(compulsoryRoundAsana.getRoundNumber());
					newCompulsoryRoundAsanas.setCompulsory(compulsoryRoundAsana.isCompulsory());
					newCompulsoryRoundAsanas.setCreatedTime(compulsoryRoundAsana.getCreatedTime());
					newCompulsoryRoundAsanas.setGender(compulsoryRoundAsana.getGender());
					newCompulsoryRoundAsanas.setChampionship(compulsoryRoundAsana.getChampionship());
					newCompulsoryRoundAsanas.setCreatedBy(compulsoryRoundAsana.getCreatedBy());
					newCompulsoryRoundAsanas.setAsana(asana);
					newCompulsoryRoundAsanas.setSequenceNumber(seqNumber);
					CompulsoryRoundAsanas savedCompulsoryAsanas = service.save(newCompulsoryRoundAsanas);
					seqNumber++;
					listSavedCompulsoryAsanas.add(savedCompulsoryAsanas);

				}
				participantTeamAsanasService.setCompulsoryAsanasForParticipantTeams(compulsoryRoundAsana,
						listSavedCompulsoryAsanas);

			} else {
				service.save(compulsoryRoundAsana);
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Cannot add/edit these compulsory asana to these category because the championship is ongoing/completed.");
			return "redirect:/admin/manage-round-asanas";
		}
		redirectAttributes.addFlashAttribute("message", "The Record has been saved successfully.");
		LOGGER.info("Exit saveRoundAsana method -ManageCompulsoryRoundAsanasController");

		return "redirect:/admin/manage-round-asanas";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-round-asanas/edit/{id}")
	public String editRoundAsana(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws CompulsoryRoundAsanasNotFoundException {

		LOGGER.info("Entered editRoundAsana method -ManageCompulsoryRoundAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			CompulsoryRoundAsanas compulsoryRoundAsana = service.get(id);
			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
//			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService.getListOfAsanas(
//					compulsoryRoundAsana.getAsanaCategory().getId(),
//					compulsoryRoundAsana.getGender().toString(),
//					compulsoryRoundAsana.getCategory().toString());
			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
			List<Asana> listAsanas = asanaService.listAllAsanas();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			List<AsanaExecutionScore> listAsanaExecutionScores = asanaExecutionScoreService.listAllAsanaExecutionScores();
			
			List<AsanaCodeDTO> listAsanasWithCode = new ArrayList<>();
			for (Asana asana : listAsanas) {
				Optional<AsanaExecutionScore> listAsanaCode = listAsanaExecutionScores.stream()
						.filter(executionScore -> executionScore.getAsana().equals(asana)).findFirst();
				if (listAsanaCode.isPresent()) {
					listAsanasWithCode
							.add(new AsanaCodeDTO(asana, listAsanaCode.get().getCode(), listAsanaCode.get().getSubGroup()));
				} else {
					listAsanasWithCode.add(new AsanaCodeDTO(asana, "------", null));
				}

			}

			Collections.sort(listAsanasWithCode, new Comparator<AsanaCodeDTO>() {
				@Override
				public int compare(AsanaCodeDTO a1, AsanaCodeDTO a2) {
					return a1.getCode().compareTo(a2.getCode());
				}
			});

			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("compulsoryRoundAsana", compulsoryRoundAsana);
			model.addAttribute("pageTitle", "Edit Round Asana Details");
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			// model.addAttribute("listAsanaExecutionScores", listAsanaExecutionScores);
			model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("listAsanas", listAsanasWithCode);
			LOGGER.info("Exit editRoundAsana method -ManageCompulsoryRoundAsanasController");

			return "administration/compulsory_round_asana_form";
		} catch (CompulsoryRoundAsanasNotFoundException ex) {
			LOGGER.error("CompulsoryRoundAsanas not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", "Record not found.");
			return "redirect:/admin/manage-round-asanas";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-round-asanas/delete/{id}")
	public String deleteRoundAsana(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes)
			throws CompulsoryRoundAsanasNotFoundException, ParticipantTeamAsanasNotFoundException {

		LOGGER.info("Entered deleteRoundAsana method -ManageCompulsoryRoundAsanasController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {

			CompulsoryRoundAsanas compulsoryRoundAsana = service.get(id);
			ChampionshipCategory championshiCategory = championshipCategoryService
					.getChampionshipCategoryForAllConditions(compulsoryRoundAsana.getChampionship().getId(),
							compulsoryRoundAsana.getAsanaCategory().getId(), compulsoryRoundAsana.getCategory().getId(),
							compulsoryRoundAsana.getGender().toString());
			ChampionshipRounds championshipRounds = championshipRoundsService
					.findByChampionshipAndChampionshipCatgeoryAndRound(compulsoryRoundAsana.getChampionship(),
							championshiCategory, compulsoryRoundAsana.getRoundNumber());
			if (championshipRounds.getStatus().equals(RoundStatusEnum.SCHEDULED)) {
				List<ParticipantTeamAsanas> ListParticipantTeamAsanas = participantTeamAsanasService
						.getByCompulsoryAsanaId(compulsoryRoundAsana.getId());
				if (!ListParticipantTeamAsanas.isEmpty()) {
					for (ParticipantTeamAsanas partiTeamAsana : ListParticipantTeamAsanas) {

						participantTeamAsanasService.deleteById(partiTeamAsana.getId());
					}
				}
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Compulsory asana cannot be deleted because it is already assigned to the teams. ");
				return "redirect:/admin/manage-round-asanas";
			}

			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Record " + compulsoryRoundAsana.getId() + " has been deleted successfully");

		} catch (CompulsoryRoundAsanasNotFoundException ex) {
			LOGGER.error("CompulsoryRoundAsanas not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteRoundAsana method -ManageCompulsoryRoundAsanasController");

		return "redirect:/admin/manage-round-asanas";
	}

}
