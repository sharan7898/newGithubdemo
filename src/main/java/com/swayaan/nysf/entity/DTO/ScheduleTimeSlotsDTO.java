package com.swayaan.nysf.entity.DTO;

public class ScheduleTimeSlotsDTO {
	
	private Integer id;
	
	private Integer time_slot;
	
	private String start_time;
	
	private String end_time;
	
	private Integer schedule_days_id;
	
	private String schedule_days_name;
	
	public ScheduleTimeSlotsDTO() {
	}

	public ScheduleTimeSlotsDTO(Integer id, Integer time_slot, String start_time, String end_time) {
		this.id = id;
		this.time_slot = time_slot;
		this.start_time = start_time;
		this.end_time = end_time;
	}
	
	public ScheduleTimeSlotsDTO(Integer id, Integer time_slot, String start_time, String end_time,Integer schedule_days_id) {
		this.id = id;
		this.time_slot = time_slot;
		this.start_time = start_time;
		this.end_time = end_time;
		this.schedule_days_id = schedule_days_id;
	}

	public ScheduleTimeSlotsDTO(Integer id, Integer time_slot, String start_time, String end_time,
			Integer schedule_days_id, String schedule_days_name) {
		super();
		this.id = id;
		this.time_slot = time_slot;
		this.start_time = start_time;
		this.end_time = end_time;
		this.schedule_days_id = schedule_days_id;
		this.schedule_days_name = schedule_days_name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getSchedule_days_id() {
		return schedule_days_id;
	}

	public void setSchedule_days_id(Integer schedule_days_id) {
		this.schedule_days_id = schedule_days_id;
	}

	public String getSchedule_days_name() {
		return schedule_days_name;
	}

	public void setSchedule_days_name(String schedule_days_name) {
		this.schedule_days_name = schedule_days_name;
	}
	
	
	
	

}
