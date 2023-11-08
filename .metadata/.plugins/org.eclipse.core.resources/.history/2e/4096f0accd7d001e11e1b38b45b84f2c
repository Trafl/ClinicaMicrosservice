package com.clinica.cadastro.domain.dto.output;

import java.time.OffsetDateTime;

import com.clinica.cadastro.domain.model.AppointmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalAppointmentOutPut {
	
	private DoctorOutput doctor;
	
	private PatienteOutPut patient;
	
	private ProcedureDTOOutput procedure;
	
	@Schema(example = "2023-10-27T17:39:58.695Z")
	private OffsetDateTime date;
	
	@Schema(example = "CREATED")
	private AppointmentStatus status;
}
