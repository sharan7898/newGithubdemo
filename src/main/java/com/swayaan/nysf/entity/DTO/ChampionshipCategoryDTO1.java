package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.Championship;

public class ChampionshipCategoryDTO1 {

	private Championship championship;
	private AsanaCategory asanaCategory;
	private AgeCategory ageCategory;
	private Integer roundNumber;

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionshipId(Championship championship) {
		this.championship = championship;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public ChampionshipCategoryDTO1() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AsanaCategory getAsanaCategory() {
		return asanaCategory;
	}

	public void setAsanaCategory(AsanaCategory asanaCategory) {
		this.asanaCategory = asanaCategory;
	}

	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public ChampionshipCategoryDTO1(Championship championship, AsanaCategory asanaCategory, AgeCategory ageCategory,
			Integer roundNumber) {
		super();
		this.championship = championship;
		this.asanaCategory = asanaCategory;
		this.ageCategory = ageCategory;
		this.roundNumber = roundNumber;
	}

	

	

}
