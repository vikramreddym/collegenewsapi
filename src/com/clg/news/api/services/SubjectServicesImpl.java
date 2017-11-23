package com.clg.news.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.dao.SubjectDao;
import com.clg.news.api.model.Subject;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("subjectServices")
public class SubjectServicesImpl implements SubjectServices {

	@Autowired
	SubjectDao subjectDao;

	@Override
	public void addSubject(Subject subject) {
		subjectDao.addSubject(subject);
	}

	@Override
	public List<Subject> getSubjects() {
		return subjectDao.getSubjects();
	}

	@Override
	public Subject getSubjectById(long subjectId) {
		return subjectDao.getSubjectById(subjectId);
	}

	@Override
	public boolean delete(long subjectId) {
		return subjectDao.delete(subjectId);
	}

	@Override
	public boolean uniqueSubject(String name) {
		return subjectDao.uniqueSubject(name);
	}

	@Override
	public void updateSubject(Subject subject) {
		subjectDao.updateSubject(subject);

	}

}
