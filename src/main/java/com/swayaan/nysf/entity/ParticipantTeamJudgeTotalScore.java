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
@Table(name = "participant_team_judge_total_scores")
public class ParticipantTeamJudgeTotalScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer round;
	
	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;

	@ManyToOne
	@JoinColumn(name = "participantteam_participants_id")
	private ParticipantTeamParticipants participantTeamParticipants;
	
	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float totalScore;
	
//	@ManyToOne
//	@JoinColumn(name = "role_id")
//	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "judge_type_id")
	private JudgeType type;
	
	public ParticipantTeamJudgeTotalScore() {
		super();
	}

	public ParticipantTeamJudgeTotalScore(Integer id, Integer round,
			ChampionshipParticipantTeams championshipParticipantTeams,
			ParticipantTeamParticipants participantTeamParticipants, Float totalScore, JudgeType type) {
		super();
		this.id = id;
		this.round = round;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.participantTeamParticipants = participantTeamParticipants;
		this.totalScore = totalScore;
		this.type = type;
	}

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

	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

	public ParticipantTeamParticipants getParticipantTeamParticipants() {
		return participantTeamParticipants;
	}

	public void setParticipantTeamParticipants(ParticipantTeamParticipants participantTeamParticipants) {
		this.participantTeamParticipants = participantTeamParticipants;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public JudgeType getType() {
		return type;
	}

	public void setType(JudgeType type) {
		this.type = type;
	}

//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
	
	
		
}
