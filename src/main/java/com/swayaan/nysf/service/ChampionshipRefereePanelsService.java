package com.swayaan.nysf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ChampionshipRefereePanelsEnum;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.GenderEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamPanel;
import com.swayaan.nysf.entity.RefereesPanels;
import com.swayaan.nysf.entity.StatusEnum;
import com.swayaan.nysf.entity.User;

import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.exception.ChampionshipRefereePanelsNotFoundException;
import com.swayaan.nysf.repository.ChampionshipRefereePanelsRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ChampionshipRefereePanelsService {

	public static final int RECORDS_PER_PAGE = 10;
	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipRefereePanelsService.class);
	@Autowired
	ChampionshipRefereePanelsRepository repo;
	@Autowired
	ParticipantTeamPanelService participantTeamPanelService;
	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;
	@Autowired
	ParticipantTeamService participantTeamService;
	@Autowired
	RefereePanelsService refereePanelService;

	private static final int TRADITIONAL_ASANA_CATEGORY_ID = 1;
	private static final int ARTISTIC_SINGLE_ASANA_CATEGORY_ID = 2;
	private static final int ARTISTIC_PAIR_ASANA_CATEGORY_ID = 3;
	private static final int RHYTHMIC_PAIR_ASANA_CATEGORY_ID = 4;
	private static final int ARTISTIC_GROUP_ASANA_CATEGORY_ID = 5;

	private static final int TRADITIONAL_CATEGORY_JUDGE_COUNT = 9;
	private static final int ARTISTIC_SINGLE_CATEGORY_JUDGE_COUNT = 11;
	private static final int ARTISTIC_PAIR_CATEGORY_JUDGE_COUNT = 12;
	private static final int RHYTHMIC_PAIR_CATEGORY_JUDGE_COUNT = 11;
	private static final int ARTISTIC_GROUP_CATEGORY_JUDGE_COUNT = 11;

	private static final int CHIEF_JUDGE_TYPE_ID = 1;
	private static final int D_JUDGE_TYPE_ID = 2;
	private static final int A_JUDGE_TYPE_ID = 3;
	private static final int T_JUDGE_TYPE_ID = 4;
	private static final int E_JUDGE_TYPE_ID = 5;
	private static final int SCORER_TYPE_ID = 6;

	private static final int TRAD_CHIEF_JUDGE_COUNT = 1;
	private static final int TRAD_D_JUDGE_COUNT = 5;
	private static final int TRAD_A_JUDGE_COUNT = 0;
	private static final int TRAD_T_JUDGE_COUNT = 1;
	private static final int TRAD_E_JUDGE_COUNT = 1;
	private static final int TRAD_SCORER_JUDGE_COUNT = 1;

	private static final int ARTISTIC_CHIEF_JUDGE_COUNT = 1;
	private static final int ARTISTIC_D_JUDGE_COUNT = 4;
	private static final int ARTISTIC_A_JUDGE_COUNT = 2;
	private static final int ARTISTIC_T_JUDGE_COUNT = 2;
	private static final int ARTISTIC_E_JUDGE_COUNT = 1;
	private static final int ARTISTIC_SCORER_JUDGE_COUNT = 1;

	private static final int APAIR_CHIEF_JUDGE_COUNT = 1;
	private static final int APAIR_D_JUDGE_COUNT = 4;
	private static final int APAIR_A_JUDGE_COUNT = 2;
	private static final int APAIR_T_JUDGE_COUNT = 2;
	private static final int APAIR_E_JUDGE_COUNT = 2;
	private static final int APAIR_SCORER_JUDGE_COUNT = 1;

	public List<ChampionshipRefereePanels> listAllChampionshipRefereePanels() {
		LOGGER.info("Entered listAllChampionshipRefereePanels method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit listAllChampionshipRefereePanels method -ChampionshipRefereePanelsService");
		return repo.findAllByOrderByChampionshipDesc();
	}

	public ChampionshipRefereePanels save(ChampionshipRefereePanels championshipRefereePanel) throws Exception {
		LOGGER.info("Entered save method -ChampionshipRefereePanelsService");
		User currentUser = CommonUtil.getCurrentUser();
		boolean isUpdatingPanel = (championshipRefereePanel.getId() != null);
		boolean isAddFlowChampionshipRefereePanelName = repo.existsByNameAndChampionship(
				championshipRefereePanel.getName(), championshipRefereePanel.getChampionship());
		boolean isAddFlowChampionshipAndAsanaCategoryAndRound = repo
				.existsByChampionshipAndAsanaCategoryAndCategoryAndGenderAndRoundNumber(
						championshipRefereePanel.getChampionship(), championshipRefereePanel.getAsanaCategory(),
						championshipRefereePanel.getCategory(), championshipRefereePanel.getGender(),
						championshipRefereePanel.getRoundNumber());

		if (isUpdatingPanel) {
			if (checkIfEditAllowed(championshipRefereePanel)) {
				boolean isEditFlowChampionshipRefereePanelName = repo.existsByNameAndChampionshipAndIdNot(
						championshipRefereePanel.getName(), championshipRefereePanel.getChampionship(),
						championshipRefereePanel.getId());
				if (isEditFlowChampionshipRefereePanelName) {
					throw new Exception("Panel name " + championshipRefereePanel.getName() + " already exist");
				}
				ChampionshipRefereePanels existingPanel = repo.findById(championshipRefereePanel.getId()).get();
				championshipRefereePanel.setLastModifiedBy(currentUser);
				championshipRefereePanel.setLastModifiedTime(new Date());
				championshipRefereePanel.setCreatedBy(existingPanel.getCreatedBy());
				championshipRefereePanel.setCreatedTime(existingPanel.getCreatedTime());
				championshipRefereePanel.setStatus(existingPanel.getStatus());
			} else {
				throw new Exception("Unable to Edit Championship Referee Panel");
			}

		} else {
			if (!(championshipRefereePanel.getChampionship().getStatus().equals(ChampionshipStatusEnum.SCHEDULED)
					|| championshipRefereePanel.getChampionship().getStatus().equals(ChampionshipStatusEnum.ONGOING))) {
				throw new Exception("Unable to create panel for this championship");

			}
			if (isAddFlowChampionshipRefereePanelName) {
				throw new Exception("Panel name " + championshipRefereePanel.getName() + " already exist");
			}
			if (isAddFlowChampionshipAndAsanaCategoryAndRound) {
				throw new Exception(
						"Panel " + championshipRefereePanel.getName() + " with these categories already exist");
			}
			championshipRefereePanel.setCreatedBy(currentUser);
			championshipRefereePanel.setCreatedTime(new Date());
			championshipRefereePanel.setStatus(ChampionshipRefereePanelsEnum.INCOMPLETE);
		}
		ChampionshipRefereePanels savedChampionshipRefereePanels = repo.save(championshipRefereePanel);
		if (isUpdatingPanel) {
			// edit in panels assigned to the teams
			modifyParticipantTeamPanels(savedChampionshipRefereePanels);
		}
		LOGGER.info("Exit save method -ChampionshipRefereePanelsService");
		return savedChampionshipRefereePanels;

	}

	public ChampionshipRefereePanels get(Integer id) throws ChampionshipRefereePanelsNotFoundException {
		LOGGER.info("Entered get method -ChampionshipRefereePanelsService");
		try {
			LOGGER.info("Exit get method -ChampionshipRefereePanelsService");
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ChampionshipRefereePanelsNotFoundException("Could not find any panel with ID " + id);
		}
	}

	public void delete(Integer id) throws ChampionshipRefereePanelsNotFoundException {
		LOGGER.info("Entered delete method -ChampionshipRefereePanelsService");
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ChampionshipRefereePanelsNotFoundException("Could not find any panel with ID " + id);
		}
		LOGGER.info("Exit delete method -ChampionshipRefereePanelsService");
		repo.deleteById(id);
	}

	public ChampionshipRefereePanels findById(Integer participantGroup_id) {
		LOGGER.info("Entered findById method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit findById method -ChampionshipRefereePanelsService");
		return repo.findById(participantGroup_id).get();
	}

	public List<ChampionshipRefereePanels> getRefereePanelsForChampionship(Championship championship,
			AsanaCategory asanaCategory) {
		LOGGER.info("Entered getRefereePanelsForChampionship method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit getRefereePanelsForChampionship method -ChampionshipRefereePanelsService");
		return repo.findByChampionshipAndAsanaCategory(championship, asanaCategory);
	}

	public List<ChampionshipRefereePanels> listAllChampionshipRefereePanel(Championship championship) {
		LOGGER.info("Entered listAllChampionshipRefereePanel method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit listAllChampionshipRefereePanel method -ChampionshipRefereePanelsService");
		return repo.findByChampionship(championship);
	}

	public List<ChampionshipRefereePanels> listAllChampionshipRefereePanelsByAsanaCategory(
			AsanaCategory asanaCategory) {
		LOGGER.info("Entered listAllChampionshipRefereePanelsByAsanaCategory method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit listAllChampionshipRefereePanelsByAsanaCategory method -ChampionshipRefereePanelsService");
		return repo.findByAsanaCategory(asanaCategory);
	}

	public List<ChampionshipRefereePanels> listAllChampionshipRefereePanelsByNotDeleted() {
		LOGGER.info("Entered listAllChampionshipRefereePanelsByNotDeleted method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit listAllChampionshipRefereePanelsByNotDeleted method -ChampionshipRefereePanelsService");
		ChampionshipStatusEnum status = ChampionshipStatusEnum.DELETED;
		return repo.findAllByChampionshipStatusNot(status);
	}

	public List<ChampionshipRefereePanels> getRefereePanelsForChampionshipAndAsanaCategoryAndAgeAndGenderAndRound(
			Championship championship, AsanaCategory asanaCategory, AgeCategory ageCategory, GenderEnum genderEnum,
			Integer round) {
		LOGGER.info(
				"Entered getRefereePanelsForChampionshipAndAsanaCategoryAndAgeAndGenderAndRound method -ChampionshipRefereePanelsService");
		LOGGER.info(
				"Exit getRefereePanelsForChampionshipAndAsanaCategoryAndAgeAndGenderAndRound method -ChampionshipRefereePanelsService");
		return repo.findByChampionshipAndAsanaCategoryAndCategoryAndGenderAndRoundNumber(championship, asanaCategory,
				ageCategory, genderEnum, round);
	}

	public boolean checkIfEditAllowed(ChampionshipRefereePanels championshipRefereePanel) {
		LOGGER.info("Entered checkIfEditAllowed method -ChampionshipRefereePanelsService");
		// check if the panel is assigned to any team
		List<ParticipantTeamPanel> listParticipantTeamPanel = participantTeamPanelService
				.findBychampionshipRefereePanelsId(championshipRefereePanel.getId());
		if (listParticipantTeamPanel.isEmpty()) {
			return true;
		} else {

			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<>();
			for (ParticipantTeamPanel participantTeamId : listParticipantTeamPanel) {
				ParticipantTeam participantTeam = participantTeamService
						.findById(participantTeamId.getParticipantTeamId());
				ChampionshipParticipantTeams championshipParticipantTeam = championshipParticipantTeamsService
						.findByParticipantTeamAndRound(participantTeam, participantTeamId.getRoundNumber());
				listChampionshipParticipantTeams.add(championshipParticipantTeam);
			}
			if (!listChampionshipParticipantTeams.isEmpty()) {
//				List<ChampionshipParticipantTeams> listFilteredChampionshipParticipantTeams = listChampionshipParticipantTeams
//						.stream().filter(c -> c.getStatus().equals(StatusEnum.SCHEDULED)).collect(Collectors.toList());
//				if (listChampionshipParticipantTeams.size() != listFilteredChampionshipParticipantTeams.size()) {
				return false;
//				}
			}

		}
		LOGGER.info("Exit checkIfEditAllowed method -ChampionshipRefereePanelsService");
		return true;
	}

	public boolean checkIfEditRefereeAllowed(ChampionshipRefereePanels championshipRefereePanel) {
		LOGGER.info("Entered checkIfEditRefereeAllowed method -ChampionshipRefereePanelsService");

		// check if the panel is assigned to any team
		List<ParticipantTeamPanel> listParticipantTeamPanel = participantTeamPanelService
				.findBychampionshipRefereePanelsId(championshipRefereePanel.getId());
		if (listParticipantTeamPanel.isEmpty()) {
			return true;
		} else {

			List<ChampionshipParticipantTeams> listChampionshipParticipantTeams = new ArrayList<>();
			for (ParticipantTeamPanel participantTeamId : listParticipantTeamPanel) {
				ParticipantTeam participantTeam = participantTeamService
						.findById(participantTeamId.getParticipantTeamId());
				ChampionshipParticipantTeams championshipParticipantTeam = championshipParticipantTeamsService
						.findByParticipantTeamAndRound(participantTeam, participantTeamId.getRoundNumber());
				listChampionshipParticipantTeams.add(championshipParticipantTeam);
			}
			if (!listChampionshipParticipantTeams.isEmpty()) {
				List<ChampionshipParticipantTeams> listFilteredChampionshipParticipantTeams = listChampionshipParticipantTeams
						.stream().filter(c -> c.getStatus().equals(StatusEnum.SCHEDULED)).collect(Collectors.toList());
				if (listChampionshipParticipantTeams.size() != listFilteredChampionshipParticipantTeams.size()) {
					return false;
				}
			}

		}
		LOGGER.info("Exit checkIfEditRefereeAllowed method -ChampionshipRefereePanelsService");
		return true;
	}

	public void modifyParticipantTeamPanels(ChampionshipRefereePanels championshipRefereePanels) {
		LOGGER.info("Entered modifyParticipantTeamPanels method -ChampionshipRefereePanelsService");
		LOGGER.info("Exit modifyParticipantTeamPanels method -ChampionshipRefereePanelsService");
		List<ParticipantTeamPanel> listParticipantTeamPanel = participantTeamPanelService
				.findBychampionshipRefereePanelsId(championshipRefereePanels.getId());
		if (!listParticipantTeamPanel.isEmpty()) {
			for (ParticipantTeamPanel participantTeamPanel : listParticipantTeamPanel) {
				ParticipantTeam participantTeam = participantTeamService
						.findById(participantTeamPanel.getParticipantTeamId());
				participantTeamService.modifyRefereesForParticipantTeam(participantTeam,
						participantTeamPanel.getRoundNumber(), championshipRefereePanels);

//				participantTeamPanel.setChampionshipRefereePanelsId(championshipRefereePanels.getId());
//				participantTeamPanelService.save(participantTeamPanel);			
			}

		}
	}

	public Page<ChampionshipRefereePanels> listByPage(int pageNum, String sortField, String sortDir, String name,
			String championshipName, String asanaCategory) {
		LOGGER.info("Entered listByPage method -ChampionshipRefereePanelsService");

		Sort sort = null;

		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);

		// default filter - championshipStatusNotIn
		List<ChampionshipStatusEnum> status = new ArrayList<>();
		status.add(ChampionshipStatusEnum.REJECTED);
		status.add(ChampionshipStatusEnum.DELETED);

		if (name != "" && championshipName != "" && asanaCategory != "") {
			// 000 name championship asanaCategory
			return repo
					.findAllByNameContainingAndChampionshipNameContainingAndAsanaCategoryNameContainingAndStatusNotIn(
							name, championshipName, asanaCategory, status, pageable);

		} else if (name != "" && championshipName != "" && asanaCategory == "") {
			// name championship 001
			return repo.findAllByNameContainingAndChampionshipNameContainingAndStatusNotIn(name, championshipName,
					status, pageable);
		} else if (name != "" && championshipName == "" && asanaCategory != "") {
			// name championship 010
			return repo.findAllByNameContainingAndAsanaCategoryNameContainingAndStatusNotIn(name, asanaCategory, status,
					pageable);
		} else if (name != "" && championshipName == "" && asanaCategory == "") {
			// name championship 011
			return repo.findAllByNameContainingAndStatusNotIn(name, status, pageable);
		} else if (name == "" && championshipName != "" && asanaCategory != "") {
			// name championship 100
			return repo.findAllByChampionshipNameContainingAndAsanaCategoryNameContainingAndStatusNotIn(
					championshipName, asanaCategory, status, pageable);
		} else if (name == "" && championshipName != "" && asanaCategory == "") {
			// name championship 101
			return repo.findAllByChampionshipNameContainingAndAndStatusNotIn(championshipName, status, pageable);
		} else if (name == "" && championshipName == "" && asanaCategory != "") {
			// name championship 110
			return repo.findAllByAsanaCategoryNameContainingAndStatusNotIn(asanaCategory, status, pageable);
		}

		else {
			LOGGER.info("findAllByChampionshipStatusNotIn method called -ChampionshipRefereePanelsService");
			LOGGER.info("Exit listByPage method -ChampionshipRefereePanelsService");
			return repo.findAllByChampionshipStatusNotIn(status, pageable);
		}
	}

	// public Page<ChampionshipRefereePanels>
	// listByChampionshipRefereePanelsPage(int pageNum, String sortField,
	// String sortDir, String name, String asanaCategory,String championshipName) {
	// LOGGER.info("Exit listByChampionshipRefereePanelsPage method
	// -ChampionshipRefereePanelsService");

	// Sort sort = null;

