package com.clg.news.api.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.model.Subject;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SubjectDaoImpl implements SubjectDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public void addSubject(Subject subject) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(subject);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Subject> getSubjects() {
		List<Subject> list = new ArrayList<Subject>();
		try {
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Subject.class);
			list = c.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Subject getSubjectById(long subjectId) {
		Session session;
		Subject subject = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Subject.class);
			criteria.add(Restrictions.eq("id", subjectId));
			Object result = criteria.uniqueResult();
			subject = (Subject) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subject;
	}

	@Override
	public boolean delete(long subjectId) {
		Session session;
		Subject subject = null;
		boolean flag = false;
		Transaction tx;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			subject = (Subject) session.load(Subject.class, subjectId);
			if (subject != null) {
				session.delete(subject);
				flag = true;
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.out.println("falseeeeeeeeeeeeee");
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean uniqueSubject(String name) {
		boolean flag = false;
		try {
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Subject.class);
			c.add(Restrictions.eq("name", name));
			Object o = c.uniqueResult();
			if (o == null) {
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public void updateSubject(Subject subject) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(subject);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
