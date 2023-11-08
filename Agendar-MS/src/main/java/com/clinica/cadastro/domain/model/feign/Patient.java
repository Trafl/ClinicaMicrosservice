package com.clinica.cadastro.domain.model.feign;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Patient {

	private Long patient_id;
	
	private String patient_name;

	private String patient_phone;
	
	private String patient_email;
}
