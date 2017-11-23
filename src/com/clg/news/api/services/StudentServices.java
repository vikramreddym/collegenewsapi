package com.clg.news.api.services;

import java.util.List;

import com.clg.news.api.model.Student;

public interface StudentServices {
	void add(Student student);

	void update(Student student);

	boolean uniqueUsername(String username);

	boolean uniqueEmail(String email);

	Student studentLogin(String username, String password);

	Student getStudentById(long subjectId);

	List<Student> getList();

	boolean delete(long studentId);
}
