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
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "championship_category")
public class ChampionshipCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;

	@ManyToOne
	@JoinColumn(name = "age_category_id")
	private AgeCategory category;

	@Enumerated(EnumType.STRING)
	@Column(length = 45)
	private GenderEnum gender;

	private Integer noOfRounds;
	
	private Integer noOfParticipants;

	public ChampionshipCategory() {
		super();
	}

	public ChampionshipCategory(Integer id, AsanaCategory asanaCategory, AgeCategory category, GenderEnum gender,
			Integer noOfRounds) {
		super();
		this.id = id;
		this.asanaCategory = asanaCategory;
		this.category = category;
		this.gender = gender;
		this.noOfRounds = noOfRounds;
	}

	public ChampionshipCategory(Integer id, AsanaCategory asanaCategory, AgeCategory category, GenderEnum gender,
			Integer noOfRounds, Integer noOfParticipants) {
		super();
		this.id = id;
		this.asanaCategory = asanaCategory;
		this.category = category;
		this.gender = gender;
		this.noOfRounds = noOfRounds;
		this.noOfParticipants = noOfParticipants;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AsanaCategory getAsanaCategory() {
		return asanaCategory;
	}

	public void setAsanaCategory(AsanaCategory asanaCategory) {
		this.asanaCategory = asanaCategory;
	}

	public AgeCategory getCategory() {
		return category;
	}

	public void setCategory(AgeCategory category) {
		this.category = category;
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
	
	public Integer getNoOfParticipants() {
		return noOfParticipants;
	}

	public void setNoOfParticipants(Integer noOfParticipants) {
		this.noOfParticipants = noOfParticipants;
	}

	@Override
	public String toString() {
		return "ChampionshipCategory [id=" + id + ", asanaCategory=" + asanaCategory + ", category=" + category
				+ ", gender=" + gender + ", noOfRounds=" + noOfRounds + ", noOfParticipants=" + noOfParticipants + "]";
	}
	
	@Transient
	public String getCategoryDetail() {
		return asanaCategory.getName() + " - " + category.getTitle() + " - " + gender.toString();
	}

}
