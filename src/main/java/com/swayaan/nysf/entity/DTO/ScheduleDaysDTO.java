package com.swayaan.nysf.entity.DTO;

import java.util.Date;

public class ScheduleDaysDTO {

	private Integer id;
	private String day;
	private Date date;
	private Integer sequenceNumber;
	
	
	public ScheduleDaysDTO(String day, Date date) {
		super();
		this.day = day;
		this.date = date;
	}
	public ScheduleDaysDTO(Integer id, String day, Date date,Integer sequenceNumber) {
		super();
		this.id = id;
		this.day = day;
		this.date = date;
		this.sequenceNumber = sequenceNumber;
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
	
	
	
	
	
}
