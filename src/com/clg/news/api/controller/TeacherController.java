package com.clg.news.api.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clg.news.api.beans.teacher.TeacherLoginResponse;
import com.clg.news.api.beans.teacher.TeacherRequest;
import com.clg.news.api.beans.teacher.TeacherResponse;
import com.clg.news.api.model.Admin;
import com.clg.news.api.model.Teacher;
import com.clg.news.api.services.AdminServices;
import com.clg.news.api.services.TeacherServices;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	TeacherServices teacherServices;

	@Autowired
	AdminServices adminServices;

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody TeacherLoginResponse login(
			@RequestBody Teacher customRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TeacherLoginResponse teacherResponse = new TeacherLoginResponse();
		Teacher teacher = teacherServices.teacherLogin(
				customRequest.getUsername(), customRequest.getPassword());

		if (teacher != null) {
			HttpSession session = request.getSession(true);
			teacherResponse.setTeacherId(teacher.getId());
			teacherResponse.setSessionId(session.getId());
			teacherResponse.setLogin(true);
			teacher.setSessionId(session.getId());
			teacherServices.updateTeacher(teacher);
		} else {
			teacherResponse.setLogin(false);
		}
		return teacherResponse;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody TeacherResponse logout(
			@RequestBody TeacherRequest customRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TeacherResponse teacherResponse = new TeacherResponse();

		Teacher teacher = teacherServices.getTeacherById(customRequest
				.getTeacherId());
		teacher.setSessionId(null);
		teacherServices.updateTeacher(teacher);
		teacherResponse.setMsg("teacher logout.");
		return teacherResponse;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody TeacherResponse addTeacher(
			@RequestBody TeacherRequest teacherRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(teacherRequest.getAdminId());

		TeacherResponse teacherResponse = new TeacherResponse();

		Properties prop = new Properties();
		InputStream inputStream = request.getSession().getServletContext()
				.getResourceAsStream("/WEB-INF/config.properties");
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException(
					"property file  not found in the classpath");
		}

		if (admin != null
				&& admin.getSessionId().equals(teacherRequest.getSessionId())) {

			if (teacherServices.uniqueEmail(teacherRequest.getEmail())
					&& teacherServices.uniqueUsername(teacherRequest
							.getUsername())) {
				Teacher teacher = new Teacher(teacherRequest);
				teacherServices.addTeacher(teacher);

				String bodyMsg = "Your username and password as follows: "
						+ "<br>username: " + teacher.getUsername()
						+ "<br>password: " + teacher.getPassword();
				String fromMail = prop.getProperty("from.mail");
				String smtpHost = prop.getProperty("smtp.host");
				String mailSmtpPort = prop.getProperty("mail.smtp.port");
				String username = prop.getProperty("username");
				String password = prop.getProperty("password");
				String auth = prop.getProperty("auth");
				String starttlsEnable = prop.getProperty("starttls.enable");

				String subject = "Credentials";
				adminServices.sendMail(teacher.getEmail(), bodyMsg, subject,
						fromMail, smtpHost, mailSmtpPort, username, password,
						auth, starttlsEnable);
				teacherResponse.setMsg("Teacher added");
			} else {
				teacherResponse.setMsg("username/email not unique");
			}
		}
		return teacherResponse;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody TeacherResponse update(
			@RequestBody TeacherRequest teacherRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(teacherRequest.getAdminId());

		TeacherResponse teacherResponse = new TeacherResponse();

		Properties prop = new Properties();
		InputStream inputStream = request.getSession().getServletContext()
				.getResourceAsStream("/WEB-INF/config.properties");
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException(
					"property file  not found in the classpath");
		}

		if (admin != null
				&& admin.getSessionId().equals(teacherRequest.getSessionId())) {
			Teacher oldTeacher = teacherServices.getTeacherById(teacherRequest
					.getTeacherId());

			if (oldTeacher != null) {
				if (teacherRequest.getEmail() != null
						&& !teacherRequest.getEmail().equals(
								oldTeacher.getEmail())) {
					if (teacherServices.uniqueEmail(teacherRequest.getEmail())) {
						oldTeacher.setEmail(teacherRequest.getEmail());
					} else {
						teacherResponse.setMsg("email not unique");
						return teacherResponse;
					}
				}
				if (teacherRequest.getName() != null)
					oldTeacher.setName(teacherRequest.getName());
				if (teacherRequest.getPassword() != null)
					oldTeacher.setPassword(teacherRequest.getPassword());
				if (teacherRequest.getUsername() != null) {
					if (!teacherRequest.getUsername().equals(
							teacherRequest.getUsername())) {
						if (teacherServices.uniqueUsername(teacherRequest
								.getUsername())) {
							oldTeacher
									.setUsername(teacherRequest.getUsername());
						} else {
							teacherResponse.setMsg("username not unique");
							return teacherResponse;
						}
					} else {
						oldTeacher.setUsername(teacherRequest.getUsername());
					}
				}
				teacherServices.updateTeacher(oldTeacher);
				teacherResponse.setMsg("teacher updated");
				return teacherResponse;
			} else {
				teacherResponse.setMsg("teacher not present");
				return teacherResponse;
			}
		} else {
			teacherResponse.setMsg("session expire");
			return teacherResponse;
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<Teacher> list(
			@RequestBody TeacherRequest teacherRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(teacherRequest.getAdminId());

		List<Teacher> list = new ArrayList<Teacher>();

		if (admin != null
				&& admin.getSessionId().equals(teacherRequest.getSessionId())) {
			list = teacherServices.getList();
		}
		return list;
	}

	@RequestMapping(value = "/getTeacher", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody Teacher getTeacher(
			@RequestBody TeacherRequest teacherRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(teacherRequest.getAdminId());

		Teacher teacher = null;

		if (admin != null
				&& admin.getSessionId().equals(teacherRequest.getSessionId())) {
			teacher = teacherServices.getTeacherById(teacherRequest
					.getTeacherId());
		}
		return teacher;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<Map<String, String>> delete(
			@RequestBody TeacherRequest teacherRequest,
			HttpServletRequest request, HttpServletResponse response) {

		Admin admin = adminServices.getAdminById(teacherRequest.getAdminId());

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		if (admin != null
				&& admin.getSessionId().equals(teacherRequest.getSessionId())) {
			for (Long teacherId : teacherRequest.getTeacherIds()) {
				Map<String, String> map = new HashMap<String, String>();
				if (teacherServices.delete(teacherId)) {
					map.put("result", "Teacher Id " + teacherId + " is deleted");
				} else {
					map.put("result", "Teacher Id " + teacherId
							+ " is not deleted");
				}
				result.add(map);
			}
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("result", "session expire");
			result.add(map);
		}
		return result;
	}

}
