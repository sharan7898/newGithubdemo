package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.ScheduleFop;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleFopRepository;

@Service
public class ScheduleFopService {

	@Autowired
	ScheduleFopRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleFopService.class);

	public ScheduleFop get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ScheduleFopService");
		try {
			LOGGER.info("Exit get method -ScheduleFopService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any ScheduleFop with ID " + id);
			throw new EntityNotFoundException("Could not find any ScheduleFop with ID " + id);
		}
	}

	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -ScheduleFopService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any ScheduleFop with ID " + id);
			throw new EntityNotFoundException("Could not find any ScheduleFop with ID " + id);
		}
		LOGGER.info("Exit delete method -ScheduleFopService");
		repo.deleteById(id);
	}

	public List<ScheduleFop> listAllScheduleFop() {
		LOGGER.info("Entered listAllScheduleFop method -ScheduleFopService");
		LOGGER.info("Exit listAllScheduleFop method -ScheduleFopService");
		return repo.findAll();
	}

	public ScheduleFop save(ScheduleFop ScheduleFop) {
		LOGGER.info("Entered save method -ScheduleFopService");
		LOGGER.info("Exit save method -ScheduleFopService");
		return repo.save(ScheduleFop);
	}

	public List<ScheduleFop> getScheduleFopForSchedule(Schedule schedule) {
		LOGGER.info("Entered getScheduleFopForSchedule method -ScheduleFopService");
		LOGGER.info("Exit getScheduleFopForSchedule method -ScheduleFopService");
		return repo.findAllBySchedule(schedule);
	}

	public List<ScheduleFop> getAllByScheduleTimeSlotsOrderBySlot(Integer scheduleTimeSlotsId) {
		LOGGER.info("Entered getAllByScheduleTimeSlotsOrderBySlot method -ScheduleFopService");
		LOGGER.info("Exit getAllByScheduleTimeSlotsOrderBySlot method -ScheduleFopService");
		return repo.findAllByScheduleTimeSlotsOrderBySlot(scheduleTimeSlotsId);
	}

}
