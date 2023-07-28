package com.swayaan.nysf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.DistrictService;
import com.swayaan.nysf.service.EventManagerRegistrationService;
import com.swayaan.nysf.service.EventManagerService;
import com.swayaan.nysf.service.EventRegistrationService;
import com.swayaan.nysf.service.NavigationHistoryService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserAuditLogService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.FileUploadUtil;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

@Controller
@RequestMapping("/admin")
public class ManageEventManagerController {

	@Autowired
	EventManagerService service;
	@Autowired
	EventManagerRegistrationService eventManagerRegistrationService;

	@Autowired
	StateService stateService;
	@Value("${site.admin-mail}")
	private String siteAdminMail;
	@Autowired
	CommonEmailUtil emailUtil;

	@Autowired
	PasswordEncodeUtil passwordUtil;

	@Autowired
	CommonUtil commonUtil;

	@Autowired
	SettingService settingService;

	@Autowired
	AgeCategoryService ageCategoryService;
	

	@Autowired
	DistrictService districtService;

	@Autowired
	ChampionshipService championshipService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	EventRegistrationService eventRegistrationService;
	
	@Autowired
	NavigationHistoryService navigationHistoryService;

	@Autowired
	UserAuditLogService userAuditLogService;
	
	@Autowired
	UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageEventManagerController.class);
	private static final Integer ROLE_EVENTMANAGER = 4;

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager")
	public String listFirstPageEventManager(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageEventManager method -ManageEventManagerController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageEventManager method -ManageEventManagerController");

		return listAllEventManagerByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager/page/{pageNum}")
	public String listAllEventManagerByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String ernNumber, HttpServletRequest request) {
		LOGGER.info("Entered listAllEventManagerByPage method -ManageEventManagerController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Page<EventManager> page = service.listByPage(pageNum, sortField, sortDir, name, ernNumber);
		List<EventManager> listEventManager = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-eventmanager");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", ernNumber);
		EventManager eventManager = new EventManager();
		model.addAttribute("listEventManager", listEventManager);
		model.addAttribute("pageTitle", "Manage Event Manager");
		model.addAttribute("eventManager", eventManager);
		LOGGER.info("Exit listAllEventManagerByPage method -ManageEventManagerController");

		return "administration/manage_eventmanager";
	}

