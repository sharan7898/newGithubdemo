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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "participant_teams")
public class ParticipantTeam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String name;

//	@Column(nullable = false, length = 45)
//	private String participantCategory;

	@ManyToOne
	@JoinColumn(name = "championship_id")
	private Championship championship;

	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;

	@OneToMany(mappedBy = "participantTeam")
	private List<ParticipantTeamParticipants> participantTeamParticipants;

	@OneToMany(mappedBy = "participantTeam")
	private List<ParticipantTeamAsanas> participantTeamAsanas;

	@OneToMany(mappedBy = "participantTeam")
	private List<ParticipantTeamReferees> participantTeamReferees;

	@Column(columnDefinition = "float default 0.0")
	private Float finalScore;

	@Column(columnDefinition = "float default 0.0")
	private Float penalty;

	@Column(nullable = false, length = 45)
	private String chestNumber;

	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private GenderEnum gender;

	@ManyToOne
	@JoinColumn(name = "age_category_id")
	private AgeCategory category;

	@Column(nullable = false)
	private Integer sequenceNumber;

	private boolean differentAsanasForParticipants;

	private Date createdTime;

	private Date lastModifiedTime;

	@Column(length = 45)
	private String autogenChestNumber;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;

	public Integer getId() {
		return id;
	}

//	private ChampionshipParticipantTeamStatus status;
	private RegistrationStatusEnum status;

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

//	public String getParticipantCategory() {
//		return participantCategory;
//	}
//
//	public void setParticipantCategory(String participantCategory) {
//		this.participantCategory = participantCategory;
//	}

	public List<ParticipantTeamReferees> getParticipantTeamReferees() {
		return participantTeamReferees;
	}

	public void setParticipantTeamReferees(List<ParticipantTeamReferees> participantTeamReferees) {
		this.participantTeamReferees = participantTeamReferees;
	}

	public void addParticipantTeamReferees(ParticipantTeamReferees participantTeamReferees) {
		this.participantTeamReferees.add(participantTeamReferees);
	}

	public List<ParticipantTeamAsanas> getParticipantTeamAsanas() {
		return participantTeamAsanas;
	}

	public void setParticipantTeamAsanas(List<ParticipantTeamAsanas> participantTeamAsanas) {
		this.participantTeamAsanas = participantTeamAsanas;
	}

	public void addParticipantTeamAsanas(ParticipantTeamAsanas participantTeamAsanas) {
		this.participantTeamAsanas.add(participantTeamAsanas);
	}

	public List<ParticipantTeamParticipants> getParticipantTeamParticipants() {
		return participantTeamParticipants;
	}

	public void setParticipantTeamParticipants(List<ParticipantTeamParticipants> participantTeamParticipants) {
		this.participantTeamParticipants = participantTeamParticipants;
	}

	public void addParticipantTeamParticipants(ParticipantTeamParticipants participantTeamParticipants) {
		this.participantTeamParticipants.add(participantTeamParticipants);
	}

	public void removeParticipantTeamParticipants(ParticipantTeamParticipants participantTeamParticipants) {
		this.participantTeamParticipants.remove(participantTeamParticipants);
	}

	public Float getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Float finalScore) {
		this.finalScore = finalScore;
	}

	public Float getPenalty() {
		return penalty;
	}

	public void setPenalty(Float penalty) {
		this.penalty = penalty;
	}

	public String getChestNumber() {
		return chestNumber;
	}

	public void setChestNumber(String chestNumber) {
		this.chestNumber = chestNumber;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public AgeCategory getCategory() {
		return category;
	}

	public void setCategory(AgeCategory category) {
		this.category = category;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public String getAutogenChestNumber() {
		return autogenChestNumber;
	}

	public void setAutogenChestNumber(String autogenChestNumber) {
		this.autogenChestNumber = autogenChestNumber;
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

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Transient
	public String getAsanaCategoryName() {
		if (asanaCategory.getName() != null) {
			return asanaCategory.getName();
		}
		return null;
	}

	@Transient
	public String getChampionshipName() {
		if (championship.getName() != null) {
			return championship.getName();
		}
		return null;
	}

	@Transient
	public String getTeamName() {
		return name;
	}

	public RegistrationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(RegistrationStatusEnum status) {
		this.status = status;
	}

	public boolean isDifferentAsanasForParticipants() {
		return differentAsanasForParticipants;
	}

	public void setDifferentAsanasForParticipants(boolean differentAsanasForParticipants) {
		this.differentAsanasForParticipants = differentAsanasForParticipants;
	}

}
