package com.swayaan.nysf.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "Participantteamparticipant_asanas_status", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "participantteam_id", "round" }) })
public class ParticipantTeamParticipantAsanasStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer round;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "participantteam_id")
	private ParticipantTeam participantTeam;

	@Column(name = "asana_status")
	private AsanaStatusEnum asanaStatus;

	private Date createdTime;

	private Date lastModifiedTime;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;

	public ParticipantTeamParticipantAsanasStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParticipantTeamParticipantAsanasStatus(Integer id, Integer round, ParticipantTeam participantTeam,
			AsanaStatusEnum asanaStatus, Date createdTime, Date lastModifiedTime, User createdBy, User lastModifiedBy) {
		super();
		this.id = id;
		this.round = round;
		this.participantTeam = participantTeam;
		this.asanaStatus = asanaStatus;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	public ParticipantTeamParticipantAsanasStatus(Integer id, Integer round, ParticipantTeam participantTeam,
			AsanaStatusEnum asanaStatus, Date lastModifiedTime, User lastModifiedBy) {
		super();
		this.id = id;
		this.round = round;
		this.participantTeam = participantTeam;
		this.asanaStatus = asanaStatus;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public ParticipantTeam getParticipantTeam() {
		return participantTeam;
	}

	public void setParticipantTeam(ParticipantTeam participantTeam) {
		this.participantTeam = participantTeam;
	}

	@Enumerated(EnumType.STRING)
	public AsanaStatusEnum getAsanaStatus() {
		return asanaStatus;
	}

	public void setAsanaStatus(AsanaStatusEnum asanaStatus) {
		this.asanaStatus = asanaStatus;
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
