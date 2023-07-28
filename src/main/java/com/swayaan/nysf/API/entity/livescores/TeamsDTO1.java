package com.swayaan.nysf.API.entity.livescores;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.ParticipantTeam;

@JsonPropertyOrder({ "chest_id", "autogen_chestNumber", "total_score","participant_name", "athletes","rank" })
public class TeamsDTO1 {

	private String chest_id;
	private String autogen_chestNumber;
	private String status;
	private Float total_score;
	private List<String> participant_name;
	private String team_name;
	private Float tie_score;
	//private String rank;
	

	public String getChest_id() {
		return chest_id;
	}

	public void setChest_id(String chest_id) {
		this.chest_id = chest_id;
	}


	public String getAutogen_chestNumber() {
		return autogen_chestNumber;
	}

	public void setAutogen_chestNumber(String autogen_chestNumber) {
		this.autogen_chestNumber = autogen_chestNumber;
	}

	public Float getTotal_score() {
		return total_score;
	}

	public void setTotal_score(Float total_score) {
		this.total_score = total_score;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Float getTie_score() {
		return tie_score;
	}

	public void setTie_score(Float tie_score) {
		this.tie_score = tie_score;
	}

	
	

//	public String getRank() {
//		return rank;
//	}
//
//	public void setRank(String rank) {
//		this.rank = rank;
//	}

	public List<String> getParticipant_name() {
		return participant_name;
	}

	public void setParticipant_name(List<String> participant_name) {
		this.participant_name = participant_name;
	}
	

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	



	public TeamsDTO1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	public TeamsDTO1(String chest_id, String autogen_chestNumber, String status, Float total_score) {
		super();
		this.chest_id = chest_id;
		this.autogen_chestNumber = autogen_chestNumber;
		this.status = status;
		this.total_score = total_score;
	
	}

	public TeamsDTO1(String chest_id, String autogen_chestNumber, String status, Float total_score,
			List<String> participant_name, String team_name) {
		super();
		this.chest_id = chest_id;
		this.autogen_chestNumber = autogen_chestNumber;
		this.status = status;
		this.total_score = total_score;
		this.participant_name = participant_name;
		this.team_name = team_name;
	}
	
	public TeamsDTO1(String chest_id, String autogen_chestNumber, String status, Float total_score, List<String> participant_name,
			String team_name, Float tie_score) {
		super();
		this.chest_id = chest_id;
		this.autogen_chestNumber = autogen_chestNumber;
		this.status = status;
		this.total_score = total_score;
		this.participant_name = participant_name;
		this.team_name = team_name;
		this.tie_score = tie_score;
	}


	
}
