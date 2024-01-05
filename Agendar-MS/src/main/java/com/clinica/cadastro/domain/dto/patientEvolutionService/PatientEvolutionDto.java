package com.clinica.cadastro.domain.dto.patientEvolutionService;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatientEvolutionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long doctor_id;
	
	private Long patient_id;
	
	private Long procedure_id;
	
	private LocalDateTime date;
	
}
