package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.security.NysfUserDetails;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.service.UserService;

@Controller
public class ProfileController {

	@Autowired
	private UserService service;
	
	@Autowired
	private StateService stateService; 
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

	@GetMapping("/profile")
	public String viewDetails(@AuthenticationPrincipal NysfUserDetails loggedUser, Model model) {
		LOGGER.info("Entered viewDetails method -ProfileController");
		String email = loggedUser.getUsername();
		User user = service.getByEmail(email);
		List<State> listStates=stateService.listAllStates();
		// List<Country> countries = service.listAllCountries();

		System.out.println(user);
		// model.addAttribute("listCountries", countries);
		model.addAttribute("listStates", listStates);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Profile");
		LOGGER.info("Exit viewDetails method -ProfileController");

		return "profile_form";

	}

	@PostMapping("/profile/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal NysfUserDetails loggedUser) throws IOException {
		LOGGER.info("Entered saveDetails method -ProfileController");
		service.updateUserProfile(user);

		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());

		redirectAttributes.addFlashAttribute("message", "Your profile details have been updated.");
		LOGGER.info("Exit saveDetails method -ProfileController");

		return "redirect:/profile";
	}

}
