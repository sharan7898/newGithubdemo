package com.swayaan.nysf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.GenderEnum;


@Entity
public class EventCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;
	
	@ManyToOne
	@JoinColumn(name = "age_category_id")
	private AgeCategory ageCategory;

	@Enumerated(EnumType.STRING)
	@Column(length = 45)
	private GenderEnum gender;

	private Integer noOfRounds;

	public AsanaCategory getAsanaCategory() {
		return asanaCategory;
	}

	public void setAsanaCategory(AsanaCategory asanaCategory) {
		this.asanaCategory = asanaCategory;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public Integer getNoOfRounds() {
		return noOfRounds;
	}

	public void setNoOfRounds(Integer noOfRounds) {
		this.noOfRounds = noOfRounds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AgeCategory getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}
	
	
	
}
