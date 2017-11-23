package com.clg.news.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.model.Admin;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AdminDaoImpl implements AdminDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction tx = null;

	@Override
	public Admin adminLogin(String username, String password) {
		Session session;
		Admin admin = null;
		boolean flag = false;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Admin.class);
			criteria.add(Restrictions.eq("username", username));
			criteria.add(Restrictions.eq("password", password));
			Object result = criteria.uniqueResult();
			admin = (Admin) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (admin != null && admin.getUsername().equals(username)
				&& admin.getPassword().equals(password)) {
			return admin;
		} else {
			return null;
		}

	}

	@Override
	public Admin getAdminById(long adminId) {
		Session session;
		Admin admin = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Admin.class);
			criteria.add(Restrictions.eq("id", adminId));
			Object result = criteria.uniqueResult();
			admin = (Admin) result;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}

	@Override
	public void updateAdmin(Admin admin) {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(admin);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
