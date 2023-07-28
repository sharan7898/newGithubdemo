package com.swayaan.nysf.API.entity.winners;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"name","teams"})
public class RoundsDTO {
	private String name;
	private List<TeamsDTO> teams;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TeamsDTO> getTeams() {
		return teams;
	}
	public void setTeams(List<TeamsDTO> teams) {
		this.teams = teams;
	}
	public RoundsDTO(String name, List<TeamsDTO> teams) {
		super();
		this.name = name;
		this.teams = teams;
	}
	public RoundsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
