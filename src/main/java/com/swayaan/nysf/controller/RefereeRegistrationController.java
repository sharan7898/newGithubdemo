//package com.swayaan.nysf.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.swayaan.nysf.entity.RefereeRegistration;
//import com.swayaan.nysf.entity.Role;
//import com.swayaan.nysf.entity.User;
//import com.swayaan.nysf.exception.RefereeRegistrationNotFoundException;
//import com.swayaan.nysf.service.RecaptchaService;
//import com.swayaan.nysf.service.RefereeRegistrationService;
//import com.swayaan.nysf.service.RoleService;
//import com.swayaan.nysf.service.UserService;
//import com.swayaan.nysf.utils.CommonUtil;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javax.mail.MessagingException;
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import org.springframework.validation.BindingResult;
//
//@Controller
//public class RefereeRegistrationController {
//
//	@Autowired
//	RefereeRegistrationService service;
//	@Autowired
//	RoleService roleService;
//	@Autowired
//	UserService userService;
//	@Autowired
//	RecaptchaService captchaService;
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(RefereeRegistrationController.class);
//
//	@GetMapping("/referee-register")
//	public String refereeRegistration(Model model) {
//
//		LOGGER.info("Entered refereeRegistration RefereeRegistrationController");
//		RefereeRegistration refereeRegistration = new RefereeRegistration();
//		model.addAttribute("pageTitle", "Judge Registration");
//		model.addAttribute("refereeRegistration", refereeRegistration);
//		return "referee_register_form";
//	}
//
//	@PostMapping("/referee-register/save")
//	public String saveReferee(@Valid RefereeRegistration refereeRegistration, BindingResult result, Model model,
//			RedirectAttributes redirectAttributes,
//			@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request) {
//
//		LOGGER.info("Entered saveReferee RefereeRegistrationController");
//		if (result.hasErrors() || !service.checkCaptcha(recaptchaResponse, request)) {
//			
//			redirectAttributes.addFlashAttribute("message1",
//					"Please click on recaptcha to verify that you are human");
//			return "redirect:/referee-register";
//		} else {
//			refereeRegistration.setEnabled(true);
//			refereeRegistration.setApprovalStatus(false);
//			service.addRefereeRegistration(refereeRegistration);
//			service.save(refereeRegistration);
//
//			redirectAttributes.addFlashAttribute("message",
//					"Your Details have been saved successfully. We will get back to you soon!");
//			return "redirect:/referee-register";
//		}
//	}
//
//	@GetMapping("/admin/manage-referee-registrations")
//	public String listAllRefereeRegistrations(Model model) {
//		LOGGER.info("Entered listAllRefereeRegistrations RefereeRegistrationController");
//		List<RefereeRegistration> listRefereeRegistrations = service.listAllEnabledReferees();
//		model.addAttribute("pageTitle", "Manage Judge Registrations");
//		model.addAttribute("listRefereeRegistrations", listRefereeRegistrations);
//		return "administration/manage_referee_registrations";
//	}
//
//	@PostMapping("/admin/manage-referee-registrations/save")
//	public String saveRefereeRegistration(RefereeRegistration refereeRegistration, Model model,
//			RedirectAttributes redirectAttributes, HttpServletRequest request)
//			throws IOException, UnsupportedEncodingException, MessagingException {
//		LOGGER.info("Entered saveRefereeRegistration RefereeRegistrationController");
//		if (refereeRegistration.isApprovalStatus()) {
//			refereeRegistration.setEnabled(false);
//			refereeRegistration.setApprovalStatus(true);
//			service.save(refereeRegistration);
//
//			// get current user
//			User currentUser = CommonUtil.getCurrentUser();
//
//			Set<Role> role = new HashSet<Role>();
//			Role refereeRole = roleService.getRoleByName("Referee");
//			role.add(refereeRole);
//
//			// copy the details of the referee to the user table
//			User user = new User();
//			user.setFirstName(refereeRegistration.getFirstName());
//			user.setLastName(refereeRegistration.getLastName());
//			user.setEmail(refereeRegistration.getEmail());
//			user.setPassword(refereeRegistration.getPassword());
//			user.setPhoneNumber(refereeRegistration.getPhoneNumber());
//			user.setRoles(role);
//			user.setRefereeType(refereeRegistration.getRefereeType());
//			user.setCreatedTime(new Date());
//			user.setCreatedBy(currentUser);
//			user.setEnabled(true); // make it false after email verification flow is added.
//			userService.save(user);
//
//			// SEND VERIFICATION EMAIL FLOW TO BE ADDED HERE
//
//			redirectAttributes.addFlashAttribute("message", "The Jugde has been approved successfully.");
//		}
//		service.save(refereeRegistration);
//		redirectAttributes.addFlashAttribute("message", "The Judge has been saved successfully.");
//		return "redirect:/admin/manage-referee-registrations";
//	}
//
//	@GetMapping("/admin/manage-referee-registrations/{id}/enabled/{status}")
//	public String updateRefereeEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
//			RedirectAttributes redirectAttributes) throws RefereeRegistrationNotFoundException {
//		LOGGER.info("Entered updateRefereeEnabledStatus RefereeRegistrationController");
//		service.updateRefereeRegistrationEnabledStatus(id, enabled);
//		String status = enabled ? "enabled" : "disabled";
//		String message = "The Judge ID " + id + " has been " + status;
//		redirectAttributes.addFlashAttribute("message", message);
//		return "redirect:/admin/manage-referee-registrations";
//	}
//
//	@GetMapping("/admin/manage-referee-registrations/{id}/approved/{status}")
//	public String updateRefereeApprovedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean approved,
//			RedirectAttributes redirectAttributes) throws RefereeRegistrationNotFoundException {
//		LOGGER.info("Entered updateRefereeApprovedStatus RefereeRegistrationController");
//		service.updateRefereeRegistrationApprovedStatus(id, approved);
//		if(approved) {
//			//soft delete
//			service.updateRefereeRegistrationEnabledStatus(id, false);
//		} else {
//			service.updateRefereeRegistrationEnabledStatus(id, true);
//		}
//		String status = approved ? "approved" : "disapproved";
//		String message = "The Judge ID " + id + " has been " + status;
//		redirectAttributes.addFlashAttribute("message", message);
//		return "redirect:/admin/manage-referee-registrations";
//	}
//
//	@GetMapping("/admin/manage-referee-registrations/edit/{id}")
//	public String editRefereeRegistration(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes redirectAttributes) throws RefereeRegistrationNotFoundException {
//		LOGGER.info("Entered editRefereeRegistration RefereeRegistrationController");
//		try {
//			RefereeRegistration refereeRegistration = service.get(id);
//			model.addAttribute("refereeRegistration", refereeRegistration);
//			model.addAttribute("pageTitle", "Edit Judge Registered");
//			return "administration/edit_referee_registration_form";
//
//		} catch (RefereeRegistrationNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//			return "redirect:/admin/manage-referee-registrations";
//		}
//	}
//
//	@GetMapping("/admin/manage-referee-registrations/delete/{id}")
//	public String deleteRefereeRegistration(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes redirectAttributes) throws RefereeRegistrationNotFoundException {
//		LOGGER.info("Entered deleteRefereeRegistration RefereeRegistrationController");
//		try {
//			RefereeRegistration refereeRegistration = service.get(id);
//			service.delete(id);
//			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
//
//		} catch (RefereeRegistrationNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//		}
//		return "redirect:/admin/manage-referee-registrations";
//	}
//
//}
