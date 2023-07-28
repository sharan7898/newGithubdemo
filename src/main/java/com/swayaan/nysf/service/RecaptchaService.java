package com.swayaan.nysf.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mysql.cj.log.Log;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantRegistration;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.AsanaNotFoundException;
import com.swayaan.nysf.exception.ParticipantRegistrationNotFoundException;
import com.swayaan.nysf.repository.AsanaRepository;
import com.swayaan.nysf.repository.ParticipantRegistrationRepository;
import com.swayaan.nysf.repository.ParticipantRepository;
import com.swayaan.nysf.utils.CommonUtil;
import com.swayaan.nysf.utils.RecaptchaUtil;

@Service
public class RecaptchaService {

	@Value("${google.recaptcha.secret}") String recaptchaSecret;
	private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
			"https://www.google.com/recaptcha/api/siteverify";
	@Autowired RestTemplateBuilder restTemplateBuilder;
	
	public String verifyRecaptcha(String ip, String recaptchaResponse){
		Map<String, String> body = new HashMap<>();
		body.put("secret", recaptchaSecret);
		body.put("response", recaptchaResponse);
		body.put("remoteip", ip);
		//Log.logDebug("Request body for recaptcha: {}", body);
		ResponseEntity<Map> recaptchaResponseEntity = 
				restTemplateBuilder.build()
				.postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL+
						"?secret={secret}&response={response}&remoteip={remoteip}", 
						body, Map.class, body);
		//log.debug("Response from recaptcha: {}", recaptchaResponseEntity);
		Map<String, Object> responseBody = recaptchaResponseEntity.getBody();
		boolean recaptchaSucess = (Boolean)responseBody.get("success");
		if ( !recaptchaSucess) {
			List<String> errorCodes = (List)responseBody.get("error-codes");
			String errorMessage = errorCodes.stream()
					.map(s -> RecaptchaUtil.RECAPTCHA_ERROR_CODE.get(s))
					.collect(Collectors.joining(", "));
			return errorMessage;
		}else {
			return StringUtils.EMPTY;
		}
	}

		public boolean checkCaptcha(String recaptchaResponse, HttpServletRequest request) {

		String ip = request.getRemoteAddr();
		String captchaVerifyMessage = verifyRecaptcha(ip, recaptchaResponse);

		if (StringUtils.isNotEmpty(captchaVerifyMessage)) {
			return false;
		}
		return true;
	}

	
	
}
