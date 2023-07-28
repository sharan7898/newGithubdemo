package com.swayaan.nysf.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "participant_scoring_judge")
public class ParticipantScoringJudge {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "Participantteam_participants_id")
	private ParticipantTeamParticipants participantTeamParticipants;
	
	@ManyToOne
	@JoinColumn(name = "participantteam_referees_id")
	private ParticipantTeamReferees participantTeamReferees;
	
	@ManyToOne
	@JoinColumn(name = "championship_participant_teams_id")
	private ChampionshipParticipantTeams championshipParticipantTeams;
	
//	@ManyToOne(cascade = CascadeType.PERSIST)
//	@JoinColumn(name = "roles_id")
//	private Role role;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "judge_type_id")
	private JudgeType type;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "championship_category_id")
	private ChampionshipCategory championshipCategory;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "championship_id")
	private Championship championship;
	
	private Integer round;

	public ParticipantScoringJudge() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParticipantScoringJudge(Integer id, ParticipantTeamParticipants participantTeamParticipants,
			ParticipantTeamReferees participantTeamReferees, ChampionshipParticipantTeams championshipParticipantTeams,
			JudgeType judgeType, ChampionshipCategory championshipCategory, Championship championship) {
		super();
		this.id = id;
		this.participantTeamParticipants = participantTeamParticipants;
		this.participantTeamReferees = participantTeamReferees;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.type = judgeType;
		this.championshipCategory = championshipCategory;
		this.championship = championship;
	}

	public ParticipantScoringJudge(ParticipantTeamParticipants participantTeamParticipants,
			ParticipantTeamReferees participantTeamReferees, ChampionshipParticipantTeams championshipParticipantTeams,
			JudgeType judgeType, ChampionshipCategory championshipCategory, Championship championship) {
		super();
		this.participantTeamParticipants = participantTeamParticipants;
		this.participantTeamReferees = participantTeamReferees;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.type = judgeType;
		this.championshipCategory = championshipCategory;
		this.championship = championship;
	}
	
	

	private ParticipantScoringJudge(ParticipantTeamParticipants participantTeamParticipants,
			ParticipantTeamReferees participantTeamReferees, ChampionshipParticipantTeams championshipParticipantTeams,
			JudgeType judgeType, ChampionshipCategory championshipCategory, Championship championship, Integer round) {
		super();
		this.participantTeamParticipants = participantTeamParticipants;
		this.participantTeamReferees = participantTeamReferees;
		this.championshipParticipantTeams = championshipParticipantTeams;
		this.type = judgeType;
		this.championshipCategory = championshipCategory;
		this.championship = championship;
		this.round = round;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public ChampionshipParticipantTeams getChampionshipParticipantTeams() {
		return championshipParticipantTeams;
	}

	public void setChampionshipParticipantTeams(ChampionshipParticipantTeams championshipParticipantTeams) {
		this.championshipParticipantTeams = championshipParticipantTeams;
	}

//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
	

	public ChampionshipCategory getChampionshipCategory() {
		return championshipCategory;
	}

	public JudgeType getType() {
		return type;
	}

	public void setType(JudgeType type) {
		this.type = type;
	}

	public void setChampionshipCategory(ChampionshipCategory championshipCategory) {
		this.championshipCategory = championshipCategory;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}
	
	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	@Override
	public String toString() {
		return "ParticipantScoringJudge [id=" + id + ", participantTeamParticipants=" + participantTeamParticipants
				+ ", participantTeamReferees=" + participantTeamReferees + ", championshipParticipantTeams="
				+ championshipParticipantTeams + ", type=" + type + ", championshipCategory=" + championshipCategory
				+ ", championship=" + championship + ", round=" + round + "]";
	}

	
	
	

}
