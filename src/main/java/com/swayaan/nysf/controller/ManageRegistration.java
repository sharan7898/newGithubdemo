package com.swayaan.nysf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManageRegistration {
	private static final Logger LOGGER = LoggerFactory.getLogger(ManageRegistration.class);
		
	@GetMapping("/manage-registration")
	public String registrationHomePage() {
		LOGGER.info("Entered registrationHomePage method - ManageRegistration controller");
		return "registration_home_page";
			
	}

}
