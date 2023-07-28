package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.ScheduleTimeSlots;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleTimeSlotsRepository;

@Service
public class ScheduleTimeSlotsService {
	
	@Autowired ScheduleTimeSlotsRepository repo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTimeSlotsService.class);
	
	
	public ScheduleTimeSlots get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ScheduleTimeSlotsService");
	try {
		LOGGER.info("Exit get method -ScheduleTimeSlotsService");
       return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any ScheduleTimeSlots with ID " + id);
			throw new EntityNotFoundException("Could not find any ScheduleTimeSlots with ID " + id);
		}
	}
	
	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -ScheduleTimeSlotsService");
       Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any ScheduleTimeSlots with ID " + id);
			throw new EntityNotFoundException("Could not find any ScheduleTimeSlots with ID " + id);
		}
		LOGGER.info("Exit delete method -ScheduleTimeSlotsService");
       repo.deleteById(id);
	}

	public List<ScheduleTimeSlots> listAllScheduleTimeSlots() {
		LOGGER.info("Entered listAllScheduleTimeSlots method -ScheduleTimeSlotsService");
		LOGGER.info("Exit listAllScheduleTimeSlots method -ScheduleTimeSlotsService");
     return repo.findAll();
	}

	public ScheduleTimeSlots save(ScheduleTimeSlots scheduleTimeSlots) {
		LOGGER.info("Entered save method -ScheduleTimeSlotsService");
		LOGGER.info("Exit save method -ScheduleTimeSlotsService");
	return repo.save(scheduleTimeSlots);
	}

	public List<ScheduleTimeSlots> getTimeSlotsForSchedule(Schedule schedule) {
		LOGGER.info("Entered getTimeSlotsForSchedule method -ScheduleTimeSlotsService");
		LOGGER.info("Exit getTimeSlotsForSchedule method -ScheduleTimeSlotsService");
       return repo.findAllBySchedule(schedule);
	}

	public List<ScheduleTimeSlots> getAllByScheduleAndScheduleDays(Integer scheduleId, Integer scheduleDayId) {
		LOGGER.info("Entered getAllByScheduleAndScheduleDays method -ScheduleTimeSlotsService");
		LOGGER.info("Exit getAllByScheduleAndScheduleDays method -ScheduleTimeSlotsService");
     return repo.findAllByScheduleAndScheduleDay(scheduleId,scheduleDayId);
	}
	
}
