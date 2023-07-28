package com.swayaan.nysf.APIs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swayaan.nysf.API.entity.schedule.ScheduleDTO;
import com.swayaan.nysf.API.entity.schedule.ScheduleDaysDTO;
import com.swayaan.nysf.API.entity.schedule.ScheduleFopDTO;
import com.swayaan.nysf.API.entity.schedule.ScheduleTimeSlotsDTO;
import com.swayaan.nysf.entity.FOPEnum;
import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.ScheduleDays;
import com.swayaan.nysf.entity.ScheduleFop;
import com.swayaan.nysf.entity.ScheduleTimeSlots;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.ScheduleDaysService;
import com.swayaan.nysf.service.ScheduleFopService;
import com.swayaan.nysf.service.ScheduleService;
import com.swayaan.nysf.service.ScheduleTimeSlotsService;

@RestController
@RequestMapping("/api/v1/championship/kheloindia")
public class KIScheduleRestController {

	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ScheduleDaysService scheduleDaysService;
	@Autowired
	ScheduleFopService scheduleFopService;
	@Autowired
	ScheduleTimeSlotsService scheduleTimeSlotsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(KIScheduleRestController.class);

	@GetMapping(value = "/KISchedule", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getKISchedule() throws EntityNotFoundException {
		Integer scheduleId = 1;
		ScheduleDTO scheduleDTO;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Schedule schedule = scheduleService.get(scheduleId);

			// get schedule days for the given schedule
			List<ScheduleDays> listScheduleDays = new ArrayList<ScheduleDays>();
			List<ScheduleDaysDTO> listScheduleDaysDTO = new ArrayList<>();
			listScheduleDays = scheduleDaysService.getScheduleDaysOrderBySequenceNumber(scheduleId);
			if (listScheduleDays.size() != 0) {
				for (ScheduleDays eachDay : listScheduleDays) {

					// get schedule time slots for the given schedule day
					List<ScheduleTimeSlots> listTimeSlotsByScheduleDay = new ArrayList<ScheduleTimeSlots>();
					listTimeSlotsByScheduleDay = scheduleTimeSlotsService.getAllByScheduleAndScheduleDays(scheduleId,
							eachDay.getId());
					List<ScheduleTimeSlotsDTO> listScheduleTimeSlotsDTO = new ArrayList<>();

					if (listTimeSlotsByScheduleDay.size() != 0) {
						for (ScheduleTimeSlots timeSlot : listTimeSlotsByScheduleDay) {

							// get schedule fop for the given schedule time slots
							List<ScheduleFop> listFopByScheduleTimeSlots = new ArrayList<ScheduleFop>();
							List<ScheduleFopDTO> listScheduleFopDTO = new ArrayList<>();
							listFopByScheduleTimeSlots = scheduleFopService
									.getAllByScheduleTimeSlotsOrderBySlot(timeSlot.getId());
							if (listFopByScheduleTimeSlots.size() != 0) {

								for (ScheduleFop eachFop : listFopByScheduleTimeSlots) {

									if (eachFop.getFop().equals(FOPEnum.Common)) {
										listScheduleFopDTO.add(new ScheduleFopDTO("common", eachFop.getDescription()));
									} else if (eachFop.getFop().equals(FOPEnum.FOP1)) {
										listScheduleFopDTO.add(new ScheduleFopDTO("Fop 1", eachFop.getCategory_name(),
												eachFop.getCategory_detail_id(), eachFop.getEvent_id(),
												eachFop.getEvent_description(), eachFop.getDescription()));
									} else if (eachFop.getFop().equals(FOPEnum.FOP2)) {
										listScheduleFopDTO.add(new ScheduleFopDTO("Fop 2", eachFop.getCategory_name(),
												eachFop.getCategory_detail_id(), eachFop.getEvent_id(),
												eachFop.getEvent_description(), eachFop.getDescription()));
									}
								}

								try {
									listScheduleTimeSlotsDTO.add(new ScheduleTimeSlotsDTO(timeSlot.getTime_slot(),
											convertDateToStringTimeStamp(df.format(eachDay.getDate()),
													timeSlot.getStart_time()),
											convertDateToStringTimeStamp(df.format(eachDay.getDate()),
													timeSlot.getEnd_time()),
											listScheduleFopDTO));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}

					}

					listScheduleDaysDTO.add(new ScheduleDaysDTO(eachDay.getDay(), df.format(eachDay.getDate()),
							listScheduleTimeSlotsDTO));
				}
			} else {
				// model.addAttribute("listScheduleDays", listScheduleDays);
			}

			scheduleDTO = new ScheduleDTO(schedule.getChampionship().getName(), schedule.getChampionship().getId(),
					schedule.getSport_id(), schedule.getSport_name(), schedule.getDescription(), listScheduleDaysDTO);

		} catch (EntityNotFoundException e) {
			// throw new EntityNotFoundException("Schedule not found for the Championship!");
			return new ResponseEntity<String>("Schedule not found for the Championship!", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<ScheduleDTO>(scheduleDTO, HttpStatus.OK);
	}

	public String convertDateToStringTimeStamp(String date, String time) throws ParseException {
		TimeZone tz = TimeZone.getTimeZone("IST");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
		df.setTimeZone(tz);
		Date dateTime = df.parse(date + " " + time);
		Long epoch = dateTime.getTime();
		return epoch.toString();
	}

}
