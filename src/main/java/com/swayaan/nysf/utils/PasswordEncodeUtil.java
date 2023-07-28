package com.swayaan.nysf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncodeUtil {
	@Autowired
	private  PasswordEncoder passwordEncoder;

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncodeUtil.class);
	
	public  String encodePassword(String password) {
		System.out.println("password"+password);
		String encodedPassword = passwordEncoder.encode(password);		
		return encodedPassword;
	}
}

