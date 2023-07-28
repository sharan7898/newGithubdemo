package com.swayaan.nysf.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.EmailSettingBag;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.Setting;
import com.swayaan.nysf.exception.RoleNotFoundException;
import com.swayaan.nysf.exception.UserNotFoundException;
import com.swayaan.nysf.service.ParticipantService;
import com.swayaan.nysf.service.ParticipantTeamParticipantsService;
import com.swayaan.nysf.service.RoleService;
import com.swayaan.nysf.service.SettingService;
import com.swayaan.nysf.service.UserService;

@Service
public class CommonEmailUtil {

	@Autowired
	SettingService settingService;

	@Autowired
	RoleService roleService;

	@Autowired
	private ParticipantService participantService;
	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;
	
	@Autowired
	UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonEmailUtil.class);

	@Async
	public void sendEmail(HttpServletRequest request, String fullName, String emailId, String password,
			String prnNumber, Integer roleId, Setting subjectString, Setting contentString, String passwordLink)
			throws MessagingException, UnsupportedEncodingException {
		Role role = null;
		try {
			role = roleService.get(roleId);
		} catch (RoleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
		String toAddress = emailId;
		String subject = subjectString.getValue();
		String content = "";
		if (role.getName().equals("Judge")) {
			content = contentString.getValue();
		} else {
			content = contentString.getValue();

		}
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[participantName]]", fullName);
		System.out.println("email - ID :" + emailId);
		content = content.replace("[[emailid]]", emailId);
		content = content.replace("[[prnNumber]]", prnNumber);
//		content = content.replace("[[password]]", password);
//		content = content.replace("[[nysfPortalLink]]", nysfPortalLink);
		content = content.replace("[[passwordLink]]", passwordLink);
		// setting the plain text to html
		helper.setText(content, true);
		mailSender.send(message);
	}
	
