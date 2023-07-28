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

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.Asana;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.CompulsoryRoundAsanas;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.CompulsoryRoundAsanasNotFoundException;
import com.swayaan.nysf.repository.CompulsoryRoundAsanasRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class CompulsoryRoundAsanasService {

	@Autowired
	CompulsoryRoundAsanasRepository repo;

	@Autowired
	ParticipantTeamAsanasService participantTeamAsanasService;

	public static final int RECORDS_PER_PAGE = 10;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompulsoryRoundAsanasService.class);

	public List<CompulsoryRoundAsanas> listAllCompulsoryRoundAsanas() {
		LOGGER.info("Entered listAllCompulsoryRoundAsanas method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit listAllCompulsoryRoundAsanas method -CompulsoryRoundAsanasService");
		return repo.findAll();
	}

	public CompulsoryRoundAsanas save(CompulsoryRoundAsanas compulsoryRoundAsanas) {
		LOGGER.info("Entered save method -CompulsoryRoundAsanasService");

		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdating = (compulsoryRoundAsanas.getId() != null);
		if (isUpdating) {
			CompulsoryRoundAsanas existingcompulsoryRoundAsanas = repo.findById(compulsoryRoundAsanas.getId()).get();

			compulsoryRoundAsanas.setLastModifiedBy(currentUser);
			compulsoryRoundAsanas.setLastModifiedTime(new Date());
			compulsoryRoundAsanas.setCreatedBy(existingcompulsoryRoundAsanas.getCreatedBy());
			compulsoryRoundAsanas.setCreatedTime(existingcompulsoryRoundAsanas.getCreatedTime());

			List<ParticipantTeamAsanas> ListPartiTeamAsanas = participantTeamAsanasService
					.getByCompulsoryAsanaId(compulsoryRoundAsanas.getId());

			for (ParticipantTeamAsanas participantTeamAsanas : ListPartiTeamAsanas) {
				participantTeamAsanas.setAsana(compulsoryRoundAsanas.getAsana());
				participantTeamAsanas.setRoundNumber(compulsoryRoundAsanas.getRoundNumber());
				participantTeamAsanas.setSequenceNumber(compulsoryRoundAsanas.getSequenceNumber());
				participantTeamAsanasService.save(participantTeamAsanas);
			}

		} else {
			compulsoryRoundAsanas.setCreatedBy(currentUser);
			compulsoryRoundAsanas.setCreatedTime(new Date());
			compulsoryRoundAsanas.setCompulsory(true);
		}
		LOGGER.info("Exit save method -CompulsoryRoundAsanasService");
		return repo.save(compulsoryRoundAsanas);
	}

	public CompulsoryRoundAsanas get(Integer id) throws CompulsoryRoundAsanasNotFoundException {
		LOGGER.info("Entered get method -CompulsoryRoundAsanasService");
		try {
			LOGGER.info("Exit get method -CompulsoryRoundAsanasService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CompulsoryRoundAsanasNotFoundException("Could not find the record");
		}
	}

	public void delete(Integer id) throws CompulsoryRoundAsanasNotFoundException {
		LOGGER.info("Entered delete method -CompulsoryRoundAsanasService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new CompulsoryRoundAsanasNotFoundException("Could not find the record");
		}
		LOGGER.info("Exit delete method -CompulsoryRoundAsanasService");
		repo.deleteById(id);
	}

	public List<CompulsoryRoundAsanas> getCompulsoryAsanas(ParticipantTeam participantTeam, Integer round_number) {
		LOGGER.info("Entered getCompulsoryAsanas method -CompulsoryRoundAsanasService");

		Integer asanaCategory_id = participantTeam.getAsanaCategory().getId();
		Integer category = participantTeam.getCategory().getId();
		Integer championship = participantTeam.getChampionship().getId();
		String gender = participantTeam.getGender().toString();
		boolean compulsory = true;
		LOGGER.info("Exit getCompulsoryAsanas method -CompulsoryRoundAsanasService");
		return repo.getCompulsoryAsanasForGivenParams(asanaCategory_id, category, gender, compulsory, round_number,
				championship);
	}

	public List<CompulsoryRoundAsanas> listCompulsoryRoundAsanasByAsanaCategory(AsanaCategory asanaCategory) {
		LOGGER.info("Entered listCompulsoryRoundAsanasByAsanaCategory method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit listCompulsoryRoundAsanasByAsanaCategory method -CompulsoryRoundAsanasService");
		return repo.findByAsanaCategory(asanaCategory);
	}

	public List<CompulsoryRoundAsanas> listCompulsoryRoundAsanasByAsana(Asana asana) {
		LOGGER.info("Entered listCompulsoryRoundAsanasByAsana method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit listCompulsoryRoundAsanasByAsana method -CompulsoryRoundAsanasService");
		return repo.findByAsana(asana);
	}

	public List<CompulsoryRoundAsanas> getCompulsoryAsanasByChampionship(Championship championship) {
		LOGGER.info("Entered getCompulsoryAsanasByChampionship method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit getCompulsoryAsanasByChampionship method -CompulsoryRoundAsanasService");
		return repo.findAllByChampionship(championship);
	}

	public List<CompulsoryRoundAsanas> listCompulsoryRoundAsanasByAgeCategory(AgeCategory ageCategory) {
		LOGGER.info("Entered listCompulsoryRoundAsanasByAgeCategory method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit listCompulsoryRoundAsanasByAgeCategory method -CompulsoryRoundAsanasService");
		return repo.findByCategory(ageCategory);
	}

	public int getCount(Championship championship, AsanaCategory asanaCategory, AgeCategory category, String gender,
			Integer roundNumber) {
		LOGGER.info("Entered getCount method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit getCount method -CompulsoryRoundAsanasService");
		return repo.getTotalCountForGivenParams(championship, asanaCategory, category, gender, roundNumber);
	}

	public List<CompulsoryRoundAsanas> getCompulsoryAsanas(Championship championship, Asana asana,
			AsanaCategory asanaCategory, GenderEnum gender, AgeCategory category, Integer roundNumber) {
		LOGGER.info("Entered getCompulsoryAsanas method -CompulsoryRoundAsanasService");
		LOGGER.info("Exit getCompulsoryAsanas method -CompulsoryRoundAsanasService");
		return repo.findAllByChampionshipAndAsanaAndAsanaCategoryAndGenderAndCategoryAndRoundNumber(championship, asana,
				asanaCategory, gender, category, roundNumber);
	}
	
	public Page<CompulsoryRoundAsanas> listByPage(int pageNum, String sortField, String sortDir, String asanaCategory,
			String category, String championshipName) {
		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		
		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		if (asanaCategory != "" && category != "" && championshipName != "") {
			// 000 name  championship asanaCategory
			return repo
					.findAllByAsanaCategoryNameContainingAndCategoryContainingAndChampionshipNameContainingAndStatusNotIn(
							asanaCategory,  category, championshipName, status,pageable);

		} 
		else if (asanaCategory != "" &&   category != "" && championshipName == "") {
			// name championship 001
			return repo.findAllByAsanaCategoryNameContainingAndCategoryContainingAndStatusNotIn(asanaCategory, category,status, pageable);
		} else if (asanaCategory != "" &&   category == "" && championshipName != "") {
			// name championship 010
			return repo.findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndStatusNotIn(asanaCategory, championshipName,status, pageable);
		} else if (asanaCategory != "" &&   category == "" && championshipName == "") {
			// name championship 011
			return repo.findAllByAsanaCategoryNameContainingAndStatusNotIn(asanaCategory, status, pageable);
		} else if (asanaCategory == "" &&   category != "" && championshipName != "") {
			// name championship 100
			return repo.findAllByCategoryContainingAndChampionshipNameContainingAndStatusNotIn(category, championshipName, status, pageable);
		}else if (asanaCategory == "" &&   category != "" && championshipName == "") {
			// name championship 101
			return repo.findAllByCategoryContainingAndStatusNotIn(category,  status, pageable);
		}else if (asanaCategory == "" &&   category == "" && championshipName != "") {
			// name championship 110
			return repo.findAllByChampionshipNameContainingAndStatusNotIn(championshipName, status, pageable);
		}
		
		else {
			LOGGER.info("findAllByChampionshipStatusNotIn method called");
			return repo.findAllByChampionshipStatusNotIn(status,pageable);
		}
	}

	public Page<CompulsoryRoundAsanas> listByPage(Integer championshipId, int pageNum, String sortField, String sortDir,
			String asanaCategory, String category, String championshipName) {
		LOGGER.info("Entered listByPage method -CompulsoryRoundAsanasService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);
		if (asanaCategory != "" && category != "" && championshipName != "") {
			// 000 name championship asanaCategory
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndCategoryContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");

			return repo
					.findAllByAsanaCategoryNameContainingAndCategoryContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId(
							asanaCategory, category, championshipName, status, championshipId, pageable);

		} else if (asanaCategory != "" && category != "" && championshipName == "") {
			// name championship 001
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndCategoryContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");

			return repo.findAllByAsanaCategoryNameContainingAndCategoryContainingAndStatusNotInAndChampionshipId(
					asanaCategory, category, status, championshipId, pageable);
		} else if (asanaCategory != "" && category == "" && championshipName != "") {
			// name championship 010
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");
			return repo
					.findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId(
							asanaCategory, championshipName, status, championshipId, pageable);
		} else if (asanaCategory != "" && category == "" && championshipName == "") {
			// name championship 011
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");
			return repo.findAllByAsanaCategoryNameContainingAndStatusNotInAndChampionshipId(asanaCategory, status,
					championshipId, pageable);
		} else if (asanaCategory == "" && category != "" && championshipName != "") {
			// name championship 100
			LOGGER.info(
					"findAllByCategoryContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");
			return repo.findAllByCategoryContainingAndChampionshipNameContainingAndStatusNotInAndChampionshipId(
					category, championshipName, status, championshipId, pageable);
		} else if (asanaCategory == "" && category != "" && championshipName == "") {
			// name championship 101
			LOGGER.info(
					"findAllByCategoryContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");
			return repo.findAllByCategoryContainingAndStatusNotInAndChampionshipId(category, status, championshipId,
					pageable);
		} else if (asanaCategory == "" && category == "" && championshipName != "") {
			// name championship 110
			LOGGER.info(
					"findAllByChampionshipNameContainingAndStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");
			return repo.findAllByChampionshipNameContainingAndStatusNotInAndChampionshipId(championshipName, status,
					championshipId, pageable);
		}

		else {
			LOGGER.info(
					"findAllByChampionshipStatusNotInAndChampionshipId method called -CompulsoryRoundAsanasService");
			LOGGER.info("Exit listByPage method -CompulsoryRoundAsanasService");
			return repo.findAllByChampionshipStatusNotInAndChampionshipId(status, championshipId, pageable);
		}
	}

}
