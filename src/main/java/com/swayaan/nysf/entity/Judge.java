package com.swayaan.nysf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "judges")
@DynamicUpdate(value=true)
public class Judge extends User {
	
	private String designation;
	
	private boolean acceptRules;
	
	private String certificate;
	
	private String jrnNumber;
	
	@Transient
	public String getImagePath1() {
		if (this.getEmail() == null)
			return "/images/image-thumbnail.png";

		return "/judge-reg-uploads/" + this.getEmail() + "/" + this.certificate;
	}
	
	public Judge() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Judge(String designation, boolean acceptRules) {
		super();
		this.designation = designation;
		this.acceptRules = acceptRules;
	}

	

	public boolean isAcceptRules() {
		return acceptRules;
	}

	public void setAcceptRules(boolean acceptRules) {
		this.acceptRules = acceptRules;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getJrnNumber() {
		return jrnNumber;
	}

	public void setJrnNumber(String jrnNumber) {
		this.jrnNumber = jrnNumber;
	}

	
	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	@Override
	public String toString() {
		return "Judge [designation=" + designation + ", acceptRules=" + acceptRules + "]";
	}

	
	

	
	
	
}