//	@GetMapping("/manage-eventmanager")
//	public String listAllEventManager(Model model,HttpServletRequest request) {
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		List<EventManager> listEventManager=service.listAll();
//		EventManager eventManager=new EventManager();
//		model.addAttribute("pageTitle", "Manage EventManager");
//		model.addAttribute("listEventManager", listEventManager);
//		model.addAttribute("eventManager", eventManager);
//		return "administration/manage_eventmanager";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager/new")
	public String newEventManager(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newEventManager method -ManageEventManagerController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		List<Role> listRoles = service.listRoles();

		List<State> listStates = stateService.listAllStates();

		EventManager eventManager = new EventManager();
		eventManager.setEnabled(true);

		model.addAttribute("eventManager", eventManager);
		model.addAttribute("pageTitle", "Add Event Manager");
		model.addAttribute("listStates", listStates);
		model.addAttribute("listRoles", listRoles);
		LOGGER.info("Exit newEventManager method -ManageEventManagerController");

		return "administration/event_manager_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-eventmanager/save")
	public String saveEventManager(@ModelAttribute("eventManager") @Valid EventManager eventManager, Model model,
			@RequestParam(value = "eventManagerImage", required = false) MultipartFile eventManagerImage,
			RedirectAttributes re, HttpServletRequest request)
			throws IOException, EventManagerNotFoundException, MessagingException, UserNotFoundException {
		LOGGER.info("Entered saveEventManager method -ManageEventManagerController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		if (!this.checkIfEditFlow(eventManager, eventManagerImage)) {
			String eventManagerImageFileName = null;

			if (!eventManagerImage.isEmpty()) {
				eventManagerImageFileName = StringUtils.cleanPath(eventManagerImage.getOriginalFilename());
				eventManager.setImage(eventManagerImageFileName);

			} else {
				if (eventManager.getImage().isEmpty()) {
					eventManager.setImage(null);
				}
			}
			eventManager.setEnabled(true);
			String number = commonUtil.getSystemGeneratedNumber();
			String eventManagerPassword = "Ern$" + String.valueOf(number);
			String encryptedPassword = passwordEncoder.encode(eventManagerPassword);
			eventManager.setErnNumber(number);
			eventManager.setPassword(encryptedPassword);
			eventManager.setUserName(number);
			String eventManagerUsername=eventManager.getUserName();
			String eventManagerEmailId = eventManager.getEmail();
			String eventManagerFullName = eventManager.getFullName();
			Setting EVENTMANAGER_ERN_SUBJECT = settingService
					.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_SUBJECT");
			Setting EVENTMANAGER_ERN_Content = settingService
					.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_Content");
			EventManager savedEventManager = service.save(eventManager);

			// send email
			//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
			String token = userService.updateResetPasswordToken(eventManagerUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, eventManagerFullName, eventManagerEmailId, eventManagerPassword, number,
					ROLE_EVENTMANAGER, EVENTMANAGER_ERN_SUBJECT, EVENTMANAGER_ERN_Content,passwordLink);

			String uploadDir = "eventmanager-reg-uploads/" + savedEventManager.getEmail();
			if (!eventManagerImage.isEmpty()) {
				FileUploadUtil.saveFile(uploadDir, eventManagerImageFileName, eventManagerImage);
			}

			re.addFlashAttribute("message",
					"The Event Manager " + eventManager.getFullName() + " has been saved successfully.");
		} else {
			service.save(eventManager);
			re.addFlashAttribute("message",
					"The Event Manager " + eventManager.getFullName() + " has been updated successfully.");
		}
		LOGGER.info("Exit saveEventManager method -ManageEventManagerController");

		return "redirect:/admin/manage-eventmanager";
	}


	private boolean checkIfEditFlow(@Valid EventManager eventManager, MultipartFile eventManagerImage) {
		
		if(eventManager.getId() !=null) {
			return true;
		}
		return false;
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager/edit/{id}")
	public String editEventManager(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editEventManager method -ManageEventManagerController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			EventManager eventManager = service.get(id);

			List<State> listStates = stateService.listAllStates();
			List<Role> listRoles = service.listRoles();
			model.addAttribute("eventManager", eventManager);
			model.addAttribute("pageTitle", "Edit Event Manager");
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("listStates", listStates);

			return "administration/event_manager_form";
		} catch (EventManagerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editEventManager method -ManageEventManagerController");

			return "redirect:/admin/manage-eventmanager";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager/delete/{id}")
	public String deleteEventManager(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws UserNotFoundException {
		LOGGER.info("Entered deleteEventManager method -ManageEventManagerController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			EventManager eventManager = service.get(id);

			if (eventManager.getEmail().equals(siteAdminMail)) {
				redirectAttributes.addFlashAttribute("message1", "The Administrator cannot be deleted!");
			} else {

				List<AgeCategory> listAgeCategory = ageCategoryService.getAgeCategoryByEventManager(eventManager);
				if (!listAgeCategory.isEmpty()) {
					redirectAttributes.addFlashAttribute("message1", "The EventManager " + eventManager.getFullName()
							+ " cannot be deleted because this eventManager is already assigned");
					return "redirect:/admin/manage-eventmanager";

				}
				List<Championship> listChampionship = championshipService
						.getChampionshipByCreatedBy(eventManager.getId());
				if (!listChampionship.isEmpty()) {
					redirectAttributes.addFlashAttribute("message1", "The EventManager " + eventManager.getFullName()
							+ " cannot be deleted because this eventManager is already assigned");
					return "redirect:/admin/manage-eventmanager";

				}
				List<EventRegistration> listEventRegistration = eventRegistrationService
						.getEventRegistrationByCreatedBy(eventManager.getId());
				if (!listEventRegistration.isEmpty()) {
					redirectAttributes.addFlashAttribute("message1", "The EventManager " + eventManager.getFullName()
							+ " cannot be deleted because this eventManager is already assigned");
					return "redirect:/admin/manage-eventmanager";

				}
				
				List<NavigationHistory> listNavigationHistory = navigationHistoryService
						.getNavigationHistoryByUserId(eventManager);
				if (!listNavigationHistory.isEmpty()) {
					for(NavigationHistory navHistory : listNavigationHistory) {
						navigationHistoryService.delete(navHistory.getId());
					}

				}
				
				List<UserAuditLog> listUserAuditLog = userAuditLogService
						.listUserAuditLogForUser(eventManager.getId());
				if (!listNavigationHistory.isEmpty()) {
					for(UserAuditLog navHistory : listUserAuditLog) {
						userAuditLogService.delete(navHistory.getId());
					}

				}

				service.delete(id);
				redirectAttributes.addFlashAttribute("message",
						"The eventmanager " + eventManager.getFullName() + " has been deleted successfully");
			}

		} catch (EventManagerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteEventManager method -ManageEventManagerController");

		return "redirect:/admin/manage-eventmanager";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager/{id}/enabled/{status}")
	public String updateEventManagerEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EventManagerNotFoundException {
		LOGGER.info("Entered updateEventManagerEnabledStatus method -ManageEventManagerController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		EventManager eventManager = service.get(id);
		if (eventManager.getEmail().equals(siteAdminMail)) {
			service.updateEventManagerEnabledStatus(id, true);
			redirectAttributes.addFlashAttribute("message", "The Administrator cannot be disabled!");
		} else {
			service.updateEventManagerEnabledStatus(id, enabled);
			String status = enabled ? "enabled" : "disabled";
			String message = "The judge " + eventManager.getFullName() + " has been " + status;
			redirectAttributes.addFlashAttribute("message", message);
		}
		LOGGER.info("Exit updateEventManagerEnabledStatus method -ManageEventManagerController");

		return "redirect:/admin/manage-eventmanager";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/send-email/eventmanager/{id}")
	public String sendEmailToEventManager(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws EventManagerNotFoundException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered sendEmailToEventManager method -ManageEventManagerController");

		try {
			EventManager eventManager = service.get(id);

			String eventManagerPassword = "ERN#" + String.valueOf(eventManager.getErnNumber());
			String ernNumberString = String.valueOf(eventManager.getErnNumber());
			String eventManagerEmailId = eventManager.getEmail();
			String eventManagerFullName = eventManager.getFullName();
			String eventManagerUsername = eventManager.getUserName();
			Integer eventManagerRoleId = eventManager.getRoleId();
			Setting PARTICIPANT_PRN_SUBJECT = settingService
					.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_SUBJECT");
			Setting PARTICIPANT_PRN_Content = settingService
					.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_CONTENT");
			
			//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
			String token = userService.updateResetPasswordToken(eventManagerUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, eventManagerFullName, eventManagerEmailId, eventManagerPassword,
					ernNumberString, eventManagerRoleId, PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content,passwordLink);

			redirectAttributes.addFlashAttribute("message",
					"The Email has been sent to the  " +  eventManager.getFullName() + " successfully");
		} catch (EventManagerNotFoundException | UserNotFoundException ex) {
			LOGGER.error("eventmanager not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit sendEmailToEventManager method -ManageEventManagerController");

		return "redirect:/admin/manage-eventmanager";
	}

}
