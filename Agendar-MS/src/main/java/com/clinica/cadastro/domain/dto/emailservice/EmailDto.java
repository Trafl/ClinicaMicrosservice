package com.clinica.cadastro.domain.dto.emailservice;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String patient_email;
	
	private String patient_name;
	
	private String doctor_name;
	
	private LocalDateTime date;
}