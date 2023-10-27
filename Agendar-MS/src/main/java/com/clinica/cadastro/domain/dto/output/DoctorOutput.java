package com.clinica.cadastro.domain.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorOutput {
	
	@Schema(example = "Mario")
	private String name;
	
	@Schema(example = "Cardiologista")
	private String specialty;
}
