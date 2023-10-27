package com.clinica.medicos.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorKafkaDTOOutput {
	
	@Schema(example = "1")
	private Long id;

	@Schema(example = "Leonard")
	private String firstName;
	
	@Schema(example = "Fisioterapeuta")
	private String specialty;
}