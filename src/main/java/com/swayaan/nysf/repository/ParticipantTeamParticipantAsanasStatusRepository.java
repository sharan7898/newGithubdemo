package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ParticipantTeam;
import com.swayaan.nysf.entity.ParticipantTeamParticipantAsanasStatus;

@Repository
public interface ParticipantTeamParticipantAsanasStatusRepository extends CrudRepository<ParticipantTeamParticipantAsanasStatus, Integer> {

	public ParticipantTeamParticipantAsanasStatus findByParticipantTeamAndRound(ParticipantTeam participantTeam, Integer roundNumber);

	public List<ParticipantTeamParticipantAsanasStatus> findByParticipantTeam(ParticipantTeam participantTeam);

	
}
