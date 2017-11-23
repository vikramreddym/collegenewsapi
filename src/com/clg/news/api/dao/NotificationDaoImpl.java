package com.clg.news.api.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.model.Notification;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NotificationDaoImpl implements NotificationDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public void addNotification(Notification notificaion) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(notificaion);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateNotificaion(Notification notification) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(notification);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<Notification> getNotificationByType(long type) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);

		criteria.add(Restrictions.eq("type", type));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationBySubjects(long subjectId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		// Criteria c = session.createCriteria(Notification.class);

		Criteria criteria = session.createCriteria(Notification.class);

		criteria.createAlias("subjects", "subject");
		criteria.add(Restrictions.eq("subject.id", subjectId));

		List<Notification> list = criteria.list();

		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationFromTeacher(long teacherId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);

		criteria.createAlias("fromTeacher", "teacher");
		criteria.add(Restrictions.eq("teacher.id", teacherId));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationFromAdmin(long adminId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);

		criteria.createAlias("fromAdmin", "admin");
		criteria.add(Restrictions.eq("admin.id", adminId));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationSearch(String search) {
		Session session;
		session = sessionFactory.openSession();
		String hql = "FROM Employee E WHERE E.id = 10";
		Query query = session.createQuery(hql);
		List<Notification> list = query.list();
		Set<Notification> notifications = new HashSet<Notification>(list);
		session.close();
		return notifications;
	}

	@Override
	public Notification getNotificationById(long notificationId) {
		Session session;
		Notification notification = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Notification.class);
			criteria.add(Restrictions.eq("id", notificationId));
			Object result = criteria.uniqueResult();
			notification = (Notification) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notification;
	}

	@Override
	public Set<Notification> getNotificationByTypeAndDate(long type,
			Date fromDate) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.lt("msgDate", fromDate));
		criteria.add(Restrictions.eq("type", type));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationBySubjectsAndDate(long subjectId,
			Date fromDate) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.lt("msgDate", fromDate));
		criteria.createAlias("subjects", "subject");
		criteria.add(Restrictions.eq("subject.id", subjectId));

		List<Notification> list = criteria.list();

		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationFromTeacherAndDate(long teacherId,
			Date fromDate) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.lt("msgDate", fromDate));
		criteria.createAlias("fromTeacher", "teacher");
		criteria.add(Restrictions.eq("teacher.id", teacherId));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationFromAdminAndDate(long adminId,
			Date fromDate) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.lt("msgDate", fromDate));
		criteria.createAlias("fromAdmin", "admin");
		criteria.add(Restrictions.eq("admin.id", adminId));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationByTypeAndDate(long type,
			Date fromDate, Date toDate) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.lt("msgDate", fromDate));
		criteria.add(Restrictions.gt("msgDate", toDate));
		criteria.add(Restrictions.eq("type", type));

		List<Notification> list = criteria.list();
		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

	@Override
	public Set<Notification> getNotificationBySubjectsAndDate(long subjectId,
			Date fromDate, Date toDate) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Notification.class);
		criteria.add(Restrictions.lt("msgDate", fromDate));
		criteria.add(Restrictions.gt("msgDate", toDate));
		criteria.createAlias("subjects", "subject");
		criteria.add(Restrictions.eq("subject.id", subjectId));

		List<Notification> list = criteria.list();

		Set<Notification> notifications = new HashSet<Notification>(list);

		tx.commit();
		session.close();
		return notifications;
	}

}