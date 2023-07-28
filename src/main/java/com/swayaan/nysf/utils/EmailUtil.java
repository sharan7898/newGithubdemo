package com.swayaan.nysf.utils;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.swayaan.nysf.entity.EmailSettingBag;


public class EmailUtil {
	
	public static String getSiteURL(HttpServletRequest request) {
		
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
		
	}
	
	public static  JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
	//	mailSender.setSmtpAuth(settings.getSmtpAuth());
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		mailProperties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
		
	}

}

