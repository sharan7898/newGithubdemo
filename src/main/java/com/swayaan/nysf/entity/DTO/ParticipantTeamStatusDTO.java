package com.swayaan.nysf.entity.DTO;

public class ParticipantTeamStatusDTO {
	
	private String participantTeamName;
	private String status;
	private String championshipName;
	private String chestNumber;
	
	public String getChestNumber() {
		return chestNumber;
	}
	public void setChestNumber(String chestNumber) {
		this.chestNumber = chestNumber;
	}
	public String getParticipantTeamName() {
		return participantTeamName;
	}
	public void setParticipantTeamName(String participantTeamName) {
		this.participantTeamName = participantTeamName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChampionshipName() {
		return championshipName;
	}
	public void setChampionshipName(String championshipName) {
		this.championshipName = championshipName;
	}
	public ParticipantTeamStatusDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ParticipantTeamStatusDTO(String participantTeamName, String status, String championshipName) {
		super();
		this.participantTeamName = participantTeamName;
		this.status = status;
		this.championshipName = championshipName;
	}
	
	public ParticipantTeamStatusDTO(String participantTeamName, String status, String championshipName,
			String chestNumber) {
		super();
		this.participantTeamName = participantTeamName;
		this.status = status;
		this.championshipName = championshipName;
		this.chestNumber = chestNumber;
	}
	

}
