package com.swayaan.nysf.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swayaan.nysf.entity.AsanaStatusEnum;
import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamAsanas;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.repository.ParticipantTeamParticipantAsanasStatusRepository;
import com.swayaan.nysf.utils.CommonUtil;

@Service
public class ParticipantTeamParticipantAsanasStatusService {

	@Autowired
	ParticipantTeamParticipantAsanasStatusRepository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantTeamParticipantAsanasStatusService.class);

	public ParticipantTeamParticipantAsanasStatus findByParticipantTeamAndRound(ParticipantTeam participantTeam,
			Integer roundNumber) {
		LOGGER.info("Entered findByParticipantTeamAndRound method -ParticipantTeamParticipantAsanasStatusService");
		LOGGER.info("Exit findByParticipantTeamAndRound method -ParticipantTeamParticipantAsanasStatusService");
		return repo.findByParticipantTeamAndRound(participantTeam, roundNumber);

	}

	public ParticipantTeamParticipantAsanasStatus save(
			ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus) {
		LOGGER.info("Entered save method -ParticipantTeamParticipantAsanasStatusService");
		LOGGER.info("Exit save method -ParticipantTeamParticipantAsanasStatusService");
		return repo.save(participantTeamParticipantAsanasStatus);

	}

	public void saveDetailsToParticipantTeamParticipantAsanasStatus(ParticipantTeamAsanas participantTeamAsanas) {
		User currentUser = CommonUtil.getCurrentUser();
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = findByParticipantTeamAndRound(
				participantTeamAsanas.getParticipantTeam(), participantTeamAsanas.getRoundNumber());
		if (existingTeamAsanasStatus == null) {
			ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = new ParticipantTeamParticipantAsanasStatus();
			participantTeamParticipantAsanasStatus.setAsanaStatus(AsanaStatusEnum.DRAFTS);
			participantTeamParticipantAsanasStatus.setCreatedBy(currentUser);
			participantTeamParticipantAsanasStatus.setCreatedTime(new Date());
			participantTeamParticipantAsanasStatus.setParticipantTeam(participantTeamAsanas.getParticipantTeam());
			participantTeamParticipantAsanasStatus.setRound(participantTeamAsanas.getRoundNumber());
			save(participantTeamParticipantAsanasStatus);
		} else {
			existingTeamAsanasStatus.setAsanaStatus(AsanaStatusEnum.DRAFTS);
			existingTeamAsanasStatus.setLastModifiedBy(currentUser);
			existingTeamAsanasStatus.setLastModifiedTime(new Date());
			save(existingTeamAsanasStatus);
		}

	}

	public void saveDetailsByParticipantTeam(ParticipantTeam participantTeam, Integer round) {
		User currentUser = CommonUtil.getCurrentUser();
		ParticipantTeamParticipantAsanasStatus existingTeamAsanasStatus = findByParticipantTeamAndRound(participantTeam,
				round);
		if (existingTeamAsanasStatus == null) {
			ParticipantTeamParticipantAsanasStatus participantTeamParticipantAsanasStatus = new ParticipantTeamParticipantAsanasStatus();
			participantTeamParticipantAsanasStatus.setAsanaStatus(AsanaStatusEnum.INCOMPLETE);
			participantTeamParticipantAsanasStatus.setCreatedBy(currentUser);
			participantTeamParticipantAsanasStatus.setCreatedTime(new Date());
			participantTeamParticipantAsanasStatus.setParticipantTeam(participantTeam);
			participantTeamParticipantAsanasStatus.setRound(round);
			save(participantTeamParticipantAsanasStatus);
		} else {
			existingTeamAsanasStatus.setAsanaStatus(AsanaStatusEnum.INCOMPLETE);
			existingTeamAsanasStatus.setLastModifiedBy(currentUser);
			existingTeamAsanasStatus.setLastModifiedTime(new Date());
			save(existingTeamAsanasStatus);
		}

	}

	public List<ParticipantTeamParticipantAsanasStatus> findAllByParticipantTeam(ParticipantTeam participantTeam) {
		LOGGER.info("Entered findAllByParticipantTeam method -ParticipantTeamParticipantAsanasStatusService");
		LOGGER.info("Exit findAllByParticipantTeam method -ParticipantTeamParticipantAsanasStatusService");
		return repo.findByParticipantTeam(participantTeam);
	}

	public void delete(Integer id) {
		LOGGER.info("Entered delete method -ParticipantTeamParticipantAsanasStatusService");
		LOGGER.info("Exit delete method -ParticipantTeamParticipantAsanasStatusService");
		repo.deleteById(id);
	}

	public ParticipantTeamParticipantAsanasStatus getById(Integer id) {
		LOGGER.info("Entered getById method -ParticipantTeamParticipantAsanasStatusService");
		LOGGER.info("Exit getById method -ParticipantTeamParticipantAsanasStatusService");
		return repo.findById(id).get();
	}

}
