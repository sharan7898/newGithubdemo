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
@Table(name = "participant_team_total_scores")
public class ParticipantTeamTotalScoring {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeam;

	@ManyToOne
	@JoinColumn(name = "judge_user_id")
	private Judge judgeUser;

	@Column(columnDefinition = "Decimal(6,2) default '0.0'")
	private Float totalScore;

	private Integer round;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChampionshipParticipantTeams getChampionshipParticipantTeam() {
		return championshipParticipantTeam;
	}

	public void setChampionshipParticipantTeam(ChampionshipParticipantTeams championshipParticipantTeam) {
		this.championshipParticipantTeam = championshipParticipantTeam;
	}

	

	public Judge getJudgeUser() {
		return judgeUser;
	}

	public void setJudgeUser(Judge judgeUser) {
		this.judgeUser = judgeUser;
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

	@Override
	public String toString() {
		return "ParticipantTeamTotalScoring [id=" + id + ", championshipParticipantTeam=" + championshipParticipantTeam + ", judgeUser=" + judgeUser
				+ ", totalScore=" + totalScore + ", roundNumber=" + round + "]";
	}

}
