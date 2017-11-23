package com.clg.news.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clg.news.api.beans.subject.SubjectRequest;
import com.clg.news.api.beans.subject.SubjectResponse;
import com.clg.news.api.model.Admin;
import com.clg.news.api.model.Subject;
import com.clg.news.api.model.Teacher;
import com.clg.news.api.services.AdminServices;
import com.clg.news.api.services.SubjectServices;
import com.clg.news.api.services.TeacherServices;

@Controller
@RequestMapping("/subject")
public class SubjectController {

	@Autowired
	SubjectServices subjectServices;

	@Autowired
	AdminServices adminServices;

	@Autowired
	TeacherServices teacherServices;

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody SubjectResponse addSubject(
			@RequestBody SubjectRequest subjectRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Admin admin = adminServices.getAdminById(subjectRequest.getAdminId());
		SubjectResponse subjectResponse = new SubjectResponse();

		if (admin != null
				&& admin.getSessionId().equals(subjectRequest.getSessionId())) {

			if (!subjectServices.uniqueSubject(subjectRequest.getName())) {
				subjectResponse
						.setMsg("Subject not added. Subject name not unique.");
				return subjectResponse;
			}
			Subject subject = new Subject();
			subject.setName(subjectRequest.getName());
			subjectServices.addSubject(subject);
			subjectResponse.setMsg("Subject added");
		} else {
			subjectResponse.setMsg("Subject not added");
		}
		return subjectResponse;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody SubjectResponse updateSubject(
			@RequestBody SubjectRequest subjectRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Admin admin = adminServices.getAdminById(subjectRequest.getAdminId());
		SubjectResponse subjectResponse = new SubjectResponse();

		if (admin != null
				&& admin.getSessionId().equals(subjectRequest.getSessionId())) {

			if (!subjectServices.uniqueSubject(subjectRequest.getName())) {
				subjectResponse
						.setMsg("Subject not added. Subject name not unique.");
				return subjectResponse;
			}
			Subject subject = subjectServices.getSubjectById(subjectRequest
					.getSubjectId());
			subject.setName(subjectRequest.getName());
			subjectServices.updateSubject(subject);
			subjectResponse.setMsg("Subject updated");
		} else {
			subjectResponse.setMsg("Subject not updated");
		}
		return subjectResponse;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<Subject> list(
			@RequestBody SubjectRequest subjectRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = null;
		Teacher teacher = null;
		if (subjectRequest.getAdminId() != null) {
			admin = adminServices.getAdminById(subjectRequest.getAdminId());
		} else if (subjectRequest.getTeacherId() != null) {
			teacher = teacherServices.getTeacherById(subjectRequest
					.getTeacherId());
		}

		List<Subject> list = new ArrayList<Subject>();
		if (admin != null || teacher != null) {
			list = subjectServices.getSubjects();
		}
		return list;
	}

	@RequestMapping(value = "/getSubject", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody Subject getSubject(
			@RequestBody SubjectRequest subjectRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Admin admin = null;

		if (subjectRequest.getAdminId() != null) {
			admin = adminServices.getAdminById(subjectRequest.getAdminId());
		}

		Subject subject = null;
		if (admin != null) {
			subject = subjectServices.getSubjectById(subjectRequest
					.getSubjectId());
		}
		return subject;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "content-type=application/json", produces = "application/json")
	public @ResponseBody List<Map<String, String>> delete(
			@RequestBody SubjectRequest subjectRequest,
			HttpServletRequest request, HttpServletResponse response) {

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		Admin admin = adminServices.getAdminById(subjectRequest.getAdminId());

		if (admin != null
				&& admin.getSessionId().equals(subjectRequest.getSessionId())) {
			for (Long subjectId : subjectRequest.getSubjectIds()) {
				Map<String, String> map = new HashMap<String, String>();
				try {
					if (subjectServices.delete(subjectId)) {
						map.put("result", "Subject Id " + subjectId
								+ " is deleted");
					} else {
						map.put("result",
								"Subject Id "
										+ subjectId
										+ " is not deleted.Notification attached for subject.");
					}
				} catch (Exception e) {
					System.out
							.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
					map.put("result",
							"Subject Id "
									+ subjectId
									+ " is not deleted. Notification attached for subject.");
					result.add(map);

				}
				result.add(map);
			}
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("result", "session expire");
			result.add(map);
		}
		return result;
	}
}
