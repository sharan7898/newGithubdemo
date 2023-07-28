package com.swayaan.nysf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.EventCategory;
import com.swayaan.nysf.entity.EventRegistration;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.repository.EventCategoryRepository;

@Service
public class EventCategoryService {

	@Autowired
	EventCategoryRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventCategoryService.class);

	public void deleteById(Integer id) {
		LOGGER.info("Entered deleteById method -EventCategoryService");
		LOGGER.info("Exit deleteById method -EventCategoryService");
		repo.deleteById(id);

	}

	public List<EventCategory> getEventCategoryByAgeCategory(AgeCategory ageCategory) {
		LOGGER.info("Entered getEventCategoryByAgeCategory method -EventCategoryService");
		LOGGER.info("Exit getEventCategoryByAgeCategory method -EventCategoryService");
		return repo.findByAgeCategory(ageCategory);
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -EventCategoryService");
		LOGGER.info("Exit delete method -EventCategoryService");
		repo.deleteById(id);
	}

	public List<EventCategory> getEventCategoryByAsanaCategory(AsanaCategory asanaCategory) {
		LOGGER.info("Entered getEventCategoryByAsanaCategory method -EventCategoryService");
		LOGGER.info("Exit getEventCategoryByAsanaCategory method -EventCategoryService");
		return repo.findByAsanaCategory(asanaCategory);
	}

	public void save(EventCategory eventCategory, EventRegistration eventRegistration) {
		LOGGER.info("Entered save method -EventCategoryService");
		if (eventCategory.getId() != null) {
			EventCategory existingEventCategory = repo.findById(eventCategory.getId()).get();

			existingEventCategory.setAsanaCategory(eventCategory.getAsanaCategory());
			existingEventCategory.setAgeCategory(eventCategory.getAgeCategory());
			existingEventCategory.setGender(eventCategory.getGender());
			existingEventCategory.setNoOfRounds(eventCategory.getNoOfRounds());
			LOGGER.info("Exit save method -EventCategoryService");
			repo.save(existingEventCategory);

		}
	}

	public EventCategory findById(Integer id) {
		LOGGER.info("Entered findById method -EventCategoryService");
		LOGGER.info("Exit findById method -EventCategoryService");
		return repo.findById(id).get();
	}

}
