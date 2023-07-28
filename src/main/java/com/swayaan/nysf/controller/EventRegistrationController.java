package com.swayaan.nysf.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.EventCategory;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ChampionshipNotFoundException;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.exception.EventRegistrationNotFoundException;
import com.swayaan.nysf.exception.NotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipLevelsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.EventCategoryService;
import com.swayaan.nysf.service.EventRegistrationService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
public class EventRegistrationController {

	@Autowired
	ChampionshipLevelsService championshipLevelsService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	EventRegistrationService eventRegistrationService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	EventCategoryService eventCategoryService;
	@Autowired
	ChampionshipService championshipService;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationController.class);
	@Value("${no.traditional.category.participants}")
	private Integer noTraditionalCategoryParticipants;
	@Value("${no.artistic.single.category.participants}")
	private Integer noArtisticSingleCategoryParticipants;
	@Value("${no.artistic.pair.category.participants}")
	private Integer noArtisticPairCategoryParticipants;
	@Value("${no.rhythmic.pair.category.participants}")
	private Integer noRhythmicPairCategoryParticipants;
	@Value("${no.artistic.group.category.participants}")
	private Integer noArtisticGroupCategoryParticipants;

	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/eventmanager/register-championship")
	public String showEventRegistrationForm(Model model, HttpServletRequest request) {
		LOGGER.info("Entered showChampionshipRegistrationForm methos -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventRegistration eventRegistration = new EventRegistration();
		EventCategory eventCategory = new EventCategory();
		List<ChampionshipLevels> listChampionshipLevels = championshipLevelsService.listAllChampionshipLevels();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		model.addAttribute("listChampionshipLevels", listChampionshipLevels);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("eventCategory", eventCategory);
		model.addAttribute("eventRegistration", eventRegistration);
		model.addAttribute("pageTitle", "Register Event");
		LOGGER.info("Exit showChampionshipRegistrationForm methos -EventRegistrationController");

		return "eventmanager/register_championship";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/eventmanager/register-championship/save")
	public String saveChampionship(@ModelAttribute("eventRegistration") EventRegistration eventRegistration,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered saveChampionship method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		String ctxPath = request.getContextPath();
		url = url.replaceFirst(uri, "");
		url = url + ctxPath;

		Boolean eventName = eventRegistrationService.checkEventName(eventRegistration.getName());
		Boolean championshipName = championshipService.checkChampionshipName(eventRegistration.getName());

		try {

			if (eventName == true || championshipName == true) {
				if (eventRegistration.getId() == null) {
					redirectAttributes.addFlashAttribute("errorMessage",
							eventRegistration.getName() + " is already present");
					return "redirect:/eventmanager/register-championship";
				} else {
					Boolean checkEventNameEditFlow = eventRegistrationService
							.checkEventNameForEditFlow(eventRegistration.getName(), eventRegistration.getId());

					if (checkEventNameEditFlow == true || championshipName == true) {
						redirectAttributes.addFlashAttribute("errorMessage",
								eventRegistration.getName() + " is already present");
						return "redirect:/eventmanager/register-championship/edit/"+eventRegistration.getId();

					} else {
						eventRegistrationService.save(eventRegistration);
						redirectAttributes.addFlashAttribute("message",
								"Championship  " + eventRegistration.getName() + " has been updated successfully");
						return "redirect:/eventmanager/register-championship/edit/"+eventRegistration.getId();
					}
				}

			} else {
				redirectAttributes.addFlashAttribute("message", "Championship " + eventRegistration.getName() + " has been added successfully");

				EventRegistration savedEventRegistration = eventRegistrationService.save(eventRegistration);
				LOGGER.info("Exit saveChampionship method -EventRegistrationController");

				return "redirect:/eventmanager/register-championship/edit/" + savedEventRegistration.getId();
			}

		} catch (Exception ex) {
			LOGGER.error("Championship not found!");
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect:/eventmanager/championship/register-championship/checkStatus";
		}

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/eventmanager/register-championship/edit/{id}")
	public String editChampionship(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editChampionship method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			EventRegistration eventRegistration = eventRegistrationService.findById(id);
			if (eventRegistration.getCreatedBy().equals(currentUser)) {
				if (eventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING)) {
					List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
					List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
					List<ChampionshipLevels> listChampionshipLevels = championshipLevelsService
							.listAllChampionshipLevels();
					EventCategory eventCategory = new EventCategory();
					model.addAttribute("listChampionshipLevels", listChampionshipLevels);
					model.addAttribute("eventCategory", eventCategory);
					model.addAttribute("listAgeCategory", listAgeCategory);
					model.addAttribute("eventRegistration", eventRegistration);
					model.addAttribute("pageTitle", "Edit Championship");
					model.addAttribute("listAsanaCategory", listAsanaCategory);
					LOGGER.info("Exit editChampionship method -EventRegistrationController");
					return "eventmanager/register_championship";
				} else {
					model.addAttribute("eventRegistration", eventRegistration);
					model.addAttribute("pageTitle", "Championship Details");
					model.addAttribute("errorMessage", "Unable to edit. Please try again later");
					return "eventmanager/event_details";

				}
			} else {
				return "error/403";
			}
		} catch (NotFoundException ex) {
			LOGGER.error("Championship not found!");
			redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
			return "redirect/:eventmanager/register_championship";
		}
	}

//	@PostMapping("/eventmanager/register-championship/event-category/assign/{id}")
//	public String saveChampionshipCategoriess(@PathVariable(name = "id") Integer id,
//			@ModelAttribute("eventCategory") EventCategory eventCategory, Model model,
//			RedirectAttributes redirectAttributes, HttpServletRequest request) {
//		LOGGER.info("Entered saveChampionship EventRegistrationController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//		EventRegistration eventRegistration=null;
//		try {
//		 eventRegistration = eventRegistrationService.findById(id);
//			List<EventCategory> listEventCategory = eventRegistration.getEventCategory();
//			if (!listEventCategory.isEmpty()) {
//				for (EventCategory cat : listEventCategory) {
//					if (cat.getAsanaCategory().equals(eventCategory.getAsanaCategory())
//							&& cat.getGender().equals(eventCategory.getGender())) {
//						redirectAttributes.addFlashAttribute("errorMessage",
//								"The Championship already have this category");
//						return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
//					}
//				}
//
//			}
//
//			EventCategory eventCategoryNew = new EventCategory();
//			eventCategoryNew.setAsanaCategory(eventCategory.getAsanaCategory());
//			eventCategoryNew.setGender(eventCategory.getGender());
//			eventCategoryNew.setAgeCategory(eventCategory.getAgeCategory());
//			eventCategoryNew.setNoOfRounds(eventCategory.getNoOfRounds());
//			listEventCategory.add(eventCategoryNew);
//			eventRegistration.setEventCategory(listEventCategory);
//			EventRegistration savedEventRegistration = eventRegistrationService.save(eventRegistration);
//			return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
//
//		} catch (Exception e) {
//			LOGGER.error("Event Registration  not found!");
//			redirectAttributes.addFlashAttribute("errorMessage",
//					"Unable to edit.Please try again Later.");
//			return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
//		}
//
//	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/eventmanager/register-championship/event-category/assign/{id}")
	public String saveEventCategories(@PathVariable(name = "id") Integer id,
			@ModelAttribute("eventCategory") EventCategory eventCategory, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		LOGGER.info("Entered saveEventCategories method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventRegistration eventRegistration = null;
		try {

			eventRegistration = eventRegistrationService.findById(id);
			if (!eventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING)) {
				// show event details page
				model.addAttribute("eventRegistration", eventRegistration);
				model.addAttribute("pageTitle", "Championship Details");
				model.addAttribute("errorMessage", "Unable to edit. Please try again later");
				return "eventmanager/event_details";
			}
			List<EventCategory> listEventCategory = eventRegistration.getEventCategory();
			if (eventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING)) {
				if (eventCategory.getId() == null) {
					if (!listEventCategory.isEmpty()) {
						for (EventCategory c : listEventCategory) {
							if (c.getAsanaCategory().equals(eventCategory.getAsanaCategory())
									&& c.getAgeCategory().equals(eventCategory.getAgeCategory())
									&& c.getGender().equals(eventCategory.getGender())) {
								redirectAttributes.addFlashAttribute("errorMessage",
										"The Championship already have this category");
								return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
							}
						}

					}

					EventCategory eventCategoryNew = new EventCategory();
					eventCategoryNew.setAsanaCategory(eventCategory.getAsanaCategory());
					eventCategoryNew.setAgeCategory(eventCategory.getAgeCategory());
					eventCategoryNew.setGender(eventCategory.getGender());
					eventCategoryNew.setNoOfRounds(eventCategory.getNoOfRounds());
//					if (eventCategory.getAsanaCategory().getId() == TRADITIONAL_ASANA_CATEGORY_ID) {
//						eventCategoryNew.set(noTraditionalCategoryParticipants);
//					} else if (eventCategory.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
//						eventCategoryNew.setNoOfParticipants(noArtisticSingleCategoryParticipants);
//					} else if (eventCategory.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
//						eventCategoryNew.setNoOfParticipants(noArtisticPairCategoryParticipants);
//					} else if (eventCategory.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
//						eventCategoryNew.setNoOfParticipants(noRhythmicPairCategoryParticipants);
//					} else if (eventCategory.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
//						eventCategoryNew.setNoOfParticipants(noArtisticGroupCategoryParticipants);
//					}

					listEventCategory.add(eventCategoryNew);
					eventRegistration.setEventCategory(listEventCategory);
					EventRegistration eventRegistration1 = eventRegistrationService.save(eventRegistration);
					// championshipRoundsService.saveChampionshipRoundsForChampionshipCategory(eventRegistration1);
					redirectAttributes.addFlashAttribute("message",
							"The Championship category  " + eventCategory.getAsanaCategory().getName() +  " has been added successfully.");
				} else {
					if (!listEventCategory.isEmpty()) {
						for (EventCategory c : listEventCategory) {
							if (c.getAsanaCategory().equals(eventCategory.getAsanaCategory())
									&& c.getAgeCategory().equals(eventCategory.getAgeCategory())
									&& c.getGender().equals(eventCategory.getGender())
									&& !c.getId().equals(eventCategory.getId())) {
								redirectAttributes.addFlashAttribute("errorMessage",
										"The Championship already have this category");
								LOGGER.info("Exit saveEventCategories method -EventRegistrationController");

								return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
							}
						}

					}
					eventCategoryService.save(eventCategory, eventRegistration);
					redirectAttributes.addFlashAttribute("message",
							"The Championship category " + eventCategory.getAsanaCategory().getName() + " has been updated successfully.");
				}

				return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "The Championship is ongoing/completed.");
				return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
			}
		} catch (Exception e) {
			LOGGER.error("Event Registration  not found!");
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to edit.Please try again Later.");
			return "redirect:/eventmanager/register-championship/edit/" + eventRegistration.getId();
		}

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/eventmanager/manage-event-category/{eventRegistrationId}/new")
	public String newEventCategory(@PathVariable(name = "eventRegistrationId") Integer eventRegistrationId, Model model,
			HttpServletRequest request) throws ChampionshipNotFoundException {
		LOGGER.info("Entered newEventCategory method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventCategory eventCategory = new EventCategory();
		EventRegistration eventRegistration = eventRegistrationService.findById(eventRegistrationId);
		List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		model.addAttribute("eventCategory", eventCategory);
		model.addAttribute("listAsanaCategory", listAsanaCategory);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("eventRegistration", eventRegistration);
		model.addAttribute("pageTitle", "Add Event Category");
		LOGGER.info("Exit newEventCategory method -EventRegistrationController");

		return "eventmanager/add_event_category";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/eventmanager/register-championship/{eventRegistrationId}/event-category/edit/{eventCategoryId}")
	public String editAssignedCategories(@PathVariable(name = "eventRegistrationId") Integer eventRegistrationId,
			@PathVariable(name = "eventCategoryId") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered editAssignedCategories method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		EventRegistration eventRegistration = eventRegistrationService.findById(eventRegistrationId);
		EventCategory eventCategory = eventCategoryService.findById(id);
		List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		model.addAttribute("eventRegistration", eventRegistration);
		model.addAttribute("eventCategory", eventCategory);
		model.addAttribute("listAsanaCategory", listAsanaCategory);
		model.addAttribute("listAgeCategory", listAgeCategory);
		LOGGER.info("Exit editAssignedCategories method -EventRegistrationController");
		return "eventmanager/add_event_category";

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/eventmanager/register-championship/{eventRegistrationId}/championship-category/delete/{eventCategoryId}")
	public String deleteAssignedCategories(@PathVariable(name = "eventRegistrationId") Integer eventRegistrationId,
			@PathVariable(name = "eventCategoryId") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered deleteAssignedCategories method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventRegistration eventRegistration = eventRegistrationService.findById(eventRegistrationId);
		if (!eventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to edit. Please try again later");
			LOGGER.info("Redirected to manage-team/edit page");
			return "redirect:/eventmanager/register-championship/edit/" + eventRegistrationId;
		}
		eventCategoryService.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "Championship category removed Successfully");
		LOGGER.info("Exit deleteAssignedCategories method -EventRegistrationController");
		return "redirect:/eventmanager/register-championship/edit/" + eventRegistrationId;

	}

	@PreAuthorize("hasAuthority('EventManager')")
	@GetMapping("/eventmanager/championship/register-championship/checkStatus")
	public String selectChampionshipForStatus(Model model, HttpServletRequest request) {
		LOGGER.info("Entered selectChampionshipForStatus method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		List<EventRegistration> listEventRegistration = eventRegistrationService.findAllForCurrentUser(currentUser);
		model.addAttribute("listEventRegistration", listEventRegistration);
		model.addAttribute("pageTitle", "Check Status");
		LOGGER.info("Exit selectChampionshipForStatus method -EventRegistrationController");

		return "eventmanager/checkStatus";
	}

	@PreAuthorize("hasAuthority('EventManager')")
	@PostMapping("/eventmanager/championship/getEventDetails")
	public String showEventDetails(@RequestParam("event") Integer id, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered showEventDetails method -EventRegistrationController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		EventRegistration eventRegistration = eventRegistrationService.findByIdAndCurrentUser(id, currentUser);
		if (eventRegistration == null) {
			redirectAttributes.addFlashAttribute("message", "Championship Not Found");
			LOGGER.info("Redirected to manage-team/edit page");
			return "redirect:/eventmanager/championship/register-championship/checkStatus";
		} else {
			model.addAttribute("eventRegistration", eventRegistration);
			model.addAttribute("pageTitle", "Championship Details");
			LOGGER.info("Exit showEventDetails method -EventRegistrationController");

			return "eventmanager/event_details";
		}

	}

}
