package com.clinica.medicos.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorKafkaDTOOutput {
	
	private Long id;

	private String firstName;
	
	private String specialty;
}
