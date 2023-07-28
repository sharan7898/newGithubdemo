package com.swayaan.nysf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipLink;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.EventCategory;
import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.JudgeRegistrationNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.ParticipantRegistrationNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.ChampionshipLinkService;
import com.swayaan.nysf.service.ChampionshipRoundsService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.EventManagerRegistrationService;
import com.swayaan.nysf.service.EventManagerService;
import com.swayaan.nysf.service.EventRegistrationService;
import com.swayaan.nysf.service.JudgeRegistrationService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.ParticipantRegistrationService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

import org.springframework.ui.Model;

@Controller

public class AdminDashboard {
	@Autowired
	EventRegistrationService eventRegistrationService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ChampionshipRoundsService championshipRoundsService;
	@Autowired
	SettingService settingService;
	@Autowired
	CommonEmailUtil commonEmailUtil;

	@Autowired
	EventManagerRegistrationService eventManagerRegistrationService;

	@Autowired
	ParticipantRegistrationService service;
	@Autowired
	ChampionshipLinkService championshipLinkService;
	@Autowired
	PasswordEncodeUtil passwordEncodeUtil;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	UserService userService;
	@Autowired
	CommonUtil commonUtil;

	@Autowired
	EventManagerService eventManagerService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	JudgeRegistrationService judgeRegistrationService;
	@Autowired
	JudgeService judgeService;

	private static final Integer ROLE_EVENT_MANAGER = 4;

	private static final Integer ASANA_CATEGORY_TRADITIONAL_ID = 1;
	private static final Integer ASANA_CATEGORY_ARTISTIC_SINGLE_ID = 2;
	private static final Integer ASANA_CATEGORY_ARTISTIC_PAIR_ID = 3;
	private static final Integer ASANA_CATEGORY_RHYTHMIC_PAIR_ID = 4;
	private static final Integer ASANA_CATEGORY_ARTISTIC_GROUP_ID = 5;

