package com.swayaan.nysf.entity.DTO;

public class EventManagerDTO {
	
	private Integer id;
	private String fullName;
	private String userName;
	

	public EventManagerDTO(Integer id, String fullName, String userName) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.userName = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
