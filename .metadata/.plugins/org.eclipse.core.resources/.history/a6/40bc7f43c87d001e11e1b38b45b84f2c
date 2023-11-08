package com.clinica.procedimentos.domain.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProcedureDTOInput {
	
	@NotBlank
	@Schema(example = "RPG")
	private String name;
	
	@NotBlank
	@Schema(example = "Reeducação Postural Global (RPG)")
	private String description;
	
	@NotNull
	@PositiveOrZero
	@Schema(example = "120.00")
	private BigDecimal value;
	
}
