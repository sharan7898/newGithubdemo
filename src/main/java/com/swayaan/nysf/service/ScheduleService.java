package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ScheduleRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ScheduleService {
	
	@Autowired ScheduleRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);
	public static final int RECORDS_PER_PAGE = 10;
	
	
	public Schedule get(Integer id) throws EntityNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any Schedule with ID " + id);
			throw new EntityNotFoundException("Could not find any Schedule with ID " + id);
		}
	}
	
	public void delete(Integer id) throws EntityNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any Schedule with ID " + id);
			throw new EntityNotFoundException("Could not find any Schedule with ID " + id);
		}
		repo.deleteById(id);
	}

	public List<Schedule> listAllSchedules() {
		return repo.findAllByOrderByChampionshipDesc();
	}

	public Schedule save(Schedule schedule) {
		User currentUser = CommonUtil.getCurrentUser();		
		boolean isUpdatingSchedule = (schedule.getId() != null);
		if (isUpdatingSchedule) {
			Schedule existingSchedule = repo.findById(schedule.getId()).get();
			schedule.setScheduleDays(existingSchedule.getScheduleDays());
			schedule.setLastModifiedBy(currentUser);
			schedule.setLastModifiedTime(new Date());
			schedule.setCreatedBy(existingSchedule.getCreatedBy());
			schedule.setCreatedTime(existingSchedule.getCreatedTime());
			
		} else {		
			schedule.setCreatedBy(currentUser);
			schedule.setCreatedTime(new Date());
		}

		return repo.save(schedule);
		
	}
	
	public void updateScheduleEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}

	public List<Schedule> getScheduleByChampionship(Championship championship) {
		return repo.findAllByChampionship(championship);
	}


	public Page<Schedule> listByPage(int pageNum, String sortField, String sortDir, String description,
			String championshipName) {
		Sort sort = null;

		
		sort = Sort.by(sortField);
		

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		
		// default filter - championshipStatusNotIn
				List<ChampionshipStatusEnum> status = new ArrayList<>();
				status.add(ChampionshipStatusEnum.REJECTED);
				status.add(ChampionshipStatusEnum.DELETED);

		
		if (description != "" && championshipName != "") {
			LOGGER.info("findAllByDescriptionContainingAndChampionshipNameContainingAndStatusNotIn method called");
			return repo.findAllByDescriptionContainingAndChampionshipNameContainingAndStatusNotIn(description, championshipName,status, pageable);
		} else if (description == "" && championshipName != "") {
			LOGGER.info("findAllByChampionshipNameContainingAndStatusNotIn method called");
			return repo.findAllByChampionshipNameContainingAndStatusNotIn(championshipName,status, pageable);
		} else if (description != "" && championshipName == "") {
			LOGGER.info("findAllByDescriptionContainingAndStatusNotIn method called");
			return repo.findAllByDescriptionContainingAndStatusNotIn(description, status,pageable);
		} else {
			LOGGER.info("findAll method called");
			return repo.findAllByChampionshipStatusNotIn(status,pageable);
		}

	}


}
