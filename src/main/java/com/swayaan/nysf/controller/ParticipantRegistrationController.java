package com.swayaan.nysf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Club;
import com.swayaan.nysf.entity.EmailSettingBag;
//import com.swayaan.nysf.entity.Event;
//import com.swayaan.nysf.entity.EventParticipantRegistrations;
//import com.swayaan.nysf.entity.EventParticipants;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
//import com.swayaan.nysf.exception.EventNotFoundException;
import com.swayaan.nysf.exception.ParticipantRegistrationNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
//import com.swayaan.nysf.exception.RefereeRegistrationNotFoundException;
import com.swayaan.nysf.repository.ParticipantRepository;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.ClubService;
//import com.swayaan.nysf.service.EventParticipantRegistrationsService;
//import com.swayaan.nysf.service.EventParticipantsService;
//import com.swayaan.nysf.service.EventService;
import com.swayaan.nysf.service.ParticipantRegistrationService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.RecaptchaService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
public class ParticipantRegistrationController {
	
	@Autowired ParticipantRegistrationService service;
	@Autowired ParticipantRepository repo;
	@Autowired AsanaCategoryService asanaCategoryService;
	@Autowired StateService stateService;
	@Autowired ParticipantService participantService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	SettingService settingService;
	@Autowired RoleService roleService; 
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	CommonEmailUtil emailUtil;
	
	@Autowired
	CommonUtil commonUtil;
	
	@Autowired
	ClubService clubService;
	@Autowired
	RecaptchaService captchaService;
	@Autowired
	UserService userService;


//	@Autowired EventService eventService;
//	@Autowired EventParticipantsService eventParticipantsService;
//	@Autowired EventParticipantRegistrationsService eventParticipantRegistrationsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantRegistrationController.class);

	
	
	
	@GetMapping("/manage-registration/register/participant")
	public String showRegistrationForm(Model model) {
		LOGGER.info("Entered showRegistrationForm method -ParticipantRegistrationController");
		ParticipantRegistration participantRegistration = new ParticipantRegistration();
		List<ParticipantRegistration> listParticipantForm = service.showParticipantForm();
		List<AsanaCategory> listasanaCategories = asanaCategoryService.listAllAsanaCategories();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		List<State> listStates = stateService.listAllStates();
		
		model.addAttribute("participantRegistration", participantRegistration);
		model.addAttribute("listParticipantForm", listParticipantForm);
		model.addAttribute("listasanaCategories", listasanaCategories);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("listStates", listStates);
		
		//model.addAttribute("pageTitle", "Participant Form");
		LOGGER.info("Exit showRegistrationForm method -ParticipantRegistrationController");

		return "participant_registration_form";
		}
	
