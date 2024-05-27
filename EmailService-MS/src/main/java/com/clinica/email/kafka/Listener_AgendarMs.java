package com.clinica.email.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.clinica.email.domain.dto.EmailDto;
import com.clinica.email.domain.service.EmailService;
import com.clinica.email.domain.service.EmailService.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class Listener_AgendarMs {

	private final EmailService service;
	
	String timestamp = LocalDateTime.now().toString();

	@KafkaListener(topics = "agendar-to-emailService", groupId = "group")
	public void listener(@Payload EmailDto emailDto) {

		log.info("[{}] - [Listener_AgendarMs] Email object received from the Agendar-MS service in the topic 'agendar-to-emailService' ", timestamp);
		
		var subText = "Aviso de consulta marcada para " + emailDto.getPatient_name();
		
		emailDto.convertDateToEmail();
	
		var message = Message.builder()
				.subText(subText)
				.addressee(emailDto.getPatient_email())
				.body("emailClinic-template.html")
				.variable("emailDto", emailDto).build();

		service.sendEmail(message);
	}

}
