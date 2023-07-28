package com.swayaan.nysf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "participant_team_round_total_scores")
public class ParticipantTeamRoundTotalScoring {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;

	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float totalScore;

	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float tieScore;
	
	private boolean isTie;
	
	@ManyToOne
	@JoinColumn(name = "championship_id")
	private Championship championship;
	
	@ManyToOne
	@JoinColumn(name = "championship_rounds_id")
	private ChampionshipRounds championshipRounds;

	
	private boolean winner;

	private Integer ranking;

	public ParticipantTeamRoundTotalScoring() {
	}

	
	public ParticipantTeamRoundTotalScoring(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	


	public boolean isWinner() {
		return winner;
	}


	public void setWinner(boolean winner) {
		this.winner = winner;
	}


	public ChampionshipRounds getChampionshipRounds() {
		return championshipRounds;
	}


	public void setChampionshipRounds(ChampionshipRounds championshipRounds) {
		this.championshipRounds = championshipRounds;
	}


	public Float getTieScore() {
		return tieScore;
	}


	public void setTieScore(Float tieScore) {
		this.tieScore = tieScore;
	}


	public boolean isTie() {
		return isTie;
	}


	public void setTie(boolean isTie) {
		this.isTie = isTie;
	}

	

}
