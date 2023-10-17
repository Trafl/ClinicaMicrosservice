package com.clinica.pacientes.domain.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTOOutput {

	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Marcos")
	private String firstName;
	
	@Schema(example = "Antonio")
	private String lastName;
	
	@Schema(example = "1980-05-15T00:00:00Z")
	private Date birthday;
	
	@Schema(example = "Masculino")
	private String gender;

	@Schema(example = "marcos@email.com")
	private String email;
	
	@Schema(example = "02499999-9999")
	private String phone;
}
