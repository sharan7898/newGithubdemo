package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.AgeCategory;
import com.swayaan.nysf.entity.AsanaCategory;
import com.swayaan.nysf.entity.GenderEnum;

public class ChampionshipParticipantTeamsDTO {
	
	public Integer id;
	
	public String status;
	
	public Integer championshipRoundsId;
	
	public String championshipName;
	
	public Integer participantId;

	public String participantName;
	
	public AsanaCategory asanaCategory;

	public String chestNumber;
	
	public GenderEnum gender;
	
	public AgeCategory category;
	
	
	public ChampionshipParticipantTeamsDTO(Integer id, String status, Integer championshipRoundsId, String championshipName,
			Integer participantId, String participantName, AsanaCategory asanaCategory, String chestNumber,
			GenderEnum gender, AgeCategory category) {
		super();
		this.id = id;
		this.status = status;
		this.championshipRoundsId = championshipRoundsId;
		this.championshipName = championshipName;
		this.participantId = participantId;
		this.participantName = participantName;
		this.asanaCategory = asanaCategory;
		this.chestNumber = chestNumber;
		this.gender = gender;
		this.category = category;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getChampionshipRoundsId() {
		return championshipRoundsId;
	}


	public void setChampionshipRoundsId(Integer championshipRoundsId) {
		this.championshipRoundsId = championshipRoundsId;
	}


	public String getChampionshipName() {
		return championshipName;
	}


	public void setChampionshipName(String championshipName) {
		this.championshipName = championshipName;
	}


	public Integer getParticipantId() {
		return participantId;
	}


	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}


	public String getParticipantName() {
		return participantName;
	}


	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}


	public AsanaCategory getAsanaCategory() {
		return asanaCategory;
	}


	public void setAsanaCategory(AsanaCategory asanaCategory) {
		this.asanaCategory = asanaCategory;
	}


	public String getChestNumber() {
		return chestNumber;
	}


	public void setChestNumber(String chestNumber) {
		this.chestNumber = chestNumber;
	}


	public GenderEnum getGender() {
		return gender;
	}


	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}


	public AgeCategory getCategory() {
		return category;
	}


	public void setCategory(AgeCategory category) {
		this.category = category;
	}


}
