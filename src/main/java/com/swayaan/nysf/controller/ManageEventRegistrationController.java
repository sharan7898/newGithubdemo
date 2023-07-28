package com.swayaan.nysf.controller;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.EventCategory;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ChampionshipLevelsService;
import com.swayaan.nysf.service.ChampionshipLinkService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.EventCategoryService;
import com.swayaan.nysf.service.EventRegistrationService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageEventRegistrationController {

	@Autowired
	EventRegistrationService eventRegistrationService;
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	StateService stateService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	CommonEmailUtil commonEmailUtil;
	@Autowired
	ChampionshipLevelsService championshipLevelsService;
	@Autowired
	EventCategoryService eventCategoryService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	SettingService settingService;
	@Autowired
	ChampionshipLinkService championshipLinkService;

	private static final Integer ASANA_CATEGORY_TRADITIONAL_ID = 1;
	private static final Integer ASANA_CATEGORY_ARTISTIC_SINGLE_ID = 2;
	private static final Integer ASANA_CATEGORY_ARTISTIC_PAIR_ID = 3;
	private static final Integer ASANA_CATEGORY_RHYTHMIC_PAIR_ID = 4;
	private static final Integer ASANA_CATEGORY_ARTISTIC_GROUP_ID = 5;

	private static final Integer PARTICIPANT_COUNT_TRADITIONAL_ARTISTIC_SINGLE = 1;
	private static final Integer PARTICIPANT_COUNT_ARTISTIC_RHYTHMIC_PAIR = 2;
	private static final Integer PARTICIPANT_COUNT_ARTISTIC_GROUP = 5;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageEventRegistrationController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-event-registrations")
	public String listFirstPageEventRegistration(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageEventRegistration method -ManageEventRegistrationsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageEventRegistration method -ManageEventRegistrationsController");

		return listAllEventRegistrationsByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-event-registrations/page/{pageNum}")
	public String listAllEventRegistrationsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String location, HttpServletRequest request) {
		LOGGER.info("Entered listAllEventRegistrationsByPage method -ManageEventRegistrationsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Page<EventRegistration> page = eventRegistrationService.listByPage(pageNum, sortField, sortDir, name, location);
		List<EventRegistration> listEventRegistrations = page.getContent();

		long startCount = (pageNum - 1) * eventRegistrationService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + eventRegistrationService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-event-registrations");
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
		EventRegistration eventRegistration = new EventRegistration();
		// Judge judge = new Judge();
		model.addAttribute("listEventRegistrations", listEventRegistrations);
		model.addAttribute("pageTitle", "Manage Event Registrations");
		model.addAttribute("eventRegistration", eventRegistration);
		LOGGER.info("Exit listAllEventRegistrationsByPage method -ManageEventRegistrationsController");

		return "administration/manage_event_registrations";
	}

	/*
	 * public String listAllEventRegistrations(Model model) { LOGGER.
	 * info("Entered listAlleventRegistrations ManageEventRegistrationController");
	 * List<EventRegistration> listEventRegistrations =
	 * eventRegistrationService.listAllNonApprovedEvents();
	 * System.out.println(listEventRegistrations); model.addAttribute("pageTitle",
	 * "Manage Event Registrations"); model.addAttribute("listEventRegistrations",
	 * listEventRegistrations); return "administration/manage_event_registrations";
	 * }
	 */

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-event-registrations/save")
	public String saveEventRegistration(EventRegistration eventRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered saveEventRegistration method -ManageEventRegistrationsController");
//		if (eventRegistration.getApprovalStatus()) {
//			
//			eventRegistration.setApprovalStatus(true);
//			eventRegistrationService.save(eventRegistration);
//
//			copyRegisteredEventToChampionship(eventRegistration, request);
//
//			redirectAttributes.addFlashAttribute("message", "The event has been approved successfully.");
//		}
		try {
			eventRegistrationService.save(eventRegistration);
		} catch (Exception e) {
			LOGGER.error("EventRegistration Not Found " + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to save the changes.Please try again later.");
			return "redirect:/admin/manage-event-registrations";
		}
		redirectAttributes.addFlashAttribute("message", "The Championship has been saved successfully.");
		LOGGER.info("Exit saveEventRegistration method -ManageEventRegistrationsController");

		return "redirect:/admin/manage-event-registrations";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-event-registrations/{id}/{status}")
	public String updateEventApprovedStatus(@PathVariable("id") Integer id,
			@PathVariable("status") String approvalStatus, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered updateEventApprovedStatus method -ManageEventRegistrationsController");

		RegistrationStatusEnum statusEnum = RegistrationStatusEnum.valueOf(approvalStatus.toUpperCase());
		LOGGER.info("statusEnum " + statusEnum);
		EventRegistration eventRegistration = null;

		try {
			eventRegistration = eventRegistrationService.findById(id);
			if (statusEnum.equals(RegistrationStatusEnum.APPROVED)) {

				try {
					Championship registeredChampionship = championshipService
							.getRegisteredEventChampionship(eventRegistration);
					if (registeredChampionship == null) {
						copyRegisteredEventToChampionship(eventRegistration, request);
					} else {
						registeredChampionship.setStatus(ChampionshipStatusEnum.SCHEDULED);
						championshipService.save(registeredChampionship);
					}
				} catch (Exception e) {
					LOGGER.error("Unable to save Championship" + e.getMessage());
					redirectAttributes.addFlashAttribute("errormessage", "Championship Approval Failed. Try Again.");
				}
			} else if (statusEnum.equals(RegistrationStatusEnum.REJECTED)) {
				// get championship if already created & set status to rejected
				Championship registeredChampionship = championshipService
						.getRegisteredEventChampionship(eventRegistration);

				if (registeredChampionship != null) {
					if (registeredChampionship.getStatus().equals(ChampionshipStatusEnum.STARTED)
							|| registeredChampionship.getStatus().equals(ChampionshipStatusEnum.ONGOING)
							|| registeredChampionship.getStatus().equals(ChampionshipStatusEnum.COMPLETED)) {

						String message = "Unable to Reject the Championship " + eventRegistration.getName();
						redirectAttributes.addFlashAttribute("message1", message);
						return "redirect:/admin/manage-event-registrations";

					} else {
						ChampionshipLink championshipLink = championshipLinkService
								.getByChampionship(registeredChampionship);

						if (championshipLink != null) {
							championshipLink.setStatus(false);
							championshipLinkService.saveChampionshipLink(championshipLink);
						}
						if (registeredChampionship.getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
							registeredChampionship.setStatus(ChampionshipStatusEnum.REJECTED);
							championshipService.updateStatus(registeredChampionship);
						} else {
							redirectAttributes.addFlashAttribute("message1", "Unable to reject the championship");
							return "redirect:/admin/manage-event-registrations";
						}

					}
				}
				// eventRegistrationService.updateEventRegistrationApprovedStatus(eventRegistration,
				// statusEnum);
			}
			eventRegistrationService.updateEventRegistrationApprovedStatus(eventRegistration, statusEnum);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message1", "Unable to reject the championship");
			return "redirect:/admin/manage-event-registrations";
		}

		String message = "The Championship " + eventRegistration.getName() + " has been " + approvalStatus;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateEventApprovedStatus method -ManageEventRegistrationsController");

		return "redirect:/admin/manage-event-registrations";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-event-registrations/delete/{id}")
	public String deleteeventRegistration(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered deleteeventRegistration method -ManageEventRegistrationsController");
		try {
			EventRegistration eventRegistration = eventRegistrationService.findById(id);
			// delete event categories before you delete
			eventRegistrationService.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The  Event Registered  " + eventRegistration.getName() + " has been deleted successfully");

		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteeventRegistration method -ManageEventRegistrationsController");

		return "redirect:/admin/manage-event-registrations";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-event-registrations/edit/{id}")
	public String editChampionship(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editChampionship method -ManageEventRegistrationsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			EventRegistration eventRegistration = eventRegistrationService.findById(id);
			List<AsanaCategory> listAsanaCategory = asanaCategoryService.listAllAsanaCategories();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			List<ChampionshipLevels> listChampionshipLevels = championshipLevelsService.listAllChampionshipLevels();
			EventCategory eventCategory = new EventCategory();
			model.addAttribute("listChampionshipLevels", listChampionshipLevels);
			model.addAttribute("eventCategory", eventCategory);
			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("eventRegistration", eventRegistration);
			model.addAttribute("listAsanaCategory", listAsanaCategory);
			model.addAttribute("pageTitle", "Edit Event Registered");
			return "administration/edit_event_registration_form";
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editChampionship method -ManageEventRegistrationsController");

			return "redirect:/admin/manage-event-registrations";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-event-registrations/event-category/assign/{id}")
	public String saveChampionshipCategories(@PathVariable(name = "id") Integer id,
			@ModelAttribute("eventCategory") EventCategory eventCategory, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered saveChampionshipCategories method -ManageEventRegistrationsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventRegistration eventRegistration = null;
		try {
			eventRegistration = eventRegistrationService.findById(id);
			if(!eventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING))
			{
				redirectAttributes.addFlashAttribute("errorMessage",
						"Unable to edit the championship. Championship is already Approved/Rejected.");
				return "redirect:/admin/manage-event-registrations/edit/" + eventRegistration.getId();
			}
			List<EventCategory> listEventCategory = eventRegistration.getEventCategory();
			if (!listEventCategory.isEmpty()) {
				for (EventCategory cat : listEventCategory) {
					if (cat.getAsanaCategory().equals(eventCategory.getAsanaCategory())
							&& cat.getAgeCategory().equals(eventCategory.getAgeCategory())
							&& cat.getGender().equals(eventCategory.getGender())) {
						redirectAttributes.addFlashAttribute("errorMessage",
								"The Championship already have this category");
						return "redirect:/admin/manage-event-registrations/edit/" + eventRegistration.getId();
					}
				}

			}

			EventCategory eventCategoryNew = new EventCategory();
			eventCategoryNew.setAsanaCategory(eventCategory.getAsanaCategory());
			eventCategoryNew.setAgeCategory(eventCategory.getAgeCategory());
			eventCategoryNew.setGender(eventCategory.getGender());
			eventCategoryNew.setNoOfRounds(eventCategory.getNoOfRounds());
			listEventCategory.add(eventCategoryNew);
			eventRegistration.setEventCategory(listEventCategory);
			EventRegistration savedEventRegistration = eventRegistrationService.save(eventRegistration);
			return "redirect:/admin/manage-event-registrations/edit/" + eventRegistration.getId();

		} catch (Exception e) {
			LOGGER.error("Championship Registration  not found!"+e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to save the changes.Please try again later.");
			LOGGER.info("Exit saveChampionshipCategories method -ManageEventRegistrationsController");

			return "redirect:/admin/manage-event-registrations/edit/" + eventRegistration.getId();
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-event-registrations/{eventRegistrationId}/championship-category/delete/{eventCategoryId}")
	public String deleteAssignedCategories(@PathVariable(name = "eventRegistrationId") Integer eventRegistrationId,
			@PathVariable(name = "eventCategoryId") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		LOGGER.info("Entered deleteAssignedCategories method -ManageEventRegistrationsController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventRegistration eventRegistration = eventRegistrationService.findById(eventRegistrationId);
		if(!eventRegistration.getApprovalStatus().equals(RegistrationStatusEnum.PENDING))
		{
			redirectAttributes.addFlashAttribute("errorMessage",
					"Unable to edit the championship. Championship is already Approved/Rejected.");
			return "redirect:/admin/manage-event-registrations/edit/" + eventRegistration.getId();
		}

		eventCategoryService.deleteById(id);

		redirectAttributes.addFlashAttribute("message", "Championship category removed Successfully");
		LOGGER.info("Entered deleteAssignedCategories method -ManageEventRegistrationsController");
		return "redirect:/admin/manage-event-registrations/edit/" + eventRegistrationId;

	}

	private void copyRegisteredEventToChampionship(EventRegistration eventRegistration, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		// TODO Auto-generated method stub
		LOGGER.info("Entered copyRegisteredEventToChampionship");
		User currentUser = CommonUtil.getCurrentUser();
		Championship championship = new Championship();
		championship.setName(eventRegistration.getName());
		championship.setLevel(eventRegistration.getLevel());
		championship.setStartDate(eventRegistration.getStartDate());
		championship.setEndDate(eventRegistration.getEndDate());
		championship.setLocation(eventRegistration.getLocation());
		championship.setCreatedBy(eventRegistration.getCreatedBy());
		championship.setCreatedTime(eventRegistration.getCreatedTime());
		championship.setLastModifiedBy(currentUser);
		championship.setLastModifiedTime(new Date());
		championship.setStatus(ChampionshipStatusEnum.SCHEDULED);
		Championship savedChampionship = championshipService.saveEventToChampionship(championship);
		// generate championship link
		generateChampionshipLink(savedChampionship, request);
		// create championship categories
		List<EventCategory> listEventCategory = eventRegistration.getEventCategory();
		createChampionshipCategories(savedChampionship, listEventCategory, eventRegistration);
		championshipRoundsService.saveChampionshipRoundsForChampionshipCategory(savedChampionship);
		Setting CHAMPIONSHIP_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("CHAMPIONSHIP_SUBJECT");
		Setting CHAMPIONSHIP_CONTENT = settingService.getMailTemplateValueForSubjectAndContent("CHAMPIONSHIP_CONTENT");
		// sendEmailForSuccessfulEventRegistration
		String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath();
		commonEmailUtil.sendEmailForSuccessfulEventRegistration(request, savedChampionship, CHAMPIONSHIP_SUBJECT,
				CHAMPIONSHIP_CONTENT,nysfPortalLink);
	}

	private void createChampionshipCategories(Championship savedChampionship, List<EventCategory> listEventCategory,
			EventRegistration eventRegistration) {
		List<ChampionshipCategory> listChampionshipCategory = new ArrayList<>();

		for (EventCategory category : listEventCategory) {
			ChampionshipCategory championshipCategory = new ChampionshipCategory();
			championshipCategory.setAsanaCategory(category.getAsanaCategory());
			championshipCategory.setCategory(category.getAgeCategory());
			championshipCategory.setGender(category.getGender());
			championshipCategory.setNoOfRounds(category.getNoOfRounds());
			if (category.getAsanaCategory().getId() == ASANA_CATEGORY_TRADITIONAL_ID
					|| category.getAsanaCategory().getId() == ASANA_CATEGORY_ARTISTIC_SINGLE_ID) {
				championshipCategory.setNoOfParticipants(PARTICIPANT_COUNT_TRADITIONAL_ARTISTIC_SINGLE);
			} else if (category.getAsanaCategory().getId() == ASANA_CATEGORY_ARTISTIC_PAIR_ID
					|| category.getAsanaCategory().getId() == ASANA_CATEGORY_RHYTHMIC_PAIR_ID) {
				championshipCategory.setNoOfParticipants(PARTICIPANT_COUNT_ARTISTIC_RHYTHMIC_PAIR);
			} else {
				championshipCategory.setNoOfParticipants(PARTICIPANT_COUNT_ARTISTIC_GROUP);
			}
			listChampionshipCategory.add(championshipCategory);

		}
		savedChampionship.setChampionshipCategory(listChampionshipCategory);

		try {
			championshipService.save(savedChampionship);
		} catch (Exception e) {
			LOGGER.info("Unable to update championship" + e.getMessage());
		}

	}

	private void generateChampionshipLink(Championship savedChampionship, HttpServletRequest request) {
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		String ctxPath = request.getContextPath();
		url = url.replaceFirst(uri, "");
		url = url + ctxPath;

		championshipService.saveChampionshipLink(savedChampionship, url);

	}

}
