package com.swayaan.nysf.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.swayaan.nysf.entity.District;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.DistrictNotFoundException;
import com.swayaan.nysf.exception.StateNotFoundException;
import com.swayaan.nysf.repository.RoleRepository;
import com.swayaan.nysf.repository.UserRepository;
import com.swayaan.nysf.service.DistrictService;
import com.swayaan.nysf.service.StateService;

@Configuration

public class InitialDataConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataConfiguration.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	Environment env;

	@Autowired
	StateService stateService;
	@Autowired
	DistrictService districtService;

	/*
	 * Copyright 2023 the original author or authors.
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
	 * use this file except in compliance with the License. You may obtain a copy of
	 * the License at
	 *
	 * https://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations under
	 * the License.
	 */

	@PostConstruct
	public void postConstruct() {
		LOGGER.info("Entered Config-> postConstruct  Method");
		List<Role> roles = new ArrayList<Role>();
		Role adminRole = new Role(1, "Administrator", "", true, false);
		roles.add(adminRole);

		List<Role> listRoles = roleRepo.findAllByOrderByNameAsc();
		if (listRoles.isEmpty()) {
			for (int i = 0; i < roles.size(); i++) {
				roleRepo.save(roles.get(i));
			}

		}

		State state = null;
		District district = null;
		try {
			state = stateService.get(Integer.parseInt(env.getProperty("nysf.admin.address.state")));
			district = districtService.get(Integer.parseInt(env.getProperty("nysf.admin.address.district")));
		} catch (NumberFormatException | StateNotFoundException e) {
			LOGGER.info("StateNotFoundException :" + e.getMessage());
		} catch (DistrictNotFoundException e) {
			LOGGER.info("DistrictNotFoundException :" + e.getMessage());
		}

		LOGGER.info("Added Intial roles to the table Roles");

		LOGGER.info("Add Admin to the table User");
		Set<Role> role = new HashSet<Role>();
		role.add(adminRole);
		User user = new User("admin@nysf.com", passwordEncoder.encode("Khelo*#123"), "Admin", "NYSF", true, role,
				"admin@nysf.com", state, district);
		User user1 = userRepo.getUserByEmail("admin@nysf.com");
		if (user1 == null) {
			userRepo.save(user);
		}

	}
}