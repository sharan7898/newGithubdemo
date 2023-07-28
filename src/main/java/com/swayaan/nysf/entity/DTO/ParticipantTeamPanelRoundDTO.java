package com.swayaan.nysf.entity.DTO;

import java.util.List;
import java.util.Set;

import com.swayaan.nysf.entity.ChampionshipRefereePanels;
import com.swayaan.nysf.entity.ParticipantTeam;

public class ParticipantTeamPanelRoundDTO {
	
	private Set<Integer> listRoundNumber;
	private List<PanelParticipantTeamDTO> listParticipantTeam;
	private List<ChampionshipRefereePanelDTO> listChampionshipRefereePanels;
	public Set<Integer> getListRoundNumber() {
		return listRoundNumber;
	}
	public void setListRoundNumber(Set<Integer> listRoundNumber) {
		this.listRoundNumber = listRoundNumber;
	}
	
	public List<PanelParticipantTeamDTO> getListParticipantTeam() {
		return listParticipantTeam;
	}
	public void setListParticipantTeam(List<PanelParticipantTeamDTO> listParticipantTeam) {
		this.listParticipantTeam = listParticipantTeam;
	}
	
	public List<ChampionshipRefereePanelDTO> getListChampionshipRefereePanels() {
		return listChampionshipRefereePanels;
	}
	public void setListChampionshipRefereePanels(List<ChampionshipRefereePanelDTO> listChampionshipRefereePanels) {
		this.listChampionshipRefereePanels = listChampionshipRefereePanels;
	}
	public ParticipantTeamPanelRoundDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ParticipantTeamPanelRoundDTO(Set<Integer> listRoundNumber, List<PanelParticipantTeamDTO> listParticipantTeam,
			List<ChampionshipRefereePanelDTO> listChampionshipRefereePanels) {
		super();
		this.listRoundNumber = listRoundNumber;
		this.listParticipantTeam = listParticipantTeam;
		this.listChampionshipRefereePanels = listChampionshipRefereePanels;
	}
	
	
	}
