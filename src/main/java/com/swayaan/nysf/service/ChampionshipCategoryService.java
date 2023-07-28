package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.controller.ManageChampionshipController;
import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamParticipants;
import com.swayaan.nysf.entity.RegistrationStatusEnum;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.entity.DTO.CategorySelectionDTO;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ChampionshipCategoryRepository;

@Service
public class ChampionshipCategoryService {

	@Autowired
	ChampionshipCategoryRepository repo;

	@Autowired
	ChampionshipService championshipService;

	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	@Autowired
	ParticipantTeamParticipantsService participantTeamParticipantsService;

	@Autowired
	ChampionshipRoundsService championshipRoundsService;

	@Value("${no.traditional.category.participants}")
	private Integer noTraditionalCategoryParticipants;
	@Value("${no.artistic.single.category.participants}")
	private Integer noArtisticSingleCategoryParticipants;
	@Value("${no.artistic.pair.category.participants}")
	private Integer noArtisticPairCategoryParticipants;
	@Value("${no.rhythmic.pair.category.participants}")
	private Integer noRhythmicPairCategoryParticipants;
	@Value("${no.artistic.group.category.participants}")
	private Integer noArtisticGroupCategoryParticipants;

	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	public static final int RECORDS_PER_PAGE = 10;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipRoundsService.class);

	public void deleteById(Integer id) {
		LOGGER.info("Entered deleteById method -ChampionshipRoundsService");
		LOGGER.info("Exit deleteById method -ChampionshipRoundsService");
      	repo.deleteById(id);
	}

	public Integer getChampionshipCategoryRoundsForChampionshipId(Championship championship,
			AsanaCategory asanaCategory, AgeCategory category, GenderEnum gender) {
		LOGGER.info("Entered getChampionshipCategoryRoundsForChampionshipId method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryRoundsForChampionshipId method -ChampionshipRoundsService");
	return repo.getRoundsForChampionshipAndAsanaCategoryAndAgeCategoryAndGender(championship.getId(),
				asanaCategory.getId(), category.getId(), gender.toString());
	}

	public ChampionshipCategory getChampionshipCategoryForAllConditions(Integer championshipId, Integer asanaCategoryId,
			Integer ageCategoryId, String gender) {
		LOGGER.info("Entered getChampionshipCategoryForAllConditions method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryForAllConditions method -ChampionshipRoundsService");
       return repo.findByChampionshipAndAsanaCategoryAndGenderAndAgeCategory(championshipId, asanaCategoryId,
				ageCategoryId, gender);
	}

	public ChampionshipCategory get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ChampionshipRoundsService");
         try {
			LOGGER.info("Exit get method -ChampionshipRoundsService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EntityNotFoundException("Could not find any championship category with ID " + id);
		}
	}

	public ChampionshipCategory getChampionshipCategoryForChampionshipId(Championship championship,
			Integer asanaCategory, Integer ageCategory, String gender) {
		LOGGER.info("Entered getChampionshipCategoryForChampionshipId method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryForChampionshipId method -ChampionshipRoundsService");
      return repo.getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryAndGender(championship.getId(),
				asanaCategory, ageCategory, gender);
	}

	public void delete(Championship championship) {
		LOGGER.info("Entered delete method -ChampionshipRoundsService");
		LOGGER.info("Exit delete method -ChampionshipRoundsService");
     	repo.deleteByChampionshipId(championship.getId());
	}

	public Integer getChampionshipCategoryParticipantsForChampionshipId(Championship championship,
			AsanaCategory asanaCategory, AgeCategory category, GenderEnum gender) {
		LOGGER.info("Entered getChampionshipCategoryParticipantsForChampionshipId method -ChampionshipRoundsService");
       LOGGER.info("Exit getChampionshipCategoryParticipantsForChampionshipId method -ChampionshipRoundsService");
       return repo.getParticipantsForChampionshipAndAsanaCategoryAndAgeCategoryAndGender(championship.getId(),
				asanaCategory.getId(), category.getId(), gender.toString());
	}

	public List<ChampionshipCategory> getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc(
			Integer championshipId, Integer asanaCategoryId, Integer ageCategoryId) {
		LOGGER.info("Entered getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryForChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc method -ChampionshipRoundsService");
      return repo.findAllByChampionshipAndAsanaCategoryAndAgeCategoryOrderByAsanaCategoryAsc(championshipId,
				asanaCategoryId, ageCategoryId);
	}

	public List<ChampionshipCategory> getChampionshipCategoryByChampionship(Championship championship) {
		LOGGER.info("Entered getChampionshipCategoryByChampionship method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryByChampionship method -ChampionshipRoundsService");
      return repo.findByChampionship(championship);
	}

	public List<ChampionshipCategory> getChampionshipCategoryByChampionshipId(Integer championship) {
		LOGGER.info("Entered getChampionshipCategoryByChampionshipId method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryByChampionshipId method -ChampionshipRoundsService");
      return repo.findByChampionshipId(championship);
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -ChampionshipRoundsService");
		LOGGER.info("Exit delete method -ChampionshipRoundsService");
        repo.deleteById(id);
	}

	public List<CategorySelectionDTO> getEnrolledCatgeoryForChampionshipAndParticipant(Championship championship,
			Participant participant) {
		LOGGER.info("Entered getEnrolledCatgeoryForChampionshipAndParticipant method -ChampionshipRoundsService");


		Set<CategorySelectionDTO> setEnrolledCategory = new HashSet<>();
		List<ParticipantTeamParticipants> listParticipantTeamParticipants = participantTeamParticipantsService
				.findByParticipant(participant);
		if (!listParticipantTeamParticipants.isEmpty()) {
			for (ParticipantTeamParticipants ptparticipant : listParticipantTeamParticipants) {
				ParticipantTeam participantTeam = ptparticipant.getParticipantTeam();
				if (!participantTeam.getStatus().equals(RegistrationStatusEnum.REJECTED)) {
					List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = championshipParticipantTeamsService
							.findByParticipantTeam(participantTeam);
					if (!listChampionshipParticipantTeams.isEmpty()) {
						for (ChampionshipParticipantTeams championshipParticipantTeams : listChampionshipParticipantTeams) {
							if (championshipParticipantTeams.getChampionshipRounds().getChampionship()
									.equals(championship)
									&& championshipParticipantTeams.getChampionshipRounds().getRound() == 1) {
								setEnrolledCategory
										.add(new CategorySelectionDTO(
												championshipParticipantTeams.getChampionshipRounds()
														.getChampionshipCategory(),
												championshipParticipantTeams,
												championshipParticipantTeams.getParticipantTeam()));
							}
						}

					}
				}
			}

		}
		List<CategorySelectionDTO> listEnrolledCategory = new ArrayList<>(setEnrolledCategory);
		List<CategorySelectionDTO> listSortedEnrolledCategory = listEnrolledCategory.stream()
				.sorted(Comparator.comparing(CategorySelectionDTO::getParticipantTeam,
						(pt1, pt2) -> (pt1.getChestNumber().compareTo(pt2.getChestNumber()))))
				.collect(Collectors.toList());
		LOGGER.info("Exit getEnrolledCatgeoryForChampionshipAndParticipant method -ChampionshipRoundsService");
       return listSortedEnrolledCategory;
	}

	public List<ChampionshipCategory> getChampionshipCategoryByAgeCategory(AgeCategory ageCategory) {
		LOGGER.info("Entered getChampionshipCategoryByAgeCategory method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryByAgeCategory method -ChampionshipRoundsService");
       return repo.findByCategory(ageCategory);
	}

	public List<ChampionshipCategory> getChampionshipCategoryByAgeCategoryandChamionship(AgeCategory ageCategory,
			Integer championshipId) {
		LOGGER.info("Entered getChampionshipCategoryByAgeCategoryandChamionship method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryByAgeCategoryandChamionship method -ChampionshipRoundsService");
      return repo.findByCategoryAndChampionship(ageCategory, championshipId);
	}

	public List<ChampionshipCategory> getChampionshipCategoryByAsanaCategoryandChamionship(AsanaCategory asanaCategory,
			Integer championshipId) {
		LOGGER.info("Entered getChampionshipCategoryByAsanaCategoryandChamionship method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryByAsanaCategoryandChamionship method -ChampionshipRoundsService");
      	return repo.findByAsanaCategoryAndChampionship(asanaCategory, championshipId);
	}

	public List<ChampionshipCategory> getChampionshipCategoryByAsanaCategory(AsanaCategory asanaCategory) {
		LOGGER.info("Entered getChampionshipCategoryByAsanaCategory method -ChampionshipRoundsService");
		LOGGER.info("Exit getChampionshipCategoryByAsanaCategory method -ChampionshipRoundsService");
     return repo.findByAsanaCategory(asanaCategory);
	}

	public void save(ChampionshipCategory championshipCategory, Championship championship) {
		LOGGER.info("Entered save method -ChampionshipRoundsService");

		if (championshipCategory.getId() != null) {
			ChampionshipCategory existingChampionshipCategory = repo.findById(championshipCategory.getId()).get();
			List<ChampionshipRounds> listChampionshipRounds = championshipRoundsService
					.findByChampionshipAndChampionshipCategory(championship, existingChampionshipCategory);
			if (existingChampionshipCategory.getNoOfRounds() != championshipCategory.getNoOfRounds()) {
				if (existingChampionshipCategory.getNoOfRounds() > championshipCategory.getNoOfRounds()) {
					for (ChampionshipRounds champRounds : listChampionshipRounds) {
						if (champRounds.getRound() > championshipCategory.getNoOfRounds()) {
							championshipRoundsService.delete(champRounds.getId());
						}

					}

				} else if (existingChampionshipCategory.getNoOfRounds() < championshipCategory.getNoOfRounds()) {

					for (int i = existingChampionshipCategory.getNoOfRounds() + 1; i <= championshipCategory
							.getNoOfRounds(); i++) {
						System.out.println(i);

						ChampionshipRounds newChampionshipRounds = new ChampionshipRounds();
						newChampionshipRounds.setChampionship(championship);
						newChampionshipRounds.setChampionshipCategory(existingChampionshipCategory);
						newChampionshipRounds.setNoOfParticipantTeams(championshipCategory.getNoOfParticipants());
						newChampionshipRounds.setRound(i);
						newChampionshipRounds.setStatus(RoundStatusEnum.SCHEDULED);
						championshipRoundsService.save(newChampionshipRounds);
					}

				}

			}

			existingChampionshipCategory.setAsanaCategory(championshipCategory.getAsanaCategory());
			existingChampionshipCategory.setCategory(championshipCategory.getCategory());
			existingChampionshipCategory.setGender(championshipCategory.getGender());
			existingChampionshipCategory.setNoOfRounds(championshipCategory.getNoOfRounds());
			if (championshipCategory.getAsanaCategory().getId() == TRADITIONAL_ASANA_CATEGORY_ID) {
				existingChampionshipCategory.setNoOfParticipants(noTraditionalCategoryParticipants);
			} else if (championshipCategory.getAsanaCategory().getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID) {
				existingChampionshipCategory.setNoOfParticipants(noArtisticSingleCategoryParticipants);
			} else if (championshipCategory.getAsanaCategory().getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID) {
				existingChampionshipCategory.setNoOfParticipants(noArtisticPairCategoryParticipants);
			} else if (championshipCategory.getAsanaCategory().getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID) {
				existingChampionshipCategory.setNoOfParticipants(noRhythmicPairCategoryParticipants);
			} else if (championshipCategory.getAsanaCategory().getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID) {
				existingChampionshipCategory.setNoOfParticipants(noArtisticGroupCategoryParticipants);
			}
			LOGGER.info("Exit save method -ChampionshipRoundsService");
           repo.save(existingChampionshipCategory);

		}
	}

	public Page<ChampionshipCategory> listPageByChampionship(Integer championship, int pageNum, String sortField,
			String sortDir, String asanaCate, String ageCate) {
		LOGGER.info("Entered listPageByChampionship method -ChampionshipRoundsService");

		Sort sort = null;

		if (sortField.equals("noOfRounds")) {
			sortField = "no_of_rounds";
		} else if (sortField.equals("ageCategory")) {
			sortField = "age_category_id";
		} else if (sortField.equals("asanaCategory")) {
			sortField = "asana_category_id";
		} else if (sortField.equals("gender")) {
			sortField = "gender";
		}

		sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		if (asanaCate != "" && ageCate != "") {
			LOGGER.info(
					"findAllByChampionshipIdAndAsanaCategoryNameContainingAndCategoryTitleContaining method called -ChampionshipRoundsService");
			return repo.findAllByChampionshipIdAndAsanaCategoryNameContainingAndCategoryTitleContaining(championship,
					asanaCate, ageCate, pageable);
		} else if (asanaCate != "" && ageCate == "") {
			LOGGER.info("findAllByChampionshipIdAndAsanaCategoryNameContaining method called -ChampionshipRoundsService");
			return repo.findAllByChampionshipIdAndAsanaCategoryNameContaining(championship, asanaCate, pageable);
		} else if (asanaCate == "" && ageCate != "") {
			LOGGER.info("findAllByChampionshipIdAndCategoryTitleContaining method called -ChampionshipRoundsService");
			return repo.findAllByChampionshipIdAndCategoryTitleContaining(championship, ageCate, pageable);
		} else {
			LOGGER.info("findAllByChampionshipId method called -ChampionshipRoundsService");
			LOGGER.info("Exit listPageByChampionship method -ChampionshipRoundsService");
        return repo.findAllByChampionshipId(championship, pageable);
		}

	}
}
