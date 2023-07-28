package com.swayaan.nysf.entity.DTO;

import com.swayaan.nysf.entity.Asana;

public class AsanaCodeDTO {
	
	private Asana asana;
	private String code;
	private Integer subGroup;
	
	public AsanaCodeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AsanaCodeDTO(Asana asana, String code) {
		super();
		this.asana = asana;
		this.code = code;
	}
	
	public AsanaCodeDTO(Asana asana, String code, Integer subGroup) {
		super();
		this.asana = asana;
		this.code = code;
		this.subGroup = subGroup;
	}

	public Asana getAsana() {
		return asana;
	}

	public void setAsana(Asana asana) {
		this.asana = asana;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(Integer subGroup) {
		this.subGroup = subGroup;
	}

	@Override
	public String toString() {
		return "AsanaCodeDTO [asana=" + asana + ", code=" + code + ", subGroup=" + subGroup + "]";
	}
	
	
	

}
