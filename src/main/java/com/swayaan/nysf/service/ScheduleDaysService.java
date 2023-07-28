package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.ScheduleDays;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleDaysRepository;

@Service
public class ScheduleDaysService {

	@Autowired
	ScheduleDaysRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDaysService.class);

	public ScheduleDays get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ScheduleDaysService");
		try {
			LOGGER.info("Exit get method -ScheduleDaysService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any ScheduleDays with ID " + id);
			throw new EntityNotFoundException("Could not find any ScheduleDays with ID " + id);
		}
	}

	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -ScheduleDaysService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any ScheduleDays with ID " + id);
			throw new EntityNotFoundException("Could not find any ScheduleDays with ID " + id);
		}
		LOGGER.info("Exit delete method -ScheduleDaysService");
		repo.deleteById(id);
	}

	public List<ScheduleDays> listAll() {
		LOGGER.info("Entered listAll method -ScheduleDaysService");
		LOGGER.info("Exit listAll method -ScheduleDaysService");
		return repo.findAll();
	}

	public ScheduleDays save(ScheduleDays scheduleDays) {
		LOGGER.info("Entered save method -ScheduleDaysService");
		LOGGER.info("Exit save method -ScheduleDaysService");
		return repo.save(scheduleDays);
	}

	public List<ScheduleDays> getScheduleDaysOrderByDate(Integer scheduleId) {
		LOGGER.info("Entered getScheduleDaysOrderByDate method -ScheduleDaysService");
		LOGGER.info("Exit getScheduleDaysOrderByDate method -ScheduleDaysService");
		return repo.getAllByScheduleOrderByDateAsc(scheduleId);
	}

	public List<ScheduleDays> getScheduleDaysOrderBySequenceNumber(Integer id) {
		LOGGER.info("Entered getScheduleDaysOrderBySequenceNumber method -ScheduleDaysService");
		LOGGER.info("Exit getScheduleDaysOrderBySequenceNumber method -ScheduleDaysService");
		return repo.getAllByScheduleOrderBySequenceNumber(id);
	}

	public List<ScheduleDays> getScheduleDaysBySchedule(Integer scheduleId) {
		LOGGER.info("Entered getScheduleDaysBySchedule method -ScheduleDaysService");
		LOGGER.info("Exit getScheduleDaysBySchedule method -ScheduleDaysService");
		return repo.findBySchedule(scheduleId);
	}

}
