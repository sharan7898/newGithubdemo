//package com.swayaan.nysf.controller;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//import javax.mail.MessagingException;
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.swayaan.nysf.entity.RefereesPanels;
//import com.swayaan.nysf.entity.Role;
//import com.swayaan.nysf.entity.State;
//import com.swayaan.nysf.entity.User;
//import com.swayaan.nysf.exception.UserNotFoundException;
//import com.swayaan.nysf.service.RefereesPanelsService;
//import com.swayaan.nysf.service.StateService;
//import com.swayaan.nysf.service.UserService;
//
//@Controller
//@RequestMapping("/referee")
//public class ManageRefereeUserController {
//
//	@Autowired
//	private UserService service;
//	@Autowired
//	StateService stateService;
//	@Autowired
//	RefereesPanelsService refereesPanelsService;
//
//	@Value("${site.admin-mail}")
//	private String siteAdminMail;
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ManageRefereeUserController.class);
//
//	@GetMapping("/manage-users")
//	public String listAllUsers(Model model) {
//		LOGGER.info("Entered listAllUsers ManageUserController");
//		User currentUser = getCurrentUser();
//
//		LOGGER.info("currentUser = "+currentUser.getFullName()+ " and " + "email id = "+currentUser.getEmail());
//		List<User> listUsers = service.listAll();
//		User user = new User();
//		model.addAttribute("listUsers", listUsers);
//		model.addAttribute("pageTitle", "Manage Users");
//		model.addAttribute("user", user);
//		return "referee/manage_users";
//	}
//
//	@GetMapping("/manage-users/new")
//	public String newUser(Model model) {
//		LOGGER.info("Entered newUser ManageUserController");
//
//		List<Role> listRoles = service.listRoles();
//		List<State> listStates = stateService.listAllStates();
//		User user = new User();
//		user.setEnabled(true);
//		User currentUser = getCurrentUser();
//		
//		model.addAttribute("user", user);
//		model.addAttribute("pageTitle", "Add User");
//		model.addAttribute("listRoles", listRoles);
//		model.addAttribute("listStates", listStates);
//
//		return "referee/user_form";
//	}
//
//	@PostMapping("/manage-users/save")
//
//	public String saveUser(User user, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
//			throws IOException, UnsupportedEncodingException, MessagingException {
//		LOGGER.info("Entered saveUser ManageUserController");
//		boolean isNewUser = (user.getId() == null);
//		if (isNewUser) {
//			service.addUser(user);
//			// sendVerificationEmail(request,user);
//			// sendCredentialEmail(request, user);
//			service.save(user);
//			redirectAttributes.addFlashAttribute("message",
//					"The User " + user.getFullName() + " has been saved successfully.");
//		} else {
//			service.save(user);
//			redirectAttributes.addFlashAttribute("message",
//					"The User " + user.getFullName() + " has been updated successfully.");
//		}
//
//		return "redirect:/referee/manage-users";
//	}
//
//	/*
//	 * private void sendVerificationEmail(HttpServletRequest request, User user)
//	 * throws UnsupportedEncodingException, MessagingException {
//	 * 
//	 * LOGGER.info("Entered sendVerificationEmail ManageUserController");
//	 * 
//	 * EmailSettingBag emailSettings = settingService.getEmailSettings();
//	 * JavaMailSenderImpl mailSender = MailUtil.prepareMailSender(emailSettings);
//	 * 
//	 * String toAddress = user.getEmail(); String subject =
//	 * emailSettings.getAccountVerifySubject(); String content =
//	 * emailSettings.getAccountVerifyContent();
//	 * 
//	 * MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
//	 * helper = new MimeMessageHelper(message);
//	 * 
//	 * helper.setFrom(emailSettings.getFromAddress(),
//	 * emailSettings.getSenderName()); helper.setTo(toAddress);
//	 * helper.setSubject(subject); //replace the mapped name to the user name
//	 * content = content.replace("[[name]]",user.getFullName());
//	 * 
//	 * String verifyURL = MailUtil.getSiteURL(request) + "/verify?code=" +
//	 * user.getVerificationCode(); //replace the mapped url to the verification code
//	 * content = content.replace("[[URL]]", verifyURL); //setting the plain text to
//	 * html helper.setText(content, true); mailSender.send(message);
//	 * 
//	 * LOGGER.debug("Verification Email - To Address : "+ toAddress);
//	 * LOGGER.debug("Verification Email - Verify URL : "+ verifyURL);
//	 * 
//	 * }
//	 */
//
//	/*
//	 * @GetMapping("/verify") public String verifyAccount(@Param("code") String
//	 * code, Model model,HttpServletRequest request, User user) throws
//	 * UnsupportedEncodingException, MessagingException {
//	 * LOGGER.info("Entered verifyAccount ManageUserController");
//	 * 
//	 * boolean verified = service.verify(code); return "settings/" + (verified ?
//	 * "verify_fail" : "verify_success"); }
//	 */
//
//	/*
//	 * private void sendCredentialEmail(HttpServletRequest request, User user)
//	 * throws UnsupportedEncodingException, MessagingException {
//	 * 
//	 * LOGGER.info("Entered sendCredentialEmail ManageUserController");
//	 * 
//	 * EmailSettingBag emailSettings = settingService.getEmailSettings();
//	 * JavaMailSenderImpl mailSender = MailUtil.prepareMailSender(emailSettings);
//	 * 
//	 * String toAddress2 = user.getEmail(); String subject =
//	 * emailSettings.getAccountCredentialSubject(); String content =
//	 * emailSettings.getAccountCredentialContent();
//	 * 
//	 * MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
//	 * helper = new MimeMessageHelper(message);
//	 * 
//	 * helper.setFrom(emailSettings.getFromAddress(),
//	 * emailSettings.getSenderName()); helper.setTo(toAddress2);
//	 * helper.setSubject(subject); //replace the mapped email to the email content =
//	 * content.replace("[[email]]",user.getEmail());
//	 * 
//	 * //replace the mapped password to the password content =
//	 * content.replace("[[password]]", user.getPassword()); //setting the plain text
//	 * to html helper.setText(content, true); mailSender.send(message);
//	 * 
//	 * LOGGER.debug("Verification Email - To Address : "+ toAddress2);
//	 * 
//	 * 
//	 * }
//	 */
//
//	@GetMapping("/manage-users/edit/{id}")
//	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
//		LOGGER.info("Entered editUser ManageUserController");
//		try {
//			User user = service.get(id);
//			List<State> listStates = stateService.listAllStates();
//			List<Role> listRoles = service.listRoles();
//			User currentUser = getCurrentUser();
//			model.addAttribute("user", user);
//			model.addAttribute("pageTitle", "Edit User");
//			model.addAttribute("listRoles", listRoles);
//			 model.addAttribute("listStates", listStates);
//
//			return "referee/user_form";
//		} catch (UserNotFoundException ex) {
//			LOGGER.error("User with given id " + id + "not found!");
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//			return "redirect:/referee/manage-users";
//		}
//	}
//
//	@GetMapping("/manage-users/delete/{id}")
//	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes redirectAttributes) {
//		LOGGER.info("Entered deleteUser ManageUserController");
//		try {
//
//			User user = service.get(id);
//			if (user.getEmail().equals(siteAdminMail)) {
//				redirectAttributes.addFlashAttribute("message", "The Administrator cannot be deleted!");
//			} else {
//
//				service.delete(id);
//				redirectAttributes.addFlashAttribute("message",
//						"The user " + user.getFullName() + " has been deleted successfully");
//			}
//
//		} catch (UserNotFoundException ex) {
//			LOGGER.error("User with given id " + id + "not found!");
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//		}
//
//		return "redirect:/referee/manage-users";
//	}
//
//	@GetMapping("/manage-users/{id}/enabled/{status}")
//	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
//			RedirectAttributes redirectAttributes) throws UserNotFoundException {
//		LOGGER.info("Entered updateUserEnabledStatus ManageUserController");
//		User user = service.get(id);
//		if (user.getEmail().equals(siteAdminMail)) {
//			service.updateUserEnabledStatus(id, true);
//			redirectAttributes.addFlashAttribute("message", "The Administrator cannot be disabled!");
//		} else {
//			service.updateUserEnabledStatus(id, enabled);
//			String status = enabled ? "enabled" : "disabled";
//			String message = "The User " + user.getFullName() + " has been " + status;
//			redirectAttributes.addFlashAttribute("message", message);
//		}
//
//		return "redirect:/referee/manage-users";
//	}
//	
//	private User getCurrentUser() {
//		String username;
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if (principal instanceof UserDetails) {
//			username = ((UserDetails) principal).getUsername();
//		} else {
//			username = principal.toString();
//		}
//		System.out.println(username);
//		User user = service.getByEmail(username);
//		return user;
//
//	}
//
//}
