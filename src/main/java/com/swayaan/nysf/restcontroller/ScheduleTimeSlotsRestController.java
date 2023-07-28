package com.swayaan.nysf.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.ScheduleTimeSlots;
import com.swayaan.nysf.entity.DTO.ScheduleTimeSlotsDTO;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleTimeSlotsRepository;
import com.swayaan.nysf.service.ScheduleTimeSlotsService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ScheduleTimeSlotsRestController {
	
	@Autowired ScheduleTimeSlotsService scheduleTimeSlotsService;
	@Autowired ScheduleTimeSlotsRepository scheduleTimeSlotsRepository;
	
	@GetMapping("/scheduleTimeSlots/findById/{id}")
	public ScheduleTimeSlotsDTO findById(@PathVariable("id") Integer id) {
		ScheduleTimeSlots scheduleTimeSlots;
		try {
			scheduleTimeSlots = scheduleTimeSlotsService.get(id);
			ScheduleTimeSlotsDTO scheduleTimeSlotsDTO = new ScheduleTimeSlotsDTO(scheduleTimeSlots.getId(),
					scheduleTimeSlots.getTime_slot(),
					scheduleTimeSlots.getStart_time(),
					scheduleTimeSlots.getEnd_time(),
					scheduleTimeSlots.getScheduleDays().getId());
			return scheduleTimeSlotsDTO;
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
