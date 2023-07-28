package com.swayaan.nysf.entity.DTO;

public class ParticipantDTO {

	private Integer id;

	private String firstName;

	private String lastName;

	private String prnNumber;

	private String userPrnNumber;

	public ParticipantDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParticipantDTO(Integer id, String firstName, String lastName, String prnNumber, String userPrnNumber) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.prnNumber = prnNumber;
		this.userPrnNumber = userPrnNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPrnNumber() {
		return prnNumber;
	}

	public void setPrnNumber(String prnNumber) {
		this.prnNumber = prnNumber;
	}

	public String getUserPrnNumber() {
		return userPrnNumber;
	}

	public void setUserPrnNumber(String userPrnNumber) {
		this.userPrnNumber = userPrnNumber;
	}

}
