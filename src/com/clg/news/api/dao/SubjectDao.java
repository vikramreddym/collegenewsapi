package com.clg.news.api.dao;

import java.util.List;

import com.clg.news.api.model.Subject;

public interface SubjectDao {

	void addSubject(Subject subject);

	void updateSubject(Subject subject);

	List<Subject> getSubjects();

	Subject getSubjectById(long subjectId);

	boolean delete(long subjectId);

	boolean uniqueSubject(String name);
}
