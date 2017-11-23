package com.clg.news.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.dao.TeacherDao;
import com.clg.news.api.model.Teacher;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("teacherServices")
public class TeacherServicesImpl implements TeacherServices {

	@Autowired
	TeacherDao teacherDao;

	@Override
	public void addTeacher(Teacher teacher) {
		teacherDao.addTeacher(teacher);
	}

	@Override
	public boolean uniqueUsername(String username) {
		return teacherDao.uniqueUsername(username);
	}

	@Override
	public boolean uniqueEmail(String email) {
		return teacherDao.uniqueEmail(email);
	}

	@Override
	public Teacher teacherLogin(String username, String password) {
		return teacherDao.teacherLogin(username, password);
	}

	@Override
	public Teacher getTeacherById(long teacherId) {
		return teacherDao.getTeacherById(teacherId);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		teacherDao.updateTeacher(teacher);

	}

	@Override
	public List<Teacher> getList() {
		return teacherDao.getList();
	}

	@Override
	public boolean delete(long teacherId) {
		return teacherDao.delete(teacherId);
	}

}
