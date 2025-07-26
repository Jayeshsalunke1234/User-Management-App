package com.user.portal.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendMail(String to, String subject ,String body) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper= new MimeMessageHelper(mimeMessage);
		
		try {
		    helper.setFrom("jsalunke1398@gmail.com"); // ✅ ADD THIS LINE

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body,true);
			mailSender.send(mimeMessage);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return false;
	}

}
