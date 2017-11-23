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

import com.clg.news.api.model.Teacher;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TeacherDaoImpl implements TeacherDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public void addTeacher(Teacher teacher) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			session.save(teacher);

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
			Criteria c = session.createCriteria(Teacher.class);
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
			Criteria c = session.createCriteria(Teacher.class);
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
	public Teacher teacherLogin(String username, String password) {
		Session session;
		Teacher teacher = null;

		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Teacher.class);
			criteria.add(Restrictions.eq("username", username));
			criteria.add(Restrictions.eq("password", password));
			Object result = criteria.uniqueResult();
			teacher = (Teacher) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (teacher != null && teacher.getUsername().equals(username)
				&& teacher.getPassword().equals(password)) {
			return teacher;
		} else {
			return null;
		}

	}

	@Override
	public Teacher getTeacherById(long teacherId) {
		Session session;
		Teacher teacher = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Teacher.class);
			criteria.add(Restrictions.eq("id", teacherId));
			Object result = criteria.uniqueResult();
			teacher = (Teacher) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teacher;
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			session.update(teacher);

			tx.commit();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Teacher> getList() {
		Session session;
		List<Teacher> list = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Teacher.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			list = criteria.list();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean delete(long teacherId) {
		Session session;
		Teacher teacher = null;
		boolean flag = false;
		Transaction tx;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			teacher = (Teacher) session.load(Teacher.class, teacherId);
			if (teacher != null) {
				session.delete(teacher);
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
