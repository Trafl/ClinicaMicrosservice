package com.clinica.cadastro.domain.dto.input;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalAppointmentDTOInput {
	
	@NotNull
	@Schema(example = "1")
	private Long doctorId;
	
	@NotNull
	@Schema(example = "1")
	private Long patientId;
	
	@NotNull
	@Schema(example = "1")
	private Long procedureId;
	
	@NotNull
	@Schema(example = "2023-10-27T17:39:58.695Z")
	private LocalDateTime date;
	
}
