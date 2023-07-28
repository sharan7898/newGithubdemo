package com.swayaan.nysf.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.District;
import com.swayaan.nysf.entity.State;
import com.swayaan.nysf.entity.DTO.DistrictDTO;
import com.swayaan.nysf.service.DistrictService;
import com.swayaan.nysf.service.StateService;

@RestController
public class DistrictRestController {
	@Autowired
	DistrictService service;
	
	@Autowired
	StateService stateService;
	private static final Logger LOGGER = LoggerFactory.getLogger(DistrictRestController.class);

	@GetMapping("/district/list_by_states/{id}")
	public ResponseEntity<?> listByState(@PathVariable("id") Integer stateId) {
		System.out.println("stateId"+stateId);

		try {
			State state=stateService.get(stateId);
			
			List<District> listDistrict = service.findByStateOrderByNameAsc(state);
			

			List<DistrictDTO> listDistrictsDto = service.convertDistrictToDistrictDto(listDistrict);
			System.out.println("listDistrictsDto"+listDistrictsDto);
			return new ResponseEntity<>(listDistrictsDto,HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info("StateNotFoundException " + e.getMessage());
			return new ResponseEntity<>("Fail",HttpStatus.BAD_REQUEST);
		}

	}

}
