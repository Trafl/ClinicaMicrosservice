package com.clinica.email.domain.service;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

public interface EmailService {

void sendEmail(Message message);
	
	@Getter
	@Builder
	public class Message {
		
		private String addressee;
		
		private String subText;
		
		private String body;
		
		@Singular("variable")
		private Map<String, Object> variables;
		
	} 
}
