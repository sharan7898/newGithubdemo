package com.swayaan.nysf.service;

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

import com.swayaan.nysf.entity.ChampionshipLevels;
import com.swayaan.nysf.entity.Role;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ChampionshipLevelsRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ChampionshipLevelsService {

	@Autowired
	ChampionshipLevelsRepository repo;
	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipLevelsService.class);

	public static final int RECORDS_PER_PAGE = 10;

	public List<ChampionshipLevels> listAllChampionshipLevels() {
		LOGGER.info("Entered listAllChampionshipLevels method -ChampionshipLevelsService");
		LOGGER.info("Exit listAllChampionshipLevels method -ChampionshipLevelsService");
      return repo.findAllByOrderByTitleAsc();
	}

	public ChampionshipLevels save(ChampionshipLevels championshipLevels) {
		LOGGER.info("Entered save method -ChampionshipLevelsService");

		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingLevel = (championshipLevels.getId() != null);
		if (isUpdatingLevel) {
			ChampionshipLevels existingLevel = repo.findById(championshipLevels.getId()).get();

			championshipLevels.setLastModifiedBy(currentUser);
			championshipLevels.setLastModifiedTime(new Date());
			championshipLevels.setCreatedBy(existingLevel.getCreatedBy());
			championshipLevels.setCreatedTime(existingLevel.getCreatedTime());

		} else {
			championshipLevels.setCreatedBy(currentUser);
			championshipLevels.setCreatedTime(new Date());
		}
		LOGGER.info("Exit save method -ChampionshipLevelsService");
        return repo.save(championshipLevels);
	}

	public ChampionshipLevels get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ChampionshipLevelsService");
            try {
        		LOGGER.info("Exit get method -ChampionshipLevelsService");
          return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			LOGGER.error("Could not find any level with ID " + id);
			throw new EntityNotFoundException("Could not find any level with ID " + id);
		}
	}

	public void delete(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered delete method -ChampionshipLevelsService");
        Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			LOGGER.error("Could not find any level with ID " + id);
			throw new EntityNotFoundException("Could not find any level with ID " + id);
		}
		LOGGER.info("Exit delete method -ChampionshipLevelsService");
         repo.deleteById(id);
	}

	public void updateChampionshipLevelsEnabledStatus(Integer id, boolean enabled) {
		LOGGER.info("Entered updateChampionshipLevelsEnabledStatus method -ChampionshipLevelsService");
		LOGGER.info("Exit updateChampionshipLevelsEnabledStatus method -ChampionshipLevelsService");
       repo.updateEnabledStatus(id, enabled);
	}

	public ChampionshipLevels getChampionshipLevelsByTitle(String title) {
		LOGGER.info("Entered getChampionshipLevelsByTitle method -ChampionshipLevelsService");
		LOGGER.info("Exit getChampionshipLevelsByTitle method -ChampionshipLevelsService");
       return repo.findByTitle(title);
	}

	public Page<ChampionshipLevels> listByPage(int pageNum, String sortField, String sortDir, String title) {
		LOGGER.info("Entered listByPage method -ChampionshipLevelsService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (title != null && !title.isEmpty()) {
			return repo.findAllByTitleContaining(title, pageable);
		}
		LOGGER.info("Entered listByPage method -ChampionshipLevelsService");

		return repo.findAll(pageable);
	}
}
