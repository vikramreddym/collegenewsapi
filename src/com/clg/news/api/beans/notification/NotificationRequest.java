package com.clg.news.api.beans.notification;

import java.util.List;

public class NotificationRequest {

	private Long id;

	private String msg;

	private String msgDate;

	// 0-both 1-only teacher 2-only student
	private Long type;

	private Long fromAdmin;

	private Long fromTeacher;

	private List<Long> subjects;

	private String sessionId;

	private Long studentId;

	private Long teacherId;

	private String fromDate;

	private Long adminId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getFromAdmin() {
		return fromAdmin;
	}

	public void setFromAdmin(Long fromAdmin) {
		this.fromAdmin = fromAdmin;
	}

	public Long getFromTeacher() {
		return fromTeacher;
	}

	public void setFromTeacher(Long fromTeacher) {
		this.fromTeacher = fromTeacher;
	}

	public List<Long> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Long> subjects) {
		this.subjects = subjects;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

}
