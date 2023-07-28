package com.swayaan.nysf.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "asana_evaluation_questions")
public class AsanaEvaluationQuestions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	@ManyToOne
//	@JoinColumn(name = "championship_id")
//	private Championship championship;

//	@ManyToOne
//	@JoinColumn(name = "championship_category_id")
//	private ChampionshipCategory championshipCategory;
	
	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;	

	@Column(name = "question", length = 1024, nullable = false)
	private String question;

	@Column(nullable = false)
	private Integer referenceMarks;

//	@ManyToOne
//	@JoinColumn(name = "role_id")
//	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "judge_type_id")
	private JudgeType type;

	private Boolean forEachAsana;
	
	
	private Boolean commonForEachParticipant;

	private Date createdTime;

	private Date lastModifiedTime;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;

	public AsanaEvaluationQuestions() {
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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Integer getReferenceMarks() {
		return referenceMarks;
	}

	public void setReferenceMarks(Integer referenceMarks) {
		this.referenceMarks = referenceMarks;
	}

//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
	
	

	public Boolean getForEachAsana() {
		return forEachAsana;
	}

	

	public JudgeType getType() {
		return type;
	}

	public void setType(JudgeType type) {
		this.type = type;
	}

	public void setForEachAsana(Boolean forEachAsana) {
		this.forEachAsana = forEachAsana;
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

	public Boolean getCommonForEachParticipant() {
		return commonForEachParticipant;
	}

	public void setCommonForEachParticipant(Boolean commonForEachParticipant) {
		this.commonForEachParticipant = commonForEachParticipant;
	}

}
