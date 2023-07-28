package com.swayaan.nysf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.EventManagerRegistrationService;
import com.swayaan.nysf.service.EventManagerService;
import com.swayaan.nysf.service.RecaptchaService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

@Controller
@RequestMapping("/admin")
public class ManageEventManagerRegistrationController {
	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	StateService stateService;
	@Autowired
	SettingService settingService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	EventManagerRegistrationService eventManagerRegistrationService;
	@Autowired
	RecaptchaService captchaService;
	@Autowired CommonEmailUtil commonEmailUtil;
	@Autowired CommonUtil commonUtil;
	@Autowired EventManagerService eventManagerService;
	@Autowired PasswordEncodeUtil passwordEncodeUtil;
	@Autowired
	UserService userService;
	
	private static final Integer ROLE_EVENT_MANAGER = 4;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageEventManagerRegistrationController.class);
	
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-eventmanager-registrations/save")
	public String saveEventManagerRegistration(EventManagerRegistration eventManagerRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException, EventManagerNotFoundException, UserNotFoundException {
		LOGGER.info("Entered saveEventManagerRegistration method -EventManagerRegistrationController");
		if (eventManagerRegistration.isApprovalStatus()) {
			eventManagerRegistration.setEnabled(true);
			eventManagerRegistration.setApprovalStatus(true);
			eventManagerRegistrationService.save(eventManagerRegistration);

			copyRegisteredEventManngerToEventManager(eventManagerRegistration, request);

			redirectAttributes.addFlashAttribute("message", "The EventManager has been approved successfully.");
		}
		eventManagerRegistrationService.save(eventManagerRegistration);
		redirectAttributes.addFlashAttribute("message", "The Event Manager " +eventManagerRegistration.getFirstName() +" has been saved successfully.");
		LOGGER.info("Exit saveEventManagerRegistration method -EventManagerRegistrationController");

		return "redirect:/admin/manage-eventmanager-registrations";
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager-registrations")
	public String listFirstPageEventManagerRegistrations(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageEventManagerRegistrations method -EventManagerRegistrationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		LOGGER.info("Exit listFirstPageEventManagerRegistrations method -EventManagerRegistrationController");

		return listAllEventManagerRegistrationsByPage(1, model, "name", "asc", "","", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager-registrations/page/{pageNum}")
	public String listAllEventManagerRegistrationsByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String district,HttpServletRequest request) {
		LOGGER.info("Entered listAllEventManagerRegistrationsByPage method -EventManagerRegistrationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();

		CommonUtil.setNavigationHistory(mappedUrl, currentUser);



		Page<EventManagerRegistration> page=eventManagerRegistrationService.listByPage(pageNum, sortField, sortDir, name, district);
		List<EventManagerRegistration> listEventManagerRegistrations = eventManagerRegistrationService.listAllNonApprovedEventManagers();

		 listEventManagerRegistrations = page.getContent();
			
		long startCount = (pageNum - 1) * eventManagerRegistrationService.RECORDS_PER_PAGE + 1;
		long endCount = startCount + eventManagerRegistrationService.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-eventmanager-registrations");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", district);
		EventManagerRegistration eventManagerRegistration = new EventManagerRegistration();
		model.addAttribute("listEventManagerRegistrations", listEventManagerRegistrations);
		model.addAttribute("pageTitle", "Manage EventManager Registrations");
		model.addAttribute("eventManagerRegistration", eventManagerRegistration);
		LOGGER.info("Exit listAllEventManagerRegistrationsByPage method -EventManagerRegistrationController");

		return "administration/manage_eventmanager_registrations";
	}
/*	public String listAllEventManagerRegistrations(Model model) {
		LOGGER.info("Entered listAllEventManagerRegistrations EventManagerRegistrationController");
		List<EventManagerRegistration> listEventManagerRegistrations = eventManagerRegistrationService.listAllNonApprovedEventManagers();
		model.addAttribute("pageTitle", "Manage EventManager Registrations");
		model.addAttribute("listEventManagerRegistrations", listEventManagerRegistrations);
		return "administration/manage_eventmanager_registrations";
	}*/

	private void copyRegisteredEventManngerToEventManager(EventManagerRegistration eventManagerRegistration,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, EventManagerNotFoundException, UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("copyRegisteredEventManngerToEventManager");
		EventManager eventManager= new EventManager();
		eventManager.setFirstName(eventManagerRegistration.getFirstName());
		eventManager.setLastName(eventManagerRegistration.getLastName());
		eventManager.setEmail(eventManagerRegistration.getEmail());
		eventManager.setPhoneNumber(eventManagerRegistration.getPhoneNumber());
		eventManager.setGender(eventManagerRegistration.getGender());
		eventManager.setAddressLine1(eventManagerRegistration.getAddressLine1());
		eventManager.setAddressLine2(eventManagerRegistration.getAddressLine2());
		eventManager.setTown(eventManagerRegistration.getTown());
		eventManager.setDistrict(eventManagerRegistration.getDistrict());
		eventManager.setState(eventManagerRegistration.getState());
		eventManager.setPostalCode(eventManagerRegistration.getPostalCode());
		eventManager.setImage(eventManagerRegistration.getImage());
		eventManager.setDesignation(null);
		eventManager.setAcceptRules(true);
		String ernNumber=commonUtil.getSystemGeneratedNumber();
		eventManager.setUserName(ernNumber);
		eventManager.setErnNumber(ernNumber);
		String eventManagerPassword = "Ern$" + ernNumber;
		String encodedPassword = passwordEncodeUtil.encodePassword(eventManagerPassword);
		eventManager.setEmail(eventManagerRegistration.getEmail());
		
		eventManager.setPassword(encodedPassword);
		eventManager.setEnabled(true);
		eventManagerService.save(eventManager);
		String eventManagerEmailId = eventManager.getEmail();
		String eventManagerFullName = eventManager.getFullName();
		String eventManagerUserName = eventManager.getUserName();

		Setting EVENTMANAGER_ERN_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_SUBJECT");
		Setting EVENTMANAGER_ERN_CONTENT = settingService.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_CONTENT");
		//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		String token = userService.updateResetPasswordToken(eventManagerUserName);
		String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
		commonEmailUtil.sendEmail(request, eventManagerFullName, eventManagerEmailId, eventManagerPassword, eventManagerUserName,
				ROLE_EVENT_MANAGER,EVENTMANAGER_ERN_SUBJECT,EVENTMANAGER_ERN_CONTENT,passwordLink);

	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager-registrations/{id}/approved/{status}")
	public String updateEventManagerApprovedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean approved,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			{
		LOGGER.info("Entered updateEventManagerApprovedStatus method -EventManagerRegistrationController");

		if (approved) {
			// soft delete
			try {
				EventManagerRegistration eventManagerRegistration = eventManagerRegistrationService.get(id);
				boolean isEmailPresent = userService.ExistByEmail(eventManagerRegistration.getEmail());
				try {
					if(isEmailPresent) {
						redirectAttributes.addFlashAttribute("errorMessage", "The email id "+eventManagerRegistration.getEmail() +" is already present");
						return "redirect:/admin/manage-eventmanager-registrations";
					}
					copyRegisteredEventManngerToEventManager(eventManagerRegistration, request);
				} catch (Exception e ) {
					LOGGER.error("Unable to save Event manager" + e.getMessage());
					redirectAttributes.addFlashAttribute("errormessage", "Event Manager Approval Failed. Try Again.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("this is enabled " + id);
			eventManagerRegistrationService.updateEventManagerRegistrationEnabledStatus(id, true);
			eventManagerRegistrationService.updateEventManagerRegistrationApprovedStatus(id, approved);
		} else {
			eventManagerRegistrationService.updateEventManagerRegistrationEnabledStatus(id, true);
		}
		EventManagerRegistration eventManagerRegistration=null;
		try {
			eventManagerRegistration = eventManagerRegistrationService.get(id);
		} catch (EventManagerNotFoundException e) {
			e.printStackTrace();
		}
		String status = approved ? "approved" : "disapproved";
		String message = "The Event Manager " + eventManagerRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateEventManagerApprovedStatus method -EventManagerRegistrationController");

		return "redirect:/admin/manage-eventmanager-registrations";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager-registrations/edit/{id}")
	public String editEventManagerRegistration(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes)  {
		LOGGER.info("Entered editEventManagerRegistration method -EventManagerRegistrationController");
		try {
			EventManagerRegistration eventManagerRegistration= eventManagerRegistrationService.get(id);
			List<State> listStates = stateService.listAllStates();
			model.addAttribute("eventManagerRegistration", eventManagerRegistration);
			model.addAttribute("listStates", listStates);
			model.addAttribute("pageTitle", "Edit Event Manager Registered");
			return "administration/edit_eventmanager_registration_form";

		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editEventManagerRegistration method -EventManagerRegistrationController");

			return "redirect:/admin/manage-eventmanager-registrations";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager-registrations/delete/{id}")
	public String deleteEventManagerRegistration(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes)  {
		LOGGER.info("Entered deleteEventManagerRegistration method -EventManagerRegistrationController");
		try {
			EventManagerRegistration eventManagerRegistration = eventManagerRegistrationService.get(id);
			eventManagerRegistrationService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The Event Manager " + eventManagerRegistration.getFullName() + " has been deleted successfully");

		} catch (Exception ex) {
			LOGGER.error("EventManager Not Found" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteEventManagerRegistration method -EventManagerRegistrationController");

		return "redirect:/admin/manage-eventmanager-registrations";
	}
	

	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-eventmanager-registrations/{id}/enabled/{status}")
	public String updateEventManagerEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered updateEventManagerEnabledStatus method -EventManagerRegistrationController");
		eventManagerRegistrationService.updateEventManagerRegistrationEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";
		String message = "The Event Manager ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateEventManagerEnabledStatus method -EventManagerRegistrationController");

		return "redirect:/admin/manage-eventmanager-registrations";
	}

}
