package com.swayaan.nysf.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "participantteam_referees")
public class ParticipantTeamReferees {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "judge_user_referee_id")
	private Judge judgeUser;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "participantteam_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ParticipantTeam participantTeam;
	
	private Integer roundNumber;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "championship_id")
	private Championship championship;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "championship_category_id")
	private ChampionshipCategory championshipCategory;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "judge_type_id")
	private JudgeType type;
	
//	@ManyToOne(cascade = CascadeType.PERSIST)
//	@JoinColumn(name = "roles_id")
//	private Role role;
	
	
	
	@Transient
	public String getUserName() {
		if (judgeUser.getFirstName() != null) {
			return judgeUser.getFirstName() + " " + judgeUser.getLastName();
		}

		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Judge getJudgeUser() {
		return judgeUser;
	}

	public void setJudgeUser(Judge judgeUser) {
		this.judgeUser = judgeUser;
	}

	public ParticipantTeam getParticipantTeam() {
		return participantTeam;
	}

	public void setParticipantTeam(ParticipantTeam participantTeam) {
		this.participantTeam = participantTeam;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public ChampionshipCategory getChampionshipCategory() {
		return championshipCategory;
	}

	public void setChampionshipCategory(ChampionshipCategory championshipCategory) {
		this.championshipCategory = championshipCategory;
	}
	
	
	

//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}

	public JudgeType getType() {
		return type;
	}

	public void setType(JudgeType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ParticipantTeamReferees [id=" + id + ", judgeUser=" + judgeUser + ", participantTeam=" + participantTeam
				+ ", roundNumber=" + roundNumber + ", championship=" + championship + ", championshipCategory="
				+ championshipCategory + "]";
	}

	
	
}
