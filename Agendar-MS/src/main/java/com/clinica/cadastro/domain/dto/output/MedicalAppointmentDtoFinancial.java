package com.clinica.cadastro.domain.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalAppointmentDtoFinancial {

	private Long id;
	
	private Long doctor_id;

	private Long patient_id;
	
	private Long procedure_id;
	
	private BigDecimal value;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime finishedAppointment;
	
	
}
