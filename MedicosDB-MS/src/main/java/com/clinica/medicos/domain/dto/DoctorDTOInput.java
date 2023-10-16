package com.clinica.medicos.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTOInput {
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;

	@NotBlank
	private String cpf;
	
	@NotBlank
	private String crm;
	
	@NotBlank
	private String specialty;
}
