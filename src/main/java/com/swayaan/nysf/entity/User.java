package com.swayaan.nysf.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "users")
@DynamicUpdate(value=true)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;

	@Column(length = 128, nullable = false,unique = true)
	private String email;
	
	@Column(length = 128, nullable = false, unique = true)
	private String userName;
	
	private String gender;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "phone_number", length = 15)
	private String phoneNumber;
	
	
	private String image;
	
	@Column(name = "address_line_1",length = 64)
	private String addressLine1;
	
	@Column(name = "address_line_2", length = 64)
	private String addressLine2;
	
	@Column(name = "city",length = 45)
	private String city;
	
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
	
//	@ManyToOne
//	@JoinColumn(name = "country_id")
//	private Country country; 
	
	private boolean enabled;
	
	@Column(name = "verification_code", length = 64)
	private String verificationCode;

		
	@Column(name = "reset_password_token", length = 30)
	private String resetpasswordToken;
	
	private Date tokenGeneratedTime;
	
	private boolean isFirstLogin;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<>();
		
	private Date createdTime;
	
	private Date lastModifiedTime;
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name = "last_modified_by")
	private User lastModifiedBy;
	
	    
	public User() {
	}
		
	public User(String email, String password, String firstName, String lastName, boolean enabled, Set<Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

	
	public User(String email, String password, String firstName, String lastName, boolean enabled, Set<Role> roles,String userName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
		this.userName=userName;
	}
	
	
	public User(String email, String password, String firstName, String lastName, boolean enabled, Set<Role> roles,String userName,State state,District district) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
		this.userName=userName;
		this.district=district;
		this.state=state;
	}
	
	
	
	public User(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getResetpasswordToken() {
		return resetpasswordToken;
	}

	public void setResetpasswordToken(String resetpasswordToken) {
		this.resetpasswordToken = resetpasswordToken;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
		
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();
		
		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public String getRoleName() {
		if(!roles.isEmpty()) {
			Iterator<Role> iterator = roles.iterator();
				Role role = iterator.next();
				if (role.getName() != null) {
					return role.getName();
				}
		}
		return null;
	}
	
	@Transient
	public Integer getRoleId() {
		if(!roles.isEmpty()) {
			Iterator<Role> iterator = roles.iterator();
				Role role = iterator.next();
				if (role.getId() != null) {
					return role.getId();
				}
		}
		return null;
	}

	public Date getTokenGeneratedTime() {
		return tokenGeneratedTime;
	}

	public void setTokenGeneratedTime(Date tokenGeneratedTime) {
		this.tokenGeneratedTime = tokenGeneratedTime;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	

//	@Transient
//	public Boolean getIsJudge() {
//		if(!roles.isEmpty()) {
//			Iterator<Role> iterator = roles.iterator();
//				Role role = iterator.next();
//				if (role.getId() != null) {
//					return role.isJudge();
//				}
//		}
//		return false;
//	}
	

}
