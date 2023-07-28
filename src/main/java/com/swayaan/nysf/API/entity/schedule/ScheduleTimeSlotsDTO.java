package com.swayaan.nysf.API.entity.schedule;

import java.util.List;

public class ScheduleTimeSlotsDTO {
	private Integer time_slot;
	private String start_time;
	private String end_time;
	private List<ScheduleFopDTO> values;
	
	public ScheduleTimeSlotsDTO(Integer time_slot, String start_time, String end_time) {
		super();
		this.time_slot = time_slot;
		this.start_time = start_time;
		this.end_time = end_time;
	}
	
	public ScheduleTimeSlotsDTO(Integer time_slot, String start_time, String end_time, List<ScheduleFopDTO> values) {
		super();
		this.time_slot = time_slot;
		this.start_time = start_time;
		this.end_time = end_time;
		this.values = values;
	}
	public ScheduleTimeSlotsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getTime_slot() {
		return time_slot;
	}
	public void setTime_slot(Integer time_slot) {
		this.time_slot = time_slot;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public List<ScheduleFopDTO> getValues() {
		return values;
	}
	public void setValues(List<ScheduleFopDTO> values) {
		this.values = values;
	}
	
	
	

}
