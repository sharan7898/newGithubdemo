package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "event_registration")
public class EventRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 256, unique = true)
	private String name;

	@ManyToOne
	@JoinColumn(name = "championship_levels_id")
	private ChampionshipLevels level;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "endDate")
	private Date endDate;

	@Column(length = 128)
	private String location;

	private Date createdTime;

	private Date lastModifiedTime;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name = "modified_by")
	private User modifiedBy;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "eventRegistration_id", referencedColumnName = "id")
	private List<EventCategory> eventCategory;
	
	
	
	private RegistrationStatusEnum approvalStatus;

	public EventRegistration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventRegistration(Integer id, String name, ChampionshipLevels level, Date startDate, Date endDate,
			String location, Date createdTime, Date lastModifiedTime, User createdBy, List<EventCategory> eventCategory,
			RegistrationStatusEnum approvalStatus) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
		this.createdBy = createdBy;
		this.eventCategory = eventCategory;
		this.approvalStatus = approvalStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChampionshipLevels getLevel() {
		return level;
	}

	public void setLevel(ChampionshipLevels level) {
		this.level = level;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public List<EventCategory> getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(List<EventCategory> eventCategory) {
		this.eventCategory = eventCategory;
	}
	
	public RegistrationStatusEnum getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(RegistrationStatusEnum approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public String toString() {
		return "EventRegistration [id=" + id + ", name=" + name + ", level=" + level + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", location=" + location + ", createdTime=" + createdTime
				+ ", lastModifiedTime=" + lastModifiedTime + ", createdBy=" + createdBy + ", eventCategory="
				+ eventCategory + ", approvalStatus=" + approvalStatus + "]";
	}

	

}
