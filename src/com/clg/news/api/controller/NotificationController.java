package com.clg.news.api.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clg.news.api.beans.NotificationSort;
import com.clg.news.api.beans.notification.NotificationBean;
import com.clg.news.api.beans.notification.NotificationRequest;
import com.clg.news.api.beans.notification.NotificationResponse;
import com.clg.news.api.model.Admin;
import com.clg.news.api.model.Notification;
import com.clg.news.api.model.Student;
import com.clg.news.api.model.Subject;
import com.clg.news.api.model.Teacher;
import com.clg.news.api.services.AdminServices;
import com.clg.news.api.services.NotificationServices;
import com.clg.news.api.services.StudentServices;
import com.clg.news.api.services.SubjectServices;
import com.clg.news.api.services.TeacherServices;

@Controller
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
	SubjectServices subjectServices;

	@Autowired
	StudentServices studentServices;

	@Autowired
	AdminServices adminServices;

	@Autowired
	TeacherServices teacherServices;

	@Autowired
	NotificationServices notificationServices;

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody NotificationResponse addNotification(
			@RequestBody NotificationRequest notificationRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = null;
		Teacher teacher = null;
		if (notificationRequest.getFromAdmin() != null) {
			admin = adminServices.getAdminById(notificationRequest
					.getFromAdmin());
		} else if (notificationRequest.getFromTeacher() != null) {
			teacher = teacherServices.getTeacherById(notificationRequest
					.getFromTeacher());
		}

		NotificationResponse notificationResponse = new NotificationResponse();
		if (admin == null && teacher == null) {
			notificationResponse.setMsg("session expire");
			return notificationResponse;
		}
		if (admin != null
				&& !admin.getSessionId().equals(
						notificationRequest.getSessionId())) {
			notificationResponse.setMsg("session expire");
			return notificationResponse;
		}
		if (teacher != null
				&& !teacher.getSessionId().equals(
						notificationRequest.getSessionId())) {
			notificationResponse.setMsg("session expire");
			return notificationResponse;
		}

		try {

			Notification notification = new Notification();
			SimpleDateFormat format = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			if (notificationRequest.getMsg() == null
					|| notificationRequest.getMsg().equals("")) {
				notificationResponse.setMsg("message should be present");
				return notificationResponse;
			}
			Long type = notificationRequest.getType();

			if (type == null || (type != 0 && type != 1 && type != 2)) {
				notificationResponse
						.setMsg("notification type is not present/notification type is wrong");
				return notificationResponse;
			}
			if (notificationRequest.getSubjects() == null
					|| notificationRequest.getSubjects().size() == 0) {
				notificationResponse.setMsg("subjects should be present");
				return notificationResponse;
			}
			List<Subject> subjects = new ArrayList<Subject>();
			for (Long subjectId : notificationRequest.getSubjects()) {
				Subject subject = subjectServices.getSubjectById(subjectId);
				if (subject != null) {
					subjects.add(subject);
				}
			}
			notification.setSubjects(subjects);
			notification.setType(notificationRequest.getType());
			notification.setMsg(notificationRequest.getMsg());
			String currentDateStr = format.format(new Date());
			Date currentDate = format.parse(currentDateStr);
			notification.setMsgDate(currentDate);

			if (admin != null) {
				notification.setFromAdmin(admin);
			} else if (teacher != null) {
				notification.setFromTeacher(teacher);
			}

			notificationServices.addNotification(notification);
			notificationResponse.setMsg("notification added");
			return notificationResponse;
		} catch (Exception e) {
			e.printStackTrace();
			notificationResponse.setMsg("notification not added");
			return notificationResponse;
		}
	}

	@RequestMapping(value = "/getNotificationStudent", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<NotificationBean> getNotificationStudent(
			@RequestBody NotificationRequest notificationRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Student student = studentServices.getStudentById(notificationRequest
				.getStudentId());

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (student == null
				|| !student.getSessionId().equals(
						notificationRequest.getSessionId())) {
			return null;
		}

		Set<Notification> notifications = new HashSet<Notification>();

		Set<Notification> notificationsTypeZero = null;
		Set<Notification> notificationsTypeTwo = null;
		if (notificationRequest.getFromDate() != null) {
			Date fromDate = format.parse(notificationRequest.getFromDate());
			notificationsTypeZero = notificationServices
					.getNotificationByTypeAndDate(0, fromDate);

			notificationsTypeTwo = notificationServices
					.getNotificationByTypeAndDate(2, fromDate);
		} else {
			notificationsTypeZero = notificationServices
					.getNotificationByType(0);
			notificationsTypeTwo = notificationServices
					.getNotificationByType(2);
		}
		for (Notification notification : notificationsTypeZero) {
			notifications.add(notification);
		}
		for (Notification notification : notificationsTypeTwo) {
			notifications.add(notification);
		}

		List<Subject> subjects = student.getSubjects();
		if (notificationRequest.getFromDate() != null) {
			Date fromDate = format.parse(notificationRequest.getFromDate());
			for (Subject subject : subjects) {
				Set<Notification> notificationsSubjects = notificationServices
						.getNotificationBySubjectsAndDate(subject.getId(),
								fromDate);
				for (Notification notification : notificationsSubjects) {
					notifications.add(notification);
				}
			}
		} else {
			for (Subject subject : subjects) {
				Set<Notification> notificationsSubjects = notificationServices
						.getNotificationBySubjects(subject.getId());
				for (Notification notification : notificationsSubjects) {
					notifications.add(notification);
				}
			}
		}
		Set<Long> ids = new HashSet<Long>();

		for (Notification notification : notifications) {

			ids.add(notification.getId());
		}

		List<Notification> list = new ArrayList<Notification>();
		for (Long id : ids) {
			Notification n = notificationServices.getNotificationById(id);
			list.add(n);
		}
		Collections.sort(list, new NotificationSort());
		List<NotificationBean> beanList = new LinkedList<NotificationBean>();

		int count = 0;
		for (Notification notification : list) {
			count++;
			if (count <= 10) {
				NotificationBean bean = new NotificationBean(notification);
				beanList.add(bean);
			}
		}

		return beanList;
	}

	@RequestMapping(value = "/getNotificationStudentRecent", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<NotificationBean> getNotificationStudentRecent(
			@RequestBody NotificationRequest notificationRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Student student = studentServices.getStudentById(notificationRequest
				.getStudentId());

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (student == null
				|| !student.getSessionId().equals(
						notificationRequest.getSessionId())) {
			return null;
		}

		Set<Notification> notifications = new HashSet<Notification>();

		Set<Notification> notificationsTypeZero = null;
		Set<Notification> notificationsTypeTwo = null;
		Date toDate = null;
		if (notificationRequest.getFromDate() != null) {
			Date fromDate = format.parse(notificationRequest.getFromDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromDate);
			cal.set(Calendar.MINUTE, -5);
			toDate = cal.getTime();

			notificationsTypeZero = notificationServices
					.getNotificationByTypeAndDate(0, fromDate, toDate);

			notificationsTypeTwo = notificationServices
					.getNotificationByTypeAndDate(2, fromDate, toDate);
		}
		if (notificationsTypeZero != null) {
			for (Notification notification : notificationsTypeZero) {
				notifications.add(notification);
			}
		}
		if (notificationsTypeTwo != null) {
			for (Notification notification : notificationsTypeTwo) {
				notifications.add(notification);
			}
		}

		List<Subject> subjects = student.getSubjects();
		if (notificationRequest.getFromDate() != null) {
			Date fromDate = format.parse(notificationRequest.getFromDate());
			for (Subject subject : subjects) {
				Set<Notification> notificationsSubjects = notificationServices
						.getNotificationBySubjectsAndDate(subject.getId(),
								fromDate, toDate);
				if (notificationsSubjects != null) {
					for (Notification notification : notificationsSubjects) {
						notifications.add(notification);
					}
				}
			}
		}
		Set<Long> ids = new HashSet<Long>();

		for (Notification notification : notifications) {

			ids.add(notification.getId());
		}

		List<Notification> list = new ArrayList<Notification>();
		for (Long id : ids) {
			Notification n = notificationServices.getNotificationById(id);
			list.add(n);
		}
		Collections.sort(list, new NotificationSort());
		List<NotificationBean> beanList = new LinkedList<NotificationBean>();

		int count = 0;
		for (Notification notification : list) {
			count++;
			if (count <= 10) {
				NotificationBean bean = new NotificationBean(notification);
				beanList.add(bean);
			}
		}

		return beanList;
	}

	@RequestMapping(value = "/getNotificationTeacher", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<NotificationBean> getNotificationTeacher(
			@RequestBody NotificationRequest notificationRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Teacher teacher = teacherServices.getTeacherById(notificationRequest
				.getTeacherId());

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (teacher == null
				|| !teacher.getSessionId().equals(
						notificationRequest.getSessionId())) {
			return null;
		}

		Set<Notification> notifications = new HashSet<Notification>();

		Set<Notification> notificationsTypeZero = null;
		Set<Notification> notificationsTypeOne = null;
		if (notificationRequest.getFromDate() != null) {
			Date fromDate = format.parse(notificationRequest.getFromDate());
			notificationsTypeZero = notificationServices
					.getNotificationByTypeAndDate(0, fromDate);

			notificationsTypeOne = notificationServices
					.getNotificationByTypeAndDate(1, fromDate);
		} else {
			notificationsTypeZero = notificationServices
					.getNotificationByType(0);
			notificationsTypeOne = notificationServices
					.getNotificationByType(1);
		}
		for (Notification notification : notificationsTypeZero) {
			notifications.add(notification);
		}
		for (Notification notification : notificationsTypeOne) {
			notifications.add(notification);
		}

		Set<Long> ids = new HashSet<Long>();

		for (Notification notification : notifications) {

			ids.add(notification.getId());
		}

		List<Notification> list = new ArrayList<Notification>();
		for (Long id : ids) {
			Notification n = notificationServices.getNotificationById(id);
			list.add(n);
		}
		Collections.sort(list, new NotificationSort());
		List<NotificationBean> beanList = new LinkedList<NotificationBean>();

		int count = 0;
		for (Notification notification : list) {
			count++;
			if (count <= 10) {
				NotificationBean bean = new NotificationBean(notification);
				beanList.add(bean);
			}
		}

		return beanList;
	}

	@RequestMapping(value = "/getNotificationTeacherRecent", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<NotificationBean> getNotificationTeacherRecent(
			@RequestBody NotificationRequest notificationRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Teacher teacher = teacherServices.getTeacherById(notificationRequest
				.getTeacherId());

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (teacher == null
				|| !teacher.getSessionId().equals(
						notificationRequest.getSessionId())) {
			return null;
		}

		Set<Notification> notifications = new HashSet<Notification>();

		Set<Notification> notificationsTypeZero = null;
		Set<Notification> notificationsTypeOne = null;
		Date toDate = null;
		if (notificationRequest.getFromDate() != null) {
			Date fromDate = format.parse(notificationRequest.getFromDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromDate);
			cal.set(Calendar.MINUTE, -5);
			toDate = cal.getTime();
			notificationsTypeZero = notificationServices
					.getNotificationByTypeAndDate(0, fromDate, toDate);

			notificationsTypeOne = notificationServices
					.getNotificationByTypeAndDate(1, fromDate, toDate);
		}
		for (Notification notification : notificationsTypeZero) {
			notifications.add(notification);
		}
		for (Notification notification : notificationsTypeOne) {
			notifications.add(notification);
		}

		Set<Long> ids = new HashSet<Long>();

		for (Notification notification : notifications) {

			ids.add(notification.getId());
		}

		List<Notification> list = new ArrayList<Notification>();
		for (Long id : ids) {
			Notification n = notificationServices.getNotificationById(id);
			list.add(n);
		}
		Collections.sort(list, new NotificationSort());
		List<NotificationBean> beanList = new LinkedList<NotificationBean>();

		int count = 0;
		for (Notification notification : list) {
			count++;
			if (count <= 10) {
				NotificationBean bean = new NotificationBean(notification);
				beanList.add(bean);
			}
		}

		return beanList;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<NotificationBean> getNotification(
			@RequestBody NotificationRequest notificationRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = adminServices.getAdminById(notificationRequest
				.getAdminId());

		Set<Notification> list = new HashSet<Notification>();

		Properties prop = new Properties();
		InputStream inputStream = request.getSession().getServletContext()
				.getResourceAsStream("/WEB-INF/config.properties");
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException(
					"property file  not found in the classpath");
		}

		if (admin.getSessionId().equals(notificationRequest.getSessionId())) {
			list = notificationServices
					.getNotificationFromAdmin(notificationRequest.getAdminId());
		}

		List<NotificationBean> notifications = new ArrayList<NotificationBean>();
		for (Notification notification : list) {
			NotificationBean bean = new NotificationBean(notification);
			notifications.add(bean);
		}
		return notifications;
	}
}
