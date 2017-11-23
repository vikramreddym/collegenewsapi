package com.clg.news.api.beans.teacher;

import java.util.List;

public class TeacherRequest {
	private Long id;

	private String name;

	private String username;

	private String password;

	private String email;

	private String sessionId;

	private Long adminId;

	private Long teacherId;

	private List<Long> teacherIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public List<Long> getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(List<Long> teacherIds) {
		this.teacherIds = teacherIds;
	}
}
