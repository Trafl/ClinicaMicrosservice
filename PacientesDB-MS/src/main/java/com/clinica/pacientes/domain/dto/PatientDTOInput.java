package com.clinica.pacientes.domain.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTOInput {

	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotNull
	private Date birthday;
	
	@NotBlank
	private String gender;

	@NotBlank
	private String email;
	
	@NotBlank
	private String phone;
}
