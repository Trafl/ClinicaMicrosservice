package com.clinica.evolution.domain.dto.kafka;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisDTOFromKafka {

	private Long id;
	
	private Long doctor_id;
	
	private Long patient_id;
	
	private Long procedure_id;
	
	private LocalDateTime date;
}
