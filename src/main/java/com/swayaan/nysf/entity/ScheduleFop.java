package com.swayaan.nysf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="schedule_fop")
public class ScheduleFop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private FOPEnum fop;
	
	private Integer category_detail_id;
	
	private String category_name;
	
	private Integer event_id;
	
	private String event_description;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;
	
	@ManyToOne
    @JoinColumn(name = "schedule_days_id")
    private ScheduleDays scheduleDays;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_time_slots_id")
    private ScheduleTimeSlots scheduleTimeSlots;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FOPEnum getFop() {
		return fop;
	}

	public void setFop(FOPEnum fop) {
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

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public ScheduleDays getScheduleDays() {
		return scheduleDays;
	}

	public void setScheduleDays(ScheduleDays scheduleDays) {
		this.scheduleDays = scheduleDays;
	}

	public ScheduleTimeSlots getScheduleTimeSlots() {
		return scheduleTimeSlots;
	}

	public void setScheduleTimeSlots(ScheduleTimeSlots scheduleTimeSlots) {
		this.scheduleTimeSlots = scheduleTimeSlots;
	}
	

}
