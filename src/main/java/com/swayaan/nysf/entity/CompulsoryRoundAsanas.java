package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.List;

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
@Table(name = "compulsory_round_asanas")
public class CompulsoryRoundAsanas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "championship_id")
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private GenderEnum gender;
	
	@ManyToOne
	@JoinColumn(name = "age_category_id")
	private AgeCategory category;
	
	
	private Integer roundNumber;
	
	@ManyToOne
	@JoinColumn(name = "asana_id")
	private Asana asana;
	
//	@ManyToOne
//	@JoinColumn(name = "asana_execution_score_id")
//	private AsanaExecutionScore asanaExecutionScore;
	
	private boolean compulsory;
	
	private Date createdTime;
	
	private Date lastModifiedTime;
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;
	
	private Integer sequenceNumber;
	
	@Transient
	private List<Asana> asanaList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

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

	public boolean isCompulsory() {
		return compulsory;
	}

	public void setCompulsory(boolean compulsory) {
		this.compulsory = compulsory;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public AgeCategory getCategory() {
		return category;
	}

	public Asana getAsana() {
		return asana;
	}

	public void setAsana(Asana asana) {
		this.asana = asana;
	}

	public List<Asana> getAsanaList() {
		return asanaList;
	}

	public void setAsanaList(List<Asana> asanaList) {
		this.asanaList = asanaList;
	}

	public void setCategory(AgeCategory category) {
		this.category = category;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return "CompulsoryRoundAsanas [id=" + id + ", championship=" + championship + ", asanaCategory=" + asanaCategory
				+ ", gender=" + gender + ", category=" + category + ", roundNumber=" + roundNumber + ", asana=" + asana
				+ ", compulsory=" + compulsory + ", createdTime=" + createdTime + ", lastModifiedTime="
				+ lastModifiedTime + ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy
				+ ", sequenceNumber=" + sequenceNumber + ", asanaList=" + asanaList + "]";
	}

//	public List<String> getAsanaList() {
//		return asanaList;
//	}
//
//	public void setAsanaList(List<String> asanaList) {
//		this.asanaList = asanaList;
//	}
	
	

}
