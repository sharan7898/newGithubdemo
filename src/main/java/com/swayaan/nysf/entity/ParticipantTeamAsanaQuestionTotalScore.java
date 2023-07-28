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

@Entity
@Table(name = "participant_team_asana_question_total_scores")
public class ParticipantTeamAsanaQuestionTotalScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer round;
	
	@Enumerated(EnumType.ORDINAL)
	private QuestionTypeEnum questionType;
	
	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;
	
	@ManyToOne
	@JoinColumn(name = "participant_team_asana_id")
	private ParticipantTeamAsanas participantTeamAsanas;
	
	@ManyToOne
	@JoinColumn(name = "participantteam_participants_id")
	private ParticipantTeamParticipants participantTeamParticipants;
	
	@ManyToOne
	@JoinColumn(name = "participantteam_referees_id")
	private ParticipantTeamReferees participantTeamReferees;

	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float totalScore;
	
	private Integer sequenceNumber;

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

	public QuestionTypeEnum getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionTypeEnum questionType) {
		this.questionType = questionType;
	}

	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
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

	public ParticipantTeamReferees getParticipantTeamReferees() {
		return participantTeamReferees;
	}

	public void setParticipantTeamReferees(ParticipantTeamReferees participantTeamReferees) {
		this.participantTeamReferees = participantTeamReferees;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	

}
