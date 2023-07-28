package com.swayaan.nysf.entity;

import java.util.Date;

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

@Entity
@Table(name = "asana_execution_scores")
public class AsanaExecutionScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "asana_id")
	private Asana asana;
	
	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;
	
	@ManyToOne
	@JoinColumn(name = "age_category_id")
	private AgeCategory category;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private GenderEnum gender;
	
	@ManyToOne
	@JoinColumn(name = "execution_category_id")
	private ExecutionCategory executionCategory;
	
	private Float weightage;
	
	private String code;
	
	private Integer subGroup;
	
	/*@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private CategoryEnum category; */
	
	private Date createdTime;
	
	private Date lastModifiedTime;
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Asana getAsana() {
		return asana;
	}

	public void setAsana(Asana asana) {
		this.asana = asana;
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

	public ExecutionCategory getExecutionCategory() {
		return executionCategory;
	}

	public void setExecutionCategory(ExecutionCategory executionCategory) {
		this.executionCategory = executionCategory;
	}

	public Float getWeightage() {
		return weightage;
	}

	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(Integer subGroup) {
		this.subGroup = subGroup;
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
	
	
}
