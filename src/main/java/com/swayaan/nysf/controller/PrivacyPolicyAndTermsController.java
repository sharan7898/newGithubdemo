package com.swayaan.nysf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivacyPolicyAndTermsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrivacyPolicyAndTermsController.class);
		
	@GetMapping("/privacy-policy")
	public String privacyPolicyPage() {
		LOGGER.info("Entered privacy-policy method -PrivacyPolicyAndTermsController");
		return "privacy_policy_content";
			
	}
	
	@GetMapping("/terms-conditions")
	public String termsAndConditionsPage() {
		LOGGER.info("Entered terms-conditions method -PrivacyPolicyAndTermsController");
		return "terms_and_conditions";
			
	}

}
