package com.clg.news.api.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clg.news.api.dao.AdminDao;
import com.clg.news.api.model.Admin;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("adminServices")
public class AdminServicesImpl implements AdminServices {

	@Autowired
	AdminDao adminDao;

	@Override
	public Admin adminLogin(String username, String password) {
		return adminDao.adminLogin(username, password);
	}

	@Override
	public void sendMail(String toMail, String bodyMsg, String subject,
			String fromMail, String smtpHost, String mailSmtpPort,
			String username, String password, String auth, String starttlsEnable) {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", mailSmtpPort);
		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.starttls.enable", starttlsEnable);
		final String usernameFinal = username;
		final String passwordFinal = password;

		// obtaining the session
		Session emailSession = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(usernameFinal,
								passwordFinal);
					}
				});

		emailSession.setDebug(true);

		// creating the message
		Message emailMessage = new MimeMessage(emailSession);
		try {
			emailMessage.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toMail));
			emailMessage.setFrom(new InternetAddress(fromMail));
			emailMessage.setSubject(subject);
			emailMessage.setContent(bodyMsg, "text/html");
			Transport.send(emailMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Admin getAdminById(long adminId) {
		return adminDao.getAdminById(adminId);
	}

	@Override
	public void updateAdmin(Admin admin) {
		adminDao.updateAdmin(admin);

	}

}
