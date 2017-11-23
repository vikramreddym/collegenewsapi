package com.clg.news.api.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clg.news.api.beans.admin.AdminLoginRespose;
import com.clg.news.api.model.Admin;
import com.clg.news.api.model.Notification;
import com.clg.news.api.model.Student;
import com.clg.news.api.model.Teacher;
import com.clg.news.api.services.AdminServices;
import com.clg.news.api.services.NotificationServices;
import com.clg.news.api.services.StudentServices;
import com.clg.news.api.services.TeacherServices;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminServices adminServices;

	@Autowired
	TeacherServices teacherServices;

	@Autowired
	NotificationServices notificationServices;

	@Autowired
	StudentServices studentServices;

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody AdminLoginRespose login(
			@RequestBody Admin customRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AdminLoginRespose adminResponse = new AdminLoginRespose();
		Admin admin = adminServices.adminLogin(customRequest.getUsername(),
				customRequest.getPassword());

		HttpSession session = request.getSession(true);

		if (admin != null) {

			adminResponse.setLogin(true);
			adminResponse.setMsg("login successful");
			adminResponse.setSessionId(session.getId());
			adminResponse.setAdminId(admin.getId());
			admin.setSessionId(session.getId());
			adminServices.updateAdmin(admin);
		} else {
			adminResponse.setLogin(false);
			adminResponse.setMsg("login unsuccessful");
		}
		return adminResponse;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody AdminLoginRespose logout(
			@RequestBody AdminLoginRespose customRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminLoginRespose adminResponse = new AdminLoginRespose();

		Admin admin = adminServices.getAdminById(customRequest.getAdminId());
		admin.setSessionId(null);
		adminServices.updateAdmin(admin);
		adminResponse.setMsg("admin logout");
		return adminResponse;
	}

	@RequestMapping(value = "/getNumbers", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody AdminLoginRespose getNumbers(
			@RequestBody AdminLoginRespose customRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminLoginRespose adminResponse = new AdminLoginRespose();

		Admin admin = adminServices.getAdminById(customRequest.getAdminId());
		if (admin != null
				&& admin.getSessionId().equals(customRequest.getSessionId())) {

			int loggedUsers = 0;
			List<Student> students = studentServices.getList();
			adminResponse.setStudents(students.size());
			for (Student student : students) {
				if (student.getSessionId() != null)
					loggedUsers++;
			}

			Set<Notification> notifications = notificationServices
					.getNotificationFromAdmin(customRequest.getAdminId());
			adminResponse.setNotifications(notifications.size());

			List<Teacher> teachers = teacherServices.getList();
			adminResponse.setTeachers(teachers.size());
			for (Teacher teacher : teachers) {
				if (teacher.getSessionId() != null)
					loggedUsers++;
			}

			adminResponse.setLoggedUsers(loggedUsers);
		}
		return adminResponse;
	}

}
