package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="schedule_days")
public class ScheduleDays {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 256)
	private String day;
	
	@Column(name = "date")
	private Date date;
	
	private Integer sequenceNumber;
	
	/*@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "schedule_days_id", referencedColumnName = "id")
	private List<ScheduleTimeSlots> scheduleTimeSlots;*/
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleDays")
    private List<ScheduleTimeSlots> scheduleTimeSlots;

	
	public ScheduleDays() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public List<ScheduleTimeSlots> getScheduleTimeSlots() {
		return scheduleTimeSlots;
	}

	public void setScheduleTimeSlots(List<ScheduleTimeSlots> scheduleTimeSlots) {
		this.scheduleTimeSlots = scheduleTimeSlots;
	}
	
	
}
