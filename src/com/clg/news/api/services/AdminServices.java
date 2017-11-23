package com.clg.news.api.services;

import com.clg.news.api.model.Admin;

public interface AdminServices {
	Admin adminLogin(String username, String password);

	public void sendMail(String toMail, String bodyMsg, String subject,
			String fromMail, String smtpHost, String mailSmtpPort,
			String username, String password, String auth, String starttlsEnable);

	Admin getAdminById(long adminId);

	void updateAdmin(Admin admin);
}
