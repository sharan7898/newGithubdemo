package com.swayaan.nysf.entity.DTO;

import java.util.Date;
import java.util.List;



public class ExcelToDBImportDTO {

	
	private String firstName;
	
	private String lastName;

	private Date dob;
	
	private String email;
	
	private String userPrnNumber;
	
	private String gender;

	private String district;
	
	private String state;
	
	private String note;

	
	
	public ExcelToDBImportDTO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ExcelToDBImportDTO(String note) {
		super();
		this.note = note;
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



	public Date getDob() {
		return dob;
	}



	public void setDob(Date dob) {
		this.dob = dob;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getUserPrnNumber() {
		return userPrnNumber;
	}



	public void setUserPrnNumber(String userPrnNumber) {
		this.userPrnNumber = userPrnNumber;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public String getDistrict() {
		return district;
	}



	public void setDistrict(String district) {
		this.district = district;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	@Override
	public String toString() {
		return "ExcelToDBImportDTO [firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", email="
				+ email + ", userPrnNumber=" + userPrnNumber + ", gender=" + gender + ", district=" + district
				+ ", state=" + state + "]";
	}
	
	
}
