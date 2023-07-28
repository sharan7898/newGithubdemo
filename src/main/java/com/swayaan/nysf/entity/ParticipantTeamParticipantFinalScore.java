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
@Table(name = "participant_team_participant_final_scores")
public class ParticipantTeamParticipantFinalScore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "participantteam_participants_id")
	private ParticipantTeamParticipants participantTeamParticipant;

	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;

	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float totalScore;

	private Integer round;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ParticipantTeamParticipants getParticipantTeamParticipant() {
		return participantTeamParticipant;
	}

	public void setParticipantTeamParticipant(ParticipantTeamParticipants participantTeamParticipant) {
		this.participantTeamParticipant = participantTeamParticipant;
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

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public ParticipantTeamParticipantFinalScore(ParticipantTeamParticipants participantTeamParticipant,
			ChampionshipParticipantTeams championshipParticipantTeams, Float totalScore, Integer round) {
		super();
		this.participantTeamParticipant = participantTeamParticipant;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.totalScore = totalScore;
		this.round = round;
	}

	public ParticipantTeamParticipantFinalScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ParticipantTeamParticipantFinalScore [id=" + id + ", participantTeamParticipant="
				+ participantTeamParticipant + ", championshipParticipantTeams=" + championshipParticipantTeams
				+ ", totalScore=" + totalScore + ", round=" + round + "]";
	}

}