	@PostMapping("/manage-registration/register/participant/save")
	public String saveRegistrationForm(Model model,
			ParticipantRegistration participantRegistration,
			@RequestParam("participantImage") MultipartFile participantImage,
	//		@RequestParam("birthCertificateFile") MultipartFile birthCertificateFile,
//			@RequestParam("medicalCertificateFile") MultipartFile medicalCertificateFile,
//			@RequestParam("paymentReceiptFile") MultipartFile paymentReceiptFile,
			//@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, 
			HttpServletRequest request,
			RedirectAttributes re) throws IOException {
		LOGGER.info("Entered saveRegistrationForm method -ParticipantRegistrationController");
		String participantImageFileName = null;
		String birthCertificateFileName = null;
		String medicalCertificateFileName = null;
		String paymentReceiptFileName = null;
		
		if (!participantImage.isEmpty()) {
			participantImageFileName = StringUtils.cleanPath(participantImage.getOriginalFilename());
			participantRegistration.setImage(participantImageFileName);
		} else {
			if (participantRegistration.getImage().isEmpty()) {
				participantRegistration.setImage(null);
			}
		}
		/*
		if (!birthCertificateFile.isEmpty()) {
			birthCertificateFileName = StringUtils.cleanPath(birthCertificateFile.getOriginalFilename());
			participantRegistration.setBirthCertificate(birthCertificateFileName);
		} else {
			if (participantRegistration.getBirthCertificate().isEmpty()) {
				participantRegistration.setBirthCertificate(null);
			}
		}
		
		if (!medicalCertificateFile.isEmpty()) {
			medicalCertificateFileName = StringUtils.cleanPath(medicalCertificateFile.getOriginalFilename());
			participantRegistration.setMedicalCertificate(medicalCertificateFileName);
		} else {
			if (participantRegistration.getMedicalCertificate().isEmpty()) {
				participantRegistration.setMedicalCertificate(null);
			}
		}
		
		if (!paymentReceiptFile.isEmpty()) {
			paymentReceiptFileName = StringUtils.cleanPath(paymentReceiptFile.getOriginalFilename());
			participantRegistration.setPaymentReceipt(paymentReceiptFileName);
		} else {
			if (participantRegistration.getPaymentReceipt().isEmpty()) {
				participantRegistration.setPaymentReceipt(null);
			}
		} */
		
//		if ( !service.checkCaptcha(recaptchaResponse, request)) {
//			
//			re.addFlashAttribute("message1","Please click on Recaptcha to verify that you are human");
//			
//			return "redirect:/manage-registration/register/participant";
//		
//		} else {
		participantRegistration.setEnabled(true);
		participantRegistration.setApprovalStatus(false);	
		ParticipantRegistration savedParticipant = service.save(participantRegistration);
		String uploadDir = "participant-reg-uploads/" + savedParticipant.getEmail();
		if(!participantImage.isEmpty()) {
			FileUploadUtil.saveFile(uploadDir, participantImageFileName, participantImage);
		}
//		if(!birthCertificateFile.isEmpty()) {
//			FileUploadUtil.saveFile(uploadDir, birthCertificateFileName, birthCertificateFile);
//		}
//		if(!medicalCertificateFile.isEmpty()) {
//			FileUploadUtil.saveFile(uploadDir, medicalCertificateFileName, medicalCertificateFile);
//		}
//		if(!paymentReceiptFile.isEmpty()) {
//			FileUploadUtil.saveFile(uploadDir, paymentReceiptFileName, paymentReceiptFile);
//		}
		
		re.addFlashAttribute("message", "You have been registered successfully. You will recieve an e-mail after Approval!");
		LOGGER.info("Exit saveRegistrationForm method -ParticipantRegistrationController");

		return "redirect:/login";
		}
//	}
	
	
	@GetMapping("/participant/select-event/{id}")
	public String showRegistrationEventForm(@PathVariable("id") Integer id, Model model) {
		LOGGER.info("Entered showRegistrationEventForm method -ParticipantRegistrationController");
		try {
			ParticipantRegistration participantRegistration = service.get(id);
//			List<Event> listEvents = eventService.listAllEvents();
//			List<AsanaCategory> listCategories = asanaCategoryService.listAllAsanaCategories();
//			
//			model.addAttribute("listCategories", listCategories);
//			model.addAttribute("participantRegistration", participantRegistration);
//			model.addAttribute("listEvents", listEvents);
			LOGGER.info("Exit showRegistrationEventForm method -ParticipantRegistrationController");

			return "participant_reg_event_form";
		} catch (ParticipantRegistrationNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@PostMapping("/participant/select-event/{id}/update")
	public String saveRegistrationEventForm(@RequestParam(value="event",required=true) Integer event_id, @PathVariable("id") Integer id, 
			ParticipantRegistration participantRegistration, 
			Model model,
			RedirectAttributes re) {
		LOGGER.info("Entered saveRegistrationEventForm method -ParticipantRegistrationController");
		
//		Event event;
//		try {
//			event = eventService.get(event_id);
//			EventParticipantRegistrations eventParticipantRegistrations = new EventParticipantRegistrations();
//			eventParticipantRegistrations.setEvent(event);
//			eventParticipantRegistrations.setParticipantRegistration(participantRegistration);
//			eventParticipantRegistrationsService.save(eventParticipantRegistrations);
//			
//			String message = "The Participant details has been updated successfully!";
//			re.addFlashAttribute("message", message);
//			
//		} catch (EventNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		LOGGER.info("Exit saveRegistrationEventForm method -ParticipantRegistrationController");

		return "redirect:/participant/registration/";	
	}
	

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-participant-registrations")
	public String listFirstPageParticipantRegistrations(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageParticipantRegistrations method -ParticipantRegistrationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		LOGGER.info("Exit listFirstPageParticipantRegistrations method -ParticipantRegistrationController");

		return listAllParticipantRegistrationsByPage(1, model, "name", "asc", "", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-participant-registrations/page/{pageNum}")
	public String listAllParticipantRegistrationsByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name, HttpServletRequest request) {
		LOGGER.info("Entered listAllParticipantRegistrationsByPage method -ParticipantRegistrationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();

		CommonUtil.setNavigationHistory(mappedUrl, currentUser);



		Page<ParticipantRegistration> page=service.listByPage(pageNum, sortField, sortDir, name);
		List<ParticipantRegistration> listParticipantRegistrations = service.listAllNonApprovedParticpants();

		 listParticipantRegistrations = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-participant-registrations");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
	//	model.addAttribute("keyword2", jrnNumber);
		ParticipantRegistration participantRegistration = new ParticipantRegistration();
		model.addAttribute("listParticipantRegistrations", listParticipantRegistrations);
		model.addAttribute("pageTitle", "Manage Participant Registrations");
		model.addAttribute("participantRegistration", participantRegistration);
		LOGGER.info("Exit listAllParticipantRegistrationsByPage method -ParticipantRegistrationController");

		return "administration/manage_participant_registrations";
	}

/*	public String listAllParticipantRegistrations(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listAllParticipantRegistrations ParticipantRegistrationController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		List<ParticipantRegistration> listParticipantRegistrations = service.listAllNonApprovedParticpants();
		model.addAttribute("pageTitle", "Manage Participant Registrations");
		model.addAttribute("listParticipantRegistrations", listParticipantRegistrations);
		return "administration/manage_participant_registrations";
	}*/
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/admin/manage-participant-registrations/save")
	public String saveParticipantRegistration(ParticipantRegistration participantRegistration ,Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws IOException,UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered saveParticipantRegistration method -ParticipantRegistrationController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		if(participantRegistration.isApprovalStatus()) {
			participantRegistration.setEnabled(false);
			participantRegistration.setApprovalStatus(true);
			service.save(participantRegistration);
			
			try {
				copyRegisteredParticipantToParticipant(participantRegistration,request);
			} catch (UnsupportedEncodingException | MessagingException | ParticipantNotFoundException | UserNotFoundException e) {
				LOGGER.error("Unable to save participant" + e.getMessage());
				redirectAttributes.addFlashAttribute("errormessage", "Participant Approval Failed. Try Again.");
			}
			
			
			redirectAttributes.addFlashAttribute("message", "The Participant " +participantRegistration.getFullName() + "has been approved successfully.");
		}
		service.save(participantRegistration);
		redirectAttributes.addFlashAttribute("message", "The Participant " + participantRegistration.getFullName() + "has been saved successfully.");
		LOGGER.info("Exit saveParticipantRegistration method -ParticipantRegistrationController");

		return "redirect:/admin/manage-participant-registrations";
	}
	

	private void copyRegisteredParticipantToParticipant(ParticipantRegistration participantRegistration,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, ParticipantNotFoundException, UserNotFoundException {
		LOGGER.info("Entered copyRegisteredParticipantToParticipant method -ParticipantRegistrationController");
		// copy the details of the participantRegistration table to the  table
		Participant participant = new Participant();
		participant.setFirstName(participantRegistration.getFirstName());
		participant.setLastName(participantRegistration.getLastName());
		participant.setEmail(participantRegistration.getEmail());
		participant.setPhoneNumber(participantRegistration.getPhoneNumber());
	//	participant.setCategory(participantRegistration.getCategory());
		participant.setDob(participantRegistration.getDob());
		participant.setAadharNumber(participantRegistration.getAadharNumber());
		participant.setGender(participantRegistration.getGender());
		//participant.setAsanaCategory(participantRegistration.getAsanaCategory());
		participant.setAddressLine1(participantRegistration.getAddressLine1());
		participant.setAddressLine2(participantRegistration.getAddressLine2());
		participant.setCertificateNumber(participantRegistration.getCertificateNumber());
		participant.setTown(participantRegistration.getTown());
		participant.setDistrict(participantRegistration.getDistrict());
		participant.setState(participantRegistration.getState());
		participant.setPhysicallyFit(true);
		participant.setPaymentTransactionId(participantRegistration.getPaymentTransactionId());
		participant.setPostalCode(participantRegistration.getPostalCode());
//		participant.setOtherArtisticGroupPlayerName(participantRegistration.getOtherArtisticGroupPlayerName());
//		participant.setOtherArtisticPairPlayerName(participantRegistration.getOtherArtisticPairPlayerName());
//		participant.setOtherRhythmicPairPlayerName(participantRegistration.getOtherRhythmicPairPlayerName());
//		participant.setUrlArtisticGroup(participantRegistration.getUrlArtisticGroup());
//		participant.setUrlArtisticPair(participantRegistration.getUrlArtisticPair());
//		participant.setUrlArtisticSingle(participantRegistration.getUrlArtisticSingle());
//		participant.setUrlTraditional(participantRegistration.getUrlTraditional());
//		participant.setUrlRhythmicPair(participantRegistration.getUrlRhythmicPair());
		participant.setImage(participantRegistration.getImage());
		participant.setBirthCertificate(participantRegistration.getBirthCertificate());
		participant.setMedicalCertificate(participantRegistration.getMedicalCertificate());
		participant.setPaymentReceipt(participantRegistration.getPaymentReceipt());
		participant.setUserPrnNumber(String.valueOf(participantRegistration.getUserPrnNumber()));
		participant.setAcceptRules(true);
		String number = commonUtil.getSystemGeneratedNumber();
		String participantPassword = "Prn$"+number;
		String encryptedPassword = passwordEncoder.encode(participantPassword);	
		participant.setPrnNumber(number);
		participant.setUserName(number);
		participant.setPassword(encryptedPassword);
		participant.setEnabled(true); // make it false after email verification flow is added.
		participantService.save(participant);
		String participantUsername=participant.getUserName();
		String participantEmailId = participant.getEmail();
		String participantFullName = participant.getFullName();
		Integer participantRoleId=participant.getRoleId();
		Setting PARTICIPANT_PRN_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_SUBJECT");
		Setting PARTICIPANT_PRN_Content = settingService.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_CONTENT");
		//SEND VERIFICATION EMAIL FLOW
		//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		String token = userService.updateResetPasswordToken(participantUsername);
		String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
		emailUtil.sendEmail(request, participantFullName, participantEmailId, participantPassword, number,
				participantRoleId,PARTICIPANT_PRN_SUBJECT,PARTICIPANT_PRN_Content,passwordLink);
		
		//Participant savedParticipant = participantService.save(participant);
		
//		List<EventParticipantRegistrations> listSelectedEvents = eventParticipantRegistrationsService.listAllEventParticipantRegistrationsByParticipant(participantRegistration);
//		System.out.println("listSelectedEvents :" + listSelectedEvents);
//		for(EventParticipantRegistrations eventParticipantRegistration : listSelectedEvents) {
//			EventParticipants eventParticipant = new EventParticipants();
//			eventParticipant.setEvent(eventParticipantRegistration.getEvent());
//			eventParticipant.setParticipant(savedParticipant);
//			
//			System.out.println("event name :" + eventParticipantRegistration.getEvent().getName());
//			eventParticipantsService.save(eventParticipant);
//		}
	}
	

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-participant-registrations/{id}/enabled/{status}")
	public String updateParticipantEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes,HttpServletRequest request) throws ParticipantRegistrationNotFoundException {
		LOGGER.info("Entered updateParticipantEnabledStatus method -ParticipantRegistrationController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		service.updateParticipantRegistrationEnabledStatus(id, enabled);
		ParticipantRegistration participantRegistration = service.get(id);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Participant " + participantRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateParticipantEnabledStatus method -ParticipantRegistrationController");

		return "redirect:/admin/manage-participant-registrations";
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-participant-registrations/{id}/approved/{status}")
	public String updateParticipantApprovedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean approved,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws ParticipantRegistrationNotFoundException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered updateParticipantApprovedStatus method -ParticipantRegistrationController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
				
		
		if(approved) {
			//soft delete
			try {
				ParticipantRegistration participantRegistration = service.get(id);
				boolean isEmailPresent = userService.ExistByEmail(participantRegistration.getEmail());
				try {
					if(isEmailPresent) {
						redirectAttributes.addFlashAttribute("errorMessage", "The email id "+participantRegistration.getEmail() +" is already present");
						return "redirect:/admin/manage-participant-registrations";
					}
					copyRegisteredParticipantToParticipant(participantRegistration,request);
				} catch (UnsupportedEncodingException | MessagingException | ParticipantNotFoundException | UserNotFoundException e) {
					LOGGER.error("Unable to save participant" + e.getMessage());
					redirectAttributes.addFlashAttribute("errorMessage", "Participant Approval Failed. Try Again.");
				}
				
			} catch (ParticipantRegistrationNotFoundException e) {
				e.printStackTrace();
			}
			service.updateParticipantRegistrationEnabledStatus(id, false);
			service.updateParticipantRegistrationApprovedStatus(id, approved);
		} else {
			service.updateParticipantRegistrationEnabledStatus(id, true);
		}
		ParticipantRegistration participantRegistration = service.get(id);
		String status = approved ? "approved" : "disapproved";
		String message = "The Participant " + participantRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateParticipantApprovedStatus method -ParticipantRegistrationController");

		return "redirect:/admin/manage-participant-registrations";
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-participant-registrations/edit/{id}")
	public String editParticipantRegistration(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ParticipantRegistrationNotFoundException {
		LOGGER.info("Entered editParticipantRegistration method -ParticipantRegistrationController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		try {
			ParticipantRegistration participantRegistration = service.get(id);
			List<AsanaCategory> listasanaCategories = asanaCategoryService.listAllAsanaCategories();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			List<State> listStates = stateService.listAllStates();
			model.addAttribute("participantRegistration", participantRegistration);
			model.addAttribute("listasanaCategories", listasanaCategories);
			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("listStates", listStates);
			model.addAttribute("pageTitle", "Edit Participant Registered");
			return "administration/edit_participant_registration_form";
			
		} catch (ParticipantRegistrationNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editParticipantRegistration method -ParticipantRegistrationController");

			return "redirect:/admin/manage-participant-registrations";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-participant-registrations/delete/{id}")
	public String deleteParticipantRegistration(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes,HttpServletRequest request) throws ParticipantRegistrationNotFoundException {
		LOGGER.info("Entered deleteParticipantRegistration method -ParticipantRegistrationController");
		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		try {
			ParticipantRegistration participantRegistration = service.get(id);
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The participant ID " + participantRegistration.getFirstName() + " has been deleted successfully");
		
		} catch (ParticipantRegistrationNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteParticipantRegistration method -ParticipantRegistrationController");

		return "redirect:/admin/manage-participant-registrations";
	}
	
}

