package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "championship")
public class Championship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 45, unique = true)
	private String name;

	@ManyToOne
	@JoinColumn(name = "championship_levels_id")
	private ChampionshipLevels level;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "endDate")
	private Date endDate;

	@Column(length = 126)
	private String location;

	private Date createdTime;

	private Date lastModifiedTime;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "championshipId", referencedColumnName = "id")
	private List<ChampionshipCategory> championshipCategory;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 45)
	private ChampionshipStatusEnum status;

	@Column(length = 256)
	private String image1;

	@Column(length = 256)
	private String image2;

	@Column(length = 256)
	private String image3;

	@Column(length = 256)
	private String image4;

	public Championship() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Championship(Integer id, String name, ChampionshipLevels level, Date startDate, Date endDate,
			String location, Date createdTime, Date lastModifiedTime, User createdBy, User lastModifiedBy) {
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
		this.lastModifiedBy = lastModifiedBy;
	}

	public Championship(Integer id, String name, ChampionshipLevels level, Date startDate, Date endDate,
			String location, Date createdTime, Date lastModifiedTime, User createdBy, User lastModifiedBy,
			List<ChampionshipCategory> championshipCategory) {
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
		this.lastModifiedBy = lastModifiedBy;
		this.championshipCategory = championshipCategory;
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

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Enumerated(EnumType.STRING)
	public ChampionshipStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ChampionshipStatusEnum status) {
		this.status = status;
	}

	public List<ChampionshipCategory> getChampionshipCategory() {
		return championshipCategory;
	}

	public void setChampionshipCategory(List<ChampionshipCategory> championshipCategory) {
		this.championshipCategory = championshipCategory;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	@Transient
	public String getImagePath1() {
		if (this.getId() == null)
			return "/images/image-thumbnail.png";

		return "/scoreboard-image/" + this.getId() + "/image1/" + this.getImage1();
	}

	@Transient
	public String getImagePath2() {
		if (this.getId() == null)
			return "/images/image-thumbnail.png";

		return "/scoreboard-image/" + this.getId() + "/image2/" + this.getImage2();
	}

	@Transient
	public String getImagePath3() {
		if (this.getId() == null)
			return "/images/image-thumbnail.png";

		return "/scoreboard-image/" + this.getId() + "/image3/" + this.getImage3();
	}

	@Transient
	public String getImagePath4() {
		if (this.getId() == null)
			return "/images/image-thumbnail.png";

		return "/scoreboard-image/" + this.getId() + "/image4/" + this.getImage4();
	}

	@Override
	public String toString() {
		return "Championship [id=" + id + ", name=" + name + ", level=" + level + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", location=" + location + ", createdTime=" + createdTime
				+ ", lastModifiedTime=" + lastModifiedTime + ", createdBy=" + createdBy + ", lastModifiedBy="
				+ lastModifiedBy + ", championshipCategory=" + championshipCategory + "]";
	}

}
