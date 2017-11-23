package com.clg.news.api.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.model.Student;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StudentDaoImpl implements StudentDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public void add(Student student) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(student);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean uniqueUsername(String username) {
		boolean flag = false;
		try {
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Student.class);
			c.add(Restrictions.eq("username", username));
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
	public boolean uniqueEmail(String email) {
		boolean flag = false;
		try {
			session = sessionFactory.openSession();
			Criteria c = session.createCriteria(Student.class);
			c.add(Restrictions.eq("email", email));
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
	public Student studentLogin(String username, String password) {
		Session session;
		Student student = null;

		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Student.class);
			criteria.add(Restrictions.eq("username", username));
			criteria.add(Restrictions.eq("password", password));
			Object result = criteria.uniqueResult();
			student = (Student) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (student != null && student.getUsername().equals(username)
				&& student.getPassword().equals(password)) {
			return student;
		} else {
			return null;
		}
	}

	@Override
	public Student getStudentById(long subjectId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Student student = null;
		try {
			Object result = session.load(Student.class, subjectId);
			student = (Student) result;
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}

	@Override
	public void update(Student student) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(student);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Student> getList() {
		Session session;
		List<Student> list = null;

		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Student.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			list = criteria.list();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean delete(long studentId) {
		Session session;
		Student student = null;
		boolean flag = false;
		Transaction tx;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			student = (Student) session.load(Student.class, studentId);
			if (student != null) {
				session.delete(student);
				flag = true;
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
