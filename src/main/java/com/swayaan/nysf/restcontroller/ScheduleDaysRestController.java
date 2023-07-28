package com.swayaan.nysf.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.entity.ScheduleDays;
import com.swayaan.nysf.entity.ScheduleTimeSlots;
import com.swayaan.nysf.entity.DTO.ScheduleDaysDTO;
import com.swayaan.nysf.entity.DTO.ScheduleTimeSlotsDTO;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleDaysRepository;
import com.swayaan.nysf.service.ScheduleDaysService;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ScheduleDaysRestController {
	
	@Autowired ScheduleDaysService scheduleDaysService;
	@Autowired ScheduleDaysRepository scheduleDaysRepository;
	
	@GetMapping("/scheduleDays/{scheduleDayId}/getTimeSlots")
	public  List<ScheduleTimeSlotsDTO>  getTimeSlotsForScheduleDay(@PathVariable(name = "scheduleDayId") Integer id){
		
		ScheduleDays scheduleDays;
		List<ScheduleTimeSlots> listScheduleTimeSlots = new ArrayList<ScheduleTimeSlots>();
		List<ScheduleTimeSlotsDTO> result = new ArrayList<>();
		try {
			scheduleDays = scheduleDaysService.get(id);
			listScheduleTimeSlots = scheduleDays.getScheduleTimeSlots();
			
			for(ScheduleTimeSlots timeSlot : listScheduleTimeSlots) {
				result.add(new ScheduleTimeSlotsDTO(timeSlot.getId(), 
						timeSlot.getTime_slot(),
						timeSlot.getStart_time(),
						timeSlot.getEnd_time(),
						timeSlot.getScheduleDays().getId(),
						timeSlot.getScheduleDays().getDay()));
			}
			System.out.println("result :" + result);
			return result;
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	@GetMapping("/scheduleDays/findById/{id}")
	public ScheduleDaysDTO findById(@PathVariable("id") Integer id) {
		ScheduleDays scheduleDays;
		try {
			scheduleDays = scheduleDaysService.get(id);
			ScheduleDaysDTO scheduleDaysDTO = new ScheduleDaysDTO(scheduleDays.getId(),scheduleDays.getDay(),scheduleDays.getDate(),scheduleDays.getSequenceNumber());
			return scheduleDaysDTO;
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
