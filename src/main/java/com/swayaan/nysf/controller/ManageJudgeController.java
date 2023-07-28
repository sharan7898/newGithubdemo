package com.swayaan.nysf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

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

import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.Judge;
import com.swayaan.nysf.entity.JudgeRegistration;
import com.swayaan.nysf.entity.NavigationHistory;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.entity.UserAuditLog;
import com.swayaan.nysf.exception.EventManagerNotFoundException;
import com.swayaan.nysf.exception.JudgeNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.JudgeService;
import com.swayaan.nysf.service.NavigationHistoryService;
import com.swayaan.nysf.service.RefereesPanelsService;
import com.swayaan.nysf.service.RoleService;
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
public class ManageJudgeController {

	
	@Autowired
	private JudgeService service;
	@Autowired
	StateService stateService;
	@Autowired
	CommonEmailUtil emailUtil;
	
	@Autowired
	CommonUtil commonUtil;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PasswordEncodeUtil passwordEncodeUtil;
	
	@Autowired
	SettingService settingService;

	@Value("${site.admin-mail}")
	private String siteAdminMail;
	
	@Autowired
	PasswordEncodeUtil passwordUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	NavigationHistoryService navigationHistoryService;

	@Autowired
	UserAuditLogService userAuditLogService;
	
	@Autowired
	UserService userService;

