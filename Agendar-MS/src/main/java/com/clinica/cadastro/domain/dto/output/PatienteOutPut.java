package com.clinica.cadastro.domain.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatienteOutPut {
	
	@Schema(example = "Regia")
	private String name;
	
	@Schema(example = "regina@email.com")
	private String email;
	
	@Schema(example = "99999-9999")
	private String phone;

}
