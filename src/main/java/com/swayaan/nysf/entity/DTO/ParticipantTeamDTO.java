package com.swayaan.nysf.entity.DTO;

import java.util.List;

import com.swayaan.nysf.entity.ParticipantTeamAsanas;

public class ParticipantTeamDTO {
	
	private Integer id;
	private List<ParticipantTeamAsanas> participantTeamAsanas;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<ParticipantTeamAsanas> getParticipantTeamAsanas() {
		return participantTeamAsanas;
	}
	public void setParticipantGroupAsanas(List<ParticipantTeamAsanas> participantGroupAsanas) {
		this.participantTeamAsanas = participantGroupAsanas;
	}
	

}
