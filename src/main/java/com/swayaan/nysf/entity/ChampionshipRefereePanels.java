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

@Entity
@Table(name="championship_referee_panels")
public class ChampionshipRefereePanels {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 64)
	private String name;

	@ManyToOne
	@JoinColumn(name = "championship_id")
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "asana_category_id")
	private AsanaCategory asanaCategory;
	
	@ManyToOne
	@JoinColumn(name = "age_category_id")
	private AgeCategory category;

	@Enumerated(EnumType.STRING)
	@Column(length = 45)
	private GenderEnum gender;
	
	@OneToMany(mappedBy = "championshipRefereePanels")
	private List<RefereesPanels> refereesPanels;
	
	Integer roundNumber;
	
	private Date createdTime;
	
	private Date lastModifiedTime;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private ChampionshipRefereePanelsEnum status;
	
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

	public List<RefereesPanels> getRefereesPanels() {
        return refereesPanels;
    }
 
    public void setRefereesPanels(List<RefereesPanels> refereesPanels) {
        this.refereesPanels = refereesPanels;
    }
     
    public void addRefereesPanels(RefereesPanels refereesPanels) {
        this.refereesPanels.add(refereesPanels);
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

	public void setCategory(AgeCategory category) {
		this.category = category;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public ChampionshipRefereePanelsEnum getStatus() {
		return status;
	}

	public void setStatus(ChampionshipRefereePanelsEnum status) {
		this.status = status;
	}
	
	
    
    
	
}

                                                                                                                                                                                                                                                                         