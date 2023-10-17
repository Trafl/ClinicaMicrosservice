package com.clinica.medicos.domain.dto;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTOInput {
	
	@NotBlank
	@Schema(example = "Leonard")
	private String firstName;
	
	@NotBlank
	@Schema(example = "Silva")
	private String lastName;

	@NotBlank
	@Email
	@Schema(example = "silva@email.com")
	private String email;
	
	@NotBlank
	@CPF
	@Schema(example = "123.456.789-09")
	private String cpf;
	
	@NotBlank
	@Size(min = 1, max = 13)
	@Schema(example = "CRM/RJ123456")
	private String crm;
	
	@NotBlank
	@Schema(example = "Fisioterapeuta")
	private String specialty;
}
