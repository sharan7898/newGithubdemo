package com.swayaan.nysf.entity.DTO;

public class UserAuditLogDTO {

	private Integer id;

	private String userName;

	private String loginTime;

	private String logoutTime;
	
	private String ipAddress;
	
	public UserAuditLogDTO() {
		super();
	}

	public UserAuditLogDTO(Integer id, String userName, String loginTime, String logoutTime, String ipAddress) {
		super();
		this.id = id;
		this.userName = userName;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.ipAddress = ipAddress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}