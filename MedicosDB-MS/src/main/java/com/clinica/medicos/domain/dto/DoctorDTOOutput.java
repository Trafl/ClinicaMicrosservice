package com.clinica.medicos.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTOOutput {
	
	@Schema(example = "1")
	private Long id;

	@Schema(example = "Leonard Silva")
	private String name;
	
	@Schema(example = "CRM/RJ123456")
	private String crm;
	
	@Schema(example = "Physiotherapist")
	private String specialty;
}
