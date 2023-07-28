package com.swayaan.nysf.API.entity.schedule;

import java.util.List;

public class ScheduleDaysDTO {

	private String day;
	private String date;
	private List<ScheduleTimeSlotsDTO> time_slots;
	
	public ScheduleDaysDTO(String day, String date) {
		super();
		this.day = day;
		this.date = date;
	}
	
	public ScheduleDaysDTO(String day, String date, List<ScheduleTimeSlotsDTO> time_slots) {
		super();
		this.day = day;
		this.date = date;
		this.time_slots = time_slots;
	}
	public ScheduleDaysDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<ScheduleTimeSlotsDTO> getTime_slots() {
		return time_slots;
	}
	public void setTime_slots(List<ScheduleTimeSlotsDTO> time_slots) {
		this.time_slots = time_slots;
	}
	
	
}
