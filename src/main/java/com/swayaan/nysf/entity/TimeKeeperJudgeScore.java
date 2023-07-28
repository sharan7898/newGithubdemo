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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "timekeeper_judge_scoring",uniqueConstraints={
	    @UniqueConstraint(columnNames = {"Participantteam_participants_id", "Participantteam_asanas_id"
	    		,"participantteam_referees_id","championship_participant_teams_id"})})
public class TimeKeeperJudgeScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.ORDINAL)
	private QuestionTypeEnum questionType;
	
	@ManyToOne
	@JoinColumn(name = "asana_evaluation_questions_id")
	private AsanaEvaluationQuestions asanaEvaluationQuestion;
	
	private Float asanaBaseValue;
	
	private Integer attempt1;
	
	private Integer attempt2;
	
	private Boolean attempt1Status;
	
	private Boolean attempt2Status;
	
	private Integer timeInMinutes;
	
	private Integer timeInSeconds;
	
	private Float score;
	
	private Boolean penaltyScore;
	
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
	
	private Integer round;
	
	private Boolean status;
	

	public TimeKeeperJudgeScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TimeKeeperJudgeScore(QuestionTypeEnum questionType, AsanaEvaluationQuestions asanaEvaluationQuestion,
			Float asanaBaseValue, Integer attempt1, Integer attempt2, Float score, Boolean penaltyScore,
			Float totalScore, Integer sequenceNumber, ParticipantTeamParticipants participantTeamParticipants,
			ParticipantTeamAsanas participantTeamAsanas, ParticipantTeamReferees participantTeamReferees,
			ChampionshipParticipantTeams championshipParticipantTeams, Integer round, Boolean status) {
		super();
		this.questionType = questionType;
		this.asanaEvaluationQuestion = asanaEvaluationQuestion;
		this.asanaBaseValue = asanaBaseValue;
		this.attempt1 = attempt1;
		this.attempt2 = attempt2;
		this.score = score;
		this.penaltyScore = penaltyScore;
		this.totalScore = totalScore;
		this.sequenceNumber = sequenceNumber;
		this.participantTeamParticipants = participantTeamParticipants;
		this.participantTeamAsanas = participantTeamAsanas;
		this.participantTeamReferees = participantTeamReferees;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.round = round;
		this.status = status;
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

	

	public Boolean getPenaltyScore() {
		return penaltyScore;
	}

	public void setPenaltyScore(Boolean penaltyScore) {
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

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getAttempt1() {
		return attempt1;
	}

	public void setAttempt1(Integer attempt1) {
		this.attempt1 = attempt1;
	}

	public Integer getAttempt2() {
		return attempt2;
	}

	public void setAttempt2(Integer attempt2) {
		this.attempt2 = attempt2;
	}

	
	

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}



	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}



	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getTimeInMinutes() {
		return timeInMinutes;
	}

	public void setTimeInMinutes(Integer timeInMinutes) {
		this.timeInMinutes = timeInMinutes;
	}

	public Integer getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(Integer timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	
	
	
	public Boolean getAttempt1Status() {
		return attempt1Status;
	}

	public void setAttempt1Status(Boolean attempt1Status) {
		this.attempt1Status = attempt1Status;
	}

	public Boolean getAttempt2Status() {
		return attempt2Status;
	}

	public void setAttempt2Status(Boolean attempt2Status) {
		this.attempt2Status = attempt2Status;
	}

	@Override
	public String toString() {
		return "TimeKeeperJudgeScore [id=" + id + ", questionType=" + questionType + ", asanaEvaluationQuestion="
				+ asanaEvaluationQuestion + ", asanaBaseValue=" + asanaBaseValue + ", attempt1=" + attempt1
				+ ", attempt2=" + attempt2 + ", score=" + score + ", penaltyScore=" + penaltyScore + ", totalScore="
				+ totalScore + ", sequenceNumber=" + sequenceNumber + ", participantTeamParticipants="
				+ participantTeamParticipants + ", participantTeamAsanas=" + participantTeamAsanas
				+ ", participantTeamReferees=" + participantTeamReferees + ", championshipParticipantTeams="
				+ championshipParticipantTeams + ", round=" + round + ", status=" + status + "]";
	}

	

	
	
	

}
