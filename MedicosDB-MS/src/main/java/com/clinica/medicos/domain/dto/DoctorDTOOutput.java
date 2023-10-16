package com.clinica.medicos.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTOOutput {
	
	private Long id;

	private String firstName;
	
	private String lastName;
	
	private String crm;
	
	private String specialty;
}
