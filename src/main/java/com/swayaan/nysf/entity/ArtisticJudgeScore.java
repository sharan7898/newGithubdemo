package com.swayaan.nysf.entity;

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
@Table(name = "artistic_judge_scoring")
public class ArtisticJudgeScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.ORDINAL)
	private QuestionTypeEnum questionType;
	
	@ManyToOne
	@JoinColumn(name = "asana_evaluation_questions_id")
	private AsanaEvaluationQuestions asanaEvaluationQuestion;
	
	private Float asanaBaseValue;
	
	private Float score;
	
	private Float penaltyScore;
	
	private Float totalScore;
	
	private Integer sequenceNumber;
	
	@ManyToOne
	@JoinColumn(name = "Participantteam_participants_id")
	private ParticipantTeamParticipants participantTeamParticipants;
	
	@ManyToOne
	@JoinColumn(name = "Participantteam_asanas_id")
	private ParticipantTeamAsanas participantTeamAsanas;
	
	@ManyToOne
	@JoinColumn(name = "participantteam_referees_id")
	private ParticipantTeamReferees participantTeamReferees;
	
	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;
	
	private Boolean status;
	
	private Integer round;

	public ArtisticJudgeScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	


	public ArtisticJudgeScore(QuestionTypeEnum questionType, AsanaEvaluationQuestions asanaEvaluationQuestion,
			Float asanaBaseValue, Float score, Float penaltyScore, Float totalScore, Integer sequenceNumber,
			ParticipantTeamParticipants participantTeamParticipants, ParticipantTeamAsanas participantTeamAsanas,
			ParticipantTeamReferees participantTeamReferees, ChampionshipParticipantTeams championshipParticipantTeams,
			Boolean status, Integer round) {
		super();
		this.questionType = questionType;
		this.asanaEvaluationQuestion = asanaEvaluationQuestion;
		this.asanaBaseValue = asanaBaseValue;
		this.score = score;
		this.penaltyScore = penaltyScore;
		this.totalScore = totalScore;
		this.sequenceNumber = sequenceNumber;
		this.participantTeamParticipants = participantTeamParticipants;
		this.participantTeamAsanas = participantTeamAsanas;
		this.participantTeamReferees = participantTeamReferees;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.status = status;
		this.round = round;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public QuestionTypeEnum getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionTypeEnum questionType) {
		this.questionType = questionType;
	}

	public AsanaEvaluationQuestions getAsanaEvaluationQuestion() {
		return asanaEvaluationQuestion;
	}

	public void setAsanaEvaluationQuestion(AsanaEvaluationQuestions asanaEvaluationQuestion) {
		this.asanaEvaluationQuestion = asanaEvaluationQuestion;
	}

	public Float getAsanaBaseValue() {
		return asanaBaseValue;
	}

	public void setAsanaBaseValue(Float asanaBaseValue) {
		this.asanaBaseValue = asanaBaseValue;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Float getPenaltyScore() {
		return penaltyScore;
	}

	public void setPenaltyScore(Float penaltyScore) {
		this.penaltyScore = penaltyScore;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public ParticipantTeamParticipants getParticipantTeamParticipants() {
		return participantTeamParticipants;
	}

	public void setParticipantTeamParticipants(ParticipantTeamParticipants participantTeamParticipants) {
		this.participantTeamParticipants = participantTeamParticipants;
	}

	public ParticipantTeamAsanas getParticipantTeamAsanas() {
		return participantTeamAsanas;
	}

	public void setParticipantTeamAsanas(ParticipantTeamAsanas participantTeamAsanas) {
		this.participantTeamAsanas = participantTeamAsanas;
	}

	public ParticipantTeamReferees getParticipantTeamReferees() {
		return participantTeamReferees;
	}

	public void setParticipantTeamReferees(ParticipantTeamReferees participantTeamReferees) {
		this.participantTeamReferees = participantTeamReferees;
	}

	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	
	
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return "ArtisticJudgeScore [id=" + id + ", questionType=" + questionType + ", asanaEvaluationQuestion="
				+ asanaEvaluationQuestion + ", asanaBaseValue=" + asanaBaseValue + ", score=" + score
				+ ", penaltyScore=" + penaltyScore + ", totalScore=" + totalScore + ", sequenceNumber=" + sequenceNumber
				+ ", participantTeamParticipants=" + participantTeamParticipants + ", participantTeamAsanas="
				+ participantTeamAsanas + ", participantTeamReferees=" + participantTeamReferees
				+ ", championshipParticipantTeams=" + championshipParticipantTeams + ", status=" + status + ", round="
				+ round + "]";
	}

	
	
}
