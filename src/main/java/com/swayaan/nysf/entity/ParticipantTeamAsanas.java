package com.swayaan.nysf.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "Participantteam_asanas")
public class ParticipantTeamAsanas {
	
	 private Integer id;
	 private Asana asana;
	 private ParticipantTeam participantTeam;
	 private float baseValue;
	 private Integer roundNumber;
	 private boolean compulsory;
	 private Integer sequenceNumber;
	 private ParticipantTeamParticipants participantTeamParticipants;
	 private String asanaCode;
	 private Integer subGroup;
	 private Integer compulsoryAsanaId;
	 
	
	  
 	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asana_id")  
    public Asana getAsana() {
        return asana;
    }
 
    public void setAsana(Asana asana) {
        this.asana = asana;
    }
 
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participantteam_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public ParticipantTeam getParticipantTeam() {
        return participantTeam;
    }
 
    public void setParticipantTeam(ParticipantTeam participantTeam) {
        this.participantTeam = participantTeam;
    }

	public float getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(float baseValue) {
		this.baseValue = baseValue;
	}

	

	public String getAsanaCode() {
		return asanaCode.toUpperCase();
	}

	public void setAsanaCode(String asanaCode) {
		this.asanaCode = asanaCode;
	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public boolean isCompulsory() {
		return compulsory;
	}

	public void setCompulsory(boolean compulsory) {
		this.compulsory = compulsory;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
		
	public Integer getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(Integer subGroup) {
		this.subGroup = subGroup;
	}
	
	
	

	public Integer getCompulsoryAsanaId() {
		return compulsoryAsanaId;
	}

	public void setCompulsoryAsanaId(Integer compulsoryAsanaId) {
		this.compulsoryAsanaId = compulsoryAsanaId;
	}

	@ManyToOne
    @JoinColumn(name = "participantTeamParticipants_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	public ParticipantTeamParticipants getParticipantTeamParticipants() {
		return participantTeamParticipants;
	}

	public void setParticipantTeamParticipants(ParticipantTeamParticipants participantTeamParticipants) {
		this.participantTeamParticipants = participantTeamParticipants;
	}
	
	
	
}
