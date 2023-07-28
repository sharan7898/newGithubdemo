package com.swayaan.nysf.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipRounds;
import com.swayaan.nysf.entity.RoundStatusEnum;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.repository.ChampionshipRoundsRepository;

@Service
public class ChampionshipRoundsService {

	@Autowired
	ChampionshipRoundsRepository repo;

	@Autowired
	ChampionshipParticipantTeamsService championshipParticipantTeamsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChampionshipRoundsService.class);

	public void saveChampionshipRoundsForChampionshipCategory(Championship championship) {
		LOGGER.info("Entered saveChampionshipRoundsForChampionshipCategory method -ChampionshipRoundsService");

		if (!championship.getChampionshipCategory().isEmpty()) {

			List<ChampionshipCategory> listChampionshipCategory = championship.getChampionshipCategory();
			for (ChampionshipCategory championshipCategory : listChampionshipCategory) {
				for (int round = 1; round <= championshipCategory.getNoOfRounds(); round++) {

					String status = RoundStatusEnum.SCHEDULED.toString();

					ChampionshipRounds championshipRounds = repo
							.findAllByRoundAndStatusAndChampionshipIdAndChapionshipCategoryId(round, status,
									championship.getId(), championshipCategory.getId());
					if (championshipRounds == null) {
						ChampionshipRounds championshipRoundsNew = new ChampionshipRounds();
						championshipRoundsNew.setChampionship(championship);
						championshipRoundsNew.setRound(round);
						championshipRoundsNew.setStatus(RoundStatusEnum.valueOf(status));
						championshipRoundsNew.setChampionshipCategory(championshipCategory);
						LOGGER.info("save championshipRoundsNew");
						LOGGER.info("Exit saveChampionshipRoundsForChampionshipCategory method -ChampionshipRoundsService");
                    repo.save(championshipRoundsNew);
					}
				}
			}

		}
	}

	public void delete(Integer championshipId, Integer championshipCategoryId) throws Exception {
		LOGGER.info("Entered delete method -ChampionshipRoundsService");

		Integer round = 1;
		List<ChampionshipRounds> listChampionshipRounds = repo
				.findAllByChampionshipIdAndChapionshipCategoryId(championshipId, championshipCategoryId);
		if (!listChampionshipRounds.isEmpty()) {
			for (ChampionshipRounds championshipRound : listChampionshipRounds) {
				// check if it is assigned to championship_partisipants_teams
				if (championshipParticipantTeamsService.findAllByChampionshipRounds(championshipRound)) {
					throw new Exception("Cant delete, particpant teams are assigned to this category");
				}

				if (championshipRound.getStatus() == RoundStatusEnum.SCHEDULED) {
					LOGGER.info("Exit delete method -ChampionshipRoundsService");
                  	repo.delete(championshipRound);
				} else {
					throw new Exception("Cannot be deleted. Championship is" + championshipRound.getStatus());
				}
			}
		}
	}

	public ChampionshipRounds getByRoundAndChampionshipAndChampionshipCategory(Integer round, Championship championship,
			ChampionshipCategory championshipCategory) {
		LOGGER.info("Entered getByRoundAndChampionshipAndChampionshipCategory method -ChampionshipRoundsService");
		LOGGER.info("Exit getByRoundAndChampionshipAndChampionshipCategory method -ChampionshipRoundsService");
   return repo.findAllByRoundAndChampionshipIdAndChapionshipCategoryId(round, championship.getId(),
				championshipCategory.getId());
	}

	public ChampionshipRounds save(ChampionshipRounds championshipRound) {
		LOGGER.info("Entered save method -ChampionshipRoundsService");
		LOGGER.info("Exit save method -ChampionshipRoundsService");
	return repo.save(championshipRound);

	}

	public void deleteById(Integer id) {
		LOGGER.info("Entered deleteById method -ChampionshipRoundsService");
		LOGGER.info("Exit deleteById method -ChampionshipRoundsService");
	repo.deleteById(id);
	}

	public ChampionshipRounds get(Integer id) throws EntityNotFoundException {
		LOGGER.info("Entered get method -ChampionshipRoundsService");
	try {
		LOGGER.info("Exit get method -ChampionshipRoundsService");
     return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new EntityNotFoundException("Could not find any championship rounds with ID " + id);
		}
	}

	public ChampionshipRounds findByChampionshipCategoryAndRound(ChampionshipCategory championshipCategory,
			Integer round) {
		LOGGER.info("Entered findByChampionshipCategoryAndRound method -ChampionshipRoundsService");
		LOGGER.info("Exit findByChampionshipCategoryAndRound method -ChampionshipRoundsService");
     return repo.findByChampionshipCategoryAndRound(championshipCategory, round);
	}

	public ChampionshipRounds getByRoundAndChampionshipAndChampionshipCategoryAndStatus(Integer round,
			Championship championship, ChampionshipCategory championshipCategory, RoundStatusEnum status) {
		LOGGER.info("Entered getByRoundAndChampionshipAndChampionshipCategoryAndStatus method -ChampionshipRoundsService");
     ChampionshipRounds championshipRounds = repo.findAllByRoundAndChampionshipIdAndChapionshipCategoryId(round,
				championship.getId(), championshipCategory.getId(), status.toString());
		LOGGER.info("Exit getByRoundAndChampionshipAndChampionshipCategoryAndStatus method -ChampionshipRoundsService");
     return championshipRounds;
	}

	public void updateStatus(Integer championshipRoundsId, RoundStatusEnum roundStatus) {
		LOGGER.info("Entered updateStatus method -ChampionshipRoundsService");
     ChampionshipRounds championshipRounds = repo.findById(championshipRoundsId).get();
		if (championshipRounds.getStatus().equals(RoundStatusEnum.SCHEDULED)) {
			championshipRounds.setStatus(roundStatus);
		}
		LOGGER.info("Exit updateStatus method -ChampionshipRoundsService");
     repo.save(championshipRounds);
	}

	public ChampionshipRounds findByChampionshipAndChampionshipCatgeoryAndRound(Championship championship,
			ChampionshipCategory championshipCategory, Integer round) {
		LOGGER.info("Entered findByChampionshipAndChampionshipCatgeoryAndRound method -ChampionshipRoundsService");
		LOGGER.info("Exit findByChampionshipAndChampionshipCatgeoryAndRound method -ChampionshipRoundsService");
      return repo.findByChampionshipAndChampionshipCategoryAndRound(championship, championshipCategory, round);
	}

	public void deleteByChampionship(Championship championship) throws Exception {
		LOGGER.info("Entered deleteByChampionship method -ChampionshipRoundsService");
	List<ChampionshipRounds> listChampionshipRounds = repo.findAllByChampionship(championship);
		for (ChampionshipRounds championshipRounds : listChampionshipRounds) {
			RoundStatusEnum status = championshipRounds.getStatus();
			if (status.equals(RoundStatusEnum.ONGOING)) {
				throw new Exception("Cannot delete Championship. Championship is ongoing");
			} else {
				// cascade all dependencies & delete
				LOGGER.info("Exit deleteByChampionship method -ChampionshipRoundsService");
				repo.delete(championshipRounds);
			}

		}

	}

	public List<ChampionshipRounds> findAllByChampionship(Championship championship) {
		LOGGER.info("Entered findAllByChampionship method -ChampionshipRoundsService");
		LOGGER.info("Exit findAllByChampionship method -ChampionshipRoundsService");
	return repo.findAllByChampionship(championship);
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -ChampionshipRoundsService");
		LOGGER.info("Exit delete method -ChampionshipRoundsService");
	repo.deleteById(id);

	}

	public List<ChampionshipRounds> findByChampionshipAndChampionshipCategory(Championship championship,
			ChampionshipCategory championshipCategory) {
		LOGGER.info("Entered findByChampionshipAndChampionshipCategory method -ChampionshipRoundsService");
		LOGGER.info("Exit findByChampionshipAndChampionshipCategory method -ChampionshipRoundsService");
	return repo.findAllByChampionshipAndChampionshipCategory(championship, championshipCategory);
	}

}
