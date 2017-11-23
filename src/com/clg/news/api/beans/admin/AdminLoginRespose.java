package com.clg.news.api.beans.admin;

public class AdminLoginRespose {
	private String msg;
	private Boolean login;
	private String sessionId;
	private Long adminId;

	private Integer students;
	private Integer teachers;
	private Integer notifications;
	private Integer loggedUsers;

	public Integer getStudents() {
		return students;
	}

	public void setStudents(Integer students) {
		this.students = students;
	}

	public Integer getTeachers() {
		return teachers;
	}

	public void setTeachers(Integer teachers) {
		this.teachers = teachers;
	}

	public Integer getNotifications() {
		return notifications;
	}

	public void setNotifications(Integer notifications) {
		this.notifications = notifications;
	}

	public Integer getLoggedUsers() {
		return loggedUsers;
	}

	public void setLoggedUsers(Integer loggedUsers) {
		this.loggedUsers = loggedUsers;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Boolean getLogin() {
		return login;
	}

	public void setLogin(Boolean login) {
		this.login = login;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
}
