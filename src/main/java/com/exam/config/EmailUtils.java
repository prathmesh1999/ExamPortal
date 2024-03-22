package com.exam.config;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {
	
	@Autowired
    private JavaMailSender emailSender;
	
	public void forgotMail(String to, String subject, String otp) throws  MessagingException {
		
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setFrom("prathah2223@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<p><b> Change Your Password</b><br><b>Email: </b> "
				+to+ "<br><b>Below is the change password link: </b>"
				+
				"<br><a href=\"" +otp+   "\">Change Password</a></p>";
		message.setContent(htmlMsg,"text/html");
		emailSender.send(message);
	}

}
