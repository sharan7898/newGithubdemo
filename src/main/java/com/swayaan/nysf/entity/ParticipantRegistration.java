package com.swayaan.nysf.entity;

import java.util.Date;
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

@Entity
@Table(name="participants_registration")
public class ParticipantRegistration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name", length = 64)
	private String firstName;

	@Column(name = "last_name", length = 64)
	private String lastName;
	
	@Column(name = "dob", length = 64)
	private Date dob;
	
	@Column(length = 128)
	private String email;
	
	private String aadharNumber;

	@Column(length = 128)
	private String image;
	
	@Column(length = 128)
	private String birthCertificate;
	
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
	
//	@Column(name = "state", length = 64, nullable = false)
//	private String state;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;
	
	@Column(name = "postal_code", length = 10)
	private String postalCode;	
	
	@Column(name = "phone_number", length = 15)
	private String phoneNumber;
	
//	@Enumerated(EnumType.STRING)
//	@Column( length = 45, nullable = false)
//	private CategoryEnum category;
	
//	@ManyToOne
//	@JoinColumn(name = "age_category_id")
//	private AgeCategory category;
	
//	@ManyToOne
//	@JoinColumn(name = "asana_category_id")
//	private AsanaCategory asanaCategory;
	
	private String urlTraditional;
	
	private String urlArtisticSingle;
	
	private String urlArtisticPair;
	
	private String otherArtisticPairPlayerName;
	
	private String urlRhythmicPair;
	
	private String otherRhythmicPairPlayerName;
	
	private String urlArtisticGroup;
	
	private String otherArtisticGroupPlayerName;
	
	private boolean physicallyFit;
	
	@Column(length = 128)
	private String medicalCertificate;
	
	@Column(length = 128)
	private String paymentReceipt;
	
	@Column(length = 128)
	private String paymentTransactionId;
	
	@Column(length = 128)
	private String paymentMode;
	
	@Column(length = 128)
	private String certificateNumber;
	
	private boolean acceptRules;
	
	private boolean enabled;
	
	private boolean approvalStatus;
	
	private int userPrnNumber;
	
	
	
	@Transient
	public String getImagePath() {
		if (this.email == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.email + "/" + this.image;
	}
	@Transient
	public String getImagePath1() {
		if (this.email == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.email + "/" + this.birthCertificate;
	}
	
	@Transient
	public String getImagePath2() {
		if (this.email == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.email + "/" + this.medicalCertificate;
	}
	
	@Transient
	public String getImagePath3() {
		if (this.email == null)
			return "/images/image-thumbnail.png";

		return "/participant-reg-uploads/" + this.email + "/" + this.paymentReceipt;
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

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBirthCertificate() {
		return birthCertificate;
	}

	public void setBirthCertificate(String birthCertificate) {
		this.birthCertificate = birthCertificate;
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



//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}

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

//	public CategoryEnum getCategory() {
//		return category;
//	}
//
//	public void setCategory(CategoryEnum category) {
//		this.category = category;
//	}
//	
//
//	public AgeCategory getCategory() {
//		return category;
//	}
//	public void setCategory(AgeCategory category) {
//		this.category = category;
//	}
//	
//	public AsanaCategory getAsanaCategory() {
//		return asanaCategory;
//	}
//	
//	public void setAsanaCategory(AsanaCategory asanaCategory) {
//		this.asanaCategory = asanaCategory;
//	}

	public String getUrlTraditional() {
		return urlTraditional;
	}

	public void setUrlTraditional(String urlTraditional) {
		this.urlTraditional = urlTraditional;
	}

	public String getUrlArtisticSingle() {
		return urlArtisticSingle;
	}

	public void setUrlArtisticSingle(String urlArtisticSingle) {
		this.urlArtisticSingle = urlArtisticSingle;
	}

	public String getUrlArtisticPair() {
		return urlArtisticPair;
	}

	public void setUrlArtisticPair(String urlArtisticPair) {
		this.urlArtisticPair = urlArtisticPair;
	}

	public String getOtherArtisticPairPlayerName() {
		return otherArtisticPairPlayerName;
	}

	public void setOtherArtisticPairPlayerName(String otherArtisticPairPlayerName) {
		this.otherArtisticPairPlayerName = otherArtisticPairPlayerName;
	}

	public String getUrlRhythmicPair() {
		return urlRhythmicPair;
	}

	public void setUrlRhythmicPair(String urlRhythmicPair) {
		this.urlRhythmicPair = urlRhythmicPair;
	}

	public String getOtherRhythmicPairPlayerName() {
		return otherRhythmicPairPlayerName;
	}

	public void setOtherRhythmicPairPlayerName(String otherRhythmicPairPlayerName) {
		this.otherRhythmicPairPlayerName = otherRhythmicPairPlayerName;
	}

	public String getUrlArtisticGroup() {
		return urlArtisticGroup;
	}

	public void setUrlArtisticGroup(String urlArtisticGroup) {
		this.urlArtisticGroup = urlArtisticGroup;
	}

	public String getOtherArtisticGroupPlayerName() {
		return otherArtisticGroupPlayerName;
	}

	public void setOtherArtisticGroupPlayerName(String otherArtisticGroupPlayerName) {
		this.otherArtisticGroupPlayerName = otherArtisticGroupPlayerName;
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
	

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
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
	
	public int getUserPrnNumber() {
		return userPrnNumber;
	}
	public void setUserPrnNumber(int userPrnNumber) {
		this.userPrnNumber = userPrnNumber;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	
	
	
	
}