	private static final Integer PARTICIPANT_COUNT_TRADITIONAL_ARTISTIC_SINGLE = 1;
	private static final Integer PARTICIPANT_COUNT_ARTISTIC_RHYTHMIC_PAIR = 2;
	private static final Integer PARTICIPANT_COUNT_ARTISTIC_GROUP = 5;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminDashboard.class);

	@GetMapping("/dashboard")
	public String viewHomePageForAdmin(Model model, HttpServletRequest request) {
		LOGGER.info("Entered viewHomePageForAdmin method  -AdminDashboardController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		List<EventRegistration> listEventRegistrations = eventRegistrationService.getAllNonApprovedEvents();
		List<EventManagerRegistration> listEventManagerRegistrations = eventManagerRegistrationService
				.listAllNonApprovedEventManagers();
		List<ParticipantRegistration> listParticipantRegistrations = service.listAllNonApprovedParticpants();
		List<JudgeRegistration> listJudgeRegistrations = judgeRegistrationService.listAllNonApprovedJudges();

		int event = 0;
		int eventManager = 0;
		int participant = 0;
		int judge = 0;
		// listEventRegistrations.size()+listEventManagerRegistrations.size()+listParticipantRegistrations.size()+listJudgeRegistrations.size();

		if (listEventRegistrations != null) {
			model.addAttribute("numberOfNotifications", listEventRegistrations.size());
			model.addAttribute("numberOfPendingEventRegistration", listEventRegistrations.size());
			if (listEventRegistrations.size() > 5) {
				model.addAttribute("listEventRegistrations", listEventRegistrations.subList(0, 5));
			} else {
				model.addAttribute("listEventRegistrations", listEventRegistrations);
			}
		} else {
			model.addAttribute("numberOfNotifications", listEventRegistrations.size());
			model.addAttribute("numberOfPendingEventRegistration", event);
			model.addAttribute("listEventRegistrations", event);
		}

		if (listEventManagerRegistrations != null) {
			model.addAttribute("numberOfNotifications", listEventManagerRegistrations.size());
			model.addAttribute("numberOfPendingEventManagerRegistration", listEventManagerRegistrations.size());
			if (listEventManagerRegistrations.size() > 5) {
				model.addAttribute("listEventManagerRegistrations", listEventManagerRegistrations.subList(0, 5));
			} else {
				model.addAttribute("listEventManagerRegistrations", listEventManagerRegistrations);
			}
		} else {
			model.addAttribute("numberOfNotifications", listEventManagerRegistrations.size());
			model.addAttribute("numberOfPendingEventManagerRegistration", eventManager);
			model.addAttribute("listEventManagerRegistrations", eventManager);
		}
		if (listParticipantRegistrations != null) {
			model.addAttribute("numberOfNotifications", listParticipantRegistrations.size());
			model.addAttribute("numberOfPendingParticipantsRegistration", listParticipantRegistrations.size());
			if (listParticipantRegistrations.size() > 5) {
				model.addAttribute("listParticipantRegistrations", listParticipantRegistrations.subList(0, 5));
			} else {
				model.addAttribute("listParticipantRegistrations", listParticipantRegistrations);
			}
		} else {
			model.addAttribute("numberOfNotifications", listParticipantRegistrations.size());
			model.addAttribute("numberOfPendingParticipantsRegistration", participant);
			model.addAttribute("listParticipantRegistrations", participant);
		}
		if (listJudgeRegistrations != null) {
			model.addAttribute("numberOfNotifications", listJudgeRegistrations.size());
			model.addAttribute("numberOfPendingJudgesRegistration", listJudgeRegistrations.size());
			if (listJudgeRegistrations.size() > 5) {
				model.addAttribute("listJudgeRegistrations", listJudgeRegistrations.subList(0, 5));
			} else {
				model.addAttribute("listJudgeRegistrations", listJudgeRegistrations);
			}
		} else {
			model.addAttribute("numberOfNotifications", listJudgeRegistrations.size());
			model.addAttribute("numberOfPendingJudgesRegistration", judge);
			model.addAttribute("listJudgeRegistrations", judge);
		}
		// model.addAttribute("count", count);

		// model.addAttribute("listJudgeRegistrations", listJudgeRegistrations);

		// model.addAttribute("listParticipantRegistrations",
		// listParticipantRegistrations);

		// model.addAttribute("listEventManagerRegistrations",
		// listEventManagerRegistrations);

		// model.addAttribute("listEventRegistrations", listEventRegistrations);
		LOGGER.info("Exit viewHomePageForAdmin method  -AdminDashboardController");

		return "administration/dashboard/admin_dashboard";
	}

	@PostMapping("/event-registration/save")
	public String saveEventRegistrationForAdminDashboard(EventRegistration eventRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered saveEventRegistrationForAdminDashboard method  -AdminDashboardController");

		try {
			eventRegistrationService.save(eventRegistration);
		} catch (Exception e) {
			LOGGER.error("unable to save championship changes " + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Unable to save the changes.Please try again later.");
			return "redirect:/dashboard";
		}
		redirectAttributes.addFlashAttribute("message", "The Championship has been saved successfully.");
		LOGGER.info("Exit saveEventRegistrationForAdminDashboard method  -AdminDashboardController");
		return "redirect:/dashboard";
	}

	@GetMapping("/dashboard/{id}/{status}")
	public String updateEventApprovedStatus(@PathVariable("id") Integer id,
			@PathVariable("status") String approvalStatus, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		LOGGER.info("Entered updateEventApprovedStatus method  -AdminDashboardController");
		EventRegistration eventRegistration = null;

		RegistrationStatusEnum statusEnum = RegistrationStatusEnum.valueOf(approvalStatus.toUpperCase());
		System.out.println("statusEnum " + statusEnum);
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
						return "redirect:/dashboard";

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
							LOGGER.info("Exit updateEventApprovedStatus method  -AdminDashboardController");

							return "redirect:/dashboard";
						}

					}
				}
				// eventRegistrationService.updateEventRegistrationApprovedStatus(eventRegistration,
				// statusEnum);
			}
			eventRegistrationService.updateEventRegistrationApprovedStatus(eventRegistration, statusEnum);

		} catch (Exception e) {
			LOGGER.error("Unable to reject Championship" + e.getMessage());
			redirectAttributes.addFlashAttribute("message1", "Unable to reject the championship");
			return "redirect:/dashboard";
		}

		String message2 = "The Championship " + eventRegistration.getName() + " has been " + approvalStatus;
		redirectAttributes.addFlashAttribute("message2", message2);
		return "redirect:/dashboard";
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
			LOGGER.info("Unable to update championship");
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

