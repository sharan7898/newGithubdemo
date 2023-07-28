package com.swayaan.nysf.API.entity.winners;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"asana_id","asana_name","display_name","type","score"})
public class AsanasDTO {
	private String asana_id;
	private String asana_name;
	private String display_name;
	private String type;
	private String score;
	public String getAsana_id() {
		return asana_id;
	}
	public void setAsana_id(String asana_id) {
		this.asana_id = asana_id;
	}
	public String getAsana_name() {
		return asana_name;
	}
	public void setAsana_name(String asana_name) {
		this.asana_name = asana_name;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public AsanasDTO(String asana_id, String asana_name, String display_name, String type, String score) {
		super();
		this.asana_id = asana_id;
		this.asana_name = asana_name;
		this.display_name = display_name;
		this.type = type;
		this.score = score;
	}
	public AsanasDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}
