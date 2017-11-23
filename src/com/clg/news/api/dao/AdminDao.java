package com.clg.news.api.dao;

import com.clg.news.api.model.Admin;

public interface AdminDao {

	Admin adminLogin(String username, String password);

	Admin getAdminById(long adminId);

	void updateAdmin(Admin admin);

}
