package com.clg.news.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.clg.news.api.dao.StudentDao;
import com.clg.news.api.model.Student;

public class StudentServicesImpl implements StudentServices {

	@Autowired
	StudentDao studentDao;

	@Override
	public void add(Student student) {
		studentDao.add(student);
	}

	@Override
	public boolean uniqueUsername(String username) {
		return studentDao.uniqueUsername(username);
	}

	@Override
	public boolean uniqueEmail(String email) {
		return studentDao.uniqueEmail(email);
	}

	@Override
	public Student studentLogin(String username, String password) {
		return studentDao.studentLogin(username, password);
	}

	@Override
	public Student getStudentById(long subjectId) {
		return studentDao.getStudentById(subjectId);
	}

	@Override
	public void update(Student student) {
		studentDao.update(student);

	}

	@Override
	public List<Student> getList() {
		return studentDao.getList();
	}

	@Override
	public boolean delete(long studentId) {
		return studentDao.delete(studentId);
	}

}
