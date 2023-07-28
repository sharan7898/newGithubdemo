package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.ChampionshipCategory;
import com.swayaan.nysf.entity.ChampionshipParticipantTeams;
import com.swayaan.nysf.entity.ParticipantTeam;

public class CategorySelectionDTO {
	
	private ChampionshipCategory championshipCategory;
	
	private ChampionshipParticipantTeams championshipParticipantTeams;
	
	private ParticipantTeam participantTeam;

	public ChampionshipCategory getChampionshipCategory() {
		return championshipCategory;
	}

	public void setChampionshipCategory(ChampionshipCategory championshipCategory) {
		this.championshipCategory = championshipCategory;
	}

	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

	public ParticipantTeam getParticipantTeam() {
		return participantTeam;
	}

	public void setParticipantTeam(ParticipantTeam participantTeam) {
		this.participantTeam = participantTeam;
	}

	public CategorySelectionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategorySelectionDTO(ChampionshipCategory championshipCategory,
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeam participantTeam) {
		super();
		this.championshipCategory = championshipCategory;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.participantTeam = participantTeam;
	}
	
	
	


}
