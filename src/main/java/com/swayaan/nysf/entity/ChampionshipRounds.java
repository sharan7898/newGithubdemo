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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "championship_rounds",uniqueConstraints={
	    @UniqueConstraint(columnNames = {"championship_id", "championship_category_id"
	    		,"round"})})
public class ChampionshipRounds {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "championship_id")
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "championship_category_id")
	private ChampionshipCategory championshipCategory;
	
	@Column(name="round")
	private Integer round;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private RoundStatusEnum status;

	private Integer noOfParticipantTeams;

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

	public ChampionshipCategory getChampionshipCategory() {
		return championshipCategory;
	}

	public void setChampionshipCategory(ChampionshipCategory championshipCategory) {
		this.championshipCategory = championshipCategory;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public RoundStatusEnum getStatus() {
		return status;
	}

	public void setStatus(RoundStatusEnum status) {
		this.status = status;
	}

	public Integer getNoOfParticipantTeams() {
		return noOfParticipantTeams;
	}

	public void setNoOfParticipantTeams(Integer noOfParticipantTeams) {
		this.noOfParticipantTeams = noOfParticipantTeams;
	}

	public ChampionshipRounds() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChampionshipRounds(Integer id, Championship championship, ChampionshipCategory championshipCategory,
			Integer round, RoundStatusEnum status, Integer noOfParticipantTeams) {
		super();
		this.id = id;
		this.championship = championship;
		this.championshipCategory = championshipCategory;
		this.round = round;
		this.status = status;
		this.noOfParticipantTeams = noOfParticipantTeams;
	}

	@Override
	public String toString() {
		return "ChampionshipRounds [id=" + id + ", championship=" + championship + ", championshipCategory="
				+ championshipCategory + ", round=" + round + ", status=" + status + ", noOfParticipantTeams="
				+ noOfParticipantTeams + "]";
	}

	
	
	
	
}
