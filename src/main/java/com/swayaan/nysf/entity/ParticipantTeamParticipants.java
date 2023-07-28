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
@Table(name = "Participantteam_participants")
public class ParticipantTeamParticipants {
	
    private Integer id;
    private Participant participant;
    private ParticipantTeam participantTeam;
    private Integer sequenceNumber;
    private Boolean isTeamLead; 
    
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
    @JoinColumn(name = "participant_id")  
    public Participant getParticipant() {
        return participant;
    }
 
    public void setParticipant(Participant participant) {
        this.participant = participant;
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
    
	@Transient
	public String getParticipantFullName() {
				if (participant.getFirstName() != null) {
					return participant.getFirstName() + " " + participant.getLastName();
				}
		return null;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Boolean getIsTeamLead() {
		return isTeamLead;
	}

	public void setIsTeamLead(Boolean isTeamLead) {
		this.isTeamLead = isTeamLead;
	}
	
	
}
