package com.clinica.cadastro.domain.dto.input;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalAppointmentDTOInput {
	
	@NotNull
	private UserInput_id doctor;
	
	@NotNull
	private UserInput_id patient;
	
	@NotNull
	private Date time;
	
	@NotNull
	private ProcedureInput_id procedure;
	
}
