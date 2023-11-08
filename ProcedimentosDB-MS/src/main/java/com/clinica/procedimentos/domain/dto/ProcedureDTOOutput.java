package com.clinica.procedimentos.domain.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcedureDTOOutput {
	
	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Lymphatic drainage")
	private String name;
	
	@Schema(example = "Lymphatic drainage in specific muscles")
	private String description;
	
	@Schema(example = "120.00")
	private BigDecimal value;
	
}
