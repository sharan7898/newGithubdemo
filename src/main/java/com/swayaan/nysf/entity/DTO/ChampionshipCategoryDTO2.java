package com.swayaan.nysf.entity.DTO;

public class ChampionshipCategoryDTO2 {
	
	private Integer id;
	private Integer championshipId;
	private String asanaCategory;
	private String ageCategory;
	private String gender;

	public ChampionshipCategoryDTO2() {
		super();
	}
	
	public ChampionshipCategoryDTO2(Integer id, Integer championshipId, String asanaCategory, String ageCategory, String gender) {
		super();
		this.id = id;
		this.championshipId = championshipId;
		this.asanaCategory = asanaCategory;
		this.ageCategory = ageCategory;
		this.gender = gender;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChampionshipId() {
		return championshipId;
	}

	public void setChampionshipId(Integer championshipId) {
		this.championshipId = championshipId;
	}

	public String getAsanaCategory() {
		return asanaCategory;
	}

	public void setAsanaCategory(String asanaCategory) {
		this.asanaCategory = asanaCategory;
	}

	public String getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(String ageCategory) {
		this.ageCategory = ageCategory;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return asanaCategory + " - " + ageCategory + " - "+  gender;
	}

	

}
