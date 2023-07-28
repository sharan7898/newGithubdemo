package com.swayaan.nysf.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.swayaan.nysf.entity.EmailSettingBag;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.EmailUtil;

@Controller
public class ForgotPasswordController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordController.class);

	@Autowired
	UserService userService;

	@Autowired
	SettingService settingService;

	@Autowired
	CommonEmailUtil commonEmailUtil;

	@GetMapping("/password/reset")
	public String showrequestform(Model model) {
		LOGGER.info("Entered showrequestform method -ForgotPasswordController");

		return "password";
	}

	@PostMapping("/password/reset")
	public String processrequestform(HttpServletRequest request, Model model) {
		LOGGER.info("Entered processrequestform method -ForgotPasswordController");

		String username = request.getParameter("username");
		try {
			String token = userService.updateResetPasswordToken(username);
			String link = EmailUtil.getSiteURL(request) + "/reset_password?token=" + token;
			// System.out.println("Email :" + email);
			// System.out.println("Token :" + token);

			User user = userService.getByUserName(username);
			commonEmailUtil.SendEmailForgotPassword(link, user.getEmail());

			model.addAttribute("message1", "We have sent a reset password link to your registered email "
					+ user.getEmail() + " " + "please check!!");
		} catch (UserNotFoundException ex) {

			model.addAttribute("message", ex.getMessage());

		} catch (UnsupportedEncodingException | MessagingException ex) {

			model.addAttribute("message", "Could not send email");
		}

		LOGGER.info("Exit processrequestform method -ForgotPasswordController");

		return "password";
	}

	@GetMapping("/reset_password")
	public String showResetForm(@Param("token") String token, Model model) {
		LOGGER.info("Entered showResetForm method -ForgotPasswordController");

		User user = userService.getByResetPasswordToken(token);
		if (user != null) {
			model.addAttribute("token", token);
		} else {

			model.addAttribute("errorMessage", "Token Expired");
			return "message";
		}
		LOGGER.info("Exit showResetForm method -ForgotPasswordController");

		return "reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		LOGGER.info("Entered processResetForm method -ForgotPasswordController");

		String token = request.getParameter("token");
		String password = request.getParameter("password");

		try {
			userService.updatePassword(token, password);
			model.addAttribute("title", "Password Changed");
			model.addAttribute("message", "Your password has been changed successfully.");
			LOGGER.info("Exit processResetForm method -ForgotPasswordController");

			return "message";
		} catch (UserNotFoundException ex) {

			model.addAttribute("errorMessage", ex.getMessage());
			return "message";
		}

	}
}
