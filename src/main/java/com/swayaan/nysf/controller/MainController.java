package com.swayaan.nysf.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.service.EventRegistrationService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
public class MainController {
	
	@Autowired UserService userService;
	@Autowired
	EventRegistrationService eventRegistrationService;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	@GetMapping("")
	public String viewHomePage(HttpServletRequest request) {
		LOGGER.info("Entered viewHomePage method -MainController");

		//For Navigation history
				String mappedUrl = request.getRequestURI();
				User currentUser = CommonUtil.getCurrentUser();
				CommonUtil.setNavigationHistory(mappedUrl,currentUser);
				LOGGER.info("Exit viewHomePage method -MainController");
                 return "pages/index";
	}

	

	
	@GetMapping("/login")
	public String viewLoginPage() {
		LOGGER.info("Entered viewLoginPage method -MainController");

		return "login";
	}
	
	@GetMapping("/password")
	public String viewPasswordPage() {
		LOGGER.info("Entered viewPasswordPage method -MainController");

		return "password";
	}

//	@GetMapping("/verify")
//	public String verifyAccount(@Param("code") String code, Model model,HttpServletRequest request, User user) throws UnsupportedEncodingException, MessagingException {
//		LOGGER.info("Entered verifyAccount MainController");
//		
//		boolean verified = userService.verify(code);
//		return "settings/" + (verified ? "verify_fail" : "verify_success");
//	}
	
}
