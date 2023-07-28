package com.swayaan.nysf.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.EventManager;
import com.swayaan.nysf.entity.EventManagerRegistration;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.service.AgeCategoryService;
import com.swayaan.nysf.service.AsanaCategoryService;
import com.swayaan.nysf.service.EventManagerRegistrationService;
import com.swayaan.nysf.service.EventManagerService;
import com.swayaan.nysf.service.RecaptchaService;
import com.swayaan.nysf.service.StateService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.FileUploadUtil;

@Controller
public class EventManagerRegistrationController {

	@Autowired
	AsanaCategoryService asanaCategoryService;
	@Autowired
	StateService stateService;
	@Autowired
	AgeCategoryService ageCategoryService;
	@Autowired
	EventManagerRegistrationService eventManagerRegistrationService;
	@Autowired
	RecaptchaService captchaService;
	@Autowired CommonEmailUtil commonEmailUtil;
	@Autowired EventManagerService eventManagerService;
	
	private static final Integer ROLE_EVENT_MANAGER = 4;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerRegistrationController.class);

	@GetMapping("/eventmanager-registration/register/eventmanager")
	public String showLoginRedirectPage(Model model) {
		LOGGER.info("Entered showLoginRedirectPage EventManagerRegistrationController");
		return "already_eventmanager";
	}

	@GetMapping("/manage-registration/register/eventmanager")
	public String showRegistrationForm(Model model) {
		LOGGER.info("Entered showRegistrationForm method -EventManagerRegistrationController");
		EventManagerRegistration eventManagerRegistration = new EventManagerRegistration();
		List<EventManagerRegistration> listEventManagerForm = eventManagerRegistrationService.showEventManagerForm();
		List<State> listStates = stateService.listAllStates();

		model.addAttribute("eventManagerRegistration", eventManagerRegistration);
		model.addAttribute("listEventManagerForm", listEventManagerForm);
		model.addAttribute("listStates", listStates);
		LOGGER.info("Exit showRegistrationForm method -EventManagerRegistrationController");

		return "eventmanager_registration_form";
	}

	@PostMapping("/manage-registration/register/eventmanager/save")
	public String saveRegistrationForm(Model model, EventManagerRegistration eventManagerRegistration,
			@RequestParam("eventmanagerImage") MultipartFile eventmanagerImage,
		//	@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, 
			HttpServletRequest request,
			RedirectAttributes re) throws IOException {
		LOGGER.info("Entered saveRegistrationForm method -EventManagerRegistrationController");
		String eventManagerImageFileName = null;

		if (!eventmanagerImage.isEmpty()) {
			eventManagerImageFileName = StringUtils.cleanPath(eventmanagerImage.getOriginalFilename());
			eventManagerRegistration.setImage(eventManagerImageFileName);
		} else {
			if (eventManagerRegistration.getImage().isEmpty()) {
				eventManagerRegistration.setImage(null);
			}
		}

//		if (!captchaService.checkCaptcha(recaptchaResponse, request)) {
//
//			re.addFlashAttribute("message1", "Please click on Recaptcha to verify that you are human");
//
//			return "redirect:/manage-registration/register/eventmanager";
//
//		} else {
			eventManagerRegistration.setEnabled(true);
			eventManagerRegistration.setApprovalStatus(false);

			EventManagerRegistration savedEventManager= eventManagerRegistrationService.save(eventManagerRegistration);
			String uploadDir = "eventmanager-reg-uploads/" + savedEventManager.getEmail();
			if (!eventmanagerImage.isEmpty()) {
				FileUploadUtil.saveFile(uploadDir, eventManagerImageFileName, eventmanagerImage);
			}

			re.addFlashAttribute("message",
					"You have been registered successfully. You will recieve an e-mail after Approval!");
			LOGGER.info("Exit saveRegistrationForm method -EventManagerRegistrationController");

			return "redirect:/login";
//		}
	}



	
}
