package com.clinica.cadastro.domain.dto.output;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProcedureDTOOutput {
	
	private String name;
	
	private String description;
	
	private BigDecimal value;
	
}
