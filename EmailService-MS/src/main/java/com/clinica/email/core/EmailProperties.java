package com.clinica.email.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class EmailProperties {

	@Value("${mail.username}")
	private String sender;
}