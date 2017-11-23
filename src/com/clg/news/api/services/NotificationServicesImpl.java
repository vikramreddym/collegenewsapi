package com.clg.news.api.services;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.dao.NotificationDao;
import com.clg.news.api.model.Notification;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("notificationServices")
public class NotificationServicesImpl implements NotificationServices {

	@Autowired
	NotificationDao notificationDao;

	@Override
	public void addNotification(Notification notificaion) {
		notificationDao.addNotification(notificaion);

	}

	@Override
	public void updateNotificaion(Notification notification) {
		notificationDao.updateNotificaion(notification);

	}

	@Override
	public Set<Notification> getNotificationByType(long type) {
		return notificationDao.getNotificationByType(type);
	}

	@Override
	public Set<Notification> getNotificationBySubjects(long subjectId) {
		return notificationDao.getNotificationBySubjects(subjectId);
	}

	@Override
	public Set<Notification> getNotificationFromTeacher(long teacherId) {
		return notificationDao.getNotificationFromTeacher(teacherId);
	}

	@Override
	public Set<Notification> getNotificationFromAdmin(long adminId) {
		return notificationDao.getNotificationFromAdmin(adminId);
	}

	@Override
	public Set<Notification> getNotificationSearch(String search) {
		return notificationDao.getNotificationSearch(search);
	}

	@Override
	public Notification getNotificationById(long notificationId) {
		return notificationDao.getNotificationById(notificationId);
	}

	@Override
	public Set<Notification> getNotificationByTypeAndDate(long type, Date toDate) {
		return notificationDao.getNotificationByTypeAndDate(type, toDate);
	}

	@Override
	public Set<Notification> getNotificationBySubjectsAndDate(long subjectId,
			Date toDate) {
		return notificationDao.getNotificationBySubjectsAndDate(subjectId,
				toDate);
	}

	@Override
	public Set<Notification> getNotificationFromTeacherAndDate(long teacherId,
			Date toDate) {
		return notificationDao.getNotificationFromTeacherAndDate(teacherId,
				toDate);
	}

	@Override
	public Set<Notification> getNotificationFromAdminAndDate(long adminId,
			Date toDate) {
		return notificationDao.getNotificationFromAdminAndDate(adminId, toDate);
	}

	@Override
	public Set<Notification> getNotificationByTypeAndDate(long type,
			Date fromDate, Date toDate) {
		return notificationDao.getNotificationByTypeAndDate(type, fromDate,
				toDate);
	}

	@Override
	public Set<Notification> getNotificationBySubjectsAndDate(long subjectId,
			Date fromDate, Date toDate) {
		return notificationDao.getNotificationBySubjectsAndDate(subjectId,
				fromDate, toDate);
	}

}