//
////		if(sortField.equals("name")) {
////			sortField="firstName";
////		}
//		sort = Sort.by(sortField);
//		
//
//		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//
//		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
//		if (name != "" && asanaCategory != "") {
//			LOGGER.info("findAllByNameContainingAndAsanaCategoryNameContaining method called");
//			return repo.findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameContaining(name, asanaCategory,championshipName, pageable);
//		} else if (name == "" && asanaCategory != "") {
//			LOGGER.info("findAllByAsanaCategoryNameContaining method called");
//			return repo.findAllByAsanaCategoryNameContainingAndChampionshipNameContaining(asanaCategory,championshipName, pageable);
//		} else if (name != "" && asanaCategory == "") {
//			LOGGER.info("findAllByNameContaining method called");
//			return repo.findAllByNameContainingAndChampionshipNameContaining(name,championshipName, pageable);
//		} else {
//			LOGGER.info("findAll method called");
//			return repo.findAllByChampionshipNameContaining(championshipName,pageable);
//		}
//
//	}

	public Page<ChampionshipRefereePanels> listByChampionshipRefereePanelsPage(Integer championshipId, int pageNum,
			String sortField, String sortDir, String name, String asanaCategory, String championshipName) {
		LOGGER.info("Entered listByChampionshipRefereePanelsPage method -ChampionshipRefereePanelsService");

		Sort sort = null;
		sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, RECORDS_PER_PAGE, sort);
		if (name != "" && asanaCategory != "") {
			LOGGER.info(
					"findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameContainingAndChampionshipId method called -ChampionshipRefereePanelsService");
			return repo
					.findAllByNameContainingAndAsanaCategoryNameContainingAndChampionshipNameContainingAndChampionshipId(
							name, asanaCategory, championshipName, championshipId, pageable);
		} else if (name == "" && asanaCategory != "") {
			LOGGER.info(
					"findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndChampionshipId method called -ChampionshipRefereePanelsService");
			return repo.findAllByAsanaCategoryNameContainingAndChampionshipNameContainingAndChampionshipId(
					asanaCategory, championshipName, championshipId, pageable);
		} else if (name != "" && asanaCategory == "") {
			LOGGER.info(
					"findAllByNameContainingAndChampionshipNameContainingAndChampionshipId method called -ChampionshipRefereePanelsService");
			return repo.findAllByNameContainingAndChampionshipNameContainingAndChampionshipId(name, championshipName,
					championshipId, pageable);
		} else {
			LOGGER.info(
					"findAllByChampionshipNameContainingAndChampionshipId method called -ChampionshipRefereePanelsService");
			LOGGER.info("Exit listByChampionshipRefereePanelsPage method -ChampionshipRefereePanelsService");
			return repo.findAllByChampionshipNameContainingAndChampionshipId(championshipName, championshipId,
					pageable);
		}
	}

	public void validateJudgePanel(ChampionshipRefereePanels championshipRefereePanels) {

		AsanaCategory asanaCategory = championshipRefereePanels.getAsanaCategory();
		Integer currentJudgeCount = refereePanelService.listAllRefereePanels(championshipRefereePanels).size();
		List<RefereesPanels> listRefereePanels = refereePanelService.listAllRefereePanels(championshipRefereePanels);
		if (!listRefereePanels.isEmpty()) {
			Integer ChiefJudgeCount = 0;
			Integer DJudgeCount = 0;
			Integer AJudgeCount = 0;
			Integer TJudgeCount = 0;
			Integer EJudgeCount = 0;
			Integer ScorerJudgeCount = 0;
			for (RefereesPanels refereePanels : listRefereePanels) {
				if (refereePanels.getType().getId() == CHIEF_JUDGE_TYPE_ID) {
					ChiefJudgeCount++;
				}
				if (refereePanels.getType().getId() == D_JUDGE_TYPE_ID) {
					DJudgeCount++;
				}
				if (refereePanels.getType().getId() == A_JUDGE_TYPE_ID) {
					AJudgeCount++;
				}
				if (refereePanels.getType().getId() == T_JUDGE_TYPE_ID) {
					TJudgeCount++;
				}
				if (refereePanels.getType().getId() == E_JUDGE_TYPE_ID) {
					EJudgeCount++;
				}
				if (refereePanels.getType().getId() == SCORER_TYPE_ID) {
					ScorerJudgeCount++;
				}

			}

			if (asanaCategory.getId() == TRADITIONAL_ASANA_CATEGORY_ID
					&& currentJudgeCount == TRADITIONAL_CATEGORY_JUDGE_COUNT) {
				if (ChiefJudgeCount == TRAD_CHIEF_JUDGE_COUNT && DJudgeCount == TRAD_D_JUDGE_COUNT
						&& AJudgeCount == TRAD_A_JUDGE_COUNT && TJudgeCount == TRAD_T_JUDGE_COUNT
						&& EJudgeCount == TRAD_E_JUDGE_COUNT && ScorerJudgeCount == TRAD_SCORER_JUDGE_COUNT) {
					championshipRefereePanels.setStatus(ChampionshipRefereePanelsEnum.COMPLETE);
				}
			} else if ((asanaCategory.getId() == ARTISTIC_SINGLE_ASANA_CATEGORY_ID
					&& currentJudgeCount == ARTISTIC_SINGLE_CATEGORY_JUDGE_COUNT)
					|| (asanaCategory.getId() == RHYTHMIC_PAIR_ASANA_CATEGORY_ID
							&& currentJudgeCount == RHYTHMIC_PAIR_CATEGORY_JUDGE_COUNT)
					|| (asanaCategory.getId() == ARTISTIC_GROUP_ASANA_CATEGORY_ID
							&& currentJudgeCount == ARTISTIC_GROUP_CATEGORY_JUDGE_COUNT)) {
				if (ChiefJudgeCount == ARTISTIC_CHIEF_JUDGE_COUNT && DJudgeCount == ARTISTIC_D_JUDGE_COUNT
						&& AJudgeCount == ARTISTIC_A_JUDGE_COUNT && TJudgeCount == ARTISTIC_T_JUDGE_COUNT
						&& EJudgeCount == ARTISTIC_E_JUDGE_COUNT && ScorerJudgeCount == ARTISTIC_SCORER_JUDGE_COUNT) {
					championshipRefereePanels.setStatus(ChampionshipRefereePanelsEnum.COMPLETE);
				}
			} else if (asanaCategory.getId() == ARTISTIC_PAIR_ASANA_CATEGORY_ID
					&& currentJudgeCount == ARTISTIC_PAIR_CATEGORY_JUDGE_COUNT) {
				if (ChiefJudgeCount == APAIR_CHIEF_JUDGE_COUNT && DJudgeCount == APAIR_D_JUDGE_COUNT
						&& AJudgeCount == APAIR_A_JUDGE_COUNT && TJudgeCount == APAIR_T_JUDGE_COUNT
						&& EJudgeCount == APAIR_E_JUDGE_COUNT && ScorerJudgeCount == APAIR_SCORER_JUDGE_COUNT) {
					championshipRefereePanels.setStatus(ChampionshipRefereePanelsEnum.COMPLETE);
				}
			}else {
				championshipRefereePanels.setStatus(ChampionshipRefereePanelsEnum.INCOMPLETE);
			}
		}
		try {
			repo.save(championshipRefereePanels);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
