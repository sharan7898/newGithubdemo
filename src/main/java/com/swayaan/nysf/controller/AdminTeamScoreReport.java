package com.swayaan.nysf.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.API.entity.livescores.CategoriesDTO1;
import com.swayaan.nysf.API.entity.livescores.LiveScoresDTO;
import com.swayaan.nysf.API.entity.livescores.RoundsDTO1;
import com.swayaan.nysf.API.entity.livescores.TeamsDTO1;
import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.ParticipantTeamReferees;
import com.swayaan.nysf.entity.ParticipantTeamRoundTotalScoring;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.DTO.ChampionshipCategoryDTO1;
import com.swayaan.nysf.exception.AsanaCategoryNotFoundException;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipCategoryService;
import com.swayaan.nysf.service.ChampionshipParticipantTeamsService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.ParticipantTeamRefereesService;
import com.swayaan.nysf.service.ParticipantTeamRoundTotalScoringService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class AdminTeamScoreReport {

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	ChampionshipCategoryService championshipCategoryService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	ParticipantTeamRefereesService participantTeamRefereesService;
	@Autowired
	ParticipantTeamRoundTotalScoringService participantTeamRoundTotalScoringService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	@Autowired
	UserService userService;
	DecimalFormat scoreFormat = new DecimalFormat("0.00");

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminTeamScoreReport.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/choose-championship")
	public String listChooseChampionships(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listChooseChampionships method  -AdminTeamScoreReportController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listChooseChampionships method  -AdminTeamScoreReportController");

		return listAllChampionshipsByPage(1, model, "name", "asc", "", "", request);
		
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/choose-championship/page/{pageNum}")
	public String listAllChampionshipsByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location,HttpServletRequest request) {
		LOGGER.info("Entered listAllChampionshipsByPage method -AdminTeamScoreReportController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		//	List<Championship> listChampionships = service.listAllChampionshipsByNotDeleted();
		Page<Championship> page=championshipService.listByPage(pageNum, sortField, sortDir, name, location);
		List<Championship> listChampionship = page.getContent();
			
		long startCount = (pageNum - 1) * championshipService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + championshipService.RECORDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		List<Integer> listUsers = userService.getUserByNotJudgeAndParticipant();
		List<User> listNonAssignedUsers = new ArrayList<>();
		for (Integer user_id : listUsers) {
			User user=null;
			try {
				user = userService.get(user_id);
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listNonAssignedUsers.add(user);
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/choose-championship");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", location);
		model.addAttribute("listNonAssignedEventManager", listNonAssignedUsers);
		model.addAttribute("listChampionships", listChampionship);
		model.addAttribute("pageTitle", "View Team Score");
		//Championship championship = new Championship();
		//model.addAttribute("Championship", championship);
		LOGGER.info("Exit listAllChampionshipsByPage method -AdminTeamScoreReportController");

		return "administration/list_all_championships";
		}


//	@GetMapping("/choose-championship")
//	public String getChampionships(Model model,HttpServletRequest request) {
//		LOGGER.info("Entered getChampionships - AdminScoreReport ");
//		//For Navigation history
//				String mappedUrl = request.getRequestURI();
//				User currentUser = CommonUtil.getCurrentUser();
//				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
//		model.addAttribute("listChampionships", listChampionships);
//		model.addAttribute("pageTitle", "View Team Scores");
//		return "administration/list_all_championships";
//
//	}

//	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
//	@GetMapping("/championship/{championshipId}/getTeamsForCategories")
//	public String listFirstPageChampionshipGetTeamCategorys(Model model,@PathVariable(name = "championshipId") Integer championshipId,HttpServletRequest request) {
//		LOGGER.info("Entered listAllChampionship AdminTeamScoreReport");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		return listAllChampionshipsTeamCategoryByPage(1,championshipId , model, "noOfRounds", "asc", "", "", request);
//		
//	}
//	
//	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
//	@GetMapping("/championship/{championshipId}/getTeamsForCategories/page/{pageNum}")
//	public String listAllChampionshipsTeamCategoryByPage(@PathVariable(name = "pageNum") int pageNum,@PathVariable(name = "championshipId") Integer championshipId,Model model,@RequestParam(name = "sortField", required = false) String sortField,
//			@RequestParam(name = "sortDir", required = false) String sortDir,
//			@RequestParam(name = "keyword1", required = false) String asanaCate,
//			@RequestParam(name = "keyword2", required = false) String ageCate,HttpServletRequest request) {
//		LOGGER.info("Entered listAllChampionship AdminTeamScoreReport");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		//	List<Championship> listChampionships = service.listAllChampionshipsByNotDeleted();
//		Page<ChampionshipCategory> page=championshipCategoryService.listByPage(pageNum, sortField, sortDir, asanaCate, ageCate);
//		List<ChampionshipCategory> listChampionship = page.getContent();
//			
//		long startCount = (pageNum - 1) * championshipCategoryService.RECORDS_PER_PAGE + 1;
//		long endCount = startCount + championshipCategoryService.RECORDS_PER_PAGE - 1;
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//		Championship championship;
//		try {
//			championship = championshipService.getChampionshipById(championshipId);
//		} catch (ChampionshipNotFoundException e) {
//			model.addAttribute("errorMessage", "Championship Not Found.");
//			return "administration/list_all_championships";
//		}
//		List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
//		List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
//				.getChampionshipCategoryByChampionshipId(championshipId);
//		// List of all age category for championship
//		Set<AgeCategory> listAgeCategories = new HashSet<>();
//
//		for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//			listAgeCategories.add(championshipCategory.getCategory());
//		}
//
//		List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
//		for (AgeCategory ageCategory : listAgeCategories) {
//			for (AsanaCategory asanaCategory : listAsanaCategories) {
//
//				List<ChampionshipCategory> listChampionhsipCategoryForAsanaCategory = new ArrayList<>();
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					if (championshipCategory.getAsanaCategory().equals(asanaCategory)
//							&& championshipCategory.getCategory().equals(ageCategory)) {
//
//						listChampionhsipCategoryForAsanaCategory.add(championshipCategory);
//					}
//				}
//
//				if (!listChampionhsipCategoryForAsanaCategory.isEmpty()) {
//					if (listChampionhsipCategoryForAsanaCategory.size() > 1) {
//						Collections.sort(listChampionhsipCategoryForAsanaCategory,
//								new Comparator<ChampionshipCategory>() {
//									@Override
//									public int compare(ChampionshipCategory c1, ChampionshipCategory c2) {
//										return c2.getNoOfRounds().compareTo(c1.getNoOfRounds());
//									}
//								});
//					}
//					Integer noOfRounds = listChampionhsipCategoryForAsanaCategory.get(0).getNoOfRounds();
//					for (int round = 1; round <= noOfRounds; round++) {
//						listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship, asanaCategory,
//								listChampionhsipCategoryForAsanaCategory.get(0).getCategory(), round));
//					}
//
//				}
//			}
//		}
//
//	}	
//	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")

//	@GetMapping("/championship/{championshipId}/getTeamsForCategories")
//	public String listFirstPageChampionshipGetTeamCategorys(Model model,@PathVariable(name = "championshipId") Integer championshipId,HttpServletRequest request) {
//		LOGGER.info("Entered listAllChampionship AdminTeamScoreReport");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		return listAllChampionshipsTeamCategoryByPage(1,championshipId , model, "name", "asc", "", "", request);
//		
//	}
//	
//	@GetMapping("/championship/{championshipId}/getTeamsForCategories/page/{pageNum}")
//	public String listAllChampionshipsTeamCategoryByPage(@PathVariable(name = "pageNum") int pageNum,@PathVariable(name = "championshipId") Integer championshipId,Model model,@RequestParam(name = "sortField", required = false) String sortField,
//			@RequestParam(name = "sortDir", required = false) String sortDir,
//			@RequestParam(name = "keyword1", required = false) String asanaCate,
//			@RequestParam(name = "keyword2", required = false) String ageCate,HttpServletRequest request) {
//		LOGGER.info("Entered listAllChampionship AdminTeamScoreReport");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		//	List<Championship> listChampionships = service.listAllChampionshipsByNotDeleted();
//		Page<ChampionshipCategory> page=championshipCategoryService.listByPage(pageNum, sortField, sortDir, asanaCate, ageCate);
//		List<ChampionshipCategory> listChampionship = page.getContent();
//			
//		long startCount = (pageNum - 1) * championshipCategoryService.RECORDS_PER_PAGE + 1;
//		long endCount = startCount + championshipCategoryService.RECORDS_PER_PAGE - 1;
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//		Championship championship;
//		try {
//			championship = championshipService.getChampionshipById(championshipId);
//		} catch (ChampionshipNotFoundException e) {
//			model.addAttribute("errorMessage", "Championship Not Found.");
//			return "administration/list_all_championships";
//		}
//		List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
//		List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
//				.getChampionshipCategoryByChampionshipId(championshipId);
//		// List of all age category for championship
//		Set<AgeCategory> listAgeCategories = new HashSet<>();
//
//		for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//			listAgeCategories.add(championshipCategory.getCategory());
//		}
//
//		List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
//		for (AgeCategory ageCategory : listAgeCategories) {
//			for (AsanaCategory asanaCategory : listAsanaCategories) {
//
//				List<ChampionshipCategory> listChampionhsipCategoryForAsanaCategory = new ArrayList<>();
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					if (championshipCategory.getAsanaCategory().equals(asanaCategory)
//							&& championshipCategory.getCategory().equals(ageCategory)) {
//
//						listChampionhsipCategoryForAsanaCategory.add(championshipCategory);
//					}
//				}
//
//				if (!listChampionhsipCategoryForAsanaCategory.isEmpty()) {
//					if (listChampionhsipCategoryForAsanaCategory.size() > 1) {
//						Collections.sort(listChampionhsipCategoryForAsanaCategory,
//								new Comparator<ChampionshipCategory>() {
//									@Override
//									public int compare(ChampionshipCategory c1, ChampionshipCategory c2) {
//										return c2.getNoOfRounds().compareTo(c1.getNoOfRounds());
//									}
//								});
//					}
//					Integer noOfRounds = listChampionhsipCategoryForAsanaCategory.get(0).getNoOfRounds();
//					for (int round = 1; round <= noOfRounds; round++) {
//						listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship, asanaCategory,
//								listChampionhsipCategoryForAsanaCategory.get(0).getCategory(), round));
//					}
//
//				}
//			}
//		}
//
//		Collections.sort(listChampionshipCategoryDTO, new Comparator<ChampionshipCategoryDTO1>() {
//			@Override
//			public int compare(ChampionshipCategoryDTO1 c1, ChampionshipCategoryDTO1 c2) {
//				return c1.getAsanaCategory().getId().compareTo(c2.getAsanaCategory().getId());
//			}
//		});
//	
//	
//		
//
//
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//		model.addAttribute("moduleURL", "/admin/championship/"+championshipId+"/getTeamsForCategories");
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword1", asanaCate);
//		model.addAttribute("keyword2", ageCate);
//		model.addAttribute("championship", championship);
//		model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
//
//		model.addAttribute("listChampionships", listChampionship);
//		model.addAttribute("pageTitle", "View Team Score");
//		//Championship championship = new Championship();
//		//model.addAttribute("Championship", championship);
//		return "administration/championship_category_form1";
//		}
	
//	@GetMapping("/championship/{championshipId}/getTeamsForCategories")
//	public String listFirstPageChampionshipGetTeamCategorys(Model model,@PathVariable(name = "championshipId") Integer championshipId,HttpServletRequest request) {
//		LOGGER.info("Entered listAllChampionship AdminTeamScoreReport");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		return listAllChampionshipsTeamCategoryByPage(1,championshipId , model, "name", "asc", "", "", request);
//		
//	}
//	
//	@GetMapping("/championship/{championshipId}/getTeamsForCategories/page/{pageNum}")
//	public String listAllChampionshipsTeamCategoryByPage(@PathVariable(name = "pageNum") int pageNum,@PathVariable(name = "championshipId") Integer championshipId,Model model,@RequestParam(name = "sortField", required = false) String sortField,
//			@RequestParam(name = "sortDir", required = false) String sortDir,
//			@RequestParam(name = "keyword1", required = false) String asanaCate,
//			@RequestParam(name = "keyword2", required = false) String ageCate,HttpServletRequest request) {
//		LOGGER.info("Entered listAllChampionship AdminTeamScoreReport");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		//	List<Championship> listChampionships = service.listAllChampionshipsByNotDeleted();
//		Page<ChampionshipCategory> page=championshipCategoryService.listByPage(pageNum, sortField, sortDir, asanaCate, ageCate);
//		List<ChampionshipCategory> listChampionship = page.getContent();
//			
//		long startCount = (pageNum - 1) * championshipCategoryService.RECORDS_PER_PAGE + 1;
//		long endCount = startCount + championshipCategoryService.RECORDS_PER_PAGE - 1;
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//		Championship championship;
//		try {
//			championship = championshipService.getChampionshipById(championshipId);
//		} catch (ChampionshipNotFoundException e) {
//			model.addAttribute("errorMessage", "Championship Not Found.");
//			return "administration/list_all_championships";
//		}
//		List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
//		List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
//				.getChampionshipCategoryByChampionshipId(championshipId);
//		// List of all age category for championship
//		Set<AgeCategory> listAgeCategories = new HashSet<>();
//
//		for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//			listAgeCategories.add(championshipCategory.getCategory());
//		}
//
//		List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
//		for (AgeCategory ageCategory : listAgeCategories) {
//			for (AsanaCategory asanaCategory : listAsanaCategories) {
//
//				List<ChampionshipCategory> listChampionhsipCategoryForAsanaCategory = new ArrayList<>();
//				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
//					if (championshipCategory.getAsanaCategory().equals(asanaCategory)
//							&& championshipCategory.getCategory().equals(ageCategory)) {
//
//						listChampionhsipCategoryForAsanaCategory.add(championshipCategory);
//					}
//				}
//
//				if (!listChampionhsipCategoryForAsanaCategory.isEmpty()) {
//					if (listChampionhsipCategoryForAsanaCategory.size() > 1) {
//						Collections.sort(listChampionhsipCategoryForAsanaCategory,
//								new Comparator<ChampionshipCategory>() {
//									@Override
//									public int compare(ChampionshipCategory c1, ChampionshipCategory c2) {
//										return c2.getNoOfRounds().compareTo(c1.getNoOfRounds());
//									}
//								});
//					}
//					Integer noOfRounds = listChampionhsipCategoryForAsanaCategory.get(0).getNoOfRounds();
//					for (int round = 1; round <= noOfRounds; round++) {
//						listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship, asanaCategory,
//								listChampionhsipCategoryForAsanaCategory.get(0).getCategory(), round));
//					}
//
//				}
//			}
//		}
//
//		Collections.sort(listChampionshipCategoryDTO, new Comparator<ChampionshipCategoryDTO1>() {
//			@Override
//			public int compare(ChampionshipCategoryDTO1 c1, ChampionshipCategoryDTO1 c2) {
//				return c1.getAsanaCategory().getId().compareTo(c2.getAsanaCategory().getId());
//			}
//		});
//	
//	
//		
//
//
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//		model.addAttribute("moduleURL", "/admin/championship/"+championshipId+"/getTeamsForCategories");
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword1", asanaCate);
//		model.addAttribute("keyword2", ageCate);
//		model.addAttribute("championship", championship);
//		model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
//
//		model.addAttribute("listChampionships", listChampionship);
//		model.addAttribute("pageTitle", "View Team Score");
//		//Championship championship = new Championship();
//		//model.addAttribute("Championship", championship);
//		return "administration/championship_category_form1";
//		}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")	
	@GetMapping("/championship/{championshipId}/getTeamsForCategories")
	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId, Model model)
			throws EntityNotFoundException {
		LOGGER.info("Entered listChampionshipCategories AdminTeamScoreReportController");
		User refereeUser = CommonUtil.getCurrentUser();
		Championship championship;
		try {
			championship = championshipService.getChampionshipById(championshipId);
		} catch (ChampionshipNotFoundException e) {
			model.addAttribute("errorMessage", "Championship Not Found.");
			return "administration/list_all_championships";
		}
		List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
		List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
				.getChampionshipCategoryByChampionshipId(championshipId);
		// List of all age category for championship
		Set<AgeCategory> listAgeCategories = new HashSet<>();

		for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
			listAgeCategories.add(championshipCategory.getCategory());
		}

		List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
		for (AgeCategory ageCategory : listAgeCategories) {
			for (AsanaCategory asanaCategory : listAsanaCategories) {

				List<ChampionshipCategory> listChampionhsipCategoryForAsanaCategory = new ArrayList<>();
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					if (championshipCategory.getAsanaCategory().equals(asanaCategory)
							&& championshipCategory.getCategory().equals(ageCategory)) {

						listChampionhsipCategoryForAsanaCategory.add(championshipCategory);
					}
				}

				if (!listChampionhsipCategoryForAsanaCategory.isEmpty()) {
					if (listChampionhsipCategoryForAsanaCategory.size() > 1) {
						Collections.sort(listChampionhsipCategoryForAsanaCategory,
								new Comparator<ChampionshipCategory>() {
									@Override
									public int compare(ChampionshipCategory c1, ChampionshipCategory c2) {
										return c2.getNoOfRounds().compareTo(c1.getNoOfRounds());
									}
								});
					}
					Integer noOfRounds = listChampionhsipCategoryForAsanaCategory.get(0).getNoOfRounds();
					for (int round = 1; round <= noOfRounds; round++) {
						listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship, asanaCategory,
								listChampionhsipCategoryForAsanaCategory.get(0).getCategory(), round));
					}

				}
			}
		}

		Collections.sort(listChampionshipCategoryDTO, new Comparator<ChampionshipCategoryDTO1>() {
			@Override
			public int compare(ChampionshipCategoryDTO1 c1, ChampionshipCategoryDTO1 c2) {
				return c1.getAsanaCategory().getId().compareTo(c2.getAsanaCategory().getId());
			}
		});
		
		model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
		model.addAttribute("pageTitle", "Championship Categories - "+ championship.getName());
		LOGGER.info("Exit listChampionshipCategories AdminTeamScoreReportController");

		return "administration/championship_category_form1";
	}

