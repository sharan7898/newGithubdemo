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
@Table(name = "participantTeam_participant_total_scores")
public class ParticipantTeamParticipantTotalScoring {

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
	
	@ManyToOne
	@JoinColumn(name = "judge_user_id")
	private Judge judgeUser;
	

	public ParticipantTeamParticipantTotalScoring() {

	}


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

	public Judge getJudgeUser() {
		return judgeUser;
	}


	public void setJudgeUser(Judge judgeUser) {
		this.judgeUser = judgeUser;
	}


	public ParticipantTeamParticipantTotalScoring(ParticipantTeamParticipants participantTeamParticipant,
			ChampionshipParticipantTeams championshipParticipantTeams, Float totalScore, Integer round, Judge judgeUser) {
		super();
		this.participantTeamParticipant = participantTeamParticipant;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.totalScore = totalScore;
		this.round = round;
		this.judgeUser = judgeUser;
	}


	@Override
	public String toString() {
		return "ParticipantTeamParticipantTotalScoring [id=" + id + ", participantTeamParticipant="
				+ participantTeamParticipant + ", championshipParticipantTeams=" + championshipParticipantTeams
				+ ", totalScore=" + totalScore + ", round=" + round + ", judgeUser=" + judgeUser + "]";
	}


}