//	@Async
//	public void sendEmail(HttpServletRequest request, String fullName, String emailId, String password,
//			String prnNumber, Integer roleId, Setting subjectString, Setting contentString, String nysfPortalLink,
//			String token, String passwordLink)
//			throws MessagingException, UnsupportedEncodingException {
//		Role role = null;
//		try {
//			role = roleService.get(roleId);
//		} catch (RoleNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		EmailSettingBag emailSettings = settingService.getEmailSettings();
//		JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
//		String toAddress = emailId;
//		String subject = subjectString.getValue();
//		String content = "";
//		if (role.getName().equals("Judge")) {
//			content = contentString.getValue();
//		} else {
//			content = contentString.getValue();
//
//		}
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//
//		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
//		helper.setTo(toAddress);
//		helper.setSubject(subject);
//
//		content = content.replace("[[participantName]]", fullName);
//		System.out.println("email - ID :" + emailId);
//		content = content.replace("[[emailid]]", emailId);
//		content = content.replace("[[prnNumber]]", prnNumber);
////		content = content.replace("[[password]]", password);
//		content = content.replace("[[nysfPortalLink]]", nysfPortalLink);
//		// setting the plain text to html
//		helper.setText(content, true);
//		mailSender.send(message);
//	}

	// Send emails to list
	@Async
	public void sendMultipleEmail(String siteRequestUrl, String[] listUserName, Setting subjectString,
			Setting contentString) throws MessagingException, UnsupportedEncodingException, UserNotFoundException {

		for (String userName : listUserName) {
			Participant participant = participantService.getParticipantByUserName(userName);
			if (participant != null) {
				String prnNumber = participant.getUserName();
				String email = participant.getEmail();
				String fullName = participant.getFullName();
				Integer roleId = participant.getRoleId();
				String token = userService.updateResetPasswordToken(prnNumber);
				String passwordLink = siteRequestUrl+ "/reset_password?token=" + token;

				Role role = null;
				try {
					role = roleService.get(roleId);
				} catch (RoleNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				EmailSettingBag emailSettings = settingService.getEmailSettings();
				JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
				String toAddress = email;
				String subject = subjectString.getValue();
				String content = "";
				if (role.getName().equals("Judge")) {
					content = contentString.getValue();
				} else {
					content = contentString.getValue();

				}
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);

				helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
				helper.setTo(toAddress);
				helper.setSubject(subject);

				content = content.replace("[[participantName]]", fullName);
				LOGGER.info("email - ID :" + email);
				content = content.replace("[[emailid]]", email);
				content = content.replace("[[prnNumber]]", prnNumber);
				content = content.replace("[[passwordLink]]", passwordLink);
				// setting the plain text to html
				helper.setText(content, true);
				mailSender.send(message);
			}
		}
	}

	private String getEmailContentForJudge(String nysfPortalLink) {
		String content = "<p>Hello [[participantName]],</p>"
				+ "<p>This is to inform that you have been approved for the nysf event.</p>"
				+ "<p> The following are the details for the event<b> :</b> </p>"
				+ "<p><label>Judge Name   &nbsp;&nbsp; : </label><b> [[participantName]]<b></p>" + "<br>"
				+ "<p><label>Email Address &nbsp;&nbsp;&nbsp; : </label><b> [[emailid]]<b></p>"
				+ "<p><label>Password  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  : </label><b> [[password]]<b></p>"
				+ "<p>Please use the above Email Id and password to login to the nysf portal. The link is given below:</p>"
				+ nysfPortalLink + "<br>" + "<p><label>Regards, </label></p>"
				+ "<p><label><b>NYSF Team </b></label></p>";

		return content;

	}

	private String getEmailContentForParticipant(String nysfPortalLink) {

		String content = "<p>Hello [[participantName]],</p>"
				+ "<p>This is to inform that you have been approved for the nysf event.</p>"
				+ "<p> The following are the details for the event<b> :</b> </p>" + "<br>"
				+ "<p><label>Email Address &nbsp;&nbsp;&nbsp; : </label><b> [[emailid]]<b></p>"
				+ "<p><label>Participant Name   &nbsp;&nbsp; : </label><b> [[participantName]]<b></p>"
				+ "<p><label>PRN Number  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  : </label><b> [[prnNumber]]<b></p>"
				+ "<p><label>Password  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  : </label><b> [[password]]<b></p>"
				+ "<p>Please use the above PRN number and password to login to the nysf portal. The link is given below:</p>"
				+ nysfPortalLink + "<br>" + "<p><label>Regards, </label></p>"
				+ "<p><label><b>NYSF Team </b></label></p>";
		return content;
	}

	@Async
	public void sendEmailForSuccessfulEventRegistration(HttpServletRequest request, Championship savedChampionship,
			Setting CHAMPIONSHIP_SUBJECT, Setting CHAMPIONSHIP_CONTENT, String nysfPortalLink)
			throws MessagingException, UnsupportedEncodingException {

		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
//		String nysfPortalLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//				+ request.getContextPath();
		String toAddress = savedChampionship.getCreatedBy().getEmail();
		String subject = CHAMPIONSHIP_SUBJECT.getValue();
		String content = CHAMPIONSHIP_CONTENT.getValue();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[championshipName]]", savedChampionship.getName());
		content = content.replace("[[eventmanagerName]]", savedChampionship.getCreatedBy().getFullName());
		content = content.replace("[[nysfPortalLink]]", nysfPortalLink);
		// setting the plain text to html
		helper.setText(content, true);
		mailSender.send(message);

	}

	@Async
	public void mailParticipantForSuccessfulTeamRegistration(String nysfPortalLink,
			ParticipantTeam participantTeam) throws MessagingException, UnsupportedEncodingException {

		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
		String toAddress = participantTeam.getCreatedBy().getEmail();
		Setting TEAM_REGISTRATION_SUBJECT = settingService
				.getMailTemplateValueForSubjectAndContent("TEAM_REGISTRATION_SUBJECT");
		Setting TEAM_REGISTRATION_CONTENT = settingService
				.getMailTemplateValueForSubjectAndContent("TEAM_REGISTRATION_CONTENT");

		String subject = TEAM_REGISTRATION_SUBJECT.getValue();
		String content = TEAM_REGISTRATION_CONTENT.getValue();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		subject = subject.replace("[[championshipName]]", participantTeam.getChampionshipName());
		content = content.replace("[[participantName]]", participantTeam.getCreatedBy().getFullName());
		content = content.replace("[[championshipName]]", participantTeam.getChampionshipName());
		content = content.replace("[[championshipCategory]]", participantTeam.getAsanaCategoryName() + " - "
				+ participantTeam.getCategory().getTitle() + " - " + participantTeam.getGender().toString());
		content = content.replace("[[teamId]]", participantTeam.getAutogenChestNumber());
		String teamMembers = "";
		List<ParticipantTeamParticipants> participantTeamParticipants = participantTeamParticipantsService
				.getByTeamOrderBySequenceNumberAsc(participantTeam);
		for (ParticipantTeamParticipants ptp : participantTeamParticipants) {
			teamMembers = teamMembers.concat(ptp.getParticipant().getPrnNumber() + "  ");
		}
		content = content.replace("[[teamMembers]]", teamMembers);
		content = content.replace("[[nysfPortalLink]]", nysfPortalLink);
		// setting the plain text to html
		helper.setText(content, true);
		helper.setSubject(subject);
		mailSender.send(message);

	}

	@Async
	public void mailParticipantForRegistrationAprovalOrRejection(String nysfPortalLink,
			ParticipantTeam participantTeam) throws MessagingException, UnsupportedEncodingException {

		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
		String toAddress = participantTeam.getCreatedBy().getEmail();
		Setting TEAM_REGISTRATION_STATUS_SUBJECT = settingService
				.getMailTemplateValueForSubjectAndContent("TEAM_REGISTRATION_STATUS_SUBJECT");
		Setting TEAM_REGISTRATION_STATUS_CONTENT = settingService
				.getMailTemplateValueForSubjectAndContent("TEAM_REGISTRATION_STATUS_CONTENT");

		String subject = TEAM_REGISTRATION_STATUS_SUBJECT.getValue();
		String content = TEAM_REGISTRATION_STATUS_CONTENT.getValue();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
	
		subject = subject.replace("[[championshipName]]", participantTeam.getChampionshipName());
		subject = subject.replace("[[teamId]]", participantTeam.getAutogenChestNumber());
		subject = subject.replace("[[approvalStatus]]", participantTeam.getStatus().toString());

		content = content.replace("[[participantName]]", participantTeam.getCreatedBy().getFullName());
		content = content.replace("[[championshipName]]", participantTeam.getChampionshipName());
		content = content.replace("[[championshipCategory]]", participantTeam.getAsanaCategoryName() + " - "
				+ participantTeam.getCategory().getTitle() + " - " + participantTeam.getGender().toString());
		content = content.replace("[[teamId]]", participantTeam.getAutogenChestNumber());
		content = content.replace("[[approvalStatus]]", participantTeam.getStatus().toString());

		content = content.replace("[[nysfPortalLink]]", nysfPortalLink);
		// setting the plain text to html
		helper.setText(content, true);
		helper.setSubject(subject);
		mailSender.send(message);

	}
	
	@Async
	public void SendEmailForgotPassword(String link, String email) 
			throws UnsupportedEncodingException, MessagingException {
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = EmailUtil.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Here's the link to Reset Password";
		String content = "<p>Hello,</p>"
				          + "<p>You have requested to reset your password.</p>"
				          + "<p>Click the link below to change your password"
				          + "<p> <a href=\"" + link + "\" >change my password </a> </p>"
				          + "<br>"
				          + "<p>Ignore this if you do remember your password  "+" "
				          + "or you have not made the request </p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content, true);
		mailSender.send(message);
	}

}
