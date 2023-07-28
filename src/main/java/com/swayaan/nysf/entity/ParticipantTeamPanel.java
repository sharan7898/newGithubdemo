package com.swayaan.nysf.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "participant_teams_panel")
public class ParticipantTeamPanel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer roundNumber;
	
	private Integer participantTeamId;
	
	private Integer championshipRefereePanelsId;

	public ParticipantTeamPanel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ParticipantTeamPanel(Integer roundNumber, Integer participantTeamId, Integer championshipRefereePanelsId) {
		super();
		this.roundNumber = roundNumber;
		this.participantTeamId = participantTeamId;
		this.championshipRefereePanelsId = championshipRefereePanelsId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Integer getParticipantTeamId() {
		return participantTeamId;
	}

	public void setParticipantTeamId(Integer participantTeamId) {
		this.participantTeamId = participantTeamId;
	}

	public Integer getChampionshipRefereePanelsId() {
		return championshipRefereePanelsId;
	}

	public void setChampionshipRefereePanelsId(Integer championshipRefereePanelsId) {
		this.championshipRefereePanelsId = championshipRefereePanelsId;
	}
	
}
