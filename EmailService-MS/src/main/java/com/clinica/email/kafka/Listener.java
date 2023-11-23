package com.clinica.email.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.clinica.email.domain.dto.EmailDto;
import com.clinica.email.domain.service.EmailService;
import com.clinica.email.domain.service.EmailService.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Listener {

	private final EmailService service;

	@KafkaListener(topics = "agendar-to-emailService", groupId = "group")
	public void receber(@Payload EmailDto emailDto) {

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
