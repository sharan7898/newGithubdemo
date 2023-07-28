package com.swayaan.nysf.API.entity.livescores;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"championship_name","championship_id","description","sport_id","sport_name","categories"})
public class WinnersDTO {

	private String championship_name;
	private Integer championship_id;
	private String description;
	private Integer sport_id;
	private String sport_name;
	private List<CategoriesDTO> categories;
	public String getChampionship_name() {
		return championship_name;
	}
	public void setChampionship_name(String championship_name) {
		this.championship_name = championship_name;
	}
	public Integer getChampionship_id() {
		return championship_id;
	}
	public void setChampionship_id(Integer championship_id) {
		this.championship_id = championship_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSport_id() {
		return sport_id;
	}
	public void setSport_id(Integer sport_id) {
		this.sport_id = sport_id;
	}
	public String getSport_name() {
		return sport_name;
	}
	public void setSport_name(String sport_name) {
		this.sport_name = sport_name;
	}
	public List<CategoriesDTO> getCategories() {
		return categories;
	}
	public void setCategories(List<CategoriesDTO> categories) {
		this.categories = categories;
	}
	public WinnersDTO(String championship_name, Integer championship_id, String description, Integer sport_id,
			String sport_name, List<CategoriesDTO> categories) {
		super();
		this.championship_name = championship_name;
		this.championship_id = championship_id;
		this.description = description;
		this.sport_id = sport_id;
		this.sport_name = sport_name;
		this.categories = categories;
	}
	public WinnersDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
