package com.swayaan.nysf.entity.DTO;




public class NotificationDTO{
	
	private Integer notificationCount;
	public NotificationDTO(Integer notificationCount) {
		super();
		this.notificationCount = notificationCount;
		
	}
	public NotificationDTO() {
		super();
	}
	public Integer getNotificationCount() {
		return notificationCount;
	}
	public void setNotificationCount(Integer notificationCount) {
		this.notificationCount = notificationCount;
	}
	@Override
	public String toString() {
		return "NotificationDTO [notificationCount=" + notificationCount + "]";
	}
	
}