	private static final Integer ROLE_JUDGE = 2;

	
	@Autowired
	RefereesPanelsService refereesPanelsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageJudgeController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-judges")
	public String listFirstPageJudges(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageJudges method -ManageJudgeController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		LOGGER.info("Exit listFirstPageJudges method -ManageJudgeController");

		return listAllJudgesByPage(1, model, "name", "asc", "", "", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-judges/page/{pageNum}")
	public String listAllJudgesByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String name,
			@RequestParam(name = "keyword2", required = false) String jrnNumber,HttpServletRequest request) {
		LOGGER.info("Entered listAllJudgesByPage method -ManageJudgeController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		Page<Judge> page=service.listByPage(pageNum, sortField, sortDir, name, jrnNumber);
		List<Judge> listJudges = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-judges");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", name);
		model.addAttribute("keyword2", jrnNumber);
		Judge judge = new Judge();
		model.addAttribute("listJudges", listJudges);
		model.addAttribute("pageTitle", "Manage Judges");
		model.addAttribute("judge", judge);
		LOGGER.info("Exit listAllJudgesByPage method -ManageJudgeController");

		return "administration/manage_judges";
	}
	
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-judges/new")
	public String newJudge(Model model,HttpServletRequest request) {
		LOGGER.info("Entered newJudge method -ManageJudgeController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		List<Role> listRoles = service.listRoles();
		List<State> listStates = stateService.listAllStates();
		Judge judge = new Judge();
		judge.setEnabled(true);

		model.addAttribute("judge", judge);
		model.addAttribute("pageTitle", "Add Judge");
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("listStates", listStates);
		LOGGER.info("Exit newJudge method -ManageJudgeController");

		return "administration/judge_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-judges/save")
	public String saveJudge(@ModelAttribute("judge") @Valid Judge judge, Model model,
			@RequestParam(value = "degreecertificate") MultipartFile judgeCertificateFile,
			RedirectAttributes re, HttpServletRequest request)
			throws IOException, JudgeNotFoundException, MessagingException, UserNotFoundException {
		LOGGER.info("Entered saveJudge method -ManageJudgeController");

		LOGGER.info("judgeCertificateFile" + judgeCertificateFile);

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		if (!this.checkIfEditFlow(judge, judgeCertificateFile)) {
			String judgeCertificateFileName = null;

			if (!judgeCertificateFile.isEmpty()) {
				judgeCertificateFileName = StringUtils.cleanPath(judgeCertificateFile.getOriginalFilename());
				judge.setCertificate(judgeCertificateFileName);

			} else {
				if (judge.getCertificate().isEmpty()) {
					judge.setCertificate(null);
				}
			}
			judge.setEnabled(true);
			String number = commonUtil.getSystemGeneratedNumber();
			String judgePassword = "Jrn$" + String.valueOf(number);
			String encryptedPassword = passwordEncoder.encode(judgePassword);
			judge.setJrnNumber(number);
			judge.setPassword(encryptedPassword);
			judge.setUserName(number);
			String judgeEmailId = judge.getEmail();
			String judgeFullName = judge.getFullName();
			String judgeUsername = judge.getUserName();
			Setting JUDGE_JRN_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_SUBJECT");
			Setting JUDGE_JRN_CONTENT = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_CONTENT");
			Judge savedJudge = service.save(judge);

			// send email
			//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
			String token = userService.updateResetPasswordToken(judgeUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, judgeFullName, judgeEmailId, judgePassword, number,
					ROLE_JUDGE, JUDGE_JRN_SUBJECT, JUDGE_JRN_CONTENT,passwordLink);

			String uploadDir = "judge-reg-uploads/" + savedJudge.getEmail();
			if (!judgeCertificateFile.isEmpty()) {
				FileUploadUtil.saveFile(uploadDir, judgeCertificateFileName, judgeCertificateFile);
			}

			re.addFlashAttribute("message",
					"The Judge " + judge.getFullName() + " has been saved successfully.");
		} else {
			service.save(judge);
			re.addFlashAttribute("message",
					"The Judge " + judge.getFullName() + " has been updated successfully.");
		}
		LOGGER.info("Exit saveJudge method -ManageJudgeController");

		return "redirect:/admin/manage-judges";
	}


	private boolean checkIfEditFlow(@Valid Judge judge, MultipartFile judgeImage) {
		
		if(judge.getId() !=null) {
			return true;
		}
		return false;
	}

//	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
//	@PostMapping("/manage-judges/save")
//	public String saveJudge(Judge judge, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
//			throws IOException, UnsupportedEncodingException, MessagingException {
//		LOGGER.info("Entered saveJudge ManageJudgeController");
//		//For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
//		boolean isNewJudge = (judge.getId() == null);
//		if (isNewJudge) {
//			//	service.addJudge(judge);
//			// sendVerificationEmail(request,user);
//			// sendCredentialEmail(request, user);
//			Set<Role> role = roleService.getRoleById(2);
//			judge.setRoles(role);
//			String judgePassword="";
//			if (judge.getJrnNumber() == null){
//				String jrnNumber = commonUtil.getSystemGeneratedNumber();
//				judge.setUserName(jrnNumber);
//				judge.setJrnNumber(jrnNumber);
//				judgePassword = "Jrn$"+jrnNumber;
//				String encodedPassword = passwordEncodeUtil.encodePassword(judgePassword);
//				judge.setPassword(encodedPassword);
//				
//			} else {
//				judge.setUserName(String.valueOf(judge.getJrnNumber()));
//			}
//			service.save(judge);
//			Setting JUDGE_JRN_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_SUBJECT");
//			Setting JUDGE_JRN_CONTENT = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_CONTENT");
//			emailUtil.sendEmail(request, judge.getFullName(), judge.getEmail(), judgePassword, judge.getUserName(),
//					judge.getRoleId(),JUDGE_JRN_SUBJECT,JUDGE_JRN_CONTENT);
//			redirectAttributes.addFlashAttribute("message",
//					"The Judge " + judge.getFullName() + " has been saved successfully.");
//		} else {
//			service.save(judge);
//			redirectAttributes.addFlashAttribute("message",
//					"The Judge " + judge.getFullName() + " has been updated successfully.");
//		}
//
//
//		return "redirect:/admin/manage-judges";
//	}

	/*
	 * private void sendVerificationEmail(HttpServletRequest request, User user)
	 * throws UnsupportedEncodingException, MessagingException {
	 * 
	 * LOGGER.info("Entered sendVerificationEmail ManageUserController");
	 * 
	 * EmailSettingBag emailSettings = settingService.getEmailSettings();
	 * JavaMailSenderImpl mailSender = MailUtil.prepareMailSender(emailSettings);
	 * 
	 * String toAddress = user.getEmail(); String subject =
	 * emailSettings.getAccountVerifySubject(); String content =
	 * emailSettings.getAccountVerifyContent();
	 * 
	 * MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
	 * helper = new MimeMessageHelper(message);
	 * 
	 * helper.setFrom(emailSettings.getFromAddress(),
	 * emailSettings.getSenderName()); helper.setTo(toAddress);
	 * helper.setSubject(subject); //replace the mapped name to the user name
	 * content = content.replace("[[name]]",user.getFullName());
	 * 
	 * String verifyURL = MailUtil.getSiteURL(request) + "/verify?code=" +
	 * user.getVerificationCode(); //replace the mapped url to the verification code
	 * content = content.replace("[[URL]]", verifyURL); //setting the plain text to
	 * html helper.setText(content, true); mailSender.send(message);
	 * 
	 * LOGGER.debug("Verification Email - To Address : "+ toAddress);
	 * LOGGER.debug("Verification Email - Verify URL : "+ verifyURL);
	 * 
	 * }
	 */

	/*
	 * @GetMapping("/verify") public String verifyAccount(@Param("code") String
	 * code, Model model,HttpServletRequest request, User user) throws
	 * UnsupportedEncodingException, MessagingException {
	 * LOGGER.info("Entered verifyAccount ManageUserController");
	 * 
	 * boolean verified = service.verify(code); return "settings/" + (verified ?
	 * "verify_fail" : "verify_success"); }
	 */

	/*
	 * private void sendCredentialEmail(HttpServletRequest request, User user)
	 * throws UnsupportedEncodingException, MessagingException {
	 * 
	 * LOGGER.info("Entered sendCredentialEmail ManageUserController");
	 * 
	 * EmailSettingBag emailSettings = settingService.getEmailSettings();
	 * JavaMailSenderImpl mailSender = MailUtil.prepareMailSender(emailSettings);
	 * 
	 * String toAddress2 = user.getEmail(); String subject =
	 * emailSettings.getAccountCredentialSubject(); String content =
	 * emailSettings.getAccountCredentialContent();
	 * 
	 * MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
	 * helper = new MimeMessageHelper(message);
	 * 
	 * helper.setFrom(emailSettings.getFromAddress(),
	 * emailSettings.getSenderName()); helper.setTo(toAddress2);
	 * helper.setSubject(subject); //replace the mapped email to the email content =
	 * content.replace("[[email]]",user.getEmail());
	 * 
	 * //replace the mapped password to the password content =
	 * content.replace("[[password]]", user.getPassword()); //setting the plain text
	 * to html helper.setText(content, true); mailSender.send(message);
	 * 
	 * LOGGER.debug("Verification Email - To Address : "+ toAddress2);
	 * 
	 * 
	 * }
	 */

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-judges/edit/{id}")
	public String editJudges(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes,HttpServletRequest request) {
		LOGGER.info("Entered editJudges method -ManageJudgeController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		try {
			Judge judge = service.get(id);
			List<State> listStates = stateService.listAllStates();
			List<Role> listRoles = service.listRoles();
			model.addAttribute("judge", judge);
			model.addAttribute("pageTitle", "Edit Judge");
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("listStates", listStates);

			return "administration/judge_form";
		} catch (JudgeNotFoundException ex) {
			LOGGER.error("Judge with given id " + id + "not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			LOGGER.info("Exit editJudges method -ManageJudgeController");

			return "redirect:/admin/manage-judges";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-judges/delete/{id}")
	public String deleteJudge(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes,HttpServletRequest request) throws UserNotFoundException {
		LOGGER.info("Entered deleteJudge method -ManageJudgeController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		try {

			Judge judge = service.get(id);
			if (judge.getEmail().equals(siteAdminMail)) {
				redirectAttributes.addFlashAttribute("message1", "The Administrator cannot be deleted!");
				return "redirect:/admin/manage-judges";
			} else {
				
				List<RefereesPanels> listRefereesPanels = refereesPanelsService.getByJudge(judge);			
				if(!listRefereesPanels.isEmpty()) {
					redirectAttributes.addFlashAttribute("message1","The judge "+judge.getFullName()+ 
							" cannot be deleted because this judge is already assigned to panels ");
					return "redirect:/admin/manage-judges";
				}
					
				List<NavigationHistory> listNavigationHistory = navigationHistoryService
						.getNavigationHistoryByUserId(judge);
				if (!listNavigationHistory.isEmpty()) {
					for(NavigationHistory navHistory : listNavigationHistory) {
						navigationHistoryService.delete(navHistory.getId());
					}

				}
				
				List<UserAuditLog> listUserAuditLog = userAuditLogService
						.listUserAuditLogForUser(judge.getId());
				if (!listNavigationHistory.isEmpty()) {
					for(UserAuditLog navHistory : listUserAuditLog) {
						userAuditLogService.delete(navHistory.getId());
					}

				}

				service.delete(id);
				redirectAttributes.addFlashAttribute("message",
						"The judge " + judge.getFullName() + " has been deleted successfully");
			}

		} catch (JudgeNotFoundException ex) {
			LOGGER.error("Judge with given id " + id + "not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit deleteJudge method -ManageJudgeController");

		return "redirect:/admin/manage-judges";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-judges/{id}/enabled/{status}")
	public String updateJudgeEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes,HttpServletRequest request) throws JudgeNotFoundException {
		LOGGER.info("Entered updateJudgeEnabledStatus method -ManageJudgeController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		Judge judge = service.get(id);
		if (judge.getEmail().equals(siteAdminMail)) {
			service.updateJudgeEnabledStatus(id, true);
			redirectAttributes.addFlashAttribute("message", "The Administrator cannot be disabled!");
		} else {
			service.updateJudgeEnabledStatus(id, enabled);
			String status = enabled ? "enabled" : "disabled";
			String message = "The judge " + judge.getFullName() + " has been " + status;
			redirectAttributes.addFlashAttribute("message", message);
		}
		LOGGER.info("Exit updateJudgeEnabledStatus method -ManageJudgeController");

		return "redirect:/admin/manage-judges";
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/send-email/judge/{id}")
	public String sendEmailToJudge(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes,HttpServletRequest request) throws JudgeNotFoundException, UnsupportedEncodingException, MessagingException {

		LOGGER.info("Entered sendEmailToJudge method -ManageJudgeController");

		try {
			Judge judge = service.findById(id);
			
			String judgePassword = "JRN$"+String.valueOf(judge.getJrnNumber());
			String jrnNumberString = String.valueOf(judge.getJrnNumber());
			String judgeEmailId = judge.getEmail();
			String judgeFullName = judge.getFullName();
			Integer judgeRoleId=judge.getRoleId();
			String judgeUsername=judge.getUserName();
			Setting PARTICIPANT_PRN_SUBJECT = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_SUBJECT");
			Setting PARTICIPANT_PRN_Content = settingService.getMailTemplateValueForSubjectAndContent("JUDGE_JRN_CONTENT");
			//String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
			String token = userService.updateResetPasswordToken(judgeUsername);
			String passwordLink = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			emailUtil.sendEmail(request, judgeFullName, judgeEmailId, judgePassword, jrnNumberString,judgeRoleId,
					PARTICIPANT_PRN_SUBJECT,PARTICIPANT_PRN_Content,passwordLink);
			
			redirectAttributes.addFlashAttribute("message",
					"The Email has been sent to the  " + judge.getFullName() + " successfully");
		} catch (JudgeNotFoundException | UserNotFoundException ex) {
			LOGGER.error("judge not found!" + ex.getMessage());
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		LOGGER.info("Exit sendEmailToJudge method -ManageJudgeController");

		return "redirect:/admin/manage-judges";
	}


}
