package com.swayaan.nysf.entity.DTO;

public class PanelParticipantTeamDTO {

	private Integer id;
	private String name;
	
	public PanelParticipantTeamDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PanelParticipantTeamDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
