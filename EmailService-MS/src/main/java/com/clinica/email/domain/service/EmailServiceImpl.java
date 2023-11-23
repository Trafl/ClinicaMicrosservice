package com.clinica.email.domain.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.clinica.email.core.EmailProperties;
import com.clinica.email.domain.exception.MailException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;
	
	private final Configuration configuration;
	
	private final EmailProperties properties;
	
	public void sendEmail(Message message) {	
		try {
			MimeMessage mimeMessage = createMimeMessage(message);
			
			mailSender.send(mimeMessage);
		
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	protected MimeMessage createMimeMessage(Message message) throws MessagingException {

		String body = createTemplate(message);
							
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"UTF-8");
		
		helper.setFrom(properties.getSender());
		helper.setTo(message.getAddressee());
		helper.setSubject(message.getSubText());
		helper.setText(body, true);
		
		return mimeMessage;
	}
	
	protected String createTemplate(Message message)  {
		try {
			Template template = configuration.getTemplate(message.getBody());
		
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
		
		} catch (Exception e) {
			throw new MailException("Unable to create an email template.", e);
		}
	}
}