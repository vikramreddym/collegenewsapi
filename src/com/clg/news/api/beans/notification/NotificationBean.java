package com.clg.news.api.beans.notification;

import java.text.SimpleDateFormat;

import com.clg.news.api.model.Notification;

public class NotificationBean {

	private Long id;

	private String msg;

	private String msgDate;

	// 0-both 1-only teacher 2-only student
	private Long type;

	private String fromAdmin;

	private String fromTeacher;

	SimpleDateFormat formate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public NotificationBean(Notification notification) {
		if (notification.getId() != null)
			this.id = notification.getId();
		if (notification.getFromAdmin() != null)
			this.fromAdmin = notification.getFromAdmin().getName();
		if (notification.getFromTeacher() != null)
			this.fromTeacher = notification.getFromTeacher().getName();
		if (notification.getMsg() != null)
			this.msg = notification.getMsg();
		if (notification.getMsgDate() != null)
			this.msgDate = formate.format(notification.getMsgDate());
		if (notification.getType() != null)
			this.type = notification.getType();
	}

	public NotificationBean() {

	}

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

	public String getFromAdmin() {
		return fromAdmin;
	}

	public void setFromAdmin(String fromAdmin) {
		this.fromAdmin = fromAdmin;
	}

	public String getFromTeacher() {
		return fromTeacher;
	}

	public void setFromTeacher(String fromTeacher) {
		this.fromTeacher = fromTeacher;
	}

}
