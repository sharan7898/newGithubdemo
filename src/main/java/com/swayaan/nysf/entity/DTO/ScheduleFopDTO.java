package com.swayaan.nysf.entity.DTO;


public class ScheduleFopDTO {
	
	private Integer id;
	private String fop;
	private Integer category_detail_id;
	private String category_name;
	private Integer event_id;
	private String event_description;
	private String description;
	private Integer schedule_id;
	private Integer schedule_days_id;
	private Integer schedule_time_slots_id;
	
	
	
	public ScheduleFopDTO(Integer id, String fop, Integer category_detail_id, String category_name, Integer event_id,
			String event_description, String description, Integer schedule_id, Integer schedule_days_id,
			Integer schedule_time_slots_id) {
		super();
		this.id = id;
		this.fop = fop;
		this.category_detail_id = category_detail_id;
		this.category_name = category_name;
		this.event_id = event_id;
		this.event_description = event_description;
		this.description = description;
		this.schedule_id = schedule_id;
		this.schedule_days_id = schedule_days_id;
		this.schedule_time_slots_id = schedule_time_slots_id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFop() {
		return fop;
	}
	public void setFop(String fop) {
		this.fop = fop;
	}
	public Integer getCategory_detail_id() {
		return category_detail_id;
	}
	public void setCategory_detail_id(Integer category_detail_id) {
		this.category_detail_id = category_detail_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
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
	public Integer getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(Integer schedule_id) {
		this.schedule_id = schedule_id;
	}
	public Integer getSchedule_days_id() {
		return schedule_days_id;
	}
	public void setSchedule_days_id(Integer schedule_days_id) {
		this.schedule_days_id = schedule_days_id;
	}
	public Integer getSchedule_time_slots_id() {
		return schedule_time_slots_id;
	}
	public void setSchedule_time_slots_id(Integer schedule_time_slots_id) {
		this.schedule_time_slots_id = schedule_time_slots_id;
	}
	
	
	
	

}
