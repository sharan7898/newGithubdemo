package com.swayaan.nysf.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="schedule_time_slots")
public class ScheduleTimeSlots {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	//sequence number
	private Integer time_slot;
	
	private String start_time;
	
	private String end_time;
	
	@ManyToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_days_id")
    private ScheduleDays scheduleDays;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleTimeSlots")
    private List<ScheduleFop> scheduleFop;
	
	/*@ManyToOne
	@JoinColumn(name = "schedule_days_id")
	private ScheduleDays scheduleDays;*/
	
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

	public List<ScheduleFop> getScheduleFop() {
		return scheduleFop;
	}

	public void setScheduleFop(List<ScheduleFop> scheduleFop) {
		this.scheduleFop = scheduleFop;
	}
	
	
	

}
