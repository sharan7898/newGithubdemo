package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.ParticipantTeamPanel;

@Repository
public interface ParticipantTeamPanelRepository extends CrudRepository<ParticipantTeamPanel, Integer>{

	ParticipantTeamPanel findByParticipantTeamIdAndRoundNumber(Integer participantTeamId, Integer round);

	List<ParticipantTeamPanel> findAllBychampionshipRefereePanelsId(Integer championshipRefereePanelId);

}
