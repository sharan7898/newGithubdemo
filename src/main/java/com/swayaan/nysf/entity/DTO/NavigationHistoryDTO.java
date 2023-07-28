package com.swayaan.nysf.entity.DTO;

public class NavigationHistoryDTO {

	private Integer id;

	private String userName;

	private String roleName;

	private String url;
	
	private String createdTime;
	
	public NavigationHistoryDTO() {
		super();
	}

	public NavigationHistoryDTO(Integer id, String userName, String roleName, String url, String createdTime) {
		super();
		this.id = id;
		this.userName = userName;
		this.roleName = roleName;
		this.url = url;
		this.createdTime = createdTime;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	

}