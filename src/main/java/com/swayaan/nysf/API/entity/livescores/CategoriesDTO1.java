package com.swayaan.nysf.API.entity.livescores;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"category_name","category_detail_id","event_id","event_description","gender","rounds","asanaCategory_name"})
public class CategoriesDTO1 {
	
	private String category_name;
	private Integer category_detail_id;
	private Integer event_id;
	private String event_description;
	private String gender;
	private List<RoundsDTO1> rounds;
	private String asanaCategory_name;

	public CategoriesDTO1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CategoriesDTO1(String category_name, Integer category_detail_id, Integer event_id, String event_description,
			String gender, List<RoundsDTO1> rounds,String asanaCategory_name ) {
		super();
		this.category_name = category_name;
		this.category_detail_id = category_detail_id;
		this.event_id = event_id;
		this.event_description = event_description;
		this.gender = gender;
		this.rounds = rounds;
		this.asanaCategory_name = asanaCategory_name;

	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public Integer getCategory_detail_id() {
		return category_detail_id;
	}
	public void setCategory_detail_id(Integer category_detail_id) {
		this.category_detail_id = category_detail_id;
	}
	public Integer getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}
	public String getEvent_description() {
		return event_description;
	}
	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<RoundsDTO1> getRounds() {
		return rounds;
	}
	public void setRounds(List<RoundsDTO1> rounds) {
		this.rounds = rounds;
	}
	public String getAsanaCategory_name() {
		return asanaCategory_name;
	}
	public void setAsanaCategory_name(String asanaCategory_name) {
		this.asanaCategory_name = asanaCategory_name;
	}
	@Override
	public String toString() {
		return "CategoriesDTO1 [category_name=" + category_name + ", category_detail_id=" + category_detail_id
				+ ", event_id=" + event_id + ", event_description=" + event_description + ", gender=" + gender
				+ ", rounds=" + rounds + " , asanaCategory_name=" + asanaCategory_name + "]";
	}
	

}
