package com.clg.news.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "notification")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy = false)
public class Notification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5697158119270198779L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "msg", length = 1000)
	private String msg;

	@Column(name = "msgDate")
	private Date msgDate;

	// 0-both 1-only teacher 2-only student
	@Column(name = "type")
	private Long type;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Admin fromAdmin;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Teacher fromTeacher;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "notificaion_subject", joinColumns = { @JoinColumn(name = "notification_id") }, inverseJoinColumns = { @JoinColumn(name = "subject_id") })
	private List<Subject> subjects;

	@Override
	public boolean equals(Object o) {
		if ((o instanceof Notification)
				&& (((Notification) o).getId() == this.id)) {
			return true;
		} else {
			return false;
		}

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

	public Date getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Admin getFromAdmin() {
		return fromAdmin;
	}

	public void setFromAdmin(Admin fromAdmin) {
		this.fromAdmin = fromAdmin;
	}

	public Teacher getFromTeacher() {
		return fromTeacher;
	}

	public void setFromTeacher(Teacher fromTeacher) {
		this.fromTeacher = fromTeacher;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

}
