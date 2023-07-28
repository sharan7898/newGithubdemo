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
import com.swayaan.nysf.entity.Club;
import com.swayaan.nysf.entity.District;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.ParticipantNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ClubService;
import com.swayaan.nysf.service.DistrictService;
import com.swayaan.nysf.service.NavigationHistoryService;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserAuditLogService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
@RequestMapping("/admin")
public class ManageParticipantController {

	@Autowired
	ParticipantService service;
	@Autowired
	StateService stateService;
	@Autowired
	ChampionshipService ChampionshipService;

	@Autowired
	AgeCategoryService ageCategoryService;

	@Autowired
	ClubService clubService;
	@Autowired
	DistrictService districtService;

	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;

	@Autowired
	RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	CommonEmailUtil emailUtil;

	@Autowired
	CommonUtil commonUtil;

	@Autowired
	SettingService settingService;

	@Autowired
	NavigationHistoryService navigationHistoryService;

	@Autowired
	UserAuditLogService userAuditLogService;
	
	@Autowired
	UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageParticipantController.class);
	private static final Integer Integer = null;
	private static final Integer ROLE_PARTICIPANT = 3;

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-participant")
	public String listFirstPageParticipants(Model model, HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageParticipants method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		LOGGER.info("Exit listFirstPageParticipants method -ManageParticipantController");

		return listParticipantsByPage(1, model, "name", "asc", "", "", request);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-participant/page/{pageNum}")
	public String listParticipantsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String prnNumber, HttpServletRequest request) {
		LOGGER.info("Entered listParticipantsByPage method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Page<Participant> page = service.listByPage(pageNum, sortField, sortDir, name, prnNumber);
		List<Participant> listParticipants = page.getContent();

		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-participant");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", prnNumber);
		Participant participant = new Participant();
		model.addAttribute("listParticipants", listParticipants);
		model.addAttribute("pageTitle", "Manage participant");
		model.addAttribute("participant", participant);
		LOGGER.info("Exit listParticipantsByPage method -ManageParticipantController");

		return "administration/manage_participant";
	}

