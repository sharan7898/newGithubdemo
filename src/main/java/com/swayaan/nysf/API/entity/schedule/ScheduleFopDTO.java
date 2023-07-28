package com.swayaan.nysf.API.entity.schedule;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "FOP", "category_name", "category_detail_id", "event_id" ,"event_description", "description"})
public class ScheduleFopDTO {
	
	private String FOP;
	private String category_name;
	private Integer category_detail_id;
	private Integer event_id;
	private String event_description;
	private String description;

	public ScheduleFopDTO() {
		super();
	}

	public ScheduleFopDTO(String FOP, String description) {
		super();
		this.FOP = FOP;
		this.description = description;
	}

	public ScheduleFopDTO(String FOP, String category_name, Integer category_detail_id, Integer event_id, String event_description,
			String description) {
		super();
		this.FOP = FOP;
		this.category_name = category_name;
		this.category_detail_id = category_detail_id;
		this.event_id = event_id;
		this.event_description = event_description;
		this.description = description;
	}

	@JsonGetter("FOP")
	public String getFOP() {
		return FOP;
	}

	public void setFOP(String fOP) {
		FOP = fOP;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
