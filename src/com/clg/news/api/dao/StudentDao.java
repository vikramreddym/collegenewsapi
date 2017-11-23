package com.clg.news.api.dao;

import java.util.List;

import com.clg.news.api.model.Student;

public interface StudentDao {

	void add(Student student);

	void update(Student student);

	boolean uniqueUsername(String username);

	boolean uniqueEmail(String email);

	Student studentLogin(String username, String password);

	Student getStudentById(long studentId);

	List<Student> getList();

	boolean delete(long studentId);
}