//event manager registration controller	for admin dashboard
	@PostMapping("/event-manager-registration/save")
	public String saveEventManagerRegistration(EventManagerRegistration eventManagerRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException, EventManagerNotFoundException, UserNotFoundException {
		LOGGER.info("Entered saveEventManagerRegistration AdminDashboardController");
		if (eventManagerRegistration.isApprovalStatus()) {
			eventManagerRegistration.setEnabled(true);
			eventManagerRegistration.setApprovalStatus(true);
			eventManagerRegistrationService.save(eventManagerRegistration);

			copyRegisteredEventManngerToEventManager(eventManagerRegistration, request);

			redirectAttributes.addFlashAttribute("message", "The EventManager has been approved successfully.");
		}
		eventManagerRegistrationService.save(eventManagerRegistration);
		redirectAttributes.addFlashAttribute("message", "The Event Manager has been saved successfully.");
		LOGGER.info("Exit saveEventManagerRegistration AdminDashboardController");

		return "redirect:/dashboard";
	}

	@GetMapping("/dashboard/{id}/approved/{status}")
	public String updateEventManagerApprovedStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean approved, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws EventManagerNotFoundException {
		LOGGER.info("Entered updateEventManagerApprovedStatus method   -AdminDashboardController");

		if (approved) {
			// soft delete
			try {
				EventManagerRegistration eventManagerRegistration = eventManagerRegistrationService.get(id);
				boolean isEmailPresent = userService.ExistByEmail(eventManagerRegistration.getEmail());
				try {
					if (isEmailPresent) {
						redirectAttributes.addFlashAttribute("message1",
								"The email id " + eventManagerRegistration.getEmail() + " is already present");
						return "redirect:/dashboard";
					}
					copyRegisteredEventManngerToEventManager(eventManagerRegistration, request);
				} catch (Exception e) {
					LOGGER.error("Unable to save Event manager" + e.getMessage());
					redirectAttributes.addFlashAttribute("errormessage", "Event Manager Approval Failed. Try Again.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			eventManagerRegistrationService.updateEventManagerRegistrationEnabledStatus(id, true);
			eventManagerRegistrationService.updateEventManagerRegistrationApprovedStatus(id, approved);
		} else {
			eventManagerRegistrationService.updateEventManagerRegistrationEnabledStatus(id, true);
		}
		EventManagerRegistration eventManagerRegistration = eventManagerRegistrationService.get(id);
		String status = approved ? "approved" : "disapproved";
		String message = "The Event Manager " + eventManagerRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateEventManagerApprovedStatus method  - AdminDashboardController");

		return "redirect:/dashboard";
	}

	private void copyRegisteredEventManngerToEventManager(EventManagerRegistration eventManagerRegistration,
			HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException, EventManagerNotFoundException, UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("copyRegisteredEventManngerToEventManager");
		EventManager eventManager = new EventManager();
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
		String ernNumber = commonUtil.getSystemGeneratedNumber();
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

		Setting EVENTMANAGER_ERN_SUBJECT = settingService
				.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_SUBJECT");
		Setting EVENTMANAGER_ERN_CONTENT = settingService
				.getMailTemplateValueForSubjectAndContent("EVENTMANAGER_ERN_CONTENT");
		//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		String token = userService.updateResetPasswordToken(eventManagerUserName);
		String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
		commonEmailUtil.sendEmail(request, eventManagerFullName, eventManagerEmailId, eventManagerPassword,
				eventManagerUserName, ROLE_EVENT_MANAGER, EVENTMANAGER_ERN_SUBJECT, EVENTMANAGER_ERN_CONTENT,passwordLink);

	}
//Manage participant registration

	@PostMapping("/participant-registration/save")
	public String saveParticipantRegistration(ParticipantRegistration participantRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered saveParticipantRegistration method   -AdminDashboardController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		if (participantRegistration.isApprovalStatus()) {
			participantRegistration.setEnabled(false);
			participantRegistration.setApprovalStatus(true);
			service.save(participantRegistration);

			try {
				copyRegisteredParticipantToParticipant(participantRegistration, request);
			} catch (UnsupportedEncodingException | MessagingException | ParticipantNotFoundException e) {
				LOGGER.error("Unable to save participant" + e.getMessage());
				redirectAttributes.addFlashAttribute("errormessage1", "Participant Approval Failed. Try Again.");
			}

			redirectAttributes.addFlashAttribute("message3", "The Participant has been approved successfully.");
		}
		service.save(participantRegistration);
		redirectAttributes.addFlashAttribute("message3", "The Participant has been saved successfully.");
		LOGGER.info("Exit saveParticipantRegistration method   -AdminDashboardController");

		return "redirect:/dashboard";
	}

	private void copyRegisteredParticipantToParticipant(ParticipantRegistration participantRegistration,
			HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException, ParticipantNotFoundException {
		LOGGER.info("Entered copyRegisteredParticipantToParticipant ParticipantRegistrationController");
		// copy the details of the participantRegistration table to the table
		Participant participant = new Participant();
		participant.setFirstName(participantRegistration.getFirstName());
		participant.setLastName(participantRegistration.getLastName());
		participant.setEmail(participantRegistration.getEmail());
		participant.setPhoneNumber(participantRegistration.getPhoneNumber());
		// participant.setCategory(participantRegistration.getCategory());
		participant.setDob(participantRegistration.getDob());
		participant.setAadharNumber(participantRegistration.getAadharNumber());
		participant.setGender(participantRegistration.getGender());
		// participant.setAsanaCategory(participantRegistration.getAsanaCategory());
		participant.setAddressLine1(participantRegistration.getAddressLine1());
		participant.setAddressLine2(participantRegistration.getAddressLine2());
		participant.setCertificateNumber(participantRegistration.getCertificateNumber());
		participant.setTown(participantRegistration.getTown());
		participant.setDistrict(participantRegistration.getDistrict());
		participant.setState(participantRegistration.getState());
		participant.setPhysicallyFit(true);
		participant.setPaymentTransactionId(participantRegistration.getPaymentTransactionId());
		participant.setPostalCode(participantRegistration.getPostalCode());

		participant.setImage(participantRegistration.getImage());
		participant.setBirthCertificate(participantRegistration.getBirthCertificate());
		participant.setMedicalCertificate(participantRegistration.getMedicalCertificate());
		participant.setPaymentReceipt(participantRegistration.getPaymentReceipt());
		participant.setUserPrnNumber(String.valueOf(participantRegistration.getUserPrnNumber()));
		participant.setAcceptRules(true);
		String number = commonUtil.getSystemGeneratedNumber();
		String participantPassword = "Prn$" + number;
		String encryptedPassword = passwordEncoder.encode(participantPassword);
		participant.setPrnNumber(number);
		participant.setUserName(number);
		participant.setPassword(encryptedPassword);
		participant.setEnabled(true); // make it false after email verification flow is added.
		participantService.save(participant);
		String participantEmailId = participant.getEmail();
		String participantFullName = participant.getFullName();
		Integer participantRoleId = participant.getRoleId();
		Setting PARTICIPANT_PRN_SUBJECT = settingService
				.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_SUBJECT");
		Setting PARTICIPANT_PRN_Content = settingService
				.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_CONTENT");
		// SEND VERIFICATION EMAIL FLOW
		String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		commonEmailUtil.sendEmail(request, participantFullName, participantEmailId, participantPassword, number,
				participantRoleId, PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content,nysfPortalLink);

	}

	@GetMapping("/participant-registration-dashboard/{id}/approved/{status}")
	public String updateParticipantApprovedStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean approved, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantRegistrationNotFoundException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered updateParticipantApprovedStatus method  - AdminDashboardController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		if (approved) {
			// soft delete
			try {
				ParticipantRegistration participantRegistration = service.get(id);
				boolean isEmailPresent = userService.ExistByEmail(participantRegistration.getEmail());
				try {
					if (isEmailPresent) {
						redirectAttributes.addFlashAttribute("errorMessage1",
								"The email id " + participantRegistration.getEmail() + " is already present");
						return "redirect:/dashboard";
					}
					copyRegisteredParticipantToParticipant(participantRegistration, request);
				} catch (UnsupportedEncodingException | MessagingException | ParticipantNotFoundException e) {
					LOGGER.error("Unable to save participant" + e.getMessage());
					redirectAttributes.addFlashAttribute("errorMessage1", "Participant Approval Failed. Try Again.");
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
		String message3 = "The Participant " + participantRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message3", message3);
		LOGGER.info("Exit updateParticipantApprovedStatus method  - AdminDashboardController");

		return "redirect:/dashboard";
	}

	// judge registration dashboard
	@PostMapping("/judge-registration-dashboard/save")
	public String saveJudgeRegistration(JudgeRegistration judgeRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered saveJudgeRegistration method AdminDashboardController");
		if (judgeRegistration.isApprovalStatus()) {
			judgeRegistration.setEnabled(true);
			judgeRegistration.setApprovalStatus(true);
			judgeRegistrationService.save(judgeRegistration);

			try {
				copyRegisteredJudgeToJudge(judgeRegistration, request);
			} catch (UnsupportedEncodingException | MessagingException | JudgeNotFoundException e) {
				LOGGER.error("Unable to save judge" + e.getMessage());
				redirectAttributes.addFlashAttribute("errorMessage2", "judge Approval Failed. Try Again.");
			}

			redirectAttributes.addFlashAttribute("message4", "The Judge has been approved successfully.");
		}
		judgeRegistrationService.save(judgeRegistration);
		redirectAttributes.addFlashAttribute("message4", "The Judge has been saved successfully.");
		LOGGER.info("Exit saveJudgeRegistration method AdminDashboardController");
		return "redirect:/dashboard";
	}

	private void copyRegisteredJudgeToJudge(JudgeRegistration judgeRegistration, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException, JudgeNotFoundException {
		// copy the details of the jRegistration table to the table
		Judge judge = new Judge();
		judge.setFirstName(judgeRegistration.getFirstName());
		judge.setLastName(judgeRegistration.getLastName());
		judge.setEmail(judgeRegistration.getEmail());
		judge.setPhoneNumber(judgeRegistration.getPhoneNumber());
		judge.setGender(judgeRegistration.getGender());
		judge.setAddressLine1(judgeRegistration.getAddressLine1());
		judge.setAddressLine2(judgeRegistration.getAddressLine2());
		judge.setTown(judgeRegistration.getTown());
		judge.setDistrict(judgeRegistration.getDistrict());
		judge.setState(judgeRegistration.getState());
		judge.setPostalCode(judgeRegistration.getPostalCode());
		judge.setImage(judgeRegistration.getImage());
		judge.setDesignation(judgeRegistration.getDesignation());
		judge.setCertificate(judgeRegistration.getCertificate());
		judge.setAcceptRules(true);
		String jrnNumber = commonUtil.getSystemGeneratedNumber();
		judge.setJrnNumber(jrnNumber);
		String judgePassword = "Jrn$" + jrnNumber;
		String encodedPassword = passwordEncodeUtil.encodePassword(judgePassword);
		judge.setEmail(judgeRegistration.getEmail());
		judge.setPassword(encodedPassword);
		judge.setEnabled(true);
		judge.setUserName(jrnNumber);
		judgeService.save(judge);
		String judgeEmailId = judge.getEmail();
		String judgeFullName = judge.getFullName();
		Integer judgeRole = judge.getRoleId();
		String judgeUserName = judge.getUserName();

		Setting PARTICIPANT_PRN_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_SUBJECT");
		Setting PARTICIPANT_PRN_Content = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_CONTENT");
		String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		commonEmailUtil.sendEmail(request, judgeFullName, judgeEmailId, judgePassword, judgeUserName, judgeRole,
				PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content,nysfPortalLink);

	}

	@GetMapping("/judge-registrations-dashboard/{id}/approved/{status}")
	public String updateJudgeApprovedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean approved,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws JudgeRegistrationNotFoundException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered updateJudgeApprovedStatus method AdminDashboardController");

		if (approved) {
			// soft delete
			try {
				JudgeRegistration judgeRegistration = judgeRegistrationService.get(id);
				boolean isEmailPresent = userService.ExistByEmail(judgeRegistration.getEmail());
				try {
					if(isEmailPresent) {
						redirectAttributes.addFlashAttribute("errorMessage2", "The email id "+judgeRegistration.getEmail() +" is already present");
						return "redirect:/dashboard";
					}
					copyRegisteredJudgeToJudge(judgeRegistration, request);
				} catch (UnsupportedEncodingException | MessagingException | JudgeNotFoundException e) {
					LOGGER.error("Unable to save judge" + e.getMessage());
					redirectAttributes.addFlashAttribute("errorMessage2", "Judge Approval Failed. Try Again.");
				}

			} catch (JudgeRegistrationNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("this is enabled " + id);
			judgeRegistrationService.updateJudgeRegistrationEnabledStatus(id, true);
			judgeRegistrationService.updateJudgeRegistrationApprovedStatus(id, approved);
		} else {
			judgeRegistrationService.updateJudgeRegistrationEnabledStatus(id, true);
		}
		JudgeRegistration judgeRegistration = judgeRegistrationService.get(id);
		String status = approved ? "approved" : "disapproved";
		String message4 = "The Judge " + judgeRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message4", message4);
		LOGGER.info("Exit updateJudgeApprovedStatus method AdminDashboardController");

		return "redirect:/dashboard";
	}

}