package com.swayaan.nysf.restcontroller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.service.UserService;

@RestController
public class ManageUserRestController {
	@Autowired
	private UserService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageUserRestController.class);

	@PostMapping("/user/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		LOGGER.info("Entered checkDuplicateEmail ManageUserRestController");
		return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}

