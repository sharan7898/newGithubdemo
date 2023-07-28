package com.swayaan.nysf.restcontroller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonEmailUtil;
import com.swayaan.nysf.utils.EmailUtil;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ManageParticipantRestController {
	@Autowired
	private ParticipantService service;

	@Autowired
	CommonEmailUtil emailUtil;

	@Autowired
	SettingService settingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageParticipantRestController.class);

	@PostMapping("/import-participants/send-email")
	public ResponseEntity<String> processSendEmail(@RequestBody String[] listUsername, HttpServletRequest request,
			Model model, RedirectAttributes re)
			throws ParseException, UnsupportedEncodingException, MessagingException, UserNotFoundException {
		System.out.println("request"+request.getContextPath());
		LOGGER.info("Entered processSendEmail -ManageParticipantRestController");
		Setting PARTICIPANT_PRN_SUBJECT = settingService
				.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_SUBJECT");
		Setting PARTICIPANT_PRN_Content = settingService
				.getMailTemplateValueForSubjectAndContent("PARTICIPANT_PRN_CONTENT");
		String siteUrlRequest = EmailUtil.getSiteURL(request);
		emailUtil.sendMultipleEmail(siteUrlRequest,listUsername, PARTICIPANT_PRN_SUBJECT, PARTICIPANT_PRN_Content);
		return new ResponseEntity<>(HttpStatus.OK);
	}


}
