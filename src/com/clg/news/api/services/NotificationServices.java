package com.clg.news.api.services;

import java.util.Date;
import java.util.Set;

import com.clg.news.api.model.Notification;

public interface NotificationServices {
	void addNotification(Notification notificaion);

	void updateNotificaion(Notification notification);

	Notification getNotificationById(long notificationId);

	Set<Notification> getNotificationByType(long type);

	Set<Notification> getNotificationBySubjects(long subjectId);

	Set<Notification> getNotificationFromTeacher(long teacherId);

	Set<Notification> getNotificationFromAdmin(long adminId);

	Set<Notification> getNotificationByTypeAndDate(long type, Date toDate);

	Set<Notification> getNotificationByTypeAndDate(long type, Date fromDate,
			Date toDate);

	Set<Notification> getNotificationBySubjectsAndDate(long subjectId,
			Date toDate);

	Set<Notification> getNotificationBySubjectsAndDate(long subjectId,
			Date fromDate, Date toDate);

	Set<Notification> getNotificationFromTeacherAndDate(long teacherId,
			Date toDate);

	Set<Notification> getNotificationFromAdminAndDate(long adminId, Date toDate);

	Set<Notification> getNotificationSearch(String search);
}
