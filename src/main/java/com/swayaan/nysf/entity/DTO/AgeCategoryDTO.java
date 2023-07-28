package com.swayaan.nysf.entity.DTO;

public class AgeCategoryDTO {
	
	private Integer id;
	private String title;
	private Integer ageAbove;
	private Integer ageBelow;
	private String code;
	
	public AgeCategoryDTO(Integer id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public AgeCategoryDTO(Integer id, String title, Integer ageAbove, Integer ageBelow, String code) {
		super();
		this.id = id;
		this.title = title;
		this.ageAbove = ageAbove;
		this.ageBelow = ageBelow;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAgeAbove() {
		return ageAbove;
	}

	public void setAgeAbove(Integer ageAbove) {
		this.ageAbove = ageAbove;
	}

	public Integer getAgeBelow() {
		return ageBelow;
	}

	public void setAgeBelow(Integer ageBelow) {
		this.ageBelow = ageBelow;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