//	@GetMapping("/manage-participant")
//	public String listAllParticipants(Model model,HttpServletRequest request) {
//		LOGGER.info("Entered listAllParticipants ManageParticipantController");
//		//For Navigation history
//				String mappedUrl = request.getRequestURI();
//				User currentUser = CommonUtil.getCurrentUser();
//				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		List<Participant> listParticipants = service.listAll();
//		model.addAttribute("listParticipants", listParticipants);
//		model.addAttribute("pageTitle", "Manage Participants");
//
//		return "administration/manage_participant";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-participant/new")
	public String newParticipant(Model model, HttpServletRequest request) {

		LOGGER.info("Entered newParticipant method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		// List<Championship> listChampionships =
		// ChampionshipService.listAllChampionships();
		List<State> listStates = stateService.listAllStates();
		List<District> listDistrict = districtService.listAllDistrict();
		List<Club> listClubs = clubService.listAllClubs();
		List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
		Participant participant = new Participant();
		model.addAttribute("participant", participant);
//		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("listClubs", listClubs);
		model.addAttribute("listAgeCategory", listAgeCategory);
		model.addAttribute("pageTitle", "Add Participant");
		model.addAttribute("listStates", listStates);
		model.addAttribute("listDistrict", listDistrict);
		LOGGER.info("Exit newParticipant method -ManageParticipantController");
		return "administration/participant_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-participant/save")
	public String saveParticipant(@ModelAttribute("participant") @Valid Participant participant, Model model,
			@RequestParam(value = "participantImage", required = false) MultipartFile participantImage,
//			@RequestParam(value = "birthCertificateFile", required = false) MultipartFile birthCertificateFile,
//			@RequestParam(value = "medicalCertificateFile", required = false) MultipartFile medicalCertificateFile,
//			@RequestParam(value = "paymentReceiptFile", required = false) MultipartFile paymentReceiptFile,
			RedirectAttributes re, HttpServletRequest request)
			throws IOException, ParticipantNotFoundException, MessagingException, UserNotFoundException {

		LOGGER.info("Entered saveParticipant method -ManageParticipantController");
		LOGGER.info("participantImage" + participantImage);
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		if (!this.checkIfEditFlow(participant, participantImage)) {
			String participantImageFileName = null;
			String birthCertificateFileName = null;
			String medicalCertificateFileName = null;
			String paymentReceiptFileName = null;

			if (!participantImage.isEmpty()) {
				participantImageFileName = StringUtils.cleanPath(participantImage.getOriginalFilename());
				participant.setImage(participantImageFileName);

			} else {
				if (participant.getImage().isEmpty()) {
					participant.setImage(null);
				}
			}

//			if (!birthCertificateFile.isEmpty()) {
//				birthCertificateFileName = StringUtils.cleanPath(birthCertificateFile.getOriginalFilename());
//				participant.setBirthCertificate(birthCertificateFileName);
//			} else {
//				if (participant.getBirthCertificate().isEmpty()) {
//					participant.setBirthCertificate(null);
//				}
//			}
//
//			if (!medicalCertificateFile.isEmpty()) {
//				medicalCertificateFileName = StringUtils.cleanPath(medicalCertificateFile.getOriginalFilename());
//				participant.setMedicalCertificate(medicalCertificateFileName);
//			} else {
//				if (participant.getMedicalCertificate().isEmpty()) {
//					participant.setMedicalCertificate(null);
//				}
//			}
//
//			if (!paymentReceiptFile.isEmpty()) {
//				paymentReceiptFileName = StringUtils.cleanPath(paymentReceiptFile.getOriginalFilename());
//				participant.setPaymentReceipt(paymentReceiptFileName);
//			} else {
//				if (participant.getPaymentReceipt().isEmpty()) {
//					participant.setPaymentReceipt(null);
//				}
//			}
			participant.setEnabled(true);
			participant.setApprovalStatus(true);
			String number = commonUtil.getSystemGeneratedNumber();
			String participantPassword = "Prn$" + String.valueOf(number);
			String encryptedPassword = passwordEncoder.encode(participantPassword);
			participant.setPrnNumber(number);
			participant.setPassword(encryptedPassword);
			participant.setUserName(number);
			String participantEmailId = participant.getEmail();
			String participantFullName = participant.getFullName();
			String participantUsername = participant.getUserName();
			Setting PARTICIPANT_PRN_SUBJECT = settingService
					.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_SUBJECT");
			Setting PARTICIPANT_PRN_Content = settingService
					.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_CONTENT");
			Participant savedParticipant = service.save(participant);

			// send email
			//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
			String token = userService.updateResetPasswordToken(participantUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, participantFullName, participantEmailId, participantPassword, number,
					ROLE_PARTICIPANT, PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content,passwordLink);

			String uploadDir = "participant-reg-uploads/" + savedParticipant.getEmail();
			if (!participantImage.isEmpty()) {
				FileUploadUtil.saveFile(uploadDir, participantImageFileName, participantImage);
			}
//			if (!birthCertificateFile.isEmpty()) {
//				FileUploadUtil.saveFile(uploadDir, birthCertificateFileName, birthCertificateFile);
//			}
//			if (!medicalCertificateFile.isEmpty()) {
//				FileUploadUtil.saveFile(uploadDir, medicalCertificateFileName, medicalCertificateFile);
//			}
//			if (!paymentReceiptFile.isEmpty()) {
//				FileUploadUtil.saveFile(uploadDir, paymentReceiptFileName, paymentReceiptFile);
//			}

			re.addFlashAttribute("message",
					" The participant " + participant.getFullName() + " has been added successfully.");

			return "redirect:/admin/manage-participant";
		} else {

			service.save(participant);
			re.addFlashAttribute("message",
					" The participant " + participant.getFullName() + " has been updated successfully.");
			LOGGER.info("Exit saveParticipant method -ManageParticipantController");

			return "redirect:/admin/manage-participant";
		}

	}

	private boolean checkIfEditFlow(Participant participant, MultipartFile participantImage) {
//		if (participantImage == null || birthCertificateFile == null || medicalCertificateFile == null
//				|| paymentReceiptFile == null) {
		if (participant.getId() != null) {
			return true;
		}
		return false;
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-participant/edit/{id}")
	public String editParticipant(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LOGGER.info("Entered editParticipant method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Participant participant = service.getParticipantById(id);
			LOGGER.info(participant.toString());
//			List<Championship> listChampionships = ChampionshipService.listAllChampionships();
			List<State> listStates = stateService.listAllStates();

			List<District> listDistrict = districtService.listAllDistrict();
			List<Club> listClubs = clubService.listAllClubs();
			List<AgeCategory> listAgeCategory = ageCategoryService.listAllAgeCategories();
			model.addAttribute("participant", participant);
			model.addAttribute("listClubs", listClubs);
			model.addAttribute("listAgeCategory", listAgeCategory);
			model.addAttribute("pageTitle", "Edit Participant");
//			model.addAttribute("listChampionships", listChampionships);
			model.addAttribute("listStates", listStates);

			model.addAttribute("listDistrict", listDistrict);
			service.save(participant);
			LOGGER.info("Redirected to add_participant page");
			return "administration/participant_form";
		} catch (ParticipantNotFoundException ex) {
			LOGGER.error("Participant not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editParticipant method -ManageParticipantController");
			return "redirect:/admin/manage-participant";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-participant/delete/{id}")
	public String deleteParticipant(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantNotFoundException, UserNotFoundException {

		LOGGER.info("Entered deleteParticipant method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Participant participant = service.getParticipantById(id);

			List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeamParticipantsService
					.findByParticipant(participant);

			if (!listParticipantTeamParticipants.isEmpty()) {
				redirectAttributes.addFlashAttribute("message1", "The participant " + participant.getFullName()
						+ " cannot be deleted because this participant is already assigned to teams ");
				return "redirect:/admin/manage-participant";
			}

			List<NavigationHistory> listNavigationHistory = navigationHistoryService
					.getNavigationHistoryByUserId(participant);
			if (!listNavigationHistory.isEmpty()) {
				for (NavigationHistory navHistory : listNavigationHistory) {
					navigationHistoryService.delete(navHistory.getId());
				}

			}

			List<UserAuditLog> listUserAuditLog = userAuditLogService.listUserAuditLogForUser(participant.getId());
			if (!listNavigationHistory.isEmpty()) {
				for (UserAuditLog navHistory : listUserAuditLog) {
					userAuditLogService.delete(navHistory.getId());
				}

			}

			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Participant  " + participant.getFullName() + " has been deleted successfully");
		} catch (ParticipantNotFoundException ex) {
			LOGGER.error("Participant not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteParticipant method -ManageParticipantController");

		return "redirect:/admin/manage-participant";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/send-email/participant/{id}")
	public String sendEmailToParticipant(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws ParticipantNotFoundException, UnsupportedEncodingException, MessagingException {

		LOGGER.info("Entered sendEmailToParticipant method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			Participant participant = service.getParticipantById(id);
			String participantPassword = "Prn$" + String.valueOf(participant.getPrnNumber());
			String prnNumberString = String.valueOf(participant.getPrnNumber());
			String participantEmailId = participant.getEmail();
			String participantFullName = participant.getFullName();
			String participantUsername=participant.getUserName();
			Integer participantRoleId = participant.getRoleId();
			Setting PARTICIPANT_PRN_SUBJECT = settingService
					.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_SUBJECT");
			Setting PARTICIPANT_PRN_Content = settingService
					.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_CONTENT");
			//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
			String token = userService.updateResetPasswordToken(participantUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, participantFullName, participantEmailId, participantPassword, prnNumberString,
					participantRoleId, PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content,passwordLink);

			redirectAttributes.addFlashAttribute("message",
					"The Email has been sent to the " + participant.getFullName() + " successfully");
		} catch (ParticipantNotFoundException | UserNotFoundException ex) {
			LOGGER.error("Participant not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit sendEmailToParticipant method -ManageParticipantController");

		return "redirect:/admin/manage-participant";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-participant/{id}/enabled/{status}")
	public String updateParticipantEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws JudgeNotFoundException, ParticipantNotFoundException {
		LOGGER.info("Entered updateParticipantEnabledStatus method -ManageParticipantController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Participant participant = service.getParticipantById(id);

		service.updateParticipantEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The participant " + participant.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		LOGGER.info("Exit updateParticipantEnabledStatus method -ManageParticipantController");

		return "redirect:/admin/manage-participant";
	}

}
