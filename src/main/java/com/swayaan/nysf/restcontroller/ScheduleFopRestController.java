package com.swayaan.nysf.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.ScheduleFop;
import com.swayaan.nysf.entity.DTO.ScheduleFopDTO;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleFopRepository;
import com.swayaan.nysf.service.ScheduleFopService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ScheduleFopRestController {
	
	@Autowired ScheduleFopService scheduleFopService;
	@Autowired ScheduleFopRepository scheduleFopRepository;
	
	@GetMapping("/scheduleFop/findById/{id}")
	public ScheduleFopDTO findById(@PathVariable("id") Integer id) {
		ScheduleFop scheduleFop;
		try {
			scheduleFop = scheduleFopService.get(id);
			ScheduleFopDTO scheduleFopDTO = new ScheduleFopDTO(scheduleFop.getId(),
					scheduleFop.getFop().toString(),
					scheduleFop.getCategory_detail_id(),
					scheduleFop.getCategory_name(),
					scheduleFop.getEvent_id(),
					scheduleFop.getEvent_description(),
					scheduleFop.getDescription(),
					scheduleFop.getSchedule().getId(),
					scheduleFop.getScheduleDays().getId(),
					scheduleFop.getScheduleTimeSlots().getId());
			return scheduleFopDTO;
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
