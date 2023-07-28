package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.Championship;

import com.swayaan.nysf.entity.ChampionshipCategory;

public class ChampionshipCategoryDTO {
	
	private Championship championship;
	private ChampionshipCategory championshipCategory;
	private Integer roundNumber;
	public Championship getChampionship() {
		return championship;
	}
	public void setChampionshipId(Championship championship) {
		this.championship = championship;
	}
	public ChampionshipCategory getChampionshipCategory() {
		return championshipCategory;
	}
	public void setChampionshipCategory(ChampionshipCategory championshipCategory) {
		this.championshipCategory = championshipCategory;
	}
	public Integer getRoundNumber() {
		return roundNumber;
	}
	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}
	public ChampionshipCategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChampionshipCategoryDTO(Championship championship, ChampionshipCategory championshipCategory,
			Integer roundNumber) {
		super();
		this.championship = championship;
		this.championshipCategory = championshipCategory;
		this.roundNumber = roundNumber;
	}
	

}
