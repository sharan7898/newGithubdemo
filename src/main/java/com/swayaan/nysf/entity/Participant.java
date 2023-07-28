package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "participants")
@DynamicUpdate(value=true)
public class Participant extends User {
		
	@Column(name = "dob", length = 64, nullable = false)
	private Date dob;
			
	@Column(length = 45)
	private String aadharNumber;
		
	@Column(length = 128)
	private String birthCertificate;
		
	private boolean physicallyFit;
	
	@Column(length = 128)
	private String medicalCertificate;
	
	@Column(length = 128)
	private String paymentReceipt;
	
	@Column(length = 128)
	private String paymentTransactionId;
	
	@Column(length = 128)
	private String certificateNumber;
	
	private boolean acceptRules;
			
	private boolean approvalStatus;
	
	private String prnNumber;
	
	private String userPrnNumber;
	
	
	
//	@ManyToOne
//	@JoinColumn(name = "age_category_id")
//	private AgeCategory category;
	
//	@Column(length = 20)
//	private String kheloIndiaId;
	
	
	


	public Participant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Participant(String email, String password, String firstName, String lastName, boolean enabled,
			Set<Role> roles) {
		super(email, password, firstName, lastName, enabled, roles);
		// TODO Auto-generated constructor stub
	}

	public Participant(String email, String password, String firstName, String lastName) {
		super(email, password, firstName, lastName);
		// TODO Auto-generated constructor stub
	}

	
	
	public Participant(Date dob, String aadharNumber, String birthCertificate, boolean physicallyFit,
			String medicalCertificate, String paymentReceipt, String paymentTransactionId, String certificateNumber,
			boolean acceptRules, boolean approvalStatus, String prnNumber, String userPrnNumber) {
		super();
		this.dob = dob;
		this.aadharNumber = aadharNumber;
		this.birthCertificate = birthCertificate;
		this.physicallyFit = physicallyFit;
		this.medicalCertificate = medicalCertificate;
		this.paymentReceipt = paymentReceipt;
		this.paymentTransactionId = paymentTransactionId;
		this.certificateNumber = certificateNumber;
		this.acceptRules = acceptRules;
		this.approvalStatus = approvalStatus;
		this.prnNumber = prnNumber;
		this.userPrnNumber = userPrnNumber;

	}	

	public String getPrnNumber() {
		return prnNumber;
	}

	public void setPrnNumber(String prnNumber) {
		this.prnNumber = prnNumber;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	
	public String getBirthCertificate() {
		return birthCertificate;
	}

	public void setBirthCertificate(String birthCertificate) {
		this.birthCertificate = birthCertificate;
	}

	
	public boolean isPhysicallyFit() {
		return physicallyFit;
	}

	public void setPhysicallyFit(boolean physicallyFit) {
		this.physicallyFit = physicallyFit;
	}

	public String getMedicalCertificate() {
		return medicalCertificate;
	}

	public void setMedicalCertificate(String medicalCertificate) {
		this.medicalCertificate = medicalCertificate;
	}

	public String getPaymentReceipt() {
		return paymentReceipt;
	}

	public void setPaymentReceipt(String paymentReceipt) {
		this.paymentReceipt = paymentReceipt;
	}

	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public boolean isAcceptRules() {
		return acceptRules;
	}

	public void setAcceptRules(boolean acceptRules) {
		this.acceptRules = acceptRules;
	}

	
	public boolean isApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	
	public String getUserPrnNumber() {
		return userPrnNumber;
	}

	public void setUserPrnNumber(String userPrnNumber) {
		this.userPrnNumber = userPrnNumber;
	}


	@Transient
	public String getImagePath() {
		if (this.getEmail() == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.getEmail() + "/" + this.getImage();
	}
	@Transient
	public String getImagePath2() {
		if (this.getEmail() == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.getEmail() + "/" + this.birthCertificate;
	}
	
	@Transient
	public String getImagePath3() {
		if (this.getEmail() == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.getEmail() + "/" + this.medicalCertificate;
	}
	
	@Transient
	public String getImagePath4() {
		if (this.getEmail() == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.getEmail() + "/" + this.paymentReceipt;
	}
	
	
	@Override
	public String toString() {
		return "Participant [dob=" + dob + ", aadharNumber=" + aadharNumber + ", birthCertificate=" + birthCertificate
				+ ", physicallyFit=" + physicallyFit + ", medicalCertificate=" + medicalCertificate
				+ ", paymentReceipt=" + paymentReceipt + ", paymentTransactionId=" + paymentTransactionId
				+ ", certificateNumber=" + certificateNumber + ", acceptRules=" + acceptRules + ", approvalStatus="
				+ approvalStatus + ", prnNumber=" + prnNumber + ", getId()=" + getId()
				+ ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName() + ", getEmail()="
				+ getEmail() + ", getPassword()=" + getPassword() + ", getPhoneNumber()=" + getPhoneNumber()
				+ ", getImage()=" + getImage() + ", isEnabled()=" + isEnabled() + ", getResetpasswordToken()="
				+ getResetpasswordToken() + ", getRoles()=" + getRoles() + ", getCreatedTime()=" + getCreatedTime()
				+ ", getLastModifiedTime()=" + getLastModifiedTime() + ", getCreatedBy()=" + getCreatedBy()
				+ ", getLastModifiedBy()=" + getLastModifiedBy() + ", getAddressLine1()=" + getAddressLine1()
				+ ", getAddressLine2()=" + getAddressLine2() + ", getCity()=" + getCity() + ", getState()=" + getState()
				+ ", getPostalCode()=" + getPostalCode() + ", getUserName()=" + getUserName() + ", getTown()="
				+ getTown() + ", getDistrict()=" + getDistrict() + ", getGender()=" + getGender()
				+ ", getVerificationCode()=" + getVerificationCode() + ", getFullName()=" + getFullName()
				+ ", getRoleName()=" + getRoleName() + ", getRoleId()=" + getRoleId() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
}
