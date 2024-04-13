package com.clinica.pacientes.domain.dto;

import java.time.LocalDate;

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
	private String name;
	
	@NotNull
	@Schema(example = "1980-05-15T00:00:00Z")
	private LocalDate birthday;
	
	@NotBlank
	@Schema(example = "Masculine")
	private String gender;

	@NotBlank
	@Email
	@Schema(example = "marcos@email.com")
	private String email;
	
	@NotBlank
	@Schema(example = "02499999-9999")
	private String phone;

	public PatientDTOInput(@NotBlank String name, @NotNull LocalDate birthday, @NotBlank String gender,
			@NotBlank @Email String email, @NotBlank String phone) {
		this.name = name;
		this.birthday = birthday;
		this.gender = gender;
		this.email = email;
		this.phone = phone;
	}
	
	
}