//	@GetMapping("/championship/{championshipId}/getTeamsForCategories")
//	public String listChampionshipCategories(@PathVariable(name = "championshipId") Integer championshipId, Model model)
//			throws EntityNotFoundException {
//		LOGGER.info("Entered listChampionships JudgeTeamScoringController");
//		User refereeUser = getCurrentUser();
//		Championship championship;
//		try {
//			championship = championshipService.getChampionshipById(championshipId);
//		} catch (ChampionshipNotFoundException e) {
//			model.addAttribute("errorMessage", "Championship Not Found.");
//			return "administration/list_all_championships";
//		}
//		List<AsanaCategory> listAsanaCategories = asanaCategoryService.listAllAsanaCategories();
//		AgeCategory ageCategory = ageCategoryService.get(1);
//		List<ChampionshipCategoryDTO1> listChampionshipCategoryDTO = new ArrayList<>();
//		for (AsanaCategory asanaCategory : listAsanaCategories) {
//			for (int round = 1; round <= 2; round++) {
//
//				if (round == 2) {
//					if (asanaCategory.getId() != 1) {
//						continue;
//					}
//					// listChampionshipCategoryDTO.add(new ChampionshipCategoryDTO1(championship,
//					// asanaCategory, ageCategory, round));
//				}
//				listChampionshipCategoryDTO
//						.add(new ChampionshipCategoryDTO1(championship, asanaCategory, ageCategory, round));
//			}
//		}
//		model.addAttribute("listChampionshipCategories", listChampionshipCategoryDTO);
//		model.addAttribute("pageTitle", "Championship Categories");
//		return "administration/championship_category_form1";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/championship/{championshipId}/getTeamsForCategroies/{round}")
	public String listAllTeamsForChampionhsip(@PathVariable(name = "championshipId") Integer id,
			@PathVariable(name = "round") Integer round, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request)
			throws AsanaCategoryNotFoundException, EntityNotFoundException {
		LOGGER.info("Entered listAllTeamsForChampionhsip method  -AdminTeamScoreReportController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
		List<CategoriesDTO1> listCategories = new ArrayList<CategoriesDTO1>();
		try {
			Championship championship = championshipService.get(id);

			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
			if (!listChampionshipCategory.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					String categoryName = championshipCategory.getCategory().getTitle() + " - "
							+ championshipCategory.getGender().toString();
					
					String asanaCategoryName = championshipCategory.getAsanaCategory().getName();

					Integer categoryId = null;
					Integer eventId = null;

					String eventDescription = "Yogasana Event";
					String gender = championshipCategory.getGender().toString();
					Integer noOfRounds = championshipCategory.getNoOfRounds();
					List<RoundsDTO1> listRounds = new ArrayList<>();

					String roundName = "Round " + round;

					ChampionshipRounds championshipRounds = championshipRoundsService
							.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
									round);
					List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
							.getAllByChampionshipRounds(championshipRounds);
					List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
							.getByRoundAndChampionshipAndChampionshipCategory(round, championship,
									championshipCategory);

					List<TeamsDTO1> listTeams = new ArrayList<>();
					if (!listChampionshipParticipantTeams.isEmpty()) {

						for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
							String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
							String autogenChestNumber = championshipParticipantTeams.getParticipantTeam().getAutogenChestNumber();
							String status = "SCHEDULED";
							Float totalScore = 0.0f;
							status = championshipParticipantTeams.getStatus().toString();
							// String rank = "-";

							ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
									.findByChampionshipParticipantTeamAndChampionshipRounds(
											championshipParticipantTeams, championshipRounds);
							if (participantTeamRoundTotalScoring != null) {
								totalScore = participantTeamRoundTotalScoring.getTotalScore();
								// rank = participantTeamRoundTotalScoring.getRanking().toString();
							}

							listTeams.add(new TeamsDTO1(chestId, autogenChestNumber, status, totalScore));
						}

					}

					// sort the list by score desc
					List<TeamsDTO1> listSortedTeams = listTeams.stream()
							.sorted(Comparator.comparing(TeamsDTO1::getTotal_score).reversed())
							.collect(Collectors.toList());

					listRounds.add(new RoundsDTO1(roundName, listSortedTeams));

					listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription, gender,
							listRounds,asanaCategoryName));

				}
			}

		} catch (

		ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("pageTitle", "View Team Scores");
		model.addAttribute("listCategories", listCategories);
		LOGGER.info("Exit listAllTeamsForChampionhsip method  -AdminTeamScoreReportController");

		return "administration/view_team_total_scores";
		// return new ResponseEntity<List<CategoriesDTO1>>(listCategories,
		// HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/championship/{championshipId}/getTeamsForCategroies/asanaCategory/{asanaCategoryId}/ageCategory/{ageCategoryId}/round/{round}")
	public String scoreDetailsForCategoryAndRound(@PathVariable(name = "championshipId") Integer championshipId,
			@PathVariable(name = "asanaCategoryId") Integer asanaCategoryId,
			@PathVariable(name = "ageCategoryId") Integer ageCategoryId, @PathVariable(name = "round") Integer round,
			Model model, RedirectAttributes redirectAttributes)
			throws AsanaCategoryNotFoundException, EntityNotFoundException {
		LOGGER.info("Entered scoreDetailsForCategoryAndRound method  -AdminTeamScoreReportController");

		LiveScoresDTO listTeamLiveScores = new LiveScoresDTO();
		List<CategoriesDTO1> listCategories = new ArrayList<CategoriesDTO1>();
		try {

			Championship championship = championshipService.get(championshipId);

			List<ChampionshipCategory> listChampionshipCategory = championshipCategoryService
					.getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc(
							championshipId, asanaCategoryId, ageCategoryId);

			System.out.println("listChampionshipCategory " + listChampionshipCategory.toString());

			if (!listChampionshipCategory.isEmpty()) {
				for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
					String categoryName = championshipCategory.getCategory().getTitle() + " - "
							+ championshipCategory.getGender().toString();
					
					String asanaCategoryName = championshipCategory.getAsanaCategory().getName();

					Integer categoryId = null;
					Integer eventId = null;

					String eventDescription = "Yogasana Event";
					String gender = championshipCategory.getGender().toString();
					Integer noOfRounds = championshipCategory.getNoOfRounds();
					List<RoundsDTO1> listRounds = new ArrayList<>();

					String roundName = "Round " + round;

					ChampionshipRounds championshipRounds = championshipRoundsService
							.findByChampionshipAndChampionshipCatgeoryAndRound(championship, championshipCategory,
									round);
					if (championshipRounds != null) {
						List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
								.getAllByChampionshipRounds(championshipRounds);
						List<ParticipantTeamReferees> listChampionshipParticipantsTeamReferees = participantTeamRefereesService
								.getByRoundAndChampionshipAndChampionshipCategory(round, championship,
										championshipCategory);

						List<TeamsDTO1> listTeams = new ArrayList<>();
						if (!listChampionshipParticipantTeams.isEmpty()) {

							for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
								String chestId = championshipParticipantTeams.getParticipantTeam().getChestNumber();
								String autogenChestNumber = championshipParticipantTeams.getParticipantTeam().getAutogenChestNumber();
								String teamName = championshipParticipantTeams.getParticipantTeam().getName();
								List<ParticipantTeamParticipants> listParticipantTeamParticipants  = participantTeamParticipantsService.getTeamParticipantsForParticipantTeam(championshipParticipantTeams.getParticipantTeam());
								List<String> listParticipantName = null;
								List<List<String>> participantLists = new ArrayList<>();
								for (ParticipantTeamParticipants participantTeamParticipants : listParticipantTeamParticipants) {
									
									participantLists.add(participantService.getParticipantByParticipantTeamParticipants(participantTeamParticipants.getParticipant().getId()));
								}
								
								listParticipantName = participantLists.stream().flatMap(List::stream).collect(Collectors.toList());
								System.out.println("listParticipantName"+listParticipantName);
								String status = "SCHEDULED";
								Float totalScore = 0.0f;
								status = championshipParticipantTeams.getStatus().toString();
								// String rank = "-";
								ParticipantTeamRoundTotalScoring participantTeamRoundTotalScoring = participantTeamRoundTotalScoringService
										.findByChampionshipParticipantTeamAndChampionshipRounds(
												championshipParticipantTeams, championshipRounds);
								Float tieScore = null;
								if (participantTeamRoundTotalScoring != null) {
									totalScore = participantTeamRoundTotalScoring.getTotalScore();
									tieScore = participantTeamRoundTotalScoring.getTieScore();
									LOGGER.info(
											"participantTeam: "
													+ participantTeamRoundTotalScoring.getChampionshipParticipantTeams()
															.getParticipantTeam().getName()
													+ "   totalScore " + totalScore);
									// rank = participantTeamRoundTotalScoring.getRanking().toString();
								}

								listTeams.add(new TeamsDTO1(chestId, autogenChestNumber, status, totalScore,listParticipantName,teamName,tieScore));
							}

						}

						// sort the list by score desc
						List<TeamsDTO1> listSortedTeams = listTeams.stream()
								.sorted(Comparator.comparing(TeamsDTO1::getTotal_score).reversed())
								.collect(Collectors.toList());

						listRounds.add(new RoundsDTO1(roundName, listSortedTeams));

						listCategories.add(new CategoriesDTO1(categoryName, categoryId, eventId, eventDescription,
								gender, listRounds,asanaCategoryName));

					}
				}
			}
		} catch (

		ChampionshipNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (CategoriesDTO1 category : listCategories) {
			System.out.println(category.toString());
		}

		model.addAttribute("pageTitle", "View Team Scores");
		model.addAttribute("listCategories", listCategories);
		LOGGER.info("Exit scoreDetailsForCategoryAndRound method  -AdminTeamScoreReportController");

		return "administration/view_team_total_scores";
		// return new ResponseEntity<List<CategoriesDTO1>>(listCategories,
		// HttpStatus.OK);
	}

	

	/*
	 * private boolean
	 * checkIfAllRefereesGivenScoresForTeams(ChampionshipParticipantTeams
	 * championshipParticipantTeams, List<ParticipantTeamReferees>
	 * listChampionshipParticipantTeamReferees, Integer round, ChampionshipRounds
	 * championshipRounds) throws Exception { // check if all referees(Djudges &
	 * chiefJudge given scores for participant teams // for that round - if // yes,
	 * update status in championship_participant table to completed
	 * System.out.println(listChampionshipParticipantTeamReferees);
	 * List<ParticipantTeamReferees>
	 * listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees =
	 * listChampionshipParticipantTeamReferees .stream().filter(referee ->
	 * referee.getUser().getRoleId() == 2 || referee.getUser().getRoleId() == 3)
	 * .collect(Collectors.toList()); System.out.println(
	 * "listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees" +
	 * listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees);
	 * 
	 * boolean status = false; List<ParticipantTeamReferees>
	 * listParticipantTeamDJudgesAndChiefJudgeReferees =
	 * listChampionshipParticipantTeamDJudgesAndChiefJudgeReferees
	 * .stream().filter(referee -> referee.getParticipantTeam()
	 * .equals(championshipParticipantTeams.getParticipantTeam()))
	 * .collect(Collectors.toList()); System.out.println(
	 * "listParticipantTeamDJudgesAndChiefJudgeReferees" +
	 * listParticipantTeamDJudgesAndChiefJudgeReferees);
	 * 
	 * for (ParticipantTeamReferees user :
	 * listParticipantTeamDJudgesAndChiefJudgeReferees) { Integer noOfNullCells =
	 * participantTeamAsanasScoringService
	 * .findAllByChampionhsipParticipantTeamsAndUser(championshipParticipantTeams,
	 * user); if (noOfNullCells != 0) { status = false; break; } else {
	 * 
	 * if (participantTeamTotalScoringService.
	 * isExistsByRoundAndChampionshipParticipantTeamAndUser(round,
	 * championshipParticipantTeams, user)) { status = true; } else { status =
	 * false; break; } } }
	 * 
	 * return status;
	 * 
	 * }
	 */

}
