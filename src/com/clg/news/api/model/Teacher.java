package com.clg.news.api.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Proxy;

import com.clg.news.api.beans.teacher.TeacherRequest;

@Entity
@Table(name = "teacher")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy = false)
public class Teacher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2118109805726740818L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "sessionId")
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Notification.class, mappedBy = "fromTeacher", cascade = CascadeType.ALL)
	private Set<Notification> notification;

	public Teacher() {

	}

	public Teacher(TeacherRequest request) {
		if (request.getEmail() != null)
			this.email = request.getEmail();
		if (request.getName() != null)
			this.name = request.getName();
		if (request.getPassword() != null)
			this.password = request.getPassword();
		if (request.getUsername() != null)
			this.username = request.getUsername();
	}

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

	public Set<Notification> getNotification() {
		return notification;
	}

	public void setNotification(Set<Notification> notification) {
		this.notification = notification;
	}

}
