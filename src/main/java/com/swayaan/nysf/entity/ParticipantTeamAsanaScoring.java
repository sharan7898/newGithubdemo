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
@Table(name = "participantTeam_asana_scores")
public class ParticipantTeamAsanaScoring {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "participant_team_asana_id")
	private ParticipantTeamAsanas participantTeamAsanas;
	
	@ManyToOne
	@JoinColumn(name = "participantteam_participants_id")
	private ParticipantTeamParticipants participantTeamParticipants;
	
	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;

	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float score;
	
	@ManyToOne
	@JoinColumn(name = "judge_user_id")
	private Judge judgeUser;
	

	public ParticipantTeamAsanaScoring() {

	}


	public ParticipantTeamAsanaScoring(ParticipantTeamAsanas participantTeamAsanas,
			ParticipantTeamParticipants participantTeamParticipants,
			ChampionshipParticipantTeams championshipParticipantTeams, Judge judgeUser) {
		super();
		this.participantTeamAsanas = participantTeamAsanas;
		this.participantTeamParticipants = participantTeamParticipants;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.judgeUser = judgeUser;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public ParticipantTeamAsanas getParticipantTeamAsanas() {
		return participantTeamAsanas;
	}


	public void setParticipantTeamAsanas(ParticipantTeamAsanas participantTeamAsanas) {
		this.participantTeamAsanas = participantTeamAsanas;
	}


	public ParticipantTeamParticipants getParticipantTeamParticipants() {
		return participantTeamParticipants;
	}


	public void setParticipantTeamParticipants(ParticipantTeamParticipants participantTeamParticipants) {
		this.participantTeamParticipants = participantTeamParticipants;
	}


	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}


	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}


	public Float getScore() {
		return score;
	}


	public void setScore(Float score) {
		this.score = score;
	}


	


	public Judge getJudgeUser() {
		return judgeUser;
	}


	public void setJudgeUser(Judge judgeUser) {
		this.judgeUser = judgeUser;
	}


	@Override
	public String toString() {
		return "ParticipantTeamAsanaScoring [id=" + id + ", participantTeamAsanas=" + participantTeamAsanas
				+ ", participantTeamParticipants=" + participantTeamParticipants + ", championshipParticipantTeams="
				+ championshipParticipantTeams + ", score=" + score + ", judgeUser=" + judgeUser + "]";
	}


	public ParticipantTeamAsanaScoring(ParticipantTeamAsanas participantTeamAsanas,
			ParticipantTeamParticipants participantTeamParticipants,
			ChampionshipParticipantTeams championshipParticipantTeams) {
		super();
		this.participantTeamAsanas = participantTeamAsanas;
		this.participantTeamParticipants = participantTeamParticipants;
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

	


}
