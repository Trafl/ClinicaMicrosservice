package com.clinica.cadastro.domain.dto.output;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcedureDTOOutput {
	
	@Schema(example = "Massagem linfatica")
	private String name;
	
	@Schema(example = "180.00")
	private BigDecimal value;
	
}
