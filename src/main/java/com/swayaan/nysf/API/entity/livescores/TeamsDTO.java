package com.swayaan.nysf.API.entity.livescores;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "chest_id", "club_name", "total_score", "athletes", "scores" })
public class TeamsDTO {

	private String chest_id;
	private String club_name;

	private String total_score;
	private List<AthletesDTO> athletes;
	private TeamScoresDTO scores;
	private String rank;

	public String getChest_id() {
		return chest_id;
	}

	public void setChest_id(String chest_id) {
		this.chest_id = chest_id;
	}

	public String getClub_name() {
		return club_name;
	}

	public void setClub_name(String club_name) {
		this.club_name = club_name;
	}

	
	public String getTotal_score() {
		return total_score;
	}

	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}

	public List<AthletesDTO> getAthletes() {
		return athletes;
	}

	public void setAthletes(List<AthletesDTO> athletes) {
		this.athletes = athletes;
	}

	public TeamScoresDTO getScores() {
		return scores;
	}

	public void setScores(TeamScoresDTO scores) {
		this.scores = scores;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public TeamsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TeamsDTO(String chest_id, String club_name, String total_score, List<AthletesDTO> athletes) {
		super();
		this.chest_id = chest_id;
		this.club_name = club_name;
		this.total_score = total_score;
		this.athletes = athletes;
	}
	
	public TeamsDTO(String chest_id, String club_name, String total_score,  List<AthletesDTO> athletes, String rank) {
		super();
		this.chest_id = chest_id;
		this.club_name = club_name;
		this.total_score = total_score;
		this.athletes = athletes;
		this.rank = rank;
	}
	
	public TeamsDTO(String chest_id, String club_name, String total_score, List<AthletesDTO> athletes, TeamScoresDTO scores) {
		super();
		this.chest_id = chest_id;
		this.club_name = club_name;
		this.total_score = total_score;
		this.athletes = athletes;
		this.scores = scores;
	}

}
