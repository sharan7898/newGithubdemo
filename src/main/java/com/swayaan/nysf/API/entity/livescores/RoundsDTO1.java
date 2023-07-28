package com.swayaan.nysf.API.entity.livescores;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"name","teams"})
public class RoundsDTO1 {
	private String name;
	private List<TeamsDTO1> teams;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TeamsDTO1> getTeams() {
		return teams;
	}
	public void setTeams(List<TeamsDTO1> teams) {
		this.teams = teams;
	}
	public RoundsDTO1(String name, List<TeamsDTO1> teams) {
		super();
		this.name = name;
		this.teams = teams;
	}
	public RoundsDTO1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
