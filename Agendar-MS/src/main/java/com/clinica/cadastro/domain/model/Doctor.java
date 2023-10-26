package com.clinica.cadastro.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Doctor {

	private Long doctor_id;
	
	private String doctor_name;
	
	private String doctor_specialty;
}
