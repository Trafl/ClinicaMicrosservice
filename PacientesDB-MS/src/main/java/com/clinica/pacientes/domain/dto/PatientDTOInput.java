package com.clinica.pacientes.domain.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTOInput {

	@NotBlank
	@Schema(example = "Marcos")
	private String firstName;
	
	@NotBlank
	@Schema(example = "Antonio")
	private String lastName;
	
	@NotNull
	@Schema(example = "1980-05-15T00:00:00Z")
	private Date birthday;
	
	@NotBlank
	@Schema(example = "Masculino")
	private String gender;

	@NotBlank
	@Email
	@Schema(example = "marcos@email.com")
	private String email;
	
	@NotBlank
	@Schema(example = "02499999-9999")
	private String phone;
}
