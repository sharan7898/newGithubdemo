package com.swayaan.nysf.API.entity.schedule;

import java.util.List;

public class ScheduleDTO {
	
	private  String championship_name;
	private Integer championship_id;
	private Integer sport_id;
	private String sport_name;
	private String description;
	private List<ScheduleDaysDTO> schedule_days;
	public ScheduleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ScheduleDTO(String championship_name, Integer championship_id, Integer sport_id, String sport_name,
			String description) {
		super();
		this.championship_name = championship_name;
		this.championship_id = championship_id;
		this.sport_id = sport_id;
		this.sport_name = sport_name;
		this.description = description;
	}

	public ScheduleDTO(String championship_name, Integer championship_id, Integer sport_id, String sport_name,
			String description, List<ScheduleDaysDTO> schedule_days) {
		super();
		this.championship_name = championship_name;
		this.championship_id = championship_id;
		this.sport_id = sport_id;
		this.sport_name = sport_name;
		this.description = description;
		this.schedule_days = schedule_days;
	}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ScheduleDaysDTO> getSchedule_days() {
		return schedule_days;
	}
	public void setSchedule_days(List<ScheduleDaysDTO> schedule_days) {
		this.schedule_days = schedule_days;
	}
	

}
