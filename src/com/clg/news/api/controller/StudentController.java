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

import com.clg.news.api.beans.student.StudentRequest;
import com.clg.news.api.beans.student.StudentResponse;
import com.clg.news.api.model.Admin;
import com.clg.news.api.model.Student;
import com.clg.news.api.model.Subject;
import com.clg.news.api.services.AdminServices;
import com.clg.news.api.services.StudentServices;
import com.clg.news.api.services.SubjectServices;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	SubjectServices subjectServices;

	@Autowired
	StudentServices studentServices;

	@Autowired
	AdminServices adminServices;

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody StudentResponse addStudent(
			@RequestBody StudentRequest studentRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(studentRequest.getAdminId());

		StudentResponse studentResponse = new StudentResponse();

		Properties prop = new Properties();
		InputStream inputStream = request.getSession().getServletContext()
				.getResourceAsStream("/WEB-INF/config.properties");
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException(
					"property file  not found in the classpath");
		}

		if (admin.getSessionId().equals(studentRequest.getSessionId())) {

			if (studentServices.uniqueEmail(studentRequest.getEmail())
					&& studentServices.uniqueUsername(studentRequest
							.getUsername())) {
				Student student = new Student(studentRequest);
				if (studentRequest.getSubjectIds() == null
						&& studentRequest.getSubjectIds().size() == 0) {
					studentResponse.setMsg("subjects should be present");
					return studentResponse;
				}
				List<Subject> subjects = new ArrayList<Subject>();
				for (Long subjectId : studentRequest.getSubjectIds()) {
					Subject subject = subjectServices.getSubjectById(subjectId);
					subjects.add(subject);
				}
				student.setSubjects(subjects);
				studentServices.add(student);
				String bodyMsg = "Your username and password as follows: "
						+ "<br>username: " + student.getUsername()
						+ "<br>password: " + student.getPassword();
				String fromMail = prop.getProperty("from.mail");
				String smtpHost = prop.getProperty("smtp.host");
				String mailSmtpPort = prop.getProperty("mail.smtp.port");
				String username = prop.getProperty("username");
				String password = prop.getProperty("password");
				String auth = prop.getProperty("auth");
				String starttlsEnable = prop.getProperty("starttls.enable");

				String subject = "Credentials";
				adminServices.sendMail(student.getEmail(), bodyMsg, subject,
						fromMail, smtpHost, mailSmtpPort, username, password,
						auth, starttlsEnable);

				studentResponse.setMsg("add student successfully");
				studentResponse.setSessionId(studentRequest.getSessionId());
			} else {
				studentResponse.setMsg("username and email not unique");
			}
		} else {
			studentResponse.setMsg("session not valid");
		}
		return studentResponse;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody StudentResponse login(
			@RequestBody Student customRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		StudentResponse studentResponse = new StudentResponse();
		Student student = studentServices.studentLogin(
				customRequest.getUsername(), customRequest.getPassword());

		if (student != null) {
			HttpSession session = request.getSession(true);
			studentResponse.setStudentId(student.getId());
			studentResponse.setSessionId(session.getId());
			student.setSessionId(session.getId());
			studentServices.update(student);
			studentResponse.setLogin(true);
		} else {
			studentResponse.setLogin(false);
		}
		return studentResponse;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody StudentResponse logout(
			@RequestBody StudentRequest customRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StudentResponse studentResponse = new StudentResponse();

		Student student = studentServices.getStudentById(customRequest
				.getStudentId());
		student.setSessionId(null);
		studentServices.update(student);
		studentResponse.setMsg("student logout.");
		return studentResponse;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody StudentResponse update(
			@RequestBody StudentRequest studentRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(studentRequest.getAdminId());

		StudentResponse studentResponse = new StudentResponse();

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
				&& admin.getSessionId().equals(studentRequest.getSessionId())) {
			Student oldStudent = studentServices.getStudentById(studentRequest
					.getStudentId());

			if (oldStudent != null) {
				if (studentRequest.getEmail() != null
						&& !studentRequest.getEmail().equals(
								oldStudent.getEmail())) {
					if (studentServices.uniqueEmail(studentRequest.getEmail())) {
						oldStudent.setEmail(studentRequest.getEmail());
					} else {
						studentResponse.setMsg("email not unique");
						return studentResponse;
					}
				}
				if (studentRequest.getName() != null)
					oldStudent.setName(studentRequest.getName());
				if (studentRequest.getPassword() != null)
					oldStudent.setPassword(studentRequest.getPassword());
				if (studentRequest.getUsername() != null) {
					if (!studentRequest.getUsername().equals(
							studentRequest.getUsername())) {
						if (studentServices.uniqueUsername(studentRequest
								.getUsername())) {
							oldStudent
									.setUsername(studentRequest.getUsername());
						} else {
							studentResponse.setMsg("username not unique");
							return studentResponse;
						}
					} else {
						oldStudent.setUsername(studentRequest.getUsername());
					}
				}
				List<Subject> subjects = new ArrayList<Subject>();
				if (studentRequest.getSubjectIds() != null) {
					for (Long subjectId : studentRequest.getSubjectIds()) {
						Subject subject = subjectServices
								.getSubjectById(subjectId);
						subjects.add(subject);
					}
				}
				oldStudent.setSubjects(subjects);
				studentServices.update(oldStudent);
				studentResponse.setMsg("student updated");
				return studentResponse;
			} else {
				studentResponse.setMsg("student not present");
				return studentResponse;
			}
		} else {
			studentResponse.setMsg("session expire");
			return studentResponse;
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<Map<String, Object>> list(
			@RequestBody StudentRequest studentRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(studentRequest.getAdminId());

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Properties prop = new Properties();
		InputStream inputStream = request.getSession().getServletContext()
				.getResourceAsStream("/WEB-INF/config.properties");
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException(
					"property file  not found in the classpath");
		}

		if (admin.getSessionId().equals(studentRequest.getSessionId())) {
			List<Student> students = studentServices.getList();
			for (Student student : students) {
				List<Subject> subjects = student.getSubjects();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("student", student);
				map.put("subjects", subjects);
				list.add(map);
			}

		}
		return list;
	}

	@RequestMapping(value = "/getStudent", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody Map<String, Object> getStudent(
			@RequestBody StudentRequest studentRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(studentRequest.getAdminId());

		Map<String, Object> map = new HashMap<String, Object>();
		Student student = null;

		Properties prop = new Properties();
		InputStream inputStream = request.getSession().getServletContext()
				.getResourceAsStream("/WEB-INF/config.properties");
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException(
					"property file  not found in the classpath");
		}

		if (admin.getSessionId().equals(studentRequest.getSessionId())) {
			student = studentServices.getStudentById(studentRequest
					.getStudentId());
			map.put("student", student);
			List<Subject> subjects = student.getSubjects();
			for (Subject subject : subjects) {
				System.out.println(subject.getName());
			}
			map.put("subjects", subjects);
		}
		return map;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<Map<String, String>> delete(
			@RequestBody StudentRequest studentRequest,
			HttpServletRequest request, HttpServletResponse response) {

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		Admin admin = adminServices.getAdminById(studentRequest.getAdminId());

		if (admin != null
				&& admin.getSessionId().equals(studentRequest.getSessionId())) {

			for (Long studentId : studentRequest.getSubjectIds()) {
				Map<String, String> map = new HashMap<String, String>();
				if (studentServices.delete(studentId)) {
					map.put("result", "Student Id " + studentId + " is deleted");
				} else {
					map.put("result", "Student Id " + studentId
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
