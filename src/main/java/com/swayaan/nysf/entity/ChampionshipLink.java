
package com.swayaan.nysf.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "championship_link")
public class ChampionshipLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
    @JoinColumn(name = "championship_id", referencedColumnName = "id",unique=true)
	private Championship championship;

	private String link;
	
	private Boolean status;

	private Date createdTime;

	private Date linkActivatedTime;
	
	private Date linkDeactivatedTime;

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

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}



	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLinkActivatedTime() {
		return linkActivatedTime;
	}

	public void setLinkActivatedTime(Date linkActivatedTime) {
		this.linkActivatedTime = linkActivatedTime;
	}

	public Date getLinkDeactivatedTime() {
		return linkDeactivatedTime;
	}

	public void setLinkDeactivatedTime(Date linkDeactivatedTime) {
		this.linkDeactivatedTime = linkDeactivatedTime;
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

	public ChampionshipLink() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ChampionshipLink(Integer id, Championship championship, String link, Boolean status, Date createdTime,
			Date linkActivatedTime, Date linkDeactivatedTime, User createdBy, User lastModifiedBy) {
		super();
		this.id = id;
		this.championship = championship;
		this.link = link;
		this.status = status;
		this.createdTime = createdTime;
		this.linkActivatedTime = linkActivatedTime;
		this.linkDeactivatedTime = linkDeactivatedTime;
		this.createdBy = createdBy;
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public String toString() {
		return "ChampionshipLink [id=" + id + ", championship=" + championship + ", link=" + link + ", status=" + status
				+ ", createdTime=" + createdTime + ", linkActivatedTime=" + linkActivatedTime + ", linkDeactivatedTime="
				+ linkDeactivatedTime + ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy + "]";
	}

	


}

