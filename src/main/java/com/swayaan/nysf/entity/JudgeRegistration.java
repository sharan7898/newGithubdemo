package com.swayaan.nysf.entity;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="judges_registration")
public class JudgeRegistration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name", length = 64)
	private String firstName;

	@Column(name = "last_name", length = 64)
	private String lastName;
	
	@Column(length = 128)
	private String email;
		
	@Column(length = 128)
	private String image;
	
	private String gender;
	
	@Column(name = "address_line_1",length = 64)
	private String addressLine1;
	
	@Column(name = "address_line_2", length = 64)
	private String addressLine2;
	
	@Column(name = "town",length = 64)
	private String town;
	
	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;
	
	@Column(name = "postal_code", length = 10)
	private String postalCode;	
	
	@Column(name = "phone_number", length = 15)
	private String phoneNumber;
	
	private boolean enabled;
	
	private boolean approvalStatus;
	
	private String designation;
	
	private boolean acceptRules;
	
	@Column(length = 128)
	private String certificate;
	
		
	@Transient
	public String getImagePath() {
		if (this.email == null)
			return "/images/image-thumbnail.png";

		return "/judge-reg-uploads/" + this.email + "/" + this.image;
	}
	
	@Transient
	public String getImagePath1() {
		if (this.email == null)
			return "/images/image-thumbnail.png";

		return "/judge-reg-uploads/" + this.email + "/" + this.certificate;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}



	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public boolean isAcceptRules() {
		return acceptRules;
	}

	public void setAcceptRules(boolean acceptRules) {
		this.acceptRules = acceptRules;
	}
		

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	
}
