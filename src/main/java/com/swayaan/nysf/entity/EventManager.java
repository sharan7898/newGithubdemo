package com.swayaan.nysf.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "event_manager")
@DynamicUpdate(value = true)
public class EventManager extends User{

	private String designation;

	private boolean acceptRules;

	private String ernNumber;

	public EventManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventManager(String designation, boolean acceptRules, String ernNumber) {
		super();
		this.designation = designation;
		this.acceptRules = acceptRules;
		this.ernNumber = ernNumber;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public boolean isAcceptRules() {
		return acceptRules;
	}

	public void setAcceptRules(boolean acceptRules) {
		this.acceptRules = acceptRules;
	}

	public String getErnNumber() {
		return ernNumber;
	}

	public void setErnNumber(String ernNumber) {
		this.ernNumber = ernNumber;
	}
	
	@Transient
	public String getImagePath() {
		if (this.getEmail() == null)
			return "/images/image-thumbnail.png";

		return "/eventmanager-reg-uploads/" + this.getEmail() + "/" + this.getImage();
	}

	@Override
	public String toString() {
		return "EventManager [designation=" + designation + ", acceptRules=" + acceptRules + ", ernNumber=" + ernNumber
				+ "]";
	}

	
}
