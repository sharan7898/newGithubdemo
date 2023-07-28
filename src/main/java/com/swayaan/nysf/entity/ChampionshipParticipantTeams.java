package com.swayaan.nysf.entity;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="championship_participant_teams")
public class ChampionshipParticipantTeams {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "championship_rounds_id")
	private ChampionshipRounds championshipRounds;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "participant_teams_id")
	private ParticipantTeam participantTeam;
	
	
//	@Column( length = 45)
//	private ChampionshipParticipantTeamStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column( length = 45)
	private StatusEnum status;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "judge_user_id")
	private Judge judgeUser;
	
	private String executionTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChampionshipRounds getChampionshipRounds() {
		return championshipRounds;
	}

	public void setChampionshipRounds(ChampionshipRounds championshipRounds) {
		this.championshipRounds = championshipRounds;
	}

	public ParticipantTeam getParticipantTeam() {
		return participantTeam;
	}

	public void setParticipantTeam(ParticipantTeam participantTeam) {
		this.participantTeam = participantTeam;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Judge getJudgeUser() {
		return judgeUser;
	}

	public void setJudgeUser(Judge judgeUser) {
		this.judgeUser = judgeUser;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public ChampionshipParticipantTeams(Integer id, ChampionshipRounds championshipRounds,
			ParticipantTeam participantTeam, StatusEnum status) {
		super();
		this.id = id;
		this.championshipRounds = championshipRounds;
		this.participantTeam = participantTeam;
		this.status = status;
	}

	public ChampionshipParticipantTeams() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ChampionshipParticipantTeams [id=" + id + ", championshipRounds=" + championshipRounds
				+ ", participantTeam=" + participantTeam + ", status=" + status + "]";
	}
	
	

}
