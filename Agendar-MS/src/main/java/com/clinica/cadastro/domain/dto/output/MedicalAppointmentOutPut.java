package com.clinica.cadastro.domain.dto.output;

import java.util.Date;

import lombok.Data;

@Data
public class MedicalAppointmentOutPut {
	
	private Long id;
	
	private UserDTOOutput doctor;
	
	private UserDTOOutput patient;
	
	private Date time;
	
	private ProcedureDTOOutput procedure;
	
	private Boolean open = true;
	
}
