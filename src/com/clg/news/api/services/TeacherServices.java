package com.clg.news.api.services;

import java.util.List;

import com.clg.news.api.model.Teacher;

public interface TeacherServices {

	void addTeacher(Teacher teacher);

	void updateTeacher(Teacher teacher);

	boolean uniqueUsername(String username);

	boolean uniqueEmail(String email);

	Teacher teacherLogin(String username, String password);

	Teacher getTeacherById(long teacherId);

	List<Teacher> getList();

	boolean delete(long teacherId);
}
