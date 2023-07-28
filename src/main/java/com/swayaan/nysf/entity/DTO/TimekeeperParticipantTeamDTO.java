package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.ChampionshipParticipantTeams;

public class TimekeeperParticipantTeamDTO {

	private ChampionshipParticipantTeams championshipParticipantTeams;
	 
	private Boolean isTeamJudge;
	
	private String asanaStatus;

	

	

	public TimekeeperParticipantTeamDTO(ChampionshipParticipantTeams championshipParticipantTeams, Boolean isTeamJudge,
			String asanaStatus) {
		super();
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.isTeamJudge = isTeamJudge;
		this.asanaStatus = asanaStatus;
	}

	public String getAsanaStatus() {
		return asanaStatus;
	}

	public void setAsanaStatus(String asanaStatus) {
		this.asanaStatus = asanaStatus;
	}

	public TimekeeperParticipantTeamDTO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

	public Boolean getIsTeamJudge() {
		return isTeamJudge;
	}

	public void setIsTeamJudge(Boolean isTeamJudge) {
		this.isTeamJudge = isTeamJudge;
	}

	
	
	
	
}
