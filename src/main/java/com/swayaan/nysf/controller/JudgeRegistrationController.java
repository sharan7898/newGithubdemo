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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.JudgeRegistrationNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.JudgeRegistrationService;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.ParticipantRegistrationService;
import com.swayaan.nysf.service.RecaptchaService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.EmailUtil;
import com.swayaan.nysf.utils.FileUploadUtil;
import com.swayaan.nysf.utils.PasswordEncodeUtil;

@Controller
public class JudgeRegistrationController {

	@Autowired
	JudgeRegistrationService service;
	@Autowired
	StateService stateService;
	@Autowired
	JudgeService judgeService;
	@Autowired
	RecaptchaService captchaService;

	@Autowired
	SettingService settingService;

	@Autowired
	CommonEmailUtil emailUtil;
	
	@Autowired
	CommonUtil commonUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncodeUtil passwordUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudgeRegistrationController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-judge-registrations")
	public String listFirstPageJudgeRegistrations(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageJudgeRegistrations method -JudgeRegistrationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		LOGGER.info("Exit listFirstPageJudgeRegistrations method -JudgeRegistrationController");

		return listAllJudgeRegistrationsByPage(1, model, "name", "asc", "", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-judge-registrations/page/{pageNum}")
	public String listAllJudgeRegistrationsByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name, HttpServletRequest request) {
		LOGGER.info("Entered listAllJudgeRegistrationsByPage method -JudgeRegistrationController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();

		CommonUtil.setNavigationHistory(mappedUrl, currentUser);



		Page<JudgeRegistration> page=service.listByPage(pageNum, sortField, sortDir, name);
		List<JudgeRegistration> listJudgeRegistrations = service.listAllNonApprovedJudges();

		 listJudgeRegistrations = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-judge-registrations");
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
		JudgeRegistration judgeRegistration = new JudgeRegistration();
		model.addAttribute("listJudgeRegistrations", listJudgeRegistrations);
		model.addAttribute("pageTitle", "Manage Judge Registrations");
		model.addAttribute("judgeRegistration", judgeRegistration);
		LOGGER.info("Exit listAllJudgeRegistrationsByPage method -JudgeRegistrationController");

		return "administration/manage_judge_registrations";
	}
/*	public String listAllJudgeRegistrations(Model model) {
		LOGGER.info("Entered listAllJudgeRegistrations JudgeRegistrationController");
		List<JudgeRegistration> listJudgeRegistrations = service.listAllNonApprovedJudges();
		model.addAttribute("pageTitle", "Manage Judge Registrations");
		model.addAttribute("listJudgeRegistrations", listJudgeRegistrations);
		return "administration/manage_judge_registrations";
	}*/

	@GetMapping("/manage-registration/register/judge")
	public String showRegistrationForm(Model model) {
		LOGGER.info("Entered showRegistrationForm method -JudgeRegistrationController");
		JudgeRegistration judgeRegistration = new JudgeRegistration();
		List<State> listStates = stateService.listAllStates();

		model.addAttribute("judgeRegistration", judgeRegistration);
		
		model.addAttribute("listStates", listStates);
		LOGGER.info("Exit showRegistrationForm method -JudgeRegistrationController");

		return "judge_registration_form";
	}

	@PostMapping("/manage-registration/register/judge/save")
	public String saveRegistrationForm(Model model, JudgeRegistration judgeRegistration,
			@RequestParam("degreecertificate") MultipartFile certificate,
			//@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, 
			HttpServletRequest request,
			RedirectAttributes re) throws IOException {
		LOGGER.info("Entered saveRegistrationForm method -JudgeRegistrationController");
		String judgeImageFileName = null;
		String certificateImageFileName = null;
		
		if (!certificate.isEmpty()) {
			certificateImageFileName = StringUtils.cleanPath(certificate.getOriginalFilename());
			judgeRegistration.setCertificate(certificateImageFileName);
		} else {
			if (judgeRegistration.getCertificate().isEmpty()) {
				judgeRegistration.setCertificate(null);
			}
		}

//		if (!judgeImage.isEmpty()) {
//			judgeImageFileName = StringUtils.cleanPath(judgeImage.getOriginalFilename());
//			judgeRegistration.setImage(judgeImageFileName);
//		} else {
//			if (judgeRegistration.getImage().isEmpty()) {
//				judgeRegistration.setImage(null);
//			}
//		}
//
//		if (!captchaService.checkCaptcha(recaptchaResponse, request)) {
//
//			re.addFlashAttribute("message1", "Please click on Recaptcha to verify that you are human");
//
//			return "redirect:/manage-registration/register/judge";
//
//		} else {
			judgeRegistration.setEnabled(true);
			judgeRegistration.setApprovalStatus(false);

			JudgeRegistration savedJudge = service.save(judgeRegistration);
			String uploadDir = "Judge-reg-uploads/" + savedJudge.getEmail();
//			if (!judgeImage.isEmpty()) {
//				FileUploadUtil.saveFile(uploadDir, judgeImageFileName, judgeImage);
//			}
			
			if (!certificate.isEmpty()) {
				FileUploadUtil.saveFile(uploadDir, certificateImageFileName, certificate);
			}

			re.addFlashAttribute("message",
					"You have been registered successfully. You will recieve an e-mail after Approval!");
			LOGGER.info("Exit saveRegistrationForm method -JudgeRegistrationController");

			return "redirect:/login";
//		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/admin/manage-judge-registrations/save")
	public String saveJudgeRegistration(JudgeRegistration judgeRegistration, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered saveJudgeRegistration method -JudgeRegistrationController");
		if (judgeRegistration.isApprovalStatus()) {
			judgeRegistration.setEnabled(true);
			judgeRegistration.setApprovalStatus(true);
			service.save(judgeRegistration);

			try {
				copyRegisteredJudgeToJudge(judgeRegistration, request);
			} catch (UnsupportedEncodingException | MessagingException | JudgeNotFoundException | UserNotFoundException e) {
				LOGGER.error("Unable to save judge" + e.getMessage());
				redirectAttributes.addFlashAttribute("errormessage", "judge Approval Failed. Try Again.");
			}

			redirectAttributes.addFlashAttribute("message", "The Judge has been approved successfully.");
		}
		service.save(judgeRegistration);
		redirectAttributes.addFlashAttribute("message", "The Judge " +judgeRegistration.getFirstName()+ " has been saved successfully.");
		LOGGER.info("Exit saveJudgeRegistration method -JudgeRegistrationController");
        return "redirect:/admin/manage-judge-registrations";
	}

	private void copyRegisteredJudgeToJudge(JudgeRegistration judgeRegistration, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException, JudgeNotFoundException, UserNotFoundException {
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
		String jrnNumber=commonUtil.getSystemGeneratedNumber();
		judge.setJrnNumber(jrnNumber);
		String judgePassword = "Jrn$" + jrnNumber;
		String encodedPassword = passwordUtil.encodePassword(judgePassword);
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
		//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
		String token = userService.updateResetPasswordToken(judgeUserName);
		String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
		emailUtil.sendEmail(request, judgeFullName, judgeEmailId, judgePassword, judgeUserName,
				judgeRole,PARTICIPANT_PRN_SUBJECT,PARTICIPANT_PRN_Content,passwordLink);

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-judge-registrations/{id}/enabled/{status}")
	public String updateJudgeEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) throws JudgeRegistrationNotFoundException {
		LOGGER.info("Entered updateJudgeEnabledStatus method -JudgeRegistrationController");
		service.updateJudgeRegistrationEnabledStatus(id, enabled);
		JudgeRegistration judgeRegistration = service.get(id);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Judge " + judgeRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateJudgeEnabledStatus method -JudgeRegistrationController");
        return "redirect:/admin/manage-judge-registrations";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-judge-registrations/{id}/approved/{status}")
	public String updateJudgeApprovedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean approved,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws JudgeRegistrationNotFoundException, UnsupportedEncodingException, MessagingException {
		LOGGER.info("Entered updateJudgeApprovedStatus method -JudgeRegistrationController");

		if (approved) {
			// soft delete
			try {
				JudgeRegistration judgeRegistration = service.get(id);
				boolean isEmailPresent = userService.ExistByEmail(judgeRegistration.getEmail());
				try {
					if(isEmailPresent) {
						redirectAttributes.addFlashAttribute("errorMessage", "The email id "+judgeRegistration.getEmail() +" is already present");
						return "redirect:/admin/manage-participant-registrations";
					}
					copyRegisteredJudgeToJudge(judgeRegistration, request);
				} catch (UnsupportedEncodingException | MessagingException | JudgeNotFoundException | UserNotFoundException e) {
					LOGGER.error("Unable to save judge" + e.getMessage());
					redirectAttributes.addFlashAttribute("errormessage", "Judge Approval Failed. Try Again.");
				}

			} catch (JudgeRegistrationNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("this is enabled " + id);
			service.updateJudgeRegistrationEnabledStatus(id, true);
			service.updateJudgeRegistrationApprovedStatus(id, approved);
		} else {
			service.updateJudgeRegistrationEnabledStatus(id, true);
		}
		JudgeRegistration judgeRegistration = service.get(id);
		String status = approved ? "approved" : "disapproved";
		String message = "The Judge " + judgeRegistration.getFullName() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateJudgeApprovedStatus method -JudgeRegistrationController");

		return "redirect:/admin/manage-judge-registrations";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-judge-registrations/edit/{id}")
	public String editJudgeRegistration(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) throws JudgeRegistrationNotFoundException {
		LOGGER.info("Entered editJudgeRegistration method -JudgeRegistrationController");
		try {
			JudgeRegistration judgeRegistration = service.get(id);
			List<State> listStates = stateService.listAllStates();
			model.addAttribute("judgeRegistration", judgeRegistration);
			model.addAttribute("listStates", listStates);
			model.addAttribute("pageTitle", "Edit Judge Registered");
			LOGGER.info("Exit editJudgeRegistration method -JudgeRegistrationController");

			return "administration/edit_judge_registration_form";

		} catch (JudgeRegistrationNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.error("JudgeRegistration Not found"+ ex.getMessage());
			return "redirect:/admin/manage-judge-registrations";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/admin/manage-judge-registrations/delete/{id}")
	public String deleteJudgeRegistration(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) throws JudgeRegistrationNotFoundException {
		LOGGER.info("Entered deleteJudgeRegistration method -JudgeRegistrationController");
		try {
			JudgeRegistration judgeRegistration = service.get(id);
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "The judge " + judgeRegistration.getFullName() + " has been deleted successfully");

		} catch (JudgeRegistrationNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteJudgeRegistration method -JudgeRegistrationController");

		return "redirect:/admin/manage-judge-registrations";
	}

}
